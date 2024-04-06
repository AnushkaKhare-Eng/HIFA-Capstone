package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettingsFragment extends Fragment {

    EditText firstNameEditText;
    EditText middleNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    User userData;
    Button saveChanges;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    public static AccountSettingsFragment newInstance(String param1, String param2) {
        AccountSettingsFragment fragment = new AccountSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        firstNameEditText = view.findViewById(R.id.firstNameAccountSettings);
        middleNameEditText = view.findViewById(R.id.middleNameAccountSettings);
        lastNameEditText = view.findViewById(R.id.lastNameAccountSettings);
        emailEditText = view.findViewById(R.id.emailAccountSettings);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberAccountSettings);
        saveChanges = view.findViewById(R.id.save_changes_account_settings);

        userData = ((HomeActivity)requireActivity()).getUser();

        firstNameEditText.setText(userData.getFirstname());
        lastNameEditText.setText(userData.getLastname());
        emailEditText.setText(userData.getEmail());
        phoneNumberEditText.setText(userData.getPhoneNumber());

        return view;
    }
}