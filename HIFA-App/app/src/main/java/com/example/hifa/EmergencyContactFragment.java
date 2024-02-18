package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactFragment extends Fragment {

    View view;
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

        Button addContactButton = (Button) view.findViewById(R.id.add_contact_button);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEmergencyContactFragment AddEmergencyContactFragment = new AddEmergencyContactFragment();
                AddEmergencyContactFragment.show(getChildFragmentManager(), "AddEmergencyContact");
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.emergency_contacts_recyclerview);

        List<EmergencyContacts> items = new ArrayList<EmergencyContacts>();
        items.add(new EmergencyContacts("Karan", "5875963855"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new EmergencyContactsAdapter(this.getContext(), items));

        return view;
    }
}