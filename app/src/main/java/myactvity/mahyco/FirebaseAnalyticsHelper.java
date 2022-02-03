package myactvity.mahyco;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsHelper {

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static FirebaseAnalyticsHelper helperInstance;

    private final String CE_MY_TRAVEL = "ce_my_travel";
    private final String CE_MFDC = "ce_mfdc";
    private final String CE_SALE_ORDER = "ce_sale_order";
    private final String CE_POG = "ce_pog";
    private final String CE_ADD_ACTIVITY_MY_TRAVEL = "ce_add_activity_my_travel";

    private final String CE_LOGIN = "ce_login_user";
    private final String CE_LOGOUT = "ce_logout_user";
    private final String CE_REGISTER = "ce_register_user";
    private final String CE_APP_OPENED = "ce_app_opened";
    private final String CE_FORGOT_PASSWORD = "ce_forgot_password";
    private final String CE_VIEW_PROFILE = "ce_view_profile";
    private final String CE_ABOUT_APP = "ce_about_app";
    private final String CE_UPLOAD_DATA = "ce_upload_data";
    private final String CE_DOWNLOAD_DATA = "ce_download_data";
    private final String CE_MY_TRADE_MAPPING_TAGGING = "ce_my_trade_mapping_tagging";
    private final String CE_REPORT = "ce_reports";
    private final String CE_IMPF = "ce_impf";
    private final String CE_VCBU_HELP_DESK_SO = "ce_vcbu_help_desk_so";
    private final String CE_RCBU_HELP_DESK_SO = "ce_rcbu_help_desk_so";
    private final String CE_TOTAL_SALES_ORDER = "ce_total_sales_order";
    private final String CE_APPROVED_SALES_ORDER = "ce_approved_sales_order";
    private final String CE_HDPS_DATA = "ce_hdps_data";
    private final String CE_HDPS_DASHBOARD = "ce_hdps_dashboard";

    public static FirebaseAnalyticsHelper getInstance(Context context) {
        // Obtain the FirebaseAnalytics instance.
        if(helperInstance==null){
            helperInstance = new FirebaseAnalyticsHelper();
        }
        if(mFirebaseAnalytics==null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        //return mFirebaseAnalytics;
        return helperInstance;
    }

    public void callLoginEvent(String userId, String displayName){ //1
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "ce_login_user");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_LOGIN, bundle);
    }

    public void callLogoutEvent(String userId, String displayName){ //2
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "ce_logout_user");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_LOGOUT, bundle);
    }

    public void callRegisterUser(String userId, String displayName){ //3
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "ce_register_user");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_REGISTER, bundle);
    }

    public void callOnLoadEvent(String userId, String displayName){ //4
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ce_app_opened");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_APP_OPENED, bundle);
    }

    public void callForgotPasswordEvent(String userId, String displayName){ //5
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "ce_forgot_password");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_FORGOT_PASSWORD, bundle);
    }

    public void callViewProfileEvent(String userId, String displayName){ //6
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_view_profile");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_VIEW_PROFILE, bundle);
    }

    public void callAboutAppEvent(String userId, String displayName){ //7
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_about_app");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_ABOUT_APP, bundle);
    }

    public void callUploadDataEvent(String userId, String displayName){ //8
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_upload_data");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_UPLOAD_DATA, bundle);
    }

    public void callDownloadMasterDataEvent(String userId, String displayName){ //9
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_download_data");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_DOWNLOAD_DATA, bundle);
    }


    public void callMyTravelAndActivityRecordingEvent(String userId, String displayName){ //10
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_my_travel_and_activity_recording");
        mFirebaseAnalytics.setUserId(userId);
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.logEvent(CE_MY_TRAVEL, bundle);
    }

    public void callTradeMappingAndTaggingEvent(String userId, String displayName){ //11
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_my_trade_mapping_and_tagging");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_MY_TRADE_MAPPING_TAGGING, bundle);
    }

    public void callSaleOrderEvent(String userId, String displayName){ //12
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_sales_order");
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_SALE_ORDER, bundle);
    }

    public void callMFDCEvent(String userId, String displayName){ //13
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_mfdc");
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_MFDC, bundle);
    }

    public void callReportEvent(String userId, String displayName){ //14
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_reports");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_REPORT, bundle);
    }

    public void callPOGEvent(String userId, String displayName){ //15
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_pog");
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_POG, bundle);
    }

    public void callIMPFEvent(String userId, String displayName){ //16
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_impf");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_IMPF, bundle);
    }

    public void callAddActivityMyTravel(String userId, String displayName){ //17
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_travel_add_activity");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_ADD_ACTIVITY_MY_TRAVEL, bundle);
    }

    public void callSaleOrderToMapHelpDeskVCBU(String userId, String displayName){ //18
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_vcbu_help_desk_sales_order");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_VCBU_HELP_DESK_SO, bundle);
    }

    public void callSaleOrderToMapHelpDeskRCBU(String userId, String displayName){ //19
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_rcbu_help_desk_sales_order");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_RCBU_HELP_DESK_SO, bundle);
    }

    public void callSaleOrderToMapHelpDeskTotal(String userId, String displayName){ //20
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_total_help_desk_sales_order");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_TOTAL_SALES_ORDER, bundle);
    }

    public void callSaleOrderApprovedOrder(String userId, String displayName){ //21
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_approved_help_desk");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_APPROVED_SALES_ORDER, bundle);
    }

    public void callHDPSDataEvent(String userId, String displayName){ //22
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_hdps_data");
        mFirebaseAnalytics.setUserId(userId);
        Log.d("callHDPSDataEvent","userId:"+userId+" displayName:"+displayName+" CREATIVE_NAME:ce_hdps_data");
        mFirebaseAnalytics.logEvent(CE_HDPS_DATA, bundle);
    }

    public void callHDPSDashboardEvent(String userId, String displayName){ //23
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName);
        bundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "ce_hdps_dashboard");
        mFirebaseAnalytics.setUserId(userId);
        mFirebaseAnalytics.logEvent(CE_HDPS_DASHBOARD, bundle);
    }
}
