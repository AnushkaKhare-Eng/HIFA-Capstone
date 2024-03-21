package com.example.hifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    private Button datePickerButton;
    private TextView date;
    private DatePickerDialog datePickerDialog;
    private Button signUpButton;
    private Button loginButton;
    private EditText firstNameeT;
    private EditText lastNameeT;
    private EditText emaileT;
    private EditText passwordeT;


    Context context;
    Intent intent;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        //FirebaseApp.initializeApp(SignupActivity.this);
        signUpButton= findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.login);
        firstNameeT = findViewById(R.id.firstNameEditText);
        lastNameeT = findViewById(R.id.lastNameEditText);
        emaileT = findViewById(R.id.emailAddressEditText);
        passwordeT = findViewById(R.id.passwordeditText);



         mAuth = FirebaseAuth.getInstance();
//
//        if(mAuth.getCurrentUser()!=null){Intent intenthome = new Intent(SignupActivity.this, HomeActivity.class);
//            startActivity(intenthome);}
//        else{Intent intentlogin = new Intent(SignupActivity.this, LoginActivity2.class);
//            startActivity(intentlogin);}

        //ensuring that the fields are not empty for the sign up page
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstNameString = firstNameeT.getText().toString();
                String lastNameString = lastNameeT.getText().toString();
                String emailString = emaileT.getText().toString();
                String passwordString = passwordeT.getText().toString();

                if (TextUtils.isEmpty(firstNameString)) {
                    firstNameeT.setError("First name is required");
                    firstNameeT.requestFocus();
                }
                if (TextUtils.isEmpty(lastNameString)) {
                    lastNameeT.setError("Last name is required");
                    lastNameeT.requestFocus();
                }
                if (TextUtils.isEmpty(emailString)) {
                    emaileT.setError("Email is required");
                    emaileT.requestFocus();
                }
                if (passwordString.isEmpty()) {
                    passwordeT.setError("First name is required");
                    passwordeT.requestFocus();
                }

                else{

                    MedicalInfoFragment fragment = new MedicalInfoFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("UserEmail", emailString);
                    bundle.putString("FirstName", firstNameString);
                    bundle.putString("LastName", lastNameString);
                    bundle.putString("UserPassword", passwordString);

                    fragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.signup, fragment).commit();

                }




            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity2.class));
                   }
        });
    }


}