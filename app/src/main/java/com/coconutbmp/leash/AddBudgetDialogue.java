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

import java.io.IOException;

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

        start_button.setOnClickListener(view1 -> AddLiabilityActivity.select_date(start_button, start_dp, begin_date_edit));
        end_button.setOnClickListener(view1 -> AddLiabilityActivity.select_date(end_button, end_dp, end_date_edit));


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

                //create intent, pass information and start Budget activity
                Intent i = new Intent(this.getContext(), Budget.class);
                i.putExtra("budget_name", budget_name_edit.getText().toString());
                i.putExtra("budget_startdate", budget_name_edit.getText().toString());
                i.putExtra("budget_enddate", budget_name_edit.getText().toString());
                this.getContext().startActivity(i);
                this.cancel();

            }


        });
    }

    /**
     * upload the data for the created budget to the database
     */
    public void addToDatabase(){  //network request to insert budget information into database

        //set up client
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder FormBodybuilding = new FormBody.Builder();

        //add parameters to insert
        FormBodybuilding.add("userid",userID);
        FormBodybuilding.add("name",name);
        FormBodybuilding.add("startdate", start_date);
        FormBodybuilding.add("enddate", end_date);



        RequestBody reqBody = FormBodybuilding.build();
        String URL = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/add_budget.php";

        //link parameters in RequestBody with URL to build request
        Request req = new Request.Builder()
                .post(reqBody)
                .url(URL)
                .build();
        client.newCall(req).enqueue(new Callback() {   //make the call with request to get a response
            @Override
            public void onFailure(Call call, IOException e) {  //if call fails
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { //if we get a response
                String strResponse =response.body().string();

                if("Success".equals(strResponse)){  //if response returns "Success"
                    System.out.println(strResponse);
                }
                else if(strResponse.equals("Failed")){ //if response returns "Failed"
                    System.out.println(strResponse);
                }

            }
        });

    }

}
