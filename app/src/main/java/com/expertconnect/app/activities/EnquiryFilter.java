package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.Category;
import com.expertconnect.app.models.MainCategoryResponse;
import com.expertconnect.app.models.SubCategory;
import com.expertconnect.app.models.SubCategoryResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class EnquiryFilter extends Activity implements View.OnClickListener, VolleyWebserviceCallBack {
    private ArrayList<Category> mALmainCat;
    private ArrayList<String> mAlMainList;
    private ArrayList<String> mAlSubList;
    private Spinner mSpinMain;
    private Spinner mSpinSub;
    private ArrayList<SubCategory> mALSubCat;
    private Context mContext;
    private ImageView mImgBack;
    private TextView mTvTitle;
    private Button mBtnSend;
    private String maincat;
    private String subcat;
    private String amount;
    private EditText mEtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_filter);
        mContext = EnquiryFilter.this;
        initViews();
        initListener();
        setFonts();

        if (Utils.isConnected(this)) {
            callMainCategoryAPI();
        } else {
            Toast.makeText(EnquiryFilter.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }

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
        mSpinMain = (Spinner) findViewById(R.id.spin_main);
        mSpinSub = (Spinner) findViewById(R.id.spin_sub);
        mImgBack = (ImageView) findViewById(R.id.back);
        mTvTitle = (TextView) findViewById(R.id.title);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtAmount = (EditText) findViewById(R.id.et_rate);
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.back:
                onBackPressed();
                break;

            case R.id.btn_send:
                if (isValidated()) {
                    if (Utils.isConnected(mContext)) {
                        String mainID = null;
                        String subID = null;
                        for (int i = 0; i < mALmainCat.size(); i++) {
                            if (mALmainCat.get(i).getCategoryName().equals(maincat)) {
                                mainID = mALmainCat.get(i).getCategoryId();
                            }
                        }

                        for (int i = 0; i < mALSubCat.size(); i++) {
                            if (mALSubCat.get(i).getSub_category_name().equals(subcat)) {
                                subID = mALSubCat.get(i).getSub_category_id();
                            }
                        }

                        Utils.setSharedPrefString(Constants.FILTER_MAIN, mainID, mContext);
                        Utils.setSharedPrefString(Constants.FILTER_SUB, subID, mContext);
                        Utils.setSharedPrefString(Constants.AMOUNT, amount, mContext);
                        Intent intent = new Intent(EnquiryFilter.this, Dashboard.class);
                        intent.putExtra(Constants.BROWSE_ENQUIRY, true);
                        startActivity(intent);
                        EnquiryFilter.this.finish();

                    } else {
                        Toast.makeText(EnquiryFilter.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            default:
                break;
        }
    }

    private boolean isValidated() {

        maincat = mSpinMain.getSelectedItem().toString();
        subcat = mSpinSub.getSelectedItem().toString();
        amount = mEtAmount.getText().toString().trim();
        if (maincat.equals("Select Main Category")) {
            Toast.makeText(EnquiryFilter.this, R.string.selectmain, Toast.LENGTH_SHORT).show();
            return false;
        } else if (subcat.equals("Select Sub Category")) {
            Toast.makeText(EnquiryFilter.this, R.string.selectsub, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(amount.length() > 0)) {
            Toast.makeText(EnquiryFilter.this, R.string.rate, Toast.LENGTH_SHORT).show();
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

            mSpinMain.setAdapter(new ArrayAdapter<String>(EnquiryFilter.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlMainList));

        } else {
            mSpinMain.setAdapter(new ArrayAdapter<String>(EnquiryFilter.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlMainList));
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
            mSpinSub.setAdapter(new ArrayAdapter<String>(EnquiryFilter.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlSubList));
        } else {
            mAlSubList.add("Select Sub Category");
            mSpinSub.setAdapter(new ArrayAdapter<String>(EnquiryFilter.this,
                    android.R.layout.simple_spinner_dropdown_item, mAlSubList));
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
            }
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(EnquiryFilter.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

}
