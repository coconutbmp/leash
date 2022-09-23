package com.coconutbmp.leash.BudgetComponents;

import android.annotation.SuppressLint;
import android.graphics.Color;

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

    public static LocalDate stringToLocalDate(String s){
        if(!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)) return null;
        String[] dates = s.split("-");
        return LocalDate.of(
                Integer.parseInt(dates[0]),
                Integer.parseInt(dates[1]),
                Integer.parseInt(dates[2]));
    }

    public LineDataSet generateLineData(){
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
                LocalDate prev_date = LocalDate.from(date);

                Integer month_counter = 0;
                while (date.isBefore(end_date) || date.isEqual(end_date)){ // create 30 transactions
                    liability_data_set.addEntry(new Entry( month_counter + (float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("payment_amount")));
                    System.out.println("-> " + (month_counter + (float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear())));
                    if(f == Frequency.MONTHLY){
                        date = date.plusMonths(1);
                        month_counter+=1;
                    }
                }
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
