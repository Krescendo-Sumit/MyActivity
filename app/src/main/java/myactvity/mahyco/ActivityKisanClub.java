package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class ActivityKisanClub extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Context context;
    private long mLastClickTime = 0;

    private static final String TAG = "ActivityRetailerSurvey";
    SearchableSpinner spState, spDist, spTerritory, spVillage, spTehsil;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
 //   ProgressDialog dialog;

    Config config;
    Switch switchYN,switchSKYN;
    Button btnSubmit;
    MultiSelectionSpinner spCropSown;
    EditText etArea, etWhatsappNumber, etFarmerName, etMobileNo,etComments;
    String userCode, state, taluka, dist;
    RadioGroup radGroupUnit, radGroupVeg, radGroupAware, radGroupPerMAhyco, radGroupPerSungro;
    RadioButton radVEGBU, radRCBU, radYes, radNo, radMahyco, radSungro, radBoth, radNone, radZero, radOne, radTwo, radSunZero, radSunOne, radSunTwo;
    String unitType = "", territory;
    String reply;
    String ratingMahyco;
    String ratingSungro;
    String productAwareness;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;





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

    LinearLayout llnh, llvg, llcd, llob,llswitchSK;
    EditText et_othernh, et_othervg, et_othercd, et_otherob;
    CardView card_demotaken;
    TextView txt_demotaken;
    double skmin = 0.0, skmax = 0.0;



    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_kisan_club);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTehsil = (SearchableSpinner) findViewById(R.id.spTehsil);
        spTerritory = (SearchableSpinner) findViewById(R.id.spTerritory);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropSown = (MultiSelectionSpinner) findViewById(R.id.spCropSown);
        radGroupUnit = (RadioGroup) findViewById(R.id.radGroupUnit);
        llswitchSK = (LinearLayout) findViewById(R.id.llswitchSK);
        dialogSK = new Dialog(context);
        dialogSK.setContentView(R.layout.popup_bcf_sk);
        dialogSK.setContentView(R.layout.popup_bcf_sk);
        skControlsit();
        radGroupVeg = (RadioGroup) findViewById(R.id.radGroupVeg);
        radGroupAware = (RadioGroup) findViewById(R.id.radGroupAware);
        radGroupPerMAhyco = (RadioGroup) findViewById(R.id.radGroupPerMAhyco);
        radGroupPerSungro = (RadioGroup) findViewById(R.id.radGroupPerSungro);
        switchYN = (Switch) findViewById(R.id.switchYN);
        switchSKYN = (Switch) findViewById(R.id.switchSKYN);


        radVEGBU = (RadioButton) findViewById(R.id.radVEGBU);
        radRCBU = (RadioButton) findViewById(R.id.radRCBU);
        radYes = (RadioButton) findViewById(R.id.radYes);
        radNo = (RadioButton) findViewById(R.id.radNo);
        radMahyco = (RadioButton) findViewById(R.id.radMahyco);
        radSungro = (RadioButton) findViewById(R.id.radSungro);
        radBoth = (RadioButton) findViewById(R.id.radBoth);
        radNone = (RadioButton) findViewById(R.id.radNone);
        radZero = (RadioButton) findViewById(R.id.radZero);
        radOne = (RadioButton) findViewById(R.id.radOne);
        radTwo = (RadioButton) findViewById(R.id.radTwo);
        radSunZero = (RadioButton) findViewById(R.id.radSunZero);
        radSunOne = (RadioButton) findViewById(R.id.radSunOne);
        radSunTwo = (RadioButton) findViewById(R.id.radSunTwo);
        etArea = (EditText) findViewById(R.id.etArea);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etComments= (EditText) findViewById(R.id.etComments);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
//
//        dialog = new ProgressDialog(context);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       userCode = pref.getString("UserID", null);


        bindState();
        bindVegCrops();


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
                bindTerritory(state);

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


                bindTehsil(dist);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spTehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    territory = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spCropSown.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {


                if (indices.size() > 3) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKisanClub.this);

                    builder.setTitle("Info");
                    builder.setMessage("Please select only three Crops");
                    builder.setCancelable(false);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            spCropSown.performClick();
                        }
                    });


                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });

        radGroupUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radVEGBU:

                        if (radVEGBU.isChecked()) {
                            unitType = "VEGBU";

                        }
                        radRCBU.setChecked(false);
                        break;
                    case R.id.radRCBU:
                        if (radVEGBU.isChecked()) {

                            unitType = "VEGBU";
                        } else {
                            unitType = "RCBU";

                        }
                        radVEGBU.setChecked(false);
                        break;


                }
                Log.d("unitType", unitType);
            }
        });


        radGroupVeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYes:

                        if (radYes.isChecked()) {
                            reply = "Yes";

                        }
                        radNo.setChecked(false);
                        break;
                    case R.id.radNo:
                        if (radYes.isChecked()) {

                            reply = "Yes";
                        } else {
                            reply = "No";

                        }
                        radYes.setChecked(false);
                        break;


                }
                Log.d("reply", reply);
            }
        });
        radGroupPerMAhyco.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radZero:

                        if (radZero.isChecked()) {
                            ratingMahyco = "0";
                        } else if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else {
                            ratingMahyco = "2";

                        }
                        radOne.setChecked(false);
                        radTwo.setChecked(false);
                        break;
                    case R.id.radOne:
                        if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else if (radTwo.isChecked()) {
                            ratingMahyco = "2";
                        } else {
                            ratingMahyco = "0";

                        }
                        radZero.setChecked(false);
                        radTwo.setChecked(false);
                        break;
                    case R.id.radTwo:
                        if (radTwo.isChecked()) {
                            ratingMahyco = "2";
                        } else if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else {
                            ratingMahyco = "0";

                        }
                        break;

                }
                Log.d("ratingMahyco", ratingMahyco);
            }
        });
        radGroupPerSungro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radSunZero:

                        if (radSunZero.isChecked()) {
                            ratingSungro = "0";
                        } else if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else {
                            ratingSungro = "2";

                        }
                        radSunOne.setChecked(false);
                        radSunTwo.setChecked(false);
                        break;
                    case R.id.radSunOne:
                        if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else if (radSunTwo.isChecked()) {
                            ratingSungro = "2";
                        } else {
                            ratingSungro = "0";

                        }
                        radSunZero.setChecked(false);
                        radSunTwo.setChecked(false);
                        break;
                    case R.id.radSunTwo:
                        if (radSunTwo.isChecked()) {
                            ratingSungro = "2";
                        } else if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else {
                            ratingSungro = "0";

                        }
                        break;

                }
                Log.d("ratingSungro", ratingSungro);
            }
        });


        radGroupAware.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radMahyco:

                        if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";
                        } else if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";
                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else {
                            productAwareness = "None";

                        }
                        radSungro.setChecked(false);
                        radBoth.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radSungro:
                        if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";

                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";
                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else {
                            productAwareness = "None";

                        }
                        radMahyco.setChecked(false);
                        radBoth.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radBoth:
                        if (radBoth.isChecked()) {
                            productAwareness = "Both";

                        } else if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";
                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";

                        } else {
                            productAwareness = "None";

                        }
                        radMahyco.setChecked(false);
                        radSungro.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radNone:
                        if (radNone.isChecked()) {
                            productAwareness = "None";

                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";

                        } else {
                            productAwareness = "Only Sungro";

                        }
                        break;

                }
                Log.d("productAwareness", productAwareness);
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKisanClub.this);

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

                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }

        });

    }
    public void OpenSamrudhkisanDialog() {
        try {
            dialogSK.show();
        } catch (Exception e) {

        }
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
                if(s.toString().trim().equals(""))
                {
                    et_mahycoproduct.setError("REQUIRED");
                }else
                {
                    int a=Integer.parseInt(s.toString().trim());
                    if(a>0 && a<60)
                    {

                    }else
                    {
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


    //validate all fields
    public boolean validation() {
//        if (radGroupUnit.getCheckedRadioButtonId() == -1) {
//            Utility.showAlertDialog("", "Please Select Unit", context);
//
//            return false;
//        }

        if (spState.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select State", context);
            return false;
        }
       /* if (spTerritory.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Territory", context);
            return false;
        }*/
        if (spDist.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select District", context);
            return false;
        }
        if (spTehsil.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Tehsil", context);
            return false;
        }
        if (spVillage.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Village", context);
            return false;
        }

        if (etFarmerName.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Farmer Name", context);
            return false;
        }


        if (etMobileNo.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
            return false;
        }

        if (etWhatsappNumber.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Whatsapp Number", context);
            return false;
        }

       /* if (radGroupVeg.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }*/

        if ( etComments.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter  comments.", context);

            return false;
        }

        if (switchSKYN.isChecked()) {
            if (validateSKFields()) {
                getSkValues();
                finalsSkData = skData;
            } else {
                Utility.showAlertDialog("Info", "Please check Samrudhakisan details.", context);
                return false;
            }
        } else {
            finalsSkData = "{}";
        }

      /*  if (etArea.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter  Area.", context);

            return false;
        }

        if (spCropSown.getSelectedItem().toString().equals("SELECT CROP")) {
            Utility.showAlertDialog("Info", "Please Select Crops", context);
            return false;
        }


        if (spCropSown.getSelectedIndices().size() > 3) {
            Utility.showAlertDialog("Info", "Please select only three Crops", context);
            return false;
        }


        if (radGroupAware.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }
        if (radGroupPerMAhyco.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }
        if (radGroupPerSungro.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }*/
        //  String mno = etMobileNo.getText().toString() != null ? etMobileNo.getText().toString() : "";;
        String mno = etMobileNo.getText().toString();

        /*if (!isAlreadydone(mno)) {
            Utility.showAlertDialog("Info", "This mobile no already exists", context);
            return false;
        }*/


        return true;
    }

    //if data already exit
    private boolean isAlreadydone(String mno) {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExistsKisanClub(mno);

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


    }

    //bind crops to multiselection spinner
    public void bindVegCrops() {

        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;


            searchQuery = "SELECT distinct CropName  FROM CropMaster";


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
                    spCropSown.setItems(array);
                    spCropSown.hasNoneOption(true);
                    spCropSown.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }


    }

    //bind state soinner
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
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }

    }

    //bind district spinner
    public void bindDist(String state) {
        try {
            spDist.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";

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
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    //bind Tehsil spinner
    public void bindTehsil(String dist) {
        try {
            spTehsil.setAdapter(null);
            String str = null;
            try {


                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TEHSIL",
                        "SELECT TEHSIL"));

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
                spTehsil.setAdapter(adapter);


            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
        }


    }


    public void bindTerritory(String state) {
        try {
            spTerritory.setAdapter(null);

            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct territory  FROM StateTerritoryMaster" +
                        " where state_code='" + state + "' order by territory asc  ";

                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TERRITORY",
                        "SELECT TERRITORY"));

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
                spTerritory.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }


    //save data to db//

    public void saveToDb() {


        String farmerName = etFarmerName.getText().toString();
        String mobileNumber = etMobileNo.getText().toString();
        String whatsappNumber = "";


        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsappNumber = "";

        } else {


            whatsappNumber = etWhatsappNumber.getText().toString();
        }


        String village = spVillage.getSelectedItem().toString();
        String state = spState.getSelectedItem().toString();
        String district = spDist.getSelectedItem().toString();
        String territory = spTerritory.getSelectedItem().toString();
        String tehsil = spTehsil.getSelectedItem().toString();
        String comments =etComments.getText().toString()+"-LD"+ "~" + finalsSkData;
        String vegetableChoice = reply;
        String area = etArea.getText().toString();
        String cropSown = spCropSown.getSelectedItemsAsString();
        String isSynced = "0";
        Date entrydate = new Date();
        long epoch = entrydate.getTime();
        System.out.println(epoch);
        String timeStamp = String.valueOf(epoch);

        boolean fl = mDatabase.insertToKisanClub(entrydate.toString(), timeStamp, userCode, state,
                territory, district, tehsil, village, unitType, farmerName, mobileNumber, whatsappNumber, vegetableChoice, area,
                cropSown, productAwareness, ratingMahyco, ratingSungro, isSynced,comments);

        if (fl) {

            //     Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            uploadKisanClubData("KisanClubData");

        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

    //kisan club data fetch and upload api calling method with data fetch
    public void uploadKisanClubData(String functionName) {
      if (config.NetworkConnection())
      {
//            dialog = new ProgressDialog(ActivityKisanClub.this);
//            dialog.setTitle("Data Uploading ...");
//            dialog.setMessage("Please wait.");
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.show();
            String str = null;
            String searchQuery = "select  *  from KisanClubData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table1", jsonArray);
                        Log.d("rhtt", "uploadKisanClubData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadKisanData(functionName, jsonObject).execute(SERVER).get();

                    cursor.close();


                    if (!str.equals("")) {

                        if (str.contains("True")) {

                        etFarmerName.setText("");
                        etMobileNo.setText("");
                        etWhatsappNumber.setText("");
                          etComments.setText("");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String mno = jsonArray.getJSONObject(i).getString("mobileNumber");
                            mDatabase.updateKisanData(mno, "1");
                            Log.d("mno", "onResultReceived: " + mno);
                        }

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
                        Utility.showAlertDialog("Error","Poor Internet: Please try after sometime.",context);
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


//upload api for kisan club

    public class UploadKisanData extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;


        public UploadKisanData(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;


        }

        protected void onPreExecute() {

           // dialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("KisanClubData", obj.toString()));

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


}
