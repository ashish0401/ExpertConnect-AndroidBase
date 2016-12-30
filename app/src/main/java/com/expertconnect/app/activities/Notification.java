package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.adapters.BrowseEnquiryAdapter;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.interfaces.OnOfferButtonClick;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.models.TeacherListResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification extends Activity implements View.OnClickListener, VolleyWebserviceCallBack,OnOfferButtonClick {
    private BrowseEnquiryAdapter mBrowseAdapter;
    private RecyclerView mRvBrowseEnq;
    private ArrayList<TeacherCategory> mAlBrowseEnq;
    private TeacherListResponse mObjBrowse;
    private Context mContext;
    private TextView mTvTitle;
    private ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = Notification.this;
        initView();
        initListener();
        setFonts();

        if (Utils.isConnected(mContext)) {
            callBrowseEnquiryAPI();
        } else {
            Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }

    }


    private void initView() {
        mImgBack=(ImageView)findViewById(R.id.back);
        mTvTitle=(TextView)findViewById(R.id.title);
        mRvBrowseEnq=(RecyclerView)findViewById(R.id.rv_browse_enquiry);
        mRvBrowseEnq.setLayoutManager(new LinearLayoutManager(mContext));

    }


    private void initListener() {
        mImgBack.setOnClickListener(this);
    }

    private void setFonts() {

        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));


    }

    private void callBrowseEnquiryAPI() {

        String user_id=String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID,mContext));
        String urlLogin = Constants.BASE_URL + Constants.BROWSE_ENQUIRY;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
                jsonObject.put(Constants.TEACHER_ID,user_id);
                jsonObject.put(Constants.CATEGORY_ID,"0");
                jsonObject.put(Constants.SUB_CATEGORY_ID,"0");
                jsonObject.put(Constants.FILTER,"no");
                jsonObject.put(Constants.AMOUNT,"0");

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.BROWSE_ENQUIRY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseBrowseEnquiryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        mObjBrowse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjBrowse.isStatus()) {
            mAlBrowseEnq=new ArrayList<>(mObjBrowse.getCategories().size());
            mAlBrowseEnq.addAll(mObjBrowse.getCategories());
            mBrowseAdapter= new BrowseEnquiryAdapter(mContext,mAlBrowseEnq,false);
            mRvBrowseEnq.setAdapter(mBrowseAdapter);
            mBrowseAdapter.setOnItemClickListenerNotifiy(this);

        } else {
            Toast.makeText(mContext, mObjBrowse.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back:
                onBackPressed();
                Notification.this.finish();
                break;


        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.BROWSE_ENQUIRY:
                    parseBrowseEnquiryResponse(serverResult);
                    break;

            }
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {

    }

    @Override
    public void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag) {

    }
}
