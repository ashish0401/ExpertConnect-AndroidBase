package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chinar on 24/10/16.
 */
public class SubCategory implements Serializable {
    @SerializedName("sub_category_id")
    private String sub_category_id;

    @SerializedName("sub_category_name")
    private String sub_category_name;

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }


}
