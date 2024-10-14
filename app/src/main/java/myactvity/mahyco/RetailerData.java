package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;

import myactvity.mahyco.app.KeyPairBoolData;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.POPDisplayActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.PromotionActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.VillageMeetingActivity;
import myactvity.mahyco.retro.RetrofitClient;
import myactvity.mahyco.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class RetailerData extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback,
        MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    View parentHolder;
    public SearchableSpinner spDist, spnewretailer, spTaluka, spState, spVillage,
            spCropType, spProductName, spMyactvity, spComment;
    public Button btnsave, btnTakephoto;
    private String state;
    MultiSelectionSpinner spdistr, spBussiness;
    RadioButton rndNo, rndYes;
    EditText txtmarketplace, txtrtname, txtAge, txtmobile, txtmobilewhatup,
            txtBirthdate, txtyearofexperiance, txtComments, txtfirmname;
    CheckBox chkfirti, chkpesti, chkmahycoproduct, chkotherprd, chktag;
    private String dist, taluka, village, Imagepath1, croptype;
    ProgressDialog dialog;
    private SqliteDatabase mDatabase;
    public Messageclass msclass;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    int imageselect;
    Context context;
    TextView lbltaluka;
    CommonExecution cx;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    public static String cordinate;
    public static String address="";
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    SharedPreferences preferences;
    ArrayAdapter<GeneralMaster> adapter3;
    String usercode, from = "";
    //Rohit given
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS = 101;
    public RadioGroup radGrp, radGrp2;
    private SimpleDateFormat dateFormatter;

    int getState, getDistrict, getTaluka;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    public RetailerData() {
        // Required empty public constructor
    }

    // New Varible for force upload

    ProgressDialog progressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mDatabase = SqliteDatabase.getInstance(this.getActivity());
        msclass = new Messageclass(this.getActivity());
        context = this.getActivity();
        cx = new CommonExecution(context);
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Uploading Data ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        parentHolder = inflater.inflate(R.layout.fragment_retailer_data, container,
                false);
        preferences = this.getActivity().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        dialog = new ProgressDialog(this.getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka = (TextView) parentHolder.findViewById(R.id.lbltaluka);

        spDist = (SearchableSpinner) parentHolder.findViewById(R.id.spDist);
        spnewretailer = (SearchableSpinner) parentHolder.findViewById(R.id.spnewretailer);
        spBussiness = (MultiSelectionSpinner) parentHolder.findViewById(R.id.spBussiness);
        spState = (SearchableSpinner) parentHolder.findViewById(R.id.spState);
        spdistr = (MultiSelectionSpinner) parentHolder.findViewById(R.id.spdistr);
        spTaluka = (SearchableSpinner) parentHolder.findViewById(R.id.spTaluka);
        btnsave = (Button) parentHolder.findViewById(R.id.btnsave);
        txtmarketplace = (EditText) parentHolder.findViewById(R.id.txtmarketplace);
        txtrtname = (EditText) parentHolder.findViewById(R.id.txtrtname);
        txtAge = (EditText) parentHolder.findViewById(R.id.txtAge);
        txtmobile = (EditText) parentHolder.findViewById(R.id.txtmobile);
        txtyearofexperiance = (EditText) parentHolder.findViewById(R.id.txtyearofexperiance);
        txtComments = (EditText) parentHolder.findViewById(R.id.txtComments);
        txtfirmname = (EditText) parentHolder.findViewById(R.id.txtfirmname);
        radGrp = (RadioGroup) parentHolder.findViewById(R.id.radGrp);
        radGrp2 = (RadioGroup) parentHolder.findViewById(R.id.radGrp2);

        txtBirthdate = (EditText) parentHolder.findViewById(R.id.txtBirthdate);
        txtmobilewhatup = (EditText) parentHolder.findViewById(R.id.txtmobilewhatup);

        chkfirti = (CheckBox) parentHolder.findViewById(R.id.chkfirti);
        chkpesti = (CheckBox) parentHolder.findViewById(R.id.chkpesti);
        rndYes = (RadioButton) parentHolder.findViewById(R.id.rndYes);
        rndNo = (RadioButton) parentHolder.findViewById(R.id.rndNo);
        chkmahycoproduct = (CheckBox) parentHolder.findViewById(R.id.chkmahycoproduct);
        chkotherprd = (CheckBox) parentHolder.findViewById(R.id.chkotherprd);
        chktag = (CheckBox) parentHolder.findViewById(R.id.chktag);
        Utility.setRegularFont(lbltaluka, getActivity());
        Utility.setRegularFont(txtmarketplace, getActivity());
        Utility.setRegularFont(txtmobilewhatup, getActivity());
        Utility.setRegularFont(txtBirthdate, getActivity());
        Utility.setRegularFont(txtrtname, getActivity());
        Utility.setRegularFont(txtAge, getActivity());
        Utility.setRegularFont(txtmobile, getActivity());
        Utility.setRegularFont(txtComments, getActivity());
        Utility.setRegularFont(txtyearofexperiance, getActivity());
        Utility.setRegularFont(txtfirmname, getActivity());
        Utility.setRegularFont(chkfirti, getActivity());
        Utility.setRegularFont(chkpesti, getActivity());
        Utility.setRegularFont(rndYes, getActivity());
        Utility.setRegularFont(rndNo, getActivity());
        Utility.setRegularFont(chkmahycoproduct, getActivity());
        Utility.setRegularFont(chkotherprd, getActivity());
        Utility.setRegularFont(chktag, getActivity());
        Utility.setRegularFont(btnsave, getActivity());


        txtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setDateTimeField(v);
            }
        });


        pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode = pref.getString("UserID", null);

        BindState();
        binddistributor(dist); // Accodding to district
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    int stateposition = spState.getSelectedItemPosition();

                    if (stateposition == 0) {

                        Preferences.remove(context, "distposition");
                        Preferences.remove(context, "talukaposition");
                        Preferences.remove(context, "stateposition");
                        BindDist("SELECT STATE");
                        BindTaluka("SELECT DISTRICT");

                    }


                    Log.d("pref", "stateposition" + Preferences.getInt(context, "stateposition"));


                } catch (Exception e) {
                    e.printStackTrace();
                }

                //    Preferences.remove(context,"distposition");
                //  Preferences.remove(context,"talukaposition");

                BindDist(state);
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

//
                    int distposition = spDist.getSelectedItemPosition();
//                    Preferences.saveInt(context, "distposition", distposition);
//
//                    Log.d("pref", "distposition" + Preferences.getInt(context, "distposition"));
                    if (distposition == 0) {

                        Preferences.remove(context, "talukaposition");


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    // binddistributor(dist); // Accodding to district
                    //BindRetailer(dist);

                    //   Preferences.remove(context,"talukaposition");
                    BindTaluka(dist);

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
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");


//
//                    Log.d("pref", "talukaposition" + Preferences.getInt(context, "talukaposition"));

                    //Stsrt Comment
                   /* int talukaposition = spTaluka.getSelectedItemPosition();
                    Preferences.saveInt(context, "talukaposition", talukaposition);
                    int distposition = spDist.getSelectedItemPosition();
                    Preferences.saveInt(context, "distposition", distposition);
                    int stateposition = spState.getSelectedItemPosition();
                    Preferences.saveInt(context, "stateposition", stateposition);
                    */
                    // end Comment
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    // binddistributor(taluka);
                    BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });


        spnewretailer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    if (gm.Desc().trim().toUpperCase().contains("NEW RETAILER")
                            || gm.Desc().trim().toUpperCase().contains("OTHER") ) {
                        txtfirmname.setEnabled(true);

                    } else {
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

                try {
                    saveStarttravel();
                } catch (Exception ex) {

                }

            }
        });
        chkmahycoproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtmobile.getText().length() == 0) {
                    msclass.showMessage("Please enter  mobile number");
                    chkmahycoproduct.setChecked(false);
                    return;
                }
                if (txtmobile.getText().length() < 10) {
                    msclass.showMessage("Please enter 10 digit mobile number");
                    return;
                }
                if (chkmahycoproduct.isChecked()) {
                    addproduct("MAHYCO");
                }
            }
        });
        chkotherprd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtmobile.getText().length() == 0) {
                    msclass.showMessage("Please enter  mobile number");
                    chkotherprd.setChecked(false);
                    return;
                }
                if (txtmobile.getText().length() < 10) {
                    msclass.showMessage("Please enter 10 digit mobile number");
                    return;
                }
                if (chkotherprd.isChecked()) {
                    addproduct("");
                }
            }
        });
        radGrp2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case -1:
                        // Log.v(TAG, "Choices cleared!");
                        break;
                    case R.id.rndYes2:
                        if (txtmobile.getText().length() < 10) {
                            msclass.showMessage("Please enter 10 digit mobile number");
                            radGrp2.clearCheck();
                            return;
                        }
                        authorizeddistributordata();
                        break;

                    default:

                        break;
                }
            }
        });


        BindIntialData();
        if (pref.getString("RoleID", null) == null || pref.getString("RoleID", null).equals("") ||
                Integer.valueOf(pref.getString("RoleID", "0")) == 0
                || Integer.valueOf(pref.getString("RoleID", "0")) == 4) {
            // binddistributor(taluka);
            BindRetailer(taluka);
        } else {
            //  binddistributor(taluka);
            BindRetailer(taluka);

        }

        spBussiness.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });
        spdistr.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });


        return parentHolder;
    }

    private void setDateTimeField(View v) {
        final EditText txt = (EditText) v;
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
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
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
            location = arg0;
            Log.d(TAG, "onLocationChanged: " + String.valueOf(longi));
            cordinate = String.valueOf(lati) + "-" + String.valueOf(longi);

            if(address.equals("")) {
                if (new Config(context).NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            //accuracy = String.valueOf(arg0.getAccuracy());
            //Toast.makeText(context, cordinate+"S", Toast.LENGTH_SHORT).show();
            // locationInsertTime = arg0.getTime();
            //LocationBearing = arg0.getBearing();
            //LocationSpeed = arg0.getSpeed();
            //  fusedlocationRecieved = true;

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
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
        } catch (Exception ex) {
            //msclass.showMessage("Please on device GPS location.\n startFusedLocationService"+ex.getMessage());
            // msclass.showMessage("GPS is not enable,Please on GPS\n startFusedLocationService");
            Toast.makeText(this.getActivity(), "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();

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
        } catch (Exception ex) {
            Toast.makeText(this.getActivity(), "Func-onResult" + ex.toString(), Toast.LENGTH_LONG).show();

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
            if (googleApiClient != null && (googleApiClient.isConnected())) {
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
        } catch (Exception ex) {
            msclass.showMessage("Funtion name :onresume" + ex.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void BindState() {

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
                getState = Preferences.getInt(context, "stateposition");

                int compareValue = getState;
                if (compareValue != 0) {
                    spState.setSelection(compareValue);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void showdata(LinearLayout my_linear_layout1, WebView web1) {
        my_linear_layout1.removeAllViews();
        String unit = "IN KGS";
        //st
        try {
            String tag = "";
            tag = "<table style='border: 1px solid #1C6EA4; font-size: 12px; font-family: 'Calibri', Helvetica, sans-serif;' width='100%'>";


            tag = tag + "<tr style='text-align: center; background-color: #2b3a56; color: #fff;'>" +
                    "<td>COMPANY NAME</td>" +
                    "<td>CROP</td>" +
                    "<td>PRODUCT</td>" +
                    "<td>QTY</td>" +
                    "<td>UNIT</td>" +
                    "</tr>";

            //  "</table>";
            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults("select * from mdo_retailerproductdetail " +
                    "where retailermobile='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "' and Status='0' "));


            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetail, null);
                view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));

                if (jObject.getString("crop").toString().toLowerCase().contains("cotton")) {
                    unit = "PKTS";
                } else {
                    unit = "KGS";
                }

                tag = tag + "<tr style='text-align: center; background-color: #fff; color: #000;'>" +
                        "<td>" + jObject.getString("compname").toString() + "</td>" +
                        "<td>" + jObject.getString("crop").toString() + "</td>" +
                        "<td>" + jObject.getString("product").toString() + "</td>" +
                        "<td>" + jObject.getString("qty").toString() + "</td>" +
                        "<td>" + unit + "</td>" +
                        "</tr>";


            }
            tag = tag + "</table>";


            // web1.loadData(tag, "text/html", "utf-8");
            WebSettings webSettings = web1.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // wb1.loadData(result, "text/html", null);
            web1.loadDataWithBaseURL(null, tag, null, "utf-8", null);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }


    public void showdataothercomapny(LinearLayout my_linear_layout1, MultiSelectionSpinner spcrop) {
        try {
            my_linear_layout1.removeAllViews();                              //add this too

            // HEADER TAG
      /*  final View view3 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetailothercomp, null);
        view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
        TextView lblunit = (TextView) view3.findViewById(R.id.lblunit );
        TextView lblcrop = (TextView) view3.findViewById(R.id.lblcrop);
        EditText lblproduct=(EditText) view3.findViewById(R.id.lblproduct);
        EditText lblqty=(EditText) view3.findViewById(R.id.lblQty);
        lblunit.setText("UNIT");
        lblunit.setTextColor(Color.WHITE);
        // lblcomp.setTextSize(14);
        lblunit.setTypeface(lblunit.getTypeface(),Typeface.BOLD);
        lblcrop.setText("CROP");
        lblcrop.setTextColor(Color.WHITE);
        // lblcrop.setTextSize(14);
        lblcrop.setTypeface(lblcrop.getTypeface(),Typeface.BOLD);
        lblproduct.setText("PRODUCT");
        lblproduct.setTextColor(Color.WHITE);
        lblproduct.setBackgroundColor(Color.TRANSPARENT);
        // lblproduct.setTextSize(14);
        lblproduct.setTypeface(lblproduct.getTypeface(),Typeface.BOLD);
        lblqty.setText("QTY");
        lblqty.setTextColor(Color.WHITE);
        lblqty.setBackgroundColor(Color.TRANSPARENT);
        //  lblqty.setTextSize(14);
        lblqty.setTypeface(lblqty.getTypeface(),Typeface.BOLD);
        view3.setBackgroundColor(getResources().getColor(R.color.heback)) ;
        my_linear_layout1.addView(view3);

     */


            //st
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults("select * from mdo_retailerproductdetail " +
                        "where retailermobile='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "' and Status='0' "));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);

                String[] splittedValues = spcrop.getSelectedItemsAsString().split(",");
                for (int i = 0; i < splittedValues.length; i++) {

                    int total = 0;
                    for (int j = 0; j < 4; j++) {

                        final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetailothercomp, null);


                        if (j == 0) //4
                        {
                            final View view1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetailothercomp, null);

                            view1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                            EditText lblunit = (EditText) view1.findViewById(R.id.lblunit);
                            TextView lblcrop = (TextView) view1.findViewById(R.id.lblcrop);
                            EditText lblproduct = (EditText) view1.findViewById(R.id.lblproduct);
                            EditText lblqty = (EditText) view1.findViewById(R.id.lblVillage);
                            EditText txtcompname = (EditText) view1.findViewById(R.id.txtcompname);
                            txtcompname.setText("COMP NAME");
                            txtcompname.setTextColor(Color.WHITE);
                            txtcompname.setBackgroundColor(Color.TRANSPARENT);
                            txtcompname.setTypeface(lblproduct.getTypeface(), Typeface.BOLD);

                            lblunit.setText("UNIT");
                            lblunit.setTextColor(Color.WHITE);
                            lblunit.setBackgroundColor(Color.TRANSPARENT);
                            // lblcomp.setTextSize(14);
                            lblunit.setTypeface(lblunit.getTypeface(), Typeface.BOLD);
                            lblcrop.setText("CROP");
                            lblcrop.setTextColor(Color.WHITE);
                            // lblcrop.setTextSize(14);
                            lblcrop.setTypeface(lblcrop.getTypeface(), Typeface.BOLD);
                            lblproduct.setText("PRODUCT");
                            lblproduct.setTextColor(Color.WHITE);
                            lblproduct.setBackgroundColor(Color.TRANSPARENT);
                            // lblproduct.setTextSize(14);
                            lblproduct.setTypeface(lblproduct.getTypeface(), Typeface.BOLD);
                            lblqty.setText("QTY");
                            lblqty.setTextColor(Color.WHITE);
                            lblqty.setBackgroundColor(Color.TRANSPARENT);
                            //  lblqty.setTextSize(14);
                            lblqty.setTypeface(lblqty.getTypeface(), Typeface.BOLD);
                            view1.setBackgroundColor(getResources().getColor(R.color.heback));
                            my_linear_layout1.addView(view1);
                        }
                        //else

                        {
                            view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                            EditText lblunit1 = (EditText) view2.findViewById(R.id.lblunit);
                            TextView lblcrop1 = (TextView) view2.findViewById(R.id.lblcrop);
                            EditText lblproduct1 = (EditText) view2.findViewById(R.id.lblproduct);
                            EditText lblqty1 = (EditText) view2.findViewById(R.id.lblVillage);

                            EditText txtcompname = (EditText) view2.findViewById(R.id.txtcompname);


                            // String[] prdnamelist = splittedValues[i].toString().split("~");
                            lblunit1.setText("");
                            if (splittedValues[i].toString().toLowerCase().contains("cotton")) {
                                lblunit1.setText("PKTS");
                            } else {
                                lblunit1.setText("KGS");
                            }
                            lblunit1.setBackgroundColor(Color.TRANSPARENT);
                            lblcrop1.setText(splittedValues[i].toString());
                            if (j == 3) {
                                lblproduct1.setText("Other");
                            }
                            //lblqty1.setText(jObject.getString("qty").toString());
                            my_linear_layout1.addView(view2);
                        }
                    }
                }
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void showdataold(LinearLayout my_linear_layout1, MultiSelectionSpinner spproduct) {
        try {


            my_linear_layout1.removeAllViews();                              //add this too

            // HEADER TAG
            final View view3 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetail, null);
            view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            TextView lblunit = (TextView) view3.findViewById(R.id.lblunit);
            TextView lblcrop = (TextView) view3.findViewById(R.id.lblcrop);
            EditText lblproduct = (EditText) view3.findViewById(R.id.lblproduct);
            EditText lblqty = (EditText) view3.findViewById(R.id.lblVillage);
            lblunit.setText("UNIT");
            lblunit.setTextColor(Color.WHITE);
            // lblcomp.setTextSize(14);
            lblunit.setTypeface(lblunit.getTypeface(), Typeface.BOLD);
            lblcrop.setText("CROP");
            lblcrop.setTextColor(Color.WHITE);
            // lblcrop.setTextSize(14);
            lblcrop.setTypeface(lblcrop.getTypeface(), Typeface.BOLD);
            lblproduct.setText("PRODUCT");
            lblproduct.setTextColor(Color.WHITE);
            // lblproduct.setTextSize(14);
            lblproduct.setTypeface(lblproduct.getTypeface(), Typeface.BOLD);
            lblqty.setText("QTY");
            lblqty.setTextColor(Color.WHITE);
            lblqty.setBackgroundColor(Color.TRANSPARENT);
            //  lblqty.setTextSize(14);
            lblqty.setTypeface(lblqty.getTypeface(), Typeface.BOLD);
            view3.setBackgroundColor(getResources().getColor(R.color.heback));
            my_linear_layout1.addView(view3);


            // Show Existing Data
            List<String> dd = spproduct.getSelectedStrings();
            String instring = "";
            for (int a = 0; a < dd.size(); a++) {
                instring = instring + "'" + dd.get(a).trim() + "',";
                //'JOWAR','MAIZE','WHEAT',
            }
            instring = instring.substring(0, instring.length() - 1);
            // end


            //st
            try {


                String[] splittedValues = spproduct.getSelectedItemsAsString().split(",");
                for (int i = 0; i < splittedValues.length; i++) {
                    String[] prdnamelist = splittedValues[i].toString().split("~");
                    JSONObject object = new JSONObject();
                    object.put("Table1", mDatabase.getResults("select * from mdo_retailerproductdetail " +
                            "where retailermobile='" + txtmobile.getText().toString() + "' " +
                            "and mdocode='" + usercode + "' and compname='MAHYCO'  and product='" + prdnamelist[0].toString().trim() + "' "));

                    JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);

                    final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetail, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    TextView lblunit1 = (TextView) view2.findViewById(R.id.lblunit);
                    TextView lblcrop1 = (TextView) view2.findViewById(R.id.lblcrop);
                    EditText lblproduct1 = (EditText) view2.findViewById(R.id.lblproduct);
                    EditText lblqty1 = (EditText) view2.findViewById(R.id.lblVillage);
                    lblunit1.setText("");
                    if (prdnamelist[1].toString().toLowerCase().contains("cotton")) {
                        lblunit1.setText("PKTS");
                    } else {
                        lblunit1.setText("KGS");
                    }

                    lblcrop1.setText(prdnamelist[1].toString());
                    lblproduct1.setText(prdnamelist[0].toString().trim());
                    if (jArray.length() > 0) {
                        JSONObject jObject = jArray.getJSONObject(0);
                        lblqty1.setText(jObject.getString("qty"));
                    }
                    //lblqty1.setText(jObject.getString("qty").toString());
                    my_linear_layout1.addView(view2);
                }
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private List<KeyPairBoolData> setSelected(CharSequence[] mainItems, String spinnerCustomFieldValues) {
        List<KeyPairBoolData> listArray0 = new ArrayList<>();
        if (spinnerCustomFieldValues != null && !spinnerCustomFieldValues.equals("")) {
            CharSequence[] existinVal = spinnerCustomFieldValues.split(",");

            for (int i = 0; i < mainItems.length; i++) { //5
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(i + 1);
                h.setName(mainItems[i].toString().trim());

                // if (isValueExist(existinVal, mainItems[i])) {
                // if (isValueExist(existinVal, mainItems[i])) {
                // h.setSelected(true);
                // } else {
                //    h.setSelected(false);
                // }
                listArray0.add(h);
            }
        } else {
            for (int i = 0; i < mainItems.length; i++) { //5
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(i + 1);
                h.setName(mainItems[i].toString());
                h.setSelected(false);
                listArray0.add(h);
            }
        }
        return listArray0;
    }

    public void addproduct(final String compname) {
        try {
            boolean flag = false;

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.addretailerproductnew);
            // dialog.setTitle("Service Operator Assignment");
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            // showdata(my_linear_layout1,web1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final MultiSelectionSpinner spCropType = (MultiSelectionSpinner) dialog.findViewById(R.id.spCropType);
            final MultiSelectionSpinner spproduct = (MultiSelectionSpinner) dialog.findViewById(R.id.spProductName);
            final CardView card_month9 = (CardView) dialog.findViewById(R.id.card_month9);

            final TextView lblproducthead = (TextView) dialog.findViewById(R.id.lblproducthead);

            final EditText txtqty = (EditText) dialog.findViewById(R.id.txtqty);
            final EditText txtcompname = (EditText) dialog.findViewById(R.id.txtcompname);

            final TextView lblunit = (TextView) dialog.findViewById(R.id.lblunit);

            if (compname.contains("MAHYCO")) {
                txtcompname.setText("MAHYCO");
                txtcompname.setEnabled(false);

                spproduct.setVisibility(View.VISIBLE);
                // txtcompname.setVisibility(View.GONE);
            } else {

                card_month9.setVisibility(View.GONE);
                lblproducthead.setVisibility(View.GONE);
                spproduct.setVisibility(View.GONE);
                txtcompname.setVisibility(View.GONE);
            }
            final WebView webView = (WebView) dialog.findViewById(R.id.webView);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            bindcroptype(spCropType, "C");
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            String[] array = null;
            array = new String[1];
            array[0] = "SELECT PRODUCT";
            spproduct.setItems(array);
            spproduct.hasNoneOption(true);
            spproduct.setSelection(new int[]{0});


            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String crop = spCropType.getSelectedItem().toString();
                        int count = 0;
                        String product = "";
                        if (crop.toLowerCase().contains("select crop")) {
                            msclass.showMessage("Please select crop");
                            return;
                        }
                        if (compname.contains("MAHYCO")) {
                            txtcompname.setText("MAHYCO");
                        } else {

                        }


                        //St
                        String Compnametemp = "";
                        for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
                            View view = my_linear_layout1.getChildAt(i);
                            TextView lblcrop = (TextView) view.findViewById(R.id.lblcrop);
                            final EditText lblproduct = (EditText) view.findViewById(R.id.lblproduct);
                            final EditText lblQty = (EditText) view.findViewById(R.id.lblVillage);
                            final TextView lblunit = (TextView) view.findViewById(R.id.lblunit);
                            if (compname.contains("MAHYCO")) {
                                Compnametemp = compname;
                            } else {
                                final EditText txtcompname = (EditText) view.findViewById(R.id.txtcompname);
                                Compnametemp = txtcompname.getText().toString();
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = new Date();
                            final String strdate = dateFormat.format(d);
                            if ((!lblunit.getText().toString().toUpperCase().contains("UNIT")) && (!TextUtils.isEmpty(lblQty.getText().toString())) && (!TextUtils.isEmpty(lblproduct.getText().toString()))) {


                                boolean f = mDatabase.InsertRetailerproductData(pref.getString("UserID", null), Compnametemp,
                                        lblcrop.getText().toString().trim(), lblproduct.getText().toString().trim(), lblQty.getText().toString(), txtmobile.getText().toString());


                                if (f == true) {
                                    count++;
                                } else {

                                }
                            }

                        }

                        //ene

                        if (count > 0) {
                            msclass.showMessage("product detail added successfully.");
                            spCropType.setSelection(0);
                            my_linear_layout1.removeAllViews();
                            if (compname.contains("MAHYCO")) {
                                // spproduct.setSelection(0);
                                chkmahycoproduct.setChecked(true);
                            } else {
                                chkotherprd.setChecked(true);
                            }
                            // showdata(my_linear_layout1,web1);
                            //lblStockDetail.setText("Stock Details Added");
                            // txtstock.setText("");
                            // bindstockdata(webView);


                        } else {
                            msclass.showMessage("Please enter product details");
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });


            spproduct.setListener(new MultiSelectionSpinner.MySpinnerListener() {

                @Override
                public void onItemClicked(int which) {
                    // String dd=spCropType.getSelectedItemsAsString() ;//getSelectedStrings();
                    // BindProductName(spCropType,spproduct,dd,null,1);
                    try {
                        if (spproduct.getSelectedItem().toString().toLowerCase().contains("select product")) {
                        } else {
                            showdataold(my_linear_layout1, spproduct);
                        }
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void selectedIndices(List<Integer> indices) {

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }


            });
            spCropType.setListener(new MultiSelectionSpinner.MySpinnerListener() {

                @Override
                public void onItemClicked(int which) {
                    try {
                        String dd = spCropType.getSelectedItemsAsString();//getSelectedStrings();

                        if (dd.toLowerCase().contains("select crop")) {
                            msclass.showMessage("Please select crop");
                            return;
                        }
                        if (compname.contains("MAHYCO")) {
                            if (chkmahycoproduct.isChecked()) {
                                BindProductName(spCropType, spproduct, dd, null, 1);
                            }
                        } else {
                            if (chkotherprd.isChecked()) {
                                card_month9.setVisibility(View.GONE);
                                lblproducthead.setVisibility(View.GONE);
                                showdataothercomapny(my_linear_layout1, spCropType);
                            }
                        }
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void selectedIndices(List<Integer> indices) {

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }


            });


           /* spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   // GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                        //spdistr.getSelectedItemsAsString().toString()

                        croptype =  spCropType.getSelectedItemsAsString().toString();
                        //URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                        if(croptype.equals("COTTON"))
                        {
                            lblunit.setText("ADD QUANTITY(IN PKTS)");
                            txtqty.setHint("IN PKTS");
                        }
                        else
                        {
                            lblunit.setText("ADD QUANTITY(IN KGS)");
                            txtqty.setHint("IN KGS");
                        }
                    }
                    catch (Exception ex)
                    {
                        msclass.showMessage(ex.toString());
                    }

                    // check = check + 1;
                    // if (check > 1)
                    {

                        BindProductName(spCropType,spproduct,croptype,null,1);


                    }

                    // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    //Toast.makeText(MyFieldActvity.this, "gngvnv", Toast.LENGTH_LONG).show();
                }
            });
*/


            dialog.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            //Window window = dialog.getWindow();
            //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }

    public void authorizeddistributordata() {
        if (txtmobile.getText().length() == 0) {
            // msclass.showMessage("Please enter  mobile number");
            txtmobile.setError("Please enter  mobile number");
            radGrp2.clearCheck();


        } else {
            try {
                boolean flag = false;

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.authoriseddistributorproductdetail);
                // dialog.setTitle("Service Operator Assignment");
                RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
                final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
                // showdata(my_linear_layout1,web1);
                Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
                final CardView card_month8 = (CardView) dialog.findViewById(R.id.card_month8);

                final TextView lblcrop = (TextView) dialog.findViewById(R.id.lblcrop);


                final MultiSelectionSpinner spCropType = (MultiSelectionSpinner) dialog.findViewById(R.id.spCropType);
                final MultiSelectionSpinner spCompanyName = (MultiSelectionSpinner) dialog.findViewById(R.id.spCompanyName);

                String[] array2 = {"SELECT COMPANY", "AJIT", "ANKUR", "BAYER", "DHAANYA", "GK", "JK", "KAVERI"
                        , "PHI", "MONSANTO", "NUZIVEEDU/NSL", "US AGRI", "VNR SEEDS", " RASI", "SYNGENTA", "UPL", "OTHERS"};
                spCompanyName.setItems(array2);
                spCompanyName.hasNoneOption(true);
                spCompanyName.setSelection(new int[]{0});


                final EditText txtqty = (EditText) dialog.findViewById(R.id.txtqty);
                final TextView lblunit = (TextView) dialog.findViewById(R.id.lblunit);

                final WebView webView = (WebView) dialog.findViewById(R.id.webView);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                bindcroptype(spCropType, "C");
                ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
                imgclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                btnaddmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            final String crop = spCropType.getSelectedItem().toString();
                            int count = 0;
                            String product = "";
                            if (crop.equals("SELECT CROP")) {
                                msclass.showMessage("Please select crop");
                                return;
                            }
                            //St
                            for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
                                View view = my_linear_layout1.getChildAt(i);
                                TextView lblcrop = (TextView) view.findViewById(R.id.lblcrop);
                                final EditText lblproduct = (EditText) view.findViewById(R.id.lblproduct);
                                final EditText lblQty = (EditText) view.findViewById(R.id.lblVillage);
                                final TextView lblunit = (TextView) view.findViewById(R.id.lblunit);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date d = new Date();
                                final String strdate = dateFormat.format(d);
                                if ((!lblunit.getText().toString().toUpperCase().contains("UNIT")) && (!TextUtils.isEmpty(lblQty.getText().toString())) && (!TextUtils.isEmpty(lblproduct.getText().toString()))) {
                                    boolean f = mDatabase.InsertRetailerproductData(pref.getString("UserID", null), "",
                                            lblcrop.getText().toString().trim(), lblproduct.getText().toString().trim(), lblQty.getText().toString(), txtmobile.getText().toString());
                                    if (f == true) {
                                        count++;
                                    } else {

                                    }
                                }

                            }

                            //ene

                            if (count > 0) {
                                msclass.showMessage("product detail added successfully.");
                                spCropType.setSelection(0);
                                my_linear_layout1.removeAllViews();
                                int btn = radGrp2.getCheckedRadioButtonId();
                                int radioButtonID = radGrp2.getCheckedRadioButtonId();
                                RadioButton radioButton = (RadioButton) radGrp2.findViewById(radioButtonID);
                                String selectaware = (String) radioButton.getText();
                                if (selectaware.toLowerCase().contains("yes")) {
                                    radioButton.setChecked(true);
                                }

                                // showdata(my_linear_layout1,web1);
                                //lblStockDetail.setText("Stock Details Added");
                                // txtstock.setText("");
                                // bindstockdata(webView);


                            } else {
                                msclass.showMessage("Please enter product details");
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            msclass.showMessage(ex.toString());

                        }
                    }
                });


                spCompanyName.setListener(new MultiSelectionSpinner.MySpinnerListener() {

                    @Override
                    public void onItemClicked(int which) {
                        // String dd=spCropType.getSelectedItemsAsString() ;//getSelectedStrings();
                        // BindProductName(spCropType,spproduct,dd,null,1);
                        // showdataold(my_linear_layout1,spproduct);
                        //showauthoriseddistProduct(my_linear_layout1,spCropType,spCompanyName);
                    }

                    @Override
                    public void selectedIndices(List<Integer> indices) {

                    }

                    @Override
                    public void selectedStrings(List<String> strings) {

                    }


                });
                spCropType.setListener(new MultiSelectionSpinner.MySpinnerListener() {
                    @Override
                    public void onItemClicked(int which) {
                        String dd = spCropType.getSelectedItemsAsString();//getSelectedStrings();
                        if (spCompanyName.getSelectedItemsAsString().toLowerCase().contains("select")) {
                            msclass.showMessage("Please select companies name");
                        } else {
                            showauthoriseddistProduct(my_linear_layout1, spCropType, spCompanyName);
                        }

                    }

                    @Override
                    public void selectedIndices(List<Integer> indices) {

                    }

                    @Override
                    public void selectedStrings(List<String> strings) {

                    }


                });


                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                //Window window = dialog.getWindow();
                //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
                dialog.dismiss();
            }
        }

    }

    public void showauthoriseddistProduct(LinearLayout my_linear_layout1, MultiSelectionSpinner spcrop, MultiSelectionSpinner spCompanyName) {
        my_linear_layout1.removeAllViews();                              //add this too

        // HEADER TAG
        final View view3 = LayoutInflater.from(this.getActivity()).inflate(R.layout.authoriseddistcroprow, null);
        view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
        //TextView lblCompname = (TextView) view3.findViewById(R.id.lblCompname );
        /*Button lblcrop1 = (Button) view3.findViewById(R.id.lblcrop1);
        Button lblcrop2 = (Button) view3.findViewById(R.id.lblcrop2);
        Button lblcrop3 = (Button) view3.findViewById(R.id.lblcrop3);
        Button lblcrop4 = (Button) view3.findViewById(R.id.lblcrop4);
       */
        LinearLayout linearLayout = new LinearLayout(this.getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(MATCH_PARENT, 60);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        int count = 10;
        // linearLayout.addView(tableLayout(count));
        TextView lblCompname = new TextView(this.getActivity());

        //et.setId(numberOfLines + 1);
        // ll.addView(et);
        String[] spComp = spCompanyName.getSelectedItemsAsString().split(",");
        String[] splittedValues = spcrop.getSelectedItemsAsString().split(",");

        lblCompname.setText("COMPANY NAME");
        lblCompname.setTextColor(Color.WHITE);
        lblCompname.setBackgroundColor(Color.TRANSPARENT);
        // lblCompname.setTypeface(lblCompname.getTypeface(),Typeface.BOLD);

        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT);
        lblCompname.setLayoutParams(p);
        lblCompname.setTextSize(11);
        linearLayout.addView(lblCompname);

        for (int k = 0; k < splittedValues.length; k++) {

            linearLayout.addView(AddTextView("", splittedValues[k].toString()));

        }

        linearLayout.setBackgroundColor(getResources().getColor(R.color.heback));
        my_linear_layout1.addView(linearLayout);


        //st
        try {
            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults("select * from mdo_retailerproductdetail " +
                    "where retailermobile='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "' and Status='0' "));

            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);


            // for (int k=0;k<spComp.length;k++ ) {
            for (int i = 0; i < spComp.length; i++) {
                int total = 0;
                LinearLayout linearLayout1 = new LinearLayout(this.getActivity());
                //ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(MATCH_PARENT, 50);
                // linearLayout1.setLayoutParams(params1);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

                final TextView lblCompname1 = new TextView(this.getActivity());
                ViewGroup.LayoutParams p1 = new ViewGroup.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT);
                lblCompname1.setLayoutParams(p1);
                lblCompname1.setText(spComp[i].toString());
                lblCompname1.setTextSize(12);
                linearLayout1.addView(lblCompname1);

                for (int k = 0; k < splittedValues.length; k++) {

                    linearLayout1.addView(AddButton(spComp[i].toString(), splittedValues[k].toString()));

                }


                my_linear_layout1.addView(linearLayout1);


            }
            // }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private TextView AddTextView(final String Comp, final String crop) {
        TextView button = new TextView(this.getActivity());
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(110, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(p);
        button.setText(crop);
        button.setTextColor(Color.WHITE);
        button.setTextSize(11);
        //button.setTypeface(button.getTypeface(),Typeface.BOLD);
        button.setBackgroundColor(Color.TRANSPARENT);
        return button;
    }

    private Button AddButton(final String Comp, final String crop) {
        Button button = new Button(this.getActivity());
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(110, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(p);

        // button.setTextSize(10);
        button.setText("ADD");
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    innerpopauthorisedistrcropwise(Comp, crop);
                } catch (Exception ex) {
                }

            }
        });

        return button;
    }


    public void innerpopauthorisedistrcropwise(final String compname, final String crop) {


        //st
        // HEADER TAG


        try {
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.innerpopauthorisedistrcropwise);
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            final Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);

            TextView lblCompname = (TextView) dialog.findViewById(R.id.lblCompname);
            lblCompname.setText("COMPANY NAME -" + compname);
            lblCompname.setVisibility(View.GONE);
            // HEADER TAG
            final View view3 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetailothercomp, null);
            view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            EditText lblunit = (EditText) view3.findViewById(R.id.lblunit);
            TextView lblcrop = (TextView) view3.findViewById(R.id.lblcrop);
            EditText lblproduct = (EditText) view3.findViewById(R.id.lblproduct);
            EditText lblqty = (EditText) view3.findViewById(R.id.lblVillage);
            final EditText txtcompname = (EditText) view3.findViewById(R.id.txtcompname);

            txtcompname.setText("COMP NAME");
            txtcompname.setTextColor(Color.WHITE);
            txtcompname.setBackgroundColor(Color.TRANSPARENT);
            lblunit.setText("UNIT");
            lblunit.setTextColor(Color.WHITE);
            lblunit.setBackgroundColor(Color.TRANSPARENT);
            // lblcomp.setTextSize(14);
            // lblunit.setTypeface(lblunit.getTypeface(),Typeface.BOLD);
            lblcrop.setText("CROP");
            lblcrop.setTextColor(Color.WHITE);
            // lblcrop.setTextSize(14);
            // lblcrop.setTypeface(lblcrop.getTypeface(),Typeface.BOLD);
            lblproduct.setText("PRODUCT");
            lblproduct.setTextColor(Color.WHITE);
            lblproduct.setBackgroundColor(Color.TRANSPARENT);
            // lblproduct.setTextSize(14);
            // lblproduct.setTypeface(lblproduct.getTypeface(),Typeface.BOLD);
            lblqty.setText("QTY");
            lblqty.setTextColor(Color.WHITE);
            lblqty.setBackgroundColor(Color.TRANSPARENT);
            //  lblqty.setTextSize(14);
            //lblqty.setTypeface(lblqty.getTypeface(),Typeface.BOLD);
            view3.setBackgroundColor(getResources().getColor(R.color.heback));
            my_linear_layout1.addView(view3);


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            int total = 0;
            for (int j = 0; j < 4; j++) {

                final View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.showaddproductdetailothercomp, null);

                {
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    EditText txtcompname1 = (EditText) view2.findViewById(R.id.txtcompname);
                    EditText lblunit1 = (EditText) view2.findViewById(R.id.lblunit);
                    TextView lblcrop1 = (TextView) view2.findViewById(R.id.lblcrop);
                    EditText lblproduct1 = (EditText) view2.findViewById(R.id.lblproduct);
                    EditText lblqty1 = (EditText) view2.findViewById(R.id.lblVillage);
                    txtcompname1.setText(compname);
                    lblunit1.setText("");
                    lblunit1.setBackgroundColor(Color.TRANSPARENT);
                    if (crop.toLowerCase().contains("cotton")) {
                        lblunit1.setText("PKTS");
                    } else {
                        lblunit1.setText("KGS");
                    }
                    lblcrop1.setText(crop);
                    if (j == 3) {
                        lblproduct1.setText("Other");
                    }
                    my_linear_layout1.addView(view2);
                }
            }

            // Show existing data
            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults("select * from mdo_authoriseddistributorproduct " +
                    "where retailermobile='" + txtmobile.getText().toString() + "'" +
                    " and compname='" + compname.trim() + "'" +
                    " and  mdocode='" + usercode + "'  and crop='" + crop.trim() + "' "));
            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {
                if (jArray.length() < 5) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    View view = my_linear_layout1.getChildAt(i + 1);
                    TextView lblcrop1 = (TextView) view.findViewById(R.id.lblcrop);
                    final EditText lblproduct1 = (EditText) view.findViewById(R.id.lblproduct);
                    final EditText lblQty1 = (EditText) view.findViewById(R.id.lblVillage);
                    //final  TextView lblunit1 = (TextView) view.findViewById(R.id.lblunit);
                    lblcrop1.setText(crop);
                    lblQty1.setText(jObject.getString("qty").toString());
                    lblproduct1.setText(jObject.getString("product").toString());
                    final EditText txtcompname1 = (EditText) view.findViewById(R.id.txtcompname);
                    txtcompname1.setText(jObject.getString("compname").toString());
                }
            }
            //end existing data


            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int count = 0;
                        String product = "";
                        //St
                        mDatabase.deleterecord("delete from mdo_authoriseddistributorproduct " +
                                "where retailermobile='" + txtmobile.getText().toString() + "' and compname='" + compname + "'" +
                                " and  mdocode='" + usercode + "'  and crop='" + crop.trim() + "'  ");


                        for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
                            View view = my_linear_layout1.getChildAt(i);
                            TextView lblcrop = (TextView) view.findViewById(R.id.lblcrop);

                            final EditText lblproduct = (EditText) view.findViewById(R.id.lblproduct);
                            final EditText lblQty = (EditText) view.findViewById(R.id.lblVillage);
                            final TextView lblunit = (TextView) view.findViewById(R.id.lblunit);
                            final EditText txtcompname = (EditText) view.findViewById(R.id.txtcompname);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = new Date();
                            final String strdate = dateFormat.format(d);
                            if ((!lblunit.getText().toString().toUpperCase().contains("UNIT")) && (!TextUtils.isEmpty(lblQty.getText().toString())) && (!TextUtils.isEmpty(lblproduct.getText().toString()))) {
                                boolean f = mDatabase.insertauthoriseddistributorproduct(pref.getString("UserID",
                                        null), txtcompname.getText().toString().trim(),
                                        lblcrop.getText().toString().trim(), lblproduct.getText().toString().trim(),
                                        lblQty.getText().toString(), txtmobile.getText().toString());
                                if (f == true) {
                                    count++;
                                } else {

                                }
                            }

                        }

                        //ene

                        if (count > 0) {
                            msclass.showMessage("product detail added successfully.");
                            my_linear_layout1.removeAllViews();
                            btnaddmore.setVisibility(View.GONE);
                            // showdata(my_linear_layout1,web1);
                            //lblStockDetail.setText("Stock Details Added");
                            // txtstock.setText("");
                            // bindstockdata(webView);


                        } else {
                            msclass.showMessage("Please enter product details");
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });


            dialog.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            //Window window = dialog.getWindow();
            //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }

    private void BindProductName(MultiSelectionSpinner spCropType, MultiSelectionSpinner spProductName, String croptype, LinearLayout my_linear_layout1, int flag) {
        //st
        try {
            List<String> dd = spCropType.getSelectedStrings();
            String instring = "";
            for (int a = 0; a < dd.size(); a++) {
                instring = instring + "'" + dd.get(a).trim() + "',";
                //'JOWAR','MAIZE','WHEAT',
            }
            instring = instring.substring(0, instring.length() - 1);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName,CropName   FROM CropMaster " +
                    "where CropName in(" + instring + ")  order by CropName ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
            cursor.moveToFirst();
            if (flag == 2) {
                my_linear_layout1.removeAllViews();
                // for (int i = 0; i < cursor.getCount(); i++) {
                // JSONObject jObject = jArray.getJSONObject(i);


                while (cursor.isAfterLast() == false) {

                    View view2 = LayoutInflater.from(this.context).inflate(R.layout.stockrow, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    TextView text = (TextView) view2.findViewById(R.id.text);
                    final EditText txtStock = (EditText) view2.findViewById(R.id.txtStock);
                    final EditText txtsalestock = (EditText) view2.findViewById(R.id.txtsalestock);
                    final EditText txtCurrentStock = (EditText) view2.findViewById(R.id.txtCurrentStock);
                    txtsalestock.setEnabled(false);
                    //Start

                    txtStock.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }

                                    //double cal = Double.parseDouble(txtGuntha.getText().toString()) * (Double.parseDouble(String.valueOf(s)));
                                    //txtcharges.setText(String.valueOf(cal));
                                    // txtsalestock.setText("");
                                    if (txtCurrentStock.getText().length() != 0) {
                                        int cont = Integer.valueOf(txtStock.getText().toString()) - Integer.valueOf(txtCurrentStock.getText().toString());
                                        txtsalestock.setText(String.valueOf(cont));
                                    }
                                    // txtCurrentStock.setText("");
                                } else {
                                    //txtStock.setText("");
                                    txtsalestock.setText("");
                                    txtCurrentStock.setText("");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }


                    });

                    //AREA CALULATION ON MANUAL Entry
                    txtCurrentStock.addTextChangedListener(new TextWatcher() {
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }
                                    //String gunthaarea = String.valueOf(Math.round(Double.parseDouble(String.valueOf(s)) * 40.00871459694989));
                                    int currentst = Integer.parseInt(String.valueOf(s));
                                    int receivestock = Integer.parseInt(String.valueOf(txtStock.getText()));
                                    if (currentst > receivestock) {
                                        msclass.showMessage("Current stock should not more than received stock.");
                                        txtCurrentStock.setText("");
                                    } else {
                                        txtsalestock.setText(String.valueOf(receivestock - currentst));
                                    }

                                } else {
                                    //txtCurrentStock.setText("");
                                    txtsalestock.setText("");
                                }
                            } catch (Exception ex) {
                                msclass.showMessage(ex.getMessage());
                                ex.printStackTrace();
                            }

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        public void afterTextChanged(Editable s) {

                        }
                    });
                    //end


                    if (croptype.equals("Cotton")) {
                        txtStock.setHint("Pkts");
                        txtsalestock.setHint("Pkts");
                        txtCurrentStock.setHint("Pkts");
                    } else {
                        txtStock.setHint("Bags");
                        txtsalestock.setHint("Bags");
                        txtCurrentStock.setHint("Bags");
                    }
                    text.setText(cursor.getString(0));
                    cursor.moveToNext();
                    my_linear_layout1.addView(view2);
                }
                cursor.close();

                // }
            }
            if (flag == 1) {


                String[] array;
                try {


                    JSONObject object = new JSONObject();
                    object.put("Table1", mDatabase.getResults(searchQuery));

                    JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                    array = new String[jArray.length() + 1];
                    array[0] = "SELECT PRODUCT";
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        array[i + 1] = jObject.getString("ProductName").toString() + "~" + jObject.getString("CropName").toString();
                    }
                    if (array.length > 0) {
                        spProductName.setItems(array);
                        spProductName.hasNoneOption(true);
                        spProductName.setSelection(new int[]{0});

                        // spProductName.setListener(this);
                    }
                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


        //en
    }

    private void bindcroptype(MultiSelectionSpinner spCropType, String Croptype) {
        String[] array;
        try {

            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' order by CropName ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster order by CropName  ";

            }

            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT CROP";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("CropName").toString();
            }
            if (array.length > 0) {
                spCropType.setItems(array);
                spCropType.hasNoneOption(true);
                spCropType.setSelection(new int[]{0});
                // spCropType.setListener(this);
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


        //en

    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        // Toast.makeText(this.getActivity(),"Selected Companies" + strings,Toast.LENGTH_LONG).show();
    }

    private void BindIntialData() {

       /* spDist.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0","SELECT DISTRICT"));
        gm.add(new GeneralMaster("1000","JALNA"));

        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDist.setAdapter(adapter);

        spTerritory.setAdapter(null);
        List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
        gm2.add(new GeneralMaster("0","SELECT TALUKA"));
        gm2.add(new GeneralMaster("01","JALNA"));
        ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTerritory.setAdapter(adapter2);

*/
        // spdistr.setAdapter(null);
       /* List<GeneralMaster> gm3 = new ArrayList<GeneralMaster>();
        gm3.add(new GeneralMaster("0","SELECT DISTRIBUTOR"));
        gm3.add(new GeneralMaster("01","OTHER"));
        adapter3 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spdistr.setAdapter(adapter3);
        String[] array = {"SELECT DISTRIBUTOR"};
        spdistr.setItems(array);
        spdistr.hasNoneOption(true);
        spdistr.setSelection(new int[]{0});
        //spdistr.setListener(this);
*/
        try {
            String[] array2 = {"SELECT", "FERTILIZERS", "PESTICIDES", "FARM MACHINERY", "OTHERS"};
            spBussiness.setItems(array2);
            spBussiness.hasNoneOption(true);
            spBussiness.setSelection(new int[]{0});
            // spBussiness.setListener(this);

            Bundle bundle = getActivity().getIntent().getExtras();
            //from = bundle.getString("From");
        }
        catch (Exception ex)
        {
            // Toast.makeText("TT",long.)
        }


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
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }

    private void captureImage() {

        try {

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        if (imageselect == 1) {
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
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
    }

    private File createImageFile4() //  throws IOException
    {
        File mediaFile = null;
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
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
        return mediaFile;
    }

    private File createImageFile() {
        // Create an image file name
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.toString());
        }
        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void cameraIntent() {
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

        } catch (Exception ex) {
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }
    }

    private void onCaptureImageResult(Intent data) {


        /*Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect==1) {
            ivImage.setImageBitmap(bm);
        }
        if (imageselect==2) {
            ivImage2.setImageBitmap(bm);
        }*/


        // Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        //thumbnail = Bitmap.createScaledBitmap(thumbnail, 300, 400, false);
        // thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        // destination = new File(Environment.getExternalStorageDirectory(),
        //       "Save.jpg");

        // FileOutputStream fo;
        //try {
        // destination.createNewFile();
        //fo = new FileOutputStream(destination);
        //fo.write(bytes.toByteArray());
        //fo.close();
        //  } catch (FileNotFoundException e) {
        //    e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        try {
            if (imageselect == 1) {
                //ivImage.setImageBitmap(thumbnail);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                //Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                // String path = getImageUri(getApplicationContext(), thumbnail);
                // msclass.showMessage(pictureImagePath);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                //File finalFile = new File(getRealPathFromURI1(tempUri));
                //Strt
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize = 2;

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    Imagepath1 = photoFile.getAbsolutePath();
                    ivImage.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }

        } catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect == 1) {
            ivImage.setImageBitmap(bm);
        }

    }

    public void saveStarttravel() {
        try {

            if (turnGPSOn() == false) {

                Toast.makeText(this.getActivity(), "GPS is not enabled,Please on GPS", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                if (validation() == true) {
                    GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                    GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                    // GeneralMaster vt = (GeneralMaster) spdistr .getSelectedItem();

                    try {

                        dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                        taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                        // village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");

                        int count = mDatabase.getrowcount("select * from mdo_Retaileranddistributordata " +
                                "where mobileno='" + txtmobile.getText().toString() + "'  and type ='Retailer'");
                        if (count > 0) {
                            msclass.showMessage("This retailer data already saved on this mobile number " + txtmobile.getText().toString() + "");


                        } else {
                            Savedata("", state, dist, taluka, village);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());

        }


    }

//    public void GetDataRetailer() {
//
//        Cursor Retailerdata = mDatabase.fetchStateDistTaluka(txtmobile.getText().toString());
//
//
//        if (Retailerdata.getCount() == 0) {
//
//        } else {
//            Retailerdata.moveToFirst();
//            if (Retailerdata != null) {
//                do {
//
//                    getState = Retailerdata.getString(Retailerdata.getColumnIndex("SoilType"));
//                    getDistrict = Retailerdata.getString(Retailerdata.getColumnIndex("SoilType"));
//                    getTaluka = Retailerdata.getString(Retailerdata.getColumnIndex("SoilType"));
//
//                } while (Retailerdata.moveToNext());
//
//
//            }
//
//            Retailerdata.close();
//        }
//    }

    private void Savedata(final String myactvity, String state, String dist, String taluka, String village) {
        try {
            pref.getString("UserID", null);
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

                    String marketplace = txtmarketplace.getText().toString();
                    String retailername = txtrtname.getText().toString();
                    String retailerfirm = spnewretailer.getSelectedItem().toString();
                    String newfirm = txtfirmname.getText().toString();
                    String mobileno = txtmobile.getText().toString();
                    String age = txtAge.getText().toString();
                    String asscoidistributor = spdistr.getSelectedItemsAsString().toString();
                    String expyear = txtyearofexperiance.getText().toString();
                    String comments = txtComments.getText().toString();
                    String mahycoretailer = "";
                    String Otherbussiness = spBussiness.getSelectedItemsAsString().toString();
                    /*if(chkfirti.isChecked()==true)
                    {
                        Otherbussiness=Otherbussiness+chkfirti.getText().toString()+",";
                    }
                    if(chkpesti.isChecked()==true)
                    {
                        Otherbussiness=Otherbussiness+chkpesti.getText().toString();
                    }*/
                    if (rndNo.isChecked() == true) {
                        mahycoretailer = "NO";
                    }
                    if (rndYes.isChecked() == true) {
                        mahycoretailer = "YES";
                    }
                    String taluka = "";
                    if (pref.getString("RoleID", null) == null || pref.getString("RoleID", null).equals("") ||
                            Integer.valueOf(pref.getString("RoleID", "0")) == 0
                            || Integer.valueOf(pref.getString("RoleID", "0")) == 4) {
                        //taluka= spTerritory.getSelectedItem().toString().trim();
                    }
                    taluka = spTaluka.getSelectedItem().toString().trim();
                    String birthdate = txtBirthdate.getText().toString().trim();
                    String WhatsappNo = txtmobilewhatup.getText().toString().trim();
                    String state = spState.getSelectedItem().toString().trim();
                    boolean fl = mDatabase.InsertRetailerdata(pref.getString("UserID", null),
                            cordinate, address, spDist.getSelectedItem().toString().trim(),
                            taluka, marketplace, retailername,
                            retailerfirm, mobileno, age,
                            asscoidistributor, mahycoretailer,
                            Otherbussiness, expyear, comments, "Retailer", newfirm,
                            birthdate, WhatsappNo, state);

                    mDatabase.InsertMDO_NEWRetailer(state, "Retailer",
                            pref.getString("UserID", null), marketplace, spDist.getSelectedItem().toString().trim(),
                            taluka, mobileno, newfirm, retailername, "");


                    if (fl == true) {
                        try {

                            if (CommonUtil.addGTVActivity(context, "39", "Retailer Tag", cordinate, retailername+" "+newfirm+" "+mobileno,"Market","0",0.0)) {
                                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                            }

                            msclass.showMessage("data saved successfully");
                            txtmobile.setText("");
                            txtAge.setText("");
                            txtComments.setText("");
                            txtfirmname.setText("");
                            txtmarketplace.setText("");
                            txtyearofexperiance.setText("");
                            txtrtname.setText("");
                            radGrp2.clearCheck();
                            radGrp.clearCheck();
                            chkmahycoproduct.setChecked(false);
                            chkotherprd.setChecked(false);
                            txtBirthdate.setText("");
                            Intent intent;
                            String callactivity=pref.getString("RetailerCallActivity", "");
                            switch (callactivity)
                            {
                                case "PurchaseList":
                                    intent = new Intent(getActivity(), PurchaseListActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "VillageMeetingActivity":
                                    intent = new Intent(getActivity(), VillageMeetingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "POPDisplayActivity":
                                    intent = new Intent(getActivity(), POPDisplayActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "PromotionActivity":
                                    intent = new Intent(getActivity(), PromotionActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "RetailerVisitsActivity":
                                    intent = new Intent(getActivity(), RetailerVisitsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "LivePlantDisplayRetailCounterActivity":
                                    intent = new Intent(getActivity(), LivePlantDisplayRetailCounterActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;
                                case "DistributerVisitsActivity":
                                    intent = new Intent(getActivity(), DistributerVisitsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    editor.putString("RetailerCallActivity", "");
                                    editor.commit();
                                    startActivity(intent);
                                    getActivity().finish();
                                    break;


                            }



                            /*if (from.contains("RetailerPOG")) {
                                getActivity().onBackPressed();
                                //super.onBackPressed();
                                // super.onBackPressed();
                            }*/
                             //  This code to upload data directly to server if internet is available
                            if(new Config(context).NetworkConnection())
                               UploadDataRetailerData();
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

        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage("" + ex.getMessage());
            msclass.showMessage(ex.getMessage());
        }
    }



    private boolean validation() {
        try {
            boolean flag = true;
            // msclass.showMessage(spdistr.getSelectedItemsAsString());
            int count0 = mDatabase.getrowcount("select * from mdo_Retaileranddistributordata " +
                    "where mobileno='" + txtmobile.getText().toString() + "' and type='Retailer'  ");
            if (count0 > 0) {
                msclass.showMessage("this retailer  maping data already saved on this mobile number " + txtmobile.getText().toString() + "");
                return false;
            }

            if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                msclass.showMessage("Please select state");
                return false;
            }
            if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                msclass.showMessage("Please select district");
                return false;
            }

            if (pref.getString("RoleID", null) == null || pref.getString("RoleID", null).equals("") ||
                    Integer.valueOf(pref.getString("RoleID", "0")) == 0
                    || Integer.valueOf(pref.getString("RoleID", "0")) == 4) {
                if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                    msclass.showMessage("Please Select Taluka");
                    return false;
                }
            } else {
                if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                    msclass.showMessage("Please Select Taluka");
                    return false;
                }

            }
            if (txtmarketplace.getText().length() == 0) {
                msclass.showMessage("Please enter market place.");
                txtmarketplace.setError("Please enter market place");
                return false;
            }

            if (spnewretailer.getSelectedItem().toString().toLowerCase().equals("select retailer")) {
                msclass.showMessage("Please Select retailer");
                return false;
            }
            if (spnewretailer.getSelectedItem().toString().toLowerCase().equals("new retailer")
                 || spnewretailer.getSelectedItem().toString().toLowerCase().equals("other")
                    ) {
                if (txtfirmname.getText().length() == 0) {
                    msclass.showMessage("Please enter new retailer firm name.");
                    txtfirmname.setError("Please enter new retailer firm name.");
                    return false;
                }

            }

            //try
            //{

            // }
            // catch (Exception e)
            // {
            //     msclass.showMessage("Please enter  DD/MM/YYYY formate date ");
            //     return false;
            //}


            if (txtrtname.length() == 0) {
                msclass.showMessage("Please enter  retailer name");
                txtrtname.setError("Please enter  retailer name.");
                return false;
            }
            if (txtmobile.getText().length() == 0) {
                msclass.showMessage("Please enter mobile number.");
                txtmobile.setError("Please enter mobile number.");
                return false;

            }
            if (txtmobile.getText().length() < 10) {
                msclass.showMessage("Please enter 10 digit mobile number");
                return false;
            }
            String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
            Pattern pattern = Pattern.compile(regex);
            if (txtBirthdate.getText().length() > 0) {
                Matcher matcher = pattern.matcher(txtBirthdate.getText().toString());
                if (matcher.matches()) {

                } else {
                    msclass.showMessage("Please enter  DD/MM/YYYY format date.. ");
                    return false;
                }
                //Date javaDate = dateFormatter.parse(txtBirthdate.getText().toString());
            }
            if (chktag.isChecked() == false) {
                msclass.showMessage("please check Geo tag checkbox..");
                return false;

            }
            if (radGrp.getCheckedRadioButtonId() == -1) {
                msclass.showMessage("Please select is he key retailer of mahyco  (Yes or No). ");
                return false;
            }

            if (chkmahycoproduct.isChecked() == false) {
                msclass.showMessage("Please add mahyco product details..");
                return false;

            }
            if (chkotherprd.isChecked() == false) {
                msclass.showMessage("Please add other company product details..");
                return false;

            }

            int count2 = mDatabase.getrowcount("select * from mdo_retailerproductdetail " +
                    "where retailermobile='" + txtmobile.getText().toString() + "' and compname='MAHYCO'  ");
            if (count2 == 0) {
                msclass.showMessage("Please  add mahyco product details..");
                return false;

            }
            int count3 = mDatabase.getrowcount("select * from mdo_retailerproductdetail " +
                    "where retailermobile='" + txtmobile.getText().toString() + "' and compname <>'MAHYCO'  ");
            if (count3 == 0) {
                msclass.showMessage("Please add other company product details...");
                return false;

            }

            int count = 0;//mDatabase.getrowcount("select * from mdo_retailerproductdetail " +
            // "where retailermobile='"+txtmobile.getText().toString()+"'");
            if (count > 0) {
                msclass.showMessage("This retailer data already exist");
                return false;

            }

            if (radGrp2.getCheckedRadioButtonId() == -1) {
                msclass.showMessage("Please select is he authorised distributor for other companies (Yes or No). ");
                return false;
            }
            int btn = radGrp2.getCheckedRadioButtonId();
            int radioButtonID = radGrp2.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radGrp2.findViewById(radioButtonID);
            String selectaware = (String) radioButton.getText();
            if (selectaware.toLowerCase().contains("yes")) {

                int count4 = mDatabase.getrowcount("select * from mdo_authoriseddistributorproduct " +
                        "where retailermobile='" + txtmobile.getText().toString() + "'  ");
                if (count4 == 0) {
                    radGrp2.clearCheck();
                    msclass.showMessage("Please add other company product details(he is authorised distributor of other companies)...");
                    return false;

                }
            }
            // msclass.showMessage(spdistr.getSelectedItemsAsString());


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
        return true;
    }

    /*public void onLocationChanged(Location location) {
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
                // Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private boolean turnGPSOn() {

        boolean flag = false;
        //boolean flag=true;
        try {
            LocationManager locationManager = null;
            try {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                msclass.showMessage(ex.getMessage());
            }
            boolean isGPSEnabled2 = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled)// &&(!isGPSEnabled2))
            {

                flag = false;

            } else {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
        return flag;
    }

    public void BindDist(String state) {
        try {
            spDist.setAdapter(null);
            String str = null;
            if (state.equals("SELECT STATE")) {

                state = "SELECT STATE";


            } else {
                try {
                    List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                    String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                            " where state='" + state + "' order by district asc  ";
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


                    getDistrict = Preferences.getInt(context, "distposition");


                    int compareValue1 = getDistrict;
                    if (compareValue1 != 0) {
                        spDist.setSelection(compareValue1);
                    }

//                    if(getDistrict==0){
//                        Preferences.remove(context, "talukaposition");
//
//
//                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }


    public void BindTaluka(String dist) {
        try {
            spTaluka.setAdapter(null);
            dialog.setMessage("Loading....");
            String str = null;

            if (dist.equals("SELECT DISTRICT")) {

                dist = "SELECT DISTRICT";


            } else {

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
//                    int talukaposition = spTerritory.getSelectedItemPosition();
//                    Preferences.saveInt(context, "talukaposition", talukaposition);
//

                    //Comment mahendra
                  /*  getTaluka = Preferences.getInt(context, "talukaposition");

                    int compareValue2 = getTaluka;
                    if (compareValue2 != 0) {
                        spTaluka.setSelection(compareValue2);
                    }
                    int talukaposition = spTaluka.getSelectedItemPosition();
                    Preferences.saveInt(context, "talukaposition", talukaposition);
                    int distposition = spDist.getSelectedItemPosition();
                    Preferences.saveInt(context, "distposition", distposition);
                    int stateposition = spState.getSelectedItemPosition();
                    Preferences.saveInt(context, "stateposition", stateposition);
                    */
 //enc comment

                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();
                }
                dialog.dismiss();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void BindRetailer(String taluka) {
        try {
            spnewretailer.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();
                if (taluka == null) {
                    taluka = "";
                }
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Retailer'" +
                        " and  upper(taluka)='" + taluka.toUpperCase() + "' order by  RetailerName ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));
                Croplist.add(new GeneralMaster("OTHER",
                        "OTHER"));

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
                spnewretailer.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void binddistributor(String dist) {
        // spdistr.setAdapter(null);


        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;

            //searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
            //        "and  dist='" + dist.toUpperCase() + "' order by  RetailerName ";

            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
                    " order by  RetailerName ";

            // searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' and  taluka='" + spDist.getSelectedItem().toString().trim()+ "' order by  RetailerName ";

            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT DISTRIBUTOR";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("RetailerName").toString();
                }
                array[jArray.length() + 1] = "OTHER";
                if (array.length > 0) {
                    spdistr.setItems(array);
                    spdistr.hasNoneOption(true);
                    spdistr.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spTehsil.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


        dialog.dismiss();

    }
    void UploadDataRetailerData() {

        try{
            String searchQuery12 = "select  *  from  mdo_Retaileranddistributordata where Status='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if(count>0) {


                JsonArray jsonArray = new JsonArray();
                JsonArray jsonArrayFinal = new JsonArray();

                JsonObject jsonObjectChild = new JsonObject();

                String searchQuery = "";

                searchQuery = "select \n" +
                        "_id,\n" +
                        "mdocode,\n" +
                        "coordinate,\n" +
                        "startaddress,\n" +
                        "dist,\n" +
                        "taluka,\n" +
                        "marketplace,\n" +
                        "retailername as name,\n" +
                        "retailerfirm as retailerCategory,\n" +
                        "age,\n" +
                        "mobileno,\n" +
                        "asscoi_distributor,\n" +
                        "keyretailer,\n" +
                        "otherbussiness,\n" +
                        "experianceSeedwork,\n" +
                        "comments,\n" +
                        "Status,\n" +
                        "type,\n" +
                        "newfirm,\n" +
                        "birthdate,\n" +
                        "WhatsappNo,\n" +
                        "state from mdo_Retaileranddistributordata where Status='0'";
                //   jsonObjectChild.put("Table4", mDatabase.getResults(searchQuery));

                jsonArray = mDatabase.getResultsRetro(searchQuery);

                Log.i("Retailer Data:", "" + mDatabase.getResults(searchQuery).length());
                searchQuery = "select * from mdo_retailerproductdetail where Status='0'";
                //object.put("Table5", mDatabase.getResults(searchQuery));
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    if (i == 0) {
                        String searchQuery1 = "select * from mdo_retailerproductdetail where Status='0'";
                        jsonObject.add("RetailerProductDetailModels", mDatabase.getResultsRetro(searchQuery1));
                    } else {

                        jsonObject.add("RetailerProductDetailModels", new JsonArray());

                    }
                    jsonArrayFinal.add(jsonObject);
                }
                JsonObject jsonFinal = new JsonObject();
                jsonFinal.add("RetaileranddistributordataModels", jsonArrayFinal);
                UplaodRetailerAndDistributor(jsonFinal);
                Log.i("Retailer Json ", jsonFinal.toString());

            }else
            {
                Toast.makeText(context, "No Data for upload.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            Log.i("Ecxeption Json ",e.getMessage());

        }



    }

    public void UplaodRetailerAndDistributor(JsonObject jsonFinal) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().uploadRetailerAndDistributor(jsonFinal);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String result = response.body();
                        try {
                            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();

                            try{
                                JSONObject jsonObject=new JSONObject(result);
                                if(jsonObject.getBoolean("ResultFlag")) {
                                    if (jsonObject.getString("status").toLowerCase().contains("success")) {

                                        String qq1="update mdo_Retaileranddistributordata set Status='1' where Status='0'";
                                        String qq2="update mdo_retailerproductdetail set Status='1' where Status='0'";

                                        mDatabase.UpdateStatus(qq1);
                                        mDatabase.UpdateStatus(qq2);

                                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                                .setMessage(jsonObject.getString("Comment"))
                                                .setTitle(jsonObject.getString("status"))
                                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();

                                                        }
                                                })
                                                .show();
                                    } else {
                                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                                .setMessage(jsonObject.getString("Comment"))
                                                .setTitle(jsonObject.getString("status"))
                                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .show();
                                    }
                                }else
                                {

                                }

                            }catch (Exception e)
                            {
                                new androidx.appcompat.app.AlertDialog.Builder(context)
                                        .setMessage("Something went wrong.")
                                        .setTitle("Exception")
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
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