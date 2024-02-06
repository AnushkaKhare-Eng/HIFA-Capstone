package com.example.hifa;



import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.security.auth.callback.Callback;

public class DatabaseFirestore {
    static private FirebaseFirestore database;
    static private CollectionReference collectionReferenceDevice;
    static private CollectionReference collectionReferenceEmergencyContacts;

    static private CollectionReference collectionReferencePersonalInfo;

    static public void setDB(FirebaseFirestore instance) {
        database = instance;
        collectionReferenceDevice = database.collection("Device");
        collectionReferenceEmergencyContacts = database.collection("Emergency Contact");
        collectionReferencePersonalInfo = database.collection("Personal Info");

    }

    static protected void userSignUp(User user, CallbackAddNewUser callbackUserExists) {
        // need to set all the attributes in the user class
        DocumentReference documentReference = collectionReferencePersonalInfo.document(user.getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //looking a snapshot of the firestore db
                    DocumentSnapshot document = task.getResult();
                    // if the document exists then send a true on the callback function
                    if(document.exists()){
                        Log.d("Does user exists?", "true");
                        callbackUserExists.onCallBack(true);
                    }
                    else{
                        Log.d("Does user exits?", "false");
                        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                callbackUserExists.onCallBack(false);
                            }
                        });
                    }
                }else{
                    Log.d("information retrieval from DB unsuccessful","");
                }

            }
        });
    }
    // updating the medical information
    static protected void addingMedicalInfo(User user, int driverslicense, int healthcard){
        if(user.getDriversLicense() == 0 && user.getHealthcard()==0){
            user.setDriversLicense(driverslicense);
            user.setHealthcard(healthcard);
        }
    }


    static protected void deviceInfo(Devices device, Callback callback) {
        // need to set UUID in for the devices
    }
    public interface CallbackAddNewUser {
        void onCallBack(Boolean userExists);
    }
}
