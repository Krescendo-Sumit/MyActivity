package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;

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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.MultiSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SearchableSpinner2;
import myactvity.mahyco.helper.SqliteDatabase;

import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class BCFCallTBM extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback, LocationListener,TextWatcher {
    private static final String TAG = "RetailerPOG";

    public EditText txtIrrigationdt, txtirrigation,
            txtQty;
    public SearchableSpinner spDist, spnewretailer, spState, spVillage, spCropType, spProductName, spMyactvity, spComment;
    private long mLastClickTime = 0;

    MultiSelectionSpinner spdistr, spBussiness;
    RadioButton rndNo, rndYes;
    EditText txtrtname, txtmobilewhatup,
            txtBirthdate, txtyearofexperiance, txtComments, txtfirmname;
    ProgressDialog dialog;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spproductlist, spdistributor, spdivision, spsaleorg;
    public SearchableSpinner2 spTaluka, spRDNlist, spRetailerCategory;
    public Messageclass msclass;
    public CommonExecution cx;
    String division, usercode, org, cmbDistributor;
    public Button btnsave, btnCompetitatorPOG, btnPOG;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";

    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    Switch switchYN, switchSKYN;
    private String dist, taluka, state, village, whatsup, firmname, comments;
    String RetailerName, retailerfirmname, customer, name, DLV_plant,
            customregroup, cmbproductlist, mobileno, retailerCategory, userCode;
    TextView lbltaluka;
    EditText etArea, etWhatsappNumber, etFarmerName, etMobileNo, etComments;
    //For GPs location
    String cordinates;
    String address="";
    String croptype;
    String cordinatesmsg = "ADDRESS TAG : *";
    LinearLayout llOtherVillages;
    File photoFile = null;
    private TextInputLayout tiOtherVillage;
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
    private Handler handler = new Handler();
    double lati;
    double longi;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    TextInputLayout lnwhatsup;
    LinearLayout liswitch;
    TextView lblvillage, labelchange, lblcontactperson;
    CardView villageCard, spRDNCard;
    ProgressDialog pd;

    String isSamrudhakisan = "", skData = "", finalsSkData = "";
    Dialog dialogSK;

    SearchableSpinner sp_catfarmer;
    EditText et_acres;

    Switch switchVGBhindi, switchVGChilli, switchVGBrinjal, switchVGBottle, switchVGCali, switchVGOther, switchVGTomato;

    Switch switchNHBhindi, switchNHChilli, switchNHBrinjal, switchNHBottle, switchNHCali, switchNHOther, switchNHTomato;

    SearchableSpinner sp_demotaken;

    Switch switchcdMahyco, switchcdbasf, switchcdeastwest, switchcdrasi, switchcdupl, switchcdsyngenta, switchcdseminis, switchcdnamdhari, switchcdother;

    EditText et_adoptedfarmer, et_mahycoproduct;

    Switch switchobbask, switchobeastwest, switchobrasi, switchobvnr, switchobupl, switchobsyngejta, switchobseminis, switchobnamdhari, switchobOther, switchobNO;

    HashSet hs_ob, hs_cd, hs_vg, hs_nh;
    Button btnsksubmit;
    JSONArray jsonArray_ob, jsonArray_cd, jsonArray_nh, jsonArray_vg;
    JSONObject finalJsonObject;

    String strob = "";
    String strcd = "";
    String strnh = "";
    String strvg = "";

    String str_COF = "";
    String str_Land_Under_Veg = "";
    String str_Veg_Grown = "";
    String str_Crop_New_Hybrid = "";
    String str_IsDemoTaken = "";
    String str_CompnanyDemoSown = "";
    String str_FarmersAdopted = "";
    String str_Mahyco_Exp = "";
    String str_OtherBrands = "";
    String str_vali_message = "";

    LinearLayout llnh, llvg, llcd, llob, llswitchSK;
    EditText et_othernh, et_othervg, et_othercd, et_otherob;
    CardView card_demotaken;
    TextView txt_demotaken;
    double skmin = 0.0, skmax = 0.0;

    String
            str_et_exp,
            str_et_avgvolume,
            str_et_contersale,
            str_et_estimatedseedsold,

    str_et_bhindione,
            str_et_bhinditwo,
            str_et_chillione,
            str_et_chillitwo,
            str_et_brinjalone,
            str_et_brinjaltwo,
            str_et_bguardone,
            str_et_bguardtwo,
            str_et_calione,
            str_et_calitwo,
            str_et_othername,
            str_et_otherone,
            str_et_othertwo,
            str_et_tomatotwo,
            str_et_radishtwo,
            str_et_tomatoone,
            str_et_radishone,
            str_et_otherseedname,
            str_distributors, str_switches;



    EditText
            et_exp,
            et_avgvolume,
            et_contersale,
            et_estimatedseedsold,

    et_bhindione,
            et_bhinditwo,
            et_chillione,
            et_chillitwo,
            et_brinjalone,
            et_brinjaltwo,
            et_bguardone,
            et_bguardtwo,
            et_calione,
            et_calitwo,
            et_othername,
            et_otherone,
            et_othertwo,
            et_otherseedname, et_tomatotwo, et_radishtwo, et_tomatoone, et_radishone, et_grandmahyco, et_grandtotal;

    Switch
            switchPesticides,
            switchFertilizer,
            switchOther;
    LinearLayout ll_otherSeedSale;

    String allDistributtor[];
    boolean isAllDistributors[];
    HashSet hs_distributors = new HashSet();
    TextView txt_distributors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcfcall_tbm);
        getSupportActionBar().hide(); //<< this
        context = this;

        dialogSK = new Dialog(context);
        dialogSK.setContentView(R.layout.popup_bcf_sk);
        cx = new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        // Inflate the layout for this fragment
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        dialog = new ProgressDialog(this);
        pd = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka = (TextView) findViewById(R.id.lbltaluka);
        msclass = new Messageclass(this);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spTaluka = (SearchableSpinner2) findViewById(R.id.spTaluka);
        spRetailerCategory = (SearchableSpinner2) findViewById(R.id.spRetailerCategory);
        spRDNlist = (SearchableSpinner2) findViewById(R.id.spRDNlist);
        btnsave = (Button) findViewById(R.id.btnsave);
        villageCard = (CardView) findViewById(R.id.villageCard);
        spRDNCard = (CardView) findViewById(R.id.spRDNCard);
        llswitchSK = (LinearLayout) findViewById(R.id.llswitchSK);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        btnCompetitatorPOG = (Button) findViewById(R.id.btnCompetitatorPOG);
        btnPOG = (Button) findViewById(R.id.btnPOG);
        txtrtname = (EditText) findViewById(R.id.txtrtname);
        Utility.setRegularFont(btnsave, this);
        switchYN = (Switch) findViewById(R.id.switchYN);
        switchSKYN = (Switch) findViewById(R.id.switchSKYN);
        lnwhatsup = (TextInputLayout) findViewById(R.id.lnwhatsup);
        liswitch = (LinearLayout) findViewById(R.id.liswitch);
        lblvillage = (TextView) findViewById(R.id.lblvillage);
        labelchange = (TextView) findViewById(R.id.labelchange);
        lblcontactperson = (TextView) findViewById(R.id.lblcontactperson);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etComments = (EditText) findViewById(R.id.etComments);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        skControlsit();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode = pref.getString("UserID", null);
        taluka = "";
        BindState();
        BindActivityCategory();
        userCode = pref.getString("UserID", null);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BindDist(state);
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    // binddistributor(dist); // Accodding to district
                    //BindRetailer(dist);
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
                // BindRetailerCategory();
                //BindIntialData();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (spRetailerCategory.getSelectedItem().toString().contains("RETAILER CALL") ||
                        spRetailerCategory.getSelectedItem().toString().contains("NURSERY CALL")) {
                    BindRetailerandNurseryonline(taluka, spRetailerCategory.getSelectedItem().toString());
                } else {
                    bindVillage(taluka);
                }


                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    // BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spRetailerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    retailerCategory = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    Toast.makeText(context, "" + spRetailerCategory.getSelectedItem().toString().toUpperCase(), Toast.LENGTH_SHORT).show();
                    if (spRetailerCategory.getSelectedItem().toString().toUpperCase().contains("FARMER CALL")) {
                        llswitchSK.setVisibility(View.VISIBLE);

                    } else {
                        llswitchSK.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    // binddistributor(taluka);
                    //BindRetailer(taluka);
                    if (!gm.Code().equals("0")) {
                        // BindRetaileronline(taluka, retailerCategory);
                        setspinnercategory();
                    }


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spRDNlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                    // mobileno = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if (retailerCategory.toUpperCase().contains("RETAILER CALL")) {
                        if (!spRDNlist.getSelectedItem().toString().toLowerCase().contains("retailer not found") &&
                                !spRDNlist.getSelectedItem().toString().toLowerCase().contains("select retailer")) {
                            String[] distr = spRDNlist.getSelectedItem().toString().split("\\|");
                            etFarmerName.setText(distr[1].toString());
                            etMobileNo.setText(gm.Code().trim());

                        }
                    }
                    if (retailerCategory.toUpperCase().contains("NURSERY CALL")) {
                        if (!spRDNlist.getSelectedItem().toString().toLowerCase().contains("nursery not found") &&
                                !spRDNlist.getSelectedItem().toString().toLowerCase().contains("select nursery")) {
                            String[] distr = spRDNlist.getSelectedItem().toString().split("\\|");
                            etFarmerName.setText(distr[1].toString());
                            etMobileNo.setText(gm.Code().trim());

                        }
                    }

                    if (spRDNlist.getSelectedItem().toString().toLowerCase().contains("retailer not found") ||
                            spRDNlist.getSelectedItem().toString().toLowerCase().contains("nursery not found")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BCFCallTBM.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("My Activity App");
                        // Setting Dialog Message

                        if (spRetailerCategory.getSelectedItem().toString().contains("RETAILER CALL")) {
                            alertDialog.setMessage("ADD NEW RETAILER FOR BCF CALL . ");

                        } else {

                            alertDialog.setMessage("ADD NEW NURSERY FOR BCF CALL . ");
                        }
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {


                                CallNewRetailerPOP();
                              /*  Bundle bundle = new Bundle();
                                bundle.putString("state", state);
                                bundle.putString("dist", dist);
                                bundle.putString("taluka", taluka );
                                bundle.putString("mktplace", txtmarketplace.getText().toString());
                                bundle.putString("From", "RetailerPOG");

                                Intent intent= new Intent(context.getApplicationContext(),RetailerandDistributorTag.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.putExtras(bundle);
                                startActivityForResult(intent, 2);*/
                            }


                        });

                       /* alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();

                            }
                        }); */

                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.weight = 10;
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);
                        //end


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                {


                    // BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        switchYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    etWhatsappNumber.setText(etMobileNo.getText().toString());

                    etMobileNo.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("ONtext changed " + s.toString());
                            etWhatsappNumber.setText(etMobileNo.getText().toString());

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            System.out.println("beforeTextChanged " + s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            System.out.println("afterTextChanged " + s.toString());

                        }
                    });

                    etWhatsappNumber.setFocusable(false);
                    etWhatsappNumber.setFocusableInTouchMode(false);
                } else if (!switchYN.isChecked() || etMobileNo.getText().toString().equals("")) {

                    etMobileNo.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("ONtext changed " + s.toString());
                            etWhatsappNumber.setText("");

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            System.out.println("beforeTextChanged " + s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            System.out.println("afterTextChanged " + s.toString());

                        }
                    });


                    etWhatsappNumber.setText("");
                    etWhatsappNumber.setFocusable(true);
                    etWhatsappNumber.setFocusableInTouchMode(true);


                }
            }
        });

        switchSKYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSamrudhakisan = "YES";
                    OpenSamrudhkisanDialog();
                } else {
                    isSamrudhakisan = "NO";
                    skData = "{}";

                }
            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(BCFCallTBM.this);

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

                            dialog.dismiss();
                        }
                    });

                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();


                }
            }

        });


        BindIntialData();


    }

    void skControlsit() {
        hs_ob = new HashSet();
        hs_cd = new HashSet();
        hs_vg = new HashSet();
        hs_nh = new HashSet();

        llnh = dialogSK.findViewById(R.id.ll_othernh);
        llvg = dialogSK.findViewById(R.id.ll_othervg);
        llcd = dialogSK.findViewById(R.id.ll_othercd);
        llob = dialogSK.findViewById(R.id.ll_otherob);
        card_demotaken = dialogSK.findViewById(R.id.card_demotaken);
        txt_demotaken = dialogSK.findViewById(R.id.txt_demotaken);

        et_othernh = dialogSK.findViewById(R.id.et_othernh);
        et_othervg = dialogSK.findViewById(R.id.et_othervg);
        et_othercd = dialogSK.findViewById(R.id.et_othercd);
        et_otherob = dialogSK.findViewById(R.id.et_otherob);

        sp_catfarmer = dialogSK.findViewById(R.id.sp_catfarmer);
        sp_demotaken = dialogSK.findViewById(R.id.sp_demotaken);

        et_acres = dialogSK.findViewById(R.id.et_acres);
        et_adoptedfarmer = dialogSK.findViewById(R.id.et_adoptedfarmer);
        et_mahycoproduct = dialogSK.findViewById(R.id.et_mahycoproduct);

        et_mahycoproduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    et_mahycoproduct.setError("REQUIRED");
                } else {
                    int a = Integer.parseInt(s.toString().trim());
                    if (a > 0 && a < 60) {

                    } else {
                        et_mahycoproduct.setText("");
                        et_mahycoproduct.setError("Years should not be more than 60.");
                    }
                }
            }
        });


        switchVGBhindi = dialogSK.findViewById(R.id.switchVGBhindi);
        switchVGChilli = dialogSK.findViewById(R.id.switchVGChilli);
        switchVGBrinjal = dialogSK.findViewById(R.id.switchVGBrinjal);
        switchVGBottle = dialogSK.findViewById(R.id.switchVGBottle);
        switchVGCali = dialogSK.findViewById(R.id.switchVGCali);
        switchVGOther = dialogSK.findViewById(R.id.switchVGOther);
        switchVGTomato = dialogSK.findViewById(R.id.switchVGTomato);

        switchNHBhindi = dialogSK.findViewById(R.id.switchNHBhindi);
        switchNHChilli = dialogSK.findViewById(R.id.switchNHChilli);
        switchNHBrinjal = dialogSK.findViewById(R.id.switchNHBrinjal);
        switchNHBottle = dialogSK.findViewById(R.id.switchNHBottle);
        switchNHCali = dialogSK.findViewById(R.id.switchNHCali);
        switchNHOther = dialogSK.findViewById(R.id.switchNHOther);
        switchNHTomato = dialogSK.findViewById(R.id.switchNHTomato);


        switchcdMahyco = dialogSK.findViewById(R.id.switchcdMahyco);
        switchcdbasf = dialogSK.findViewById(R.id.switchcdbasf);
        switchcdeastwest = dialogSK.findViewById(R.id.switchcdeastwest);
        switchcdrasi = dialogSK.findViewById(R.id.switchcdrasi);
        switchcdupl = dialogSK.findViewById(R.id.switchcdupl);
        switchcdsyngenta = dialogSK.findViewById(R.id.switchcdsyngenta);
        switchcdseminis = dialogSK.findViewById(R.id.switchcdseminis);
        switchcdnamdhari = dialogSK.findViewById(R.id.switchcdnamdhari);
        switchcdother = dialogSK.findViewById(R.id.switchcdother);


        switchobbask = dialogSK.findViewById(R.id.switchobbask);
        switchobeastwest = dialogSK.findViewById(R.id.switchobeastwest);
        switchobrasi = dialogSK.findViewById(R.id.switchobrasi);
        switchobvnr = dialogSK.findViewById(R.id.switchobvnr);
        switchobupl = dialogSK.findViewById(R.id.switchobupl);
        switchobsyngejta = dialogSK.findViewById(R.id.switchobsyngejta);
        switchobseminis = dialogSK.findViewById(R.id.switchobseminis);
        switchobnamdhari = dialogSK.findViewById(R.id.switchobnamdhari);
        switchobOther = dialogSK.findViewById(R.id.switchobOther);
        switchobNO = dialogSK.findViewById(R.id.switchobNO);

        btnsksubmit = dialogSK.findViewById(R.id.btnsksubmit);
        btnsksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  if(str)

                if (validateSKFields()) {
                    if (switchSKYN.isChecked()) {
                        if (validateSKFields()) {
                            finalsSkData = skData;
                        } else {
                            new androidx.appcompat.app.AlertDialog.Builder(context)
                                    .setMessage(str_vali_message)
                                    .setTitle("Check form data ")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        finalsSkData = "{}";
                    }


                    dialogSK.dismiss();
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setMessage(str_vali_message)
                            .setTitle("Check form data ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }


            }
        });


        switchVGBhindi.setOnCheckedChangeListener(this);
        switchVGChilli.setOnCheckedChangeListener(this);
        switchVGBrinjal.setOnCheckedChangeListener(this);
        switchVGBottle.setOnCheckedChangeListener(this);
        switchVGCali.setOnCheckedChangeListener(this);
        switchVGOther.setOnCheckedChangeListener(this);
        switchVGTomato.setOnCheckedChangeListener(this);

        switchNHBhindi.setOnCheckedChangeListener(this);
        switchNHChilli.setOnCheckedChangeListener(this);
        switchNHBrinjal.setOnCheckedChangeListener(this);
        switchNHBottle.setOnCheckedChangeListener(this);
        switchNHCali.setOnCheckedChangeListener(this);
        switchNHOther.setOnCheckedChangeListener(this);
        switchNHTomato.setOnCheckedChangeListener(this);


        switchcdMahyco.setOnCheckedChangeListener(this);
        switchcdbasf.setOnCheckedChangeListener(this);
        switchcdeastwest.setOnCheckedChangeListener(this);
        switchcdrasi.setOnCheckedChangeListener(this);
        switchcdupl.setOnCheckedChangeListener(this);
        switchcdsyngenta.setOnCheckedChangeListener(this);
        switchcdseminis.setOnCheckedChangeListener(this);
        switchcdnamdhari.setOnCheckedChangeListener(this);
        switchcdother.setOnCheckedChangeListener(this);


        switchobbask.setOnCheckedChangeListener(this);
        switchobeastwest.setOnCheckedChangeListener(this);
        switchobrasi.setOnCheckedChangeListener(this);
        switchobvnr.setOnCheckedChangeListener(this);
        switchobupl.setOnCheckedChangeListener(this);
        switchobsyngejta.setOnCheckedChangeListener(this);
        switchobseminis.setOnCheckedChangeListener(this);
        switchobnamdhari.setOnCheckedChangeListener(this);
        switchobOther.setOnCheckedChangeListener(this);
        switchobOther.setOnCheckedChangeListener(this);


        sp_catfarmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_acres.setText("");

                switch (position) {
                    case 1:
                        skmin = 1.0;
                        skmax = 5.0;
                        break;

                    case 2:
                        skmin = 5.0;
                        skmax = 10.0;
                        break;
                    case 3:
                        skmin = 10.0;
                        skmax = 25.0;
                        break;
                    case 4:
                        skmin = 25.0;
                        skmax = 5000.0;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_acres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (str.length() > 0) {
                    double d = Double.parseDouble(str);
                    if (d >= skmin && d <= skmax) {

                    } else {
                        et_acres.setError("Enter the lands in between " + skmin + " to " + skmax + ".");

                    }
                }
            }
        });
        et_acres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String str = et_acres.getText().toString().trim();
                if (str.length() > 0) {
                    double d = Double.parseDouble(str);
                    if (d >= skmin && d <= skmax) {

                    } else {
                        et_acres.setError("Enter the lands in between " + skmin + " to " + skmax + ".");
                        et_acres.setText("");
                    }
                } else {
                    et_acres.setError("Enter the lands in between " + skmin + " to " + skmax + ".");
                    et_acres.setText("");
                }
            }
        });

        sp_demotaken.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_demotaken.getSelectedItem().toString().trim().toLowerCase().equals("yes")) {
                    card_demotaken.setVisibility(View.VISIBLE);
                    txt_demotaken.setVisibility(View.VISIBLE);
                } else {
                    card_demotaken.setVisibility(View.GONE);
                    txt_demotaken.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    //save data to db//

    public void saveToDb() {


        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsup = "";

        } else {


            whatsup = etWhatsappNumber.getText().toString();
        }


        name = etFarmerName.getText().toString();

        village = spVillage.getSelectedItem().toString();
        state = spState.getSelectedItem().toString();
        dist = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        comments = etComments.getText().toString() + "~" + finalsSkData;
        mobileno = etMobileNo.getText().toString() + "";
        //whatsappNumber= etWhatsappNumber.getText().toString();
        firmname = spRDNlist.getSelectedItem().toString();
        String isSynced = "0";
        Date entrydate = new Date();
        long epoch = entrydate.getTime();
        System.out.println(epoch);
        String timeStamp = String.valueOf(epoch);
        BCFCALLdataupload("BCFCALLTBM");


    }

    //kisan club data fetch and upload api calling method with data fetch
    public void BCFCALLdataupload(String functionName) {
        if (config.NetworkConnection()) {
//            dialog = new ProgressDialog(ActivityKisanClub.this);
//            dialog.setTitle("Data Uploading ...");
//            dialog.setMessage("Please wait.");
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.show();
            String str = null;
            int count = 1;
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {


                        JSONObject jsonsubObject = new JSONObject();
                        jsonsubObject.put("userCode", userCode);
                        jsonsubObject.put("state", state);
                        jsonsubObject.put("district", dist);
                        jsonsubObject.put("tehsil", taluka);
                        jsonsubObject.put("village", village);
                        jsonsubObject.put("activityType", retailerCategory);
                        jsonsubObject.put("Name", name);
                        jsonsubObject.put("firmname", firmname);
                        jsonsubObject.put("mobileNumber", mobileno);
                        jsonsubObject.put("whatsappNumber", whatsup);
                        jsonsubObject.put("comments", comments);
                        jsonArray.put(jsonsubObject);
                        jsonObject.put("Table", jsonArray);
                        Log.d("rhtt", "BCFTBMCALL: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new uploadbcfcall(functionName, jsonObject).execute(SERVER).get();


                    if (!str.equals("")) {

                        if (str.contains("True")) {

                            etFarmerName.setText("");
                            etMobileNo.setText("");
                            etWhatsappNumber.setText("");
                            etComments.setText("");

                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Utility.showAlertDialog("Success", "Records Uploaded successfully", context);


                            // dialog.dismiss();

                        } else {
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Utility.showAlertDialog("Error", str, context);


                        }
                    } else {
                        Utility.showAlertDialog("Error", "Poor Internet: Please try after sometime.", context);
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }


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
            Utility.showAlertDialog("Info", "Data Saved Successfully", context);
            etFarmerName.setText("");
            etMobileNo.setText("");
            etWhatsappNumber.setText("");
            etComments.setText("");

        }
        // dialog.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
       try {
           // double mmahyco = Double.parseDouble(et_estimatedseedsold.getText().toString().trim());
           double m1 = et_bhinditwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_bhinditwo.getText().toString().trim());
           double m2 = et_chillitwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_chillitwo.getText().toString().trim());
           double m3 = et_brinjaltwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_brinjaltwo.getText().toString().trim());
           double m4 = et_bguardtwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_bguardtwo.getText().toString().trim());
           double m5 = et_calitwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_calitwo.getText().toString().trim());
           double m6 = et_othertwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_othertwo.getText().toString().trim());
           double m7 = et_tomatotwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_tomatotwo.getText().toString().trim());
           double m8 = et_radishtwo.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_radishtwo.getText().toString().trim());
           double sum = m1 + m2 + m3 + m4 + m5 + m6 + m7 + m8;
           et_grandmahyco.setText("" + sum);
           et_estimatedseedsold.setText("" + sum);
           double m11 = et_bhindione.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_bhindione.getText().toString().trim());
           double m22 = et_chillione.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_chillione.getText().toString().trim());
           double m33 = et_brinjalone.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_brinjalone.getText().toString().trim());
           double m44 = et_bguardone.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_bguardone.getText().toString().trim());
           double m55 = et_calione.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_calione.getText().toString().trim());
           double m66 = et_otherone.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_otherone.getText().toString().trim());
           double m77 = et_tomatoone.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_tomatoone.getText().toString().trim());
           double m88 = et_radishone.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_radishone.getText().toString().trim());
           double sumTotal = m11 + m22 + m33 + m44 + m55 + m66 + m77 + m88;

           et_grandtotal.setText("" + sumTotal);
           et_avgvolume.setText("" + sumTotal);
           if (m11 < m1) {
               et_bhinditwo.setError("Must be less than total sale.");
               return;
           }

           if (m22 < m2) {
               et_chillitwo.setError("Must be less than total sale.");
               return;
           }

           if (m33 < m3) {
               et_brinjaltwo.setError("Must be less than total sale.");
               return;
           }

           if (m44 < m4) {
               et_bguardtwo.setError("Must be less than total sale.");
               return;
           }

           if (m55 < m5) {
               et_calitwo.setError("Must be less than total sale.");
               return;
           }

           if (m66 < m6) {
               et_othertwo.setError("Must be less than total sale.");
               return;
           }
           if (m77 < m7) {
               et_tomatotwo.setError("Must be less than total sale.");
               return;
           }
           if (m88 < m8) {
               et_radishtwo.setError("Must be less than total sale.");
               return;
           }
       }catch(Exception e)
       {
           Toast.makeText(context, "Please enter valid entry.", Toast.LENGTH_SHORT).show();
       }
    }


    public class uploadbcfcall extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;


        public uploadbcfcall(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;


        }

        protected void onPreExecute() {

            // dialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... urls) {

            String utf = HttpUtils.convertStringToUTF8(obj.toString().trim());
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "TBMBCFCall"));
            postParameters.add(new BasicNameValuePair("TBMBCFCall", utf));

            Log.d("RequestKisanClubData", postParameters + "");


            String Urlpath = SERVER + "?userCode=" + userCode + "";
            Log.d("uploadKisanClubData", Urlpath);

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
                //  dialog.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                //dialog.dismiss();
            }

            //dialog.dismiss();
            return builder.toString();
        }

        @SuppressLint("LongLogTag")
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    Log.d("ResponseKisanClubData", resultout);


                } else {
                    Log.d("rhtt", "uploadKisanClubData: " + result);

                }

                // dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                //  dialog.dismiss();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult: " + data.getExtras().toString());

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK
                    && data != null) {
                Bundle bundle = data.getExtras();
                String state = bundle.getString("state");
                String dist = bundle.getString("dist");
                String taluka = bundle.getString("taluka");
                String mktplace = bundle.getString("mktplace");
                String retailermobileno = bundle.getString("retailermobileno");


                //Log.d(TAG, "onActivityResult: +"+txnAmount);
                for (String key : bundle.keySet()) {
                    Log.d("transaction", key + " is a key in the bundle");
                }
            }

            if (requestCode == 2) {
                BindRetailer(taluka.trim());
            }
        } catch (Exception ex) {

        }
    }


    public void CallNewRetailerPOP() {
        try {


            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.retailernurseryaddpop);
             /* s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L); */
            final LinearLayout tblLayout = (LinearLayout) dialog.findViewById(R.id.li1);
            // TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            //TableRow row = (TableRow)tblLayout.getChildAt(1);
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);

            final EditText txtretailername = (EditText) dialog.findViewById(R.id.txtretailername);
            final EditText txtretailerfirmname = (EditText) dialog.findViewById(R.id.txtretailerfirmname);

            final TextView lblretailername = (TextView) dialog.findViewById(R.id.lblretailername);
            final TextView lblretailerfirmname = (TextView) dialog.findViewById(R.id.lblretailerfirmname);
            final Switch switchyesno = (Switch) dialog.findViewById(R.id.switchyesno);
            final EditText txtretailermobileno = (EditText) dialog.findViewById(R.id.txtretailermobileno);
            final EditText txtretailerWAmobileno = (EditText) dialog.findViewById(R.id.txtretailerWAmobileno);
            txt_distributors = (TextView) dialog.findViewById(R.id.txt_distributors);

            Spinner sp_distributor_retailer = dialog.findViewById(R.id.sp_distributorretailer);
            bindDistributorNewRetailer();

            txt_distributors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectMultipleDistributors();
                }
            });
            switchyesno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        txtretailerWAmobileno.setText(txtretailermobileno.getText().toString().trim());
                    } else {
                        txtretailerWAmobileno.setText("");
                    }
                }
            });




            et_exp = dialog.findViewById(R.id.et_exp);
            et_avgvolume = dialog.findViewById(R.id.et_avgvolume);
            et_contersale = dialog.findViewById(R.id.et_contersale);
            et_estimatedseedsold = dialog.findViewById(R.id.et_estimatedseedsold);

            et_bhindione = dialog.findViewById(R.id.et_bhindione);
            et_bhinditwo = dialog.findViewById(R.id.et_bhinditwo);
            et_chillione = dialog.findViewById(R.id.et_chillione);
            et_chillitwo = dialog.findViewById(R.id.et_chillitwo);
            et_brinjalone = dialog.findViewById(R.id.et_brinjalone);
            et_brinjaltwo = dialog.findViewById(R.id.et_brinjaltwo);
            et_bguardone = dialog.findViewById(R.id.et_bguardone);
            et_bguardtwo = dialog.findViewById(R.id.et_bguardtwo);
            et_calione = dialog.findViewById(R.id.et_calione);
            et_calitwo = dialog.findViewById(R.id.et_calitwo);
            et_othername = dialog.findViewById(R.id.et_othername);
            et_otherone = dialog.findViewById(R.id.et_otherone);
            et_othertwo = dialog.findViewById(R.id.et_othertwo);
            et_tomatoone = dialog.findViewById(R.id.et_tomatoone);
            et_tomatotwo = dialog.findViewById(R.id.et_tomatotwo);
            et_radishone = dialog.findViewById(R.id.et_radishone);
            et_radishtwo = dialog.findViewById(R.id.et_radishtwo);
            et_otherseedname = dialog.findViewById(R.id.et_otherseedname);
            et_grandmahyco = dialog.findViewById(R.id.et_grandmahyco);
            et_grandtotal = dialog.findViewById(R.id.et_grandtotal);


            et_bhindione.addTextChangedListener(this);
            et_bhinditwo .addTextChangedListener(this);
            et_chillione .addTextChangedListener(this);
            et_chillitwo .addTextChangedListener(this);
            et_brinjalone .addTextChangedListener(this);
            et_brinjaltwo .addTextChangedListener(this);
            et_bguardone .addTextChangedListener(this);
            et_bguardtwo.addTextChangedListener(this);
            et_calione.addTextChangedListener(this);
            et_calitwo.addTextChangedListener(this);
            et_othername.addTextChangedListener(this);
            et_otherone.addTextChangedListener(this);
            et_othertwo.addTextChangedListener(this);
            et_tomatoone.addTextChangedListener(this);
            et_tomatotwo.addTextChangedListener(this);
            et_radishone.addTextChangedListener(this);
            et_radishtwo.addTextChangedListener(this);





            switchPesticides = dialog.findViewById(R.id.switchPesticides);
            switchFertilizer = dialog.findViewById(R.id.switchFertilizer);
            switchOther = dialog.findViewById(R.id.switchOther);
            ll_otherSeedSale = dialog.findViewById(R.id.ll_otherSeedSale);

            HashSet hashSetRetailer = new HashSet();
            switchPesticides.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        hashSetRetailer.add("Pesticides");
                    else

                        hashSetRetailer.remove("Pesticides");
                }
            });
            switchFertilizer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        hashSetRetailer.add("Fertilizer");
                    else

                        hashSetRetailer.remove("Fertilizer");
                }
            });
            switchOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        hashSetRetailer.add("Other");
                        ll_otherSeedSale.setVisibility(View.VISIBLE);
                    } else {

                        hashSetRetailer.remove("Other");
                        ll_otherSeedSale.setVisibility(View.GONE);
                    }
                }
            });


            if (spRetailerCategory.getSelectedItem().toString().contains("NURSERY CALL")) {
                lblretailername.setText("CONTACT PERSON NAME");
                lblretailerfirmname.setText("NURSERY NAME");

            }
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    selectMultipleDistributors();
                }
            });
            String[] array = null;
            array = new String[1];
            array[0] = "SELECT PRODUCT";
            retailerfirmname = txtretailerfirmname.getText().toString().trim();
            RetailerName = txtretailername.getText().toString().trim();
            mobileno = txtretailermobileno.getText().toString().trim();


            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        str_et_exp = et_exp.getText().toString().trim();
                        str_et_avgvolume = et_avgvolume.getText().toString().trim();
                        str_et_contersale = et_contersale.getText().toString().trim();
                        str_et_estimatedseedsold = et_estimatedseedsold.getText().toString().trim();

                        str_et_bhindione = et_bhindione.getText().toString().trim();
                        str_et_bhinditwo = et_bhinditwo.getText().toString().trim();
                        str_et_chillione = et_chillione.getText().toString().trim();
                        str_et_chillitwo = et_chillitwo.getText().toString().trim();
                        str_et_brinjalone = et_brinjalone.getText().toString().trim();
                        str_et_brinjaltwo = et_brinjaltwo.getText().toString().trim();
                        str_et_bguardone = et_bguardone.getText().toString().trim();
                        str_et_bguardtwo = et_bguardtwo.getText().toString().trim();
                        str_et_calione = et_calione.getText().toString().trim();
                        str_et_calitwo = et_calitwo.getText().toString().trim();
                        str_et_othername = et_othername.getText().toString().trim();
                        str_et_otherone = et_otherone.getText().toString().trim();
                        str_et_othertwo = et_othertwo.getText().toString().trim();
                        str_et_tomatotwo = et_tomatotwo.getText().toString().trim();
                        str_et_radishtwo = et_radishtwo.getText().toString().trim();
                        str_et_tomatoone = et_tomatoone.getText().toString().trim();
                        str_et_radishone = et_radishone.getText().toString().trim();
                        str_et_otherseedname = et_otherseedname.getText().toString().trim();

                        str_distributors = hs_distributors.toString();


                        str_switches = hashSetRetailer.toString();
                        if (str_switches.contains("Other")) {
                            str_switches = str_switches.replace("Other", "Other (" + et_otherseedname.getText().toString() + ")");
                        }


                        // call Save Retailer Api
                        if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                            msclass.showMessage("Please select state");
                            return;
                        }
                        if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                            msclass.showMessage("Please select district");
                            return;
                        }
                        if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                            msclass.showMessage("Please Select Taluka");
                            return;
                        }
                        if (txtretailername.getText().length() == 0) {
                            msclass.showMessage("Please  enter the  name ");
                            txtretailername.setError("Please  enter the name ");
                            return;
                        }
                        if (txtretailerfirmname.getText().length() == 0) {
                            msclass.showMessage("Please  enter the firm name ");
                            return;
                        }
                        if (txtretailermobileno.getText().length() == 0) {
                            msclass.showMessage("Please  enter the mobile number  ");
                            return;
                        }
                        if (txtretailermobileno.getText().length() != 10) {
                            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
                            return;
                        }
                        JSONObject jsonObject = new JSONObject();
                        try {
                            String data = "";
                            if (str_et_exp.toString().trim().equals("")) {
                                et_exp.setError("");
                                return;
                            } else {
                                double d = Double.parseDouble(str_et_exp.toString().trim());
                                if (d > 0 && d < 60) {

                                } else {
                                    msclass.showMessage("Enter your experience in between 1 to 60 ");
                                    return;
                                }
                            }

                            if (str_et_avgvolume.toString().trim().equals("")) {
                                et_avgvolume.setError("");
                                return;
                            }
                            if (str_et_contersale.toString().trim().equals("")) {
                                et_contersale.setError("");
                                return;
                            } else {
                                int a = Integer.parseInt(str_et_contersale.trim());
                                if (a > 0 && a <= 100) {

                                } else {
                                    et_contersale.setError("must be in between 1-100.");

                                }
                                //=et_exp.getText().toString().trim();
                            }
                            if (str_et_estimatedseedsold.toString().trim().equals("")) {
                                et_estimatedseedsold.setError("");
                                return;
                            } else {
                                double est = Double.parseDouble(str_et_estimatedseedsold.trim());
                                double avg = Double.parseDouble(str_et_avgvolume.trim());
                                if (avg < est) {
                                    et_estimatedseedsold.setError("The value entered in Average Volume should always be less than or equal to the value entered under the 'Average volume of Hybrid Vegetable seed sold, as estimated (in Kgs) ' ");
                                    msclass.showMessage("The value entered in Average Volume should always be less than or equal to the value entered under the '\tAverage volume of Hybrid Vegetable seed sold, as estimated (in Kgs) ' ");
                                    return;
                                }
                                //=et_exp.getText().toString().trim();
                            }

                            if (str_et_bhindione.toString().trim().equals("")) {
                                et_bhindione.setError("");
                                return;
                            }//=et_exp.getText().toString().trim();
                            if (str_et_bhinditwo.toString().trim().equals("")) {
                                et_bhinditwo.setError("");
                                return;

                            }//=et_exp.getText().toString().trim();
                            if (str_et_chillione.toString().trim().equals("")) {
                                et_chillione.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_chillitwo.toString().trim().equals("")) {
                                et_chillitwo.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_brinjalone.toString().trim().equals("")) {
                                et_brinjalone.setError("");
                            }// =et_exp.getText().toString().trim();
                            if (str_et_brinjaltwo.toString().trim().equals("")) {
                                et_brinjaltwo.setError("");
                                return;
                            }//=et_exp.getText().toString().trim();
                            if (str_et_bguardone.toString().trim().equals("")) {
                                et_bguardone.setError("");
                                return;
                            }//=et_exp.getText().toString().trim();
                            if (str_et_bguardtwo.toString().trim().equals("")) {
                                et_bguardtwo.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_calione.toString().trim().equals("")) {
                                et_calione.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_calitwo.toString().trim().equals("")) {
                                et_calitwo.setError("");
                                return;
                            }//=et_exp.getText().toString().trim();
                            if (str_et_othername.toString().trim().equals("")) {
                                et_othername.setError("");
                                return;
                            }//=et_exp.getText().toString().trim();
                            if (str_et_otherone.toString().trim().equals("")) {
                                et_otherone.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_othertwo.toString().trim().equals("")) {
                                et_othertwo.setError("");
                                return;
                            }
                            if (str_et_tomatoone.toString().trim().equals("")) {
                                et_tomatoone.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_tomatotwo.toString().trim().equals("")) {
                                et_tomatotwo.setError("");
                                return;
                            }
                            if (str_et_radishone.toString().trim().equals("")) {
                                et_radishone.setError("");
                                return;
                            }// =et_exp.getText().toString().trim();
                            if (str_et_radishtwo.toString().trim().equals("")) {
                                et_radishtwo.setError("");
                                return;
                            }


                            //=et_exp.getText().toString().trim();
                            if (str_switches.trim().contains("Other"))
                                if (str_et_otherseedname.toString().trim().equals("")) {
                                    et_otherseedname.setError("");
                                    return;
                                }//=et_exp.getText().toString().trim();

                            if (hs_distributors.isEmpty()) {
                                Toast.makeText(BCFCallTBM.this, "Please select distributor", Toast.LENGTH_SHORT).show();
                                return;
                            }//=sp_distributor_retailer.getSelectedItem().toString().trim();
                            if (hashSetRetailer.isEmpty()) {
                                Toast.makeText(BCFCallTBM.this, "Please select business other than seeds.", Toast.LENGTH_SHORT).show();
                                return;
                            }//=hashSetRetailer.toString();

                            double d = 0.0;
                            try {
                                double mmahyco = Double.parseDouble(str_et_estimatedseedsold.trim());
                                double m1 = Double.parseDouble(str_et_bhinditwo.trim());
                                double m2 = Double.parseDouble(str_et_chillitwo.trim());
                                double m3 = Double.parseDouble(str_et_brinjaltwo.trim());
                                double m4 = Double.parseDouble(str_et_bguardtwo.trim());
                                double m5 = Double.parseDouble(str_et_calitwo.trim());
                                double m6 = Double.parseDouble(str_et_othertwo.trim());
                                double m7 = Double.parseDouble(str_et_tomatotwo.trim());
                                double m8 = Double.parseDouble(str_et_radishtwo.trim());
                                double sum = m1 + m2 + m3 + m4 + m5 + m6 + m7 + m8;
                                et_grandmahyco.setText("" + sum);
                                double m11 = Double.parseDouble(str_et_bhindione.trim());
                                double m22 = Double.parseDouble(str_et_chillione.trim());
                                double m33 = Double.parseDouble(str_et_brinjalone.trim());
                                double m44 = Double.parseDouble(str_et_bguardone.trim());
                                double m55 = Double.parseDouble(str_et_calione.trim());
                                double m66 = Double.parseDouble(str_et_otherone.trim());
                                double m77 = Double.parseDouble(str_et_tomatoone.trim());
                                double m88 = Double.parseDouble(str_et_radishone.trim());
                                double sumTotal = m11 + m22 + m33 + m44 + m55 + m66 + m77 + m88;

                                et_grandtotal.setText("" + sumTotal);
                                if (m11 < m1) {
                                    et_bhinditwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (m22 < m2) {
                                    et_chillitwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (m33 < m3) {
                                    et_brinjaltwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (m44 < m4) {
                                    et_bguardtwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (m55 < m5) {
                                    et_calitwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (m66 < m6) {
                                    et_othertwo.setError("Must be less than total sale.");
                                    return;
                                }
                                if (m77 < m7) {
                                    et_othertwo.setError("Must be less than total sale.");
                                    return;
                                }
                                if (m88 < m8) {
                                    et_othertwo.setError("Must be less than total sale.");
                                    return;
                                }

                                if (mmahyco < sum) {
                                    msclass.showMessage("Estimated average volume of MPL's Hybrid Vegetable seed sold, as estimated (in Kgs) is Exceeding the limit .");
                                    return;
                                }

                                if (sumTotal < sum) {
                                    msclass.showMessage("Average Volume of hybrid vegtable sold as Estimated value is not matching with total Estimated Sales volume of major hybrid veg crops sold,as Estimated.");
                                    return;
                                }


                            } catch (NumberFormatException e) {

                            }


                            jsonObject.put("MahycoExp", str_et_exp);
                            jsonObject.put("Distributors", str_distributors);
                            jsonObject.put("AvgVolume", str_et_avgvolume);
                            jsonObject.put("SaleHybridPer", str_et_contersale);
                            jsonObject.put("AvgVolMPL", str_et_estimatedseedsold);
                            jsonObject.put("EstVolumeBhendi", "" + str_et_bhindione + "," + str_et_bhinditwo);
                            jsonObject.put("EstVolumeChilli", "" + str_et_chillione + "," + str_et_chillitwo);
                            jsonObject.put("EstVolumeBrinjal", "" + str_et_brinjalone + "," + str_et_brinjaltwo);
                            jsonObject.put("EstVolumeBottleGuard", "" + str_et_bguardone + "," + str_et_bguardtwo);
                            jsonObject.put("EstVolumeCaliflower", "" + str_et_calione + "," + str_et_calitwo);
                            jsonObject.put("EstVolumeTomato", "" + str_et_tomatoone + "," + str_et_tomatotwo);
                            jsonObject.put("EstVolumeRadish", "" + str_et_radishone + "," + str_et_radishtwo);
                            jsonObject.put("EstVolumeOther", "" + str_et_otherone + "," + str_et_othertwo + "," + str_et_othername);
                            jsonObject.put("OtherBussiness", str_switches);
                            jsonObject.put("WhatsappNumber", txtretailerWAmobileno.getText().toString().trim());
                            jsonObject.put("Address", address);
                            jsonObject.put("Lat", lati);
                            jsonObject.put("Long", longi);


                            Log.i("Datat su", jsonObject.toString());
                        } catch (Exception e) {
                            Log.i("Error is ", e.getMessage());
                        }


                        retailerfirmname = txtretailerfirmname.getText().toString().trim();
                        RetailerName = txtretailername.getText().toString().trim();
                        mobileno = txtretailermobileno.getText().toString().trim();
//String nedata="";
//                        byte[] encodeValue = Base64.encode(jsonObject.toString().getBytes(), Base64.DEFAULT);
//                        nedata=new String(encodeValue);
                        //Passing Data in Base64
                        //    RetailerName = RetailerName + " ~ " + nedata;
                        //Paasing String Formate data
                        RetailerName = RetailerName + " ~ " + jsonObject.toString();
                        String str = addPOGretaill();
                        // String str = "False";
                        if (str.contains("True")) {
                            txtretailername.setText("");
                            txtretailermobileno.setText("");
                            txtretailerfirmname.setText("");
                            msclass.showMessage("data saved successfully");
                            dialog.dismiss();
                            BindRetailerList_online(str);
                        } else {
                            msclass.showMessage(str);
                        }
                       /* final String status = spstatus.getSelectedItem().toString();
                        int count = 0;
                        String product = "";
                        if (status.equals("SELECT STATUS")) {
                            msclass.showMessage("Please select stock  status");
                            return;
                        }
                        if (txtqty.getText().length()==0) {
                            msclass.showMessage("Please  enter qty");
                            return;
                        }


                        if (status.contains("SALES RETURN") )
                        {
                            if (Integer.valueOf(txtqty.getText().toString())>Integer.valueOf(txtbalance.getText().toString()))
                            {
                                msclass.showMessage("Sales return ,not allow to more than balance qty.");
                                return;
                            }
                        }
                        if (txtdate.getText().length()==0) {
                            msclass.showMessage("Please  enter date");
                            return;
                        }
                        if (spdistr.getSelectedStrings().contains("SELECT DISTRIBUTOR")) {
                            msclass.showMessage("Please  enter distributor");
                            return;
                        }
                        */
                        //St

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        // Date d = new Date();
                        //final String strdate = dateFormat.format(d);


                        //ene

                       /* if (count > 0) {
                            msclass.showMessage("product detail added successfully.");
                            dialog.dismiss();

                        } else {
                            msclass.showMessage("Please enter product details");
                        }
  */

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


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    public String addPOGretaill() {
        String str = "";
        if (config.NetworkConnection()) {
            try {
                str = new GetBindRetailerandNurseryonline("2", usercode, retailerCategory, division, cmbDistributor).execute().get();

            } catch (Exception ex) {
                str = ex.getMessage();
            }
        } else {
            str = "Please check internet connection";
        }
        return str;
    }

    private boolean validation() {
        try {
            boolean flag = true;
            GeneralMaster rc = (GeneralMaster) spRetailerCategory.getSelectedItem();
            GeneralMaster rr = (GeneralMaster) spRDNlist.getSelectedItem();
            if (rc.Code().toLowerCase().equals("0")) {
                msclass.showMessage("Please select activity type.");
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
            if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }
            /*if (txtmarketplace.getText().length() == 0) {
                msclass.showMessage("Please  enter market place .");
                return false;

            }*/

            name = etFarmerName.getText().toString();

            village = spVillage.getSelectedItem().toString();
            state = spState.getSelectedItem().toString();
            dist = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            comments = etComments.getText().toString() + "";
            mobileno = etMobileNo.getText().toString() + "";
            //whatsappNumber= etWhatsappNumber.getText().toString();
            firmname = spRDNlist.getSelectedItem().toString();
            GeneralMaster vr = (GeneralMaster) spVillage.getSelectedItem();

            if (rc.Code().toLowerCase().equals("farmer call")) {

                if (vr.Code().toLowerCase().equals("0")) {
                    msclass.showMessage("Please select village");
                    return false;
                }

                if (etFarmerName.getText().length() == 0) {
                    msclass.showMessage("Please enter farmer name ");
                    return false;
                }
            } else {
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select retailer") || spRDNlist.getSelectedItem().toString().toLowerCase().contains("retailer not found")) {
                    msclass.showMessage("Please select retailer firm name ");
                    return false;
                }
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select nursery") || spRDNlist.getSelectedItem().toString().toLowerCase().contains("nursery not found")) {
                    msclass.showMessage("Please select nursery name ");
                    return false;
                }
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select distributor")) {
                    msclass.showMessage("Please select distributor  ");
                    return false;
                }
                if (etFarmerName.getText().length() == 0) {
                    msclass.showMessage("Please enter contact person name  ");
                    return false;
                }
            }
            if (etMobileNo.getText().length() == 0) {
                msclass.showMessage("Please enter mobile number   ");
                return false;
            }
            if (etMobileNo.getText().length() != 10) {
                Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
                return false;
            }
            if (etComments.getText().length() == 0) {
                msclass.showMessage("Please enter comments  ");
                return false;
            }
            if (switchSKYN.isChecked()) {
                if (validateSKFields()) {
                    getSkValues();
                    finalsSkData = skData;
                } else {
                    msclass.showMessage("Samrudha Kisan details.");
                    return false;
                }
            } else {
                finalsSkData = "{}";
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }

    public void BindRetailer(String taluka) {
        try {
            spRDNlist.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();
                if (taluka == null) {
                    taluka = "";
                }
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                //String  searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                //        "where activity='Retailer'" +
                //      " and  upper(taluka)='" + taluka.toUpperCase() + "' order by  RetailerName ";

                String searchQuery = "SELECT distinct mobileno,firmname as RetailerName  FROM MDO_tagRetailerList " +
                        "where  upper(taluka)='" + taluka.toUpperCase() + "' order by  firmname ";


                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));
                Croplist.add(new GeneralMaster("0",
                        "TAG RETAILER (If not tagged earlier)"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRDNlist.setAdapter(adapter);
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void BindDist(String state) {
        try {
            spDist.setAdapter(null);
            String str = null;
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }

    //Bind vallage to spinner
    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
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

    public void setspinnercategory() {
        try {
            String category = spRetailerCategory.getSelectedItem().toString();
            spState.setSelection(0);
            switch (category) {
                case "FARMER CALL":
                    liswitch.setVisibility(View.VISIBLE);
                    lnwhatsup.setVisibility(View.VISIBLE);
                    lblvillage.setVisibility(View.VISIBLE);
                    villageCard.setVisibility(View.VISIBLE);
                    labelchange.setVisibility(View.GONE);
                    spRDNCard.setVisibility(View.GONE);
                    lblcontactperson.setText("FARMER NAME");
                    etFarmerName.setHint("Farmer Name");
                    break;
                case "RETAILER CALL":
                    etFarmerName.setHint("Contact Person Name");
                    liswitch.setVisibility(View.GONE);
                    lnwhatsup.setVisibility(View.GONE);
                    lblvillage.setVisibility(View.GONE);
                    villageCard.setVisibility(View.GONE);
                    labelchange.setVisibility(View.VISIBLE);
                    lblcontactperson.setText("CONTACT PERSON NAME");
                    labelchange.setText("SELECT RETAILER");
                    spRDNCard.setVisibility(View.VISIBLE);
                    BindIntialData();
                    //   bi
                    break;
                case "DISTRIBUTOR CALL":
                    etFarmerName.setHint("Contact Person Name");
                    lblcontactperson.setText("CONTACT PERSON NAME");
                    labelchange.setText("SELECT DISTRIBUTOR");
                    liswitch.setVisibility(View.GONE);
                    lnwhatsup.setVisibility(View.GONE);
                    lblvillage.setVisibility(View.GONE);
                    villageCard.setVisibility(View.GONE);
                    labelchange.setVisibility(View.VISIBLE);
                    spRDNCard.setVisibility(View.VISIBLE);
                    bindDistributor();

                    break;
                case "NURSERY CALL":
                    etFarmerName.setHint("Contact Person Name");
                    lblcontactperson.setText("CONTACT PERSON NAME");
                    labelchange.setText("SELECT NURSERY");
                    liswitch.setVisibility(View.GONE);
                    lnwhatsup.setVisibility(View.GONE);
                    lblvillage.setVisibility(View.GONE);
                    // spVillage.setVisibility(View.GONE);
                    labelchange.setVisibility(View.VISIBLE);
                    villageCard.setVisibility(View.GONE);
                    spRDNCard.setVisibility(View.VISIBLE);
                    BindIntialData();
                    break;

                default:
                    break;
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
        }
    }

    public void bindDistributor() {
        try {
            spRDNlist.setAdapter(null);
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            Croplist.add(new GeneralMaster("0",
                    "SELECT DISTRIBUTOR"));
            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                    "where activity='Distributor' " +
                    " order by  RetailerName ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRDNlist.setAdapter(adapter);

            dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void bindDistributorNewRetailer(Spinner spinner) {
        try {
            spinner.setAdapter(null);
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            Croplist.add(new GeneralMaster("0",
                    "SELECT DISTRIBUTOR"));
            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                    "where activity='Distributor' " +
                    " order by  RetailerName ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void bindDistributorNewRetailer() {
        try {

            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            Croplist.add(new GeneralMaster("0",
                    "SELECT DISTRIBUTOR"));
            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                    "where activity='Distributor' " +
                    " order by  RetailerName ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            allDistributtor = new String[cursor.getCount()];
            isAllDistributors = new boolean[cursor.getCount()];
            int i = 0;
            while (cursor.isAfterLast() == false) {

                allDistributtor[i] = cursor.getString(0).toUpperCase();
                if (hs_distributors.contains(cursor.getString(0).toUpperCase()))
                    isAllDistributors[i] = true;
                i++;
                cursor.moveToNext();
            }
            cursor.close();


            dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

    public void BindActivityCategory() {
        try {
            spRetailerCategory.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Croplist.add(new GeneralMaster("0",
                        "SELECT ACTIVITY  TYPE"));
                Croplist.add(new GeneralMaster("FARMER CALL",
                        "FARMER CALL"));
                Croplist.add(new GeneralMaster("RETAILER CALL",
                        "RETAILER CALL"));
                Croplist.add(new GeneralMaster("DISTRIBUTOR CALL",
                        "DISTRIBUTOR CALL"));
                Croplist.add(new GeneralMaster("NURSERY CALL",
                        "NURSERY CALL"));


                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailerCategory.setAdapter(adapter);
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

    public void BindTaluka(String dist) {
        try {
            spTaluka.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
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
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
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

    private void BindIntialData() {

        try {


            spRDNlist.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

            if (spRetailerCategory.getSelectedItem().toString().contains("RETAILER CALL")) {
                Croplist.add(new GeneralMaster("0",
                        "SELECT RETAILER"));
                Croplist.add(new GeneralMaster("RETAILER NOT FOUND",
                        "RETAILER NOT FOUND"));
            } else {
                Croplist.add(new GeneralMaster("0",
                        "SELECT NURSERY"));
                Croplist.add(new GeneralMaster("NURSERY NOT FOUND",
                        "NURSERY NOT FOUND"));
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRDNlist.setAdapter(adapter);

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void BindProductList(String str) {
        try {
            spproductlist.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Product "));

            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("MATNR") + "|" + jObject.getString("MAKTX") + "|" + jObject.getString("MTART") + "|" + jObject.getString("MATKL"),
                                jObject.getString("MAKTX") + "\n" + jObject.getString("MATNR")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msclass.showMessage("Internet network not available.");
            }
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spproductlist.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void BindRetailerList_online(String str) {
        pd.dismiss();
        spRDNlist.setAdapter(null);

        try {
            spRDNlist.setAdapter(null);

            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

            if (retailerCategory.contains("RETAILER CALL")) {
                Croplist.add(new GeneralMaster("0",
                        "SELECT RETAILER"));
            } else {
                Croplist.add(new GeneralMaster("0",
                        "SELECT NURSERY"));
            }
            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("mobileno"), jObject.getString("firmname")
                        ));
                    }
                    if (retailerCategory.contains("RETAILER CALL")) {
                        Croplist.add(new GeneralMaster("RETAILER NOT FOUND",
                                "RETAILER NOT FOUND"));
                    } else {
                        Croplist.add(new GeneralMaster("NURSERY NOT FOUND",
                                "NURSERY NOT FOUND"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msclass.showMessage("Internet network not available.");
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRDNlist.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    public void BindRetailerandNurseryonline(String taluka, String retailerCategory) {
        if (config.NetworkConnection()) {
            new GetBindRetailerandNurseryonline("1", usercode, retailerCategory, division, cmbDistributor).execute();

        } else {
            msclass.showMessage("Please check internet connection");
        }
    }

    public class GetBindRetailerandNurseryonline extends AsyncTask<String, String, String> {

        private String usercode, retailerCategory, division, returnstring, action, cmbDistributor;

        public GetBindRetailerandNurseryonline(String action, String usercode, String retailerCategory, String division, String cmbDistributor) {
            this.usercode = usercode;
            this.retailerCategory = retailerCategory;
            this.division = division;
            this.action = action;
            this.cmbDistributor = cmbDistributor;

        }

        protected void onPreExecute() {

            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetandSetVegRetailerListOnline"));
            postParameters.add(new BasicNameValuePair("Taluka", taluka));
            postParameters.add(new BasicNameValuePair("Dist", dist));
            postParameters.add(new BasicNameValuePair("State", state));
            postParameters.add(new BasicNameValuePair("retailerName", RetailerName));
            postParameters.add(new BasicNameValuePair("retailerFirmName", retailerfirmname));
            postParameters.add(new BasicNameValuePair("retailerMobileNo", mobileno));
            postParameters.add(new BasicNameValuePair("retailerCategory", retailerCategory));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("action", action));
            postParameters.add(new BasicNameValuePair("cordinates", cordinates));
            postParameters.add(new BasicNameValuePair("address", address));

            String Urlpath1 = cx.MDOurlpath;
            HttpPost httppost = new HttpPost(Urlpath1);
            Log.i("URL ", Urlpath1);
            Log.i("Param", postParameters.toString());


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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                // JSONObject jsonObject = new JSONObject(result);
                Log.i("Result", result);
                pd.dismiss();
                // if (result.contains("True")) {
                if (action.equals("1")) {
                    BindRetailerList_online(result);
                }
                if (action.equals("2")) {
                    if (result.contains("True")) {
                        // msclass.showMessage("New retailer data saved successfully.");
                        // BindRetailerList_online(result);

                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

    public void OpenSamrudhkisanDialog() {
        try {
            dialogSK.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        switch (buttonView.getId()) {
            case R.id.switchVGBhindi:

                if (buttonView.isChecked())
                    hs_vg.add("Bhindi");
                else
                    hs_vg.remove("Bhindi");

                break;
            case R.id.switchVGChilli:
                if (buttonView.isChecked())
                    hs_vg.add("Chilli");
                else
                    hs_vg.remove("Chilli");
                break;
            case R.id.switchVGBrinjal:
                if (buttonView.isChecked())
                    hs_vg.add("Brinjal");
                else
                    hs_vg.remove("Brinjal");
                break;
            case R.id.switchVGBottle:
                if (buttonView.isChecked())
                    hs_vg.add("Bottle Guard");
                else
                    hs_vg.remove("Bottle Guard");
                break;
            case R.id.switchVGCali:
                if (buttonView.isChecked())
                    hs_vg.add("Cauliflower");
                else
                    hs_vg.remove("Cauliflower");
                break;
            case R.id.switchVGTomato:
                if (buttonView.isChecked())
                    hs_vg.add("Tomato");
                else
                    hs_vg.remove("Tomato");
                break;
            case R.id.switchVGOther:
                if (buttonView.isChecked()) {
                    hs_vg.add("Other");
                    llvg.setVisibility(View.VISIBLE);
                } else {
                    hs_vg.remove("Other");
                    llvg.setVisibility(View.GONE);
                }
                break;

            case R.id.switchNHBhindi:
                if (buttonView.isChecked())
                    hs_nh.add("Bhindi");
                else
                    hs_nh.remove("Bhindi");

                break;
            case R.id.switchNHChilli:
                if (buttonView.isChecked())
                    hs_nh.add("Chilli");
                else
                    hs_nh.remove("Chilli");

                break;
            case R.id.switchNHBrinjal:
                if (buttonView.isChecked())
                    hs_nh.add("Brinjal");
                else
                    hs_nh.remove("Brinjal");

                break;
            case R.id.switchNHBottle:
                if (buttonView.isChecked())
                    hs_nh.add("Bottle Gourd");
                else
                    hs_nh.remove("Bottle Gourd");

                break;
            case R.id.switchNHCali:
                if (buttonView.isChecked())
                    hs_nh.add("Cauliflower");
                else
                    hs_nh.remove("Cauliflower");

                break;
            case R.id.switchNHTomato:
                if (buttonView.isChecked())
                    hs_nh.add("Tomato");
                else
                    hs_nh.remove("Tomato");
                break;

            case R.id.switchNHOther:
                if (buttonView.isChecked()) {
                    hs_nh.add("Other");
                    llnh.setVisibility(View.VISIBLE);
                } else {
                    hs_nh.remove("Other");
                    llnh.setVisibility(View.GONE);
                }
                break;


            case R.id.switchcdMahyco:


                if (buttonView.isChecked())
                    hs_cd.add("Mahyco Pvt Ltd");
                else
                    hs_cd.remove("Mahyco Pvt Ltd");

                break;
            case R.id.switchcdbasf:
                if (buttonView.isChecked())
                    hs_cd.add("BASF");
                else
                    hs_cd.remove("BASF");

                break;
            case R.id.switchcdeastwest:
                if (buttonView.isChecked())
                    hs_cd.add("East- west seeds");
                else
                    hs_cd.remove("East- west seeds");

                break;
            case R.id.switchcdrasi:
                if (buttonView.isChecked())
                    hs_cd.add("Rasi seeds");
                else
                    hs_cd.remove("Rasi seeds");

                break;

            case R.id.switchcdvnr:
                if (buttonView.isChecked())
                    hs_cd.add("VNR");
                else
                    hs_cd.remove("VNR");

                break;
            case R.id.switchcdupl:
                if (buttonView.isChecked())
                    hs_cd.add("UPL");
                else
                    hs_cd.remove("UPL");

                break;
            case R.id.switchcdsyngenta:
                if (buttonView.isChecked())
                    hs_cd.add("Syngenta");
                else
                    hs_cd.remove("Syngenta");

                break;
            case R.id.switchcdseminis:
                if (buttonView.isChecked())
                    hs_cd.add("Seminis");
                else
                    hs_cd.remove("Seminis");

                break;
            case R.id.switchcdnamdhari:
                if (buttonView.isChecked())
                    hs_cd.add("Namdhari seeds");
                else
                    hs_cd.remove("Namdhari seeds");

                break;
            case R.id.switchcdother:
                if (buttonView.isChecked()) {
                    hs_cd.add("Other");
                    llcd.setVisibility(View.VISIBLE);
                } else {
                    hs_cd.remove("Other");
                    llcd.setVisibility(View.GONE);
                }
                break;

            case R.id.switchobNO:
                if (buttonView.isChecked())
                    hs_ob.add("No");
                else
                    hs_ob.remove("No");

                break;
            case R.id.switchobbask:
                if (buttonView.isChecked())
                    hs_ob.add("BASF");
                else
                    hs_ob.remove("BASF");

                break;
            case R.id.switchobeastwest:
                if (buttonView.isChecked())
                    hs_ob.add("East- west seeds");
                else
                    hs_ob.remove("East- west seeds");
                break;
            case R.id.switchobrasi:
                if (buttonView.isChecked())
                    hs_ob.add("Rasi seeds");
                else
                    hs_ob.remove("Rasi seeds");
                break;
            case R.id.switchobvnr:
                if (buttonView.isChecked())
                    hs_ob.add("VNR");
                else
                    hs_ob.remove("VNR");
                break;
            case R.id.switchobupl:
                if (buttonView.isChecked())
                    hs_ob.add("UPL");
                else
                    hs_ob.remove("UPL");
                break;
            case R.id.switchobsyngejta:
                if (buttonView.isChecked())
                    hs_ob.add("Syngenta");
                else
                    hs_ob.remove("Syngenta");
                break;
            case R.id.switchobseminis:
                if (buttonView.isChecked())
                    hs_ob.add("Seminis");
                else
                    hs_ob.remove("Seminis");
                break;
            case R.id.switchobnamdhari:
                if (buttonView.isChecked())
                    hs_ob.add("Namdhari seeds");
                else
                    hs_ob.remove("Namdhari seeds");
                break;
            case R.id.switchobOther:
                if (buttonView.isChecked()) {
                    hs_ob.add("Other");
                    llob.setVisibility(View.VISIBLE);
                } else {
                    hs_ob.remove("Other");
                    llob.setVisibility(View.GONE);
                }
                break;
        }

        getSkValues();


    }

    void getSkValues() {

        finalJsonObject = new JSONObject();
        jsonArray_ob = new JSONArray();
        jsonArray_cd = new JSONArray();
        jsonArray_nh = new JSONArray();
        jsonArray_vg = new JSONArray();
        strob = "";
        strcd = "";
        strnh = "";
        strvg = "";


        for (Object str : hs_ob) {
            try {

                Log.i("Values", str.toString());
                JSONObject jsonObject = new JSONObject();
                String d = "";
                d = str.toString().trim();
                if (d.toLowerCase().contains("other")) {
                    d += "(" + et_otherob.getText().toString().trim() + ")";
                }
                jsonObject.put("text", d);
                jsonArray_ob.put(jsonObject);
                strob += d.toString().trim() + "-";
            } catch (Exception e) {

            }
        }
        for (Object str : hs_cd) {
            try {
                Log.i("Values", str.toString());
                JSONObject jsonObject = new JSONObject();
                String d = "";
                d = str.toString().trim();
                if (d.toLowerCase().contains("other")) {
                    d += "(" + et_othercd.getText().toString().trim() + ")";
                }
                jsonObject.put("text", d);
                jsonArray_cd.put(jsonObject);
                strcd += d.toString().trim() + "-";
            } catch (Exception e) {

            }
        }

        for (Object str : hs_vg) {
            try {
                Log.i("Values", str.toString());
                JSONObject jsonObject = new JSONObject();
                String d = "";
                d = str.toString().trim();
                if (d.toLowerCase().contains("other")) {
                    d += "(" + et_othervg.getText().toString().trim() + ")";
                }
                jsonObject.put("text", d);
                jsonArray_vg.put(jsonObject);
                strvg += d.toString().trim() + "-";
            } catch (Exception e) {

            }
        }
        for (Object str : hs_nh) {
            try {
                Log.i("Values", str.toString());
                JSONObject jsonObject = new JSONObject();
                String d = "";
                d = str.toString().trim();
                if (d.toLowerCase().contains("other")) {
                    d += "(" + et_othernh.getText().toString().trim() + ")";
                }
                jsonObject.put("text", d);
                jsonArray_nh.put(jsonObject);
                strnh += d.toString().trim() + "-";
            } catch (Exception e) {

            }
        }
        switchSKYN.isChecked();
        str_COF = sp_catfarmer.getSelectedItem().toString();
        str_Land_Under_Veg = et_acres.getText().toString();
        str_Veg_Grown = strvg;
        str_Crop_New_Hybrid = strnh;
        str_IsDemoTaken = sp_demotaken.getSelectedItem().toString();
        str_CompnanyDemoSown = strcd;
        str_FarmersAdopted = et_adoptedfarmer.getText().toString();
        str_Mahyco_Exp = et_mahycoproduct.getText().toString();
        str_OtherBrands = strob;
        try {
            finalJsonObject.put("IsSamrudhKisan", switchSKYN.isChecked());
            finalJsonObject.put("COF", sp_catfarmer.getSelectedItem().toString());
            finalJsonObject.put("Land_Under_Veg", et_acres.getText().toString());
            finalJsonObject.put("Veg_Grown", strvg);
            finalJsonObject.put("Crop_New_Hybrid", strnh);
            finalJsonObject.put("IsDemoTaken", sp_demotaken.getSelectedItem().toString());
            finalJsonObject.put("CompnanyDemoSown", strcd);
            finalJsonObject.put("FarmersAdopted", et_adoptedfarmer.getText().toString());
            finalJsonObject.put("Mahyco_Exp", et_mahycoproduct.getText().toString());
            finalJsonObject.put("OtherBrands", strob);

            JSONObject fjson = new JSONObject();

            fjson.put("Model", finalJsonObject);

            Log.i("Final Json", fjson.toString());
            skData = fjson.toString().trim();


        } catch (Exception e) {

        }
    }

    boolean validateSKFields() {
        getSkValues();
        int cnt = 0;
        str_COF = sp_catfarmer.getSelectedItem().toString();
        str_Land_Under_Veg = et_acres.getText().toString();
        str_Veg_Grown = strvg;
        str_Crop_New_Hybrid = strnh;
        str_IsDemoTaken = sp_demotaken.getSelectedItem().toString();
        str_CompnanyDemoSown = strcd;
        str_FarmersAdopted = et_adoptedfarmer.getText().toString();
        str_Mahyco_Exp = et_mahycoproduct.getText().toString();
        str_OtherBrands = strob;
        str_vali_message = "";
        if (str_COF.trim().equals("") || str_COF.trim().toLowerCase().contains("select")) {
            str_vali_message += "* Select Categorization of Farmer.\n";
            cnt++;
        }
        if (str_Land_Under_Veg.trim().equals("")) {
            str_vali_message += "* Enter Land under vegitable cultivation.\n";
            cnt++;
        }
        if (hs_vg.isEmpty()) {
            str_vali_message += "* Select Vegetable grown.\n";
            cnt++;
        }
        if (hs_cd.isEmpty()) {
            if (sp_demotaken.getSelectedItem().toString().trim().toLowerCase().equals("yes")) {
                str_vali_message += "* Select which company's demo you have sown?.\n";
                cnt++;
            }
        }
        if (str_IsDemoTaken.trim().equals("") || str_IsDemoTaken.trim().toLowerCase().contains("Select")) {
            str_vali_message += "* Select have you taken any demos.\n";
            cnt++;
        }
        if (hs_nh.isEmpty()) {
            str_vali_message += "* Select which of following crop new hybrid have you sown in the last 3 years?.\n";
            cnt++;
        }
        if (str_FarmersAdopted.trim().equals("")) {
            str_vali_message += "* Enter how many farmers adopted your advice.\n";
            cnt++;
        }
        if (str_Mahyco_Exp.trim().equals("")) {
            str_vali_message += "* Enter how long you have been using Mahyco's product?.\n";
            cnt++;
        }
        if (hs_ob.isEmpty()) {
            str_vali_message += "* Enter what are the other brands that you have sown?.\n";
            cnt++;
        }
        if (switchcdother.isChecked()) {
            if (et_othercd.getText().toString().trim().equals("")) {
                str_vali_message += "* Enter other company demo?.\n";
                et_othercd.setError("");
                cnt++;
            }
        }
        if (switchNHOther.isChecked()) {
            if (et_othernh.getText().toString().trim().equals("")) {
                str_vali_message += "* Enter other new hybrid?.\n";
                et_othernh.setError("");
                cnt++;
            }
        }
        if (switchobOther.isChecked()) {
            if (et_otherob.getText().toString().trim().equals("")) {
                str_vali_message += "* Enter other brand?.\n";
                et_otherob.setError("");
                cnt++;
            }
        }
        if (switchVGOther.isChecked()) {
            if (et_othervg.getText().toString().trim().equals("")) {
                str_vali_message += "* Enter other Vegetable grown?.\n";
                et_othervg.setError("");
                cnt++;
            }
        }


        if (cnt == 0) {
            return true;
        } else
            return false;
    }


    @Override
    protected void onPause() {
        super.onPause();


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

    @Override
    protected void onResume() {
        super.onResume();
        try {

            startFusedLocationService();

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }
    }

    private void manageGeoTag() {
        if (cordinates != null && !cordinates.contains("null")) {


            startFusedLocationService();


        } else {
            Utility.showAlertDialog("Info", "Please wait fetching location", context);
            startFusedLocationService();
        }
    }

    //Stop location fuseApi
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

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                strAdd = addresses.get(0).getAddressLine(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }


    //start fusedApi location
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
                AlertDialog.Builder builder = new AlertDialog.Builder(BCFCallTBM.this);

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
        } catch (Exception ex) {
            ex.printStackTrace();

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


    public int getLocationMode() {
        try {
            return Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
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
    public synchronized void onLocationChanged(Location arg0) {

        try {
            if (arg0 == null) {
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

            manageGeoTag();
            stopFusedApi();

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }

    public void selectMultipleDistributors() {
        try {


            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // set title
            builder.setTitle("Select Distributors");

            // set dialog non cancelable
            builder.setCancelable(false);
            builder.setMultiChoiceItems(allDistributtor, isAllDistributors, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked)
                        hs_distributors.add(allDistributtor[which]);
                    else
                        hs_distributors.remove(allDistributtor[which]);

                    txt_distributors.setText(Html.fromHtml(hs_distributors.toString().replace("[", "<ol><li>").replace("]", "</ol>").replace(",", "</li><li>")));
                }
            });
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        } catch (Exception e) {

        }
    }
}


