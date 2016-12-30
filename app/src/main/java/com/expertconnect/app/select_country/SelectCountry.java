package com.expertconnect.app.select_country;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.expertconnect.app.R;
import com.expertconnect.app.activities.RegisterProfile;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


public class SelectCountry extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView mlvCountryList;
    private FrameLayout mflDefaultCountry;
    private ArrayList<CountryModel> mCountryList;
    private EditText mEtSearch;
    private CountryListAdapter mCountryListAdapter;
    private Button mBtnClose;
    private Toolbar toolbar;
    private TextView tvfortooltitle;
    private ImageView backs;

    /* Calls by OS when activity launches */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);

        initUi();
        setListener();
        setToolbar();
        getCountryList();
    }

    /* Set click listeners */

    private void setListener() {
        mflDefaultCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

      /* Set toolbar */

    private void setToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolBar); // Attaching the layout to the toolbar object
//        setSupportActionBar(toolbar);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setText(getResources().getString(R.string.select_country));
    }

    /*  Initialize all components  */

    private void initUi() {
        mflDefaultCountry = (FrameLayout) findViewById(R.id.flDefaultCountry);
        mlvCountryList = (ListView) findViewById(R.id.lvCountryList);
        mlvCountryList.setOnItemClickListener(this);
        mEtSearch = (EditText) findViewById(R.id.etForSearch);
        mBtnClose = (Button) findViewById(R.id.btnForClose);
        tvfortooltitle = (TextView) findViewById(R.id.tvfortooltitle);
        tvfortooltitle.setText("Select Country");


        backs = (ImageView) findViewById(R.id.backs);

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();


            }
        });


        mEtSearch.setCursorVisible(false);
        mEtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEtSearch.setCursorVisible(true);
                return false;
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCountryListAdapter != null) {
                    String text = mEtSearch.getText().toString()
                            .toLowerCase(Locale.getDefault());
                    mCountryListAdapter.filter(text);
                    mEtSearch.setCursorVisible(false);
                }
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtSearch.length() > 0) {
                    mEtSearch.setText("");
                    mCountryListAdapter = new CountryListAdapter(SelectCountry.this, mCountryList);
                    mlvCountryList.setAdapter(mCountryListAdapter);
                    mEtSearch.setCursorVisible(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*  select country on item click  */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Utils.hideKeyBoard(SelectCountry.this);
        CountryModel obj = mCountryList.get(position);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("clCode", "" + obj.getCountryCodeStr());
        returnIntent.putExtra("clFlagId", obj.getResId());
        returnIntent.putExtra("ids", obj.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /*  get Country list flags and country codes */
    private void getCountryList() {
        mCountryList = new ArrayList<CountryModel>();
        String countryJson = loadJSONFromAsset();
        parseJSON(countryJson);
        mCountryListAdapter = new CountryListAdapter(SelectCountry.this, mCountryList);
        mlvCountryList.setAdapter(mCountryListAdapter);
    }

    /*  Fetch flags and country code from saved json */

    private void parseJSON(String countryJson) {
        try {
            JSONObject jsonObject = new JSONObject(countryJson);
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.COUNTRIES);

            CountryModel countryModel;
            for (int i = 0; i < jsonArray.length(); i++) {
                countryModel = new CountryModel();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String countryName = jsonObject1.getString(Constants.COUNTRY_NAMS);
                String countryCode = jsonObject1.getString(Constants.COUNTRY_CODE_1);
                String fileName = jsonObject1.getString(Constants.COUNTRY_FLAG);
                String id = jsonObject1.getString("id");
                countryModel.setId(id);
                countryModel.setCountryCodeStr("+" + countryCode);
                countryModel.setName(countryName);
                int mResId = getApplicationContext().getResources().getIdentifier(fileName, "drawable", getApplicationContext().getPackageName());
                countryModel.setResId(mResId);


                mCountryList.add(countryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*  Read all flags data from json saved in assets  */

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("country_lists.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
