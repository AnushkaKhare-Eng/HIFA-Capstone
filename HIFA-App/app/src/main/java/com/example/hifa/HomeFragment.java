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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import android.telephony.SmsManager;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    TextView userNameText;
    String userFirstName;

    Button emergencyButton;
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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        userNameText = (TextView) view.findViewById(R.id.userName);
        emergencyButton = (Button) view.findViewById(R.id.emergency_button);

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

        if (bundle != null) {

            // Extract the object from the arguments using the unique key
            userFirstName = ((HomeActivity) requireActivity()).getUser().getFirstname();
            Log.d("HomeFrag", "Recieved User's first Name"+userFirstName);
            //why is this not changing the value??
            userNameText.setText(userFirstName);
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
                    Log.d("Location:", location.getLatitude() + ", " + location.getLongitude());
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
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
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
                                    sendSMSMessage("5875963855");
                                } else {
                                    Log.d("Location", "Location is null");
                                }
                            }
                        });

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    protected void sendSMSMessage(String phoneNo) {
        String message = "GRANDSON, I NEED YOU, Love Grandma XOXO <3 <3 https://www.google.com/maps?q=" + latitude + "," + longitude;
        if (!phoneNo.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(requireContext(), "SMS sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Please enter phone", Toast.LENGTH_SHORT).show();
        }
    }
}