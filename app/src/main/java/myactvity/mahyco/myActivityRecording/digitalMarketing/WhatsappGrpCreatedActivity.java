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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.UserRegister;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Function;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;


public class WhatsappGrpCreatedActivity extends AppCompatActivity {
    private static final String TAG = "WHATSAPPGROUP";
    SearchableSpinner spRegion, spTBM, spGroupCategory, spMDO;
    private String region, tbm, mdo, category,createdBy;
    Button btnSubmit;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etGroupName, etTotalMember;
    String[]  categoryArray;
    RadioGroup radGroupActivity;
    RadioButton radTBM, radMDO;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    LinearLayout llMDO;
    private long mLastClickTime = 0;
    String userCode;
    Config config;
    Prefs mPref;
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
        setContentView(R.layout.activity_whatsapp_grp_created);


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
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);
        etGroupName = findViewById(R.id.etGroupName);
        etTotalMember = findViewById(R.id.etTotalMember);
        llMDO = findViewById(R.id.llMDO);
        radMDO = findViewById(R.id.radMDO);
        radTBM = findViewById(R.id.radTBM);
        radGroupActivity = findViewById(R.id.radGroupActivity);
        spGroupCategory = (SearchableSpinner) findViewById(R.id.spGroupCategory);
        spMDO = (SearchableSpinner) findViewById(R.id.spMDO);
        spTBM = (SearchableSpinner) findViewById(R.id.spTBM);
        spRegion = (SearchableSpinner) findViewById(R.id.spRegion);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);

        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    region = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                bindTBM(region);
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
                bindMDO(tbm);


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
                    case R.id.radTBM:

                        llMDO.setVisibility(View.GONE);
                        radMDO.setChecked(false);
                        break;
                    case R.id.radMDO:
                        llMDO.setVisibility(View.VISIBLE);
                        radTBM.setChecked(false);
                        break;
                }
            }
        });


        bindRegion();
        bindGroupCategory();
        onSubmitBtn();

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(WhatsappGrpCreatedActivity.this);

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

    private boolean validation() {

        if (spRegion.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Region");
            return false;
        }
        if (spTBM.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select TBM");
            return false;
        }

        if (radMDO.isChecked()) {
            if (spMDO.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select MDO");
                return false;
            }
            createdBy = radMDO.getText().toString();

        }else {
            createdBy= radTBM.getText().toString();
        }


        if (spGroupCategory.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Group Category");
            return false;
        }

        if (etGroupName.getText().length() == 0) {
            msclass.showMessage("Please enter group name");
            return false;
        }
        if (etTotalMember.getText().length() == 0) {
            msclass.showMessage("Please enter total member");
            return false;
        }

        return true;
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

        JSONObject distributorCall = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            distributorCall.put("id", "0");
            distributorCall.put("userCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
            distributorCall.put("region", spRegion.getSelectedItem());
            distributorCall.put("groupCreatedBy", createdBy);
            distributorCall.put("tbm", spTBM.getSelectedItem());
            distributorCall.put("mdo", spMDO.getSelectedItem());
            distributorCall.put("whatsappGroupCategory", spGroupCategory.getSelectedItem());
            distributorCall.put("WhatsappGroupName", etGroupName.getText());
            distributorCall.put("totalMember", etTotalMember.getText() );

            distributorCall.put("entryDt",   Function.getCurrentDate() );

            //  jsonArray.put(distributorCall);
            requestParams.put("Table", distributorCall);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (CommonUtil.addGTVActivity(context, "29", "Whatsapp group creation", cordinates, createdBy+" "+spRegion.getSelectedItem(),"GTV")) {
            // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
        }


        Log.d("whatsappGroupData", requestParams.toString());
        new WhatsappGrpCreatedActivity.WhatsappGroupApiCall("whatsappGroupData", requestParams).execute();

    }



    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class WhatsappGroupApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public WhatsappGroupApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadVOPFData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WhatsappGrpCreatedActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(WhatsappGrpCreatedActivity.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(WhatsappGrpCreatedActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                progressBarVisibility();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                }else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(WhatsappGrpCreatedActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            progressBarVisibility();
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
    private String uploadVOPFData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.WHATSAPP_GROUP_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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



    private void bindRegion() {

        try {
            spRegion.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct Region  FROM RbmMaster where tbmCode = '"+ userCode +"' ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                tbmlist.add(0, new GeneralMaster("SELECT REGION",
                        "SELECT REGION"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    tbmlist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, tbmlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRegion.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }


    private void bindTBM(String region) {

        try {
            spTBM.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct tbmCode,tbmDesc  FROM MdoTbmMaster where tbmCode = '"+ userCode +"' AND( Region ='" + region + "')    order by tbmDesc asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                tbmlist.add(0, new GeneralMaster("SELECT TBM",
                        "SELECT TBM"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    tbmlist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, tbmlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTBM.setAdapter(adapter);
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
                List<GeneralMaster> mdolist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mdoCode,mdoDesc  FROM MdoTbmMaster where  tbmDesc= '" + tbm + "' AND( mdoCode!='" + "NA" + "') order by mdoDesc asc  ";

                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                mdolist.add(0,new GeneralMaster("SELECT MDO",
                        "SELECT MDO"));


                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    mdolist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, mdolist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMDO.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    private void bindGroupCategory() {


        try {
            spGroupCategory.setAdapter(null);

            String str = null;
            try {
                categoryArray = getResources().getStringArray(R.array.grpCategory);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spGroupCategory.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


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
