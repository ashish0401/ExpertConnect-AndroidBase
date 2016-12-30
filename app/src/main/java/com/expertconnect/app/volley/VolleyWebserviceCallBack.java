package com.expertconnect.app.volley;



import org.json.JSONObject;

public interface VolleyWebserviceCallBack {
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode);

    public void onError(JSONObject serverResult, String requestTag, int mStatusCode);
}
