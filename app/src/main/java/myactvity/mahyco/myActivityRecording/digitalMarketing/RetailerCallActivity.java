package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.UserRegister;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Function;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

public class RetailerCallActivity extends AppCompatActivity {

    private static final String TAG = "RETAILERCALL";
    SearchableSpinner spState, spDist, spTaluka, spRetailer,spRetailerResponse;
    MultiSelectionSpinner spProductName,spCropType;
    CheckBox chkBoxProduct,chkBoxOther;
    private String state, dist, croptype, taluka,retailerName, productName, calltypeProduct="", calltypeOther="";
    Button btnSubmit, btnAdd;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText   etFirmName, etRetailerName, etRetailerMob, etCallSummary;
    String[]  ratingArray;
    TextView  lblheader;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    List<GeneralMaster> retailerList;
    LinearLayout newRetailer;
    private long mLastClickTime = 0;
    String userCode;
    Config config;
    Prefs mPref;
    private String pkRetailerMobileNumber;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    private FusedLocationProviderClient fusedLocationClient;
    double lati;
    double longi;
    String cordinates;
    String address = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_call_activity);

        initUI();
        updateLocation();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {
        mPref = Prefs.with(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);

        lblheader = (TextView) findViewById(R.id.lblheader);
        etCallSummary = findViewById(R.id.etCallSummary);
         spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spRetailer = (SearchableSpinner) findViewById(R.id.spRetailer);
        spRetailerResponse = (SearchableSpinner) findViewById(R.id.spRetailerResponse);

        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        chkBoxProduct =  findViewById(R.id.chkBoxProduct);
        chkBoxOther =  findViewById(R.id.chkBoxOther);
        etRetailerMob =  findViewById(R.id.etRetailerMob);
        etRetailerName =  findViewById(R.id.etRetailerName);
        etFirmName =  findViewById(R.id.etFirmName);
        newRetailer =  findViewById(R.id.newRetailer);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);

        Function.bindState(spState,mDatabase, this,msclass);
        Function.bindcroptype(spCropType, null,"C",this, mDatabase,msclass);
        Function.bindProductName(spProductName, null,"", this,mDatabase,msclass);
         bindRetailerResponse();

        onSubmitBtn();
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Function.bindDist(state,spDist,mDatabase,getBaseContext(),msclass);
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
              Function.bindTaluka(dist,spTaluka,getBaseContext(),mDatabase, msclass);
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
                bindRetailer();
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spRetailer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    retailerName = gm.toString();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    pkRetailerMobileNumber = gm.Code().trim();

                    if(retailerName.contains("RETAILER DETAILS NOT FOUND")){
                        newRetailer.setVisibility(View.VISIBLE);

                    }else
                    { newRetailer.setVisibility(View.GONE);
                        etRetailerName.setText("");
                        etRetailerMob.setText("");
                        etFirmName.setText("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spProductName.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                productName = String.valueOf(strings);

            }
        });

        onCropItemSelected();
        onAddNewRetailerDetails();
    }

    private void onAddNewRetailerDetails() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateNewRetailer()) {

                    relPRogress.setVisibility(View.VISIBLE);
                    uploadNewRetailer();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    container.setEnabled(false);
                    container.setClickable(false);

                }
            }
        });

    }

    private void uploadNewRetailer() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {


                        JSONObject requestParams = new JSONObject();

                        JSONObject retailerCall = new JSONObject();

                        try {

                            retailerCall.put("id", "0");
                            retailerCall.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
                            retailerCall.put("State", spState.getSelectedItem());
                            retailerCall.put("District", spDist.getSelectedItem());
                            retailerCall.put("Taluka", spTaluka.getSelectedItem());
                            retailerCall.put("Name", etRetailerName.getText().toString().toUpperCase());
                            retailerCall.put("FirmName", etFirmName.getText().toString().toUpperCase());
                            retailerCall.put("MobileNumber", etRetailerMob.getText() );
                            retailerCall.put("FormType", "RetailerCall" );


                            retailerCall.put("entryDt",  Function.getCurrentDate() );
                            requestParams.put("Table", retailerCall);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("NewRetailerData", requestParams.toString());
                        // progressBarVisibility();

                        new  AddNewRetailer("NewRetailerData", requestParams).execute();

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
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class AddNewRetailer extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public AddNewRetailer(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadNewRetailerData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Added Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                             //   Config.refreshActivity(RetailerCallActivity.this);
                                bindNewRetailer();
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            progressBarVisibility();
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

    public void redirecttoRegisterActivity(String result )
    {
        if (result.toLowerCase().contains("authorization")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

                   /* Intent intent = new Intent(UploadData.this, UserRegister.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    editor.putString("UserID", null);
                    editor.commit();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    /**
     * <P>Method to upload the distributor survey data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadNewRetailerData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.NEW_RETAILER_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }


    private void bindNewRetailer() {

        try {
            spRetailer.setAdapter(null);

            String str = null;
            try {

                boolean res = mDatabase.InsertMDO_NEWRetailer(state.toUpperCase(), "Retailer",userCode,"",
                        dist.toUpperCase(), taluka.toUpperCase(),etRetailerMob.getText().toString(),etFirmName.getText().toString().toUpperCase() + "(" + etRetailerMob.getText().toString()+ ")" ,etRetailerName.getText().toString().toUpperCase(),etRetailerMob.getText().toString());
            if(res){
                retailerList = new ArrayList<GeneralMaster>();

                String searchQuery = "SELECT distinct mobileno,name,firmname  " +
                        "FROM MDO_tagRetailerList where taluka='" + taluka.trim().toUpperCase() + "' ";

                retailerList.add(new GeneralMaster("SELECT RETAILER (BY NAME/ MOBILE NUMBER)",
                        "SELECT RETAILER (BY NAME/ MOBILE NUMBER)"));

                  Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

                retailerList.add(1,new GeneralMaster("RETAILER DETAILS NOT FOUND",
                        "RETAILER DETAILS NOT FOUND"));
//
//                retailerList.add(2, new GeneralMaster(etRetailerName.getText().toString().toUpperCase() + "(" + etRetailerMob.getText() +")",
//                        etRetailerName.getText().toString().toUpperCase() + "(" + etRetailerMob.getText() + ")"));

                if (cursor != null && cursor.getCount() > 0) {
                    newRetailer.setVisibility(View.GONE);

                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(1).toUpperCase() + ", " + cursor.getString(2).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }


                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailer.setAdapter(adapter);
                spRetailer.setSelection(retailerList.size()-1);

            }



            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }




    private boolean validateNewRetailer() {

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

        if (etFirmName.getText().length() == 0) {
            msclass.showMessage("Please enter firm name");
            return false;
        }if (etRetailerName.getText().length() == 0) {
            msclass.showMessage("Please enter retailer name");
            return false;
        }
          if (etRetailerMob.getText().length() == 0) {
            msclass.showMessage("Please enter mobile no");
            return false;
        }

        return  true;
    }

    private void onSubmitBtn() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);

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

    private void dowork() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        uploadData();
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

    private void uploadData() {


        JSONObject requestParams = new JSONObject();

        JSONObject retailerCall = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            retailerCall.put("id", "0");
            retailerCall.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
            retailerCall.put("tbm","");
            retailerCall.put("State", spState.getSelectedItem());
            retailerCall.put("District", spDist.getSelectedItem());
            retailerCall.put("Taluka", spTaluka.getSelectedItem());
            retailerCall.put("RetailerName", retailerName);
            retailerCall.put("RetailerMobile", pkRetailerMobileNumber);
            retailerCall.put("CallTypeProductPromotion", calltypeProduct );
            retailerCall.put("CallTypeOtherActivity", calltypeOther );
            if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                croptype= "";
                retailerCall.put("CropType", croptype);
            }else {
                retailerCall.put("CropType", spCropType.getSelectedItem());
            }

            if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                productName="";
                retailerCall.put("ProductName", productName);
            }else {
                retailerCall.put("ProductName", spProductName.getSelectedItem());
            }
            retailerCall.put("RetailerResponse", spRetailerResponse.getSelectedItem());
            retailerCall.put("CallSummary", etCallSummary.getText());


            retailerCall.put("entryDt",   Function.getCurrentDate() );

            jsonArray.put(retailerCall);
            requestParams.put("Table", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("RetailerData", requestParams.toString());
        //progressBarVisibility();
        if (CommonUtil.addGTVActivity(context, "27", "Retailer Call", cordinates, retailerName+" "+pkRetailerMobileNumber,"GTV","0",0.0)) {
            // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
        }
        new RetailerApiCall("RetailerData", requestParams).execute();

    }


    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class RetailerApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public RetailerApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(RetailerCallActivity.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RetailerCallActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            progressBarVisibility();
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


    /**
     * <P>Method to upload the distributor survey data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.RETAILER_CALL_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }

    /**
     * <P>Manage the progressbar visibility</P>
     */
    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container.setClickable(true);
        container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void bindRetailerResponse() {

            try {
                spRetailerResponse.setAdapter(null);

                try {
                    ratingArray = getResources().getStringArray(R.array.rating3);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRetailerResponse.setAdapter(adapter);

                     } catch (Exception e) {
                    e.printStackTrace();

                }

            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }

    }

          private void onCropItemSelected() {


            spCropType.setListener(new MultiSelectionSpinner.MySpinnerListener() {

                @Override
                public void onItemClicked(int which) {

                }

                @Override
                public void selectedIndices(List<Integer> indices) {
                }

                @Override
                public void selectedStrings(List<String> strings) {
                    Log.d("which selectedSt:: ", String.valueOf(strings));
                    croptype = String.valueOf(strings);
                    Function.bindProductName(spProductName, null,croptype,getBaseContext(),mDatabase,msclass);
                }
            });

    }


    public void bindRetailer() {
        try {
            spRetailer.setAdapter(null);

            String str = null;
            try {

                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname  " +
                        "FROM MDO_tagRetailerList where taluka='" + taluka.trim().toUpperCase() + "' " +
                        "order by name asc  ";

                // String searchQuery = "SELECT distinct mobileno,name,firmname" +
                //  "  FROM MDO_tagRetailerList order by mobileno asc ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                retailerList.add(new GeneralMaster("SELECT RETAILER (BY NAME/ MOBILE NUMBER)",
                        "SELECT RETAILER (BY NAME/ MOBILE NUMBER)"));

                retailerList.add(1,new GeneralMaster("RETAILER DETAILS NOT FOUND",
                        "RETAILER DETAILS NOT FOUND"));

                if (cursor != null && cursor.getCount() > 0) {
                    newRetailer.setVisibility(View.GONE);

                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(1).toUpperCase() + ", " + cursor.getString(2).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
              }


                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailer.setAdapter(adapter);


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private boolean validateForTaluka() {
        if (spState.getSelectedItemPosition() == 0) {

            return false;
        }
        if (spDist.getSelectedItemPosition() == 0) {

            return false;
        }
        if (spTaluka.getSelectedItemPosition() == 0) {

            return false;
        }

        return  true;
    }

    /**
            * <P>Method is used to validate the input values of activity</P>
            * @return
            */
    public boolean validation() {


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
            if (spRetailer.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select retailer name");
                return false;
            }
            if(!chkBoxProduct.isChecked()&& !chkBoxOther.isChecked()){
                msclass.showMessage("Please select call type ");
                return false;
            }
            if(chkBoxProduct.isChecked())
            {
                calltypeProduct= chkBoxProduct.getText().toString();
                if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                    msclass.showMessage("Please select crop type");
                    return false;
                }
                if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

                    Utility.showAlertDialog("Info", "Please Select  Product Name", context);
                    return false;
                }
            }else
            {
                calltypeProduct="";
            }
            if(chkBoxOther.isChecked()){
                calltypeOther= chkBoxOther.getText().toString();
            }else{
                calltypeOther="";

        }

        if (spRetailerResponse.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select retailer response");
            return false;
        }

        if (etCallSummary.getText().length() == 0) {
            msclass.showMessage("Please enter call summary");
            return false;
        }

        return true;
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

}
