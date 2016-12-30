package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chinar on 15/11/16.
 */
public class CoachingDetails implements Serializable {

    @SerializedName("teacher_coaching_id")
    private String teacher_coaching_id;

    @SerializedName("venue")
    private String venue;

    public String getTeacher_coaching_id() {
        return teacher_coaching_id;
    }

    public void setTeacher_coaching_id(String teacher_coaching_id) {
        this.teacher_coaching_id = teacher_coaching_id;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


}
