package com.coconutbmp.leash;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UXFunctions { // functions that will be reused throughout UI
    static public void setDate(TextView day, TextView month){ // display date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

        String today = sdf.format(new Date());

        day.setText(today.substring(0, 2));
        month.setText(today.substring(3, 6));
    }

    /**
     *
     * @param sender
     * @param target_dp
     * @param target_tv
     *
     *  if the "set date" buttons' text is set to "set date" then
     *      set the height of the date picker to 150dp;
     *      change the button text to "save"
     *  else if its "save" then
     *      set the textview to then selected date and set the button text to "set date"
     */
    public static void select_date(Button sender, DatePicker target_dp, TextView target_tv){
        String save = "Save";
        String set = "Set Date";
        try {
            if (sender.getText().equals(save)) {
                sender.setText(set);
                target_dp.getLayoutParams().height = 0;

                String resulting_date = Integer.toString(target_dp.getYear()) + "-" ;
                resulting_date = resulting_date + Integer.toString(target_dp.getMonth() + 1) + "-" ;
                resulting_date = resulting_date + Integer.toString(target_dp.getDayOfMonth())  ;

                target_tv.setText(resulting_date);


            } else if (sender.getText().equals(set)) {
                System.out.println("<- olo ->");
                sender.setText(save);
                // convert 150 dp to pixels
                final float scale = sender.getContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (150 * scale + 0.5f);
                target_dp.getLayoutParams().height = pixels;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
