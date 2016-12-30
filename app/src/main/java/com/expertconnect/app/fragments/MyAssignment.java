package com.expertconnect.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;

import com.expertconnect.app.adapters.BrowseEnquiryAdapter;
import com.expertconnect.app.adapters.MyAssignmentAdapter;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.interfaces.OnOfferButtonClick;
import com.expertconnect.app.models.GenerateOTPStatus;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.models.TeacherListResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyAssignment extends Fragment implements View.OnClickListener,VolleyWebserviceCallBack,OnOfferButtonClick {

    private View view;
    private RecyclerView mRvBrowseExp;
    private Context mContext;
    private ArrayList<TeacherCategory> mAlMyAssignment;
    private TextView mTvTitle;
    private String tagVal;
    int selectPosition;
    private MyAssignmentAdapter mBrowseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_assignment, container, false);
        }
        mContext = getActivity();

        initView(view);
        initListener();
        setFonts();

        if (Utils.isConnected(mContext)) {
            callMyAssignmentAPI();
        } else {
            Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void callMyAssignmentAPI() {
        String user_id = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
        String urlLogin = Constants.BASE_URL + Constants.MY_ASSIGNMENT;

        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.USER_ID, user_id);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.MY_ASSIGNMENT);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFonts() {

        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }

    private void initView(View view) {
        mRvBrowseExp = (RecyclerView) view.findViewById(R.id.rv_browse_expert);
        mTvTitle = (TextView) view.findViewById(R.id.title);
    }

    private void initListener() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.MY_ASSIGNMENT:
                    parseMyAssignmentResponse(serverResult);
                    break;

                case Constants.MY_ASSIGNMENT_CONFIRM_REJECT:
                    parseAcceptRejectRespnse(serverResult);
                    break;

            }
        }
    }

    private void parseMyAssignmentResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        TeacherListResponse mObjBrowse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjBrowse.isStatus()) {
            mAlMyAssignment = new ArrayList<>(mObjBrowse.getCategories().size());
            mAlMyAssignment.addAll(mObjBrowse.getCategories());
            mBrowseAdapter = new MyAssignmentAdapter(mContext, mAlMyAssignment);
            mRvBrowseExp.setAdapter(mBrowseAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            mRvBrowseExp.setLayoutManager(linearLayoutManager);
            mBrowseAdapter.setOnItemClickListener(this);

        } else {
            Toast.makeText(mContext, mObjBrowse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseAcceptRejectRespnse(JSONObject serverResult) {

        Gson gson = new Gson();
        GenerateOTPStatus mObjRespose = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mObjRespose.isStatus()) {
            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(mContext);
            LayoutInflater inflater1 = getActivity().getLayoutInflater();
            View dialogView1 = inflater1.inflate(R.layout.custom_request, null);
            dialogBuilder1.setView(dialogView1);
            ImageView mImgCancel1 = (ImageView) dialogView1.findViewById(R.id.imgcancel);
            TextView mTvForgorEmail = (TextView) dialogView1.findViewById(R.id.tv_requestsucess);
            mTvForgorEmail.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            final AlertDialog alertDialog1 = dialogBuilder1.create();
            if (tagVal.equals("confirm")) {
                mAlMyAssignment.get(selectPosition).setConfirm_tag(true);
                mTvForgorEmail.setText(R.string.yourconfirmrequest);
            } else {
                mAlMyAssignment.get(selectPosition).setConfirm_reject_tag(true);
                mTvForgorEmail.setText(R.string.yourrejectrequest);
            }
            alertDialog1.show();
            mImgCancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });


            mBrowseAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(mContext, mObjRespose.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag) {
        if (tag.equals("confirm")) {

            String toId = String.valueOf(browseResult.get(position).getTeacher_id());
             String fromId = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
             tagVal=tag;
             selectPosition = position;
             callConfirmNotificationAPI(toId, fromId,browseResult.get(position).getExpert_id());

        }

        if (tag.equals("reject")) {

            String toId = String.valueOf(browseResult.get(position).getTeacher_id());
            String fromId = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
            tagVal=tag;
            selectPosition = position;
            callConfirmNotificationAPI(toId, fromId,browseResult.get(position).getExpert_id());

        }

    }

    private void callConfirmNotificationAPI(String toId, String fromId, String expert_id) {

        String urlLogin = Constants.BASE_URL + Constants.MY_ASSIGNMENT_CONFIRM_REJECT;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FROM_ID, fromId);
            jsonObject.put(Constants.TO_ID, toId);
            jsonObject.put(Constants.EXPERT_ID, expert_id);
            jsonObject.put(Constants.NOTIFICATION_TYPE, tagVal);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.MY_ASSIGNMENT_CONFIRM_REJECT);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
