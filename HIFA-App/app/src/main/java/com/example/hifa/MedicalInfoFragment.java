package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class MedicalInfoFragment extends Fragment {

    private Button saveChangesbutton;
    private EditText driverlicense;
    private EditText healthcardnumber;
    private EditText dateofbirth;
    private EditText phoneNumber;
    int age;
    String userEmailString;
    String firstNameString;
    String lastNameString;
    String userPasswordString;
    private FirebaseAuth mAuth;

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
        Bundle bundle = getArguments();



        if(bundle!=null){
            userEmailString = bundle.getString("UserEmail");
            firstNameString = bundle.getString("FirstName");
            lastNameString = bundle.getString("LastName");
            userPasswordString = bundle.getString("UserPassword");
        }
        mAuth = FirebaseAuth.getInstance();
        saveChangesbutton = view.findViewById(R.id.savechaangesButton);
        driverlicense = view.findViewById(R.id.driverseditText);
        healthcardnumber = view.findViewById(R.id.healtheditText);
        dateofbirth = view.findViewById(R.id.editTextDOB);
        phoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        saveChangesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean incompletedata;
                String driverlicenseString = driverlicense.getText().toString();
                String healthcardnumberString = healthcardnumber.getText().toString();
                String phoneNumberString = phoneNumber.getText().toString();
                String dateofbirthString = dateofbirth.getText().toString();
                incompletedata = false;

                if(driverlicenseString.isEmpty()){
                    driverlicense.setError("Driver's License Number is required");
                    incompletedata = true;
                }
                if(healthcardnumberString.isEmpty()){
                    healthcardnumber.setError("Healthcard Number is required");
                    incompletedata = true;
                }
                if(phoneNumberString.isEmpty()){
                    phoneNumber.setError("Phone Number is required");
                    incompletedata = true;
                }
                if(dateofbirthString.isEmpty()){
                    dateofbirth.setError("Date of Birth is required");
                    incompletedata = true;
                }
                if(!incompletedata){
                    int age = calculateAge(dateofbirthString);
                    DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                    creatingNewUser(userEmailString, userPasswordString,firstNameString,lastNameString,age,healthcardnumberString,driverlicenseString,phoneNumberString);
//                    Intent i = new Intent(getActivity(),HomeActivity.class);
//
//                    startActivity(i);
                    // After signing up the user will be required to log in
                    mAuth.createUserWithEmailAndPassword(userEmailString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "User registration successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginActivity2.class));
                            }else{
                                Toast.makeText(getContext(),"Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



//                 Remove bottom two lines to implement actual sign up

        return view;
    }
    private void creatingNewUser(String email, String password, String firstname, String lastname, int age, String healthcard, String driversLicense,String phoneNumber  ) {
        Toast.makeText(getContext(),"creating new user", Toast.LENGTH_SHORT).show();
        DatabaseFirestore.userSignUp(new User(email, password, firstname, lastname, age, healthcard, driversLicense, phoneNumber), new DatabaseFirestore.CallbackAddNewUser() {
            @Override
            public void onCallBack(Boolean userExists) {
                if(userExists) {
                    //can change this to a toast if we want
                    healthcardnumber.setError("User account exists");
                }
            }
        });
        Toast.makeText(getContext(),"creating new user", Toast.LENGTH_SHORT).show();
    }

    public static int calculateAge(String dob) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the pattern based on your date format
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            // Handle parsing errors
            System.out.println("Error parsing date: " + e.getMessage());
            return -1; // Or handle the error in another way
        }
    }
}