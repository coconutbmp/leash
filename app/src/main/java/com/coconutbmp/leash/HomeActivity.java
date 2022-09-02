package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    CardView add_budget_button, btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setDate();

        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            AddBudgetDialogue dialogue = new AddBudgetDialogue(this);
            dialogue.show();
        });

        add_budget_button = findViewById(R.id.add_budget_card);
        add_budget_button.setOnClickListener(click -> {
            Intent i = new Intent(this, AddLiabilityActivity.class);
            startActivity(i);
        });
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
