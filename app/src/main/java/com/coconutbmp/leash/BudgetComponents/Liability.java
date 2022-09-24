package com.coconutbmp.leash.BudgetComponents;

import android.graphics.Color;

import com.coconutbmp.leash.InternetRequest;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

public class Liability extends BudgetComponent{

    Vector<Transaction> liability_transactions = new Vector<Transaction>();
    InternetRequest ir = new InternetRequest();

    void add_transactions(String response){
        JSONArray ja;
        System.out.println(response);
        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                liability_transactions.add(new Transaction((JSONObject) ja.get(i)));
            }
            System.out.println("got the array -> " + ja.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(){
        try {
            JSONObject jo = new JSONObject();
            jo.put("", ""); //todo: put needed values
            /*ir.doRequest(
                    InternetRequest.std_url + "get_liability_transactions.php",
                    null,
                    jo,
                    this::add_transactions
            );*/
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static LocalDate stringToLocalDate(String s){
        if(!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)) return null;
        String[] dates = s.split("-");
        return LocalDate.of(
                Integer.parseInt(dates[0]),
                Integer.parseInt(dates[1]),
                Integer.parseInt(dates[2]));
    }

    ArrayList<Entry> getPeriodSummary(LocalDate start_date, LocalDate end_date) {

        ArrayList<Entry> entries = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) try {
            Frequency f = Frequency.valueOf(getJsonRep().getString("payment_frequency").toUpperCase());
            LocalDate liability_start = Liability.stringToLocalDate(getJsonRep().getString("start_date"));
            LocalDate liability_end = Liability.stringToLocalDate(getJsonRep().getString("end_date"));
            LocalDate date = null;

            date = LocalDate.from(liability_start);

            while (date.isBefore(start_date)) {
                if (f == Frequency.MONTHLY) {
                    date = date.plusMonths(1);
                }
            }

            while (date.isEqual(start_date) || (date.isAfter(start_date) && date.isBefore(end_date)))
                if (f == Frequency.MONTHLY) {
                    date = date.plusMonths(1);
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + date.getDayOfMonth());
                    entries.add(new Entry((float) date.getDayOfMonth() / date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("payment_amount")));
                } else if (f == Frequency.WEEKLY) {
                    date = date.plusDays(7);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }

    public LineDataSet generatePaymentSet(){
        try {
            LineDataSet liability_data_set = new LineDataSet(new ArrayList<Entry>(), getJsonRep().getString("name"));
            liability_data_set.setLineWidth(2f);
            liability_data_set.setFillAlpha(50);
            liability_data_set.setFillColor(Color.rgb(255,34,100));
            liability_data_set.setDrawFilled(true);
            liability_data_set.setCircleColor(Color.rgb(255,34,100));
            liability_data_set.setColor(Color.rgb(255,34,100));
            Frequency f = Frequency.valueOf(getJsonRep().getString("payment_frequency").toUpperCase());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate start_date = stringToLocalDate(getJsonRep().getString("start_date"));
                LocalDate end_date = stringToLocalDate(getJsonRep().getString("end_date"));
                LocalDate date = LocalDate.from(start_date);

                int month_counter = 0;

                // get all payments until the end of the loan period
                while (date.isBefore(end_date) || date.isEqual(end_date)){
                    liability_data_set.addEntry(new Entry( month_counter + (float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("payment_amount")));
                    //System.out.println("-> " + (month_counter + (float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear())));
                    if(f == Frequency.MONTHLY){
                        date = date.plusMonths(1);
                        month_counter+=1;
                    }
                }
            }
            return liability_data_set;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    private LineDataSet generateRemainderSet(LineDataSet payment_entries){

        LineDataSet remainder_set = new LineDataSet(new ArrayList<Entry>(), "Remaining Debt");
        remainder_set.setLineWidth(2f);
        remainder_set.setFillAlpha(50);
        remainder_set.setFillColor(Color.rgb(255,255,100));
        remainder_set.setDrawFilled(true);
        remainder_set.setCircleColor(Color.rgb(255,255,100));
        remainder_set.setColor(Color.rgb(255,255,100));

        try {
            Frequency f = Frequency.valueOf(getJsonRep().getString("loan_calc_frequency").toUpperCase());

            double i = getJsonRep().getDouble("loan_interest_rate")/100;
            double r = i/12;
            double principle = this.getJsonRep().getDouble("loan_principle");

            for (int index = 0; index < payment_entries.getEntryCount(); index++) {
                Entry e = payment_entries.getEntryForIndex(index);
                principle = principle * (1+r);
                principle = principle - e.getY();
                remainder_set.addEntry(new Entry(e.getX(), (float) principle));

            }
            return  remainder_set;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<LineDataSet> generateOverTimeAnalysis(){
        ArrayList<LineDataSet> data_sets = new ArrayList<>();

        LineDataSet payment_set = generatePaymentSet();
        data_sets.add(payment_set);
        data_sets.add(generateRemainderSet(payment_set));

        return data_sets;
    }

    public Liability(Budget parent, JSONObject json_rep) {
        super(parent, json_rep);
    }
}
