package com.example.hifa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEmergencyContactFragment extends DialogFragment {

    User user;
    private Button saveChanges;
    private Button cancelButton;
    private EditText etName;
    private EditText etPhoneNum;
    EmergencyContacts emergencyContacts = new EmergencyContacts("nameString", "phoneNumberString","example@gmail.com");
    Map<String,String> tempMap = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_emergency_contact, container, false);
        //getting the user obj
        saveChanges = (Button) view.findViewById(R.id.saveChangesButtonAEC);
        cancelButton = (Button) view.findViewById(R.id.cancelButtonAEC);
        user = ((HomeActivity) requireActivity()).getUser();
        Log.d("EmergencyContact", user.getFirstname());

        etName = view.findViewById(R.id.etName);
        etPhoneNum = view.findViewById(R.id.etPhoneNum);
        String phoneNumberString = etPhoneNum.getText().toString();
        String nameString = etName.getText().toString();


        // Method for adding / editting EC -- tested
//        String keyToUpdate = "nameString2";
//        if (tempMap.containsKey(nameString)) {
//            // Update the value associated with the key
//            tempMap.put(keyToUpdate, "newValue");
//            emergencyContacts.updateContactInfo(keyToUpdate,"newValue");
//
//
//        } else {
//            tempMap.put(keyToUpdate, "newValue");
//            emergencyContacts.addContactInfo(keyToUpdate,"newValue");
//
//        }
//        DatabaseFirestore.editEmergencyContact(user, tempMap, new DatabaseFirestore.CallbackEditEmergencyContact() {
//            @Override
//            public void onCallBack(EmergencyContacts emergencyContacts) {
//
//            }
//        });
//        if(tempMap.size()>1) {
//            tempMap.remove("nameString", "phoneString");
//            emergencyContacts.deleteContactInfo("nameString", "phoneString");
//        }
//        if(tempMap.size()==1){tempMap=tempMap;}
//        DatabaseFirestore.deleteEmergencyContact(user, tempMap, "nameString", new DatabaseFirestore.CallbackEditEmergencyContact() {
//            @Override
//            public void onCallBack(EmergencyContacts emergencyContacts) {
//
//            }
//        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // going back to the Emergency Contact
//                Intent intent2 = new Intent(getActivity(), EmergencyContactFragment.class);
//                startActivity(intent2);
//
//            }
//        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddEC","databaseEC");

                Map<String,String> tempMap2 = new HashMap<>();
                tempMap2.put("Karan","4039030919");
                DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                // Method for saving EC - Tested
                Log.d("AddECDBini","databaseEC"+user.getEmail());
                DatabaseFirestore.saveEmergencyContact(user, tempMap2, new DatabaseFirestore.CallbackEC() {

                    @Override
                    public void onCallBack(Boolean emergencyContactsExists) {
                        Log.d("AddECDBsave","databaseEC");
                    }
                });
//                 Intent intent = new Intent(getActivity(), EmergencyContactFragment.class);
//                        Log.d("DatabaseEC",emergencyContacts.getTestName());
//// Put the data you want to pass as extras
//                        intent.putExtra("emergencyContact", emergencyContacts); // Replace "key" with a unique identifier and value with the data you want to pass
//// Start the second activity
//                        startActivity(intent);
            }
        });

        return view;


    }
}
