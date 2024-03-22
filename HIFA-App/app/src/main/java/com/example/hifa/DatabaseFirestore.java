package com.example.hifa;



import android.health.connect.datatypes.Device;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DatabaseFirestore {
    static private FirebaseFirestore database;
    static private CollectionReference collectionReferenceDevice;
    static private CollectionReference collectionReferenceEmergencyContacts;

    static private CollectionReference collectionReferencePersonalInfo;

    static public void databaseSetUp(FirebaseFirestore instance) {
        database = instance;
        collectionReferenceDevice = database.collection("Device");
        collectionReferenceEmergencyContacts = database.collection("Emergency Contact");
        collectionReferencePersonalInfo = database.collection("Personal Info");

    }



    static protected void userSignUp(User user, CallbackAddNewUser callbackUserExists) {
        // need to set all the attributes in the user class
        if(user.getEmail()!=null) {
            DocumentReference documentReference = collectionReferencePersonalInfo.document(user.getEmail());

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("Registering user", "User registration success");
                        //looking a snapshot of the firestore db
                        DocumentSnapshot document = task.getResult();
                        // if the document exists then send a true on the callback function
                        if (document.exists()) {
                            Log.d("Does user exists?", "true");
                            callbackUserExists.onCallBack(true);
                        } else {
                            Log.d("Does user exits?", "false");
                            documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    callbackUserExists.onCallBack(false);
                                }
                            });
                        }
                    } else {
                        Log.d("information retrieval from DB unsuccessful", "");
                    }

                }

            });
        }
    }

    static protected void saveEmergencyContact(User user, Map<String, String> emergencyContactsmap, CallbackEC callbackEC) {

        if(user.getEmail()!=null) {
            DocumentReference documentReference = collectionReferenceEmergencyContacts.document(user.getEmail());
            //EmergencyContacts emergencyContacts = new EmergencyContacts(ecName, ecPhoneNum,user.getEmail());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("Registering EC","Added Ec successfully");
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Log.d("EC exists for user","true");
                            callbackEC.onCallBack(true);
                        }
                        else {
                            Log.d("Adding user to the database", "");
                            documentReference
                                    .set(emergencyContactsmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("ECPg", "EC Info" + user.getEmail() + "added");
                                            callbackEC.onCallBack(false);
                                        }
                                    });
                        }


                    }
                }
            });
        }
    }
    static protected void getUser(String email, CallbackGetUser callbackGetUser){
        collectionReferencePersonalInfo.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            Log.d(" Database Getting user with email", "email " + email + " does not exists");
                            callbackGetUser.onCallBack(null);
                        } else {
                            User user = task.getResult().getDocuments().get(0).toObject(User.class);
                            Log.d(" SuccessDB Getting user with email", "email: " + email );
                            callbackGetUser.onCallBack(user);
                        }
                    }
                });
    }

    static protected void editMedicalInfo(User user,int age, String healthcard, String driversLicense, String phonenumber, CallbackEditMedicalInfo callbackEditMedicalInfo){
        collectionReferencePersonalInfo.document(user.getEmail())
                .update("age", age);
        collectionReferencePersonalInfo.document(user.getEmail())
                .update("healthcard",healthcard);
        collectionReferencePersonalInfo.document(user.getEmail())
                .update("phonenumber", phonenumber);
        collectionReferencePersonalInfo.document(user.getEmail())
                .update("driversLicense", (String)driversLicense)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.setAge(age);
                        user.setHealthcard(healthcard);
                        user.setDriversLicense(driversLicense);
                        user.setPhoneNumber(phonenumber);
                        Log.d("Saving medical info fields ", "Age:"+ user.getAge()+ "healthcard"+ user.getHealthcard()+"phonenumber"+ user.getPhoneNumber()+"driversLicense" + user.getDriversLicense());
                        callbackEditMedicalInfo.onCallBack(user);
                    }
                });

    }
    static protected void editEmergencyContact(User user,Map<String,String> emergencyContactsmap, CallbackEditEmergencyContact callbackEditEmergencyContact){
        if(user.getEmail()!=null) {
            DocumentReference documentReference = collectionReferenceEmergencyContacts.document(user.getEmail());
            //EmergencyContacts emergencyContacts = new EmergencyContacts(ecName, ecPhoneNum,user.getEmail());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("Registering EC","Added Ec successfully");
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Log.d("Adding user to the database", "");
                            documentReference
                                    .set(emergencyContactsmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("ECPg", "EC Info" + user.getEmail() + "added");

                                        }
                                    });

                        }
                        else {
                            Log.d("EC exists for user","true");


                        }


                    }
                }
            });
        }
    }
    static protected void deleteEmergencyContact(User user,Map<String,String> emergencyContactsmap, String ecName, CallbackEditEmergencyContact callbackEditEmergencyContact){
        if(user.getEmail()!=null) {
            DocumentReference documentReference = collectionReferenceEmergencyContacts.document(user.getEmail());
            //EmergencyContacts emergencyContacts = new EmergencyContacts(ecName, ecPhoneNum,user.getEmail());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    //Map<String,String> ecmap = (Map<String,String>) documentSnapshot.get("nameString");
                    if (task != null) {

                            Log.d("Deleting scannerInfo", "the whole code got deleted");
                            documentSnapshot.getReference().delete();

                            if(documentSnapshot.exists()){
                                Log.d("Adding user to the database", "");
                                documentReference
                                        .set(emergencyContactsmap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("ECPg", "EC Info" + user.getEmail() + "added");

                                            }
                                        });

                            }

                    }
                    }
            });
        }
    }

    static protected void saveDeviceInfo(User user, Device device, CallbackDevice callbackDevice){
        DocumentReference documentReference = collectionReferenceDevice.document(user.getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    Log.d("DevicesPg", "saving devices info" + user.getEmail() + "already exists");
                    callbackDevice.onCallBack(device);
                } else {
                    documentReference
                            .set(device)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("DevicesPg", "Devices Info" + user.getEmail() + "added");
                                    callbackDevice.onCallBack(device);
                                }
                            });
                }
            }
        });
    }


    public interface CallbackAddNewUser {
        void onCallBack(Boolean userExists);
    }
    public interface CallbackEditMedicalInfo {
        void onCallBack(User user);
    }
    public interface CallbackEditEmergencyContact {
        void onCallBack(EmergencyContacts emergencyContacts);
    }

    public interface CallbackGetUser {
        void onCallBack(User user);
    }

    public interface CallbackDevice {
        void onCallBack(Device device);
    }

    public interface CallbackEC {
        void onCallBack(Boolean emergencyContactsExists);
    }

}
