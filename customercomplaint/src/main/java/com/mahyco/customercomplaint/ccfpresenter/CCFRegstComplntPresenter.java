package com.mahyco.customercomplaint.ccfpresenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFRegstComplntInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFRegstComplntModel;


import retrofit2.Call;
import retrofit2.Response;

public class CCFRegstComplntPresenter implements CCFRegstComplntInterface.ResponseFromServer {


    private CCFCommonViewInterface commonViewInterface;
    private final CCFRegstComplntModel ccfRegstComplntModel;

    public CCFRegstComplntPresenter(CCFCommonViewInterface commonViewInterface, CCFRegstComplntModel tGenericPurityModel) {
        this.commonViewInterface = commonViewInterface;
        this.ccfRegstComplntModel = tGenericPurityModel;
    }

    @Override
    public void onModelInterfaceSuccess(Call<?> call, Response<?> response) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.successResponse(response);
        }
    }

    @Override
    public void onModelInterfaceFailure(Throwable t) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.failResponse(t);
        }
    }

    public void callApi(JsonObject jsonObject, Context context) {
        if (commonViewInterface != null) {
            commonViewInterface.showDialog();
            ccfRegstComplntModel.callApi(this, jsonObject, context);
        }
    }

    public void destroy() {
        commonViewInterface = null;
    }
}
