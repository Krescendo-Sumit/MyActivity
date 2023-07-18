package com.mahyco.customercomplaint.ccfstoredata;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mahyco.customercomplaint.CCFConstantValues;

public class CCFStoreData {
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_CUST_COMPLAINT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context mContext, String key) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_CUST_COMPLAINT, AppCompatActivity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void clearString(Context mContext) {
      //  Log.e("temporary","clear string called");
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_CUST_COMPLAINT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /*added below code on 15/06/2017*/
    /*public static void putStringForPendingComment(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_PENDING_COMMENT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPendingComment(Context mContext, String key) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_PENDING_COMMENT, AppCompatActivity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void clearPendingComment(Context mContext) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_PENDING_COMMENT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }*/

    public static void putStringForSubmitComment(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_SUBMIT_COMMENT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringSubmitComment(Context mContext, String key) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_SUBMIT_COMMENT, AppCompatActivity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void clearSubmitComment(Context mContext) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(CCFConstantValues.CCF_SUBMIT_COMMENT, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /*added code ended here 15/06/2017*/
}
