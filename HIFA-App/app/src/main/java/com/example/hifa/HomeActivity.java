package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hifa.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    User userData;
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
                replaceFragment(new SettingsFragment());
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


    protected void gettingUser(String userEmail){

        DatabaseFirestore.getUser(userEmail, new DatabaseFirestore.CallbackGetUser() {
            @Override
            public void onCallBack(User user) {
                userData = user;
                String userObjFirstname = user.getFirstname();
                Log.d("HomeActivity", "Recieved User's first Name"+userObjFirstname);
                // Create a Bundle and put the object into it
                sendtoHomeFragment(userObjFirstname);

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

    public User getUser(){
        return userData;
    }

}