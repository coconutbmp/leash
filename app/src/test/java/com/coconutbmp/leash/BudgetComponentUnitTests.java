package com.coconutbmp.leash;

import static com.coconutbmp.leash.Data.current;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.coconutbmp.leash.BudgetComponents.Income;
import com.coconutbmp.leash.BudgetComponents.Liability;
import com.coconutbmp.leash.BudgetComponents.Transaction;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BudgetComponentUnitTests {
    Budget test_budget;
    JSONObject test_budget_json_rep;
    String test_user_string = "{\"user_ID\":\"11\",\"user_Email\":\"test@user.com\",\"user_Password\":\"1000:69026e4866e4e9f81f651f3a787cbf05:89f26a2afca111707cea3ee2cd022f1250428bc837d70dc583be1fc592e8c4d23aa428f3ae2bc89626da8ced131eaacbf0c989fd780e2b02d3753c25f2d6b227\",\"user_FirstName\":\"Test\",\"user_LastName\":\"User\"}";

    String test_liability_string;
    String test_income_string;
    String test_transaction_string;
    Boolean current_set;
    //Activity test_activity = new Activity();

    JSONObject test_user;
    String response = null;

    private CountDownLatch lock;

    @Test
    public void set_user(){
        Integer check = -1;
        try {
            test_user = new JSONObject(test_user_string);
            check = Integer.parseInt((String) test_user.get("user_ID"));
            Data.setUserID(Integer.parseInt((String) test_user.getString("user_ID")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertTrue(check.equals(Data.getUserID()));
    }

    void accept_response(String _response){
        System.out.println(_response);
        response = _response;
        lock.countDown();
    }

    @Test public void request_all_budgets() throws InterruptedException {
        if(Data.getUserID() == -1 || test_user == null)
            set_user();

        InternetRequest ir = new InternetRequest();
        JSONObject budget_request = null;


        try {
            budget_request = new JSONObject();
            budget_request.put("userid", test_user.get("user_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert budget_request != null;

        lock = new CountDownLatch(1);
        ir.doRequest(
                InternetRequest.std_url + "get_budget.php",
                null,
                budget_request,
                this::accept_response
        );

        lock.await(5, TimeUnit.SECONDS);
        assertNotNull(response);
    }

    @Test
    public void create_budgets(){
        try {
            if (response == null)
                request_all_budgets();
            JSONArray ja = new JSONArray(response);
            JSONObject jo = ja.getJSONObject(0);
            Data.addBudget(jo);
        } catch (Exception e){
            e.printStackTrace();
        }

        assert Data.getAll().size() > 0;
    }

    @Test
    public void set_current_budget(){
        try{
            if(Data.getAll().size() == 0)
                create_budgets();
            Data.setCurrent(Data.getAll().get(0).getJsonRep());
        } catch (Exception e){
            e.printStackTrace();
        }
        assert current != null;
    }

    /*Boolean got_all = false; // liabilities, incomes, transactions retrieved
    @Test
    public void accept_budget_components() throws InterruptedException{

        if(current == null) set_current_budget();
        if(got_all) return;

        lock = new CountDownLatch(3);
        Data.setCurrentListener((result) -> {
            System.out.println(result);
            lock.countDown();
            assert (result);
        });

        got_all = lock.await(5, TimeUnit.SECONDS);
        assert got_all;
    }

    boolean are_populated = false;
    @Test
    public void ensure_populated(){
        try{
            accept_budget_components();
        } catch (Exception e){
            e.printStackTrace();
        }

        assert current.getLiabilities().size() > 0;
        assert current.getIncomes().size() > 0;
        assert current.getTransactions().size() > 0;
        are_populated = true;
    }*/
}
