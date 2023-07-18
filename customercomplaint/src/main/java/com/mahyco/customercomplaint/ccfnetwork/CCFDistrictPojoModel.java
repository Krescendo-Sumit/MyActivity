package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFDistrictPojoModel {

    @SerializedName("success")
    boolean success = false;

    @SerializedName("Message")
    private String Message = "";

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }

    @SerializedName("DistrictName")
    private CCFDistrictList ccfDistrictList;

    public CCFDistrictList getDistrictList() {
        return ccfDistrictList;
    }

    public void setDistrictList(CCFDistrictList ccfDistrictList) {
        this.ccfDistrictList = ccfDistrictList;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /*@SerializedName("districtList")
    ArrayList<CCFDistrictListData> districtList =  new ArrayList<>();

    public ArrayList<CCFDistrictListData> getDistrictList() {
        return districtList;
    }

    public void setStateList(ArrayList<CCFDistrictListData> districtList) {
        this.districtList = districtList;
    }*/
}
