package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFDepotListData {

    @SerializedName("WERKS")
    String WERKS;

    public CCFDepotListData() {
    }

    public CCFDepotListData(String werks) {
        WERKS = werks;
    }

    public String getDepo() {
        return WERKS;
    }

    public void setDepo(String werks) {
        WERKS = werks;
    }

    public String toString() {
        return WERKS;
    }
}
