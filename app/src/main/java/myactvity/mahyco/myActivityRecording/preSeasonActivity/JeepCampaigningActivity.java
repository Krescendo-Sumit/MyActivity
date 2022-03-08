package myactvity.mahyco.myActivityRecording.preSeasonActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.TimePicker;
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
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class JeepCampaigningActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback, IPickResult,View.OnClickListener {

    private static final String TAG = "DEMOMODELRECORD";
    SearchableSpinner spState, spDist, spTaluka, spDialogTaluka, spDialogVillage, spDialogState, spDialogDis;
    MultiSelectionSpinner spProductName, spCropType;
    Button btnSubmit, btnAddDialog, btnAddLocation;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etCheckHybrid, etRTOnumber, etStartLocation, etEndLocation;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageView imgBtnGps, imgBtnGps2, imgDialogBtnGps;
    private Context context;
    String action = "1";
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String cordinatesmsgEnd = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String cordinatesmsgAddloc = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address="", addressEnd="", addressAddloc="";
    public String search = "";
    // String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    String SERVER = "https://packhouse.mahyco.com/api/preseason/jeepCampaigningData";
    String userCode;
    Config config;
    boolean startlocationflag= false;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    TextView etStartTimePopup, etEndTimePopup, tvCordinates,
            tvEndCordinates, lblheader,
            tvEndAddress, tvAddress,
            tvAddLocCordinatesPopup,
            tvAddlocAddressPopup;
    EditText etOdometerReading, etOdometerReadingEnd,txtProductList,txtCropList;
    EditText etFarmernoPopup;
    private String state, dist, croptype, taluka;
    private long mLastClickTime = 0;
    public String cordinates = "", endCordinates = "", addLocCordinates = "";
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
    boolean GpsEnabled, isfromAddSpot, hasAddedLocation;
    double lati, latiEnd, latiAddloc;
    double longi, longEnd, longiAddloc;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    List<GeneralMaster> retailerList;
    ScrollView container;
    List<GeneralMaster> stateList, distList, talukaList;
    private Handler handler = new Handler();
    String villageType = "focussed";
    Dialog dialog;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
    Button btnSaveClose;
    ArrayList<CropSeminarModel> farmerListCropSeminar;
    int farmerNumber = 0;
    String isTagLocation = "";
    RadioGroup radGroup;
    CardView cardTaluka, cardDistrict, cardState;
    RadioButton radFocused, radOther;
    ImageView ivImage;
    Button btnActivityPhotoPopup;
    int imageselect;
    File photoFile = null;
    public String Imagepath1 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    String statePopup = "";
    String districtPopup = "";
    String talukaPopup = "";
    String villagePopup = "";
    Prefs mPref;
    String startTime = "", endTime, noOfFarmers = "";
    ImageView ivImagePopup;
    String taggedAddressMandiNameEnd = "";
    String taggedCordinatesMandiNameEnd = "";

    static final int TIME_DIALOG_ID = 1111;
    static final int END_TIME_DIALOG_ID = 2222;
    private TextView view;
    public Button btnClick;
    private int hr;
    private int min;
    int calllisner;
    int checkdetailentrydaved=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeep_campaigning);

        initUI();
        checkForLocalStorage();
    }

    /**
     * <p>Method to initialize the elements and functions</p>
     */
    private void initUI() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mPref = Prefs.with(this);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        lblheader = (TextView) findViewById(R.id.lblheader);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvEndAddress = (TextView) findViewById(R.id.tvEndAddress);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_addlocation_jeep_campaigning);
        btnAddLocation = (Button) findViewById(R.id.btnAddLocation);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvEndCordinates = (TextView) findViewById(R.id.tvEndCordinates);
        etRTOnumber = (EditText) findViewById(R.id.etRTOnumber);
        etEndLocation = (EditText) findViewById(R.id.etEndLocation);
        txtCropList= (EditText) findViewById(R.id.txtCropList);
        txtProductList= (EditText) findViewById(R.id.txtProductList);
        etStartLocation = (EditText) findViewById(R.id.etStartLocation);
        etOdometerReading = (EditText) findViewById(R.id.etOdometerReading);
        etOdometerReadingEnd = (EditText) findViewById(R.id.etOdometerReadingEnd);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        imgBtnGps2 = (ImageView) findViewById(R.id.imgBtnGps2);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode =  pref.getString("UserID", null);
        msclass = new Messageclass(this);
        farmerListCropSeminar = new ArrayList<>();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        if (checkPlayServices()) {
            // startFusedLocationService();
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }

        //Check Entry  Saved
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);
        String searchQuery = "";
        searchQuery = "select  * from JeepCampaigningData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0'";
        calllisner =  mDatabase.checkentryexist(searchQuery);// cursor.getCount();



        bindState();
        onSubmitButtonClicked();
        // onStateItemSelected();
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    state = gm.Desc().trim();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (calllisner==0) {
                    bindDist(state);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //onDistItemSelected();
        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (calllisner==0) {
                    bindTaluka(dist);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // onTalukaItemSelected();
        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        bindcroptype(spCropType, "C");
        bindProductName(spProductName, "");

        onCropItemSelected();

        onAddLocationClicked();

        if (lati == 0) {
            startFusedLocationService();
        }

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
     * <P>Method to show the add location dialog</P>
     */
    private void onAddLocationClicked() {
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationAddLocation())
                {

                    //check intial start jeep campinging
                    if(calllisner==0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JeepCampaigningActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Are you sure to Start the Jeep Campaigning ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @SuppressLint("ClickableViewAccessibility")
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //
                                  saveToDb("SAVEEVENT");
                                  openDialog();
                                //

                               // container.setClickable(true);
                               /// container.setEnabled(true);
                               // getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                //  WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                               // container.setEnabled(false);
                               // container.setClickable(false);

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
                    else {

                        openDialog();
                    }
                }
            }
        });
    }

    private void checkForLocalStorage() {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);

        String searchQuery = "select  *  from JeepCampaigningData ";
        //String searchQuery = "";
        searchQuery = "select  * from JeepCampaigningData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0' " +
                "order by  EntryDt desc  limit 1";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            calllisner = count;
            jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
            try {
                String localJson = jsonArray.getJSONObject(jsonArray.length() - 1).toString();
                setStoredValues(localJson);
                Log.d("JeepCampaigningData", localJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setStoredValues(String localJson) {
        try {
            JSONObject jsonObject = new JSONObject(localJson);
            spState.setEnabled(false);
            spDist.setEnabled(false);
            spTaluka.setEnabled(false);
            spCropType.setEnabled(false);
            spProductName.setEnabled(false);
            bindDist(jsonObject.getString("state"));
            bindTaluka(jsonObject.getString("district"));
            bindcroptype(spCropType, "C");


            String[] array;
            String[] array2;
            try {
                String cropl=jsonObject.getString("cropType").toString();
                bindProductName(spProductName, "["+cropl+"]");
                String productlist=jsonObject.getString("product").toString();
                array=cropl.split(",");
                array2=productlist.split(",");
                spCropType.setSelection(array);
                spProductName.setSelection(array2);


            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }




           // txtProductList.setText(jsonObject.getString("product"));
           // txtCropList.setText(jsonObject.getString("cropType"));
           // txtProductList.setEnabled(false);
           // txtCropList.setEnabled(false);


           // txtProductList.setVisibility(View.VISIBLE) ;
           // txtCropList.setVisibility(View.VISIBLE);

           // spCropType.setVisibility(View.GONE) ;
           // spProductName.setVisibility(View.GONE);


//for state selection
            for (int i = 0; i < stateList.size(); i++) {
                if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                    state = stateList.get(i).toString();
                    spState.setSelection(i);
                }
            }
//for dist selection
            for (int j = 0; j < distList.size(); j++) {
                if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                    spDist.setSelection(j);
                }
            }
//for taluka selection
            for (int k = 0; k < talukaList.size(); k++) {
                if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                    spTaluka.setSelection(k);
                }
            }
            // Start Location set ..mahendra
            etStartLocation.setText(jsonObject.getString("startLocation"));
            etOdometerReading.setText(jsonObject.getString("OdometerReading"));
            etRTOnumber.setText(jsonObject.getString("rtoRegistrationNumber"));
            etStartLocation.setEnabled(false);
            etOdometerReading.setEnabled(false);
            etRTOnumber.setEnabled(false);
            tvAddress .setText(jsonObject.getString("taggedAddress"));
            String start =jsonObject.getString("taggedAddress");
            //Check null or empty string
            if (start.isEmpty() )
            {
                startlocationflag = false ;
            }
            else
            {  startlocationflag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * <P>Method to initialize the elements of add location dialog</P>
     */
    public void openDialog() {

        try {
            isTagLocation = "ADDLOC";
            startFusedLocationService();
            ivImage = (ImageView) dialog.findViewById(R.id.ivImagePopup);
            ivImagePopup = (ImageView) dialog.findViewById(R.id.ivImagePopup);
            btnActivityPhotoPopup = (Button) dialog.findViewById(R.id.btnActivityPhotoPopup);
            spDialogVillage = (SearchableSpinner) dialog.findViewById(R.id.spVillagePopup);
            spDialogTaluka = (SearchableSpinner) dialog.findViewById(R.id.spTalukaPopup);
            spDialogDis = (SearchableSpinner) dialog.findViewById(R.id.spDstrictPopup);
            spDialogState = (SearchableSpinner) dialog.findViewById(R.id.spStatePopup);
            etFarmernoPopup = (EditText) dialog.findViewById(R.id.etFarmernoPopup);
            etStartTimePopup = (TextView) dialog.findViewById(R.id.etStartTimePopup);
            etEndTimePopup = (TextView) dialog.findViewById(R.id.etEndTimePopup);
            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);
            imgDialogBtnGps = (ImageView) dialog.findViewById(R.id.imgDialogBtnGpsPopup);
            tvAddLocCordinatesPopup = (TextView) dialog.findViewById(R.id.tvAddlocCordinatesPopup);
            tvAddlocAddressPopup = (TextView) dialog.findViewById(R.id.tvAddlocAddressPopup);
            radGroup = (RadioGroup) dialog.findViewById(R.id.radGroup);
            radFocused = (RadioButton) dialog.findViewById(R.id.radFocused);
            radOther = (RadioButton) dialog.findViewById(R.id.radOther);
            cardTaluka = (CardView) dialog.findViewById(R.id.cardTaluka);
            cardDistrict = (CardView) dialog.findViewById(R.id.cardDistrict);
            cardState = (CardView) dialog.findViewById(R.id.cardState);


            bindFocussedVillage(spDialogVillage);

            bindDialogState(spDialogState);

            dialog.setCancelable(true);

            onRadioGroupChecked();
            btnActivityPhotoPopup.setOnClickListener(this);
           // onActivityPhotoButtonClick();

            onSaveCloseBtnClicked();

            onStateDialogItemSelected();

            onDistDialogItemSelected();

            onTalukaDialogItemSelected();

            startTime = etStartTimePopup.getText().toString();
            endTime = etEndTimePopup.getText().toString();
            noOfFarmers = etFarmernoPopup.getText().toString();

            manageStartTimePicker();
            manageEndTimePicker();
            // Mahendra 16-02-2020
            etStartTimePopup.setText("");
            etEndTimePopup.setText("");
            dialog.show();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
    public void openDialog1() {

        try {
            isTagLocation = "ADDLOC";
            startFusedLocationService();
            ivImage = (ImageView) dialog.findViewById(R.id.ivImagePopup);
            ivImagePopup = (ImageView) dialog.findViewById(R.id.ivImagePopup);
            btnActivityPhotoPopup = (Button) dialog.findViewById(R.id.btnActivityPhotoPopup);
            spDialogVillage = (SearchableSpinner) dialog.findViewById(R.id.spVillagePopup);
            spDialogTaluka = (SearchableSpinner) dialog.findViewById(R.id.spTalukaPopup);
            spDialogDis = (SearchableSpinner) dialog.findViewById(R.id.spDstrictPopup);
            spDialogState = (SearchableSpinner) dialog.findViewById(R.id.spStatePopup);
            etFarmernoPopup = (EditText) dialog.findViewById(R.id.etFarmernoPopup);
            etStartTimePopup = (TextView) dialog.findViewById(R.id.etStartTimePopup);
            etEndTimePopup = (TextView) dialog.findViewById(R.id.etEndTimePopup);
            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);
            imgDialogBtnGps = (ImageView) dialog.findViewById(R.id.imgDialogBtnGpsPopup);
            tvAddLocCordinatesPopup = (TextView) dialog.findViewById(R.id.tvAddlocCordinatesPopup);
            tvAddlocAddressPopup = (TextView) dialog.findViewById(R.id.tvAddlocAddressPopup);
            radGroup = (RadioGroup) dialog.findViewById(R.id.radGroup);
            radFocused = (RadioButton) dialog.findViewById(R.id.radFocused);
            radOther = (RadioButton) dialog.findViewById(R.id.radOther);
            cardTaluka = (CardView) dialog.findViewById(R.id.cardTaluka);
            cardDistrict = (CardView) dialog.findViewById(R.id.cardDistrict);
            cardState = (CardView) dialog.findViewById(R.id.cardState);


            bindFocussedVillage(spDialogVillage);
            bindDialogState(spDialogState);

            dialog.setCancelable(true);

            onRadioGroupChecked();

            onActivityPhotoButtonClick();

            onSaveCloseBtnClicked();

            onStateDialogItemSelected();

            onDistDialogItemSelected();

            onTalukaDialogItemSelected();

            startTime = etStartTimePopup.getText().toString();
            endTime = etEndTimePopup.getText().toString();
            noOfFarmers = etFarmernoPopup.getText().toString();

            manageStartTimePicker();
            manageEndTimePicker();

            dialog.show();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void manageStartTimePicker() {

        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        //  updateTime(hr, min);
        startTimeClickListener();
    }

    private void startTimeClickListener() {
        etStartTimePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createdDialog(1111).show();
            }
        });

    }

    private void manageEndTimePicker() {

        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        //updateEndTime(hr, min);
        endTimeClickListener();
    }

    private void endTimeClickListener() {
        etEndTimePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createdDialog(2222).show();
            }
        });

    }

    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, timePickerListener, hr, min, false);
            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this,android.app.AlertDialog.THEME_HOLO_LIGHT, endtimePickerListener, hr, min, false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };

    private TimePickerDialog.OnTimeSetListener endtimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateEndTime(hr, min);
        }
    };

    private static String utilTime(int value) {

        if (value < 10) return "0" + String.valueOf(value);
        else return String.valueOf(value);
    }

    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        etStartTimePopup.setText(aTime);
    }

    private static String utilENdTime(int value) {

        if (value < 10) return "0" + String.valueOf(value);
        else return String.valueOf(value);
    }

    private void updateEndTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        etEndTimePopup.setText(aTime);
    }


    private void onStateDialogItemSelected() {
        spDialogState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!villageType.isEmpty()) {

                    if (villageType.contains("other")) {
                        bindDialogDistrict(spDialogDis, state);
                    } else {
                        bindFocussedVillage(spDialogVillage);
                    }
                } else {
                    Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void onDistDialogItemSelected() {

        spDialogDis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!villageType.isEmpty()) {

                    if (villageType.contains("other")) {
                        bindDialogTaluka(spDialogTaluka, dist);
                    } else {
                        bindFocussedVillage(spDialogVillage);
                    }
                } else {
                    Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onTalukaDialogItemSelected() {
        spDialogTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


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
                        bindVillage(spDialogVillage, taluka);
                    } else {
                        bindFocussedVillage(spDialogVillage);
                    }
                } else {
                    Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doDetailWork() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveDetailsToDb();
                        saveToDb("SAVEEVENT");
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

    /**
     * <P>Method is used to save the Jeep campaigning spots to local DB </P>
     */
    private void saveDetailsToDb() {

        ///// Popup data
        String taggedAddressPopup = "";
        String taggedCordinatesPopup = "";


        if (tvAddlocAddressPopup.getText().toString().isEmpty() || tvAddlocAddressPopup.getText().toString().equals("")) {
            taggedAddressPopup = "";
        } else {
            taggedAddressPopup = tvAddlocAddressPopup.getText().toString();
            tvEndAddress.setText(taggedAddressPopup + "\n" + addLocCordinates);
            taggedAddressMandiNameEnd = tvEndAddress.getText().toString();
        }

        /*if (!addLocCordinates.isEmpty()) {
            taggedCordinatesPopup = addLocCordinates;
            endCordinates = addLocCordinates;
            taggedCordinatesMandiNameEnd = addLocCordinates;

        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
        }*/
        if (!cordinates.isEmpty()) {
            taggedCordinatesPopup = cordinates;
            endCordinates = cordinates;
            taggedCordinatesMandiNameEnd = cordinates;

        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
        }


        startTime = etStartTimePopup.getText().toString();
        endTime = etEndTimePopup.getText().toString();
        noOfFarmers = etFarmernoPopup.getText().toString();
        statePopup = spDialogState.getSelectedItem().toString().equals("SELECT STATE") ? "" : spDialogState.getSelectedItem().toString();
        try {
            if (spDialogDis.getSelectedItem() != null)
                districtPopup = spDialogDis.getSelectedItem().toString().equals("SELECT DISTRICT") ? "" : spDialogDis.getSelectedItem().toString();

            if (spDialogTaluka.getSelectedItem() != null) {
                talukaPopup = spDialogTaluka.getSelectedItem().toString().equals("SELECT TALUKA") ? "" : spDialogTaluka.getSelectedItem().toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        villagePopup = spDialogVillage.getSelectedItem().toString();

        String isSynced = "0";
        String activityImgStatus = "0";
        Date entrydate = new Date();
        final String activityImgPath;
        activityImgPath = Imagepath1;
        String villcode="";
        villcode=config.getvalue(spDialogVillage);
        final String activityImgName = villcode+AppConstant.Imagename;//"_JeepCampaigningPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());

        boolean fl = mDatabase.insertJeepCampaginingLocationDetails(userCode,
                statePopup, districtPopup, talukaPopup, villagePopup, startTime, endTime, noOfFarmers,
                taggedCordinatesPopup + " " + taggedAddressPopup, taggedCordinatesPopup,
                activityImgName, activityImgPath, activityImgStatus, isSynced);

        if (fl) {
            etFarmernoPopup.setText("");
            ivImage.setImageDrawable(null);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }


    private void selectImage() {
        try {
            if (Indentcreate.getPickImageIntent(this) != null) {
                photoFile = Indentcreate.fileobj;
                startActivityForResult(Indentcreate.getPickImageIntent(this), REQUEST_CAMERA);//100
            } else {
                Toast.makeText(this, "Picker intent not found", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "selectImage(): "+ex.toString());
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
            ivImagePopup.setImageBitmap(bm);
        }

    }

    private void onCaptureImageResult(Intent data) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =2;
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
            Date entrydate = new Date();
            String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            if (imageselect == 1) {
                isfromAddSpot = true;
                try {
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImagePopup.setImageBitmap(myBitmap);

                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }
    // Method to capture Farmer image
    private void onActivityPhotoButtonClick() {
        btnActivityPhotoPopup.setOnClickListener(new View.OnClickListener() {
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
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActivityPhotoPopup:
                imageselect=1;
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


            if (imageselect == 1)
            {
                ivImage.setImageBitmap(r.getBitmap());
                if (ivImage.getDrawable() != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            /*if (imageselect == 2)
            {
                ivImageFarmerListPhoto.setImageBitmap(r.getBitmap());
                if (ivImageFarmerListPhoto.getDrawable() != null) {
                    ivImageFarmerListPhoto.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImageFarmerListPhoto.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            if (imageselect == 3)
            {
                ivImageRetailerListPhoto.setImageBitmap(r.getBitmap());
                if (ivImageRetailerListPhoto.getDrawable() != null) {
                    ivImageRetailerListPhoto.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImageRetailerListPhoto.setVisibility(View.GONE);
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
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            /*if (imageselect == 2) {
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    /**
     * <P>Method to save add location details </P>
     */
    private void onSaveCloseBtnClicked() {
        btnSaveClose.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialogValidation())
                {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle("MyActivity");
                    builder.setMessage("Do you want to save the details ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface alertdialog, int which) {
                            hasAddedLocation = true;
                            doDetailWork();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface alertdialog, int which) {
                            alertdialog.dismiss();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    //   dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private boolean dialogValidation() {
        if (radFocused.isChecked() && radOther.isChecked()) {
            msclass.showMessage("Please Select Village");
            return false;
        }

        if (radOther.isChecked()) {

            if (spDialogState.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select State");
                return false;
            }
            if (spDialogDis.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select District");
                return false;
            }
            if (spDialogTaluka.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }
            if (spDialogVillage.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select village name");
                return false;
            }
        } else {

            if (spDialogVillage.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select Focused Village");
                return false;
            }
        }
        if (etStartTimePopup.getText().length() == 0) {
            msclass.showMessage("Please enter start time");
            return false;
        }
        if (etEndTimePopup.getText().length() == 0) {
            msclass.showMessage("Please enter end time");
            return false;
        }

        if (etFarmernoPopup.getText().length() == 0) {
            msclass.showMessage("Please enter no. of farmers");
            return false;
        }

//        if (!checkImageResource(this, imgDialogBtnGps, R.drawable.ic_location_on)) {
//            Utility.showAlertDialog("Info", "Please Tag The Field", context);
//            return false;
//        }

        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please Click Photo of  Farmer Gathering");
            return false;
        }

        return true;
    }


    /**
     * <P>Method to bind the DialogDistrict data into a spinner</P>
     *
     * @param spDialogDis
     */
    private void bindDialogDistrict(SearchableSpinner spDialogDis, String state) {
        try {
            spDialogDis.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                distList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                distList.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    distList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, distList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDialogDis.setAdapter(adapter);
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

    /**
     * <P>Method to bind the DialogState data into a spinner</P>
     *
     * @param spDailogState
     */
    private void bindDialogState(SearchableSpinner spDailogState) {

        try {
            spDailogState.setAdapter(null);
            String str = null;
            try {
                stateList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                stateList.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    stateList.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, stateList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDailogState.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    /**
     * <p>Method to handle the spinners visibility when radio button checked</p>
     */
    private void onRadioGroupChecked() {
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocused:

                        if (radFocused.isChecked()) {
                            villageType = "focussed";
                            bindFocussedVillage(spDialogVillage);
                            cardTaluka.setVisibility(View.GONE);
                            cardDistrict.setVisibility(View.GONE);
                            cardState.setVisibility(View.GONE);
                            statePopup = "";
                            districtPopup = "";
                            talukaPopup = "";
                            villagePopup = "";
                        } else {
                            villageType = "other";
                            bindDialogTaluka(spDialogTaluka, dist);
                            bindDialogState(spDialogState);
                            bindDialogDistrict(spDialogDis, state);
                            cardTaluka.setVisibility(View.VISIBLE);
                            cardDistrict.setVisibility(View.VISIBLE);
                            cardState.setVisibility(View.VISIBLE);
                            statePopup = spDialogState.getSelectedItem().toString();
                            districtPopup = spDialogDis.getSelectedItem().toString();
                            talukaPopup = spDialogTaluka.getSelectedItem().toString();
                            villagePopup = spDialogVillage.getSelectedItem().toString();
                        }
                        // createAndAddData();

                        radOther.setChecked(false);
                        break;
                    case R.id.radOther:
                        if (radOther.isChecked()) {
                            villageType = "other";
                            bindDialogTaluka(spDialogTaluka, dist);
                            bindDialogState(spDialogState);
                            bindDialogDistrict(spDialogDis, state);
                            cardTaluka.setVisibility(View.VISIBLE);
                            cardDistrict.setVisibility(View.VISIBLE);
                            cardState.setVisibility(View.VISIBLE);
                            statePopup = spDialogState.getSelectedItem().toString();
                            districtPopup = spDialogDis.getSelectedItem().toString();
                            talukaPopup = spDialogTaluka.getSelectedItem().toString();
                            villagePopup = spDialogVillage.getSelectedItem().toString();
                        } else {
                            villageType = "focussed";
                            bindFocussedVillage(spDialogVillage);
                            cardTaluka.setVisibility(View.GONE);
                            cardDistrict.setVisibility(View.GONE);
                            cardState.setVisibility(View.GONE);
                            statePopup = "";
                            districtPopup = "";
                            talukaPopup = "";
                            villagePopup = spDialogVillage.getSelectedItem().toString();

                        }
                        //   createAndAddData();

                        break;

                }
                Log.d("Villagetype", villageType);
            }


        });

    }

    /**
     * <P>Method to bind the DialogTaluka data into a spinner</P>
     *
     * @param spTaluka
     */
    private void bindDialogTaluka(SearchableSpinner spTaluka, String dist) {

        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                talukaList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";

                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                talukaList.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    talukaList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (context, android.R.layout.simple_spinner_dropdown_item, talukaList);
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

    /**
     * <P>Method to bind the focusedVillage data into a spinner</P>
     *
     * @param spVillage
     */
    private void bindFocussedVillage(SearchableSpinner spVillage) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
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


    /**
     * <P>Method to handle the action on submit button</P>
     */
    private void onSubmitButtonClicked() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(JeepCampaigningActivity.this);

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

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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


    /**
     * <P>Method to handle the data saving to db work</P>
     */
    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        saveToDb("SUBMITEVENT");
                       // uploadData("JeepCampaigningData");
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

    /**
     * <P>Method to save the data to db</P>
     */
    public void saveToDb(String buttonEvent) {
        String state = "";
        String district = "";
        String taluka = "";

        state = spState.getSelectedItem().toString();
        district = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        String OdometerReading = etOdometerReading.getText().toString();
        String OdometerReadingEnd = etOdometerReadingEnd.getText().toString();
        String rtoRegistrationNumber = etRTOnumber.getText().toString();
        String startLocation = etStartLocation.getText().toString();
        String endLocation = etEndLocation.getText().toString();
        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        String taggedAddress = "";
        String taggedCordinates = "";

        String taggedAddressEnd = "";
        String taggedCordinatesEnd = "";

        if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {
            taggedAddress = "";
        } else {
            taggedAddress = tvAddress.getText().toString();
        }

        if (tvEndAddress.getText().toString().isEmpty() || tvEndAddress.getText().toString().equals("")) {
            taggedAddressEnd = "";
        } else {
            taggedAddressEnd = tvEndAddress.getText().toString();
        }


        if (!cordinates.isEmpty()) {
            taggedCordinates = cordinates;
            taggedCordinatesEnd = cordinates;
        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
        }
        Log.d("LocationDatasaveToDb", cordinates);


       /* if (!endCordinates.isEmpty()) {
            taggedCordinatesEnd = endCordinates;
        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
        }*/


        String finalPopupJson = getPopupData();

        //Star
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);


        String searchQuery = "";
        searchQuery = "select  * from JeepCampaigningData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        String  updatestring ="";
        if (count > 0) {

            if (buttonEvent.equals("SAVEEVENT"))
            {
                updatestring="update  JeepCampaigningData set finalPopupJson='"+finalPopupJson+"' " +
                        " where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit=0 ";
                mDatabase.Updatedata(updatestring);
                msclass.showMessage("Data saved  successfully.");
            }
            if (buttonEvent.equals("SUBMITEVENT"))
            {
                // After submit delete  wallpainging details data
                updatestring="update  JeepCampaigningData set finalPopupJson='"+finalPopupJson+"' " +
                        ",FinalSubmit=1,taggedAddressEnd='"+taggedAddressEnd+"'," +
                        "taggedCordinatesEnd='"+taggedCordinatesEnd+"'" +
                        ",OdometerReadingEnd='"+OdometerReadingEnd+"'" +
                        ",endLocation='"+endLocation+"' where strftime('%Y-%m-%d', EntryDt)='"+strdate+"' " +
                        " and FinalSubmit=0 ";
                mDatabase.Updatedata(updatestring);
                mDatabase.deleterecord("delete from JeepCampaigningLocDetails");
                msclass.showMessage("Data submitted successfully.");
                uploadData("ATLVillagePosteringData");

            }
            // Check submit button and Save button Event
            //update  finalPopupJson coloum   base entry and finalsubmit coloum
        }
        else {
            boolean fl = mDatabase.insertJeepCampaginingData(userCode, state, district, taluka,
                    cropType, product, taggedCordinates + " " + taggedAddress, taggedCordinates, taggedCordinatesEnd + " " + taggedAddressEnd, taggedCordinatesEnd, OdometerReading, startLocation,
                    endLocation, rtoRegistrationNumber, OdometerReadingEnd, finalPopupJson);
            if (fl) {

                if(calllisner==0)  // Intial Click on Add location
                {
                    checkForLocalStorage();

                }
                else {
                    msclass.showMessage("Data saved successfully.");
                    checkForLocalStorage();
                }
                // uploadData("JeepCampaigningData");

                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
        //end


    }

    private String getPopupData() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);

        String searchQuery = "select  *  from JeepCampaigningLocDetails where " +
                "strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and " +
                "isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
        }
        return jsonArray.toString();
    }
    /**
     * <P>Method is used to upload the saved data onto the server</P>
     *
     * @param jeepCampaigningData
     */
    private void uploadData(String jeepCampaigningData) {
        String str = null;
       /* if (config.NetworkConnection()) {

            try {
                new UploadJeepCampaigningDataServer(jeepCampaigningData, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
        */
       {
            AlertDialog.Builder builder = new AlertDialog.Builder(JeepCampaigningActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(JeepCampaigningActivity.this);
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

    /**
     * <P>AsyncTask Class for api batch code upload call</P>
     */
    private class UploadJeepCampaigningDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadJeepCampaigningDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadJeepCampaigningData("JeepCampaigningData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JeepCampaigningActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(JeepCampaigningActivity.this);
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
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(JeepCampaigningActivity.this);
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

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(JeepCampaigningActivity.this);
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

    private String uploadJeepCampaigningData(String jeepCampaigningData) {

        String str = "";
        int action = 1;
        String searchQuery = "select  *  from JeepCampaigningData";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String finalPopupJson = jsonArray.getJSONObject(i).getString("finalPopupJson");
                    JSONArray jsonArrayPop = new JSONArray(finalPopupJson);
                    jsonArray.getJSONObject(i).put("finalPopupJson", jsonArrayPop);


                    for (int j = 0; j < jsonArrayPop.length(); j++) {

                        String imgPath = jsonArrayPop.getJSONObject(j).getString("activityImgPath");
                        Log.d("imgPath", imgPath);
                        jsonArrayPop.getJSONObject(j).put("activityImgPath", mDatabase.getImageDatadetail(imgPath));
                    }

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("JeepCampaigningData", jsonObject.toString());
                    str = syncJeepCampaigningDataSingleImage(jeepCampaigningData, SERVER, jsonObject);
                    handleJeepCampaigningImageSyncResponse("JeepCampaigningData", str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public void handleJeepCampaigningImageSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("JeepCampaigningData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateJeepCampaigningData("0", "1", "1");
                } else {
                }
            }
        }
        Log.d("JeepCampaigningData", "JeepCampaigningData: " + resultout);

    }


    public synchronized String syncJeepCampaigningDataSingleImage(String jeepCampaigningData, String server, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    private boolean validationAddLocation() {


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
        if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
            return false;
        }
        if (etRTOnumber.getText().length() == 0) {
            msclass.showMessage("Please enter vehicle RTO registration no. ");
            return false;
        }
        if (etOdometerReading.getText().length() == 0) {
            msclass.showMessage("Please enter odometer reading at start location");
            return false;
        }
        if (etStartLocation.getText().length() == 0) {
            msclass.showMessage("Please enter start location");
            return false;
        }

        return true;
    }

    /**
     * <P>Method is used to validate the input values of activity</P>
     *
     * @return
     */
    private int getPopupDataCount() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);


        String searchQuery = "select  *  from JeepCampaigningLocDetails where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();

        return count;

    }
    private boolean validation() {
       if (!validationAddLocation()) {
            return false;
        }
        checkdetailentrydaved= getPopupDataCount();
        if (checkdetailentrydaved==0) {
            msclass.showMessage("Please add location details");
            return false;
        }


        if (etEndLocation.getText().length() == 0) {
            msclass.showMessage("Please enter end location");
            return false;
        }


        if (etOdometerReadingEnd.getText().length() == 0) {
            msclass.showMessage("Please enter odometer reading at end location");
            return false;
        }
        if (!checkImageResource(this, imgBtnGps2, R.drawable.ic_location_on)) {
            Utility.showAlertDialog("Info", "Please Tag The Field", context);
            return false;
        }


        return true;
    }

    /**
     * <P>Method to get the gps location</P>
     */
    private void onGpsIconEndTagClicked() {

        imgBtnGps2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTagLocation = "END";
                if (endCordinates != null && !endCordinates.contains("null")) {
                    if (tvEndCordinates.getText().toString().contains("Yes")) {
                        imgBtnGps2.setImageResource(R.drawable.ic_location_off);
                        cordinatesmsgEnd = "GEO TAG : \n";
                    } else {
                        if (latiEnd != 0) {
                            imgBtnGps2.setImageResource(R.drawable.ic_location_on);
                            cordinatesmsgEnd = "GEO TAG RECIEVED SUCCESSFULLY : \n";
                        } else {
                            startFusedLocationService();
                        }
                    }
                } else {
                    startFusedLocationService();
                }
            }
        });
    }

    /**
     * <P>Method to get the gps location</P>
     */
    private void onGpsIconStartTagClicked() {

        imgBtnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTagLocation = "START";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(JeepCampaigningActivity.this);

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
                FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
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
     * <p>Method to bind crop type into a spinner</p>
     *
     * @param spCropType
     * @param Croptype
     */
    private void bindcroptype(MultiSelectionSpinner spCropType, String Croptype) {
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

            getCropArrayList(searchQuery);

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

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     *
     * @param searchQuery
     */
    private void getCropArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
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
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
    }


    /**
     * <P>Method to do action on crop item selection</P>
     */
    private void onCropItemSelected() {


        spCropType.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ", String.valueOf(strings));
                croptype = String.valueOf(strings);
                bindProductName(spProductName, croptype);
            }
        });

      /* spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(JeepCampaigningActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProductName, croptype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

       */
    }

    /**
     * <P>Method to bind the product name into a spinner</P>
     *
     * @param spProductName
     * @param croptype      Selected crop type
     */
    private void bindProductName(MultiSelectionSpinner spProductName, String croptype) {
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                for (String n : croptype.substring(1, croptype.length() - 1).split(",")) {
                    nameBuilder.append("'").append(n.trim().replace("'", "\\'")).append("',");
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName  IN (" + nameBuilder.toString().substring(0, nameBuilder.length() - 1) + ") ORDER BY 'CropName'";

                }
            } else {
                Log.d("Crop type", "First time");
            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            getArrayList(searchQuery);
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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


    /**
     * <p>Method to do action on taluka item selection</p>
     */
    private void onTalukaItemSelected() {

        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /**
     * <p>Method to do action on dist item selection</p>
     */
    private void onDistItemSelected() {

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

    }

    /**
     * <p>Method to do action on state item selection</p>
     */
    private void onStateItemSelected() {

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
    }

    /**
     * <P>Method to bind the taluka into a spinner</P>
     *

     */
    //bind District to spinner

    public void bindDist(String state) {
        try {
            spDist.setAdapter(null);
            String str = null;
            try {
                distList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        //" where state_code='" + state + "' order by district asc  ";
                        " where state='" + state + "' order by district asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                distList.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    distList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, distList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                // spDist.setSelection(0,false) ;
                // dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // dialog.dismiss();
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }

    //bind Taluka to spinner
    public void bindTaluka(String dist) {
        try {
            spTaluka.setAdapter(null);
            String str = null;
            try {
                talukaList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster " +
                        "where upper(district)='" + dist.toUpperCase()+ "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                talukaList.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    talukaList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, talukaList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                // spTaluka.setSelection(0,false) ;

            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * <p>Method to bind the state into a spinner</p>
     */
    //bind state to spinner
    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                stateList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                stateList.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    stateList.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, stateList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
        }
    }

    /**
     * <p>Method to check that the device google play services not supported for Devices location</p>
     *
     * @return a boolean flag
     */
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
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
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

    @Override
    public void onLocationChanged(Location arg0) {
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
            cordinates = String.valueOf(lati) + "-" + String.valueOf(longi);
            //tvAddress.setText(address + "\n" + cordinates);
            tvEndAddress.setText(addressEnd + "\n" + cordinates);



            if (startlocationflag)  // false as start entry  not saved
            {
                tvEndAddress.setText(addressEnd + "\n" + cordinates);
                addressEnd = getCompleteEndAddressString(lati, longi);
                tvEndAddress.setText(addressEnd + "\n" + cordinates);

            }
            else
            {
                addressEnd = "";
                tvEndAddress.setText("");
                address = getFirstTimeCompleteAddressString(lati, longi);
                tvAddress.setText(address + "\n" + cordinates);
            }


            switch (isTagLocation) {
                case "FIRSTTIME":

                    Log.d(TAG, "onLocationChanged: " + String.valueOf(longi));

                    /*if (!isfromAddSpot)
                    {
                        address = getFirstTimeCompleteAddressString(lati, longi);
                        addressEnd = getCompleteEndAddressString(lati, longi);
                        tvAddress.setText(address + "\n" + cordinates);
                    }
                    tvEndAddress.setText(addressEnd + "\n" + cordinates);
                    */
                    Log.d(TAG, "onlocation" + cordinates);
                    break;
                case "ADDLOC":
                    latiAddloc = arg0.getLatitude();
                    longiAddloc = arg0.getLongitude();
                    location = arg0;
                    Log.d(TAG, "onLocationChanged: " + String.valueOf(longiAddloc));
                    addLocCordinates = String.valueOf(latiAddloc) + "-" + String.valueOf(longiAddloc);
                    addressAddloc = getCompleteAddlocAddressString(latiAddloc, longiAddloc);
                    tvAddlocAddressPopup.setText(addressAddloc + "\n" + addLocCordinates);
                    Log.d(TAG, "onlocation" + addLocCordinates);
                    break;

            }

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }

    /**
     * <P>fetch address from cordinates of add location tag</P>
     *
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */
    private String getCompleteAddlocAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                addressAddloc = addresses.get(0).getAddressLine(0);
                if (checkImageResource(this, imgDialogBtnGps, R.drawable.ic_location_on)) {
                    if (tvAddLocCordinatesPopup != null) {
                        tvAddLocCordinatesPopup.setText(cordinatesmsgAddloc + "\n" + addLocCordinates);
                        tvAddlocAddressPopup.setText(addressAddloc + "\n" + tvAddLocCordinatesPopup);
                    }

                } else {

                    if (tvAddLocCordinatesPopup != null) {
                        tvAddlocAddressPopup.setText(addressAddloc + "\n" + tvAddLocCordinatesPopup);
                        tvAddLocCordinatesPopup.setText(cordinatesmsgAddloc + "\n" + addLocCordinates);
                    }

                }

            }
            tvAddlocAddressPopup.setText(addressAddloc + "\n" + addLocCordinates);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * <P>fetch address from cordinates of end tag location</P>
     *
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */
    private String getCompleteEndAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                addressEnd = addresses.get(0).getAddressLine(0);
                tvEndAddress.setText(addressEnd + "\n" + endCordinates);
                endCordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);
            }
            tvEndAddress.setText(addressEnd + "\n" + endCordinates);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * <p>fetch address from cordinates of start tag location</p>
     *
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */
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

    private String getFirstTimeCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                tvAddress.setText(address + "\n" + cordinates);
                cordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);

            }
            tvAddress.setText(address + "\n" + cordinates);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * <p>Method to check the image resources</p>
     *
     * @param ctx
     * @param imageView
     * @param imageResource
     * @return
     */
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

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopFusedApi();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override
    protected void onResume() {
        super.onResume();


        try {
            isTagLocation = "FIRSTTIME";
            startFusedLocationService();

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }

    }

}
