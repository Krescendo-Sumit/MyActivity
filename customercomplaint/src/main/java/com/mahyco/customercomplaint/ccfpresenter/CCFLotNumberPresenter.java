package com.mahyco.customercomplaint.ccfpresenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotNumberInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFLotNumberModel;

import retrofit2.Call;
import retrofit2.Response;

public class CCFLotNumberPresenter implements CCFLotNumberInterface.ResponseFromServer {

    private CCFLotViewInterface commonViewInterface;
    private final CCFLotNumberModel tGenericPurityModel;

    public CCFLotNumberPresenter(CCFLotViewInterface commonViewInterface, CCFLotNumberModel tGenericPurityModel) {
        this.commonViewInterface = commonViewInterface;
        this.tGenericPurityModel = tGenericPurityModel;
    }

    @Override
    public void onLotModelInterfaceSuccess(Call<?> call, Response<?> response) {
        if (commonViewInterface != null) {
            commonViewInterface.hideLotDialog();
            commonViewInterface.successLotResponse(response);
        }
    }

    @Override
    public void onLotModelInterfaceFailure(Throwable t) {
        if (commonViewInterface != null) {
            commonViewInterface.hideLotDialog();
            commonViewInterface.failLotResponse(t);
        }
    }

   /* public void callApi(String lotNumber) {
        if (commonViewInterface != null) {
            commonViewInterface.showLotDialog();
            tGenericPurityModel.callLotApi(this, lotNumber);
        }
    }
*/
    public void callApi(JsonObject jsonObject, Context context ) {
        if (commonViewInterface != null) {
            commonViewInterface.showLotDialog();
            tGenericPurityModel.callLotApi(this, jsonObject, context);
        }
    }

    public void destroy() {
        commonViewInterface = null;
    }
}
