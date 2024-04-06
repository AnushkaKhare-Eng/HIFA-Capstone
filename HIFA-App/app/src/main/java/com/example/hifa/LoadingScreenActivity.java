package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoadingScreenActivity extends AppCompatActivity {

    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user_email");
        gettingUser(userEmail);

        // Duration of wait
        int SPLASH_DISPLAY_LENGTH = 1000; // This is 1 second

        // New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
//        new Looper.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                // Create an Intent that will start the next activity you want to show after splash
//                Intent mainIntent = new Intent(LoadingScreenActivity.this, HomeActivity.class);
//                LoadingScreenActivity.this.startActivity(mainIntent);
//                LoadingScreenActivity.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);

    }

    protected void gettingUser(String userEmail){
        DatabaseFirestore.getUser(userEmail, new DatabaseFirestore.CallbackGetUser() {
            @Override
            public void onCallBack(User user) {
                userData = user;
                String userObjFirstname = user.getFirstname();
                Log.d("HomeActivity", "Recieved User's first Name"+userObjFirstname);
                Intent intent = new Intent(LoadingScreenActivity.this, HomeActivity.class);
                intent.putExtra("user_data", userData);
                startActivity(intent);
            }
        });
    }

}
