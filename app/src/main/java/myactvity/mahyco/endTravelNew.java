package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class endTravelNew   extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback,
        IPickResult,View.OnClickListener
{
    View parentHolder;
    public SearchableSpinner spvehicletype,spDist,spState, spTaluka, spVillage, spCropType, spProductName, spMyactvity, spComment;
    public Button btnstUpdate, btnTakephoto;
    private String state;
    EditText txtlocation;
    private Bitmap bitmap;

    private String dist, taluka, village, Imagepath1;
    private SqliteDatabase mDatabase;
    public Messageclass msclass;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    int imageselect;
    Context context;
    CheckBox chktag;
    Config confing;
    CommonExecution cx;
    public static int startreading=0;
    private long mLastClickTime = 0;
    File photoFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    SharedPreferences preferences;
    public   static String cordinate="";
    public static String address="" ;
    EditText txtkm;
    String usercode;
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
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private  Uri imageUri = null;
    private   String queryImageUrl = "";
    private String Imagename="";
    public endTravelNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_endtravel);
        getSupportActionBar().hide(); //<< this
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        context = this;
        confing=new Config(context);
        cx=new CommonExecution(context);
        initUI();
        if (checkPlayServices()) {

        }
        else
        {
            msclass.showMessage("This device google play services not supported for Devices location");
        }

    }


    private void initUI()
    {


        preferences = this.getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        TextView lblwelcome = (TextView) findViewById(R.id.lblwelcome);
        lblwelcome.setText("NAME: " + preferences.getString("Displayname", null));
        spState=(SearchableSpinner) findViewById(R.id.spState);
        spvehicletype= (SearchableSpinner)findViewById(R.id.spvehicletype);



        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        // spTehsil = (Spinn) parentHolder.findViewById(R.id.spTehsil);
        btnstUpdate = (Button) findViewById(R.id.btnstUpdate);
        btnTakephoto = (Button) findViewById(R.id.btnTakephoto);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        txtkm=(EditText) findViewById(R.id.txtEnd);
        chktag=(CheckBox) findViewById(R.id.chktag);
        txtlocation=(EditText) findViewById(R.id.txtlocation);


        //Freeze the Vehicle Type for Veg Users
        // Given By : Mr Munjaji Sir,Nitesh Kumar
        // Developer: Sumit
        Toast.makeText(context, ""+preferences.getString("unit", null), Toast.LENGTH_SHORT).show();
        if(preferences.getString("unit", null).contains("VCBU")) {
            spvehicletype.setEnabled(false);
            chktag.setChecked(true);

        }

        TextView lbltime = (TextView) findViewById(R.id.lbltime);
        Utility.setRegularFont(lblwelcome,context);
        Utility.setRegularFont(lbltime,context);
        Utility.setRegularFont(btnTakephoto,context);
        Utility.setRegularFont(btnstUpdate,context);
        Utility.setRegularFont(txtkm,context);
        Utility.setRegularFont(chktag,context);
        Utility.setRegularFont(txtlocation,context);
        Utility.setRegularFont(chktag,context);
        btnTakephoto.setOnClickListener(this);

        Date entrydate = new Date();
        // String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        String  InTime = new SimpleDateFormat("dd-MM-yyyy").format(entrydate);

        lbltime.setText("END TOUR FOR THE DAY- "+InTime);
        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode= pref.getString("UserID", null);

        // BindDist("");
        BindState();

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
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    //BindVillage(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        btnstUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveStarttravel();

            }
        });
        /*
        btnTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    //  ActivityCompat.requestPermissions(context, new String[] {android.Manifest.permission.CAMERA}, 101);
                }

                imageselect=1;
                // selectImage();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        imagetaken();
                       // selectImage();
                       // captureImage();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imagetaken();
                            //captureImage();
                           // selectImage2();
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
        });*/

        intialbinddata();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakephoto:
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
            ivImage.setImageBitmap(r.getBitmap());
            if (ivImage.getDrawable() != null) {
                ivImage.setVisibility(View.VISIBLE);
                //crdImage.setVisibility(View.VISIBLE);
            } else {
                ivImage.setVisibility(View.GONE);
                // crdImage.setVisibility(View.GONE);
            }
            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
            AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
            FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                    this, AppConstant.Imagename);
            // need to set commpress image path
            Imagename=AppConstant.Imagename;
            Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            //roundedImageView.setImageBitmap(myBitmap);
            ////
            /// encodedImage = FileUtilImage.getImageDatadetail(pathImage);

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void imagetaken()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    private void selectImage2() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                    Toast.makeText(this,
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    this.finish();
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
            address = getCompleteAddressString(lati, longi);
            //accuracy = String.valueOf(arg0.getAccuracy());
            // Toast.makeText(context, cordinate+"E", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Enable gps in High Accuracy only.", Toast.LENGTH_LONG).show();

            //msclass.showMessage("Please on device GPS location.\n startFusedLocationService"+ex.getMessage());
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

    public void intialbinddata()
    {
        spvehicletype.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0","SELECT VEHICLE TYPE"));
        gm.add(new GeneralMaster("1","COMPANY VEHICLE"));
        gm.add(new GeneralMaster("2","PERSONAL VEHICLE(4 wheeler)"));
        gm.add(new GeneralMaster("3","PERSONAL VEHICLE(2 wheeler)"));
        gm.add(new GeneralMaster("4","PUBLIC VEHICLE"));
        gm.add(new GeneralMaster("5","OTHER"));
        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spvehicletype.setAdapter(adapter);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d=new Date();
            String strdate=dateFormat.format(d);

            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults("select  * from mdo_endtravel  " +
                    "where strftime( '%Y-%m-%d', enddate)='"+strdate+"' and upper(mdocode)='"+usercode.toUpperCase()+"'"));
            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);

            JSONObject object2 = new JSONObject();
            object2.put("Table2", mDatabase.getResults("select  * from mdo_starttravel  " +
                    "where strftime( '%Y-%m-%d', startdate)='"+strdate+"' and" +
                    " upper(mdocode)='"+usercode.toUpperCase()+"'"));
            JSONArray jArray2 = object2.getJSONArray("Table2");//new JSONArray(result);
            startreading=0;
            if(jArray2.length()>0)
            {
                JSONObject jObject = jArray2.getJSONObject(0);
                if(jObject.getString("txtkm").toString().length() !=0) {
                    startreading = Integer.valueOf(jObject.getString("txtkm").toString());
                }
                spvehicletype.setSelection(confing.getIndex(spvehicletype,jObject.getString("vehicletype").toString()));
            }
            else
            {
                btnstUpdate.setVisibility(View.INVISIBLE);
                //chktag.setChecked(true);
                msclass.showMessage("Tour is not started ,please fill tour started data.");
            }




            if(jArray.length()>0) {

                btnstUpdate.setVisibility(View.INVISIBLE);
                chktag.setChecked(true);
                msclass.showMessage("Tour ended.");
                JSONObject jObject = jArray.getJSONObject(0);
           /* for(int i=0; i <jArray.length(); i++) {
                lblcomp1.setText(jObject.getString("compname").toString());
                lblcrop1.setText(jObject.getString("crop").toString());
                lblproduct1.setText(jObject.getString("product").toString());
                lblqty1.setText(jObject.getString("qty").toString());
            }*/
                BindDist("");
                spDist.setSelection(confing.getIndex(spDist, jObject.getString("dist").toString()));
                BindTaluka(jObject.getString("dist").toString()) ;
                spTaluka.setSelection(confing.getIndex(spTaluka, jObject.getString("taluka").toString()));
                // BindVillage(jObject.getString("taluka").toString());
                // spTehsil.setSelection(confing.getIndex(spTehsil, jObject.getString("village").toString()));
                txtkm.setText(jObject.getString("txtkm").toString());
                Imagepath1=jObject.getString("imgpath");
                // cx.bindimage(ivImage,jObject.getString("imgpath"));
                txtlocation.setText(jObject.getString("place").toString());
            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

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

    private void cameraIntent() {
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
            Uri mCapturedImageURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            // pictureImagePath = getRealPathFromURI(mCapturedImageURI);
            Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            this.startActivityForResult(intentPicture, REQUEST_CAMERA);

        } catch (Exception ex) {
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


       /* try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }*/
    }
    private void onCaptureImageResultnewol(Intent data) {

        try {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);
                Date entrydate = new Date();
                String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

                ivImage.setImageBitmap(bitmap);
                Imagepath1 = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "End" + File.separator + InTime;
                f.delete();
                OutputStream outFile = null;
                File file = new File(Imagepath1, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }
    private void onCaptureImageResult(Intent data) {

        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize =2;

                    //Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);

                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // this only get capture photo
                    //************
                    Date entrydate = new Date();
                    String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    queryImageUrl = photoFile.getAbsolutePath();
                    imageUri = Uri.fromFile(new File(queryImageUrl));
                    Imagename=this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    //ivImage.setImageBitmap(myBitmap);


                    Glide.with(context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .load(queryImageUrl)
                            .into(ivImage);

                    FileUtilImage.compressImageFile(queryImageUrl, imageUri,
                            this,Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave

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
    /* public void onLocationChanged1(Location location) {
         // Assign the new location
         try {
             //mLastLocation = location;
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
     public void onStatusChanged1(String provider, int status, Bundle extras) {
 
     }
 
     @Override
     public void onProviderEnabled1(String provider) {
 
     }
 
     @Override
     public void onProviderDisabled1(String provider) {
 
     } */
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
                Log.w("MyCurrentloctioaddress", strReturnedAddress.toString());
            } else {
                Log.w("MCurrent loctioaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MCurrenloctioaddress", "Canont get Address!");
        }
        return strAdd;
    }

    private boolean validation()
    {
        try {
            boolean flag = true;
            GeneralMaster sv = (GeneralMaster) spvehicletype.getSelectedItem();
            String vehicletype =sv.Code().toString();
            if (sv.Code().equals("0")) {
                msclass.showMessage("Please select vehicle type");
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


            /*if (spTehsil.getSelectedItem().toString().toLowerCase().equals("select village")) {
                msclass.showMessage("Please select village");
                return false;
            }*/




            if(vehicletype.equals("2")||vehicletype.equals("3")) // Only for company vehicle code validation
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d=new Date();
                String strdate=dateFormat.format(d);
                JSONObject object1 = new JSONObject();
                object1.put("Table1", mDatabase.getResults("select  * from mdo_addplace  " +
                        "where strftime( '%Y-%m-%d', date)='"+strdate+"' and mdocode='"+usercode+"'"));
                JSONArray jArray1 = object1.getJSONArray("Table1");//new JSONArray(result);
                /*if (jArray1.length() <2)
                 {
                     msclass.showMessage("Please tag the atleast two add places");
                     return false;
                 }
               */
                if (ivImage.getDrawable() == null) {
                    msclass.showMessage("Please upload vehicle reading photo(km)");
                    return false;
                }

                if (txtkm.getText().length() == 0) {
                    msclass.showMessage("Please enter start reading (km).");
                    return false;

                }

                JSONObject object2 = new JSONObject();
                object2.put("Table2", mDatabase.getResults("select  * from mdo_starttravel  " +
                        "where strftime( '%Y-%m-%d', startdate)='"+strdate+"' and mdocode='"+usercode+"'"));
                JSONArray jArray2 = object2.getJSONArray("Table2");//new JSONArray(result);
                if(jArray2.length()>0) {
                    JSONObject jObject = jArray2.getJSONObject(0);
                    startreading=Integer.valueOf(jObject.getString("txtkm").toString());
                }
                if(Integer.valueOf(txtkm.getText().toString()) < startreading ) {
                    msclass.showMessage("Your tour started KM reading is("+String.valueOf(startreading)+")."
                            +"\n"+"Please enter tour ended km reading should be more than tour start reading.");
                    return false;
                }



            }


            if (txtlocation.getText().length() == 0) {
                msclass.showMessage("Please enter location/place.");
                return false;
            }
            if(chktag.isChecked()==false)
            {
                msclass.showMessage("Please check geo tag.");
                return false;
            }
            if(cordinate.length()==0)
            {
                msclass.showMessage(" GPS Location not found ,please check GPS location setting .");
                return false;
            }


        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }

    public void saveStarttravel() {
        try {
            if (turnGPSOn() == false) {
                Toast.makeText(this, "GPS is not enabled,Please on GPS", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                if (validation() == true) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                    GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                    //  GeneralMaster vt = (GeneralMaster) spTehsil.getSelectedItem();

                    try {

                        dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                        taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                        // village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");
                        Savedata("", state, dist, taluka, village);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());

        }


    }

    private void Savedata(final String myactvity, String state, String dist, String taluka, String village) {
        try {
            pref.getString("UserID", null);
            Date entrydate = new Date();
            final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            final String Tempimagepath1;
            // final String Imagename = "endTravel" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
            Tempimagepath1 = Imagepath1;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog.setTitle("My Activity App");
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure to save this data ");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do do my action here
                    String vehicletype=spvehicletype.getSelectedItem().toString();

                    String crop = "";
                    String product = "";
                    String villagename="";//spTehsil.getSelectedItem().toString();
                    boolean fl = mDatabase.InsertendTravelTime(pref.getString("UserID", null),
                            cordinate, address, InTime,spDist.getSelectedItem().toString().trim(),
                            spTaluka.getSelectedItem().toString().trim(),
                            villagename,
                            Imagename,Tempimagepath1,txtkm.getText().toString(),
                            txtlocation.getText().toString(),vehicletype);

                    //dialog.dismiss();
                    // cleardata();
                    //dialog.dismiss();
                    if (fl == true) {
                        try {
                            msclass.showMessage("Tour end data saved successfully");
                            txtkm.setText("");
                            txtlocation.setText("");
                            btnstUpdate.setVisibility(View.INVISIBLE);

                        } catch (Exception ex) {
                            msclass.showMessage(ex.toString());
                        }
                    } else {
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

        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage("" + ex.getMessage());
            msclass.showMessage(ex.getMessage());
        }
    }

    private boolean turnGPSOn(){
        boolean flag=false;
        // boolean flag=true;
        try {
            LocationManager locationManager=null;
            try {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // locationManager = (LocationManager) context.getSystemService();
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
            if(!isGPSEnabled)// && !isGPSEnabled1)
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
    public void  BindState()
    {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("0",
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

    public void BindDist(String state) {
        try {
            spDist.setAdapter(null);

            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='"+state+"' order by district asc  ";

                //String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
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

        }


    }

    public void BindTaluka(String dist) {
        try {
            spTaluka.setAdapter(null);
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


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void BindVillage(String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {
            //str = cx.new getVillage(taluka).execute().get();
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster " +
                    "where taluka='" + taluka + "' and village <>'' order by  village ";
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

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }



    }
}

