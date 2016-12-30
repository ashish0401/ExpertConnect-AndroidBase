package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expertconnect.app.R;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;

import org.json.JSONObject;

public class MyAccount extends Activity implements View.OnClickListener, VolleyWebserviceCallBack {
    private Button mBtnBank;
    private Button mBtnECredits;
    private Context mContext;
    private boolean isSent=false;
    private LinearLayout mLLBank;
    private LinearLayout mLLECredits;
    private TextView mTitle;
    private ImageView mBack;
    private TextView mTvTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mContext=this;
        initView();
        initListener();
        setFonts();

    }

    private void initView() {
        mBtnBank = (Button) findViewById(R.id.btn_bankdetails);
        mBtnECredits = (Button) findViewById(R.id.btn_ecredits);
        mLLBank = (LinearLayout) findViewById(R.id.ll_bank);
        mLLECredits = (LinearLayout) findViewById(R.id.ll_ecredits);
        mBack=(ImageView)findViewById(R.id.back);
        mTitle=(TextView)findViewById(R.id.title);
        mTvTotal=(TextView)findViewById(R.id.tv_toatalcredits);


    }


    private void initListener() {
        mBtnBank.setOnClickListener(this);
        mBtnECredits.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }

    private void setFonts() {
        mBtnBank.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnECredits.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvTotal.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back:
                onBackPressed();
                MyAccount.this.finish();
                break;

            case R.id.btn_bankdetails:
                shuffel("bank");
                break;

            case R.id.btn_ecredits:
                shuffel("ecredits");
                break;
        }
    }

    private void shuffel(String expert) {
        if(expert.equals("bank")){
            mBtnBank.setBackgroundResource(R.drawable.expert_selected);
            mBtnBank.setTextColor(getResources().getColor(R.color.white));
            mBtnECredits.setTextColor(getResources().getColor(R.color.black));
            mBtnECredits.setBackgroundResource(R.drawable.expert_unselected);
            isSent=false;
            mLLBank.setVisibility(View.VISIBLE);
            mLLECredits.setVisibility(View.GONE);

        }

        else {
            mBtnECredits.setBackgroundResource(R.drawable.expert_selected);
            mBtnBank.setBackgroundResource(R.drawable.expert_unselected);
            mBtnECredits.setTextColor(getResources().getColor(R.color.white));
            mBtnBank.setTextColor(getResources().getColor(R.color.black));
            isSent=true;
            mLLBank.setVisibility(View.GONE);
            mLLECredits.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {

    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {

    }
}
