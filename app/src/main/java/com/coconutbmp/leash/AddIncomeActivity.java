package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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
    Spinner incomeFrequency;
    CardView returnCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        start = findViewById(R.id.start_button);
        end = findViewById(R.id.end_button);
        save = findViewById(R.id.btnSaveIncome);

        startDate = findViewById(R.id.loan_start_dp);
        endDate = findViewById(R.id.loan_end_dp);

        lblStartDate = findViewById(R.id.loan_start_label);
        lblEndDate = findViewById(R.id.loan_end_label);

        returnCard = findViewById(R.id.ReturnCard);

        incomeFrequency = findViewById(R.id.sprRecurringFrequency2);

        start.setOnClickListener(view1 -> UXFunctions.select_date(start, startDate, lblStartDate)); // link the button to the date picker and label
        end.setOnClickListener(view1 -> UXFunctions.select_date(end, endDate, lblEndDate)); // link the button to the date picker and label

        String[] freq = getResources().getStringArray(R.array.calculation_frequencies);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, freq);
        incomeFrequency.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));
        incomeFrequency.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        returnCard.setOnClickListener(view1->{
            AddIncomeActivity.this.finish();
        });
    }
}