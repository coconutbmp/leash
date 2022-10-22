package com.coconutbmp.leash.GraphAbstractions;

import android.widget.Switch;

import com.coconutbmp.leash.BudgetComponents.Frequency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;

public class LineAbstractions {

    public static LineDataSet overTimeLineDataSet(String name, LocalDate start, LocalDate end, double regular_amount, Frequency frequency){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return null;

        LineDataSet data_set = new LineDataSet(new ArrayList<Entry>(), name);

        LocalDate date = LocalDate.from(start);

        int month_counter = (date.getYear() - LocalDate.MIN.getYear()) * 12 + date.getMonth().getValue() - LocalDate.MIN.getMonth().getValue();

        while (date.isBefore(end)){
            switch (frequency) {
                case MONTHLY:
                    date = date.plusMonths(1);
                    month_counter += 1;
                    break;

                case YEARLY:
                    date = date.plusYears(1);
                    month_counter += 12;
                    break;

                case DAILY:
                    date = date.plusDays(1);
                    if(date.getMonth().getValue() - date.minusDays(1).getMonth().getValue() != 0)
                        // step into new month
                        month_counter += 1;

                    break;

                case WEEKLY:
                    date = date.plusDays(7);
                    if(date.getMonth().getValue() - date.minusDays(7).getMonth().getValue() != 0)
                        // step into new month
                        month_counter += 1;
                    break;

                case QUARTERLY:
                    date = date.plusMonths(3);
                    month_counter += 4;
                    break;
            }

            data_set.addEntry(new Entry( month_counter + (float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) regular_amount));
        }
        return data_set;
    }

    public LineDataSet debtRemainderSet(LineDataSet payments, LocalDate start, Frequency calc_frequency, double interest_percentage, double principle){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return null;

        LineDataSet remainder_set = new LineDataSet(new ArrayList<Entry>(), "Remaining Debt");

        double r = 0;
        double i = interest_percentage /100d;

        switch (calc_frequency){
            case DAILY:r = i/365;
            case WEEKLY:r = i/52;
            case MONTHLY:r = i/12;
            case QUARTERLY:r = i/4;
            case YEARLY:r = i;
        }

        for (int index = 0; index < payments.getEntryCount(); index++) {
            Entry e = payments.getEntryForIndex(index);

            switch (calc_frequency){
                case DAILY:

                    break;
                case WEEKLY:

                    break;
                case MONTHLY:

                    break;
                case QUARTERLY:

                    break;
                case YEARLY:

                    break;
            }

            principle = principle * (1+r);
            principle = principle - e.getY();
            remainder_set.addEntry(new Entry(e.getX(), (float) principle));

        }



        return remainder_set;
    }



}
