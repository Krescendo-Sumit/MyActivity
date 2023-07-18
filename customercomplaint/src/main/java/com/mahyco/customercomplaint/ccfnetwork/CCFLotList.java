package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CCFLotList {

    @SerializedName("Table")
    private List<CCFLotData> lotDataList = new ArrayList<>();

    public List<CCFLotData> getLotDataList() {
        return lotDataList;
    }

    public void setLotDataList(List<CCFLotData> lotDataList) {
        this.lotDataList = lotDataList;
    }
}
