package myactvity.mahyco.retro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import myactvity.mahyco.TBMWiseMdoList;
import myactvity.mahyco.newupload.HDPSPaymentDetailAPI;
import myactvity.mahyco.travelreport.ActivityTravelReportGTV;
import myactvity.mahyco.travelreport.ActivityTravelReportTriggered;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Api {


    @POST(RetroConstants.UploadRetailer)
    Call<String> uploadRetailer(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadFarmerVisit)
    Call<String> uploadFarmer(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadDistributor)
    Call<String> uploadDistributor(@Body JsonObject jsonObject);


    @FormUrlEncoded
    @POST("http://10.80.50.153/maatest/MDOHandler.ashx?appName=Myactivity")
    Call<String> syncTraveldata(@Field("Type") String mdo_travelData, @Field("encodedData") String objAsBytes, @Field("input1") String imagestring1, @Field("input2") String imagestring2);

    @POST(RetroConstants.UplaodShellingDay)
    Call<String> uploadShellingDay(@Body JsonArray jsonObject);

    @POST(RetroConstants.UplaodUtpadanMohatsav)
    Call<String> uploadUtpadanMohatsav(@Body JsonArray jsonObject);

    @POST(RetroConstants.UploadHDPSPaymentDeposit)
    Call<String> uploadHDPSPaymentDeposit(@Body JsonArray jsonObject);

    @POST(RetroConstants.UplaodPosteringData)
    Call<String> uplaodPosteringData(@Body JSONObject jsonObject);

    @POST(RetroConstants.GETHDPSUserwiseReport)
    Call<String> getHDPSUserwiseReport(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodStartTravel)
    Call<String> uploadStartTravel(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodEndTravel)
    Call<String> uploadEndTravel(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodRetailerAndDistributor)
    Call<String> uploadRetailerAndDistributor(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadVillageMeeting)
    Call<String> uploadVillageMeeting(@Body JsonObject jsonFinal);

    @POST(RetroConstants.GETHDPSUserwisePaymentDetails)
    Call<HDPSPaymentDetailAPI.PaymentModel> getHDPSUserwisePaymentDetails(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadCropShow)
    Call<String> uploadCropShow(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadFieldBanner)
    Call<String> uploadFieldBanner(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadFieldBoard)
    Call<String> uploadFieldBoard(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadMarketDay)
    Call<String> uploadMarketDay(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodExhibition)
    Call<String> uploadExhibition(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodPosteringData)
    Call<String> uploadPosteringData(@Body JsonObject jsonObject);

    @POST(RetroConstants.UplaodPosteringData)
    Call<String> uploadPosteringDataOld(@Body JSONObject jsonObject);

    @POST(RetroConstants.UploadReviewMeeting)
    Call<String> uploadReviewMeeting(@Body JsonObject jsonObject);

    @POST(RetroConstants.DownloadPlotMasterData)
    Call<String> getUserPlotMasterData(@Body JsonObject jsonObject);

    @POST(RetroConstants.DownloadAllMasterData)
    Call<String> getUserAllMasterData(@Body JsonObject jsonObject);

    @POST(RetroConstants.DownloadPlotValidationMasterData)
    Call<String> getUserPlotValidationMasterData(@Body JsonObject jsonObject);

    @POST(RetroConstants.GET_TBM_WISE_KA_LIST)
    Call<TBMWiseMdoList.KAListModel> GetTBMWiseKAList(@Body JsonObject jsonObject);

    @POST(RetroConstants.GET_TRAVEL_REPORT_TRIGGED)
    Call<ActivityTravelReportTriggered.MyTravelModel> GetTravelReportTriggered(@Body JsonObject jsonObject);

    @POST(RetroConstants.GET_TRAVEL_REPORT_GTV_REPORT)
    Call<ActivityTravelReportGTV.Root> GetTravelGTVReport(@Body JsonObject jsonObject);

    @POST(RetroConstants.GET_TRAVEL_REPORT_GTV_REPORT_String)
    Call<String> GetTravelGTVReportString(@Body JsonObject jsonObject);

    @POST(RetroConstants.SUBMIT_MYTRAVELREPORT_TRIGGERED)
    Call<String> submitMyTravelReportTriggeredRemark(@Body JsonObject jsonObject);

    @POST(RetroConstants.SUBMIT_FOCUSVILLAGETAGGING)
    Call<String> submitFocusVillageData(@Body JsonObject jsonObject);

    @POST(RetroConstants.GETVERSIONDETAILS)
    Call<String> getAppVersion(@Query("packageName") String packageName, @Query("userCode") String userCode, @Query("IMEICode") String IMEICode);


    @POST(RetroConstants.UplaodGTVTRavelData)
    Call<String> uploadGTVTravelData(@Body JsonObject jsonObject);

    @POST(RetroConstants.GetSystemDistanceGTV)
    Call<String> GetSystemDistanceGTV(@Body JsonArray jsonObject);
}