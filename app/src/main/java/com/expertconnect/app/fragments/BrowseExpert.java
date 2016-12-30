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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.activities.Dashboard;
import com.expertconnect.app.adapters.BrowseEnquiryAdapter;
import com.expertconnect.app.adapters.BrowseExpertAdapter;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.interfaces.OnOfferButtonClick;
import com.expertconnect.app.models.BrowseEnquiryResult;
import com.expertconnect.app.models.CoachingDetails;
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


public class BrowseExpert extends Fragment implements OnOfferButtonClick, View.OnClickListener, VolleyWebserviceCallBack, Dashboard.OnBackPressedListener {

    private View view;
    private BrowseExpertAdapter mBrowseAdapter;
    private RecyclerView mRvBrowseExp;
    private ArrayList<TeacherCategory> mAlBrowseExp = new ArrayList<>();
    private TeacherListResponse mObjTeacherResponse;
    private Context mContext;
    private Button mBtnExpert;
    private Button mBtnExpertWho;
    private TextView mTvTitle;
    private View view1;
    int selectPosition;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisited = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_browse_expert, container, false);
        }
        mContext = getActivity();
        initView(view);
        initListener();
        setFonts();
        ((Dashboard) getActivity()).setOnBackPressedListener(this);
        AllConstants.callBrowseExpert = false;
        if (Utils.isConnected(mContext)) {
            callBrowseExpertAPI();
        } else {
            Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnExpert.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnExpertWho.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }


    private void initView(View view) {
        mRvBrowseExp = (RecyclerView) view.findViewById(R.id.rv_browse_expert);

        mBtnExpert = (Button) view.findViewById(R.id.btn_expert);
        mBtnExpertWho = (Button) view.findViewById(R.id.btn_expert_who);
        mTvTitle = (TextView) view.findViewById(R.id.title);
    }

    private void initListener() {
        mBtnExpert.setOnClickListener(this);
        mBtnExpertWho.setOnClickListener(this);
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
    public void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag) {
        if (mAlBrowseExp.get(position).isTag()) {
            Toast.makeText(mContext, R.string.already, Toast.LENGTH_SHORT).show();
        } else {
            String toId = String.valueOf(browseResult.get(position).getTeacher_id());
            String fromId = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
            view1 = v;
            selectPosition = position;

            callNotificationRequestAPI(toId,fromId,browseResult.get(position).getExpert_id());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_expert:
                shuffel("Expert");
                break;

            case R.id.btn_expert_who:
                shuffel("ExpertWho");
                break;
        }
    }

    private void shuffel(String expert) {
        if (expert.equals("Expert")) {
            mBtnExpert.setBackgroundResource(R.drawable.expert_selected);
            mBtnExpert.setTextColor(getResources().getColor(R.color.white));
            mBtnExpertWho.setTextColor(getResources().getColor(R.color.black));
            mBtnExpertWho.setBackgroundResource(R.drawable.expert_unselected);
        } else {
            mBtnExpertWho.setBackgroundResource(R.drawable.expert_selected);
            mBtnExpert.setBackgroundResource(R.drawable.expert_unselected);
            mBtnExpertWho.setTextColor(getResources().getColor(R.color.white));
            mBtnExpert.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void callNotificationRequestAPI(String toId, String fromId, String expert_id) {
        String urlLogin = Constants.BASE_URL + Constants.NOTIFICATION_REQUEST;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FROM_ID, fromId);
            jsonObject.put(Constants.TO_ID, toId);
            jsonObject.put(Constants.EXPERT_ID, expert_id);
            jsonObject.put(Constants.NOTIFICATION_TYPE, "request");


            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.NOTIFICATION_REQUEST);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callBrowseExpertAPI() {


        String user_id = String.valueOf(Utils.getSharedPrefInt(Constants.USER_ID, mContext));
        String location = String.valueOf(Utils.getSharedPrefString(Constants.LOCATION, mContext));
        String urlLogin = Constants.BASE_URL + Constants.BROWSE_EXPERT;

        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.USER_ID, user_id);
            jsonObject.put(Constants.LOCATION, location);
            if (mAlBrowseExp.size() == 0) {
                jsonObject.put(Constants.OFFSET, "0");
            } else {
                jsonObject.put(Constants.OFFSET, String.valueOf(mObjTeacherResponse.getNext_offset()));
            }
            jsonObject.put(Constants.LIMIT, "10");

            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.BROWSE_EXPERT);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.BROWSE_EXPERT:
                    parseBrowseExpertResponse(serverResult);
                    break;

                case Constants.NOTIFICATION_REQUEST:
                    parseNotificationResponse(serverResult);
                    break;
            }
        }
    }

    private void parseBrowseExpertResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        mObjTeacherResponse = gson.fromJson(serverResult.toString(), TeacherListResponse.class);
        if (mObjTeacherResponse.isStatus()) {
            int finalCount = mObjTeacherResponse.getTotal_count();
            int nextOffset = mObjTeacherResponse.getNext_offset();
            if (nextOffset >= finalCount) {
                AllConstants.callBrowseExpert = true;
            }
            if (mAlBrowseExp.size() == 0) {
                mAlBrowseExp.addAll(mObjTeacherResponse.getCategories());
                mBrowseAdapter = new BrowseExpertAdapter(mContext, mAlBrowseExp);
                mRvBrowseExp.setAdapter(mBrowseAdapter);
                linearLayoutManager = new LinearLayoutManager(mContext);
                mRvBrowseExp.setLayoutManager(linearLayoutManager);
            } else {
                mAlBrowseExp.addAll(mObjTeacherResponse.getCategories());
                mBrowseAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPositionWithOffset(lastVisited, 0);
            }


            mRvBrowseExp.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!mAlBrowseExp.isEmpty()) {
                        lastVisited = ((LinearLayoutManager) mRvBrowseExp.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        if (!(AllConstants.callBrowseExpert)) {
                            if (lastVisited == mBrowseAdapter.getItemCount() - 1) {
                                callBrowseExpertAPI();
                            }
                        }

                    }
                }
            });


            mBrowseAdapter.setOnItemClickListener(this);
        } else {
            //Toast.makeText(mContext, mObjTeacherResponse.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void parseNotificationResponse(JSONObject serverResult) {
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
            alertDialog1.show();
            mImgCancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });
            mAlBrowseExp.get(selectPosition).setTag(true);
            view1.findViewById(R.id.btn_sendoffer).setBackgroundColor(getResources().getColor(R.color.request_gray));
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
    public void doBack() {
        AllConstants.callBrowseExpert = true;
    }
}
