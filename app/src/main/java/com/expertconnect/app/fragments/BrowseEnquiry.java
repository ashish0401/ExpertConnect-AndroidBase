package com.expertconnect.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.activities.EnquiryFilter;
import com.expertconnect.app.adapters.BrowseEnquiryAdapter;
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


public class BrowseEnquiry extends Fragment implements View.OnClickListener, VolleyWebserviceCallBack, OnOfferButtonClick {

    private View view;
    private ImageView mImgFilterSearch;
    private BrowseEnquiryAdapter mBrowseAdapter;
    private RecyclerView mRvBrowseEnq;
    private ArrayList<TeacherCategory> mAlBrowseEnq;
    private Context mContext;
    private TextView mTvTitle;
    private boolean result;
    private Button mBtnRecived;
    private Button mBtnSent;
    private boolean isSent = false;
    int selectPosition;
    private String tagVal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_browse_enquiry, container, false);
        }
        mContext = getActivity();
        initView(view);
        initListener();
        setFonts();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(Constants.BROWSE_ENQUIRY)) {
            result = arguments.getBoolean(Constants.BROWSE_ENQUIRY);
        }
        if (Utils.isConnected(mContext)) {
            callBrowseEnquiryAPI();
        } else {
            Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnRecived.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnSent.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }


    private void initView(View view) {
        mRvBrowseEnq = (RecyclerView) view.findViewById(R.id.rv_browse_enquiry);
        mRvBrowseEnq.setLayoutManager(new LinearLayoutManager(getActivity()));
        mImgFilterSearch = (ImageView) view.findViewById(R.id.img_search_filter);
        mTvTitle = (TextView) view.findViewById(R.id.title);
        mBtnRecived = (Button) view.findViewById(R.id.btn_recived);
        mBtnSent = (Button) view.findViewById(R.id.btn_sent);
    }

    private void initListener() {
        mImgFilterSearch.setOnClickListener(this);
        mBtnRecived.setOnClickListener(this);
        mBtnSent.setOnClickListener(this);
    }

    private void callBrowseEnquiryAPI() {
        String main = Utils.getSharedPrefString(Constants.FILTER_MAIN, mContext);
        String sub = Utils.getSharedPrefString(Constants.FILTER_SUB, mContext);
        String amount = Utils.getSharedPrefString(Constants.AMOUNT, mContext);
        String user_id = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
        String urlLogin = Constants.BASE_URL + Constants.BROWSE_ENQUIRY;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
            if (result) {
                jsonObject.put(Constants.TEACHER_ID, user_id);
                jsonObject.put(Constants.CATEGORY_ID, main);
                jsonObject.put(Constants.SUB_CATEGORY_ID, sub);
                jsonObject.put(Constants.FILTER, "yes");
                jsonObject.put(Constants.AMOUNT, amount);
                result = false;
            } else {
                jsonObject.put(Constants.TEACHER_ID, user_id);
                jsonObject.put(Constants.CATEGORY_ID, "0");
                jsonObject.put(Constants.SUB_CATEGORY_ID, "0");
                jsonObject.put(Constants.FILTER, "no");
                jsonObject.put(Constants.AMOUNT, "0");
            }

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.BROWSE_ENQUIRY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        int id = v.getId();

        switch (id) {
            case R.id.img_search_filter:
                Intent mIntent = new Intent(mContext, EnquiryFilter.class);
                startActivity(mIntent);
                break;

            case R.id.btn_recived:
                shuffel("recived");
                break;

            case R.id.btn_sent:
                shuffel("sent");
                break;
        }
    }

    private void shuffel(String expert) {
        if (expert.equals("recived")) {
            mBtnRecived.setBackgroundResource(R.drawable.expert_selected);
            mBtnRecived.setTextColor(getResources().getColor(R.color.white));
            mBtnSent.setTextColor(getResources().getColor(R.color.black));
            mBtnSent.setBackgroundResource(R.drawable.expert_unselected);
            isSent = false;
            callBrowseEnquiryAPI();
        } else {
            mBtnSent.setBackgroundResource(R.drawable.expert_selected);
            mBtnRecived.setBackgroundResource(R.drawable.expert_unselected);
            mBtnSent.setTextColor(getResources().getColor(R.color.white));
            mBtnRecived.setTextColor(getResources().getColor(R.color.black));
            isSent = true;
            callBrowseEnquiryAPINotify();
        }
    }

    private void callBrowseEnquiryAPINotify() {

        String user_id = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
        String urlLogin = Constants.BASE_URL + Constants.BROWSE_ENQUIRY_SENT;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, user_id);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.BROWSE_ENQUIRY_SENT);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.BROWSE_ENQUIRY:
                    parseBrowseEnquiryResponse(serverResult);
                    break;

                case Constants.BROWSE_ENQUIRY_SENT:
                    parseBrowseEnquiryResponse(serverResult);
                    break;

                case Constants.BROWSE_ENQUIRY_ACCEPT_CONFIRM:
                    parseAcceptRejectRespnse(serverResult);
                    break;

            }
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
            if (tagVal.equals("accept")) {
                mAlBrowseEnq.get(selectPosition).setAccept_tag(true);
                mTvForgorEmail.setText(R.string.youracceptrequest);
            } else {
                mAlBrowseEnq.get(selectPosition).setAccept_reject_tag(true);
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

    private void parseBrowseEnquiryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        TeacherListResponse mObjBrowse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjBrowse.isStatus()) {
            mAlBrowseEnq = new ArrayList<>(mObjBrowse.getCategories().size());
            mAlBrowseEnq.addAll(mObjBrowse.getCategories());
            if (isSent) {
                mBrowseAdapter = new BrowseEnquiryAdapter(mContext, mAlBrowseEnq, true);
            } else {
                mBrowseAdapter = new BrowseEnquiryAdapter(mContext, mAlBrowseEnq, false);
            }
            mRvBrowseEnq.setAdapter(mBrowseAdapter);
            mBrowseAdapter.setOnItemClickListener(this);

        } else {
            Toast.makeText(mContext, mObjBrowse.getMessage(), Toast.LENGTH_SHORT).show();
            mAlBrowseEnq=new ArrayList<>();
            if (isSent) {
                mBrowseAdapter = new BrowseEnquiryAdapter(mContext, mAlBrowseEnq, true);
            } else {
                mBrowseAdapter = new BrowseEnquiryAdapter(mContext, mAlBrowseEnq, false);
            }
            mRvBrowseEnq.setAdapter(mBrowseAdapter);
            mBrowseAdapter.setOnItemClickListener(this);

        }

    }

    @Override
    public void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag) {
        if (tag.equals("request")) {
            Toast.makeText(mContext, R.string.already, Toast.LENGTH_SHORT).show();
        }

        if (tag.equals("accept")) {
            if (mAlBrowseEnq.get(position).isAccept_tag()) {
                Toast.makeText(mContext, R.string.alreadyaccept, Toast.LENGTH_SHORT).show();
            } else {
                String toId = String.valueOf(browseResult.get(position).getTeacher_id());
                String fromId = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
                selectPosition = position;
                tagVal = "accept";
                callAcceptNotificationAPI(toId, fromId,browseResult.get(position).getExpert_id());
            }
        }

        if (tag.equals("reject")) {
            if (mAlBrowseEnq.get(position).isAccept_tag()) {
                Toast.makeText(mContext, R.string.alreadyrejected, Toast.LENGTH_SHORT).show();
            } else {
                String toId = String.valueOf(browseResult.get(position).getTeacher_id());
                String fromId = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
                selectPosition = position;
                tagVal = "reject";
                callAcceptNotificationAPI(toId, fromId, browseResult.get(position).getExpert_id());
            }
        }

        if (tag.equals("accepted")) {
            Toast.makeText(mContext, R.string.alreadyaccept, Toast.LENGTH_SHORT).show();
        }

        if (tag.equals("rejected")) {
            Toast.makeText(mContext, R.string.alreadyrejected, Toast.LENGTH_SHORT).show();
        }
    }

    private void callAcceptNotificationAPI(String toId, String fromId, String expert_id) {
        String urlLogin = Constants.BASE_URL + Constants.BROWSE_ENQUIRY_ACCEPT_CONFIRM;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FROM_ID, fromId);
            jsonObject.put(Constants.TO_ID, toId);
            jsonObject.put(Constants.EXPERT_ID, expert_id);
            jsonObject.put(Constants.NOTIFICATION_TYPE, tagVal);

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.BROWSE_ENQUIRY_ACCEPT_CONFIRM);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
    }
}
