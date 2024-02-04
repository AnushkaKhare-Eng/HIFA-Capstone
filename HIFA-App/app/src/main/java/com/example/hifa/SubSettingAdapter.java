package com.example.hifa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubSettingAdapter extends RecyclerView.Adapter<MyViewHolderSubSettings> {

    Context context;
    List<SubSettingItem> items;

    public SubSettingAdapter(Context context, List<SubSettingItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolderSubSettings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderSubSettings(LayoutInflater.from(context).inflate(R.layout.setting_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSubSettings holder, int position) {
        holder.titleView.setText(items.get(position).getSetting_title());
        holder.imageView.setImageResource(items.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch(position){
                    case 0:
                        replaceFragment(new AccountSettingsFragment(), view);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void replaceFragment(Fragment fragment, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}
