package myactvity.mahyco.myActivityRecording.preSeasonActivity;

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
import myactvity.mahyco.RetailerandDistributorTag;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.CustomSearchableSpinner;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class VillageMeetingActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback, IPickResult,View.OnClickListener {

    private static final String TAG = "DEMOMODELRECORD";
    SearchableSpinner spState, spDist, spTaluka, spVillage, spFocusedVillages, spTalukaRetailer;
    CustomSearchableSpinner spFarmerDetails;
    MultiSelectionSpinner spCropType, spProductName, spSelectRBM, spSelectTBM, spSelectMDO, spRetailerDetails;
    Button btnSubmit, btnActivityPhoto, btnAddFarmerDetails;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etFarmerName, etMobileNumber, etWhatsappNumber, etArea, etSeedQty, etDate, etRemarks, etCheckHybrid;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageView imgBtnGps;
    private Context context;
    String action = "1";
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address="";
    public String search = "";
    int imageselect;
    File photoFile = null;
    public String Imagepath1 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    // String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    String SERVER = "https://maapackhousenxg.mahyco.com/api/preseason/villageMeeting";
    String userCode;
    Config config;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    TextView tvCordinates, lblheader, tvQty, tvAddress;
    EditText etFarmerCount, etRetailerCount;
    private String state, dist, croptype, taluka, talukaRetailer, tbmCode, retailerDetails;
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
    double lati;
    double longi;
    String focusedVillage;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    List<GeneralMaster> retailerList;
    ScrollView container;
    private Handler handler = new Handler();
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity;
    RadioButton radOtherActivity;
    Prefs mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_meeting);

        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        mPref = Prefs.with(this);
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
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spTalukaRetailer = (SearchableSpinner) findViewById(R.id.spTalukaRetailer);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        spFarmerDetails = (CustomSearchableSpinner) findViewById(R.id.spFarmerDetails);
        spRetailerDetails = (MultiSelectionSpinner) findViewById(R.id.spRetailerDetails);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        spSelectRBM = (MultiSelectionSpinner) findViewById(R.id.spSelectRBM);
        spSelectTBM = (MultiSelectionSpinner) findViewById(R.id.spSelectTBM);
        spSelectMDO = (MultiSelectionSpinner) findViewById(R.id.spSelectMDO);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnActivityPhoto = (Button) findViewById(R.id.btnActivityPhoto);
        btnAddFarmerDetails = (Button) findViewById(R.id.btnAddFarmerDetails);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etArea = (EditText) findViewById(R.id.etArea);
        etSeedQty = (EditText) findViewById(R.id.etSeedQty);
        etDate = (EditText) findViewById(R.id.etDate);
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        etCheckHybrid = (EditText) findViewById(R.id.etCheckHybrid);
        etFarmerCount = (EditText) findViewById(R.id.etFarmerCount);
        etRetailerCount = (EditText) findViewById(R.id.etRetailerCount);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);

        userCode = pref.getString("UserID", null);
        userCode =  pref.getString("UserID", null);
        msclass = new Messageclass(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        bindTBM();
        bindRBM();
        bindMDO();
        bindState();
        bindFocussedVillages();
        bindTalukaRetailer();
        bindcroptype(spCropType, "C");
        bindProductName(spProductName, "");

        onCropItemSelected();

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
                            // bindFocussedVillage(spVillage);
                            bindFocussedVillages();
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
                            //  bindFocussedVillage(spVillage);
                            bindFocussedVillages();
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


        spSelectRBM.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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

        spSelectTBM.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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
        spSelectMDO.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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

        spTalukaRetailer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    talukaRetailer = gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    bindRetailerDetails(talukaRetailer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        btnActivityPhoto.setOnClickListener(this);
        /*btnActivityPhoto.setOnClickListener(new View.OnClickListener() {
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
 */
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(VillageMeetingActivity.this);

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


      /*  spRetailerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                Intent intent;
                try {
                    retailerDetails = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if (retailerList != null) {
                        if (position ==  1) {
                            intent = new Intent(VillageMeetingActivity.this, RetailerandDistributorTag.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

       */

        spRetailerDetails.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {

            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ", String.valueOf(strings));

                if (String.valueOf(strings).contains("OTHER") ||
                String.valueOf(strings).contains("NEW RETAILER (TAG THE RETAILER)")) {

                    Intent intent;
                    editor.putString("RetailerCallActivity","VillageMeetingActivity");
                    editor.commit();
                    intent = new Intent(VillageMeetingActivity.this, RetailerandDistributorTag.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Log.d("Other:: ", String.valueOf(strings));
                } else {
                    Log.d("All indices", String.valueOf(strings));
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

    /**
     * <P>Method is used to validate the input values of activity</P>
     *
     * @return
     */
    public boolean validation() {

        if((spSelectRBM.getSelectedItem().toString().equals("SELECT RBM"))&&
           (spSelectTBM.getSelectedItem().toString().equals("SELECT TBM"))
        &&(spSelectMDO.getSelectedItem().toString().equals("SELECT MDO / SO"))
        )
        {
            msclass.showMessage("Please select RBM/TBM/MDO conducting the meeting");

           return false;
        }

        //for spinners validation
        if (radFocusedActivity.isChecked()) {
            if (spFocusedVillages.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select focused village");
                return false;
            }
        }

        if (radOtherActivity.isChecked()) {

            if (spState.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select state");
                return false;
            }
            if (spDist.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select district");
                return false;
            }
            if (spTaluka.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select taluka");
                return false;
            }
            if (spVillage.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select village name");
                return false;
            }
        }

        if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
            return false;
        }
        if (etFarmerCount.getText().length() == 0) {
            msclass.showMessage("Please enter no. of farmers");
            return false;
        }

        //for input edit text
        if (etRetailerCount.getText().length() == 0) {
            msclass.showMessage("Please enter no. of retailers");
           return false;
        }

//        if (spTalukaRetailer.getSelectedItemPosition() == 0) {
//            msclass.showMessage("Please select retailer taluka");
//            return false;
//        }

        //for retailer spinner
//        if (spRetailerDetails.getSelectedItem().toString().equalsIgnoreCase("SELECT RETAILER")) {
//
//            Utility.showAlertDialog("Info", "Please Select  Retailer Name", context);
//            return false;
//        }
        if (etRetailerCount.getText().length() > 0) {
            if (Integer.parseInt(etRetailerCount.getText().toString())>0) {
                if (spTalukaRetailer.getSelectedItemPosition() == 0)
                {
                    msclass.showMessage("Please select retailer taluka");
                    return false;
                }
                //for retailer spinner
                if (spRetailerDetails.getSelectedItem().toString().equalsIgnoreCase("SELECT RETAILER"))
                {
                    Utility.showAlertDialog("Info", "Please Select  Retailer Name", context);
                    return false;
                }
            }
        }
        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
            Utility.showAlertDialog("Info", "Please Tag The Field", context);
            return false;
        }

        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please Click Activity Photo");
            return false;
       }
        String retdetails = spRetailerDetails.getSelectedItem().toString();

      /*  if (!isAlreadydone(retdetails)) {
            Utility.showAlertDialog("Info", "This village meeting details already exists", context);
            return false;
        }*/
        return true;
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

    public void bindTalukaRetailer() {
        try {
            spTalukaRetailer.setAdapter(null);
            String str = null;
            try {

                // str= cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster";
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
                spTalukaRetailer.setAdapter(adapter);
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

    public void bindRetailerDetails(String taluka) {
        try {
            // spRetailerDetails.setAdapter(null);
            String str = null;
            try {
                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname  " +
                        "FROM MDO_tagRetailerList where taluka='" + taluka + "' order by name asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
//                retailerList.add(new GeneralMaster("SELECT RETAILER",
//                        "SELECT RETAILER"));
                getRetailerArrayList(searchQuery);

                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(2).toUpperCase() + ", " + cursor.getString(1).toUpperCase()));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
//                retailerList.add(1, new
//
//                        GeneralMaster("OTHER",
//                        "OTHER"));
                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spRetailerDetails.setAdapter(adapter);
                //spMobNumber.setSelected(false);  // must
                // spMobNumber.setSelection(0, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private void getRetailerArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 2];
            array[0] = "SELECT RETAILER";
           // array[1]="NEW RETAILER (TAG THE RETAILER)";
            array[1]="OTHER";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 2] = jObject.getString("name").toUpperCase() + ", " + jObject.getString("firmname").toUpperCase();
            }

            //array[jArray.length() + 1] = "OTHER";
            if (array.length > 0)
            {
                spRetailerDetails.setItems(array);
                spRetailerDetails.hasNoneOption(true);
                spRetailerDetails.hasRetailerOption(true);
                spRetailerDetails.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
    }


    //bind RBM to spinner
    public void bindRBM() {

        try {
            // spSelectRBM.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct code,description  FROM RbmMaster order by description asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
//                list.add(new GeneralMaster("SELECT RBM",
//                        "SELECT RBM"));

                getRBMArrayList(searchQuery);

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // spSelectRBM.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }

    }

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     *
     * @param searchQuery
     */
    private void getRBMArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT RBM";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("description").toUpperCase();
            }

            if (array.length > 0) {
                spSelectRBM.setItems(array);
                spSelectRBM.hasNoneOption(true);
                spSelectRBM.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
    }


    //bind District to spinner
    public void bindTBM() {
        try {
            //spSelectTBM.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct tbmCode,tbmDesc  FROM MdoTbmMaster" +
                        "  order by tbmDesc asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
//                list.add(new GeneralMaster("SELECT TBM",
//                        "SELECT TBM"));

                getTBMArrayList(searchQuery);

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // spSelectTBM.setAdapter(adapter);
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

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     *
     * @param searchQuery
     */
    private void getTBMArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT TBM";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("tbmDesc").toUpperCase();
            }

            if (array.length > 0) {
                spSelectTBM.setItems(array);
                spSelectTBM.hasNoneOption(true);
                spSelectTBM.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
    }


    public void bindMDO() {
        try {
            //spSelectMDO.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();

                String searchQuery = "SELECT distinct mdoCode,mdoDesc  FROM MdoTbmMaster where mdoCode!='" + "NA" + "' order by mdoDesc asc  ";


                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
//                list.add(new GeneralMaster("SELECT MDO / SO",
//                        "SELECT MDO / SO"));

                getMDOArrayList(searchQuery);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //  spSelectMDO.setAdapter(adapter);
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

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     *
     * @param searchQuery
     */
    private void getMDOArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT MDO / SO";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("mdoDesc").toUpperCase();
            }

            if (array.length > 0) {
                spSelectMDO.setItems(array);
                spSelectMDO.hasNoneOption(true);
                spSelectMDO.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(VillageMeetingActivity.this);

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

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                    tvAddress.setText(address + "\n"+ cordinates);
                    tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                } else {
                    tvAddress.setText(address + "\n"+ cordinates);
                    tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                }
            }
            tvAddress.setText(address + "\n"+ cordinates);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
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
            ivImage.setImageBitmap(bm);
        }

    }

    private void onCaptureImageResult(Intent data) {

        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize =2;
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    Date entrydate = new Date();
                    String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
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



            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));

            if (imageselect == 1) {
                AppConstant.Imagename = "farmerlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }



        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
   // @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }*/
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }


    //////////////////////////////
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


    public void saveToDb() {
        String focussedVillage = "";
        String state = "";
        String district = "";
        String taluka = "";
        String othervillage = "", farmerCount = "", retailerCount = "";
        String villagecode="";
        if (radFocusedActivity.isChecked()) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();
            villagecode=config.getvalue(spFocusedVillages);
        } else {
            focussedVillage = "";
        }
        if (radOtherActivity.isChecked()) {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            othervillage = spVillage.getSelectedItem().toString();
            villagecode=config.getvalue(spVillage);
        } else {
            state = "";
            district = "";
            taluka = "";
            othervillage = "";
        }
        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();

        String taggedAddress = "";

        String selectRBM = spSelectRBM.getSelectedItem().toString();
        String selectTBM = spSelectTBM.getSelectedItem().toString();
        String selectMDO = spSelectMDO.getSelectedItem().toString();
        String retailerDetails = spRetailerDetails.getSelectedItem().toString();
        String talukaRetailer = spTalukaRetailer.getSelectedItem().toString();
        if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {
            taggedAddress = "";

        } else {
            taggedAddress = tvAddress.getText().toString();
        }

        String taggedCordinates = "";
        if (!cordinates.isEmpty()) {
            taggedCordinates = cordinates;
        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }
        if (etFarmerCount.getTextSize() > 0) {
            farmerCount = etFarmerCount.getText().toString();
        } else {
            farmerCount = "";
        }
        if (etRetailerCount.getTextSize() > 0) {
            retailerCount = etRetailerCount.getText().toString();
        } else {
            retailerCount = "";
        }
        Log.d("LocationDatasaveToDb", cordinates);
        String isSynced = "0";
        String activityImgStatus = "0";
        Date entrydate = new Date();
        final String activityImgPath;
        activityImgPath = Imagepath1;
        final String activityImgName = AppConstant.Imagename;//"VillageMeetingPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());

        boolean fl = mDatabase.insertVillageMeetingData(userCode, focussedVillage, state, district, taluka, othervillage,
                cropType, product, taggedCordinates +" "+ taggedAddress, taggedCordinates, selectRBM, selectTBM, selectMDO, retailerDetails, talukaRetailer,
                activityImgName, activityImgPath, activityImgStatus, farmerCount,
                retailerCount, isSynced,villagecode);

        if (fl) {


            if (CommonUtil.addGTVActivity(context, "2", "Village Meeting", cordinates, product,"GTV","0",0.0)) {
               // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }

            uploadData("VillageMeetingData");
           // msclass.showMessage("data saved successfully.");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadData(String VillageMeetingData) {
        String str = null;
       /* if (config.NetworkConnection()) {

            try {
                new VillageMeetingActivity.UploadVillageMeetingDataServer(VillageMeetingData, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else*/ {
            AlertDialog.Builder builder = new AlertDialog.Builder(VillageMeetingActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(VillageMeetingActivity.this);
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

    public String uploadVillageMeetingData(String VillageMeetingData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from VillageMeetingData where  isSynced ='0'";
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
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("VillageMeetingData", jsonObject.toString());
                    str = syncVillageMeetingDataSingleImage(VillageMeetingData, SERVER, jsonObject, activityImgName, activityImgPath);
                    handleVillageMeetingDataImageSyncResponse("VillageMeetingData", str,id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadVillageMeetingDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadVillageMeetingDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadVillageMeetingData("VillageMeetingData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VillageMeetingActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(VillageMeetingActivity.this);
                                             dialog.dismiss();
                                relPRogress.setVisibility(View.GONE);
                                container.setClickable(true);
                                container.setEnabled(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(VillageMeetingActivity.this);
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(VillageMeetingActivity.this);
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

    public synchronized String syncVillageMeetingDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                 String activityImgPath) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleVillageMeetingDataImageSyncResponse(String function, String resultout,String id) throws JSONException {
        if (function.equals("VillageMeetingData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateVillageMeetingData("0", "1", "1",id);

                } else {

                }
            }
        }

        Log.d("VillageMeetingData", "VillageMeetingData: " + resultout);
    }

    ///////////////////
    //if retailer  already exist

    public String uniqueId(String focussedVillage) {
        String village = "";
        if (spFocusedVillages.getSelectedItemPosition() != 0) {
            if (focussedVillage.contains("OTHER")) {
                village = spVillage.getSelectedItem().toString();
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
    private boolean isAlreadydone(String retailerDetails) {

        String village = "";
        boolean isExist = false;
        if (radOtherActivity.isChecked()) {

            village = spVillage.getSelectedItem().toString();
        } else {
            village = spFocusedVillages.getSelectedItem().toString();
        }
        Cursor data = mDatabase.fetchAlreadyExistVillageMeetingCount(village);
        Cursor data2 = mDatabase.fetchAlreadyExistVillageMeetingCountFocusssed(village);
        int a=0;
        a=data2.getCount()+data.getCount();
        if (a == 0) {

            isExist = true;

        }
        data.close();
        data2.close();



        return isExist;


    }


}
