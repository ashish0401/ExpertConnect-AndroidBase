package com.expertconnect.app.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expertconnect.app.R;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.constants.EmailValidator;
import com.expertconnect.app.models.GenerateOTPStatus;
import com.expertconnect.app.models.User;
import com.expertconnect.app.select_country.SelectCountry;
import com.expertconnect.app.utils.Utils;
import com.expertconnect.app.volley.VolleyWebserviceCall;
import com.expertconnect.app.volley.VolleyWebserviceCallBack;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterProfile extends AppCompatActivity implements View.OnClickListener, VolleyWebserviceCallBack, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Button mbtnNext;
    private ImageView mImgChoose;
    private TextView mTvLocation;
    private ImageView mImgBack;
    private ImageView mImgProfilePic;
    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtMobile;
    private EditText mEtDob;
    private EditText mEtOtp;
    private ImageView mImgLocationPicker;
    private GoogleApiClient mGoogleApiClient;
    private Activity activity;
    private Double lat = 0.0, lon = 0.0;
    private LocationManager lm;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 11111;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private int currentDay;
    private int currentMonth, currentYear;
    private String currentDate;
    private Date currentDateObject;
    private Context mContext;
    private String firstname;
    private String mobile;
    private String lastname;
    private String email;
    private String gender = "male";
    private String password;
    private String dob;
    private String countryCode;
    private String location;
    private TextView mTVCountryCode;
    private LinearLayout llForCountryCode;
    private AlertDialog alertDialog;
    private String imgBase64 = "";
    private String socialId;
    private RadioGroup radioGenderGrp;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private boolean isGender = false;
    int idx;
    private LinearLayout mProfilMain;
    private User mUser;
    private long milliseconds = (long) 365 * 24 * 60 * 60 * 12009;

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            mEtDob.setText(selectedDay + " - " + (selectedMonth + 1) + " - "
                    + selectedYear);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = RegisterProfile.this;
        initViews();
        setFonts();
        initListener();
        AllConstants.isProfilePicSet = false;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermissions();
        getCurrentDate();

        if (AllConstants.isFacebook) {
            setFbData();
        }
        activity = RegisterProfile.this;

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void initViews() {
        mbtnNext = (Button) findViewById(R.id.btnnext);
        mEtFirstName = (EditText) findViewById(R.id.firstname);
        mEtLastName = (EditText) findViewById(R.id.lastname);
        mEtEmail = (EditText) findViewById(R.id.email);
        mEtPassword = (EditText) findViewById(R.id.pass);
        mEtMobile = (EditText) findViewById(R.id.mobile);
        mEtDob = (EditText) findViewById(R.id.dob);
        mImgBack = (ImageView) findViewById(R.id.back);
        mImgChoose = (ImageView) findViewById(R.id.imgChooseOpt);
        mImgProfilePic = (ImageView) findViewById(R.id.profile_image);
        mImgLocationPicker = (ImageView) findViewById(R.id.locationpicker);
        mTvLocation = (TextView) findViewById(R.id.tvlocation);
        mTVCountryCode = (TextView) findViewById(R.id.tv_CountryCode);
        llForCountryCode = (LinearLayout) findViewById(R.id.llForCountryCode);
        radioGenderGrp = (RadioGroup) findViewById(R.id.radio_grp);
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        mProfilMain = (LinearLayout) findViewById(R.id.profile_layout);
    }


    private void initListener() {
        mbtnNext.setOnClickListener(this);
        mEtDob.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mImgChoose.setOnClickListener(this);
        mImgLocationPicker.setOnClickListener(this);
        llForCountryCode.setOnClickListener(this);
        mProfilMain.setOnClickListener(this);
        mTvLocation.setOnClickListener(this);
    }

    private void setFonts() {
        mbtnNext.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
    }

    private void setFbData() {
        String firstname = Utils.getSharedPrefString(Constants.FIRST_NAME, mContext);
        String wholeName[] = firstname.split("\\s+");
        socialId = Utils.getSharedPrefString(Constants.FACEBOOK_ID, mContext);
        String fbEmail = Utils.getSharedPrefString(Constants.EMAIL_ID, mContext);
        gender = Utils.getSharedPrefString(Constants.GENDER, mContext);
        Picasso.with(mContext)
                .load("https://graph.facebook.com/" + socialId + "/picture?type=large")
                .into(mImgProfilePic);
        mEtFirstName.setText(wholeName[0]);
        mEtLastName.setText(wholeName[1]);
        mEtEmail.setText(fbEmail);
        if (gender.equals("female")) {
            radioFemale.setChecked(true);
        } else {
            radioMale.setChecked(true);
        }
        mImgChoose.setVisibility(View.GONE);
        mEtFirstName.setFocusable(false);
        mEtFirstName.setFocusableInTouchMode(false);
        mEtFirstName.setClickable(false);
        mEtLastName.setFocusable(false);
        mEtLastName.setFocusableInTouchMode(false);
        mEtLastName.setClickable(false);
        mEtEmail.setFocusable(false);
        mEtEmail.setFocusableInTouchMode(false);
        mEtEmail.setClickable(false);
    }


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-milliseconds);
        return datePickerDialog;
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof EditText)) {
            try {
                Utils.hideSoftKeyboard(RegisterProfile.this);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }

        int id = v.getId();
        switch (id) {

            case R.id.btnnext:
                if (isUserValidated()) {
                    if (Utils.isConnected(this)) {
                        setUserData();
                    } else {
                        Toast.makeText(RegisterProfile.this, R.string.nointernet, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.llForCountryCode:

                Intent intent = new Intent(mContext, SelectCountry.class);
                startActivityForResult(intent, 1111);
                break;

            case R.id.dob:
                showDialog(0);
                break;

            case R.id.back:
                startActivity(new Intent(RegisterProfile.this, Login.class));
                break;

            case R.id.tvlocation:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isLocationGranted()) {
                        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            displayLocationSettingsRequest(this);
                        } else {
                            if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
                                return;

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            try {
                                startActivityForResult(builder.build(activity), AllConstants.LOCATION_PICKER);
                            } catch (GooglePlayServicesRepairableException e) {

                            } catch (GooglePlayServicesNotAvailableException e) {

                            }
                        }

                    } else {
                        checkPermissions();
                    }
                } else {
                    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        displayLocationSettingsRequest(this);
                    } else {
                        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
                            return;

                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        try {
                            startActivityForResult(builder.build(activity), AllConstants.LOCATION_PICKER);
                        } catch (GooglePlayServicesRepairableException e) {

                        } catch (GooglePlayServicesNotAvailableException e) {

                        }
                    }
                }

                break;

            case R.id.locationpicker:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isLocationGranted()) {
                        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            displayLocationSettingsRequest(this);
                        } else {
                            if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
                                return;

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            try {
                                startActivityForResult(builder.build(activity), AllConstants.LOCATION_PICKER);
                            } catch (GooglePlayServicesRepairableException e) {

                            } catch (GooglePlayServicesNotAvailableException e) {

                            }
                        }

                    } else {
                        checkPermissions();
                    }
                } else {
                    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        displayLocationSettingsRequest(this);
                    } else {
                        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
                            return;

                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        try {
                            startActivityForResult(builder.build(activity), AllConstants.LOCATION_PICKER);
                        } catch (GooglePlayServicesRepairableException e) {

                        } catch (GooglePlayServicesNotAvailableException e) {

                        }
                    }
                }
                break;

            case R.id.imgChooseOpt:
                if (isCameraGranted()) {
                    chooseImageOptions();
                } else {
                    checkPermissions();
                }
                break;

            default:
                break;
        }
    }

    private void setUserData() {
        mUser = new User();
        String outputDateStr = null;
        mUser.setReg_type("1");
        if (AllConstants.isStudentLoggedIn) {
            mUser.setUsertype("2");
        } else {
            mUser.setUsertype("3");
        }
        if (AllConstants.isFacebook) {
            mUser.setReg_type("2");
        }
        DateFormat inputFormat = new SimpleDateFormat("dd - MM - yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputDateStr = dob;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mUser.setProfile_pic(imgBase64);
        mUser.setFirstname(firstname);
        mUser.setLastname(lastname);
        mUser.setEmail_id(email);
        mUser.setPassword(password);
        mUser.setCountry_code(countryCode);
        mUser.setMobile_no(mobile);
        mUser.setDob(outputDateStr);
        mUser.setGender(gender);
        mUser.setLatitude(lat.toString());
        mUser.setLongitude(lon.toString());
        mUser.setLocation(location);
        mUser.setSocial_id(socialId);

        callRegisterExpertAPI();
    }

    private void callRegisterExpertAPI() {

        if (Utils.isConnected(this)) {
            String urlLogin = Constants.BASE_URL + Constants.VERIFY_EMAIL_MOBILE;
            VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.EMAIL_ID, email);
                jsonObject.put(Constants.MOBILE_NO, mobile);
                webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.VERIFY_EMAIL_MOBILE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(RegisterProfile.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
        }
    }

    private void callOTPGenerateAPI(String mobile) {

        String urlLogin = Constants.BASE_URL + Constants.SEND_OTP;
        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MOBILE_NO, mobile);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.SEND_OTP);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserValidated() {

        EmailValidator emailValidator = new EmailValidator();
        firstname = mEtFirstName.getText().toString().trim();
        lastname = mEtLastName.getText().toString().trim();
        email = mEtEmail.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        mobile = mEtMobile.getText().toString().trim();
        dob = mEtDob.getText().toString().trim();
        countryCode = mTVCountryCode.getText().toString().trim();
        location = mTvLocation.getText().toString().trim();
        int selectedId = radioGenderGrp.getCheckedRadioButtonId();
        if (selectedId == -1) {
            isGender = false;
            idx = -1;
        } else {
            View radioButton = radioGenderGrp.findViewById(selectedId);
            idx = radioGenderGrp.indexOfChild(radioButton);
            if (idx == 1) {
                gender = "female";
            }
        }

        if (!(firstname.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.firstnameerror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(lastname.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.lastnameerror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(email.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.emailerror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!emailValidator.validate(email)) {
            Toast.makeText(RegisterProfile.this, R.string.validemail, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(password.length() >= 6)) {
            Toast.makeText(RegisterProfile.this, R.string.passwordminimum, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(countryCode.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.countrycodeerror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(mobile.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.mobileerror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(mobile.length() >= 9)) {
            Toast.makeText(RegisterProfile.this, R.string.mobilemin, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(dob.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.doberror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (idx == -1) {
            Toast.makeText(RegisterProfile.this, R.string.gendererror, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(location.length() > 0)) {
            Toast.makeText(RegisterProfile.this, R.string.locationerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void chooseImageOptions() {
        final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel(Optional Field)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterProfile.this);
        builder.setTitle("Add Profile Pic");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openPhotosFromGallery();

                } else if (options[item].equals("Cancel(Optional Field)")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void getCurrentDate() {

        Calendar cal = Calendar.getInstance();
        currentDay = cal.get(Calendar.DAY_OF_MONTH);
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentYear=currentYear-12;
        currentDate = currentDay + " - " + (currentMonth + 1) + " - "
                + currentYear;
        currentDateObject = cal.getTime();

    }


    private void openCamera() {

        try {
            // use standard intent to capture an image
            Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent,AllConstants.CAMERA);
        } catch (ActivityNotFoundException anfe) {

        }
    }


    private void openPhotosFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, AllConstants.GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case AllConstants.CAMERA:
                getProfilePic(resultCode, requestCode, data);
                break;

            case AllConstants.COUNTRY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String countryCode = data.getStringExtra("clCode");
                    mTVCountryCode.setText(countryCode);
                }
                break;


            case AllConstants.GALLERY:
                getProfilePic(resultCode, requestCode, data);
                break;

            case AllConstants.LOCATION_PICKER:

                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    lat = place.getLatLng().latitude;
                    lon = place.getLatLng().longitude;
                    try {
                        getSelectedLocation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void getProfilePic(int resultCode, int requestCode, Intent data) {
        if (requestCode == AllConstants.CAMERA || requestCode == AllConstants.GALLERY) {

            switch (requestCode) {
                case AllConstants.CAMERA:
                    if (data != null) {

                        Bundle mData = data.getExtras();
                        final Uri i = data.getData();
                        Bitmap mImageBitmap = null;
                        if (mData != null) {
                            mImageBitmap = (Bitmap) data.getExtras().get("data");
                            Bundle extras = data.getExtras();
                            if (mImageBitmap != null) {
                                mImageBitmap = (Bitmap) extras.get("data");
                                mImageBitmap = Bitmap.createScaledBitmap(mImageBitmap, 300, 300, true);
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                mImgProfilePic.setImageBitmap(mImageBitmap);
                                AllConstants.isProfilePicSet = true;
                                imgBase64 = Utils.encodeTobase64(mImageBitmap);

                            } else {
                                Toast.makeText(RegisterProfile.this, R.string.photoerror, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    break;

                case AllConstants.GALLERY:
                    try {
                        if (data != null) {
                            final Uri imageUri = data.getData();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                                Bitmap selected = BitmapFactory.decodeStream(imageStream);

                                if (selected.getWidth() > 380) {
                                    selected = Bitmap.createScaledBitmap(selected, 380, 380, true);
                                }
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                selected.compress(Bitmap.CompressFormat.PNG, 100, out);
                                mImgProfilePic.setImageBitmap(selected);
                                AllConstants.isProfilePicSet = true;
                                imgBase64 = Utils.encodeTobase64(selected);

                            } else {
//                            performCrop(imageUri);
                                final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                                Bitmap selected = BitmapFactory.decodeStream(imageStream);
                                if (selected.getWidth() > 380) {
                                    selected = Bitmap.createScaledBitmap(selected, 380, 380, true);
                                }
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                selected.compress(Bitmap.CompressFormat.PNG, 100, out);
                                mImgProfilePic.setImageBitmap(selected);
                                AllConstants.isProfilePicSet = true;
                                imgBase64 = Utils.encodeTobase64(selected);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isLocationGranted() {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasLocationPermission2 = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if ((hasLocationPermission != PackageManager.PERMISSION_GRANTED || hasLocationPermission2 != PackageManager.PERMISSION_GRANTED)) {

            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        return flag;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isCameraGranted() {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasExternalPermission = checkSelfPermission(Manifest.permission.CAMERA);
            if ((hasCameraPermission != PackageManager.PERMISSION_GRANTED || hasExternalPermission != PackageManager.PERMISSION_GRANTED)) {

            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        return flag;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasWriteStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasLocationPermission2 = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if ((hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED || hasCameraPermission != PackageManager.PERMISSION_GRANTED || hasLocationPermission != PackageManager.PERMISSION_GRANTED || hasLocationPermission2 != PackageManager.PERMISSION_GRANTED)) {
                requestForPermission();
            } else {
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    displayLocationSettingsRequest(this);
                }
            }

        } else {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                displayLocationSettingsRequest(this);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestForPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access Your Location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Access Your Maps");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Access Your External Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Access Your Camera");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                String message = "Need permissions " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(RegisterProfile.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("Display", "All location settings are satisfied.");


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("Display", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {

                            status.startResolutionForResult(RegisterProfile.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("Display", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("Display", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void getSelectedLocation() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(lat, lon, 1);
        String city = addresses.get(0).getLocality();
        mTvLocation.setText("    " + city);


    }

    @Override
    public void onSuccess(JSONObject serverResult, String requestTag, int mStatusCode) {
        if (serverResult != null) {
            switch (requestTag) {
                case Constants.SEND_OTP:
                    parseGenerateOTPResponse(serverResult);
                    break;

                case Constants.VERIFY_OTP:
                    parseVerifyOTPRespons(serverResult);
                    break;

                case Constants.VERIFY_EMAIL_MOBILE:
                    parseUserProfileResponse(serverResult);
                    break;

            }

        }
    }

    private void parseUserProfileResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        GenerateOTPStatus mOtpStatus = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mOtpStatus.isStatus()) {
            callOTPGenerateAPI(mobile);
        } else {
            Toast.makeText(RegisterProfile.this, mOtpStatus.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseVerifyOTPRespons(JSONObject serverResult) {
        Gson gson = new Gson();
        GenerateOTPStatus mOtpStatus = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mOtpStatus.isStatus()) {
            if (AllConstants.isStudentLoggedIn) {
                Intent intent = new Intent(RegisterProfile.this, RegisterCoaching.class);
                intent.putExtra(Constants.USER_DATA, mUser);
                startActivity(intent);
                alertDialog.dismiss();
            } else {
                Intent intent = new Intent(RegisterProfile.this, RegisterExpertDetails.class);
                intent.putExtra(Constants.USER_DATA, mUser);
                startActivity(intent);
                alertDialog.dismiss();
            }
        } else {
            Toast.makeText(RegisterProfile.this, R.string.otperror, Toast.LENGTH_SHORT).show();
        }
    }

    private void parseGenerateOTPResponse(JSONObject serverResult) {
        Gson gson = new Gson();
        GenerateOTPStatus mOtpStatus = gson.fromJson(serverResult.toString(), GenerateOTPStatus.class);
        if (mOtpStatus.isStatus()) {
            String otp = Integer.toString(mOtpStatus.getOtp());
            Toast.makeText(RegisterProfile.this, otp, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.otp_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView mTvEnterOTP = (TextView) dialogView.findViewById(R.id.tv_otpenter);
            ImageView mImgCancel = (ImageView) dialogView.findViewById(R.id.imgcancel);
            mEtOtp = (EditText) dialogView.findViewById(R.id.et_otp);
            Button mSubmit = (Button) dialogView.findViewById(R.id.btnsubmit);
            mTvEnterOTP.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Medium.otf"));
            mSubmit.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            alertDialog = dialogBuilder.create();
            alertDialog.show();

            mImgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String otp = mEtOtp.getText().toString().trim();
                    if (otp.length() > 0) {
                        if (Utils.isConnected(mContext)) {
                            callOTPVerifyAPI(otp);
                        } else {
                            Toast.makeText(RegisterProfile.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterProfile.this, R.string.enterotp, Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } else {
            Toast.makeText(RegisterProfile.this, R.string.error_went, Toast.LENGTH_SHORT).show();
        }
    }

    private void callOTPVerifyAPI(String otp) {
        String urlLogin = Constants.BASE_URL + Constants.VERIFY_OTP;
        int otpVerify = Integer.parseInt(otp);

        VolleyWebserviceCall webserviceCall = new VolleyWebserviceCall();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.MOBILE_NO, mobile);
            jsonObject.put(Constants.OTP, otpVerify);
            webserviceCall.volleyPostCallJsonRequest(mContext, urlLogin, jsonObject, this, true, Constants.VERIFY_OTP);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onError(JSONObject serverResult, String requestTag, int mStatusCode) {
        Toast.makeText(RegisterProfile.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

}


