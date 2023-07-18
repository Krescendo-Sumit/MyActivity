package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CCFCategoryList {

    @SerializedName("Table1")
    private List<CCFMainCategoryList> mainCategoryLists = new ArrayList<>();

    @SerializedName("Table2")
    private List<CCFSubCategoryList> subCategoryLists = new ArrayList<>();

    public List<CCFMainCategoryList> getMainCategoryLists() {
        return mainCategoryLists;
    }

    public void setMainCategoryLists(List<CCFMainCategoryList> mainCategoryLists) {
        this.mainCategoryLists = mainCategoryLists;
    }

    public List<CCFSubCategoryList> getSubCategoryLists() {
        return subCategoryLists;
    }

    public void setSubCategoryLists(List<CCFSubCategoryList> subCategoryLists) {
        this.subCategoryLists = subCategoryLists;
    }
}
