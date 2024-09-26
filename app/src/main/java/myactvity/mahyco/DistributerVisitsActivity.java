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
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.CustomSearchableSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class DistributerVisitsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {
    Context context;

    private static final String TAG = "DistributerActivity";
    SearchableSpinner spState, spDist, spTaluka, spMarketName;
    CustomSearchableSpinner spDistributerDetails;
    MultiSelectionSpinner spMComments;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    public Messageclass msclass;
    EditText etFarmerName, etVillage, etCommentDesc, etTaluka;
    String userCode, state, taluka, dist, village, distributerDetails;
    Button btnSubmit;
    ImageView imgBtnGps;
    TextView lblheader, tvCordinates, tvAddress;
    ProgressDialog dialog;
    private long mLastClickTime = 0;
    String cordinates="";
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
    double lati;
    double longi;
   // String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    String SERVER = "https://maapackhousenxg.mahyco.com/api/generalactivity/distributorvisit";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    List<GeneralMaster> distributerList;
    Prefs mPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_distributer_visits);
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
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spDistributerDetails = (CustomSearchableSpinner) findViewById(R.id.spDistributerDetails);
        spMarketName = (SearchableSpinner) findViewById(R.id.spMarketName);
        spMComments = (MultiSelectionSpinner) findViewById(R.id.spMComments);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        tiOtherVillage = (TextInputLayout) findViewById(R.id.tiOtherVillage);
        etCommentDesc = (EditText) findViewById(R.id.etCommentDesc);
        etVillage = (EditText) findViewById(R.id.etVillage);
        etTaluka = (EditText) findViewById(R.id.etTaluka);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        lblheader = (TextView) findViewById(R.id.lblheader);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);


        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);


        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        userCode = pref.getString("UserID", null);

        bindState();

        bindComments();

        bindDistributer();

        onSubmitBtnClicked();

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


        lblheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DistributerVisitsActivity.this, AndroidDatabaseManager.class);
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
                    taluka = gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bindMarketPlace(taluka);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spDistributerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                Intent intent;
                try {
                    distributerDetails = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if (distributerList != null) {
                        if (position ==  1) {
                            editor.putString("RetailerCallActivity","DistributerVisitsActivity");
                            editor.commit();
                            intent = new Intent(DistributerVisitsActivity.this, RetailerandDistributorTag.class);
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

        spMComments.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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


    /**
     * <p>Method is used to manage geo tag</p>
     */
    private void manageGeoTag() {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(DistributerVisitsActivity.this);

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

    /**
     * <P>Validation for all fields</P>
     * @return
     */
    public boolean validation() {

        if (spState.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select State", context);
            return false;
        }
        if (spDist.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select District", context);
            return false;
        }
        if (spTaluka.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Taluka", context);
            return false;
        }
//        if (spMarketName.getSelectedItemPosition() == 0) {
//            Utility.showAlertDialog("Info", "Please Select Market Name", context);
//            return false;
//        }

        if (spDistributerDetails.getSelectedItemPosition() == 0
                || spDistributerDetails.getSelectedItem().toString().equalsIgnoreCase("New Distributor (Tag the Distributor)"))
        {

            Utility.showAlertDialog("Info", "Please Select Distributor details", context);
            return false;
        }

        if (spMComments.getSelectedStrings() == null || spMComments.getSelectedStrings().size() == 0) {
            Utility.showAlertDialog("Info", "Please Select Comments ", context);
            return false;
        }

        if (etCommentDesc.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Comment Description", context);
            return false;
        }

        if (!checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
            Utility.showAlertDialog("Info", "Please Tag The Field", context);
            return false;
        }

        String retdetails = spDistributerDetails.getSelectedItem().toString();

        if (!isAlreadydone(retdetails)) {
            Utility.showAlertDialog("Info", "This Distributor  details already exists", context);
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


    @Override
    protected void onResume() {
        super.onResume();
        try {

            startFusedLocationService();

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }
    }

    //bind state to spinner
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

    //bind MarketName to spinner

    public void bindMarketPlace(String taluka) {

        try {
            spMarketName.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> mobilelist = new ArrayList<GeneralMaster>();

                String searchQuery = "SELECT distinct marketPlace FROM RetailerDetailsMaster where taluka='" + taluka + "' order by marketPlace";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


                mobilelist.add(new GeneralMaster("SELECT MARKETPLACE",
                        "SELECT MARKETPLACE"));


                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    mobilelist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();


                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, mobilelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMarketName.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    //bind Territory to spinner
    public void bindComments() {


        String[] array;
        try {
            String searchQuery = "SELECT  *  FROM commentlist  ";
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT COMMENTS";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("comment").toUpperCase();
            }

            if (array.length > 0) {
                spMComments.setItems(array);
                spMComments.hasNoneOption(true);
                spMComments.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }






       /* String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";

            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                array = new String[jArray.length() + 1];
                array[0] = "SELECT COMMENTS";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("ProductName").toString();
                }

                if (array.length > 0) {
                    spMComments.setItems(array);
                    spMComments.hasNoneOption(true);
                    spMComments.setSelection(new int[]{0});
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
        } */
    }


    public void bindDistributer() {

        try {
            spDistributerDetails.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                distributerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                        "where activity='Distributor' " +
                        " order by  RetailerName ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                distributerList.add(new GeneralMaster("SELECT DISTRIBUTOR",
                        "SELECT DISTRIBUTOR"));
                distributerList.add(1, new GeneralMaster("New Distributor (Tag the Distributor)",
                        "New Distributor (Tag the Distributor)"));
                if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            distributerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(0).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }


                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, distributerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistributerDetails.setAdapter(adapter);
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
    private boolean isAlreadydone(String retailerDetails) {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExistsDistributerVisitsDataCount(retailerDetails);

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


    }

    //save data to db//


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
                if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                    tvAddress.setText(address +"\n"+ cordinates);
                    tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                } else {

                    tvAddress.setText(address +"\n"+ cordinates);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DistributerVisitsActivity.this);

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
            tvAddress.setText(address + "\n" + cordinates);
            tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
            Log.d(TAG, "onlocation" + cordinates);
            manageGeoTag();

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }


    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {

        String state = "";
        String district = "";
        String taluka = "";
        String marketPlace = "";
        String distributerDetails = "";
        String comments = "";
        String commentDesc = "";

        state = spState.getSelectedItem().toString();
        district = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        marketPlace = spMarketName.getSelectedItem().toString();
        distributerDetails = spDistributerDetails.getSelectedItem().toString();
        comments = spMComments.getSelectedItem().toString();
        commentDesc = etCommentDesc.getText().toString();

        String taggedAddress = "";

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

        Log.d("LocationDatasaveToDb", cordinates);

        String isSynced = "0";
        String activityImgStatus = "0";


        Date entrydate = new Date();


        boolean fl = mDatabase.insertDistributerVisitisData(userCode, state, district, taluka, marketPlace, distributerDetails,
                comments, commentDesc, taggedCordinates +" "+ taggedAddress, taggedCordinates, isSynced);

        if (fl)
        {
            if (CommonUtil.addGTVActivity(context, "38", "Distributor Visit", cordinates, distributerDetails+" "+comments,"Market","0",0.0)) {
                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }

                uploadDistributerVisitsData("DistributerVisitsData");
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    ///////

    //////

    public void uploadDistributerVisitsData(String functionName) {
       /* if (config.NetworkConnection())
        {

            String str = null;
            String searchQuery = "select  *  from DistributerVisitsData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table", jsonArray);
                        Log.d("rhtt", "uploadDistributerVisitsData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadDistributerVisitsData(functionName, jsonObject).execute(SERVER).get();

                    cursor.close();

                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            } else
            {
                //  dialog.dismiss();
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else */
       {
           cordinates="";
           AlertDialog.Builder builder = new AlertDialog.Builder(DistributerVisitsActivity.this);

           builder.setTitle("MyActivity");
           builder.setMessage("Data Saved Successfully");
           builder.setCancelable(false);

           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialog, int which) {
                   Config.refreshActivity(DistributerVisitsActivity.this);
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
        // dialog.dismiss();
    }


    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadDistributerVisitsData extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        JSONObject obj;
        String Funname;


        public UploadDistributerVisitsData(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;
        }


        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return HttpUtils.POSTJSON(SERVER, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
        }

        protected void onPostExecute(String result) {

            try {
                String resultout = result.trim();
                Log.d("Response", resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DistributerVisitsActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(DistributerVisitsActivity.this);
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
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DistributerVisitsActivity.this);
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DistributerVisitsActivity.this);
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

}
