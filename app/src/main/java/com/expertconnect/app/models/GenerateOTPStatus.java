package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chinar on 22/10/16.
 */
public class GenerateOTPStatus {
    @SerializedName("status")
    private boolean status;

    @SerializedName("otp")
    private int otp;

    @SerializedName("message")
    private String message;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
