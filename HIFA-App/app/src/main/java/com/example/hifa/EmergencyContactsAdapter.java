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
    List<EmergencyContacts> items;
    private Button saveChangesAdapterButton;

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
        List<String> names = items.get(holder.getAbsoluteAdapterPosition()).getNames();
        holder.emergencyContactName.setVisibility(View.VISIBLE);
        // Iterate through the list using an iterator
//        Iterator<String> iterator = names.iterator();
//        while (iterator.hasNext()) {
//            String name = iterator.next();
//            holder.emergencyContactName.setText(name);
//        }

        String name = names.get(0);
        holder.emergencyContactName.setText(name);
        System.out.println("Name: " + name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> names = items.get(holder.getAbsoluteAdapterPosition()).getNames();
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
