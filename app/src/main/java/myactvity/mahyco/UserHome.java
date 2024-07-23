package myactvity.mahyco;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.mahyco.customercomplaint.CCFFirstActivity;
import com.mahyco.exportdbtocsv.ExportDbToCsv;
import com.mahyco.feedbacklib.view.DialogFeedback;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.newupload.NotificationReceiver;
import myactvity.mahyco.newupload.SetAlarmActivity;
import myactvity.mahyco.newupload.UploadDataNew;
import myactvity.mahyco.travelreport.ActivityTravelReportTriggered;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    FragmentManager fragmentManager;
    public LinearLayout my_linear_layout;
    GridView gridView, gridView2;
    private Boolean exit = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    String vername;
    // public  ProgressDialog dialog,pd;
    Config config;
    private Context context;
    String Intime = "";
    TextView lblwelcome, myTextProgress;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private Handler handler = new Handler();
    String userId;
    Dialog dialog1;
    private ExportDbToCsv mExportDbToCsv;


    private SQLiteOpenHelper dbHelper = null;

    // private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);//activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // setTitle("MY ACTIVITY HOME PAGE");
        getSupportActionBar().setTitle("MY ACTIVITY HOME PAGE");
        createcustometitlebar();
        // Always call the superclass so it can restore the view hierarchy
        //  super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        //mCurrentScore = savedInstanceState.getInt(STATE_SCORE);

        context = this;

        // pd = new ProgressDialog(context);

        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        lblwelcome = (TextView) findViewById(R.id.lblwelcome);
        lblwelcome.setText("Welcome: " + preferences.getString("Displayname", null));
        context = this;
        cx = new CommonExecution(this);
        msclass = new Messageclass(this);
        //DB Crration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            AppConstant.dbnamepath = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/SQLiteDB/MDOApps.db";
            Log.d("Rajshri", "DB_PATH : " + this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/SQLiteDB/MDOApps.db");
        } else {
            AppConstant.dbnamepath = "/mnt/sdcard/MDOApps.db";
        }
        mDatabase = SqliteDatabase.getInstance(this);


        config = new Config(this); //Here the context is passingGetAppversion
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        myTextProgress = (TextView) findViewById(R.id.myTextProgress);
        userId = preferences.getString("UserID", "");
        // container = (ScrollView) findViewById(R.id.container);
        //dialog = new ProgressDialog(this);
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        SharedPreferences locsp = getApplicationContext().getSharedPreferences("locdata", 0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//create an instance of Fragment-transaction
        fragment = new UserHomeContainer();
        transaction.replace(R.id.FragmentContainer, fragment);//new UserHomeContainer(),"Product Information");
        transaction.commit();


        View headerView = navigationView.getHeaderView(0);
        TextView txtversion = (TextView) headerView.findViewById(R.id.textView);
        TextView tvAppName = (TextView) headerView.findViewById(R.id.tvAppName);
        TextView tvEmailId = (TextView) headerView.findViewById(R.id.tvEmailId);
        Utility.setRegularFont(lblwelcome, context);
        //    Utility.setRegularFont(txtversion, context);
        Utility.setRegularFont(tvAppName, context);
        Utility.setRegularFont(tvEmailId, context);
        try {
            vername = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
       /*
            boolean flag22 = mDatabase.isTableExists("mdo_Retaileranddistributordata");
            if (flag22 == false) {
                mDatabase.CreateTable7();
            }
            boolean flag3 = mDatabase.isTableExists("mdo_retailerproductdetail");
            if (flag3 == false) {
                mDatabase.CreateTable8();
            }

*/

            if (config.NetworkConnection()) {
                //UploadalldataActvity("tagdatauploadMDONew_Testold");
                // MDO_TravelData();
                //UploadalldataInnovation("InnovationData");
                // UploadalldataActvityNEW("tagdatauploadMDONew_Testold");


              /*  new UploadDataONServernew("").execute();
                UploadFarmerData("MDOFarmerMasterdataInsertNew");
                downloadphotopath("mdo_photoupdate");
              */


                //New Changes
               /* relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Wait");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });*/
                progressBar.setIndeterminate(true);
                new Thread(new Runnable() {
                    public void run() {

                        handler.post(new Runnable() {
                            public void run() {

                                new UploadDataONServernew("").execute();
                                // UploadFarmerData("MDOFarmerMasterdataInsertNew");
                                // downloadphotopath("mdo_photoupdate");
                            }
                        });

                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBarVisibility();
                        }
                    }
                }).start();
            }
        } catch
        (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            // msclass.showMessage(ex.getMessage());
            Toast.makeText(context, ex.getMessage() + "Home page", Toast.LENGTH_SHORT).show();

            ex.printStackTrace();

        }


        lblwelcome.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(UserHome.this, AndroidDatabaseManager.class);
                startActivity(i);
                return false;
            }
        });
        lblwelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logAppOpenEvent();
        try {
            myAlarm(18,45,00);
//            myAlarm(15,15,10);
//            myAlarm(18,1,10);
//            myAlarm(21,1,10);
        } catch (Exception e) {
            Log.i("Exception ",e.getMessage());
         //   Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //testCrash();

        /*TODO Uncomment when App Feedback Module required.*/
        // showUserFeedbackScreen(userId);

        if (config.NetworkConnection()) {
            try {
                String IME = msclass.getDeviceIMEI();
                SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0);
                String userCode = sp.getString("UserID", null);
                userCode = userCode.replace(" ", "%20");
                IME = IME.replace(" ", "%20");

              //  new CheckVersion().execute("https://feedbackapi.mahyco.com/api/Feedback/getAppFeedbackStatus?packageName=myactvity.mahyco&userCode="+userCode+"&IMEICode="+IME+"");
               //   new CheckVersion().execute("https://feedbackapi.mahyco.com/api/Feedback/getAppFeedbackStatus?packageName=myactvity.mahyco");
            } catch (Exception e) {
                Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {

        }
        showTBMKAList("Please verify your KA list and submit your remark.");

    }
    public void myAlarm(int hr, int min, int sec) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20000, pendingIntent);
           // Toast.makeText(context, "Alarm Seted", Toast.LENGTH_SHORT).show();
        }


    }

    private void showUserFeedbackScreen(String user) {
        Prefs mPref = Prefs.with(UserHome.this);
        String userId = user.trim();
        boolean isFeedDone = mPref.getBoolean(Constants.LOCAL_CHECK_ISFEED_GIVEN, false);
        Log.d("AppFeed_MAA", "GET PREF SAVED AS : " + mPref.getBoolean(Constants.LOCAL_CHECK_ISFEED_GIVEN, false));

        if (isFeedDone == false) {
            // userfeedback
            String json = "";
            String packageName = "myactvity.mahyco";
            Config config = new Config(UserHome.this); //Here the context is passing
            try {
                if (config.NetworkConnection()) {
                    if (userId != null && !userId.isEmpty() && !userId.equals("")) {
                        CommonExecution cxx = new CommonExecution(this);
                        json = cxx.new BreederMasterDataIsFeedGiven(1, userId, packageName).execute().get();
                        Log.d("IsFeed", "User data str :" + json);
                        JSONObject obj = new JSONObject(json);
                        String IsFeedbackGiven = obj.getString("IsFeedbackGiven");
                        if (IsFeedbackGiven.equalsIgnoreCase("False")) {
                            showFeedbackScreen(userId);
                        } else {
                            saveFeedStatusLocally();
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("IsFeed", "Msg: " + e.getMessage());
            }
        }
    }

    private void showFeedbackScreen(String userCode) {
        String packageName = "myactvity.mahyco";
        DialogFeedback feedbackDialog = new DialogFeedback(UserHome.this, packageName, userCode);
        feedbackDialog.showFeedbackDialog();
        IntentFilter filter = new IntentFilter();
        filter.addAction("FeedbackResponse");
        registerReceiver(receiver, filter);
    }

    private void saveFeedStatusLocally() {
        Prefs mPref = Prefs.with(UserHome.this);
        mPref.saveBoolean(Constants.LOCAL_CHECK_ISFEED_GIVEN, true);
        Log.d("AppFeed_MAA", "PREF SAVED AS : " + mPref.getBoolean(Constants.LOCAL_CHECK_ISFEED_GIVEN, false));
    }


    void testCrash() {
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            Log.d("Receiver", "Registered : " + e.getMessage());
        }
    }


    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        //container.setClickable(true);
        // container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void createcustometitlebar() {
        // Get the ActionBar
        androidx.appcompat.app.ActionBar ab = getSupportActionBar();

        // Create a TextView programmatically.
        TextView tv = new TextView(getApplicationContext());
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(ab.getTitle()); // ActionBar title text

        // Set the text color of TextView to black
        // This line change the ActionBar title text color
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(null, Typeface.BOLD);
        // Set the TextView text size in dp
        // This will change the ActionBar title text size
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        // Set the ActionBar display option
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Finally, set the newly created TextView as ActionBar custom view
        ab.setCustomView(tv);
    }

    @Override
    public void onBackPressed() {


        /*if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
            Toast.makeText(this, "Press Back.",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }*/

        /*if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }*/
        //FragmentManager fm = getSupportFragmentManager();
        //fm.popBackStack();
        // finish();
        // System.exit(0);

       /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); */

       /* if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);

        } */

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 9826511395
        // moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = 2;//item.getItemId();
        id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*ToDo comment later, before upload, 6th Sept 2021*/

            /*Intent i = new Intent(this, AndroidDatabaseManager.class);
            startActivity(i);*/

            try {
                try {
                    Context c = UserHome.this;
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();
                    File exportDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);


                    String currentDBPath = "//data//myactvity.mahyco//databases//MDOApps.db";
                    String userid = preferences.getString("UserID", null);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));
                    String backupDBPath = userid + "_" + dtf.format(now) + "_MDAPP.db";
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(exportDir, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.i("File Path ", backupDB.getAbsolutePath());
                    try {
                        File file = backupDB;
                        if (file.exists()) {
                        //    Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".com.vansuita.pickimage.provider", file);
//                          Intent intent = new Intent(Intent.ACTION_SEND);
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            intent.setType("*/*");
//                            intent.putExtra(Intent.EXTRA_STREAM, uri);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            Uri screenshotUri = Uri.parse(file.getAbsolutePath());
                            sharingIntent.setType("*/*");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                            startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        }
                    } catch (Exception e) {
                        Log.i("Error in Sharing ",e.getMessage());
                    }
                    Toast.makeText(c, "Exported", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Not Exported", Toast.LENGTH_SHORT).show();
                    Log.d("Main", e.toString());
                }

            } catch (Exception e) {

            }

            return true;
        }
        if (id == R.id.action_resetall) {
            /*ToDo comment later, before upload, 6th Sept 2021*/

            /*Intent i = new Intent(this, AndroidDatabaseManager.class);
            startActivity(i);*/

            try {
                //clearApplicationData();
                clearAppData();
            } catch (Exception e) {

            }

            return true;
        }

        if (id == R.id.action_setalram) {
            /*ToDo comment later, before upload, 6th Sept 2021*/

            /*Intent i = new Intent(this, AndroidDatabaseManager.class);
            startActivity(i);*/

            try {
                Intent i = new Intent(this, SetAlarmActivity.class);
                startActivity(i);
            } catch (Exception e) {

            }

            return true;
        }

        if (id == R.id.action_exportdata) {
            try {

                ExportDbToCsv mExportDbToCsv = new ExportDbToCsv();
                mExportDbToCsv.exportWholeDb(context, new SqliteDatabase(context), "MDOApps");
                //           dbHelper = new  mExportDbToCsv.exportWholeDb(UserHome.this, null, "1#myactivitydb#");
            } catch (Exception e) {

            }

            return true;
        }
        if (id == R.id.action_rbmdownload) {
            /*ToDo comment later, before upload, 6th Sept 2021*/

            /*Intent i = new Intent(this, AndroidDatabaseManager.class);
            startActivity(i);*/

            try {
                //clearApplicationData();

                Intent intent = new Intent(context, DownloadMasterdataRBM.class);
                startActivity(intent);

                //   CCF 2.0 Module code for Integration
             /*   Intent intent = new Intent(this, CCFFirstActivity.class);
                intent.putExtra("ccfUserId", "1");
                intent.putExtra("ccfToken", "addd");
                intent.putExtra("ccfContactNo", "9420181669");
                intent.putExtra("ccfTBMOrRBMCode", "97260647");
//ccfUserRoleID == 2 RBM, ccfUserRoleID == 4 TBM ,
// ccfUserRoleID == 5 ZBM, ccfUserRoleID == 7 ZMM
                intent.putExtra("ccfUserRoleID", "4");
                startActivity(intent);*/


            } catch (Exception e) {

            }

            return true;
        }
        if (id == R.id.action_newupload) {
            try {
                Intent intent = new Intent(context, UploadDataNew.class);
                startActivity(intent);
            } catch (Exception e) {

            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDB() {
        SQLiteDatabase sampleDB = this.openOrCreateDatabase("Sumit_MyActivity.db", MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS mahycoDev (LastName VARCHAR, FirstName VARCHAR," +
                " Rank VARCHAR);");
        sampleDB.execSQL("INSERT INTO mahycoDev Values ('Kirk','James, T','Captain');");
        sampleDB.close();
        sampleDB.getPath();
        Toast.makeText(this, "DB Created @ " + sampleDB.getPath(), Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // Fragment fragment = null;
        Class fragmentClass = null;
        try {
            // ProductDetail productdetail = null;
            if (id == R.id.nav_Logout) {
                //Log out

                //editor.clear();
                editor.putString("UserID", null);
                editor.commit();
                //finish();
                //System.exit(0);
                logLogOutEvent();
                Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
            } else if (id == R.id.nav_Profile) {
              /*  Intent intent = new Intent(this.getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);*/
                logProfileEvent();
                Toast.makeText(getApplicationContext(), "Working Progress", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this.getApplicationContext(), ActivityTravelReportTriggered.class);
                this.startActivity(intent);
            } else if (id == R.id.nav_share) {

                // Intent i = new Intent(UserHome.this, AndroidDatabaseManager.class);
                //startActivity(i);
                logAboutAppEvent();
                Toast.makeText(getApplicationContext(), "Working Progress", Toast.LENGTH_SHORT).show();

            }


            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.FragmentContainer, fragment).addToBackStack(null).commit();


            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            // return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    public void MDO_TravelData() {
        //if(config.NetworkConnection()) {
        // dialog.setMessage("Loading. Please wait...");
        //dialog.show();
        String str = null;
        String Imagestring1 = "";
        String Imagestring2 = "";
        String ImageName = "";
        Cursor cursor = null;
        String searchQuery = "";
        int count = 0;
        searchQuery = "select * from mdo_starttravel where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_endtravel where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_addplace where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();


        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select * from mdo_starttravel where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from mdo_endtravel where Status='0'";
                    object.put("Table2", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from mdo_addplace where Status='0'";
                    object.put("Table3", mDatabase.getResults(searchQuery));

                    searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
                    object.put("Table4", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from mdo_retailerproductdetail where Status='0'";
                    object.put("Table5", mDatabase.getResults(searchQuery));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                str = new UploadDataServer("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "").execute(cx.MDOurlpath).get();
                //End
                if (str.contains("True")) {
                    // msclass.showMessage("Records Uploaded successfully");

                } else {
                    //msclass.showMessage(str);

                }


            } catch (Exception ex) {
                // msclass.showMessage(ex.getMessage());


            }
        } else {

        }

    }

    public void UploadFarmerData(String MDOFarmerMasterdataInsert) {
        //if(config.NetworkConnection()) {
        // dialog.setMessage("Loading. Please wait...");
        //dialog.show();
        String str = null;
        String Imagestring1 = "";
        String Imagestring2 = "";
        String ImageName = "";

        String searchQuery = "select * from FarmerMaster where Status='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();


        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select * from FarmerMaster where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                str = new UploadDataServer(MDOFarmerMasterdataInsert, objAsBytes, Imagestring1, Imagestring2, ImageName, "").execute(cx.MDOurlpath).get();
                //End
                if (str.contains("True")) {

                } else {

                }


            } catch (Exception ex) {
                // msclass.showMessage(ex.getMessage());


            }
        } else {

        }

    }

    public void UploadalldataInnovation(String Functionname) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from InnovationData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                searchQuery = "select * from InnovationData where Status='0' and Imgname='" + ImageName + "' limit 1 ";
                                object.put("Table1", mDatabase.getResults(searchQuery));
                                Imagestring1 = mDatabase.getImageData(searchQuery, "imagepath");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            new UploadDataServer(Functionname, objAsBytes, Imagestring1, Imagestring2, ImageName, "t").execute(cx.MDOurlpath);

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    } catch (Exception ex) {  // dialog.dismiss();
                        // msclass.showMessage(ex.getMessage());


                    }
                } else {
                    //dialog.dismiss();
                    // msclass.showMessage("Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                //  msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            // msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void UploadalldataActvityNEW(String tagdatauploadMDONew) {
        if (config.NetworkConnection()) {
            //dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            String Imagestring1 = "";
            String Imagestring2 = "";
            String ImageName = "";
            try {
                String searchQuery = "select  *  from TagData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {
                    //START
                    byte[] objAsBytes = null;//new byte[10000];
                    JSONObject object = new JSONObject();
                    try {
                        searchQuery = "select * from TagData where Status='0'";
                        object.put("Table1", mDatabase.getResults(searchQuery));
                        Imagestring1 = "";
                        mDatabase.getImageData(searchQuery, "Outcoordinate");
                        Imagestring2 = "";// mDatabase.getImageData(searchQuery, "OutLocation");
                        searchQuery = "select * from Tempstockdata where status='1'";
                        object.put("Table2", mDatabase.getResults(searchQuery));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        objAsBytes = object.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    cursor.close();
                    //new UploadDataServernew(tagdatauploadMDONew, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(cx.MDOurlpath);

                    UploadDataON(tagdatauploadMDONew, objAsBytes, Imagestring1,
                            Imagestring2, ImageName, Intime, cx.MDOurlpath);


                }
                cursor.close();
                //End
            } catch (Exception e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage());
            }

        }

    }

    public void UploadalldataActvity(String tagdatauploadMDONew) {
        if (config.NetworkConnection()) {
            //dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            String Imagestring1 = "";
            String Imagestring2 = "";
            String ImageName = "";


            try {
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from TagData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                searchQuery = "select * from TagData where Status='0' and Imgname='" + ImageName + "' limit 1 ";
                                object.put("Table1", mDatabase.getResults(searchQuery));
                                Imagestring1 = mDatabase.getImageData(searchQuery, "Outcoordinate");
                                Imagestring2 = mDatabase.getImageData(searchQuery, "OutLocation");
                                Intime = mDatabase.getInamename(searchQuery, "InTime");
                                // Stock Data
                                searchQuery = "select * from Tempstockdata where status='1' and INTime='" + Intime + "' ";
                                object.put("Table2", mDatabase.getResults(searchQuery));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new UploadDataServer(tagdatauploadMDONew, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(cx.MDOurlpath);


                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                   /* if(str.contains("True")) {

                        dialog.dismiss();
                    }
                    else
                    {
                        msclass.showMessage(str);
                        dialog.dismiss();
                    } */

                    } catch (Exception ex) {

                        msclass.showMessage(ex.getMessage());


                    }
                } else {

                    //msclass.showMessage("Data not available for Uploading ");
                    // dialog.dismiss();

                }
            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
            }

        }

    }

    public class UploadDataServer extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;

        public UploadDataServer(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;

        }

        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            String Urlpath = urls[0];

            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";

            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());


            } catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());

            }


            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);

                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Imgname='" + ImageName + "'");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where INTime='" + Intime + "'");
                        // mDatabase.deleterecord("delete from TagData where Imgname='"+ImageName+"'");

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsertNew")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");

                    }
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");

                    }
                    if (Funname.equals("MDO_TravelData")) {
                        mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                        mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'" + strdate + "'");
                        mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'" + strdate + "'");
                        mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'" + strdate + "'");
                        mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                        mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");

                    }
                } else {
                    msclass.showMessage(result + "--E");

                }


            } catch (Exception e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());

            }

        }
    }

    public class UploadDataServernew extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;

        public UploadDataServernew(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;

        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));
            String Urlpath = urls[0];
            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";
            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());


            } catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());

            }


            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");

                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        String strdate = dateFormat.format(d);
                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' and imgstatus='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Status='0'");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'");
                        // mDatabase.deleterecord("delete from TagData where Imgname='"+ImageName+"'");

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsertNew")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");

                    }
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");

                    }
                } else {
                    msclass.showMessage(result + "--E");

                }


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());

            }

        }
    }

    public void downloadphotopath(String mdo_photoupdate) {
        if (config.NetworkConnection()) {
            String str = null;
            String uid = preferences.getString("UserID", null).trim();
            try {
                mDatabase.deleledata("mdo_photoupdate", " ");
                new MDOMasterData(6, uid, "blank", UserHome.this).execute(cx.MDOurlpath);
            } catch (Exception ex) {

            }
        }
    }

    private class MDOMasterData extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String password;
        private ProgressDialog p;
        private Context ctx;
        public String returnstring;

        public MDOMasterData(int action, String username, String password, Context ctx) {
            this.action = action;
            this.username = username;
            this.password = password;
            this.p = new ProgressDialog(ctx);
        }

        @Override
        protected void onPreExecute() {
            // dialog.setMessage("Loading....");
            //dialog.show();
            super.onPreExecute();
            //p.setMessage("Downloading ");
            //p.setIndeterminate(false);
            // p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //p.setCancelable(false);
            // p.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDOVerify_user"));
            // postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1 = cx.MDOurlpath + "?username=" + username.trim() + "&sapcode=" + action + "&password=" + password + "";
            HttpPost httppost = new HttpPost(Urlpath1);
            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring = builder.toString();

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();


            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();


            }
            // p.dismiss();
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            progressBarVisibility();
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                JSONArray jArray4 = object.getJSONArray("Table");
                boolean f1;
                if (jArray4.length() > 0) {
                    f1 = mDatabase.InsertMyphotodata(jArray4);
                }
                progressBarVisibility();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarVisibility();

            }

        }
    }

    //NEW

    class UploadDataONServernew extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        ProgressDialog progressDialog;

        public UploadDataONServernew(String tag) {
            this.tag = tag;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            UploadalldataActvityNEW("tagdatauploadMDONew_Testold");

            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            progressBarVisibility();
            try {
              /*  relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Wait");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });*/
                progressBar.setIndeterminate(true);
                downloadphotopath("mdo_photoupdate");
            } catch (Exception ex) {
                progressBarVisibility();
            }
        }
    }


    public synchronized void UploadDataON(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime, String urls) {

        try {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);

            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));


            //String Urlpath = urls + "?ImageName=" + ImageName+"&ImageName2=" + ImageName;
            String Urlpath = urls;
            Log.d("rohit", "doInBackground: " + Urlpath);
            Log.d("rohit", "doInBackground:params::: " + postParameters);
            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        //builder.append(line).append("\n");
                        builder.append(line);
                    }

                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            try {
                handleImageSyncResponse(Funname, builder.toString().trim(), ImageName, "");

            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void handleImageSyncResponse(String function, String resultout, String ImageName, String id) {
        try {


            if (resultout.contains("True")) {

                if (function.equals("tagdatauploadMDONew_Testold")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);
                    // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                    mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' and imgstatus='1' ");
                    mDatabase.Updatedata("update TagData  set Status='1' where Status='0'");
                    mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'");

                }
                if (function.equals("MDOFarmerMasterdataInsertNew")) {
                    mDatabase.deleterecord("delete from FarmerMaster");
                    // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");
                    // pd.dismiss();
                }
                if (function.equals("InnovationData")) {
                    mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");
                    // pd.dismiss();
                }
            } else {
                msclass.showMessage(resultout + "--E");
                // pd.dismiss();
            }

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);

    }


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                if (config.NetworkConnection()) {


                    getOrderDetailFromDB();
                    //     Log.d(TAG, "onReceive:avail ");
                } else {
                    //   Log.d(TAG, "onReceive: ");
                }
            }
        }
    };

    public void getOrderDetailFromDB() {
        SqliteDatabase databaseAdapter = new SqliteDatabase(context);

        JSONObject jsonObject = databaseAdapter.getJson("");
        if (jsonObject != null)
            new SyncPaymentStatus(0, jsonObject).execute();
        //    getReqParamsForPayment(databaseAdapter.getJson(""));

    }


    private class SyncPaymentStatus extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        JSONObject jsonObject;
        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);


        public SyncPaymentStatus(int action, JSONObject jsonObject) {

            this.action = action;
            this.jsonObject = jsonObject;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(UserHome.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);

            try {
                postParameters.add(new BasicNameValuePair("Type", "SendPaymentDetails"));
                postParameters.add(new BasicNameValuePair("rzpPaymentId", jsonObject.getString("rzpPaymentId")));
                postParameters.add(new BasicNameValuePair("usercode", jsonObject.getString("usercode")));
                postParameters.add(new BasicNameValuePair("farmersList", jsonObject.getString("farmersIdList")));
                postParameters.add(new BasicNameValuePair("status", jsonObject.getString("status")));
                postParameters.add(new BasicNameValuePair("amount", jsonObject.getString("amount")));

                String Urlpath1 = cx.MDOurlpath + "?action=" + "";
                HttpPost httppost = new HttpPost(Urlpath1);
                httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
                Log.d("rohitt", "doInBack:DB " + postParameters.toString());

                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();

                pd.dismiss();
            } catch (Exception e) {

                e.printStackTrace();
                pd.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                Log.d("SyncPaymentStatus", "onPostExecute: " + result);
                pd.dismiss();
                if (result.contains("Table")) {
                    //    setAnimation();
                    // farmerPaymentModelArrayList.clear();
                    JSONObject object = new JSONObject(result);
                    JSONArray jArray = object.getJSONArray("Table");
                    double amount = 0;
                    // for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(0);
                    String orderId = jObject.getString("orderID");
                    String paymentID = jObject.getString("paymentID");

                    SqliteDatabase database = new SqliteDatabase(context);
                    database.updatePaymentTable(paymentID, orderId);

                } else {
                    Utility.showAlertDialog("Error", result, context);
                    //  setAnimation();
                }
                //   lblusername.setText(result) ;


            } catch (Exception e) {
                e.printStackTrace();
                Utility.showAlertDialog("Error1", result, context);
                pd.dismiss();
                //     setAnimation();
            }

        }
    }

    private void logAppOpenEvent() {
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callOnLoadEvent(userId, displayName);
            }
        }
    }

    private void logProfileEvent() {
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callViewProfileEvent(userId, displayName);
            }
        }
    }

    private void logAboutAppEvent() {
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callAboutAppEvent(userId, displayName);
            }
        }
    }

    private void logLogOutEvent() {
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callLogoutEvent(userId, displayName);
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                boolean isFeedbackGiven = intent.getBooleanExtra("IS_FEEDBACK_GIVEN", false);
                if (isFeedbackGiven == true) {
                    //mDatabase.insertUserFeedback(userId, "1");
                    //Toast.makeText(home.this, "IS_FEEDBACK_GIVEN: " + isFeedbackGiven, Toast.LENGTH_LONG).show();
                    Log.d("MAA_Feed", "IS_FEEDBACK_GIVEN : " + isFeedbackGiven);
                    if (isFeedbackGiven) {
                        saveFeedStatusLocally();
                    }
                }
            }
        }
    };

    // Check Version Class is Added to check feedback module is active or not
    // Date : 14-11-2022


    private class CheckVersion extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(context);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
           // Toast.makeText(context, "Entered", Toast.LENGTH_SHORT).show();

            //UI Element
            //   uiUpdate.setText("Output : ");
            //  Dialog.setMessage("Please Wait..");
            // Dialog.show();
            //pb.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {

                Log.d("Url", urls[0]);
                Log.d("Url", urls[0]);
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.

                // Server url call by GET method
                HttpPost httpget = new HttpPost(urls[0]);
                //     httpget.setHeader("Authorization", "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);

            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            //Dialog.dismiss();

            if (Error != null) {

                //  uiUpdate.setText("Output : "+Error);

            } else {
                //pb.setVisibility(View.GONE);
                //   uiUpdate.setText("Output : "+Content);
                // loadFromServer(Content.toString().trim());
                Log.i("Details123", "" + Content);
                //   Toast.makeText(getApplicationContext(), ""+Content, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jsonVersionDetails = new JSONObject(Content.trim());
                    String vcode = BuildConfig.VERSION_NAME;

                    if (jsonVersionDetails.getBoolean("success")) {

                        if (!(vcode.trim().equals(jsonVersionDetails.getString("AppVersion").trim()))) {
                            showUpdateDialog();
                        }
                        if (jsonVersionDetails.getInt("UserStatus") == 0) {

                            new androidx.appcompat.app.AlertDialog.Builder(UserHome.this)
                                    .setMessage("Session Expired . Please login again.")
                                    .setTitle("Information")
                                    .setPositiveButton("Login Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putString("UserID", null);
                                            editor.commit();
                                            //finish();
                                            //System.exit(0);
                                            logLogOutEvent();
                                            Intent intent = new Intent(UserHome.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                            return;
                        }


                        if (jsonVersionDetails.getBoolean("IsFeedbackStatus")) {
                            //  showUpdateDialog();
                            //  Toast.makeText(context, "CheckFeedback Given.", Toast.LENGTH_SHORT).show();
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            CommonExecution cxx = new CommonExecution(context);
                            String json = cxx.new CheckFeedbackStatus(1, userId, "" + year).execute().get();
                            Log.i("Feedback Status", json + " Year:" + year);
                            try {
                                JSONObject jsonObject = new JSONObject(json.trim());
                                if (jsonObject.getBoolean("success")) {
                                    if (!(jsonObject.getBoolean("IsFeedbackGiven"))) {
                                        showFeedbackScreen(userId);
                                    }
                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                        }
                        if (jsonVersionDetails.getBoolean("DescriptionStatus")) {
                            android.app.Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            dialog.setContentView(R.layout.dialog_notification);
                            WebView web = dialog.findViewById(R.id.web);
                            Button btn_close = dialog.findViewById(R.id.btn_close);
                            btn_close.setEnabled(false);
                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            web.getSettings().setJavaScriptEnabled(true);
                            web.getSettings().setBuiltInZoomControls(true);
                            web.getSettings().setDisplayZoomControls(false);
                            web.setWebChromeClient(new WebChromeClient());
                            web.loadUrl(jsonVersionDetails.getString("Description"));

                            web.setWebChromeClient(new WebChromeClient() {
                                public void onProgressChanged(WebView view, int progress) {
                                    //Make the bar disappear after URL is loaded, and changes string to Loading...
                                    setTitle("Loading...");
                                    setProgress(progress * 100); //Make the bar disappear after URL is loaded

                                    // Return the app name after finish loading
                                    if (progress == 100)
                                        setTitle("My Activity");
                                }
                            });

                            web.setWebViewClient(new WebViewClient() {
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // do your handling codes here, which url is the requested url
                                    // probably you need to open that url rather than redirect:
                                    view.loadUrl(url);
                                    return false; // then it is not handled by default action
                                }
                            });

                            web.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                        WebView webView = (WebView) v;

                                        switch (keyCode) {
                                            case KeyEvent.KEYCODE_BACK:
                                                if (webView.canGoBack()) {
                                                    webView.goBack();
                                                    return true;
                                                }
                                                break;
                                        }
                                    }

                                    return false;
                                }
                            });

                            new CountDownTimer(10000, 1000) {
                                int count = 1;

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO Auto-generated method stub
                                    btn_close.setText("" + millisUntilFinished / 1000);
                                    count++;
                                }

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub
                                    btn_close.setText("Close");
                                    btn_close.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close_24, 0);
                                    btn_close.setEnabled(true);
                                    // dialog.dismiss();
                                }
                            }.start();


                            dialog.show();
                        }
                    } else  //  Coming False from the Version API
                    {
                        Toast.makeText(context, "" + jsonVersionDetails.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.i("Error is ", e.getMessage());
                }
            }
        }

    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A new update is available.");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=myactvity.mahyco")));
                dialog.dismiss();
            }
        });

        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });*/

        builder.setCancelable(false); //Update 17 Jan. 2022
        dialog1 = builder.show();
    }

     private void showTBMKAList(String Message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify your KA list !");
        builder.setMessage(""+Message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               Intent intent=new Intent(context,TBMWiseMdoList.class);
               startActivity(intent);
            }
        });

        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });*/

        builder.setCancelable(false); //Update 17 Jan. 2022
        dialog1 = builder.show();
    }

}
