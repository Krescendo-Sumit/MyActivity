package myactvity.mahyco.myActivityRecording.atlActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import myactvity.mahyco.AndroidDatabaseManager;
import myactvity.mahyco.R;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.AddFarmerListFieldDayAdapter;
import myactvity.mahyco.helper.FieldDayModel;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class TrolleyPaintingActivity extends AppCompatActivity {
    Context context;

    private static final String TAG = "TROLLEYPAINTING";
    SearchableSpinner spState, spDist, spVillage, spTaluka, spFocusedVillages;
    MultiSelectionSpinner spProductName,spCropType;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    public Messageclass msclass;
    EditText etleftSidewidth, etleftSideHeight, etrightSidewidth, etrightSideHeight, etbackSidewidth, etbackSideHeight, etfrontSidewidth, etfrontSideHeight;
    EditText etFarmerName, etMobileNumber, etTrolleyNumber;
    TextView tvTotalleft, tvTotalright, tvtotalfrontSide, tvtotalbackSide, tvtotalWidth,tvtotalHeight, tvtotal;
    Button btnleftSide, btnRightSide, btnbackSide, btnfrontSide;
    String userCode, state, taluka, dist, village, farmerDetails;
    Button btnSubmit;
    LinearLayout llImages;
    private long mLastClickTime = 0;
    String cordinates="";
    String address="";
    String croptype;
    String focusedVillage;
    LinearLayout llOtherVillages, llFocussedVillages;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
  //  String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    String SERVER = "https://packhouse.mahyco.com/api/atl/trolleyPaintingData";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    List<GeneralMaster> farmerlist;
    ArrayList<FieldDayModel> farmerListWithNumber;
    String villageType = "focussed";
    int farmerNumber = 0;
    Button btnSaveClose;
    String pkFarmerMobileNumber;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int imageselect;
    File photoFile = null;
    public String imagePathLeftSide = "", imagePathRightSide = "", imagePathBackSide = "", imagePathFrontSide = "";
    private ImageView ivImageLeftSide, ivImageRightSide, ivImageBackSide, ivImageFrontSide;
    private static final String IMAGE_DIRECTORY_NAME = "FIELD DAY";
    File photoFileLeftSide = null, photoFileRightSide = null, photoFileBackSide = null, photoFileFrontSide = null;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity,radOtherActivity;
    Prefs mPref;
    TextView lblheader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trolley_painting);
//        getSupportActionBar().hide(); //<< this
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
        msclass = new Messageclass(this);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        etleftSidewidth = (EditText) findViewById(R.id.etleftSidewidth);
        etleftSideHeight = (EditText) findViewById(R.id.etleftSideHeight);
        etrightSidewidth = (EditText) findViewById(R.id.etrightSidewidth);
        etrightSideHeight = (EditText) findViewById(R.id.etrightSideHeight);
        etbackSidewidth = (EditText) findViewById(R.id.etbackSidewidth);
        etbackSideHeight = (EditText) findViewById(R.id.etbackSideHeight);
        etfrontSidewidth = (EditText) findViewById(R.id.etfrontSidewidth);
        etfrontSideHeight = (EditText) findViewById(R.id.etfrontSideHeight);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etTrolleyNumber = (EditText) findViewById(R.id.etTrolleyNumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnleftSide = (Button) findViewById(R.id.btnleftSide);
        btnRightSide = (Button) findViewById(R.id.btnRightSide);
        btnbackSide = (Button) findViewById(R.id.btnbackSide);
        btnfrontSide = (Button) findViewById(R.id.btnfrontSide);
        tvtotalfrontSide = (TextView) findViewById(R.id.tvtotalfrontSide);
        tvtotalbackSide = (TextView) findViewById(R.id.tvtotalbackSide);
        tvTotalright = (TextView) findViewById(R.id.tvTotalright);
        tvTotalleft = (TextView) findViewById(R.id.tvTotalleft);
        tvtotal = (TextView) findViewById(R.id.tvtotal);
        tvtotalWidth = (TextView) findViewById(R.id.tvtotalWidth);
        tvtotalHeight = (TextView) findViewById(R.id.tvtotalHeight);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        ivImageLeftSide = (ImageView) findViewById(R.id.ivImageLeftSide);
        ivImageRightSide = (ImageView) findViewById(R.id.ivImageRightSide);
        ivImageBackSide = (ImageView) findViewById(R.id.ivImageBackSide);
        ivImageFrontSide = (ImageView) findViewById(R.id.ivImageFrontSide);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        lblheader = (TextView) findViewById(R.id.lblheader);
        farmerListWithNumber = new ArrayList<>();
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        llImages = (LinearLayout) findViewById(R.id.llImages);
       // userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode =  pref.getString("UserID", null);
        bindState();
        bindFocussedVillages();

        onSubmitBtnClicked();

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


        lblheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrolleyPaintingActivity.this, AndroidDatabaseManager.class);
                startActivity(intent);
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

        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    village = gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        etleftSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int leftwidth = 0;
                int leftheight = 0;
                if (!etleftSidewidth.getText().toString().equals("") && !etleftSideHeight.getText().toString().equals("")) {
                    leftwidth = Integer.valueOf(etleftSidewidth.getText().toString());
                    leftheight = Integer.valueOf(etleftSideHeight.getText().toString());

                }

                int totalAreaLeft = leftwidth * leftheight;
                tvTotalleft.setText(String.valueOf(totalAreaLeft));

            }
        });



        etleftSidewidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int leftwidth = 0;
                int leftheight = 0;
                if (!etleftSidewidth.getText().toString().equals("") && !etleftSideHeight.getText().toString().equals("")) {
                    leftwidth = Integer.valueOf(etleftSidewidth.getText().toString());
                    leftheight = Integer.valueOf(etleftSideHeight.getText().toString());

                }

                int totalAreaLeft = leftwidth * leftheight;
                tvTotalleft.setText(String.valueOf(totalAreaLeft));

            }
        });


        etrightSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int rightwidth = 0;
                int rightheight = 0;
                if (!etrightSidewidth.getText().toString().equals("") && !etrightSideHeight.getText().toString().equals("")) {
                    rightwidth = Integer.valueOf(etrightSidewidth.getText().toString());
                    rightheight = Integer.valueOf(etrightSideHeight.getText().toString());

                }

                int totalAreaRight = rightwidth * rightheight;
                tvTotalright.setText(String.valueOf(totalAreaRight));

            }
        });
        etrightSidewidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int rightwidth = 0;
                int rightheight = 0;
                if (!etrightSidewidth.getText().toString().equals("") && !etrightSideHeight.getText().toString().equals("")) {
                    rightwidth = Integer.valueOf(etrightSidewidth.getText().toString());
                    rightheight = Integer.valueOf(etrightSideHeight.getText().toString());

                }

                int totalAreaRight = rightwidth * rightheight;
                tvTotalright.setText(String.valueOf(totalAreaRight));

            }
        });


        etbackSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int backwidth = 0;
                int backheight = 0;
                if (!etbackSidewidth.getText().toString().equals("") && !etbackSideHeight.getText().toString().equals("")) {
                    backwidth = Integer.valueOf(etbackSidewidth.getText().toString());
                    backheight = Integer.valueOf(etbackSideHeight.getText().toString());

                }

                int totalAreaBack = backwidth * backheight;
                tvtotalbackSide.setText(String.valueOf(totalAreaBack));

            }
        });
        etbackSidewidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int backwidth = 0;
                int backheight = 0;
                if (!etbackSidewidth.getText().toString().equals("") && !etbackSideHeight.getText().toString().equals("")) {
                    backwidth = Integer.valueOf(etbackSidewidth.getText().toString());
                    backheight = Integer.valueOf(etbackSideHeight.getText().toString());

                }

                int totalAreaBack = backwidth * backheight;
                tvtotalbackSide.setText(String.valueOf(totalAreaBack));

            }
        });

        etfrontSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int frontwidth = 0;
                int frontheight = 0;
                if (!etfrontSidewidth.getText().toString().equals("") && !etfrontSideHeight.getText().toString().equals("")) {
                    frontwidth = Integer.valueOf(etfrontSidewidth.getText().toString());
                    frontheight = Integer.valueOf(etfrontSideHeight.getText().toString());

                }

                int totalAreaFront = frontwidth * frontheight;
                tvtotalfrontSide.setText(String.valueOf(totalAreaFront));

            }
        });
        etfrontSidewidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int frontwidth = 0;
                int frontheight = 0;
                if (!etfrontSidewidth.getText().toString().equals("") && !etfrontSideHeight.getText().toString().equals("")) {
                    frontwidth = Integer.valueOf(etfrontSidewidth.getText().toString());
                    frontheight = Integer.valueOf(etfrontSideHeight.getText().toString());

                }

                int totalAreaFront = frontwidth * frontheight;
                tvtotalfrontSide.setText(String.valueOf(totalAreaFront));

            }
        });


        btnleftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TrolleyPaintingActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
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

        btnRightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TrolleyPaintingActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 2;
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
        btnbackSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TrolleyPaintingActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 3;
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
        btnfrontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TrolleyPaintingActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 4;
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


        bindcroptype(spCropType, "C");
        bindProductName(spProductName, "");

        calculateTotalWidthDimensions();

        calculateTotalHeightDimensions();

        calculateTotalDimensions();
        onCropItemSelected();

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
                Log.d("which selectedSt:: ",String.valueOf(strings));
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
    /**
     * <P>Method to calculate the  total dimensions </P>
     */
    private void calculateTotalDimensions() {

        etfrontSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int totalleft = 0;
                int totalright = 0;
                int totalfrontSide = 0;
                int totalbackSide = 0;
                if (!tvTotalleft.getText().toString().equals("") && !tvTotalright.getText().toString().equals("") &&!tvtotalbackSide.getText().toString().equals("") && !tvtotalfrontSide.getText().toString().equals("")) {
                    totalleft = Integer.valueOf(tvTotalleft.getText().toString());
                    totalright = Integer.valueOf(tvTotalright.getText().toString());
                    totalbackSide = Integer.valueOf(tvtotalbackSide.getText().toString());
                    totalfrontSide = Integer.valueOf(tvtotalfrontSide.getText().toString());

                }

                int totalAreaFront = totalleft + totalright + totalbackSide + totalfrontSide;
                tvtotal.setText(String.valueOf(totalAreaFront));

            }
        });

      }

    /**
     * <P>Method to calculate the  total height </P>
     */
    private void calculateTotalHeightDimensions() {

        etfrontSideHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int leftHeight = 0;
                int righHeight = 0;
                int backHeight = 0;
                int frontHeight = 0;
                if (!etleftSideHeight.getText().toString().equals("") && !etrightSideHeight.getText().toString().equals("") &&!etbackSideHeight.getText().toString().equals("") && !etfrontSideHeight.getText().toString().equals("")) {
                    leftHeight = Integer.valueOf(etleftSideHeight.getText().toString());
                    righHeight = Integer.valueOf(etrightSideHeight.getText().toString());
                    backHeight = Integer.valueOf(etbackSideHeight.getText().toString());
                    frontHeight = Integer.valueOf(etfrontSideHeight.getText().toString());

                }

                int totalAreaLeft = leftHeight + righHeight+ backHeight + frontHeight;
                tvtotalHeight.setText(String.valueOf(totalAreaLeft));

            }
        });

    }

    /**
     * <P>Method to calculate the  total width </P>
     */
    private void calculateTotalWidthDimensions() {

        etfrontSidewidth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged " + s.toString());
                int leftwidth = 0;
                int rightwidth = 0;
                int backwidth = 0;
                int frontwidth = 0;
                if (!etleftSidewidth.getText().toString().equals("") && !etrightSidewidth.getText().toString().equals("") &&!etbackSidewidth.getText().toString().equals("") && !etfrontSidewidth.getText().toString().equals("")) {
                    leftwidth = Integer.valueOf(etleftSidewidth.getText().toString());
                    rightwidth = Integer.valueOf(etrightSidewidth.getText().toString());
                    backwidth = Integer.valueOf(etbackSidewidth.getText().toString());
                    frontwidth = Integer.valueOf(etfrontSidewidth.getText().toString());

                }

                int totalAreaLeft = leftwidth + rightwidth+ backwidth + frontwidth;
                tvtotalWidth.setText(String.valueOf(totalAreaLeft));

            }
        });

    }

    /**
     * <P>Method is used to do API related work on submit button clicked</P>
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(TrolleyPaintingActivity.this);

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

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            container.setEnabled(false);
                            container.setClickable(false);


                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }

        });
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
                        if (imageselect == 2) {
                            photoFile = createImageFile();
                            //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            // Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                            }
                        }
                        if (imageselect == 3) {
                            photoFile = createImageFile();
                            //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            // Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                            }
                        }
                        if (imageselect == 4) {
                            photoFile = createImageFile();
                            //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            // Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
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
            if (imageselect == 2) {
                photoFile = createImageFile4();
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
            if (imageselect == 3) {
                photoFile = createImageFile4();
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
            if (imageselect == 4) {
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


        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =2;
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
            Date entrydate = new Date();
            String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            if (imageselect == 1) {
                try {

                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = "TrolleyPainitingLeftSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    imagePathLeftSide = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImageLeftSide.setImageBitmap(myBitmap);

                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }
            if (imageselect == 2) {
                //ivImage2.setImageBitmap(thumbnail);
                try {
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename2 = "TrolleyPainitingRightSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename2);
                    // need to set commpress image path
                    imagePathRightSide = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImageRightSide.setImageBitmap(myBitmap);


                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }
            if (imageselect == 3) {
                //ivImage2.setImageBitmap(thumbnail);
                try {
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename3 = "TrolleyPainitingBackSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename3);
                    // need to set commpress image path
                    imagePathBackSide = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImageBackSide.setImageBitmap(myBitmap);

                    ;
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }
            if (imageselect == 4) {
                //ivImage2.setImageBitmap(thumbnail);
                try {

                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename4 = "TrolleyPainitingFrontSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename4);
                    // need to set commpress image path
                    imagePathFrontSide = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImageFrontSide.setImageBitmap(myBitmap);

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


    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect == 1) {
            ivImageLeftSide.setImageBitmap(bm);
        }
        if (imageselect == 2) {
            ivImageRightSide.setImageBitmap(bm);
        }
        if (imageselect == 3) {
            ivImageBackSide.setImageBitmap(bm);
        }
        if (imageselect == 4) {
            ivImageFrontSide.setImageBitmap(bm);
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

    public boolean validation() {

        //spinner validation
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
        if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
            Utility.showAlertDialog("Info", "Please Select Crop", context);
            return false;
        }

        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
                return false;
        }
        if (etFarmerName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }

        if(etMobileNumber.getText().length()>0) {
            if(etMobileNumber.getText().length()<10) {
                msclass.showMessage("Mobile number should be of 10 digits");
                return false;
            }
        }else {
            msclass.showMessage("Please enter mobile number");
            return false;
        }

        if (etTrolleyNumber.getText().length() == 0) {
            msclass.showMessage("Please enter trolley RTO number");
            return false;
        }

        //Dimensions validation
        if (etleftSidewidth.getText().length() == 0) {
            msclass.showMessage("Please enter left side width");
            return false;
        }

        if (etleftSideHeight.getText().length() == 0) {
            msclass.showMessage("Please enter left side height");
            return false;
        }

        if (etrightSidewidth.getText().length() == 0) {
            msclass.showMessage("Please enter right side width");
            return false;
        }

        if (etrightSideHeight.getText().length() == 0) {
            msclass.showMessage("Please enter right side height");
            return false;
        }

        if (etbackSidewidth.getText().length() == 0) {
            msclass.showMessage("Please enter back side width");
            return false;
        }

        if (etbackSideHeight.getText().length() == 0) {
            msclass.showMessage("Please enter back side height");
            return false;
        }


        if (etfrontSidewidth.getText().length() == 0) {
            msclass.showMessage("Please enter front side width");
            return false;
        }

        if (etfrontSideHeight.getText().length() == 0) {
            msclass.showMessage("Please enter front side height");
            return false;
        }

        // Image validation
        if (ivImageLeftSide.getDrawable() == null) {
            msclass.showMessage("Please click left photo");
            return false;
        }
        if (ivImageRightSide.getDrawable() == null) {
            msclass.showMessage("Please click right photo");
            return false;
        }
        if (ivImageBackSide.getDrawable() == null) {
            msclass.showMessage("Please click back photo");
            return false;
        }

        if (ivImageFrontSide.getDrawable() == null) {
            msclass.showMessage("Please click front photo");
            return false;
        }

       /* if (!isAlreadydone(pkFarmerMobileNumber)) {
            Utility.showAlertDialog("Info", "This entry already exists", context);
            return false;
        }*/
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


    @Override
    protected void onResume() {
        super.onResume();

        if (ivImageLeftSide.getDrawable() != null) {
            ivImageLeftSide.setVisibility(View.VISIBLE);
            llImages.setVisibility(View.VISIBLE);
        } else {
            ivImageLeftSide.setVisibility(View.GONE);
        }
        if (ivImageRightSide.getDrawable() != null) {
            ivImageRightSide.setVisibility(View.VISIBLE);
            llImages.setVisibility(View.VISIBLE);
        } else {
            ivImageRightSide.setVisibility(View.GONE);
        }
        if (ivImageBackSide.getDrawable() != null) {
            ivImageBackSide.setVisibility(View.VISIBLE);
            llImages.setVisibility(View.VISIBLE);
        } else {
            ivImageBackSide.setVisibility(View.GONE);
        }
        if (ivImageFrontSide.getDrawable() != null) {
            ivImageFrontSide.setVisibility(View.VISIBLE);
            llImages.setVisibility(View.VISIBLE);
        } else {
            ivImageFrontSide.setVisibility(View.GONE);
        }
        try {
        } catch (Exception ex) {
            msclass.showMessage("Funtion name :onresume" + ex.getMessage());
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
            spVillage.setAdapter(adapter);

        } catch (
                Exception ex) {
            ex.printStackTrace();

        }
    }

    public void bindFocussedVillages() {
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
                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

//            Croplist.add(newGeneralMaster("OTHER","OTHER"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFocusedVillages.setAdapter(adapter);

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }


    //bind state to spinner
    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code FROM VillageLevelMaster order by state asc  ";
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
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }

    }


    //bind District to spinner

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
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    //bind Territory to spinner
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

    //bind Village to spinner

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


    //Bind Crops

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
//            Croplist.add(new GeneralMaster("SELECT CROP",
//                    "SELECT CROP"));
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
         //   spCropType.setAdapter(adapter);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
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

    private void bindProductName(Spinner spProductName, String croptype) {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery ="";
            StringBuilder nameBuilder = new StringBuilder();

            if(croptype.length()>0){
                for (String n : croptype.substring(1,croptype.length()-1).split(",")) {
                    nameBuilder.append("'").append(n.trim().replace("'", "\\'")).append("',");
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName  IN (" + nameBuilder.toString().substring(0,nameBuilder.length()-1) +") ORDER BY 'CropName'";

                }
            }else {
                Log.d("Crop type","First time");
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
    //////////////////////////

    public void createAndAddData() {

        try {
            if (villageType.contains("other")) {
                ArrayList<FieldDayModel> otherVillageList = new ArrayList<>();
                for (int i = 0; i < farmerListWithNumber.size(); i++) {
                    if (farmerListWithNumber.get(i).getVillageType().contains("other")) {
                        otherVillageList.add(farmerListWithNumber.get(i));
                    }
                }
                recDemoList.setLayoutManager(new LinearLayoutManager(context));
                recDemoList.setAdapter(new AddFarmerListFieldDayAdapter(context, otherVillageList));

            } else if (villageType.contains("focussed")) {
                ArrayList<FieldDayModel> focussedList = new ArrayList<>();

                for (int i = 0; i < farmerListWithNumber.size(); i++) {
                    if (farmerListWithNumber.get(i).getVillageType().contains("focussed")) {
                        focussedList.add(farmerListWithNumber.get(i));
                    }
                }
                recDemoList.setLayoutManager(new LinearLayoutManager(context));
                recDemoList.setAdapter(new AddFarmerListFieldDayAdapter(context, focussedList));
            } else {
                recDemoList.setLayoutManager(new LinearLayoutManager(context));
                recDemoList.setAdapter(new AddFarmerListFieldDayAdapter(context, farmerListWithNumber));
            }
            // } else {adapterMDO.notifyDataSetChanged(); }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    ////////


    /////////////////////////

    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler = null;
        }


    }

    //if mob no already exist
    private boolean isAlreadydone(String retailerNumber) {
        boolean isExist = false;
        Cursor data = mDatabase.fetchAlreadyExistsFieldDayData(retailerNumber);
        if (data.getCount() == 0) {
            isExist = true;
        }
        data.close();
        return isExist;
    }

    //save data to db//


    //////////////////////////////

    public void saveToDb() {
        String focussedVillage = spFocusedVillages.getSelectedItem().toString();
        String state = spState.getSelectedItem().toString();
        String district = "";
        String taluka = "";
        String othervillage = "";
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
        String farmerName = etFarmerName.getText().toString();
        String farmerMobileNumber = etMobileNumber.getText().toString();
        String trolleyNumber = etTrolleyNumber.getText().toString();
        String totalDimensionLeft = tvTotalleft.getText().toString();
        String totalDimensionRight = tvTotalright.getText().toString();
        String totalDimensionBack = tvtotalbackSide.getText().toString();
        String totalDimensionFront = tvtotalfrontSide.getText().toString();

        String isSynced = "0";
        String leftSideImgStatus = "0", rightSideImgStatus = "0", backSideImgStatus = "0", frontSideImgStatus = "0";
        Date entrydate = new Date();
        final String leftSideImgPath, rightSideImgPath, backSideImgPath, frontSideImgPath;
        leftSideImgPath = imagePathLeftSide;
        rightSideImgPath = imagePathRightSide;
        backSideImgPath = imagePathBackSide;
        frontSideImgPath = imagePathFrontSide;
        final String leftSideImgName =AppConstant.Imagename; //"TrolleyPainitingLeftSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        final String rightSideImgName =AppConstant.Imagename2;// "TrolleyPainitingRightSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        final String backSideImgName = AppConstant.Imagename3;//"TrolleyPainitingBackSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        final String frontSideImgName =AppConstant.Imagename4;// "TrolleyPainitingFrontSidePhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());

        boolean fl = mDatabase.insertTrolleyPaintingData(userCode, focussedVillage, state, district, taluka, othervillage, cropType, product,
                farmerName, farmerMobileNumber, trolleyNumber, totalDimensionLeft, totalDimensionRight, totalDimensionBack,
                totalDimensionFront, leftSideImgName, leftSideImgPath, leftSideImgStatus, rightSideImgName, rightSideImgPath, rightSideImgStatus,
                backSideImgName, backSideImgPath, backSideImgStatus,
                frontSideImgName, frontSideImgPath, frontSideImgStatus, isSynced,villagecode);

        if (fl) {
            Toast.makeText(context, "Save to Db", Toast.LENGTH_SHORT).show();
            //msclass.showMessage("data saved successfully.");
            uploadData("TrolleyPaintingData");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadData(String function) {
        String str = null;
        /*if (config.NetworkConnection()) {

            try {
                new UploadTrolleyPaintingServer(function, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else*/ {
            AlertDialog.Builder builder = new AlertDialog.Builder(TrolleyPaintingActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(TrolleyPaintingActivity.this);
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

    public String uploadTrolleyPaintingData(String function) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from TrolleyPaintingData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String leftSideImgName = jsonArray.getJSONObject(i).getString("leftSideImgName");
                    String leftSideImgPath = jsonArray.getJSONObject(i).getString("leftSideImgPath");

                    jsonArray.getJSONObject(i).put("leftSideImgPath",  mDatabase.getImageDatadetail(leftSideImgPath));

                    String rightSideImgName = jsonArray.getJSONObject(i).getString("rightSideImgName");
                    String rightSideImgPath = jsonArray.getJSONObject(i).getString("rightSideImgPath");

                    jsonArray.getJSONObject(i).put("rightSideImgPath",  mDatabase.getImageDatadetail(rightSideImgPath));


                    String backSideImgName = jsonArray.getJSONObject(i).getString("backSideImgName");
                    String backSideImgPath = jsonArray.getJSONObject(i).getString("backSideImgPath");

                    jsonArray.getJSONObject(i).put("backSideImgPath",  mDatabase.getImageDatadetail(backSideImgPath));


                    String frontSideImgName = jsonArray.getJSONObject(i).getString("frontSideImgName");
                    String frontSideImgPath = jsonArray.getJSONObject(i).getString("frontSideImgPath");

                    jsonArray.getJSONObject(i).put("frontSideImgPath",  mDatabase.getImageDatadetail(frontSideImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("TrolleyPaintingData", jsonObject.toString());
                    str = syncTrolleyPaintingDataImage(function, SERVER, jsonObject, leftSideImgName, leftSideImgPath,
                            rightSideImgName, rightSideImgPath, backSideImgName, backSideImgPath, frontSideImgName, frontSideImgPath);
                    handleTrolleyPaintingImageSyncResponse("TrolleyPaintingData", str,id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }
//AsyncTask Class for api batch code upload call

    private class UploadTrolleyPaintingServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadTrolleyPaintingServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadTrolleyPaintingData("TrolleyPaintingData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                if(jsonObject.has("success"))
                {
                    if(Boolean.parseBoolean(jsonObject.get("success").toString())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrolleyPaintingActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(TrolleyPaintingActivity.this);
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
                    }else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TrolleyPaintingActivity.this);
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TrolleyPaintingActivity.this);
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

    public synchronized String syncTrolleyPaintingDataImage(String function, String urls, JSONObject jsonObject, String leftSideImgName,
                                                            String leftSideImgPath, String rightSideImgName, String rightSideImgPath,
                                                            String backSideImgName, String backSideImgPath,
                                                            String frontSideImgName, String frontSideImgPath) {

        return HttpUtils.POSTJSON(SERVER,jsonObject,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));

    }


    public void handleTrolleyPaintingImageSyncResponse(String function, String resultout,String id) throws JSONException {
        if (function.equals("TrolleyPaintingData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateTrolleyPaintingData("0", "1", "1",
                            "1", "1", "1",id);

                } else {
                }
            }
        }

        Log.d("TrolleyPaintingData", "TrolleyPaintingData: " + resultout);
    }
}
