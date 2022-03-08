package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.UserRegister;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Function;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class DistributorValidationRecordActivity extends AppCompatActivity {
    TextView etValidateDate,txtDistributorName,txtMobileNum,txtDistributorfirm,txtFarmerType,txtDist,txtTaluka,txtVillage,txtCallInitiateBy,txtCallInitiateOn,txtCallType;
    EditText etArea,etCallSummary,etRemark;
    MultiSelectionSpinner spCropType,spProductName;
    SearchableSpinner spDistResponse,spStatus;
    String[] ratingArray,statusyArray;
    Button btnSubmit;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    private long mLastClickTime = 0;
    String userCode;
    Config config;
    Prefs mPref;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    String json,croptype,productName;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private String callInitiateOn;
    private String state;
    private String distributorId;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_validation_record);

        initUI();
    }
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

        txtDistributorName = (TextView) findViewById(R.id.txtDistributorName);
        txtMobileNum = (TextView) findViewById(R.id.txtMobileNum);
        txtDistributorfirm = (TextView) findViewById(R.id.txtDistributorfirm);
        txtFarmerType = (TextView) findViewById(R.id.txtFarmerType);
        txtDist = (TextView) findViewById(R.id.txtDist);
        txtTaluka = (TextView) findViewById(R.id.txtTaluka);
        txtVillage = (TextView) findViewById(R.id.txtVillage);
        txtCallInitiateBy = (TextView) findViewById(R.id.txtCallInitiateBy);
        txtCallInitiateOn = (TextView) findViewById(R.id.txtCallInitiateOn);
        txtCallType = (TextView) findViewById(R.id.txtCallType);
        etArea = findViewById(R.id.etArea);
        spCropType = findViewById(R.id.spCropType);
        spProductName = findViewById(R.id.spProductName);
        spDistResponse = findViewById(R.id.spDistResponse);
        etCallSummary = findViewById(R.id.etCallSummary);
        spStatus = findViewById(R.id.spStatus);
        etRemark = findViewById(R.id.etRemark);
        etValidateDate = findViewById(R.id.etValidateDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);

        Function.bindcroptype(spCropType,"C",this, mDatabase,msclass);
        Function.bindProductName(spProductName,"", this,mDatabase,msclass);
        bindDistributorResponse();
        onCropItemSelected();
        bindStatus();
        getBundleValue();
        onValidateDateSelected();
      //  etValidateDate.setText(Function.ConvertDateFormat(Function.getCurrentDate()));
        onBtnSubmit();

    }

    private void onValidateDateSelected() {

        etValidateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                int cmonth = calendar.get(Calendar.MONTH);
                int cday = calendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(DistributorValidationRecordActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, month, day, 0, 0, 0);
                        Date chosenDate = cal.getTime();
                        Date myDate = null;

                        DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
                        String strDate = df_medium_uk.format(chosenDate);

                        //  05/07/2020
                        etValidateDate.setText( Function.ConvertDateFormat(month+1 + "/" + day  + "/" + year));


                    }
                }, cyear, cmonth, cday);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }

        });
    }

    private void onBtnSubmit() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
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

        JSONObject FarmerCallValidation = new JSONObject();
        JSONObject RetailerCallValidation = new JSONObject();
        JSONObject DistributorCallValidation = new JSONObject();
        JSONObject callValidation = new JSONObject();

        try {

            DistributorCallValidation.put("userCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            DistributorCallValidation.put("distributorName", txtDistributorName.getText());
            DistributorCallValidation.put("distributorID", distributorId);
            DistributorCallValidation.put("distributorFirmName", txtDistributorfirm.getText());
            DistributorCallValidation.put("distributorMobile", txtMobileNum.getText().equals("null")?"":txtMobileNum.getText());
            DistributorCallValidation.put("state", state);
            DistributorCallValidation.put("district", txtDist.getText());
            DistributorCallValidation.put("taluka", txtTaluka.getText());
            DistributorCallValidation.put("callInitiatedOn", callInitiateOn);
            DistributorCallValidation.put("callInitiatedBy", txtCallInitiateBy.getText());
            DistributorCallValidation.put("CallType", txtCallType.getText());

            if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                croptype = "";
                DistributorCallValidation.put("CropDiscussed", croptype);
            } else {
                DistributorCallValidation.put("CropDiscussed", spCropType.getSelectedItem());
            }

            if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                productName = "";
                DistributorCallValidation.put("ProductDiscussed", productName);
            } else {
                DistributorCallValidation.put("ProductDiscussed", spProductName.getSelectedItem());
            }

                DistributorCallValidation.put("status", spStatus.getSelectedItem());


            DistributorCallValidation.put("distributorResponse", spDistResponse.getSelectedItem());
            DistributorCallValidation.put("callSummary", etCallSummary.getText());
            DistributorCallValidation.put("remarkByValidator", etRemark.getText());
            DistributorCallValidation.put("validateDt", Function.getCurrentDate());
            DistributorCallValidation.put("validationType", "DISTRIBUTOR CALL VALIDATION");


            callValidation.put("FarmerCallValidation", FarmerCallValidation);
            callValidation.put("RetailerCallValidation", RetailerCallValidation);
            callValidation.put("DistributorCallValidation", DistributorCallValidation);
            callValidation.put("validationType", "DISTRIBUTOR CALL VALIDATION");
            requestParams.put("Table", callValidation);


            progressBarVisibility();




        } catch (JSONException e) {
            e.printStackTrace();
            progressBarVisibility();
        }

        Log.d("DistributorValidation", requestParams.toString());
        new ValidationApiCall("DistributorValidation", requestParams).execute();
    }


    /**
     * <P> AsyncTask Class for api call to upload  data</P>
     */
    private class ValidationApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public ValidationApiCall(String function, JSONObject obj) {

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorValidationRecordActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              //  Config.refreshActivity(DistributorValidationRecordActivity.this);

                                finish();
                            CallValidation.isValidate = true;
                                dialog.dismiss();
                                progressBarVisibility();


                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorValidationRecordActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(DistributorValidationRecordActivity.this);
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
     * <P>Method to upload the  data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.VALIDATE_CALL_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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

    private boolean validation() {
        if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
            msclass.showMessage("Please select crop discussed");
            return false;
        }
        if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            msclass.showMessage( "Please select  product discussed");
            return false;
        }

        if (spDistResponse.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select distributor response");
            return false;
        }
        if (etCallSummary.getText().length() == 0) {
            msclass.showMessage("Please enter call summary");
            return false;
        }

        if (spStatus.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please select status");
            return false;
        }
        if (etRemark.getText().length() == 0) {
            msclass.showMessage("Please enter remark");
            return false;
        }
        if (etValidateDate.getText().length() == 0) {
            msclass.showMessage("Please enter validate date");
            return false;
        }

        return true;
    }






    private void getBundleValue() {
        Intent intent = getIntent();

        json = intent.getStringExtra("distributorcallModel");


        try {
            JSONObject jsonObject = new JSONObject(json);
            txtDistributorName.setText(jsonObject.getString("DistributorName"));
             distributorId = jsonObject.getString("DistributorID");

            txtMobileNum.setText(jsonObject.getString("distributorMobile").equals("null")?"":jsonObject.getString("distributorMobile"));
            int firstBracket = jsonObject.getString("DistributorName").indexOf("("); // detect the first bracket character
            String firmName = jsonObject.getString("DistributorName").substring(0, firstBracket);

            txtDistributorfirm.setText(firmName);

           state= jsonObject.getString("State");
            txtDist.setText(jsonObject.getString("District"));
            txtTaluka.setText(jsonObject.getString("Taluka").equals("null")? "":jsonObject.getString("Taluka"));
            callInitiateOn=jsonObject.getString("entryDt");
            txtCallInitiateOn.setText(Function.ConvertDateFormat(jsonObject.getString("entryDt")));
            if(!jsonObject.getString("CallTypeProductPromotion").isEmpty()) {
                txtCallType.setText(jsonObject.getString("CallTypeProductPromotion"));
            }else {
                txtCallType.setText(jsonObject.getString("CallTypeOtherActivity"));

            }
            if (!jsonObject.getString("MDO_name").isEmpty()) {
                txtCallInitiateBy.setText(jsonObject.getString("MDO_name"));

            }
            if(!jsonObject.getString("CallSummary").isEmpty()){
                etCallSummary.setText(jsonObject.getString("CallSummary"));

            }

            for (int k = 0; k < ratingArray.length; k++) {
                if (jsonObject.getString("DistributorResponse").equalsIgnoreCase(ratingArray[k])) {
                    spDistResponse.setSelection(k);
                }
            }
            String[] array1;
            String[] array2;
            List<String> croplst =  new ArrayList<String>();
            List<String> prodctlst =  new ArrayList<String>();

            if(!jsonObject.getString("CropType").isEmpty()&& jsonObject.getString("CropType")!=null) {
                String cropl = jsonObject.getString("CropType").toString();
                Function.bindProductName(spProductName, "["+cropl+"]",this,mDatabase,msclass);
                String productlist1=jsonObject.getString("ProductName").toString();
                array1=cropl.split(",");
                array2=productlist1.split(",");

                for (String s : array1) {
                    croplst.add(s.trim());

                }
                for (String s : array2) {
                    prodctlst.add(s.trim());

                }
                spCropType.setSelection(croplst);

                spProductName.setSelection(prodctlst);


            }
            for (int k = 0; k < statusyArray.length; k++) {
                if (jsonObject.getString("status").equalsIgnoreCase(statusyArray[k])) {
                    spStatus.setSelection(k);
                }
            }

            if (!jsonObject.getString("remarkByValidator").isEmpty()) {
                if(!jsonObject.getString("remarkByValidator").equals("null"))
                    etRemark.setText(jsonObject.getString("remarkByValidator"));

            }

            if (!jsonObject.getString("validateDt").isEmpty()) {
                if(!jsonObject.getString("validateDt").equals("null")) {
                    etValidateDate.setText(Function.ConvertDateFormat(jsonObject.getString("validateDt")));
                }else {
                    etValidateDate.setText(Function.ConvertDateFormat(Function.getCurrentDate()));

                }

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void bindDistributorResponse() {

        try {
            spDistResponse.setAdapter(null);

            try {
                ratingArray = getResources().getStringArray(R.array.rating4);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistResponse.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void bindStatus() {
        try {
            spStatus.setAdapter(null);

            String str = null;
            try {
                statusyArray = getResources().getStringArray(R.array.status1);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statusyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spStatus.setAdapter(adapter);

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
    }
}
