package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFStateList {

    @SerializedName("Table")
    private ArrayList<CCFStateListData> Table =  new ArrayList<>();

    public ArrayList<CCFStateListData> getTable() {
        return Table;
    }

    public void setTableList(ArrayList<CCFStateListData> Table) {
        this.Table = Table;
    }
}
