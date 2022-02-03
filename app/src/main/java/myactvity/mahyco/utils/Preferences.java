package myactvity.mahyco.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {


    public Preferences(Context context) {
    }


    public static final String APP_PREF = "MahycoSchool";
    public static SharedPreferences sp;

    public static String KEY_LOGIN = "login";
    public static String KEY_NAME = "name";


    public static void save(Context context, String key, String value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void saveBool(Context context, String key, boolean value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static Boolean getBool(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        boolean val = sp.getBoolean(key, false);
        return val;
    }

    public static int getInt(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        int userId = sp.getInt(String.valueOf(key), 0);
        return userId;
    }

    public static void saveInt(Context context, String key, int value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static String get(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        String userId = sp.getString(key, "");
        return userId;
    }

    public static void clearPreference(Context context) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    public static void remove(Context context,String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();


    }


}
