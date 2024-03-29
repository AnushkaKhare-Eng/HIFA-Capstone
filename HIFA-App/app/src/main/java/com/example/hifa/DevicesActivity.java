package com.example.hifa;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hifa.databinding.ActivityDevicesBinding;

public class DevicesActivity extends AppCompatActivity {
    ActivityDevicesBinding activityDevicesBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDevicesBinding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(activityDevicesBinding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            replaceFragment(new BLEScanFragment());
        }
    }

    /**
     * Function to dynamically change fragment views/pages.
     * @param fragment fragment to be brought up to view.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.device_activity_fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
