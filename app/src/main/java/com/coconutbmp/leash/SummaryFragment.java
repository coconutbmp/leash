package com.coconutbmp.leash;

import static java.lang.Thread.sleep;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Fragment to Show Summaries for some data
 */
public class SummaryFragment extends Fragment {

    RadarChart income_rc;
    RadarChart spending_rc;
    View thisveiw;
    ArrayList<LineData> line_data_list = new ArrayList<>();

    void retrieve_data(){
        ArrayList<Budget> budgets = Data.getAll();

        for (Budget budget: budgets) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate date = LocalDate.now().minusMonths(1);
                date = date.minusDays(date.getDayOfMonth()-1);
                LineData ld = budget.getPeriodSummary(date, date.plusMonths(1));
                line_data_list.add(ld);
            }
        }
    }

    void display_income_summary(){
        income_rc.setMinimumHeight(600);

        ArrayList<RadarEntry> re_list = new ArrayList<>();
        RadarDataSet rds = new RadarDataSet(re_list, "Income");
        for(int i = 0; i < line_data_list.size(); i++){
            LineData data = line_data_list.get(i);
            ILineDataSet lds = data.getDataSets().get(0);
            try {
                RadarEntry re = new RadarEntry(lds.getEntryForIndex(lds.getEntryCount() - 1).getY(), Data.getAll().get(i).getJsonRep().get("budget_Name"));
                rds.addEntry(re);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(line_data_list.size() > 0){
            ILineDataSet lds = line_data_list.get(0).getDataSetByIndex(0);
            rds.setColor(lds.getColor());
            rds.setFillColor(lds.getFillColor());
            rds.setFillAlpha(lds.getFillColor());
            rds.setDrawFilled(true);
        }


        income_rc.setData(new RadarData(rds));
        income_rc.getData().setLabels();

        income_rc.getYAxis().setAxisMinimum(0f);

        income_rc.getDescription().setText("Income Chart");
        income_rc.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis){
                String res = "";
                try {
                    res = (String) Data.getAll().get((int)value).getJsonRep().get("budget_Name");
                }catch (Exception e) {
                    e.printStackTrace();
                }

                return res;
            }
        });
    }

    void display_spending_summary(){
        spending_rc.setMinimumHeight(600);

        ArrayList<RadarEntry> re_list = new ArrayList<>();
        RadarDataSet rds = new RadarDataSet(re_list, "Costs");
        for(int i = 0; i < line_data_list.size(); i++){
            LineData data = line_data_list.get(i);
            ILineDataSet lds = data.getDataSets().get(1);
            try {
                RadarEntry re = new RadarEntry(lds.getEntryForIndex(lds.getEntryCount() - 1).getY(), Data.getAll().get(i).getJsonRep().get("budget_Name"));
                rds.addEntry(re);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(line_data_list.size() > 0){
            ILineDataSet lds = line_data_list.get(0).getDataSetByIndex(1);
            rds.setColor(lds.getColor());
            rds.setFillColor(lds.getFillColor());
            rds.setFillAlpha(lds.getFillColor());
            rds.setDrawFilled(true);
        }


        spending_rc.setData(new RadarData(rds));
        spending_rc.getData().setLabels();

        spending_rc.getYAxis().setAxisMinimum(0f);
        spending_rc.getDescription().setText("Spending Chart");

        spending_rc.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis){
                String res = "";
                try {
                    res = (String) Data.getAll().get((int)value).getJsonRep().get("budget_Name");
                }catch (Exception e) {
                    e.printStackTrace();
                }

                return res;
            }
        });
    }



    void display_summaries() throws InterruptedException {

        sleep(500);
        while (Data.getLockCount() != 0){
            sleep(100);
        }
        System.out.println("everything has been loaded");
        line_data_list.clear();

        getActivity().runOnUiThread(()->{
            retrieve_data();
            display_income_summary();
            display_spending_summary();
        });

    }

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        thisveiw = view;
        income_rc = view.findViewById(R.id.income_summary);
        spending_rc = view.findViewById(R.id.spending_summary);

        return view;
    }

    public void refresh(){
        Thread summary_thread = new Thread(){

            @Override
            public void run(){
                try {
                    display_summaries();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        summary_thread.start();
    }
}