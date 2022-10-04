package com.coconutbmp.leash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    CardView returnCard, editCard, deleteCard;
    TextView name, surname, email, budgets, incomes, expenses, liabilities, transactions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        hook();
        JSONObject params = new JSONObject();
        try {
            params.put("userID", Data.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InternetRequest ir = new InternetRequest();
        ir.doRequest(InternetRequest.std_url + "get_user.php", this, params, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                fillInfo(response);
            }
        });

        returnCard.setOnClickListener(view->{
            this.finish();
        });
    }

    public void hook(){
        returnCard = findViewById(R.id.profileReturnCard);
        editCard = findViewById(R.id.profileEditCard);
        deleteCard = findViewById(R.id.profileDeleteCard);
        name = findViewById(R.id.lblProfileName);
        surname = findViewById(R.id.lblProfileSurname);
        email = findViewById(R.id.lblProfileEmail);
        budgets = findViewById(R.id.lblBudgetCount);
        incomes = findViewById(R.id.lblIncomeCount);
        expenses = findViewById(R.id.lblExpenseCount);
        liabilities = findViewById(R.id.lblLiabilityCount);
        transactions = findViewById(R.id.lblTransactionCount);
    }

    void fillInfo(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject info = jsonArray.getJSONObject(0);
            name.setText(info.getString("user_FirstName"));
            surname.setText(info.getString("user_LastName"));
            email.setText(info.getString("user_Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
