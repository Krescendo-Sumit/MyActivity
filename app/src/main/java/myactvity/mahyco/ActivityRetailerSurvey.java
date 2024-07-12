package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;


/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class ActivityRetailerSurvey extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {
    Context context;

    private static final String TAG = "ActivityRetailerSurvey";
    SearchableSpinner spState, spDist, spTerritory, spTehsil;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    EditText etWhatsappNumber, etRetailerName, etContactPersonName, etRetailerNumber, etAvgVolume, etVolumeMahyco, etOtherCompany;
    String userCode, state, taluka, dist;
    RadioGroup radGroupUnit;
    RadioButton radVEGBU, radRCBU;
    Switch switchYN, switchYNRet;
    Button btnSubmit;
    ImageView imgBtnGps;
    TextView lblheader, tvCordinates, tvAddress;
    MultiSelectionSpinner spdistr, spBusiness, spRetailerOtherCompany, spMahyco, spSungro, spvegcrops;
    ProgressDialog dialog;
    private long mLastClickTime = 0;
    String cordinates;
    String address="";
    String otherCompany;
    String unitType = "";
    String cordinatesmsg = "ADDRESS TAG : *";


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

    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_retailer_survey);
//        getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTerritory = (SearchableSpinner) findViewById(R.id.spTerritory);
        spTehsil = (SearchableSpinner) findViewById(R.id.spTehsil);
        radGroupUnit = (RadioGroup) findViewById(R.id.radGroupUnit);
        radVEGBU = (RadioButton) findViewById(R.id.radVEGBU);
        radRCBU = (RadioButton) findViewById(R.id.radRCBU);
        spdistr = (MultiSelectionSpinner) findViewById(R.id.spdistr);
        spBusiness = (MultiSelectionSpinner) findViewById(R.id.spBusiness);
        spMahyco = (MultiSelectionSpinner) findViewById(R.id.spMahyco);
        spSungro = (MultiSelectionSpinner) findViewById(R.id.spSungro);
        spvegcrops = (MultiSelectionSpinner) findViewById(R.id.spvegcrops);
        spRetailerOtherCompany = (MultiSelectionSpinner) findViewById(R.id.spRetailerOtherCompany);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etRetailerName = (EditText) findViewById(R.id.etRetailerName);
        etContactPersonName = (EditText) findViewById(R.id.etContactPersonName);
        etRetailerNumber = (EditText) findViewById(R.id.etRetailerNumber);
        etAvgVolume = (EditText) findViewById(R.id.etAvgVolume);
        etVolumeMahyco = (EditText) findViewById(R.id.etVolumeMahyco);
        etOtherCompany = (EditText) findViewById(R.id.etOtherCompany);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        lblheader = (TextView) findViewById(R.id.lblheader);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);


        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);

        switchYN = (Switch) findViewById(R.id.switchYN);
        switchYNRet = (Switch) findViewById(R.id.switchYNRet);
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        userCode = pref.getString("UserID", null);

        bindState();
        bindBusiness();
        bindRetailerOtherCompany();
        bindMahycoSold();
        bindSungroSold();
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

        lblheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(ActivityRetailerSurvey.this, AndroidDatabaseManager.class);
//                startActivity(intent);


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

                binddistributor(dist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        imgBtnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cordinates != null && !cordinates.contains("null")) {
                    if (tvCordinates.getText().toString().contains("Yes")) {
                        imgBtnGps.setImageResource(R.drawable.ic_location_off);
                        cordinatesmsg = "ADDRESS TAG : \n";

                    } else {
                        if (lati != 0) {
                            imgBtnGps.setImageResource(R.drawable.ic_location_on);
                            cordinatesmsg = "ADDRESS TAGGED RECIEVED SUCCESSFULLY : \n";
                        } else {
                            startFusedLocationService();
                        }
                    }
                } else {
                    Utility.showAlertDialog("Info", "Please wait fetching location", context);
                    startFusedLocationService();
                }
            }
        });


        spTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                bindVillage(taluka);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spdistr.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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


        switchYNRet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    etOtherCompany.setVisibility(View.VISIBLE);

                } else {
                    etOtherCompany.setVisibility(View.GONE);

                }


            }
        });


        switchYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    etWhatsappNumber.setText(etRetailerNumber.getText().toString());

                    etRetailerNumber.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("ONtext changed " + s.toString());
                            etWhatsappNumber.setText(etRetailerNumber.getText().toString());

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
                } else if (!switchYN.isChecked() || etRetailerNumber.getText().toString().equals("")) {

                    etRetailerNumber.addTextChangedListener(new TextWatcher() {

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


        spBusiness.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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
        spRetailerOtherCompany.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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
        spMahyco.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
//                if (indices.size() > 5) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRetailerSurvey.this);
//
//                    builder.setTitle("Info");
//                    builder.setMessage("Please select only five Mahyco Products");
//                    builder.setCancelable(false);
//                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            spSungro.performClick();
//                        }
//                    });
//
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                }
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });
        spvegcrops.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {


            }

            @Override
            public void selectedIndices(List<Integer> indices) {


//                if (indices.size() > 5) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRetailerSurvey.this);
//
//                    builder.setTitle("Info");
//                    builder.setMessage("Please select only five Crops");
//                    builder.setCancelable(false);
//                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            spSungro.performClick();
//                        }
//                    });
//
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                }
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });
        spSungro.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
//                if (indices.size() > 5) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRetailerSurvey.this);
//
//                    builder.setTitle("Info");
//                    builder.setMessage("Please select only five Sungro Products");
//                    builder.setCancelable(false);
//                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            spSungro.performClick();
//                        }
//                    });
//
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                }
            }


            @Override
            public void selectedStrings(List<String> strings) {

//                 Toast.makeText(context, String.valueOf(strings.size()), Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRetailerSurvey.this);

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
//        if (radGroupUnit.getCheckedRadioButtonId() == -1) {
//            Utility.showAlertDialog("", "Please Select Unit", context);
//
//            return false;
//        }

        if (spState.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select State", context);
            return false;
        }
        if (spDist.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select District", context);
            return false;
        }
        if (spTerritory.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Territory", context);
            return false;
        }
        if (spTehsil.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Tehsil", context);
            return false;
        }

        if (etRetailerName.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Retailer Firm Name", context);
            return false;
        }


        if (etContactPersonName.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Contact Person Name", context);
            return false;
        }
        if (etRetailerNumber.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
            return false;
        }


        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
            Utility.showAlertDialog("Info", "Please Tag The Field", context);
            return false;
        }
        if (etWhatsappNumber.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Whatsapp Number", context);
            return false;
        }
        if (spdistr.getSelectedItem().toString().equals("SELECT DISTRIBUTOR")) {
            Utility.showAlertDialog("Info", "Please Select  Distributer", context);
            return false;
        }
        if (spvegcrops.getSelectedItem().toString().equals("SELECT CROP")) {
            Utility.showAlertDialog("Info", "Please Select Crops", context);
            return false;
        }

        if (spvegcrops.getSelectedIndices().size() > 5) {
            Utility.showAlertDialog("Info", "Please select only five  Crops", context);
            return false;
        }
        if (switchYNRet.isChecked() && etOtherCompany.getText().toString().length() == 0) {

            Utility.showAlertDialog("Info", "Please Enter Company", context);
            return false;

        }


        if (etAvgVolume.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Volume", context);
            return false;
        }
        if (etVolumeMahyco.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter volume Mahyco", context);
            return false;
        }


        int vol = 0;
        int mahvol = 0;
        String volumeMahyco = etVolumeMahyco.getText().toString();
        if (etVolumeMahyco.getText().length() != 0) {
            String totalVolume = etAvgVolume.getText().toString();
            vol = Integer.valueOf(totalVolume);
            mahvol = Integer.valueOf(volumeMahyco);
        }
        if (mahvol > vol) {
            Utility.showAlertDialog("Info", "Please  Enter volume equal to or less then total avg volume", context);
            return false;
        }


        if (spMahyco.getSelectedItem().toString().toLowerCase().equals("select")) {
            Utility.showAlertDialog("Info", "Please Select Mahyco Products", context);
            return false;
        }


        if (spMahyco.getSelectedIndices().size() > 5) {
            Utility.showAlertDialog("Info", "Please select only five Mahyco Products", context);
            return false;
        }

        if (spSungro.getSelectedItem().toString().toLowerCase().equals("select")) {
            Utility.showAlertDialog("Info", "Please Select Sungro Products", context);
            return false;
        }


        if (spSungro.getSelectedIndices().size() > 5) {
            Utility.showAlertDialog("Info", "Please select only five Sungro Products", context);
            return false;
        }

//        if (spRetailerOtherCompany.getSelectedItem().toString().toLowerCase().equals("select")) {
//            Utility.showAlertDialog("Info", "Please Select  Company", context);
//            return false;
//        }


        if (spBusiness.getSelectedItem().toString().toLowerCase().equals("select")) {
            Utility.showAlertDialog("Info", "Please Select  Business", context);
            return false;
        }
        String mno = etRetailerNumber.getText().toString();

        if (!isAlreadydone(mno)) {
            Utility.showAlertDialog("Info", "This mobile no already exists", context);
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


    public void bindBusiness() {

        try {
            String[] array2 = {"SELECT", "PESTICIDES", "FERTILIZERS", "OTHERS", "NO BUSINESS"};
            spBusiness.setItems(array2);
            spBusiness.hasNoneOption(true);
            spBusiness.setSelection(new int[]{0});


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void bindRetailerOtherCompany() {

        try {
            String[] array2 = {"SELECT", "Rasi", "Nijwidu"};
            spRetailerOtherCompany.setItems(array2);
            spRetailerOtherCompany.hasNoneOption(true);
            spRetailerOtherCompany.setSelection(new int[]{0});


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void bindMahycoSold() {

        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> productlist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct ProductName  FROM CropMaster ";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("ProductName").toString();
                }
                array[jArray.length() + 1] = "Mahyco Not Sold";
                if (array.length > 0) {
                    spMahyco.setItems(array);
                    spMahyco.hasNoneOption(true);
                    spMahyco.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, productlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            dialog.dismiss();
        }


        dialog.dismiss();

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

    public void bindSungroSold() {

        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> productlist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct ProductName  FROM CropMaster ";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("ProductName").toString();
                }
                array[jArray.length() + 1] = "Sungro Not Sold";
                if (array.length > 0) {
                    spSungro.setItems(array);
                    spSungro.hasNoneOption(true);
                    spSungro.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, productlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            dialog.dismiss();
        }


        dialog.dismiss();


    }


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
                    spvegcrops.setItems(array);
                    spvegcrops.hasNoneOption(true);
                    spvegcrops.setSelection(new int[]{0});
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
            dialog.dismiss();
        }


        dialog.dismiss();

    }

    //bind state to spinner
    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM StateTerritoryMaster order by state asc  ";
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

    public void bindTerritory(String state) {
        try {
            spTerritory.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct territory  FROM StateTerritoryMaster" +
                        " where state_code='" + state + "' order by territory asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
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

    //bind Tehsil to spinner

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

    //bind distributer data to spinner distributer
    public void binddistributor(String dist) {


        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
                    " order by  RetailerName ";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT DISTRIBUTOR";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("RetailerName").toString();
                }
                array[jArray.length() + 1] = "OTHER";
                if (array.length > 0) {
                    spdistr.setItems(array);
                    spdistr.hasNoneOption(true);
                    spdistr.setSelection(new int[]{0});
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
            dialog.dismiss();
        }


        dialog.dismiss();

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

    //if mob no already exist
    private boolean isAlreadydone(String mno) {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExistsRetailerSurvey(mno);

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


    }

    //save data to db//
    public void saveToDb() {

        otherCompany = etOtherCompany.getText().toString();
        String retailerNumber = etRetailerNumber.getText().toString();
        String whatsappNumber = "";
        String retailerAddress = "";

        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsappNumber = "";

        } else {


            whatsappNumber = etWhatsappNumber.getText().toString();
        }
        if (tvAddress.getText().toString().isEmpty() || tvAddress.getText().toString().equals("")) {

            retailerAddress = "";


        } else {


            retailerAddress = tvAddress.getText().toString();
            //    String addressREceived = retailerAddress != null ? retailerAddress : "";

        }

        String retailerCordinates = "";
        if (!cordinates.isEmpty()) {

            retailerCordinates = cordinates;
        } else {
            Utility.showAlertDialog("", "Please Wait for location", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }
        String retailerFirmName = etRetailerName.getText().toString();
        String contactPersonName = etContactPersonName.getText().toString();
        String village = spTehsil.getSelectedItem().toString();
        String state = spState.getSelectedItem().toString();
        String district = spDist.getSelectedItem().toString();
        String territory = spTerritory.getSelectedItem().toString();
        String asscoidistributor = spdistr.getSelectedItemsAsString();
        String majorVegCrops = spvegcrops.getSelectedItemsAsString();
        String mahycoProductSold = spMahyco.getSelectedItemsAsString().toString();
        String sungroProductSold = spSungro.getSelectedItemsAsString().toString();


        String retailerOtherCompany = otherCompany != null ? otherCompany : "";

        String businessOtherThenSeed = spBusiness.getSelectedItemsAsString().toString();
        String estimatedAvgVolumeSeedSold = etAvgVolume.getText().toString();
        String estimatedVolumeMahycoSungro = etVolumeMahyco.getText().toString();

        String isSynced = "0";

        Date entrydate = new Date();
        long epoch = entrydate.getTime();
        System.out.println(epoch);
        String timeStamp = String.valueOf(epoch);

        boolean fl = mDatabase.insertToRetailerSurvey(entrydate.toString(), timeStamp, userCode, state,
                district, territory, village, unitType, retailerFirmName, contactPersonName, retailerNumber,
                retailerAddress, retailerCordinates,
                whatsappNumber
                , asscoidistributor, majorVegCrops, estimatedAvgVolumeSeedSold, estimatedVolumeMahycoSungro, mahycoProductSold,
                sungroProductSold, retailerOtherCompany, businessOtherThenSeed, isSynced);

        if (fl) {


            uploadRetailerSurveyData("RetailerSurvery");
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }


    //Stop location fuseApi
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

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                    tvAddress.setText(address);
                    tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                } else {

                    tvAddress.setText(address);
                    tvCordinates.setText(cordinatesmsg + "\n" + cordinates);

                }
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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityRetailerSurvey.this);

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

                androidx.appcompat.app.AlertDialog alert = builder.create();
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


    public int getLocationMode(Context context) {
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
            tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
            Log.d(TAG, "onlocation" + cordinates);


        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }

    //Upload retailer method gets data from db and call api
    public void uploadRetailerSurveyData(String functionName) {
        if (config.NetworkConnection()) {
//            dialog = new ProgressDialog(ActivityRetailerSurvey.this);
//            dialog.setTitle("Data Uploading ...");
//            dialog.setMessage("Please wait.");
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.show();
            String str = null;
            String searchQuery = "select  *  from RetailerSurveyData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table1", jsonArray);
                        Log.d("rhtt", "uploadRetailerSurveyData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadDataServer(functionName, jsonObject).execute(SERVER).get();

                    cursor.close();
                    if (!str.equals("")) {

                        if (str.contains("True")) {


                            etRetailerName.setText("");
                            etContactPersonName.setText("");
                            etRetailerNumber.setText("");
                            etWhatsappNumber.setText("");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String mno = jsonArray.getJSONObject(i).getString("retailerNumber");
                                mDatabase.updateRetailerSurveyData(mno, "1");
                                Log.d("mno", "onResultReceived: " + mno);
                            }
                            Utility.showAlertDialog("Success", "Records Uploaded successfully", context);
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            dialog.dismiss();


                        } else {

                            Utility.showAlertDialog("Error", str, context);
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


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
                dialog.dismiss();
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
            etRetailerName.setText("");
            etContactPersonName.setText("");
            etRetailerNumber.setText("");
            etWhatsappNumber.setText("");


        }
        //dialog.dismiss();
    }

    //server upload Api
    public class UploadDataServer extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;


        public UploadDataServer(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;


        }

        protected void onPreExecute() {

//            dialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("RetailerSurveyData", obj.toString()));

            Log.d("RequestuploadRetailerSurveyData", postParameters + "");


            String Urlpath = SERVER + "?userCode=" + userCode + "";
            Log.d("uploadRetailerSurveyData", Urlpath);

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

                //dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
//                dialog.dismiss();

            }

//            dialog.dismiss();

            return builder.toString();
        }

        @SuppressLint("LongLogTag")
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    Log.d("ResponseRetailerSurveyData", resultout);

//

                } else {
                    Log.d("rhtt", "uploadRetailerSurveyData: " + result);
                    //   msclass.showMessage(result);
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    // Utility.showAlertDialog("Info", result, context);


                }

//                dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
//                dialog.dismiss();
            }

        }
    }


}
