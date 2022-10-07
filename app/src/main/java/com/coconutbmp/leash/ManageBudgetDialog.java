package com.coconutbmp.leash;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.coconutbmp.leash.BudgetComponents.BudgetComponent;

public class ManageBudgetDialog extends Dialog {

    Button edit_button, delete_button;

    public ManageBudgetDialog(@NonNull Context context, BudgetComponent subject) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
}
