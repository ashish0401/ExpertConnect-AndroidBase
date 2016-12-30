package com.expertconnect.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.expertconnect.app.R;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.User;
import com.expertconnect.app.utils.Utils;

public class RegisterECredits extends Activity implements View.OnClickListener {
    private Button mbtnSkip;
    private ImageView mImgBack;
    private User mUserId;
    private Context mContext;
    private TextView mTvAddECred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ecredits);
        mContext = RegisterECredits.this;
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_DATA)) {
            mUserId= (User) getIntent().getSerializableExtra(Constants.USER_DATA);
        }
        initViews();
        initListener();
        setFonts();
    }

    private void initViews() {
        mbtnSkip = (Button) findViewById(R.id.btnskip);
        mImgBack = (ImageView) findViewById(R.id.back);
        mTvAddECred = (TextView) findViewById(R.id.tv_addecredits);
    }

    private void initListener() {
        mbtnSkip.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    private void setFonts() {
        mbtnSkip.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvAddECred.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.btnskip:
                Intent intent = new Intent(RegisterECredits.this, RegisterCoaching.class);
                intent.putExtra(Constants.USER_DATA, mUserId);
                startActivity(intent);
                break;

            case R.id.back:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
