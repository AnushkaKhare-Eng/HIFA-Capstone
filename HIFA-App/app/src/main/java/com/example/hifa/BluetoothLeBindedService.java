package com.example.hifa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.List;

public class BluetoothLeBindedService extends Service {
    public final static String BLE_SERVICE = "BLE_SERVICE";
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_GATT_TX_SERVICE_DATA_AVAILABLE =
            "ACTION_GATT_TX_SERVICE_DATA_AVAILABLE";

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTED = 2;

    public static final String TAG = "BluetoothLeService";
    private Binder binder = new LocalBinder();
    BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private int connectionState;

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                connectionState = STATE_CONNECTED;
                broadcastUpdate(ACTION_GATT_CONNECTED);
                // Attempts to discover services after successful connection.
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
                connectionState = STATE_DISCONNECTED;
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.w(TAG, "Discovered Services!");
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        //TODO: If we need to read from various characteristics this needs to be broken into different steps:
        @Override
        public void onCharacteristicChanged(
                BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic
        ) {
            Log.d("CHAR", "DATA CHANGE DETECTED");
            broadcastTXDataReceived(ACTION_GATT_TX_SERVICE_DATA_AVAILABLE, characteristic);
        }
    };

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothGatt not initialized");
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        boolean status = bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        //Setting the characteristic notifier to be notified of new data:
        BluetoothGattDescriptor notificationDescriptor = null;
        for (BluetoothGattDescriptor bluetoothGattDescriptor : characteristic.getDescriptors()) {
            //Only one service to watch out for:
            notificationDescriptor = bluetoothGattDescriptor;
        }

        notificationDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(notificationDescriptor);

        if (!status) {
            Log.d(BLE_SERVICE, "Error with setting status of characteristic!!!");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class LocalBinder extends Binder {

        public BluetoothLeBindedService getService() {
            return BluetoothLeBindedService.this;
        }
    }

    public boolean initialize() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.e(TAG, "STARTED BLE SERVICE!!!");
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    public boolean connect(final String bluetoothDeviceAddress) {
        if (bluetoothDeviceAddress == null) {
            Log.w(TAG, "No actual device found!");
            return false;
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }

            //Fetch the actual device GATT connection:
            final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress);
            bluetoothGatt = device.connectGatt(this, true, bluetoothGattCallback);
            Log.w(TAG, "Connected to device!");
            return true;
        } catch (IllegalArgumentException exception) {
            Log.w(TAG, "Device not found with provided address.");
            return false;
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (bluetoothGatt == null) return null;
        return bluetoothGatt.getServices();
    }

    //Interprocess communication:
    private void broadcastUpdate(final String broadcastMessage) {
        final Intent intent = new Intent(broadcastMessage);
        sendBroadcast(intent);
    }

    //Process function for extracting TX data from the BLE stack on the NRF52840:
    private void broadcastTXDataReceived(final String broadcastMessage, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        String signalMessage = Arrays.toString(bluetoothGattCharacteristic.getValue());

        //
        List<BluetoothGattDescriptor> descriptors = bluetoothGattCharacteristic.getDescriptors();

        for (BluetoothGattDescriptor descriptor : descriptors) {
            String messageReceived = String.valueOf(descriptor.getValue());
            Log.d("DESCRIPTOR_DATA:", messageReceived);
        }

        final Intent intent = new Intent(broadcastMessage);
        sendBroadcast(intent);
    }
}
