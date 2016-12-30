package com.expertconnect.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.constants.EmailValidator;
import com.expertconnect.app.models.GenerateOTPStatus;
import com.expertconnect.app.models.LoginResponse;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;


public class Login extends Activity implements View.OnClickListener, VolleyWebserviceCallBack {

    private Button mBtnLogin;
    private Button mBtnSignUp;
    private ImageView mImgFacebook;
    private EditText mEtEmail;
    private TextView mTvForgotPass;
    private TextView mTvLoginWith;
    private TextView mTvExpertLogo;
    private TextView mTvExpertLogo1;
    private EditText mEtPassword;
    private CallbackManager callbackManager;
    private Context mContext;
    private String email;
    private String password;
    private LoginResponse mLoginResponse;
    private Button mBtnSendEmail;
    private EditText mEtSendEmail;
    private GenerateOTPStatus mForgotPass;
    private String facebook_id;
    private LinearLayout mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        mContext = Login.this;
        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();
        initView();
        initListener();
        setFonts();
        AllConstants.isFacebook = false;

        boolean loggedIn = Utils.getSharedPrefBoolean(Constants.ISLOGGEDIN, mContext);
        if (!loggedIn) {
            new deleteFCMID().execute();
        }


    }


    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.btnlogin);
        mBtnSignUp = (Button) findViewById(R.id.btnsign);
        mImgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        mEtEmail = (EditText) findViewById(R.id.email);
        mTvForgotPass = (TextView) findViewById(R.id.tv_forgotpassword);
        mTvLoginWith = (TextView) findViewById(R.id.tv_loginwith);
        mTvExpertLogo = (TextView) findViewById(R.id.tv_expertlogo);
        mTvExpertLogo1 = (TextView) findViewById(R.id.tv_expertlogo1);
        String text = "<font color='#f25a0b'>E</font>XPERT";
        String text1 = "<font color='#f25a0b'>C</font>ONNECT";
        mTvExpertLogo.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        mTvExpertLogo1.setText(Html.fromHtml(text1), TextView.BufferType.SPANNABLE);
        mEtPassword = (EditText) findViewById(R.id.pass);
        mScrollView = (LinearLayout) findViewById(R.id.login_mainlayout);
    }


    private void initListener() {
        mBtnLogin.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mImgFacebook.setOnClickListener(this);
        mTvForgotPass.setOnClickListener(this);
        mScrollView.setOnClickListener(this);
    }

    private void setFonts() {
        mBtnLogin.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnSignUp.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvForgotPass.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvLoginWith.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvExpertLogo.setTypeface(Utils.setFonts(mContext, "fonts/logo.ttf"));
        mTvExpertLogo1.setTypeface(Utils.setFonts(mContext, "fonts/logo.ttf"));

    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof EditText)) {
            try {
                Utils.hideSoftKeyboard(Login.this);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
        int id = v.getId();
        switch (id) {

            case R.id.btnlogin:
                email = mEtEmail.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();

                if (email.length() > 0) {
                    EmailValidator emailValidator = new EmailValidator();
                    if (emailValidator.validate(email)) {
                        if (password.length() > 0) {
                            if (Utils.isConnected(this)) {
                                callLoginAPI();
                            } else {
                                Toast.makeText(Login.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Login.this, R.string.passworderror, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, R.string.validemail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, R.string.emailerror, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnsign:
                callSignUpAs();
                break;

            case R.id.tv_forgotpassword:
                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(this);
                LayoutInflater inflater1 = this.getLayoutInflater();
                View dialogView1 = inflater1.inflate(R.layout.forgot_password, null);
                dialogBuilder1.setView(dialogView1);
                mBtnSendEmail = (Button) dialogView1.findViewById(R.id.btnsendemail);
                mEtSendEmail = (EditText) dialogView1.findViewById(R.id.et_emailforgot);
                TextView mTvForgorEmail = (TextView) dialogView1.findViewById(R.id.tv_forgotemail);
                ImageView mImgCancel1 = (ImageView) dialogView1.findViewById(R.id.imgcancel);
                mBtnSendEmail.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
                mTvForgorEmail.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

                final AlertDialog alertDialog1 = dialogBuilder1.create();
                alertDialog1.show();

                mBtnSendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String forgotemail = mEtSendEmail.getText().toString().trim();
                        if (forgotemail.length() > 0) {
                            EmailValidator emailValidator = new EmailValidator();
                            if (emailValidator.validate(forgotemail)) {
                                if (Utils.isConnected(mContext)) {
                                    callForgotPasswordAPI(forgotemail);
                                    alertDialog1.dismiss();
                                } else {
                                    Toast.makeText(Login.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mImgCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                break;

            case R.id.imgFacebook:
                logInWithFb();
                break;

            default:
                break;
        }
    }

    private void callSignUpAs() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        Button mBtnSignTeacher = (Button) dialogView.findViewById(R.id.teachersign);
        Button mBtnSignStudent = (Button) dialogView.findViewById(R.id.studentsign);
        TextView mTvSignUpAs = (TextView) dialogView.findViewById(R.id.tv_signupas);
        ImageView mImgCancel = (ImageView) dialogView.findViewById(R.id.imgcancel);
        mBtnSignTeacher.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mBtnSignStudent.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        mTvSignUpAs.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Medium.otf"));
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        mBtnSignTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllConstants.isStudentLoggedIn = false;
                Intent intent = new Intent(Login.this, RegisterProfile.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        mBtnSignStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllConstants.isStudentLoggedIn = true;
                Intent intent = new Intent(Login.this, RegisterProfile.class);
                startActivity(intent);
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

    private void callForgotPasswordAPI(String forgotemail) {
        String urlLogin = Constants.BASE_URL + Constants.FORGOT_PASSWORD;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.EMAIL_ID, forgotemail);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.FORGOT_PASSWORD);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callLoginAPI() {
        String urlLogin = Constants.BASE_URL + Constants.LOGIN;
        String device_Token = Utils.getSharedPrefString(Constants.GCM_ID, mContext);
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EMAIL_ID, email);
            jsonObject.put(Constants.PASSWORD, password);
            jsonObject.put(Constants.DEVICE_TOKEN, device_Token);
            jsonObject.put(Constants.OS_TYPE, "android");
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.LOGIN);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void logInWithFb() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_location"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Bundle params = new Bundle();
                params.putString("fields", "id,email,gender,cover,picture.type(large)");
                new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {

                            }
                        }).executeAsync();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                String name = null;

                                facebook_id = null;
                                String gender = null;
                                try {

                                    email = object.getString("email");
                                    facebook_id = object.getString("id");
                                    name = object.getString("name");
                                    gender = object.getString("gender");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                LoginManager.getInstance().logOut();
                                AllConstants.isFacebook = true;
                                Utils.setSharedPrefString(Constants.FACEBOOK_ID, facebook_id, mContext);
                                Utils.setSharedPrefString(Constants.FIRST_NAME, name, mContext);
                                Utils.setSharedPrefString(Constants.EMAIL_ID, email, mContext);
                                Utils.setSharedPrefString(Constants.GENDER, gender, mContext);
                                if (Utils.isConnected(mContext)) {
                                    checkIfFbCredentialExist();
                                } else {
                                    Toast.makeText(Login.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,link,gender,birthday,email,name,location");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, R.string.requestcancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(Login.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class deleteFCMID extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                    Utils.setSharedPrefString(Constants.GCM_ID, null, Login.this);
                    if (Utils.getSharedPrefString(Constants.GCM_ID, mContext) == null ||Utils.getSharedPrefString(Constants.GCM_ID, mContext).equals("") ) {
                        String token = FirebaseInstanceId.getInstance().getToken();
                        Utils.setSharedPrefString(Constants.GCM_ID, token, mContext);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AllConstants.isFacebook = false;
        boolean loggedIn = Utils.getSharedPrefBoolean(Constants.ISLOGGEDIN, mContext);
        if (loggedIn) {
            Intent mIntent = new Intent(Login.this, Dashboard.class);
            startActivity(mIntent);
            Login.this.finish();
        }
    }

    private void checkIfFbCredentialExist() {
        String urlLogin = Constants.BASE_URL + Constants.FB_LOGIN;
        String device_Token = Utils.getSharedPrefString(Constants.GCM_ID, mContext);
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.REG_TYPE, "2");
            jsonObject.put(Constants.SOCIAL_ID, facebook_id);
            jsonObject.put(Constants.OS_TYPE, "android");
            jsonObject.put(Constants.DEVICE_TOKEN, device_Token);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.FB_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.LOGIN:
                    parseLoginResponse(serverResult);
                    break;

                case Constants.FB_LOGIN:
                    parseFBLoginResponse(serverResult);
                    break;

                case Constants.FORGOT_PASSWORD:
                    parseForgotPasswordResponse(serverResult);
                    break;
            }
        }
    }

    private void parseFBLoginResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        mLoginResponse = gson.fromJson(serverResult.toString(), LoginResponse.class);
        if (mLoginResponse.isStatus()) {
            setUserLoggedData();
        } else {
            callSignUpAs();
        }

    }

    private void setUserLoggedData() {
        Utils.setSharedPrefInt(Constants.USER_ID, mLoginResponse.getUser_id(), mContext);
        Utils.setSharedPrefString(Constants.USER_TYPE, mLoginResponse.getUsertype(), mContext);
        Utils.setSharedPrefString(Constants.FIRST_NAME, mLoginResponse.getFirstname(), mContext);
        Utils.setSharedPrefString(Constants.LAST_NAME, mLoginResponse.getLastname(), mContext);
        Utils.setSharedPrefString(Constants.LOCATION, mLoginResponse.getLocation(), mContext);
        Utils.setSharedPrefString(Constants.LATITUDE, mLoginResponse.getLatitude(), mContext);
        Utils.setSharedPrefString(Constants.LONGITUDE, mLoginResponse.getLongitude(), mContext);


        if (mLoginResponse.getReg_type().equals("2")) {
            AllConstants.isFacebook = true;
            Utils.setSharedPrefString(Constants.FACEBOOK_ID, mLoginResponse.getSocial_id(), mContext);
        } else {
            Utils.setSharedPrefString(Constants.PROFILE_PIC, mLoginResponse.getProfile_pic(), mContext);
        }
        Intent mIntent = new Intent(Login.this, Dashboard.class);
        startActivity(mIntent);
        Utils.setSharedPrefBoolean(Constants.ISLOGGEDIN, true, mContext);
        Login.this.finish();
    }

    private void parseForgotPasswordResponse(JSONObject serverResult) {

        Gson gson = new Gson();
        mForgotPass = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mForgotPass.isStatus()) {
            Toast.makeText(Login.this, R.string.verification, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Login.this, mForgotPass.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseLoginResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        mLoginResponse = gson.fromJson(serverResult.toString(), LoginResponse.class);
        if (mLoginResponse.isStatus()) {
            setUserLoggedData();
        } else {
            Toast.makeText(Login.this, R.string.pleasechecklogin, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(Login.this, R.string.error, Toast.LENGTH_SHORT).show();
    }


}
