package com.example.ble_sample_app;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple BLE_Recycler_View to display the scanned BLE devices that are found nearby:
 */
public class BLE_Device_Disp_Adapter extends RecyclerView.Adapter<BLE_Device_Disp_Adapter.ViewHolder> {

    //Interface defining the behaviour of the click on the recycler view element. (In this case a connect request to a device)
    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice bluetoothDevice);
    }

    private final List<BluetoothDevice> localDataSet;
    private final OnItemClickListener onItemClickListener;

    @SuppressLint("MissingPermission")
    public void addDevice(BluetoothDevice device) {
        for (int i = 0; i < localDataSet.size(); i++) {
            BluetoothDevice tempDevice = localDataSet.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (tempDevice.getAddress().equals(device.getAddress())) {
                    return; //Break out of loop if we already have the device on our list.
                }
            }
        }
        localDataSet.add(device);
        Log.d("NOTICE", "Logged new device!");
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }

        @SuppressLint("MissingPermission")
        public void bind(final BluetoothDevice bluetoothDevice, final OnItemClickListener onItemClickListener) {
            textView.setText(bluetoothDevice.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(bluetoothDevice); //Passing in the bluetooth device.
                }
            });
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param bluetoothDevices String[] containing the data to populate views to be used
     * by RecyclerView
     * TODO: Might need to change this later if we pass in discoverables as different data structure!
     */
    public BLE_Device_Disp_Adapter(ArrayList<BluetoothDevice> bluetoothDevices, OnItemClickListener onItemClickListener) {
        localDataSet = bluetoothDevices;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(localDataSet.get(position), onItemClickListener);
    }

    public void resetDeviceList() {
        localDataSet.clear();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
