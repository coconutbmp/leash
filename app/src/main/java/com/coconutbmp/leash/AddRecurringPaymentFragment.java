package com.coconutbmp.leash;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Fragment for the recurring payment section for the liability page
 * implements {@link LiabilityDetails}
 */
public class AddRecurringPaymentFragment extends Fragment implements LiabilityDetails {

    EditText rp_edit;
    TextView begin_date_edit, end_date_edit;
    Spinner payment_freq_spinner;
    Button start_button, end_button;
    DatePicker start_dp, end_dp;

    /**
     * Constructor
     */
    public AddRecurringPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recurring_payment, container, false);
        rp_edit = view.findViewById(R.id.rp_edit);
        payment_freq_spinner = view.findViewById(R.id.payment_freq_spinner);
        begin_date_edit = view.findViewById(R.id.rp_start_label);
        end_date_edit = view.findViewById(R.id.rp_end_label);
        start_button = view.findViewById(R.id.set_rp_start_date_button);
        end_button = view.findViewById(R.id.set_rp_end_date_button);
        start_dp = view.findViewById(R.id.start_dp);
        end_dp = view.findViewById(R.id.end_dp);

        String[] freq = getResources().getStringArray(R.array.calculation_frequencies);
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, freq);
        payment_freq_spinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.smokey_white));
        payment_freq_spinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        payment_freq_spinner.setAdapter(freqAdapter);

        start_button.setOnClickListener(v1 -> UXFunctions.select_date(start_button, start_dp, begin_date_edit));// link the button to the date picker and label
        end_button.setOnClickListener(v2 -> UXFunctions.select_date(end_button, end_dp, end_date_edit));// link the button to the date picker and label

        return view;
    }

    /**
     * implementation for parsing the data in this class to json
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getJSONRepresentation() throws Exception{
        JSONObject rep = new JSONObject();

        rep.put("payment_amt", Double.parseDouble(String.valueOf(rp_edit.getText())));
        rep.put("pay_freq", payment_freq_spinner.getSelectedItem());
        rep.put("start", begin_date_edit.getText());
        rep.put("end", end_date_edit.getText());

        return rep;
    }
}