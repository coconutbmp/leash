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
import com.coconutbmp.leash.BudgetComponents.Income;
import com.coconutbmp.leash.BudgetComponents.Liability;
import com.coconutbmp.leash.BudgetComponents.Transaction;

/**
 * Controller for the Budget Page
 */
public class BudgetActivity extends AppCompatActivity {
    CardView add_button, return_button;
    String budget_name;
    TextView budget_title, day, month;
    LinearLayout liability_ll, income_ll, transaction_ll, budget_summary_ll;

    String getBudgetName(){
        return  budget_name;
    }

    void display_liabilities(){
        Budget current = Data.current;
        liability_ll.removeAllViews();
        for (Liability l: current.getLiabilities()) {
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(liability_ll.getId(), new LiabilityBrief(this, l)).commit();
        }
    }

    void display_transactions(){
        Budget current = Data.current;
        transaction_ll.removeAllViews();
        for(Transaction t: current.getTransactions()){
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(transaction_ll.getId(),new TransactionBrief(this, t)).commit();
        }
    }

    void display_income(){
        Budget current = Data.current;
        income_ll.removeAllViews();
        for (Income i: current.getIncomes()) {
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(income_ll.getId(), new IncomeBrief(this, i)).commit();
        }
    }

    void display_budget_summary(){
        Budget current = Data.current;
        budget_summary_ll.removeAllViews();
        FragmentManager frag_man = getSupportFragmentManager();
        FragmentTransaction frag_tran = frag_man.beginTransaction();
        frag_tran.add(budget_summary_ll.getId(), new BudgetBrief(this, Data.current)).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // give context for async resume functions to run on ui Thread
        Data.setCurrentActivity(this);

        setContentView(R.layout.activity_budget);

        budget_title = findViewById(R.id.budget_title_label);
        add_button = findViewById(R.id.btnAddSomething);
        return_button = findViewById(R.id.ReturnCard);
        day = findViewById(R.id.lblBudgetDay);
        month = findViewById(R.id.lblBudgetMonth);

        liability_ll = findViewById(R.id.liability_holder_ll);
        transaction_ll = findViewById(R.id.transaction_holder_ll);
        income_ll = findViewById(R.id.income_holder_ll);
        budget_summary_ll = findViewById(R.id.budget_summary_ll);

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
        display_transactions();
        display_income();
        display_budget_summary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        display_liabilities();
        display_transactions();
        display_income();
        display_budget_summary();
    }
}