package com.example.hifa;

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

public class EmergencyContactFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    public EmergencyContactFragment() {
        // Required empty public constructor
    }

    public static EmergencyContactFragment newInstance() {
        EmergencyContactFragment fragment = new EmergencyContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    Map<String, String> eCMap;
    String userEmail;
    List<EmergencyContact> items;

    EmergencyContacts emergencyContacts;

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
                AddEmergencyContactFragment AddEmergencyContactFragment = new AddEmergencyContactFragment();
                AddEmergencyContactFragment.show(getChildFragmentManager(), "AddEmergencyContact");
            }
        });

        recyclerView = view.findViewById(R.id.emergency_contacts_recyclerview);

//        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                // Retrieve the data map from the result bundle
//                eCMap = (Map<String, String>) result.getSerializable("requestKey");
//                userEmail = result.getString("userEmail");
//                Log.d("ECFrag","StatementReached");
//                // Handle the received data map here
//            }
//        });
        // passing information to the adapter
        Bundle bundle = getArguments();
        if (bundle != null) {

            // Extract the object from the arguments using the unique key
            userEmail = ((HomeActivity) requireActivity()).getUser().getEmail();
            Log.d("ECFrag", "Recieved User's email"+userEmail);
        }

        DatabaseFirestore.databaseSetUp(FirebaseFirestore.getInstance());
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

                    recyclerView.setAdapter(new EmergencyContactsAdapter(requireContext(), items));

                    // Create a Bundle and put the object into it
                    //sendtoHomeFragment(userObjFirstname);

                }
            });


        List<String> names = new ArrayList<>();
        List<String> phonenums = new ArrayList<>();

// Populate the map with key-value pairs

        //items.add(emergencyContactsObj);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new EmergencyContactsAdapter(this.getContext(), items));

        return view;
    }

//    @Override
//    public void onDataPass(Map<String, String> tempMap) {
//        for (String key : eCMap.keySet()) {
//            String value = eCMap.get(key);
//            items.add(new EmergencyContacts(key, value));
//            // Process the key-value pair
//        }
//        recyclerView.setAdapter(new EmergencyContactsAdapter(this.getContext(), items));
//    }
}