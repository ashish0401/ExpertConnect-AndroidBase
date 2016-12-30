package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinar on 24/10/16.
 */
public class SubCategoryResponse {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("sub_categories")
    private List<SubCategory> sub_categories = new ArrayList<SubCategory>();

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SubCategory> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(List<SubCategory> sub_categories) {
        this.sub_categories = sub_categories;
    }


}
