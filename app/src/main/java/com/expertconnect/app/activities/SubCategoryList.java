package com.expertconnect.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.expertconnect.app.R;
import com.expertconnect.app.adapters.SubCategoryAdapter;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.interfaces.onSubCatClick;
import com.expertconnect.app.models.CoachingDetails;
import com.expertconnect.app.models.SubCategory;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.models.TeacherListResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategoryList extends Activity implements onSubCatClick, View.OnClickListener, VolleyWebserviceCallBack {
    private RecyclerView mRvSubCat;
    private Context mContext;
    private ImageView mImgBack;
    private TextView mTvTitle;
    private String finalmainId;
    private String finalsubId;
    private TextView tvExpertLevel;
    private String mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        mContext = SubCategoryList.this;
        initView();
        initListener();
        setFonts();
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SUB_CATLIST)) {
            ArrayList<SubCategory> mAlSubCat = new ArrayList<>();
            mAlSubCat = (ArrayList<SubCategory>) intent.getSerializableExtra(Constants.SUB_CATLIST);
            SubCategoryAdapter mSubAdapter = new SubCategoryAdapter(mContext, mAlSubCat);
            mRvSubCat.setAdapter(mSubAdapter);
            mSubAdapter.setOnItemClickListener(this);
        }

        if(intent.hasExtra(Constants.MAIN_CAT_NAME)){
            mTitle= intent.getExtras().getString(Constants.MAIN_CAT_NAME);
        }
        mTvTitle.setText(mTitle);
    }

    private void initView() {
        mRvSubCat = (RecyclerView) findViewById(R.id.rv_subcategory);
        mRvSubCat.setLayoutManager(new LinearLayoutManager(mContext));
        mImgBack = (ImageView) findViewById(R.id.img_back);
        mTvTitle = (TextView) findViewById(R.id.title);
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.img_back:
               Intent intent=new Intent(SubCategoryList.this,Dashboard.class);
                startActivity(intent);
                SubCategoryList.this.finish();
                break;


            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(View v, int position, ArrayList<SubCategory> browseResult) {
        finalmainId = Utils.getSharedPrefString(Constants.MAIN_CAT, mContext);
        finalsubId = browseResult.get(position).getSub_category_id();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_expert_dialog, null);
        dialogBuilder.setView(dialogView);
        RangeBar mRgBar = (RangeBar) dialogView.findViewById(R.id.rangebar_expert);
        TextView tvExpertText = (TextView) dialogView.findViewById(R.id.tv_experttext);
        tvExpertLevel = (TextView) dialogView.findViewById(R.id.tv_expertlevel);
        Button mBtnSubmit = (Button) dialogView.findViewById(R.id.btn_expert_submit);
        ImageView mImgCancel = (ImageView) dialogView.findViewById(R.id.imgcancel);
        tvExpertText.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        tvExpertLevel.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnSubmit.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        mRgBar.setSeekPinByValue(1);
        mRgBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                if (rightPinIndex == 0) {

                    tvExpertLevel.setText(getResources().getString(R.string.beginner));
                }
                if (rightPinIndex == 1) {
                    tvExpertLevel.setText(getResources().getString(R.string.inter));

                }
                if (rightPinIndex == 2) {
                    tvExpertLevel.setText(getResources().getString(R.string.advance));


                }
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(mContext)) {
                    callTeacherListApi();
                } else {
                    Toast.makeText(SubCategoryList.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                }

                ;
                alertDialog.dismiss();
            }
        });

        mImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void callTeacherListApi() {

        String urlLogin = Constants.BASE_URL + Constants.TEACHER_LIST_NOFILTER;
        String user_id=String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID,mContext));
        String level = tvExpertLevel.getText().toString().trim();
        Utils.setSharedPrefString(Constants.MAIN_SEND,finalmainId,mContext);
        Utils.setSharedPrefString(Constants.SUB_SEND,finalsubId,mContext);
        Utils.setSharedPrefString(Constants.LEVEL_SEND, level,mContext);
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.USER_ID, user_id);
            jsonObject.put(Constants.CATEGORY_ID, finalmainId);
            jsonObject.put(Constants.SUB_CATEGORY_ID, finalsubId);
            jsonObject.put(Constants.LEVEL, level);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.TEACHER_LIST_NOFILTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.TEACHER_LIST_NOFILTER:
                    parseTeacherResponse(serverResult);
                    break;
            }
        }

    }

    private void parseTeacherResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        TeacherListResponse mObjTeacherResponse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjTeacherResponse.isStatus()) {
            ArrayList<TeacherCategory> mALTacherList=new ArrayList<>(mObjTeacherResponse.getCategories().size());
            mALTacherList.addAll(mObjTeacherResponse.getCategories());
           Intent intent=new Intent(SubCategoryList.this,TeacherList.class);
            intent.putExtra(Constants.TEACHER_LIST, mALTacherList);
            startActivity(intent);

        } else {
            Toast.makeText(SubCategoryList.this, mObjTeacherResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(SubCategoryList.this, R.string.error, Toast.LENGTH_SHORT).show();
    }
}
