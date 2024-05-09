package myactvity.mahyco.retro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import myactvity.mahyco.newupload.HDPSPaymentDetailAPI;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Api {


    @POST(RetroConstants.UploadRetailer)
    Call<String> uploadRetailer(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadFarmerVisit)
    Call<String> uploadFarmer(@Body JsonObject jsonObject);

    @POST(RetroConstants.UploadDistributor)
    Call<String> uploadDistributor(@Body JsonObject jsonObject);
//    @POST(Constants.SUB_COURSE_URL)
//    Call<List<SubCourseModel>> getSubCourseList(@Query("mobile") String mobile, @Query("id")String id);
//
@FormUrlEncoded
@POST("http://10.80.50.153/maatest/MDOHandler.ashx?appName=Myactivity")
Call<String> syncTraveldata(@Field("Type") String mdo_travelData, @Field("encodedData")  String objAsBytes, @Field("input1")  String imagestring1, @Field("input2")  String imagestring2);

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
}
