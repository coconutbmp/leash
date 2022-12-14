package com.coconutbmp.leash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.coconutbmp.leash.BudgetComponents.BudgetComponent;

public class ManageBudgetDialog extends Dialog {

    Button edit_button, delete_button;
    BudgetComponent subject;
    Activity current_activity;

    public ManageBudgetDialog(Activity activity, @NonNull Context context, BudgetComponent subject) {
        super(context);
        current_activity = activity;
        setContentView(R.layout.dialog_manage_budget_component); // link to xml file
        this.subject = subject;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        delete_button = findViewById(R.id.delete_button);

        delete_button.setOnClickListener(view -> { // what happens when deletion is started
            Data.setCurrentActivity(current_activity);
            Data.setCurrentListener(resp ->
            {
                if(resp) {
                    Toast.makeText(this.getContext(), "Deletion Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Deletion Failed", Toast.LENGTH_SHORT).show();
                }
                ((BudgetActivity)current_activity).refresh();
                this.dismiss();
            });
            subject.delete();
        });
    }




}
