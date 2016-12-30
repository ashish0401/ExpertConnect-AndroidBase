package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinar on 15/11/16.
 */
public class TeacherListResponse implements Serializable {

    @SerializedName("status")
    private boolean status;


    @SerializedName("next_offset")
    private int next_offset;



    @SerializedName("total_count")
    private int total_count;



    @SerializedName("message")
    private String message;

    @SerializedName("categories")
    private List<TeacherCategory> categories = new ArrayList<TeacherCategory>();

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getNext_offset() {
        return next_offset;
    }

    public void setNext_offset(int next_offset) {
        this.next_offset = next_offset;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TeacherCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<TeacherCategory> categories) {
        this.categories = categories;
    }


}
