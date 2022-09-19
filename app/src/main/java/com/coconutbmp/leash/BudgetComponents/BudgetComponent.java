package com.coconutbmp.leash.BudgetComponents;

import com.coconutbmp.leash.InternetRequest;

import org.json.JSONObject;

public class BudgetComponent {
    private String type;
    private JSONObject json_rep;
    protected InternetRequest ir;

    BudgetComponent(JSONObject json_rep){
        try{
            setJsonRep(json_rep);
            initialize();
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
        ir = new InternetRequest();
    }
}
