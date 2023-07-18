package com.mahyco.customercomplaint.ccfinterfaces;

import android.content.Context;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public interface CCFRegstComplntInterface {

    interface ResponseFromServer {
        void onModelInterfaceSuccess(Call<?> call, Response<?> response);

        void onModelInterfaceFailure(Throwable t);
    }

    void callApi(CCFRegstComplntInterface.ResponseFromServer responseFromServer, JsonObject jsonObject, Context Context);
}
