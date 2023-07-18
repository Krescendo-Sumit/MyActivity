package com.mahyco.customercomplaint.ccfmodel;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCategoryInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFSbmtCmplntInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;

public class CCFSbmtCmplntModel implements CCFSbmtCmplntInterface {

    private static CCFSbmtCmplntModel mCcfSbmtCmplntModel;

    public static synchronized CCFSbmtCmplntModel getInstance() {

        if (mCcfSbmtCmplntModel == null) {
            mCcfSbmtCmplntModel = new CCFSbmtCmplntModel();
        }

        return mCcfSbmtCmplntModel;
    }


    @Override
    public void callApi(ResponseFromServer responseFromServer, JsonObject jsonObject, Context context) {
        CCFApiClass.getSubmitPendingCmplntResponse(responseFromServer,jsonObject,  context);
    }
}

