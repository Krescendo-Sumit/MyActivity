package myactvity.mahyco;
import android.app.Activity;
import android.app.Dialog;
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


import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.textfield.TextInputLayout;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.core.content.FileProvider;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import androidx.cardview.widget.CardView;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
import android.widget.CheckBox;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
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
import java.io.OutputStream;
        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

        import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
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

public class voiceofchanelpartner_be extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {


    private static final String TAG = "VOCP";
    private static final String IMAGE_DIRECTORY_NAME = "PHOTO";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public String cordinates = "";
    String  RetailerName,retailerfirmname, name ,mobileno,marketplace;

    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    CheckBox chktag;
    int REQUEST_CHECK_SETTINGS = 101;
    double lati;
    double longi;

    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    public String Imagepath1 = "";
  public String improve1="",improve2="",improve3="";
    SearchableSpinner spState, spDist, spTaluka, spVillage, spType, spFarmerSearch;
    SearchableSpinner  spImprovment,spImprovment2,spImprovment3;
    String[] companyArray, ratingArray, ratingArray1, ratingArray2;
    Button btnSubmit, btnNext, btnSearch;
    CardView cvRating, cvFeedback;
    TextView tvQuestion,lblothlack, lbllack,tvCordinates, tvAddress,tvVillage,
            tvTaluka,tvDist,tvState,myTextProgress;
    String cordinatesmsg = "TAG * \n";
    String address;
    EditText txtTotalRetailer,txtMFieldCrop,txtMCotton,txtMonth,txtYear,etSearchText, etName, etPhone, etAcres,
            etComment, etOther, etOther1, etOther2,etOther3, etOtherCompany,etOtherCompany2,
            etOtherCompany3,
            etOtherCompany1,etSuggestion, etNotAttend,etWhatup,txtOtherMCotton,txtOtherFieldCrop;


    //  String SERVER = "https://packhouse.mahyco.com/api/preseason/sanmanMela";
    String userCode;
    Config config;
    LinearLayout linearretailertot, llFocussedVillages, Linearlocation, LinearLayout02;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    RadioGroup radGrp;
    RadioButton rndone,rndtwo,rndthree,rndfour,
            radYesActivity2, radNoActivity2, radOtherActivity2,
             radOtherActivity1, radSuggestionActivity;
    String strReceiveInvite = "", strAttendSeminar = "", strIsEventUseful = "", strEasyToBuySeeds = "", strSeedsAvaliability = "",
            strEasySeedFeatures = "";
    TextInputLayout tiComments, tiPhone, tiName,tiOther,tiOther2,tiOther3;
    int count = 0;
    int tlqcount=0;
    int  selecttype=0;
    Prefs mPref;
    int imageselect;
    File photoFile = null;
    ImageView ivImage;

    ImageView imgBtnGps;
    private Context context;
    private String state, dist, taluka;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private long mLastClickTime = 0;
    private Handler handler = new Handler();

    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    int counter=1;
    String customercode="";
    public CommonExecution cx;
    JSONArray questionans =  new JSONArray();
    String questionlist[] =
            {"1 Product performance rating (Overall product performance, satisfaction of the farmers in terms of quality and yields etc)\n",
            " 2 Seed quality and germination (Appearance, size , Germination)\n",
            " 3 Promotional activities (PDA & PSA at village, farmer, trade  level)\n",
            " 4 Customer services (Farmer services, trade communications, interaction)\n",
            " 5 Packaging (Quality of primary and secondary bags, design, strength, legibility of matter )\n",
            " 6 Promotional activities (PDA & PSA at village, farmer, trade  level)\n",
            " 7 Effective problems solving (Lead time , satisfaction level of solution)\n",
            " 8 Pricing policy (Invoice prices, farmer prices etc)\n",
            " 9 Trade discounts ( Distributors and retailers margins, other scheme discounts, timing of credit notes issuance)\n",
            " 10 Sales returns policy\n",
            " 11 Credit limit and payment terms ( Kharif and rabi crops credit period, limits and payment terms )\n",
            " 12 Promotional schemes (Farmer and trade related promotional schemes , booking , discounts etc  understanding and communication)\n",
            " 13 Ease of doing business ( Overall feedback about doing  Mahyco business)\n"
                   };
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

        try {
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
        }
        catch (Exception ex )
        {
            result = false;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiceofchanelpartner_be);
        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPref = Prefs.with(this);
        config = new Config(this); //Here the context is passing
        context = this;
        // userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode =  pref.getString("UserID", null);
        cx=new CommonExecution(this);
        mPref.save(AppConstant.USER_CODE_TAG, userCode);
        msclass = new Messageclass(this);
        mDatabase = SqliteDatabase.getInstance(this);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);

        myTextProgress = (TextView) findViewById(R.id.myTextProgress);

        tvState = (TextView) findViewById(R.id.tvState);
        tvDist = (TextView) findViewById(R.id.tvDist);
        tvTaluka = (TextView) findViewById(R.id.tvTaluka);
        tvVillage = (TextView) findViewById(R.id.tvVillage);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spType = (SearchableSpinner) findViewById(R.id.spType);


        spFarmerSearch = (SearchableSpinner) findViewById(R.id.spFarmerSearch);
        etOther = findViewById(R.id.etOther);
        spImprovment = (SearchableSpinner) findViewById(R.id.spImprovment);

        tiOther = findViewById(R.id.tiOther);
        etOtherCompany = findViewById(R.id.etOtherCompany);

        spImprovment2 = (SearchableSpinner) findViewById(R.id.spImprovment2);
        tiOther2 = findViewById(R.id.tiOther2);
        etOtherCompany2 = findViewById(R.id.etOtherCompany2);

        spImprovment3 = (SearchableSpinner) findViewById(R.id.spImprovment3);
        tiOther3 = findViewById(R.id.tiOther3);
        etOtherCompany3 = findViewById(R.id.etOtherCompany3);

        LinearLayout02 = findViewById(R.id.LinearLayout02);
        Linearlocation = findViewById(R.id.Linearlocation);
        linearretailertot = findViewById(R.id.linearretailertot);
        cvFeedback = findViewById(R.id.cvFeedback);
        cvRating = findViewById(R.id.cvRating);

        etSearchText = findViewById(R.id.etSearchText);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etWhatup = findViewById(R.id.etWhatup);
        txtOtherMCotton = findViewById(R.id.txtOtherMCotton);
        txtOtherFieldCrop = findViewById(R.id.txtOtherFieldCrop);
        txtTotalRetailer= findViewById(R.id.txtTotalRetailer);
                txtMFieldCrop= findViewById(R.id.txtMFieldCrop);
        txtMCotton= findViewById(R.id.txtMCotton);
                txtMonth= findViewById(R.id.txtMonth);
                txtYear= findViewById(R.id.txtYear);

        tiOther = findViewById(R.id.tiOther);
        rndone = (RadioButton)findViewById(R.id.rndone);
        rndtwo = (RadioButton)findViewById(R.id.rndtwo);
        rndthree = (RadioButton)findViewById(R.id.rndthree);
        rndfour = (RadioButton)findViewById(R.id.rndfour);
        radGrp = (RadioGroup)findViewById(R.id.radGrp);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext= findViewById(R.id.btnNext);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        lblothlack=(TextView) findViewById(R.id.lblothlack);
        lbllack=(TextView) findViewById(R.id.lbllack);
        String first = "TOTAL ANNUAL SALES OTHER THAN MAHYCO ";
        String next = "<font color='#EE0000'>(IN LAKHS)</font>";
        lblothlack.setText(Html.fromHtml(first + next));

        lbllack.setText(Html.fromHtml("MAHYCO SALES <font color='#EE0000'>(IN LAKHS)</font>"));

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearrating);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(false);
        }



       // bindFarmerDetail(null);
        bindFarmerCategory();
        bindState();
        bindBrand();

        //onSearchBtn();
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                   // if(parent.getSelectedItemPosition()!=0 )//&& parent.getSelectedItemPosition()!=1)
                    {
                        // set the UI visibility

                        GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                        resetdropdownlist();
                        //getSearchData(new JSONObject(gm.Code()));
                         if (gm.Code().toString().equals("1")) // distribitor bind
                         {
                             binddistributor();
                             selecttype=1; // distributor selection
                            linearretailertot.setVisibility(View.VISIBLE);
                         }
                        if (gm.Code().toString().equals("2")) // retailer  bind
                        {

                            selecttype=2; // retailer selection
                            //  Linearlocation.setVisibility(View.GONE);
                            linearretailertot.setVisibility(View.GONE);
                        }
                        setUIVisibility();


                    }
                    /*else if(parent.getSelectedItemPosition()==1) {

                        Intent intent = new Intent(voiceofchanelpartner_be.this, ActivityKisanClub.class);
                        startActivity(intent);

                        // setUIVisibilityGone();

                    }else {

                        //  setUIVisibilityGone();
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spFarmerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    etName.setText("");
                    etPhone.setText("");
                    etWhatup.setText("");

                    if (spFarmerSearch.getSelectedItem().toString().toLowerCase().contains("retailer not found") )
                    {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("My Activity App");
                        // Setting Dialog Message
                        alertDialog.setMessage("ADD NEW RETAILER FOR VCP . ");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                if (validationnotfound()!=true)
                                {
                                    return ;
                                }
                                CallNewRetailerPOP();

                            }


                        });


                        android.app.AlertDialog alert = alertDialog.create();
                        alert.show();
                        final Button positiveButton = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.weight = 10;
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);
                        //end




                    }
                   else {

                       // if (parent.getSelectedItemPosition() != 0 )//&& parent.getSelectedItemPosition() != 1)
                            if (!spFarmerSearch.getSelectedItem().toString().toLowerCase().contains("select retailer") )

                             {
                            // set the UI visibility

                            GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();

                            getSearchData(new JSONObject(gm.Code()));

                            setUIVisibility();


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
                    if (!gm.Code().equals("0")) {
                        if (tlqcount>1) {
                             if (selecttype==2) {
                                 BindRetaileronline(taluka, "");
                             }
                        }
                        tlqcount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spImprovment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                //GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                //GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    improve1=gm.Desc();
                   /* if(improve1.equals(improve2) || improve1.equals(improve3)) {
                      msclass.showMessage("You can't select the same improvement option more than once");
                        spImprovment.setSelection(0);
                        spImprovment2.setSelection(0);
                        spImprovment3.setSelection(0);
                    }
                    else {*/
                        if (gm.Desc().trim().contains("Other") || gm.Desc().trim().contains("OTHER")) {

                            // visible other input field
                            tiOther.setVisibility(View.VISIBLE);
                             


                        } else {
                            
                            tiOther.setVisibility(View.GONE);
                            etOtherCompany.setText("");
                            if(improve1.equals(improve2) || improve1.equals(improve3)) {
                                msclass.showMessage("You can't select the same improvement option more than once");
                                spImprovment.setSelection(0);
                                spImprovment2.setSelection(0);
                                spImprovment3.setSelection(0);
                            }
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spImprovment2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    improve2=gm.Desc();
                   /* if(improve2.equals(improve1) || improve2.equals(improve3)) {
                        msclass.showMessage("You can't select the same improvement option more than once");
                        spImprovment.setSelection(0);
                        spImprovment2.setSelection(0);
                        spImprovment3.setSelection(0);
                    }
                    else {*/
                        if (gm.Desc().trim().contains("Other") || gm.Desc().trim().contains("OTHER")) {

                            // visible other input field
                            tiOther2.setVisibility(View.VISIBLE);


                        } else {
                            tiOther2.setVisibility(View.GONE);
                            etOtherCompany2.setText("");
                            if(improve2.equals(improve1) || improve2.equals(improve3)) {
                        msclass.showMessage("You can't select the same improvement option more than once");
                        spImprovment.setSelection(0);
                        spImprovment2.setSelection(0);
                        spImprovment3.setSelection(0);
                    }
                        }
                   // }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spImprovment3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    improve3=gm.Desc();
                   /* if(improve3.equals(improve1) || improve3.equals(improve2)) {
                        msclass.showMessage("You can't select the same improvement option more than once");
                        spImprovment.setSelection(0);
                        spImprovment2.setSelection(0);
                        spImprovment3.setSelection(0);
                    }
                    else {*///
                        if (gm.Desc().trim().contains("Other") || gm.Desc().trim().contains("OTHER")) {

                            // visible other input field
                            tiOther3.setVisibility(View.VISIBLE);


                        } else {
                            tiOther3.setVisibility(View.GONE);
                            etOtherCompany3.setText("");
                            if(improve3.equals(improve1) || improve3.equals(improve2)) {
                        msclass.showMessage("You can't select the same improvement option more than once");
                        spImprovment.setSelection(0);
                        spImprovment2.setSelection(0);
                        spImprovment3.setSelection(0);
                    }
                        }
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        //onTouchListener();

        if (checkPlayServices()) {
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }
        onNextBtnClicked();
        onSubmitBtnClicked();


    }

    public void CallNewRetailerPOP()
    {
        try
        {
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.vcpnewretailerpopup);
             /* s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L); */
            final LinearLayout tblLayout = (LinearLayout)dialog.findViewById(R.id.li1);
            // TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            //TableRow row = (TableRow)tblLayout.getChildAt(1);
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final EditText txtretailername = (EditText)dialog.findViewById(R.id.txtretailername);
            final EditText txtretailerfirmname = (EditText) dialog.findViewById(R.id.txtretailerfirmname);
            final EditText txtretailermobileno = (EditText)dialog.findViewById(R.id.txtretailermobileno);
            final EditText txtMarketPlace = (EditText)dialog.findViewById(R.id.txtMarketPlace);



            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            String [] array=null;
            array = new String[1];
            array[0]="SELECT PRODUCT";
            retailerfirmname =txtretailerfirmname.getText().toString().trim();
            RetailerName=txtretailername.getText().toString().trim();
            mobileno =txtretailermobileno.getText().toString().trim();



            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        // call Save Retailer Api
                        if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                            msclass.showMessage("Please select state");
                            return ;
                        }
                        if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                            msclass.showMessage("Please select district");
                            return ;
                        }
                        if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                            msclass.showMessage("Please Select Taluka");
                            return ;
                        }
                        if (txtretailername.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer name ");
                            return;
                        }
                        if (txtretailerfirmname.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer firm name ");
                            return;
                        }
                        if (txtretailermobileno.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer mobile number  ");
                            return;
                        }
                        if (txtretailermobileno.getText().length() != 10) {
                            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
                            return ;
                        }
                        if (txtMarketPlace.getText().length()==0) {
                            Utility.showAlertDialog("Info", "Please Enter market place", context);
                            return ;
                        }

                        retailerfirmname =txtretailerfirmname.getText().toString().trim();
                        RetailerName=txtretailername.getText().toString().trim();
                        mobileno =txtretailermobileno.getText().toString().trim();
                        marketplace =txtMarketPlace.getText().toString().trim();
                        String str= addPOGretaill();
                        if (str.contains("True"))
                        {
                            txtretailername.setText("");
                            txtretailermobileno.setText("");
                            txtretailerfirmname.setText("");
                            txtMarketPlace.setText("");
                            msclass.showMessage("New retailer data saved successfully");
                            spFarmerSearch.setSelection(0);
                            spTaluka.setSelection(0);

                            BindRetailerList_online(str);
                        }
                        else
                        {
                            msclass.showMessage(str.replace("\"",""));
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });

            dialog.show();
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            //Window window = dialog.getWindow();
            //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        } catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }



    }
    public String  addPOGretaill()
    {
        String  str="";
        if (config.NetworkConnection() )
        {
            try {
                // action 2  save the VCP retailer and get data
                str = new GetRetailerListOnline("2", userCode, context
                        ).execute().get();

            }
            catch (Exception ex )
            {
                str=ex.getMessage();
            }
        }
        else
        {
            str="Please check internet connection";
        }
        return str;
    }
    private boolean validationnotfound() {
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
            if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }







        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
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
    public void BindRetaileronline(String taluka,String retailerCategory)
    {
        if (config.NetworkConnection() )
        {

            try {
                relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Searching Data");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });

                callRetailerAPI();

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                container.setEnabled(false);
                container.setClickable(false);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
        {
            msclass.showMessage("Please check internet connection");
        }
    }
    private void binddistributor()
    {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Searching Data");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });
                // resetActivity();
               // doSearch();
                calldistributorAPI();

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                container.setEnabled(false);
                container.setClickable(false);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callRetailerAPI() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        // action 3  get taluka base retailer list
                        new GetRetailerListOnline( "3","SetGetRetailerListOnline", context).execute(Constants.FIELDBANNER_SERVER_API);
                       // new getDistributorVOCP("getDistributorVOCP", context).execute(Constants.FIELDBANNER_SERVER_API);

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
    private void calldistributorAPI() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        new getDistributorVOCP("getDistributorVOCP", context).execute(Constants.FIELDBANNER_SERVER_API);

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




    private void onNextBtnClicked() {


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                try
                {
                    if (radGrp.getCheckedRadioButtonId()==-1)
                    {
                        msclass.showMessage("Please select the answer.");
                        return ;
                    }
                    if(questionans.length()==13)
                    {
                        counter=0;
                        msclass.showMessage("All questions rating are  selected ,Please  click on submit  button.");

                    }
                    else
                    {
                         RadioButton radioButton;
                        // get selected radio button from radioGroup

                        int selectedId = radGrp.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioButton = (RadioButton) findViewById(selectedId);
                        JSONObject idsJsonObject = new JSONObject();
                        idsJsonObject.put("questionNo", counter);
                        idsJsonObject.put("answer", radioButton.getText());
                        idsJsonObject.put("answerId", radioButton.getId());
                        idsJsonObject.put("userCode", userCode);
                        idsJsonObject.put("customerCode", customercode);
                        if(questionans.length()<13) {
                            questionans.put(idsJsonObject);
                            tvQuestion.setText(questionlist[counter-1].toString());
                            counter++;
                        }
                        radGrp.clearCheck();

                    }


                }
                catch(Exception ex )
                {
                    Log.d(TAG, "Validation Function : "+ex.toString());
                }
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



 private void resetdropdownlist()
 {

    // Config.refreshActivity(voiceofchanelpartner_be.this);
       spState.setSelection(0);
       spDist.setSelection(0);
      tlqcount=1;
      counter=1;
      spTaluka.setSelection(0);
      spFarmerSearch.setSelection(0);
      etName.setText("");
      etPhone.setText("");
      etWhatup.setText("");
     txtMCotton .setText("");
     txtMFieldCrop  .setText("");
     txtOtherMCotton  .setText("");
     txtOtherFieldCrop   .setText("");
     txtTotalRetailer   .setText("");
     txtYear.setText("");
     txtMonth.setText("");
    // questionans=null;
     spImprovment.setSelection(0);
     spImprovment2.setSelection(0);
     spImprovment3.setSelection(0);
    // questionlist=null;
     //questionans.
    // questionans = new JSONArray();
     while(questionans.length()>0)
     {
         questionans.remove(0);
     }
     etOtherCompany.setText("");
     etOtherCompany2.setText("");
     etOtherCompany3.setText("");
     tvQuestion.setText(questionlist[counter-1].toString());


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


        //new VOFPSearchApiCall("SearchDetail").execute();


    }

    //AsyncTask Class for api get retailer list
    private class GetRetailerListOnline extends AsyncTask<String, String, String>
    {

        private ProgressDialog p;
        String action ;

        public GetRetailerListOnline(String action,String Funname, Context context) {
this.action=action;
        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            String returnstring = "";
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SetGetRetailerListOnline"));
            postParameters.add(new BasicNameValuePair("Taluka", taluka));
            postParameters.add(new BasicNameValuePair("Dist", dist));
            postParameters.add(new BasicNameValuePair("State", state));
            postParameters.add(new BasicNameValuePair("retailerName", RetailerName));
            postParameters.add(new BasicNameValuePair("retailerFirmName", retailerfirmname));
            postParameters.add(new BasicNameValuePair("retailerMobileNo", mobileno));
            postParameters.add(new BasicNameValuePair("retailerCategory", "VCP"));
            postParameters.add(new BasicNameValuePair("usercode", userCode));
            postParameters.add(new BasicNameValuePair("action", action));
            postParameters.add(new BasicNameValuePair("cordinates", cordinates));
            postParameters.add(new BasicNameValuePair("address", address));
            postParameters.add(new BasicNameValuePair("marketplace", "marketplace"));
            String Urlpath1 = cx.MDOurlpath;
            HttpPost httppost = new HttpPost(Urlpath1);
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
                    returnstring = builder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();

            }


            return builder.toString();

        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                // JSONObject jsonObject = new JSONObject(result);

                    BindRetailerList_online(result);


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());

            }

        }
    }
     public void  BindRetailerList_online(String str)
    {
              try {
                    spFarmerSearch.setAdapter(null);

                    List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                    Croplist.add(new GeneralMaster("0",
                            "SELECT RETAILER"));

                    if (config.NetworkConnection()) {
                        try {
                            JSONObject object = new JSONObject(str);
                            JSONArray jArray = object.getJSONArray("Table");
                            for (int i = 0; i < jArray.length(); i++) {

                                Croplist.add(new GeneralMaster((jArray.get(i)).toString(),
                                        new JSONObject(jArray.get(i).toString()).getString("firmname")) );
                            }
                           /* for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);
                                //Croplist.add(new GeneralMaster(jObject.getString("mobileno"),jObject.getString("firmname")
                                 Croplist.add(new GeneralMaster((jObject.get(i)).toString(),
                                                new JSONObject(jObject.get(i).toString()).getString("firmname") + ");



                                ));
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        msclass.showMessage("Internet network not available.");
                    }
                  Croplist.add(new GeneralMaster("RETAILER NOT FOUND (ADD THE RETAILER)",
                          "RETAILER NOT FOUND (ADD THE RETAILER)"));
                  progressBarVisibility();
                    ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                            (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spFarmerSearch.setAdapter(adapter);

                }
                catch (Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }



    }

    //AsyncTask Class for api batch code upload call
    private class getDistributorVOCP extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public getDistributorVOCP(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return getdistributorlist();//Getdistributor("getDistributorVOCP");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);

                if(resultout!=null) {
                    JSONObject jsonObject = new JSONObject(resultout);
                   // if (jsonObject.has("success")) {
                    if (jsonObject.has("success")) {
                        if (jsonObject.has("vocpDistributors")) {

                            if (jsonObject.getString("vocpDistributors").length() == 0) {
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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
                                //if((new JSONArray(jsonObject.getString("Table")).length()!=0)) {
                                 //   binddistributordata(new JSONArray(jsonObject.getString("Table")));
                                //}
                                if((new JSONArray(jsonObject.getString("vocpDistributors")).length()!=0)) {
                                    binddistributordata(new JSONArray(jsonObject.getString("vocpDistributors")));
                                }
//
                                //  getSearchData(jsonObject);


                            }

                        }

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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
    private String Getdistributor(String function) {

        String str = "";
        int action = 1;


            try {

                     JSONObject jsonObject = new JSONObject();
                     JSONObject jsonObjchild = new JSONObject();

                     jsonObjchild.put("userCode", userCode);
                      jsonObjchild.put("action", "2");
                     JSONArray jaheader = new JSONArray();
                     //jaheader.put(jsonObjchild) ;
                     jsonObject.put("Table",jsonObjchild);// jaheader);
                     Log.d("getdistributorlist", jsonObject.toString());
                   str= HttpUtils.POSTDatabyte(function, cx.MDOurlpath,
                        1,jsonObject);






            } catch (Exception e) {
                e.printStackTrace();
            }



        return str;
    }
    private String getdistributorlist() {

        String str = "";
        int action = 1;


        try {

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObjchild = new JSONObject();

            jsonObjchild.put("userCode", userCode);
            jsonObjchild.put("action", "2");
            JSONArray jaheader = new JSONArray();
            //jaheader.put(jsonObjchild) ;
            jsonObject.put("Table",jsonObjchild);// jaheader);
            Log.d("getdistributorlist", jsonObject.toString());
           // return HttpUtils.p(searchUri.toString(),mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
            str= HttpUtils.POSTJSON(Constants.VCP_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));







        } catch (Exception e) {
            e.printStackTrace();
        }



        return str;
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
                    if (jsonObject.has("success")){
                       // if (result.contains("True")) {

                        if (jsonObject.has("Table")) {

                            if (jsonObject.getString("Table").length() == 0) {
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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
                                if((new JSONArray(jsonObject.getString("Table")).length()!=0)) {
                                    binddistributordata(new JSONArray(jsonObject.getString("Table")));
                                }
//
                                //  getSearchData(jsonObject);


                            }

                        }

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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

    private void binddistributordata(JSONArray vofpData) {


        try {

            spFarmerSearch.setAdapter(null);
            String str = null;
            try {

                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                List<GeneralMaster> list1 = new ArrayList<GeneralMaster>();

                list.add(new GeneralMaster("0",
                        "SELECT FIRM NAME"));

               // list.add(new GeneralMaster("RETAILER FIRM(NOT FOUND)",
                  //      "RETAILER FIRM(NOT FOUND)"));

                if(vofpData!=null) {

                    for (int i = 0; i < vofpData.length(); i++) {

                        list.add(new GeneralMaster((vofpData.get(i)).toString(),
                                new JSONObject(vofpData.get(i).toString()).getString("CUSTOMERNAME") + "," + new JSONObject(vofpData.get(i).toString()).getString("CUSTOMERID")));
                    }




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
            GeneralMaster gm = (GeneralMaster) spType.getSelectedItem();
             if (gm.Code().equals("1")) {
                 txtMCotton.setText(object.getString("cotton"));
                 txtMFieldCrop.setText(object.getString("fieldcrop"));
                 etPhone.setText(object.getString("MOBILENO"));
                 txtMonth.setText(object.getString("bmonth"));
                 etName.setText(object.getString("CUSTOMERNAME"));
                 txtYear.setText(object.getString("byear"));
                 customercode=object.getString("CUSTOMERID");
                 txtMonth.setEnabled(false);
                 txtYear.setEnabled(false);
                 txtMCotton.setEnabled(false);
                 txtMFieldCrop.setEnabled(false);
             }
            if (gm.Code().equals("2")) {
                //txtMCotton.setText(object.getString("cotton"));
                //txtMFieldCrop.setText(object.getString("fieldcrop"));
                etPhone.setText(object.getString("mobileno"));
               // txtMonth.setText(object.getString("bmonth"));
                etName.setText(object.getString("name"));
                //txtYear.setText(object.getString("byear"));
                customercode=object.getString("mobileno");
                txtMonth.setEnabled(true);
                txtYear.setEnabled(true);
                txtMCotton.setEnabled(true);
                txtMFieldCrop.setEnabled(true);
                //etName.setEnabled(false);
                // etPhone.setEnabled(false);
            }
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
                        if (validation())
                        {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);

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
                        }
                        //else {
                         //   msclass.showMessage("All fields are mandatory");
                       // }
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

                        uploadVOCP();
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

    private void uploadVOCP() {

        JSONObject requestParams = new JSONObject();
        JSONObject farmerRequest = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            farmerRequest.put("Id", "0");
            farmerRequest.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            farmerRequest.put("Type", spType.getSelectedItem());
            farmerRequest.put("State", spState.getSelectedItem());
            farmerRequest.put("District", spDist.getSelectedItem());
            farmerRequest.put("Taluka", spTaluka.getSelectedItem());
            farmerRequest.put("Firmname",spFarmerSearch.getSelectedItem());

            farmerRequest.put("customerName", etName.getText());
            farmerRequest.put("customerCode", customercode);

            farmerRequest.put("MobileNo", etPhone.getText());
            farmerRequest.put("WhatSAPP", etWhatup.getText());
            farmerRequest.put("cottonOther", txtOtherMCotton.getText());
            farmerRequest.put("fieldOther", txtOtherFieldCrop.getText());



            //farmerRequest.put("StaffListens", spListens.getSelectedItem());
           // farmerRequest.put("StaffShareContact", spShareCont.getSelectedItem());

            farmerRequest.put("year", txtYear.getText());
            farmerRequest.put("month", txtMonth.getText());
            farmerRequest.put("mahycoCotton", txtMCotton.getText());
            farmerRequest.put("mahycoFieldCrop", txtMFieldCrop.getText());
            farmerRequest.put("totalRetailer", txtTotalRetailer.getText());
            //farmerRequest.put("improvement", spImprovment.getSelectedItem());
           // farmerRequest.put("improvement2", spImprovment2.getSelectedItem());
           // farmerRequest.put("improvement3", spImprovment2.getSelectedItem());

            if(!spImprovment.getSelectedItem().toString().toLowerCase().contains("other")) {
                farmerRequest.put("improvement", spImprovment.getSelectedItem());
            }else {
                farmerRequest.put("improvement", spImprovment.getSelectedItem() + "(" + etOtherCompany.getText() + ")");

            }
            if(!spImprovment2.getSelectedItem().toString().toLowerCase().contains("other"))
            {
                farmerRequest.put("improvement2", spImprovment2.getSelectedItem());
                //farmerRequest.put("improvement", spImprovment.getSelectedItem());
            }else {
                farmerRequest.put("improvement2", spImprovment2.getSelectedItem() + "(" + etOtherCompany2.getText() + ")");

            }
            if(!spImprovment3.getSelectedItem().toString().toLowerCase().contains("other")) {
                farmerRequest.put("improvement3", spImprovment3.getSelectedItem());
                //farmerRequest.put("improvement", spImprovment.getSelectedItem());
            }else {
                farmerRequest.put("improvement3", spImprovment3.getSelectedItem() + "(" + etOtherCompany3.getText() + ")");
                // farmerRequest.put("OtherImprovement", spImprovment.getSelectedItem() );
            }

            farmerRequest.put("TaggedAddress", address);
            farmerRequest.put("TaggedCoordinates", cordinates);

            //String farmerImgStatus = "0";
            Date entrydate = new Date();
            //final String farmerImgPath;
            //farmerImgPath = Imagepath1;

            //final String farmerImgName =AppConstant.Imagename ;// "VOFPPhoto" + mPref.getString(AppConstant.USER_CODE_TAG, "") + String.valueOf(entrydate.getTime());


            farmerRequest.put("entryDt", getCurrentDate());
            farmerRequest.put("questionans", questionans);
          //  jsonArray.put(farmerRequest);
            requestParams.put("Table", farmerRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("FarmerPartnerData", requestParams.toString());
        new VOCPApiCall("VOCPApiCall", requestParams).execute();
        // progressBarVisibility();


    }

    /**
     * <P>Method to upload the distributor survey data to server</P>
     *
     * @param function
     * @param obj
     * @return
     */
    private String uploadVOCPData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.VOCP_SERVER_API, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    private boolean validation() {

        boolean ret = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        GeneralMaster gm = (GeneralMaster) spType.getSelectedItem();


        if (gm.Code().toString().equals("0"))
        {
            msclass.showMessage("Please select  type ");
           return false;

        }

        //else
       // {
          //  if (gm.Code().toString().equals("2"))  // Retailer
           // {
                if (spState.getSelectedItem().toString().equalsIgnoreCase("SELECT STATE")) {

                    msclass.showMessage("Please select  state ");
                    return false;

                }
                if (spDist.getSelectedItem().toString().equalsIgnoreCase("SELECT DISTRICT")) {

                    msclass.showMessage("Please select  district ");
                    return false;

                }

                if (spTaluka.getSelectedItem().toString().equalsIgnoreCase("SELECT TALUKA")) {

                    msclass.showMessage("Please select  taluka ");
                    return false;

                }
           // }

        //}
        if (spFarmerSearch.getSelectedItem().toString().equalsIgnoreCase("SELECT FIRM")) {

            msclass.showMessage("Please select  firm name ");
            return false;

        }
        if (etName.getText().length()==0) {

            msclass.showMessage("Please enter the customer name  ");
            return false;

        }
        if (etPhone.getText().length() != 10) {
            msclass.showMessage("Please  enter valid phone no");
            return false;
        }
        if (txtOtherMCotton.getText().length() == 0) {
            msclass.showMessage("Please  enter cotton sales other than mahyco");
            return false;
        }
        if (txtOtherFieldCrop .getText().length() == 0) {
            msclass.showMessage("Please  enter field crop sales other than mahyco");
            return false;
        }

        if (txtYear .getText().length() == 0) {
            msclass.showMessage("Please  enter  associate year with mahyco .");
            return false;
        }
        if (txtYear .getText().length() >0 ) {
            if (  Integer.parseInt(txtYear .getText().toString()) ==0)
            {
                if (txtMonth .getText().length() ==0) {
                    msclass.showMessage("please enter associate with mahyco month value  between 1 to  11 .  ");
                    return false;
                }
            }
        }
        if (txtMonth .getText().length() >0 ) {
            if (  Integer.parseInt(txtMonth .getText().toString()) >11) {
                msclass.showMessage("please enter month value between 1 to  11 .  ");
                return false;
            }
        }
        if (txtMCotton .getText().length() == 0) {
            msclass.showMessage("Please  enter  mahyco cotton sales(in lakhs)");
            return false;
        }
        if (txtMFieldCrop  .getText().length() == 0) {
            msclass.showMessage("Please  enter  mahyco field crop sales(in lakhs)");
            return false;
        }
          if (gm.Code().toString().equals("1"))  // distributor
          {
              if (txtTotalRetailer.getText().length() == 0) {
                  msclass.showMessage("Please  enter total retailer count.");
                  return false;
              }
          }

        if(questionans.length()<13)
        {
            msclass.showMessage("Please select  the rating of all questions");
            return false;
        }
       /* if (questionans.length()!=13)
        {
            msclass.showMessage("Please give the rating of all questions");
            return false;
        }*/
        if (spImprovment.getSelectedItem().toString().equalsIgnoreCase("1ST AREA OF IMPROVEMENT")) {

            msclass.showMessage("Please select area of improvement for mahyco.");
            return false;
        }else if(spImprovment.getSelectedItem().toString().toLowerCase().contains("other")) {
            if (etOtherCompany.getText().length() == 0) {
                msclass.showMessage("Please enter(other) area of improvement for mahyco.");
                return false;
            }
        }
        if(spImprovment2.getSelectedItem().toString().toLowerCase().contains("other")) {
            if (etOtherCompany2.getText().length() == 0) {
                msclass.showMessage("Please enter(other) area of improvement2 for mahyco.");
                return false;
            }
        }
        if(spImprovment3.getSelectedItem().toString().toLowerCase().contains("other")) {
            if (etOtherCompany3.getText().length() == 0) {
                msclass.showMessage("Please enter(other) area of improvement3 for mahyco.");
                return false;
            }
        }
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

        /*if (etName.getText().length() == 0) {
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
        }*/


       /*

        if (spVillage.getSelectedItem().toString().equalsIgnoreCase("SELECT VILLAGE")) {

            setErrorTextColor((TextView) spVillage.getSelectedView());
            ret = false;

        }

        */

        // comment by mahendra
        /*
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
 */









/*
        if (spImprovment.getSelectedItem().toString().equalsIgnoreCase("SELECT AREA OF IMPROVEMENT")) {

            setErrorTextColor((TextView) spImprovment.getSelectedView());
            ret = false;
        }else if(spImprovment.getSelectedStrings().contains("Other")){
            if(etOtherCompany.getText().length()==0){
                tiOther.setError("Please  enter other improvement details");
                ret=false;
            }else {
                tiOther.setError("");

            }
        } */





        /* if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please click photo");
            ret = false;
        }*/


      //  ret = true;
        return true;
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
        spImprovment.setAdapter(null);
        spImprovment2.setAdapter(null);
        spImprovment3.setAdapter(null);

        try {



            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "1ST AREA OF IMPROVEMENT"));
            gm.add(new GeneralMaster("1", "PRODUCT PORTFOLIO"));
            gm.add(new GeneralMaster("2", "MARKET DEVELOPMENT ACTIVITIES"));
            gm.add(new GeneralMaster("3", "COMPLAINT HANDLING"));
            gm.add(new GeneralMaster("4", "STAFF RESPONSIVENESS"));
            gm.add(new GeneralMaster("5", "OTHER"));

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spImprovment.setAdapter(adapter);

            List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
            gm2.add(new GeneralMaster("0", "2ST AREA OF IMPROVEMENT"));
            gm2.add(new GeneralMaster("1", "PRODUCT PORTFOLIO"));
            gm2.add(new GeneralMaster("2", "MARKET DEVELOPMENT ACTIVITIES"));
            gm2.add(new GeneralMaster("3", "COMPLAINT HANDLING"));
            gm2.add(new GeneralMaster("4", "STAFF RESPONSIVENESS"));
            gm2.add(new GeneralMaster("5", "OTHER"));

            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spImprovment2.setAdapter(adapter2);

            List<GeneralMaster> gm3 = new ArrayList<GeneralMaster>();
            gm3.add(new GeneralMaster("0", "3ST AREA OF IMPROVEMENT"));
            gm3.add(new GeneralMaster("1", "PRODUCT PORTFOLIO"));
            gm3.add(new GeneralMaster("2", "MARKET DEVELOPMENT ACTIVITIES"));
            gm3.add(new GeneralMaster("3", "COMPLAINT HANDLING"));
            gm3.add(new GeneralMaster("4", "STAFF RESPONSIVENESS"));
            gm3.add(new GeneralMaster("5", "OTHER"));
            ArrayAdapter<GeneralMaster> adapter3 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm3);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spImprovment3.setAdapter(adapter3);

        }
        catch (Exception ex) {
            msclass.showMessage(ex.getMessage());

        }

    }



    private void bindFarmerCategory() {
        spType.setAdapter(null);
        spFarmerSearch.setAdapter(null);

            try {

                spType.setAdapter(null);
                List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
                gm.add(new GeneralMaster("0", "SELECT TYPE"));
                gm.add(new GeneralMaster("1", "DISTRIBUTOR"));
                gm.add(new GeneralMaster("2", "RETAILER"));

                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, gm);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spType.setAdapter(adapter);

            }
            catch (Exception ex) {
            msclass.showMessage(ex.getMessage());

        }
            // Bind intial data

        try {

            spFarmerSearch.setAdapter(null);
            List<GeneralMaster> gm1 = new ArrayList<GeneralMaster>();
            gm1.add(new GeneralMaster("0", "SELECT FIRM"));
            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm1);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFarmerSearch.setAdapter(adapter2);

        }
        catch (Exception ex) {
            msclass.showMessage(ex.getMessage());

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
                mDatabase.close();
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
                mDatabase.close();
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
                mDatabase.close();
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
            mDatabase.close();

        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }


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
            address = getCompleteAddressString(lati, longi);
            Log.d(TAG, "onlocation" + cordinates);
             tvCordinates.setText(cordinates + "\n" + address);
             tvAddress.setText(address + "\n" + cordinates);
            //manageGeoTag();
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
                address = "ADDRESS : " + addresses.get(0).getAddressLine(0);
            if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                tvCordinates.setText(cordinates + "\n" + address);
                tvAddress.setText(address +"\n"+ cordinates);
            } else {

                tvCordinates.setText(cordinates + "\n" + address);
                tvAddress.setText(address +"\n"+ cordinates);
            }
            strAdd=address;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(voiceofchanelpartner_be.this);

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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class VOCPApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public VOCPApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadVOCPData(function, obj);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                redirecttoRegisterActivity(result);
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success"))
                {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(voiceofchanelpartner_be.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(voiceofchanelpartner_be.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else
                        {

                            if (jsonObject.get("success").toString().contains("false")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(voiceofchanelpartner_be.this);
                                builder.setTitle("MyActivity");
                                builder.setMessage(jsonObject.get("message").toString());
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Config.refreshActivity(voiceofchanelpartner_be.this);
                                        dialog.dismiss();
                                        progressBarVisibility();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                progressBarVisibility();
                            }
                            else {

                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(voiceofchanelpartner_be.this);
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
