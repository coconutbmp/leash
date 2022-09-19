package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";
    LinearLayout budgets;
    InternetRequest internetRequest;
    LinearLayout summary_holder;

    public void processBudgetResponse(String response){
        try {
            budgets.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(24, 4, 24, 4);
            JSONArray jsonArray = new JSONArray(response);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data.addBudget(jsonObject);
                BudgetListLayout budget = new BudgetListLayout(HomeActivity.this);

                budget.json_rep = jsonObject;
                budget.budgetName.setText(jsonObject.getString("budget_Name"));
                budget.budgetSummary.setText(jsonObject.getString("budget_StartDate") + " - "+jsonObject.getString("budget_EndDate"));
                budgets.addView(budget, layoutParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initialising variables
        userID = getIntent().getStringExtra("userID");

        internetRequest = new InternetRequest();

        btnAdd = findViewById(R.id.btnAddSomething);
        home_return_button = findViewById(R.id.homeReturnCard);
        day = findViewById(R.id.lblDay);
        month = findViewById(R.id.lblMonth);
        budgets = findViewById(R.id.budgetLayout);

        UXFunctions.setDate(day, month);

        JSONObject userParams = new JSONObject();
        try {
            userParams.put("userid", userID);
            internetRequest.doRequest(url + "get_budget.php", this, userParams, this::processBudgetResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        summary_holder = findViewById(R.id.summaryLayout);
        FragmentManager frag_man = getSupportFragmentManager();
        FragmentTransaction frag_tran = frag_man.beginTransaction();

        SummaryFragment summary_frag = new SummaryFragment();
        frag_tran.add(summary_holder.getId(), summary_frag).commit();



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

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject userParams = new JSONObject();
        try {
            userParams.put("userid", userID);
            internetRequest.doRequest(url + "get_budget.php", this, userParams, this::processBudgetResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

