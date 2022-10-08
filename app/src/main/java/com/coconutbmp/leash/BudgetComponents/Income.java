package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Income extends BudgetComponent{

    public Income(BudgetComponent parent, JSONObject json_rep) {
        super(parent, json_rep);
    }

    ArrayList<Entry> getPeriodSummary(LocalDate start_date, LocalDate end_date){

        ArrayList<Entry> entries = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) try {
            Frequency f = Frequency.valueOf(getJsonRep().getString("income_Frequency").toUpperCase());
            LocalDate income_start = Liability.stringToLocalDate(getJsonRep().getString("income_StartDate"));
            LocalDate income_end = Liability.stringToLocalDate(getJsonRep().getString("income_EndDate"));
            LocalDate date = null;

            date = LocalDate.from(income_start);

            while (date.isBefore(start_date)){
                if(f == Frequency.MONTHLY) {
                    date = date.plusMonths(1);
                }
            }

            while (date.isEqual(start_date) || (date.isAfter(start_date) && date.isBefore(end_date)))
                if(f == Frequency.MONTHLY){
                    date = date.plusMonths(1);
                    //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+  date.getDayOfMonth() );
                    entries.add(new Entry((float) date.getDayOfMonth()/date.getMonth().length(date.isLeapYear()), (float) getJsonRep().getDouble("income_Amount")));
                } else if (f == Frequency.WEEKLY){
                    date = date.plusDays(7);
                }

        }catch (Exception e){
            e.printStackTrace();
        }

        return entries;
    }

    @Override
    public void acceptDeletionResponse(String response){
        System.out.println(this.parent.getJsonRep());
        if(response.equals("success")) ((Budget) this.parent).removeIncome(this);
        super.acceptDeletionResponse(response);
    }

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
