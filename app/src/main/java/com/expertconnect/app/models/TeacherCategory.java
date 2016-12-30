package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinar on 15/11/16.
 */
public class TeacherCategory implements Serializable {


    @SerializedName("user_id")
    private int user_id;

    @SerializedName("flag")
    private int flag;

    @SerializedName("rate")
    private String rate;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("country_code")
    private String country_code;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("profile_pic")
    private String profile_pic;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("location")
    private String location;

    @SerializedName("coaching_details")
    private ArrayList<CoachingDetails> coaching_details = new ArrayList<CoachingDetails>();

    @SerializedName("rate_details")
    private ArrayList<RateDetails> rate_details = new ArrayList<RateDetails>();

    @SerializedName("expert_id")
    private String expert_id;

    @SerializedName("reg_type")
    private String reg_type;

    @SerializedName("social_id")
    private String social_id;

    @SerializedName("tag")
    private boolean tag;

    @SerializedName("accept_tag")
    private boolean accept_tag;

    @SerializedName("accept_reject_tag")
    private boolean accept_reject_tag;

    @SerializedName("confirm_tag")
    private boolean confirm_tag;

    @SerializedName("confirm_reject_tag")
    private boolean confirm_reject_tag;

    @SerializedName("timestamp")
    private String timestamp;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("sub_category_id")
    private String sub_category_id;

    @SerializedName("sub_category")
    private String sub_category;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    private String type;


    public boolean isAccept_tag() {
        return accept_tag;
    }

    public void setAccept_tag(boolean accept_tag) {
        this.accept_tag = accept_tag;
    }

    public boolean isAccept_reject_tag() {
        return accept_reject_tag;
    }

    public void setAccept_reject_tag(boolean accept_reject_tag) {
        this.accept_reject_tag = accept_reject_tag;
    }

    public boolean isConfirm_tag() {
        return confirm_tag;
    }

    public void setConfirm_tag(boolean confirm_tag) {
        this.confirm_tag = confirm_tag;
    }

    public boolean isConfirm_reject_tag() {
        return confirm_reject_tag;
    }

    public void setConfirm_reject_tag(boolean confirm_reject_tag) {
        this.confirm_reject_tag = confirm_reject_tag;
    }



    public ArrayList<RateDetails> getRate_details() {
        return rate_details;
    }

    public void setRate_details(ArrayList<RateDetails> rate_details) {
        this.rate_details = rate_details;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }

    public String getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(String expert_id) {
        this.expert_id = expert_id;
    }

    public int getTeacher_id() {
        return user_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.user_id = teacher_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<CoachingDetails> getCoaching_details() {
        return coaching_details;
    }

    public void setCoaching_details(ArrayList<CoachingDetails> coaching_details) {
        this.coaching_details = coaching_details;
    }




}
