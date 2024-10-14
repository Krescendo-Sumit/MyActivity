package myactvity.mahyco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
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


import com.google.android.material.textfield.TextInputLayout;
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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
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

public class ActivityVOFP extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback,
        IPickResult,View.OnClickListener{


    private static final String TAG = "VOFP";
    private static final String IMAGE_DIRECTORY_NAME = "PHOTO";
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    public String Imagepath1 = "";
    public String cordinates = "";
    SearchableSpinner spState, spDist, spTaluka, spVillage, spFarmer, spOptions, spYield, spGermination, spAppearance, spPacking,
            spPrice, spTalk, spExplain, spClarifies, spListens, spShareCont, spOften, spRecommend, spRateOverallBrand, spRateStaff, spRateProduct, spFarmerSearch;
    MultiSelectionSpinner spVegeGrown, spCompany, spBrand;
    String[] categoryArray, optionsArray, companyArray, ratingArray, ratingArray1, ratingArray2;
    Button btnSubmit, btnActivityPhoto, btnSearch;
    CardView cvRating, cvFeedback;
    TextView lblheader, tvCordinates, tvAddress,tvVillage, tvTaluka,tvDist,tvState,myTextProgress;
    String cordinatesmsg = "TAG THE PLOT (2ND ROW INSIDE THE PLOT)* \n";
    String address="";
    EditText etSearchText, etName, etPhone, etAcres, etComment, etOther, etOther1, etOther2, etOtherCompany,etOtherCompany1,etSuggestion, etNotAttend;

    //  String SERVER = "https://packhouse.mahyco.com/api/preseason/sanmanMela";
    String userCode;
    Config config;
    LinearLayout llOtherVillages, llFocussedVillages, llYESReceived, LinearLayout02;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    RadioGroup radGroupActivity, radGroupActivity1, radGroupActivity2, radGroupActivity3, radGroupActivity4, radGroupActivity5;
    RadioButton radYesActivity, radNoActivity, radOtherActivity,
            radYesActivity5, radNoActivity5,
            radYesActivity4, radNoActivity4,
            radYesActivity3, radNoActivity3,
            radYesActivity2, radNoActivity2, radOtherActivity2,
            radYesActivity1, radNoActivity1, radOtherActivity1, radNotAttendActivity, radSuggestionActivity;
    String strReceiveInvite = "", strAttendSeminar = "", strIsEventUseful = "", strEasyToBuySeeds = "", strSeedsAvaliability = "",
            strEasySeedFeatures = "";
    TextInputLayout tiComments, tiAcres, tiPhone, tiName, tiEmail,tiOther,tiOtherCompany;
    LinearLayout llCompanyPro, llNo, llYes;
    TextView tvError,tvErrorAttend,tvAttend,tvReceive,tvNotAttend,tvUsefulEvent;
    int count = 0;
    Prefs mPref;
    int imageselect;
    File photoFile = null;
    ImageView ivImage;
    boolean IsGPSEnabled = false;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    double lati;
    double longi;
    Location location;
    ImageView imgBtnGps;
    private Context context;
    private String state, dist, taluka;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private long mLastClickTime = 0;
    private Handler handler = new Handler();
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    /**
     * <P>Method to get the current date time</P>
     *
     * @return
     */
    public static String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        try {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
          /* Date d=new Date();
            String strdate=dateFormat.format(d);

            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                today = Calendar.getInstance().getTime();
            }*/
        }
        catch (Exception ex)
        {
            Log.d(TAG, "getCurrentDate: "+ex.toString());
        }
        return dateFormat.format(today);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vofp);
        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPref = Prefs.with(this);
        config = new Config(this); //Here the context is passing
        context = this;
        // userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode =  pref.getString("UserID", null);
        mPref.save(AppConstant.USER_CODE_TAG, userCode);
        msclass = new Messageclass(this);
        mDatabase = SqliteDatabase.getInstance(this);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        btnActivityPhoto = (Button) findViewById(R.id.btnActivityPhoto);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        myTextProgress = (TextView) findViewById(R.id.myTextProgress);
        tvErrorAttend = (TextView) findViewById(R.id.tvErrorAttend);
        tvAttend = (TextView) findViewById(R.id.tvAttend);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvReceive = (TextView) findViewById(R.id.tvReceive);
        tvUsefulEvent = (TextView) findViewById(R.id.tvUsefulEvent);
        tvNotAttend = (TextView) findViewById(R.id.tvNotAttend);
        tvState = (TextView) findViewById(R.id.tvState);
        tvDist = (TextView) findViewById(R.id.tvDist);
        tvTaluka = (TextView) findViewById(R.id.tvTaluka);
        tvVillage = (TextView) findViewById(R.id.tvVillage);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spFarmer = (SearchableSpinner) findViewById(R.id.spFarmer);
        spOptions = (SearchableSpinner) findViewById(R.id.spOptions);
        spPrice = (SearchableSpinner) findViewById(R.id.spPrice);
        spPacking = (SearchableSpinner) findViewById(R.id.spPacking);
        spAppearance = (SearchableSpinner) findViewById(R.id.spAppearance);
        spGermination = (SearchableSpinner) findViewById(R.id.spGermination);
        spYield = (SearchableSpinner) findViewById(R.id.spYield);
        spTalk = (SearchableSpinner) findViewById(R.id.spTalk);
        spExplain = (SearchableSpinner) findViewById(R.id.spExplain);
        spClarifies = (SearchableSpinner) findViewById(R.id.spClarifies);
        spListens = (SearchableSpinner) findViewById(R.id.spListens);
        spShareCont = (SearchableSpinner) findViewById(R.id.spShareCont);
        spOften = (SearchableSpinner) findViewById(R.id.spOften);
        spRecommend = (SearchableSpinner) findViewById(R.id.spRecommend);
        spRateProduct = (SearchableSpinner) findViewById(R.id.spRateProduct);
        spRateStaff = (SearchableSpinner) findViewById(R.id.spRateStaff);
        spRateOverallBrand = (SearchableSpinner) findViewById(R.id.spRateOverallBrand);
        spFarmerSearch = (SearchableSpinner) findViewById(R.id.spFarmerSearch);
        spVegeGrown = (MultiSelectionSpinner) findViewById(R.id.spVegeGrown);
        spBrand = (MultiSelectionSpinner) findViewById(R.id.spBrand);
        spCompany = (MultiSelectionSpinner) findViewById(R.id.spCompany);
        llYESReceived = findViewById(R.id.llYESReceived);
        LinearLayout02 = findViewById(R.id.LinearLayout02);
        cvFeedback = findViewById(R.id.cvFeedback);
        cvRating = findViewById(R.id.cvRating);
        radGroupActivity = findViewById(R.id.radGroupActivity);
        radGroupActivity1 = findViewById(R.id.radGroupActivity1);
        radGroupActivity2 = findViewById(R.id.radGroupActivity2);
        radGroupActivity3 = findViewById(R.id.radGroupActivity3);
        radGroupActivity4 = findViewById(R.id.radGroupActivity4);
        radGroupActivity5 = findViewById(R.id.radGroupActivity5);

        radYesActivity = findViewById(R.id.radYesActivity);
        radYesActivity1 = findViewById(R.id.radYesActivity1);
        radYesActivity2 = findViewById(R.id.radYesActivity2);
        radYesActivity3 = findViewById(R.id.radYesActivity3);
        radYesActivity4 = findViewById(R.id.radYesActivity4);
        radYesActivity5 = findViewById(R.id.radYesActivity5);

        radNoActivity = findViewById(R.id.radNoActivity);
        radNoActivity1 = findViewById(R.id.radNoActivity1);
        radNoActivity2 = findViewById(R.id.radNoActivity2);
        radNoActivity3 = findViewById(R.id.radNoActivity3);
        radNoActivity4 = findViewById(R.id.radNoActivity4);
        radNoActivity5 = findViewById(R.id.radNoActivity5);

        radOtherActivity = findViewById(R.id.radOtherActivity);
        radOtherActivity1 = findViewById(R.id.radOtherActivity1);
        radOtherActivity2 = findViewById(R.id.radOtherActivity2);
        radNotAttendActivity = findViewById(R.id.radNotAttendActivity);
        radSuggestionActivity = findViewById(R.id.radSuggestionActivity);


        etSearchText = findViewById(R.id.etSearchText);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAcres = findViewById(R.id.etAcres);
        etComment = findViewById(R.id.etComment);

        etOther = findViewById(R.id.etOther);
        etOther1 = findViewById(R.id.etOther1);
        etOther2 = findViewById(R.id.etOther2);
        etOtherCompany = findViewById(R.id.etOtherCompany);
        etOtherCompany1 = findViewById(R.id.etOtherCompany1);
        etSuggestion = findViewById(R.id.etSuggestion);
        etNotAttend = findViewById(R.id.etNotAttend);

        tiOther = findViewById(R.id.tiOther);
        tiOtherCompany = findViewById(R.id.tiOtherCompany);
        tiEmail = findViewById(R.id.tiEmail);
        tiName = findViewById(R.id.tiName);
        tiPhone = findViewById(R.id.tiPhone);
        tiAcres = findViewById(R.id.tiAcres);
        tiComments = findViewById(R.id.tiComments);
        tvError = findViewById(R.id.tvError);

        llCompanyPro = findViewById(R.id.llCompanyPro);
        llNo = findViewById(R.id.llNo);
        llYes = findViewById(R.id.llYes);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);



        // bindState();
        bindFarmerDetail(null);
        bindFarmerCategory();
        bindOptions();
        bindCompany();
        bindVegetableGrown("C");
        bindRating();
        bindBrand();

        onSearchBtn();

        spFarmerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    if(parent.getSelectedItemPosition()!=0 && parent.getSelectedItemPosition()!=1) {
                        // set the UI visibility
                        GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();

                        getSearchData(new JSONObject(gm.Code()));

                        setUIVisibility();


                    }else if(parent.getSelectedItemPosition()==1) {

                        Intent intent = new Intent(ActivityVOFP.this, ActivityKisanClub.class);
                        startActivity(intent);

                        setUIVisibilityGone();

                    }else {

                        setUIVisibilityGone();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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

        spOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String option = (String) parent.getSelectedItem();

                    if (option.equals("No")) {
                        llCompanyPro.setVisibility(View.VISIBLE);
                    } else {
                        llCompanyPro.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spVegeGrown.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {


            }

            @Override
            public void selectedStrings(List<String> strings) {
                count = strings.size();
                if (count > 5) {
                    //  tvError.setVisibility(View.VISIBLE);
                    msclass.showMessage("Max crop selection limit is 5");
                } else {
                    //tvError.setVisibility(View.GONE);
                }
            }
        });

        spCompany.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {

                if(strings.size()>0) {
                    for (int i = 0; i < strings.size(); i++) {

                        if (strings.get(i).contains("Other")) {

                            // visible other input field

                            tiOtherCompany.setVisibility(View.VISIBLE);
                            etOtherCompany1.setText("");

                        }else {
                            tiOtherCompany.setVisibility(View.GONE);
                        }
                    }
                }

            }
        });
        spBrand.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {

                if(strings.size()>0) {
                    for (int i = 0; i < strings.size(); i++) {

                        if (strings.get(i).contains("Other")) {

                            // visible other input field
                            tiOther.setVisibility(View.VISIBLE);




                        }else {
                            tiOther.setVisibility(View.GONE);
                            etOtherCompany.setText("");
                        }
                    }
                }
            }
        });

        btnActivityPhoto.setOnClickListener(this);
       /* btnActivityPhoto.setOnClickListener(new View.OnClickListener() {
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


        onTouchListener();

        onSubmitBtnClicked();

        manageRadioBtnSelection();

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
    private void setUIVisibilityGone() {
        LinearLayout02.setVisibility(View.GONE);
        cvFeedback.setVisibility(View.GONE);

        cvRating.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
    }

    private void setUIVisibility() {
        LinearLayout02.setVisibility(View.VISIBLE);
        cvFeedback.setVisibility(View.VISIBLE);

        cvRating.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void onTouchListener() {

        etOther.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                radOtherActivity.setChecked(true);
                return false;
            }
        });
        etOther1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                radOtherActivity1.setChecked(true);
                return false;
            }
        });
        etOther2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                radSuggestionActivity.setChecked(false);
                radOtherActivity2.setChecked(true);
                radNoActivity2.setChecked(false);
                radYesActivity2.setChecked(false);
                return false;
            }
        });
        etSuggestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                radSuggestionActivity.setChecked(true);
                radOtherActivity2.setChecked(false);
                radNoActivity2.setChecked(false);
                radYesActivity2.setChecked(false);

                return false;
            }
        });
    }

    private void radioGroupActivity2Unchecked(Boolean val1, Boolean val2) {
        radSuggestionActivity.setChecked(val1);
        radOtherActivity2.setChecked(val2);
        radNoActivity2.setChecked(false);
        radYesActivity2.setChecked(false);
    }

    private void manageRadioBtnSelection() {

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity:
                        strAttendSeminar = radYesActivity.getText().toString();
                        llNo.setVisibility(View.GONE);
                        llYes.setVisibility(View.VISIBLE);
                        etOther.setText("");
                        etOther.setFocusable(false);
                        etOther.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.GONE);

                        break;

                    case R.id.radNoActivity:
                        strAttendSeminar = radNoActivity.getText().toString();
                        llNo.setVisibility(View.VISIBLE);
                        llYes.setVisibility(View.GONE);
                        etOther.setText("");
                        etOther.setFocusable(false);
                        etOther.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.GONE);

                        break;
                    case R.id.radOtherActivity:
                        strAttendSeminar = etOther.getText().toString();
                        llNo.setVisibility(View.GONE);
                        llYes.setVisibility(View.GONE);
                        etOther.setFocusable(true);
                        etOther.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.GONE);
                        break;

                }
            }
        });

        radGroupActivity1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity1:
                        strReceiveInvite = radYesActivity1.getText().toString();
                        etOther.setFocusable(false);
                        etOther1.setFocusable(false);
                        etOther1.setText("");
                        etOther.setFocusableInTouchMode(true);
                        etOther1.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radNoActivity1:
                        strReceiveInvite = radNoActivity1.getText().toString();
                        etOther.setFocusable(false);
                        etOther1.setFocusable(false);
                        etOther.setFocusableInTouchMode(true);
                        etOther1.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.GONE);
                        etNotAttend.setText("");
                        etOther1.setText("");
                        break;
                    case R.id.radNotAttendActivity:
                        strReceiveInvite = radNotAttendActivity.getText().toString();
                        //   llYESReceived.setVisibility(View.GONE);
                        break;
                    case R.id.radOtherActivity1:
                        strReceiveInvite = etOther1.getText().toString();
                        etOther1.setFocusable(true);
                        etOther1.setFocusableInTouchMode(true);
                        llYESReceived.setVisibility(View.GONE);
                        etNotAttend.setText("");
                        break;

                }
            }
        });

        radGroupActivity2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity2:
                        strIsEventUseful = radYesActivity2.getText().toString();
                        etOther2.setFocusable(false);
                        etSuggestion.setFocusable(false);
                        etOther2.setText("");
                        etOther2.setFocusableInTouchMode(true);
                        etSuggestion.setFocusableInTouchMode(true);
                        radSuggestionActivity.setChecked(false);
                        radOtherActivity2.setChecked(false);
                        radYesActivity2.setChecked(true);
                        radNoActivity2.setChecked(false);

                        break;

                    case R.id.radNoActivity2:
                        strIsEventUseful = radNoActivity2.getText().toString();
                        etOther2.setFocusable(false);
                        etOther2.setText("");
                        etSuggestion.setFocusable(false);
                        etOther2.setFocusableInTouchMode(true);
                        etSuggestion.setFocusableInTouchMode(true);
                        radSuggestionActivity.setChecked(false);
                        radOtherActivity2.setChecked(false);
                        radYesActivity2.setChecked(false);
                        radNoActivity2.setChecked(true);

                        break;
                    case R.id.radSuggestionActivity:
                        strIsEventUseful = etSuggestion.getText().toString();

                        etOther2.setFocusable(false);
                        etSuggestion.setFocusable(true);
                        etOther2.setFocusableInTouchMode(true);
                        etSuggestion.setFocusableInTouchMode(true);
                        radSuggestionActivity.setChecked(true);
                        radOtherActivity2.setChecked(false);
                        radNoActivity2.setChecked(false);
                        radYesActivity2.setChecked(false);

                        break;
                    case R.id.radOtherActivity2:
                        strIsEventUseful = etOther2.getText().toString();
                        etOther2.setFocusable(true);
                        etSuggestion.setFocusable(false);
                        etOther2.setFocusableInTouchMode(true);
                        etSuggestion.setFocusableInTouchMode(true);
                        radSuggestionActivity.setChecked(false);
                        radOtherActivity2.setChecked(true);
                        radNoActivity2.setChecked(false);
                        radYesActivity2.setChecked(false);

                        break;

                }
            }
        });

        radGroupActivity3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity3:
                        strEasyToBuySeeds = radYesActivity3.getText().toString();
                        radNoActivity3.setError(null);

                        break;

                    case R.id.radNoActivity3:
                        strEasyToBuySeeds = radNoActivity3.getText().toString();
                        radNoActivity3.setError(null);
                        break;

                }
            }
        });

        radGroupActivity4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity4:
                        strSeedsAvaliability = radYesActivity4.getText().toString();
                        radNoActivity4.setError(null);

                        break;

                    case R.id.radNoActivity4:
                        strSeedsAvaliability = radNoActivity4.getText().toString();
                        radNoActivity4.setError(null);

                        break;

                }
            }
        });

        radGroupActivity5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYesActivity5:
                        strEasySeedFeatures = radYesActivity5.getText().toString();
                        radNoActivity5.setError(null);
                        break;

                    case R.id.radNoActivity5:
                        strEasySeedFeatures = radNoActivity5.getText().toString();
                        radNoActivity5.setError(null);
                        break;

                }
            }
        });
    }

    private void radioGroup2Focusable(Boolean val1, Boolean val2) {
        etOther2.setFocusable(false);
        etSuggestion.setFocusable(false);
        etOther2.setFocusableInTouchMode(true);
        etSuggestion.setFocusableInTouchMode(true);
        radSuggestionActivity.setChecked(false);
        radOtherActivity2.setChecked(false);
        radYesActivity2.setChecked(val1);
        radNoActivity2.setChecked(val2);

    }

    private void onSearchBtn() {

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationSearch()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    relPRogress.setVisibility(View.VISIBLE);
                    myTextProgress.setText("Searching Data");
                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            return true;
                        }
                    });
                    // resetActivity();
                    doSearch();

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    container.setEnabled(false);
                    container.setClickable(false);

                }
            }
        });
    }

    private boolean validationSearch() {

        if (etSearchText.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name or mobile number");
            return false;
        }


        return  true;
    }

    private void doSearch() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        searchData();
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

    private void searchData() {


        new VOFPSearchApiCall("SearchDetail").execute();


    }

    private class VOFPSearchApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public VOFPSearchApiCall(String function) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return searchAPIData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);

                if(resultout!=null) {
                    JSONObject jsonObject = new JSONObject(resultout);
                    if (jsonObject.has("success")) {
                        if (jsonObject.has("vofpData")) {

                            if (jsonObject.getString("vofpData").length() == 0) {
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);
                                builder.setTitle("Info");
                                builder.setMessage("No data found");
                                // Config.refreshActivity(FarmerCallActivity.this);
                                // isSearched = false;
                                // resetActivity();
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                androidx.appcompat.app.AlertDialog alert = builder.create();
                                alert.show();
                                progressBarVisibility();

                            } else {

//                            //  isSearched = true;
//                            if(!isSearched){
//                                isSearched = true;
//                            }
                                if((new JSONArray(jsonObject.getString("vofpData")).length()!=0)) {
                                    bindFarmerDetail(new JSONArray(jsonObject.getString("vofpData")));
                                }
//
                                //  getSearchData(jsonObject);


                            }

                        }

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);
                        builder.setTitle("Info");
                        builder.setMessage("Something went wrong please try again later.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                progressBarVisibility();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else {
                    progressBarVisibility();
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressBarVisibility();
            }

        }

    }

    private void bindFarmerDetail(JSONArray vofpData) {


        try {

            spFarmerSearch.setAdapter(null);

            String str = null;
            try {

                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                List<GeneralMaster> list1 = new ArrayList<GeneralMaster>();

                list.add(new GeneralMaster("SELECT FARMER DETAILS",
                        "SELECT FARMER DETAILS"));

                list.add(new GeneralMaster("FARMER DETAILS(NOT FOUND)",
                        "FARMER DETAILS(NOT FOUND)"));

                if(vofpData!=null) {

                    for (int i = 0; i < vofpData.length(); i++) {

                        list.add(new GeneralMaster((vofpData.get(i)).toString(),
                                new JSONObject(vofpData.get(i).toString()).getString("farmerName") + "," + new JSONObject(vofpData.get(i).toString()).getString("mobileNumber")));
                    }




                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);
                    builder.setTitle("Info");
                    builder.setMessage("Data searched successfully");
                    // Config.refreshActivity(FarmerCallActivity.this);
                    // isSearched = false;
                    // resetActivity();
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();

                    progressBarVisibility();

                }


                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFarmerSearch.setAdapter(adapter);


            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void getSearchData(JSONObject object) {


        try {

            tvState.setText(   object.getString("state"));
            tvDist.setText(   object.getString("district"));
            tvTaluka.setText(  object.getString("tehsil"));
            tvVillage.setText(    object.getString("village"));
            etName.setText(   object.getString("farmerName"));
            etPhone.setText(   object.getString("mobileNumber"));

            etName.setEnabled(false);
            etPhone.setEnabled(false);
            progressBarVisibility();






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * <P>Method to get the search  data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String searchAPIData(String function, JSONObject obj) {
        String str = etSearchText.getText().toString().replaceAll(" ","%20");
        String uri = String.format(Locale.ENGLISH, "https://packhouse.mahyco.com/api/generalactivity/getVOFPData?filter=%s",str );
        Uri searchUri = Uri.parse(uri);
        return HttpUtils.POSTInQuery(searchUri.toString(),mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }








    private void onSubmitBtnClicked() {


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 try {


                    if (config.NetworkConnection()) {
                        if (validation()) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);

                            builder.setTitle("MyActivity");
                            builder.setMessage("Are you sure to submit data");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    relPRogress.setVisibility(View.VISIBLE);
                                    myTextProgress.setText("Uploading Data");
                                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {

                                            return true;
                                        }
                                    });
                                    doWork();
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
                        } else {
                            msclass.showMessage("All fields are mandatory");
                        }
                    } else {

                        Utility.showAlertDialog("Mahyco", "Please Check for Internet", context);
                        progressBarVisibility();

                    }
                 }
                 catch(Exception ex )
                 {
                     Log.d(TAG, "Validation Function : "+ex.toString());
                 }
                }

        });
    }

    private void doWork() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {

                        uploadVOPF();
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

    private void uploadVOPF() {

        JSONObject requestParams = new JSONObject();
        JSONObject farmerRequest = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            farmerRequest.put("id", "0");
            farmerRequest.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));

            farmerRequest.put("Email","");
            farmerRequest.put("Name", etName.getText());
            farmerRequest.put("Phoneno", etPhone.getText());

           /* farmerRequest.put("State", spState.getSelectedItem());
            farmerRequest.put("District", spDist.getSelectedItem());
            farmerRequest.put("Taluka", spTaluka.getSelectedItem());
            farmerRequest.put("Village", spVillage.getSelectedItem());

            */


            farmerRequest.put("State", tvState.getText());
            farmerRequest.put("District", tvDist.getText());
            farmerRequest.put("Taluka", tvTaluka.getText());
            farmerRequest.put("Village", tvVillage.getText());
            farmerRequest.put("FarmerCategory", spFarmer.getSelectedItem());

            farmerRequest.put("LandInAcres", etAcres.getText());

            farmerRequest.put("VegetableGrown", spVegeGrown.getSelectedItem());
            farmerRequest.put("VisitedShop", spOptions.getSelectedItem());


            if(!etOtherCompany1.getText().toString().isEmpty()) {
                farmerRequest.put("CompanyName", spCompany.getSelectedItem() + "(" + etOtherCompany1.getText() + ")");
            }else {
                farmerRequest.put("CompanyName", spCompany.getSelectedItem());
            }

            farmerRequest.put("SeedYield", spYield.getSelectedItem());
            farmerRequest.put("SeedGermination", spGermination.getSelectedItem());
            farmerRequest.put("SeedAppearance", spAppearance.getSelectedItem());
            farmerRequest.put("Packing", spPacking.getSelectedItem());
            farmerRequest.put("Price", spPrice.getSelectedItem());
            farmerRequest.put("StaffTalks", spTalk.getSelectedItem());
            farmerRequest.put("StaffExplain", spExplain.getSelectedItem());
            farmerRequest.put("StaffClarifies", spClarifies.getSelectedItem());
            farmerRequest.put("StaffListens", spListens.getSelectedItem());
            farmerRequest.put("StaffShareContact", spShareCont.getSelectedItem());

            farmerRequest.put("AttendSeminar", strAttendSeminar);
            farmerRequest.put("ReceiveInvite", strReceiveInvite);
            farmerRequest.put("IsEventUseful", strIsEventUseful);
            farmerRequest.put("EasyToBuySeeds", strEasyToBuySeeds);
            farmerRequest.put("SeedsAvaliability", strSeedsAvaliability);
            farmerRequest.put("EasySeedFeatures", strEasySeedFeatures);

            farmerRequest.put("BuyMahycoSeeds", spOften.getSelectedItem());
            farmerRequest.put("RecommendProduct", spRecommend.getSelectedItem());
            if(!etOtherCompany.getText().toString().isEmpty()) {
                farmerRequest.put("OtherBrand", spBrand.getSelectedItem() + "(" + etOtherCompany.getText() + ")");
            }else {
                farmerRequest.put("OtherBrand", spBrand.getSelectedItem() );
            }
            farmerRequest.put("RateProduct", spRateProduct.getSelectedItem());
            farmerRequest.put("Ratestaff", spRateStaff.getSelectedItem());
            farmerRequest.put("RateBrand", spRateOverallBrand.getSelectedItem());
            farmerRequest.put("Comments", etComment.getText());

            farmerRequest.put("TaggedAddress", address);
            farmerRequest.put("TaggedCoordinates", cordinates);
            farmerRequest.put("EventNotAttend", etNotAttend.getText());

            String farmerImgStatus = "0";
            Date entrydate = new Date();
            final String farmerImgPath;
            farmerImgPath = Imagepath1;

            final String farmerImgName =AppConstant.Imagename ;// "VOFPPhoto" + mPref.getString(AppConstant.USER_CODE_TAG, "") + String.valueOf(entrydate.getTime());


            farmerRequest.put("FarmerImgName", farmerImgName);
            farmerRequest.put("FarmerImgPath", mDatabase.getImageDatadetail(farmerImgPath));
            farmerRequest.put("FarmerImgStatus", farmerImgStatus);

            farmerRequest.put("entryDt", getCurrentDate());

            jsonArray.put(farmerRequest);
            requestParams.put("Table", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("FarmerPartnerData", requestParams.toString());
        new VOPFApiCall("FarmerPartnerData", requestParams).execute();
        // progressBarVisibility();


    }

    /**
     * <P>Method to upload the distributor survey data to server</P>
     *
     * @param function
     * @param obj
     * @return
     */
    private String uploadVOPFData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.VOPF_SERVER_API, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    private boolean validation() {

        boolean ret = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

       /* if (etSearchText.getText().length() == 0) {
            tiEmail.setError("Please enter email address");
            ret = false;
        } else if (!(etSearchText.getText().toString()).matches(emailPattern)) {
            tiEmail.setError("Please enter valid email address");
            ret = false;
        } else {

            tiEmail.setError("");
            ret = true;
        }

        */

        if (etName.getText().length() == 0) {
            tiName.setError("Please enter the name");
            ret = false;
        } else {
            tiName.setError("");
            ret = true;
        }

        if (etPhone.getText().length() != 10) {
            tiPhone.setError("Please  enter valid phone no");
            ret = false;
        } else {
            tiPhone.setError("");
            ret = true;
        }


       /* if (spState.getSelectedItem().toString().equalsIgnoreCase("SELECT STATE")) {

            setErrorTextColor((TextView) spState.getSelectedView());
            ret = false;

        }
        if (spDist.getSelectedItem().toString().equalsIgnoreCase("SELECT DISTRICT")) {

            setErrorTextColor((TextView) spDist.getSelectedView());
            ret = false;

        }

        if (spTaluka.getSelectedItem().toString().equalsIgnoreCase("SELECT TALUKA")) {

            setErrorTextColor((TextView) spTaluka.getSelectedView());
            ret = false;

        }

        if (spVillage.getSelectedItem().toString().equalsIgnoreCase("SELECT VILLAGE")) {

            setErrorTextColor((TextView) spVillage.getSelectedView());
            ret = false;

        }

        */
        if (spFarmer.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spFarmer.getSelectedView());
            ret = false;

        }

        if (etAcres.getText().length() == 0) {
            tiAcres.setError("Please  enter land in acres");
            ret = false;
        } else if (spFarmer.getSelectedItem().toString().contains("Small")) {

            if (!(1.0 <= Float.parseFloat(etAcres.getText().toString()) && 5.0 > Float.parseFloat(etAcres.getText().toString()))) {
                tiAcres.setError("Please  enter correct range of land in acres");
                ret = false;
            } else {
                tiAcres.setError("");
                //  ret = true;
            }
        } else if (spFarmer.getSelectedItem().toString().contains("Semi- Medium")) {

            if (!(5.0 <= Float.parseFloat(etAcres.getText().toString()) && 10.0 > Float.parseFloat(etAcres.getText().toString()))) {
                tiAcres.setError("Please  enter correct range of land in acres");
                ret = false;
            } else {
                tiAcres.setError("");
                // ret = true;
            }
        } else if (spFarmer.getSelectedItem().toString().contains("Medium")) {

            if (!(10.0 <= Float.parseFloat(etAcres.getText().toString()) && 25.0 > Float.parseFloat(etAcres.getText().toString()))) {
                tiAcres.setError("Please  enter correct range of land in acres");
                ret = false;
            } else {
                tiAcres.setError("");
                // ret = true;
            }
        } else if (spFarmer.getSelectedItem().toString().contains("Large")) {

            if (!(25.0 <= Float.parseFloat(etAcres.getText().toString()))) {
                tiAcres.setError("Please  enter correct range of land in acres");
                ret = false;
            } else {
                tiAcres.setError("");
                // ret = true;
            }
        } else {
            tiAcres.setError("");
            //  ret = true;
        }


        if (spVegeGrown.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {

            setErrorTextColor((TextView) spVegeGrown.getSelectedView());
            ret = false;

        } else if (count > 5) {
            // tvError.setVisibility(View.VISIBLE);
            msclass.showMessage("Max crop selection limit is 5");

            ret = false;
        } else {
            //  tvError.setVisibility(View.GONE);
            ret = true;
        }

        if (spOptions.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spOptions.getSelectedView());
            ret = false;

        }else if(spOptions.getSelectedItem().equals("No")){
            if (spCompany.getSelectedItem().toString().equalsIgnoreCase("SELECT COMPANY")) {

                setErrorTextColor((TextView) spCompany.getSelectedView());
                ret = false;
            }else if(spCompany.getSelectedStrings().contains("Other")){
                if(etOtherCompany1.getText().length()==0){
                    tiOtherCompany.setError("Please  enter other Company name");
                    ret=false;
                }else {
                    tiOtherCompany.setError("");
                }
            }}

        if (spYield.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spYield.getSelectedView());
            ret = false;

        }
        if (spGermination.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spGermination.getSelectedView());
            ret = false;

        }
        if (spAppearance.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spAppearance.getSelectedView());
            ret = false;
        }
        if (spPacking.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spPacking.getSelectedView());
            ret = false;
        }
        if (spPrice.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spPrice.getSelectedView());
            ret = false;
        }


        if (spTalk.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spTalk.getSelectedView());
            ret = false;
        }
        if (spExplain.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spExplain.getSelectedView());
            ret = false;
        }
        if (spClarifies.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spClarifies.getSelectedView());
            ret = false;
        }

        if (spListens.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spListens.getSelectedView());
            ret = false;
        }
        if (spShareCont.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spShareCont.getSelectedView());
            ret = false;
        }


        if (radGroupActivity.getCheckedRadioButtonId() <= 0) {

            tvErrorAttend.setVisibility(View.VISIBLE);
            setErrorTextColor1((TextView) tvAttend);


            ret = false;
        }else {

            final String value = ((RadioButton)findViewById(radGroupActivity.getCheckedRadioButtonId()))
                    .getText().toString();
            switch (value){
                case "Yes":

                    ret =  manageValidationForYes(true);
                    setDefaultTextColor((TextView) tvAttend);

                    break;
                case "No":
                    ret =  manageValidationForNo(true);
                    setDefaultTextColor((TextView) tvAttend);
                    break;

                case "Other":
                    if(etOther.getText().length()==0){
                        tvErrorAttend.setVisibility(View.VISIBLE);
                        setErrorTextColor1((TextView) tvAttend);
                        ret =false;
                    }else {
                        tvErrorAttend.setVisibility(View.GONE);
                        setDefaultTextColor((TextView) tvAttend);
                    }
                    break;

            }



        }


        if (radGroupActivity5.getCheckedRadioButtonId() <= 0) {
            radNoActivity5.setError("");

            ret = false;
        }
        if (radGroupActivity4.getCheckedRadioButtonId() <= 0) {
            radNoActivity4.setError("");

            ret = false;
        }
        if (radGroupActivity3.getCheckedRadioButtonId() <= 0) {
            radNoActivity3.setError("");

            ret = false;
        }


        if (spOften.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spOften.getSelectedView());
            ret = false;
        }

        if (spRecommend.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spRecommend.getSelectedView());
            ret = false;
        }

        if (spBrand.getSelectedItem().toString().equalsIgnoreCase("SELECT COMPANY")) {

            setErrorTextColor((TextView) spBrand.getSelectedView());
            ret = false;
        }else if(spBrand.getSelectedStrings().contains("Other")){
            if(etOtherCompany.getText().length()==0){
                tiOther.setError("Please  enter other Brand name");
                ret=false;
            }else {
                tiOther.setError("");

            }
        }

        if (spRateProduct.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spRateProduct.getSelectedView());
            ret = false;
        }
        if (spRateStaff.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spRateStaff.getSelectedView());
            ret = false;
        }
        if (spRateOverallBrand.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

            setErrorTextColor((TextView) spRateOverallBrand.getSelectedView());
            ret = false;
        }

        if (etComment.getText().length() == 0) {
            tiComments.setError("Please  enter comments/ suggestions/ improvements");
            ret = false;
        } else {
            tiComments.setError("");
            //ret = true;
        }


        /* if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please click photo");
            ret = false;
        }*/



        return ret;
    }

    private boolean manageValidationForNo(boolean ret) {

        if (radGroupActivity1.getCheckedRadioButtonId() <= 0) {
            tvErrorAttend.setVisibility(View.VISIBLE);
            setErrorTextColor1(tvReceive);

            ret = false;
        }else {
            final String value1 = ((RadioButton)findViewById(radGroupActivity1.getCheckedRadioButtonId()))
                    .getText().toString();

            if(value1.contains("Other")){
                if(etOther1.getText().length()==0)
                {
                    tvErrorAttend.setVisibility(View.VISIBLE);
                    setErrorTextColor1(tvReceive);

                    ret=false;
                }else {
                    tvErrorAttend.setVisibility(View.GONE);
                    setDefaultTextColor(tvReceive);


                }
            }else if(value1.contains("Yes")){
                setDefaultTextColor(tvReceive);
                if(etNotAttend.getText().length()==0)
                {
                    tvErrorAttend.setVisibility(View.VISIBLE);
                    setErrorTextColor1(tvNotAttend);
                    ret=false;
                }else {
                    tvErrorAttend.setVisibility(View.GONE);
                    setDefaultTextColor(tvNotAttend);
                }
            }else {
                tvErrorAttend.setVisibility(View.GONE);
                setDefaultTextColor(tvReceive);
            }

        }

        return  ret;
    }

    private Boolean manageValidationForYes(boolean ret) {
        if (radGroupActivity2.getCheckedRadioButtonId() <= 0) {
            tvErrorAttend.setVisibility(View.VISIBLE);
            setErrorTextColor1(tvUsefulEvent);


            ret = false;
        }else {

            final String value1 = ((RadioButton)findViewById(radGroupActivity2.getCheckedRadioButtonId()))
                    .getText().toString();

            if(value1.contains("Other")){
                if(etOther2.getText().length()==0)
                {
                    tvErrorAttend.setVisibility(View.VISIBLE);
                    setErrorTextColor1(tvUsefulEvent);
                    ret=false;
                }else {
                    tvErrorAttend.setVisibility(View.GONE);
                    setDefaultTextColor(tvUsefulEvent);
                }
            }else {
                tvErrorAttend.setVisibility(View.GONE);
                setDefaultTextColor(tvUsefulEvent);
            }

        }
        return ret;
    }

    /**
     * <P>Method to change the textview color when error occured</P>
     *
     * @param txtQues
     */
    private void setErrorTextColor(TextView txtQues) {
        // txtQues.setTextColor(getResources().getColor(R.color.errorColor));

        TextView errorTextview = txtQues;
        errorTextview.setError("");
        errorTextview.setTextColor(getResources().getColor(R.color.errorColor));
    }

    private void setErrorTextColor1(TextView txtQues) {
        // txtQues.setTextColor(getResources().getColor(R.color.errorColor));

        TextView errorTextview = txtQues;
        errorTextview.setTextColor(getResources().getColor(R.color.errorColor));
    }
    private void setDefaultTextColor(TextView txtQues) {

        TextView errorTextview = txtQues;
        //errorTextview.setError("");
        errorTextview.setTextColor(getResources().getColor(R.color.QRCodeBlackColor));
    }
    /**
     * <P>Manage the progressbar visibility</P>
     */
    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container.setClickable(true);
        container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void bindBrand() {
        try {

            String str = null;
            try {
                companyArray = getResources().getStringArray(R.array.company_name);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, companyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (companyArray.length > 0) {
                    spBrand.setItems(companyArray);
                    spBrand.hasNoneOption(true);
                    spBrand.setSelection(new int[]{0});
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void bindRating() {
        try {
            spYield.setAdapter(null);
            spGermination.setAdapter(null);
            spAppearance.setAdapter(null);
            spPacking.setAdapter(null);
            spPrice.setAdapter(null);
            spTalk.setAdapter(null);
            spExplain.setAdapter(null);
            spClarifies.setAdapter(null);
            spListens.setAdapter(null);
            spShareCont.setAdapter(null);
            spOften.setAdapter(null);
            spRateProduct.setAdapter(null);
            spRateOverallBrand.setAdapter(null);
            spRateStaff.setAdapter(null);

            String str = null;
            try {
                ratingArray = getResources().getStringArray(R.array.rating);
                ratingArray1 = getResources().getStringArray(R.array.rating1);
                ratingArray2 = getResources().getStringArray(R.array.rating2);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spYield.setAdapter(adapter);
                spGermination.setAdapter(adapter);
                spAppearance.setAdapter(adapter);
                spPacking.setAdapter(adapter);
                spPrice.setAdapter(adapter);
                spRateOverallBrand.setAdapter(adapter);
                spRateStaff.setAdapter(adapter);
                spRateProduct.setAdapter(adapter);


                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTalk.setAdapter(adapter1);
                spExplain.setAdapter(adapter1);
                spClarifies.setAdapter(adapter1);
                spListens.setAdapter(adapter1);
                spShareCont.setAdapter(adapter1);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOften.setAdapter(adapter2);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    private void bindCompany() {
        try {

            String str = null;
            try {
                companyArray = getResources().getStringArray(R.array.company_name);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, companyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (companyArray.length > 0) {
                    spCompany.setItems(companyArray);
                    spCompany.hasNoneOption(true);
                    spCompany.setSelection(new int[]{0});

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void bindOptions() {
        try {
            spOptions.setAdapter(null);
            spRecommend.setAdapter(null);

            String str = null;
            try {
                optionsArray = getResources().getStringArray(R.array.option);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, optionsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOptions.setAdapter(adapter);
                spRecommend.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void bindFarmerCategory() {
        try {
            spFarmer.setAdapter(null);
            // spFarmerSearch.setAdapter(null);

            String str = null;
            try {
                categoryArray = getResources().getStringArray(R.array.farmer_category);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFarmer.setAdapter(adapter);


                //    spFarmerSearch.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
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

    private void bindVegetableGrown(String Croptype) {
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
                spVegeGrown.setItems(array);
                spVegeGrown.hasNoneOption(true);
                spVegeGrown.setSelection(new int[]{0});
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
            // tvCordinates.setText(cordinatesmsg + "\n" + address);
            // tvAddress.setText(address + "\n" + cordinates);
            manageGeoTag();
        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }
    }

    /**
     * <p>Method is used to manage geo tag</p>
     */
    private void manageGeoTag() {
        if (cordinates != null && !cordinates.contains("null")) {
            if (tvCordinates.getText().toString().contains("Yes")) {
                // imgBtnGps.setImageResource(R.drawable.ic_location_off);
                cordinatesmsg = "GEO TAG : \n";
            } else {
                if (lati != 0) {
                    //    imgBtnGps.setImageResource(R.drawable.ic_location_on);
                    cordinatesmsg = "GEO TAG RECEIVED SUCCESSFULLY : \n";
                } else {
                    startFusedLocationService();
                }
            }
        } else {
            startFusedLocationService();
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
                tvAddress.setText(address + "\n" + cordinates);
            } else {

                tvCordinates.setText(cordinatesmsg + "\n" + address);
                tvAddress.setText(address + "\n" + cordinates);
            }
            tvAddress.setText(address + "\n" + cordinates);
            strAdd = tvAddress.getText().toString();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVOFP.this);

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
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize =2;
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // this only get capture photo
                    //************
                    Date entrydate = new Date();
                    String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename=this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    FileUtilImage.compressImageFile( AppConstant.queryImageUrl, AppConstant.imageUri,
                            this,AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    //writeMediaStoreFile(this, AppConstant.queryImageUrl, ivImage,myBitmap);
                    ivImage.setImageBitmap(myBitmap);
                    //**************
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
    private   void writeMediaStoreFile(final Context context, String imgPath,
                                             ImageView imgChoosen,
                                       Bitmap bitmap )
    {

                      ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "NewImage");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+"/MyActivity/");
                        Uri localImageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        if (localImageUri == null) {
                            // Toast.makeText(context, "Failed to create new MediaStore record",  Toast.LENGTH_SHORT).show();
                        } else {
                            // textViewPath.setText("Image Path: \n" + localImageUri);
                            // Toast.makeText(context, "Created new MediaStore record",  Toast.LENGTH_SHORT).show();
                            try {
                                OutputStream outputStream = context.getContentResolver().openOutputStream(localImageUri);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();
                                imgChoosen.setImageBitmap(bitmap);
                                // Toast.makeText(context, "Bitmap created",  Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // Toast.makeText(context, "Failed to store bitmap",  Toast.LENGTH_SHORT).show();
                            }
                        }
                    }



    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.btnActivityPhoto:
                // msclass.showMessage("message");
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
        try {
            if (r.getError() == null) {
                ivImage.setImageBitmap(r.getBitmap());
                if (ivImage.getDrawable() != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage.setVisibility(View.GONE);
                    ivImage.setVisibility(View.GONE);
                }
                Date entrydate = new Date();
                //Image path
                String pathImage = r.getPath();
                ////
                AppConstant.queryImageUrl = pathImage;
                AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                // need to set commpress image path
                //  Imagename=AppConstant.Imagename;
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave

                //roundedImageView.setImageBitmap(myBitmap);
                ////
                // encodedImage = FileUtilImage.getImageDatadetail(pathImage);
                //Log.d("encodedImage::", encodedImage);
            } else {
                //Handle possible errors
                //TODO: do what you have to do with r.getError();
                // Log.e(TAG, "onPickResult: ", r.getError());
                Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
            // Object tr = null;
            // Log.d("error ::"+ex.toString(), encodedImage);

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

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class VOPFApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public VOPFApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadVOPFData(function, obj);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                redirecttoRegisterActivity(result);
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVOFP.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(ActivityVOFP.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                progressBarVisibility();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityVOFP.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            progressBarVisibility();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressBarVisibility();
            }

        }

    }
    public void redirecttoRegisterActivity(String result )
    {
        if (result.toLowerCase().contains("authorization")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("MyActivity");
            builder.setMessage("Your login session is  expired, please register user again. ");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {



                    //editor.putString("UserID", null);
                    // editor.commit();

                    Intent intent1 = new Intent(context.getApplicationContext(), UserRegister.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);

                   /* Intent intent = new Intent(UploadData.this, UserRegister.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    editor.putString("UserID", null);
                    editor.commit();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


}
