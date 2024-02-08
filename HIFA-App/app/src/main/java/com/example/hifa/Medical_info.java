package com.example.hifa;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Medical_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Medical_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button saveChangesbutton;
    private EditText driverlicense;
    private EditText healthcardnumber;
    int age;
    String phonenumber;
    public Medical_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Medical_info.
     */
    // TODO: Rename and change types and number of parameters
    public static Medical_info newInstance(String param1, String param2) {
        Medical_info fragment = new Medical_info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: implement the spinners
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        saveChangesbutton = getView().findViewById(R.id.savechaangesButton);
        driverlicense = getView().findViewById(R.id.driverseditText);
        healthcardnumber = getView().findViewById(R.id.healtheditText);
        String driverlicenseString = driverlicense.getText().toString();
        String healthcardnumberString = healthcardnumber.getText().toString();


        saveChangesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean incompletedata;


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
        Intent intent = getActivity().getIntent();


// Retrieve data using key
        String userEmail = intent.getStringExtra("UserEmail");
        String firstName = intent.getStringExtra("FirstName");
        String lastName = intent.getStringExtra("LastName");
        String userPassword = intent.getStringExtra("UserPassword");

        getParentFragmentManager().setFragmentResultListener("phonenumber", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                phonenumber = result.getString("phonenumber");
                age = result.getInt("age");
            }
        });

        creatingNewUser(userEmail, userPassword,firstName,lastName,age,healthcardnumberString,driverlicenseString,phonenumber);

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