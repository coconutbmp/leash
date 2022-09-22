package com.coconutbmp.leash;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.coconutbmp.leash.BudgetComponents.Liability;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LiabilityBrief extends BudgetComponentFragment {

    Liability liability;
    LineChart lc;

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

        lc = new LineChart(getContext());
        LineData ld = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i = 0; i < 1000; i++){
            entries.add(new Entry(i, (float)Math.sin((double) i/100)));
        }
        LineDataSet lds = new LineDataSet(entries, "sine vals");

        ld.addDataSet(lds);
        lc.setData(ld);

        lc.setMinimumHeight(500);

        details_ll.addView(lc);
        this.details_ll.addView(tv);

    }

    LiabilityBrief(Context context, Liability liability){
        super();
        this.liability = liability;
    }
}
