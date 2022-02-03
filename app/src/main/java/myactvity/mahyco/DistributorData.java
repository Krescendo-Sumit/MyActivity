package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class DistributorData   extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, ResultCallback
         {
    View parentHolder;
    public SearchableSpinner spDist, spTaluka,spdistributor, spState, spCropType, spProductName, spMyactvity;
    public Button btnsave, btnTakephoto;
    private String state;
    RadioButton rndNo2, rndYes2;
    EditText txtmarketplace,txtComments, txtrtname, txtAge, txtmobile, txtfirmname,txtBirthdate;
    CheckBox chktag;
    TextView lbltaluka;
    private String dist, taluka, village, Imagepath1, croptype;
    ProgressDialog dialog;
    private SqliteDatabase mDatabase;
    public Messageclass msclass;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    int imageselect;
    Context context;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    SharedPreferences preferences;
    private static String cordinate;
    private static String address;
    public RadioGroup radGrp2;

             //variable for GPS
    // location last updated time
    private String mLastUpdateTime;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
             private SimpleDateFormat dateFormatter;
             Calendar dateSelected = Calendar.getInstance();
             private DatePickerDialog datePickerDialog;

             // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
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
            // int REQUEST_CHECK_SETTINGS=101;


    public DistributorData() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mDatabase = SqliteDatabase.getInstance(this.getActivity());
        msclass = new Messageclass(this.getActivity());
        context = this.getActivity();
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //initialize the necessary libraries
         // init();
       // startLocation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        parentHolder = inflater.inflate(R.layout.fragment_distributor_data, container,
                false);
        preferences = this.getActivity().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        dialog = new ProgressDialog(this.getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka = (TextView) parentHolder.findViewById(R.id.lbltaluka);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        rndYes2=(RadioButton)parentHolder.findViewById(R.id.rndYes2);
        rndNo2=(RadioButton)parentHolder.findViewById(R.id.rndNo2);

        spdistributor=(SearchableSpinner) parentHolder.findViewById(R.id.spdistributor);
        spDist = (SearchableSpinner) parentHolder.findViewById(R.id.spDist);
        spState = (SearchableSpinner) parentHolder.findViewById(R.id.spState);
        spTaluka = (SearchableSpinner) parentHolder.findViewById(R.id.spTaluka);
        btnsave = (Button) parentHolder.findViewById(R.id.btnsave);
        txtmarketplace = (EditText) parentHolder.findViewById(R.id.txtmarketplace);
        txtComments=(EditText)parentHolder.findViewById(R.id.txtComments);
        txtBirthdate=(EditText)parentHolder.findViewById(R.id.txtBirthdate);

        txtrtname = (EditText) parentHolder.findViewById(R.id.txtrtname);
        txtAge = (EditText) parentHolder.findViewById(R.id.txtAge);
        txtmobile = (EditText) parentHolder.findViewById(R.id.txtmobile);
        txtfirmname = (EditText) parentHolder.findViewById(R.id.txtfirmname);
        chktag = (CheckBox) parentHolder.findViewById(R.id.chktag);
        // btnTakephoto=(Button)parentHolder.findViewById(R.id.btnTakephoto);
        pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        //BindDist("");
        BindState();
        binddistributor(dist);
        txtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // setDateTimeField(v);
            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = gm.Code().trim() ;//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BindDist(state);
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    BindTaluka(dist);
                   // binddistributor(dist);


                }
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
                // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                   // binddistributor(taluka);

                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spdistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    if(gm.Desc().trim().toUpperCase().equals("NEW DISTRIBUTOR")) {
                        txtfirmname.setEnabled(true);

                    }
                    else
                    {
                        txtfirmname.setText("");
                        txtfirmname.setEnabled(false);

                    }
                    // URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {



                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveStarttravel();

            }
        });


        if (pref.getString("RoleID", null) == null || pref.getString("RoleID", null).equals("") ||
                Integer.valueOf(pref.getString("RoleID", "0")) == 0
                || Integer.valueOf(pref.getString("RoleID", "0")) == 4) {
           // binddistributor(taluka);
        } else {
           // binddistributor(taluka);
            //spTerritory.setVisibility(View.GONE);
            //lbltaluka.setText("");
            // lbltaluka.setVisibility(View.GONE);
            // binddistributor(taluka);
            ///  BindRetailer(taluka);


        }
        radGrp2 = (RadioGroup) parentHolder.findViewById(R.id.radGrp2);

        radGrp2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case -1:
                        // Log.v(TAG, "Choices cleared!");
                        break;
                    case R.id.rndYes2:
                        saveStarttravel();
                        break;

                    default:

                        break;
                }
            }
        });

        // BindIntialData();
        return parentHolder;
    }

//GPS FUSED


             private void setDateTimeField(View v) {
                 final EditText txt=(EditText)v;
                 Calendar newCalendar = dateSelected;
                 datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                     public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                         txt.setText(dateFormatter.format(dateSelected.getTime()));
                     }
                 }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                 datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                     @Override
                     public void onCancel(DialogInterface dialogInterface) {
                         txt.setText("");
                     }
                 });
                 datePickerDialog.show();
                 // txt.setText(dateFormatter.format(dateSelected.getTime()));


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


    private void updateLocationUI() {
        try {
            if (mCurrentLocation != null) {


                // giving a blink animation on TextView
                // txtLocationResult.setAlpha(0);
                //txtLocationResult.animate().alpha(1).setDuration(300);

                // location last updated time
                //  txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
               // RetailerData.address =getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                //RetailerData.cordinate=mCurrentLocation.getLatitude() + "-" + mCurrentLocation.getLongitude();
               // cordinate = mCurrentLocation.getLatitude() + "-" + mCurrentLocation.getLongitude();
               // address = getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                //Toast.makeText(getActivity(), "Lat: " + mCurrentLocation.getLatitude() + ", " +
                 //       "Lng: " + mCurrentLocation.getLongitude()+"\n+"+address, Toast.LENGTH_SHORT).show();

            }
        }
        catch(Exception ex)
        {
            msclass.showMessage("Function Name-updateLocationUI "+ex.toString());
        }

        //toggleButtons();
    }

   /* @Override
    public void onResume() {
        super.onResume();

        try {
            // Resuming location updates depending on button state and
            // allowed permissions
            if (mRequestingLocationUpdates && checkPermissions()) {
                startLocationUpdates();
               // updateLocationUI();

            }
        }
        catch(Exception ex)
        {
            msclass.showMessage("Funtion name :onresume"+ex.getMessage());
        }
        // updateLocationUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            if (mRequestingLocationUpdates) {
                // pausing location updates
                stopLocationUpdates();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    */
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
               /* result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        //final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                int st = getLocationMode();
                                if (st == 1 || st == 2) {
                                    Toast.makeText(getActivity(), "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                }

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(
                                            getActivity(),
                                            REQUEST_CHECK_SETTINGS);
                                } catch (Exception e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                //...
                                break;
                        }
                    }
                }); */

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
            address = getCompleteAddressString(lati, longi);
            //accuracy = String.valueOf(arg0.getAccuracy());
           // Toast.makeText(context, cordinate+"S", Toast.LENGTH_SHORT).show();
            // locationInsertTime = arg0.getTime();
            //LocationBearing = arg0.getBearing();
            //LocationSpeed = arg0.getSpeed();
            // fusedlocationRecieved = true;

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
            //msclass.showMessage("Please on device GPS location.\n startFusedLocationService"+ex.getMessage());
           // msclass.showMessage("GPS is not enable,Please on GPS\n startFusedLocationService");
            Toast.makeText(this.getActivity(), "GPS is not enable,Please on GPS\n startFusedLocationService", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
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
                       Toast.makeText(this.getActivity(), "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                   }
                   break;
               case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                   try {

                       status.startResolutionForResult(this.getActivity(), REQUEST_CHECK_SETTINGS);

                   } catch (IntentSender.SendIntentException e) {

                   }
                   break;

               case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                   // Location settings are unavailable so not possible to show any dialog now
                   break;
           }
       }
       catch(Exception ex)
       {
           Toast.makeText(this.getActivity(),"Func-onResult"+ex.toString(), Toast.LENGTH_LONG).show();

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
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
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
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
 // GPS END




public void binddistributor(String dist)
{
    if(dist==null) {
        dist="";
    }
try

    {
        spdistributor.setAdapter(null);
        dialog.setMessage("Loading....");
        dialog.show();
        String str = null;
        try {

            // str = cx.new getTaluka(dist).execute().get();

            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            //String searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
            //        "and  dist='" + dist.toUpperCase() + "' order by  RetailerName ";
            String searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
                    " order by  RetailerName ";

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT DISTRIBUTOR",
                    "SELECT DISTRIBUTOR"));
            //Croplist.add(new GeneralMaster("NEW DISTRIBUTOR",
              //      "NEW DISTRIBUTOR"));

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
            spdistributor.setAdapter(adapter);
            dialog.dismiss();


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }
        catch(
    Exception ex)

    {
        msclass.showMessage(ex.getMessage());
        ex.printStackTrace();
        dialog.dismiss();
    }

}
    public void  BindState()
    {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
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
            } else
                {
                if (validation() == true)
                {
                GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();

                try {

                    dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                    taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                    // village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");

                    int count = mDatabase.getrowcount("select * from mdo_Retaileranddistributordata " +
                            "where mobileno='"+txtmobile.getText().toString()+"' and type ='Distributor'");
                    if(count>0) {
                        msclass.showMessage("This distributor data already saved on this mobile number "+txtmobile.getText().toString()+"");

                    }
                    else {
                        Savedata("", state, dist, taluka, village);
                    }
                   // Savedata("", state, dist, taluka, village);
                }

                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
                }
                else
                {
                    radGrp2.clearCheck();
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


            if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                msclass.showMessage("Please select state");
                return false;
            }
            if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                msclass.showMessage("Please select district");
                return false;
            }
            if(pref.getString("RoleID",null)==null || pref.getString("RoleID",null).equals("")||
                    Integer.valueOf(pref.getString("RoleID","0"))==0
                    || Integer.valueOf(pref.getString("RoleID","0"))==4) {
                if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                    msclass.showMessage("Please Select Taluka");
                    return false;
                }
            }
            else
            {


            }
            if (txtmarketplace.getText().length() == 0) {
                msclass.showMessage("Please enter market place.");
                return false;
            }
            if (txtrtname.getText().length() == 0) {
                msclass.showMessage("Please enter distributor name.");
                return false;
            }
           /* if (txtfirmname.getText().length() == 0) {
                msclass.showMessage("Please enter distributor firm name.");
                return false;
            }*/
            if (txtmobile.getText().length() == 0) {
                msclass.showMessage("Please enter mobile number.");
                return false;
            }
            if (txtmobile.getText().length() < 10) {
                msclass.showMessage("Please enter 10 digit mobile number");
                return false;
            }
            if (spdistributor.getSelectedItem().toString().toLowerCase().equals("select distributor")) {
                msclass.showMessage("Please Select distributor");
                return false;
            }
            if (spdistributor.getSelectedItem().toString().toLowerCase().equals("new distributor")) {
                if (txtfirmname.getText().length() == 0) {
                    msclass.showMessage("Please enter new distributor firm name.");
                    return false;
                }

            }


            //try
            //{
            String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
            Pattern pattern = Pattern.compile(regex);
            if (txtBirthdate.getText().length()>0)
            {
                Matcher matcher = pattern.matcher(txtBirthdate.getText().toString());
                if (matcher.matches()) {

                }
                else
                {
                    msclass.showMessage("Please enter  DD/MM/YYYY format date.. ");
                    return false;
                }
                //Date javaDate = dateFormatter.parse(txtBirthdate.getText().toString());
            }
            // }
            // catch (Exception e)
            // {
            //     msclass.showMessage("Please enter  DD/MM/YYYY formate date ");
            //     return false;
            //}



            if(chktag.isChecked()==false)
            {
                msclass.showMessage("Please check geo tag.");
                return false;

            }
            if (radGrp2.getCheckedRadioButtonId() == -1)
            {
                msclass.showMessage("Please select is he key retailer as well  (Yes or No). ");
                return false;
            }


        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
        return true;
    }

    private  void Savedata(final String myactvity, String state, String dist, String taluka, String village)
    {
        try
        {
            pref.getString("UserID", null);
            Date entrydate = new Date();
            final String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            final String Tempimagepath1;
            final String Imagename="STravel"+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
            Tempimagepath1=Imagepath1;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
            // Setting Dialog Title
            alertDialog.setTitle("My Activity App");
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure to save this data ");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do do my action here

                    String marketplace =txtmarketplace.getText().toString();
                    String retailername =txtrtname.getText().toString();
                    String retailerfirm =spdistributor.getSelectedItem().toString();//txtfirmname.getText().toString();
                    String newfirm=txtfirmname.getText().toString();

                    String mobileno =txtmobile.getText().toString();
                    String age =txtAge .getText().toString();
                    String mahycoretailer="";
                    String Otherbussiness="";
                    if(chktag.isChecked()==true)
                    {

                    }
                    if(rndNo2.isChecked()==true ) {
                        mahycoretailer="NO";
                    }
                    if(rndYes2 .isChecked()==true ) {
                        mahycoretailer="YES";
                    }
                    String taluka="";
                    if(pref.getString("RoleID",null)==null || pref.getString("RoleID",null).equals("")||
                            Integer.valueOf(pref.getString("RoleID","0"))==0
                            || Integer.valueOf(pref.getString("RoleID","0"))==4) {
                        taluka= spTaluka.getSelectedItem().toString().trim();
                    }
                    String birthdate=txtBirthdate.getText().toString().trim();

                  /*  if(rndNo2.isChecked()==true ) {
                        mahycoretailer="NO";
                    }*/

                    String state=spState.getSelectedItem().toString().trim();

                    boolean fl = mDatabase.InsertRetailerdata(pref.getString("UserID", null),
                            cordinate, address,spDist.getSelectedItem().toString().trim(),
                            taluka,marketplace,retailername,
                            retailerfirm, mobileno,age,
                            "",mahycoretailer,Otherbussiness,
                            "",txtComments.getText().toString(),
                            "Distributor",newfirm,birthdate,"",state);

                    if (fl==true)
                    {
                        try {
                            if(rndYes2 .isChecked()==true ) {
                                msclass.showMessage("Distributor data saved successfully,Please fill retailer form.");
                            }
                            else {
                                msclass.showMessage("data saved successfully");
                            }

                            txtmobile.setText("");
                            txtAge.setText("");
                            txtComments.setText("");
                            txtfirmname.setText("");
                            txtmarketplace.setText("");
                            txtrtname.setText("");
                            radGrp2.clearCheck();
                            txtBirthdate.setText("");
                            Intent intent;
                            String callactivity=pref.getString("RetailerCallActivity", "");
                            switch (callactivity)
                            {

                                case "DistributerVisitsActivity":
                                    intent = new Intent(getActivity(), DistributerVisitsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();

                                    break;


                            }

                        }
                        catch (Exception ex)
                        {
                            msclass.showMessage(ex.toString());
                        }
                    }
                    else
                    {
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
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private boolean turnGPSOn(){
         boolean flag=false;
       // boolean flag=true;
        try {
            LocationManager locationManager=null;
            try {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
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
        return  flag;
    }
    public void  BindDist(String state)
    {
        try {
            spDist.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state='"+state+"' order by district asc  ";
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

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }

    public void  BindDist2(String state)
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

}