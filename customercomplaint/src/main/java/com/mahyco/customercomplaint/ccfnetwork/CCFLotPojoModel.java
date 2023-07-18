package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFLotPojoModel extends CCFBaseApiResponse {

    @SerializedName("returnval")
    private CCFLotList lotResponseList;

    public CCFLotList getLotResponseList() {
        return lotResponseList;
    }

    public void setLotResponseList(CCFLotList lotResponseList) {
        this.lotResponseList = lotResponseList;
    }
}
