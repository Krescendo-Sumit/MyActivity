package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFBaseApiResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("Message")
    private String Message;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
