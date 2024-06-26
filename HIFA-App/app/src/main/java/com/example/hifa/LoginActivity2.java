package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.splashscreen.SplashScreen;

public class LoginActivity2 extends AppCompatActivity {
    private EditText emaileT;
    private EditText passwordeT;

    private TextView signUpButton;
    private Button loginButton;
    View view;
    String emailString;
    String passwordString;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signup_button_login);
        loginButton = findViewById(R.id.LoginButton);
        emaileT = findViewById(R.id.editTextText);
        passwordeT = findViewById(R.id.passwordEditText);

        loginButton.setEnabled(true);

        Intent intent = getIntent();

        // Retrieve Object from Intent
        User userObj = (User) intent.getSerializableExtra("User");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(LoginActivity2.this, "Logging in", Toast.LENGTH_SHORT).show();

                emailString = emaileT.getText().toString().trim();
                passwordString = passwordeT.getText().toString().trim();
                if(emailString.isEmpty()){
                    emaileT.setError("Email is required");
                    emaileT.requestFocus();
                    loginButton.setEnabled(true);
                }
                else if(passwordString.isEmpty()){
                    passwordeT.setError("Password is required");
                    passwordeT.requestFocus();
                    loginButton.setEnabled(true);
                }
                else{
                        loginButton.setEnabled(false);
                        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity2.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity2.this, LoadingScreenActivity.class);
                                    intent.putExtra("user_email", emailString);
                                    startActivity(intent);

//                                    Intent intent = new Intent(LoginActivity2.this, HomeActivity.class);
//                                    intent.putExtra("userEmail", emailString);
//                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity2.this, "Login Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    loginButton.setEnabled(true);
                                }
                            }
                        });

                }

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginActivity2.this, "Login successful",Toast.LENGTH_SHORT).show();
                startActivity( new Intent(LoginActivity2.this,SignupActivity.class));


            }
        });

        if(mAuth.getCurrentUser()!=null && emailString!=null){                                    Toast.makeText(LoginActivity2.this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(LoginActivity2.this, HomeActivity.class);
            intent2.putExtra("userEmail", emailString);
            startActivity(intent2);}
    }
}