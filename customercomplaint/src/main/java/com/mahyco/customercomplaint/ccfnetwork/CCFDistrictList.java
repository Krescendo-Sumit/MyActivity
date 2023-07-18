package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFDistrictList {

    @SerializedName("Table")
    private ArrayList<CCFDistrictListData> Table =  new ArrayList<>();

    public ArrayList<CCFDistrictListData> getTable() {
        return Table;
    }

    public void setTable(ArrayList<CCFDistrictListData> table) {
        Table = table;
    }
}
