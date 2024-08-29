package myactvity.mahyco.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

    public static boolean addGTVActivity(Context context, String activityid, String activityName, String cordinates, String remark,String activityType,String actualKM) {
        try {
            SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
            String userCode = sp.getString("UserID", null);
            userCode = userCode.replace(" ", "%20");
            Prefs prefs = Prefs.with(context);
            String punchInCordinates = prefs.getString(AppConstant.GTVPunchIdCoordinates, "");
            activityType=prefs.getString(AppConstant.GTVSELECTEDBUTTON,"Market");
            String selectedGtvtype = prefs.getString(AppConstant.GTVType, "");
           // String selectedActivityType = prefs.getString(AppConstant.ACTIVITYTYPE, "");
            String selectedActivityType = activityType;

            Date entrydate = new Date();
            final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
            String selectedGTV1VillageCode = prefs.getString(AppConstant.GTVSelectedVillageCode, "");
            String selectedGTV1Village = prefs.getString(AppConstant.GTVSelectedVillage, "");


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
            if(punchInCordinates.trim().equals("")||punchInCordinates.trim().equals("0-0"))
            {
                gtvTravelActivityDataModel.setDistanceFromPunchKm("0");// TEXT,
            }else {
                gtvTravelActivityDataModel.setDistanceFromPunchKm("" + CommonUtil.getDistance(punchInCordinates, cordinates));// TEXT,
            }
            if (lastCordinate.trim().equals("0-0"))
                gtvTravelActivityDataModel.setGTVActivityKM("0");// TEXT,
            else
                gtvTravelActivityDataModel.setGTVActivityKM("" + CommonUtil.getDistance(lastCordinate, cordinates));// TEXT,
            gtvTravelActivityDataModel.setAppVersion(BuildConfig.VERSION_NAME);// TEXT,
            gtvTravelActivityDataModel.setRemark(remark);
            gtvTravelActivityDataModel.setIsSynced(0);

            if (mDatabase.InsertGTVTravelData(gtvTravelActivityDataModel)) {

                Toast.makeText(context, activityType+" activity tagged.", Toast.LENGTH_SHORT).show();
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

}
