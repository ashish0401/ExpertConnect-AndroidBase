package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MainCategoryResponse {

    @SerializedName("status")
    private Boolean status;

    @SerializedName("categories")
    private List<Category> categories = new ArrayList<Category>();

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }


}
