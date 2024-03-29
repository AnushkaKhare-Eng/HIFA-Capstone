package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEmergenyContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEmergenyContactFragment extends DialogFragment {

    Button saveChanges;
    Button delete;
    Button cancel;
    EditText editTextName;
    EditText editTextPhoneNo;
    String Name;
    String PhoneNo;

    EmergencyContacts emergencyContacts;

    EditRefreshListener listener;

    public EditEmergenyContactFragment() {
        // Required empty public constructor
    }

    public EditEmergenyContactFragment(String name, String phoneNo) {
        Name = name;
        PhoneNo = phoneNo;
    }

    public static EditEmergenyContactFragment newInstance(EmergencyContact emergencyContact, EmergencyContacts emergencyContacts) {
        EditEmergenyContactFragment fragment = new EditEmergenyContactFragment();
        Bundle args = new Bundle();
        args.putSerializable("emergencyContact", emergencyContact);
        args.putSerializable("emergencyContacts", emergencyContacts);
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

        assert getArguments() != null;
        EmergencyContact contact = (EmergencyContact) getArguments().getSerializable("emergencyContact");

        saveChanges = (Button) view.findViewById(R.id.saveChangesButtonEEC);
        delete = (Button) view.findViewById(R.id.deleteButtonEEC);
        cancel = (Button) view.findViewById(R.id.cancelButtonEEC);
        editTextName = (EditText) view.findViewById(R.id.etNameEEC);
        editTextPhoneNo = (EditText) view.findViewById(R.id.etPhoneNumEEC);

        assert contact != null;
        editTextName.setText(contact.getName());
        editTextPhoneNo.setText(contact.getPhoneNo());

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phoneNo = editTextPhoneNo.getText().toString();
                editEmergencyContact(name, phoneNo);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmergenyContact();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void deleteEmergenyContact(){
        assert getArguments() != null;
        User user = ((HomeActivity)requireActivity()).getUser();
        EmergencyContact contact = (EmergencyContact) getArguments().getSerializable("emergencyContact");
        EmergencyContacts contacts = (EmergencyContacts) getArguments().getSerializable("emergencyContacts");

        assert contact != null;
        assert contacts != null;

        if (contacts.canDelete()){
            contacts.deleteContactInfo(contact.getName(), contact.getPhoneNo());
            DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());

            DatabaseFirestore.editEmergencyContact(user, contacts.getEmergencyContactmap(), new DatabaseFirestore.CallbackEditEmergencyContact() {
                @Override
                public void onCallBack(EmergencyContacts emergencyContacts) {
                    Log.d("EditEC", "Successful: " + emergencyContacts);
                    listener.onRefresh(contacts);
                    dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Need to have atleast one emergency contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void editEmergencyContact(String name, String phoneNo){

        assert getArguments() != null;
        User user = ((HomeActivity)requireActivity()).getUser();
        EmergencyContact contact = (EmergencyContact) getArguments().getSerializable("emergencyContact");
        EmergencyContacts contacts = (EmergencyContacts) getArguments().getSerializable("emergencyContacts");

        assert contact != null;
        assert contacts != null;
        contacts.editExistingContact(contact.getName(), name, phoneNo);

        if(contacts.checkName(name)){
            Toast.makeText(getContext(), "Emergency contact already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());

        DatabaseFirestore.editEmergencyContact(user, contacts.getEmergencyContactmap(), new DatabaseFirestore.CallbackEditEmergencyContact() {
            @Override
            public void onCallBack(EmergencyContacts emergencyContacts) {
                Log.d("EditEC", "Successful: " + emergencyContacts);
                listener.onRefresh(contacts);
                dismiss();
            }
        });
    }

    public void setOnListener(EditRefreshListener listener){
        this.listener = listener;
    }
    public interface EditRefreshListener {
        void onRefresh(EmergencyContacts emergencyContacts);
    }
}