package com.mahyco.customercomplaint.ccfmodel;

import android.content.Context;

import com.mahyco.customercomplaint.ccfinterfaces.CCFCategoryInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;

public class CCFCategoryModel implements CCFCategoryInterface {

    private static CCFCategoryModel mTCategoryModel;

    public static synchronized CCFCategoryModel getInstance() {

        if (mTCategoryModel == null) {
            mTCategoryModel = new CCFCategoryModel();
        }

        return mTCategoryModel;
    }


    @Override
    public void callApi(ResponseFromServer responseFromServer, Context context) {
        CCFApiClass.getCategoryResponse(responseFromServer, context);
    }
}
