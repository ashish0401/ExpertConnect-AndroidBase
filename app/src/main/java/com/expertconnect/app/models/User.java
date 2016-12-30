package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chinar on 6/12/16.
 */

public class User implements Serializable {

    @SerializedName("usertype")
    private String usertype;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("email_id")
    private String email_id;

    @SerializedName("password")
    private String password;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("profile_pic")
    private String profile_pic;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("location")
    private String location;

    @SerializedName("reg_type")
    private String reg_type;

    @SerializedName("social_id")
    private String social_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("sub_category_id")
    private String sub_category_id;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("about")
    private String about;

    @SerializedName("base_price")
    private String base_price;

    private String[] beginnerArray=new String[3];
    private String[] interArray=new String[3];
    private String[] advanceArray=new String[3];
    private String[] coaching_venue=new String[5];

    public String[] getCoaching_venue() {
        return coaching_venue;
    }

    public void setCoaching_venue(String[] coaching_venue) {
        this.coaching_venue = coaching_venue;
    }



    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
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

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String[] getBeginnerArray() {
        return beginnerArray;
    }

    public void setBeginnerArray(String[] beginnerArray) {
        this.beginnerArray = beginnerArray;
    }

    public String[] getInterArray() {
        return interArray;
    }

    public void setInterArray(String[] interArray) {
        this.interArray = interArray;
    }

    public String[] getAdvanceArray() {
        return advanceArray;
    }

    public void setAdvanceArray(String[] advanceArray) {
        this.advanceArray = advanceArray;
    }


}
