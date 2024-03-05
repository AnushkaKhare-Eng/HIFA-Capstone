package com.example.hifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2 extends AppCompatActivity {
    private EditText emaileT;
    private EditText passwordeT;

    private Button signUpButton;
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

        signUpButton = findViewById(R.id.signupbutton2);
        loginButton = findViewById(R.id.LoginButton);
        emaileT = findViewById(R.id.editTextTextEmailAddress);
        passwordeT = findViewById(R.id.editTextTextPassword);
        emailString = emaileT.getText().toString().trim();
        passwordString = passwordeT.getText().toString().trim();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                emailString ="example2@gmail.com";
//                passwordString = "123456";

                if(emailString.isEmpty()){
                    emaileT.setError("Email is required");
                    emaileT.requestFocus();
                    Toast.makeText(LoginActivity2.this,"email detected",Toast.LENGTH_SHORT).show();

                }
                else if(passwordString.isEmpty()){
                    passwordeT.setError("Password is required");
                    passwordeT.requestFocus();

                }

                else{
                    mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity2.this, "Login successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity2.this, HomeActivity.class));
                            }else{
                                Toast.makeText(LoginActivity2.this,"Login Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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
    }
}