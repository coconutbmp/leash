package com.coconutbmp.leash;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.coconutbmp.leash.BudgetComponents.BudgetComponent;

public class ManageBudgetDialog extends Dialog {

    Button edit_button, delete_button;
    BudgetComponent subject;

    public ManageBudgetDialog(@NonNull Context context, BudgetComponent subject) {
        super(context);
        setContentView(R.layout.dialog_manage_budget_component); // link to xml file
        this.subject = subject;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        edit_button = findViewById(R.id.edit_button);
        delete_button = findViewById(R.id.delete_button);

        edit_button.setOnClickListener(view -> {
            Toast.makeText(this.getContext(), "Editing is currently unavailable.", Toast.LENGTH_LONG).show();
            this.dismiss();
        });

        delete_button.setOnClickListener(view -> {
            Toast.makeText(this.getContext(), "Deleting " + subject.getJsonRep(), Toast.LENGTH_LONG).show();
            Data.setCurrentActivity(this.getOwnerActivity());
            subject.delete();
            this.dismiss();
        });
    }


}
