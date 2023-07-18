package com.mahyco.customercomplaint.ccfmodel;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotNumberInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;

public class CCFLotNumberModel implements CCFLotNumberInterface {

    private static CCFLotNumberModel mLotNumberModel;

    public static synchronized CCFLotNumberModel getInstance() {

        if (mLotNumberModel == null) {
            mLotNumberModel = new CCFLotNumberModel();
        }

        return mLotNumberModel;
    }

    @Override
    public void callLotApi(ResponseFromServer responseFromServer, JsonObject jsonObject, Context context) {
        CCFApiClass.getLotNumberResponse(responseFromServer, jsonObject, context);
    }
}
