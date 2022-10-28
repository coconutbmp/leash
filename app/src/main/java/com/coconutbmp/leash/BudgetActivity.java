package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    CardView add_button, return_button, manage_button;
    String budget_name;
    TextView budget_title, day, month;
    LinearLayout liability_ll, income_ll, transaction_ll, budget_summary_ll;
    Button more_liabilities_btn, more_transactions_btn, more_income_btn;

    String getBudgetName(){
        return  budget_name;
    }

    void display_liabilities(){
        Budget current = Data.current;
        liability_ll.removeAllViews();
        int count = 0;

        for (Liability l: current.getLiabilities()) {
            if (more_liabilities_btn.getText().equals("More") && count == 3) break;
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(liability_ll.getId(), new LiabilityBrief(this, l)).commit();
            count ++;
        }
    }

    void display_transactions(){
        Budget current = Data.current;
        transaction_ll.removeAllViews();
        int count = 0;

        for(Transaction t: current.getTransactions()){
            if (more_transactions_btn.getText().equals("More") && count == 3) break;
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(transaction_ll.getId(),new TransactionBrief(this, t)).commit();
            count++;
        }
    }

    void display_income(){
        Budget current = Data.current;
        income_ll.removeAllViews();
        int count = 0;

        for (Income i: current.getIncomes()) {
            if (more_income_btn.getText().equals("More") && count == 3) break;
            FragmentManager frag_man = getSupportFragmentManager();
            FragmentTransaction frag_tran = frag_man.beginTransaction();
            frag_tran.add(income_ll.getId(), new IncomeBrief(this, i)).commit();
            count++;
        }
    }

    void display_budget_summary(){
        Budget current = Data.current;
        budget_summary_ll.removeAllViews();
        FragmentManager frag_man = getSupportFragmentManager();
        FragmentTransaction frag_tran = frag_man.beginTransaction();
        frag_tran.add(budget_summary_ll.getId(), new BudgetBrief(this, Data.current)).commit();
    }

    /**
     * handles toggling of button
     * @param b button
     */
    void toggleMoreLess(Button b){
        if (b.getText().equals("More")){
            b.setText("Less");
        } else {
            b.setText("More");
        }
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
        manage_button = findViewById(R.id.btnManageBudget);

        more_transactions_btn = findViewById(R.id.more_transactions_button);
        more_income_btn = findViewById(R.id.more_income_button);
        more_liabilities_btn = findViewById(R.id.more_liabilities_button);

        more_transactions_btn.setOnClickListener(view -> {
            toggleMoreLess(more_transactions_btn);
            display_transactions();
        });

        more_income_btn.setOnClickListener(view -> {
            toggleMoreLess(more_income_btn);
            display_income();
        });

        more_liabilities_btn.setOnClickListener(view -> {
            toggleMoreLess(more_liabilities_btn);
            display_liabilities();
        });

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

        manage_button.setOnClickListener(view -> {
            ManageBudgetDialog dialog = new ManageBudgetDialog(this, this, Data.current);
            dialog.show();
        });
    }

    public void refresh(){
        System.out.println("refreshing page");
        this.runOnUiThread(() ->{
            if(Data.current != null) {
                display_liabilities();
                display_transactions();
                display_income();
                display_budget_summary();
            } else {
                this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}