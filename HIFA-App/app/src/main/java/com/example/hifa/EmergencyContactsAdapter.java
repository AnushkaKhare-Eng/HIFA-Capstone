package com.example.hifa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyContactsAdapter extends RecyclerView.Adapter<MyViewHolderEmergencyContacts> {

    Context context;
    List<EmergencyContacts> items;

    public EmergencyContactsAdapter(Context context, List<EmergencyContacts> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolderEmergencyContacts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderEmergencyContacts(LayoutInflater.from(context).inflate(R.layout.emergency_contact_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEmergencyContacts holder, int position) {
        String name = items.get(holder.getAbsoluteAdapterPosition()).getName();
        holder.emergencyContactName.setVisibility(View.VISIBLE);
        holder.emergencyContactName.setText(name);
        System.out.println("Name: " + name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = items.get(holder.getAbsoluteAdapterPosition()).getName();
                Toast toast = Toast.makeText(view.getContext(), "name = " + name, Toast.LENGTH_SHORT);
                toast.show();
                }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
