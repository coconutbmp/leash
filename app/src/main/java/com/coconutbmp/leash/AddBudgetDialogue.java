package com.coconutbmp.leash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class AddBudgetDialogue extends Dialog {

    //declarations
    String name, start_date, end_date, userID;
    Button proceed, cancel;
    EditText budget_name_edit;
    TextView begin_date_edit, end_date_edit;
    Button start_button, end_button;
    DatePicker start_dp, end_dp;
    String s_date,e_date;


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
        setContentView(R.layout.dialog_add_budget);


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
   
        start_button.setOnClickListener(view1 -> {
            closeKeyboard();
            UXFunctions.select_date(start_button, start_dp, begin_date_edit);
            int s_year  = start_dp.getYear();
            int s_month = start_dp.getMonth();
            int s_day   = start_dp.getDayOfMonth();

            s_date =construct_date(s_year,s_month,s_day);
        });

        end_button.setOnClickListener(view1 -> {
            closeKeyboard();
            UXFunctions.select_date(end_button, end_dp, end_date_edit);
            int e_year  = end_dp.getYear();
            int e_month = end_dp.getMonth();
            int e_day   = end_dp.getDayOfMonth();

            e_date= construct_date(e_year,e_month,e_day);
         });


        cancel.setOnClickListener(view -> {   //when "cancel" button is clicked
            this.cancel();
        });

        proceed.setOnClickListener(view -> {     // when "done" button is clicked
            if (budget_name_edit.getText().length() > 0 && begin_date_edit.getText().length()>0 && end_date_edit.getText().length()>0){
                if(s_date.compareTo(e_date)<=0){
                    //get EditText content in string format
                    name = budget_name_edit.getText().toString();
                    start_date = begin_date_edit.getText().toString();
                    end_date = end_date_edit.getText().toString();
                    addToDatabase();
                }
                else{
                    Toast.makeText(getContext(), "End Date must be no earlier than Start Date", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getContext(), "Please fill in the entire form", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeKeyboard() { //collapse the keyboard

        View view = this.getCurrentFocus(); // this will give us the view which is currently in focus

        if (view != null) {  //protect app from crashing if nothing is currently in focus
            //assigning the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    String construct_date(int year, int month,int day ){  //construct date in a lexicographically comparable format

        String str_month = "" + month;
        String str_day   = "" + day;

        if(month < 10){
            str_month = "0"+ month;
        }
        if(day < 10){
            str_day = "0" + day;
        }

        return year +"-"+ str_month +"-"+ str_day;
    }



    void handle_response(String response){
        if (response.toLowerCase().charAt(0) != 'f'){ // s for success
            //create intent, pass information and start Budget activity
            Intent i = new Intent(this.getContext(), BudgetActivity.class);
            JSONObject json_rep;
            try {
                json_rep = new JSONObject(response);
                Data.addBudget(json_rep);
                Data.setCurrent(json_rep);
            } catch (Exception e){
                e.printStackTrace();
            }
            this.getContext().startActivity(i);
        } else {
            getOwnerActivity().runOnUiThread(()->{
                Toast.makeText(getOwnerActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                dismiss();
            });
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

    public String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
}
