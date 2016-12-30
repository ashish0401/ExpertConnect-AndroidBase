package com.expertconnect.app.models;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chinar on 9/11/16.
 */
public class BrowseEnquiryResult {

    @SerializedName("status")
    private boolean status;

    @SerializedName("name")
    private String name;

    @SerializedName("sub_category")
    private String sub_category;

    @SerializedName("location")
    private String location;

    @SerializedName("preference")
    private String preference;

    @SerializedName("profile")
    private ImageView profile;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @SerializedName("fee")
    private String fee;



    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public ImageView getProfile() {
        return profile;
    }

    public void setProfile(ImageView profile) {
        this.profile = profile;
    }


}
