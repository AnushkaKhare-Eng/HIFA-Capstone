package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button datePickerButton;
    TextView date;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePickerButton = findViewById(R.id.datePickerButton);
        date = findViewById(R.id.datePickerTextView);
        datePickerButton.setOnClickListener(view -> {
            datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (datePicker,))
        });
    }
}