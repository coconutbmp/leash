package com.coconutbmp.leash.BudgetComponents;

import org.json.JSONObject;
import java.util.Vector;

public class Budget extends BudgetComponent{

    Vector<Income> income_list = new Vector<Income>();
    Vector<Liability> liability_list = new Vector<Liability>();
    Vector<Transaction> transaction_list = new Vector<Transaction>();



    @Override
    public void initialize() {
        //todo: get income
        //todo: get liabilities
        //todo: get transactions
    }

    public Budget(JSONObject json_rep){
        super(json_rep);


    }



}
