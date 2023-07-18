package com.mahyco.customercomplaint.ccfinterfaces;

import retrofit2.Response;

public interface CCFLotViewInterface {
    void showLotDialog();

    void hideLotDialog();

    void successLotResponse(Response<?> response);

    void failLotResponse(Throwable t);
}
