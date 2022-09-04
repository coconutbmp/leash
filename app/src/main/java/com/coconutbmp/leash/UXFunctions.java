package com.coconutbmp.leash;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UXFunctions {
    static public void setDate(TextView day, TextView month){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

        String today = sdf.format(new Date());

        day.setText(today.substring(0, 2));
        month.setText(today.substring(3, 6));
    }
}
