package com.coconutbmp.leash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class AddToBudgetDialogue extends Dialog {

    CardView returnCard;
    Button add_transact_button, add_liability_button, add_income_button;
    String budget_name;

    /**
     * Constructor
     * @param context of the parent Activity
     */
    public AddToBudgetDialogue(@NonNull Context context) {
        super(context);
        Budget b = (Budget) context;
        budget_name = b.getBudgetName();
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // dont show default title for this dialog
        setContentView(R.layout.add_to_budget_dialog); // link to xml file

        // link to elements in the xml file
        add_transact_button = findViewById(R.id.add_transaction_button);
        add_income_button = findViewById(R.id.add_income_button);
        add_liability_button = findViewById(R.id.add_liability_button);
        returnCard = findViewById(R.id.ReturnCard);

        add_liability_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddLiabilityActivity.class);
            i.putExtra("budget_name", budget_name); // pass data useful for the AddLiabilityActivity
            this.getContext().startActivity(i);
        });

        add_income_button.setOnClickListener(view -> {
            Intent i = new Intent(this.getContext(), AddIncomeActivity.class);
            i.putExtra("budget_name", budget_name); // pass data useful for the AddIncomeActivity
            this.getContext().startActivity(i);
        });

        returnCard.setOnClickListener(view -> {
            AddToBudgetDialogue.this.dismiss();
        });

        //todo: set up transitions to the transaction page.
    }
}
