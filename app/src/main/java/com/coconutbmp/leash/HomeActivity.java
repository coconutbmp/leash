package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    CardView home_return_button, btnAdd;
    TextView day, month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String userID = getIntent().getStringExtra("userID");

        btnAdd = findViewById(R.id.btnAddSomething);
        home_return_button = findViewById(R.id.homeReturnCard);
        day = findViewById(R.id.lblDay);
        month = findViewById(R.id.lblMonth);

        UXFunctions.setDate(day, month);

        btnAdd.setOnClickListener(view -> {
            AddBudgetDialogue dialogue = new AddBudgetDialogue(this, userID);
            dialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogue.show();
        });

        home_return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.finish();
            }
        });
    }
}

