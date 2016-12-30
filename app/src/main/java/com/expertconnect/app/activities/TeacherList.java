package com.expertconnect.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.adapters.BrowseExpertAdapter;
import com.expertconnect.app.adapters.SubCategoryAdapter;
import com.expertconnect.app.adapters.TeacherListAdapter;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.interfaces.OnOfferButtonClick;
import com.expertconnect.app.models.BrowseEnquiryResult;
import com.expertconnect.app.models.CoachingDetails;
import com.expertconnect.app.models.GenerateOTPStatus;
import com.expertconnect.app.models.LoginResponse;
import com.expertconnect.app.models.SubCategory;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherList extends Activity implements View.OnClickListener,OnOfferButtonClick,VolleyWebserviceCallBack {
    private ImageView mImgBack;
    private RecyclerView mRvBrowseExp;
    private Context mContext;
    private ImageView mImgFilter;
    private TextView mTvTitle;
    private ArrayList<TeacherCategory> mAlTeacherList;
    private View view;
    int selectPosition;
    public static TeacherList mStatObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mContext=this;
        mStatObj=this;
        initView();
        initListener();
        setFonts();
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.TEACHER_LIST)) {

            mAlTeacherList=new ArrayList<>();
            mAlTeacherList= (ArrayList<TeacherCategory>) intent.getSerializableExtra(Constants.TEACHER_LIST);
            TeacherListAdapter mBrowseAdapter = new TeacherListAdapter(mContext, mAlTeacherList);
            mRvBrowseExp.setAdapter(mBrowseAdapter);
            mBrowseAdapter.setOnItemClickListener(this);
        }
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }


    private void initView() {
        mRvBrowseExp=(RecyclerView)findViewById(R.id.rv_browse_expert);
        mRvBrowseExp.setLayoutManager(new LinearLayoutManager(mContext));
        mImgFilter=(ImageView)findViewById(R.id.img_filter);
        mImgBack = (ImageView) findViewById(R.id.back);
        mTvTitle=(TextView)findViewById(R.id.title);
    }

    private void initListener() {
        mImgFilter.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_filter:
                Intent mIntent=new Intent(TeacherList.this,FilterTeacherList.class);
                startActivity(mIntent);
                break;

            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag) {

        if(mAlTeacherList.get(position).isTag()){
            Toast.makeText(TeacherList.this,R.string.already,Toast.LENGTH_SHORT).show();
        }
        else {
            String toId= String.valueOf(browseResult.get(position).getTeacher_id());
            String fromId=String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID,mContext));
            view=v;
            selectPosition=position;

            if (Utils.isConnected(this)) {
                callNotificationRequestAPI(toId,fromId,browseResult.get(position).getExpert_id());
            } else {
                Toast.makeText(TeacherList.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callNotificationRequestAPI(String toId, String fromId, String expert_id) {
        String urlLogin = Constants.BASE_URL + Constants.NOTIFICATION_REQUEST;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FROM_ID,fromId);
            jsonObject.put(Constants.TO_ID, toId);
            jsonObject.put(Constants.EXPERT_ID, expert_id);
            jsonObject.put(Constants.NOTIFICATION_TYPE,"request");
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.NOTIFICATION_REQUEST);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.NOTIFICATION_REQUEST:
                    parseNotificationResponse(serverResult);
                    break;
            }
        }
    }

    private void parseNotificationResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        GenerateOTPStatus mObjRespose = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mObjRespose.isStatus()) {
            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = this.getLayoutInflater();
            View dialogView1 = inflater1.inflate(R.layout.custom_request, null);
            dialogBuilder1.setView(dialogView1);
            ImageView mImgCancel1 = (ImageView) dialogView1.findViewById(R.id.imgcancel);
            TextView mTvForgorEmail = (TextView) dialogView1.findViewById(R.id.tv_requestsucess);
            mTvForgorEmail.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            final AlertDialog alertDialog1 = dialogBuilder1.create();
            alertDialog1.show();
            mImgCancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });
            mAlTeacherList.get(selectPosition).setTag(true);
            view.findViewById(R.id.btn_sendoffer).setBackgroundColor(getResources().getColor(R.color.request_gray));
        } else {
            Toast.makeText(TeacherList.this, mObjRespose.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(TeacherList.this, R.string.error, Toast.LENGTH_SHORT).show();
    }
}
