package com.example.kevinramirez.openweatcher.Misc;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by william.montiel on 4/11/2017.
 */

public class Connection {

    protected final String TAG = "Connection";

    public void endPoint(final String url, final String jsonPost, final Context context,final Boolean isPost,final VolleyCallback  callback) throws Exception{
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(
                    (isPost == true)?1:0,
                    url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error.getMessage());
                        }
                    }) {
                @Override
                public Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("data", jsonPost);

                    return params;
                }            };
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {}
            });

            requestQueue.add(stringRequest);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

    public interface VolleyCallback{
        void onSuccess(String result);
        void onError(String result);
    }

}
