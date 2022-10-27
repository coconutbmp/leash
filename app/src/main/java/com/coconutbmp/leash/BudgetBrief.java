package com.coconutbmp.leash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.coconutbmp.leash.BudgetComponents.Income;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;

public class BudgetBrief extends BudgetComponentFragment{
    private Budget budget;
    private LineChart chart;


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    void initiate_view() throws Exception{
        super.initiate_view();
        details_ll.removeAllViews();

        name_label.setText("Last Month Summary");

        LocalDate date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        chart.getDescription().setText("Income vs Expenditure");

        PieChart pc = new PieChart(getContext());
        PieDataSet pds = new PieDataSet(new ArrayList<PieEntry>(), "");
        pc.getDescription().setText("Income vs Expenditure");

        ArrayList<Integer> colors = new ArrayList<>();
        LineData ld = chart.getLineData();
        for (ILineDataSet lds: ld.getDataSets()){
            pds.addEntry(new PieEntry(lds.getEntryForIndex(lds.getEntryCount()-1).getY(), lds.getLabel()));
            colors.add(lds.getColor());
        }

        pds.setColors(colors);

        PieData pd = new PieData(pds);

        pc.setData(pd);
        pc.setMinimumHeight(600);
        pds.setSliceSpace(3f);

        details_ll.addView(new TextView(getContext()));

        RadioGroup rg = new RadioGroup(getContext());
        RadioButton rb = new RadioButton(getContext());

        rb.setText("Line Chart");
        rg.addView(rb);
        rb.setId(1);
        rb = new RadioButton(getContext());
        rb.setText("Pie Chart");
        rg.addView(rb);
        rb.setId(2);

        rg.setOnCheckedChangeListener((group, index) -> {
            System.out.println(index);
            details_ll.removeView(details_ll.getChildAt(0));
            if(index == 1) {
                details_ll.addView(chart, 0);
            } else {
                details_ll.addView(pc, 0);
            }
        });
        details_ll.addView(rg);

        rg.check(1);
    }
    public BudgetBrief(Context context, Budget budget) {
        super();
        this.budget = budget;
        this.budget_comp = budget;
    }


}
