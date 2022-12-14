package com.coconutbmp.leash;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.coconutbmp.leash.BudgetComponents.Budget;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Data {

    private static int user_id = -1;
    static Budget current;
    static private ArrayList<Budget> all_budgets = new ArrayList<>();
    static private CountDownLatch lock = new CountDownLatch(0);

    public static void addBudget(JSONObject json_rep){
        try{
            for(Budget already_in: all_budgets){
                if(already_in.getJsonRep().get("budget_ID").equals(json_rep.get("budget_ID"))){
                    return;
                }
            }

            lock = new CountDownLatch((int) (lock.getCount() + 3));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Budget b = new Budget(json_rep);
        all_budgets.add(b);
    }

    public static void countDown(){
        System.out.println("counting down");
        lock.countDown();
    }

    public static int getLockCount(){
        return (int)lock.getCount();
    }

    public static void setCurrent(Budget budget){
        current = budget;
    }

    public static void setCurrent(JSONObject jo){
        try {
            for (Budget b : all_budgets) {
                if (b.getJsonRep().get("budget_ID").equals(jo.get("budget_ID"))) {
                    setCurrent(b);
                }
            }
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }

    public static void setUserID(int userID){
        user_id = userID;
    }

    public static int getUserID(){
        return user_id;
    }

    public static ArrayList<Budget> getAll(){
        return all_budgets;
    }


    @SuppressLint("StaticFieldLeak")
    private static Activity current_act = null;
    private static CompletionListener listener = null;

    public static void setCurrentActivity(Activity act){
        current_act = act;
    }

    public static void setCurrentListener(CompletionListener _listener){
        listener = _listener;
    }

    public static CompletionListener getCurrentListener(){
        return listener;
    }

    public static Activity getCurrentAct(){
        return current_act;
    }

    public static void respond (boolean success){
        if(listener != null){
            if(current_act != null) {
                current_act.runOnUiThread(() -> {
                    listener.processCompletion(success);
                    listener = null;
                });
            } else {
                Thread response_thread = new Thread("Response"){
                    @Override
                    public void run(){
                        listener.processCompletion(success);
                    }
                };
                response_thread.start();
            }
        }
    }

    public static void removeBudget(Budget budget) {
        if (current == budget) current = null;
        all_budgets.remove(budget);
    }
}
