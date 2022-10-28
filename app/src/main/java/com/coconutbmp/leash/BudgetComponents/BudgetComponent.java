package com.coconutbmp.leash.BudgetComponents;

import android.widget.Toast;

import com.coconutbmp.leash.Data;
import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

/**
 * Super class for Budget related things
 */
public class BudgetComponent {
    private String type;
    private JSONObject json_rep;
    protected InternetRequest ir;
    public BudgetComponent parent;

    /**
     * Super constructor accepting a json representation
     * @param json_rep
     */
    BudgetComponent(JSONObject json_rep){
        try{
            setJsonRep(json_rep);
            initialize();
            parent = this;
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    /**
     * Super constructor accepting a json representation and a parent {@link BudgetComponent}
     * @param parent
     * @param json_rep
     */
    BudgetComponent(BudgetComponent parent, JSONObject json_rep){
        try{
           setJsonRep(json_rep);
           initialize();
           this.parent = parent;
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    /**
     * sets the json representation
     * @param json_rep
     */
    public void setJsonRep(JSONObject json_rep) {
        this.json_rep = json_rep;
    }

    /**
     * returns the current json representation
     * @return
     */
    public JSONObject getJsonRep() {
        return json_rep;
    }

    /**
     * super initialization step
     * @throws Exception
     */
    public void initialize() throws Exception{
        parent = this;
        ir = new InternetRequest();
    }

    /**
     * default deletion step after receiving a response
     * @param response
     */
    void acceptDeletionResponse(String response){
        if(response.equals("success")){
            Data.respond(true);
            return;
        }

        Data.respond(false);
    }

    /**
     * deletion function
     */
    public void delete(){

    }
}
