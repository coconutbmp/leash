package com.coconutbmp.leash.BudgetComponents;

import android.app.Activity;
import android.graphics.Color;
import android.telecom.Call;

import com.coconutbmp.leash.CompletionListener;
import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

import javax.security.auth.callback.Callback;

/***
 * Budget contains details of a budget and contains other components
 */
public class Budget extends BudgetComponent{

    Vector<Income> income_list = new Vector<Income>();
    Vector<Liability> liability_list = new Vector<Liability>();
    Vector<Transaction> transaction_list = new Vector<Transaction>();
    InternetRequest ir = new InternetRequest();

    /**
     * @param response
     * populate Incomes
     */
    void setIncomes(String response){
        Data.countDown();
        income_list.clear();
        JSONArray ja;
        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                // create new income and append to income_list
                income_list.add(new Income(this, (JSONObject) ja.get(i)));
            }
        }catch (Exception e){
            // callback to let any interested parties know that the operation was a failure
            Data.respond(false);
        }
        // callback to let any interested parties know that the operation was successful
        Data.respond(true);
    }

    public Vector<Income> getIncomes() { return income_list; };

    /**
     * @param response
     * populate Liabilities
     */
    void setLiabilities(String response){
        Data.countDown();
        liability_list.clear();
        JSONArray ja;
        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                // create new liability and append to liability_list
                liability_list.add(new Liability(this, (JSONObject) ja.get(i)));
            }
        }catch (Exception e){
            // callback to let any interested parties know that the operation was a failure
            Data.respond(false);
        }
        // callback to let any interested parties know that the operation was successful
        Data.respond(true);
    }

    public Vector<Liability> getLiabilities() { return liability_list; };

    /**
     * @param response
     * populate Transactions
     */
    void setTransactions(String response){
        Data.countDown();
        transaction_list.clear();
        JSONArray ja;

        try {
            ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++){
                // create new transaction and append to transaction_list
                transaction_list.add(new Transaction(this, (JSONObject) ja.get(i)));
            }
        }catch (Exception e){
            // callback to let any interested parties know that the operation was a failure
            Data.respond(false);
        }

        // callback to let any interested parties know that the operation was successful
        Data.respond(true);
    }

    public Vector<Transaction> getTransactions() { return transaction_list; };

    /**
     * @throws Exception
     * refresh liabilities after change or on creation
     */
    public void refreshLiabilities() throws Exception{
        InternetRequest ir = new InternetRequest();
        JSONObject params = new JSONObject();
        params.put("budgetid", this.getJsonRep().get("budget_ID"));
        ir.doRequest(
                InternetRequest.std_url+"get_liabilities.php",
                null,
                params,
                this::setLiabilities
        );
    }

    /**
     * @throws Exception
     * refresh incomes after some change or on creation
     */
    public void refreshIncomes() throws Exception{
        InternetRequest ir = new InternetRequest();
        JSONObject params = new JSONObject();

        params.put("budgetid", this.getJsonRep().get("budget_ID"));
        ir.doRequest(
                InternetRequest.std_url+"get_income.php",
                null,
                params,
                this::setIncomes
        );
    }

    /**
     * @throws Exception
     * refresh transactions after some change or on creation
     */
    public void refreshTransactions() throws Exception{
        InternetRequest ir = new InternetRequest();
        JSONObject params = new JSONObject();
        params.put("budgetid", getJsonRep().get("budget_ID"));
        ir.doRequest(
                InternetRequest.std_url+"get_all_transactions.php",
                null,
                params,
                this::setTransactions
        );
    }

    /**
     * initialization function
     */
    @Override
    public void initialize() throws Exception{
        //get income
        refreshIncomes();
        //get liabilities
        refreshLiabilities();
        //get transactions
        refreshTransactions();
    }

    /**
     * constructor for a new {@link Budget}
     * <br/>
     * @param json_rep the JSON Object containing details for the budget
     */
    public Budget(JSONObject json_rep){
        super(json_rep);
    }

    /**
     * Accumulates entries in lists into a {@link LineDataSet} subject
     * <br/>
     * with this we can generate accumulating lists
     * <br/>
     * @param subject a lineDataSet that is subject of accumulation
     * @param lists contains lists that need to be summed up into the subject p
     */
    private void accumulate(LineDataSet subject, ArrayList<ArrayList<Entry>> lists){
        float total=0f; // the accumulator
        float x=0f; // the base value for looking for the next element to accumulate
                    // this sort of a measure of time along a period [0..1]

        Entry hold = new Entry(0f,0f); // holds the next element to be accumulated;
        float min = Float.MAX_VALUE;

        ArrayList<Entry> used = new ArrayList<>();

        while (hold != null){ // while there are still elements to be accumulated
            x = hold.getX();
            total += hold.getY(); // add to
            used.add(hold);

            subject.addEntry(new Entry(x, total));

            hold = null;
            min = Float.MAX_VALUE;
            for(ArrayList<Entry> list: lists){ // find next value in each list
                for(Entry e: list){
                    // find a new x that is ( >= x; < min and is unused);
                    if(e.getX() >= x && e.getX() < min && !used.contains(e)) {

                        hold = e;
                        min = e.getX();
                    }
                }
            }
        }
        subject.addEntry(new Entry(1f, total));
    }

    /**
     * Get Line data between start and end dates
     * <br/>
     * @param start_date beginning of period
     * @param end_date end of period
     * @return corresponding LineData object
     */
    public LineData getPeriodSummary(LocalDate start_date, LocalDate end_date){
        LineData data = new LineData();

        float total_income = 0f, total_expense = 0f;
        float x = 0f;


        // set all the relevant line styling

        LineDataSet income_set = new LineDataSet(new ArrayList<>(), "Income");
        income_set.setLineWidth(2f);
        income_set.setFillAlpha(50);
        income_set.setFillColor(Color.rgb(55,234,100));
        income_set.setDrawFilled(true);
        income_set.setCircleColor(Color.rgb(55,234,100));
        income_set.setColor(Color.rgb(55,234,100));

        LineDataSet expense_set = new LineDataSet(new ArrayList<>(), "Liability");
        expense_set.setLineWidth(2f);
        expense_set.setFillAlpha(50);
        expense_set.setFillColor(Color.rgb(255,34,100));
        expense_set.setDrawFilled(true);
        expense_set.setCircleColor(Color.rgb(255,34,100));
        expense_set.setColor(Color.rgb(255,34,100));

        ArrayList<ArrayList<Entry>> income_sets = new ArrayList<>();
        ArrayList<ArrayList<Entry>> liability_sets = new ArrayList<>();

        for (int i = 0; i < income_list.size(); i++){
            // add income period summaries to the income_sets
            income_sets.add(income_list.get(i).getPeriodSummary(start_date, end_date));
        }

        accumulate(income_set, income_sets); // create an accumulative LineDataSet

        for (int i = 0; i < liability_list.size(); i++){
            // add liability period summaries to the liability_sets
            liability_sets.add(liability_list.get(i).getPeriodSummary(start_date, end_date));
        }

        accumulate(expense_set, liability_sets); // create an accumulative LineDataSet

        data.addDataSet(income_set);
        data.addDataSet(expense_set);

        return data;
    }

    /**
     * Gets response telling it whether deletion on server side was successful
     * and acts accordingly
     * @param response
     */
    @Override
    public void acceptDeletionResponse(String response){
        if(response.equals("success")) Data.removeBudget(this);
        super.acceptDeletionResponse(response);
    }

    /**
     * performs the delete action for the budget
     */
    @Override
    public void delete(){
        System.out.println("deleting budget");

        InternetRequest ir = new InternetRequest();
        JSONObject jo;
        try{
            jo = new JSONObject();
            jo.put("budgetID", getJsonRep().get("budget_ID"));
        } catch (Exception e){
            e.printStackTrace();
            Data.respond(false);
            return;
        }

        ir.doRequest(
                InternetRequest.std_url + "remove_budget.php",
                null,
                jo,
                this::acceptDeletionResponse
        );
    }

    /**
     * removes Transaction
     * @param t
     */
    public void removeTransaction(Transaction t){
        transaction_list.remove(t);
    }

    /**
     * removes Income
     * @param i
     */
    public void removeIncome(Income i){
        income_list.remove(i);
    }

    /**
     * removes Liability
     * @param l
     */
    public void removeLiability(Liability l){
        liability_list.remove(l);
    }
}
