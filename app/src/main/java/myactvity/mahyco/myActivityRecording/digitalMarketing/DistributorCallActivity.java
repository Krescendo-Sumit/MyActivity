package myactvity.mahyco.myActivityRecording.digitalMarketing;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.os.AsyncTask;
        import android.os.Handler;
        import android.os.SystemClock;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
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
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.util.ArrayList;
        import java.util.List;

        import myactvity.mahyco.R;
        import myactvity.mahyco.UserRegister;
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

public class DistributorCallActivity extends AppCompatActivity {

    private static final String TAG = "DISTRIBUTORCALL";
    SearchableSpinner spTBM, spState, spDist, spTaluka, spDistributor,spDistributorResponse;
    MultiSelectionSpinner spProductName,spCropType;
    CheckBox chkBoxProduct,chkBoxOther;
    private String state, dist, croptype, taluka,distributorName, productName, calltypeProduct="", calltypeOther="";
    Button btnSubmit, btnAdd;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etCallSummary;
    String[]  ratingArray;
    TextView lblheader;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    List<GeneralMaster> distributorList;
    private long mLastClickTime = 0;
    String userCode;
    Config config;
    Prefs mPref;
    private String distributorCode;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_call);

        initUI();
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
        spTBM = (SearchableSpinner) findViewById(R.id.spTBM);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spDistributor = (SearchableSpinner) findViewById(R.id.spDistributor);
        spDistributorResponse = (SearchableSpinner) findViewById(R.id.spDistributorResponse);

        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        chkBoxProduct =  findViewById(R.id.chkBoxProduct);
        chkBoxOther =  findViewById(R.id.chkBoxOther);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        bindTBM();
        
        Function.bindState(spState,mDatabase, this,msclass);
        Function.bindcroptype(spCropType, null,"C",this, mDatabase,msclass);
        Function.bindProductName(spProductName, null,"", this,mDatabase,msclass);
        bindDistributorResponse();

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
                bindDistributor();
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    distributorName = gm.toString();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    distributorCode = gm.Code().trim();
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

    }

    private void bindTBM() {
        try {
            spTBM.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> tbmlist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct tbmCode,tbmDesc  FROM MdoTbmMaster where tbmCode = '"+ userCode +"' order by tbmDesc asc  ";
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


    private void onSubmitBtn() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DistributorCallActivity.this);

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

        JSONObject distributorCall = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            distributorCall.put("id", "0");
            distributorCall.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
            distributorCall.put("tbm", spTBM.getSelectedItem());
            distributorCall.put("State", spState.getSelectedItem());
            distributorCall.put("District", spDist.getSelectedItem());
            distributorCall.put("Taluka", spTaluka.getSelectedItem());
            distributorCall.put("DistributorID", distributorCode);
            distributorCall.put("DistributorName", distributorName);
            distributorCall.put("CallTypeProductPromotion", calltypeProduct );
            distributorCall.put("CallTypeOtherActivity", calltypeOther );
            if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                croptype= "";
                distributorCall.put("CropType", croptype);
            }else {
                distributorCall.put("CropType", spCropType.getSelectedItem());
            }

            if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                productName="";
                distributorCall.put("ProductName", productName);
            }else {
                distributorCall.put("ProductName", spProductName.getSelectedItem());
            }
            distributorCall.put("DistributorResponse", spDistributorResponse.getSelectedItem());
            distributorCall.put("CallSummary", etCallSummary.getText());


            distributorCall.put("entryDt",   Function.getCurrentDate() );

            jsonArray.put(distributorCall);
            requestParams.put("Table", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("distributorData", requestParams.toString());

          new  DistributorApiCall("distributorData", requestParams).execute();



    }



    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class DistributorApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public DistributorApiCall(String function, JSONObject obj) {

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(DistributorCallActivity.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorCallActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(DistributorCallActivity.this);
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
    private String uploadVOPFData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.DISTRIBUTOR_CALL_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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


    private void bindDistributorResponse() {

        try {
            spDistributorResponse.setAdapter(null);

            try {
                ratingArray = getResources().getStringArray(R.array.rating4);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistributorResponse.setAdapter(adapter);

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
                Function.bindProductName(spProductName,null, croptype,getBaseContext(),mDatabase,msclass);
            }
        });

    }
    public void bindDistributor() {
        try {
            spDistributor.setAdapter(null);

            String str = null;
            try {

                distributorList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct RetailerName,code  FROM RetailerMaster " +
                        "where activity='Distributor' " +
                        " order by  RetailerName ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                distributorList.add(new GeneralMaster("SELECT DISTRIBUTOR",
                        "SELECT DISTRIBUTOR"));
//                distributorList.add(1, new GeneralMaster("New Distributor (Tag the Distributor)",
//                        "New Distributor (Tag the Distributor)"));
               if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            distributorList.add(new GeneralMaster(cursor.getString(1),
                                    cursor.getString(0).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }

                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, distributorList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistributor.setAdapter(adapter);


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

        if (spTBM.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select TBM");
            return false;
        }
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
            if (spDistributor.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select distributor name");
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

                    msclass.showMessage("Please Select  Product Name");
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

            if (spDistributorResponse.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select distributor response");
                return false;
            }

            if (etCallSummary.getText().length() == 0) {
                msclass.showMessage("Please enter call summary");
                return false;
              }

        return true;
    }


}

