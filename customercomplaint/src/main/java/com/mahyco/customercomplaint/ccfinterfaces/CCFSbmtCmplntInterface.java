package com.mahyco.customercomplaint.ccfinterfaces;

import android.content.Context;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public interface CCFSbmtCmplntInterface {

    interface ResponseFromServer {
        void onSuccess(Call<?> call, Response<?> response);

        void onFailure(Throwable t);
    }

    void callApi(CCFSbmtCmplntInterface.ResponseFromServer responseFromServer, JsonObject jsonObject, Context context);
}
