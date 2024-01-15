package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DevicesScreen extends AppCompatActivity {
    String devicesList [] = {"Device 1","Device 2"};
    ListView devicesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_screen);
        devicesListView = (ListView) findViewById(R.id.devicesListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,R.layout.activity_device_list_view,R.id.textView,devicesList);
        devicesListView.setAdapter(arrayAdapter);

        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


}