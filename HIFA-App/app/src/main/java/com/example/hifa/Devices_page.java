package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Devices_page extends Fragment {

    public Devices_page() {
        // Required empty public constructor
    }

    public static Devices_page newInstance(String param1, String param2) {
        Devices_page fragment = new Devices_page();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devices_page, container, false);
       Button addDeviceButton = (Button) view.findViewById(R.id.add_device_button);

       addDeviceButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((HomeActivity) requireActivity()).sendToBLEScanFragment();
           }
       });

        return view;
    }
}