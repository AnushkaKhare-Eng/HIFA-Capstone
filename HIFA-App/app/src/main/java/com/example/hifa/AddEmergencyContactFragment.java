package com.example.hifa;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddEmergencyContactFragment extends DialogFragment {


    User user;
    private Button saveChanges;
    private Button cancelButton;
    private EditText etName;
    private EditText etPhoneNum;
    EmergencyContacts emergencyContacts = new EmergencyContacts("nameString", "phoneNumberString");
    Map<String,String> tempMap = new HashMap<>();

//    public interface OnDataPassListener {
//        void onDataPass(Map<String,String> tempMap);
//    }
//
//    private OnDataPassListener dataPassListener;
//
//    // Attach the parent fragment as the listener
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnDataPassListener) {
//            dataPassListener = (OnDataPassListener) context;
//        } else {
//            throw new ClassCastException(context.toString()
//                    + " must implement ChildFragment.OnDataPassListener");
//        }
//    }

    // Method to pass data to the parent fragment

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

        etName = (EditText) view.findViewById(R.id.etName);
        etPhoneNum = (EditText) view.findViewById(R.id.etPhoneNum);
        saveChanges = (Button) view.findViewById(R.id.saveChangesButtonAEC);
        cancelButton = (Button) view.findViewById(R.id.cancelButtonAEC);
        user = ((HomeActivity) requireActivity()).getUser();
        Log.d("EmergencyContact", user.getFirstname());

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
        //creating a interface

        Map<String,String> tempMap2 = new HashMap<>();

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddEC","databaseEC");

                String nameString = etName.getText().toString();
                String phoneNumberString = etPhoneNum.getText().toString();

                tempMap.put(nameString,phoneNumberString);
                Log.d("nameString",nameString);
                Log.i("phoneString",phoneNumberString);
                DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                // Method for saving EC - Tested
                Log.d("AddECDBini","databaseEC"+user.getEmail());
                DatabaseFirestore.saveEmergencyContact(user, tempMap, new DatabaseFirestore.CallbackEC() {

                    @Override
                    public void onCallBack(Boolean emergencyContactsExists) {
                        Log.d("AddECDBsave","databaseEC");
                    }
                });
                emergencyContacts.setEmergencyContactmap(tempMap);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("emergencyContact", (Serializable) tempMap);
//                bundle.putString("userEmail", user.getEmail());
//                getParentFragmentManager().setFragmentResult("key", bundle);
                Log.d("InfoSent",nameString);
                dismiss();
            }
        });


        // Method to demonstrate passing data

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // going back to the Emergency Contact
                dismiss();

            }
        });

        return view;


    }
//    private void sendDataToParent() {
//        //String data = "Hello from child fragment!";
//        passDataToParent(tempMap);
//    }
//
//    private void passDataToParent(Map<String, String> tempMap) {
//        if (dataPassListener != null) {
//            dataPassListener.onDataPass(tempMap);
//        }
//    }
}
