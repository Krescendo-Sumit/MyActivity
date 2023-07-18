package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFDepotPojoModel {

    @SerializedName("success")
    boolean success = false;

    @SerializedName("Message")
    private String Message = "";

    @SerializedName("returnval")
    private CCFDepotList ccfDepotList;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public CCFDepotList getDepotList() {
        return ccfDepotList;
    }

    public void setDepotList(CCFDepotList ccfDepotList) {
        this.ccfDepotList = ccfDepotList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
