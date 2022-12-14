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
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LiabilityBrief extends BudgetComponentFragment {

    Liability liability;
    LineChart lc;
    LocalDate now;

    @Override
    void initiate_view() throws Exception{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) now = LocalDate.now();

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
        axis.setGranularity(1f);
        axis.setGranularityEnabled(true);
        axis.setAxisLineWidth(1);
        axis.setValueFormatter(new ValueFormatter() {
            SimpleDateFormat date_format = new SimpleDateFormat("MMM", Locale.ENGLISH);
            SimpleDateFormat year_format = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                long millis = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { try {
                    LocalDate start = Liability.stringToLocalDate(liability.getJsonRep().getString("start_date"));

                    assert start != null;
                    start = start.minusDays(start.getDayOfMonth()-1);
                    start = start.plusMonths((int) value);

                    LocalDateTime ldt = LocalDateTime.of(start, LocalTime.MIN.plusNanos(1));
                    millis = ldt.toEpochSecond(ZoneOffset.ofHours(2)) * 1000L;

                    if(start.getMonthValue() == now.getMonthValue() && start.getYear() == now.getYear()){
                        return "NOW";
                    } else if(start.getMonthValue() == 1){
                        return year_format.format(new Date(millis));
                    } else {
                        return date_format.format(new Date(millis));
                    }

                }catch (Exception e){e.printStackTrace(); return null;}}

            return null;
            }
        });

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i = 0; i < 1000; i++){
            entries.add(new Entry(i, (float)Math.sin((double) i/100)));
        }

        //ld.addDataSet(liability.generatePaymentSet());

        try {
            if (liability.getJsonRep().getString("type").equals("loan")) {
                for (LineDataSet lds: liability.generateOverTimeAnalysis()) {
                    ld.addDataSet(lds);
                }
            } else {
                ld.addDataSet(liability.generatePaymentSet());
            }
        } catch (Exception e) { e.printStackTrace(); }
        ld.setDrawValues(false);

        lc.setData(ld);

        lc.setMinimumHeight(500);
        lc.setVisibleXRangeMaximum(5f);


        details_ll.addView(lc);

        this.details_ll.addView(tv);

    }

    LiabilityBrief(Context context, Liability liability){
        super();
        this.liability = liability;
        this.budget_comp = liability;
    }
}
