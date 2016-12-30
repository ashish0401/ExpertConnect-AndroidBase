package com.expertconnect.app.select_country;

import android.content.Context;


public class CountryModel {
    private String mName;
    private String mCountryISO;
    private int mCountryCode;
    private String mCountryCodeStr;
    private int mPriority;
    private int mResId;
    private int mNum;
    private String mNativeName;
    private String id;

    public CountryModel() {
    }

    public CountryModel(Context context, String str, int num) {
        String[] data = str.split(",");
        mNum = num;
        mName = data[0];
        mCountryISO = data[1];
        mCountryCode = Integer.parseInt(data[2]);
        mCountryCodeStr = "+" + data[2];
        if (data.length > 3) {
            mPriority = Integer.parseInt(data[3]);
        }
        String fileName = String.format("f%03d", num);
        mResId = context.getApplicationContext().getResources().getIdentifier(fileName, "drawable", context.getApplicationContext().getPackageName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCountryCodeStr() {
        return mCountryCodeStr;
    }

    public void setCountryCodeStr(String mCountryCodeStr) {
        this.mCountryCodeStr = mCountryCodeStr;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int mResId) {
        this.mResId = mResId;
    }

}
