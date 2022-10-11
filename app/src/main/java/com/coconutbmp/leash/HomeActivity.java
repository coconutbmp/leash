package com.coconutbmp.leash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.graphics.Color;

/**
 * Controller For the Home Page
 */
public class HomeActivity extends AppCompatActivity {
    //declaring variables
    CardView home_return_button, btnAdd, liabilityReport, logout, profile;
    TextView day, month;
    TextView empty;
    String userID;
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";
    LinearLayout budgets;
    SharedPreferences prefs;
    InternetRequest internetRequest;
    LinearLayout summary_holder;
    public static Activity fa;
    SummaryFragment summary_frag = new SummaryFragment();


    public void processBudgetResponse(String response){
        try {
            budgets.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(24, 4, 24, 4);
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() == 0){
                budgets.addView(empty);
                return;
            }
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
        Data.setUserID(Integer.parseInt(userID));
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        internetRequest = new InternetRequest();

        btnAdd = findViewById(R.id.btnAddSomething);
        liabilityReport = findViewById(R.id.liabilityReportCardView);
        home_return_button = findViewById(R.id.homeReturnCard);
        profile = findViewById(R.id.btnProfile);
        logout = findViewById(R.id.logoutCard);
        day = findViewById(R.id.lblDay);
        month = findViewById(R.id.lblMonth);
        budgets = findViewById(R.id.budgetLayout);
        empty = findViewById(R.id.lblEmpty);

        UXFunctions.setDate(day, month);

        JSONObject userParams = new JSONObject();
        try {
            userParams.put("userid", userID);
            internetRequest.doRequest(url + "get_budget.php", this, userParams, this::processBudgetResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        summary_holder = findViewById(R.id.summaryLayout);
        summary_holder.removeAllViews();
        FragmentManager frag_man = getSupportFragmentManager();
        FragmentTransaction frag_tran = frag_man.beginTransaction();
        if(!frag_tran.isEmpty()){
            frag_tran.remove(summary_frag).commit();
        }
        frag_tran.add(summary_holder.getId(), summary_frag).commit();


        //implementing button to add budget dialogue
        String finalUserID = userID;
        btnAdd.setOnClickListener(view -> {
            AddBudgetDialogue dialogue = new AddBudgetDialogue(this, finalUserID);
            dialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogue.show();
        });

        profile.setOnClickListener(view->{
            fa = this;
            Intent goToProfile = new Intent(this, ProfileActivity.class);
            startActivity(goToProfile);
        });

        liabilityReport.setOnClickListener(view -> {
            Intent i = new Intent(HomeActivity.this, LiabilityDetailsActivity.class);
            i.putExtra("userID", userID);
            startActivity(i);
        });

        //implementing return button
        home_return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.finish();
            }
        });

        logout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            boolean staySignedIn = prefs.getBoolean("StaySignedIn", false);
            if(staySignedIn) {
                editor.putBoolean("StaySignedIn", false);
            }
            editor.commit();
            this.finish();
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

