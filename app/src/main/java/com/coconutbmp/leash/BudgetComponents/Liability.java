package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.InternetRequest;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public Liability(Budget parent, JSONObject json_rep) {
        super(parent, json_rep);
    }
}
