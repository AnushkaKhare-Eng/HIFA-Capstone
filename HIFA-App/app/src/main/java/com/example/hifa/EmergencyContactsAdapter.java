package com.example.hifa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyContactsAdapter extends RecyclerView.Adapter<MyViewHolderEmergencyContacts> {

    Context context;
    List<EmergencyContact> items;
    EmergencyContacts emergencyContacts;
    private Button saveChangesAdapterButton;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EmergencyContact contact);
    }

    public EmergencyContactsAdapter(Context context, List<EmergencyContact> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    public EmergencyContactsAdapter(Context context, List<EmergencyContact> items) {
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
        //getting the names from the EC frag
        EmergencyContact emergencyContact = items.get(holder.getAbsoluteAdapterPosition());
        holder.emergencyContactName.setVisibility(View.VISIBLE);

        String name = emergencyContact.getName();
        //setting the text
        holder.emergencyContactName.setText(name);
        System.out.println("Name: " + name);

        EmergencyContact item = items.get(holder.getAbsoluteAdapterPosition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyContact item = items.get(holder.getAbsoluteAdapterPosition());
                String names = item.getName();
                String phone = item.getPhoneNo();
//                Toast toast = Toast.makeText(view.getContext(), "name = " + name + " phone = " + phone, Toast.LENGTH_SHORT);
//                toast.show();
                listener.onItemClick(item);
                }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
