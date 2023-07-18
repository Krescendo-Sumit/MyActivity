package com.mahyco.customercomplaint.ccfactivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mahyco.customercomplaint.CCFFirstActivity;
import com.mahyco.customercomplaint.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class CCFBaseActivity extends AppCompatActivity {

    private Dialog mDialog = null;
    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.ccf_status_color));
        init();
    }

    protected abstract int getLayout();

    protected abstract void init();

    protected void callComplaintTypeActivity(Context mContext) {
        Intent intent = new Intent(mContext, CCFComplaintTypesActivity.class);
        startActivity(intent);
        finish();
    }

    protected void showAfterGettingResponseDialog(Context context, String message) {
        mDialog = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Customer Feedback");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismissDialog();
                Intent intent = new Intent(context, CCFFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mDialog = alertDialog.create();
        mDialog.show();
    }

    protected void responseDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    protected void callAppActivity() {
       /* try {
            Log.e("temporary","try block called");
            Intent intent = new Intent(BaseActivity.this, Class.forName(StoreData.getString(this, ConstantValues.ACTIVITY_PATH+"MainActivity")));
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            Log.e("temporary","catch block called");
            e.printStackTrace();
            finish();
        }*/
        finish();
    }

    protected void showProgressDialog(Context context) {
        mProgressDialog = null;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    protected String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
//        String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd HH:mm:ss.SSS";

        SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.getDefault());
        return df.format(date);
    }

    protected boolean checkAutoTimeEnabledOrNot() {
        return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
    }

    protected void showAutomaticTimeMessage(Context context, String message) {
        mDialog = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Customer Feedback");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Settings", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_DATE_SETTINGS)));
        mDialog = alertDialog.create();
        mDialog.show();
    }

    protected void showInternetDialog(Context context, String message) {
        mDialog = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Customer Feedback");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> dismissDialog());
        mDialog = alertDialog.create();
        mDialog.show();
    }

    protected void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    protected void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void deleteImagesAfterPhotoTaken() {

        /*try {
            File dir = getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES + "/" + "Customer Complaint"
            );
              Log.e("temporary", " dir " + dir);
            if (dir != null) {
                   Log.e("temporary", " inside dir " + dir);
                File[] photoFile = dir.listFiles();
                Log.e("temporary", "deleteImagesAfterPhotoTaken photoFile " + photoFile);
                if (photoFile != null) {
                      Log.e("temporary", " photoFile size " + photoFile.length);
                    for (int i = 0; i < photoFile.length; i++) {
                        photoFile[0].delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("temporary", "deleteImagesAfterPhotoTaken e.getCause()" + e.getCause());
        }*/
    }

    protected void deleteSinglePhoto(String path) {
        try {
            File dir = new File(path);
        //    Log.e("temporary", " dir " + dir + " isExists " + dir.exists() + " path " + path);
            if (dir.exists()) {
                boolean delete = dir.delete();
             //   Log.e("temporary", " delete " + delete);
            }
        } catch (Exception e) {
            Log.e("temporary", "deleteImagesAfterPhotoTaken e.getCause()" + e.getCause());
        }
    }

    protected boolean checkInternetConnection(Context context) {
        boolean result = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cm != null) {
                    NetworkCapabilities ne = cm.getNetworkCapabilities(cm.getActiveNetwork());
                    if (ne != null) {
                        result = ne.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                ne.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                ne.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                    }
                }
            } else {*/
            if (cm != null) {
                NetworkInfo activeInfo = cm.getActiveNetworkInfo();
                if (activeInfo != null && activeInfo.isConnected()) {
                    result = activeInfo.getType() == ConnectivityManager.TYPE_WIFI || activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            /*}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected File createImageFile(String type) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = type + "_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Log.e("temporary", " dir " + storageDir);
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        );
    }
}