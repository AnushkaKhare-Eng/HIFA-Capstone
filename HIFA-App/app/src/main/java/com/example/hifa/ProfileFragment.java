package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    TextView name;

    TextView driversLicense;

    TextView healthCare;

    User user;
    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.userNameTextView);
        driversLicense = view.findViewById(R.id.dLTextView);
        healthCare = view.findViewById(R.id.hcTextView);

        user = ((HomeActivity)requireActivity()).getUser();

        String fullName = user.getFirstname() + " " + user.getLastname();

        name.setText(fullName);
        driversLicense.setText(user.getDriversLicense());
        healthCare.setText(user.getHealthcard());

        // Inflate the layout for this fragment
        return view;
    }
}