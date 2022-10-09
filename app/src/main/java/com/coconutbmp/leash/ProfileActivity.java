package com.coconutbmp.leash;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.coconutbmp.leash.BudgetComponents.Budget;
import com.coconutbmp.leash.BudgetComponents.Liability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class ProfileActivity extends AppCompatActivity {
    CardView returnCard, editCard, deleteCard;
    TextView name, surname, email, budgets, incomes, liabilities, transactions;
    String pass;
    ArrayList<Budget> budgetList;
    int budgetSize;
    InternetRequest ir = new InternetRequest();
    String myName;
    String mySurname;
    String myEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        budgetList = Data.getAll();
        budgetSize = budgetList.size();
        hook();
        JSONObject params = new JSONObject();
        int userID = Data.getUserID();
        try {
            params.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ir.doRequest(InternetRequest.std_url + "get_user.php", this, params, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                fillInfo(response);
            }
        });

        editCard.setOnClickListener(view -> {
            if(pass.equals("googlePass")){
                Toast.makeText(this, "You cannot edit your google account from this app", Toast.LENGTH_SHORT).show();
            }
            else{
                EditProfileDialog dialog = new EditProfileDialog(this, myName, mySurname, myEmail, pass);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        deleteCard.setOnClickListener(view->{
            new AlertDialog.Builder(this, R.style.alertDialog)
                    .setTitle("Delete Account?")
                    .setMessage("Are You Sure?\nAll your data will be removed permanently.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteUser();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        returnCard.setOnClickListener(view->{
            this.finish();
        });
    }

    public void deleteUser(){
        JSONObject params = new JSONObject();
        try {
            params.put("userID", Data.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ir.doRequest(InternetRequest.std_url + "remove_user.php", this, params, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                if(response.equals("success")){
                    HomeActivity.fa.finish();
                    ProfileActivity.this.finish();
                }
            }
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
        liabilities = findViewById(R.id.lblLiabilityCount);
        transactions = findViewById(R.id.lblTransactionCount);
    }

    void fillInfo(String response){
        int incomeCount = 0;
        int transactionCount = 0;
        int LiabilityCount = 0;
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject info = jsonArray.getJSONObject(0);
            name.setText("Name: " + info.getString("user_FirstName"));
            myName = info.getString("user_FirstName");
            surname.setText("Surname: " + info.getString("user_LastName"));
            mySurname = info.getString("user_LastName");
            email.setText("Email: " + info.getString("user_Email"));
            myEmail = info.getString("user_Email");
            pass = info.getString("user_Password");
            budgets.setText("Active Budgets: " + String.valueOf(budgetSize));
            for(Budget b: budgetList){
                incomeCount += b.getIncomes().size();
                LiabilityCount += b.getLiabilities().size();
                transactionCount += b.getTransactions().size();
            }
            incomes.setText("Recorded Incomes: " + String.valueOf(incomeCount));
            liabilities.setText("Recorded Liabilities: " + String.valueOf(LiabilityCount));
            transactions.setText("Recorded Transactions: " + String.valueOf(transactionCount));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
