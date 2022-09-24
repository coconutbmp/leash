package com.coconutbmp.leash;

import android.content.Context;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.coconutbmp.leash.BudgetComponents.Income;

public class IncomeBrief extends BudgetComponentFragment {
    private Income income;


    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        details_ll.removeAllViews();

        name_label.setText((String)income.getJsonRep().get("income_Source"));
        TextView income_tv = new TextView(getContext());
        income_tv.setText(String.format("Receiving: R%s, %s", income.getJsonRep().get("income_Amount"), income.getJsonRep().get("income_Frequency")));
        details_ll.addView(income_tv);
    }
    public IncomeBrief(Context context, Income income) {
        super();
        this.income = income;
    }
}
