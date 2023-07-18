package com.mahyco.customercomplaint;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mahyco.customercomplaint.ccfnetwork.CCFCategoryPojoModel;

public class CCFDataPreferences {
    private static final String KEY_ARRAY_LIST3 = "CCF_arrayListData3";
    private static final String DATA_IS_STORED = "CCF_DATA_IS_STORED";
    private static final String PREF_NAME = "CCF_CATEGORY_DATA";


    public static void storeDataArrayList3(CCFCategoryPojoModel stringList, Context context) {
        Gson gson = new Gson();
        String s = gson.toJson(stringList);
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_ARRAY_LIST3, s);
        editor.apply();
    }

    public static void putDataIsStoredString(Context mContext, String value) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATA_IS_STORED, value);
        editor.apply();
    }

    public static String getDataStoredString(Context mContext) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        return sharedPreferences.getString(DATA_IS_STORED, "");
    }

    public static String getCategoryString(Context mContext) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ARRAY_LIST3, "");
    }

    public static void getClearCategoryData(Context mContext) {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
