package com.coconutbmp.leash;

import android.content.Context;

import com.coconutbmp.leash.BudgetComponentFragment;
import com.coconutbmp.leash.BudgetComponents.Transaction;

public class TransactionBrief extends BudgetComponentFragment {

    private Transaction transaction;

    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        details_ll.removeAllViews();
        try {
            name_label.setText("Transaction: ");// + transaction.getJsonRep().getString("transaction_Name"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    TransactionBrief (Context context, Transaction transaction){
        super();
        this.transaction = transaction;
    }
}
