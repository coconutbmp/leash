package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Controller For the Home Page
 */
public class HomeActivity extends AppCompatActivity {
    //declaring variables
    CardView home_return_button, btnAdd;
    TextView day, month;
    String userID;

    JSONArray getMyBudgets(){ // retrieve all my budgets
        System.out.println("->->->->->->->->->->->->->->->->->");
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("userid", userID);

        RequestBody reqBody = builder.build();
        System.out.println(userID);

        String URL = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/get_budget.php";

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
                    System.out.println("---------------------------------");
                    System.out.println(strResponse);
                    System.out.println("---------------------------------");
                }
                else if(strResponse.equals("Failed")){ //if response returns "Failed"
                    System.out.println("---------------------------------");
                    System.out.println(strResponse);
                    System.out.println("---------------------------------");
                }

            }
        });

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initialising variables
        userID = getIntent().getStringExtra("userID");

        userID = getIntent().getStringExtra("userID");

        btnAdd = findViewById(R.id.btnAddSomething);
        home_return_button = findViewById(R.id.homeReturnCard);
        day = findViewById(R.id.lblDay);
        month = findViewById(R.id.lblMonth);

        UXFunctions.setDate(day, month);

        getMyBudgets(); // retrieve budgets from the db

        //implementing button to add budget dialogue
        String finalUserID = userID;
        btnAdd.setOnClickListener(view -> {
            AddBudgetDialogue dialogue = new AddBudgetDialogue(this, finalUserID);
            dialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogue.show();
        });

        //implementing return button
        home_return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.finish();
            }
        });
    }
}

