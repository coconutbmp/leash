package com.coconutbmp.leash;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class BudgetListLayout extends LinearLayout {
    TextView budgetName, budgetSummary;
    ImageView budgetDetails;
    LinearLayout textLayout;
    View divider;

    ArrayList<String> colors = new ArrayList<>();

    public BudgetListLayout(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.beige));

        colors.add("#FF00E676");
        colors.add("#FFFFC400");
        colors.add("#E53935");
        colors.add("#3D5AFE");

        int index = new Random().nextInt(5);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 8, 4, 8);

        LayoutParams dividerParams = new LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(4, 4, 4, 4);

        divider.setBackgroundColor(Color.parseColor(colors.get(index)));

        budgetName.setTextSize(22);
        budgetName.setTextColor(ContextCompat.getColor(context, R.color.grey));
        budgetName.setTypeface(ResourcesCompat.getFont(context, R.font.convergence));

        budgetSummary.setTextSize(12);
        budgetSummary.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));

        textLayout.addView(budgetName);
        addView(divider, dividerParams);
        addView(textLayout, params);
        addView(budgetDetails);
    }
}
