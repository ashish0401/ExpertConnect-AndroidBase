package com.expertconnect.app.activities;


import android.content.ComponentName;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.fragments.BrowseEnquiry;
import com.expertconnect.app.fragments.BrowseExpert;
import com.expertconnect.app.fragments.FragmentDrawer;
import com.expertconnect.app.fragments.Home;
import com.expertconnect.app.fragments.MyAssignment;
import com.expertconnect.app.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
    private DrawerLayout mDrawerLayout;
    private ImageView mImgHome;
    private ImageView mImgAssignment;
    private ImageView mImgEnquiry;
    private ImageView mImgExpert;
    private OnBackPressedListener onBackPressedListener;
    private RelativeLayout mRlDash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        initView();
        initListener();
        AllConstants.isCallBrowseEnquiry = false;
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.BROWSE_ENQUIRY)) {
            AllConstants.isCallBrowseEnquiry = true;
        }

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), null);
        drawerFragment.setDrawerListener(this);
        if (AllConstants.isCallBrowseEnquiry) {
            selectFragment(AllConstants.BROWSE_ENQUIRY, true);
        } else {
            selectFragment(AllConstants.HOME, false);
        }

    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mImgHome = (ImageView) findViewById(R.id.img_home);
        mImgAssignment = (ImageView) findViewById(R.id.img_assignment);
        mImgEnquiry = (ImageView) findViewById(R.id.img_enquiry);
        mImgExpert = (ImageView) findViewById(R.id.img_expert);
        mRlDash=(RelativeLayout)findViewById(R.id.rl_dash);
    }

    private void initListener() {

        mImgHome.setOnClickListener(this);
        mImgAssignment.setOnClickListener(this);
        mImgEnquiry.setOnClickListener(this);
        mImgExpert.setOnClickListener(this);
        mRlDash.setOnClickListener(this);
    }

    private void selectFragment(int position, boolean b) {
        Fragment fragment = null;

        switch (position) {
            case AllConstants.HOME:
                fragment = new Home();
                break;

            case AllConstants.ASSIGNMENTS:
                fragment = new MyAssignment();
                break;

            case AllConstants.BROWSE_ENQUIRY:
                fragment = new BrowseEnquiry();
                if (AllConstants.isCallBrowseEnquiry && b) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.BROWSE_ENQUIRY, true);
                    fragment.setArguments(bundle);
                }
                break;

            case AllConstants.BROWSE_EXPERT:
                fragment = new BrowseExpert();
                break;

            default:
                break;
        }
        displayFragment(fragment);
    }

    private void displayFragment(Fragment fragment) {
        if (fragment != null) {

            String backStateName = fragment.getClass().getName();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        navigationSelection(position);

    }


    private void navigationSelection(int position) {
        switch (position) {

            case AllConstants.NOTIFICATION:
                Intent mIntent = new Intent(Dashboard.this, Notification.class);
                startActivity(mIntent);
                break;

            case AllConstants.MY_CATEGORY:
                break;

            case AllConstants.REFER_FRIEND:
                break;

            case AllConstants.MANAGE_EXPERTISE:
                break;

            case AllConstants.MY_ACCOUNT:
                Intent mIntent1 = new Intent(Dashboard.this, MyAccount.class);
                startActivity(mIntent1);
                break;

            case AllConstants.SETTINGS:
                break;

            case AllConstants.LOGOUT:

                Intent intent = new Intent(Dashboard.this, Login.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
                Utils.setSharedPrefBoolean(Constants.ISLOGGEDIN, false, Dashboard.this);


                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if(! (v instanceof AutoCompleteTextView))
        {
            try {
                Utils.hideSoftKeyboard(this);
            }

            catch (Exception e){
                Log.e("Error",e.toString());
            }
        }

        int id = v.getId();

        switch (id) {

            case R.id.img_home:
                homeSelection();
                selectFragment(AllConstants.HOME, false);
                break;

            case R.id.img_assignment:
                assignmentSelection();
                selectFragment(AllConstants.ASSIGNMENTS, false);
                break;

            case R.id.img_enquiry:
                enquirySelection();
                selectFragment(AllConstants.BROWSE_ENQUIRY, false);
                break;

            case R.id.img_expert:
                expertSelection();
                selectFragment(AllConstants.BROWSE_EXPERT, false);
                break;


            default:
                break;
        }
    }

    public void expertSelection() {

        mImgExpert.setImageResource(R.drawable.browse_experts_selected_btn);
        mImgHome.setImageResource(R.drawable.home_unselected_btn);
        mImgAssignment.setImageResource(R.drawable.myassignment_unselected_btn);
        mImgEnquiry.setImageResource(R.drawable.browse_enquiry_unselected_btn);
    }

    public void enquirySelection() {
        mImgEnquiry.setImageResource(R.drawable.browse_enquiry_selected_btn);
        mImgHome.setImageResource(R.drawable.home_unselected_btn);
        mImgAssignment.setImageResource(R.drawable.myassignment_unselected_btn);
        mImgExpert.setImageResource(R.drawable.browse_experts_unselected_btn);
    }

    public void assignmentSelection() {
        mImgAssignment.setImageResource(R.drawable.myassignment_selected_btn);
        mImgHome.setImageResource(R.drawable.home_unselected_btn);
        mImgEnquiry.setImageResource(R.drawable.browse_enquiry_unselected_btn);
        mImgExpert.setImageResource(R.drawable.browse_experts_unselected_btn);
    }

    public void homeSelection() {
        mImgHome.setImageResource(R.drawable.home_selected_btn);
        mImgAssignment.setImageResource(R.drawable.myassignment_unselected_btn);
        mImgEnquiry.setImageResource(R.drawable.browse_enquiry_unselected_btn);
        mImgExpert.setImageResource(R.drawable.browse_experts_unselected_btn);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            finish();
            return;
        }
        getSupportFragmentManager().popBackStackImmediate();
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        if (tag.equals("com.expertconnect.app.fragments.Home")) {
            homeSelection();
        } else if (tag.equals("com.expertconnect.app.fragments.MyAssignment")) {
            assignmentSelection();
        } else if (tag.equals("com.expertconnect.app.fragments.BrowseEnquiry")) {
            enquirySelection();
        } else if (tag.equals("com.expertconnect.app.fragments.BrowseExpert")) {
            onBackPressedListener.doBack();
            expertSelection();

        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        onBackPressedListener = null;

    }
}
