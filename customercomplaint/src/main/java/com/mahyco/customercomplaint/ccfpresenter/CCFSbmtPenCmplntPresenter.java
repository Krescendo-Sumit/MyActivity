package com.mahyco.customercomplaint.ccfpresenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFSbmtCmplntInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFSbmtCmplntModel;

import retrofit2.Call;
import retrofit2.Response;

public class CCFSbmtPenCmplntPresenter implements CCFSbmtCmplntInterface.ResponseFromServer {

    private CCFCommonViewInterface commonViewInterface;
    private final CCFSbmtCmplntModel ccfSbmtCmplntModel;

    public CCFSbmtPenCmplntPresenter(CCFCommonViewInterface commonViewInterface, CCFSbmtCmplntModel ccfSbmtCmplntModel) {
        this.commonViewInterface = commonViewInterface;
        this.ccfSbmtCmplntModel = ccfSbmtCmplntModel;
    }

    @Override
    public void onSuccess(Call<?> call, Response<?> response) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.successResponse(response);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.failResponse(t);
        }
    }

    public void callApi(JsonObject jsonObject,Context context) {
        if (commonViewInterface != null) {
            commonViewInterface.showDialog();
            ccfSbmtCmplntModel.callApi(this,jsonObject, context);
        }
    }

    public void destroy() {
        commonViewInterface = null;
    }
}
