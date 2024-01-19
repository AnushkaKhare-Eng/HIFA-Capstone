package com.example.hifa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                                date.setText(day+"/"+month+"/"+year);

                            }
                        },0,0,0 );
                datePickerDialog.show();
            }
        });
    }
}