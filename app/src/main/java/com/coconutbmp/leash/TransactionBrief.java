package com.coconutbmp.leash;

import android.content.Context;
import android.widget.TextView;

import com.coconutbmp.leash.BudgetComponentFragment;
import com.coconutbmp.leash.BudgetComponents.Transaction;

public class TransactionBrief extends BudgetComponentFragment {

    private Transaction transaction;

    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        details_ll.removeAllViews();
        TextView more_tv = new TextView(this.getContext());
        try {
            name_label.setText(transaction.getJsonRep().getString("transaction_Type"));
            more_tv.setText(String.format("Paid :R %s On %s", transaction.getJsonRep().get("transaction_Amount"), transaction.getJsonRep().get("transaction_Date")));
            // + transaction.getJsonRep().getString("transaction_Name"));
        } catch (Exception e){
            e.printStackTrace();
        }

        details_ll.addView(more_tv);
    }

    TransactionBrief (Context context, Transaction transaction){
        super();
        this.transaction = transaction;
    }
}
