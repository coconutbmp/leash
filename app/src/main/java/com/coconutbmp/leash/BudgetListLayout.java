package com.coconutbmp.leash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetListLayout extends LinearLayout {
    TextView budgetName, budgetSummary;
    CircleImageView budgetDetails;
    LinearLayout textLayout;
    View divider;
    JSONObject json_rep;

    ArrayList<String> colors = new ArrayList<>();

    public BudgetListLayout(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.smokey_white));

        colors.add("#FF00E676");
        colors.add("#FFFFC400");
        colors.add("#E53935");
        colors.add("#3D5AFE");

        int index = new Random().nextInt(4);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(4, 8, 4, 8);

        LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        params.setMargins(0, 8, 16, 8);

        LayoutParams dividerParams = new LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(16, 4, 4, 4);

        budgetName = new TextView(context);
        budgetSummary = new TextView(context);
        budgetDetails = new CircleImageView(context);
        textLayout = new LinearLayout(context);
        divider = new View(context);

        divider.setBackgroundColor(Color.parseColor(colors.get(index)));

        textLayout.setOrientation(VERTICAL);

        budgetName.setTextSize(22);
        budgetName.setTextColor(ContextCompat.getColor(context, R.color.grey));
        budgetName.setTypeface(ResourcesCompat.getFont(context, R.font.convergence));

        budgetSummary.setTextSize(12);
        budgetSummary.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));

        budgetDetails.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
        budgetDetails.setElevation(8);
        budgetDetails.setCircleBackgroundColor(ContextCompat.getColor(context, R.color.beige));

        textLayout.addView(budgetName);
        textLayout.addView(budgetSummary, params);

        addView(divider, dividerParams);
        addView(textLayout, params);
        addView(budgetDetails, imageParams);

        this.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), BudgetActivity.class);
            try{
                intent.putExtra("json_representation", json_rep.toString());
                intent.putExtra("budget_name", (String) json_rep.get("budget_Name"));
                intent.putExtra("budget_startdate", (String) json_rep.get("budget_StartDate"));
                intent.putExtra("budget_enddate", (String) json_rep.get("budget_EndDate"));
            } catch (Exception e){
                e.printStackTrace();
            }


            this.getContext().startActivity(intent);
        });
    }
}
