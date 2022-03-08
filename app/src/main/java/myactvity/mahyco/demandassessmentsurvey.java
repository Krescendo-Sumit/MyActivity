package myactvity.mahyco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.crash.FirebaseCrash;

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

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;


import android.os.AsyncTask;
import android.util.Base64;

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

import java.io.BufferedReader;
import java.io.InputStream;
        import java.io.InputStreamReader;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class demandassessmentsurvey extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback
{

    // implements GoogleApiClient.ConnectionCallbacks,
    // GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
    public Button btnsave,addstock;
    public RadioGroup radGrp;

    public EditText txtFarmerCount, txtmobile,txtFarmername;
    public TextView text,text2,lblStockDetail,lblgeoststus; //txtvillage
    public SearchableSpinner spDist,spState, spTaluka, spVillage, spCropType, spProductName, spMyactvity,spComment;
    private String AadharNo, name;
    private String state;
    private String dist;
    public String imagestring;
    private String taluka;
    private String village, Iagree,croptype,myactvity, comments,nooffarmer;
    private String InTime ;
    private Bitmap bm=null;
    private byte[] b=null;
    private byte[] b2=null;
    private String Imagename="";
    private String Imagename2="";
    private static final String TAG = MyFieldActvity.class.getSimpleName();
    public AutoCompleteTextView email;
    ProgressDialog dialog;
    public String SERVER = "https://farm.mahyco.com/TestHandler.ashx";
    public String langcode = "", mobileno, username;
    public RadioButton rndRC, rndVC, rndFC;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    SharedPreferences locsp;
    public Messageclass msclass;
    Locale locale;
    public CommonExecution cx;
    private String userChoosenTask;
    public File destination;
    String usercode;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    //public  cx.getCrop gcrop;
    Config config;
    Bitmap image;
    private SqliteDatabase mDatabase;

    public static int check = 0, check2 = 0, check3 = 0, check4 = 0, rowcount = 0;
    private Location mLastLocation;


    private int imageselect;
    public String Imagepath1="",Imagepath2="";
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private static int UPDATE_INTERVAL = 2000;//10000; // 10 sec
    private static int FATEST_INTERVAL = 2000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private  static String cordinate;
    private static String address ;
    private String pictureImagePath = "";
    private Uri file1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    File photoFile = null;
    File photoFile2 = null;
    final Context context = this;
    LinearLayout listock,liimagelayout;
    public CardView card_month7,card_month8;
    CheckBox chkyear1,chkyear2;
    //Rohit given
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandassessmentsurvey);
        listock = (LinearLayout) findViewById(R.id.listock);
        liimagelayout = (LinearLayout) findViewById(R.id.liimagelayout);
        btnsave = (Button) findViewById(R.id.btnsave);
        txtmobile = (EditText) findViewById(R.id.txtmobile);
       // txtvillage= (EditText) findViewById(R.id.txtvillage);
        txtFarmername=(EditText) findViewById(R.id.txtFarmername);
        txtFarmerCount = (EditText) findViewById(R.id.txtFarmerCount);
        lblgeoststus= (TextView) findViewById(R.id.lblgeoststus);
        lblStockDetail= (TextView) findViewById(R.id.lblStockDetail);
        addstock= (Button) findViewById(R.id.btnaddstock);
        config = new Config(this); //Here the context is passing
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (SearchableSpinner) findViewById(R.id.spProductName);
        //  spState = (Spinner) findViewById(R.id.spState);
        spState= (SearchableSpinner)findViewById(R.id.spState);

        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        rndRC = (RadioButton) findViewById(R.id.rndRC);
        // rndFC = (RadioButton) findViewById(R.id.rndFC);
        rndVC = (RadioButton) findViewById(R.id.rndVC);
        mDatabase = SqliteDatabase.getInstance(this);
        radGrp = (RadioGroup) findViewById(R.id.radGrp);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        chkyear1=(CheckBox) findViewById(R.id.chkyear1);
        chkyear2=(CheckBox) findViewById(R.id.chkyear2);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getSupportActionBar().hide(); //<< this
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        locedit = locdata.edit();
        usercode= pref.getString("UserID", null);
       bindcroptype(spCropType,"C");
       // BindDist("");
        BindState();

        ImageView backbtn2 = (ImageView) findViewById(R.id.backbtn);
        backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                        if (validation() == true) {
                            // LoginRequest();
                            /* image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap(); */
                            // dialog.setMessage("Loading. Please wait...");
                            //dialog.show();
                            GeneralMaster st = (GeneralMaster) spState.getSelectedItem();
                            GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                            GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                            GeneralMaster vt = (GeneralMaster) spVillage.getSelectedItem();
                           // GeneralMaster myact = (GeneralMaster) spMyactvity.getSelectedItem();

                            try {

                                dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                                taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                                village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");
                                state = st.Code().trim();//URLEncoder.encode(myact.Code().trim(), "UTF-8");
                                Savedata(myactvity, state, dist, taluka, village);
                            } //catch (UnsupportedEncodingException e) {
                            //  e.printStackTrace();
                            //}
                            catch (Exception ex) {
                                ex.printStackTrace();
                                msclass.showMessage(ex.getMessage());
                            }
                            // dialog.setMessage("Loading....");
                            // dialog.show();

                            //new UpdateGetLangDt(2,image,langcode,name,AadharNo,mobileno,state,dist,taluka,village,"Y").execute(SERVER);
                        }

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());

                }
            }
        });



        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }



                // check = check + 1;
                // if (check > 1)
                {

                   // BindProductName(spProductName,croptype,null,1);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(demandassessmentsurvey.this, "gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state= URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
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
                try {
                    taluka =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    BindVillage(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        chkyear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtmobile.getText().length()==0)
                { msclass.showMessage("Please enter  mobile number");
                    chkyear1.setChecked(false);
                    return;
                }
                if (txtmobile.getText().length() < 10) {
                    msclass.showMessage("Please enter 10 digit mobile number");
                    return ;
                }
                if (chkyear1.isChecked()==true) {
                    addcultivation("");
                }
            }
        });
        chkyear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtmobile.getText().length()==0)
                { msclass.showMessage("Please enter  mobile number");
                    chkyear2.setChecked(false);
                    return;
                }
                if (txtmobile.getText().length() < 10) {
                    msclass.showMessage("Please enter 10 digit mobile number");
                    return ;
                }
                if (chkyear2.isChecked()==true) {
                    addcultivationtobe("");
                }

            }
        });
           radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case -1:
                        // Log.v(TAG, "Choices cleared!");
                        break;
                    case R.id.rndYes:
                        awaremahycoproduct();
                        break;

                    default:

                        break;
                }
            }
        });

         // UPLOAD DATA ON PAGE

        if(config.NetworkConnection()) {

               MDO_demandassesmentservey();

        }

    }
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }
    @Override
    public void onConnected(Bundle arg0) {
        try {
            LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fusedlocationRecieved = false;
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    Log.d(TAG, "Fused api connected: ");
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest,  this);
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
        }


    }

    @Override
    public void onConnectionSuspended(int arg0) {

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
            double lati = arg0.getLatitude();
            double longi = arg0.getLongitude();
            location=arg0;
            Log.d(TAG, "onLocationChanged: "+String.valueOf(longi));
            cordinate = String.valueOf(lati)+"-"+String.valueOf(longi);
            address = getCompleteAddressString(lati, longi);
            //accuracy = String.valueOf(arg0.getAccuracy());
            //Toast.makeText(context, cordinate+"S", Toast.LENGTH_SHORT).show();
            // locationInsertTime = arg0.getTime();
            //LocationBearing = arg0.getBearing();
            //LocationSpeed = arg0.getSpeed();
            //  fusedlocationRecieved = true;

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: "+e.toString());
            e.printStackTrace();
            //  }
        }

    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private synchronized void startFusedLocationService() {
       /* cancelNotification(NOTIFICATION_ID_REALTIME);*/
        try {
            LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                IsGPSEnabled = true;
            } else {
                IsGPSEnabled = false;
            }
            if (IsGPSEnabled) {
                locationRequest = LocationRequest.create();
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
                }
                GpsEnabled = true;

            } else {
                GpsEnabled = false;
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
            Log.d(TAG, "startFusedLocationService: ");
        }
        catch (Exception  ex)
        {
            //msclass.showMessage("Please on device GPS location.\n startFusedLocationService"+ex.getMessage());
            // msclass.showMessage("GPS is not enable,Please on GPS\n startFusedLocationService");
            Toast.makeText(this, "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResult(@NonNull Result result) {
        try {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    int st = getLocationMode();
                    if (st == 1 || st == 2) {
                        Toast.makeText(this, "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {

                        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException e) {

                    }
                    break;

                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are unavailable so not possible to show any dialog now
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this,"Func-onResult"+ex.toString(), Toast.LENGTH_LONG).show();

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int getLocationMode() {
        int val = 0;
        try {
            val = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
            Log.d(TAG, "getLocationMode status: " + val);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return val;

    }
    public void stopFusedApi() {
        try {
            if (googleApiClient != null && (googleApiClient.isConnected() )) {
                FusedLocationApi.removeLocationUpdates(googleApiClient, this);
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
    public void onResume() {
        super.onResume();
        try {
            // Resuming location updates depending on button state and
            // allowed permissions
            //  if (checkPermissions()) {
            startFusedLocationService();
            // updateLocationUI();
            // }
        }
        catch(Exception ex)
        {
            msclass.showMessage("Funtion name :onresume"+ex.getMessage());
        }

    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            stopFusedApi();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void MDO_demandassesmentservey()
    {

        String str= null;
        String Imagestring1="";
        String Imagestring2="";
        String ImageName="";
        Cursor cursor=null;
        String searchQuery="";
        int count=0;
        searchQuery = "select * from mdo_demandassesmentsurvey where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivation where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivationtobe where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_awaremahycoproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_authoriseddistributorproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();
        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select distinct  * from mdo_demandassesmentsurvey where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivation where Status='0'";
                    object.put("Table2", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivationtobe where Status='0'";
                    object.put("Table3", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_awaremahycoproduct where Status='0'";
                    object.put("Table4", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_authoriseddistributorproduct where Status='0'";
                    object.put("Table5", mDatabase.getResults(searchQuery));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new UploadDataServer("MDO_demandassesmentservey",objAsBytes, Imagestring1, Imagestring2, ImageName,"").execute(cx.MDOurlpath);

            } catch (Exception ex) {
                // msclass.showMessage(ex.getMessage());
                dialog.dismiss();
            }
        }
        else
        {
            dialog.dismiss();
        }
    }
    public void awaremahycoproduct()
    {
        try {
            boolean flag = false;

            /* flag = mDatabase.isTableExists("Tempstockdata");
            if (flag == false) {
                mDatabase.CreateTable2("Tempstockdata");
            } else {
                mDatabase.deleledata("Tempstockdata", " where status= 0 ");
            }*/

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.mahycoproductaware);
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            // showdata(my_linear_layout1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final SearchableSpinner spstatus = (SearchableSpinner) dialog.findViewById(R.id.spstatus);
            final SearchableSpinner spreason = (SearchableSpinner) dialog.findViewById(R.id.spreason);
            final WebView web1 = (WebView) dialog.findViewById(R.id.web1);
            final EditText txthydsown = (EditText) dialog.findViewById(R.id.txthydsown);

            showdata(my_linear_layout1,web1,"AwareMahyco",txthydsown);
            try {
                spstatus.setAdapter(null);
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

                Croplist.add(new GeneralMaster("0", "SELECT STATUS"));
                Croplist.add(new GeneralMaster("1", "CONTINUED(REPEATED USE)"));
                Croplist.add(new GeneralMaster("2", "DISCONTINUED (STOPPED USING)"));
                Croplist.add(new GeneralMaster("3", "NEW(WILL TRY THIS TIME)"));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spstatus.setAdapter(adapter);
            }
            catch (Exception ex)
            {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }



            final WebView webView = (WebView) dialog.findViewById(R.id.webView);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            final int[] flag1 = {0};
            ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag1[0] ==0)
                    {
                       // radGrp.clearCheck();
                    }
                    dialog.dismiss();
                }
            });




            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {

                        if(txthydsown .getText().length()==0) {
                            msclass.showMessage("Please enter hybrid sown.");
                            return;
                        }

                        GeneralMaster sp = (GeneralMaster) spstatus.getSelectedItem();
                        if (sp.Code().equals("0"))
                        {
                            msclass.showMessage("Please select status.") ;
                            return ;
                        }
                        GeneralMaster sr = (GeneralMaster) spreason.getSelectedItem();
                        if (sr.Code().equals("0"))
                        {
                            msclass.showMessage("Please select reason.") ;
                            return ;
                        }
                        // final String product=spproduct.getSelectedItem().toString();
                        // final String stock=txtstock.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        final String strdate = dateFormat.format(d);
                        // mDatabase.deleterecord("delete  from  Tempstockdata  where  status='0'" +
                        //        " and visitno='" + rowcount + "' and crop='" + crop + "'");

                        boolean f =mDatabase.Insertawaremahycoproduct(pref.getString("UserID", null)
                                ,txthydsown .getText().toString(),
                                txtmobile.getText().toString(),spstatus.getSelectedItem().toString(),
                                spreason.getSelectedItem().toString(),strdate,"0");
                        if (f == true)
                        {
                            msclass.showMessage("Mahyco product aware detail added.");

                            txthydsown.setText("");
                            txthydsown.requestFocus();
                            //chkyear1.setChecked(true);
                            flag1[0]=1;
                            showdata(my_linear_layout1,web1,"AwareMahyco",txthydsown);
                        }
                        else
                        {
                            radGrp.clearCheck();
                            flag1[0]=1;
                        }



                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });


            spstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                       String  reasontype = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                        String [] reasonlist=null;
                        if(reasontype.equals("1")) // Continued
                        {

                            reasonlist = new String[]{"SELECT REASON","SATISFACTORY PERFORMANCE","EASY AVAILABILITY","COMPANY BRAND NAME"+""
                                    ,"RIGHT SEED PRICE","GOOD AFTER SALE SERVICE","OTHER"
                            };
                        }

                        if(reasontype.equals("2")) // not Continued
                        {
                            reasonlist = new String[]{"SELECT REASON","UNSATISFACTORY PERFORMANCE","PRODUCT NOT AVAILABLE EASILY"+""
                                    ,"HIGHER SEED PRICE","POOR AFTER SALE SERVICE","SWITCHED TO BETTER PRODUCT","OTHER"
                            };
                        }
                        if(reasontype.equals("3")) // NEW
                        {
                            reasonlist = new String[]{"SELECT REASON","GOOD WORD OF MOUTH","RECOMMENDED BY RETAILER","RECOMMENDED BY FELLOW FARMER"+""
                                    ,"COMPANY BRAND NAME","EASY AVAILABILITY","GOOD AFTER SALES SERVICE","OTHER"
                            };
                        }
                        if (reasonlist!=null)
                        {
                            BindReason(spreason, reasonlist);
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    catch (Exception ex)
                    {
                        msclass.showMessage(ex.toString());
                    }


                 }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    //Toast.makeText(MyFieldActvity.this, "gngvnv", Toast.LENGTH_LONG).show();
                }
            });






            dialog.show();
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
            dialog.dismiss();
        }


    }
    private void BindReason(SearchableSpinner spreason,String[] reasonlist)
    {
        //st
        try {
            spreason.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

                for (int a=0;a<reasonlist.length ;a++) {
                    Croplist.add(new GeneralMaster(String.valueOf(a),
                            reasonlist[a].toString()));
                }

                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spreason.setAdapter(adapter);
                dialog.dismiss();

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }



        //en
    }

    public double totalareaset()
    {
        double  totalarea=0;
    try
    {
        JSONObject object = new JSONObject();
        object.put("Table1", mDatabase.getResults("select * from mdo_cultivation " +
                "where mobileno='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "'  "));

        JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);
        }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return totalarea;
    }
    public double showdata(LinearLayout my_linear_layout1,WebView web1,String type,EditText textarea)
    {
        my_linear_layout1.removeAllViews();
        //add this too
         double  totalarea=0;
        double  croparea=0;
        //st
        try {
            String tag="";
            tag = "<table style='border: 1px solid #1C6EA4; font-size: 12px; font-family: 'Calibri', Helvetica, sans-serif;' width='100%'>" ;

            // Cultivation
            if (type.equals("Cultivation")) {
                tag = tag + "<tr style='text-align: center; background-color: #2b3a56; color: #fff;'>" +
                        "<td>HY SOWN</td>" +
                        "<td>AREA UNDER HY</td>" +
                        "<td>PERFORMANCE</td>" +
                        "</tr>";

                //  "</table>";
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults("select * from mdo_cultivation " +
                        "where mobileno='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "'  "));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    final View view2 = LayoutInflater.from(this).inflate(R.layout.showaddproductdetail, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    tag = tag + "<tr style='text-align: center; background-color: #fff; color: #000;'>" +
                            "<td>" + jObject.getString("hybridsown").toString() + "</td>" +
                            "<td>" + jObject.getString("underhydarea").toString() + "</td>" +
                            "<td>" + jObject.getString("performance").toString() + "</td>" +
                            "</tr>";

                    totalarea = totalarea + Double.valueOf(jObject.getString("underhydarea").toString());
                    croparea = Double.valueOf(jObject.getString("area").toString());
                }
                tag = tag + "</table>";
                if (croparea > 0)
                {
                    textarea.setText(String.valueOf(croparea));
                    textarea.setEnabled(false) ;
                }
            }
            // Cultivation TO Be
            if (type.equals("CultivationToBe")) {
                        tag=tag+      "<tr style='text-align: center; background-color: #2b3a56; color: #fff;'>" +
                        "<td>HY SOWN</td>" +
                        "<td>AREA UNDER HY</td>" +
                        "<td>REASON</td>" +
                        "</tr>";

                //  "</table>";
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults("select * from mdo_cultivationtobe " +
                        "where mobileno='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "'  "));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    final View view2 = LayoutInflater.from(this).inflate(R.layout.showaddproductdetail, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    tag = tag + "<tr style='text-align: center; background-color: #fff; color: #000;'>" +
                            "<td>" + jObject.getString("hybridsown").toString() + "</td>" +
                            "<td>" + jObject.getString("underhydarea").toString() + "</td>" +
                            "<td>" + jObject.getString("reason").toString() + "</td>" +
                            "</tr>";
                    totalarea=totalarea+Double.valueOf(jObject.getString("underhydarea").toString());
                    croparea = Double.valueOf(jObject.getString("area").toString());
                }
                tag = tag + "</table>";
                if (croparea > 0)
                {
                    textarea.setText(String.valueOf(croparea));
                    textarea.setEnabled(false) ;
                }
            }
            // Aware Mahyco Product
            if (type.contains("AwareMahyco")) {
                tag=tag+  "<tr style='text-align: center; background-color: #2b3a56; color: #fff;'>" +
                        "<td>HY SOWN</td>" +
                        "<td>STATUS</td>" +
                        "<td>REASON</td>" +
                        "</tr>";

                //  "</table>";
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults("select * from mdo_awaremahycoproduct " +
                        "where mobileno='" + txtmobile.getText().toString() + "' and mdocode='" + usercode + "'  "));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    final View view2 = LayoutInflater.from(this).inflate(R.layout.showaddproductdetail, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    tag = tag + "<tr style='text-align: center; background-color: #fff; color: #000;'>" +
                            "<td>" + jObject.getString("hybridsown").toString() + "</td>" +
                            "<td>" + jObject.getString("typestatus").toString() + "</td>" +
                            "<td>" + jObject.getString("reason").toString() + "</td>" +
                            "</tr>";

                }
                tag = tag + "</table>";
            }

           // web1.loadData(tag, "text/html", "utf-8");
            WebSettings webSettings = web1.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // wb1.loadData(result, "text/html", null);
            web1.loadDataWithBaseURL(null, tag, null, "utf-8", null);

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return totalarea;
    }
    public void addcultivation(final String compname)
    {
        final double[] totalarea = {0};
        final double[] ttotal = {0};
        try {
            boolean flag = false;

            /* flag = mDatabase.isTableExists("Tempstockdata");
            if (flag == false) {
                mDatabase.CreateTable2("Tempstockdata");
            } else {
                mDatabase.deleledata("Tempstockdata", " where status= 0 ");
            }*/

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cultivation);
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            final WebView web1 = (WebView) dialog.findViewById(R.id.web1);
            final EditText txtarea = (EditText) dialog.findViewById(R.id.txtarea);
            totalarea[0] =showdata(my_linear_layout1,web1,"Cultivation",txtarea);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final SearchableSpinner spPerformance = (SearchableSpinner) dialog.findViewById(R.id.spPerformance);
           // final SearchableSpinner spCropType = (SearchableSpinner) dialog.findViewById(R.id.spCropType);


            try {
                spPerformance.setAdapter(null);
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

                Croplist.add(new GeneralMaster("0", "SELECT PERFORMANCE"));
                Croplist.add(new GeneralMaster("1", "VERY GOOD"));
                Croplist.add(new GeneralMaster("2", "GOOD"));
                Croplist.add(new GeneralMaster("3", "AVERAGE"));
                Croplist.add(new GeneralMaster("4", "POOR"));
                Croplist.add(new GeneralMaster("5", "  OTHER"));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPerformance.setAdapter(adapter);
            }
            catch (Exception ex)
            {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }


            final EditText txthydsown = (EditText) dialog.findViewById(R.id.txthydsown);
            final EditText txthydacre = (EditText) dialog.findViewById(R.id.txthydacre);

            final WebView webView = (WebView) dialog.findViewById(R.id.webView);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
           // bindcroptype(spCropType, "C");
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            final int[] flag1 = {0};
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag1[0] ==0)
                    {
                       // chkyear1.setChecked(false);
                    }
                    dialog.dismiss();
                }
            });


            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {  final String crop = spCropType.getSelectedItem().toString();
                        int count = 0;
                        //  String crop = "";
                        String product = "";
                        String stock = "";
                        /* if(crop.equals("SELECT CROP"))
                        { msclass.showMessage("Please select crop");
                            return;
                        }*/
                        if(txtarea.getText().length()==0) {
                           msclass.showMessage("Please enter total area under the crop(acre)");
                            return;
                        }
                       /* if( Double.valueOf(txtarea.getText().toString())==0) {
                            msclass.showMessage("Please enter total area under the crop(acre)");
                            return;
                        }*/
                        if(txthydsown .getText().length()==0) {
                            msclass.showMessage("Please enter hybrid sown.");
                            return;
                        }
                        if(txthydacre .getText().length()==0) {
                            msclass.showMessage("Please enter area under the hybrid (acre).");
                            return;
                        }

                        GeneralMaster sp = (GeneralMaster) spPerformance.getSelectedItem();
                        if (sp.Code().equals("0"))
                        {
                            msclass.showMessage("Please select overall performance.") ;
                            return ;
                        }
                        // final String product=spproduct.getSelectedItem().toString();
                        // final String stock=txtstock.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        final String strdate = dateFormat.format(d);
                        // mDatabase.deleterecord("delete  from  Tempstockdata  where  status='0'" +
                        //        " and visitno='" + rowcount + "' and crop='" + crop + "'");

                        ttotal[0] =Double.valueOf(totalarea[0])+Double.valueOf(txthydacre.getText().toString());

                         if (Double.valueOf( ttotal[0])> Double.valueOf(txtarea.getText().toString()))
                         {
                             msclass.showMessage("Total area under the hybrid should not be greater than total area under the crop.") ;
                             return ;
                         }
                             boolean f =mDatabase.Insertcultivationdata(pref.getString("UserID", null),"2018",
                                crop, txtarea.getText().toString(),txthydsown .getText().toString(),
                                txtmobile.getText().toString(),txthydacre .getText().toString(),spPerformance.getSelectedItem().toString(),
                                strdate,"0");
                        if (f == true)
                        {
                            msclass.showMessage("Hybrid detail added successfully.");
                            spPerformance.setSelection(0);
                            txthydacre.setText("");
                            txthydsown.setText("");
                            txthydsown.requestFocus();
                            chkyear1.setChecked(true);
                            flag1[0] =1;
                            totalarea[0] =showdata(my_linear_layout1,web1,"Cultivation",txtarea);
                            }
                            else
                        {
                            flag1[0] =0;
                            chkyear1.setChecked(false);
                        }



                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });





            dialog.show();
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
            dialog.dismiss();
        }


    }

    public void addcultivationtobe(final String compname)
    {
        final double[] totalarea = {0};
        final double[] ttotal = {0};
        try {
            boolean flag = false;

            /* flag = mDatabase.isTableExists("Tempstockdata");
            if (flag == false) {
                mDatabase.CreateTable2("Tempstockdata");
            } else {
                mDatabase.deleledata("Tempstockdata", " where status= 0 ");
            }*/

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cultivationtobe);
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            final WebView web1 = (WebView) dialog.findViewById(R.id.web1);
            final EditText txtarea = (EditText) dialog.findViewById(R.id.txtarea);

            totalarea[0] =showdata(my_linear_layout1,web1,"CultivationToBe",txtarea);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final SearchableSpinner spPerformance = (SearchableSpinner) dialog.findViewById(R.id.spPerformance);
           // final SearchableSpinner spCropType = (SearchableSpinner) dialog.findViewById(R.id.spCropType);

            try {
                spPerformance.setAdapter(null);
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();

                Croplist.add(new GeneralMaster("0", "SELECT REASON"));
                Croplist.add(new GeneralMaster("12", "NEW (WILL TRY THIS TIME)"));
                Croplist.add(new GeneralMaster("1", "OWN EXPERIENCE"));
                Croplist.add(new GeneralMaster("2", "FREE GIFTS"));
                Croplist.add(new GeneralMaster("3", "PARTICIPATED IN THE FIELD VISIT"));
                Croplist.add(new GeneralMaster("4", "RECOMMENDED BY FELLOW FARMER"));
                Croplist.add(new GeneralMaster("5", "RECOMMENDED BY RETAILER"));
                Croplist.add(new GeneralMaster("6", "ATTRACTIVE PACKAGING"));
                Croplist.add(new GeneralMaster("7", "INFLUENCED BY COMPANY REPRESENTATIVE"));
                Croplist.add(new GeneralMaster("8", "COMPANY BRAND VALUE"));
                Croplist.add(new GeneralMaster("9", "CHEAPER SEED PRICE"));
                Croplist.add(new GeneralMaster("10", "ADVT (TV/RADIO/NEWSPAPER/POP ETC)"));
                Croplist.add(new GeneralMaster("11", "  WORD OF MOUTH"));
                Croplist.add(new GeneralMaster("13", "  OTHER"));



                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPerformance.setAdapter(adapter);
            }
            catch (Exception ex)
            {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }
              final EditText txthydsown = (EditText) dialog.findViewById(R.id.txthydsown);
            final EditText txthydacre = (EditText) dialog.findViewById(R.id.txthydacre);

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            //bindcroptype(spCropType, "C");
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            final int[] flag1 = {0};
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag1[0]==0 )
                    {
                      //  chkyear2.setChecked(false);
                    }
                    dialog.dismiss();
                }
            });


            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {  final String crop = spCropType.getSelectedItem().toString();
                        int count = 0;
                        //  String crop = "";
                        String product = "";
                        String stock = "";
                       /* if(crop.equals("SELECT CROP"))
                        { msclass.showMessage("Please select crop");
                            return;
                        }*/
                        if(txtarea.getText().length()==0) {
                            msclass.showMessage("Please enter total area under the crop(acre)");
                            return;
                        }
                       /* if( Double.valueOf(txtarea.getText().toString())==0) {
                            msclass.showMessage("Please enter total area under the crop(acre)");
                            return;
                        }*/
                        if(txthydsown .getText().length()==0) {
                            msclass.showMessage("Please enter hybrid sown.");
                            return;
                        }
                        if(txthydacre .getText().length()==0) {
                            msclass.showMessage("Please enter area under the hybrid (acre).");
                            return;
                        }
                        ttotal[0] =Double.valueOf(totalarea[0])+Double.valueOf(txthydacre.getText().toString());

                        if (Double.valueOf( ttotal[0])> Double.valueOf(txtarea.getText().toString()))
                        {
                            msclass.showMessage("Total area under the hybrid should not be greater than total area under the crop.") ;
                            return ;
                        }
                        GeneralMaster sp = (GeneralMaster) spPerformance.getSelectedItem();
                        if (sp.Code().equals("0"))
                        {
                            msclass.showMessage("Please select overall performance.") ;
                            return ;
                        }
                        // final String product=spproduct.getSelectedItem().toString();
                        // final String stock=txtstock.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        final String strdate = dateFormat.format(d);
                        // mDatabase.deleterecord("delete  from  Tempstockdata  where  status='0'" +
                        //        " and visitno='" + rowcount + "' and crop='" + crop + "'");

                        boolean f =mDatabase.Insertcultivationtobedata(pref.getString("UserID", null),"2019",
                                crop, txtarea.getText().toString(),txthydsown .getText().toString(),
                                txtmobile.getText().toString(),txthydacre.getText().toString(),spPerformance.getSelectedItem().toString(),
                                strdate,"0") ;
                        if (f == true)
                        {
                            msclass.showMessage("Hybrid detail added successfully.");
                            spPerformance.setSelection(0);
                            txthydacre.setText("");
                            txthydsown.setText("");
                            txthydsown.requestFocus();
                            chkyear2.setChecked(true);
                            flag1[0]=1;
                            totalarea[0]=showdata(my_linear_layout1,web1,"CultivationToBe",txtarea);
                        }
                        else
                        {
                            chkyear2.setChecked(false);
                            flag1[0]=0;
                        }



                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });





            dialog.show();
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
            dialog.dismiss();
        }


    }






    public void AddstockDetail()
    {
        try {
            boolean flag = false;

            flag = mDatabase.isTableExists("Tempstockdata");
            if (flag == false) {
                mDatabase.CreateTable2("Tempstockdata");
            } else {
                mDatabase.deleledata("Tempstockdata", " where status= 0 ");
            }

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.addstockpanel);
            // dialog.setTitle("Service Operator Assignment");
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final Spinner spcrop = (Spinner) dialog.findViewById(R.id.spcrop);
            final Spinner spproduct = (Spinner) dialog.findViewById(R.id.spproduct);
            final EditText txtstock = (EditText) dialog.findViewById(R.id.txtStock);
            final WebView webView = (WebView) dialog.findViewById(R.id.webView);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            bindcroptype(spcrop, "C");
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try

                    {
                        int count = 0;
                        //  String crop = "";
                        String product = "";
                        String stock = "";
                        String salestock = "";
                        String currentstock = "";
                        final String crop = spcrop.getSelectedItem().toString();
                        // final String product=spproduct.getSelectedItem().toString();
                        // final String stock=txtstock.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        final String strdate = dateFormat.format(d);
                        mDatabase.deleterecord("delete  from  Tempstockdata  where  status='0'" +
                                " and visitno='" + rowcount + "' and crop='" + crop + "'");
                        // strftime( '%Y-%m-%d', INTime) ='" + strdate + "'" );

                        for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                            View view = my_linear_layout1.getChildAt(i);
                            TextView text = (TextView) view.findViewById(R.id.text);
                            final TextView txtStock = (TextView) view.findViewById(R.id.txtStock);
                            final TextView txtsalestock = (TextView) view.findViewById(R.id.txtsalestock);
                            final  TextView txtCurrentStock = (TextView) view.findViewById(R.id.txtCurrentStock);
                            txtsalestock.setEnabled(false);
                            product = text.getText().toString();
                            stock = txtStock.getText().toString();
                            salestock = txtsalestock.getText().toString();
                            currentstock = txtCurrentStock.getText().toString();





                            if ((!TextUtils.isEmpty(txtStock.getText().toString())) && (!TextUtils.isEmpty(txtsalestock.getText().toString()))) {

                                count++;

                                boolean f = mDatabase.Insertstockdata(pref.getString("UserID", null), String.valueOf(rowcount), crop, product, stock, currentstock, salestock, "0");
                                if (f == true) {
                                    //lblStockDetail.setText("Stock Details Added");
                                    // txtstock.setText("");
                                    // bindstockdata(webView);
                                } else {
                                    lblStockDetail.setText("");
                                }
                            }

                        }

                        if (count == 0) {
                            lblStockDetail.setText("");
                            msclass.showMessage("Please  enter atlest one product, stock received,current stock and sale stock details.");
                            return;
                        } else {
                            msclass.showMessage("Stock  detail added");
                            lblStockDetail.setText("Stock Details Added");
                            bindstockdata(webView);
                        }

                           /*boolean f= mDatabase.Insertstockdata( pref.getString("UserID", null),String.valueOf(rowcount), crop, product, stock,"0");
                            if (f==true) {
                                lblStockDetail.setText("Stock Details Added");
                               // txtstock.setText("");
                                bindstockdata(webView);
                            }
                            else
                            {
                                lblStockDetail.setText("");
                            } */
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            spcrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                        croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (Exception ex) {
                        Toast.makeText(demandassessmentsurvey.this, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                    // check = check + 1;
                    // if (check > 1)
                    {
                        BindProductName(spproduct, croptype, my_linear_layout1, 2);

                    }
                    // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(demandassessmentsurvey.this, "gngvnv", Toast.LENGTH_LONG).show();
                }
            });


            // text.setTextColor(getResources().getColor(R.color.black));
                   /* final Spinner spServiceOperator=(Spinner) dialog.findViewById(R.id.spServiceOperator);
                    TextView txtAccountdetail = (TextView) dialog.findViewById(R.id.txtAccountdetail);
                    text.setText("");
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    dialogButton.setVisibility(View.GONE);
                    lblcard.setVisibility(View.GONE);
                    */


            dialog.show();
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
            dialog.dismiss();
        }


    }
    public void bindstockdata(WebView wb1) {
        try {
            //String result=;

            JSONObject pobject = new JSONObject();
            try {
                pobject.put("Table1", mDatabase.getResults("select crop,product,stock ,salestock from Tempstockdata where status=0"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // JSONObject object = new JSONObject(result);
            //// Table 1 Data display
            JSONArray jArray = pobject.getJSONArray("Table1");
            String myHtmlString = "<html><body>" +
                    "<h4>Stock Details </h4>\n" +
                    "<table  border='1px' cellpadding='5' cellspacing='0' style='border-collapse:collapse; font-size: x-small;' width=\"100%\">\n" +
                    "  <tr align='left' valign='top'>\n" +
                    "    <td>SN</td>\n" +
                    "    <td>Crop</td>\t\t\n" +
                    "    <td>Product</td>\t\t\n" +
                    "    <td>Stock(bags)</td>\t\t\n" +
                    "    <td>Sale Stock(bags)</td>\t\t\n" +
                    "  </tr>\n";
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                myHtmlString = myHtmlString + "  <tr align='left' valign='top'>\n" +
                        "    <td>" + String.valueOf(i + 1) + "</td>\n" +
                        "    <td>" + jObject.getString("crop").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("product").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("stock").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("salestock").toString() + "</td>\t\t\n" +
                        "  </tr>\n";
            }

            wb1.loadData(myHtmlString, "text/html", null);
            //wb1.loadData(result, "text/html", null);
            // dialog.dismiss();

        }
        //catch (JSONException e) {
        //   e.printStackTrace();
        //   msclass.showMessage(e.getMessage());

        //}
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.getMessage());
            // dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        // dismissProgressDialog();
        dialog.dismiss();
        super.onDestroy();
    }

    private  void Savedata(final String myactvity, final String state, final String dist, String taluka, final String village)
    {
        try
        {
            pref.getString("UserID", null);
            Date entrydate = new Date();
                InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
             AlertDialog.Builder alertDialog = new AlertDialog.Builder(demandassessmentsurvey.this);
             alertDialog.setTitle("My Activity App");

            int radioButtonID = radGrp.getCheckedRadioButtonId();

            RadioButton radioButton = (RadioButton) radGrp.findViewById(radioButtonID);

            final String selectaware = (String) radioButton.getText();

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure to save this data ");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do do my action here

                    boolean fl = mDatabase.Insertdemandassesmentsurveydata(pref.getString("UserID", null)
                            ,txtFarmername.getText().toString(), state, spDist.getSelectedItem().toString().trim(), spTaluka.getSelectedItem().toString().trim(),
                            village,txtmobile.getText().toString(),
                            selectaware,InTime,"0",spCropType.getSelectedItem().toString(),cordinate,address) ;
                    // Toast.makeText(this, "Save Input data successfully", Toast.LENGTH_SHORT).show();
                    //dialog.dismiss();
                    // cleardata();
                    //dialog.dismiss();
                    if (fl==true)
                    {
                        try {
                            msclass.showMessage("Data saved successfully.");
                            txtFarmername.setText("");
                            txtmobile.setText("");
                            chkyear1.setChecked(false) ;
                            chkyear2.setChecked(false) ;
                           // radGrp.clearCheck();
                            int radioButtonID = radGrp.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) radGrp.findViewById(radioButtonID);
                            radioButton.setChecked(false);



                           /* mDatabase.Updatedata("update Tempstockdata set INTime='"+InTime+"',status='1' where status='0'");
                            Toast.makeText(demandassessmentsurvey.this, "data saved successfully.", Toast.LENGTH_SHORT).show();
                            // UploadData.UploadalldataActvity("tagdatauploadMDONew");
                            // UploadFarmerData("MDOFarmerMasterdataInsert");
                            Intent intent = new Intent(demandassessmentsurvey.this, UserHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish(); */
                        }
                        catch (Exception ex)
                        {
                            msclass.showMessage(ex.toString());
                        }
                    }
                    else
                    {
                        msclass.showMessage("Please check entry data.");
                    }


                }

            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // I do not need any action here you might

                    dialog.dismiss();

                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();
            final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            positiveButtonLL.weight = 10;
            positiveButtonLL.gravity = Gravity.CENTER;
            positiveButton.setLayoutParams(positiveButtonLL);
            //end

        }
        catch (Exception ex)
        {   ex.printStackTrace();
            Toast.makeText(this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            msclass.showMessage(ex.getMessage());
        }
    }


    private void BindProductName(Spinner spProductName,String croptype,LinearLayout my_linear_layout1, int flag)
    {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
            cursor.moveToFirst();
            if (flag == 2) {
                my_linear_layout1.removeAllViews();
                // for (int i = 0; i < cursor.getCount(); i++) {
                // JSONObject jObject = jArray.getJSONObject(i);


                while (cursor.isAfterLast() == false) {

                    View view2 = LayoutInflater.from(this).inflate(R.layout.stockrow, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    TextView text = (TextView) view2.findViewById(R.id.text);
                    final EditText txtStock = (EditText) view2.findViewById(R.id.txtStock);
                    final EditText txtsalestock = (EditText) view2.findViewById(R.id.txtsalestock);
                    final  EditText txtCurrentStock = (EditText) view2.findViewById(R.id.txtCurrentStock);
                    txtsalestock.setEnabled(false);
                    //Start

                    txtStock.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }

                                    //double cal = Double.parseDouble(txtGuntha.getText().toString()) * (Double.parseDouble(String.valueOf(s)));
                                    //txtcharges.setText(String.valueOf(cal));
                                    // txtsalestock.setText("");
                                    if (txtCurrentStock.getText().length()!=0)
                                    {
                                        int cont=Integer.valueOf(txtStock.getText().toString())-Integer.valueOf(txtCurrentStock.getText().toString());
                                        txtsalestock.setText(String.valueOf(cont));
                                    }
                                    // txtCurrentStock.setText("");
                                } else {
                                    //txtStock.setText("");
                                    txtsalestock.setText("");
                                    txtCurrentStock.setText("");
                                }
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }


                    });

                    //AREA CALULATION ON MANUAL Entry
                    txtCurrentStock.addTextChangedListener(new TextWatcher() {
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }
                                    //String gunthaarea = String.valueOf(Math.round(Double.parseDouble(String.valueOf(s)) * 40.00871459694989));
                                    int  currentst=Integer.parseInt(String.valueOf(s));
                                    int  receivestock=Integer.parseInt(String.valueOf(txtStock.getText()));
                                    if (currentst>receivestock)
                                    {
                                        msclass.showMessage("Current stock should not more than received stock.");
                                        txtCurrentStock.setText("");
                                    }
                                    else
                                    {
                                        txtsalestock.setText(String.valueOf(receivestock-currentst));
                                    }

                                } else {
                                    //txtCurrentStock.setText("");
                                    txtsalestock.setText("");
                                }
                            }
                            catch (Exception ex)
                            {
                                msclass.showMessage(ex.getMessage());
                                ex.printStackTrace();
                            }

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        public void afterTextChanged(Editable s) {

                        }
                    });
                    //end




                    if (croptype.equals("Cotton")) {
                        txtStock.setHint("In Pkts");
                        txtsalestock.setHint("In Pkts");
                        txtCurrentStock.setHint("In Pkts");
                    } else {
                        txtStock.setHint("In Bags");
                        txtsalestock.setHint("In Bags");
                        txtCurrentStock.setHint("In Bags");
                    }
                    text.setText(cursor.getString(0));
                    cursor.moveToNext();
                    my_linear_layout1.addView(view2);
                }
                cursor.close();

                // }
            }
            if (flag == 1) {
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
                dialog.dismiss();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }



        //en
    }
    private void bindcroptype(Spinner spCropType,String Croptype)
    {
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
            Croplist.add(new GeneralMaster("COTTON",
                    "COTTON"));
            Croplist.add(new GeneralMaster("PADDY",
                    "PADDY"));
            Croplist.add(new GeneralMaster("BAJRA",
                    "BAJRA"));
            cursor.moveToFirst();
           /* while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }*/
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

    }

    public void  BindState()
    {

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
            }catch (Exception e) {
                e.printStackTrace();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }
    public void  BindDist(String state)
    {
        try {
            spDist.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                //String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='"+state+"' order by district asc  ";

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
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }
    public void  BindTaluka(String dist)
    {
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
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    public void  BindVillage(String taluka)
    {
        spVillage.setAdapter(null);


        String str= null;
        try {
            dialog.setMessage("Loading....");
            dialog.show();
            //str = cx.new getVillage(taluka).execute().get();
            String searchQuery="";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;

            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "'" +
                    " and village_code<>'' order by  village ";
                        //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        Croplist.add(new GeneralMaster("SELECT VILLAGE",
                                "SELECT VILLAGE"));

            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

            Croplist.add(new GeneralMaster("OTHER",
                    "OTHER"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);
            dialog.dismiss();

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


        dialog.dismiss();

    }



    private boolean validation()
    {
        try {
            boolean flag = true;

            int count = mDatabase.getrowcount("select * from mdo_demandassesmentsurvey " +
                    "where mobileno='"+txtmobile.getText().toString()+"'  ");
            if(count>0) {
                msclass.showMessage("this farmer demand assessment servey data already saved on this mobile number "+txtmobile.getText().toString()+"");
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

            if (spVillage.getSelectedItem().toString().toLowerCase().equals("select village")) {
                msclass.showMessage("Please Select village");
                return false;
            }
            //if (txtvillage.getText().length() ==0) {
             //   msclass.showMessage("Please  enter village name ");
              //  return false;
           // }

            if (txtFarmername.getText().length()==0) {
                msclass.showMessage("Please enter farmer name");
                return false;
            }
            if (txtmobile.getText().length() < 10) {
                msclass.showMessage("Please enter 10 digit mobile number");
                return false;
            }
             String crop = spCropType.getSelectedItem().toString();
            if(crop.equals("SELECT CROP"))
            { msclass.showMessage("Please select crop");
                return false;
            }
            if(chkyear1.isChecked()==false)
            {
                msclass.showMessage("Please enter cultivation details for 2018..");
                return false;

            }
            if(chkyear2.isChecked()==false)
            {
                msclass.showMessage("Please enter cultivation details for 2019..");
                return false;

            }

            int count2 = mDatabase.getrowcount("select * from mdo_cultivation " +
                    "where mobileno='"+txtmobile.getText().toString()+"'  ");
            if(count2==0) {
                msclass.showMessage("Please enter cultivation details for 2018..");
                return false;

            }
            int count3 = mDatabase.getrowcount("select * from mdo_cultivationtobe " +
                    "where mobileno='"+txtmobile.getText().toString()+"'  ");
            if(count3==0) {
                msclass.showMessage("Please enter cultivation details for 2019..");
                return false;

            }


            if (radGrp.getCheckedRadioButtonId() == -1)
            {
                msclass.showMessage("Please select he aware of mahyco product (Yes or No). ");
                return false;

            }
            int radioButtonID = radGrp.getCheckedRadioButtonId();

            RadioButton radioButton = (RadioButton) radGrp.findViewById(radioButtonID);

            String selectaware = (String) radioButton.getText();
            if(selectaware.toLowerCase().contains("yes"))
            {
                int count4 = mDatabase.getrowcount("select * from mdo_awaremahycoproduct " +
                        "where mobileno='"+txtmobile.getText().toString()+"'  ");
                if(count4==0) {
                    msclass.showMessage("Please enter aware of mahyco product list..");
                    return false;

                }


            }




        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return true;
    }
    @Override
    public void onBackPressed() {

        try {
            Intent intent = new Intent(demandassessmentsurvey.this, voiceofcustomerDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }

    }

    public class UploadDataServer extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname,Intime;
        public UploadDataServer( String Funname, byte[] objAsBytes,String Imagestring1,String Imagestring2,String ImageName,String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes=objAsBytes;
            this.Imagestring1 =Imagestring1;
            this.Imagestring2 =Imagestring2;
            this.ImageName=ImageName;
            this.Funname=Funname;
            this.Intime=Intime;

        }
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            String Urlpath=urls[0];
            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";
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

            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());

            }
            catch (Exception e) {
                e.printStackTrace();

            }

            return builder.toString();
        }
        protected void onPostExecute(String result) {
              try{
                String resultout=result.trim();
                if(resultout.contains("True")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d=new Date();
                    String strdate=dateFormat.format(d);
                   if(Funname.equals("MDO_demandassesmentservey")) {
                        mDatabase.Updatedata("update mdo_demandassesmentsurvey  set Status='1'");
                        mDatabase.Updatedata("update mdo_cultivation         set Status='1'");
                        mDatabase.Updatedata("update mdo_cultivationtobe     set Status='1'");
                        mDatabase.Updatedata("update mdo_awaremahycoproduct  set Status='1'");
                        mDatabase.Updatedata("update mdo_authoriseddistributorproduct  set Status='1'");
                     }
                }
                else
                {
                    msclass.showMessage(result+"--E");

                }




            }

            catch (Exception e) {
                e.printStackTrace();

            }

        }
    }


}


