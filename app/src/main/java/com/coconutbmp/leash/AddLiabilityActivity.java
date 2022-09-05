package com.coconutbmp.leash;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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

    /**
     *
     * @param sender
     * @param target_dp
     * @param target_tv
     *
     *  if the "set date" buttons' text is set to "set date" then
     *      set the height of the date picker to 150dp;
     *      change the button text to "save"
     *  else if its "save" then
     *      set the textview to then selected date and set the button text to "set date"
     */
    public static void select_date(Button sender, DatePicker target_dp, TextView target_tv){
        String save = "Save";
        String set = "Set Date";
        try {
            if (sender.getText().equals(save)) {
                sender.setText(set);
                target_dp.getLayoutParams().height = 0;

                String resulting_date = Integer.toString(target_dp.getYear()) + "-";
                resulting_date = resulting_date + Integer.toString(target_dp.getMonth()) + "-" ;
                resulting_date = resulting_date + Integer.toString(target_dp.getDayOfMonth())  ;

                target_tv.setText(resulting_date);


            } else if (sender.getText().equals(set)) {
                System.out.println("<- olo ->");
                sender.setText(save);
                // convert 150 dp to pixels
                final float scale = sender.getContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (150 * scale + 0.5f);
                target_dp.getLayoutParams().height = pixels;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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

        cancel_button = findViewById(R.id.ReturnCard);
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