package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Controller for the Add Income Page
 */
public class AddIncomeActivity extends AppCompatActivity {

    //Declare variables
    Button start, end, save;
    DatePicker startDate, endDate;
    TextView lblStartDate, lblEndDate;
    EditText label, incomeAmount;
    Spinner incomeFrequency;
    CardView returnCard;
    InternetRequest internetRequest;
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        //Hook xml components to java components
        start = findViewById(R.id.start_button);
        end = findViewById(R.id.end_button);
        save = findViewById(R.id.btnSaveIncome);
        label = findViewById(R.id.income_name_label);
        startDate = findViewById(R.id.loan_start_dp);
        endDate = findViewById(R.id.loan_end_dp);
        lblStartDate = findViewById(R.id.loan_start_label);
        lblEndDate = findViewById(R.id.loan_end_label);
        incomeAmount = findViewById(R.id.edtIncomeAmount);
        returnCard = findViewById(R.id.ReturnCard);
        incomeFrequency = findViewById(R.id.sprRecurringFrequency2);

        // Call UX Function to save the date from the datepicker to TextView
        start.setOnClickListener(view1 -> UXFunctions.select_date(start, startDate, lblStartDate)); // link the button to the date picker and label
        end.setOnClickListener(view1 -> UXFunctions.select_date(end, endDate, lblEndDate)); // link the button to the date picker and label

        /**
         * Setup for spinner
         * Assigns array, layout, colors and text color
         */
        String[] freq = getResources().getStringArray(R.array.calculation_frequencies);// Create array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, freq);// Create Spinner adapter
        incomeFrequency.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));// Set Background Color for popup menu
        incomeFrequency.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP); //set text color
        incomeFrequency.setAdapter(adapter); // apply adapter to spinner

        save.setOnClickListener(view1 ->{
            addIncome();
        });

        returnCard.setOnClickListener(view1->{
            AddIncomeActivity.this.finish(); // Close Activity
        });
    }

    /**
     * Void Method
     * Makes a request to the server to add parameters to Database's Income Table
     * Closes Activity
     */
    private void addIncome(){
        String startDate, endDate, name, frequency, amount;// declare variables for parameters
        internetRequest = new InternetRequest();// create new request

        if (label.getText().toString().equals("") || label.getText().equals(null)){// No Name Error
            Toast.makeText(this, "No Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else{ // assign inputted name to variable
            name = label.getText().toString();
        }

        if(lblStartDate.getText().toString().equals("0000/00/00")){ // No date selected
            Toast.makeText(this, "Selected a Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{ // assign selected date
            startDate = lblStartDate.getText().toString();
        }

        if(lblEndDate.getText().toString().equals("0000/00/00")){
            Toast.makeText(this, "Selected a End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            endDate = lblStartDate.getText().toString();
        }

        if(incomeFrequency.getSelectedItemPosition() == 0){ // no selection
            Toast.makeText(this, "No Selected Income Frequency", Toast.LENGTH_SHORT).show();
            return;
        }
        else{ // get Frequency from spinner
            frequency = incomeFrequency.getSelectedItem().toString().toLowerCase(Locale.ROOT);
        }

        if (incomeAmount.getText().toString().equals("") || incomeAmount.getText().equals(null)){ // no amount inputted
            Toast.makeText(this, "No Amount Received", Toast.LENGTH_SHORT).show();
            return;
        }
        else{ // assign amount
            amount = incomeAmount.getText().toString();
        }

        // create JsonObject for parameters
        JSONObject income = new JSONObject();

        //add values to parameters
        try {
            income.put("incomeName", name);
            income.put("amount", amount);
            income.put("startDate", startDate);
            income.put("endDate", endDate);
            income.put("freq", frequency);
            income.put("budgetid", Data.current.getJsonRep().get("budget_ID"));
            internetRequest.doRequest(url + "submit_income.php", this, income, this::processIncome); //make request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void awaitReload(boolean success){
        if(success)
            Toast.makeText(this, "Income Successfully Added", Toast.LENGTH_SHORT).show();
        if(!success)
            Toast.makeText(this, "An Error has Occurred", Toast.LENGTH_SHORT).show();
        Data.setCurrentListener(null);
        this.finish();
    }

    /**
     * Handle success or failure
     * @param response
     */
    void processIncome(String response){
        if(response.equals("Success")){
            Data.setCurrentActivity(this);
            Data.setCurrentListener(this::awaitReload);
            try {
                Data.current.refreshIncomes();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "An error occurred, check your values again", Toast.LENGTH_LONG).show();
        }
    }
}