package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFTalukaList {

    @SerializedName("Table")
    private ArrayList<CCFTalukaListData> Table =  new ArrayList<>();

    public ArrayList<CCFTalukaListData> getTable() {
        return Table;
    }

    public void setTableList(ArrayList<CCFTalukaListData> Table) {
        this.Table = Table;
    }
}
