package com.coconutbmp.leash;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/submit_liability.php?user_id=2&liability_name=Lib&liability_type=Loan&start=2023/08/2&end=2023/08/2&category=Leisure&pay_freq=Monthly&rate=4&calc_freq=Monthly&principle=12000.00&payment_amt=323.33&interest_type=Simple

public class InternetRequest { // send and receive http requests to server
    Request request;
    OkHttpClient client = new OkHttpClient();
    public static final String std_url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/";

    // make request by passing a parameters fro  a json to server
    public void doRequest(String url, Activity activity, JSONObject parameters, com.coconutbmp.leash.RequestHandler requestHandler){
        HttpUrl.Builder test = HttpUrl.parse(url).newBuilder();
        List<String> keys = new ArrayList<>();
        Iterator<String> it = parameters.keys();
        it.forEachRemaining(e->keys.add(e)); // get json keys
        for (String key: keys){
            try {
                test.addQueryParameter(key, parameters.getString(key)); //pass keys and values as parameters
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        request = new Request.Builder().url(test.build()).build(); // create request with url and parameters

        client.newCall(request).enqueue(new Callback() {// handle server responses
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                if (activity != null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestHandler.processResponse(responseData);
                        }
                    });
                } else {
                    Thread response_thread = new Thread("Response"){
                        @Override
                        public void run(){
                            requestHandler.processResponse(responseData);
                        }
                    };
                    response_thread.start();
                }
            }
        });
    }
}
