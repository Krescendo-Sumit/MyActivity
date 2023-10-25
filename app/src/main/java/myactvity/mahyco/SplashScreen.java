package myactvity.mahyco;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.helper.SqliteDatabase;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NOTIFICATION_POLICY;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG ="MAA" ;
    // Splash screen timer
    SharedPreferences pref,locdata;
    SharedPreferences.Editor editor,locedit;
    private static int SPLASH_TIME_OUT = 3000;
    TextView version,lblheader;
    String vername;
    ImageView imgLogo;
    private SqliteDatabase mDatabase;
    public static final int RequestPermissionCode = 112;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        locedit= locdata.edit();
        version=(TextView)findViewById(R.id.version);
        lblheader=(TextView)findViewById(R.id.lblheader);
        imgLogo=(ImageView) findViewById(R.id.imgLogo);
        getSupportActionBar().hide(); //<< this
        setTitle("MY ACTIVITY");

        Animation a = AnimationUtils.loadAnimation(this, R.anim.textanim);
        a.reset();
        //a.setDuration(700);
        lblheader.clearAnimation();
        lblheader.startAnimation(a);
        //DB Crration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            AppConstant.dbnamepath =this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db";
            Log.d("Rajshri","DB_PATH : "+this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db");
        }
        else
        {
            AppConstant.dbnamepath="/mnt/sdcard/MDOApps.db";
        }
       // mDatabase = SqliteDatabase.getInstance(this);
        // Picasso.with(this).load(images[position]).fit() // resizes the image to these dimensions (in pixel)
        //        .centerCrop()
        //      .into(imgLogo);

        try {

            vername=getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        version.setText("Version :"+vername+"");
        //Start
        if (Build.VERSION.SDK_INT < 23) {

           // mDatabase.close() ;
            new Handler().postDelayed(new Runnable() {

             /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            // flag = checkAndRequestPermissions();

           // mDatabase.close() ;
            if (CheckingPermissionIsEnabledOrNot()) {
                // Toast.makeText(SplashScreen.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

             /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);

            }

            // If, If permission is not enabled then else condition will execute.
            else {

                //Calling method to enable permission.
                //int permission = ContextCompat.checkSelfPermission(this,
                //      android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                RequestMultiplePermission();


            }
        }
        // end
       /* if (Build.VERSION.SDK_INT < 23) {

        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission

            }
        }
        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT); */

        //movingdatabase();
    }

    public void movingdatabase()
    {


        String pathToOurFile = "/mnt/sdcard/MDOApps";
        File folder1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String   dbpath = folder1.getAbsolutePath()+"/MDOAppsnew.db";///mnt/sdcard/FarmMech";

        // your sd card
        String sdCard = Environment.getExternalStorageDirectory().toString();

        // the file to be moved or copied
        File sourceLocation = new File (pathToOurFile);

        // make sure your target location folder exists!
        File targetLocation = new File (dbpath);

        // just to take note of the location sources
        Log.v(TAG, "sourceLocation: " + sourceLocation);
        Log.v(TAG, "targetLocation: " + targetLocation);

        File file1=null;
        file1 = this.getDatabasePath(pathToOurFile);
        if (file1.exists()) {

            if(sourceLocation.renameTo(targetLocation)){
                file1.delete();
                Log.v(TAG, "Move file successful.");
            }else{
                Log.v(TAG, "Move file failed.");
            }

        }
    }
    //Permission function starts from here
    private void RequestMultiplePermission() {

        try {
            // Creating String Array with Permissions.
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            READ_EXTERNAL_STORAGE

                            , ACCESS_FINE_LOCATION
                            , ACCESS_COARSE_LOCATION
                            , INTERNET
                            //, RECEIVE_SMS
                            //, READ_SMS
                            , ACCESS_NOTIFICATION_POLICY,
                            WRITE_EXTERNAL_STORAGE,
                            CAMERA,
                             READ_PHONE_STATE,
                            READ_MEDIA_IMAGES,
                            READ_MEDIA_AUDIO,
                            READ_MEDIA_VIDEO



                    }, RequestPermissionCode);
        }
        catch (Exception ex)
        {
            Toast.makeText(SplashScreen.this,ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        try {


            switch (requestCode) {

                case RequestPermissionCode:

                    if (grantResults.length > 0) {


                        boolean READEXTERNALSTORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                        boolean ACCESS_FINE_LOCATION = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ACCESSCOARSELOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean INTERNET = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                       // boolean RECEIVESMS = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                      //  boolean READSMS = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                        boolean ACCESSNOTIFICATIONPOLICY = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        boolean WRITEEXTERNALSTORAGE = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                        boolean CameraPermission = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                        boolean READPHONESTATE = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                        READEXTERNALSTORAGE= grantResults[8] == PackageManager.PERMISSION_GRANTED;;
                        WRITEEXTERNALSTORAGE= grantResults[9] == PackageManager.PERMISSION_GRANTED;;
                        WRITEEXTERNALSTORAGE= grantResults[10] == PackageManager.PERMISSION_GRANTED;;

                        String str="";

                                str=str+ "1-"+READEXTERNALSTORAGE;
                                str=str+ "2-"+ACCESS_FINE_LOCATION;
                                str=str+ "3-"+ACCESSCOARSELOCATION;
                                str=str+ "4-"+INTERNET;
                                str=str+ "5-"+ACCESSNOTIFICATIONPOLICY;
                                str=str+ "6-"+WRITEEXTERNALSTORAGE;
                                str=str+ "7-"+CameraPermission;
                                str=str+ "8-"+READPHONESTATE;
                        Log.i("Result ",str);

                        int version = android.os.Build.VERSION.SDK_INT;
                        Log.i("Version ",""+version);
                        if(version>32)
                        {
                            READEXTERNALSTORAGE= grantResults[8] == PackageManager.PERMISSION_GRANTED;;
                            WRITEEXTERNALSTORAGE= grantResults[9] == PackageManager.PERMISSION_GRANTED;;
                            WRITEEXTERNALSTORAGE= grantResults[10] == PackageManager.PERMISSION_GRANTED;;
                        }else
                        {
                            READEXTERNALSTORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                            WRITEEXTERNALSTORAGE = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                        }
                        if (CameraPermission && READEXTERNALSTORAGE
                                && ACCESSCOARSELOCATION && ACCESS_FINE_LOCATION && INTERNET
                                && ACCESSNOTIFICATIONPOLICY && WRITEEXTERNALSTORAGE && READPHONESTATE) {

                            new Handler().postDelayed(new Runnable() {


                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    // close this activity
                                    finish();
                                }
                            }, SPLASH_TIME_OUT);

                            // Toast.makeText(BarcodeAcitivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SplashScreen.this, "Permission Denied", Toast.LENGTH_LONG).show();

                        }
                    }

                    break;
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(SplashScreen.this,ex.toString(),Toast.LENGTH_LONG).show();
        }

    }
    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {


        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int PermissionResult4 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int PermissionResult5 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int PermissionResult6 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        int PermissionResult8 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int PermissionResult9 = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int PermissionResult10 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int PermissionResult11 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_NOTIFICATION_POLICY);
        int PermissionResult12 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                PermissionResult4 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult5 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult6 == PackageManager.PERMISSION_GRANTED &&

                PermissionResult8 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult9 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult10 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult11 == PackageManager.PERMISSION_GRANTED &&
                PermissionResult12 == PackageManager.PERMISSION_GRANTED;



    }
    private boolean checkAndRequestPermissions() {

        int storagePermission = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.READ_EXTERNAL_STORAGE);



        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.READ_PHONE_STATE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int USE_FINGERPRINT = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.USE_FINGERPRINT);
        int INTERNET = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET);

        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (INTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (USE_FINGERPRINT != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.USE_FINGERPRINT);
        }

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        else
        {
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            // close this activity
            finish();
        }
        return true;
    }
}
