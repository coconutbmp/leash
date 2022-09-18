package com.coconutbmp.leash;

import com.coconutbmp.leash.BudgetComponents.Budget;

import org.json.JSONObject;
import java.util.Vector;

public class Data {
    private String user_id;
    Budget current;
    private Vector<Budget> all_budgets;

    void add_budget(JSONObject json_rep){
        all_budgets.add(new Budget(json_rep));
    }

}
