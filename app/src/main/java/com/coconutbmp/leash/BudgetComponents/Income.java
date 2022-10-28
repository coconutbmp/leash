package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Income extends BudgetComponent{
    /**
     * constructor for Income
     * @param parent
     * @param json_rep
     */
    public Income(BudgetComponent parent, JSONObject json_rep) {
        super(parent, json_rep);
    }

    /**
     * Get the period sumary for this income
     * @param start_date
     * @param end_date
     * @return
     */
    ArrayList<Entry> getPeriodSummary(LocalDate start_date, LocalDate end_date){

        ArrayList<Entry> entries = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) try {
            Frequency f = Frequency.valueOf(getJsonRep().getString("income_Frequency").toUpperCase());
            LocalDate income_start = Liability.stringToLocalDate(getJsonRep().getString("income_StartDate"));
            LocalDate income_end = Liability.stringToLocalDate(getJsonRep().getString("income_EndDate"));
            LocalDate date = null;

            date = LocalDate.from(income_start);

            while (date.isBefore(start_date)){ // move to the start of the period
                if(f == Frequency.MONTHLY) {
                    date = date.plusMonths(1);
                }
            }

            while (date.isEqual(start_date) || (date.isAfter(start_date) && date.isBefore(end_date))) // add entries while date is less than the end date
                if(f == Frequency.MONTHLY){
                    date = date.plusMonths(1);
                    entries.add(new Entry((float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("income_Amount"))); // scale ove a month
                } else if (f == Frequency.WEEKLY){
                    date = date.plusDays(7);
                }

        }catch (Exception e){
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Gets response telling it whether deletion on server side was successful
     * and acts accordingly
     * @param response
     */
    @Override
    public void acceptDeletionResponse(String response){
        if(response.equals("success")) ((Budget) this.parent).removeIncome(this);
        super.acceptDeletionResponse(response);
    }

    /**
     * initiate deletion process
     */
    @Override
    public void delete(){
        System.out.println("deleting income");
        InternetRequest ir = new InternetRequest();
        JSONObject jo;
        try{
            jo = new JSONObject();
            jo.put("incomeID", getJsonRep().get("income_ID"));
        } catch (Exception e){
            e.printStackTrace();
            Data.respond(false);
            return;
        }

        ir.doRequest(
                InternetRequest.std_url + "delete_income.php",
                null,
                jo,
                this::acceptDeletionResponse
        );

    }

}
