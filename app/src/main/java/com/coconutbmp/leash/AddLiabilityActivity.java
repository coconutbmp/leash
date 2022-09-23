package com.coconutbmp.leash;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Controller for the Add Liability Activity
 *
 * You can add a Loan or a Recurring payment within
 */
public class AddLiabilityActivity extends AppCompatActivity {

    Button continue_button;
    CardView cancel_button;

    Spinner liability_type_spinner;
    Spinner category_spinner;

    EditText name_edit;

    LinearLayout frag_container;

    FragmentManager frag_man;
    FragmentTransaction frag_tran;
    JSONObject json_rep;
    LiabilityDetails ld_container;
    int budget_id;


    void handleSubmissionResponse(String response){
        System.out.println(response);
        if (!response.equals("failed")){
            Toast.makeText(this, "Liability Added", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(this, "Failed, Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean submitLiability(JSONObject final_rep){
        System.out.println(final_rep.toString());
        InternetRequest ir = new InternetRequest();

        ir.doRequest(
                InternetRequest.std_url + "submit_liability.php",
                this,
                final_rep,
                this::handleSubmissionResponse
        );

        try {
            if(((String)final_rep.get("liability_type")).equals("loan"));
                JSONObject transaction = new JSONObject();
                transaction.put("budgetid", (String)Data.current.getJsonRep().get("budget_ID"));
                transaction.put("transactiontype", "once-off income");
                transaction.put("transactionamount", (String) json_rep.get("payment_amt"));
                ir.doRequest(
                        InternetRequest.std_url + "submit_transaction.php",
                        this,
                        transaction,
                        response ->{
                            if(response.toUpperCase().charAt(0) == 'S'){
                                System.out.println("successfully added loan");
                            } else System.out.println("failed to add loan");
                        }
                );
        } catch (Exception e){
            e.printStackTrace();
        }


        return true;
    }

    private void createJSONRepresentation() throws Exception {
        json_rep = new JSONObject();
        json_rep.put("liability_name", name_edit.getText());
        json_rep.put("category", category_spinner.getSelectedItem());
        json_rep.put("budget_id", Data.current.getJsonRep().get("budget_ID"));
        System.out.println(Data.current.getJsonRep().get("budget_ID"));
        String lt = liability_type_spinner.getSelectedItem().toString().split(" ")[0].toLowerCase();
        json_rep.put("liability_type", lt);

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

        if(Objects.equals(type, "Loan")){ // display Loan form
            AddLoanDetailsFragment loan_frag = new AddLoanDetailsFragment();
            ld_container = loan_frag;
            frag_tran.replace(frag_container.getId(), loan_frag).commit();
        } else if (Objects.equals(type, "Recurring Expense")){ // display Recurring payment form
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

        cancel_button = findViewById(R.id.ReturnCard);
        continue_button = findViewById(R.id.continue_button);
        liability_type_spinner = findViewById(R.id.liability_type_spinner);
        category_spinner = findViewById(R.id.category_spinner);
        name_edit = findViewById(R.id.name_edit);
        frag_container = findViewById(R.id.frag_container_ll);

        cancel_button.setOnClickListener(view -> finish()); // close this activity when cancel clicked

        continue_button.setOnClickListener(view -> {
            try{
                createJSONRepresentation(); // create json storing relevant data about the liability to add.
                submitLiability(json_rep);
            } catch (Exception e){
                System.out.println("json parsing failed");
                e.printStackTrace();
            }

        });

        String[] cats = getResources().getStringArray(R.array.predefined_categories);
        String[] types = getResources().getStringArray(R.array.liability_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, cats);
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, R.layout.spinner_text, types);
        liability_type_spinner.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));
        liability_type_spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        category_spinner.setPopupBackgroundDrawable(getResources().getDrawable(R.color.smokey_white));
        category_spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        category_spinner.setAdapter(adapter);
        liability_type_spinner.setAdapter(adapter_type);

        // initialize what form were on, based on what type if liability is first in the spinner
        displayByContext(liability_type_spinner.getSelectedItem().toString());

        /**
         * if "loan" selected -> display loan form
         * if "recurring payment" selected -> display that form
         */
        liability_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayByContext(liability_type_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("nothing selected");
            }
        });
    }
}