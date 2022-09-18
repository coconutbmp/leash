package com.coconutbmp.leash;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.core.content.ContextCompat;

public class LiabilityHistoryLayout extends LinearLayout {
    TextView name, amount, date, interest;
    View divider;
    public LiabilityHistoryLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);

        setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        name.setTextSize(24);
        amount.setTextSize(18);
        date.setTextSize(10);
        interest.setTextSize(10);

        name.setTextColor(ContextCompat.getColor(context, R.color.grey));
        amount.setTextColor(ContextCompat.getColor(context, R.color.light_brown));
        name.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));
        name.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));

        LayoutParams dividerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4);
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.beige));
    }
}
