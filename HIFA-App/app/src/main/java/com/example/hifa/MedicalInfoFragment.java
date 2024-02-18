package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MedicalInfoFragment extends Fragment {

    private Button saveChangesbutton;
    private EditText driverlicense;
    private EditText healthcard;
    private EditText healthcardnumber;
    int age;
    String phonenumber;
    String userEmail;
    String firstName;
    String lastName;
    String userPassword;

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

        Intent intent = getActivity().getIntent();


// Retrieve data using key
        userEmail = intent.getStringExtra("UserEmail");
        firstName = intent.getStringExtra("FirstName");
        lastName = intent.getStringExtra("LastName");
        userPassword = intent.getStringExtra("UserPassword");

        getParentFragmentManager().setFragmentResultListener("phonenumber", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                phonenumber = result.getString("phonenumber");
                age = result.getInt("age");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medical_info, container, false);
        saveChangesbutton = view.findViewById(R.id.savechaangesButton);
        driverlicense = view.findViewById(R.id.driverseditText);
        healthcardnumber = view.findViewById(R.id.healtheditText);
        String driverlicenseString = driverlicense.getText().toString();
        String healthcardnumberString = healthcardnumber.getText().toString();
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

        creatingNewUser(userEmail, userPassword,firstName,lastName,age,healthcardnumberString,driverlicenseString,phonenumber);
        return view;

    }

    private void creatingNewUser(String email, String password, String firstname, String lastname, int age, String healthcard, String driversLicense, String phonenumber) {
        DatabaseFirestore.userSignUp(new User(email, password, firstname, lastname, age, healthcard, driversLicense, phonenumber), new DatabaseFirestore.CallbackAddNewUser() {
            @Override
            public void onCallBack(Boolean userExists) {
                if(!userExists) getActivity().finish();
                else {
                    // telling the user that the user profile already exists
                    TextView txtView = (TextView)getView().findViewById(R.id.errorTextView);
                    txtView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}