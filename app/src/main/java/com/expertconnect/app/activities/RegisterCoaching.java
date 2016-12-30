package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.LoginResponse;
import com.expertconnect.app.models.User;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterCoaching extends Activity implements View.OnClickListener, VolleyWebserviceCallBack {
    private ImageView mImgBack;
    private RangeBar mRgBar;
    private TextView mTvKm;
    private TextView mTvCoaching;
    private TextView mTvPref;
    private Button mBtJoin;
    private User mUserId;
    private Context mContext;
    private CheckBox mCbHome;
    private CheckBox mCbInstitute;
    private CheckBox mCbTravel;
    private CheckBox mCbOther;
    private CheckBox mCbOnline;
    private CheckBox mCbAgree;
    private LoginResponse mLoginResponse;
    private LinearLayout mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercoaching);
        mContext = RegisterCoaching.this;
        initViews();
        initListener();
        setFonts();
        AllConstants.isHome = false;
        AllConstants.isInstitute = false;
        AllConstants.isOnline = false;
        AllConstants.isOther = false;
        AllConstants.isTravel = false;
        AllConstants.isAgree = false;
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_DATA)) {
            mUserId = (User) getIntent().getSerializableExtra(Constants.USER_DATA);
        }
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

    private void initViews() {

        mImgBack = (ImageView) findViewById(R.id.back);
        mRgBar = (RangeBar) findViewById(R.id.rangebar);
        mTvKm = (TextView) findViewById(R.id.tv_km);
        mTvCoaching = (TextView) findViewById(R.id.tv_coaching);
        mTvPref = (TextView) findViewById(R.id.tv_studentpref);
        if (AllConstants.isStudentLoggedIn) {
            mTvPref.setText(R.string.studentcoach);
        } else {
            mTvPref.setText(R.string.teachercoach);
        }
        mBtJoin = (Button) findViewById(R.id.btnjoin);
        mCbHome = (CheckBox) findViewById(R.id.home);
        mCbInstitute = (CheckBox) findViewById(R.id.institute);
        mCbTravel = (CheckBox) findViewById(R.id.travel);
        mCbOther = (CheckBox) findViewById(R.id.other);
        mCbOnline = (CheckBox) findViewById(R.id.online);
        mCbAgree = (CheckBox) findViewById(R.id.agree);
        mScrollView = (LinearLayout) findViewById(R.id.coaching_main);

    }

    private void setFonts() {
        mTvKm.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvCoaching.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvPref.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtJoin.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbHome.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbInstitute.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbTravel.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbOther.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbOnline.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mCbAgree.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));


    }

    private void initListener() {

        mImgBack.setOnClickListener(this);
        mCbHome.setOnClickListener(this);
        mCbInstitute.setOnClickListener(this);
        mCbTravel.setOnClickListener(this);
        mCbOther.setOnClickListener(this);
        mCbOnline.setOnClickListener(this);
        mCbAgree.setOnClickListener(this);
        mBtJoin.setOnClickListener(this);
        mScrollView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof EditText))

            try {
                Utils.hideSoftKeyboard(RegisterCoaching.this);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        {

        }

        int id = v.getId();

        switch (id) {

            case R.id.back:
                onBackPressed();
                break;

            case R.id.btnjoin:
                if (isDetailFilled()) {
                    if (Utils.isConnected(this)) {
                        callRegisterCoachingAPI();
                    } else {
                        Toast.makeText(RegisterCoaching.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
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

            case R.id.agree:
                if (mCbAgree.isChecked())
                    AllConstants.isAgree = true;
                else
                    AllConstants.isAgree = false;
                break;

            default:
                break;
        }
    }

    private void callRegisterCoachingAPI() {
        String urlLogin = Constants.BASE_URL + Constants.USER_REGISTER;
        String device_Token = Utils.getSharedPrefString(Constants.GCM_ID, mContext);
        String[] mArr = new String[5];

        if (AllConstants.isHome) {
            mArr[0] = mCbHome.getText().toString();
        }
        if (AllConstants.isInstitute) {
            mArr[1] = mCbInstitute.getText().toString();
        }
        if (AllConstants.isTravel) {
            mArr[2] = mTvKm.getText().toString() + " Km";
        }
        if (AllConstants.isOther) {
            mArr[3] = "Other-Library,Community Centre etc.";
        }
        if (AllConstants.isOnline) {
            mArr[4] = mCbOnline.getText().toString();
        }

        mUserId.setCoaching_venue(mArr);


        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        JSONArray jsonArr3 = new JSONArray();

        try {
            jsonObject.put(Constants.USER_TYPE, mUserId.getUsertype());
            jsonObject.put(Constants.FIRST_NAME, mUserId.getFirstname());
            jsonObject.put(Constants.LAST_NAME, mUserId.getLastname());
            jsonObject.put(Constants.EMAIL_ID, mUserId.getEmail_id());
            jsonObject.put(Constants.PASSWORD, mUserId.getPassword());
            jsonObject.put(Constants.COUNTRY_COD, mUserId.getCountry_code());
            jsonObject.put(Constants.MOBILE_NO, mUserId.getMobile_no());
            jsonObject.put(Constants.DOB, mUserId.getDob());
            jsonObject.put(Constants.GENDER, mUserId.getGender());
            jsonObject.put(Constants.PROFILE_PIC, mUserId.getProfile_pic());
            jsonObject.put(Constants.DEVICE_TOKEN, device_Token);
            jsonObject.put(Constants.OS_TYPE, "android");
            jsonObject.put(Constants.LATITUDE, mUserId.getLatitude());
            jsonObject.put(Constants.LONGITUDE, mUserId.getLongitude());
            jsonObject.put(Constants.LOCATION, mUserId.getLocation());
            jsonObject.put(Constants.REG_TYPE, mUserId.getReg_type());
            jsonObject.put(Constants.SOCIAL_ID, mUserId.getSocial_id());
            jsonObject.put(Constants.CATEGORY_ID, mUserId.getCategory_id());
            jsonObject.put(Constants.SUB_CATEGORY_ID, mUserId.getSub_category_id());
            if (mUserId.getUsertype().equals("3")) {
                if (!(mUserId.getQualification().equals("NA"))) {
                    jsonObject.put(Constants.QUALIFICATION, mUserId.getQualification());
                }

                jsonObject.put(Constants.ABOUT, mUserId.getAbout());
                jsonObject.put(Constants.BASE_PRICE, mUserId.getBase_price());

                for (int i = 0; i < mUserId.getBeginnerArray().length; i++) {
                    jsonArr.put(mUserId.getBeginnerArray()[i]);
                }
                jsonObject.put(Constants.BEGINNER, jsonArr);

                if (AllConstants.isInter) {
                    JSONArray jsonArr1 = new JSONArray();
                    for (int i = 0; i < mUserId.getInterArray().length; i++) {
                        jsonArr1.put(mUserId.getInterArray()[i]);
                    }
                    jsonObject.put(Constants.INTER, jsonArr1);
                }

                if (AllConstants.isAdvance) {
                    JSONArray jsonArr2 = new JSONArray();
                    for (int i = 0; i < mUserId.getAdvanceArray().length; i++) {
                        jsonArr2.put(mUserId.getAdvanceArray()[i]);
                    }
                    jsonObject.put(Constants.ADVANCE, jsonArr2);
                }
            }


            for (int i = 0; i < mUserId.getCoaching_venue().length; i++) {
                if (mUserId.getCoaching_venue()[i] != null) {
                    jsonArr3.put(mUserId.getCoaching_venue()[i]);
                }

            }
            jsonObject.put(Constants.COACHING_VENUE, jsonArr3);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.USER_REGISTER);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isDetailFilled() {
        if (AllConstants.isHome || AllConstants.isInstitute || AllConstants.isTravel || AllConstants.isOther || AllConstants.isOnline) {
            if (AllConstants.isAgree) {
                return true;
            } else {
                Toast.makeText(RegisterCoaching.this, R.string.termserror, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(RegisterCoaching.this, R.string.atleastone, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.USER_REGISTER:
                    parseRegisterCoachingResponse(serverResult);
                    break;
            }
        }
    }

    private void setUserLoggedData() {

        Utils.setSharedPrefInt(Constants.USER_ID, mLoginResponse.getUser_id(), mContext);
        Utils.setSharedPrefString(Constants.USER_TYPE, mLoginResponse.getUsertype(), mContext);
        Utils.setSharedPrefString(Constants.FIRST_NAME, mLoginResponse.getFirstname(), mContext);
        Utils.setSharedPrefString(Constants.LAST_NAME, mLoginResponse.getLastname(), mContext);
        Utils.setSharedPrefString(Constants.LOCATION, mLoginResponse.getLocation(), mContext);
        Utils.setSharedPrefString(Constants.LATITUDE, mLoginResponse.getLatitude(), mContext);
        Utils.setSharedPrefString(Constants.LONGITUDE, mLoginResponse.getLongitude(), mContext);

        if (mLoginResponse.getReg_type().equals("2")) {
            AllConstants.isFacebook = true;
            Utils.setSharedPrefString(Constants.FACEBOOK_ID, mLoginResponse.getSocial_id(), mContext);
        } else {
            Utils.setSharedPrefString(Constants.PROFILE_PIC, mLoginResponse.getProfile_pic(), mContext);
        }

        Intent intent = new Intent(RegisterCoaching.this, Dashboard.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
        Utils.setSharedPrefBoolean(Constants.ISLOGGEDIN, true, mContext);
    }

    private void parseRegisterCoachingResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        mLoginResponse = gson.fromJson(serverResult.toString(), LoginResponse.class);
        if (mLoginResponse.isStatus()) {
            setUserLoggedData();
        } else {
            Toast.makeText(RegisterCoaching.this, R.string.something, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(RegisterCoaching.this, "Error", Toast.LENGTH_SHORT).show();
    }
}

