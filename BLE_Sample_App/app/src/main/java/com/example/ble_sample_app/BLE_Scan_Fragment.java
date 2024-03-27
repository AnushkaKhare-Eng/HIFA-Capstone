package com.example.ble_sample_app;

import static com.example.ble_sample_app.BluetoothLeService.ACTION_GATT_CONNECTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ble_sample_app.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
public class BLE_Scan_Fragment extends Fragment {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private static String BLE_ERROR_STRING = "NO BLE FEATURES ON DEVICE!";
    private static String BLE_CONNECT_ERROR_STRING = "BLE";
    private static String DEBUG_SERVICES_REGISTRY = "SERVICE_TESTING";
    public final static String GATT_NORDIC_SERVICE =
            "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public final static String GATT_TX_CHAR =
            "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    private FragmentFirstBinding binding;
    private RecyclerView ble_recycler_view;
    private BLE_Device_Disp_Adapter ble_recycler_view_adapter;
    private ArrayList<BluetoothDevice> test_bles;
    private ArrayList<String> testStrings;

    //Bluetooth Scanning Variables:
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler;

    BluetoothLeService bluetoothLeService;
    private BluetoothDevice bluetoothDeviceToConnect;
    private boolean connected;


    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                Log.d("DEBUGGING", "CONNECTED DEVICE!");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
                Log.d("DEBUGGING", "DISCONNECTED DEVICE!");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d("DEBUGGING", "Services are being iterated over:!");
                bluetoothLeService.getSupportedGattServices();
                displayGattServices(bluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_GATT_TX_SERVICE_DATA_AVAILABLE.equals(action)) {
                Log.d("DEBUGGING", "Notified of change to the TX Register!");
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder bluetoothLeService) {
            BLE_Scan_Fragment.this.bluetoothLeService = ((BluetoothLeService.LocalBinder) bluetoothLeService).getService();
            if (BLE_Scan_Fragment.this.bluetoothLeService != null) {
                if (!BLE_Scan_Fragment.this.bluetoothLeService.initialize()) {
                    if (!BLE_Scan_Fragment.this.bluetoothLeService.initialize()) {
                        Log.e(BLE_CONNECT_ERROR_STRING, "Unable to initialize Bluetooth");
                    }
                }
                // perform device connection
                BLE_Scan_Fragment.this.bluetoothLeService.connect(bluetoothDeviceToConnect.getAddress());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bluetoothLeService = null;
        }
    };

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    // Demonstrates how to iterate through the supported GATT
    // Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the
    // ExpandableListView on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            Log.d(DEBUG_SERVICES_REGISTRY, "WE HAVE NO SERVICES");
        }
        // Loops through available GATT Services.
        BluetoothGattCharacteristic nordicTXCharacteristic;
        for (BluetoothGattService gattService : gattServices) {
            Log.d(DEBUG_SERVICES_REGISTRY, gattService.getUuid().toString());
            if (gattService.getUuid().toString().equals(GATT_NORDIC_SERVICE)) {
                List<BluetoothGattCharacteristic> nordicServices = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic nordicService : nordicServices) {
                    Log.d(DEBUG_SERVICES_REGISTRY, nordicService.getUuid().toString());
                    if (nordicService.getUuid().toString().equals(GATT_TX_CHAR)) {
                        bluetoothLeService.setCharacteristicNotification(nordicService, true);
                        //CHECKING FOR DATA IN SERVICE:
                        for (BluetoothGattDescriptor bluetoothGattDescriptor : nordicService.getDescriptors()) {
                            Log.d("DESCRIPTORS", String.valueOf(bluetoothGattDescriptor.getValue()));
                        }
                        String messageReceived = String.valueOf(nordicService.getValue());
                        Log.d("DATAS", messageReceived);

                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        test_bles = new ArrayList<>();

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        ble_recycler_view = binding.bleList;
        ble_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        //Setting Up Adapter:
        BLE_Device_Disp_Adapter.OnItemClickListener onItemClickListener = new BLE_Device_Disp_Adapter.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(BluetoothDevice bluetoothDevice) {
//                bluetoothGatt = bluetoothDevice.connectGatt(getContext(), false, bluetoothGattCallback);
//                NavHostFragment.findNavController(BLE_Scan_Fragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                //Set Up Ble Services:
                scanLeDevice(); //Stop scanning early!
                bluetoothDeviceToConnect = bluetoothDevice;
                Intent gattServiceIntent = new Intent(getContext(), BluetoothLeService.class);
                getActivity().bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

                //Registering Receivers:
                getActivity().registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
                Log.d("TEST", "Made it to the click!");
            }
        };

        ble_recycler_view_adapter = new BLE_Device_Disp_Adapter(test_bles, onItemClickListener);

        //Bluetooth Set Up:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothManager = getContext().getSystemService(BluetoothManager.class);
        }
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            //Device does not support bluetooth. TODO: ENTER PROPER ERROR HANDLING BRANCHING.
        }

        checkPermissions(); //NEED TO CHECK PERMISSIONS!

        //BLE Set UP:
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();

        ble_recycler_view.setAdapter(ble_recycler_view_adapter);

        return binding.getRoot();

    }

    // Device scan callback.
    public android.bluetooth.le.ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
//                    Toast.makeText(getContext(), "Adding device", Toast.LENGTH_SHORT).show();
//                    ble_recycler_view_adapter.addDevice(result.getDevice());
                    ble_recycler_view_adapter.addDevice(result.getDevice());
                    ble_recycler_view_adapter.notifyDataSetChanged();
                }
            };

    private void scanLeDevice() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    ble_recycler_view_adapter.resetDeviceList();
                    ble_recycler_view_adapter.notifyDataSetChanged();
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
            ble_recycler_view_adapter.resetDeviceList();
            ble_recycler_view_adapter.notifyDataSetChanged();
        }
    }

    private void scanLeDeviceSingleThread() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            scanning = false;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothLeScanner.stopScan(leScanCallback);

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_TX_SERVICE_DATA_AVAILABLE);
        return intentFilter;
    }
}
