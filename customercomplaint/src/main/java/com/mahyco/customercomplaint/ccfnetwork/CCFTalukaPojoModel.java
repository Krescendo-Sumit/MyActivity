package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFTalukaPojoModel {

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

    @SerializedName("TalukaName")
    private CCFTalukaList ccfTalukaList;

    public CCFTalukaList getTalukaList() {
        return ccfTalukaList;
    }

    public void setDepotList(CCFTalukaList ccfTalukaList) {
        this.ccfTalukaList = ccfTalukaList;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    /*@SerializedName("talukaList")
    ArrayList<CCFTalukaListData> talukaList = new ArrayList<>();

    public ArrayList<CCFTalukaListData> getTalukaList() {
        return talukaList;
    }

    public void setTalukaList(ArrayList<CCFTalukaListData> stateList) {
        this.talukaList = talukaList;
    }*/
}
