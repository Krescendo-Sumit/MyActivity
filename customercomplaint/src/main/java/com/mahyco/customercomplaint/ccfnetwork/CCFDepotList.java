package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFDepotList {

    @SerializedName("Table")
    private ArrayList<CCFDepotListData> Table =  new ArrayList<>();

    public ArrayList<CCFDepotListData> getTable() {
        return Table;
    }

    public void setTableList(ArrayList<CCFDepotListData> Table) {
        this.Table = Table;
    }
}
