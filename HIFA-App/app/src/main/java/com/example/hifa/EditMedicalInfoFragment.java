package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditMedicalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMedicalInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User user;
    EditText editTextAge;
    EditText editTextPhoneNum;
    EditText editTextDriverLicense;
    EditText editTextHealthCard;
    View view;
    String ageInput;
    String phoneNumInput;
    String driversLicenseInput;
    String healthcardInput;
    Button savechangesButton;

    public EditMedicalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditMedicalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMedicalInfoFragment newInstance(String param1, String param2) {
        EditMedicalInfoFragment fragment = new EditMedicalInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_medical_info, container, false);
        editTextAge = view.findViewById(R.id.editTextage);
        editTextDriverLicense = view.findViewById(R.id.editTextDL);
        editTextHealthCard = view.findViewById(R.id.editTextHC);
        editTextPhoneNum = view.findViewById(R.id.editTextpn);
        savechangesButton = view.findViewById(R.id.savechangesMedicalInfoButton);

        user = ((HomeActivity) requireActivity()).getUser();
        Log.d("MedicalFrag", user.getFirstname());

        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            user = (User) bundle2.getSerializable("User");
        }

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
        DatabaseFirestore.editMedicalInfo(user,12,"12334","239829328","239729372", new DatabaseFirestore.CallbackEditMedicalInfo() {
            @Override
            public void onCallBack(User user) {

            }
        });
        savechangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageInput = editTextAge.getText().toString();
                int ageInt = Integer.parseInt(ageInput);
                phoneNumInput = editTextPhoneNum.getText().toString();
                driversLicenseInput = editTextDriverLicense.getText().toString();
                healthcardInput = editTextHealthCard.getText().toString();
                DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                DatabaseFirestore.editMedicalInfo(user,ageInt,healthcardInput,driversLicenseInput,phoneNumInput, new DatabaseFirestore.CallbackEditMedicalInfo() {
                    @Override
                    public void onCallBack(User user) {
                        user.setAge(ageInt);
                        user.setHealthcard(healthcardInput);
                        user.setDriversLicense(driversLicenseInput);
                        user.setPhoneNumber(phoneNumInput);
                    }
                });
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}