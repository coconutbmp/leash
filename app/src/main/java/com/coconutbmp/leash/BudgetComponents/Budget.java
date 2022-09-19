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


    }

    void setLiabilities(String response){
        JSONArray ja;
        System.out.println(response);
        try {
            ja = new JSONArray(response);
            System.out.println("got the array -> " + ja.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void setTransactions(String response){


    }

    @Override
    public void initialize() throws Exception{
        JSONObject params = new JSONObject();
        System.out.println(this.getJsonRep().get("budget_ID") + " " + this.getJsonRep().get("budget_Name"));
        params.put("budgetid", this.getJsonRep().get("budget_ID"));
        ir = new InternetRequest();
        //todo: get income
        ir.doRequest(
                InternetRequest.std_url+"get_income.php",
                null,
                params,
                this::setIncomes
        );
        //todo: get liabilities
        ir.doRequest(
                InternetRequest.std_url+"get_liabilities.php",
                null,
                params,
                this::setLiabilities
        );
        //todo: get transactions
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
