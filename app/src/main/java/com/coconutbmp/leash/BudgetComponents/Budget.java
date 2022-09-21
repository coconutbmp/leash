package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.InternetRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Vector;

public class Budget extends BudgetComponent{

    Vector<Income> income_list = new Vector<Income>();
    Vector<Liability> liability_list = new Vector<Liability>();
    Vector<Transaction> transaction_list = new Vector<Transaction>();
    InternetRequest ir = new InternetRequest();

    void setIncomes(String response){
        JSONArray ja;
        System.out.println(response);

    }

    public Vector<Income> getIncomes() { return income_list; };


    void setLiabilities(String response){
        JSONArray ja;
        System.out.println(response);
        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                liability_list.add(new Liability(this, (JSONObject) ja.get(i)));
            }
            System.out.println("got the array -> " + ja.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Vector<Liability> getLiabilities() { return liability_list; };

    void setTransactions(String response){
        if(response.equals("")){
            System.out.println("no response");
            return;
        }
        JSONArray ja;
        System.out.println(response + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");

        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                transaction_list.add(new Transaction((JSONObject) ja.get(i)));
            }
            System.out.println("got the array -> " + ja.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Vector<Transaction> getTransactions() { return transaction_list; };


    @Override
    public void initialize() throws Exception{
        JSONObject params = new JSONObject();
        System.out.println(this.getJsonRep().get("budget_ID") + " " + this.getJsonRep().get("budget_Name"));
        params.put("budgetid", this.getJsonRep().get("budget_ID"));
        ir = new InternetRequest();

        //get income
        params = new JSONObject();
        //todo: add parameters for income request
        ir.doRequest(
                InternetRequest.std_url+"get_income.php",
                null,
                params,
                this::setIncomes
        );
        //get liabilities
        params = new JSONObject();
        params.put("budgetid", this.getJsonRep().get("budget_ID"));
        ir.doRequest(
                InternetRequest.std_url+"get_liabilities.php",
                null,
                params,
                this::setLiabilities
        );
        //get transactions
        params = new JSONObject();
        //todo: add parameters for transaction request
        ir.doRequest(
                InternetRequest.std_url+"get_liabilities.php",
                null,
                params,
                this::setTransactions
        );
    }

    public Budget(JSONObject json_rep){
        super(json_rep);


    }



}
