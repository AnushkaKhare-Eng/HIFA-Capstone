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
 * Use the {@link EditEmergenyContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEmergenyContactFragment extends Fragment {

    Button saveChanges;
    Button cancel;
    EditText editTextName;
    EditText editTextPhoneNo;
    String Name;
    String PhoneNo;

    public EditEmergenyContactFragment() {
        // Required empty public constructor
    }

    public EditEmergenyContactFragment(String name, String phoneNo) {
        Name = name;
        PhoneNo = phoneNo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditEmergenyContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditEmergenyContactFragment newInstance(String param1, String param2) {
        EditEmergenyContactFragment fragment = new EditEmergenyContactFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_edit_emergeny_contact, container, false);

        saveChanges = (Button) view.findViewById(R.id.saveChangesButtonEEC);
        cancel = (Button) view.findViewById(R.id.cancelButtonEEC);
        editTextName = (EditText) view.findViewById(R.id.etNameEEC);
        editTextPhoneNo = (EditText) view.findViewById(R.id.etPhoneNumEEC);

        editTextName.setText(Name);
        editTextPhoneNo.setText(PhoneNo);

        return view;
    }

}