package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

public class Budget extends AppCompatActivity {

    CardView add_button;
    String budget_name;
    TextView budget_title;

    String getBudgetName(){
        return  budget_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        Bundle b = getIntent().getExtras();
        budget_name = getIntent().getExtras().getString("budget_name");
        System.out.println("    --->    " + budget_name);
        System.out.println(b.toString());

        budget_title = findViewById(R.id.budget_title_label);
        budget_title.setText(budget_name);

        add_button = findViewById(R.id.btnAddSomething);
        add_button.setOnClickListener(view -> {
            AddToBudgetDialogue dialogue = new AddToBudgetDialogue(this);
            dialogue.show();
        });
    }
}