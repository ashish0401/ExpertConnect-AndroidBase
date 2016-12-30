package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.Category;
import com.expertconnect.app.models.GenerateOTPStatus;
import com.expertconnect.app.models.MainCategoryResponse;
import com.expertconnect.app.models.SubCategory;
import com.expertconnect.app.models.SubCategoryResponse;
import com.expertconnect.app.models.User;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterExpertDetails extends Activity implements View.OnClickListener, VolleyWebserviceCallBack {
    private Button mbtnNext;
    private ImageView mImgBack;
    private RangeBar mRgBar;
    private TextView mTvExpertLevel;
    private TextView mTvExpertDetails;
    private TextView mTvExpertText;
    private TextView mTvBegineer;
    private TextView mTvInter;
    private TextView mTvAdvance;
    private Context mContext;
    private ArrayList<Category> mALmainCat;
    private ArrayList<String> mAlMainList;
    private ArrayList<String> mAlSubList;
    private Spinner mSpinMain;
    private Spinner mSpinSub;
    private ArrayList<SubCategory> mALSubCat;
    private TextView mTvYourCharge;
    private EditText mEtQualification;
    private EditText mEtAboutYou;
    private String maincat;
    private String subcat;
    private String about;
    private String yourCharge;
    private String qualification;
    private User mUserId;
    private TextView mTvLecReq;
    private TextView mTvLevel;
    private TextView mTvLectures;
    private TextView mTvTotalPrice;
    private TextView mTvBeginnerPrice;
    private TextView mTvInterPrice;
    private TextView mTvAdvancePrice;
    private EditText mEtBeginnerLec;
    private EditText mEtInterLec;
    private EditText mEtAdvanceLec;
    private EditText mEtYourCharge;
    private String baseCharge;
    private String beginLec;
    private String interLec;
    private String advanceLec;
    private int finalCharge;
    private int begLecCnt;
    private int intLecCnt;
    private int advLecCnt;
    private LinearLayout mExpertLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerexpert);
        mContext = RegisterExpertDetails.this;
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_DATA)) {
            mUserId = (User) getIntent().getSerializableExtra(Constants.USER_DATA);
        }
        initViews();
        initListener();
        setFonts();

        AllConstants.isInter = false;
        AllConstants.isAdvance = false;
        mEtInterLec.setFocusable(false);
        mEtInterLec.setFocusableInTouchMode(false);
        mEtInterLec.setClickable(false);
        mEtAdvanceLec.setFocusable(false);
        mEtAdvanceLec.setFocusableInTouchMode(false);
        mEtAdvanceLec.setClickable(false);

       callMainCategoryAPI();

        mRgBar.setSeekPinByValue(1);
        mRgBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                if (rightPinIndex == 0) {
                    AllConstants.isInter = false;
                    AllConstants.isAdvance = false;
                    mTvExpertLevel.setText(getResources().getString(R.string.beginner));
                    mTvInter.setTextColor(getResources().getColor(R.color.black));
                    mTvAdvance.setTextColor(getResources().getColor(R.color.black));
                    mTvInterPrice.setTextColor(getResources().getColor(R.color.black));
                    mTvAdvancePrice.setTextColor(getResources().getColor(R.color.black));
                    mEtInterLec.setBackground(getResources().getDrawable(R.drawable.border_black));
                    mEtAdvanceLec.setBackground(getResources().getDrawable(R.drawable.border_black));
                    mEtInterLec.setText("");
                    mEtAdvanceLec.setText("");
                    mTvInterPrice.setText(R.string.aus700);
                    mTvAdvancePrice.setText(R.string.aus700);
                    mEtInterLec.setFocusable(false);
                    mEtInterLec.setFocusableInTouchMode(false);
                    mEtInterLec.setClickable(false);
                    mEtAdvanceLec.setFocusable(false);
                    mEtAdvanceLec.setFocusableInTouchMode(false);
                    mEtAdvanceLec.setClickable(false);

                }
                if (rightPinIndex == 1) {
                    mTvExpertLevel.setText(getResources().getString(R.string.inter));
                    mTvInter.setTextColor(getResources().getColor(R.color.login_orange));
                    mTvAdvance.setTextColor(getResources().getColor(R.color.black));
                    mTvInterPrice.setTextColor(getResources().getColor(R.color.login_orange));
                    mTvAdvancePrice.setTextColor(getResources().getColor(R.color.black));
                    mTvAdvancePrice.setText(R.string.aus700);
                    AllConstants.isInter = true;
                    AllConstants.isAdvance = false;
                    mEtAdvanceLec.setText("");
                    mEtInterLec.setBackground(getResources().getDrawable(R.drawable.border_bg));
                    mEtAdvanceLec.setBackground(getResources().getDrawable(R.drawable.border_black));
                    mEtInterLec.setFocusable(true);
                    mEtInterLec.setFocusableInTouchMode(true);
                    mEtInterLec.setClickable(true);
                    mEtAdvanceLec.setFocusable(false);
                    mEtAdvanceLec.setFocusableInTouchMode(false);
                    mEtAdvanceLec.setClickable(false);
                }
                if (rightPinIndex == 2) {
                    mTvExpertLevel.setText(getResources().getString(R.string.advance));
                    mTvInter.setTextColor(getResources().getColor(R.color.login_orange));
                    mTvAdvance.setTextColor(getResources().getColor(R.color.login_orange));
                    mTvInterPrice.setTextColor(getResources().getColor(R.color.login_orange));
                    mTvAdvancePrice.setTextColor(getResources().getColor(R.color.login_orange));
                    AllConstants.isInter = true;
                    AllConstants.isAdvance = true;
                    mEtInterLec.setBackground(getResources().getDrawable(R.drawable.border_bg));
                    mEtAdvanceLec.setBackground(getResources().getDrawable(R.drawable.border_bg));
                    mEtInterLec.setFocusable(true);
                    mEtInterLec.setFocusableInTouchMode(true);
                    mEtInterLec.setClickable(true);
                    mEtAdvanceLec.setFocusable(true);
                    mEtAdvanceLec.setFocusableInTouchMode(true);
                    mEtAdvanceLec.setClickable(true);

                }
            }
        });


        mEtYourCharge.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                baseCharge = mEtYourCharge.getText().toString().trim();
                if (baseCharge.length() > 0) {
                    finalCharge = Integer.parseInt(baseCharge);
                    if(finalCharge==0){
                        mTvBeginnerPrice.setText(R.string.aus700);
                        mTvInterPrice.setText(R.string.aus700);
                        mTvAdvancePrice.setText(R.string.aus700);
                    }

                    else{
                        beginLec = mEtBeginnerLec.getText().toString().trim();
                        if (beginLec.length() > 0) {
                            begLecCnt = Integer.parseInt(beginLec);
                            int finalCnt = begLecCnt * finalCharge;
                            mTvBeginnerPrice.setText("AU$ " + String.valueOf(finalCnt));

                        }


                        if (AllConstants.isInter) {
                            interLec = mEtInterLec.getText().toString().trim();
                            if (interLec.length() > 0) {
                                intLecCnt = Integer.parseInt(interLec);
                                int finalCnt = intLecCnt * finalCharge;
                                mTvInterPrice.setText("AU$ " + String.valueOf(finalCnt));

                            }

                        }

                        if (AllConstants.isAdvance) {
                            advanceLec = mEtAdvanceLec.getText().toString().trim();
                            if (advanceLec.length() > 0) {
                                advLecCnt = Integer.parseInt(advanceLec);
                                int finalCnt = advLecCnt * finalCharge;
                                mTvAdvancePrice.setText("AU$ " + String.valueOf(finalCnt));
                            }

                        }
                    }

                }

                else {
                    mTvBeginnerPrice.setText(R.string.aus700);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        mEtBeginnerLec.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                beginLec = mEtBeginnerLec.getText().toString().trim();
                if (beginLec.length() > 0) {
                    begLecCnt = Integer.parseInt(beginLec);

                    if(begLecCnt==0){
                        mTvBeginnerPrice.setText(R.string.aus700);
                    }
                    else {
                        if(baseCharge!=null){
                            if (baseCharge.length() > 0) {
                                int finalCnt = begLecCnt * finalCharge;
                                mTvBeginnerPrice.setText("AU$ " + String.valueOf(finalCnt));
                            }
                        }
                    }



                }

                else {
                    mTvBeginnerPrice.setText(R.string.aus700);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        mEtInterLec.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                interLec = mEtInterLec.getText().toString().trim();
                if (interLec.length() > 0) {
                    intLecCnt = Integer.parseInt(interLec);
                    if(intLecCnt==0){
                        mTvInterPrice.setText(R.string.aus700);
                    }

                    else {
                        if(baseCharge!=null){
                            if (baseCharge.length() > 0) {
                                int finalCnt = intLecCnt * finalCharge;
                                mTvInterPrice.setText("AU$ " + String.valueOf(finalCnt));
                            }
                        }
                    }

                }

                else {
                    mTvInterPrice.setText(R.string.aus700);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        mEtAdvanceLec.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                advanceLec = mEtAdvanceLec.getText().toString().trim();
                if (advanceLec.length() > 0) {
                    advLecCnt = Integer.parseInt(advanceLec);
                    if(intLecCnt==0){
                        mTvAdvancePrice.setText(R.string.aus700);
                    }

                    else {
                        if(baseCharge!=null){
                            if (baseCharge.length() > 0) {
                                int finalCnt = advLecCnt * finalCharge;
                                mTvAdvancePrice.setText("AU$ " + String.valueOf(finalCnt));
                            }
                        }
                    }
                    }

                else {
                    mTvAdvancePrice.setText(R.string.aus700);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });


        mSpinMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int position, long arg3) {
                if (position != 0) {
                    position = position - 1;
                }
                String id = mALmainCat.get(position).getCategoryId();
                callSubCategoryAPI(id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void initViews() {
        mbtnNext = (Button) findViewById(R.id.btnnext);
        mImgBack = (ImageView) findViewById(R.id.back);
        mRgBar = (RangeBar) findViewById(R.id.rangebar);
        mTvExpertLevel = (TextView) findViewById(R.id.tv_expertlevel);
        mTvYourCharge = (TextView) findViewById(R.id.tv_yourcharge);
        mTvExpertDetails = (TextView) findViewById(R.id.tv_expertdetails);
        mTvLecReq = (TextView) findViewById(R.id.tv_lecreq);
        mTvLevel = (TextView) findViewById(R.id.tv_level);
        mTvLectures = (TextView) findViewById(R.id.tv_lectures);
        mTvTotalPrice = (TextView) findViewById(R.id.tv_totalprice);
        mTvExpertText = (TextView) findViewById(R.id.tv_experttext);
        mTvBegineer = (TextView) findViewById(R.id.tv_beginner);
        mTvBeginnerPrice = (TextView) findViewById(R.id.tv_beginner_price);
        mTvInterPrice = (TextView) findViewById(R.id.tv_intermediate_price);
        mTvAdvancePrice = (TextView) findViewById(R.id.tv_advance_price);
        mTvInter = (TextView) findViewById(R.id.tv_intermediate);
        mTvAdvance = (TextView) findViewById(R.id.tv_advance);
        mSpinMain = (Spinner) findViewById(R.id.spin_main);
        mSpinSub = (Spinner) findViewById(R.id.spin_sub);
        mEtQualification = (EditText) findViewById(R.id.et_qualification);
        mEtAboutYou = (EditText) findViewById(R.id.et_aboutyou);
        mEtBeginnerLec = (EditText) findViewById(R.id.et_beginnerlec);
        mEtInterLec = (EditText) findViewById(R.id.et_interlec);
        mEtAdvanceLec = (EditText) findViewById(R.id.et_advancelec);
        mEtYourCharge = (EditText) findViewById(R.id.et_charge);
        mExpertLayoutMain=(LinearLayout)findViewById(R.id.expert_layout);
    }

    private void setFonts() {
        mbtnNext.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvExpertLevel.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvExpertDetails.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvExpertText.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvBegineer.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvInter.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvAdvance.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvYourCharge.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvLecReq.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvLevel.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvLectures.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvTotalPrice.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvBeginnerPrice.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvInterPrice.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvAdvancePrice.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

    }

    private void initListener() {
        mbtnNext.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mExpertLayoutMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(! (v instanceof EditText))
        {
            try {
                Utils.hideSoftKeyboard(RegisterExpertDetails.this);
            }

            catch (Exception e){
                Log.e("Error",e.toString());
            }
        }
        int id = v.getId();

        switch (id) {

            case R.id.btnnext:
                if (isDetailsFilled()) {
                    if (Utils.isConnected(mContext)) {
                        setUserData();
                    } else {
                        Toast.makeText(RegisterExpertDetails.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.back:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    private void setUserData() {

        String[] mArr = new String[3];
        String[] mArr1 = new String[3];
        String[] mArr2 = new String[3];

        for (int i = 0; i < mALmainCat.size(); i++) {
            if (mALmainCat.get(i).getCategoryName().equals(maincat)) {
                mUserId.setCategory_id(mALmainCat.get(i).getCategoryId());
            }
        }

        for (int i = 0; i < mALSubCat.size(); i++) {
            if (mALSubCat.get(i).getSub_category_name().equals(subcat)) {
                mUserId.setSub_category_id(mALSubCat.get(i).getSub_category_id());
            }
        }

        if (qualification.length() > 0) {
            mUserId.setQualification(qualification);
        } else {
            mUserId.setQualification("NA");
        }
        mUserId.setBase_price(yourCharge);
        mArr[0] = "Beginner";
        mArr[1] = mTvBeginnerPrice.getText().toString().trim().substring(4);
        mArr[2] = mEtBeginnerLec.getText().toString().trim();
        mUserId.setBeginnerArray(mArr);

        if (AllConstants.isInter) {
            mArr1[0] = "Intermediate";
            mArr1[1] = mTvInterPrice.getText().toString().trim().substring(4);
            mArr1[2] = mEtInterLec.getText().toString().trim();
            mUserId.setInterArray(mArr1);
        }

        if (AllConstants.isAdvance) {
            mArr2[0] = "Advance";
            mArr2[1] = mTvAdvancePrice.getText().toString().trim().substring(4);
            mArr2[2] = mEtAdvanceLec.getText().toString().trim();
            mUserId.setAdvanceArray(mArr2);
        }

        mUserId.setAbout(about);
        Intent intent = new Intent(RegisterExpertDetails.this, RegisterECredits.class);
        intent.putExtra(Constants.USER_DATA, mUserId);
        startActivity(intent);


    }


    private boolean isDetailsFilled() {

        maincat = mSpinMain.getSelectedItem().toString();
        subcat = mSpinSub.getSelectedItem().toString();
        about = mEtAboutYou.getText().toString().trim();
        qualification = mEtQualification.getText().toString().trim();
        yourCharge = mEtYourCharge.getText().toString().trim();
        String beginPrice = mEtBeginnerLec.getText().toString().trim();
        String interPrice = mEtInterLec.getText().toString().trim();
        String advancePrice = mEtAdvanceLec.getText().toString().trim();


        if (maincat.equals("Select Main Category")) {
            Toast.makeText(RegisterExpertDetails.this, R.string.selectmain, Toast.LENGTH_SHORT).show();
            return false;
        } else if (subcat.equals("Select Sub Category")) {
            Toast.makeText(RegisterExpertDetails.this, R.string.selectsub, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(yourCharge.length() > 0)) {

            Toast.makeText(RegisterExpertDetails.this, R.string.yourCharge, Toast.LENGTH_SHORT).show();
            return false;
        }else if ((yourCharge.equals("0")) || (yourCharge.equals("00"))|| (yourCharge.equals("000"))||(yourCharge.equals("0000"))) {

            Toast.makeText(RegisterExpertDetails.this, R.string.yourCharge0, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(beginPrice.length() > 0)) {

            Toast.makeText(RegisterExpertDetails.this, R.string.begin, Toast.LENGTH_SHORT).show();
            return false;
        }else if ((beginPrice.equals("0")) || (beginPrice.equals("00"))|| (beginPrice.equals("000"))) {

            Toast.makeText(RegisterExpertDetails.this, R.string.begin0, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(interPrice.length() > 0) && AllConstants.isInter) {

            Toast.makeText(RegisterExpertDetails.this, R.string.interr, Toast.LENGTH_SHORT).show();
            return false;
        }else if ((interPrice.equals("0")) || (interPrice.equals("00"))|| (interPrice.equals("000"))) {

            Toast.makeText(RegisterExpertDetails.this, R.string.inter0, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(advancePrice.length() > 0) && AllConstants.isAdvance) {

            Toast.makeText(RegisterExpertDetails.this, R.string.advanced, Toast.LENGTH_SHORT).show();
            return false;
        }else if ((advancePrice.equals("0")) || (advancePrice.equals("00"))|| (advancePrice.equals("000"))) {

            Toast.makeText(RegisterExpertDetails.this, R.string.advance0, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(about.length() > 0)) {

            Toast.makeText(RegisterExpertDetails.this, R.string.about, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void callMainCategoryAPI() {
        String urlLogin = Constants.BASE_URL + Constants.GET_CATEGORY;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.GET_CATEGORY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.GET_CATEGORY:
                    parseMainCategoryResponse(serverResult);
                    break;

                case Constants.GET_SUB_CATEGORY:
                    parseSubCategoryResponse(serverResult);
                    break;

                case Constants.REGISTER_EXPERT_DETAIS:
                    parseExpertDetailsResponse(serverResult);
                    break;


            }
        }
    }

    private void parseExpertDetailsResponse(JSONObject serverResult) {

        Gson gson = new Gson();
        GenerateOTPStatus mRegisterExpert = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mRegisterExpert.isStatus()) {
            Intent intent = new Intent(RegisterExpertDetails.this, RegisterECredits.class);
            intent.putExtra(Constants.USER_ID, mUserId);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterExpertDetails.this, R.string.error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(RegisterExpertDetails.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    private void parseMainCategoryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        MainCategoryResponse mMainCat = gson.fromJson(serverResult.toString(), MainCategoryResponse.class);
        if (mMainCat.getStatus()) {
            mALmainCat = new ArrayList<>(mMainCat.getCategories().size());
            mALmainCat.addAll(mMainCat.getCategories());
            mAlMainList = new ArrayList<>();
            mAlMainList.add("Select Main Category");
            for (Category cat : mALmainCat) {
                mAlMainList.add(cat.getCategoryName());
            }

            mSpinMain.setAdapter(new ArrayAdapter<String>(RegisterExpertDetails.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlMainList));

        } else {
            mSpinMain.setAdapter(new ArrayAdapter<String>(RegisterExpertDetails.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlMainList));
        }
    }

    private void callSubCategoryAPI(String subId) {
        mAlSubList = new ArrayList<>();

        String urlLogin = Constants.BASE_URL + Constants.GET_SUB_CATEGORY;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.CATEGORY_ID, subId);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.GET_SUB_CATEGORY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSubCategoryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        SubCategoryResponse mSubCat = gson.fromJson(serverResult.toString(), SubCategoryResponse.class);
        if (mSubCat.getStatus()) {
            mALSubCat = new ArrayList<>(mSubCat.getSub_categories().size());
            mALSubCat.addAll(mSubCat.getSub_categories());
            mAlSubList = new ArrayList<>();
            mAlSubList.add("Select Sub Category");
            for (SubCategory cat : mALSubCat) {
                mAlSubList.add(cat.getSub_category_name());
            }
            mSpinSub.setAdapter(new ArrayAdapter<String>(RegisterExpertDetails.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlSubList));
        } else {
            mAlSubList.add("Select Sub Category");
            mSpinSub.setAdapter(new ArrayAdapter<String>(RegisterExpertDetails.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlSubList));
        }
    }
}
