package com.coconutbmp.leash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddBudgetDialogue extends Dialog {

    Button proceed, cancel;
    EditText budget_name_edit;

    public AddBudgetDialogue(Activity act) {
        super(act);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_budget_dialog);
        proceed = findViewById(R.id.add_budget_button);
        cancel = findViewById(R.id.cancel_add_budget_button);
        budget_name_edit = findViewById(R.id.budget_name_edit);

        cancel.setOnClickListener(view -> {
            this.cancel();
        });

        proceed.setOnClickListener(view -> {
            if (budget_name_edit.getText().length() > 0){
                //todo: add code to add the budget to the users account
                Intent i = new Intent(this.getContext(), Budget.class);
                i.putExtra("budget_name", budget_name_edit.getText());
                this.getContext().startActivity(i);
            }

        });
    }
}
