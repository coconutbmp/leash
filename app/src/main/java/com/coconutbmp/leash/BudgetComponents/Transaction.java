package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

public class Transaction extends BudgetComponent{


    @Override
    public void initialize(){

    }

    @Override
    public void acceptDeletionResponse(String response){
        if(response.equals("success")) ((Budget) this.parent).removeTransaction(this);
        super.acceptDeletionResponse(response);
    }

    @Override
    public void delete() {
        InternetRequest ir = new InternetRequest();
        JSONObject jo;
        try{
            jo = new JSONObject();
            jo.put("transactID", getJsonRep().get("transaction_ID"));
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

    public Transaction(BudgetComponent parent, JSONObject json_rep) {
        super(parent, json_rep);
    }
}
