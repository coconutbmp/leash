package com.coconutbmp.leash;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.coconutbmp.leash.BudgetComponents.Liability;

import org.json.JSONObject;

public class LiabilityBrief extends BudgetComponentFragment {

    Liability liability;

    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        JSONObject json_rep = liability.getJsonRep();
        System.out.println(liability.getJsonRep());
        this.name_label.setText((String) liability.getJsonRep().get("name"));
        this.details_ll.removeAllViews();

        TextView tv = new TextView(this.getContext());
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText("Paying: R" + (String) json_rep.get("payment_amount") + ", " + (String) json_rep.get("payment_frequency"));
        this.details_ll.addView(tv);

    }

    LiabilityBrief(Context context, Liability liability){
        super();
        this.liability = liability;
    }
}
