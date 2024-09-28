package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class VillageTaggingGTV extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {
    Context context;
    private static final String TAG = "VillageTagging";
    SearchableSpinner spFocusedVillages;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    public Messageclass msclass;
    EditText etVillageRadius;
    String userCode, villageName, villageCode;
    Button btnSubmit;
    ImageView imgBtnGps;
    TextView lblheader, tvCordinates, tvAddress;
    ProgressDialog dialog;
    private long mLastClickTime = 0;
    String cordinates = "";
    String address = "";

    String focusedVillage;
    String cordinatesmsg = "ADDRESS TAG : *";
    LinearLayout llFocussedVillages;


    private TextInputLayout tiOtherVillage;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS = 101;
    double lati;
    double longi;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    // String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    String SERVER = "https://maapackhousenxg.mahyco.com/api/postSeason/livePlantDisplayVillageData";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity;
    RadioButton radOtherActivity;
    Prefs mPref;
    ProgressDialog progressDialog;
    String InTime ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_village_tagging_gtv);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();

    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");

        mPref = Prefs.with(this);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        tiOtherVillage = (TextInputLayout) findViewById(R.id.tiOtherVillage);
        etVillageRadius = (EditText) findViewById(R.id.etVillageRadius);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        lblheader = (TextView) findViewById(R.id.lblheader);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        container = (ScrollView) findViewById(R.id.container);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        userCode = pref.getString("UserID", null);
        // userCode =  pref.getString("UserID", null);
        bindFocussedVillage();

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocusedActivity:

                        if (radFocusedActivity.isChecked()) {
                            // villageType = "focussed";
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);

                        }
                        break;

                }
            }
        });


        lblheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VillageTaggingGTV.this, AndroidDatabaseManager.class);
                startActivity(intent);


            }
        });


        spFocusedVillages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    villageCode = gm.Code().trim();
                    villageName = gm.Desc().trim();
                    Log.d("string", focusedVillage);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        onSubmitBtnClicked();
    }


    /**
     * <P>//Method is used to do API related work on submit button clicked</P>
     */
    private void onSubmitBtnClicked() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (config.NetworkConnection()) {
                    if (validation()) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        AlertDialog.Builder builder = new AlertDialog.Builder(VillageTaggingGTV.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Are you sure to submit data");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @SuppressLint("ClickableViewAccessibility")
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();


                                relPRogress.setVisibility(View.VISIBLE);
                                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        return true;
                                    }
                                });

                                dowork();

                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                container.setEnabled(false);
                                container.setClickable(false);


                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                } else {
                    Toast.makeText(context, "Please check internet connection.", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        saveToDb();
                    }
                });

                try {

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public boolean validation() {
//
        if (radFocusedActivity.isChecked() && spFocusedVillages.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Village", context);
            return false;
        }
        if (etVillageRadius.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter Village Radius.", context);
            return false;
        }
        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
            Utility.showAlertDialog("Info", "Please Tag The Village", context);
            return false;
        }
        if (cordinates.length() == 0) {
            Utility.showAlertDialog("Info", "GEO Tag not captured ,please check GPS connectivity ", context);
            return false;

        }
        return true;
    }


    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }


    public String uniqueId(String focussedVillage) {
        String village = "";
        if (spFocusedVillages.getSelectedItemPosition() != 0) {
            if (focussedVillage.contains("OTHER")) {
                village = "";
            } else {
                village = spFocusedVillages.getSelectedItem().toString();
            }
        } else {
            msclass.showMessage("Please select village");
        }
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);


        String uid = userCode + village + formattedDate;

        Log.d("UID", uid);
        return uid;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            startFusedLocationService();

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }


    }


    public void bindFocussedVillage() {
        spFocusedVillages.setAdapter(null);


        String str = null;
        try {
            String gtvtype = mPref.getString(AppConstant.GTVSELECTEDBUTTON, "");
            if (gtvtype.trim().equals("GTV")) {
                radOtherActivity.setVisibility(View.GONE);
                String vname = mPref.getString(AppConstant.GTVSelectedVillage, "");
                String vcode = mPref.getString(AppConstant.GTVSelectedVillageCode, "");
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Croplist.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                        "SELECT FOCUSED VILLAGE"));
                Croplist.add(new GeneralMaster(vcode, vname));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);
            } else {

                String searchQuery = "";
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Cursor cursor;
                searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster v where ((select TagCoordinates from FocusVillageGeoTagDtls where vil_code=v.vil_code)='null') order by vil_desc asc  ";
                Croplist.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                        "SELECT FOCUSED VILLAGE"));


                cursor = mDatabase.getReadableDatabase().
                        rawQuery(searchQuery, null);
                cursor.moveToFirst();

                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();

                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Toast.makeText(this, "OnPause called", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler = null;
        }
        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    //Stop location fuseApi
    public void stopFusedApi() {
        try {
            if (googleApiClient != null && (googleApiClient.isConnected())) {
                FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationListener) this);
                googleApiClient.disconnect();
            }
        } catch (Exception ex) {
            FirebaseCrash.report(ex);
            ex.printStackTrace(); // Ignore error

            // ignore the exception
        } finally {

            googleApiClient = null;
            locationRequest = null;
        }
    }

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            if (config.NetworkConnection()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    address = addresses.get(0).getAddressLine(0);
                    if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                        tvAddress.setText(address + "\n" + cordinates);
                        tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                    } else {

                        tvAddress.setText(address + "\n" + cordinates);
                        tvCordinates.setText(cordinatesmsg + "\n" + cordinates);

                    }
                }
            } else {
                tvAddress.setText(cordinates);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }


    //start fusedApi location
    private synchronized void startFusedLocationService() {
        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                IsGPSEnabled = true;
            } else {
                IsGPSEnabled = false;
            }
            if (IsGPSEnabled) {
                locationRequest = new LocationRequest();//.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(INTERVAL);
                locationRequest.setSmallestDisplacement(0f);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);

                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API).addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                try {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "startFusedLocationService: " + e.toString());
                }
                GpsEnabled = true;

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(VillageTaggingGTV.this);

                builder.setTitle("MyActivity");
                builder.setMessage("Please enable location and Gps");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fusedlocationRecieved = false;
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    Log.d(TAG, "Fused api connected: ");
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
                }

            } else {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                PendingResult result =
                        LocationServices.SettingsApi.checkLocationSettings(
                                googleApiClient,
                                builder.build()
                        );

                result.setResultCallback(this);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onConnected: " + e.toString());
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }

    @Override
    public synchronized void onLocationChanged(Location arg0) {

        try {
            if (arg0 == null) {
                return;
            }
            if (arg0.getLatitude() == 0 && arg0.getLongitude() == 0) {
                return;
            }
            lati = arg0.getLatitude();
            longi = arg0.getLongitude();
            location = arg0;
            Log.d(TAG, "onLocationChanged: " + String.valueOf(longi));
            cordinates = String.valueOf(lati) + "-" + String.valueOf(longi);
            if (address.equals("")) {
                if (config.NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
            Log.d(TAG, "onlocation" + cordinates);


        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }


    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {


        String taggedAddress = "";

        if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {
            taggedAddress = "";
        } else {
            taggedAddress = tvAddress.getText().toString();
        }
        String taggedCordinates = "";
        if (!cordinates.isEmpty()) {
            taggedCordinates = cordinates;
        } else {
            Utility.showAlertDialog("", "Please wait for location", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        Log.d("Location Data savetoDB", cordinates);

        String isSynced = "0";
        String activityImgStatus = "0";

        Date entrydate = new Date();
        InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        if (true) {


            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("KACode", "" + userCode);//: "string",
            jsonObject.addProperty("ActivityDt", "" + InTime);//: "2024-09-04T11:08:04.153Z",
            jsonObject.addProperty("VillageCode", "" + villageCode);//: "string",
            jsonObject.addProperty("VillageName", "" + villageName);//: "string",
            jsonObject.addProperty("TagCoordinates", "" + taggedCordinates);//: "string",
            jsonObject.addProperty("TagAddr", "" + taggedAddress);//: "string",
            jsonObject.addProperty("AppVersion", "" + BuildConfig.VERSION_NAME);//: "string",
            jsonObject.addProperty("Remark", "");//: "string"
            jsonObject.addProperty("KARadius", "" + etVillageRadius.getText().toString().trim());//: "string",
            jsonObject.addProperty("AdminRadius", "" + etVillageRadius.getText().toString().trim());//: "string",
            jsonObject.addProperty("ExtraParam1", "");//: "string",
            jsonObject.addProperty("ExtraParam2", "");//: "string"

            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObject);

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.add("focusVillageGeoTagModels", jsonArray);
            Log.i("Json Request", jsonObject1.toString());
            SubmitVillage(jsonObject1);


        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

    private void SubmitVillage(JsonObject jsonObject) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().submitFocusVillageData(jsonObject);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                 //   Toast.makeText(VillageTaggingGTV.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.getString("status").trim().toLowerCase().equals("success")) {

                                if (CommonUtil.addGTVActivity(context, "144", "Focus Village Tagging", cordinates, villageCode + " " + villageName, "Market", "0",0.0)) {
                                    // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                                }
                                new AlertDialog.Builder(context).setMessage("Village Co-ordinates Tagged Successfully.")
                                        .setCancelable(false)
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                
                                                boolean b=mDatabase.updateFocussedVillageTaggedDtls(villageCode,villageName,"",userCode,InTime,villageCode,villageName,cordinates,tvAddress.getText().toString(),etVillageRadius.getText().toString().trim(),etVillageRadius.getText().toString().trim(),BuildConfig.VERSION_NAME,"","","","1");
                                                if(b)
                                                    Toast.makeText(VillageTaggingGTV.this, "focus village entry added in local.", Toast.LENGTH_SHORT).show();
                                                
                                                finish();
                                            }
                                        }).show();
                            }
                            else {
                                new AlertDialog.Builder(context).setMessage("Something went wrong \nResponse : " + object.getString("Comment"))
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }
                            // Toast.makeText(ActivityTravelReportGTV.this, "" + response.body(), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {

                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }
    }

}
