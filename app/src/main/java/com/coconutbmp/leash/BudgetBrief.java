package com.coconutbmp.leash;

import android.content.Context;
import android.widget.TextView;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.coconutbmp.leash.BudgetComponents.Income;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import java.time.LocalDate;

public class BudgetBrief extends BudgetComponentFragment{
    private Budget budget;
    private LineChart chart;


    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        details_ll.removeAllViews();

        name_label.setText("Last Month Summary");

        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now().minusMonths(1);
            date = date.minusDays(date.getDayOfMonth()-1);

            chart = new LineChart(getContext());
            chart.setData(budget.getPeriodSummary(date, date.plusMonths(1)));
        }

        chart.setMinimumHeight(600);
        XAxis axis = chart.getXAxis();
        axis.setGranularity(1f);
        axis.setGranularityEnabled(true);
        axis.setAxisLineWidth(1);
        chart.setVisibleXRangeMaximum(1f);
        chart.setVisibleXRangeMinimum(1f);


        details_ll.addView(chart);
    }
    public BudgetBrief(Context context, Budget budget) {
        super();
        this.budget = budget;
    }


}
