package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Controller for the Budget Page
 */
public class BudgetActivity extends AppCompatActivity {
    CardView add_button, return_button;
    String budget_name;
    TextView budget_title, day, month;

    String getBudgetName(){
        return  budget_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budget_title = findViewById(R.id.budget_title_label);
        add_button = findViewById(R.id.btnAddSomething);
        return_button = findViewById(R.id.ReturnCard);
        day = findViewById(R.id.lblBudgetDay);
        month = findViewById(R.id.lblBudgetMonth);

        UXFunctions.setDate(day, month);

        Bundle b = getIntent().getExtras();// get data passed from previous activity
        budget_name = getIntent().getExtras().getString("budget_name");

        budget_title.setText(budget_name);

        add_button.setOnClickListener(view -> {
            AddToBudgetDialogue dialogue = new AddToBudgetDialogue(this);
            dialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogue.show();
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BudgetActivity.this.finish();
            }
        });
    }
}