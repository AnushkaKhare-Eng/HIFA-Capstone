package com.example.hifa;

import static com.example.hifa.DatabaseFirestore.addingMedicalInfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Medical_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Medical_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button saveChangesbutton;
    private EditText driverlicense;
    private EditText healthcard;
    private EditText healthcardnumber;
    public Medical_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Medical_info.
     */
    // TODO: Rename and change types and number of parameters
    public static Medical_info newInstance(String param1, String param2) {
        Medical_info fragment = new Medical_info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: implement the spinners
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        saveChangesbutton = getView().findViewById(R.id.savechaangesButton);
        driverlicense = getView().findViewById(R.id.driverseditText);
        healthcardnumber = getView().findViewById(R.id.healtheditText);
        saveChangesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean incompletedata;
                String driverlicenseString = driverlicense.getText().toString();
                String healthcardnumberString = healthcardnumber.getText().toString();

                incompletedata = false;

                if(driverlicenseString.isEmpty()){
                    driverlicense.setError("Driver's License Number is required");
                    incompletedata = true;
                }
                if(healthcardnumberString.isEmpty()){
                    healthcardnumber.setError("Healthcard Number is required");
                    incompletedata = true;
                }
                addingMedicalInfo(driverlicenseString, healthcardnumberString);
            }
        });

    }

    private void addingMedicalInfo(String driverlicenseString,String healthcardnumberString) {
    // get user and then add the medical info to the user.
        

    }

}