package com.coconutbmp.leash;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class AddLiabilityActivity extends AppCompatActivity {

    Button continue_button;
    Button cancel_button;

    Spinner liability_type_spinner;
    Spinner category_spinner;

    EditText name_edit;

    LinearLayout frag_container;

    FragmentManager frag_man;
    FragmentTransaction frag_tran;

    public void displayByContext(String type){
        System.out.printf("\n\n %s \n\n", type);

        frag_man = getSupportFragmentManager();
        frag_tran = frag_man.beginTransaction();

        if(Objects.equals(type, "Loan")){
            System.out.println("performing transaction");
            AddLoanDetailsFragment loan_frag = new AddLoanDetailsFragment();
            frag_tran.replace(frag_container.getId(), loan_frag).commit();
        } else if (Objects.equals(type, "Recurring Payment")){
            AddRecurringPaymentFragment recurring_payment_frag = new AddRecurringPaymentFragment();
            frag_tran.replace(frag_container.getId(), recurring_payment_frag).commit();
        } else {
            System.out.println("failed");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_liability);

        cancel_button = findViewById(R.id.cancel_button);
        continue_button = findViewById(R.id.continue_button);
        liability_type_spinner = findViewById(R.id.liability_type_spinner);
        category_spinner = findViewById(R.id.category_spinner);
        name_edit = findViewById(R.id.name_edit);
        frag_container = findViewById(R.id.frag_container_ll);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("olo");
            }
        });


        displayByContext(liability_type_spinner.getSelectedItem().toString());

        liability_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.print("selected -> ");
                System.out.println(liability_type_spinner.getSelectedItem());
                displayByContext(liability_type_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("nothing selected");
            }
        });

    }
}