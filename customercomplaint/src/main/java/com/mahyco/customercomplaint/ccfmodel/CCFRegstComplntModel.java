package com.mahyco.customercomplaint.ccfmodel;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFRegstComplntInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;

import java.util.List;

public class CCFRegstComplntModel implements CCFRegstComplntInterface {

    private static CCFRegstComplntModel ccfRegstComplntModel;

    public static synchronized CCFRegstComplntModel getInstance() {

        if (ccfRegstComplntModel == null) {
            ccfRegstComplntModel = new CCFRegstComplntModel();
        }
        return ccfRegstComplntModel;
    }

    @Override
    public void callApi(ResponseFromServer responseFromServer, JsonObject jsonObject, Context context) {
        CCFApiClass.getRegstComplaintResponse(responseFromServer, jsonObject, context);
    }
}