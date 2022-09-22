package com.coconutbmp.leash.BudgetComponents;

import android.annotation.SuppressLint;

import com.coconutbmp.leash.InternetRequest;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Locale;
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

    public LineDataSet generateLineData(){
        try {
            LineDataSet liability_data_set = new LineDataSet(new ArrayList<Entry>(), getJsonRep().getString("name"));
            String date_string = getJsonRep().getString("start_date");
            String[] dates = date_string.split("-");
            Frequency f = Frequency.valueOf(getJsonRep().getString("payment_frequency").toUpperCase());
            LocalDate date;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                 date = LocalDate.of(
                            Integer.parseInt(dates[0]),
                            Integer.parseInt(dates[1]),
                            Integer.parseInt(dates[2]));

                 LocalDate start = LocalDate.from(date);

                 for (int t = 0; t < 30; t++){ // create 30 transactions
                    liability_data_set.addEntry(new Entry(date.toEpochDay() - start.toEpochDay(), (float) getJsonRep().getDouble("payment_amount")));
                    System.out.println("-> " + Long.toString(date.toEpochDay() - start.toEpochDay()));
                    if(f == Frequency.MONTHLY){
                        date = date.plusMonths(1);
                    }
                 }
            } else {
                System.out.println("Your phone is wack>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>....");
            }
            return liability_data_set;
        } catch ( Exception e ) {
            return null;
        }
    }

    public Liability(Budget parent, JSONObject json_rep) {
        super(parent, json_rep);
    }
}
