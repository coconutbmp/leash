package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Controller for the Add Income Page
 */
public class AddIncomeActivity extends AppCompatActivity {

    //todo: link elements from activity_add_income.xml to this class
    //todo: Upload data from the user to the database
    //todo: navigate back to Budget Page

    Button start, end;
    DatePicker startDate, endDate;
    TextView lblStartDate, lblEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        start = findViewById(R.id.start_button);
        end = findViewById(R.id.end_button);

        startDate = findViewById(R.id.loan_start_dp);
        endDate = findViewById(R.id.loan_end_dp);

        lblStartDate = findViewById(R.id.loan_start_label);
        lblEndDate = findViewById(R.id.loan_end_label);

        start.setOnClickListener(view1 -> UXFunctions.select_date(start, startDate, lblStartDate)); // link the button to the date picker and label
        end.setOnClickListener(view1 -> UXFunctions.select_date(end, endDate, lblEndDate)); // link the button to the date picker and label
    }
}