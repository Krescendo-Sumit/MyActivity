package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CCFApiInterface {

    @POST("master/registerComplaint")
    Call<CCFBaseApiResponse> complaintResponse(@Body JsonObject jsonObject);

    @GET("master/CategoryMaster")
    Call<CCFCategoryPojoModel> getCategories();

    /*@FormUrlEncoded*/
    @POST("master/GetLotDetails")
    Call<CCFLotPojoModel> getLotNumberDetails(/*@Field("LotNo") String lotNumber*/@Body JsonObject jsonObject);

    @GET("master/StateMaster")
    Call<CCFStatePojoModel> getStateList();

    @POST("master/GetDistrictName")
    Call<CCFDistrictPojoModel> getDistrictList(@Body JsonObject jsonObject);

    @POST("master/GetTalukaName")
    Call<CCFTalukaPojoModel> getTalukaList(@Body JsonObject jsonObject);

    @POST("master/GetDepoDetails")
    Call<CCFDepotPojoModel> getDepot(@Body JsonObject jsonObject);

    @POST("master/ViewMyComplaints")
    Call<CCFViewCmplntPojoModel> getViewComplaint(/*@Query("tbmCode") String tbmCode*/@Body JsonObject jsonObject);

    @POST("master/PendingComplaint")
    Call<CCFPendingCmplntPojoMOdel> getPendingComplaint(@Body JsonObject jsonObject);

    @POST("master/ForwardByRBM")
    Call<CCFBaseApiResponse> getSubmitPendingComplaint(@Body JsonObject jsonObject);
}
