package myactvity.mahyco.myActivityRecording.preSeasonActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

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

import myactvity.mahyco.R;
import myactvity.mahyco.RetailerandDistributorTag;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.AddToolPOPDisplayAdapter;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.CustomSearchableSpinner;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.POPDisplayModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;


public class POPDisplayActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback, IPickResult,View.OnClickListener {

    private static final String TAG ="POPDisplay" ;
    Prefs mPref;
    private Context context;
    public SqliteDatabase mDatabase;
    Dialog dialog;
    Button btnAddTools, btnPhoto1, btnPhoto2, btnPhoto3, btnAdd4, btnAdd3, btnAdd2, btnAdd, btnSaveClose, btnSubmit;
    SearchableSpinner spState, spDist, spTaluka, spTool, spCrop, spCrop2, spCrop3, spCrop4;
    SearchableSpinner spProduct, spProduct2, spProduct3, spProduct4;
    CustomSearchableSpinner spRetailerDetails;
    EditText etMandiName;
    private String state, dist, taluka, retailerDetails;
    String croptypePopup = "", ToolNamePopup = "";
    public Messageclass msclass;
    List<GeneralMaster> retailerList;
    public String search = "";
    int imageselect;
    File photoFile = null;
    File photoFile2 = null;
    File photoFile3 = null;
    String userCode, Imagepath1 = "", Imagepath2 = "", Imagepath3 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage, ivImagePhoto2, ivImagePhoto3;
    LinearLayout llAddPoster, llAddLeaflet, llAddBunting, llOther;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
    ArrayList<POPDisplayModel> addedtoolList;
    EditText etNumber, etToolName, etBuntingNo, etLeafletNo, etPosterNo;
    ProgressBar progressBar;
    ProgressDialog pDialog;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private long mLastClickTime = 0;
    String SERVER = "https://packhouse.mahyco.com/api/preseason/popDisplayData";
    Config config;
    SharedPreferences.Editor loceditor, editor;
    SharedPreferences locdata, pref;
    private static final String IMAGE_DIRECTORY_NAME = "POPDISPLAYPHOTO";

    private FusedLocationProviderClient fusedLocationClient;
    double lati;
    double longi;
    String cordinates;
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popdisplay);
        getSupportActionBar().hide();

        initUI();
        updateLocation();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        mPref = Prefs.with(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        msclass = new Messageclass(this);
        config = new Config(this); //Here the context is passing
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.add_tools_dialog);

        btnAddTools = (Button) findViewById(R.id.btnAddTools);

        btnAddTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationAddTool()){
                    openDialog();
                }

            }
        });

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spRetailerDetails = (CustomSearchableSpinner) findViewById(R.id.spRetailerDetails);
        etMandiName = (EditText) findViewById(R.id.etMandiName);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImagePhoto2 = (ImageView) findViewById(R.id.ivImagePhoto2);
        ivImagePhoto3 = (ImageView) findViewById(R.id.ivImagePhoto3);
        btnPhoto1 = (Button) findViewById(R.id.btnPhoto1);
        btnPhoto2 = (Button) findViewById(R.id.btnPhoto2);
        btnPhoto3 = (Button) findViewById(R.id.btnPhoto3);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
       // userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode =  pref.getString("UserID", null);
        addedtoolList = new ArrayList<>();

        bindSpinners();
        //manageShopPhoto();
        btnPhoto1.setOnClickListener(this);
        btnPhoto2.setOnClickListener(this);
        btnPhoto3.setOnClickListener(this);
        onSubmitBtnClicked();

    }

    private void onSubmitBtnClicked() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(POPDisplayActivity.this);

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

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });

    }

    /**
     * <P>Method to handle the data saving to db work</P>
     */
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
     * <P>Method to save the data to db</P>
     */
    public void saveToDb() {

        String state = "";
        String district = "";
        String taluka = "";
        String retailerDetails = "";
        String marketplace = "";
        state = spState.getSelectedItem().toString();
        district = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        retailerDetails = spRetailerDetails.getSelectedItem().toString();

        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(addedtoolList).getAsJsonArray();
        String finalToolJSON = myCustomArray.toString();

        String isSynced = "0";
        String shopPhoto1Status = "0", shopPhoto2Status = "0", shopPhoto3Status = "0";

        Date entrydate = new Date();
        final String shopImg1Path, shopImg2Path, shopImg3Path;
        shopImg1Path = Imagepath1;
        shopImg2Path = Imagepath2;
        shopImg3Path = Imagepath3;

        marketplace = etMandiName.getText().toString();

        final String shopPhoto1Name = AppConstant.Imagename.isEmpty() ? "" :AppConstant.Imagename;
        final String shopPhoto2Name = AppConstant.Imagename2.isEmpty() ? "" : AppConstant.Imagename2;
        final String shopPhoto3Name = AppConstant.Imagename3.isEmpty() ? "" : AppConstant.Imagename3;
        boolean fl = mDatabase.insertPopDisplayData(userCode, state, district, taluka, marketplace,
                retailerDetails, finalToolJSON, shopPhoto1Name, shopImg1Path,
                shopPhoto1Status, shopPhoto2Name, shopImg2Path, shopPhoto2Status, shopPhoto3Name, shopImg3Path,
                shopPhoto3Status, isSynced);

        if (fl) {

            if (CommonUtil.addGTVActivity(context, "31", "POP display", cordinates, retailerDetails,"Market","0")) {
                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }

            uploadData("PopDisplayData");
            //msclass.showMessage("data saved successfully.");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

        }

    }

    private void uploadData(String popDisplayData) {
        String str = null;
        /*if (config.NetworkConnection()) {

            try {
                new UploadPopDisplayDataServer(popDisplayData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else*/ {
            AlertDialog.Builder builder = new AlertDialog.Builder(POPDisplayActivity.this);
            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "data saved", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(POPDisplayActivity.this, POPDisplayActivity.class);
//                    startActivity(intent);
                    Config.refreshActivity(POPDisplayActivity.this);
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
    }


    //AsyncTask Class for api batch code upload call

    private class UploadPopDisplayDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadPopDisplayDataServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadPopDisplayData("PopDisplayData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(POPDisplayActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(POPDisplayActivity.this);
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
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(POPDisplayActivity.this);
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

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(POPDisplayActivity.this);
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

    private String uploadPopDisplayData(String popDisplayData) {
        String str = "";
        int action = 1;
        String searchQuery = "select * from PopDisplayData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    // String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String photoOneImgPath = jsonArray.getJSONObject(i).getString("photoOneImgPath");

                    jsonArray.getJSONObject(i).put("photoOneImgPath", mDatabase.getImageDatadetail(photoOneImgPath));

                    // String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String photoTwoImgPath = jsonArray.getJSONObject(i).getString("photoTwoImgPath");

                    jsonArray.getJSONObject(i).put("photoTwoImgPath", mDatabase.getImageDatadetail(photoTwoImgPath));

                    // String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    String photoThreeImgPath = jsonArray.getJSONObject(i).getString("photoThreeImgPath");

                    jsonArray.getJSONObject(i).put("photoThreeImgPath", mDatabase.getImageDatadetail(photoThreeImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("PopDisplayData", jsonObject.toString());
                    str = syncPopDisplayImage(popDisplayData, SERVER, jsonObject);
                    handlePopDisplayImageSyncResponse("PopDisplayData", str,id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    private String syncPopDisplayImage(String popDisplayData, String server, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public void handlePopDisplayImageSyncResponse(String function, String resultout,String id) throws JSONException {
        if (function.equals("PopDisplayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updatePopDisplayData("0", "1", "1", "1",
                            "1",id);


                } else {

                }
            }
        }

        Log.d("PopDisplayData", "PopDisplayData: " + resultout);


    }


    /**
     * <p>Method to handle the shop photo click Listeners</p>
     */
    private void manageShopPhoto() {
        btnPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(POPDisplayActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 1;
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });

        btnPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(POPDisplayActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 2;
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });

        btnPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(POPDisplayActivity.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 3;
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });

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
    /**
     * <P>Method used to bind the spinners(state, dist, talukas)</P>
     */
    private void bindSpinners() {

        bindState();

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
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                bindRetailerDetails(taluka);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spRetailerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                Intent intent;
                try {
                    retailerDetails = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if (retailerList != null) {
                        if (position==1) {
                            editor.putString("RetailerCallActivity","POPDisplayActivity");
                            editor.commit();
                            intent = new Intent(POPDisplayActivity.this, RetailerandDistributorTag.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            // Log.d("Other:: ", String.valueOf(strings));
                        } else {
                            // Log.d("All indices", String.valueOf(strings));
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

    }

    /**
     * <P>Method to bind the state into spinner</P>
     */
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


    /**
     * <P>Method to bind Dist into spinner</P>
     */
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


    /**
     * <P>Method to bind Taluka into spinner</P>
     */
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


    /**
     * <P>Method to bind Retailer Details into spinner</P>
     */
    public void bindRetailerDetails(String taluka) {

        try {
            spRetailerDetails.setAdapter(null);
            pDialog.setMessage("Loading....");
            pDialog.show();
            String str = null;
            try {
                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname " +
                        " FROM MDO_tagRetailerList " +
                        "where taluka='" + taluka.trim().toUpperCase() + "' order by name asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                retailerList.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));
                //  getRetailerArrayList(searchQuery);
                retailerList.add(1,new GeneralMaster("NEW RETAILER (TAG THE RETAILER)",
                        "NEW RETAILER (TAG THE RETAILER)"));
                if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(2).toUpperCase() + ", " + cursor.getString(1).toUpperCase()));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                }

                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailerDetails.setAdapter(adapter);
                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }


    /**
     * <P>Method to open the add tool dialog </P>
     */
    private void openDialog() {

        try {
            spTool = (SearchableSpinner) dialog.findViewById(R.id.spTool);
            spCrop = (SearchableSpinner) dialog.findViewById(R.id.spCropType);
            spCrop2 = (SearchableSpinner) dialog.findViewById(R.id.spCropType2);
            spCrop3 = (SearchableSpinner) dialog.findViewById(R.id.spCropType3);
            spCrop4 = (SearchableSpinner) dialog.findViewById(R.id.spCropType4);

            spProduct = (SearchableSpinner) dialog.findViewById(R.id.spProductName);
            spProduct2 = (SearchableSpinner) dialog.findViewById(R.id.spProductName2);
            spProduct3 = (SearchableSpinner) dialog.findViewById(R.id.spProductName3);
            spProduct4 = (SearchableSpinner) dialog.findViewById(R.id.spProductName4);

            btnAdd4 = (Button) dialog.findViewById(R.id.btnAdd4);
            btnAdd3 = (Button) dialog.findViewById(R.id.btnAdd3);
            btnAdd2 = (Button) dialog.findViewById(R.id.btnAdd2);
            btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

            etPosterNo = (EditText) dialog.findViewById(R.id.etPosterNo);
            etLeafletNo = (EditText) dialog.findViewById(R.id.etLeafletNo);
            etBuntingNo = (EditText) dialog.findViewById(R.id.etBuntingNo);
            etToolName = (EditText) dialog.findViewById(R.id.etToolName);
            etNumber = (EditText) dialog.findViewById(R.id.etNumber);


            llAddPoster = (LinearLayout) dialog.findViewById(R.id.llAddPoster);
            llAddLeaflet = (LinearLayout) dialog.findViewById(R.id.llAddLeaflet);
            llAddBunting = (LinearLayout) dialog.findViewById(R.id.llAddBunting);
            llOther = (LinearLayout) dialog.findViewById(R.id.llOther);

            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);

            recDemoList = (RecyclerView) dialog.findViewById(R.id.recDemoList);
            layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recDemoList.setLayoutManager(layoutManager);
            String getSelectedRetailer = mPref.getString("selectedRetailer", "");
            if (getSelectedRetailer.equalsIgnoreCase(spRetailerDetails.getSelectedItem().toString())) {
                // msclass.showMessage("Already exists");
            } else {
                //msclass.showMessage("not exists");
                //  addedtoolList.removeAll(addedtoolList);
                recDemoList.setAdapter(new AddToolPOPDisplayAdapter(context, null));
                addedtoolList.removeAll(addedtoolList);
                mPref.save("selectedRetailer", spRetailerDetails.getSelectedItem().toString());
            }
            bindTool(spTool);
            onToolItemSelected(spTool);

            bindAddPosterCrop(spCrop);
            bindLeafletCrop(spCrop2);
            bindBuntingCrop(spCrop3);
            bindOtherCrop(spCrop4);

            onSaveAndCloseBtnClicked();

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();
    }

    private void onSaveAndCloseBtnClicked() {

        btnSaveClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addedtoolList.size() != 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Do you want to close the dailog ?");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface alertdialog, int which) {


                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface alertdialog, int which) {

                            alertdialog.dismiss();
                        }
                    });

                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();


                } else {
                    dialog.dismiss();
                }
            }


        });

    }

    public void myHandler(View view) {

        switch (view.getId()) {
            case R.id.btnAdd:
                addBtnClicked(1);
                break;
            case R.id.btnAdd2:
                addBtnClicked(2);
                break;
            case R.id.btnAdd3:
                addBtnClicked(3);
                break;
            case R.id.btnAdd4:
                addBtnClicked(4);
                break;

        }

    }

    private void addBtnClicked(int toolId) {
        String getSelectedRetailer = "", getSelectedTool = "", getSelectedCrop = "", getSelectedProduct = "";
        POPDisplayModel popDisplayModel = new POPDisplayModel();
        if (validationAddTool(toolId)) {
            getSelectedRetailer = mPref.getString("selectedRetailer", "");
            getSelectedTool = mPref.getString("selectedTool", "");
            getSelectedCrop = mPref.getString("selectedCrop", "");
            getSelectedProduct = mPref.getString("selectedProduct", "");
            if (addedtoolList != null) {
                if (addedtoolList.size() > 0) {
                    for (int i = 0; i < addedtoolList.size(); i++) {
                        setValuesToModel(popDisplayModel, toolId);
                    }
                    if (getSelectedRetailer.equalsIgnoreCase(spRetailerDetails.getSelectedItem().toString()) &&
                            getSelectedTool.equalsIgnoreCase(spTool.getSelectedItem().toString()) &&
                            getSelectedProduct.equalsIgnoreCase(spProduct.getSelectedItem().toString()) &&
                            getSelectedCrop.equalsIgnoreCase(spCrop.getSelectedItem().toString())
                    ) {
                        msclass.showMessage(" This entry already exists");
                    } else {
                        mPref.save("selectedRetailer", spRetailerDetails.getSelectedItem().toString());
                        mPref.save("selectedTool", spTool.getSelectedItem().toString());
                        mPref.save("selectedCrop", spCrop.getSelectedItem().toString());
                        mPref.save("selectedProduct", spProduct.getSelectedItem().toString());
                        addedtoolList.add(popDisplayModel);
                        resetValues(toolId);
                    }
                } else {
                    setValuesToModel(popDisplayModel, toolId);
                    mPref.save("selectedRetailer", spRetailerDetails.getSelectedItem().toString());
                    mPref.save("selectedTool", spTool.getSelectedItem().toString());
                    mPref.save("selectedCrop", spCrop.getSelectedItem().toString());
                    mPref.save("selectedProduct", spProduct.getSelectedItem().toString());
                    addedtoolList.add(popDisplayModel);
                    resetValues(toolId);

                }
                createAndAddData();
            }
        }
    }

    private void resetValues(int toolId) {
        switch (toolId) {
            case 1:
                spProduct.setSelection(0);
                spCrop.setSelection(0);
                //spTool.setSelection(0);
                etPosterNo.setText("");
                break;
            case 2:
                spProduct2.setSelection(0);
                spCrop2.setSelection(0);
               // spTool.setSelection(0);
                etLeafletNo.setText("");
                break;
            case 3:
                spProduct3.setSelection(0);
                spCrop3.setSelection(0);
                //spTool.setSelection(0);
                etBuntingNo.setText("");
                break;
            case 4:
                spProduct4.setSelection(0);
                spCrop4.setSelection(0);
               // spTool.setSelection(0);
                etNumber.setText("");
                etToolName.setText("");
                break;
        }
    }

    private void setValuesToModel(POPDisplayModel popDisplayModel, int toolId) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        popDisplayModel.setToolCategory(spTool.getSelectedItem().toString());
        //popDisplayModel.setUniqueID(timeStamp);

        switch (toolId) {
            case 1:
                popDisplayModel.setCropType(spCrop.getSelectedItem().toString());
                popDisplayModel.setProductName(spProduct.getSelectedItem().toString());
                popDisplayModel.setCount(etPosterNo.getText().toString());
                break;
            case 2:
                popDisplayModel.setCropType(spCrop2.getSelectedItem().toString());
                popDisplayModel.setProductName(spProduct2.getSelectedItem().toString());
                popDisplayModel.setCount(etLeafletNo.getText().toString());
                break;
            case 3:
                popDisplayModel.setCropType(spCrop3.getSelectedItem().toString());
                popDisplayModel.setProductName(spProduct3.getSelectedItem().toString());
                popDisplayModel.setCount(etBuntingNo.getText().toString());
                break;
            case 4:
                popDisplayModel.setCropType(spCrop4.getSelectedItem().toString());
                popDisplayModel.setProductName(spProduct4.getSelectedItem().toString());
                popDisplayModel.setCount(etNumber.getText().toString());
                popDisplayModel.setToolName(etToolName.getText().toString());
                break;

        }


    }

    public boolean validationAddTool(int toolId) {
        switch (toolId) {
            case 1:
                return validationPoster();

            case 2:
                return validationLeaflet();
            case 3:
                return validationBunting();
            case 4:
                return validationOther();

        }

        return true;
    }

    private boolean validationOther() {

        if (spCrop4.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProduct4.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
            msclass.showMessage("Please select product name");
            return false;
        }
        if (etToolName.getText().toString().length() == 0) {
            msclass.showMessage("Please enter tool name");
            return false;
        }

        if (etNumber.getText().toString().length() == 0) {
            msclass.showMessage("Please enter numbers");
            return false;
        }
        return true;
    }

    private boolean validationBunting() {

        if (spCrop3.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProduct3.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
            msclass.showMessage("Please select product name");
            return false;
        }
        if (etBuntingNo.getText().toString().length() == 0) {
            msclass.showMessage("Please enter no. of buntings");
            return false;
        }

        return true;

    }

    private boolean validationLeaflet() {
        if (spCrop2.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProduct2.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
            msclass.showMessage("Please select product name");
            return false;
        }
        if (etLeafletNo.getText().toString().length() == 0) {
            msclass.showMessage("Please enter no. of leaflets");
            return false;
        }

        return true;
    }

    private boolean validationPoster() {
        if (spCrop.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select crop type");
            return false;
        }
        if (spProduct.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
            msclass.showMessage("Please select product name");
            return false;
        }
        if (etPosterNo.getText().toString().length() == 0) {
            msclass.showMessage("Please enter no. of posters");
            return false;
        }

        return true;
    }


    public void createAndAddData() {

        try {
            ArrayList<POPDisplayModel> popDisplayModelArrayList = new ArrayList<>();
            for (int i = 0; i < addedtoolList.size(); i++) {
                popDisplayModelArrayList.add(addedtoolList.get(i));
            }
            recDemoList.setLayoutManager(new LinearLayoutManager(context));
            recDemoList.setAdapter(new AddToolPOPDisplayAdapter(context, popDisplayModelArrayList));


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void onToolItemSelected(SearchableSpinner spTool) {

        spTool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    ToolNamePopup = URLEncoder.encode(gm.Code().trim(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(POPDisplayActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                manageToolType(ToolNamePopup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void manageToolType(String toolNamePopup) {
        switch (toolNamePopup) {
            case "1":
                llAddPoster.setVisibility(View.VISIBLE);
                llAddLeaflet.setVisibility(View.GONE);
                llAddBunting.setVisibility(View.GONE);
                llOther.setVisibility(View.GONE);
                break;
            case "2":
                llAddPoster.setVisibility(View.GONE);
                llAddLeaflet.setVisibility(View.VISIBLE);
                llAddBunting.setVisibility(View.GONE);
                llOther.setVisibility(View.GONE);
                break;
            case "3":
                llAddPoster.setVisibility(View.GONE);
                llAddLeaflet.setVisibility(View.GONE);
                llAddBunting.setVisibility(View.VISIBLE);
                llOther.setVisibility(View.GONE);
                break;
            case "4":
                llAddPoster.setVisibility(View.GONE);
                llAddLeaflet.setVisibility(View.GONE);
                llAddBunting.setVisibility(View.GONE);
                llOther.setVisibility(View.VISIBLE);
                break;
            default:
                llAddPoster.setVisibility(View.GONE);
                llAddLeaflet.setVisibility(View.GONE);
                llAddBunting.setVisibility(View.GONE);
                llOther.setVisibility(View.GONE);
        }


    }

    private void bindOtherCrop(SearchableSpinner spCrop4) {
        bindcroptype(spCrop4, "C");

        spCrop4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptypePopup = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(POPDisplayActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProduct4, croptypePopup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void bindBuntingCrop(SearchableSpinner spCrop3) {
        bindcroptype(spCrop3, "C");

        spCrop3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptypePopup = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(POPDisplayActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProduct3, croptypePopup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void bindLeafletCrop(SearchableSpinner spCrop2) {
        bindcroptype(spCrop2, "C");

        spCrop2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptypePopup = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(POPDisplayActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProduct2, croptypePopup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spProduct2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void bindAddPosterCrop(SearchableSpinner spCrop) {

        bindcroptype(spCrop, "C");

        spCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptypePopup = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Toast.makeText(POPDisplayActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProduct, croptypePopup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void bindTool(Spinner spTool) {
        try {

            List<GeneralMaster> toolList = new ArrayList<GeneralMaster>();
            toolList.add(new GeneralMaster("SELECT TOOL", "SELECT TOOL"));
            toolList.add(1, new GeneralMaster("1", "ADD POSTER"));
            toolList.add(2, new GeneralMaster("2", "ADD LEAFLET"));
            toolList.add(3, new GeneralMaster("3", "ADD BUNTING"));
            toolList.add(4, new GeneralMaster("4", "OTHERS"));

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, toolList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTool.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void bindcroptype(Spinner spCropType, String Croptype) {
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

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void bindProductName(SearchableSpinner spProductName, String croptype) {
        //st
        try {
            spProductName.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
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
            spProductName.setAdapter(adapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <P>Validation for all fields</P>
     *
     * @return
     */
    public boolean validation() {

        if(!validationAddTool()){
            return false;
        }

        if (addedtoolList != null) {
            if (addedtoolList.size() == 0) {
                Utility.showAlertDialog("Info", "Please Add tool Details", context);
                return false;
            }
        }
        if (ivImage.getDrawable() == null && ivImagePhoto2.getDrawable() == null && ivImagePhoto3.getDrawable() == null) {
            msclass.showMessage("Please click shop image");
            return false;
        }
        return true;
    }

    public boolean validationAddTool() {
        if (spState.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select State");
            return false;
        }
        if (spDist.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select District");
            return false;
        }
        if (spTaluka.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Taluka");
            return false;
        }
        if (spRetailerDetails.getSelectedItem().toString().equalsIgnoreCase("SELECT RETAILER")
                || spRetailerDetails.getSelectedItem().toString().equalsIgnoreCase("NEW RETAILER (TAG THE RETAILER)"))
        {

            Utility.showAlertDialog("Info", "Please Select Retailer Name", context);
            return false;
        }
        if (etMandiName.getText().length() == 0) {
            msclass.showMessage("Please enter Mandi Name");
            return false;
        }

        return true;
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
                        if (imageselect == 2) {
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
                        if (imageselect == 3) {
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
            // dialog.dismiss();
        }
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
                photoFile = createImageFile4();
                if (photoFile != null) {
                    //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());
                    Uri photoURI = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
            }
            if (imageselect == 3) {
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

    private void onCaptureImageResult(Intent data) {


        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =2;
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
            Date entrydate = new Date();
            String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

            if (imageselect == 1) {
                try {

                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = "s1"+this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
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
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename2 = "s2"+this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename2);
                    // need to set commpress image path
                    Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImagePhoto2.setImageBitmap(myBitmap);

                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
            }
            if (imageselect == 3) {
                //ivImage2.setImageBitmap(thumbnail);
                try {
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename3 = "s3"+this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename3);
                    // need to set commpress image path
                    Imagepath3 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImagePhoto3.setImageBitmap(myBitmap);

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


    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect == 1) {
            ivImage.setImageBitmap(bm);
        }
        if (imageselect == 2) {
            ivImagePhoto2.setImageBitmap(bm);
        }
        if (imageselect == 3) {
            ivImagePhoto3.setImageBitmap(bm);
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPhoto1:
                imageselect=1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnPhoto2:
                imageselect=2;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnPhoto3:
                imageselect=3;
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
                ivImagePhoto2.setImageBitmap(r.getBitmap());
                if (ivImagePhoto2.getDrawable() != null) {
                    ivImagePhoto2.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImagePhoto2.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            if (imageselect == 3)
            {
                ivImagePhoto3.setImageBitmap(r.getBitmap());
                if (ivImagePhoto3.getDrawable() != null) {
                    ivImagePhoto3.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImagePhoto3.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }

            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));

            if (imageselect == 1) {
                AppConstant.Imagename = "Crop" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 2) {
                AppConstant.Imagename = "Cropfrmlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename2);
                Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 3) {
                AppConstant.Imagename = "crpfrmrelist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename3);
                Imagepath3 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    //@Override
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

    @Override
    protected void onResume() {
        super.onResume();

        if (ivImage.getDrawable() != null) {
            ivImage.setVisibility(View.VISIBLE);

        } else {
            ivImage.setVisibility(View.GONE);
        }
        if (ivImagePhoto2.getDrawable() != null) {
            ivImagePhoto2.setVisibility(View.VISIBLE);

        } else {
            ivImagePhoto2.setVisibility(View.GONE);
        }
        if (ivImagePhoto3.getDrawable() != null) {
            ivImagePhoto3.setVisibility(View.VISIBLE);
        } else {
            ivImagePhoto3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
    public void onLocationChanged(Location location) {

    }




    public void updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object

                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    cordinates = String.valueOf(lati) + "-" + String.valueOf(longi);
                    Log.i("Coordinates", cordinates);
                    address = getCompleteAddressString(lati, longi);
                    Toast.makeText(context, "Location Latitude : " + location.getLatitude() + " Longitude :" + location.getLongitude() + " Hello :" + address, Toast.LENGTH_SHORT).show();
                    //  edGeoTagging.setText(location.getLatitude() + "," + location.getLongitude());
                }
            }
        });

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
    ///////////////////
}
