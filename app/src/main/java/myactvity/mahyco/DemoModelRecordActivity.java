package myactvity.mahyco;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.myActivityRecording.atlActivity.FieldBannerActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.FieldBoardActivity;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;


/**
 * Created by Akash Namdev on 2019-07-19.
 */


public class DemoModelRecordActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback,
        IPickResult, View.OnClickListener {
//
//        implements

    private static final String TAG = "DEMOMODELRECORD";
    SearchableSpinner spState, spDist, spTaluka, spCropType, spProductName, spVillage, spFocusedVillages, spCheckHybrids;
    Spinner spKgPacks, spRow, spPlan;
    Button btnSubmit, btnTakePhoto;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etVillage, etFarmerName, etMobileNumber, etWhatsappNumber, etArea, etSeedQty, etDate, etRemarks, etCheckHybrid;

    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageView imgBtnGps;
    private Context context;
    String action = "1";
    LocationManager locationManager;
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address = "";

    public String search = "";
    int imageselect;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    List<GeneralMaster> mList = new ArrayList<>();
    String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";

    String userCode, imagePath;
    String plotType = "";
    String soilType = "";
    String irrigationType = "";
    CardView crdSpace;
    Config config;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    TextView tvCordinates, lblheader, tvQty, tvAddress;
    //    ProgressDialog progressDailog;
    private String state, dist, croptype, taluka;
    RadioGroup radGroup, radGrpSoilType, radGrpIrrigationMode;
    RadioButton radDemo, radModel, radJumbo, radOther, radLight, radMedium, radHeavy, radIrrigated, radRainfed, radDrip;
    private long mLastClickTime = 0;
    public String cordinates = "";


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
    String focusedVillage;
    LinearLayout llOtherVillages, llFocussedVillages, llCheckHybrids;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity, radOtherActivity;


    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_demo_model_record_screen);
        //    getSupportActionBar().hide(); //<< this-
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        lblheader = (TextView) findViewById(R.id.lblheader);
        tvQty = (TextView) findViewById(R.id.tvQty);

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (SearchableSpinner) findViewById(R.id.spProductName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        spRow = (Spinner) findViewById(R.id.spRow);
        spPlan = (Spinner) findViewById(R.id.spPlan);
        // spKgPacks = (Spinner) findViewById(R.id.spKgPacks);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
//        etVillage = (EditText) findViewById(R.id.etVillage);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        spCheckHybrids = (SearchableSpinner) findViewById(R.id.spCheckHybrids);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etArea = (EditText) findViewById(R.id.etArea);
        etSeedQty = (EditText) findViewById(R.id.etSeedQty);
        etDate = (EditText) findViewById(R.id.etDate);
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        etCheckHybrid = (EditText) findViewById(R.id.etCheckHybrid);
        radModel = (RadioButton) findViewById(R.id.radModel);
        radJumbo = (RadioButton) findViewById(R.id.radJumbo);
        radOther = (RadioButton) findViewById(R.id.radOther);
        radDemo = (RadioButton) findViewById(R.id.radDemo);
        radLight = (RadioButton) findViewById(R.id.radLight);
        radMedium = (RadioButton) findViewById(R.id.radMedium);
        radHeavy = (RadioButton) findViewById(R.id.radHeavy);
        radIrrigated = (RadioButton) findViewById(R.id.radIrrigated);
        radRainfed = (RadioButton) findViewById(R.id.radRainfed);
        radDrip = (RadioButton) findViewById(R.id.radDrip);
        radGroup = (RadioGroup) findViewById(R.id.radGroup);
        radGrpSoilType = (RadioGroup) findViewById(R.id.radGrpSoilType);
        crdSpace = (CardView) findViewById(R.id.crdSpace);
        radGrpIrrigationMode = (RadioGroup) findViewById(R.id.radGrpIrrigationMode);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        // progressDailog = new ProgressDialog(this);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llCheckHybrids = (LinearLayout) findViewById(R.id.llCheckHybrids);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        //progressDailog.setIndeterminate(false);
        // progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // progressDailog.setMessage("Data Uploading");

        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);

        bindSpinnerData();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        bindState();
        bindFocussedVillage();
        //  bindCheckHybridMaster(); not used
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });


        if (checkPlayServices()) {
            // startFusedLocationService();
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }


        spFocusedVillages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    focusedVillage = gm.Desc().trim();
                    Log.d("string", focusedVillage);

                    if (focusedVillage.contains("OTHER")) {


                        //  llOtherVillages.setVisibility(View.VISIBLE);


                    } else {


                        llOtherVillages.setVisibility(View.GONE);
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                bindDist(state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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


                bindTaluka(dist);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(DemoModelRecordActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }


                bindProductName(spProductName, croptype);


                if (croptype.toLowerCase().trim().equals("cotton")) {
                    tvQty.setText("Pkts");

                    crdSpace.setVisibility(View.VISIBLE);
                } else {
                    crdSpace.setVisibility(View.GONE);
                    tvQty.setText("Kgs");
                }

                // bindSpinnerData();


                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spCheckHybrids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        imgBtnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cordinates != null && !cordinates.contains("null")) {
                    if (tvCordinates.getText().toString().contains("Yes")) {
                        imgBtnGps.setImageResource(R.drawable.ic_location_off);
                        cordinatesmsg = "GEO TAG : \n";
                    } else {
                        if (lati != 0) {
                            imgBtnGps.setImageResource(R.drawable.ic_location_on);
                            cordinatesmsg = "GEO TAG RECIEVED SUCCESSFULLY : \n";
                        } else {
                            startFusedLocationService();
                        }
                    }
                } else {
                    startFusedLocationService();
                }
            }
        });

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radDemo:

                        if (radDemo.isChecked()) {
                            plotType = "demo plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radModel.isChecked()) {
                            plotType = "model plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radJumbo.isChecked()) {
                            plotType = "jumbo plot";

                            llCheckHybrids.setVisibility(View.GONE);
                        } else {
                            plotType = "other field";
                            llCheckHybrids.setVisibility(View.VISIBLE);

                        }

                        radModel.setChecked(false);
                        radOther.setChecked(false);
                        radJumbo.setChecked(false);
                        break;
                    case R.id.radJumbo:
                        if (radJumbo.isChecked()) {
                            plotType = "jumbo plot";
                            llCheckHybrids.setVisibility(View.GONE);
                        } else if (radOther.isChecked()) {
                            plotType = "other field";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radModel.isChecked()) {
                            plotType = "model plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else {
                            plotType = "demo plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        }
                        radDemo.setChecked(false);
                        radModel.setChecked(false);
                        radOther.setChecked(false);
                        break;
                    case R.id.radModel:
                        if (radModel.isChecked()) {
                            plotType = "model plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radOther.isChecked()) {
                            plotType = "other field";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radJumbo.isChecked()) {
                            plotType = "jumbo plot";
                            llCheckHybrids.setVisibility(View.GONE);
                        } else {
                            plotType = "demo plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        }
                        radDemo.setChecked(false);
                        radJumbo.setChecked(false);
                        radOther.setChecked(false);
                        break;
                    case R.id.radOther:
                        if (radOther.isChecked()) {
                            plotType = "other field";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radModel.isChecked()) {
                            plotType = "model plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        } else if (radJumbo.isChecked()) {
                            plotType = "jumbo plot";
                            llCheckHybrids.setVisibility(View.GONE);
                        } else {
                            plotType = "demo plot";
                            llCheckHybrids.setVisibility(View.VISIBLE);
                        }
                        break;

                }
                Log.d("PlotTYpe", plotType);
            }
        });


        radGrpSoilType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radLight:

                        if (radLight.isChecked()) {
                            soilType = "light";
                        } else if (radHeavy.isChecked()) {
                            soilType = "heavy";
                        } else {
                            soilType = "medium";

                        }
                        radMedium.setChecked(false);
                        radHeavy.setChecked(false);
                        break;
                    case R.id.radMedium:
                        if (radMedium.isChecked()) {
                            soilType = "medium";
                        } else if (radHeavy.isChecked()) {
                            soilType = "heavy";
                        } else {
                            soilType = "light";

                        }
                        radLight.setChecked(false);
                        radHeavy.setChecked(false);
                        break;
                    case R.id.radHeavy:
                        if (radHeavy.isChecked()) {
                            soilType = "heavy";
                        } else if (radMedium.isChecked()) {
                            soilType = "medium";
                        } else {
                            soilType = "light";

                        }
                        break;

                }
                Log.d("SoilType", soilType);
            }
        });


        radGrpIrrigationMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radIrrigated:

                        if (radIrrigated.isChecked()) {
                            irrigationType = "irrigated";
                        } else if (radRainfed.isChecked()) {
                            irrigationType = "rainfed";
                        } else {
                            irrigationType = "drip";

                        }
                        radRainfed.setChecked(false);
                        radDrip.setChecked(false);
                        break;
                    case R.id.radRainfed:
                        if (radRainfed.isChecked()) {
                            irrigationType = "rainfed";
                        } else if (radDrip.isChecked()) {
                            irrigationType = "drip";
                        } else {
                            irrigationType = "irrigated";

                        }
                        radIrrigated.setChecked(false);
                        radDrip.setChecked(false);
                        break;
                    case R.id.radDrip:
                        if (radDrip.isChecked()) {
                            irrigationType = "drip";
                        } else if (radRainfed.isChecked()) {
                            irrigationType = "rainfed";
                        } else {
                            irrigationType = "irrigated";

                        }
                        break;

                }
                Log.d("IrrigationType", irrigationType);
            }
        });

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocusedActivity:

                        if (radFocusedActivity.isChecked()) {
                            // villageType = "focussed";
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                        } else {
                            // villageType = "other";
                            bindState();
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        }

                        radOtherActivity.setChecked(false);
                        break;
                    case R.id.radOtherActivity:
                        if (radOtherActivity.isChecked()) {
                            //   villageType = "other";
                            bindState();
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        } else {
                            //  villageType = "focussed";
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });

//submit save to db and go to next screen
        btnSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelRecordActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Are you sure to submit data");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

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

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });

        btnTakePhoto.setOnClickListener(this);
       /* btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    //  ActivityCompat.requestPermissions(context, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 1;
                // selectImage();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        selectImage();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            captureImage();
                        } else {
                            captureImage2();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        }); */

        bindcroptype(spCropType, "C");


    }

    private void selectImage() {
        try {
            if (Indentcreate.getPickImageIntent(this) != null) {
                photoFile = Indentcreate.fileobj;
                startActivityForResult(Indentcreate.getPickImageIntent(this), REQUEST_CAMERA);//100
            } else {
                Toast.makeText(this, "Picker intent not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.d(TAG, "selectImage(): " + ex.toString());
        }
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


    public void bindCheckHybridMaster() {
        spCheckHybrids.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct hybridName  FROM MDO_checkHybridMaster order by hybridName asc  ";
            Croplist.add(new GeneralMaster("SELECT CHECK HYBRIDS",
                    "SELECT CHECK HYBRIDS"));

            cursor = mDatabase.getReadableDatabase().
                    rawQuery(searchQuery, null);
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCheckHybrids.setAdapter(adapter);

        } catch (
                Exception ex) {
            ex.printStackTrace();

        }


    }

    public void bindFocussedVillage() {
        spFocusedVillages.setAdapter(null);


        String str = null;
        try {
            Prefs mPref = Prefs.with(context);
            String gtvtype = mPref.getString(AppConstant.GTVSELECTEDBUTTON, "");
            if (gtvtype.trim().equals("GTV")) {
                radOtherActivity.setVisibility(View.GONE);
                String vname = mPref.getString(AppConstant.GTVSelectedVillage, "");
                String vcode = mPref.getString(AppConstant.GTVSelectedVillageCode, "");
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Croplist.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                        "SELECT FOCUSED VILLAGE"));Croplist.add(new GeneralMaster(vcode, vname));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);
            } else {
                String searchQuery = "";
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Cursor cursor;
                searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
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


    private void bindSpinnerData() {

        try {


            spRow.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1 ft"));
            gm.add(new GeneralMaster("2", "2 ft"));
            gm.add(new GeneralMaster("3", "3 ft"));
            gm.add(new GeneralMaster("4", "4 ft"));
            gm.add(new GeneralMaster("5", "5 ft"));
            gm.add(new GeneralMaster("6", "6 ft"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRow.setAdapter(adapter);


            spPlan.setAdapter(null);
            List<GeneralMaster> gm1 = new ArrayList<GeneralMaster>();
            gm1.add(new GeneralMaster("0", "SELECT"));
            gm1.add(new GeneralMaster("1", "0.5 ft"));
            gm1.add(new GeneralMaster("2", "1 ft"));
            gm1.add(new GeneralMaster("3", "1.5 ft"));
            gm1.add(new GeneralMaster("4", "2 ft"));
            gm1.add(new GeneralMaster("5", "2.5 ft"));
            gm1.add(new GeneralMaster("6", "3 ft"));
            gm1.add(new GeneralMaster("7", "3.5 ft"));
            gm1.add(new GeneralMaster("8", "4 ft"));
            gm1.add(new GeneralMaster("9", "4.5 ft"));
            gm1.add(new GeneralMaster("10", "5 ft"));
            ArrayAdapter<GeneralMaster> adapter1 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPlan.setAdapter(adapter1);

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private boolean checkPlayServices() {
        boolean flag = false;
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(this,
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
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
            // dialog.dismiss();
        }
    }

    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

    @Override
    protected void onResume() {
        super.onResume();

        if (ivImage.getDrawable() != null) {

            ivImage.setVisibility(View.VISIBLE);


        } else {

            ivImage.setVisibility(View.GONE);

        }
        try {

            startFusedLocationService();

        } catch (Exception ex) {
            msclass.showMessage("Funtion name :onresume" + ex.getMessage());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

//        if (progressDailog != null) {
//            progressDailog.dismiss();
//        }


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

    //    Validation for all fields
    public boolean validation() {
//No Radio Button Is Checked
        if (radGroup.getCheckedRadioButtonId() == -1) {
            msclass.showMessage("Please Select PlotType");
            return false;
        }


        if (radFocusedActivity.isChecked() && spFocusedVillages.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Village", context);
            return false;
        }

        if (radOtherActivity.isChecked()) {
            if (spState.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select State");
                return false;
            }
            if (spDist.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select District");
                return false;
            }
            if (spTaluka.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }
            if (spVillage.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please  enter village name");
                return false;
            }
        }

        if (etFarmerName.getText().length() == 0) {
            msclass.showMessage("Please  enter farmer name");
            return false;
        }
        if (etMobileNumber.getText().length() != 10) {
            msclass.showMessage("Please  enter Valid Mobile Number");
            return false;
        }
//        if (etCheckHybrid.getText().length() == 0) {
//            msclass.showMessage("Please  enter Check Hybrids");
//            return false;
//        }
//

        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductName.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Product Name");
            return false;
        }


       /* if (!plotType.contains("jumbo plot") && spCheckHybrids.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Mahyco Check Hybrids", context);
            return false;
        }*/
        GeneralMaster gm = (GeneralMaster) spCropType.getSelectedItem();

        try {
            croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (croptype.contains("COTTON")) {

            if (spRow.getSelectedItemPosition() == 0) {
                Utility.showAlertDialog("Info", "Please Select Row to Row Spacing", context);
                return false;
            }
            if (spPlan.getSelectedItemPosition() == 0) {
                Utility.showAlertDialog("Info", "Please Select Plant to Plant Spacing", context);
                return false;
            }

        }

        if (etArea.getText().length() == 0) {
            msclass.showMessage("Please  enter  Area.");
            return false;
        }


        if (etSeedQty.getText().length() == 0) {
            msclass.showMessage("Please  enter  seed quantity.");
            return false;
        }

//        if (spKgPacks.getSelectedItemPosition() == 0) {
//            msclass.showMessage("Please Select Quantity");
//            return false;
//        }
        if (etDate.getText().length() == 0) {
            msclass.showMessage("Please  enter  date.");
            return false;
        }


        if (radGrpSoilType.getCheckedRadioButtonId() == -1) {
            msclass.showMessage("Please Select SoilType");
            return false;
        }
        if (radGrpIrrigationMode.getCheckedRadioButtonId() == -1) {
            msclass.showMessage("Please Select Mode of Irrigation");
            return false;
        }
        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please click plot image");
            return false;
        }
        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {

            msclass.showMessage("Please Tag The Field.");
            return false;
        }
        if (cordinates.trim().equals("")) {
            msclass.showMessage("Please Wait for location.");
            return false;
        }

        if (!isAlreadydone()) {
            msclass.showMessage("This crop,product and mobile number already exists");
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


    //Save to Batch Code Database
    public void saveToDb() {

        try {
            String uId = uniqueId();
            String mobileNumber = etMobileNumber.getText().toString();
            String chkHybrids = spCheckHybrids.getSelectedItem().toString();//etCheckHybrid.getText().toString();
            String remrk = etRemarks.getText().toString();
            GeneralMaster fvc = (GeneralMaster) spFocusedVillages.getSelectedItem();
            GeneralMaster vc = (GeneralMaster) spVillage.getSelectedItem();


            String whatsappNumber = "";
            if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

                whatsappNumber = "";

            } else {


                whatsappNumber = etWhatsappNumber.getText().toString();
            }


            String checkHybrids = chkHybrids != null ? chkHybrids : "";
            String remarks = remrk != null ? remrk : "";

            String farmerName = etFarmerName.getText().toString();
            //  String village = etVillage.getText().toString();


            String focussedVillage = spFocusedVillages.getSelectedItem().toString();

            String state = "";
            String district = "";
            String taluka = "";
            String othervillage = "";
            String vil_code = "";

            if (radFocusedActivity.isChecked()) {
                state = "";
                district = "";
                taluka = "";
                othervillage = "";
                vil_code = fvc.Code();
                // Get Taluka Name
            } else {

                state = spState.getSelectedItem().toString();
                district = spDist.getSelectedItem().toString();
                taluka = spTaluka.getSelectedItem().toString();
                othervillage = spVillage.getSelectedItem().toString();
                vil_code = vc.Code();

            }

            String spacingRow = spRow.getSelectedItem().toString();
            String spacingPlan = spPlan.getSelectedItem().toString();
            String sowingDate = etDate.getText().toString();
            //   String checkHybridsSelected = spCheckHybrids.getSelectedItem().toString();
            String checkHybridsSelected = "sdhgs";
            String area = etArea.getText().toString();
            String cropType = spCropType.getSelectedItem().toString();
            String product = spProductName.getSelectedItem().toString();
            String seedQuantity = etSeedQty.getText().toString();
//        String unit = spKgPacks.getSelectedItem().toString();
            String unit = tvQty.getText().toString();

            if (String.valueOf(lati).trim().equals(""))

                //  cordinates = String.valueOf(lati) + "-" + String.valueOf(longi) + "-" + getCompleteAddressString(lati, longi);
                cordinates = String.valueOf(lati) + "-" + String.valueOf(longi);
            Log.d("LocationDatasaveToDb", cordinates);

            String isSynced = "0";
            String imgStatus = "0";


            Date entrydate = new Date();
            final String tempImagePath;
            final String imageName = AppConstant.Imagename;// "DemoModel" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
            tempImagePath = imagePath;
            boolean fl = mDatabase.insertDemoModelData(uId, userCode, plotType, state,
                    district, taluka, othervillage, farmerName, mobileNumber, whatsappNumber,
                    cropType, product, area, seedQuantity, unit, sowingDate, cordinates,
                    soilType, irrigationType, spacingRow, spacingPlan,
                    imageName, tempImagePath, imgStatus, isSynced, checkHybrids, remarks,
                    checkHybridsSelected, focussedVillage, vil_code);

            if (fl) {

                if (CommonUtil.addGTVActivity(context, "8", "Plot Register", cordinates, plotType + "-" + farmerName + " " + product + " " + mobileNumber, "GTV","0")) {
                    // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                }


                Intent intent;
                String callactivity = pref.getString("calldemoplot", "");
                Log.i("callactivity", "->" + callactivity);

                Toast.makeText(context, "callactivity:" + callactivity, Toast.LENGTH_SHORT).show();

                switch (callactivity) {
                    case "FieldBoard":
                        intent = new Intent(DemoModelRecordActivity.this, FieldBoardActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        editor.putString("calldemoplot", "");
                        editor.commit();
                        startActivity(intent);
                        this.finish();
                        break;
                    case "FieldBanner":
                        intent = new Intent(DemoModelRecordActivity.this, FieldBannerActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                        this.finish();
                        break;
                    case "FieldHarvest":
                        intent = new Intent(DemoModelRecordActivity.this, HarvestDayActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        editor.putString("calldemoplot", "");
                        editor.commit();
                        startActivity(intent);
                        this.finish();
                        break;
                    case "CropShow":
                        intent = new Intent(DemoModelRecordActivity.this, CropShowActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        editor.putString("calldemoplot", "");
                        editor.commit();
                        startActivity(intent);
                        this.finish();
                        break;
                    case "FieldDay":
                        intent = new Intent(DemoModelRecordActivity.this, FieldDayActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        editor.putString("calldemoplot", "");
                        editor.commit();
                        startActivity(intent);
                        this.finish();
                        break;

                    default:
                        saveToReviewDb();
                        break;

                }


            } else {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void saveToReviewDb() {


        String uIdP = uniqueId();
        String mobileNumber = etMobileNumber.getText().toString();
        String farmerName = etFarmerName.getText().toString();
        String sowingDate = etDate.getText().toString();
        String area = etArea.getText().toString();
        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();


        String focussedVillage = spFocusedVillages.getSelectedItem().toString();


        String taluka = "";

        if (radFocusedActivity.isChecked()) {
            taluka = "";


        } else {

            taluka = spTaluka.getSelectedItem().toString();


        }


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);


        Date entrydate = new Date();
        final String tempImagePath;
        final String imageName = "DemoReview" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        tempImagePath = imagePath;


        long epoch = entrydate.getTime();
        System.out.println(epoch);

        String uId = uniqueId() + String.valueOf(epoch);

        String imgStatus = "0";
        String isSynced = "0";
        String addressREceived = address != null ? address : "";
        cordinates = "" + String.valueOf(lati) + "-" + String.valueOf(longi) + "-" + addressREceived;
        Log.d("LocationDat", cordinates);
        boolean fl = mDatabase.insertDemoModelReview(uId, uIdP, userCode, taluka,
                farmerName, mobileNumber, cropType, product, area, sowingDate, formattedDate,
                cordinates, "", "", imageName, tempImagePath, imgStatus,
                isSynced, focussedVillage, "", "", "", "",
                "", "");
        if (fl) {
            // Toast.makeText(this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            if (CommonUtil.addGTVActivity(context, "8", "Plot Review", cordinates, farmerName + " " + mobileNumber + " " + product, "GTV","0")) {
                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }

            // uploadDataModelRecords("mdo_demoModelVisitDetail");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            finish();
            Intent intent = new Intent(DemoModelRecordActivity.this, DemoModelListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

    public String uniqueId() {

        String desiredmobno = etMobileNumber.getText().toString();
        String croptype = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        String desiredproduct = "";

        String desiredcroptype = croptype.trim();


        //  product = product.replaceAll("([^a-zA-Z]|\\s)+", "");
        product = product.replaceAll(" ", "");
        desiredproduct = product.trim();


        String uid = desiredmobno + desiredcroptype + desiredproduct + plotType;

        Log.d("UID", uid);
        return uid;
    }


    //Get date from calender object and date picker
    private void setDateTimeField(View v) {
        final EditText txt = (EditText) v;
        Calendar newCalendar = dateSelected;


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
            }


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();
        // txt.setText(dateFormatter.format(dateSelected.getTime()));


    }

    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    public void bindDist(String state) {
        try {
            spDist.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                // dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // dialog.dismiss();
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    public void bindTaluka(String dist) {
        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            // dialog.show();
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                // dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    private void bindcroptype(Spinner spCropType, String Croptype) {
        try {
            //st
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT CROP",
                    "SELECT CROP"));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

    }


    private boolean isAlreadydone() {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExists(uniqueId());

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


    }


    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));


            cursor = mDatabase.getReadableDatabase().

                    rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

            Croplist.add(new

                    GeneralMaster("OTHER",
                    "OTHER"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }


    private void bindProductName(Spinner spProductName, String croptype) {
        //st
        try {
            spCheckHybrids.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProductName.setAdapter(adapter);
            spCheckHybrids.setAdapter(adapter);

        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void onCaptureImageResult(Intent data) {


        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
            Date entrydate = new Date();
            String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            if (imageselect == 1) {

                try {
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = "Demo" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    imagePath = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakePhoto:
                imageselect = 1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
           /* case R.id.btnSuccessStoryPhoto:
                imageselect=2;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnPhoto3:
                imageselect=3;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;*/

        }
    }

    @Override
    public void onPickResult(PickResult r) {

        if (r.getError() == null) {


            if (imageselect == 1) {
                ivImage.setImageBitmap(r.getBitmap());
                if (ivImage.getDrawable() != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
          /*  if (imageselect == 2)
            {
                ivImage2.setImageBitmap(r.getBitmap());
                if (ivImage2.getDrawable() != null) {
                    ivImage2.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage2.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            if (imageselect == 3)
            {
                ivImagePhoto3.setImageBitmap(r.getBitmap());
                if (ivImagePhoto3.getDrawable() != null) {
                    ivImagePhoto3.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImagePhoto3.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }*/

            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));

            if (imageselect == 1) {
                AppConstant.Imagename = "Crop" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);

                imagePath = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
           /* if (imageselect == 2) {
                AppConstant.Imagename = "Cropfrmlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename2);
                Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 3) {
                AppConstant.Imagename = "crpfrmrelist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename3);
                Imagepath3 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }*/

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
   /* @Override
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
*/

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
            Log.d(TAG, "onlocation" + cordinates);
            tvCordinates.setText(cordinatesmsg + "\n" + address);


        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {

            if (config.NetworkConnection()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

                if (addresses != null)
                    address = "ADDRESS : " + addresses.get(0).getAddressLine(0);
                if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                    tvCordinates.setText(cordinatesmsg + "\n" +
                            address);
                    tvAddress.setText(address);

                } else {

                    tvCordinates.setText(cordinatesmsg + "\n" +
                            address);
                    tvAddress.setText(address);

                }
            }
            {
                tvAddress.setText(cordinates);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
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


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }


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
                //  mLocationRequest = new LocationRequest();

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
                AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelRecordActivity.this);

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
            //     Log.d(TAG, "startFusedLocationService: ");
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public int getLocationMode() {
        int val = 0;
        try {
            val = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            Log.d(TAG, "getLocationMode status: " + val);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return val;

    }

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


//AsyncTask Class for api batch code upload call

    public void uploadDataModelRecords(String UploadBatchCodeData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadDataServer(UploadBatchCodeData, context).execute(SERVER).get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelRecordActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finish();
                    Intent intent = new Intent(DemoModelRecordActivity.this, DemoModelListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });


            AlertDialog alert = builder.create();
            alert.show();

        }


    }


    public String uploadDataModel(String UploadBatchCodeData) {
        String str = "";
        // int action = 1; // Old
        int action = 3; // New added Entry date


        String searchQuery = "select  *  from DemoModelData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("imgName");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");


                        str = syncSingleImage(UploadBatchCodeData, SERVER, action, objAsBytes, uId, imageName, imagePath);

                        handleImageSyncResponse(UploadBatchCodeData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }


        return str;
    }
//AsyncTask Class for api batch code upload call

    private class UploadDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadDataModel("mdo_demoModelVisitDetail");

        }

        protected void onPostExecute(String result) {
            try {

                String resultout = result.trim();

                if (!result.equals("")) {

                    if (resultout.contains("True")) {

//                        progressDailog.dismiss();

                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
                        Intent intent = new Intent(DemoModelRecordActivity.this, DemoModelListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    } else {

                    }
                    Log.d("Response", resultout);

                } else {
//                    progressDailog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelRecordActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Poor Internet: Please try after sometime.\nData Saved Successfully");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            finish();
                            Intent intent = new Intent(DemoModelRecordActivity.this, DemoModelListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    });


                    AlertDialog alert = builder.create();
                    alert.show();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }


    public synchronized String syncSingleImage(String function, String urls, int action, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleImageSyncResponse(String function, String resultout, String id) {
        if (function.equals("mdo_demoModelVisitDetail")) {
            if (resultout.contains("True")) {
                mDatabase.updateDemoModelData(id, "1", "1");


            } else {

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }


}
