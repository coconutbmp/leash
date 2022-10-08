package com.coconutbmp.leash;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coconutbmp.leash.BudgetComponents.BudgetComponent;
import com.coconutbmp.leash.BudgetComponents.Transaction;


public class BudgetComponentFragment extends Fragment {

    public TextView name_label;
    public LinearLayout details_ll;
    public CardView card;
    public ImageButton manage_button;
    protected BudgetComponent budget_comp;

    public BudgetComponentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    void initiate_view() throws Exception{

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_budget_component, container, false);

        name_label = v.findViewById(R.id.name_label);
        details_ll = v.findViewById(R.id.details_ll);
        card = v.findViewById(R.id.component_card_view);
        manage_button = v.findViewById(R.id.manage_button);

        manage_button.setOnClickListener(view -> {
            ManageBudgetDialog dialog = new ManageBudgetDialog(this.getActivity(), v.getContext(), budget_comp);
            dialog.show();
        });

        try{
            initiate_view();
        } catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }
}