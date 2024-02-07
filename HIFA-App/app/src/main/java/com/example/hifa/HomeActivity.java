package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hifa.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);
        datePickerButton = findViewById(R.id.datePickerButton);
        date = findViewById(R.id.datePickerTextView);
        signUpButton= findViewById(R.id.signUpButton);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        emaileditText = findViewById(R.id.emailAddressEditText);
        password = findViewById(R.id.passwordeditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        // ensuring that the fields are not empty for the sign up page
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                                date.setText(day+"/"+month+"/"+year);

                            }
                        },0,0,0 );
                datePickerDialog.show();
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