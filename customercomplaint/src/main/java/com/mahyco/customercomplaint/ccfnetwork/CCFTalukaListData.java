package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFTalukaListData {

    @SerializedName("Taluka_name")
    String Taluka_name;


    public CCFTalukaListData() {
    }

    public CCFTalukaListData(String Taluka_name) {
        this.Taluka_name = Taluka_name;
    }

    public String getTaluka_name() {
        return Taluka_name;
    }

    public void setTaluka_name(String taluka_name) {
        Taluka_name = taluka_name;
    }

    @Override
    public String toString() {
        return Taluka_name;
    }
}
