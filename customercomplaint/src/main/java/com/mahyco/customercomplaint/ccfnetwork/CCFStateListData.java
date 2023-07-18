package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFStateListData {

    @SerializedName("state_name")
    String state_name;


    public CCFStateListData() {
    }

    public CCFStateListData(String state_name) {
        this.state_name = state_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }


    public String toString() {
        return state_name;
    }
}
