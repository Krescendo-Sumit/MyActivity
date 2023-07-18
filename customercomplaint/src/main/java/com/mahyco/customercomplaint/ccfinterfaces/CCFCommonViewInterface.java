package com.mahyco.customercomplaint.ccfinterfaces;

import retrofit2.Response;

public interface CCFCommonViewInterface {
    void showDialog();

    void hideDialog();

    void successResponse(Response<?> response);

    void failResponse(Throwable t);

}
