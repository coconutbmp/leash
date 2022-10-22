package com.coconutbmp.leash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class AddToBudgetDialogue extends Dialog {
    Button add_transact_button, add_liability_button, add_income_button, cancel_button;
    String budget_name;

    /**
     * Constructor
     * @param context of the parent Activity
     */
    public AddToBudgetDialogue(@NonNull Context context) {
        super(context);
        BudgetActivity b = (BudgetActivity) context;
        budget_name = b.getBudgetName();
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // don't show default title for this dialog
        setContentView(R.layout.dialog_add_to_budget); // link to xml file

        // link to elements in the xml file
        add_transact_button = findViewById(R.id.add_transaction_button);
        add_income_button = findViewById(R.id.add_income_button);
        add_liability_button = findViewById(R.id.edit_button);
        cancel_button = findViewById(R.id.btnCancelAddTo);

        add_liability_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddLiabilityActivity.class);
            i.putExtra("budget_name", budget_name); // pass data useful for the AddLiabilityActivity
            this.getContext().startActivity(i);
            this.dismiss();
        });

        add_income_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddIncomeActivity.class);
            i.putExtra("budget_name", budget_name); // pass data useful for the AddIncomeActivity
            this.getContext().startActivity(i);
            this.dismiss();
        });

        add_transact_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddTransactionActivity.class);
            i.putExtra("budget_name", budget_name); // pass data useful for the AddIncomeActivity
            this.getContext().startActivity(i);
            this.dismiss();
        });

        cancel_button.setOnClickListener(view -> {
            AddToBudgetDialogue.this.dismiss();
        });
    }
}
