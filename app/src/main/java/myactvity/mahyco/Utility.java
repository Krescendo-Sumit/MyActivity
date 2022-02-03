package myactvity.mahyco;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 25/07/2017.
 */

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void setRegularFont(final TextView tv, Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/JosefinSans-Regular.ttf");
        tv.setTypeface(face);

    }

    public static void setRegularFontEt(final EditText tv, Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/JosefinSans-Regular.ttf");
        tv.setTypeface(face);

    }

    public static void setLightFont(final TextView tv, Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/JosefinSans-Light.ttf");
        tv.setTypeface(face);

    }

    public static void setThinFont(final TextView tv, Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/JosefinSans-Thin.ttf");
        tv.setTypeface(face);

    }

    public static void showAlertDialog(String sTitle, String sMessage, final Context myContext) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Activity) myContext);

        builder
                .setTitle(sTitle)
                .setMessage("" + sMessage + "")
                .setCancelable(false)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(false);
                        dialog.dismiss();
                    }
                });

        // Showing Alert Message
        builder.show();


    }


}
