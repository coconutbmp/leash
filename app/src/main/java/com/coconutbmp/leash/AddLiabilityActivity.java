package com.coconutbmp.leash;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

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
    JSONObject json_rep;
    LiabilityDetails ld_container;

    private void createJSONRepresentation() throws Exception {
        json_rep = new JSONObject();
        json_rep.put("liability_name", name_edit.getText());
        json_rep.put("liability_category", category_spinner.getSelectedItem());
        json_rep.put("liability_type", liability_type_spinner.getSelectedItem());

        LiabilityDetails details_container = ld_container;
        JSONObject details = details_container.getJSONRepresentation();
        for (int i = 0; i < Objects.requireNonNull(details.names()).length(); i++) {
            json_rep.put(Objects.requireNonNull(details.names()).getString(i), details.get(details.names().getString(i)));
        }

        System.out.println(json_rep);
    }

    /**
     *
     * @param type liability type
     *
     * display fragment corresponding to the type of liability selected
     *
     */
    public void displayByContext(String type){
        frag_man = getSupportFragmentManager();
        frag_tran = frag_man.beginTransaction(); // allows for switching elements with a fragment

        if(Objects.equals(type, "Loan")){
            AddLoanDetailsFragment loan_frag = new AddLoanDetailsFragment();
            ld_container = loan_frag;
            frag_tran.replace(frag_container.getId(), loan_frag).commit();
        } else if (Objects.equals(type, "Recurring Payment")){
            AddRecurringPaymentFragment recurring_payment_frag = new AddRecurringPaymentFragment();
            ld_container = recurring_payment_frag;
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

        cancel_button.setOnClickListener(view -> finish()); // close this activity when cancel clicked
        continue_button.setOnClickListener(view -> {
            try{
                createJSONRepresentation();
            } catch (Exception e){
                System.out.println("json parsing failed");
                e.printStackTrace();
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