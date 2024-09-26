package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

public class CallValidation extends AppCompatActivity {
    RecyclerView recRecordList;
    LinearLayoutManager layoutManager;
    Config config;
    public Messageclass msclass;
    TextView lblheader, txtRecord, txtCount;
    private Context context;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    Prefs mPref;
    String userCode;
    SearchableSpinner spMDO, spTBM, spType;
    private String tbm, mdo, type, tbmCode, mdoCode;
    public SqliteDatabase mDatabase;
    String[] typeArray;
    TextView fromDate, toDate;
    LinearLayout llRecord;
    Button btnValidate;
    private long mLastClickTime = 0;
    private String ToDate;
    private JSONObject validateDetail;
    public  static  boolean isValidate;
    private FusedLocationProviderClient fusedLocationClient;
    double lati;
    double longi;
    String cordinates;
    String address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_validation);

        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        mPref = Prefs.with(this);
        context = this;
        config = new Config(this); //Here the context is passing
        mDatabase = SqliteDatabase.getInstance(this);
        lblheader = findViewById(R.id.lblheader);
        txtCount = findViewById(R.id.txtCount);
        txtRecord = findViewById(R.id.txtRecord);
        llRecord = findViewById(R.id.llRecord);
        btnValidate = findViewById(R.id.btnValidate);
        spMDO = (SearchableSpinner) findViewById(R.id.spMDO);
        spTBM = (SearchableSpinner) findViewById(R.id.spTBM);
        spType = (SearchableSpinner) findViewById(R.id.spType);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        msclass = new Messageclass(this);
        fromDate = findViewById(R.id.tvSelectedDate);
        toDate = findViewById(R.id.tvSelectedToDate);
        recRecordList = (RecyclerView) findViewById(R.id.recRecordList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);




        bindTBM();
        bindValidationType();
        onFromDateSelected();
        onToDateSelected();

        onValidateBtnClicked();

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    type = (String) parent.getSelectedItem();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    hideKeyboard(view);
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
                    tbm = gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    tbmCode = gm.Code();
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
                    mdoCode = gm.Code();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

           updateLocation();
    }

    private void onValidateBtnClicked() {

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    relPRogress.setVisibility(View.VISIBLE);
                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            return true;
                        }
                    });

                    validateData();

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    container.setEnabled(false);
                    container.setClickable(false);

                }
            }
        });


    }

    private boolean validation() {
        if (spTBM.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select TBM");
            return false;
        }
        if (spMDO.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select MDO");
            return false;
        }
        if (spType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Validation Type");
            return false;
        }
        if (fromDate.getText().length() == 0) {
            msclass.showMessage("Please Select From Date");
            return false;
        }
        if (toDate.getText().length() == 0) {
            msclass.showMessage("Please Select To Date");
            return false;
        }
        return true;
    }

    /**
     * <P>//validate data  according to validation type</P>
     */
    private void validateData() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        sendDataToServer();

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

    private void sendDataToServer() {

        JSONObject requestParams = new JSONObject();
         validateDetail = new JSONObject();


        try {

            validateDetail.put("userCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            validateDetail.put("tbm", spTBM.getSelectedItem());
            validateDetail.put("tbmCode", tbmCode);
            validateDetail.put("mdo", spMDO.getSelectedItem());
            //validateDetail.put("mdo", "");
            validateDetail.put("mdoCode", mdoCode);
           // validateDetail.put("mdoCode", "");
            validateDetail.put("validationType", spType.getSelectedItem());
            validateDetail.put("fromDate", fromDate.getText());
            validateDetail.put("toDate", ToDate);
//             validateDetail.put("tbm", "S T WAYAL");
//             validateDetail.put("tbmCode", "97190469");
//               validateDetail.put("mdo", "APPASAHEB JIGE");
//             validateDetail.put("mdoCode", "MH215");
            if (CommonUtil.addGTVActivity(context, "25", "Call Validation", cordinates, spType.getSelectedItem()+" "+spTBM.getSelectedItem(),"GTV","0",0.0)) {
                // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
            }

            new CallValidationApiCall("validateDetail", validateDetail).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void  notifyAPI()
    {
        if(validateDetail!= null)
        new CallValidationApiCall("validateDetail", validateDetail).execute();

    }



    /**
     * <P> AsyncTask Class for api call to upload call validation data</P>
     */
    private class CallValidationApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public CallValidationApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return getData(function, obj);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                if (!resultout.isEmpty()) {
                    final JSONObject jsonObject = new JSONObject(resultout);
                    if (jsonObject.has("success")) {
                        if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                            if (jsonObject.has("validationType")) {
                                isValidate=false;
                                try {
                                    switch (jsonObject.getString("validationType")) {

                                        case "FARMER CALL VALIDATION":
                                            recRecordList.setLayoutManager(layoutManager);

                                            recRecordList.setAdapter(new CallValidationRecordAdapter(getBaseContext(), jsonObject.getString("entity"), mDatabase, mdo, 0));
                                            txtRecord.setText("FARMER RECORDS");
                                            JSONArray jsonArray0 = new JSONArray(jsonObject.getString("entity"));
                                            txtCount.setText("COUNT :" + jsonArray0.length());
                                            lblheader.setText("FARMER CALL VALIDATION");
                                            llRecord.setVisibility(View.VISIBLE);
                                            recRecordList.setVisibility(View.VISIBLE);
                                            break;
                                        case "DISTRIBUTOR CALL VALIDATION":
                                            recRecordList.setLayoutManager(layoutManager);

                                            recRecordList.setAdapter(new CallValidationRecordAdapter(getBaseContext(), jsonObject.getString("entity"), mDatabase, mdo,1));
                                            txtRecord.setText("DISTRIBUTOR RECORDS");
                                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("entity"));
                                            txtCount.setText("COUNT :" + jsonArray1.length());
                                            lblheader.setText("DISTRIBUTOR CALL VALIDATION");
                                            llRecord.setVisibility(View.VISIBLE);
                                            recRecordList.setVisibility(View.VISIBLE);
                                            break;

                                        case "RETAILER CALL VALIDATION":
                                            recRecordList.setLayoutManager(layoutManager);

                                            recRecordList.setAdapter(new CallValidationRecordAdapter(getBaseContext(), jsonObject.getString("entity"), mDatabase, mdo,2));
                                            txtRecord.setText("RETAILER RECORDS");
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString("entity"));
                                            txtCount.setText("COUNT :" + jsonArray.length());
                                            lblheader.setText("RETAILER CALL VALIDATION");
                                            llRecord.setVisibility(View.VISIBLE);
                                            recRecordList.setVisibility(View.VISIBLE);
                                            break;
                                        default:
                                            llRecord.setVisibility(View.GONE);
                                            recRecordList.setVisibility(View.GONE);
                                            lblheader.setText("CALL VALIDATION");
                                            break;


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                            }

                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CallValidation.this);
                            builder.setTitle("MyActivity");
                            builder.setMessage("Data Fetched Successfully");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Config.refreshActivity(FarmerCallActivity.this);


                                    dialog.dismiss();
                                    progressBarVisibility();

                                }
                            });
                            androidx.appcompat.app.AlertDialog alert = builder.create();
                            alert.show();
                            progressBarVisibility();
                        } else {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CallValidation.this);
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

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CallValidation.this);
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
                }else
                {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CallValidation.this);
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

                } catch(Exception e){
                    e.printStackTrace();
                    progressBarVisibility();
                }
            }

    }


    /**
     * <P>Method to upload the distributor survey data to server</P>
     *
     * @param function
     * @param obj
     * @return
     */
    private String getData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.CALLVALIDATION_SERVER_API, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
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

    private void onToDateSelected() {
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                int cmonth = calendar.get(Calendar.MONTH);
                int cday = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CallValidation.this, AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(0);
                                cal.set(year, month, day, 0, 0, 0);
                                Date chosenDate = cal.getTime();

                                DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
                                String strDate = df_medium_uk.format(chosenDate);

                                toDate.setText(year + "/" + (month +1 )+ "/"+ (day));
                                ToDate = year + "/" + (month +1 )+ "/"+ (day+1);

                            }
                        }, cyear, cmonth, cday);
                datePickerDialog.show();

            }

        });

    }

    private void onFromDateSelected() {

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                int cmonth = calendar.get(Calendar.MONTH);
                int cday = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CallValidation.this, AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(0);
                                cal.set(year, month, day, 0, 0, 0);
                                Date chosenDate = cal.getTime();
                                Date myDate = null;

                                DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
                                String strDate = df_medium_uk.format(chosenDate);

                                /*SimpleDateFormat   dateFormat = new SimpleDateFormat("dd MMM yyyy");

                                try {
                                    myDate = dateFormat.parse(chosenDate.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
                               String finalDate = timeFormat.format(myDate);

                                 */
                              //  05/07/2020
                                fromDate.setText(year + "/" + (month +1 )+ "/"+ (day));

                            }
                        }, cyear, cmonth, cday);
                datePickerDialog.show();

            }

        });
    }

    private void bindValidationType() {


        try {
            spType.setAdapter(null);

            String str = null;
            try {
                typeArray = getResources().getStringArray(R.array.validationType);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spType.setAdapter(adapter);

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
            spTBM.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct tbmCode,tbmDesc  FROM MdoTbmMaster where tbmCode = '" + userCode + "' order by tbmDesc asc  ";
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
                mdolist.add(0, new GeneralMaster("SELECT MDO",
                        "SELECT MDO"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    mdolist.add(new GeneralMaster(cursor.getString(0),
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(isValidate) {
            notifyAPI();
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
