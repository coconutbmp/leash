package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    CardView add_budget_button, btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setDate();

        btnAdd = findViewById(R.id.btnAddSomething);

        btnAdd.setOnClickListener(view -> {
            AddBudgetDialogue dialogue = new AddBudgetDialogue(this);
            dialogue.show();
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
