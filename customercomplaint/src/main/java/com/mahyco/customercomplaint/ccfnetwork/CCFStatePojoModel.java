package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFStatePojoModel {

    @SerializedName("success")
    boolean success  = false;

    @SerializedName("Message")
    private String Message = "";

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }

    @SerializedName("StateList")
    private CCFStateList ccfStateList;

    public CCFStateList getStateList() {
        return ccfStateList;
    }

    public void setStateList(CCFStateList ccfStateList) {
        this.ccfStateList = ccfStateList;
    }


    /*@SerializedName("stateList")
    ArrayList<CCFStateListData> stateList =  new ArrayList<>();

    public ArrayList<CCFStateListData> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<CCFStateListData> stateList) {
        this.stateList = stateList;
    }*/
}
