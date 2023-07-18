package com.mahyco.customercomplaint.ccfpresenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotNumberInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFViewCmplntInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFLotNumberModel;
import com.mahyco.customercomplaint.ccfmodel.CCFViewCmplntModel;

import retrofit2.Call;
import retrofit2.Response;

public class CCFViewCmplntPresenter implements CCFViewCmplntInterface.ResponseFromServer {

    private CCFCommonViewInterface commonViewInterface;
    private final CCFViewCmplntModel mModel;

    public CCFViewCmplntPresenter(CCFCommonViewInterface commonViewInterface, CCFViewCmplntModel mModel) {
        this.commonViewInterface = commonViewInterface;
        this.mModel = mModel;
    }

    @Override
    public void onLotModelInterfaceSuccess(Call<?> call, Response<?> response) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.successResponse(response);
        }
    }

    @Override
    public void onLotModelInterfaceFailure(Throwable t) {
        if (commonViewInterface != null) {
            commonViewInterface.hideDialog();
            commonViewInterface.failResponse(t);
        }
    }

    public void callViewCmplntApi(/*String tbmCode*/JsonObject jsonObject, Context context) {
        if (commonViewInterface != null) {
            commonViewInterface.showDialog();
            mModel.callViewCmplntApi(this, /*tbmCode*/jsonObject, context);
        }
    }

    public void callPendingCmplntApi(/*String rbmCode*/JsonObject jsonObject, Context context) {
        if (commonViewInterface != null) {
            commonViewInterface.showDialog();
            mModel.callPendingCmplntApi(this, /*rbmCode*/jsonObject, context);
        }
    }

    public void destroy() {
        commonViewInterface = null;
    }
}
