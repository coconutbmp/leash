package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

public class BudgetComponent {
    private String type;
    private JSONObject json_rep;
    protected InternetRequest ir;
    public BudgetComponent parent;

    BudgetComponent(JSONObject json_rep){
        try{
            setJsonRep(json_rep);
            initialize();
            parent = this;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    BudgetComponent(BudgetComponent parent, JSONObject json_rep){
        try{
           setJsonRep(json_rep);
           initialize();
           this.parent = parent;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setJsonRep(JSONObject json_rep) {
        this.json_rep = json_rep;
    }

    public JSONObject getJsonRep() {
        return json_rep;
    }

    public void initialize() throws Exception{
        parent = this;
        ir = new InternetRequest();
    }
}
