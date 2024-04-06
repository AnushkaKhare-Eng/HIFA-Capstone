package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.hifa.databinding.ActivityHomeBinding;
import com.example.hifa.twilioapi.SendMessageRequest;
import com.example.hifa.twilioapi.TwilioAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    User userData;
    EmergencyContactFragment emergencyContactFragment;
    BLEScanFragment bleScanFragment;
    BluetoothLeService mainBLEService;
    EmergencyContacts emergencyContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        userData = (User) intent.getSerializableExtra("user_data");

        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userData.getEmail());

        // Create a new fragment instance
        emergencyContactFragment = new EmergencyContactFragment();

        // Set the arguments containing the object Bundle to the fragment
        emergencyContactFragment.setArguments(bundle);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getContactsFromDatabase();

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.action_devices){
                replaceFragment(new Devices_page());
            } else if (item.getItemId() == R.id.action_profile){
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.action_settings){
                replaceFragment(new SettingsFragment(emergencyContactFragment));
            }
            return true;
        });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(HomeActivity.this, LoginActivity2.class));
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void goToHome(){
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.action_devices){
                replaceFragment(new Devices_page());
            } else if (item.getItemId() == R.id.action_profile){
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.action_settings){
                replaceFragment(new SettingsFragment(emergencyContactFragment));
            }
            return true;
        });
    }

    public void sendToBLEScanFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (bleScanFragment == null) {
                bleScanFragment = new BLEScanFragment();
            }
        }

        replaceFragment(bleScanFragment);
    }
    public User getUser(){
        return userData;
    }

    public void bindBLEServiceRoutine(BluetoothLeService bluetoothLeService) {
        this.mainBLEService = bluetoothLeService;
        this.mainBLEService.setEmergencyContactPhone("7806047635"); //TODO: Change this to a generic function call.
        this.mainBLEService.setupLocationFirstTime();
        this.mainBLEService.setUpLocation();
        this.mainBLEService.setUser(userData);
    }

    public EmergencyContacts getEmergencyContacts(){
        return emergencyContacts;
    }

    public void setEmergencyContact(EmergencyContacts emergencyContacts){
        this.emergencyContacts = emergencyContacts;
    }

    public void getContactsFromDatabase(){
        DatabaseFirestore.getEC(userData.getEmail(), new DatabaseFirestore.CallbackGetEC() {

            @Override
            public void onCallBack(Map<String,Object> ecMap1) {
                Log.d("Database", "working1" );

                EmergencyContacts emergencyContacts1 = new EmergencyContacts();
                //eCMap = ecMap1;

                for (String key : ecMap1.keySet()) {
                    String value = (String) ecMap1.get(key);
                    emergencyContacts1.addContactInfo(key, value);
                    // Process the key-value pair
                }
                setEmergencyContact(emergencyContacts1);

            }
        });
    }
}