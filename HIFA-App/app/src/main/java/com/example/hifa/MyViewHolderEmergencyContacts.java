package com.example.hifa;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyViewHolderEmergencyContacts extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView emergencyContactName;
    public MyViewHolderEmergencyContacts(@NonNull View itemView) {
        super(itemView);

        emergencyContactName = (TextView) itemView.findViewById(R.id.emergency_contact_name);
    }

    @Override
    public void onClick(View view) {
    }
}
