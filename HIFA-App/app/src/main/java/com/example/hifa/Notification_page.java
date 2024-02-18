package com.example.hifa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification_page extends Fragment {
    View view;

    public Notification_page() {
    }
    // TODO: Rename and change types and number of parameters
    public static Notification_page newInstance(String param1, String param2) {
        Notification_page fragment = new Notification_page();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notificationpage2, container, false);
        return view;
    }
}