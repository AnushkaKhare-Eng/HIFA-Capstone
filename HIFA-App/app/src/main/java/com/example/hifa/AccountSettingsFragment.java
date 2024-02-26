package com.example.hifa;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button savebutton;
    private EditText phonenumber;
    private EditText dateofbirth;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettingsFragment newInstance(String param1, String param2) {
        AccountSettingsFragment fragment = new AccountSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }
    int age;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savebutton= getView().findViewById(R.id.save_changes_account_settings);
        phonenumber = getView().findViewById(R.id.phoneNumberAccountSettings);
        dateofbirth = getView().findViewById(R.id.DateOfBirtheditTextDate);
        String phoneNumberString = phonenumber.getText().toString();
        String dateofbirthString = dateofbirth.getText().toString();


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean incompletedata;
                String phoneNumberString = phonenumber.getText().toString();


                incompletedata = false;

                if(phoneNumberString.isEmpty()){
                    phonenumber.setError("Phone Number is required");
                    incompletedata = true;
                }
                if(dateofbirthString.isEmpty()){
                    dateofbirth.setError("Date of Birth is required");
                    incompletedata = true;
                }
                if(incompletedata==false){
                    age = calculateAge(String.valueOf(dateofbirthString));
                }

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("phonenumber", phoneNumberString);
        bundle.putInt("age", age);
        getParentFragmentManager().setFragmentResult("phonenumber",bundle);
        getParentFragmentManager().setFragmentResult("age",bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    // function to calculate the age
    public static int calculateAge(String dob) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the pattern based on your date format
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            // Handle parsing errors
            System.out.println("Error parsing date: " + e.getMessage());
            return -1; // Or handle the error in another way
        }
    }


}