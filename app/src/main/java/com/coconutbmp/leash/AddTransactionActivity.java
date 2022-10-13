package com.coconutbmp.leash;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.coconutbmp.leash.BudgetComponents.Liability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class AddTransactionActivity extends AppCompatActivity {

    // declare variables
    CardView returnCard;
    RadioButton paying, receiving;
    EditText amount;
    Spinner liabilities;
    DatePicker date_dp;
    Button save;
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";
    InternetRequest internetRequest;
    Vector<Liability> currentLiabilities;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        hook();
        currentLiabilities = Data.current.getLiabilities(); // get all liabilities for current budget
        int liabilitySize = currentLiabilities.size() + 1;

        // create array of all budget's liabilities
        String[] liabilityNames = new String[liabilitySize];
        liabilityNames[0] = "None";
        for (int i = 0; i < liabilitySize - 1; i++){
            try {
                liabilityNames[i + 1] = (String) currentLiabilities.get(i).getJsonRep().get("name");
                int j = 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * Setup for spinner
         * Assigns array, layout, colors and text color
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, liabilityNames);
        liabilities.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));
        liabilities.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        liabilities.setAdapter(adapter);

        returnCard.setOnClickListener(view -> {
            AddTransactionActivity.this.finish();
        });

        save.setOnClickListener(view -> {
            addTransaction();
        });
    }

    /**
     * hook all xml components to java components
     * @return
     */
    private boolean hook(){
        returnCard = findViewById(R.id.transactionReturnCard);
        paying = findViewById(R.id.rbtnPaying);
        receiving = findViewById(R.id.rbtnReceiving);
        amount = findViewById(R.id.edtTransactionAmount);
        liabilities = findViewById(R.id.sprLiabilities);
        save = findViewById(R.id.btnSaveTransaction);
        date_dp = findViewById(R.id.transaction_date_dp);
        return true;
    }

    /**
     *  Make Request to store once-off transactions to Transaction table in DB
     */
    private void addTransaction(){
        internetRequest = new InternetRequest();
        //Initialise Variables
        String transactionType = "NULL";
        String transactionAmount = amount.getText().toString();
        String liabilityID = "NULL";
        String incomeID = "NULL";

        if(paying.isChecked()){ // select once off transaction type
            transactionType = "once-off expense";
        }
        else if(receiving.isChecked()){
            transactionType = "once-off income";
        }

        //get liability ID from array of liabilities
        int pos = liabilities.getSelectedItemPosition();
        if(pos > 0){
            try {
                liabilityID = (String) currentLiabilities.get(pos - 1).getJsonRep().get("ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Create Parameter JsonObject
        JSONObject transaction = new JSONObject();
        try {
            transaction.put("budgetid", Data.current.getJsonRep().get("budget_ID"));
            transaction.put("incomeid", incomeID);
            transaction.put("liabilityid", liabilityID);
            transaction.put("transactiontype", transactionType);
            transaction.put("transactionamount", transactionAmount);
            String date = String.format("%s-%s-%s", date_dp.getYear(), date_dp.getMonth()+1, date_dp.getDayOfMonth());
            transaction.put("date",date);
            System.out.println(transaction);
            internetRequest.doRequest(url+"submit_transaction.php", this, transaction, this::processTransaction);//make request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void awaitReload(boolean success){
        if(success)
            Toast.makeText(this, "Transaction Recorded", Toast.LENGTH_SHORT).show();
        if(!success)
            Toast.makeText(this, "An Error has Occurred", Toast.LENGTH_SHORT).show();
        Data.setCurrentListener(null);
        this.finish();
    }

    /**
     * handle success or failure and reload
     * @param response
     */
    public void processTransaction(String response){
        if(response.equals("Success")){
            Data.setCurrentActivity(this);
            Data.setCurrentListener(this::awaitReload);
            try {
                Data.current.refreshTransactions();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Something Went Wrong, Please Check Your Details Again", Toast.LENGTH_SHORT).show();
        }
    }
}
