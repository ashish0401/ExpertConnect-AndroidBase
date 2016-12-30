package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.models.TeacherListResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterTeacherList extends Activity implements View.OnClickListener,VolleyWebserviceCallBack {

    private ImageView mImgBack;
    private TextView mTvTitle;
    private Context mContext;
    private CheckBox mCbHome;
    private CheckBox mCbInstitute;
    private CheckBox mCbTravel;
    private CheckBox mCbOther;
    private CheckBox mCbOnline;
    private RangeBar mRgBar;
    private TextView mTvKm;
    private Button mBtApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_teacher_list);
        mContext = FilterTeacherList.this;
        initView();
        initListener();
        setFonts();
        AllConstants.isHome=false;
        AllConstants.isInstitute=false;
        AllConstants.isOnline=false;
        AllConstants.isOther=false;
        AllConstants.isTravel=false;

        mRgBar.setSeekPinByValue(1);
        mRgBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                if (rightPinIndex == 0) {
                    mTvKm.setText("10");
                }
                if (rightPinIndex == 1) {
                    mTvKm.setText("20");
                }
                if (rightPinIndex == 2) {
                    mTvKm.setText("30");
                }
                if (rightPinIndex == 3) {
                    mTvKm.setText("40");
                }
                if (rightPinIndex == 4) {
                    mTvKm.setText("50");
                }
            }
        });
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvKm.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbHome.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbInstitute.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbTravel.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbOther.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbOnline.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtApply.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

    }


    private void initView() {
        mRgBar = (RangeBar) findViewById(R.id.rangebar);
        mImgBack = (ImageView) findViewById(R.id.back);
        mTvTitle = (TextView) findViewById(R.id.title);
        mTvKm = (TextView) findViewById(R.id.tv_km);
        mCbHome = (CheckBox) findViewById(R.id.home);
        mCbInstitute = (CheckBox) findViewById(R.id.institute);
        mCbTravel = (CheckBox) findViewById(R.id.travel);
        mCbOther = (CheckBox) findViewById(R.id.other);
        mCbOnline = (CheckBox) findViewById(R.id.online);
        mBtApply = (Button) findViewById(R.id.apply);
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mCbHome.setOnClickListener(this);
        mCbInstitute.setOnClickListener(this);
        mCbTravel.setOnClickListener(this);
        mCbOther.setOnClickListener(this);
        mCbOnline.setOnClickListener(this);
        mBtApply.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.back:
                onBackPressed();
                break;

            case R.id.apply:
                if (isDetailFilled()) {
                    if (Utils.isConnected(this)) {
                        callTeacherListWithFilterAPI();
                    } else {
                        Toast.makeText(FilterTeacherList.this,R.string.nointernet, Toast.LENGTH_SHORT).show();
                    }
                }
                    break;

            case R.id.home:
                if (mCbHome.isChecked())
                    AllConstants.isHome = true;
                else
                    AllConstants.isHome = false;
                break;

            case R.id.institute:
                if (mCbInstitute.isChecked())
                    AllConstants.isInstitute = true;
                else
                    AllConstants.isInstitute = false;
                break;

            case R.id.travel:
                if (mCbTravel.isChecked())
                    AllConstants.isTravel = true;
                else
                    AllConstants.isTravel = false;
                break;

            case R.id.other:
                if (mCbOther.isChecked())
                    AllConstants.isOther = true;
                else
                    AllConstants.isOther = false;
                break;

            case R.id.online:
                if (mCbOnline.isChecked())
                    AllConstants.isOnline = true;
                else
                    AllConstants.isOnline = false;
                break;
        }
    }

    private void callTeacherListWithFilterAPI() {
        String urlLogin = Constants.BASE_URL + Constants.TEACHER_LIST_FILTER;
        String user_id = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID,mContext));
        String main = Utils.getSharedPrefString(Constants.MAIN_SEND, mContext);
        String sub = Utils.getSharedPrefString(Constants.SUB_SEND, mContext);
        String level = Utils.getSharedPrefString(Constants.LEVEL_SEND, mContext);
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        if (AllConstants.isHome) {
            jsonArr.put(mCbHome.getText().toString());
        }
        if (AllConstants.isInstitute) {
            jsonArr.put(mCbInstitute.getText().toString());
        }
        if (AllConstants.isTravel) {
            jsonArr.put(mTvKm.getText().toString() +" Km");
        }
        if (AllConstants.isOther) {
            jsonArr.put("Other-Library Community Centre");
        }
        if (AllConstants.isOnline) {
            jsonArr.put(mCbOnline.getText().toString());
        }
        try {
            jsonObject.put(Constants.USER_ID, user_id);
            jsonObject.put(Constants.CATEGORY_ID, main);
            jsonObject.put(Constants.SUB_CATEGORY_ID,sub);
            jsonObject.put(Constants.LEVEL,level);
            jsonObject.put(Constants.VENUE,jsonArr);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.TEACHER_LIST_FILTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isDetailFilled() {
        if (AllConstants.isHome || AllConstants.isInstitute || AllConstants.isTravel || AllConstants.isOther || AllConstants.isOnline) {
           return true;
        } else {
            Toast.makeText(FilterTeacherList.this,R.string.atleastone, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.TEACHER_LIST_FILTER:
                    parseTeacherFilterList(serverResult);
                    break;
            }
        }
    }

    private void parseTeacherFilterList(JSONObject serverResult) {
        Gson gson = new Gson();
        TeacherListResponse mObjTeacherResponse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjTeacherResponse.isStatus()) {
            ArrayList<TeacherCategory> mALTacherList=new ArrayList<>(mObjTeacherResponse.getCategories().size());
            mALTacherList.addAll(mObjTeacherResponse.getCategories());
            TeacherList.mStatObj.finish();
            Intent intent=new Intent(FilterTeacherList.this,TeacherList.class);
            intent.putExtra(Constants.TEACHER_LIST, mALTacherList);
            startActivity(intent);
            FilterTeacherList.this.finish();
        } else {
            Toast.makeText(FilterTeacherList.this, mObjTeacherResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(FilterTeacherList.this, R.string.error, Toast.LENGTH_SHORT).show();
    }
}
