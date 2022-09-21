package com.coconutbmp.leash;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BudgetComponentFragment extends Fragment {

    public TextView name_label;
    public LinearLayout details_ll;
    public CardView card;

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
        try{
            initiate_view();
        } catch (Exception e){
            e.printStackTrace();
        }


        return v;
    }
}