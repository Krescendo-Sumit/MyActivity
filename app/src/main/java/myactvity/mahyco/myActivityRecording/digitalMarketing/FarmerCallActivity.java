package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import myactvity.mahyco.helper.AsyncResponse;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class FarmerCallActivity extends AppCompatActivity {
    private static final String TAG = "FARMERCALL";
    SearchableSpinner spState, spDist, spTaluka,  spVillage, spFocusedVillages,spFarmer,spFarmerResponse;
    MultiSelectionSpinner spProductName,spCropType,spCropGrown,spProductUsed,spFarmerType;
    CheckBox chkBoxProduct,chkBoxOther;
    private String state, dist, croptype, taluka,FarmerName, productName, calltypeProduct="", calltypeOther="";
    Button btnSubmit, btnSearch;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText  etFarmerName, etFarmerMob, etCallSummary,etArea;
    String[]  ratingArray, farmerTypeArray;
    TextView myTextProgress;
    ProgressBar progressBar;
    RelativeLayout relPRogress,relCountActivity;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    List<GeneralMaster> stateList,distList, talukaList, vilageList, focussedVillageList, productList, croplist;
    List<GeneralMaster> stateListStr = new ArrayList<>();
    List<GeneralMaster> distListStr = new ArrayList<>();
    List<GeneralMaster> talukaListStr = new ArrayList<>();
    List<GeneralMaster> focussedVillageListStr = new ArrayList<>();
    List<GeneralMaster> vilageListStr = new ArrayList<>();
    Boolean isFromTestimonial ;
    LinearLayout newFarmer;
    private long mLastClickTime = 0;
    String userCode="";
    Config config;
    Prefs mPref;
    boolean isSearched ;
    RadioGroup radGroupActivity;
    RadioButton radFocusedActivity;
    RadioButton radOtherActivity;
    LinearLayout llOtherVillages, llFocussedVillages;
    private boolean isFirst=true;
    private boolean isFirstDist=true;
    private boolean isFirstTaluka=true;
    private String stateCode;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_call);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBundle();

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
        myTextProgress = findViewById(R.id.myTextProgress);

        etCallSummary = findViewById(R.id.etCallSummary);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spFarmerResponse = (SearchableSpinner) findViewById(R.id.spFarmerResponse);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        spCropGrown = findViewById(R.id.spCropGrown);
        spProductUsed = findViewById(R.id.spProductUsed);
        spFarmerType = findViewById(R.id.spFarmerType);
        etArea = findViewById(R.id.etArea);
        etFarmerMob = findViewById(R.id.etMob);
        etFarmerName = findViewById(R.id.etName);
       llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
       // llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);


        spCropType = (MultiSelectionSpinner) findViewById(R.id.spCropType);
        spProductName = (MultiSelectionSpinner) findViewById(R.id.spProductName);
        chkBoxProduct =  findViewById(R.id.chkBoxProduct);
        chkBoxOther =  findViewById(R.id.chkBoxOther);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radFocusedActivity = (RadioButton) findViewById(R.id.radFocusedActivity);
        radOtherActivity = (RadioButton) findViewById(R.id.radOtherActivity);
        relCountActivity =  findViewById(R.id.relCountActivity);


        bindFarmerType();

      //   stateList =  Function.getBindStatelist(spState,mDatabase, this,msclass);

        if(config.NetworkConnection()) {

            relPRogress.setVisibility(View.VISIBLE);
            myTextProgress.setText("Uploading Data");
            relPRogress.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;
                }
            });

            doWorkStateAPI("", "", "");

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            container.setEnabled(false);
            container.setClickable(false);

        }

        Function.bindcroptype(spCropType, spCropGrown,"C",this, mDatabase,msclass);
        Function.bindProductName(spProductName, spProductUsed,"", this,mDatabase,msclass);

        bindFarmerResponse();
        onSubmitBtn();
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state =gm.Code().trim();
                    //state = (String) parent.getSelectedItem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               if(!isSearched) {
                   isFirstDist=true;
                   bindDist(state, gm.Desc(), spDist, null, mDatabase, getBaseContext(), msclass);
               }
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
                if(!isSearched) {
                     bindTalukalist(dist,spTaluka, null, getBaseContext(),mDatabase, msclass);
                }
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
                    taluka = gm.Code().trim();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!isSearched) {
                    spVillage.setSelection(0);
                    spFocusedVillages.setSelection(0);
                    bindFocussedVillage(taluka);
                    bindVillage(taluka, null);

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                 String village =   gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spFocusedVillages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");



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
        spProductUsed.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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
        spFarmerType.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
               // productName = String.valueOf(strings);

            }
        });

      /*  radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocusedActivity:

                        if (radFocusedActivity.isChecked()) {
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);

                        } else {
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        }

                        radOtherActivity.setChecked(false);
                        spState.setSelection(0);
                        break;
                    case R.id.radOtherActivity:
                        if (radOtherActivity.isChecked()) {
                            llFocussedVillages.setVisibility(View.GONE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        } else {

                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.GONE);
                        }
                        spFocusedVillages.setSelection(0);
                        break;
                }
            }
        });

       */




        onCropItemSelected();
        onSearchBtn();
    }

    private List<GeneralMaster> bindTalukalist(String dist, final SearchableSpinner spTaluka, final JSONObject jsonObject1, Context baseContext, SqliteDatabase mDatabase, Messageclass msclass) {

        try {
            spTaluka.setAdapter(null);
            final List<GeneralMaster> list = new ArrayList<GeneralMaster>();
            String str = null;
            try {

                if(!dist.equals("SELECT DISTRICT")) {

                    relPRogress.setVisibility(View.VISIBLE);
                    myTextProgress.setText("Please wait,taluka list");
                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });


                    CallApi callApi=new CallApi( createJsonObject(userCode, stateCode, dist, ""), new AsyncResponse() {
                        @Override
                        public void processFinish(String callbackResponse) {
                            Log.d("Callback response ->", callbackResponse);
                            //process after callback
                            callBackResponseMethod(callbackResponse);
                            if(isSearched){
                                //for taluka selection
                                for (int k = 0; k < talukaListStr.size(); k++) {
                                    try {
                                        if (jsonObject1.getString("taluka").equalsIgnoreCase(talukaListStr.get(k).toString())) {
                                            taluka = talukaListStr.get(k).Code();
                                            spTaluka.setSelection(k);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                progressBarVisibility();
                                bindVillage(taluka,jsonObject1);


                            }

                        }
                    });
                    callApi.execute();//.get();



                   // String result = new CallApi(createJsonObject(userCode, state, dist, ""),null).execute().get();

                }else {
                    talukaListStr.clear();
                    talukaListStr.add(0,new GeneralMaster("SELECT TALUKA","SELECT TALUKA"));
                   //list.addAll(talukaListStr);
                }

                if(talukaListStr.size()>1) {
                    talukaListStr.add(0, new GeneralMaster("SELECT  TALUKA",
                            "SELECT  TALUKA"));
                }
               // list.addAll(talukaListStr);
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, talukaListStr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
                // dialog.dismiss();
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
        return  talukaListStr;
    }

    private void getBundle() {


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
      isFromTestimonial =  bundle.getBoolean("FromTestimonial");
    }

    public String   bindDist(String stateCode, String state,final SearchableSpinner spDist, final JSONObject jsonObject1,final SqliteDatabase mDatabase, Context mContext, final Messageclass msclass) {
        try {
            spDist.setAdapter(null);
            final List<GeneralMaster> list = new ArrayList<GeneralMaster>();
            try {
              if(!state.equals("SELECT STATE")) {

                  relPRogress.setVisibility(View.VISIBLE);
                  myTextProgress.setText("Please wait, bind district Data");
                  relPRogress.setOnTouchListener(new View.OnTouchListener() {
                      @Override
                      public boolean onTouch(View v, MotionEvent event) {
                          return true;
                      }
                  });
                  CallApi callApi=new CallApi( createJsonObject(userCode, stateCode, "", ""), new AsyncResponse() {
                      @Override
                      public void processFinish(String callbackResponse) {
                          Log.d("Callback response ->", callbackResponse);
                          //process after callback
                          callBackResponseMethod(callbackResponse);
                          if (isSearched) {
                              //for dist selection
                              for (int j = 0; j < distListStr.size(); j++) {
                                  try {
                                      if (jsonObject1.getString("district").equalsIgnoreCase(distListStr.get(j).toString())) {
                                          dist = distListStr.get(j).Code();
                                          spDist.setSelection(j);
                                      }
                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }
                              }
                              progressBarVisibility();
                              bindTalukalist(dist,spTaluka,  jsonObject1, getBaseContext(),mDatabase, msclass);




                          }
                      }
                  });
                //  callApi.execute().get();
                  callApi.execute();//.get();

              }else {
                  distListStr.clear();
                  distListStr.add(0,new GeneralMaster("SELECT DISTRICT","SELECT DISTRICT"));
                // list.addAll(distListStr);
              }
                              if(distListStr.size()>1) {
                                  distListStr.add(0, new GeneralMaster("SELECT  DISTRICT",
                                          "SELECT  DISTRICT"));
                              }
//                              list.addAll(distListStr);
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (mContext, android.R.layout.simple_spinner_dropdown_item, distListStr);
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
        return dist;
    }

 JSONObject createJsonObject(String userCode, String stateCode, String districtCode, String talukaCode)
{
    JSONObject object = new JSONObject();
    try {
        object.put("userid", userCode);
        object.put("selectedState",stateCode);
        object.put("selectedDistrict",districtCode);
        object.put("selectedTaluka",talukaCode);
    } catch (JSONException e) {
        e.printStackTrace();
    }
return object;
}

    public class CallApi extends  AsyncTask<String, String, String>
    {
        private  JSONObject object;
        public AsyncResponse delegate = null;//Call back interface


        public CallApi( JSONObject object, AsyncResponse asyncResponse){
            this.object=object;
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected String doInBackground(String... strings) {

           return HttpUtils.POSTJSON(Constants.getStatesByUserID , object, mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
            //Log.d(TAG, "doInBackground:Xkyoan7PvPfE761jcV5MJx3Or_hRFpRvJNbXL50tz2leP1Up76Vn3N_ZYzdDyoHYYvlyyr0ZzoBlm9qLIHGdhdwvcXTK58dTuAFq3YyFVpDEFkfbiU71HCrZYxGFrWNSxf-Pyglz_RirFOjRTw2GhsG32m5kpMmjVmhazGGXaGK7Mz0Y6No9RpCjy-petsJGWfJpvvGe55z4U3zzNgOjy1MMn-78j8hPvP8w4Ty-6AM"+object);
            //return HttpUtils.POSTJSON(Constants.getStatesByUserID , object, mPref.getString("Xkyoan7PvPfE761jcV5MJx3Or_hRFpRvJNbXL50tz2leP1Up76Vn3N_ZYzdDyoHYYvlyyr0ZzoBlm9qLIHGdhdwvcXTK58dTuAFq3YyFVpDEFkfbiU71HCrZYxGFrWNSxf-Pyglz_RirFOjRTw2GhsG32m5kpMmjVmhazGGXaGK7Mz0Y6No9RpCjy-petsJGWfJpvvGe55z4U3zzNgOjy1MMn-78j8hPvP8w4Ty-6AM",""));


        }

       protected void onPostExecute(String result) {

            Log.d("result", result);
            super.onPostExecute(result);
            delegate.processFinish(result);
        }

    }


    public  void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();

                stateListStr.add(0,new GeneralMaster("SELECT STATE","SELECT STATE"));


                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, stateListStr);
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



    private void onSearchBtn() {

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationSearch()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    relPRogress.setVisibility(View.VISIBLE);
                    myTextProgress.setText("Searching Data");
                    relPRogress.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    resetActivity();
                    doSearch();

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    container.setEnabled(false);
                    container.setClickable(false);

                }
            }
        });
    }

    private boolean validationSearch() {

        if (etFarmerName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }
        if (etFarmerMob.getText().length() == 0) {
            msclass.showMessage("Please enter mobile no");
            return false;
        }

        return  true;
    }

    private void doSearch() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        searchData();
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

    private void searchData() {

        JSONObject requestParams = new JSONObject();
        JSONObject searchDetail = new JSONObject();


        try {

            searchDetail.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            searchDetail.put("farmerName", etFarmerName.getText());
            searchDetail.put("mobileNumber", etFarmerMob.getText());


            requestParams.put("Table", searchDetail);
            new FarmerSearchApiCall("SearchDetail", requestParams).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class FarmerSearchApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public FarmerSearchApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return searchAPIData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                        if( jsonObject.has("message") &&  jsonObject.getString("message").equals("No activities found"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);
                            builder.setTitle("Info");
                            builder.setMessage("No data found");
                           // Config.refreshActivity(FarmerCallActivity.this);
                            isSearched = false;
                            resetActivity();
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                            progressBarVisibility();

                        }else {

                         //  isSearched = true;
                           if(!isSearched){
                               isSearched = true;
                           }


                            getSearchData(jsonObject);


                        }

                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);
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

    private void resetActivity() {

        spState.setSelection(0);
        spDist.setSelection(0);
        spTaluka.setSelection(0);
        spVillage.setSelection(0);
        spFocusedVillages.setSelection(0);
        spProductName.setSelection(0);
        spProductUsed.setSelection(0);
        spCropType.setSelection(0);
        spCropGrown.setSelection(0);
        spFarmerResponse.setSelection(0);
        chkBoxOther.setChecked(false);
        chkBoxProduct.setChecked(false);
        etArea.setText("");
        etCallSummary.setText("");

        spFocusedVillages.setEnabled(true);
        spState.setEnabled(true);
        spDist.setEnabled(true);
        spTaluka.setEnabled(true);
        spVillage.setEnabled(true);
        spCropGrown.setEnabled(true);
        spProductUsed.setEnabled(true);

        for (int i = 0; i < radGroupActivity.getChildCount(); i++) {
            radGroupActivity.getChildAt(i).setEnabled(true);
        }
    }

    private void getSearchData(JSONObject jsonObject) {

        try {

            JSONArray array = new JSONArray(jsonObject.getString("table"));
           final JSONObject jsonObject1 = new JSONObject(array.get(array.length()-1).toString());

            if(!jsonObject1.getString("totalLand").isEmpty()) {
                etArea.setText(jsonObject1.getString("totalLand"));
                etArea.setEnabled(false);

            }else {
                etArea.setEnabled(true);
                }

            Function.bindcroptype(spCropGrown, spCropType,"C",this,mDatabase,msclass);

            String[] array1;
            String[] array2;
            String[] array3;
            String[] arrayCrop;
            String[] arrayProduct;
            List<String> croplst =  new ArrayList<String>();
            List<String> prodctlst =  new ArrayList<String>();

          List<String> croplstGrown =  new ArrayList<String>();
            List<String> prodctlstUsed =  new ArrayList<String>();

            List<String> farmerType =  new ArrayList<String>();

            try {
                if(!jsonObject1.getString("cropGrownByFarmer").isEmpty()) {
                    String cropl = jsonObject1.getString("cropGrownByFarmer").toString();
                    Function.bindProductName(spProductUsed, "["+cropl+"]",this,mDatabase,msclass);
                    String productlist1=jsonObject1.getString("productUsedByFarmer").toString();
                    array1=cropl.split(",");
                    array2=productlist1.split(",");

                    for (String s : array1) {
                        croplstGrown.add(s.trim());

                    }
                    for (String s : array2) {
                        prodctlstUsed.add(s.trim());

                    }

                    spCropGrown.setSelection(croplstGrown);

                    spProductUsed.setSelection(prodctlstUsed);



                    if(cropl.equals("SELECT CROP")){
                        spCropGrown.setEnabled(true);

                    } if(productlist1.equals("SELECT PRODUCT")){
                        spProductUsed.setEnabled(true);

                    }else {
                        spProductUsed.setEnabled(false);
                        spCropGrown.setEnabled(false);

                    }

                }else {
                    spProductUsed.setEnabled(true);
                    spCropGrown.setEnabled(true);


                }

                if(!jsonObject1.getString("farmerType").isEmpty()){
                    String farmerTypeStr=jsonObject1.getString("farmerType").toString();
                    array3=farmerTypeStr.split(",");
                    spFarmerType.setEnabled(false);

                    for (String s : array3) {
                        farmerType.add(s.trim());

                    }

                    spFarmerType.setSelection(farmerType);
                }else {
                    spFarmerType.setEnabled(true);
                }




                if(!jsonObject1.getString("CropDiscussed").isEmpty()) {
                    String cropl = jsonObject1.getString("CropDiscussed").toString();
                    Function.bindProductName(spProductName, "["+cropl+"]",this,mDatabase,msclass);
                    String productlist1=jsonObject1.getString("ProductDiscussed").toString();
                    arrayCrop=cropl.split(",");
                    arrayProduct=productlist1.split(",");

                    for (String s : arrayCrop) {
                        croplst.add(s.trim());

                    }
                    for (String s : arrayProduct) {
                        prodctlst.add(s.trim());

                    }
                    spCropType.setSelection(croplst);

                    spProductName.setSelection(prodctlst);


                }else {
                    spProductName.setEnabled(true);
                    spCropType.setEnabled(true);


                }



                if(!jsonObject1.getString("state").isEmpty()) {
                    setSpinnerDisable();


                    //for state selection
                    for (int i = 0; i < stateListStr.size(); i++) {
                        if (jsonObject1.getString("state").equalsIgnoreCase(stateListStr.get(i).toString())) {
                            state = stateListStr.get(i).toString();
                            stateCode =stateListStr.get(i).Code();
                            spState.setSelection(i);


                        }
                    }

                    dist = bindDist(stateCode, state, spDist, jsonObject1, mDatabase, getBaseContext(), msclass);





//                    focussedVillageList =  bindFocussedVillage(taluka);
//                    for (int k = 0; k < focussedVillageList.size(); k++) {
//                        if (jsonObject1.getString("focussedVillage").equalsIgnoreCase(focussedVillageList.get(k).toString())) {
//                            spFocusedVillages.setSelection(k);
//                        }
//                    }



                }
//                else
//                 if(!jsonObject1.getString("focussedVillage").isEmpty()){
//                     isSearched= false;
//                     spState.setSelection(0);
//                     spDist.setSelection(0);
//                     spTaluka.setSelection(0);
//                     spVillage.setSelection(0);
//                    //for focussed  vilage selection
////                    for (int k = 0; k < focussedVillageList.size(); k++) {
////                        if (jsonObject1.getString("focussedVillage").equalsIgnoreCase(focussedVillageList.get(k).toString())) {
////                            spFocusedVillages.setSelection(k);
////                        }
////                    }
//                }




                if(!jsonObject1.getString("CallTypeProductPromotion").isEmpty()){
                   chkBoxProduct.setChecked(true);

                }

                if(!jsonObject1.getString("CallTypeOtherActivity").isEmpty()){
                    chkBoxOther.setChecked(true);

                }



                for (int k = 0; k < ratingArray.length; k++) {
                    if (jsonObject1.getString("farmerResponse").equalsIgnoreCase(ratingArray[k])) {
                        spFarmerResponse.setSelection(k);
                    }
                }

                if(!jsonObject1.getString("callSummary").isEmpty()){
                    etCallSummary.setText(jsonObject1.getString("callSummary"));

                }

            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }
//            if(isSearched){
//                isSearched= false;
//            }
            progressBarVisibility();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void  setSpinnerDisable(){
    spFocusedVillages.setEnabled(false);
    spState.setEnabled(false);
    spDist.setEnabled(false);
    spTaluka.setEnabled(false);
    spVillage.setEnabled(false);

//    for (int i = 0; i < radGroupActivity.getChildCount(); i++) {
//            radGroupActivity.getChildAt(i).setEnabled(false);
//        }
    }
    /**
     * <P>Method to get the search  data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String searchAPIData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.GET_FARMERDATA_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }







    private void bindFarmerType() {

        try {
           // spFarmerType.setAdapter(null);

            try {
                farmerTypeArray = getResources().getStringArray(R.array.farmerType);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, farmerTypeArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (farmerTypeArray.length > 0) {
                    spFarmerType.setItems(farmerTypeArray);
                    spFarmerType.hasNoneOption(true);
                    spFarmerType.setSelection(new int[]{0});
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }


    private  void  doWorkStateAPI(final String stateCode, final String distCode, final String talukaCode)
    {
        try {
           // String result = new CallApi(createJsonObject(userCode,stateCode,distCode,talukaCode)).execute().get();
            progressBar.setIndeterminate(true);
            new Thread(new Runnable() {
                public void run() {

                    handler.post(new Runnable() {
                        public void run() {
                           callAPIMethod(stateCode,distCode,talukaCode);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

    private void callAPIMethod(String stateCode, String distCode, String talukaCode) {

        CallApi callApi=new CallApi( createJsonObject(userCode,stateCode, distCode, talukaCode), new AsyncResponse() {
            @Override
            public void processFinish(String callbackResponse) {
                Log.d("Callback response ->", callbackResponse);
                //process after callback
                callBackResponseMethod(callbackResponse);
            }
        });
        try {
            callApi.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void callBackResponseMethodOld(String callbackResponse) {

        JSONObject jsonObject = null;

        try {
            redirecttoRegisterActivity(callbackResponse);
            jsonObject = new JSONObject(callbackResponse);
            if (jsonObject.has("userStates")) {
                //progressBarVisibility();
                JSONObject obj = new JSONObject(jsonObject.getString("userStates"));
                // for state list
                JSONArray arrState = new JSONArray( obj.getString("stateList"));
                if(arrState.length()>0) {
                    if(isFirst) {
                        stateListStr.clear();
                        for (int i = 0; i < arrState.length(); i++) {
                            JSONObject object = new JSONObject(arrState.get(i).toString());
                            GeneralMaster generalMaster = new GeneralMaster(object.getString("stateCode"), object.getString("stateName"));
                            stateListStr.add(generalMaster);
                            bindState();
                            isFirst=false;
                        }

                    }else {
                        // for District list
                        JSONArray arrDistrict = new JSONArray( obj.getString("districtList"));
                        if(arrDistrict.length()>0){
                            if(isFirstDist)
                            {
                                distListStr.clear();
                                distListStr.add(0,new GeneralMaster("SELECT DISTRICT","SELECT DISTRICT"));
                                for (int i = 0; i < arrDistrict.length(); i++) {

                                    JSONObject object = new JSONObject(arrDistrict.get(i).toString());
                                    GeneralMaster generalMaster = new GeneralMaster(object.getString("districtCode"), object.getString("districtName").toUpperCase());
                                    distListStr.add(generalMaster);
                                }
                                isFirstDist=false;
                            }
                            else{
                                // for taluka list
                                JSONArray arrTaluka = new JSONArray( obj.getString("talukaList"));
                                if(arrTaluka.length()>0){

//                                    if(isFirstTaluka) {
                                        talukaListStr.clear();
                                    talukaListStr.add(0,new GeneralMaster("SELECT TALUKA","SELECT TALUKA"));

                                    for (int i = 0; i < arrTaluka.length(); i++) {

                                            JSONObject object = new JSONObject(arrTaluka.get(i).toString());
                                            GeneralMaster generalMaster = new GeneralMaster(object.getString("talukaCode"), object.getString("talukaName").toUpperCase());

                                            talukaListStr.add(generalMaster);


                                        }
                                       // isFirstTaluka=false;
                                    //}

                                    // for focuss villagggge list
                                    JSONArray arrFocusVillage = new JSONArray( obj.getString("focussedVillageList"));
                                    if(arrFocusVillage.length()>0){
                                        focussedVillageListStr.clear();
                                        for (int i = 0; i < arrFocusVillage.length(); i++) {

                                            JSONObject object = new JSONObject(arrFocusVillage.get(i).toString());
                                            GeneralMaster generalMaster = new GeneralMaster(object.getString("villageCode"), object.getString("vil_desc").toUpperCase());

                                            focussedVillageListStr.add(generalMaster);


                                        }
                                    }

                                    // for focuss villagggge list
                                    JSONArray arrVillage = new JSONArray( obj.getString("regularVillageList"));
                                    if(arrVillage.length()>0){
                                        vilageListStr.clear();
                                        for (int i = 0; i < arrVillage.length(); i++) {

                                            JSONObject object = new JSONObject(arrVillage.get(i).toString());
                                            GeneralMaster generalMaster = new GeneralMaster(object.getString("villageCode"), object.getString("vil_desc").toUpperCase());

                                            vilageListStr.add(generalMaster);


                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            }else {
              msclass.showMessage(jsonObject.getString("Message"));
            }
            progressBarVisibility();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void callBackResponseMethod(String callbackResponse) {

        JSONObject jsonObject = null;

        try {
            redirecttoRegisterActivity(callbackResponse);
            jsonObject = new JSONObject(callbackResponse);
            if (jsonObject.has("userStates")) {
                //progressBarVisibility();
                JSONObject obj = new JSONObject(jsonObject.getString("userStates"));
                // for state list
                JSONArray arrState = new JSONArray( obj.getString("stateList"));
                if(arrState.length()>0) {
                    if(isFirst) {
                        stateListStr.clear();
                        for (int i = 0; i < arrState.length(); i++) {

                            JSONObject object = new JSONObject(arrState.get(i).toString());
                            GeneralMaster generalMaster = new GeneralMaster(object.getString("stateCode"), object.getString("stateName"));
                            stateListStr.add(generalMaster);
                            bindState();
                            isFirst=false;
                        }

                    }else {
                        // for District list
                        JSONArray arrDistrict = new JSONArray( obj.getString("districtList"));
                        if(arrDistrict.length()>0){
                            if(isFirstDist) {
                                distListStr.clear();
                                distListStr.add(0,new GeneralMaster("SELECT DISTRICT","SELECT DISTRICT"));

                                for (int i = 0; i < arrDistrict.length(); i++) {

                                    JSONObject object = new JSONObject(arrDistrict.get(i).toString());
                                    GeneralMaster generalMaster = new GeneralMaster(object.getString("districtCode"), object.getString("districtName").toUpperCase());

                                    distListStr.add(generalMaster);
                                }
                                isFirstDist=false;
                            }else{
                                // for taluka list
                                JSONArray arrTaluka = new JSONArray( obj.getString("talukaList"));
                                if(arrTaluka.length()>0){

//                                    if(isFirstTaluka) {
                                    talukaListStr.clear();
                                    talukaListStr.add(0,new GeneralMaster("SELECT TALUKA","SELECT TALUKA"));

                                    for (int i = 0; i < arrTaluka.length(); i++) {

                                        JSONObject object = new JSONObject(arrTaluka.get(i).toString());
                                        GeneralMaster generalMaster = new GeneralMaster(object.getString("talukaCode"), object.getString("talukaName").toUpperCase());

                                        talukaListStr.add(generalMaster);


                                    }
                                    // isFirstTaluka=false;
                                    //}

                                    // for focuss villagggge list
                                    JSONArray arrFocusVillage = new JSONArray( obj.getString("focussedVillageList"));
                                    if(arrFocusVillage.length()>0){
                                        focussedVillageListStr.clear();
                                        for (int i = 0; i < arrFocusVillage.length(); i++) {

                                            JSONObject object = new JSONObject(arrFocusVillage.get(i).toString());
                                            GeneralMaster generalMaster = new GeneralMaster(object.getString("villageCode"), object.getString("vil_desc").toUpperCase());

                                            focussedVillageListStr.add(generalMaster);


                                        }
                                    }

                                    // for focuss villagggge list
                                    JSONArray arrVillage = new JSONArray( obj.getString("regularVillageList"));
                                    if(arrVillage.length()>0){
                                        vilageListStr.clear();
                                        for (int i = 0; i < arrVillage.length(); i++) {

                                            JSONObject object = new JSONObject(arrVillage.get(i).toString());
                                            GeneralMaster generalMaster = new GeneralMaster(object.getString("villageCode"), object.getString("vil_desc").toUpperCase());

                                            vilageListStr.add(generalMaster);


                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            }else {
              //  msclass.showMessage(jsonObject.getString("Message"));
            }
            progressBarVisibility();
        } catch (JSONException e) {
            e.printStackTrace();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Are you sure to submit data");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            relPRogress.setVisibility(View.VISIBLE);
                            myTextProgress.setText("Uploading Data");
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

        JSONObject farmerCall = new JSONObject();


        try {

            farmerCall.put("id", "0");
            farmerCall.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
            farmerCall.put("farmerName", etFarmerName.getText());
            farmerCall.put("farmerMobile", etFarmerMob.getText());
            farmerCall.put("farmerType", spFarmerType.getSelectedItem());
            farmerCall.put("state", spState.getSelectedItem());
            farmerCall.put("district", spDist.getSelectedItem());
            farmerCall.put("taluka", spTaluka.getSelectedItem());
            GeneralMaster gv = (GeneralMaster) spVillage.getSelectedItem();
            GeneralMaster gfv = (GeneralMaster) spFocusedVillages.getSelectedItem();

            farmerCall.put("otherVillage", spVillage.getSelectedItem());
            if (!spFocusedVillages.getSelectedItem().toString().equalsIgnoreCase("SELECT FOCUSSED VILLAGE")) {
                farmerCall.put("focussedVillage", spFocusedVillages.getSelectedItem());
                farmerCall.put("villagecode", gfv.Code());
               /* farmerCall.put("state", "");
                farmerCall.put("district", "");
                farmerCall.put("taluka", "");
                farmerCall.put("otherVillage", ""); */


            }else {
                farmerCall.put("villagecode", gv.Code());
                farmerCall.put("focussedVillage", "");

            }

            farmerCall.put("totlaLand", etArea.getText());
            farmerCall.put("cropGrownByFarmer", spCropGrown.getSelectedItem());
            farmerCall.put("productUsedByFarmer", spProductUsed.getSelectedItem());


            farmerCall.put("CallTypeProductPromotion", calltypeProduct );
            farmerCall.put("CallTypeOtherActivity", calltypeOther );
            if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                croptype= "";
                farmerCall.put("CropDiscussed", croptype);
            }else {
                farmerCall.put("CropDiscussed", spCropType.getSelectedItem());
            }

            if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                productName="";
                farmerCall.put("ProductDiscussed", productName);
            }else {
                farmerCall.put("ProductDiscussed", spProductName.getSelectedItem());
            }
            farmerCall.put("farmerResponse", spFarmerResponse.getSelectedItem());
            farmerCall.put("callSummary", etCallSummary.getText());


            farmerCall.put("entryDt",   Function.getCurrentDate() );

            requestParams.put("Table", farmerCall);


        } catch (JSONException e) {
            e.printStackTrace();
            progressBarVisibility();
        }

        Log.d("FarmerData", requestParams.toString());
       // progressBarVisibility();

       new FarmerApiCall("FarmerData", requestParams).execute();

    }


    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class FarmerApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public FarmerApiCall(String function, JSONObject obj) {

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(FarmerCallActivity.this);
                                dialog.dismiss();
                                progressBarVisibility();

                                if(isFromTestimonial==null){
                                    return;
                                }
                                if(isFromTestimonial){
                                    Intent i = new Intent(FarmerCallActivity.this, TestimonialSharingActivity.class);
                                    isFromTestimonial=false;
                                     startActivity(i);
                                    finish();

                                }

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(FarmerCallActivity.this);
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
     * <P>Method to upload the   data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.FARMERCALL_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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
     * <P>Manage the progressbar visibility</P>
     */
    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container.setClickable(true);
        container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    /**
     * <P>//bind Village to spinner</P>
     * @param taluka
     * @param jsonObject1
     */
    public List<GeneralMaster> bindVillage(String taluka , final JSONObject jsonObject1) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";

            final List<GeneralMaster> list = new ArrayList<GeneralMaster>();
            if(!taluka.equals("SELECT TALUKA")) {
             //   String result = new CallApi(createJsonObject(userCode, state, dist, taluka)).execute().get();
                relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Please wait, bind village Data");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                CallApi callApi=new CallApi( createJsonObject(userCode, stateCode, dist, taluka), new AsyncResponse() {
                    @Override
                    public void processFinish(String callbackResponse) {
                        Log.d("Callback response ->", callbackResponse);
                        //process after callback
                        callBackResponseMethod(callbackResponse);
                        list.add(0,new GeneralMaster("SELECT VILLAGE",
                                "SELECT VILLAGE"));
                        list.addAll(vilageListStr);
                        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                                (getBaseContext(), android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spVillage.setAdapter(adapter);
                        if(isSearched){

                            for (int k = 0; k < vilageListStr.size(); k++) {
                                try {
                                    if (jsonObject1.getString("village").equalsIgnoreCase(vilageListStr.get(k).toString())) {

                                        spVillage.setSelection(k);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                          //  isSearched= false;
                        }
                     progressBarVisibility();


                    }
                });
                callApi.execute();//.get();

            }else {
                vilageListStr.clear();
                vilageListStr.add(0,new GeneralMaster("SELECT VILLAGE",
                        "SELECT VILLAGE"));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, vilageListStr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spVillage.setAdapter(adapter);
            }


        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
        return vilageList;
    }

    public List<GeneralMaster> bindFocussedVillage(String taluka) {
        spFocusedVillages.setAdapter(null);
        String str = null;
        try {

           final List<GeneralMaster> list = new ArrayList<GeneralMaster>();
            if(!taluka.equals("SELECT TALUKA")) {
              //  String result = new CallApi(createJsonObject(userCode, state, dist, taluka)).execute().get();
                relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Please wait, bind focus village Data");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                CallApi callApi=new CallApi( createJsonObject(userCode, state, dist, taluka), new AsyncResponse() {
                    @Override
                    public void processFinish(String callbackResponse) {
                        Log.d("Callback response ->", callbackResponse);
                        //process after callback
                        callBackResponseMethod(callbackResponse);
                        if(focussedVillageListStr.size()>1) {
                            list.add(0, new GeneralMaster("SELECT  FOCUSSED VILLAGE",
                                    "SELECT  FOCUSSED VILLAGE"));
                        }
                        list.addAll(focussedVillageListStr);
                        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                                (context, android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spFocusedVillages.setAdapter(adapter);

                    }
                });
                progressBarVisibility();
                callApi.execute();//.get();


            }else {
                focussedVillageListStr.clear();
                focussedVillageListStr.add(0,new GeneralMaster("SELECT  FOCUSSED VILLAGE",
                        "SELECT  FOCUSSED VILLAGE"));
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (context, android.R.layout.simple_spinner_dropdown_item, focussedVillageListStr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFocusedVillages.setAdapter(adapter);
            }

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
        return focussedVillageList;
    }

    private void bindFarmerResponse() {

        try {
            spFarmerResponse.setAdapter(null);

            try {
                ratingArray = getResources().getStringArray(R.array.rating5);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ratingArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFarmerResponse.setAdapter(adapter);

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


        spCropGrown.setListener(new MultiSelectionSpinner.MySpinnerListener() {

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
                Function.bindProductName(spProductUsed, null,croptype,getBaseContext(),mDatabase,msclass);
            }
        });

    }
    /**
     * <P>Method is used to validate the input values of activity</P>
     * @return
     */
    public boolean validation() {

        if (etFarmerName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }
        if (etFarmerMob.getText().length() == 0) {
            msclass.showMessage("Please enter mobile no");
            return false;
        }

        if (spFarmerType.getSelectedItem().toString().equalsIgnoreCase("SELECT FARMER TYPE")) {
            msclass.showMessage("Please select farmer type");
            return false;
        }
        if(!isSearched) {

            if (spState.getSelectedItemPosition() == 0) {
                    msclass.showMessage("Please select state");
                    return false;
                }
                if (spDist.getSelectedItemPosition() == 0) {
                    msclass.showMessage("Please select district");
                    return false;
                }
                if (spTaluka.getSelectedItemPosition() == 0) {
                    msclass.showMessage("Please select taluka");
                    return false;
                }
            if (spFocusedVillages.getSelectedItemPosition() == 0 && spVillage.getSelectedItemPosition() == 0 ) {
                msclass.showMessage("Please select village");
                return false;
            }
            if(spFocusedVillages.getSelectedItemPosition()!=0 && spVillage.getSelectedItemPosition()!=0){
                msclass.showMessage("Please select any one village,either  focus village or other village ");
                return false;
            }


        }

        if (etArea.getText().length() == 0) {
            msclass.showMessage("Please enter total land");
            return false;
        }

        if (spCropGrown.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
            msclass.showMessage("Please select crop grown");
            return false;
        }
        if (spProductUsed.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

            msclass.showMessage("Please select product used");
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

                msclass.showMessage( "Please Select  Product Name");
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

        if (spFarmerResponse.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select farmer response");
            return false;
        }

        if (etCallSummary.getText().length() == 0) {
            msclass.showMessage("Please enter call summary");
            return false;
        }

        return true;
    }


}
