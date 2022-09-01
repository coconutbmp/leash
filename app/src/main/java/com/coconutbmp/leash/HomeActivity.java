package com.coconutbmp.leash;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setDate();
    }

    public void setDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        TextView day = findViewById(R.id.lblDay);
        TextView month = findViewById(R.id.lblMonth);

        String today = sdf.format(new Date());

        day.setText(today.substring(0, 2));
        month.setText(today.substring(3, 6));
    }
}
