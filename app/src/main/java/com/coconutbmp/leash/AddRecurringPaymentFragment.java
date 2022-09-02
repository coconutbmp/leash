package com.coconutbmp.leash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

/**
 * Fragment for the recurring payment section for the liability page
 * implements {@link LiabilityDetails}
 */
public class AddRecurringPaymentFragment extends Fragment implements LiabilityDetails {

    EditText rp_edit;
    EditText begin_date_edit;
    EditText end_date_edit;
    Spinner payment_freq_spinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRecurringPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recurring_payment_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecurringPaymentFragment newInstance(String param1, String param2) {
        AddRecurringPaymentFragment fragment = new AddRecurringPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurring_payment, container, false);
    }

    @Override
    public JSONObject getJSONRepresentation() throws Exception{
        JSONObject rep = new JSONObject();

        rep.put("recurring_payment_amt", Double.parseDouble(String.valueOf(rp_edit.getText())));
        rep.put("payment_frequency", payment_freq_spinner.getSelectedItem());
        rep.put("start_date", begin_date_edit.getText());
        rep.put("end_date", end_date_edit.getText());

        return rep;
    }
}