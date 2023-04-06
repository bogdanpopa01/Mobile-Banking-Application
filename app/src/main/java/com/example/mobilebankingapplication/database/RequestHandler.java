package com.example.mobilebankingapplication.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandler {
    private static RequestHandler mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private RequestHandler(Context context){
        mContext = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new RequestHandler(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> requestQueue){
        getRequestQueue().add(requestQueue);
    }
}
