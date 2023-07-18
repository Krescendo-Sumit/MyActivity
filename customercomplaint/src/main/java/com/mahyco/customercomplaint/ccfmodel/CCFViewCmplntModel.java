package com.mahyco.customercomplaint.ccfmodel;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFViewCmplntInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;

public class CCFViewCmplntModel implements CCFViewCmplntInterface {

    private static CCFViewCmplntModel mViewCmplntModel;

    public static synchronized CCFViewCmplntModel getInstance() {

        if (mViewCmplntModel == null) {
            mViewCmplntModel = new CCFViewCmplntModel();
        }
        return mViewCmplntModel;
    }

    @Override
    public void callViewCmplntApi(ResponseFromServer responseFromServer, /*String jsonObject*/JsonObject jsonObject, Context context) {
        CCFApiClass.getViewCmplntResponse(responseFromServer, jsonObject, context);
    }

    @Override
    public void callPendingCmplntApi(ResponseFromServer responseFromServer, JsonObject jsonObject, Context context) {
        CCFApiClass.getPendingCmplntResponse(responseFromServer, jsonObject, context);
    }
}
