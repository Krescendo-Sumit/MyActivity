package myactvity.mahyco.myActivityRecording.atlActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.CustomSearchableSpinner;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class ATLPosteringActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback
, IPickResult,View.OnClickListener{
    SearchableSpinner spState, spDist, spVillage, spTaluka, spCrop, spProduct;
    CustomSearchableSpinner spFocusedVillages;
    EditText etTotalPosters;
    String userCode, state, taluka, dist, village;
    ProgressBar progressBar;
    public SqliteDatabase mDatabase;
    SharedPreferences pref;
    Context context;
    public Messageclass msclass;
    private Handler handler = new Handler();
    RelativeLayout relPRogress;
    private long mLastClickTime = 0;
    ScrollView container;
    Config config;
    SharedPreferences locdata;
    SharedPreferences.Editor editor;
    String focusedVillage;
    LinearLayout llOtherVillages, llFocussedVillages, llVillage, llTagStartLocation, llTagEndLocation, llTagField;
    List<GeneralMaster> stateList, distList, talukaList, vilageList, focussedVillageList;
    RadioGroup radGroupPosteringType;
    RadioButton radVillagePostering, radMandiPostering, radRoadsidePostering;
    CardView cardFocussedVillage, cardVillage;
    EditText etMandiName, etNumberOfSpots;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity, radOtherActivity;
    String croptypePopup = "", productNamePopup = "", totalSpots = "", totalPosters = "";
    Dialog dialog;
    boolean startlocationflag=false;
    Button btnAddPosteringSpots, btnSpotPhoto, btnSaveClose, btnSubmit;
    TextView tvAddress, tvCordinates, tvAddressStartLocation, tvAddressEndLocation, tvAddressPopup, tvCordinatesPopup, etTotalSpots;
    ImageView imgBtnGps, ivImage;
    RelativeLayout relVillageTYpeActivity;
    public String cordinates = "", startCordinates = "", endCordinates = "", addLocCordinates = "";
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled, isfromAddSpot;
    private static final String TAG = "ATLPostering";
    double lati, latiEnd, latiAddloc;
    double longi, longEnd, longiAddloc;
    String cordinatesmsg = "";
    String address="", addressEnd="", addressAddloc="", addressStart="";

    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    int imageselect;
    File photoFile = null;
    public String Imagepath1 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "DEMOMODELPHOTO";
    boolean hasAddedPosteringSpot = false;
    LinearLayout llSpot, llMandi;
    String selectedPosteringType = "VILLAGE POSTERING";
    String strMandiName = "", strTotalPosters = "";
    String isTagLocation = "";
    String taggedAddressMandiNameEnd = "";
    String taggedCordinatesMandiNameEnd = "";
    int calllisner;
    int checkdetailentrydaved=0;

    //  String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    String SERVER = "https://packhouse.mahyco.com/api/atl/posteringData";
    Prefs mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlpostering);
        initUI();

        checkForLocalStorage();

    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        context = this;
        mPref = Prefs.with(this);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        config = new Config(this); //Here the context is passing
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spFocusedVillages = (CustomSearchableSpinner) findViewById(R.id.spFocusedVillages);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        llVillage = (LinearLayout) findViewById(R.id.llVillage);
        llTagStartLocation = (LinearLayout) findViewById(R.id.llTagStartLocation);
        llTagEndLocation = (LinearLayout) findViewById(R.id.llTagEndLocation);
        llTagField = (LinearLayout) findViewById(R.id.llTagField);
        radGroupPosteringType = (RadioGroup) findViewById(R.id.radGroupPosteringType);
        radVillagePostering = (RadioButton) findViewById(R.id.radVillagePostering);
        radMandiPostering = (RadioButton) findViewById(R.id.radMandiPostering);
        radRoadsidePostering = (RadioButton) findViewById(R.id.radRoadsidePostering);
        cardFocussedVillage = (CardView) findViewById(R.id.cardFocussedVillage);
        cardVillage = (CardView) findViewById(R.id.cardVillage);
        etMandiName = (EditText) findViewById(R.id.etMandiName);
        etNumberOfSpots = (EditText) findViewById(R.id.etNumberOfSpots);
        llSpot = (LinearLayout) findViewById(R.id.llSpot);
        llMandi = (LinearLayout) findViewById(R.id.llMandi);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        relVillageTYpeActivity = (RelativeLayout) findViewById(R.id.relVillageTYpeActivity);
        tvAddressStartLocation = (TextView) findViewById(R.id.tvAddressStartLocation);
        tvAddressEndLocation = (TextView) findViewById(R.id.tvAddressEndLocation);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");

        radVillagePostering.setSelected(true);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        container = (ScrollView) findViewById(R.id.container);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode =  pref.getString("UserID", null);
        config = new Config(this); //Here the context is passing
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_atl_postering);

        btnAddPosteringSpots = findViewById(R.id.btnAddPosteringSpots);
        //Check Entry  Saved
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);
        String searchQuery = "";
        searchQuery = "select  * from ATLVillagePosteringData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0'";
         calllisner =  mDatabase.checkentryexist(searchQuery);// cursor.getCount();


        bindState();
        onSubmitBtnClicked();
        managePosteringRadTYpe();
        bindFocussedVillage();
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


        // State Spinner Selection
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

        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (calllisner==0) {
                    bindVillage(taluka);
                }
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
                            llVillage.setVisibility(View.VISIBLE);
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


        btnAddPosteringSpots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationAddPostering()) {
                    //check intial start jeep campinging
                    if(calllisner==0 && radRoadsidePostering.isChecked())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ATLPosteringActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Are you sure to Start the roadside postering ?");
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

    private void managePosteringRadTYpe() {
        radGroupPosteringType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radVillagePostering:
                        selectedPosteringType = "VILLAGE POSTERING";
                        radFocusedActivity.setChecked(true);
                        llFocussedVillages.setVisibility(View.VISIBLE);
                        llOtherVillages.setVisibility(View.GONE);
                        llVillage.setVisibility(View.GONE);
                        etMandiName.setVisibility(View.GONE);
                        llTagStartLocation.setVisibility(View.GONE);
                        llTagEndLocation.setVisibility(View.GONE);
                        llTagField.setVisibility(View.VISIBLE);
                        llSpot.setVisibility(View.GONE);
                        llMandi.setVisibility(View.GONE);
                        relVillageTYpeActivity.setVisibility(View.VISIBLE);
                        if (spFocusedVillages.getSelectedItem().toString().equalsIgnoreCase("OTHER")) {
                            llVillage.setVisibility(View.VISIBLE);
                        } else {
                            llVillage.setVisibility(View.GONE);
                        }
                        strMandiName = "";
                        if (btnSaveClose != null) {
                            btnSaveClose.setText(getString(R.string.save_village));
                        }
                        btnSubmit.setText(getString(R.string.submit_village));
                        break;
                    case R.id.radMandiPostering:
                        selectedPosteringType = "MANDI / MARKET POSTERING";
                        relVillageTYpeActivity.setVisibility(View.GONE);
                        llSpot.setVisibility(View.GONE);
                        llFocussedVillages.setVisibility(View.GONE);
                        llOtherVillages.setVisibility(View.VISIBLE);
                        llVillage.setVisibility(View.GONE);
                        etMandiName.setVisibility(View.VISIBLE);
                        llMandi.setVisibility(View.VISIBLE);
                        llTagStartLocation.setVisibility(View.GONE);
                        llTagEndLocation.setVisibility(View.GONE);
                        llTagField.setVisibility(View.GONE);

                        strMandiName = etMandiName.getText().toString();
                        if (btnSaveClose != null) {
                            btnSaveClose.setText(getString(R.string.save_mandi));
                        }
                        btnSubmit.setText(getString(R.string.submit_mandi));
                        break;
                    case R.id.radRoadsidePostering:
                        selectedPosteringType = "ROADSIDE POSTERING";
                        relVillageTYpeActivity.setVisibility(View.GONE);
                        llSpot.setVisibility(View.GONE);
                        llFocussedVillages.setVisibility(View.GONE);
                        llOtherVillages.setVisibility(View.VISIBLE);
                        llVillage.setVisibility(View.GONE);
                        etMandiName.setVisibility(View.GONE);
                        llMandi.setVisibility(View.GONE);
                        llTagField.setVisibility(View.GONE);
                        llTagStartLocation.setVisibility(View.VISIBLE);
                        llTagEndLocation.setVisibility(View.VISIBLE);
                        strMandiName = "";
                        if (btnSaveClose != null) {
                            btnSaveClose.setText(getString(R.string.save_road));
                        }
                        btnSubmit.setText(getString(R.string.submit_road));
                        break;
                }
            }
        });
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ATLPosteringActivity.this);

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


    private void openDialog() {

        try {
            isTagLocation = "ADDPOSTERINGLOC";
            startFusedLocationService();
            spCrop = (SearchableSpinner) dialog.findViewById(R.id.spCropType);
            spProduct = (SearchableSpinner) dialog.findViewById(R.id.spProductName);
            etTotalPosters = (EditText) dialog.findViewById(R.id.etTotalPosters);
            etTotalSpots = (TextView) dialog.findViewById(R.id.etTotalSpots);
            imgBtnGps = (ImageView) dialog.findViewById(R.id.imgBtnGps);
            tvCordinatesPopup = (TextView) dialog.findViewById(R.id.tvCordinatesPopup);
            tvAddressPopup = (TextView) dialog.findViewById(R.id.tvAddressPopup);
            btnSpotPhoto = (Button) dialog.findViewById(R.id.btnSpotPhoto);
            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);
            ivImage = (ImageView) dialog.findViewById(R.id.ivImage);
            ivImage.setImageDrawable(null);

            etTotalSpots.setText(Integer.toString(getPopupDataCount()));

            onSaveBtnClicked();
            btnSpotPhoto.setOnClickListener(this);
           /* btnSpotPhoto.setOnClickListener(new View.OnClickListener() {
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

            bindcroptype(spCrop, "C");
            spCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                        croptypePopup = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (Exception ex) {
                        Toast.makeText(ATLPosteringActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                    bindProductName(spProduct, croptypePopup);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


            spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onSaveBtnClicked() {
        btnSaveClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ////////////////
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());

                totalPosters = etTotalPosters.getText().toString();
                totalSpots = etTotalSpots.getText().toString();
                croptypePopup = spCrop.getSelectedItem().toString();
                productNamePopup = spProduct.getSelectedItem().toString();
                strTotalPosters = etTotalPosters.getText().toString();
                /////////////
                if (dailogValidation()) {

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Do you want to close the dailog ?");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface alertdialog, int which) {

                            hasAddedPosteringSpot = true;
                            mPref.save("isSubmitClickedPostering", "false");
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

                }

            }
        });

    }

    private boolean dailogValidation() {

        if (spCrop.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select crop type");
            return false;
        }

        if (spProduct.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select product name");
            return false;
        }

        if (etTotalPosters.getText().toString().length() == 0) {
            msclass.showMessage("Please enter no. of posters");
            return false;
        }
        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please click spot poster image");
            return false;
        }
        return true;
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
    }

    private void bindProductName(Spinner spProductName, String croptype) {
        //st
        try {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * <P>Method is used to save the postering spots to local DB </P>
     */
    private void saveDetailsToDb() {

        ///// Popup data
        String taggedAddressPopup = "";
        String taggedCordinatesPopup = "";

        if (tvAddressPopup.getText().toString().isEmpty() || tvAddressPopup.getText().toString().equals("")) {
            taggedAddressPopup = "";

        } else {
            taggedAddressPopup = tvAddressPopup.getText().toString();
            tvAddressEndLocation.setText(taggedAddressPopup);
            taggedAddressMandiNameEnd = tvAddressEndLocation.getText().toString();
        }

        if (!cordinates.isEmpty()) {
            taggedCordinatesPopup =cordinates;// addLocCordinates;
            endCordinates = cordinates;//addLocCordinates;
            taggedCordinatesMandiNameEnd = cordinates;//addLocCordinates;

        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        String isSynced = "0";
        String activityImgStatus = "0";
        Date entrydate = new Date();
        final String activityImgPath;
        activityImgPath = Imagepath1;
        final String activityImgName =AppConstant.Imagename;// "ATLVillagePosteringData" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());

        boolean fl = mDatabase.insertATLVillagePosteringDetails(userCode, croptypePopup, productNamePopup,
                taggedCordinatesPopup + " " + taggedAddressPopup, taggedCordinatesPopup,
                activityImgName, activityImgPath, activityImgStatus, strTotalPosters, totalSpots, isSynced);

        if (fl) {

            etTotalPosters.setText("");
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
    //////////////////////////////
    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveToDb("SUBMITEVENT");
                       // uploadData("ATLVillagePosteringData");
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

    public void saveToDb(String buttonEvent) {

        String focussedVillage = "";
        String state = "";
        String district = "";
        String taluka = "";
        String othervillage = "";
        String numberOfSpots = "";
        String villagecode="";
        if (radFocusedActivity.isChecked()) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();
            villagecode=config.getvalue(spFocusedVillages);
        }

        if (radOtherActivity.isChecked() || radMandiPostering.isChecked() || radRoadsidePostering.isChecked()) {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            othervillage = spVillage.getSelectedItem().toString();
            villagecode=config.getvalue(spVillage);
        }

        ///// Popup data
        String taggedAddress = "";
        String taggedCordinates = "";
        if (radVillagePostering.isChecked()) {
            if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {
                taggedAddress = "";

            } else {
                taggedAddress = tvAddress.getText().toString();
            }
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
            numberOfSpots = etNumberOfSpots.getText().toString();
        }
        if (radMandiPostering.isChecked()) {
            strMandiName = etMandiName.getText().toString();
        }

        String taggedAddressMandiNameStart = "";
        String taggedCordinatesMandiNameStart = "";


        if (radRoadsidePostering.isChecked()) {

            //for start location tag
            if (tvAddressStartLocation.getText().toString().isEmpty() || tvAddressStartLocation.getText().toString().equals("")) {
                taggedAddressMandiNameStart = "";

            } else {
                taggedAddressMandiNameStart = tvAddressStartLocation.getText().toString();
            }

            if (!cordinates.isEmpty()) {
                taggedCordinatesMandiNameStart = cordinates;//startCordinates;
            } else {
                Utility.showAlertDialog("", "Please Wait for location", context);
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                return;
            }

            //for end location tag
            if (tvAddressEndLocation.getText().toString().isEmpty() || tvAddressEndLocation.getText().toString().equals("")) {
                taggedAddressMandiNameEnd = "";
            } else {
                taggedAddressMandiNameEnd = tvAddressEndLocation.getText().toString();
            }

            if (!cordinates.isEmpty()) {
                taggedCordinatesMandiNameEnd =cordinates;// endCordinates;
            } else {
                Utility.showAlertDialog("", "Please Wait for location", context);
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                return;
            }
        }
        String finalPopupJson = getPopupData();

        String activityImgStatus = "0";

        Log.d("LocationDatasaveToDb", cordinates);
        String isSynced = "0";
        Date entrydate = new Date();
        final String activityImgPath;
        activityImgPath = Imagepath1;
        final String activityImgName =AppConstant.Imagename;// "ATLVillagePosteringData" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());

        //Star
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);
        String searchQuery = "";
        searchQuery = "select  * from ATLVillagePosteringData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        String  updatestring ="";
        if (count > 0) {

            if (buttonEvent.equals("SAVEEVENT"))
            {
                updatestring="update  ATLVillagePosteringData set finalPopupJson='"+finalPopupJson+"' " +
                        " where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit=0 ";
                mDatabase.Updatedata(updatestring);
                msclass.showMessage("Data saved  successfully.");
            }
            if (buttonEvent.equals("SUBMITEVENT"))
            {
                // After submit delete  wallpainging details data
                updatestring="update  ATLVillagePosteringData set finalPopupJson='"+finalPopupJson+"' " +
                        ",FinalSubmit=1,taggedAddressMandiNameEnd='"+taggedAddressMandiNameEnd+"'," +
                        "taggedCordinatesMandiNameEnd='"+taggedCordinatesMandiNameEnd+"'" +
                        " where strftime('%Y-%m-%d', EntryDt)='"+strdate+"' " +
                        " and FinalSubmit=0 ";
                mDatabase.Updatedata(updatestring);
                mDatabase.deleterecord("delete from ATLVillagePosteringDetails");
                msclass.showMessage("Data submitted successfully.");
                 uploadData("ATLVillagePosteringData");

            }
            // Check submit button and Save button Event
            //update  finalPopupJson coloum   base entry and finalsubmit coloum
        }
        else {
            boolean fl = mDatabase.insertATLVillagePosteringData(userCode, selectedPosteringType, focussedVillage, state, district, taluka, othervillage,
                    strMandiName, numberOfSpots, taggedCordinates + " " + taggedAddress, taggedCordinates, taggedCordinatesMandiNameStart + " " + taggedAddressMandiNameStart, taggedCordinatesMandiNameStart,
                    taggedCordinatesMandiNameEnd + " " + taggedAddressMandiNameEnd, taggedCordinatesMandiNameEnd, finalPopupJson,villagecode);
            if (fl) {

                if (CommonUtil.addGTVActivity(context, "23", "Postering", cordinates, selectedPosteringType+" Spots-"+numberOfSpots,"GTV","0")) {
                    // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                }

                if(calllisner==0)  // Intial Click on Add location
                {
                    checkForLocalStorage();

                }
                else {
                    msclass.showMessage("Data saved successfully.");
                    checkForLocalStorage();
                }


                // uploadData("ATLVillageWallPaintingData");

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

        String searchQuery = "select  *  from ATLVillagePosteringDetails " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and   isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
        }
        return jsonArray.toString();

    }

    private int getPopupDataCount() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);

        String searchQuery = "select  *  from ATLVillagePosteringDetails where" +
                " strftime( '%Y-%m-%d', EntryDt)='"+strdate+"'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();

        return count;

    }

    public void uploadData(String ATLVillagePosteringData) {
        String str = null;
       /* if (config.NetworkConnection()) {

            try {
                new ATLPosteringActivity.UploadATLVillagePosteringDataServer(ATLVillagePosteringData, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
        */
       {
            AlertDialog.Builder builder = new AlertDialog.Builder(ATLPosteringActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    mPref.save("isSubmitClickedPostering", "true");
                    Config.refreshActivity(ATLPosteringActivity.this);
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

    public String uploadATLVillagePosteringData(String atlVillagePosteringData) {
        String str = "";
        int action = 1;


        String searchQuery = "select * from ATLVillagePosteringData";

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

                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("ATLVillagePosteringData", jsonObject.toString());
                    str = syncATLVillagePosteringDataSingleImage(atlVillagePosteringData, SERVER, jsonObject);
                    handleATLVillagePosteringDataImageSyncResponse("ATLVillagePosteringData", str,id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }
//AsyncTask Class for api batch code upload call

    private class UploadATLVillagePosteringDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadATLVillagePosteringDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadATLVillagePosteringData("ATLVillagePosteringData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mPref.save("isSubmitClickedPostering", "true");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ATLPosteringActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mPref.save("isSubmitClickedPostering", "true");
                                Config.refreshActivity(ATLPosteringActivity.this);
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
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ATLPosteringActivity.this);
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ATLPosteringActivity.this);
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

    public synchronized String syncATLVillagePosteringDataSingleImage(String function, String urls, JSONObject jsonObject
    ) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleATLVillagePosteringDataImageSyncResponse(String function, String resultout,String id) throws JSONException {
        if (function.equals("ATLVillagePosteringData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateATLVillagePosteringData("0", "1", id);

                } else {

                }
            }
        }

        Log.d("ATLVillagePosteringData", "ATLVillagePosteringData: " + resultout);
    }

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

    //bind Village to spinner

    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            vilageList = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster " +
                    "where upper(taluka)='" + taluka.toUpperCase()+ "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            vilageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));


            cursor = mDatabase.getReadableDatabase().

                    rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                vilageList.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, vilageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);

            String submitClicked = mPref.getString("isSubmitClickedPostering", "");
            //if (submitClicked.equalsIgnoreCase("false")) {
               // checkForLocalStorage();
            //}

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }

    // bind focussed villages

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
                        "SELECT FOCUSED VILLAGE"));Croplist.add(new GeneralMaster(vcode, vname));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);
            } else {
                String searchQuery = "";
                focussedVillageList = new ArrayList<GeneralMaster>();
                Cursor cursor;
                searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
                focussedVillageList.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                        "SELECT FOCUSED VILLAGE"));

                cursor = mDatabase.getReadableDatabase().
                        rawQuery(searchQuery, null);
                cursor.moveToFirst();

                while (cursor.isAfterLast() == false) {
                    focussedVillageList.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();
//            focussedVillageList.add(new
//                    GeneralMaster("OTHER",
//                    "OTHER"));
                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, focussedVillageList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);

                // String submitClicked = mPref.getString("isSubmitClickedPostering", "");
                //if (submitClicked.equalsIgnoreCase("false")) {
                // checkForLocalStorage();
                //}
            }

        } catch (
                Exception ex) {
            ex.printStackTrace();

        }
    }

    //    Validation for all fields
    public boolean validation() {

        //validation for  Radio Button Is Checked
        checkdetailentrydaved= getPopupDataCount();

        if (radVillagePostering.isChecked()) {

            return checkForVillageRadioBtn();

        } else if (radMandiPostering.isChecked()) {

            return checkForMandiRadioBtn();

        } else if (radRoadsidePostering.isChecked()) {

            return checkForRoadsideRadioBtn();
        }

        return true;
    }

    private boolean checkForRoadsideRadioBtn() {

        if (!checkForRoadsideRadioBtnSpot()) {
            return false;
        }

        if (checkdetailentrydaved==0) {
            msclass.showMessage("Please add postering spot details");
            return false;
        }

        return true;
    }

    private boolean checkForMandiRadioBtn() {

        if (!checkForMandiRadioBtnSpot()) {
            return false;
        }

        //check for postering spot
        if (checkdetailentrydaved==0) {
            msclass.showMessage("Please add postering spot details");
            return false;
        }

        return true;
    }


    private Boolean checkForVillageRadioBtn() {

        if (!checkForVillageRadioBtnSpot()) {
            return false;
        }

        //check for postering spot
        if (checkdetailentrydaved==0) {
            msclass.showMessage("Please add postering spot details");
            return false;
        }
//        if (etNumberOfSpots.getText().length() == 0) {
//            msclass.showMessage("Please enter no. of spots");
//            return false;
//        }

        return true;
    }

    public boolean validationAddPostering() {

        //validation for  Radio Button Is Checked
        if (radVillagePostering.isChecked()) {

            return checkForVillageRadioBtnSpot();

        } else if (radMandiPostering.isChecked()) {

            return checkForMandiRadioBtnSpot();

        } else if (radRoadsidePostering.isChecked()) {

            return checkForRoadsideRadioBtnSpot();
        }

        return true;
    }

    private boolean checkForRoadsideRadioBtnSpot() {

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
        return true;
    }

    private boolean checkForMandiRadioBtnSpot() {

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
        if (etMandiName.getText().length() == 0) {
            msclass.showMessage("Please enter Mandi Name");
            return false;
        }
        return true;
    }


    private Boolean checkForVillageRadioBtnSpot() {

        if (radFocusedActivity.isChecked()) {
            if (spFocusedVillages.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select Focused Village");
                return false;
            }
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
                msclass.showMessage("Please Select Village");
                return false;
            }
        }

        return true;
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
            tvAddress.setText(addressAddloc + "\n" + cordinates);
            tvAddressEndLocation.setText(addressEnd + "\n" + cordinates);
           // tvAddressStartLocation.setText(address + "\n" + cordinates);

            if (startlocationflag)  // false as start entry  not saved
            {
                tvAddressEndLocation.setText(addressEnd + "\n" + cordinates);
                addressEnd = getCompleteEndAddressString(lati, longi);
                tvAddressEndLocation.setText(addressEnd + "\n" + cordinates);

            }
            else
            {
                addressEnd = "";
                tvAddressEndLocation.setText("");
                tvAddressStartLocation.setText(addressStart + "\n" + cordinates);
                addressStart = getCompleteStartAddressString(lati, longi);
                tvAddressStartLocation.setText(addressStart + "\n" + cordinates);
            }



            switch (isTagLocation) {
                case "FIRSTTIME":

                    Log.d(TAG, "onLocationChanged: " + String.valueOf(longi));
                    address = getCompleteAddressString(lati, longi);

                    if (!isfromAddSpot) {
                        addressStart = getCompleteStartAddressString(lati, longi);
                        addressEnd = getCompleteEndAddressString(lati, longi);

                    }
                    //tvAddressStartLocation.setText(addressStart + "\n"+cordinates);
                    Log.d(TAG, "onlocation" + cordinates);
                    break;
                case "ADDPOSTERINGLOC":
                    latiAddloc = arg0.getLatitude();
                    longiAddloc = arg0.getLongitude();
                    location = arg0;
                    Log.d(TAG, "onLocationChanged: " + String.valueOf(longiAddloc));
                    addressAddloc = getCompleteAddlocAddressString(latiAddloc, longiAddloc);
                    tvAddressPopup.setText(addressAddloc + "\n"+cordinates);
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
     * <P>fetch address from cordinates of start tag location</P>
     *
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */
    private String getCompleteStartAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                tvAddressStartLocation.setText(address + "\n"+startCordinates);
                startCordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);

            }
            tvAddressStartLocation.setText(address + "\n"+startCordinates);

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
                tvAddressEndLocation.setText(addressEnd + "\n"+endCordinates);
                endCordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);
            }
            tvAddressEndLocation.setText(addressEnd + "\n"+endCordinates);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
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
                tvAddressPopup.setText(addressAddloc + "\n"+addLocCordinates);
                addLocCordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);


            }
            tvAddressPopup.setText(addressAddloc + "\n"+addLocCordinates);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpotPhoto:
                imageselect=1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
           /* case R.id.btnPhoto2:
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
           /* if (imageselect == 2)
            {
                ivImagePhoto2.setImageBitmap(r.getBitmap());
                if (ivImagePhoto2.getDrawable() != null) {
                    ivImagePhoto2.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImagePhoto2.setVisibility(View.GONE);
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
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
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

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                tvAddress.setText(address +"\n"+ cordinates);
                cordinates = String.valueOf(LATITUDE) + "-" + String.valueOf(LONGITUDE);

            }
            tvAddress.setText(address +"\n"+ cordinates);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ATLPosteringActivity.this);

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
                    Log.i(" ", photoFile.getAbsolutePath());
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
            ivImage.setVisibility(View.VISIBLE);
            ivImage.setImageBitmap(bm);
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
                    AppConstant.Imagename="Crop"+this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    FileUtilImage.compressImageFile( AppConstant.queryImageUrl, AppConstant.imageUri,
                            this,AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImage.setImageBitmap(myBitmap);
                    ivImage.setVisibility(View.VISIBLE);

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }

    /////////
    private void checkStorage() {

        String searchQuery = "select  *  from ATLVillagePosteringData";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
            try {
                String localJson = jsonArray.getJSONObject(jsonArray.length() - 1).toString();
                setRadioButton(localJson);
                Log.d("LocalPosteringData", localJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkForLocalStorage() {
        try
        {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String strdate=dateFormat.format(d);

        String searchQuery = "select  *  from ATLVillagePosteringData ";
        //String searchQuery = "";
        searchQuery = "select  * from ATLVillagePosteringData  " +
                "where strftime( '%Y-%m-%d', EntryDt)='"+strdate+"' and FinalSubmit='0' " +
                "order by  EntryDt desc  limit 1";
       Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            calllisner =  mDatabase.checkentryexist(searchQuery);// cursor.getCount();

            jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
            try {
                String localJson = jsonArray.getJSONObject(jsonArray.length() - 1).toString();
                setRadioButton(localJson);
                setStoredValues(localJson);
                Log.d("LocalPosteringData", localJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        } catch (Exception e) {

        }
    }

    private void setRadioButton(String localJson) {
        try {
            JSONObject jsonObject = new JSONObject(localJson);
            selectedPosteringType = jsonObject.getString("selectedPosteringType");


            switch (selectedPosteringType) {
                case "VILLAGE POSTERING":
                    radVillagePostering.setChecked(true);
                    if (jsonObject.getString("focussedVillage").isEmpty()) {
                        radOtherActivity.setChecked(true);
                    } else {
                        radFocusedActivity.setChecked(true);
                        radFocusedActivity.setEnabled(false);
                        radOtherActivity.setEnabled(false);
                        radMandiPostering.setEnabled(false);
                        radRoadsidePostering.setEnabled(false);
                        radVillagePostering.setEnabled(false);
                        spFocusedVillages.setEnabled(false);
                        //for focusced village list
                        for (int k = 0; k < focussedVillageList.size(); k++) {
                            if (jsonObject.getString("focussedVillage").equalsIgnoreCase(focussedVillageList.get(k).toString())) {
                                spFocusedVillages.setSelection(k);
                            }
                        }
                    }
                    break;
                case "MANDI / MARKET POSTERING":
                    radMandiPostering.setChecked(true);
                    //   for state selection
                    if (stateList != null) {

                        for (int i = 0; i < stateList.size(); i++) {
                            if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                                state = stateList.get(i).toString();
                                spState.setSelection(i);
                            }
                        }
                    }
                    //for dist selection
                    if (distList != null) {
                        for (int j = 0; j < distList.size(); j++) {
                            if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                                spDist.setSelection(j);
                            }
                        }
                    }
                    //for taluka selection
                    if (talukaList != null) {

                        for (int k = 0; k < talukaList.size(); k++) {
                            if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                                spTaluka.setSelection(k);
                            }
                        }
                    }

                    break;
                case "ROADSIDE POSTERING":
                    radRoadsidePostering.setChecked(true);
                    //for state selection
                    if (stateList != null) {

                        for (int i = 0; i < stateList.size(); i++) {
                            if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                                state = stateList.get(i).toString();
                                spState.setSelection(i);
                            }
                        }
                    }
                    //for dist selection
                    if (distList != null) {
                        for (int j = 0; j < distList.size(); j++) {
                            if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                                spDist.setSelection(j);
                            }
                        }
                    }
                    //for taluka selection
                    if (talukaList != null) {

                        for (int k = 0; k < talukaList.size(); k++) {
                            if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                                spTaluka.setSelection(k);
                            }
                        }
                    }
                    break;
                default:
                    Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStoredValues(String localJson) {
        try {
            JSONObject jsonObject = new JSONObject(localJson);
            selectedPosteringType = jsonObject.getString("selectedPosteringType");



            bindDist(jsonObject.getString("state"));
            bindTaluka(jsonObject.getString("district"));
            bindVillage(jsonObject.getString("taluka"));

            switch (selectedPosteringType) {
                case "VILLAGE POSTERING":
                    if (jsonObject.getString("focussedVillage").isEmpty()) {

                        spState.setEnabled(false);
                        spDist.setEnabled(false);
                        spTaluka.setEnabled(false);
                        spVillage.setEnabled(false);
                        radFocusedActivity.setEnabled(false);
                        radOtherActivity.setEnabled(false);
                        radMandiPostering.setEnabled(false);
                        radRoadsidePostering.setEnabled(false);
                        radVillagePostering.setEnabled(false);

//for state selection
                        for (int i = 0; i < stateList.size(); i++) {
                            if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                                state = stateList.get(i).toString();
                                spState.setSelection(i);
                                //spState.setSelection(i,false);
                            }
                        }
//for dist selection
                        if (distList != null) {
                            for (int j = 0; j < distList.size(); j++) {
                                if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                                    spDist.setSelection(j);
                                }
                            }
                        }
//for taluka selection
                        if (talukaList != null) {
                            for (int k = 0; k < talukaList.size(); k++) {
                                if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                                    spTaluka.setSelection(k);
                                }
                            }
                        }
//for village selection
                        if (vilageList != null) {
                            for (int k = 0; k < vilageList.size(); k++) {
                                if (jsonObject.getString("othervillage").equalsIgnoreCase(vilageList.get(k).toString())) {
                                    spVillage.setSelection(k);
                                }
                            }
                        }

                    } else {
                        spFocusedVillages.setEnabled(false);
                        radFocusedActivity.setEnabled(false);
                        radOtherActivity.setEnabled(false);
                        radMandiPostering.setEnabled(false);
                        radRoadsidePostering.setEnabled(false);
                        radVillagePostering.setEnabled(false);
//for focusced village list
                        for (int k = 0; k < focussedVillageList.size(); k++) {
                            if (jsonObject.getString("focussedVillage").equalsIgnoreCase(focussedVillageList.get(k).toString())) {
                                spFocusedVillages.setSelection(k);
                            }
                        }
                    }

                    break;
                case "MANDI / MARKET POSTERING":
                    etMandiName.setText(jsonObject.getString("strMandiName"));
                    spState.setEnabled(false);
                    spDist.setEnabled(false);
                    spTaluka.setEnabled(false);
                    radFocusedActivity.setEnabled(false);
                    radOtherActivity.setEnabled(false);
                    radMandiPostering.setEnabled(false);
                    radRoadsidePostering.setEnabled(false);
                    radVillagePostering.setEnabled(false);
                    etMandiName.setEnabled(false);
                    //for state selection
                    if (stateList != null) {

                        for (int i = 0; i < stateList.size(); i++) {
                            if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                                state = stateList.get(i).toString();
                                spState.setSelection(i);
                            }
                        }
                    }
                    //for dist selection
                    if (distList != null) {
                        for (int j = 0; j < distList.size(); j++) {
                            if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                                spDist.setSelection(j);
                            }
                        }
                    }
                    //for taluka selection
                    if (talukaList != null) {

                        for (int k = 0; k < talukaList.size(); k++) {
                            if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                                spTaluka.setSelection(k);
                            }
                        }
                    }

                    break;
                case "ROADSIDE POSTERING":
                    //for state selection
                    spState.setEnabled(false);
                    spDist.setEnabled(false);
                    spTaluka.setEnabled(false);
                    radFocusedActivity.setEnabled(false);
                    radOtherActivity.setEnabled(false);
                    radMandiPostering.setEnabled(false);
                    radRoadsidePostering.setEnabled(false);
                    radVillagePostering.setEnabled(false);
                    if (stateList != null) {

                        for (int i = 0; i < stateList.size(); i++) {
                            if (jsonObject.getString("state").equalsIgnoreCase(stateList.get(i).toString())) {
                                state = stateList.get(i).toString();
                                spState.setSelection(i);
                            }
                        }
                    }
                    //for dist selection
                    if (distList != null) {
                        for (int j = 0; j < distList.size(); j++) {
                            if (jsonObject.getString("district").equalsIgnoreCase(distList.get(j).toString())) {
                                spDist.setSelection(j);
                            }
                        }
                    }
                    //for taluka selection
                    if (talukaList != null) {

                        for (int k = 0; k < talukaList.size(); k++) {
                            if (jsonObject.getString("taluka").equalsIgnoreCase(talukaList.get(k).toString())) {
                                spTaluka.setSelection(k);
                            }
                        }
                    }
                    addressStart = jsonObject.getString("taggedAddressMandiNameStart");

                    tvAddressStartLocation.setText(addressStart);
                    if (!addressStart.isEmpty()) {
                        startlocationflag = true;
                    }
                    break;
                default:
                    Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
            }



        } catch (JSONException e) {
            e.printStackTrace();


        }
    }
}
