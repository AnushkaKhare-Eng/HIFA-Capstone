package com.example.hifa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Imports for emergency signal sending and location gathering
import com.example.hifa.twilioapi.SendMessageRequest;
import com.example.hifa.twilioapi.TwilioAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.hifa.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    View view;
    TextView userNameText;
    String userFirstName;

    Button emergencyButton;
    Button addDeviceButton;
    String[] permissions = new String[3];
    private FusedLocationProviderClient fusedLocationClient;
    private double longitude = 0.0;
    private double latitude = 0.0;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        userNameText = (TextView) view.findViewById(R.id.userName);
        emergencyButton = (Button) view.findViewById(R.id.emergency_button);
        addDeviceButton = (Button) view.findViewById(R.id.add_device_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        permissions[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[2] = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 99);
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
        }

        // Retrieve the arguments
        Bundle bundle = getArguments();
//        userFirstName = ((HomeActivity) requireActivity()).getUser().getFirstname();
//        userNameText.setText(userFirstName);

        if (bundle != null) {
            Log.d("HomeFrag", "Recieved User's first Name"+userFirstName);
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMaxUpdateDelayMillis(10000)
                .setWaitForAccurateLocation(false)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // For example, show a toast with the location:
//                    Log.d("Location:", location.getLatitude() + ", " + location.getLongitude());
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(requireActivity(), permissions, 99);
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.d("Location", latitude + " " + longitude);
                                    sendSMSMessage();
                                } else {
                                    Log.d("Location", "Location is null");
                                }
                            }
                        });

            }
        });

        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).sendToBLEScanFragment();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    protected void sendSMSMessage() {
        Log.d("SMS", "sendSMSMessage: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zq9aaxp7gg.execute-api.us-east-2.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwilioAPIService twilioAPIService = retrofit.create(TwilioAPIService.class);

        Map<String, String> emergencyContacts = new HashMap<String, String>();

        emergencyContacts.put("Karan", "5875963855");
        emergencyContacts.put("Karan1", "5875963855");
        emergencyContacts.put("Karan2", "5875963855");

        SendMessageRequest sendMessageRequest = null;
        Call<Void> call = null;

        for(Map.Entry<String, String> entry:emergencyContacts.entrySet()){
            String name = entry.getKey();
            String phoneNo = entry.getValue();
            String messageString = name + " https://www.google.com/maps?q=" + latitude + "," + longitude;
            sendMessageRequest = new SendMessageRequest(phoneNo, messageString);
            call = twilioAPIService.createPost(BuildConfig.TWILIO_API_KEY,sendMessageRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Log.d("POST", "Post successful");
                    Toast.makeText(requireContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("POST", "Post unsuccessful");
                    Toast.makeText(requireContext(), "SMS not sent", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (sendMessageRequest == null){
            Toast.makeText(requireContext(), "Please add an emergency contact", Toast.LENGTH_SHORT).show();
        }
    }
}