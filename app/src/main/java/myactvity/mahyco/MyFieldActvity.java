package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.crash.FirebaseCrash;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MyFieldActvity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback
    , IPickResult,View.OnClickListener
{

    // implements GoogleApiClient.ConnectionCallbacks,
    // GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
    public Button btnsave, btnTakephoto,btnTakephoto2,addstock;
    public RadioGroup radGrp;
    private  ImageView ivImage,ivImage2,imggeo;
    public EditText  txtFarmerCount, txtComments;
    public TextView text,text2,lblStockDetail,lblgeoststus;
    public SearchableSpinner spDist, spTaluka, spVillage, spCropType, spProductName, spMyactvity,spComment;
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
    public String SERVER2 = "https://cmr.mahyco.com/FormerApp.asmx";
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
   // final Context context = this;
    private Context context;
    LinearLayout listock,liimagelayout;
    public CardView card_month7,card_month8;
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
        setContentView(R.layout.activity_my_field_actvity);

        listock = (LinearLayout) findViewById(R.id.listock);
        liimagelayout = (LinearLayout) findViewById(R.id.liimagelayout);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        imggeo= (ImageView) findViewById(R.id.imggeo);
        btnsave = (Button) findViewById(R.id.btnsave);
        txtComments = (EditText) findViewById(R.id.txtComments);
        txtFarmerCount = (EditText) findViewById(R.id.txtFarmerCount);
        text = (TextView) findViewById(R.id.text);
        text2 = (TextView) findViewById(R.id.text2);
        lblgeoststus= (TextView) findViewById(R.id.lblgeoststus);
        lblStockDetail= (TextView) findViewById(R.id.lblStockDetail);
        btnTakephoto = (Button) findViewById(R.id.btnTakephoto);
        btnTakephoto2 = (Button) findViewById(R.id.btnTakephoto2);
        addstock= (Button) findViewById(R.id.btnaddstock);
        config = new Config(this); //Here the context is passing
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (SearchableSpinner) findViewById(R.id.spProductName);
        context = this;
      //  spState = (Spinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spMyactvity = (SearchableSpinner) findViewById(R.id.spMyactvity);
        spComment= (SearchableSpinner) findViewById(R.id.spComment);
        rndRC = (RadioButton) findViewById(R.id.rndRC);
       // rndFC = (RadioButton) findViewById(R.id.rndFC);
        rndVC = (RadioButton) findViewById(R.id.rndVC);
        mDatabase = SqliteDatabase.getInstance(this);
        radGrp = (RadioGroup) findViewById(R.id.radGrp);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        card_month7= (CardView) findViewById(R.id.card_month7);
        card_month8= (CardView) findViewById(R.id.card_month8);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getSupportActionBar().hide(); //<< this
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        locedit = locdata.edit();
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    //    Toast.makeText(context, "Hiii ", Toast.LENGTH_SHORT).show();
     /*   radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case -1:
                        // Log.v(TAG, "Choices cleared!");
                        break;
                    case R.id.rndRC:
                        bindcroptype(spCropType,"C");
                        break;
                    case R.id.rndVC:
                        bindcroptype(spCropType,"V");
                        break;
                    default:
                        bindcroptype(spCropType,"C");
                        break;
                }
            }
        });
        rndRC.setChecked(true);
        */


        bindcroptype(spCropType,"C");
        BindDist("");
        bindmyActvityandcomment();
        bindvisitno();
        //init();
        turnGPSOn();
        //startLocation();  update Network  GPS cordinate

        if (checkPlayServices()) {

        }
        else
        {
          msclass.showMessage("This device google play services not supported for Devices location");
        }


        imggeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lblgeoststus.getText().toString().contains("Yes"))
                {
                    lblgeoststus.setText("Location Tag-No");
                    imggeo.setImageResource(R.drawable.nogeo);
                  //  msclass.showMessage("");
                }
                else
                {
                    imggeo.setImageResource(R.drawable.yesgeo);
                    lblgeoststus.setText("Location Tag-Yes");
                  //  msclass.showMessage(address+" "+cordinate);
                }

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (turnGPSOn() == false) {
                       // msclass.showMessage("GPS is not enabled,Please on GPS");
                        Toast.makeText(context, "GPS is not enabled,Please on GPS.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    } else {
                        if (validation() == true) {
                            // LoginRequest();

                   /* image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap(); */
                            // dialog.setMessage("Loading. Please wait...");
                            //dialog.show();

                            // GeneralMaster st = (GeneralMaster) spState.getSelectedItem();
                            GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                            GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                            GeneralMaster vt = (GeneralMaster) spVillage.getSelectedItem();
                            GeneralMaster myact = (GeneralMaster) spMyactvity.getSelectedItem();

                            try {

                                dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                                taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                                village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");
                                myactvity = myact.Code().trim();//URLEncoder.encode(myact.Code().trim(), "UTF-8");
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
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());

                }
            }
        });


        spMyactvity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                    boolean flag = false;
                  /*  flag = mDatabase.isTableExists("Tempstockdata");
                    if (flag == false) {
                        mDatabase.CreateTable2("Tempstockdata");
                    } else {
                        mDatabase.deleledata("Tempstockdata", " where status= 0 ");
                    }*/

                    if(gm.Desc().trim().toUpperCase().contains("FIELD VISIT")) {
                        Intent intent= new Intent(context.getApplicationContext(), DemoModelVisit.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else {
                        croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                        taluka = URLEncoder.encode(spTaluka.getSelectedItem().toString().trim(), "UTF-8");
                        if (gm.Desc().trim().toLowerCase().equals("retailer visit") || gm.Desc().trim().toLowerCase().equals("distributor visit")) {
                            // listock.setVisibility(view.VISIBLE);
                            addstock.setVisibility(view.VISIBLE);
                            lblStockDetail.setVisibility(view.VISIBLE);
                            spCropType.setVisibility(view.GONE);
                            spProductName.setVisibility(view.GONE);
                            txtFarmerCount.setVisibility(view.GONE);
                            lblStockDetail.setText("");
                            ivImage2.setVisibility(view.GONE);
                            btnTakephoto2.setVisibility(view.GONE);
                            card_month7.setVisibility(view.GONE);
                            card_month8.setVisibility(view.GONE);

                        } else
                        // listock.setVisibility(view.GONE);
                        {
                            card_month7.setVisibility(view.VISIBLE);
                            card_month8.setVisibility(view.VISIBLE);
                            lblStockDetail.setVisibility(view.GONE);
                            addstock.setVisibility(view.GONE);
                            spCropType.setVisibility(view.VISIBLE);
                            spProductName.setVisibility(view.VISIBLE);
                            txtFarmerCount.setVisibility(view.VISIBLE);
                            ivImage2.setVisibility(view.VISIBLE);
                            btnTakephoto2.setVisibility(view.VISIBLE);
                        }
                        BindVillage(taluka);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (Exception ex)
                {    ex.printStackTrace();
                   // Toast.makeText(MyFieldActvity.this,ex.toString(),Toast.LENGTH_LONG).show();
                }
                // check = check + 1;
                // if (check > 1)
                {

                   //BindVillage(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(MyFieldActvity.this, "gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (Exception ex)
                {
                    Toast.makeText(MyFieldActvity.this,ex.toString(),Toast.LENGTH_LONG).show();
                }


                // check = check + 1;
                // if (check > 1)
                {

                    BindProductName(spProductName,croptype,null,1);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(MyFieldActvity.this, "gngvnv", Toast.LENGTH_LONG).show();
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

        btnTakephoto.setOnClickListener(this);
        btnTakephoto2.setOnClickListener(this);
       /* btnTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(MyFieldActvity.this, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                imageselect=1;
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
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });

        btnTakephoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(MyFieldActvity.this, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                imageselect=2;
                try {
                    //selectImage();
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
                }catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });
  */

        addstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddstockDetail();
            }
        });
       // StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());

      //  turnGPSOn();

    }

       private boolean checkPlayServices() {
        boolean flag=false;
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                flag=false;
            }
            else
            {
                flag=true;
            }
        }
        catch (Exception ex)
        {
            flag=false;
        }
        return flag;
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
            if(address.equals("")) {
                if (config.NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            //accuracy = String.valueOf(arg0.getAccuracy());
            //Toast.makeText(this, cordinate+"S", Toast.LENGTH_SHORT).show();
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
            //msclass.showMessage("GPS is not enable,Please on GPS\n startFusedLocationService");
            Toast.makeText(context, "GPS is not enabled,Please on GPS.",
                    Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "onResult-Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {

                        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException e)
                    {

                    }
                    break;

                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are unavailable so not possible to show any dialog now
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this, "Func-onResult"+ex.toString(), Toast.LENGTH_LONG).show();

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
                        Toast.makeText(MyFieldActvity.this, ex.toString(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MyFieldActvity.this, "gngvnv", Toast.LENGTH_LONG).show();
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
    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            if (imageselect==1) {
                photoFile = createImageFile4();
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
            if (imageselect==2) {
                photoFile2 = createImageFile4();
                if (photoFile2 != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile2.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile2);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }
    private void captureImage()
    {

        try {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        if (imageselect==1) {
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
                        if (imageselect==2) {
                            photoFile2 = createImageFile();
                            //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            // Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile2 != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
                                        "myactvity.mahyco.fileProvider",
                                        photoFile2);
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
        }

        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
    }
    private File createImageFile4() //  throws IOException
    {    File mediaFile=null;
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
        return mediaFile;
    }
    private File createImageFile()
    {
        // Create an image file name
        File image=null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
             image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        }
        catch (Exception ex)
        {
             ex.printStackTrace();
            msclass.showMessage(ex.toString());
        }
        // Save a file: path for use with ACTION_VIEW intents
       // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void startLocation()
    {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        try {
            Dexter.withActivity(this)
                    .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            //startLocationUpdates();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void openSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }
    private void init() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mSettingsClient = LocationServices.getSettingsClient(this);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    // location is received
                    mCurrentLocation = locationResult.getLastLocation();

                    updateLocationUI();
                }
            };
            mRequestingLocationUpdates = false;
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            mLocationSettingsRequest = builder.build();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void updateLocationUI() {

        try {


            if (mCurrentLocation != null) {

                long time = mCurrentLocation.getTime();
              //  latitude = mCurrentLocation.getLatitude();
              //  longitude = mCurrentLocation.getLongitude();
              //  txtLocationResult.setText(String.valueOf(mCurrentLocation.getLatitude()) + "-" + String.valueOf(mCurrentLocation.getLongitude()));
              //  lblLocationaddress.setText(getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) );

                cordinate=String.valueOf(mCurrentLocation.getLatitude()) + "-" + String.valueOf(mCurrentLocation.getLongitude());
                address=getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());


            }
        }
        catch(Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }

    }
    private boolean turnGPSOn(){
        boolean flag=false;
        try {
            LocationManager  locationManager=null;
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
               // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            catch(SecurityException e) {
                e.printStackTrace();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                msclass.showMessage(ex.getMessage());
            }
            //boolean isGPSEnabled = locationManager
            //        .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isGPSEnabled1 = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled )//&& !isGPSEnabled1)
            {

                flag=false;

            }
            else
            {
                flag=true;
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
        return  flag;
    }

    @Override
    protected void onDestroy() {
       // dismissProgressDialog();
        dialog.dismiss();
        super.onDestroy();
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


  /*   @Override
   protected void onStop() {
        super.onStop();
        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }

    } */


 /*
    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        try {
            mLastLocation = location;
            // Displaying the new location on UI
            //displayLocation();
            cordinate = location.getLatitude() + "-" + location.getLongitude();
            address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
*/
 /*
    private void displayLocation() {

        try {


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
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
               // latitude = mLastLocation.getLatitude();
                //longitude = mLastLocation.getLongitude();

                //long time = mLastLocation.getTime();
               // entrydate = new Date(time);
                //  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
               // endate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(entrydate);
                // lblLocation.setText("ghfgh");
                cordinate=String.valueOf(mLastLocation.getLatitude()) + "-" + String.valueOf(mLastLocation.getLongitude());
                address=getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                text2.setText(cordinate);

            } else {
                //Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
                //intent.putExtra("enabled", true);
                // sendBroadcast(intent);
                //lblLocation
                 //       .setText("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

   */



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

    private  void Savedata(final String myactvity, String state, String dist, String taluka, String village)
   {
       Toast.makeText(MyFieldActvity.this, "Test Message", Toast.LENGTH_SHORT).show();

       try
       {
           pref.getString("UserID", null);
           Date entrydate = new Date();
           comments=spComment.getSelectedItem().toString(); //txtComments.getText().toString();
          final String commentdescription=txtComments.getText().toString();
           nooffarmer=txtFarmerCount.getText().toString();
             InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
             bm=null;
             b=null;
             b2=null;
             Imagename="";
             Imagename2="";
             final String Tempimagepath1;
             final String Tempimagepath2 ;


              Imagename2= AppConstant.Imagename2 ;//  "T"+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
              Imagename=AppConstant.Imagename ;//"O"+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;//pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
             Tempimagepath1=Imagepath1;
             Tempimagepath2=Imagepath2;
            //St
           // fg=false;
           // AlertDialog alertDialog = new AlertDialog.Builder(VisitorInformation.this).create();
           AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyFieldActvity.this);
           // Setting Dialog Title
           alertDialog.setTitle("My Activity App");
           // Setting Dialog Message
           alertDialog.setMessage("Are you sure to save this data ");
           alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialog, int which) {
                   // Do do my action here
                   Toast.makeText(MyFieldActvity.this, "Hiii", Toast.LENGTH_SHORT).show();
                   String crop="";
                   String product="";
                   if (spCropType.getSelectedItem().toString().toLowerCase().equals("select crop")) {
                       crop="";
                   }
                   else
                   {
                       crop=spCropType.getSelectedItem().toString();
                   }
                   if (spProductName.getSelectedItem().toString().toLowerCase().equals("select product")) {
                       product="";
                   }
                   else
                   {
                       product=spProductName.getSelectedItem().toString();
                   }
                   boolean fl = mDatabase.InsertTagData(pref.getString("UserID", null),
                           String.valueOf(rowcount), pref.getString("IMEI", null), myactvity, cordinate,
                           Tempimagepath1, address, Tempimagepath2, crop,
                           product, nooffarmer, InTime,
                           comments, "0", spDist.getSelectedItem().toString().trim(), spTaluka.getSelectedItem().toString().trim(),
                           spVillage.getSelectedItem().toString(), Imagename, b, Imagename2, b2,commentdescription);
                   // Toast.makeText(this, "Save Input data successfully", Toast.LENGTH_SHORT).show();
                   //dialog.dismiss();
                   // cleardata();
                   //dialog.dismiss();
                   if (fl==true)
                   {
                      try {
                          mDatabase.Updatedata("update Tempstockdata set INTime='"+InTime+"',status='1' where status='0'");
                            Toast.makeText(MyFieldActvity.this, "data saved successfully.", Toast.LENGTH_SHORT).show();
                          // UploadData.UploadalldataActvity("tagdatauploadMDONew");
                          // UploadFarmerData("MDOFarmerMasterdataInsert");
                          Intent intent = new Intent(MyFieldActvity.this, UserHome.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          startActivity(intent);
                          finish();
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
   private  void cleardata()
   {
       // txtComments.setText("");
        spComment.setSelection(0);
        txtFarmerCount.setText("");
       ivImage2.setImageDrawable(null);
       ivImage.setImageDrawable(null);
        bindvisitno();
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
    private void selectImageold(){
        final CharSequence[] items = { "Take Photo",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MyFieldActvity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }
    private void cameraIntent()
    {
      /*  try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           // file1 = Uri.fromFile(getOutputMediaFile());
           // file1= FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", getOutputMediaFile());
            file1=FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    getOutputMediaFile());


            intent.putExtra(MediaStore.EXTRA_OUTPUT, file1);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.toString());
            ex.printStackTrace();
        }
        */
      try {
          String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
          String imageFileName = timeStamp + "ABC.jpg";
          ContentValues values = new ContentValues();
          values.put(MediaStore.Images.Media.TITLE, imageFileName);
          Uri mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
          pictureImagePath = getRealPathFromURI(mCapturedImageURI);
          Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
          startActivityForResult(intentPicture, REQUEST_CAMERA);

      }
      catch (Exception ex) {
          ex.printStackTrace();
          msclass.showMessage(ex.getMessage());
      }


         /*try {

             destination = new File(Environment.getExternalStorageDirectory(),
                     imageFileName);
             //File storageDir = Environment.getExternalStoragePublicDirectory(
             //       Environment.DIRECTORY_PICTURES);
             pictureImagePath = destination.getAbsolutePath();// + "/" + imageFileName;
             File file = new File(pictureImagePath);
             Uri outputFileUri = Uri.fromFile(file);
             Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
             startActivityForResult(cameraIntent, REQUEST_CAMERA);
         }
         catch (Exception e) {
             e.printStackTrace();
         } */
       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnTakephoto:
                imageselect=1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnTakephoto2:
                imageselect=2;
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

        if (r.getError() == null) {
            GeneralMaster myact = (GeneralMaster) spMyactvity.getSelectedItem();


            if (imageselect == 1)
            {
                ivImage.setImageBitmap(r.getBitmap());
                if (ivImage.getDrawable() != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            if (imageselect == 2)
            {
                ivImage2.setImageBitmap(r.getBitmap());
                if (ivImage2.getDrawable() != null) {
                    ivImage2.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage2.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }


            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////


            if (imageselect == 1) {
                AppConstant.queryImageUrl = pathImage;
                AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
               // AppConstant.Imagename = myact.Desc().replace(" ","_")+"_Img1" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
               AppConstant.Imagename = "farmerlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 2) {
                AppConstant.queryImageUrl2 = pathImage;
                AppConstant.imageUri2 = Uri.fromFile(new File(AppConstant.queryImageUrl2));
              //  AppConstant.Imagename2 = myact.Desc().replace(" ","_")+"_Img2" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
               AppConstant.Imagename2 = "retailerlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile2(AppConstant.queryImageUrl2, AppConstant.imageUri2,
                        this, AppConstant.Imagename2);
                Imagepath2 = FileUtilImage.savefilepath2;// photoFile.getAbsolutePath();  old ssave
            }
            Log.i("Images ",Imagename+"   ---   "+Imagename2);

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
   // @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //File file = new File(Environment.getExternalStorageDirectory()+File.separator +pictureImagePath);
        //ivImage.setImageURI(file1);

        //Uri imageUri = data.getData();

      /* if (requestCode == REQUEST_CAMERA) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                myBitmap = Bitmap.createScaledBitmap(myBitmap, 300, 300, false);
               // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                try {
                    if (imageselect == 1) {
                        ivImage.setImageBitmap(myBitmap);
                    }
                    if (imageselect == 2) {
                        ivImage2.setImageBitmap(myBitmap);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

 */

      /*  try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }*/
    }



    private void onCaptureImageResult(Intent data) {


        /*Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect==1) {
            ivImage.setImageBitmap(bm);
        }
        if (imageselect==2) {
            ivImage2.setImageBitmap(bm);
        }*/




        // Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        //thumbnail = Bitmap.createScaledBitmap(thumbnail, 300, 400, false);
       // thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


       // destination = new File(Environment.getExternalStorageDirectory(),
         //       "Save.jpg");

       // FileOutputStream fo;
        //try {
           // destination.createNewFile();
            //fo = new FileOutputStream(destination);
            //fo.write(bytes.toByteArray());
            //fo.close();
      //  } catch (FileNotFoundException e) {
        //    e.printStackTrace();
       // } catch (IOException e) {
       //     e.printStackTrace();
       // }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =2;
            Date entrydate = new Date();
            String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            if (imageselect == 1) {


                try {

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    //************
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename=this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    FileUtilImage.compressImageFile( AppConstant.queryImageUrl, AppConstant.imageUri,
                            this,AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImage.setImageBitmap(myBitmap);
                    //**************
                }
                catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }

                //end
            }
            if (imageselect == 2) {
                try {

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    //************
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename2=this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    FileUtilImage.compressImageFile( AppConstant.queryImageUrl, AppConstant.imageUri,
                            this,AppConstant.Imagename2);
                    // need to set commpress image path
                    Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImage2.setImageBitmap(myBitmap);
                }
                catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }
        }
         catch (Exception e) {
             msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI1(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect==1) {
            ivImage.setImageBitmap(bm);
        }
        if (imageselect==2) {
            ivImage2.setImageBitmap(bm);
        }

    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private void bindvisitno()
    {
        //st
        Cursor cursor=null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String strdate = dateFormat.format(d);
            String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)='" + strdate + "'  ";
            //  String searchQuery = "select count(*) as total from TagData   ";

             cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount() + 1;
            rowcount = count;
            text.setText("VISIT NO : " + String.valueOf(rowcount));
            cursor.close();
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        //en
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

    private void bindmyActvityandcomment()
    {

        //st
        try {
            spMyactvity.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT  *  FROM MyActivityMaster  ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT ACTIVITY",
                    "SELECT ACTIVITY"));

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
            spMyactvity.setAdapter(adapter);
            dialog.dismiss();

            //en

            //st
            boolean flag = mDatabase.isTableExists("Commentlist");
            if (flag == false) {
                mDatabase.CreateTable2("Commentlist");
            }


            spComment.setAdapter(null);
            List<GeneralMaster> Croplist2 = new ArrayList<GeneralMaster>();
            String myTable2 = "Table1";//Set name of your table
            String searchQuery2 = "SELECT  *  FROM commentlist  ";
            Cursor cursor2 = mDatabase.getReadableDatabase().rawQuery(searchQuery2, null);
            Croplist2.add(new GeneralMaster("SELECT COMMENTS",
                    "SELECT COMMENTS"));

            cursor2.moveToFirst();
            while (cursor2.isAfterLast() == false) {

                Croplist2.add(new GeneralMaster(cursor2.getString(0),
                        cursor2.getString(0).toUpperCase()));

                cursor2.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spComment.setAdapter(adapter2);
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
            spCropType.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

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
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
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

                if(spMyactvity.getSelectedItem().toString().toLowerCase().equals("nursery visit"))
                {
                    searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Nursery' and taluka='"+taluka+"' order by  RetailerName  ";
                   // cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
                    Croplist.add(new GeneralMaster("SELECT NURSERY",
                            "SELECT NURSERY"));

                }
                else {
                    if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("retailer visit")) {
                        searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Retailer' and   upper(taluka)='" + taluka.toUpperCase() + "' order by  RetailerName ";
                      Log.i("Query",searchQuery);
                       // cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        Croplist.add(new GeneralMaster("SELECT RETAILER",
                                "SELECT RETAILER"));

                    }
                    else
                    {
                        if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("distributor visit")) {
                              // searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster" +
                              //      " where activity='Distributor' and " +
                              //      " taluka='" + taluka + "' order by  RetailerName ";
                            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                                    "where activity='Distributor' " +
                                    " order by  RetailerName ";


                           // cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                            Croplist.add(new GeneralMaster("SELECT DISTRIBUTOR",
                                    "SELECT DISTRIBUTOR"));
                          //  Croplist.add(new GeneralMaster("OTHER",
                            //        "OTHER"));

                        }
                        else {
                            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
                            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                            Croplist.add(new GeneralMaster("SELECT VILLAGE",
                                    "SELECT VILLAGE"));

                        }
                    }
                }
                cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                    cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();
                if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("retailer visit")) {
                   // Croplist.add(new GeneralMaster("N-FOCUSED",
                    //        "N-FOCUSED"));
                    //Croplist.add(new GeneralMaster("OTHER",
                      //      "OTHER"));
                }
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
            if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("select activity")) {
                msclass.showMessage("Please select activity");
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

            if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("retailer visit")) {
                if (spVillage.getSelectedItem().toString().toLowerCase().equals("select retailer")) {
                    msclass.showMessage("Please select retailer");
                    return false;
                }
            } else {
                if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("nursery visit")) {
                    if (spVillage.getSelectedItem().toString().toLowerCase().equals("select nursery")) {
                        msclass.showMessage("Please select  nursery");
                        return false;
                    }
                } else {
                    if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("distributor visit")) {
                        if (spVillage.getSelectedItem().toString().toLowerCase().equals("select distributor")) {
                            msclass.showMessage("Please Select distributor");
                            return false;
                        }
                    } else {
                        if (spVillage.getSelectedItem().toString().toLowerCase().equals("select village")) {
                            msclass.showMessage("Please select village");
                            return false;
                        }
                        if (spCropType.getSelectedItem().toString().toLowerCase().equals("select crop")) {
                            msclass.showMessage("Please select crop");
                            return false;
                        }
                        if (spProductName.getSelectedItem().toString().toLowerCase().equals("select product")) {
                            msclass.showMessage("Please select product");
                            return false;
                        }
                    }


                }
            }


            if (!spMyactvity.getSelectedItem().toString().toLowerCase().equals("retailer visit")
                    && !spMyactvity.getSelectedItem().toString().toLowerCase().equals("distributor visit")
                    //&& !spMyactvity.getSelectedItem().toString().toLowerCase().equals("farmer visit")
                    && !spMyactvity.getSelectedItem().toString().toLowerCase().contains("farmer visit")) {
                if (lblgeoststus.getText().toString().contains("No")) {
                    msclass.showMessage("Please take geo tag.");
                    return false;
                }
            if (ivImage.getDrawable() == null) {
                msclass.showMessage("Please upload activity photo");
                return false;
            }
            if (ivImage2.getDrawable() == null) {
                msclass.showMessage("Please upload farmer list photo");
                return false;
            }
            }

            //TOTAL NO od Farmer Compulsary
            if (!spMyactvity.getSelectedItem().toString().toLowerCase().equals("retailer visit")
                    && !spMyactvity.getSelectedItem().toString().toLowerCase().equals("distributor visit")
                    && !spMyactvity.getSelectedItem().toString().toLowerCase().equals("nursery visit")
                    )
             {

                if (txtFarmerCount.getText().length() == 0) {
                    msclass.showMessage("Please select total number of farmers met.");
                    return false;

                }
            }

            if (pref.getString("unit", null).contains("VCBU")) // Only For VEG TEAM
            {
                if (spMyactvity.getSelectedItem().toString().toLowerCase().equals("farmer visit(door to door)")
                        || spMyactvity.getSelectedItem().toString().toLowerCase().equals("farmer meeting")
                        || spMyactvity.getSelectedItem().toString().toLowerCase().equals("nursery visit")
                        || spMyactvity.getSelectedItem().toString().toLowerCase().equals("crop show")
                        || spMyactvity.getSelectedItem().toString().toLowerCase().equals("field day")
                        ||  spMyactvity.getSelectedItem().toString().toLowerCase().equals("live sample/plant display")
                        )
                {

                    if (ivImage.getDrawable() == null) {
                        msclass.showMessage("Please upload activity photo");
                        return false;
                    }
                }
            }
                if (spComment.getSelectedItem().toString().toLowerCase().equals("select comments")) {
                msclass.showMessage("Please select comments");
                return false;

            }
            if (txtComments.getText().length() == 0) {
                msclass.showMessage("Please enter comments description");
                return false;

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
             Intent intent = new Intent(MyFieldActvity.this, UserHome.class);
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

}


