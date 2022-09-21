package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.coconutbmp.leash.BudgetComponents.Liability;

/**
 * Controller for the Budget Page
 */
public class BudgetActivity extends AppCompatActivity {
    CardView add_button, return_button;
    String budget_name;
    TextView budget_title, day, month;
    LinearLayout liability_ll, income_ll, transaction_ll;

    String getBudgetName(){
        return  budget_name;
    }

    void display_liabilities(){
        Budget current = Data.current;
        for (Liability l: current.getLiabilities()) {
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();

            frag_tran.add(liability_ll.getId(), new LiabilityBrief(this, l)).commit();
        }
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

        liability_ll = findViewById(R.id.liability_holder_ll);

        UXFunctions.setDate(day, month);

        try {
            budget_name = Data.current.getJsonRep().getString("budget_Name");
        } catch (Exception e){
            e.printStackTrace();
        }

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

        display_liabilities();
    }
}