package myactvity.mahyco;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.mahyco.feedbacklib.view.DialogFeedback;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

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
    TextView lblwelcome,myTextProgress;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private Handler handler = new Handler();
    String userId;
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
            AppConstant.dbnamepath =this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db";
            Log.d("Rajshri","DB_PATH : "+this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db");
        }
        else
        {
            AppConstant.dbnamepath="/mnt/sdcard/MDOApps.db";
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


        lblwelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(UserHome.this, AndroidDatabaseManager.class);
//                startActivity(i);
            }
        });

        logAppOpenEvent();

        //testCrash();

        /*TODO Uncomment when App Feedback Module required.*/
        showUserFeedbackScreen(userId);
    }

    private void showUserFeedbackScreen(String user) {
        Prefs mPref = Prefs.with(UserHome.this);
        String userId=user.trim();
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


    void testCrash(){
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
        int id = 2 ;//item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*ToDo comment later, before upload, 6th Sept 2021*/

            Intent i = new Intent(this, AndroidDatabaseManager.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            try
            {
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
            }
            catch (Exception ex)
            {
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

    private void logAppOpenEvent(){
        if(preferences!=null){
        String userId="", displayName="";
        if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
            userId = preferences.getString("UserID", "");
            displayName = preferences.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callOnLoadEvent(userId,displayName);
        }
        }
    }

    private void logProfileEvent(){
        if(preferences!=null){
            String userId="", displayName="";
        if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
            userId = preferences.getString("UserID", "");
            displayName = preferences.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callViewProfileEvent(userId,displayName);
        }
        }
    }

    private void logAboutAppEvent(){
        if(preferences!=null){
            String userId="", displayName="";
        if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
            userId = preferences.getString("UserID", "");
            displayName = preferences.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callAboutAppEvent(userId,displayName);
        }
        }
    }

    private void logLogOutEvent(){
        if(preferences!=null){
            String userId="", displayName="";
        if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
            userId = preferences.getString("UserID", "");
            displayName = preferences.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callLogoutEvent(userId,displayName);
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

}
