package com.example.hifa;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyViewHolderSubSettings extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    TextView titleView;


    public MyViewHolderSubSettings(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.subsetting_icon);
        titleView = itemView.findViewById(R.id.subsetting_title);

    }

    @Override
    public void onClick(View view) {
        Toast toast = Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT);
        toast.show();
    }
}
