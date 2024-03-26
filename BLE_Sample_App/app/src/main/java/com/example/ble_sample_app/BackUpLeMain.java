//package com.example.ble_sample_app;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothClass;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.bluetooth.le.BluetoothLeScanner;
//import android.bluetooth.le.ScanCallback;
//import android.bluetooth.le.ScanResult;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ble_sample_app.databinding.FragmentFirstBinding;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//@RequiresApi(api = Build.VERSION_CODES.S)
//public class BLE_Scan_Fragment extends Fragment {
//
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.BLUETOOTH_PRIVILEGED
//    };
//    private static String[] PERMISSIONS_LOCATION = {
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.BLUETOOTH_PRIVILEGED
//    };
//
//    private static String BLE_ERROR_STRING = "NO BLE FEATURES ON DEVICE!";
//    private FragmentFirstBinding binding;
//    private RecyclerView ble_recycler_view;
//    private BLE_Device_Disp_Adapter ble_recycler_view_adapter;
//    private ArrayList<BluetoothDevice> test_bles;
//    private ArrayList<String> testStrings;
//
//    //TODO: THIS MUST HAVE THE BLE SCANNER CLASS!
//    BluetoothManager bluetoothManager;
//    BluetoothAdapter bluetoothAdapter;
//    private BluetoothLeScanner bluetoothLeScanner;
//    private boolean scanning;
//    private Handler handler;
//
//    private static final int REQUEST_ENABLE_BT = 1;
//    // Stops scanning after 10 seconds.
//    private static final long SCAN_PERIOD = 10000;
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//        test_bles = new ArrayList<>();
//        testStrings = new ArrayList<>();
//
//        testStrings.add("Hello");
//        testStrings.add("Testing");
//
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        ble_recycler_view = binding.bleList;
//        ble_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
//        ble_recycler_view_adapter = new BLE_Device_Disp_Adapter(test_bles, testStrings);
//
//        //Bluetooth Set Up:
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            bluetoothManager = getContext().getSystemService(BluetoothManager.class);
//        }
//        bluetoothAdapter = bluetoothManager.getAdapter();
//
//        if (bluetoothAdapter == null) {
//            //Device does not support bluetooth. TODO: ENTER PROPER ERROR HANDLING BRANCHING.
//        }
//
//        //Check if Bluetooth is enabled:
////        if (!bluetoothAdapter.isEnabled()) {
////            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
////        }
//
//        checkPermissions(); //NEED TO CHECK PERMISSIONS!
//
//        //BLE Set UP:
//        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//        handler = new Handler();
//
//        ble_recycler_view.setAdapter(ble_recycler_view_adapter);
//
//        return binding.getRoot();
//
//    }
//
//    // Device scan callback.
//    public android.bluetooth.le.ScanCallback leScanCallback =
//            new ScanCallback() {
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    super.onScanResult(callbackType, result);
////                    Toast.makeText(getContext(), "Adding device", Toast.LENGTH_SHORT).show();
////                    ble_recycler_view_adapter.addDevice(result.getDevice());
//                    ble_recycler_view_adapter.addDevice(result.getDevice());
//                    ble_recycler_view_adapter.notifyDataSetChanged();
//                }
//            };
//
//    private void scanLeDevice() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        if (!scanning) {
//            // Stops scanning after a predefined scan period.
//            handler.postDelayed(new Runnable() {
//                @SuppressLint("MissingPermission")
//                @Override
//                public void run() {
//                    scanning = false;
//                    bluetoothLeScanner.stopScan(leScanCallback);
//                    ble_recycler_view_adapter.notifyDataSetChanged();
//                }
//            }, SCAN_PERIOD);
//
//            scanning = true;
//            bluetoothLeScanner.startScan(leScanCallback);
//        } else {
//            scanning = false;
//            bluetoothLeScanner.stopScan(leScanCallback);
//        }
//    }
//
//    private void scanLeDeviceSingleThread() throws InterruptedException {
//        if (!scanning) {
//            // Stops scanning after a predefined scan period.
//            scanning = false;
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            bluetoothLeScanner.stopScan(leScanCallback);
//
////            wait(SCAN_PERIOD);
//
//            scanning = true;
//            bluetoothLeScanner.startScan(leScanCallback);
//        } else {
//            scanning = false;
//            bluetoothLeScanner.stopScan(leScanCallback);
//        }
//    }
//
//    private void checkPermissions(){
//        int permission1 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permission2 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN);
//        if (permission1 != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    PERMISSIONS_STORAGE,
//                    1
//            );
//        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    PERMISSIONS_LOCATION,
//                    1
//            );
//        }
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Make it so you update the button on the first fragment.
////                NavHostFragment.findNavController(BLE_Scan_Fragment.this)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
////                ble_recycler_view_adapter.addSampleString("Testing");
//
//                scanLeDevice();
//            }
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//}
