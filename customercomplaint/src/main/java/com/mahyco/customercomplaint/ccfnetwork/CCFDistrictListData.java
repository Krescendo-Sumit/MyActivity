package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFDistrictListData {

    @SerializedName("District_name")
    String District_name;

    public CCFDistrictListData() {
    }

    public CCFDistrictListData(String District_name) {
        this.District_name = District_name;
    }

    public String getDistrict_name() {
        return District_name;
    }

    public void setDistrict_name(String district_name) {
        District_name = district_name;
    }

    public String toString() {
        return District_name;
    }
}
