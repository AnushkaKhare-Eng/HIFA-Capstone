package com.example.hifa;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmergencyContactFragment extends Fragment implements EditEmergenyContactFragment.EditRefreshListener, AddEmergencyContactFragment.AddRefreshListener {

    View view;
    RecyclerView recyclerView;

    EmergencyContactsAdapter emergencyContactsAdapter;

    Map<String, String> eCMap;
    String userEmail;
    List<EmergencyContact> items;

    EmergencyContacts emergencyContacts;
    public EmergencyContactFragment() {
        // Required empty public constructor
    }

    public static EmergencyContactFragment newInstance() {
        EmergencyContactFragment fragment = new EmergencyContactFragment();
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
        view = inflater.inflate(R.layout.fragment_emergency_contact, container, false);

        items = new ArrayList<EmergencyContact>();

        emergencyContacts = new EmergencyContacts();

        Button addContactButton = (Button) view.findViewById(R.id.add_contact_button);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddContactDialog();
            }
        });

        recyclerView = view.findViewById(R.id.emergency_contacts_recyclerview);

        // passing information to the adapter
        Bundle bundle = getArguments();
        if (bundle != null) {

            // Extract the object from the arguments using the unique key
            userEmail = ((HomeActivity) requireActivity()).getUser().getEmail();
            Log.d("ECFrag", "Recieved User's email"+userEmail);
        }

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
        getContactsFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EmergencyContactsAdapter(this.getContext(), items));

        return view;
    }

    public void getContactsFromDatabase(){
        DatabaseFirestore.getEC(userEmail, new DatabaseFirestore.CallbackGetEC() {

            @Override
            public void onCallBack(Map<String,Object> ecMap1) {
                Log.d("Database", "working1" );

                //eCMap = ecMap1;

                for (String key : ecMap1.keySet()) {
                    String value = (String) ecMap1.get(key);
                    emergencyContacts.addContactInfo(key, value);
                    // Process the key-value pair
                }

                items = emergencyContacts.getEmergencyContactsList();

                emergencyContactsAdapter = new EmergencyContactsAdapter(getContext(), items, new EmergencyContactsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EmergencyContact contact) {
                        showEditContactDialog(contact);
                    }
                });

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(emergencyContactsAdapter);

            }
        });
    }

    public void showEditContactDialog(EmergencyContact contact) {
        EditEmergenyContactFragment editEmergenyContactFragment = EditEmergenyContactFragment.newInstance(contact, emergencyContacts);
        editEmergenyContactFragment.setOnListener(this);
        editEmergenyContactFragment.show(getChildFragmentManager(), "EditEmergencyContact");
    }

    public void showAddContactDialog() {
        AddEmergencyContactFragment addEmergencyContactFragment = AddEmergencyContactFragment.newInstance(emergencyContacts);
        addEmergencyContactFragment.setOnListener(this);
        addEmergencyContactFragment.show(getChildFragmentManager(), "AddEmergencyContact");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRefresh(EmergencyContacts emergencyContacts) {
        items = emergencyContacts.getEmergencyContactsList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(emergencyContactsAdapter);
    }
}