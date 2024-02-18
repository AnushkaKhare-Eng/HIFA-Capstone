package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {
    private Button datePickerButton;
    private TextView date;
    private DatePickerDialog datePickerDialog;
    private Button signUpButton;
    private EditText firstName;
    private EditText lastName;
    private EditText emaileditText;
    private EditText password;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpButton= findViewById(R.id.signUpButton);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        emaileditText = findViewById(R.id.emailAddressEditText);
        password = findViewById(R.id.passwordeditText);
         //ensuring that the fields are not empty for the sign up page
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remove bottom two lines to implement actual sign up
                Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                startActivity(i);

                boolean incompletedata;
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String emailString = emaileditText.getText().toString();
                String passwordString = password.getText().toString();
                String phoneString = phoneNumber.getText().toString();

                incompletedata = false;

                if (firstname.isEmpty()) {
                    firstName.setError("First name is required");
                    incompletedata = true;
                }
                if (lastname.isEmpty()) {
                    lastName.setError("Last name is required");
                    incompletedata = true;
                }
                if (emailString.isEmpty()) {
                    emaileditText.setError("Email is required");
                    incompletedata = true;
                }
                if (passwordString.isEmpty()) {
                    password.setError("First name is required");
                    incompletedata = true;
                }
                if (phoneString.isEmpty()) {
                    phoneNumber.setError("First name is required");
                    incompletedata = true;
                }

                creatingNewUser(emailString, passwordString,firstname,lastname,0,0,0);

            }
        });
    }

    private void creatingNewUser(String email, String password, String firstname, String lastname, float age, int healthcard, int driversLicense  ) {
        DatabaseFirestore.userSignUp(new User(email, password, firstname, lastname, 0, 0, 0), new DatabaseFirestore.CallbackAddNewUser() {
            @Override
            public void onCallBack(Boolean userExists) {
                if(!userExists) finish();
                else {
                    emaileditText.setError("User account exists");
                }
            }
        });
    }
}