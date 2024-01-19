package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class addtravel  extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback
{
    View parentHolder;
    public Spinner  spDist, spTaluka, spVillage, spCropType, spProductName, spMyactvity,spComment;
    public Button btnstUpdate,btnTakephoto, btnAddActivity;
    private String state;
    private String dist,taluka,village,Imagepath1,usercode;
    ProgressDialog dialog;
    private SqliteDatabase mDatabase;
    public Messageclass msclass;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    int imageselect;
    Context context;
    CheckBox chktag;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    EditText txtpalce;
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    SharedPreferences preferences;
    LinearLayout my_linear_layout1;
    CommonExecution cx;
    private  static String cordinate="";
    private static String address="" ;
    // Location updates intervals in sec
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private static int UPDATE_INTERVAL = 2000;//10000; // 10 sec
    private static int FATEST_INTERVAL = 2000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    //Rohit given
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;


    public addtravel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = SqliteDatabase.getInstance(this.getActivity());

        msclass = new Messageclass(this.getActivity());
        context=this.getActivity();
        cx=new CommonExecution(context);

        //init();
        // startLocation();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        parentHolder = inflater.inflate(R.layout.fragment_addtravel, container,
                false);
        preferences = this.getActivity().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        dialog = new ProgressDialog(this.getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TextView lblwelcome=(TextView)parentHolder.findViewById(R.id.lblwelcome);
        lblwelcome.setText("NAME: "+preferences.getString("Displayname",null));
        txtpalce = (EditText) parentHolder.findViewById(R.id.txtpalce);
        chktag=(CheckBox) parentHolder.findViewById(R.id.chktag);
        btnstUpdate=(Button)parentHolder.findViewById(R.id.btnstUpdate);
        btnTakephoto=(Button)parentHolder.findViewById(R.id.btnTakephoto);
        btnAddActivity=(Button)parentHolder.findViewById(R.id.btnAddActivity);
        ivImage=(ImageView) parentHolder.findViewById(R.id.ivImage);
        my_linear_layout1 = (LinearLayout)parentHolder.findViewById(R.id.my_linear_layout1);
        pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode = pref.getString("UserID", null);

        btnstUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveStarttravel();
                // addrow();

            }
        });
        btnTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    //  ActivityCompat.requestPermissions(context, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                imageselect=1;
                // selectImage();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        captureImage();
                    } else {
                        captureImage2();
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checktourstated();

            }
        });
       /* boolean flag2 = mDatabase.isTableExists("mdo_addplace");
        if (flag2 == false) {
            mDatabase.CreateTable6();
        }*/
        bindviewdata();
        // startLocation();
        /*if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }*/

        return parentHolder;
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }
    @Override
    public void onConnected(Bundle arg0) {
        try {
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fusedlocationRecieved = false;
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    Log.d(TAG, "Fused api connected: ");
                    if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest,  this);
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
        }


    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }
    @Override
    public synchronized void onLocationChanged(Location arg0) {

        try {
            if (arg0 == null) {
                // isLocationReceivedByGAPI = false;
                return;
            }
            if (arg0.getLatitude() == 0 && arg0.getLongitude() == 0) {
                return;
            }
            double lati = arg0.getLatitude();
            double longi = arg0.getLongitude();
            location=arg0;
            Log.d(TAG, "onLocationChanged: "+String.valueOf(longi));
            cordinate = String.valueOf(lati)+"-"+String.valueOf(longi);
            if(address.equals("")) {
                if (new Config(context).NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            //accuracy = String.valueOf(arg0.getAccuracy());
            // Toast.makeText(context, cordinate+"M", Toast.LENGTH_SHORT).show();
            // locationInsertTime = arg0.getTime();
            //LocationBearing = arg0.getBearing();
            //LocationSpeed = arg0.getSpeed();
            //  fusedlocationRecieved = true;

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: "+e.toString());
            e.printStackTrace();
            //  }
        }

    }
    private synchronized void startFusedLocationService() {
       /* cancelNotification(NOTIFICATION_ID_REALTIME);*/
        try {
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                IsGPSEnabled = true;
            } else {
                IsGPSEnabled = false;
            }
            if (IsGPSEnabled) {
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(INTERVAL);
                locationRequest.setSmallestDisplacement(0f);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);
                googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                        .addApi(LocationServices.API).addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                try {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GpsEnabled = true;

            } else {
                GpsEnabled = false;
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
            Log.d(TAG, "startFusedLocationService: ");
        }
        catch (Exception  ex)
        {
            Toast.makeText(this.getActivity(), "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResult(@NonNull Result result) {
        try {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    int st = getLocationMode();
                    if (st == 1 || st == 2) {
                        Toast.makeText(this.getActivity(), "onResult-Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {

                        status.startResolutionForResult(this.getActivity(), REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException e)
                    {

                    }
                    break;

                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are unavailable so not possible to show any dialog now
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this.getActivity(), "Func-onResult"+ex.toString(), Toast.LENGTH_LONG).show();

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int getLocationMode() {
        int val = 0;
        try {
            val = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            Log.d(TAG, "getLocationMode status: " + val);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return val;

    }
    public void stopFusedApi() {
        try {
            if (googleApiClient != null && (googleApiClient.isConnected() )) {
                FusedLocationApi.removeLocationUpdates(googleApiClient, this);
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
    @Override
    public void onResume() {
        super.onResume();
        try {
            // Resuming location updates depending on button state and
            // allowed permissions
            //  if (checkPermissions()) {
            startFusedLocationService();
            // updateLocationUI();
            // }
        }
        catch(Exception ex)
        {
            msclass.showMessage("Funtion name :onresume"+ex.getMessage());
        }

    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            stopFusedApi();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void checktourstated()
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String strdate = dateFormat.format(d);
            JSONObject object = new JSONObject();
            // JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            JSONObject object2 = new JSONObject();
            object2.put("Table2", mDatabase.getResults("select  * from mdo_starttravel  " +
                    "where strftime( '%Y-%m-%d', startdate)='" + strdate + "' and upper(mdocode)='"+usercode.toUpperCase()+"'"));
            JSONArray jArray2 = object2.getJSONArray("Table2");//new JSONArray(result);
            Intent intent;
            if (jArray2.length() > 0) {
                logMyTravelAddActivityData(getActivity()); //Add Travel activity
                intent = new Intent(getContext(),MyActivityRecordingNew.class);
                startActivity(intent);
            } else
            {
                // intent = new Intent(getContext(),MyActivityRecordingNew.class);
                //startActivity(intent);
                msclass.showMessage("Tour is not started ,please fill tour started data.");
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.toString());
        }

    }
    private void init() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
            mSettingsClient = LocationServices.getSettingsClient(this.getActivity());
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    // location is received
                    mCurrentLocation = locationResult.getLastLocation();
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    updateLocationUI();
                }
            };
            mRequestingLocationUpdates = false;
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            mLocationSettingsRequest = builder.build();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            msclass.showMessage("Function Name-init "+ex.toString());
        }

    }
    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this.getActivity())
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this.context)
                    .addApi(LocationServices.API).build();
        }
        catch(Exception ex)
        {
            Toast.makeText(this.getActivity().getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this.getActivity());
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(),
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(this.getActivity().getApplicationContext(),
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    this.getActivity().finish();
                }
                return false;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this.getActivity().getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
        return true;
    }
    protected void createLocationRequest() {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }
        catch(Exception ex)
        {
            Toast.makeText(this.getActivity().getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }
    private boolean checkPermissions() {

        int permissionState = ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;

    }


    private void updateLocationUI() {
        try {
            if (mCurrentLocation != null) {


                // giving a blink animation on TextView
                // txtLocationResult.setAlpha(0);
                //txtLocationResult.animate().alpha(1).setDuration(300);

                // location last updated time
                //  txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
                starttravel.address =getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                starttravel.cordinate=mCurrentLocation.getLatitude() + "-" + mCurrentLocation.getLongitude();
                endtravel.address =getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                endtravel.cordinate=mCurrentLocation.getLatitude() + "-" + mCurrentLocation.getLongitude();

                cordinate = mCurrentLocation.getLatitude() + "-" + mCurrentLocation.getLongitude();
                address = getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                // Toast.makeText(getActivity(), "Lat: " + mCurrentLocation.getLatitude() + ", " +
                ///       "Lng: " + mCurrentLocation.getLongitude()+"\n+"+address, Toast.LENGTH_SHORT).show();
                // Toast.makeText(getActivity(), cordinate, Toast.LENGTH_SHORT).show();

            }
        }
        catch(Exception ex)
        {
            msclass.showMessage("Function Name-updateLocationUI "+ex.toString());
        }

        //toggleButtons();
    }

    private void startLocationUpdates() {
        try {


            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Log.i(TAG, "All location settings are satisfied.");

                            //Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                            //noinspection MissingPermission
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());

                            updateLocationUI();
                        }
                    })
                    .addOnFailureListener(this.getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                            "location settings ");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);

                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                            }

                            updateLocationUI();
                        }
                    });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            msclass.showMessage("Function Name-startLocationUpdates "+ex.toString());
        }
    }

    public void stopLocationUpdates() {
        try {
            // Removing location updates
            mFusedLocationClient
                    .removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                            //toggleButtons();
                        }
                    });
        }
        catch(Exception ex)
        {
            msclass.showMessage("Function Name"+ex.toString());
        }
    }

    private void startLocation()
    {
        try {
            // Requesting ACCESS_FINE_LOCATION using Dexter library
            Dexter.withActivity(this.getActivity())
                    .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            startLocationUpdates();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
        catch (Exception ex)
        {
            msclass.showMessage("Function Name-startLocation "+ex.toString());
        }
    }

    private void openSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }

    public void bindviewdata()
    {
       /* int tot=1;
        try {

            //Strt
            try {

                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults("select * from mdo_addplace where mdocode='"+usercode+"'"));
                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                my_linear_layout1.removeAllViews();                              //add this too

                // HEADER TAG
                final View view3 = LayoutInflater.from(this.getActivity()).inflate(R.layout.locationadd, null);
                //view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                TextView txtproduct1 = (TextView) view3.findViewById(R.id.txtproduct);
                TextView txtqty1 = (TextView) view3.findViewById(R.id.txtqty);

                txtproduct1.setText("PLACES");
                //  lblcomp.setTextSize(14);
                txtproduct1.setTypeface(txtproduct1.getTypeface(), Typeface.BOLD);

                txtqty1.setText("COORDINATE");
                // lblcrop.setTextSize(14);
                txtqty1.setTypeface(txtqty1.getTypeface(),Typeface.BOLD);




                my_linear_layout1.addView(view3);


                for(int i=0; i <jArray.length(); i++) {
                    tot=tot+i;
                    JSONObject jObject = jArray.getJSONObject(i);
                    //st
                    try {
                        List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                        int count=my_linear_layout1.getChildCount();
                        count=count+1;
                        final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.locationadd, null);
                        view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                        TextView txtproduct = (TextView) view2.findViewById(R.id.txtproduct);
                        TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
                        ImageView  ivImage=(ImageView) view2.findViewById(R.id.ivImage);
                        txtproduct.setText(jObject.getString("place"));
                        txtqty.setText(jObject.getString("coordinate"));
                        Imagepath1=jObject.getString("imgpath");
                        cx.bindimage(ivImage,jObject.getString("imgpath"));
                        my_linear_layout1.addView(view2);
                    }
                    catch (Exception ex)
                    {
                        msclass.showMessage(ex.getMessage());
                        ex.printStackTrace();

                    }


                }
            } catch (Exception e) {
                Log.e("JSONException", "Error: " + e.toString());

            }


        }

        catch (Exception e) {
            e.printStackTrace();
        }
*/
    }

    private void addrow()//LinearLayout my_linear_layout1)
    {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            int count=my_linear_layout1.getChildCount();
            count=count+1;
            final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.locationadd, null);
            view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            TextView txtproduct = (TextView) view2.findViewById(R.id.txtproduct);
            TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
            ImageView  ivImage=(ImageView) view2.findViewById(R.id.ivImage);
            my_linear_layout1.addView(view2);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

        //en
    }
    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            if (imageselect == 1) {
                photoFile = createImageFile4();
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }
    private void captureImage()
    {

        try {

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        if (imageselect==1) {
                            photoFile = createImageFile();
                            //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            // Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(context,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                            }
                        }

                    } catch (Exception ex) {
                        // Error occurred while creating the File
                        // displayMessage(getBaseContext(),ex.getMessage().toString());
                        msclass.showMessage(ex.toString());
                        ex.printStackTrace();
                    }


                } else {
                    //displayMessage(getBaseContext(),"Nullll");
                }
            }
        }

        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
    }
    private File createImageFile4() //  throws IOException
    {    File mediaFile=null;
        try {
            // External sdcard location
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    //displayMessage(getBaseContext(),"Unable to create directory.");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
        return mediaFile;
    }
    private File createImageFile()
    {
        // Create an image file name
        File image=null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            msclass.showMessage(ex.toString());
        }
        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void cameraIntent()
    {
      /*  try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           // file1 = Uri.fromFile(getOutputMediaFile());
           // file1= FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", getOutputMediaFile());
            file1=FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    getOutputMediaFile());


            intent.putExtra(MediaStore.EXTRA_OUTPUT, file1);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.toString());
            ex.printStackTrace();
        }
        */
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + "ABC.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFileName);
            Uri mCapturedImageURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            // pictureImagePath = getRealPathFromURI(mCapturedImageURI);
            Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            getActivity().startActivityForResult(intentPicture, REQUEST_CAMERA);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }


         /*try {

             destination = new File(Environment.getExternalStorageDirectory(),
                     imageFileName);
             //File storageDir = Environment.getExternalStoragePublicDirectory(
             //       Environment.DIRECTORY_PICTURES);
             pictureImagePath = destination.getAbsolutePath();// + "/" + imageFileName;
             File file = new File(pictureImagePath);
             Uri outputFileUri = Uri.fromFile(file);
             Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
             startActivityForResult(cameraIntent, REQUEST_CAMERA);
         }
         catch (Exception e) {
             e.printStackTrace();
         } */
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }
    }
    private void onCaptureImageResult(Intent data) {


        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize =2;

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    Imagepath1 = photoFile.getAbsolutePath();
                    ivImage.setImageBitmap(myBitmap);
                }
                catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }

        }
        catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect==1) {
            ivImage.setImageBitmap(bm);
        }

    }
    public void saveStarttravel()
    {
        try
        {
            if (turnGPSOn() == false) {
                //msclass.showMessage("GPS is not enabled,Please on GPS");
                Toast.makeText(this.getActivity(), "GPS is not enabled,Please on GPS", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                if (validation() == true) {


                    try {


                        Savedata("", state, dist, taluka, village);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.getMessage());
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }

    }
    private boolean validation()
    {
        try {
            boolean flag = true;
            if (txtpalce.getText().length() == 0) {
                msclass.showMessage("Please enter location(place).");
                return false;
            }
            if(chktag.isChecked() ==false)
            {
                msclass.showMessage("Please select geo tag");
                return false;
            }
            if(cordinate.length()==0)
            {
                msclass.showMessage(" GPS Location not found ,please check GPS location setting .");
                return false;
            }

        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }

    private  void Savedata(final String myactvity, String state, String dist, String taluka, String village)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d=new Date();
            String strdate=dateFormat.format(d);
            JSONObject object2 = new JSONObject();
            object2.put("Table2", mDatabase.getResults("select  * from mdo_starttravel  " +
                    "where strftime( '%Y-%m-%d', startdate)='"+strdate+"' and mdocode='"+usercode+"'"));
            JSONArray jArray2 = object2.getJSONArray("Table2");//new JSONArray(result);
            int startreading=0;
            if(jArray2.length()>0) {
                Date entrydate = new Date();
                final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

                final String Tempimagepath1;
                final String Imagename = "STravel" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                Tempimagepath1 = Imagepath1;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
                // Setting Dialog Title
                alertDialog.setTitle("My Activity App");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure to save this data ");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do do my action here

                        String crop = "";
                        String product = "";
                        boolean fl = mDatabase.insertAddplace(pref.getString("UserID", null),
                                txtpalce.getText().toString(), cordinate, address, InTime, Imagename, Tempimagepath1);

                        if (fl == true) {
                            try {
                                msclass.showMessage("Save Input data successfully");
                                txtpalce.setText("");
                                // ivImage.setImageDrawable(null);
                                bindviewdata();
                            } catch (Exception ex) {
                                msclass.showMessage(ex.toString());
                            }
                        } else {
                            msclass.showMessage("Please check entry data.");
                        }


                    }

                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might

                        dialog.dismiss();

                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();
                final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                positiveButtonLL.weight = 10;
                positiveButtonLL.gravity = Gravity.CENTER;
                positiveButton.setLayoutParams(positiveButtonLL);
                //end
            }
            else
            {
                msclass.showMessage("Tour is not started ,please fill tour started data.");
            }

        }
        catch (Exception ex)
        {   ex.printStackTrace();
            msclass.showMessage( "" + ex.getMessage());
            msclass.showMessage(ex.getMessage());
        }
    }
    /*
   public void onLocationChanged(Location location) {
        // Assign the new location
        try {
            //mLastLocation = location;
            // Displaying the new location on UI
            //displayLocation();
            cordinate = location.getLatitude() + "-" + location.getLongitude();
            address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

*/

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }
        return strAdd;
    }

    private boolean turnGPSOn(){
        // boolean flag=false;
        boolean flag=true;
        /*try {
            LocationManager locationManager=null;
            try {
                 locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            catch(SecurityException e) {
                e.printStackTrace();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                msclass.showMessage(ex.getMessage());
            }
            //boolean isGPSEnabled = locationManager
            //        .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled){

                flag=false;

            }
            else
            {
                flag=true;
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
*/
        return  flag;
    }

    public void  BindDist(String state)
    {
        try {
            spDist.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }
    public void  BindTaluka(String dist)
    {
        try {
            spTaluka.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    private void logMyTravelAddActivityData(Context context){
        if(preferences!=null){
            String userId="", displayName="";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(context).callAddActivityMyTravel(userId,displayName);
            }
        }
    }
}