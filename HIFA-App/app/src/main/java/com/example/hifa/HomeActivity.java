package com.example.hifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.hifa.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
//    Button datePickerButton;
//    TextView date;
//    DatePickerDialog datePickerDialog;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.action_home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.action_profile){
                replaceFragment(new ProfileFragment());
            }
            else if (item.getItemId() == R.id.action_settings){
                replaceFragment(new SettingsFragment());
            }

            return true;
        });

//        datePickerButton = findViewById(R.id.datePickerButton);
//        date = findViewById(R.id.datePickerTextView);
//        datePickerButton.setOnClickListener(view -> {
//            datePickerDialog = new DatePickerDialog(MainActivity.this,
//                    (datePicker,))
//        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}