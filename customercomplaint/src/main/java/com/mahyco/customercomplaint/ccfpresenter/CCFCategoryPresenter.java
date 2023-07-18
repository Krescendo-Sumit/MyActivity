package com.mahyco.customercomplaint.ccfpresenter;

import android.content.Context;

import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCategoryInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFCategoryModel;

import retrofit2.Call;
import retrofit2.Response;

public class CCFCategoryPresenter implements CCFCategoryInterface.ResponseFromServer {

    private CCFCommonViewInterface commonViewInterface;
    private final CCFCategoryModel ccftCategoryModel;

    public CCFCategoryPresenter(CCFCommonViewInterface commonViewInterface, CCFCategoryModel tGenericPurityModel) {
        this.commonViewInterface = commonViewInterface;
        this.ccftCategoryModel = tGenericPurityModel;
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

    public void callApi(Context context) {
        if (commonViewInterface != null) {
            commonViewInterface.showDialog();
            ccftCategoryModel.callApi(this, context);
        }
    }

    public void destroy() {
        commonViewInterface = null;
    }
}