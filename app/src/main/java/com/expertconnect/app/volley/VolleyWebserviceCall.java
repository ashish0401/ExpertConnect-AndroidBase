package com.expertconnect.app.volley;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.expertconnect.app.application.ExpertApplication;

import org.json.JSONException;
import org.json.JSONObject;


public class VolleyWebserviceCall {

    private Dialog pd;
    int mStatusCode = 0;

    /*
         * Post request
         * @param Context
         * @param URL to server
         * @param HAshmap of keys and values to be passed with service
         * @param Callback
         * */

    public void volleyPostCallJsonRequest(final Context context, final String url,
                                          final JSONObject jObj,
                                          final VolleyWebserviceCallBack callBack,
                                          final Boolean showProgressDialog, final String tag) {
        mStatusCode = 0;
        if (showProgressDialog) {
            ((Activity) context).setProgressBarIndeterminateVisibility(true);
            pd = Dialogs.showCircleLoading(context);
//            SUtil.showCircleLoading(context);
            pd.show();
        }


        final JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (showProgressDialog) {
                    ((Activity) context)
                            .setProgressBarIndeterminateVisibility(false);
                }
                callBack.onSuccess(jsonObject, tag, mStatusCode);

                try {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (showProgressDialog) {
                    try {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                callBack.onError(new JSONObject(), tag, mStatusCode);
                if (showProgressDialog) {
                    ((Activity) context).setProgressBarIndeterminateVisibility(false);
                }


                error.printStackTrace();
                // all errors will be recieved here
                if (error instanceof NetworkError) {
                    if (showProgressDialog) {
                        Dialogs.showAlert(context, "Network Error",
                                "PLease try after some time.!");
                    }
                } else if (error instanceof ServerError) {
//                     Utils.printLog("ServerError", "volley timeout+++");


                } else if (error instanceof AuthFailureError) {
//                     Utils.printLog("AuthFailureError", "volley timeout");
                } else if (error instanceof ParseError) {
//                     Utils.printLog("ParseError", "volley timeout");
                } else if (error instanceof NoConnectionError) {
//                     Utils.printLog("NoConnectionError", "volley timeout");
                } else if (error instanceof TimeoutError) {
//                     Utils.printLog("TimeoutError", "volley timeout");
                    if (showProgressDialog) {
                        Dialogs.showAlert(context, "Network Error",
                                "PLease try after some time.!");
                    } else {
//                         Utils.printLog("volley timeout", "volley timeout");
                    }
                }

            }


        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;

                return super.parseNetworkResponse(response);
//                AyprocaConstants.IS_UPDATE_PROFILE = true;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {

            }
        });


        // Adding request to request queue
        ExpertApplication.getInstance()
                .addToRequestQueue(request, tag);
    }
    /*
     * GET request
     * @param Context
     * @param URL to server
     * @param Callback
     * */
    public void volleyGetCall(final Context context, final String url, final VolleyWebserviceCallBack callBack, final String tag) {
        ((Activity) context).setProgressBarIndeterminateVisibility(true);

        pd = Dialogs.showCircleLoading(context);
        pd.show();

        String tag_json_obj = context.getClass().getSimpleName(); // tag will be requied to identify request

        StringRequest gsonRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String serverJson) {
                        ((Activity) context).setProgressBarIndeterminateVisibility(false);
                        try {
                            callBack.onSuccess(new JSONObject(serverJson), tag, mStatusCode);
                            if (pd != null && pd.isShowing()) {
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                ((Activity) context)
                        .setProgressBarIndeterminateVisibility(false);

                error.printStackTrace();
                // all errors will be recieved here
                if (error instanceof NetworkError) {
                    Dialogs.showAlert(context, "Network Error!", "Please try after some time");
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Dialogs.showAlert(context, "Network Error!", "Please try after some time, due to slow connection");
                }
            }
        });
        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(gsonRequest, tag_json_obj);
    }

}