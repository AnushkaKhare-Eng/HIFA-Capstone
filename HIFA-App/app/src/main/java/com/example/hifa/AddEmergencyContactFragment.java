package com.example.hifa;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class AddEmergencyContactFragment extends DialogFragment {

    User user;
    private Button saveChanges;
    private EditText etName;
    private EditText etPhoneNum;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //getting the user obj
        user = ((HomeActivity) requireActivity()).getUser();
        Log.d("EmergencyContact", user.getFirstname());

        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            user = (User) bundle2.getSerializable("User");
        }
        //getting the text entered by the user
        etName = view.findViewById(R.id.etName);
        etPhoneNum = view.findViewById(R.id.etPhoneNum);
        String phoneNumberString = etPhoneNum.getText().toString();
        String nameString = etName.getText().toString();
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseFirestore.saveEmergencyContact(user, nameString, phoneNumberString, new DatabaseFirestore.CallbackEC() {
                    @Override
                    public void onCallBack(EmergencyContacts emergencyContacts) {

                    }
                });
            }
        });
        return inflater.inflate(R.layout.fragment_add_emergency_contact, container, false);


    }
}
