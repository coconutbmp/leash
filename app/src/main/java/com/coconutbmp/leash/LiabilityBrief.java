package com.coconutbmp.leash;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.coconutbmp.leash.BudgetComponents.Liability;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        XAxis axis = lc.getXAxis();
        axis.setValueFormatter(new ValueFormatter() {
            SimpleDateFormat date_format = new SimpleDateFormat("MMM dd yy", Locale.ENGLISH);
            SimpleDateFormat year_format = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                long millis = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    millis = TimeUnit.DAYS.toMillis((long) value);
                }
                return date_format.format(new Date(millis));

            }
        });

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i = 0; i < 1000; i++){
            entries.add(new Entry(i, (float)Math.sin((double) i/100)));
        }

        ld.addDataSet(liability.generateLineData());
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
