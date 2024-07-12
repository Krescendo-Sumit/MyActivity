package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.GalleryCameraPickImageDialog;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.FileUtils;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.PathUtil;
import myactvity.mahyco.helper.SqliteDatabase;

public class Innovation extends AppCompatActivity implements LocationListener,
        IPickResult, View.OnClickListener {

    // implements GoogleApiClient.ConnectionCallbacks,
    // GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
    public ProgressDialog pd;
    public Button btnsave, btnTakephoto; //btnSelectPhotoGallery;
    private ImageView ivImage, ivImage2, imggeo;
    public EditText txtMobileNo, txtVehicle, txtFarmername, txtname2,
            txtname3, txtTotalPkt, txtTotalMahycoPkt, txtRemark, txtVillage, txtfarmname;
    public TextView text, lblTotalMahycoPkt, lblTotalPkt;
    public Spinner spDist, spTaluka, spLocation, spType;
    private String state;
    private String dist;
    public String imagestring;
    private String taluka;
    private String village, remark, name1, name2, name3, Totalpkt, MahycototalPkt;
    private String InTime;
    private Bitmap bm = null;
    private byte[] b = null;
    private byte[] b2 = null;
    private String Imagename = "";
    private String Imagename2 = "";
    private static final String TAG = Innovation.class.getSimpleName();
    public AutoCompleteTextView email;
    ProgressDialog dialog;

    public String langcode = "", mobileno, location, username, vehicle;

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
    public String Imagepath1 = "", Imagepath2 = "";
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
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
    private static String cordinate;
    private static String address="";
    private String pictureImagePath = "";
    private Uri file1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    File photoFile = null;
    File photoFile2 = null;
    final Context context = this;
    LinearLayout listock, liimagelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innovation);
        listock = (LinearLayout) findViewById(R.id.listock);
        liimagelayout = (LinearLayout) findViewById(R.id.liimagelayout);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        imggeo = (ImageView) findViewById(R.id.imggeo);
        btnsave = (Button) findViewById(R.id.btnsave);
        lblTotalMahycoPkt = (TextView) findViewById(R.id.lblTotalMahycoPkt);
        lblTotalPkt = (TextView) findViewById(R.id.lblTotalPkt);
        txtMobileNo = (EditText) findViewById(R.id.txtMobileNo);
        txtVehicle = (EditText) findViewById(R.id.txtVehicle);
        txtfarmname = (EditText) findViewById(R.id.txtfarmname);
        txtFarmername = (EditText) findViewById(R.id.txtFarmername);
        txtname2 = (EditText) findViewById(R.id.txtname2);
        txtname3 = (EditText) findViewById(R.id.txtname3);
        txtTotalMahycoPkt = (EditText) findViewById(R.id.txtTotalMahycoPkt);
        txtTotalPkt = (EditText) findViewById(R.id.txtTotalPkt);

        txtRemark = (EditText) findViewById(R.id.txtRemark);
        text = (TextView) findViewById(R.id.text);

        btnTakephoto = (Button) findViewById(R.id.btnTakephoto);
        //btnSelectPhotoGallery = (Button) findViewById(R.id.btnSelectPhotoGallery);
        config = new Config(this); //Here the context is passing
        //context = this;
        pd = new ProgressDialog(context);

        spLocation = (Spinner) findViewById(R.id.spLocation);
        spType = (Spinner) findViewById(R.id.spType);
        spDist = (Spinner) findViewById(R.id.spDist);
        spTaluka = (Spinner) findViewById(R.id.spTaluka);
        txtVillage = (EditText) findViewById(R.id.txtVillage);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);


        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getSupportActionBar().hide(); //<< this
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        locedit = locdata.edit();
        BindDist("");
        bindVehicle();
        bindvisitno();

        UploadalldataActvity("InnovationData");


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    if (validation() == true) {
                        // LoginRequest();

                        /* image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap(); */
                        // dialog.setMessage("Loading. Please wait...");
                        //dialog.show();

                        // GeneralMaster st = (GeneralMaster) spState.getSelectedItem();
                        GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                        GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                        // GeneralMaster vt = (GeneralMaster) spVillage.getSelectedItem();
                        GeneralMaster vehicle = (GeneralMaster) spLocation.getSelectedItem();

                        try {

                            dist = dt.Code().trim();//URLEncoder.encode(dt.Code().trim(), "UTF-8");
                            taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
                            village = txtVillage.getText().toString().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");
                            String vh = vehicle.Code().trim();//URLEncoder.encode(myact.Code().trim(), "UTF-8");
                            String firmame = "";
                            if (spType.getSelectedItem().toString().equals("RETAILER") || spType.getSelectedItem().toString().equals("DISTRIBUTOR")) {
                                firmame = txtfarmname.getText().toString();
                            }


                            Savedata(vh, state, dist, taluka, village, firmame);
                        } //catch (UnsupportedEncodingException e) {

                        catch (Exception ex) {
                            ex.printStackTrace();
                            msclass.showMessage("PLEASE  DOWNLOAD MASTER DATA FIRST\n" + ex.getMessage());
                            // msclass.showMessage(ex.getMessage());
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage("PLEASE  DOWNLOAD MASTER DATA FIRST\n" + ex.getMessage());


                }
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
            }
        });

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    if (gm.Code().trim().equals("RETAILER") || gm.Code().trim().equals("DISTRIBUTOR")) {
                        lblTotalMahycoPkt.setText("K22  PKT SOLD OF MAHYCO");
                        lblTotalPkt.setText("K22 TOT  PKT SOLD ALL COMPANIES");
                        txtfarmname.setVisibility(View.VISIBLE);
                    } else {
                        lblTotalMahycoPkt.setText("TOT MAHYCO PKT SOWN IN K-22");
                        lblTotalPkt.setText("TOT  PKT SOWN IN K-22");
                        txtfarmname.setVisibility(View.GONE);
                    }
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
            }
        });
        btnTakephoto.setOnClickListener((View.OnClickListener) this);
         /* btnTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                imageselect=1;
                selectImage();

            }
        });*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakephoto:
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                /*Added on 9th Sept 2021 for Camera and Gallery Option*/
                GalleryCameraPickImageDialog.build(new PickSetup()).show(this);
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
            Imagename = AppConstant.Imagename;
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

    private void bindVehicle() {

        //st
        try {
            spLocation.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT  *  FROM VehicleMaster";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            //Croplist.add(new GeneralMaster("Jalna-maharashtra",
            //      "Jalna-maharashtra "));

            Croplist.add(new GeneralMaster("SELECT LOCATION", "SELECT LOCATION"));
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
            spLocation.setAdapter(adapter);
            dialog.dismiss();
            spType.setAdapter(null);
            List<GeneralMaster> Croplist2 = new ArrayList<GeneralMaster>();
            Croplist2.add(new GeneralMaster("0",
                    "SELECT TYPE"));
            Croplist2.add(new GeneralMaster("RETAILER",
                    "RETAILER"));
            Croplist2.add(new GeneralMaster("DISTRIBUTOR",
                    "DISTRIBUTOR"));

            Croplist2.add(new GeneralMaster("FARMER",
                    "FARMER"));
            Croplist2.add(new GeneralMaster("OTHER",
                    "OTHER"));

            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spType.setAdapter(adapter2);
            dialog.dismiss();


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        //en
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
            if (imageselect == 2) {
                photoFile2 = createImageFile4();
                if (photoFile2 != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile2.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile2);
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

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        if (imageselect == 1) {
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
                        if (imageselect == 2) {
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
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
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
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
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

                cordinate = String.valueOf(mCurrentLocation.getLatitude()) + "-" + String.valueOf(mCurrentLocation.getLongitude());
                address = getCompleteAddressString(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());


            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        // dismissProgressDialog();
        dialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

              /*  try {
               if (mRequestingLocationUpdates) {
                   // pausing location updates
                   stopLocationUpdates();
               }
             }
            catch (Exception ex)
            {
                ex.printStackTrace();
            } */
    }


    /**
     * Starting the location updates
     */
    private void startLocationUpdates() {
        try {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            //Log.i(TAG, "All location settings are satisfied.");

                            Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                            //noinspection MissingPermission
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());

                            updateLocationUI();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                            "location settings ");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(Innovation.this, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);

                                    Toast.makeText(Innovation.this, errorMessage, Toast.LENGTH_LONG).show();
                            }

                            updateLocationUI();
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    public void stopLocationUpdates() {
        try {
            mFusedLocationClient
                    .removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        try {
            mLastLocation = location;
            // Displaying the new location on UI
            //displayLocation();
            cordinate = location.getLatitude() + "-" + location.getLongitude();
            address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
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


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }


    public void UploadalldataActvity(String Functionname) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from InnovationData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                searchQuery = "select * from InnovationData where Status='0' and Imgname='" + ImageName + "' limit 1 ";
                                object.put("Table1", mDatabase.getResults(searchQuery));
                                Imagestring1 = mDatabase.getImageData(searchQuery, "imagepath");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new UploadDataServer(Functionname, objAsBytes, Imagestring1, Imagestring2, ImageName, "t", this).execute(cx.MDOurlpath);

                            pd.dismiss();

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End

                    } catch (Exception ex) {  // dialog.dismiss();
                        // msclass.showMessage(ex.getMessage());


                    }
                } else {
                    //dialog.dismiss();
                    //  msclass.showMessage("Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                //  msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            // msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void Savedata(final String Loc, String state, String dist, String taluka, String village, final String Firmname) {
        try {
            pref.getString("UserID", null);
            Date entrydate = new Date();
            name1 = txtFarmername.getText().toString();
            name2 = txtname2.getText().toString();
            final String vehicle = txtVehicle.getText().toString();
            name3 = txtname3.getText().toString();

            remark = txtRemark.getText().toString();
            mobileno = txtMobileNo.getText().toString();
            Totalpkt = txtTotalPkt.getText().toString();
            MahycototalPkt = txtTotalMahycoPkt.getText().toString();
            InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
            bm = null;
            b = null;
            b2 = null;

            final String Tempimagepath1;
            final String Tempimagepath2;


            // Imagename="Innovation"+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;//pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
            Tempimagepath1 = Imagepath1;
            Tempimagepath2 = Imagepath2;
            //St
            // fg=false;
            // AlertDialog alertDialog = new AlertDialog.Builder(VisitorInformation.this).create();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Innovation.this);
            // Setting Dialog Title
            alertDialog.setTitle("My Activity App");
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure to save this data ");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    try {
                        String searchQuery = "select  *  from InnovationData where Mobileno='" + mobileno + "'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        int count = cursor.getCount();

                        if (count > 0) {
                            msclass.showMessage("This mobile no." + mobileno + " alredy registered for innovation day 2021");
                        } else {
                            // Do do my action here
                            boolean fl = mDatabase.InnovationDataDetail(spType.getSelectedItem().toString(), pref.getString("UserID", null),
                                    name1, name2, name3, mobileno,
                                    spDist.getSelectedItem().toString().trim(), spTaluka.getSelectedItem().toString().trim(),
                                    txtVillage.getText().toString(), Imagename, Totalpkt, MahycototalPkt
                                    , remark+"-"+cordinate, Loc, vehicle, Tempimagepath1, Firmname);
                            // Toast.makeText(this, "Save Input data successfully", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                            // cleardata();
                            //dialog.dismiss();
                            if (fl == true) {
                                try {
                                    Toast.makeText(Innovation.this, "data saved successfully.", Toast.LENGTH_SHORT).show();
                                    // Intent intent = new Intent(Innovation.this, UserHome.class);
                                    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // startActivity(intent);
                                    //finish();
                                    cleardata();
                                } catch (Exception ex) {
                                    msclass.showMessage(ex.toString());
                                }
                            } else {
                                msclass.showMessage("Please check entry data.");
                            }
                        }
                    } catch (Exception ex) {
                        msclass.showMessage(ex.toString());
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
            Toast.makeText(this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            msclass.showMessage(ex.getMessage());
        }
    }

    private void cleardata() {
        // txtComments.setText("");
        // spDist.setSelection(0);
        // spTaluka.setSelection(0);
        // txtVillage.setText("");
        //sp.setSelection(0);
        txtRemark.setText("");
        txtFarmername.setText("");
        txtname2.setText("");
        txtname3.setText("");
        txtMobileNo.setText("");
        txtTotalPkt.setText("");
        txtTotalMahycoPkt.setText("");

        ivImage.setImageDrawable(null);

        if (spType.getSelectedItem().toString().trim().equals("RETAILER")) {
            txtfarmname.setText("");
        }
        bindvisitno();
    }

    private void selectImage() {
        final CharSequence[] items = {"Capture Photo From Phone Camera", "Select Photo from Phone Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Innovation.this);
                if (items[item].equals("Capture Photo From Phone Camera")) {
                    userChoosenTask = "Take Photo";
                    if (result) {
                        // cameraIntent();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                captureImage();
                            } else {
                                captureImage2();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            msclass.showMessage(ex.getMessage());
                        }
                    }
                } else if (items[item].equals("Select Photo from Phone Gallery")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent() {
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

        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage("Please capture the image from Camera ." + ex.getMessage());
        }


    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
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

     /*   try {
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
            if (imageselect == 1) {
                //ivImage.setImageBitmap(thumbnail);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                //Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                // String path = getImageUri(getApplicationContext(), thumbnail);
                // msclass.showMessage(pictureImagePath);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                //File finalFile = new File(getRealPathFromURI1(tempUri));
                //Strt
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize = 2;

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    Imagepath1 = photoFile.getAbsolutePath();
                    ivImage.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }
            if (imageselect == 2) {
                //ivImage2.setImageBitmap(thumbnail);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize = 2;
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile2.getAbsolutePath(), options);
                    Imagepath2 = photoFile2.getAbsolutePath();
                    ivImage2.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        } catch (Exception e) {
            //return res;
            return contentUri.getPath();
        }
        //return res;
    }

    @SuppressLint("NewApi")
    public static String getPathAPI19(Context context, Uri uri) {
        String filePath = "";
        try {
            String fileId = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = fileId.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String selector = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, selector, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            //int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } catch (Exception e) {
            //return res;
            return uri.getPath();
        }
    }

    @SuppressLint("NewApi")
    public static String getPathAPI11To18(Context context, Uri contentUri) {

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        } catch (Exception e) {
            //return res;
            return contentUri.getPath();
        }

    }

    public static String getPathBelowAPI11(Context context, Uri contentUri) {

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } catch (Exception e) {
            //return res;
            return contentUri.getPath();
        }

    }

    private void onSelectFromGalleryResult(Intent data) {

        try {
            Uri selectedImage = data.getData();
            Bitmap bm = null;
            if (data != null) {
                try {

                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    // String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bm, "Title", null);

                    //File thePath = new File(getRealPathFromURI(selectedImage));

                    if (Build.VERSION.SDK_INT >= 19) {
                        pictureImagePath = getPathAPI19(this.context, selectedImage);
                    } else {
                        if (Build.VERSION.SDK_INT >= 11) {
                            pictureImagePath = getPathAPI11To18(this.context, selectedImage);
                        } else {
                            pictureImagePath = getPathBelowAPI11(this.context, selectedImage);

                        }
                        // do something for phones running an SDK before lollipop
                    }
                    pictureImagePath = FileUtils.getPath(this.context, selectedImage);
                    // pictureImagePath= getPathAPI19(this.context, selectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (imageselect == 1) {
                String filePath = PathUtil.getPath(this.context, selectedImage);
                Imagepath1 = pictureImagePath;
                ivImage.setImageBitmap(bm);

                // String path = ivImage.getTag().toString();
                //get absolute path
                // File file=new  File(bm.)
                //String realPath = file.getAbsolutePath();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
        }

    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void bindvisitno() {
        //st
        Cursor cursor = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String strdate = dateFormat.format(d);
            String searchQuery = "select * from InnovationData";//  where  strftime( '%Y-%m-%d', INTime)='" + strdate + "'  ";
            //  String searchQuery = "select count(*) as total from TagData   ";

            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount() + 1;
            rowcount = count;
            text.setText("REG.COUNT : " + String.valueOf(rowcount));
            cursor.close();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        //en
    }


    public void BindDist(String state) {
        try {
            spDist.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT DIST",
                        "SELECT DIST"));

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
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
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

    private boolean validation() {
        try {
            boolean flag = true;
            GeneralMaster type = (GeneralMaster) spType.getSelectedItem();
            if (type.Code().trim().equals("0")) {
                msclass.showMessage("Please Select Type ");
                return false;
            }

            if (txtFarmername.getText().length() == 0) {
                msclass.showMessage("Please enter first name ");
                return false;

            }
            if (txtname3.getText().length() == 0) {
                msclass.showMessage("Please enter Last name ");
                return false;

            }

            if (type.Code().trim().equals("RETAILER")) {
                if (txtfarmname.getText().length() == 0) {
                    msclass.showMessage("Please enter firm name ");
                    return false;

                }

            }

            if (txtMobileNo.getText().length() == 0) {
                msclass.showMessage("Please enter Mobile Number ");
                return false;

            }
            if (txtMobileNo.getText().length() < 10) {
                msclass.showMessage("Mobile number should be 10 digit.");
                return false;

            }
            if (spDist.getSelectedItem().toString().toLowerCase().equals("select dist")) {
                msclass.showMessage("Please select distinct");
                return false;
            }
            if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }

            if (txtVillage.getText().length() == 0) {
                msclass.showMessage("Please enter village/place name ");
                return false;

            }
            if (txtTotalPkt.getText().length() == 0) {
                msclass.showMessage("Please enter total cotton pkt k-21 ");
                return false;

            }
            if (txtTotalMahycoPkt.getText().length() == 0) {
                msclass.showMessage("Please enter total mahyco cotton pkt  k-21 ");
                return false;

            }

            GeneralMaster vh = (GeneralMaster) spLocation.getSelectedItem();
            if (vh.Code().trim().isEmpty() || vh.Code() == null) {
                msclass.showMessage("Please Select innovation day location/Download master data");
                return false;
            }

            if (vh.Code().trim()=="SELECT LOCATION") {
                msclass.showMessage("Please Select Jumbo field/innovation day location");
                return false;
            }

            //URLEncoder.encode(dt.Code().trim(), "UTF-8");
            if (ivImage.getDrawable() == null) {
                msclass.showMessage("Please upload (retailer/farmer) photo");
                return false;
            }

            if (cordinate.trim().equals("")) {
                msclass.showMessage("Please wait for location.");
                return false;
            }


        } catch (Exception ex) {
            msclass.showMessage("PLEASE  DOWNLOAD MASTER DATA FIRST\n" + ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        try {
            Intent intent = new Intent(Innovation.this, UserHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }

    }

    /*Changes START Added on 8th Sept 2021 ------------------------------------------------------------------------------------------*/

    private class UploadDataServer extends AsyncTask<String, String, String> {
        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;
        Context context;

        public UploadDataServer(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime, Context context) {
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;
            this.context = context;
        }

        protected void onPreExecute() {
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject obj = new JSONObject();
                String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("encodedData", encodeImage);
                jsonObject.put("input1", Imagestring1);
                jsonObject.put("input2", Imagestring2);
                obj.put("Table", jsonObject);
                Log.d("Innovation", "************ UPDATE API URL : " + Constants.POST_INNOVATION_DATA);
                Log.d("Innovation", "************ UPDATE API JSON OBJECT : " + obj);
                Prefs mPref = Prefs.with(context);
                Log.d("Innovation", "************ UPDATE API JSON OBJECT ACCESS_TOKEN_TAG : " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                String result = HttpUtils.POSTJSON(Constants.POST_INNOVATION_DATA, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                Log.d("Innovation", "************ UPDATE API RESPONSE : " + result);
                return result;
            } catch (Exception e) {
                Log.d("MSG", "MSG : " + e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                redirecttoRegisterActivity(resultout);

                if (pd != null) {
                    pd.dismiss();
                }
                Log.d("Innovation", "UPDATE API Response" + resultout);
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");
                        pd.dismiss();
                    }
                }
            } catch (Exception e) {
                Log.d("Innovation", e.getMessage());
                msclass.showMessage("Something went wrong, please try again later");

            }

        }

    }

    public void redirecttoRegisterActivity(String result) {
        if (result.toLowerCase().contains("authorization")) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

            builder.setTitle("MyActivity");
            builder.setMessage("Your login session is  expired, please register user again. ");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    editor.putString("UserID", null);
                    editor.commit();
                    Intent intent1 = new Intent(context.getApplicationContext(), UserRegister.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);
                }
            });
            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /*Changes END Added on 8th Sept 2021* ----------------------------------------------------------------------/

    /*Commented on 8th Sept 2021*/
   /* public class UploadDataServer extends AsyncTask<String, String, String> {

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

            // pd = new ProgressDialog(context);
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));


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
                pd.dismiss();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();

            }
            catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                String resultout=result.trim();
                pd.dismiss();
                if(resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if(Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='"+ImageName+"'");
                        pd.dismiss();
                    }
                }
                else
                { pd.dismiss();
                    msclass.showMessage(result+"--E");

                }

                pd.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }*/


}



