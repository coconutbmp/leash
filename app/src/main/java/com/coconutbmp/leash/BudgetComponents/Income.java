package com.coconutbmp.leash.BudgetComponents;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Income extends BudgetComponent{

    public Income(JSONObject json_rep) {
        super(json_rep);
    }

    ArrayList<Entry> getPeriodSummary(LocalDate start_date, LocalDate end_date){

        ArrayList<Entry> entries = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) try {
            Frequency f = Frequency.valueOf(getJsonRep().getString("income_Frequency").toUpperCase());
            LocalDate income_start = Liability.stringToLocalDate(getJsonRep().getString("income_StartDate"));
            LocalDate income_end = Liability.stringToLocalDate(getJsonRep().getString("income_EndDate"));
            LocalDate date = null;

            date = LocalDate.from(income_start);

            while (date.isBefore(start_date)){
                if(f == Frequency.MONTHLY) {
                    date = date.plusMonths(1);
                }
            }

            while (date.isEqual(start_date) || (date.isAfter(start_date) && date.isBefore(end_date)))
                if(f == Frequency.MONTHLY){
                    date = date.plusMonths(1);
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+  date.getDayOfMonth() );
                    entries.add(new Entry((float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("income_Amount")));
                } else if (f == Frequency.WEEKLY){
                    date = date.plusDays(7);
                }

        }catch (Exception e){
            e.printStackTrace();
        }

        return entries;
    }
}
