package myactvity.mahyco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.DatabaseHelper;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class VocpvegetablewebviewActivity extends AppCompatActivity implements LocationListener {
    Prefs mPref;
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ConstraintLayout container;
    private long mLastClickTime = 0;
    private Handler handler = new Handler();
    private Context context;
    public ProgressDialog pd;
    Config config;
    WebView webview;
    String usercode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    // Toolbar toolbar;
    protected LocationManager locationManager;
    Location location_network;
    Location location_gps;
    protected static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    double Latitude, Longitude;
    public String newlat;
    public String newlng;

    DatabaseHelper myDb;
    List<LatLng> points;
    boolean isNewLatLng = false, isFirstTime = true;
    WebView webView;
    AlertDialog.Builder builder;
    Intent intent;
    public Criteria criteria;
    int locationMode = 0;
    String FormName="";
    private FusedLocationProviderClient fusedLocationClient;
    private static final String[] requiredPermissions = new String[]
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().hide();
        // super.onCreate(savedInstanceState);
        //setContentView(R.layout.vocpegetablewebview);
        //getSupportActionBar().hide();

        context = this;
        pd = new ProgressDialog(context);
        mPref = Prefs.with(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        msclass = new Messageclass(this);
        config = new Config(this); //Here the context is passing
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = findViewById(R.id.container);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        usercode = pref.getString("UserID", null);
        FormName=getIntent().getExtras().getString("FormName");
        if(FormName!=null)
          FormName=getIntent().getExtras().getString("FormName");

        points = new ArrayList<LatLng>();
        boolean istrue = mPref.getBoolean(Constants.LOCAL_userencrptionVOCP, false);
  /*  if (istrue == true) {
        //   getCurrentLocation();
           callvocpfunction();
    }
    else
    {*/
        new GetEncryandDecyCode().execute();
        //       }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object

                    newlat = ""+location.getLatitude();
                    newlng = ""+location.getLongitude();

                     //     Toast.makeText(context, "Location Latitude : " + location.getLatitude() + " Longitude :" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    //  edGeoTagging.setText(location.getLatitude() + "," + location.getLongitude());
                }
            }
        });

    }


    public void callvocpfunction(String Urls) {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDomStorageEnabled(true);

        String encyptusercode = mPref.getString(Constants.userencrytion, "false");

        webView.loadUrl(Urls);
        //  OLD CODE COMMENTED
        //     webView.loadUrl("https://dt.mahyco.com/?UserCode="+encyptusercode);
        Log.i("URLS", Urls);
        // webView.loadUrl("file:///android_asset/web.html");
        webView.setWebChromeClient(new WebChromeClient() {
        });


        webView.addJavascriptInterface(new JavaScriptInterface(newlat, newlng, this), "Android");

    }

    public void getCurrentLocation() {

        builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        criteria = new Criteria();
        locationMode = getLocationMode(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //  if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        if (locationMode == 0) {
            new AlertDialog.Builder(context)
                    .setTitle("GPS not found")  // GPS not found
                    .setMessage("Kindly click OK to enable your GPS Location and then set Location mode to Device Only") // Want to enable?
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            builder.setMessage("Tip");
            builder.setPositiveButton("Get Permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

                }
            });
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //   Toast.makeText(this, "Hiiiiiiiiii", Toast.LENGTH_SHORT).show();
        }
        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE); //setAccuracyは内部では、https://stackoverflow.com/a/17874592/1709287の用にHorizontalAccuracyの設定に変換されている。
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAltitudeRequired(false);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
            criteria.setBearingRequired(false);

            //API level 9 and up
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
            //criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
            // criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
            Log.d("locationMode:: ", locationMode + "");
            if (locationMode != 0) {
                //  if (locationMode == 1) {

                locationManager.requestLocationUpdates(0, (float) 0, criteria, (LocationListener) this, null);
//                } else  {
//                    new AlertDialog.Builder(context)
//                            .setTitle("Change Location mode")  // GPS not found
//                            .setMessage("Kindly click OK to set Location mode to Device Only") // Want to enable?
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                }
//                            })
//                            .setNegativeButton("Cancel", null)
//                            .show();
//                }
            }

        } catch (Exception e) {
            Log.e("error::: ", e.toString());
        }
        if (location_gps != null) {
            //  onLocationChanged(location_gps);

        }

    }

    public int getLocationMode(Context context) {
        try {
            return Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void getPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
                return false;
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged:", location.toString());
        if (location != null) {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            newlat = String.valueOf(Latitude);
            newlng = String.valueOf(Longitude);

            if (newlat != null && newlng != null) {
                //  Toast.makeText(MainActivity.this, "Android newLatt: " + newlat + " newLnggg:: " + newlng, Toast.LENGTH_LONG).show();
            }

            if (isFirstTime != true && isNewLatLng != true) {
                //  addData();
            }
            isFirstTime = false;
            isNewLatLng = false;
            locationManager.removeUpdates(this);
            locationManager = null;
        }
    }

    private void notifyLocationProviderStatusUpdated(boolean isLocationProviderAvailable) {
        //Broadcast location provider status change here
        // Toast.makeText(MainActivity.this, "isLocationProviderAvailable: " + isLocationProviderAvailable, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                notifyLocationProviderStatusUpdated(false);
            } else {
                notifyLocationProviderStatusUpdated(true);
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            notifyLocationProviderStatusUpdated(true);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            notifyLocationProviderStatusUpdated(false);
        }
    }

    @Override
    public void onBackPressed() {
        VocpvegetablewebviewActivity.this.finish();
    }


//    @Override
//    public void onPause() {
//        locationManager.removeUpdates(this);
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        locationManager.removeUpdates(this);
//        super.onDestroy();
//    }

    public boolean compareLatLng(String latPassed, String lngPassed) {
        getCurrentLocation();
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(Double.valueOf(latPassed));
        startPoint.setLongitude(Double.valueOf(lngPassed));
        Log.d("Passed newLat ", latPassed + " lng: " + lngPassed);
        Location endPoint = new Location("locationA");
        Log.d("compare newLat ", newlat + " lng: " + newlng);

        if (newlat != null && newlng != null) {
            endPoint.setLatitude(Double.valueOf(newlat));
            endPoint.setLongitude(Double.valueOf(newlng));
            final double distance = startPoint.distanceTo(endPoint);
            Log.d("distance:: ", String.valueOf(distance));

            if (distance <= 500) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Info")
                        .setMessage("Distance:: " + distance + " \n You are inside the field")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

                //  Toast.makeText(MainActivity.this, "Distance:: " + distance + "You are inside the field", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Info")
                        .setMessage("Distance:: " + distance + " \n You are outside the field")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                //  Toast.makeText(MainActivity.this, "Distance:: " + distance + "You are outside the field", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(VocpvegetablewebviewActivity.this, "There is some issue in fetching location", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    /*Changes START Added on 11th Oct 2021 ------------------------------------------------------------------------------------------*/

    private class GetEncryandDecyCode extends AsyncTask<String, String, String> {
        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;
        Context context;

        public GetEncryandDecyCode() {

        }

        protected void onPreExecute() {
            pd.setTitle("Data loading ...");
            pd.setMessage("Please wait.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                JSONObject obj = new JSONObject();
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("UserCode", usercode);
                jsonObject.put("AppName", "VOCPSurvey");
                jsonObject.put("FormName", FormName);
                //obj.put("Table", jsonObject);
                Log.d("Get Ency and Descpt ", "************ UPDATE API URL : ");
                Log.d("Get Ency and Descpt ", "************ UPDATE API JSON OBJECT : " + obj);
                Prefs mPref = Prefs.with(context);
                Log.d("Get Ency and Descpt ", "************ UPDATE API JSON OBJECT ACCESS_TOKEN_TAG : " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                result = HttpUtils.POSTJSON("https://dt.mahyco.com/api/survey/getRedirectURL", jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                Log.d("Get Ency and Descpt ", "************ UPDATE API RESPONSE : " + result);
                return result;
            } catch (Exception e) {
                Log.d("MSG", "MSG : " + e.getMessage());
            }
            return result;
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                String Urls = "";
                boolean resultfalg = false;
                //redirecttoRegisterActivity(resultout);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("ResultFlag")) {

                        resultfalg = (boolean) jsonObject.get("ResultFlag");
                        Urls = jsonObject.getString("RedirectURL");
                        mPref.save(Constants.userencrytion, jsonObject.get("UserCode").toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (pd != null) {
                    pd.dismiss();
                }
                Log.d("encryt and decrpt", "UPDATE API Response" + resultout);
                if (resultfalg) {
                    mPref.saveBoolean(Constants.LOCAL_userencrptionVOCP, true);
                    callvocpfunction(Urls);

                }
            } catch (Exception e) {
                Log.d("Get Ency and Descpt ", e.getMessage());
                msclass.showMessage("Something went wrong, please try again later");

            }

        }

    }

    public void redirecttoRegisterActivity(String result) {
        if (result.toLowerCase().contains("authorization")) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

            builder.setTitle("MyActivity");
            builder.setMessage("Your login session is  expired, please register user again. ");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    editor.putString("UserID", null);
                    editor.commit();
                    Intent intent1 = new Intent(context.getApplicationContext(), UserRegister.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);
                }
            });
            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }

}

class JavaScriptInterface {
    Context mContext;
    String lat = "", lng = "";

    JavaScriptInterface(Context c) {
        mContext = c;
    }

    JavaScriptInterface(String lat, String lng, Context c) {
        mContext = c;
        this.lat = lat;
        this.lng = lng;
    }

    @JavascriptInterface
    public String getFromAndroid() {
        String ar[] = new String[2];
        try {
            ((VocpvegetablewebviewActivity) mContext).getCurrentLocation();

            if (((VocpvegetablewebviewActivity) mContext).newlat != null && ((VocpvegetablewebviewActivity) mContext).newlng != null) {
                ar[0] = ((VocpvegetablewebviewActivity) mContext).newlat;
                ar[1] = ((VocpvegetablewebviewActivity) mContext).newlng;

                Toast.makeText(mContext, "newLatt: " + ar[0] + " newLnggg:: " + ar[1], Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(mContext, "There is some issue in fetching location", Toast.LENGTH_LONG).show();
                return "";
            }

            Log.d("JAVA newLat ", ar[0] + " lng: " + ar[1]);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Arrays.toString(ar);
    }

    @JavascriptInterface
    public Boolean inOutStatus(String latPassed, String lngPassed) {
        boolean status = false;
        try {
            status = ((VocpvegetablewebviewActivity) mContext).compareLatLng(latPassed, lngPassed);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }


}
