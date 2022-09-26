package com.coconutbmp.leash;

import com.coconutbmp.leash.BudgetComponents.Budget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BudgetComponentUnitTests {
    Budget test_budget;
    JSONObject test_budget_json_rep;
    String test_user_string = "{\"user_ID\":\"11\",\"user_Email\":\"test@user.com\",\"user_Password\":\"1000:69026e4866e4e9f81f651f3a787cbf05:89f26a2afca111707cea3ee2cd022f1250428bc837d70dc583be1fc592e8c4d23aa428f3ae2bc89626da8ced131eaacbf0c989fd780e2b02d3753c25f2d6b227\",\"user_FirstName\":\"Test\",\"user_LastName\":\"User\"}";

    String test_liability_string;
    String test_income_string;
    String test_transaction_string;

    JSONObject test_user;
    String response = null;

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

    private CountDownLatch lock = new CountDownLatch(1);

    @Test public void request_all_budgets() throws InterruptedException {
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
            create_budgets();
            Data.setCurrent(Data.getAll().get(0).getJsonRep());
        } catch (Exception e){
            e.printStackTrace();
        }
        assert Data.current != null;
    }


    //Liability Tests


}
