package myactvity.mahyco;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.retro.RetrofitClient;
import myactvity.mahyco.travelreport.ActivityTravelReportGTVNew;
import myactvity.mahyco.travelreport.ActivityTravelReportTriggered;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class CustomerTaggingGTV extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {
    Context context;
    private static final String TAG = "VillageTagging";
   // SearchableSpinner spFocusedVillages;
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
    private static final long FASTEST_INTERVAL = 1000 * 8;
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

    // String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    String SERVER = "https://packhouse.mahyco.com/api/postSeason/livePlantDisplayVillageData";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity;
    RadioButton radOtherActivity;
    Prefs mPref;
    ProgressDialog progressDialog;
    String InTime = "";
    Dialog dialog_customer;
    RecyclerView rc_customerlist;
    EditText et_searchtext;
   public NotificationAdapter adapter1;

    LinearLayoutManager mManager;
    List<RootModel> arraylist_customer;
    TextView txt_customername;
    String str_customername="Select ",str_customercode="Select";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_tagging_gtv);
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
        tiOtherVillage = (TextInputLayout) findViewById(R.id.tiOtherVillage);
        etVillageRadius = (EditText) findViewById(R.id.etVillageRadius);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        lblheader = (TextView) findViewById(R.id.lblheader);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        txt_customername = (TextView) findViewById(R.id.txt_customername);
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
        dialog_customer = new Dialog(context);
        mManager = new LinearLayoutManager(context);
        // userCode =  pref.getString("UserID", null);

        RetryCustomerList();
        lblheader.setText("Customer Tagging");
        txt_customername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomer();
            }
        });


        int adb = Settings.Secure.getInt(this.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0);
        Toast.makeText(context, "Developer Option : "+adb, Toast.LENGTH_SHORT).show();



        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocusedActivity:

                        if (radFocusedActivity.isChecked()) {
                            // villageType = "focussed";

                            llFocussedVillages.setVisibility(View.VISIBLE);

                        }
                        break;

                }
            }
        });



        mLastClickTime = SystemClock.elapsedRealtime();
        onSubmitBtnClicked();
    }

    void showCustomer() {
        try {

            if (arraylist_customer != null) {
                if (arraylist_customer.size() > 1) {
                    dialog_customer.setContentView(R.layout.dialog_customertagging);
                    rc_customerlist = dialog_customer.findViewById(R.id.rc_customerlist);
                    et_searchtext = dialog_customer.findViewById(R.id.et_searchtext);
                    mManager=new LinearLayoutManager(context);
                    rc_customerlist.setLayoutManager(mManager);
                    adapter1 = new NotificationAdapter((ArrayList) arraylist_customer, context);
                    rc_customerlist.setAdapter(adapter1);
                    et_searchtext.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                filter(s.toString().trim());
                        }
                    });
                    dialog_customer.show();
                } else {
                    showRetryList();
                }
            } else {
                showRetryList();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Error is ",e.getMessage());
        }
    }

    void RetryCustomerList() {
        try {
            arraylist_customer = null;
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("FilterValue", userCode);
            jsonObject.addProperty("FilterOption", "GetByTBMCode");
            getCustomerList(jsonObject);
        } catch (Exception w) {

        }
    }

    void showRetryList() {
        try {
            new AlertDialog.Builder(context)
                    .setMessage("Customer list not found please retry again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RetryCustomerList();
                        }
                    })
                    .show();

        } catch (Exception e) {

        }
    }

    void showMessage(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
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
                       if (SystemClock.elapsedRealtime() - mLastClickTime < 60000) {
                            long time = SystemClock.elapsedRealtime() - mLastClickTime;
                            int seconds = (int) ((time / 1000) % 60);
                            showMessage("Wait for " + (60 - seconds) + " seconds, We are finding proper location.");
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerTaggingGTV.this);

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
        if (str_customername.trim().equals("")||str_customername.toLowerCase().contains("select")) {
            Utility.showAlertDialog("Info", "Please Select Customer", context);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerTaggingGTV.this);

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
        mPref.save(AppConstant.GTVCurrentCoOrdinates, "" + cordinates);
        Log.d("Location Data savetoDB", cordinates);

        String isSynced = "0";
        String activityImgStatus = "0";

        Date entrydate = new Date();
        InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        if (true) {

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("KACode", "" + userCode);//: "string",
            jsonObject.addProperty("ActivityDt", "" + InTime);//: "2024-09-04T11:08:04.153Z",
            jsonObject.addProperty("DistributorCode", "" + str_customercode);//: "string",
            jsonObject.addProperty("DistributorName", "" + str_customername);//: "string",
            jsonObject.addProperty("TagCoordinates", "" + taggedCordinates);//: "string",
            jsonObject.addProperty("TagAddr", "" + taggedAddress);//: "string",
            jsonObject.addProperty("AppVersion", "" + BuildConfig.VERSION_NAME);//: "string",
            jsonObject.addProperty("Remark", "");//: "string"
            jsonObject.addProperty("ExtraParam1", "");//: "string",
            jsonObject.addProperty("ExtraParam2", "");//: "string"

            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObject);

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.add("distributorGeoTagModels", jsonArray);
            Log.i("Json Request", jsonObject1.toString());
            SubmitCustomer(jsonObject1);


        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

    private void SubmitCustomer(JsonObject jsonObject) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().submitCustomerTaggingData(jsonObject);
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

                                if (CommonUtil.addGTVActivity(context, "144", "Customer Tagging", cordinates, str_customercode + " " + str_customername, "Market", "0", 0.0)) {
                                    // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                                }
                                new AlertDialog.Builder(context).setMessage("Customer Co-ordinates Tagged Successfully.")
                                        .setCancelable(false)
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                                 finish();
                                            }
                                        }).show();
                            } else {
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


    void getCustomerList(JsonObject jsonObject) {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();


            Call<List<RootModel>> call = null;
            call = RetrofitClient.getInstance().getMyApi().GetCustomerForTagging(jsonObject);
            call.enqueue(new Callback<List<RootModel>>() {
                @Override
                public void onResponse(Call<List<RootModel>> call, Response<List<RootModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        List<RootModel> root = response.body();
                        try {
                            if (root != null && root.size() > 0) {
                                try {
                                    arraylist_customer = root;
                                    Toast.makeText(CustomerTaggingGTV.this, "" + root.size(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    arraylist_customer=null;
                                     showRetryList();

                                }
                            } else {
                                arraylist_customer = null;
                                showRetryList();
                            }
                        } catch (Exception e) {

                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        arraylist_customer = null;
                        showRetryList();
                    }
                }

                @Override
                public void onFailure(Call<List<RootModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }


    }


    public class RootModel {
        public String getCustomercode() {
            return customercode;
        }

        public void setCustomercode(String customercode) {
            this.customercode = customercode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
        }

        public String getIsTagged() {
            return IsTagged;
        }

        public void setIsTagged(String isTagged) {
            IsTagged = isTagged;
        }

        String customercode;//": "0040214062",
        String city;//": "MOHAGAON HAWELI",
        String customername;//": "OM SAI AGRO TRADERS",
        String IsTagged;//": "NO"
    }

    private void filter(String text) {
        // creating a new array list to filter data
        ArrayList<RootModel> filteredlist = new ArrayList<>();

        // running a for loop to compare elements
        for (RootModel item : arraylist_customer) {
            // checking if the entered string matches any item of our recycler view
            if (item.getCustomername().toLowerCase().contains(text.toLowerCase())) {
                // adding matched item to the filtered list
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // displaying a toast message if no data found
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class

            adapter1.filterList(filteredlist);
        }
    }


    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DataObjectHolder> {


        Context context;

        private static final int UNSELECTED = -1;

        ArrayList<RootModel> bhartiModelArrayList = null;


        public NotificationAdapter(ArrayList<RootModel> productModels, Context context) {

            this.bhartiModelArrayList = productModels;
            Log.i("Seller produ:", ">>" + productModels.size());
            this.context = context;

        }

        public void filterList(ArrayList<RootModel> filterlist) {
            // below line is to add our filtered
            // list in our course array list.
            bhartiModelArrayList = filterlist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_chat2, parent, false);

            return new DataObjectHolder(view);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            //  if (mSellerProductlist.size() > 0) {
            return bhartiModelArrayList.size();
            //} else {
            //  return 0;
            // }
        }


        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            try {
                RootModel ResultModel = bhartiModelArrayList.get(position);
                holder.txt_dtvtype.setTextColor(Color.BLUE);
                holder.txt_dtvtype.setText(Html.fromHtml("<b style='color:RED;'>" + ResultModel.customername + "</b>"));
                holder.txt_message.setText(ResultModel.getCustomercode());

                holder.txt_date.setText(" " + ResultModel.city);

                holder.txt_status.setText(" " + ResultModel.getIsTagged());
                if (ResultModel.getIsTagged().toLowerCase().trim().equals("no")) {
                    holder.txt_status.setText("");
                } else {
                    holder.txt_status.setText(Html.fromHtml("<b style='color:RED;'>Tagged</b>"));
                    holder.txt_status.setTextColor(Color.RED);
                 //   holder.linearLayout.setBackgroundColor(Color.LTGRAY);
                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            str_customercode="";
                            str_customername="";
                            if (ResultModel.getIsTagged() != null) {
                                if (ResultModel.getIsTagged().toLowerCase().trim().equals("no")) {
                                    Toast.makeText(context, "Data is " + ResultModel.customercode + " Name :" + ResultModel.customername, Toast.LENGTH_SHORT).show();
                                     str_customercode=ResultModel.customercode;
                                     str_customername=ResultModel.customername;
                                    txt_customername.setText(str_customername+"("+str_customercode+")");
                                    dialog_customer.dismiss();
                                } else {
                                    str_customercode="Select";
                                    str_customername="";
                                    txt_customername.setText(str_customername+"("+str_customercode+")");
                                    Toast.makeText(context, "Already Tagged.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            str_customercode="";
                            str_customername="";
                        }
                    }
                });


            } catch (Exception e) {

                Log.i("Error ", e.getMessage());

            }
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView txt_message, txt_date, txt_dtvtype, txt_status;
            LinearLayout linearLayout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                txt_message = itemView.findViewById(R.id.txt_message);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_dtvtype = itemView.findViewById(R.id.txt_gtv);
                linearLayout = itemView.findViewById(R.id.cc);
                txt_status = itemView.findViewById(R.id.txt_status);


            }
        }


    }


}
