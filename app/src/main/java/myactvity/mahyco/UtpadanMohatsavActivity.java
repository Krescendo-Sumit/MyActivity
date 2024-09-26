package myactvity.mahyco;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.BuildConfig;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.AddFarmerListHarvestDayAdapter;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.HarvestDayModel;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.newupload.ShellingDayAndUtpadanMohatsavAPI;


/**
 * Created by Akash Namdev on 2019-07-19.
 */


public class UtpadanMohatsavActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback,
        IPickResult, View.OnClickListener, ShellingDayAndUtpadanMohatsavAPI.ShellingDayListener {

    private static final String TAG = "DEMOMODELRECORD";
    SearchableSpinner spState, spDist, spTaluka, spCropType, spVillage, spFocusedVillages;

    MultiSelectionSpinner spProductName;

    Button btnSubmit, btnActivityPhoto;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    ;

    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageView imgBtnGps;
    private Context context;
    String action = "1";
    LocationManager locationManager;
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address="";
    public String search = "";
    int imageselect;
    File photoFile = null;
    File photoFile2 = null;
    public String Imagepath1 = "";
    public String Imagepath2 = "";
    public String Imagepath3 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;

    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    List<GeneralMaster> mList = new ArrayList<>();
    //   String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    String SERVER = "https://maapackhousenxg.mahyco.com/api/postSeason/harvestDayData";
    String userCode, imagePath, imagePath2;
    String plotType = "";
    String soilType = "";
    String irrigationType = "";
    CardView crdSpace;
    Config config;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    TextView tvCordinates, lblheader, tvQty, tvAddress;
    //    ProgressDialog progressDailog;
    private String state, dist, croptype, taluka, talukaRetailer, farmerDetails, retailerDetails;
    private long mLastClickTime = 0;
    public String cordinates = "";
    LinearLayout llOtherVillages, llFocussedVillages;
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
    String focusedVillage, village;
    ProgressBar progressBar;
    RelativeLayout relPRogress;

    ScrollView container;
    private Handler handler = new Handler();
    Dialog dialog;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
    Button btnAddDialog;
    Button btnSaveClose;
    String villageType = "focussed";
    ArrayList<HarvestDayModel> farmerListHarvestDay;
    String pkRetailerMobileNumber;
    int farmerNumber = 0;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity;
    RadioButton radOtherActivity;
    int farmerCount = 0;
    Prefs mPref;
    EditText et_farmername, et_farmerarea, et_farmeryeild, et_farmermobile, et_farmerfelisited, et_numberoffarmer, et_numberofretailer;
    String str_et_farmername, str_et_farmerarea, str_et_farmeryeild, str_et_farmermobile, str_et_farmerfelisited, str_et_numberoffarmer, str_et_numberofretailer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_utpadan_mohatsav);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        context = this;
        mPref = Prefs.with(this);
        mDatabase = SqliteDatabase.getInstance(this);
        
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        lblheader = (TextView) findViewById(R.id.lblheader);
        tvQty = (TextView) findViewById(R.id.tvQty);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        et_farmername = findViewById(R.id.et_farmername);
        et_farmerarea = findViewById(R.id.et_farmerarea);
        et_farmeryeild = findViewById(R.id.et_farmeryeild);
        et_farmermobile = findViewById(R.id.et_farmermobile);
        et_farmerfelisited = findViewById(R.id.et_farmerfelisited);
        et_numberoffarmer = findViewById(R.id.et_numberoffarmer);
        et_numberofretailer = findViewById(R.id.et_numberofretailer);

        btnActivityPhoto = (Button) findViewById(R.id.btnActivityPhoto);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);

        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_participant_list_harvest_day);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        farmerListHarvestDay = new ArrayList<>();
        bindState();
        bindFocussedVillages();

        onSubmitBtnClicked();

        if (checkPlayServices()) {
            // startFusedLocationService();
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocusedActivity:

                        if (radFocusedActivity.isChecked()) {
                            // villageType = "focussed";
                            bindFocussedVillage(spVillage);
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
                            bindFocussedVillage(spVillage);
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                        }
                        break;
                }
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

        spFocusedVillages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    focusedVillage = gm.Desc().trim();
                    Log.d("string", focusedVillage);
                    village = focusedVillage;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    village = gm.Code().trim(); // URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    village = gm.Desc().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                    Toast.makeText(UtpadanMohatsavActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }


                bindProductName(spProductName, croptype);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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

        btnActivityPhoto.setOnClickListener(this);


        bindcroptype(spCropType, "C");

        mDatabase.addshellinday();

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(UtpadanMohatsavActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Are you sure to submit data");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                            relPRogress.setVisibility(View.VISIBLE);
//                            relPRogress.setOnTouchListener(new View.OnTouchListener() {
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//
//                                    return true;
//                                }
//                            });

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
      //  progressBar.setIndeterminate(true);
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


    public void bindFocussedVillages() {
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

                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(context,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                            }
                        }
                        if (imageselect == 2) {
                            photoFile2 = createImageFile();

                            if (photoFile2 != null) {
                                Uri photoURI = FileProvider.getUriForFile(context,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile2);
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

    /**
     * <P> //Validation for all fields</P>
     *
     * @return
     */
    public boolean validation() {
        //No Radio Button Is Checked


        String focussedVillage = "";

        String state = "";
        String district = "";
        String taluka = "";
        String othervillage = "";
        String villagecode = "";
        String finalvillage = "";
        if (radFocusedActivity.isChecked()) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();
            villagecode = config.getvalue(spFocusedVillages);
            finalvillage = focussedVillage;
        } else {
            focussedVillage = "";

        }

        if (radOtherActivity.isChecked()) {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            othervillage = spVillage.getSelectedItem().toString();
            villagecode = config.getvalue(spVillage);
            finalvillage = othervillage;
        } else {
            state = "";
            district = "";
            taluka = "";
            othervillage = "";

        }


        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        str_et_farmername = et_farmername.getText().toString().trim();
        str_et_farmerarea = et_farmerarea.getText().toString().trim();
        str_et_farmeryeild = et_farmeryeild.getText().toString().trim();
        str_et_farmermobile = et_farmermobile.getText().toString().trim();
        str_et_farmerfelisited = et_farmerfelisited.getText().toString().trim();
        str_et_numberoffarmer = et_numberoffarmer.getText().toString().trim();
        str_et_numberofretailer = et_numberofretailer.getText().toString().trim();
        int nooffarmer=0;
        int falicit=0;
        try{
            if(!str_et_numberoffarmer.equals(""))
            nooffarmer=Integer.parseInt(str_et_numberoffarmer.trim());
            else
                nooffarmer=0;
            if(!str_et_farmerfelisited.equals(""))
                falicit=Integer.parseInt(str_et_farmerfelisited.trim());
            else
                falicit=0;

        }catch (NumberFormatException e)
        {

        }
        String UserCode = userCode;
        String State = state;
        String District = district;
        String Taluka = taluka;
        String VillageCode = villagecode;

        String VillageName = finalvillage;

        String Crop = cropType;

        String Product = product;

        String HostFarmerName = str_et_farmername;
        String FarmerMobile = str_et_farmermobile;
        String FarmerArea = str_et_farmerarea;
        String FarmerYeild = str_et_farmeryeild;
        String NumberOfFarmerFelisited = str_et_farmerfelisited;
        String NumberOfFarmer = str_et_numberoffarmer;
        String NumberOfRetailer = str_et_numberofretailer;
        String LatLong = cordinates;
        String PhotoName = userCode + "_UtpadanMohatsav_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        String PhotoString = Imagepath1;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String CreatedDate = "" + formattedDate;
        String Status = "0";
        String VersionName = BuildConfig.VERSION_NAME;
        String UplaodStatus = "0";
        ;
        String Extra1 = "";
        String Extra2 = "";

        if (UserCode.equals("")) {
            showAlert("User Code Missing");
            return false;
        }  else if (VillageCode.equals("")||VillageCode.toLowerCase().contains("select")) {
            showAlert("Village Code Missing");
            return false;
        } else if (VillageName.equals("")||VillageName.toLowerCase().contains("select")) {
            showAlert("Village Name Missing");
            return false;
        } else if (Crop.equals("")||Crop.toLowerCase().contains("select")) {
            showAlert("Crop Missing");
            return false;
        } else if (Product.equals("")||Product.toLowerCase().contains("select")) {
            showAlert("Product Missing");
            return false;
        } else if (HostFarmerName.equals("")) {
            showAlert("Farmer Name Missing");
            return false;
        } else if (FarmerMobile.equals("")) {
            showAlert("Farmer Mobile Missing");
            return false;
        }
        else if (FarmerMobile.length()!=10) {
            showAlert("Farmer Mobile Invalid");
            return false;
        }
        else if (FarmerArea.equals("")) {
            showAlert("Farmer Area Missing");
            return false;
        } else if (FarmerYeild.equals("")) {
            showAlert("Farmer Yeild Missing");
            return false;
        } else if (NumberOfFarmerFelisited.equals("")) {
            showAlert("Number of Felisited Farmer Missing");
            return false;
        } else if (NumberOfFarmer.equals("")) {
            showAlert("Number of Farmer Missing");
            return false;
        } else if (NumberOfRetailer.equals("")) {
            showAlert("Number of Retailer Missing");
            return false;
        } else if (LatLong.equals("")) {
            showAlert("Location not Found");
            return false;
        } else if (PhotoName.equals("")) {
            showAlert("Photo Name Missing");
            return false;
        } else if (PhotoString.equals("")) {
            showAlert("Photopath  Missing");
            return false;
        } else if (CreatedDate.equals("")) {
            showAlert("Created Date Missing");
            return false;
        } else if (nooffarmer<falicit) {
            showAlert("Number of farmer should not be less than number of felicitate farmer.");
            return false;
        }else if (VersionName.equals("")) {
            showAlert("Version name Missing");
            return false;
        }else if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please Add Activity Photo");
            return false;
        }else  if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
            msclass.showMessage("Please Tag The Field.");
            return false;
        }




        return true;
    }


    public boolean validationFarmerDetail() {
        //No Radio Button Is Checked
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
        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
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

    public String uniqueId() {

        //String desiredmobno = etMobileNumber.getText().toString();
        String croptype = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        String desiredproduct = "";

        String desiredcroptype = croptype.trim();


        //  product = product.replaceAll("([^a-zA-Z]|\\s)+", "");
        product = product.replaceAll(" ", "");
        desiredproduct = product.trim();


        String uid = desiredcroptype + desiredproduct + plotType;

        Log.d("UID", uid);
        return uid;
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
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
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

    //if mob no already exist
    private boolean isAlreadydone(String retailerNumber) {
        boolean isExist = false;
        Cursor data = mDatabase.fetchAlreadyExistsHarvestDayData(retailerNumber);
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
            //spProductName.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     *
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
                    AppConstant.Imagename = "H1" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
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
            case R.id.btnActivityPhoto:
                imageselect = 1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnFarmerListPhoto:
                imageselect = 2;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;

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


            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));

            if (imageselect == 1) {
                AppConstant.Imagename = "liveplantretailer" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 2) {
                AppConstant.Imagename = "liveplantretailer" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }


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
            if(address.equals("")) {
                if (config.NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            Log.d(TAG, "onlocation" + cordinates);
            tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
            tvAddress.setText(address + "\n" + cordinates);

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UtpadanMohatsavActivity.this);

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


    //////////////////////////////

    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {

        String focussedVillage = "";

        String state = "";
        String district = "";
        String taluka = "";
        String othervillage = "";
        String villagecode = "";
        String finalvillage = "";
        if (radFocusedActivity.isChecked()) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();
            villagecode = config.getvalue(spFocusedVillages);
            finalvillage = focussedVillage;
        } else {
            focussedVillage = "";

        }

        if (radOtherActivity.isChecked()) {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            othervillage = spVillage.getSelectedItem().toString();
            villagecode = config.getvalue(spVillage);
            finalvillage = othervillage;
        } else {
            state = "";
            district = "";
            taluka = "";
            othervillage = "";

        }


        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        str_et_farmername = et_farmername.getText().toString().trim();
        str_et_farmerarea = et_farmerarea.getText().toString().trim();
        str_et_farmeryeild = et_farmeryeild.getText().toString().trim();
        str_et_farmermobile = et_farmermobile.getText().toString().trim();
        str_et_farmerfelisited = et_farmerfelisited.getText().toString().trim();
        str_et_numberoffarmer = et_numberoffarmer.getText().toString().trim();
        str_et_numberofretailer = et_numberofretailer.getText().toString().trim();

        String UserCode = userCode;
        String State = state;
        String District = district;
              String Taluka = taluka;
               String VillageCode = villagecode;

        String VillageName = finalvillage;

        String Crop = cropType;

        String Product = product;

        String HostFarmerName = str_et_farmername;
        String FarmerMobile = str_et_farmermobile;
        String FarmerArea = str_et_farmerarea;
        String FarmerYeild = str_et_farmeryeild;
        String NumberOfFarmerFelisited = str_et_farmerfelisited;
        String NumberOfFarmer = str_et_numberoffarmer;
        String NumberOfRetailer = str_et_numberofretailer;
        String LatLong = cordinates;
        String PhotoName = userCode + "_UtpadanMohatsav_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        String PhotoString = Imagepath1;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String CreatedDate = "" + formattedDate;
        String Status = "0";
        String VersionName = BuildConfig.VERSION_NAME;
        String UplaodStatus = "0";
        ;
        String Extra1 = "";
        String Extra2 = "";

        if (UserCode.equals("")) {
            showAlert("User Code Missing");
        }  else if (VillageCode.equals("")||VillageCode.toLowerCase().contains("select")) {
            showAlert("Village Code Missing");
        } else if (VillageName.equals("")||VillageName.toLowerCase().contains("select")) {
            showAlert("Village Name Missing");
        } else if (Crop.equals("")||Crop.toLowerCase().contains("select")) {
            showAlert("Crop Missing");
        } else if (Product.equals("")||Product.toLowerCase().contains("select")) {
            showAlert("Product Missing");
        } else if (HostFarmerName.equals("")) {
            showAlert("Farmer Name Missing");
        } else if (FarmerMobile.equals("")) {
            showAlert("Farmer Mobile Missing");
        } else if (FarmerArea.equals("")) {
            showAlert("Farmer Area Missing");
        } else if (FarmerYeild.equals("")) {
            showAlert("Farmer Yeild Missing");
        } else if (NumberOfFarmerFelisited.equals("")) {
            showAlert("Number of felicitated Farmer Missing");
        } else if (NumberOfFarmer.equals("")) {
            showAlert("Number of Farmer Missing");
        } else if (NumberOfRetailer.equals("")) {
            showAlert("Number of Retailer Missing");
        } else if (LatLong.equals("")) {
            showAlert("Location not Found");
        } else if (PhotoName.equals("")) {
            showAlert("Photo Name Missing");
        } else if (PhotoString.equals("")) {
            showAlert("Photopath  Missing");
        } else if (CreatedDate.equals("")) {
            showAlert("Created Date Missing");
        } else if (VersionName.equals("")) {
            showAlert("Version name Missing");
        } else {



        if (mDatabase.insertUtpadanMohatsav(UserCode, State, District, Taluka, VillageCode, VillageName, Crop, Product, HostFarmerName, FarmerMobile, FarmerArea, FarmerYeild, NumberOfFarmerFelisited, NumberOfFarmer, NumberOfRetailer, LatLong, PhotoName, PhotoString, CreatedDate, Status, VersionName, UplaodStatus, Extra1, Extra2)) {
            Toast.makeText(context, "Data Saved Successfully.", Toast.LENGTH_SHORT).show();
            if (CommonUtil.addGTVActivity(context, "12", "Utpadan Mohatsav", cordinates, HostFarmerName+" "+product,"GTV","0",0.0)) {
                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }
            String data="";
            boolean flag=false;
            if(config.NetworkConnection()) {
                data = "Data Saved Successfully . Internet is available , would you like to upload it to server ?";
                flag=true;
            }else {
                data = "Data Saved Successfully.";
                flag=false;
            }
            AlertDialog.Builder builder= new AlertDialog.Builder(context);

            if(flag) {
                builder.setTitle("Information")
                        .setMessage(data)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();

                                if(config.NetworkConnection())
                                {
                                    JsonArray jsonArray=new JsonArray();
                                    ArrayList<HashMap> list=mDatabase.getUtpadanMohatsav();
                                    if(list.size()>0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            JsonObject jsonObject = new JsonObject();
                                            HashMap data = list.get(i);
                                            jsonObject.addProperty("ID",data.get("ID").toString());
                                            jsonObject.addProperty("UserCode",data.get("UserCode").toString());
                                            jsonObject.addProperty("State",data.get("State").toString());
                                            jsonObject.addProperty("District",data.get("District").toString());
                                            jsonObject.addProperty("Taluka",data.get("Taluka").toString());
                                            jsonObject.addProperty("VillageCode",data.get("VillageCode").toString());
                                            jsonObject.addProperty("VillageName",data.get("VillageName").toString());
                                            jsonObject.addProperty("Crop",data.get("Crop").toString());
                                            jsonObject.addProperty("Product",data.get("Product").toString());
                                            jsonObject.addProperty("HostFarmerName",data.get("HostFarmerName").toString());
                                            jsonObject.addProperty("FarmerMobile",data.get("FarmerMobile").toString());
                                            jsonObject.addProperty("FarmerArea",data.get("FarmerArea").toString());
                                            jsonObject.addProperty("FarmerYeild",data.get("FarmerYeild").toString());
                                            jsonObject.addProperty("NumberOfFarmerFelisited",data.get("NumberOfFarmerFelisited").toString());
                                            jsonObject.addProperty("NumberOfFarmer",data.get("NumberOfFarmer").toString());
                                            jsonObject.addProperty("NumberOfRetailer",data.get("NumberOfRetailer").toString());
                                            jsonObject.addProperty("PhotoName",data.get("PhotoName").toString());
                                            jsonObject.addProperty("PhotoString",mDatabase.getImageDatadetail(data.get("PhotoString").toString().trim()));
                                            jsonObject.addProperty("Status",data.get("Status").toString());
                                            jsonObject.addProperty("VersionName",data.get("VersionName").toString());
                                            jsonObject.addProperty("LatLong",data.get("LatLong").toString());
                                            jsonObject.addProperty("CreatedDate",data.get("CreatedDate").toString());
                                            jsonObject.addProperty("UplaodStatus",data.get("UplaodStatus").toString());
                                            jsonObject.addProperty("info1",data.get("Extra1").toString());
                                            jsonObject.addProperty("info2",data.get("Extra2").toString());
                                            jsonArray.add(jsonObject);
                                        }
                                        Log.i("JsonData", jsonArray.toString());
                                        new ShellingDayAndUtpadanMohatsavAPI(context, UtpadanMohatsavActivity.this).UploadUtpadanMohatsav(jsonArray);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .show();
            }else
            {
                builder.setTitle("Information")
                        .setMessage(data)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .show();
            }

        } else {
            Toast.makeText(context, "Data not saved.", Toast.LENGTH_SHORT).show();

        }

        }


    }

    void showAlert(String message) {
        try {

            new AlertDialog.Builder(context)
                    .setTitle("Validation Message")
                    .setMessage("" + message)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();

        } catch (Exception e) {

        }
    }

    public void uploadData(String HarvestDayData) {
        String str = null;
       /* if (config.NetworkConnection()) {

            try {
                new UploadHarvestDayDataServer(HarvestDayData, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else*/
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(UtpadanMohatsavActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(UtpadanMohatsavActivity.this);
                    dialog.dismiss();
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public String uploadHarvestDayData(String harvestDayData) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from HarvestDayData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));

                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");

                    jsonArray.getJSONObject(i).put("farmerListPhoto", mDatabase.getImageDatadetail(farmerListPhoto));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));

                    Log.d("HarvestDayData", jsonObject.toString());
                    str = syncHarvestDayDataSingleImage(harvestDayData, SERVER, jsonObject, activityImgName, activityImgPath, farmerListPhotoName, farmerListPhoto);

                    handleHarvestDayDataImageSyncResponse("HarvestDayData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    @Override
    public void onShellingDayUpload(String result) {

    }

    @Override
    public void onUtpadanMohatsavUpload(String result) {
        try {

            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.getBoolean("success")) {
                mDatabase.UpdateStatus("Update tbl_utpadanmohatsavdata set UplaodStatus='1' where UplaodStatus='0'");

                new AlertDialog.Builder(context)
                        .setTitle("Upload Status : Success")
                        .setMessage("" + jsonObject.getString("Message"))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .show();
            }else
            {
                new AlertDialog.Builder(context)
                        .setTitle("Upload Status : Fail")
                        .setMessage("" + jsonObject.getString("Message"))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        }catch (Exception e)
        {
            new AlertDialog.Builder(context)
                    .setTitle("Upload Status : Fail")
                    .setMessage("Something went wrong \n\nResult :"+result)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
    }

    /**
     * <p> //AsyncTask Class for api batch code upload call</p>
     */
    private class UploadHarvestDayDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadHarvestDayDataServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadHarvestDayData("HarvestDayData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UtpadanMohatsavActivity.this);
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
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UtpadanMohatsavActivity.this);
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
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public synchronized String syncHarvestDayDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                             String activityImgPath, String farmerListPhotoName, String farmerListPhoto) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleHarvestDayDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("HarvestDayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateHarvestDayData("0", "1", "1",
                            "1", id);

                } else {

                }
            }
        }

        Log.d("HarvestDayData", "HarvestDayData: " + resultout);
    }

    ///////////////////
    public void openDialog() {

        try {


            final SearchableSpinner spVillage = (SearchableSpinner) dialog.findViewById(R.id.spVillage);
            final SearchableSpinner spTaluka = (SearchableSpinner) dialog.findViewById(R.id.spTaluka);
            recDemoList = (RecyclerView) dialog.findViewById(R.id.recDemoList);
            final TextView txtFarmerCount = (TextView) dialog.findViewById(R.id.txtFarmerCount);
            final TextView txtVillageCount = (TextView) dialog.findViewById(R.id.txtVillageCount);


            layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recDemoList.setLayoutManager(layoutManager);


            lblheader = (TextView) dialog.findViewById(R.id.lblheader);
            final TextView noDataText = (TextView) dialog.findViewById(R.id.noDataText);
            TextView clearSearch = (TextView) dialog.findViewById(R.id.clearSearch);
            final EditText etFarmerNo = (EditText) dialog.findViewById(R.id.etFarmerNo);
            //  Button decrementFarmers = (Button) dialog.findViewById(R.id.decrementFarmers);
            //  Button incrementFarmers = (Button) dialog.findViewById(R.id.incrementFarmers);
            btnAddDialog = (Button) dialog.findViewById(R.id.btnAdd);
            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);
            RadioGroup radGroup = (RadioGroup) dialog.findViewById(R.id.radGroup);
            final RadioButton radFocused = (RadioButton) dialog.findViewById(R.id.radFocused);
            final RadioButton radOther = (RadioButton) dialog.findViewById(R.id.radOther);
            final CardView cardTaluka = (CardView) dialog.findViewById(R.id.cardTaluka);
            bindFocussedVillage(spVillage);
            bindTaluka(spTaluka);
            dialog.setCancelable(true);

            // if button is clicked, close the custom dialog
            btnAddDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());

                    HarvestDayModel harvestDayModel = new HarvestDayModel();

                    if (spVillage.getSelectedItemPosition() != 0 && etFarmerNo.getText().toString().length() != 0) {

                        if (farmerListHarvestDay != null) {
                            if (farmerListHarvestDay.size() > 0) {
                                for (int i = 0; i < farmerListHarvestDay.size(); i++) {
                                    if (!farmerListHarvestDay.get(i).getVillageName().contains(spVillage.getSelectedItem().toString())) {
                                        harvestDayModel.setVillageName(spVillage.getSelectedItem().toString());
                                        harvestDayModel.setFarmerCount(etFarmerNo.getText().toString());
                                        harvestDayModel.setUniqueID(spVillage.getSelectedItem().toString() + timeStamp);
                                        harvestDayModel.setVillageType(villageType);


                                    } else {
                                        Toast.makeText(context, "Already Existed", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                farmerCount = Integer.valueOf(etFarmerNo.getText().toString()) + farmerCount;
                                farmerListHarvestDay.add(harvestDayModel);
                                txtFarmerCount.setText(String.valueOf(farmerCount));
                                txtVillageCount.setText(String.valueOf(farmerListHarvestDay.size()));
                                etFarmerNo.setText("");
                                spVillage.setSelection(0);
                                spTaluka.setSelection(0);

                            } else {
                                harvestDayModel.setVillageName(spVillage.getSelectedItem().toString());
                                harvestDayModel.setFarmerCount(etFarmerNo.getText().toString());
                                harvestDayModel.setUniqueID(spVillage.getSelectedItem().toString() + timeStamp);
                                harvestDayModel.setVillageType(villageType);
                                farmerCount = Integer.valueOf(etFarmerNo.getText().toString()) + farmerCount;
                                farmerListHarvestDay.add(harvestDayModel);
                                txtFarmerCount.setText(String.valueOf(farmerCount));
                                txtVillageCount.setText(String.valueOf(farmerListHarvestDay.size()));
                                etFarmerNo.setText("");
                                spVillage.setSelection(0);
                                spTaluka.setSelection(0);

                            }
                            createAndAddData();
                        }

                    } else {
                        msclass.showMessage("Please select village and Farmers");


                    }
                }
            });

            dialog.show();


            btnSaveClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (spTaluka.getSelectedItemPosition() != 0 || spVillage.getSelectedItemPosition() != 0 || etFarmerNo.getText().toString().length() != 0) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Do you want to close the dailog ?");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface alertdialog, int which) {


                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface alertdialog, int which) {

                                alertdialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {
                        dialog.dismiss();
                    }


                }
            });
//
            spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                        village = gm.Code().trim();
                        Log.i("vilage selected", village);// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // bindFarmerDetails(village);
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
                        taluka = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (!villageType.isEmpty()) {

                        if (villageType.contains("other")) {
                            bindVillage(spVillage, taluka);
                        } else {
                            bindFocussedVillage(spVillage);
                        }
                    } else {

                        Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();

                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


//            incrementFarmers.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!etFarmerNo.getText().toString().isEmpty()) {
//
//                        farmerNumber = Integer.valueOf(etFarmerNo.getText().toString());
//                        if (farmerNumber < 99) {
//                            farmerNumber++;
//                            etFarmerNo.setText(String.valueOf(farmerNumber));
//
//                        } else {
//
//
//                            Toast.makeText(context, "Cannot search more", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        if (farmerNumber < 99) {
//                            farmerNumber++;
//                            etFarmerNo.setText(String.valueOf(farmerNumber));
//
//                        } else {
//
//
//                            Toast.makeText(context, "Cannot search more", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//            decrementFarmers.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    if (!etFarmerNo.getText().toString().isEmpty()) {
//
//                        farmerNumber = Integer.valueOf(etFarmerNo.getText().toString());
//                        if (farmerNumber > 1) {
//                            farmerNumber--;
//                            etFarmerNo.setText(String.valueOf(farmerNumber));
//                        }
//
//                    } else {
//                        if (farmerNumber > 1) {
//                            farmerNumber--;
//                            etFarmerNo.setText(String.valueOf(farmerNumber));
//                        }
//                    }
//                }
//            });


            radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (checkedId) {
                        case R.id.radFocused:

                            if (radFocused.isChecked()) {
                                villageType = "focussed";
                                bindFocussedVillage(spVillage);
                                cardTaluka.setVisibility(View.GONE);

                            } else {
                                villageType = "other";
                                bindTaluka(spTaluka);
                                cardTaluka.setVisibility(View.VISIBLE);

                            }
                            createAndAddData();

                            radOther.setChecked(false);
                            break;
                        case R.id.radOther:
                            if (radOther.isChecked()) {
                                villageType = "other";
                                bindTaluka(spTaluka);
                                cardTaluka.setVisibility(View.VISIBLE);
                            } else {
                                villageType = "focussed";
                                bindFocussedVillage(spVillage);
                                cardTaluka.setVisibility(View.GONE);


                            }
                            createAndAddData();

                            break;

                    }
                    Log.d("Villagetype", villageType);
                }


            });


        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void bindFocussedVillage(SearchableSpinner spVillage) {


        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
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
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);

        } catch (
                Exception ex) {

            ex.printStackTrace();

        }
    }

    private void bindTaluka(SearchableSpinner spTaluka) {


        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster  order by  taluka";
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
                        (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void createAndAddData() {

        try {
            if (villageType.contains("other")) {
                ArrayList<HarvestDayModel> otherVillageList = new ArrayList<>();
                for (int i = 0; i < farmerListHarvestDay.size(); i++) {
                    if (farmerListHarvestDay.get(i).getVillageType().contains("other")) {
                        otherVillageList.add(farmerListHarvestDay.get(i));
                    }
                }
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListHarvestDayAdapter(context, otherVillageList));

            } else if (villageType.contains("focussed")) {
                ArrayList<HarvestDayModel> focussedList = new ArrayList<>();

                for (int i = 0; i < farmerListHarvestDay.size(); i++) {
                    if (farmerListHarvestDay.get(i).getVillageType().contains("focussed")) {
                        focussedList.add(farmerListHarvestDay.get(i));
                    }
                }
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListHarvestDayAdapter(context, focussedList));
//            } else {
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListHarvestDayAdapter(context, farmerListHarvestDay));
            }
            // } else {adapterMDO.notifyDataSetChanged(); }
            recDemoList.setLayoutManager(new LinearLayoutManager(context));
            recDemoList.setAdapter(new AddFarmerListHarvestDayAdapter(context, farmerListHarvestDay));


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindVillage(SearchableSpinner spVillage, String taluka) {
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
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }

}
