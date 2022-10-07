package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

public class Transaction extends BudgetComponent{


    @Override
    public void initialize(){

    }

    public Transaction(BudgetComponent parent, JSONObject json_rep) {
        super(parent, json_rep);
        System.out.println("got transaction rep" + json_rep);

        InternetRequest ir = new InternetRequest();
        JSONObject jo;
        try{
            jo = new JSONObject();
            jo.put("transactionid", getJsonRep().get("transaction_ID"));
        } catch (Exception e){
            e.printStackTrace();
            Data.respond(false);
            return;
        }

        ir.doRequest(
                InternetRequest.std_url + "delete_transaction.php",
                null,
                jo,
                this::acceptDeletionResponse
        );
    }
}
