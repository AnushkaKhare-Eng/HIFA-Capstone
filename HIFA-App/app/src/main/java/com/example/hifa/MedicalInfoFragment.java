package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MedicalInfoFragment extends Fragment {

    private Button saveChangesbutton;
    private EditText driverlicense;
    private EditText healthcard;
    private EditText healthcardnumber;

    View view;
    public MedicalInfoFragment() {
        // Required empty public constructor
    }

    public static MedicalInfoFragment newInstance(String param1, String param2) {
        MedicalInfoFragment fragment = new MedicalInfoFragment();
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
        view = inflater.inflate(R.layout.fragment_medical_info, container, false);
        saveChangesbutton = view.findViewById(R.id.savechaangesButton);
        driverlicense = view.findViewById(R.id.driverseditText);
        healthcardnumber = view.findViewById(R.id.healtheditText);
        saveChangesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean incompletedata;
                String driverlicenseString = driverlicense.getText().toString();
                String healthcardnumberString = healthcardnumber.getText().toString();

                incompletedata = false;

                if(driverlicenseString.isEmpty()){
                    driverlicense.setError("Driver's License Number is required");
                    incompletedata = true;
                }
                if(healthcardnumberString.isEmpty()){
                    healthcardnumber.setError("Healthcard Number is required");
                    incompletedata = true;
                }
            }
        });
        return view;
    }
}