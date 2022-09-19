package com.coconutbmp.leash;

import com.coconutbmp.leash.BudgetComponents.Budget;

import org.json.JSONObject;

import java.util.ArrayList;

public class Data {

    private static int user_id;
    static Budget current;
    static private ArrayList<Budget> all_budgets = new ArrayList<>();

    public static void addBudget(JSONObject json_rep){
        Budget b = new Budget(json_rep);
        all_budgets.add(b);
    }

    public static void setCurrent(Budget budget){
        current = budget;
    }

    public static void setCurrent(JSONObject jo){
        for (Budget b: all_budgets) {
            if(b.getJsonRep() == jo){
                setCurrent(b);
            }
        }
    }

    public static void setUserID(int userID){
        user_id = userID;
    }

    public static void getUserID(){

    }

}
