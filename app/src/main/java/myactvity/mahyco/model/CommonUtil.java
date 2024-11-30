package myactvity.mahyco.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import myactvity.mahyco.BuildConfig;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.SqliteDatabase;

public class CommonUtil {
    public static double getDistance(String source, String destination) {
        try {
            String src[] = source.trim().split("-");
            String des[] = destination.trim().split("-");

            LatLng sourceLoc = new LatLng(Double.parseDouble(src[0].trim()), Double.parseDouble(src[1].trim()));
            LatLng destinationLoc = new LatLng(Double.parseDouble(des[0].trim()), Double.parseDouble(des[1].trim()));
            double d = SphericalUtil.computeDistanceBetween(sourceLoc, destinationLoc);
            return d;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static boolean addGTVActivity(Context context, String activityid, String activityName, String cordinates, String remark, String activityType, String actualKM, double attendance) {
        try {
            SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
            String userCode = sp.getString("UserID", null);
            userCode = userCode.replace(" ", "%20");
            Prefs prefs = Prefs.with(context);
            String punchInCordinates = prefs.getString(AppConstant.GTVPunchIdCoordinates, "");
            activityType = prefs.getString(AppConstant.GTVSELECTEDBUTTON, "Market");
            String selectedGtvtype = prefs.getString(AppConstant.GTVType, "");
            // String selectedActivityType = prefs.getString(AppConstant.ACTIVITYTYPE, "");
            String selectedActivityType = activityType;

            Date entrydate = new Date();
            final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
            String selectedGTV1VillageCode = prefs.getString(AppConstant.GTVSelectedVillageCode, "");
            String selectedGTV1Village = prefs.getString(AppConstant.GTVSelectedVillage, "");
            String spendtime = "";
            double mins = 0;
            try {

                if (activityType.trim().equals("GTV")) {
                    String lasttime = prefs.getString(AppConstant.LASTGTVACTIVITYTIME, "");
                    String currenttime = InTime;
                    mins = 0;
                    Log.i("Time Span is ", lasttime + "--->" + currenttime);
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lasttime);
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currenttime);
                        long millis = date2.getTime() - date1.getTime();
                        mins = (int) (millis / 1000) / 60;
                        Log.i("Time Span is ", "--->" + mins);
                        spendtime = "Time Span is Last GTV Act time:" + lasttime + " and Current GTV Act time:" + currenttime + ". Total Min :  " + mins;
                    } catch (Exception e) {
                        mins = 0;
                    }

                } else {
                    mins = 0;
                }
            } catch (Exception e) {

            }
            SqliteDatabase mDatabase = SqliteDatabase.getInstance(context);
            GTVTravelActivityDataModel gtvTravelActivityDataModel = new GTVTravelActivityDataModel();
            gtvTravelActivityDataModel.setId(0);// integer auto increment,
            gtvTravelActivityDataModel.setActivityId(activityid);// TEXT,
            gtvTravelActivityDataModel.setKACode(userCode);// TEXT,
            gtvTravelActivityDataModel.setGTVType(selectedGtvtype);// GTV1 or GTV2
            gtvTravelActivityDataModel.setActivityName(activityName);// TEXT,  Activity name
            gtvTravelActivityDataModel.setActivityType(selectedActivityType);// TEXT, GTV or Market
            gtvTravelActivityDataModel.setActivityDt(InTime);// TEXT,  add current date and time
            gtvTravelActivityDataModel.setVillageCode(selectedGTV1VillageCode);// TEXT, add selected GTV village code
            gtvTravelActivityDataModel.setVillageName(selectedGTV1Village);// TEXT,     add selected GTV village name
            String lastCordinate = mDatabase.getLastGTVActivityCoordinates(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // getting
            gtvTravelActivityDataModel.setLastCoordinates(lastCordinate);// TEXT,
            gtvTravelActivityDataModel.setCoordinates(cordinates);// TEXT,
            gtvTravelActivityDataModel.setRefrenceId("0");// TEXT,
            gtvTravelActivityDataModel.setActualKM(actualKM);// TEXT,

            if (punchInCordinates.trim().equals("") || punchInCordinates.trim().equals("0-0")) {
                gtvTravelActivityDataModel.setDistanceFromPunchKm("0");// TEXT,
            } else {
                gtvTravelActivityDataModel.setDistanceFromPunchKm("" + CommonUtil.getDistance(punchInCordinates, cordinates));// TEXT,
            }

            if (lastCordinate.trim().equals("0-0") || cordinates.trim().equals("0-0"))
                gtvTravelActivityDataModel.setGTVActivityKM("0");// TEXT,
            else
                gtvTravelActivityDataModel.setGTVActivityKM("" + CommonUtil.getDistance(lastCordinate, cordinates));// TEXT,
            gtvTravelActivityDataModel.setAppVersion(BuildConfig.VERSION_NAME);// TEXT,
            gtvTravelActivityDataModel.setRemark(remark);
            gtvTravelActivityDataModel.setIsSynced(0);


            gtvTravelActivityDataModel.setAttendance(attendance);
            gtvTravelActivityDataModel.setTimeSpend(mins);
            gtvTravelActivityDataModel.setInfo1(spendtime + "~AutoTime:" + isTimeAutomatic(context)+"~Dev:"+checkDeveloperMode(context));
            gtvTravelActivityDataModel.setInfo2("" + prefs.getString(AppConstant.GTVSELECTEDMARKETBUTTON, ""));
            String gtvCordinates = "";
            try {
                gtvCordinates = prefs.getString(AppConstant.GTVCurrentCoOrdinates, "" + cordinates);
                gtvTravelActivityDataModel.setInfo3("" + gtvCordinates);
            } catch (Exception exception) {
                gtvTravelActivityDataModel.setInfo3("");
            }
            gtvTravelActivityDataModel.setInfo3("" + gtvCordinates);
            if (mDatabase.InsertGTVTravelData(gtvTravelActivityDataModel)) {
                prefs.save(AppConstant.LASTGTVACTIVITYTIME, InTime);
                Toast.makeText(context, activityType + " activity tagged.", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Log.i("Exception in add", e.getMessage());
            return false;
        }
    }

    public static boolean isTimeAutomatic(Context c) {
        try {
            boolean a = Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME) == 1;
            return a;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean validateAutoTimeAndDevAccount(Context c) {
        try {

            if(!isTimeAutomatic(c)) {
             showMessage(c,"Please enable auto date/time setting.");
                return false;
            }
            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean checkDeveloperMode(Context c) {
        try {
            int adb = Settings.Secure.getInt(c.getContentResolver(),
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0);
            if(adb==1) {
                showMessage(c,"For Security, Please disable developer mode or developer options from setting.");
                return true;
            }
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }




    public static void showMessage(Context context,String message)
    {
        try{

            new AlertDialog.Builder(context)
                    .setMessage(""+message)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }catch (Exception e)
        {

        }
    }

}
