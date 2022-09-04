package com.coconutbmp.leash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    String name,startdate,enddate, userID;
    Button proceed, cancel;
    EditText budget_name_edit, budget_startdate_edit, budget_enddate_edit;

    public AddBudgetDialogue(Activity act,String data) {
        super(act);
        userID=data;
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
        budget_startdate_edit = findViewById(R.id.et_startdate);
        budget_enddate_edit = findViewById(R.id.et_enddate);


        cancel.setOnClickListener(view -> {   //when "cancel" button is clicked
            this.cancel();
        });

        proceed.setOnClickListener(view -> {     // when "done" button is clicked
            if (budget_name_edit.getText().length() > 0){
                //todo: add code to add the budget to the users account

                //get EditText content in string format
                name= budget_name_edit.getText().toString();
                startdate= budget_startdate_edit.getText().toString();
                enddate= budget_enddate_edit.getText().toString();

                addtoDatabase();

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

    public void addtoDatabase(){  //network request to insert budget information into database

        //set up client
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder FormBodybuilding = new FormBody.Builder();

        //add parameters to insert
        FormBodybuilding.add("userid",userID);
        FormBodybuilding.add("name",name);
        FormBodybuilding.add("startdate",startdate);
        FormBodybuilding.add("enddate",enddate);



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
