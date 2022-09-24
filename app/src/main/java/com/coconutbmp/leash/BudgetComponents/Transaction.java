package com.coconutbmp.leash.BudgetComponents;

import org.json.JSONObject;

public class Transaction extends BudgetComponent{


    @Override
    public void initialize(){

    }

    public Transaction(JSONObject json_rep) {
        super(json_rep);
        System.out.println("got transaction rep" + json_rep);

    }
}
