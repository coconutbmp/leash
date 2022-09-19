package com.coconutbmp.leash;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiabilityDetailsActivity extends AppCompatActivity {

    CardView returnCard;
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";
    InternetRequest internetRequest;
    LinearLayout history;
    ArrayList<String> ids = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liability_details);

        internetRequest = new InternetRequest();

        String userID = getIntent().getStringExtra("userID");
        JSONObject params = new JSONObject();
        JSONObject LiabilityParams = new JSONObject();

        history = findViewById(R.id.historyLayout);

        try {
            params.put("userid", userID);
            internetRequest.doRequest(url + "get_budget.php", this, params, new RequestHandler() {
                @Override
                public void processResponse(String response) {
                    try {
                        JSONArray budgets = new JSONArray(response);
                        for(int i = 0; i < budgets.length(); i++) {
                            JSONObject jsonObject = budgets.getJSONObject(i);
                            ids.add(jsonObject.getString("budget_ID"));
                        }

                        for(int i = 0; i < ids.size(); i++){
                            try {
                                LiabilityParams.put("budgetid", ids.get(i));
                                internetRequest.doRequest(url + "get_liabilities.php", LiabilityDetailsActivity.this, LiabilityParams, new RequestHandler() {
                                    @Override
                                    public void processResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int j = 0; j < jsonArray.length(); j++){
                                                JSONObject jo = jsonArray.getJSONObject(j);
                                                String interest = jo.getString("loan_interest_type") + " interest at a rate of " + jo.getString("loan_interest_ate") +"%";
                                                LiabilityHistoryLayout liabilityHistoryLayout = new LiabilityHistoryLayout(LiabilityDetailsActivity.this);
                                                liabilityHistoryLayout.name.setText(jo.getString("liability_name"));
                                                liabilityHistoryLayout.amount.setText("R "+jo.getString("loan_principle"));
                                                liabilityHistoryLayout.date.setText(jo.getString("start_date") +" - "+jo.getString("end_date"));
                                                liabilityHistoryLayout.interest.setText(interest);
                                                history.addView(liabilityHistoryLayout);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                LiabilityParams.remove("budgetid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.remove("userid");

        returnCard = findViewById(R.id.liabilityDetailReturnCard);

        returnCard.setOnClickListener(view -> {
            this.finish();
        });
    }
}
