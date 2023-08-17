/*
package com.mahyco.exportdbtocsv;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ExportToCSVActivity extends AppCompatActivity implements View.OnClickListener {

    private ExportDbToCsv mExportDbToCsv;
    private SQLiteOpenHelper dbHelper = null;
    private Context mContext;
    private CardView mExportDbCardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_db_activity);

        mContext = this;

        mExportDbCardView = findViewById(R.id.export_lib_db_to_csv);
        mExportDbCardView.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkAllPermission()) {
                requestAllPermission();
            }

//            create your actual SQLiteOpenHelper class object
//            dbHelper = new (your class name)
            mExportDbToCsv = new ExportDbToCsv();
        }
    }

    private boolean checkAllPermission() {
        int write = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
    }

    private AlertDialog mAlertDialogPermissions;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                requestAllPermission();
            }
        } else {
            requestAllPermission();
        }
    }

    void requestAllPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {

            try {

                AlertDialog.Builder sayWindows = new AlertDialog.Builder(this);
                sayWindows.setCancelable(false);

                TextView myMsg = new TextView(this);
                myMsg.setText("You have forcefully denied some of the required permissions " +
                        "for this action. Please open settings, go to permissions and allow them.");
                myMsg.setPadding(10, 20, 10, 0);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(12);
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                sayWindows.setPositiveButton("Setting", null);
                sayWindows.setNegativeButton("Retry", null);
                sayWindows.setNeutralButton("Cancel", null);
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
                                    //    new GetDataAsyncTask().execute();
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }
                        });

                        Button retry = mAlertDialogPermissions.getButton(AlertDialog.BUTTON_NEGATIVE);
                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (checkAllPermission()) {
                                    mAlertDialogPermissions.cancel();
                                    //   new GetDataAsyncTask().execute();
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
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
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onDestroy() {
        if (mAlertDialogPermissions != null && mAlertDialogPermissions.isShowing()) {
            mAlertDialogPermissions.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mExportDbCardView) {
            if (dbHelper != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkAllPermission()) {
                        requestAllPermission();
                    } else {
                        mExportDbToCsv.exportWholeDb(mContext, null, "1#myactivitydb#");
                    }
                } else {
                    mExportDbToCsv.exportWholeDb(mContext, null, "1#myactivitydb#");
                }
            } else {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
*/
