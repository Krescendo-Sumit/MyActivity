package myactvity.mahyco;

import android.annotation.SuppressLint;
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
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.AddFarmerListAdapter;
import myactvity.mahyco.helper.AddFarmerListModel;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.PurchaseListModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class PurchaseListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback,
        IPickResult,View.OnClickListener {
    SearchableSpinner spState, spDist, spMarketPlace, spTaluka;
    MultiSelectionSpinner spRetailerDetails;
    Button btnSubmit, btnTakePhoto, btnAddFarmer;
    private ImageView ivImage;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    private String state, dist, taluka, talukaName, retailerDetails, retailerNumber;
    Intent intent;
    private Context context;
    private int imageselect;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
    Dialog dialog;
    Button btnAddDialog;
    Button btnSaveClose;
    int farmerNumber = 0;
    String villageType = "focussed";
    ArrayList<AddFarmerListModel> mList = new ArrayList<>();
    File photoFile = null;
    ImageView imgBtnGps;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "VISITPHOTO";
    public String Imagepath1 = "";
    TextView lblheader, tvCordinates, tvAddress;
    String cordinates="";
    String address="";
    String cordinatesmsg = "ADDRESS TAG : *";
    private GoogleApiClient googleApiClient;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS = 101;
    double lati;
    double longi;
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean fusedlocationRecieved;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    private static final String TAG = "PurchaseListActivity";
    List<GeneralMaster> retailerList;
    ArrayList<PurchaseListModel> farmerlist;
    Config config;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    String userCode;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private long mLastClickTime = 0;
    ScrollView container;
  //  String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    String SERVER = "https://packhouse.mahyco.com/api/postSeason/purchaseListData";
    private Handler handler = new Handler();
    int farmerCount = 0;
    Prefs mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        mPref = Prefs.with(this);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spMarketPlace = (SearchableSpinner) findViewById(R.id.spMarketPlace);
        spRetailerDetails = (MultiSelectionSpinner) findViewById(R.id.spRetailerDetails);
        btnAddFarmer = (Button) findViewById(R.id.btnAddFarmer);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        mDatabase = SqliteDatabase.getInstance(this);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        imgBtnGps = (ImageView) findViewById(R.id.imgBtnGps);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        msclass = new Messageclass(this);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode = pref.getString("UserID", null);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        context = this;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addfarmer_purchase_list);
        bindState();

        farmerlist = new ArrayList<>();

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
                    talukaName = gm.Desc().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                bindMarketPlace(talukaName);
                bindRetailerDetails(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spMarketPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spRetailerDetails.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {

            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ",String.valueOf(strings));
                if(String.valueOf(strings).contains("NEW RETAILER (TAG THE RETAILER)")) {

                    Intent intent;
                    editor.putString("RetailerCallActivity","PurchaseList");
                    editor.commit();
                    intent = new Intent(PurchaseListActivity.this, RetailerandDistributorTag.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Log.d("Other:: ", String.valueOf(strings));
                }else{
                    Log.d("All indices",String.valueOf(strings));
                }
            }
        });


        btnAddFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validationPurchaseList()) {
                    openDialog();
                }

            }
        });
        btnTakePhoto.setOnClickListener(this);
        /*btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    //  ActivityCompat.requestPermissions(context, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                try
                {
                imageselect=1;
                // selectImage();

                        selectImage();

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        }); */

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        startFusedLocationService();
        if (ivImage.getDrawable() != null) {
            ivImage.setVisibility(View.VISIBLE);
            // crdImage.setVisibility(View.VISIBLE);
        } else {
            ivImage.setVisibility(View.GONE);
            //  crdImage.setVisibility(View.GONE);
        }
        // bindRetailerDetails(taluka);

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

    // Comparator for Ascending Order
    public static Comparator<GeneralMaster> StringAscComparator = new Comparator<GeneralMaster>() {

        public int compare(GeneralMaster app1, GeneralMaster app2) {

            String stringName1 = app1.toString();
            String stringName2 = app2.toString();

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };


    public void bindRetailerDetails(String taluka) {

        try {
            String str = null;
            try {
                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname " +
                        " FROM MDO_tagRetailerList " +
                        "where taluka='" + taluka.trim().toUpperCase() + "' order by name asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                getRetailerArrayList( searchQuery);
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
              //  spRetailerDetails.setAdapter(adapter);
                //spMobNumber.setSelected(false);  // must
                // spMobNumber.setSelection(0, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }



    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     * @param searchQuery
     */
    private void getRetailerArrayList(String searchQuery) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 2];
            array[0] = "SELECT RETAILER";
            array[1]="NEW RETAILER (TAG THE RETAILER)";

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 2] = jObject.getString("name").toUpperCase() +", " + jObject.getString("firmname").toUpperCase();
            }
           // array[jArray.length()+1]= "OTHER";
            if (array.length > 0) {
                spRetailerDetails.setItems(array);
                spRetailerDetails.hasNoneOption(true);
                spRetailerDetails.hasRetailerOption(true);
                spRetailerDetails.setSelection(new int[]{0});
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }
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

    public void bindMarketPlace(String taluka) {

        try {
            spMarketPlace.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> mobilelist = new ArrayList<GeneralMaster>();

                //  String searchQuery = "SELECT distinct marketPlace FROM RetailerDetailsMaster where taluka='" + taluka + "' order by marketPlace";
                String searchQuery = "SELECT distinct marketPlace FROM MDO_tagRetailerList where taluka='" + taluka + "' order by marketPlace";
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
                spMarketPlace.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void openDialog() {

        try {
            final SearchableSpinner spVillage = (SearchableSpinner) dialog.findViewById(R.id.spVillage);
            final SearchableSpinner spTaluka = (SearchableSpinner) dialog.findViewById(R.id.spTaluka);
            recDemoList = (RecyclerView) dialog.findViewById(R.id.recDemoList);
            final TextView txtFarmerCount = (TextView) dialog.findViewById(R.id.txtFarmerCount);
            final TextView txtVillageCount = (TextView) dialog.findViewById(R.id.txtVillageCount);

            layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recDemoList.setLayoutManager(layoutManager);
            farmerCount = 0;

            lblheader = (TextView) dialog.findViewById(R.id.lblheader);
            final TextView noDataText = (TextView) dialog.findViewById(R.id.noDataText);
            TextView clearSearch = (TextView) dialog.findViewById(R.id.clearSearch);
            final EditText etFarmerNo = (EditText) dialog.findViewById(R.id.etFarmerNo);
            //  Button decrementFarmers = (Button) dialog.findViewById(R.id.decrementFarmers);
            //  Button incrementFarmers = (Button) dialog.findViewById(R.id.incrementFarmers);
            btnAddDialog = (Button) dialog.findViewById(R.id.btnAdd);
            btnSaveClose = (Button) dialog.findViewById(R.id.btnSaveClose);
            RadioGroup radGroup = (RadioGroup) dialog.findViewById(R.id.radGroup);
            final RadioButton radFocused = (RadioButton) dialog.findViewById(R.id.radFocused);
            final RadioButton radOther = (RadioButton) dialog.findViewById(R.id.radOther);
            final CardView cardTaluka = (CardView) dialog.findViewById(R.id.cardTaluka);
            bindFocussedVillage(spVillage);
            bindTaluka(spTaluka);
            dialog.setCancelable(true);

            // if button is clicked, close the custom dialog
            btnAddDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());


                    PurchaseListModel purchaseListModel = new PurchaseListModel();


                    if (spVillage.getSelectedItemPosition() != 0 && etFarmerNo.getText().toString().length() != 0) {

                        if (farmerlist != null) {
                            if (farmerlist.size() > 0) {
                                for (int i = 0; i < farmerlist.size(); i++) {
                                    if (!farmerlist.get(i).getVillageName().contains(spVillage.getSelectedItem().toString())) {
                                        purchaseListModel.setVillageName(spVillage.getSelectedItem().toString());
                                        purchaseListModel.setFarmerCount(etFarmerNo.getText().toString());
                                        purchaseListModel.setUniqueID(config.getvalue(spVillage)+"_" + timeStamp);
                                       // purchaseListModel.setUniqueID(spVillage.getSelectedItem().toString() + timeStamp);

                                        purchaseListModel.setVillageType(villageType);


                                    } else {
                                        Toast.makeText(context, "Already Existed", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                farmerCount = Integer.valueOf(etFarmerNo.getText().toString()) + farmerCount;
                                farmerlist.add(purchaseListModel);
                                txtFarmerCount.setText(String.valueOf(farmerCount));
                                txtVillageCount.setText(String.valueOf(farmerlist.size()));
                                Log.d("farmerCount if: ", String.valueOf(farmerCount));
                                etFarmerNo.setText("");
                                spVillage.setSelection(0);
                                spTaluka.setSelection(0);

                            } else {
                                purchaseListModel.setVillageName(spVillage.getSelectedItem().toString());
                                purchaseListModel.setFarmerCount(etFarmerNo.getText().toString());
                                //purchaseListModel.setUniqueID(spVillage.getSelectedItem().toString() + timeStamp);
                                purchaseListModel.setUniqueID(config.getvalue(spVillage)+"_" + timeStamp);

                                purchaseListModel.setVillageType(villageType);
                                farmerCount = Integer.valueOf(etFarmerNo.getText().toString()) + farmerCount;
                                farmerlist.add(purchaseListModel);
                                txtFarmerCount.setText(String.valueOf(farmerCount));
                                txtVillageCount.setText(String.valueOf(farmerlist.size()));
                                Log.d("farmerCount else: ", String.valueOf(farmerCount));
                                etFarmerNo.setText("");
                                spVillage.setSelection(0);
                                spTaluka.setSelection(0);
                            }
                            createAndAddData();
                        }

                    } else {
                        msclass.showMessage("Please select village and Farmers");


                    }
                }
            });

            dialog.show();


            btnSaveClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (spTaluka.getSelectedItemPosition() != 0 || spVillage.getSelectedItemPosition() != 0 || etFarmerNo.getText().toString().length() != 0) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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

                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {
                        dialog.dismiss();
                    }


                }
            });
//
            spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                    try {
                        taluka = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (!villageType.isEmpty()) {

                        if (villageType.contains("other")) {
                            bindVillage(spVillage, taluka);
                        } else {
                            bindFocussedVillage(spVillage);
                        }
                    } else {

                        Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();

                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (checkedId) {
                        case R.id.radFocused:

                            if (radFocused.isChecked()) {
                                villageType = "focussed";
                                bindFocussedVillage(spVillage);
                                cardTaluka.setVisibility(View.GONE);

                            } else {
                                villageType = "other";
                                bindTaluka(spTaluka);
                                cardTaluka.setVisibility(View.VISIBLE);

                            }
                            createAndAddData();

                            radOther.setChecked(false);
                            break;
                        case R.id.radOther:
                            if (radOther.isChecked()) {
                                villageType = "other";
                                bindTaluka(spTaluka);
                                cardTaluka.setVisibility(View.VISIBLE);
                            } else {
                                villageType = "focussed";
                                bindFocussedVillage(spVillage);
                                cardTaluka.setVisibility(View.GONE);


                            }
                            createAndAddData();

                            break;

                    }
                    Log.d("Villagetype", villageType);
                }


            });


        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void bindTaluka(SearchableSpinner spTaluka) {


        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster  order by  taluka";
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
                        (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
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

    private void bindVillage(SearchableSpinner spVillage, String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster" +
                    " where taluka='" + taluka + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT OTHER VILLAGE",
                    "SELECT OTHER VILLAGE"));

            cursor = mDatabase.getReadableDatabase().

                    rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }

    public void bindFocussedVillage(SearchableSpinner spVillage) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                    "SELECT FOCUSED VILLAGE"));
            cursor = mDatabase.getReadableDatabase().
                    rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }
    }

    public void createAndAddData() {

        try {
            if (villageType.contains("other")) {
                ArrayList<PurchaseListModel> otherVillageList = new ArrayList<>();
                for (int i = 0; i < farmerlist.size(); i++) {
                    if (farmerlist.get(i).getVillageType().contains("other")) {
                        otherVillageList.add(farmerlist.get(i));
                    }
                }
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListAdapter(context, otherVillageList));

            } else if (villageType.contains("focussed")) {
                ArrayList<PurchaseListModel> focussedList = new ArrayList<>();

                for (int i = 0; i < farmerlist.size(); i++) {
                    if (farmerlist.get(i).getVillageType().contains("focussed")) {
                        focussedList.add(farmerlist.get(i));
                    }
                }
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListAdapter(context, focussedList));
//            } else {
//                recDemoList.setLayoutManager(new LinearLayoutManager(context));
//                recDemoList.setAdapter(new AddFarmerListAdapter(context, farmerlist));
            }
            // } else {adapterMDO.notifyDataSetChanged(); }
            recDemoList.setLayoutManager(new LinearLayoutManager(context));
            recDemoList.setAdapter(new AddFarmerListAdapter(context, farmerlist));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public boolean validation() {

        if (!validationPurchaseList()) {
            return false;
        }

        if (farmerlist.size() == 0) {
            msclass.showMessage("Please create farmer list");
            return false;
        }

        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please click farmer list photo");
            return false;
        }
        /*if (!isAlreadydone(retailerNumber)) {
            Utility.showAlertDialog("Info", "This entry already exists", context);
            return false;
        }*/
        return true;
    }

    public boolean validationPurchaseList() {

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
                    || spRetailerDetails.getSelectedItem().toString().equalsIgnoreCase("NEW RETAILER (TAG THE RETAILER)")) {

            Utility.showAlertDialog("Info", "Please Select  Retailer Name", context);
            return false;
        }
        return true;
    }
    //if mob no already exist
    private boolean isAlreadydone(String retailerNumber) {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExistsPurchaseListData(retailerNumber);

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakePhoto:
                imageselect=1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
           /* case R.id.btnSuccessStoryPhoto:
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
                break;*/

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
          /*  if (imageselect == 2)
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
            }*/

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
           /* if (imageselect == 2) {
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
            }*/

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }
    } */

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

    }

    private void onCaptureImageResult(Intent data) {

        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize =2;
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // this only get capture photo
                    //************
                    Date entrydate = new Date();
                    String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
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

        }
        catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);

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
    public void onLocationChanged(Location arg0) {

        try {
            Log.d(TAG, "onLocation arg0: " + String.valueOf(arg0));
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
            tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
            Log.d(TAG, "onlocation" + cordinates);

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }
    }

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            if (config.NetworkConnection()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    address = addresses.get(0).getAddressLine(0);
                    if (checkImageResource(this, imgBtnGps, R.drawable.ic_location_on)) {
                        tvAddress.setText(address + "\n" + cordinates);
                        tvCordinates.setText(cordinatesmsg + "\n" + cordinates);
                    } else {

                        tvAddress.setText(address + "\n" + cordinates);
                        tvCordinates.setText(cordinatesmsg + "\n" + cordinates);

                    }
                }
            }else {
                tvAddress.setText(cordinates);

            }
            } catch(Exception e){
                e.printStackTrace();
                Log.w("My", "Canont get Address!");
            }

        return strAdd;
    }


    //////////////////////////////
    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {
        String state = spState.getSelectedItem().toString();
        String district = "";
        String taluka = "";
        String marketPlace = "";

        district = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        marketPlace = spMarketPlace.getSelectedItem().toString();
        String retailerDetail = spRetailerDetails.getSelectedItem().toString();
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
        }
        Log.d("LocationDatasaveToDb", cordinates);
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(farmerlist).getAsJsonArray();
        String finalVillageJSON = myCustomArray.toString();
        Log.d("finalVillageJSON:: ", finalVillageJSON);
        String isSynced = "0";
        String farmerListPhotoStatus = "0";
        Date entrydate = new Date();
        final String farmerListPhoto;
        farmerListPhoto = Imagepath1;
        final String farmerListPhotoName =AppConstant.Imagename;// "PurchaseListFarmerListPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        boolean fl = mDatabase.insertPurchaseListData(userCode, state, district, taluka, marketPlace, retailerDetail, retailerNumber,
                taggedCordinates +" "+ taggedAddress, taggedCordinates, finalVillageJSON,
                farmerListPhotoName, farmerListPhoto, farmerListPhotoStatus, isSynced);

        if (fl) {
           // msclass.showMessage("data saved successfully.");

            uploadData("PurchaseListData");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }


    public void uploadData(String PurchaseListData) {
        String str = null;
        /*if (config.NetworkConnection()) {

            try {
                new UploadPurchaseListDataServer(PurchaseListData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else */ {
            AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Config.refreshActivity(PurchaseListActivity.this);
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


    public String uploadPurchaseListData(String LivePlantDisplayVillage) {
        String str = "";
        int action = 1;

        String searchQuery = "select  *  from PurchaseListData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");

                    jsonArray.getJSONObject(i).put("farmerListPhoto",  mDatabase.getImageDatadetail(farmerListPhoto));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    JSONArray jsonArray1 = new JSONArray(jsonArray.getJSONObject(i).getString("finalVillageJSON"));
                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("PurchaseListData", jsonObject.toString());
                    str = syncPurchaseSingleImage(LivePlantDisplayVillage, SERVER, jsonObject, farmerListPhotoName, farmerListPhoto);
                    handlePurchaseImageSyncResponse("PurchaseListData", str,id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadPurchaseListDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadPurchaseListDataServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadPurchaseListData("PurchaseListData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(PurchaseListActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);
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
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseListActivity.this);
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
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public synchronized String syncPurchaseSingleImage(String function, String urls, JSONObject jsonObject, String farmerListPhotoName,
                                                       String farmerListPhoto) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }


    public void handlePurchaseImageSyncResponse(String function, String resultout,String id) throws JSONException {
        if (function.equals("PurchaseListData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updatePurchaseListData("0", "1", "1",id );


                } else {

                }
            }
        }

        Log.d("PurchaseListData", "PurchaseListData: " + resultout);
    }
}
