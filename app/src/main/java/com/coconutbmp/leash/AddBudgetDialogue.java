package com.coconutbmp.leash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBudgetDialogue extends Dialog {

    //declarations
    String name, start_date, end_date, userID;
    Button proceed, cancel;
    EditText budget_name_edit;
    TextView begin_date_edit, end_date_edit;
    Button start_button, end_button;
    DatePicker start_dp, end_dp;


    /**
     * Constructor for the AddBudgetDialogue
     * @param act parent activity
     * @param data accept user id in order to link the budget-to-be to the correct user
     */
    public AddBudgetDialogue(Activity act,String data) {
        super(act);
        userID = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_budget_dialog);


        //link xml widgets to variables
        proceed = findViewById(R.id.add_budget_button);
        cancel = findViewById(R.id.cancel_add_budget_button);
        budget_name_edit = findViewById(R.id.budget_name_edit);

        begin_date_edit = findViewById(R.id.budget_start_label);
        end_date_edit = findViewById(R.id.budget_end_label);

        start_button = findViewById(R.id.budget_start_button);
        end_button = findViewById(R.id.budget_end_button);

        start_dp = findViewById(R.id.budget_start_dp);
        end_dp= findViewById(R.id.budget_end_dp);

        start_button.setOnClickListener(view1 -> UXFunctions.select_date(start_button, start_dp, begin_date_edit));
        end_button.setOnClickListener(view1 -> UXFunctions.select_date(end_button, end_dp, end_date_edit));


        cancel.setOnClickListener(view -> {   //when "cancel" button is clicked
            this.cancel();
        });

        proceed.setOnClickListener(view -> {     // when "done" button is clicked
            if (budget_name_edit.getText().length() > 0){

                //get EditText content in string format
                name= budget_name_edit.getText().toString();
                start_date = begin_date_edit.getText().toString();
                end_date = end_date_edit.getText().toString();

                addToDatabase();
            }
        });
    }

    void handle_response(String response){
        System.out.println(response + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (response.toLowerCase().charAt(0) == 's'){ // s for success

            //create intent, pass information and start Budget activity
            Intent i = new Intent(this.getContext(), BudgetActivity.class);
            JSONObject json_rep = new JSONObject();
            try {
                json_rep.put("budget_Name", budget_name_edit.getText().toString());
                json_rep.put("budget_StartDate", begin_date_edit.getText().toString());
                json_rep.put("budget_EndDate", end_date_edit.getText().toString());
                Data.addBudget(json_rep);
                Data.setCurrent(json_rep);
            } catch (Exception e){
                e.printStackTrace();
            }


            this.getContext().startActivity(i);
        } else {
            // todo: show error message
        }

        this.cancel();
    }

    /**
     * upload the data for the created budget to the database
     */
    public void addToDatabase(){  //network request to insert budget information into database

        InternetRequest ir = new InternetRequest();
        try{
            //add parameters to insert
            JSONObject params = new JSONObject();
            params.put("userid",userID);
            params.put("name",name);
            params.put("startdate", start_date);
            params.put("enddate", end_date);
            ir.doRequest(
                    InternetRequest.std_url+ "add_budget.php",
                    this.getOwnerActivity(),
                    params,
                    this::handle_response
            );

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
