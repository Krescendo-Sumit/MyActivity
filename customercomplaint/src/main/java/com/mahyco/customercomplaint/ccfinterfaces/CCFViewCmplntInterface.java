package com.mahyco.customercomplaint.ccfinterfaces;

import android.content.Context;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public interface CCFViewCmplntInterface {

    interface ResponseFromServer {
        void onLotModelInterfaceSuccess(Call<?> call, Response<?> response);

        void onLotModelInterfaceFailure(Throwable t);
    }

    void callViewCmplntApi(CCFViewCmplntInterface.ResponseFromServer responseFromServer, /*String tbmCode*/JsonObject jsonObject, Context context);
    void callPendingCmplntApi(CCFViewCmplntInterface.ResponseFromServer responseFromServer, /*String rbmCode*/JsonObject jsonObject, Context context);
}
