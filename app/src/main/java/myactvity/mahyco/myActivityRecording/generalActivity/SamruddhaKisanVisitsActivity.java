package myactvity.mahyco.myActivityRecording.generalActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import myactvity.mahyco.R;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;


/**
 * Created by Akash Namdev on 2019-07-19.
 */


public class SamruddhaKisanVisitsActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {

//        implements
    private static final String TAG = "SamruddhaKisanVisits";
    SearchableSpinner spState, spDist, spTaluka, spCropType, spVillage, spFocusedVillages;
    MultiSelectionSpinner spProductName;
    Spinner spRow, spPlan;
    Button btnSubmit;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    public Messageclass msclass;
    EditText etFarmerName, etMobileNumber, etWhatsappNumber, etArea, etAadharNumber, etEmailID;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageView imgBtnGps;
    TextView lblheader, tvCordinates, tvAddress;
    private Context context;
    String action = "1";
    LocationManager locationManager;
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address;
    public String search = "";
    int imageselect;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    List<GeneralMaster> mList = new ArrayList<>();
   // String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
   String SERVER = "https://packhouse.mahyco.com/api/generalactivity/samruddhakisanvisit";

    String userCode;
    String plotType = "";
    CardView crdSpace;
    TextView tvQty;
    private String state, dist, croptype, taluka;
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
    LinearLayout llOtherVillages;
    LinearLayout llCheckHybrids;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity,radOtherActivity;
    LinearLayout llFocussedVillages;

    private Handler handler = new Handler();
    Prefs mPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samruddha_kisan_visits);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         initUI();

    }
    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        mPref = Prefs.with(this);
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
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        spRow = (Spinner) findViewById(R.id.spRow);
        spPlan = (Spinner) findViewById(R.id.spPlan);
        // spKgPacks = (Spinner) findViewById(R.id.spKgPacks);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etArea = (EditText) findViewById(R.id.etArea);
        etAadharNumber = (EditText) findViewById(R.id.etAadharNumber);
        etEmailID = (EditText) findViewById(R.id.etEmailID);
        crdSpace = (CardView) findViewById(R.id.crdSpace);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llCheckHybrids = (LinearLayout) findViewById(R.id.llCheckHybrids);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode =  pref.getString("UserID", null);
        msclass = new Messageclass(this);
       // bindSpinnerData();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        bindState();

        bindFocussedVillage();

        onSubmitBtnClicked();

        bindcroptype(spCropType, "C");

        if (checkPlayServices()) {
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
                       // llOtherVillages.setVisibility(View.VISIBLE);
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
                    Toast.makeText(SamruddhaKisanVisitsActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProductName, croptype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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


        spProductName.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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

    }

    /**
     * <p>Method is used to manage geo tag</p>
     */
    private void manageGeoTag() {
        if (cordinates != null && !cordinates.contains("null")) {
            if (tvCordinates.getText().toString().contains("Yes")) {
                imgBtnGps.setImageResource(R.drawable.ic_location_off);
                cordinatesmsg = "GEO TAG : \n";
            } else {
                if (lati != 0) {
                    imgBtnGps.setImageResource(R.drawable.ic_location_on);
                    cordinatesmsg = "GEO TAG RECEIVED SUCCESSFULLY : \n";
                } else {
                    startFusedLocationService();
                }
            }
        } else {
            startFusedLocationService();
        }
    }

    /**
     * <P>//Method is used to do API related work on submit button clicked</P>
     */
    private void onSubmitBtnClicked() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SamruddhaKisanVisitsActivity.this);

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


    public void bindFocussedVillage() {
        spFocusedVillages.setAdapter(null);


        String str = null;
        try {


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
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFocusedVillages.setAdapter(adapter);

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


    /**
     * <p>Validation for all fields</p>
     * @return
     */
    public boolean validation() {

        //No Radio Button Is Checked
        if (radFocusedActivity.isChecked() && spFocusedVillages.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Village", context);
            return false;
        }
        if (radOtherActivity.isChecked()) {
                if (spState.getSelectedItemPosition() == 0) {
                    Utility.showAlertDialog("Info", "Please Select State", context);
                    return false;
                }
                if (spDist.getSelectedItemPosition() == 0) {
                    Utility.showAlertDialog("Info", "Please Select District", context);
                    return false;
                }
                if (spTaluka.getSelectedItemPosition() == 0) {
                    Utility.showAlertDialog("Info", "Please Select Taluka", context);
                    return false;
                }
                if (spVillage.getSelectedItemPosition() == 0) {
                    Utility.showAlertDialog("Info", "Please Select Village", context);
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

        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
                return false;
        }
        if (etArea.getText().length() == 0) {
            msclass.showMessage("Please  enter  Area.");
            return false;
        }

        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {

            msclass.showMessage("Please Tag The Field.");
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
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


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
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            getArrayList(searchQuery);

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
           // spProductName.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     * @param searchQuery
     */
    private void getArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT PRODUCT";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("ProductName").toString();
            }

            if (array.length > 0) {
                spProductName.setItems(array);
                spProductName.hasNoneOption(true);
                spProductName.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
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
            if(address.equals("")) {
                if (config.NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            Log.d(TAG, "onlocation" + cordinates);
            tvCordinates.setText(cordinatesmsg + "\n" + address);
            tvAddress.setText(address + "\n" + cordinates);
            manageGeoTag();
        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null)
                address = addresses.get(0).getAddressLine(0);
            if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                tvCordinates.setText(cordinatesmsg + "\n" + address);
                tvAddress.setText(address +"\n"+ cordinates);
            } else {

                tvCordinates.setText(cordinatesmsg + "\n" + address);
                tvAddress.setText(address +"\n"+ cordinates);
            }
            tvAddress.setText(address +"\n"+ cordinates);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SamruddhaKisanVisitsActivity.this);

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

    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {

        String state = "", district = "", taluka = "", village = "", focussedVillage="";
        String aadharNumber = "", emailID = "";
        String farmerName = "", mobileNumber = "", whatsappNumber = "", totalLand = "", crop = "", product = "";
        String taggedAddress = "";
        String taggedCordinates = "";


        if (radFocusedActivity.isChecked() && spFocusedVillages.getSelectedItemPosition() != 0) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();

        }else {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            village = spVillage.getSelectedItem().toString();
        }


        crop = spCropType.getSelectedItem().toString();
        product = spProductName.getSelectedItem().toString();
        farmerName = etFarmerName.getText().toString();
        mobileNumber = etMobileNumber.getText().toString();
        whatsappNumber = etWhatsappNumber.getText().toString();
        totalLand = etArea.getText().toString();
        aadharNumber = etAadharNumber.getText().toString();
        emailID = etEmailID.getText().toString();

        if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {
            taggedAddress = "";
        } else {
            taggedAddress = tvAddress.getText().toString();
        }

        if (!cordinates.isEmpty()) {
            taggedCordinates = cordinates;
        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
        }
        Log.d("LocationDatasaveToDb", cordinates);
        String isSynced = "0";
        boolean fl = mDatabase.insertSamruddhaKisanVisitsData( userCode, focussedVillage, state, district, taluka, village, crop, product, farmerName, mobileNumber,
                whatsappNumber, totalLand, aadharNumber, emailID, taggedCordinates +" "+ taggedAddress, taggedCordinates, isSynced);

        if (fl) {
            uploadSamruddhaKisanVisitsData("SamruddhaKisanVisitsData");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
    ///////

    //////
    public void uploadSamruddhaKisanVisitsData(String functionName) {
        if (config.NetworkConnection()) {

            String str = null;
            String searchQuery = "select  *  from SamruddhaKisanVisitsData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table", jsonArray);
                        Log.d("rhtt", "uploadSamruddhaKisanVisitsData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadSamruddhaKisanVisitsData(functionName, jsonObject).execute(SERVER).get();

                    cursor.close();


                } catch (Exception ex) {
                    ex.printStackTrace();

                }


            } else {
                //  dialog.dismiss();
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else {
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Utility.showAlertDialog("Info", "Please check your internet connection", context);
            etFarmerName.setText("");


        }


        // dialog.dismiss();
    }

    /**
     * <p>//AsyncTask Class for api batch code upload call</p>
     */
    private class UploadSamruddhaKisanVisitsData extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        JSONObject obj;
        String Funname;


        public UploadSamruddhaKisanVisitsData(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;
        }

        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return HttpUtils.POSTJSON(SERVER, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
        }

       protected void onPostExecute(String result) {


            try {
            String resultout = result.trim();
            Log.d("Response", resultout);

            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SamruddhaKisanVisitsActivity.this);
                    builder.setTitle("MyActivity");
                    builder.setMessage("Data uploaded Successfully");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Config.refreshActivity(SamruddhaKisanVisitsActivity.this);
                           dialog.dismiss();
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                   // msclass.showMessage("Data Uploaded Successfully");
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SamruddhaKisanVisitsActivity.this);
                    builder.setTitle("MyActivity");
                    builder.setMessage("Poor Internet: Please try after sometime.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            }else {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SamruddhaKisanVisitsActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Something went wrong please try again later.");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();
            }

            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
}
