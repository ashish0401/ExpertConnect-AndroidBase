package com.expertconnect.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.activities.Dashboard;
import com.expertconnect.app.activities.RegisterExpertDetails;
import com.expertconnect.app.activities.SubCategoryList;
import com.expertconnect.app.adapters.MainCategoryAdapter;
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
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Home extends Fragment implements VolleyWebserviceCallBack, View.OnClickListener {

    private View view;

    private GridView mGVMain;
    private Context mContext;
    private ArrayList<Category> mALmainCat;
    private Toolbar mToolbar;
    private ImageView mImgMenu;
    private Dashboard activity;
    private AutoCompleteTextView mACSearch;
    private String[] mMainList;
    private TextView mTvTitle;
    private String mainCatName;
    private LinearLayout mHome;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        mContext = getActivity();
        activity = (Dashboard) getActivity();


        initView(view);
        initListener();
        setFonts();

        ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        ((AppCompatActivity) mContext).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Utils.isConnected(mContext)) {
            callMainCategoryAPI();
        } else {
            Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }


        mGVMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String subId = mALmainCat.get(position).getCategoryId();
                mainCatName = mALmainCat.get(position).getCategoryName();
                Utils.setSharedPrefString(Constants.MAIN_CAT, subId, mContext);
                callSubCategoryAPI(subId);

            }
        });


        mACSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                int pos1 = Arrays.asList(mMainList).indexOf(selected);
                String subId = mALmainCat.get(pos1).getCategoryId();
                mainCatName = mALmainCat.get(pos1).getCategoryName();
                callSubCategoryAPI(subId);

            }
        });

        return view;
    }

    private void setFonts() {
        mTvTitle.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }

    private void callSubCategoryAPI(String subId) {
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


    private void initView(View view) {
        mGVMain = (GridView) view.findViewById(R.id.grid_main);
        mImgMenu = (ImageView) view.findViewById(R.id.img_menu);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mACSearch = (AutoCompleteTextView) view.findViewById(R.id.ac_search);
        mTvTitle = (TextView) view.findViewById(R.id.title);
        mHome=(LinearLayout)view.findViewById(R.id.expert_layout);
    }

    private void initListener() {

        mImgMenu.setOnClickListener(this);
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

    private void parseSubCategoryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        SubCategoryResponse mSubCat = gson.fromJson(serverResult.toString(), SubCategoryResponse.class);
        if (mSubCat.getStatus()) {
            ArrayList<SubCategory> mALSubCat = new ArrayList<>(mSubCat.getSub_categories().size());
            mALSubCat.addAll(mSubCat.getSub_categories());
            Intent intent = new Intent(mContext, SubCategoryList.class);
            intent.putExtra(Constants.SUB_CATLIST, mALSubCat);
            intent.putExtra(Constants.MAIN_CAT_NAME, mainCatName);
            startActivity(intent);
        }
    }

    private void parseMainCategoryResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        MainCategoryResponse mMainCat = gson.fromJson(serverResult.toString(), MainCategoryResponse.class);
        if (mMainCat.getStatus()) {
            mALmainCat = new ArrayList<>(mMainCat.getCategories().size());


            mALmainCat.addAll(mMainCat.getCategories());
            mMainList = new String[mALmainCat.size()];
            for (int i = 0; i < mALmainCat.size(); i++) {
                mMainList[i] = mALmainCat.get(i).getCategoryName();
            }
            ArrayAdapter<?> adapter = new ArrayAdapter<Object>(activity, android.R.layout.simple_dropdown_item_1line, mMainList);
            mACSearch.setAdapter(adapter);
            mACSearch.setThreshold(1);

            mACSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() == 3) {
                        if (!mACSearch.isPopupShowing()) {
                            Toast toast = Toast.makeText(mContext,R.string.noresultsfound, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                    }
                }
            });

            MainCategoryAdapter mAdapter = new MainCategoryAdapter(getActivity(), mALmainCat);
            mGVMain.setAdapter(mAdapter);

        }
    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if(! (v instanceof AutoCompleteTextView))
        {
            try {
                Utils.hideSoftKeyboard(getActivity());
            }

            catch (Exception e){
                Log.e("Error",e.toString());
            }
        }

        int id = v.getId();

        switch (id) {
            case R.id.img_menu:
                activity.openDrawer();
                break;
        }
    }
}
