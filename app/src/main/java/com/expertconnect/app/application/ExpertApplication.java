package com.expertconnect.app.application;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.expertconnect.app.utils.Foreground;

/**
 * Created by chinar on 7/11/16.
 */
public class ExpertApplication extends MultiDexApplication {

    private static ExpertApplication mInstance;
    private RequestQueue mRequestQueue;  // volley request Queue
    public static final String TAG = ExpertApplication.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        Foreground.init(mInstance);
    }
    public static synchronized ExpertApplication getInstance() {
        return mInstance;
    }

    /*
    * Get volley request queue
    * */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /*
     * Add request to volley queue with tag
     * @param request
     * @param tag to request
     * */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /*
     * Add request to volley queue without tag
     * @param request
     * */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}

