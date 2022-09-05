package com.coconutbmp.leash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class AddToBudgetDialogue extends Dialog {

    Button add_transact_button, add_liability_button, add_income_button;
    String budget_name;

    public AddToBudgetDialogue(@NonNull Context context) {
        super(context);
        Budget b = (Budget) context;
        budget_name = b.getBudgetName();
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_to_budget_dialog);

        add_transact_button = findViewById(R.id.add_transaction_button);
        add_income_button = findViewById(R.id.add_income_button);
        add_liability_button = findViewById(R.id.add_liability_button);

        add_liability_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddLiabilityActivity.class);
            i.putExtra("budget_name", budget_name);
            this.getContext().startActivity(i);
        });

        add_income_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddIncomeActivity.class);
            i.putExtra("budget_name", budget_name);
            this.getContext().startActivity(i);
        });

        //todo: set up transitions to the transaction page.
    }
}
