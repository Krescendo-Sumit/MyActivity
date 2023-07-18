package com.mahyco.customercomplaint.ccfinterfaces;


import android.content.Context;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public interface CCFLotNumberInterface {
    interface ResponseFromServer {
        void onLotModelInterfaceSuccess(Call<?> call, Response<?> response);

        void onLotModelInterfaceFailure(Throwable t);
    }

    void callLotApi(CCFLotNumberInterface.ResponseFromServer responseFromServer, JsonObject jsonObject, Context context );
}
