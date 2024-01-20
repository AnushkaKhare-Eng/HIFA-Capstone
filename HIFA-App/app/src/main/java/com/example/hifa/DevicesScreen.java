//package com.example.hifa;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class DevicesScreen extends AppCompatActivity {
//    String devicesList [] = {"Device 1","Device 2"};
//    ListView devicesListView;
//    BottomNavigationView bottomNavigationView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.action_devices);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @SuppressLint("NonConstantResourceId")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.action_home:
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.action_devices:
//                        return true;
//                    case R.id.action_profile:
//                        startActivity(new Intent(getApplicationContext(), Profile.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_devices_screen);
//        devicesListView = (ListView) findViewById(R.id.devicesListView);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
//                (this,R.layout.activity_device_list_view,R.id.textView,devicesList);
//        devicesListView.setAdapter(arrayAdapter);
//
//        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
//    }
//
//
//}