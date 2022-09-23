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

import java.util.ArrayList;
import java.util.Locale;

/**
 * Controller for the Add Income Page
 */
public class AddIncomeActivity extends AppCompatActivity {

    //todo: link elements from activity_add_income.xml to this class
    //todo: Upload data from the user to the database
    //todo: navigate back to Budget Page

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

        start.setOnClickListener(view1 -> UXFunctions.select_date(start, startDate, lblStartDate)); // link the button to the date picker and label
        end.setOnClickListener(view1 -> UXFunctions.select_date(end, endDate, lblEndDate)); // link the button to the date picker and label

        String[] freq = getResources().getStringArray(R.array.calculation_frequencies);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, freq);
        incomeFrequency.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));
        incomeFrequency.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        incomeFrequency.setAdapter(adapter);

        save.setOnClickListener(view1 ->{
            addIncome();
        });

        returnCard.setOnClickListener(view1->{
            AddIncomeActivity.this.finish();
        });
    }

    private void addIncome(){
        String startDate, endDate, name, frequency, amount;
        internetRequest = new InternetRequest();

        if (label.getText().toString().equals("") || label.getText().equals(null)){
            Toast.makeText(this, "No Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            name = label.getText().toString();
        }

        if(lblStartDate.getText().toString().equals("0000/00/00")){
            Toast.makeText(this, "Selected a Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            startDate = lblStartDate.getText().toString();
        }

        if(lblEndDate.getText().toString().equals("0000/00/00")){
            Toast.makeText(this, "Selected a End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            endDate = lblStartDate.getText().toString();
        }

        if(incomeFrequency.getSelectedItemPosition() == 0){
            Toast.makeText(this, "No Selected Income Frequency", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            frequency = incomeFrequency.getSelectedItem().toString().toLowerCase(Locale.ROOT);
        }

        if (incomeAmount.getText().toString().equals("") || incomeAmount.getText().equals(null)){
            Toast.makeText(this, "No Amount Received", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            amount = incomeAmount.getText().toString();
        }

        JSONObject income = new JSONObject();

        try {
            income.put("incomeName", name);
            income.put("amount", amount);
            income.put("startDate", startDate);
            income.put("endDate", endDate);
            income.put("freq", frequency);
            income.put("budgetid", Data.current.getJsonRep().get("budget_ID"));
            internetRequest.doRequest(url + "submit_income.php", this, income, this::processIncome);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void processIncome(String response){
        if(response.equals("Success")){
            Toast.makeText(this, "Income Successfully Added", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(this, "An error occurred, check your values again", Toast.LENGTH_LONG).show();
        }
    }
}