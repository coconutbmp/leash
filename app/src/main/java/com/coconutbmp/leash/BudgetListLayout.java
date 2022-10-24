package com.coconutbmp.leash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetListLayout extends LinearLayout {
    // Declare Objects
    TextView budgetName, budgetSummary;
    CardView budgetDetails;
    ImageView image;
    LinearLayout textLayout;
    View divider;
    JSONObject json_rep;

    //List of indicator colors
    ArrayList<String> colors = new ArrayList<>();

    public BudgetListLayout(Context context) {
        // Layout Properties
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.smokey_white));

        // Indicator Colors
        colors.add("#FF00E676");
        colors.add("#FFFFC400");
        colors.add("#E53935");
        colors.add("#3D5AFE");

        int index = new Random().nextInt(4);

        //Object positions and margins
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(4, 8, 4, 8);

        LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        params.setMargins(4, 4, 4, 4);

        LayoutParams dividerParams = new LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(16, 4, 4, 4);

        LayoutParams btnParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(4, 4, 16, 4);

        // Instantiate objects
        budgetName = new TextView(context);
        budgetSummary = new TextView(context);
        budgetDetails = new CardView(context);
        image = new ImageView(context);
        textLayout = new LinearLayout(context);
        divider = new View(context);

        // set indicator color
        divider.setBackgroundColor(Color.parseColor(colors.get(index)));

        textLayout.setOrientation(VERTICAL);

        // text properties
        budgetName.setTextSize(22);
        budgetName.setTextColor(ContextCompat.getColor(context, R.color.grey));
        budgetName.setTypeface(ResourcesCompat.getFont(context, R.font.convergence));

        budgetSummary.setTextSize(12);
        budgetSummary.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));

        // Button Properties
        image.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
        budgetDetails.setCardBackgroundColor(ContextCompat.getColor(context, R.color.beige));
        budgetDetails.setRadius(16f);
        budgetDetails.setElevation(8);
        budgetDetails.addView(image, imageParams);

        textLayout.addView(budgetName);
        textLayout.addView(budgetSummary, params);

        // add objects to layout
        addView(divider, dividerParams);
        addView(textLayout, params);
        addView(budgetDetails, btnParams);

        // onclick for layout
        this.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), BudgetActivity.class);
            Data.setCurrent(json_rep);
            this.getContext().startActivity(intent);
        });
    }
}
