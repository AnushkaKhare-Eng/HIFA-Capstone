package com.example.hifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String userEmail = extras.getString("userEmail");

        Log.d("HomeActivity", "Recieved User email"+userEmail);

        // Use the object
        if (userEmail != null) {
            DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
            gettingUser(userEmail);
        }
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


    protected void gettingUser(String userEmail){

        DatabaseFirestore.getUser(userEmail, new DatabaseFirestore.CallbackGetUser() {
            @Override
            public void onCallBack(User user) {
                goToHome();
                userData = user;
                String userObjFirstname = user.getFirstname();
                Log.d("HomeActivity", "Recieved User's first Name"+userObjFirstname);
                // Create a Bundle and put the object into it
                sendtoHomeFragment(userObjFirstname);
                sendtoECFragment(userEmail);

            }
        });
    }
    protected void sendtoHomeFragment(String userFirstname){
        Bundle bundle = new Bundle();
        bundle.putString("UserFirstName", userFirstname);

        // Create a new fragment instance
        HomeFragment homefragment = new HomeFragment();

        // Set the arguments containing the object Bundle to the fragment
        homefragment.setArguments(bundle);

        // Add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_fragment, homefragment)
                .commit();
    }
    protected void sendtoECFragment(String userEmail){
        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);

        // Create a new fragment instance
        emergencyContactFragment = new EmergencyContactFragment();

        // Set the arguments containing the object Bundle to the fragment
        emergencyContactFragment.setArguments(bundle);


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
}