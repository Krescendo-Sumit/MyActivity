package myactvity.mahyco.app;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import  java.lang.String;
import java.net.URLDecoder;

import static android.content.ContentValues.TAG;

public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
    Context mContext;
    public Config(Context mContext) {
        this.mContext = mContext;
    }

    public boolean  NetworkConnection() {

        boolean flag=  false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            if (networkInfo != null && networkInfo.isConnected()) {
                flag=  true;
            }
        }
        catch(Exception ex)
        {
            Log.d(TAG, "NetworkConnection: ");
        }
        return flag;


    }
    public int getIndexonvalue(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){

            GeneralMaster dt = (GeneralMaster) spinner.getItemAtPosition(i);
            if (dt.Code().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public String  getvalue(Spinner spinner)
    {
        String  code="";

        GeneralMaster gm = (GeneralMaster) spinner.getSelectedItem();
        try {
            code = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    public int getIndex(Spinner spinner, String myString)
    {
        try {
            myString= URLDecoder.decode(myString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    public static boolean isInternetConnected(Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public  static void refreshActivity(Activity activity)
    {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);
    }
    public String returnmsg(String resultout)
    {
        String rntmsg="";
        try
        {
            JSONObject object = new JSONObject(resultout);
            JSONArray jArray = object.getJSONArray("Table");
            JSONObject jObject = jArray.getJSONObject(0);
            rntmsg=jObject.getString("message").toString() + "Error";
        }
        catch (Exception ex )
        {
            rntmsg=ex.getMessage()+resultout;
        }
        return rntmsg;
    }
}