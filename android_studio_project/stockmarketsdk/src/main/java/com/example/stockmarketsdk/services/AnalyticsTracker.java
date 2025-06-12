package com.example.stockmarketsdk.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockmarketsdk.ApiClient;

import org.json.JSONObject;

public class AnalyticsTracker {

    private static final String TAG = "Analytics";
    private static final String BASE_URL = "https://stockmarketapi-qr65.onrender.com/";

    public static void logStockView(Context context, String symbol, String userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("event", "stock_view");
            data.put("symbol", symbol);
            data.put("user_id", userId);

            sendRequest(context, data);
        } catch (Exception e) {
            Log.e(TAG, "Error logging stock_view", e);
        }
    }

    public static void logScreenView(Context context, String screen, String userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("event", "screen_view");
            data.put("screen", screen);
            data.put("user_id", userId);

            sendRequest(context, data);
        } catch (Exception e) {
            Log.e(TAG, "Error logging screen_view", e);
        }
    }


    public static void logEvent(Context context, String event, String symbol, String userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("event", event);
            data.put("symbol", symbol);
            data.put("user_id", userId);

            sendRequest(context, data);
        } catch (Exception e) {
            Log.e(TAG, "Error logging " + event, e);
        }
    }


    public static void logScreenTime(Context context, String screen, double duration, String userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("event", "screen_time");
            data.put("screen", screen);
            data.put("duration", duration);
            data.put("user_id", userId);

            sendRequest(context, data);
        } catch (Exception e) {
            Log.e(TAG, "Error logging screen_time", e);
        }
    }

    private static void sendRequest(Context context, JSONObject data) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "analytics/events",
                data,
                response -> Log.d(TAG, "Event sent: " + response),
                error -> Log.e(TAG, "Failed to send event: " + error.getMessage())
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
