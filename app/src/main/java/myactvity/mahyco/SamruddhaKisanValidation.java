package myactvity.mahyco;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import myactvity.mahyco.R;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SamruddhaKisanModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import myactvity.mahyco.myActivityRecording.preSeasonActivity.AddFarmerListCropSeminarAdapter;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.CropSeminarActivity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SamruddhaKisanValidation extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "SamruddhaKisanValidation";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    SearchableSpinner spState, spDist, spTaluka, spVillage, spFocusedVillages, spMDO, spTBM, spStatus, spFarmerName;
    String userCode;
    Button btnFilter, btnDownload;
    private String stateDesc ="", state = "", dist = "", taluka = "", village = "", focusedVillage = "", status = "", tbm, mdo, farmerName = "";
    LinearLayout llOtherVillages;
    String[] statusyArray;
    LinearLayout llFocussedVillages;
    public SqliteDatabase mDatabase;
    Config config;
    public Messageclass msclass;
    TextView lblheader;
    private Context context;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity, radOtherActivity;
    private Handler handler = new Handler();
    Prefs mPref;
    String BaseUrl="https://packhouse.mahyco.com/api/generalactivity/";
    String GET_MDO_BY_TBM_URL = BaseUrl+"getMdoByTbm";
    String GET_REGION_TBM_URL = BaseUrl+"getRegionTbm";
    String BASE="packhouse.mahyco.com";
    String SERVER = BaseUrl+"getSamruddhaKisanValidationData";
   // String SERVER = "http://10.80.50.153/MAAPackHouseTest/api/generalactivity/getSamruddhaKisanValidationData";

    
    //New Samrudhha Kisan Changes

    Location mLocation;
    TextView latLng;
    GoogleApiClient mGoogleApiClient;
   // private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    // Creating Button to load All TBM list Form Server samrudhkisan validation.
    Button btn_refresh_tbm_list;
    int flag_load=0;
    int cnt=0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samruddha_kisan_validation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
        mGoogleApiClient = new GoogleApiClient.Builder(SamruddhaKisanValidation.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(SamruddhaKisanValidation.this)
                .addOnConnectionFailedListener(SamruddhaKisanValidation.this)
                .build();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        mPref = Prefs.with(this);
        context = this;
        mDatabase = new SqliteDatabase(this);
        config = new Config(this); //Here the context is passing

        lblheader = (TextView) findViewById(R.id.lblheader);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spMDO = (SearchableSpinner) findViewById(R.id.spMDO);
        spTBM = (SearchableSpinner) findViewById(R.id.spTBM);
        spStatus = (SearchableSpinner) findViewById(R.id.spStatus);
        spFarmerName = (SearchableSpinner) findViewById(R.id.spFarmerName);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        btn_refresh_tbm_list = (Button) findViewById(R.id.btn_refresh_tbm_list);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        msclass = new Messageclass(this);

       if(flag_load==0) {
           bindTBM();
           flag_load=1;
       }
       // mdo="";
        //tbm="";
        bindStatus();
        bindState();
        bindFocussedVillage();

        btn_refresh_tbm_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (config.NetworkConnection()) {

                    try {
                        new GetRegionTBMList().execute(GET_REGION_TBM_URL,userCode,"0");// passing last 0 for we wish to load data in tbm list and 1 for load data in MDO list.


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Utility.showAlertDialog("Error", "Poor Internet: Please try after sometime.", context);
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }

            }
        });

        // bindFarmerDetails();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    relPRogress.setVisibility(View.VISIBLE);
                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    downloadData();

            }
        });


        if (checkPlayServices()) {
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }

        spFocusedVillages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    focusedVillage = gm.Desc().trim();

//                    if ((!focusedVillage.equals("") && !focusedVillage.equals("SELECT FOCUSED VILLAGE") )) {

                    bindFarmerDetails();

                    // }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    stateDesc = gm.Desc();
                    //  if ((!state.equals("") && !state.equals("SELECT STATE") )) {

                    bindFarmerDetails();

                    // }
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
                    // if ((!dist.equals("") && !dist.equals("SELECT DISTRICT") )) {

                    bindFarmerDetails();

                    //  }

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
                    //   if ((!taluka.equals("") && !taluka.equals("SELECT TALUKA") )) {

                    bindFarmerDetails();

                    //  }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    status = (String) parent.getSelectedItem(); // URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    //   if ((!status.equals("") && !status.equals("CATEGORY") )) {

                    bindFarmerDetails();

                    // }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spTBM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    tbm = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
//                    if ((!tbm.equals("")&& !tbm.equals("SELECT TBM"))) {
//                        bindFarmerDetails();
//                       }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(cnt>0)
                bindMDO(tbm);
                else
                    cnt++;
                bindFarmerDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spMDO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    mdo = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if(mdo.equals("ALL"))
                    {
                        mdo="";
                    }

                    if ((!mdo.equals("") && !mdo.equals("SELECT USERNAME") )) {
                        bindFarmerDetails();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    village = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    //if ((!village.equals("") && !village.equals("SELECT VILLAGE") )) {

                    bindFarmerDetails();

                    // }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spFarmerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    farmerName = gm.toString();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
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
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                            stateDesc="";
                            dist="";
                            taluka="";
                            village="";

                        } else {
                            bindState();
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                            focusedVillage="";
                        }

                        radOtherActivity.setChecked(false);
                        break;
                    case R.id.radOtherActivity:
                        if (radOtherActivity.isChecked()) {
                            bindState();
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                            focusedVillage="";
                        } else {
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                            stateDesc="";
                            dist="";
                            taluka="";
                            village="";
                        }
                        break;
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, mdo+" === "+tbm, Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(SamruddhaKisanValidation.this, SamruddhaKisanValidationRecords.class);

                    if (spTBM.getSelectedItem().toString().equalsIgnoreCase("SELECT TBM")) {
                        intent.putExtra("tbm", "");
                    } else {
                        intent.putExtra("tbm", tbm);
                    }
                    if (spMDO.getSelectedItem().toString().equalsIgnoreCase("SELECT USERNAME")) {
                        intent.putExtra("mdo", "");
                    } else {
                        intent.putExtra("mdo", mdo);
                    }
                if(mdo==null) {
                    mdo="";
                }
                if(tbm==null) {
                    tbm="";
                }

                    if (spStatus.getSelectedItem().toString().equalsIgnoreCase("CATEGORY")) {
                        intent.putExtra("status", "");
                    } else {
                        intent.putExtra("status", status);
                    }
                    if (spFocusedVillages.getSelectedItem().toString().equalsIgnoreCase("SELECT FOCUSED VILLAGE")) {
                        intent.putExtra("focussedVillage", "");
                    } else {
                        intent.putExtra("focussedVillage", focusedVillage);
                    }

                    if (radOtherActivity.isChecked()) {
                        if (spState.getSelectedItem().toString().equalsIgnoreCase("SELECT STATE")) {
                            intent.putExtra("state", "");
                        } else {
                            intent.putExtra("state", stateDesc);
                        }
                        if (spDist.getSelectedItem().toString().equalsIgnoreCase("SELECT DISTRICT")) {
                            intent.putExtra("district", "");
                        } else {
                            intent.putExtra("district", dist);
                        }
                        if (spTaluka.getSelectedItem().toString().equalsIgnoreCase("SELECT TALUKA")) {
                            intent.putExtra("taluka", "");
                        } else {
                            intent.putExtra("taluka", taluka);
                        }

                        if (spVillage.getSelectedItem().toString().equalsIgnoreCase("SELECT VILLAGE")) {
                            intent.putExtra("village", "");
                        } else {
                            intent.putExtra("village", village);
                        }
                    } else {
                        intent.putExtra("district", "");
                        intent.putExtra("taluka", "");
                        intent.putExtra("village", "");
                    }

                    if (spFarmerName.getSelectedItem().toString().equalsIgnoreCase("SELECT FARMER'S MOBILE NUMBER/ NAME")) {
                        intent.putExtra("farmerDetail", "");
                    } else {
                        intent.putExtra("farmerDetail", farmerName);
                    }

                    startActivity(intent);

            }
        });
    }


    /**
     * <P>//Download data for validation according to TBM/MDO</P>
     */
    private void downloadData() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        getDataFromServer();

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

    private void getDataFromServer() {

        if (config.NetworkConnection()) {

            try {

                new GetValidationDataServer(context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utility.showAlertDialog("Error", "Poor Internet: Please try after sometime.", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }


    }


    /**
     *  <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class GetValidationDataServer extends AsyncTask<String, String, String> {

        public GetValidationDataServer(Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            return getValidationData("SamruddhaKisanData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                        saveToDb(jsonObject);
                        Log.i("Json Data ",jsonObject.toString());

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SamruddhaKisanValidation.this);
                        builder.setTitle("MyActivity");
                        //builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setMessage(jsonObject.get("message").toString());

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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SamruddhaKisanValidation.this);
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

    private String getValidationData(String samruddhaKisanData) {
        String str = "";
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject obj = new JSONObject();
            //obj.put("userCode", userCode);
            obj.put("userCode", mdo);   // Get mdo wise  farmer visit  samruddha kisan data

            jsonObject.put("Table", obj);
            Log.d("samruddhaKisanData",SERVER +"+++++"+ jsonObject.toString());
            str = syncSamruddhaKisanValidationData(samruddhaKisanData, SERVER, jsonObject);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }


    private String syncSamruddhaKisanValidationData(String samruddhaKisanData, String server, JSONObject jsonObject) {
        return HttpUtils.POSTJSON(server, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }


    private void saveToDb(JSONObject jsonObject) throws JSONException {

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("table");
            JSONObject obj = new JSONObject();

            //Select table and delete the previous data
            String searchQuery = "SELECT * FROM SamruddhaKisanValidationData";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            if (count > 0) {

                obj = mDatabase.getResultsDetails(searchQuery);
                String searchQuery1 = "DELETE  FROM SamruddhaKisanValidationData";

                Cursor cursor1 = mDatabase.getReadableDatabase().rawQuery(searchQuery1, null);
                int count1 = cursor1.getCount();

                if(count1==0){

                    insertIntoLocalDB(jsonArray);
                }

            }else {

                insertIntoLocalDB(jsonArray);
            }






        /*    if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    String searchQuery = "SELECT * FROM SamruddhaKisanValidationData";

                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                    int count = cursor.getCount();
                    if (count > 0) {

                        obj = mDatabase.getResultsDetails(searchQuery);
                        String searchQuery1 = "DELETE  FROM SamruddhaKisanValidationData WHERE id = '" + obj.getString("id") + "'";

                        Cursor cursor1 = mDatabase.getReadableDatabase().rawQuery(searchQuery1, null);
                        int count1 = cursor1.getCount();

                        //   f2 = mDatabase.updateGetSamruddhaKisanValidationData("0", obj);
                      //  checkForItemInsert(f2);

                    } else {

                        if(jsonArray.getJSONObject(i).getString("action").equals("")){
                            jsonArray.getJSONObject(i).put("action","PENDING");
                        }
                        fl = mDatabase.insertSamruddhaKisanValidationData(jsonArray.getJSONObject(i));
                        checkForItemInsert(fl);
                    }
                }

            } else {
                for (int i = 0; i < jsonArray.length(); i++) {

                    if(jsonArray.getJSONObject(i).getString("action").equals("")){
                        jsonArray.getJSONObject(i).put("action","PENDING");
                    }

                    fl = mDatabase.insertSamruddhaKisanValidationData(jsonArray.getJSONObject(i));
                }
                checkForItemInsert(fl);
            }

         */


            String state = "", district = "", taluka = "", village = "", focussedVillage = "";
            String reasons = "", action = "", mdoCode = "", tbmCode = "", validateBy = userCode;
            String farmerName = "", mobileNumber = "", whatsappNumber = "", totalLand = "", crop = "", product = "";
            String taggedAddress = "";
            String taggedCordinates = "";
            String isSynced = "0";
      /*  focussedVillage = spFocusedVillages.getSelectedItem().toString();

        if (radOtherActivity.isChecked()) {
            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            village = spVillage.getSelectedItem().toString();
        }


        boolean fl = mDatabase.insertSamruddhaKisanValidationData(userCode, focussedVillage, state, district, taluka, village, crop, product, farmerName, mobileNumber,
                whatsappNumber, totalLand, taggedAddress, taggedCordinates, reasons, action, mdoCode, tbmCode, validateBy, isSynced);


        if (fl) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SamruddhaKisanValidation.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Download Successfully");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(SamruddhaKisanValidation.this);
                    dialog.dismiss();
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

       */

        } catch (JSONException e) {
            e.printStackTrace();
            msclass.showMessage("Plese check all input data");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    private void insertIntoLocalDB(JSONArray jsonArray) {

        boolean fl = false;
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                if (jsonArray.getJSONObject(i).getString("Action").equals("")) {
                    jsonArray.getJSONObject(i).put("Action", "PENDING");
                }


                fl = mDatabase.insertSamruddhaKisanValidationData(jsonArray.getJSONObject(i),userCode);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        checkForItemInsert(fl);

    }


    private void checkForItemInsert(Boolean f1) {

        if (f1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SamruddhaKisanValidation.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Download Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                  //  Config.refreshActivity(SamruddhaKisanValidation.this);
                    dialog.dismiss();
                    relPRogress.setVisibility(View.GONE);
                    container.setClickable(true);
                    container.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }


    private void bindFarmerDetails() {


        try {
            spFarmerName.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                StringBuilder sb = new StringBuilder();
                if ((!tbm.equals("")&& !tbm.equals("SELECT TBM"))) {
                    // Initial Query
                    sb.append("SELECT distinct farmerName,mobileNumber  FROM SamruddhaKisanValidationData");

                    sb.append(" WHERE ");

                    // tbm
                    if (tbm != null) {
                        if ((!tbm.equals("") && !tbm.equals("SELECT TBM"))) {
                            //sb.append("tbmDesc = '" + tbm.toUpperCase() + "'");
                            sb.append("upper(tbmCode) = '" + tbm.toUpperCase() + "'");
                            sb.append(" AND ");
                        }
                    }
                    // mdo
                    if (mdo != null) {
                        if ((!mdo.equals("") && !mdo.equals("SELECT USERNAME"))) {
                            //sb.append("mdoDesc = '" + mdo.toUpperCase() + "'");
                            sb.append("upper(mdoCode) = '" + mdo.toUpperCase() + "'");
                            sb.append(" AND ");
                        }
                    }

                    // category
                    if ( status!= null) {
                        if ((!status.equals("") && !status.equals("CATEGORY"))) {
                            if(!status.equals("ALL")) {
                                sb.append("action = '" + status.toUpperCase() + "'");
                                sb.append(" AND ");
                            }
                        }
                    }

                    // focussed village
                    if ( focusedVillage!= null) {
                        if ((!focusedVillage.equals("") && !focusedVillage.equals("SELECT FOCUSED VILLAGE"))) {

                            sb.append("focussedVillage = '" + focusedVillage.toUpperCase() + "'");
                            sb.append(" AND ");
                        }

                    }
                    //  state
                    if ( stateDesc!= null) {
                        if ((!stateDesc.equals("") && !stateDesc.equals("SELECT STATE"))) {

                            sb.append("state = '" + stateDesc.toUpperCase() + "'");
                            sb.append(" AND ");
                        }

                    }
                    //  dist
                    if ( dist!= null) {
                        if ((!dist.equals("") && !dist.equals("SELECT DISTRICT"))) {

                            sb.append("district = '" + dist.toUpperCase() + "'");
                            sb.append(" AND ");
                        }

                    }

                    //  taluka
                    if ( taluka!= null) {
                        if ((!taluka.equals("") && !taluka.equals("SELECT TALUKA"))) {

                            sb.append("taluka = '" + taluka.toUpperCase() + "'");
                            sb.append(" AND ");
                        }

                    }

                    //  village
                    if ( village!= null) {
                        if ((!village.equals("") && !village.equals("SELECT VILLAGE"))) {

                            sb.append("village = '" + village.toUpperCase() + "'");
                            sb.append(" AND ");
                        }

                    }

                    // sb.append("  order by farmerName asc ");

                    sb.delete(sb.length() - 4, sb.length());

                    String searchQuery = sb.toString();

                    // String searchQuery = "SELECT distinct farmerName,mobileNumber  FROM SamruddhaKisanValidationData where tbmDesc = '"+ tbm   + "'   order by farmerName asc  "


                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        list.add(new GeneralMaster(cursor.getString(1),
                                cursor.getString(0) + "-" + cursor.getString(1).toUpperCase()));

                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                list.add(0, new GeneralMaster("SELECT FARMER'S MOBILE NUMBER/ NAME",
                        "SELECT FARMER'S MOBILE NUMBER/ NAME"));

                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFarmerName.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void bindTBM() {

        try {

            // Code is comment to take all the TBM form server by new API and and load to Spinner By CUrrent Register TBM id
 // Comment Date : 22 Feb 2022
 // Commented By : Sumit Suradkar
 // Suggested By : Anand Sir and Junaid Sidiquee
 // API desgn By : Junaid Sidiquee
          /*  spTBM.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
               // String searchQuery = "SELECT distinct tbmCode,tbmDesc  FROM MdoTbmMaster where tbmCode = '"+ userCode +"' order by tbmDesc asc  ";
                String searchQuery = "SELECT distinct TBMCode,TBMName  FROM RbmMaster where (TBMCode = '"+ userCode +"' or code = '"+ userCode +"') order by TBMName asc  ";


                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                tbmlist.add(0, new GeneralMaster("SELECT TBM",
                        "SELECT TBM"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    tbmlist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, tbmlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTBM.setAdapter(adapter);*/

            // New Added Code to Load all the TBM
              try{
            new GetRegionTBMList().execute(GET_REGION_TBM_URL,userCode,"0");// passing last 0 for we wish to load data in tbm list and 1 for load data in MDO list.


            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void bindMDO(String tbm) {

        try {
            spMDO.setAdapter(null);
            String str = null;
            try {

               // Toast.makeText(context, ""+tbm, Toast.LENGTH_SHORT).show();
                new GetRegionTBMList().execute(GET_MDO_BY_TBM_URL,tbm,"1");// Passing 1 to load data in MDO list 0 to Load Data in TBM list

          /*      List<GeneralMaster> mdolist = new ArrayList<GeneralMaster>();
                //String searchQuery = "SELECT distinct mdoCode,mdoDesc,tbmDesc  FROM MdoTbmMaster where  tbmDesc= '" + tbm + "' AND( mdoCode!='" + "NA" + "') order by mdoDesc asc  ";
                String searchQuery = "SELECT distinct mdoCode,mdoDesc,tbmDesc  FROM MdoTbmMaster where  tbmCode= '" + tbm + "' AND( mdoCode!='" + "NA" + "') order by mdoDesc asc  ";

                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                mdolist.add(0,new GeneralMaster("SELECT USERNAME",
                        "SELECT USERNAME"));
                mdolist.add(1,new GeneralMaster("ALL",
                        "ALL"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    mdolist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase() + " , " + cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, mdolist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMDO.setAdapter(adapter);*/
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    private void bindStatus() {


        try {
            spStatus.setAdapter(null);

            String str = null;
            try {
                statusyArray = getResources().getStringArray(R.array.status);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statusyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spStatus.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    private boolean checkPlayServices1() {
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

    public void bindFocussedVillage() {
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
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFocusedVillages.setAdapter(adapter);

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }




    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm.toString())) {
                result.add(perm);
            }
        }

        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast.makeText(context, "Please install Google Play services.", Toast.LENGTH_SHORT).show();
            //latLng.setText("Please install Google Play services.");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // Toast.makeText(SamruddhaKisanValidation.this, "Connected", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLocation!=null)
        {
            mPref.save("currentLocation",mLocation.getLatitude() + "-" + mLocation.getLongitude());
          //  Toast.makeText(context, "Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location!=null) {
          //  Toast.makeText(context, "changed Latitude : " + location.getLatitude() + " , Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            mPref.save("currentLocation",location.getLatitude() + "-" + location.getLongitude());
        }


    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    private boolean hasPermission(String permission) {
     /*   if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }*/
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

      /*  switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (O perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }
*/
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(SamruddhaKisanValidation.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private class GetRegionTBMList extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(SamruddhaKisanValidation.this);
private String type="";

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
            //   uiUpdate.setText("Output : ");
            Dialog.setMessage("Please Wait..");
            Dialog.show();
            //pb.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            StringBuilder sb = new StringBuilder();
            Log.i("pass", "1");
            String http = urls[0].trim();
            String tbmcode=urls[1].trim();
            type=urls[2].trim(); // type =0 load TBM list and 1= load MDO list

            Log.i("Host is::",""+http);
            HttpURLConnection urlConnection = null;
            try {


                Log.i("pass", "2");
                URL url = new URL(http);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                Log.i("pass", "3");
                urlConnection.setRequestProperty("Host", BASE);
                urlConnection.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("UserCode",tbmcode);

                DataOutputStream printout;
                Log.i("pass", "5");
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                Log.i("content:", jsonParam.toString());
                //Toast.makeText(getApplicationContext(),jsonParam.toString(),Toast.LENGTH_LONG).show();
                out.close();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    Log.i("content:", sb.toString());
                    //    Toast.makeText(getApplicationContext(), "" + sb.toString(), Toast.LENGTH_SHORT).show();
                    Content = sb.toString();
                } else {
                    Log.i("ResponseMsg:", urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                //  uiUpdate.setText("Output : "+Error);

            } else {
                //pb.setVisibility(View.GONE);
                //   uiUpdate.setText("Output : "+Content);
                // loadFromServer(Content.toString().trim());

                try {
                    Log.i("Details", "" + Content);
                    JSONObject jsonObject=new JSONObject(Content.trim());
                    JSONArray jsonArray=jsonObject.getJSONArray("Table");
                 //   Toast.makeText(context, "Total Size :"+jsonArray.length(), Toast.LENGTH_SHORT).show();

                    if(jsonArray.length()>0)
                    {

                        String str = null;
                        try {

                          if(type.equals("0")) {
                              List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
                              tbmlist.add(0, new GeneralMaster("SELECT TBM",
                                      "SELECT TBM"));
                              JSONObject json_TBM;
                              for(int i=0;i<jsonArray.length();i++)
                              {
                                  json_TBM=jsonArray.getJSONObject(i);
                                  String code=json_TBM.getString("TBMCode");
                                  String desc=json_TBM.getString("TBMName");

                                  tbmlist.add(new GeneralMaster(code,
                                          desc.toUpperCase()));
                              }
                              spTBM.setAdapter(null);
                              ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                                      (context, android.R.layout.simple_spinner_dropdown_item, tbmlist);
                              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spTBM.setAdapter(adapter);

                              spMDO.setAdapter(null);
                              List<GeneralMaster> mdoList = new ArrayList<GeneralMaster>();
                              mdoList.add(0, new GeneralMaster("SELECT USERNAME",
                                      "SELECT USERNAME"));
                              ArrayAdapter<GeneralMaster> adapter1 = new ArrayAdapter<GeneralMaster>
                                      (context, android.R.layout.simple_spinner_dropdown_item, mdoList);
                              adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spMDO.setAdapter(adapter1);
                          }else if(type.equals("1"))
                          {
                              List<GeneralMaster> mdoList = new ArrayList<GeneralMaster>();
                              mdoList.add(0, new GeneralMaster("SELECT USERNAME",
                                      "SELECT USERNAME"));
                              JSONObject json_TBM;
                              for(int i=0;i<jsonArray.length();i++)
                              {
                                  json_TBM=jsonArray.getJSONObject(i);
                                  String code=json_TBM.getString("MDOCode");
                                  String desc=json_TBM.getString("MDO_name");

                                  mdoList.add(new GeneralMaster(code,
                                          desc.toUpperCase()));
                              }
                              spMDO.setAdapter(null);
                              ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                                      (context, android.R.layout.simple_spinner_dropdown_item, mdoList);
                              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spMDO.setAdapter(adapter);

                          }
                        }catch(Exception e)
                        {

                        }
                    }else
                    {
                        if(type.equals("0"))
                          Toast.makeText(context, "TBM not found.", Toast.LENGTH_SHORT).show();
                        else if(type.equals("1")) {
                            spMDO.setAdapter(null);
                            List<GeneralMaster> mdoList = new ArrayList<GeneralMaster>();
                            mdoList.add(0, new GeneralMaster("SELECT USERNAME",
                                    "SELECT USERNAME"));
                            ArrayAdapter<GeneralMaster> adapter1 = new ArrayAdapter<GeneralMaster>
                                    (context, android.R.layout.simple_spinner_dropdown_item, mdoList);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spMDO.setAdapter(adapter1);
                            Toast.makeText(context, "MDO not found.", Toast.LENGTH_SHORT).show();
                        }
                    }


                }catch(Exception e)
                {
                    Log.i("Details", "" + e.getMessage());


                }
            }
        }

    }


}
