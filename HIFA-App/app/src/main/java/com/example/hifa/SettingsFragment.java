package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    EmergencyContactFragment emergencyContactFragment;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(EmergencyContactFragment emergencyContactFragment){
        this.emergencyContactFragment = emergencyContactFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.subsettings_recyclerview);

        List<SubSettingItem> items = new ArrayList<SubSettingItem>();
        items.add(new SubSettingItem("Account", R.drawable.user));
        items.add(new SubSettingItem("Medical Profile", R.drawable.medicalhistory));
        items.add(new SubSettingItem("Notifications", R.drawable.notification));
        items.add(new SubSettingItem("Emergency Contacts", R.drawable.emergencycontact));
        items.add(new SubSettingItem("About", R.drawable.information));
        RelativeLayout relativeLayout = view.findViewById(R.id.logout);

        // Set OnClickListener on the RelativeLayout
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define behavior when RelativeLayout is clicked
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity2.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new SubSettingAdapter(this.getContext(), items, emergencyContactFragment));

        return view;
    }

}