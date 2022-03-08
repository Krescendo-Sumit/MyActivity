package myactvity.mahyco.helper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by 97260828 on 12/15/2017.
 */

public class Messageclass {

    private Context context;
    private Boolean fg = false;

    public Messageclass(Context context) {
        this.context = context;
    }

    public void showMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle("My Activity App");
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);
        // Setting OK Button

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        //alertDialog.show();

        /*If context not null then only show dialog*/
        if(context!=null){
            // Showing Alert Message
            alertDialog.show();
        }

        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.weight = 10;
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public void showMessageview(String msg) {
        //TextView msg1 = new TextView(context);
        WebView wb1 = new WebView(context);
        WebSettings webSettings = wb1.getSettings();
        webSettings.setDefaultFontSize(11);
        // wb1.loadData(msg, "text/html", null);
        wb1.loadDataWithBaseURL(null, msg, null, "utf-8", null);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            msg1.setText(Html.fromHtml(msg,Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            msg1.setText(Html.fromHtml(msg));
        }*/

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle("My Activity App");
        // Setting Dialog Message
        alertDialog.setView(wb1);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);
        // Setting OK Button

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();

        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.weight = 10;
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public boolean showMessageConfirm(String message) {

        // fg=false;
        // AlertDialog alertDialog = new AlertDialog.Builder(VisitorInformation.this).create();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("My Actvity App");
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);
        // Setting OK Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do do my action here
                fg = true;
                dialog.dismiss();

            }

        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                fg = false;
                dialog.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        return fg;
    }

    public  String getDeviceIMEI() {
        String deviceId = "";
        try {

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;
           // deviceId=String.valueOf(i1);
            //deviceId = UUID.randomUUID().toString();
        //Settings.Secure.getString(
                  //  context.getContentResolver(),
                  //  Settings.Secure.ANDROID_ID);
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Log.d("MAA", "android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ");
                Log.d("MAA", "MAA 1 DEVICE_ID : " + deviceId);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "TODO";
                }
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                    Log.d("MAA", "MAA 2 DEVICE_ID : " + deviceId);
                } else {
                    deviceId = Settings.Secure.getString(
                            context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    Log.d("MAA", "MAA 3 ANDROID_ID : " + deviceId);
                }
            }
        }
        catch (Exception e){
            Log.d("MSG",e.getMessage());
        }
        return deviceId;
    }
    public String getDeviceIMEIold() {
        String deviceUniqueIdentifier = null;
        try {
           TelephonyManager tm = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                }
                deviceUniqueIdentifier = tm.getDeviceId();
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier = Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        catch (Exception ex)
        {
            deviceUniqueIdentifier=ex.toString();
        }
        return deviceUniqueIdentifier;
    }

    public String getMobileNo() {

       // TelephonyManager tm1 = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //String number = tm1.getLine1Number();

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            }
            deviceUniqueIdentifier = tm.getLine1Number();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }
}


