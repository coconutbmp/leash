package com.coconutbmp.leash;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.core.content.ContextCompat;

public class LiabilityHistoryLayout extends LinearLayout {
    TextView name, amount, date, interest;
    View divider;
    LinearLayout details;
    public LiabilityHistoryLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);

        setBackgroundColor(ContextCompat.getColor(context, R.color.beige));

        name = new TextView(context);
        amount = new TextView(context);
        date = new TextView(context);
        interest = new TextView(context);
        divider = new View(context);
        details = new LinearLayout(context);


        name.setTextSize(24);
        amount.setTextSize(24);
        date.setTextSize(10);
        interest.setTextSize(10);

        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        name.setTextColor(ContextCompat.getColor(context, R.color.grey));
        amount.setTextColor(ContextCompat.getColor(context, R.color.grey));
        amount.setGravity(Gravity.RIGHT);
        date.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));
        interest.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));

        LayoutParams dividerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4);
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.beige));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        details.setOrientation(HORIZONTAL);

        details.addView(name, textParams);
        details.addView(amount, textParams);
        addView(details, layoutParams);
        addView(date);
        addView(interest);
        addView(divider, dividerParams);
    }
}
