package com.coconutbmp.leash;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetBriefFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetBriefFragment extends Fragment {

    JSONObject details;

    TextView name_label;
    CardView card;


    public BudgetBriefFragment(JSONObject details) {
        this.details = details;
    }

    public BudgetBriefFragment(){

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BudgetBriefFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetBriefFragment newInstance(String param1, String param2) {
        BudgetBriefFragment fragment = new BudgetBriefFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget_brief, container, false);
        name_label = view.findViewById(R.id.name_label);
        card = view.findViewById(R.id.card_view);
        card.setOnClickListener(view1 -> {
            Intent i = new Intent(this.getContext(), Budget.class);
        });

        try{
            name_label.setText(details.getString("budget_Name"));
        } catch (Exception e){
            e.printStackTrace();
        }



        return view;
    }
}