package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

public class Transaction extends BudgetComponent{


    @Override
    public void initialize(){
    }

    /**
     * complete deletion based on response
     * @param response
     */
    @Override
    public void acceptDeletionResponse(String response){
        if(response.equals("success")) ((Budget) this.parent).removeTransaction(this);
        super.acceptDeletionResponse(response);
    }

    /**
     * initiate deletion process
     */
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

    /**
     * constructor
     * @param parent owner
     * @param json_rep representation
     */
    public Transaction(BudgetComponent parent, JSONObject json_rep) {
        super(parent, json_rep);
    }
}
