package myactvity.mahyco.utils;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import myactvity.mahyco.helper.LocalHelper;


//import com.google.android.gms.analytics.Tracker;

//public class MyApplication extends Application {
public class MyApplication extends MultiDexApplication {

    //private Tracker mTracker;

    /**
     * @return tracker
     *//*
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }*/
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //  Fabric.with(this, new Crashlytics());
        mContext = this;
        //JobManager.create(this).addJobCreator(new DemoJobCreator());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base, "en"));
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration configuration = new Configuration(newConfig);
        Utils.adjustFontScale(getApplicationContext(), configuration);
    }*/

    public static Context getContext() {
        return mContext;
    }

    private static boolean activityVisible;
}