package com.example.hifa;

import static java.lang.Thread.yield;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ForegroundServiceStartNotAllowedException;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

public class BluetoothLeService extends Service {
    public final static String BLE_SERVICE = "BLE_SERVICE";
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_GATT_TX_SERVICE_DATA_AVAILABLE =
            "ACTION_GATT_TX_SERVICE_DATA_AVAILABLE";

    //The following need to be changed if we are looking for particular gatt services:
    public final static String GATT_NORDIC_DATA_SERVICE =
            "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public final static String GATT_TX_CHARACTERISTIC =
            "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public final static String GATT_NORDIC_UUID_SERVICE =
            "6e4073a1-b5a3-f393-e0a9-e50e24dcca9e";
    public final static String GATT_DEVICE_UUID_CHARACTERISTIC =
            "6e4003a1-b5a3-f393-e0a9-e50e24dcca9e";

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTED = 2;

    public static final String TAG = "BluetoothLeService";
    public static final String CHANNEL_ID = "BLE_Channel";
    NotificationManager notificationManager;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic EmergencySignalCharacteristic;
    private Binder binder = new BluetoothLeService.LocalBinder();
    private int connectionState;
    private long deviceID = 0;
    private HomeActivity handleSmsActivity;

    private boolean waitOnReadCharacteristic = true;

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
                waitOnReadCharacteristic = true; //When disconnected, read ID again.
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.w(TAG, "Discovered Services!");
                try {
                    setGattCharacteristicListeners(bluetoothGatt.getServices());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                setGattCharacteristicListenersTEST(bluetoothGatt.getServices());
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

            //Profiles for broadcasting the information received.
            if (characteristic.getUuid().toString().equals(GATT_TX_CHARACTERISTIC)) {
                broadcastTXDataReceived(ACTION_GATT_TX_SERVICE_DATA_AVAILABLE, characteristic);
                handleEmergencySignal(characteristic);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] value, int status) {
            Log.d("CHAR", "READING ID FUNCTION CALL DETECTED");
            if (bluetoothGattCharacteristic.getUuid().toString().equals(GATT_DEVICE_UUID_CHARACTERISTIC)) {
                extractDeviceUUID(value);
            }

            waitOnReadCharacteristic = false;
            setCharacteristicNotification(EmergencySignalCharacteristic, true);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Implement UI notification for the user to know of the BLE's connection.
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

    /**
     * Starts the service as a foreground service so it can continue to run even if app goes down.
     */
    public void startForeground() {
        int bluetoothPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
        if (bluetoothPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf();
            return;
        }

        try {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "BLE_Device_Channel", NotificationManager.IMPORTANCE_HIGH); //TODO: FIX NOTIFICATIONS!
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Notification notification =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .build();
            int type = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                type = ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
            }
            startForeground(100, notification);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    e instanceof ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g started from bg)
            }
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (bluetoothGatt == null) return null;
        return bluetoothGatt.getServices();
    }

    //TODO: To optimize the speed of this method, can purely scan for the wanted services and characteristics.
    @SuppressLint("MissingPermission")
    private void setGattCharacteristicListeners(List<BluetoothGattService> gattServices) throws InterruptedException {
        if (gattServices == null) {
            return;
        }
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equals(GATT_NORDIC_DATA_SERVICE)) {
                List<BluetoothGattCharacteristic> nordicCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic nordicCharacteristic : nordicCharacteristics) {
                    if (nordicCharacteristic.getUuid().toString().equals(GATT_TX_CHARACTERISTIC)) {
//                        setCharacteristicNotification(nordicCharacteristic, true);
                        EmergencySignalCharacteristic = nordicCharacteristic;
                    }
                }
            } else if (gattService.getUuid().toString().equals(GATT_NORDIC_UUID_SERVICE)) {
                List<BluetoothGattCharacteristic> nordicCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic nordicCharacteristic : nordicCharacteristics) {
                    if (nordicCharacteristic.getUuid().toString().equals(GATT_DEVICE_UUID_CHARACTERISTIC) && this.deviceID == 0) {
                        while (!bluetoothGatt.readCharacteristic(nordicCharacteristic)) {
                            ;
                        }
                    }
                }
            }
        }

        //After reading characteristic, subscribe:
//        setCharacteristicNotification(EmergencySignalCharacteristic, true);
    }

    private void setGattCharacteristicListenersTEST(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }
        // Loops through available GATT Services.
        BluetoothGattCharacteristic nordicTXCharacteristic;
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equals(GATT_NORDIC_DATA_SERVICE) || gattService.getUuid().toString().equals(GATT_NORDIC_UUID_SERVICE)) {
                List<BluetoothGattCharacteristic> nordicCharacteristics = gattService.getCharacteristics();
                String service_name = String.valueOf(gattService.getUuid());
                for (BluetoothGattCharacteristic nordicCharacteristic : nordicCharacteristics) {
                    if (nordicCharacteristic.getUuid().toString().equals(GATT_TX_CHARACTERISTIC) || nordicCharacteristic.getUuid().toString().equals(GATT_DEVICE_UUID_CHARACTERISTIC)) {
                        byte[] data = nordicCharacteristic.getValue();
                        for (BluetoothGattDescriptor bluetoothGattDescriptor : nordicCharacteristic.getDescriptors()) {
                            byte[] desc_data = bluetoothGattDescriptor.getValue();
                        }
                        setCharacteristicNotification(nordicCharacteristic, true);
                    }
                }
            }
        }

    }

    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
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

        //Set Characteristics to be listening to:
        boolean status = bluetoothGatt.setCharacteristicNotification(characteristic, enabled);


        //Setting the characteristic notifiers to be notified of new data:
        if (characteristic.getUuid().toString().equals(GATT_TX_CHARACTERISTIC)) {
            BluetoothGattDescriptor notificationDescriptor = null;
            for (BluetoothGattDescriptor bluetoothGattDescriptor : characteristic.getDescriptors()) {
                //Only one service to watch out for:
                notificationDescriptor = bluetoothGattDescriptor;
            }

            notificationDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(notificationDescriptor);
        }

        if (!status) {
            Log.d(BLE_SERVICE, "Error with setting status of characteristic!!!");
        }
    }

//    private void extractDeviceUUID(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//        byte[] data = bluetoothGattCharacteristic.getValue();
//
////        //Using String Builder to interpret byte data:
////        StringBuilder stringBuilder = new StringBuilder(data.length);
////        for (byte byteChar : data) {
////            if (byteChar == 0) {
////                break;
////            }
////            stringBuilder.append((char) (byteChar));
////        }
////
////        String deviceID = stringBuilder.toString();
//
//        this.deviceID = deviceID;
//    }

    private void extractDeviceUUID(byte[] byteUUID) {
        Log.d(TAG, "extractDeviceUUID: EXTRACTED UUID");
        long value = 0;
        for (byte b : byteUUID) {
            value = (value << 8) + (b);
        }

        this.deviceID = value;
    }

    private void handleEmergencySignal(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] data = bluetoothGattCharacteristic.getValue();

        Log.d(TAG, "handleEmergencySignal: HAS BEEN CALLED!");

        //Using String Builder to interpret byte data:
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            if (byteChar == 0) {
                break;
            }
            stringBuilder.append((char) (byteChar));
        }

        String signalMessage = stringBuilder.toString();

        //TODO: Insert logic to make the call based on the signal received.
        handleSmsActivity.sendSMSMessage();
    }

    //Interprocess communication:
    private void broadcastUpdate(final String broadcastMessage) {
        final Intent intent = new Intent(broadcastMessage);
        sendBroadcast(intent);
    }

    //TODO: Make sure this broadcast is picked up by the user so they can react to it!.
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

    public void setHandleSmsActivity(HomeActivity homeActivity) {
        this.handleSmsActivity = homeActivity;
    }
    public Long getDeviceID() {
        return deviceID;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class LocalBinder extends Binder {

        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }
}
