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
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SearchableSpinner2;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class BCFCallTBM extends AppCompatActivity {
    private static final String TAG = "RetailerPOG";

    public EditText txtIrrigationdt, txtirrigation,
            txtQty;
    public SearchableSpinner spDist, spnewretailer, spState, spVillage, spCropType, spProductName, spMyactvity, spComment;
    private long mLastClickTime = 0;

    MultiSelectionSpinner spdistr, spBussiness;
    RadioButton rndNo, rndYes;
    EditText  txtrtname, txtmobilewhatup,
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
    Switch switchYN,switchSKYN;
    private String dist, taluka, state,village,whatsup,firmname,comments;
    String RetailerName, retailerfirmname, customer, name, DLV_plant,
            customregroup, cmbproductlist, mobileno, retailerCategory, userCode;
    TextView lbltaluka;
    EditText etArea, etWhatsappNumber, etFarmerName, etMobileNo, etComments;
    //For GPs location
    String cordinates;
    String address;
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
    CardView villageCard,spRDNCard;
    ProgressDialog pd;

    String isSamrudhakisan="",skData="";
    Dialog dialogSK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcfcall_tbm);
        getSupportActionBar().hide(); //<< this
        context = this;
        dialogSK=new Dialog(context);
        dialogSK.setContentView(R.layout.popup_bcf_sk);
        cx = new CommonExecution(this);
        Toast.makeText(context, "Hii", Toast.LENGTH_SHORT).show();
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
        spRDNCard= (CardView) findViewById(R.id.spRDNCard);
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
                    if(retailerCategory.toUpperCase().contains("RETAILER CALL"))
                    {
                        if (!spRDNlist.getSelectedItem().toString().toLowerCase().contains("retailer not found") &&
                                !spRDNlist.getSelectedItem().toString().toLowerCase().contains("select retailer"))
                        {
                            String[] distr=spRDNlist.getSelectedItem().toString().split("\\|");
                            etFarmerName.setText(distr[1].toString());
                            etMobileNo.setText(gm.Code().trim());

                        }
                    }
                    if(retailerCategory.toUpperCase().contains("NURSERY CALL"))
                    {
                        if (!spRDNlist.getSelectedItem().toString().toLowerCase().contains("nursery not found") &&
                                !spRDNlist.getSelectedItem().toString().toLowerCase().contains("select nursery"))
                        {
                            String[] distr=spRDNlist.getSelectedItem().toString().split("\\|");
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
                if(isChecked)
                {
                    isSamrudhakisan="YES";
                    OpenSamrudhkisanDialog();
                }
                else
                {
                    isSamrudhakisan="NO";
                    skData="{}";

                }
            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation())
                {
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
         comments = etComments.getText().toString() + "";
        mobileno= etMobileNo.getText().toString() + "";
        //whatsappNumber= etWhatsappNumber.getText().toString();
        firmname=spRDNlist.getSelectedItem().toString();
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
            if (count > 0)
            {

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
                        jsonsubObject.put("whatsappNumber",whatsup);
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

            String utf=HttpUtils.convertStringToUTF8(obj.toString().trim());
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

            final EditText txtretailermobileno = (EditText) dialog.findViewById(R.id.txtretailermobileno);
            if (spRetailerCategory.getSelectedItem().toString().contains("NURSERY CALL")) {
                lblretailername.setText("CONTACT PERSON NAME");
                lblretailerfirmname.setText("NURSERY NAME");

            }
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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
                            return ;
                        }

                        retailerfirmname = txtretailerfirmname.getText().toString().trim();
                        RetailerName = txtretailername.getText().toString().trim();
                        mobileno = txtretailermobileno.getText().toString().trim();
                        String str = addPOGretaill();
                        if (str.contains("True")) {
                            txtretailername.setText("");
                            txtretailermobileno.setText("");
                            txtretailerfirmname.setText("");
                            msclass.showMessage("data saved successfully");
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
            mobileno= etMobileNo.getText().toString() + "";
            //whatsappNumber= etWhatsappNumber.getText().toString();
            firmname=spRDNlist.getSelectedItem().toString();
            GeneralMaster vr = (GeneralMaster) spVillage.getSelectedItem();

            if (rc.Code().toLowerCase().equals("farmer call")) {

                if (vr.Code().toLowerCase().equals("0")) {
                    msclass.showMessage("Please select village");
                    return false;
                }

                if (etFarmerName.getText().length()==0) {
                    msclass.showMessage("Please enter farmer name ");
                    return false;
                }
            }
            else {
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select retailer") || spRDNlist.getSelectedItem().toString().toLowerCase().contains("retailer not found")) {
                    msclass.showMessage("Please select retailer firm name ");
                    return false;
                }
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select nursery") || spRDNlist.getSelectedItem().toString().toLowerCase().contains("nursery not found")) {
                    msclass.showMessage("Please select nursery name ");
                    return false;
                }
                if (spRDNlist.getSelectedItem().toString().toLowerCase().equals("select distributor") ) {
                    msclass.showMessage("Please select distributor  ");
                    return false;
                }
                if (etFarmerName.getText().length()==0) {
                    msclass.showMessage("Please enter contact person name  ");
                    return false;
                }
            }
            if (etMobileNo.getText().length()==0) {
                msclass.showMessage("Please enter mobile number   ");
                return false;
            }
            if (etMobileNo.getText().length() != 10) {
                Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
                return false;
            }
            if (etComments.getText().length()==0) {
                msclass.showMessage("Please enter comments  ");
                return false;
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
        }
        catch (Exception ex)
        {
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
        try{
            dialogSK.show();
        }catch (Exception e)
        {

        }
    }

}



   /*

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
                strAdd=addresses.get(0).getAddressLine(0);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BCFCallTBM .this);

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

                android.support.v7.app.AlertDialog alert = builder.create();
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
            address = getCompleteAddressString(lati, longi);

            Log.d(TAG, "onlocation" + cordinates);
            manageGeoTag();

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }
*/


