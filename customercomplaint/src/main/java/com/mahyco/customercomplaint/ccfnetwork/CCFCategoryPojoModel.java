package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

public class CCFCategoryPojoModel extends CCFBaseApiResponse {

    @SerializedName("returnval")
    private CCFCategoryList ccfCategoryResponse;

    public CCFCategoryList getCategoryResponseList() {
        return ccfCategoryResponse;
    }

    public void setCategoryResponseList(CCFCategoryList ccfCategoryResponse) {
        this.ccfCategoryResponse = ccfCategoryResponse;
    }
}



