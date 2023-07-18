package com.mahyco.customercomplaint.ccfinterfaces;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Response;

public interface CCFCategoryInterface {
    interface ResponseFromServer {
        void onModelInterfaceSuccess(Call<?> call, Response<?> response);

        void onModelInterfaceFailure(Throwable t);
    }

    void callApi(CCFCategoryInterface.ResponseFromServer responseFromServer, Context context);
}
