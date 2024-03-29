package com.example.hifa;
import static com.example.hifa.BluetoothLeService.ACTION_GATT_CONNECTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
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
import android.os.Looper;
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

import com.example.hifa.databinding.FragmentScanDevicesBinding;

import java.util.ArrayList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.S)
public class BLEScanFragment extends Fragment {

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private FragmentScanDevicesBinding binding;
    private RecyclerView ble_recycler_view;
    private BLEScanListAdapter ble_recycler_view_adapter;
    private ArrayList<BluetoothDevice> bleDeviceList;

    //Bluetooth Scanning Variables:
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler;

    BluetoothLeService bluetoothLeService;
    private BluetoothDevice bluetoothDeviceToConnect;
    private boolean connected;

    private ScanSettings scanSettings;

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            String debugging = "DEBUGGING";
            if (ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                Log.d(debugging, "CONNECTED DEVICE!");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
                Log.d(debugging, "DISCONNECTED DEVICE!");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d(debugging, "Services are being iterated over:!");
            } else if (BluetoothLeService.ACTION_GATT_TX_SERVICE_DATA_AVAILABLE.equals(action)) {
                Log.d(debugging, "Notified of change to the TX Register!");
            }
        }
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder bluetoothLeService) {
            BLEScanFragment.this.bluetoothLeService = ((BluetoothLeService.LocalBinder) bluetoothLeService).getService();
            if (BLEScanFragment.this.bluetoothLeService != null) {
                if (!BLEScanFragment.this.bluetoothLeService.initialize()) {
                    Log.e(BluetoothLeService.BLE_SERVICE, "Unable to initialize Bluetooth");
                }
                // perform device connection
                BLEScanFragment.this.bluetoothLeService.connect(bluetoothDeviceToConnect.getAddress());
                BLEScanFragment.this.bluetoothLeService.startForeground();

                ((HomeActivity)getActivity()).bindBLEServiceRoutine(BLEScanFragment.this.bluetoothLeService);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bluetoothLeService = null;
        }
    };

    private static final long SCAN_PERIOD = 10000;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        bleDeviceList = new ArrayList<>();

        binding = FragmentScanDevicesBinding.inflate(inflater, container, false);
        ble_recycler_view = binding.bleScanList;
        ble_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        //Setting Up Adapter:
        BLEScanListAdapter.OnItemClickListener onItemClickListener = bluetoothDevice -> {
            scanLeDevice(); //Stop scanning early!
            bluetoothDeviceToConnect = bluetoothDevice;
            Context context = getContext();
            Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
//                gattServiceIntent.putExtra("nd_address", bluetoothDevice.getAddress());
            requireActivity().bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

            //Registering Receivers:
            requireActivity().registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter(), Context.RECEIVER_EXPORTED);
            Log.d("TEST", "Made it to the click!");
        };

        ble_recycler_view_adapter = new BLEScanListAdapter(bleDeviceList, onItemClickListener);

        //Bluetooth Set Up:
        bluetoothManager = requireContext().getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            //Device does not support bluetooth. TODO: ENTER PROPER ERROR HANDLING BRANCHING.
        }

        checkPermissions();

        //BLE Set UP:
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
                .setLegacy(false)
                .build();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()));

        ble_recycler_view.setAdapter(ble_recycler_view_adapter);

        return binding.getRoot();

    }

    // Device scan callback.
    public android.bluetooth.le.ScanCallback leScanCallback =
            new ScanCallback() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
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
            bluetoothLeScanner.startScan(null, scanSettings, leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
            ble_recycler_view_adapter.resetDeviceList();
            ble_recycler_view_adapter.notifyDataSetChanged();
        }
    }

    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN);
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

        binding.buttonScanBles.setOnClickListener(view1 -> scanLeDevice());
    }

    //TODO: Make sure to destroy the service that was started by this application. (BLEService)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //TODO: Need to add the unique id to this one!!!
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_TX_SERVICE_DATA_AVAILABLE);
        return intentFilter;
    }
}
