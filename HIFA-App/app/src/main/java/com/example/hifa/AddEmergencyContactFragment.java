package com.example.hifa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

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

    AddRefreshListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public static AddEmergencyContactFragment newInstance(EmergencyContacts emergencyContacts) {
        AddEmergencyContactFragment fragment = new AddEmergencyContactFragment();
        Bundle args = new Bundle();
//        args.putSerializable("emergencyContact", emergencyContact);
        args.putSerializable("emergencyContacts", emergencyContacts);
        fragment.setArguments(args);
        return fragment;
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

        Map<String,String> tempMap2 = new HashMap<>();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddEC","databaseEC");

                String nameString = etName.getText().toString();
                String phoneNumberString = etPhoneNum.getText().toString();

                tempMap.put(nameString,phoneNumberString);
                assert getArguments() != null;
                emergencyContacts = (EmergencyContacts) getArguments().getSerializable("emergencyContacts");
                Log.d("nameString",nameString);
                Log.i("phoneString",phoneNumberString);
                if (!emergencyContacts.checkName(nameString)){
                    DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                    // Method for saving EC - Tested
                    Log.d("AddECDBini","databaseEC"+user.getEmail());
                    DatabaseFirestore.saveEmergencyContact(user, tempMap, new DatabaseFirestore.CallbackEC() {
                        @Override
                        public void onCallBack(Boolean emergencyContactsExists) {
                            Log.d("AddECDBsave","databaseEC");
                        }
                    });
                    emergencyContacts.addContactInfo(nameString, phoneNumberString);
                    Log.d("InfoSent",nameString);
                    listener.onRefresh(emergencyContacts);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Emergency contact already exists", Toast.LENGTH_SHORT).show();
                }
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

    public void setOnListener(AddRefreshListener listener){
        this.listener = listener;
    }
    public interface AddRefreshListener {
        void onRefresh(EmergencyContacts emergencyContacts);
    }

}
