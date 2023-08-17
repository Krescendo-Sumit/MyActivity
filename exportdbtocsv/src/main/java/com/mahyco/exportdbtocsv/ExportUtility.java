package com.mahyco.exportdbtocsv;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ExportUtility {

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123321;
    private Context mContext;
    private Activity activity;
    public AlertDialog mAlertDialogPermissions;
    private ExportPermissionGranted mExportPermissionGranted;

    public ExportUtility(Context mContext, Activity activity, ExportPermissionGranted mExportPermissionGranted) {
        this.mContext = mContext;
        this.activity = activity;
        this.mExportPermissionGranted = mExportPermissionGranted;
    }

    public boolean checkAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int write = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    public void requestAllPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_EXTERNAL_STORAGE)) {

            try {

                AlertDialog.Builder sayWindows = new AlertDialog.Builder(mContext);
                sayWindows.setCancelable(false);

                TextView myMsg = new TextView(mContext);
                myMsg.setText("You have forcefully denied some of the required permissions " +
                        "for this action. Please open settings, go to permissions and allow them.");
                myMsg.setPadding(10, 20, 10, 0);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(12);
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                sayWindows.setPositiveButton("Setting", null);
                sayWindows.setNegativeButton("Retry", null);
                sayWindows.setView(myMsg);

                mAlertDialogPermissions = sayWindows.create();
                mAlertDialogPermissions.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button setting = mAlertDialogPermissions.getButton(AlertDialog.BUTTON_POSITIVE);
                        setting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (checkAllPermission()) {
                                    mAlertDialogPermissions.cancel();
                                    mExportPermissionGranted.isPermissionGranted(true);
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                }
                            }
                        });

                        Button retry = mAlertDialogPermissions.getButton(AlertDialog.BUTTON_NEGATIVE);
                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (checkAllPermission()) {
                                    mAlertDialogPermissions.cancel();
                                    mExportPermissionGranted.isPermissionGranted(true);
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                    }
                });
                mAlertDialogPermissions.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
}
