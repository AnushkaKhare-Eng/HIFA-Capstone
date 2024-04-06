package com.example.hifa;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

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
    EditText editTextDOB;
    EditText editTextPhoneNum;
    EditText editTextDriverLicense;
    EditText editTextHealthCard;
    View view;
    String dobInput;
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
        editTextDOB = view.findViewById(R.id.editTextage);
        editTextDriverLicense = view.findViewById(R.id.editTextDL);
        editTextHealthCard = view.findViewById(R.id.editTextHC);
        editTextPhoneNum = view.findViewById(R.id.editTextpn);
        savechangesButton = view.findViewById(R.id.savechangesMedicalInfoButton);

        user = ((HomeActivity) requireActivity()).getUser();

        editTextDOB.setText(user.getAge());
        editTextDriverLicense.setText(user.getDriversLicense());
        editTextHealthCard.setText(user.getHealthcard());
        editTextPhoneNum.setText(user.getPhoneNumber());

        Log.d("MedicalFrag", user.getFirstname());

//        editTextDriverLicense.setText(user.getDriversLicense());
//        editTextHealthCard.setText(user.getHealthcard());
//        editTextAge.setText((int) user.getAge());
//        editTextPhoneNum.setText(user.getPhoneNumber());

        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            user = (User) bundle2.getSerializable("User");
        }

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());

        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                                editTextDOB.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        savechangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateOfBirth = editTextDOB.getText().toString();
                phoneNumInput = editTextPhoneNum.getText().toString();
                driversLicenseInput = editTextDriverLicense.getText().toString();
                healthcardInput = editTextHealthCard.getText().toString();
                DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
                DatabaseFirestore.editMedicalInfo(user, dateOfBirth,healthcardInput,driversLicenseInput,phoneNumInput, new DatabaseFirestore.CallbackEditMedicalInfo() {
                    @Override
                    public void onCallBack(User user) {
                        user.setAge(dateOfBirth);
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