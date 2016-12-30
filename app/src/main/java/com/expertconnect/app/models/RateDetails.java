package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chinar on 17/11/16.
 */
public class RateDetails implements Serializable {

    @SerializedName("level")
    private String level;

    @SerializedName("rate")
    private String rate;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}
