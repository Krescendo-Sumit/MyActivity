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
import com.google.android.material.textfield.TextInputLayout;
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
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class TestimonialSharingActivity extends AppCompatActivity {


    private static final String TAG = "TESTIMONIALSHARING";
    SearchableSpinner spSharingMode, spSharedContent, spSharedContentIndi,spState, spDist, spTaluka,spVillage,spFocusedVillages;

    private String croptype, croptypeIndi, productName, productNameIndi, sharedContent, sharedContentindi,state, dist, taluka;
    MultiSelectionSpinner spProductName,spCropType, spProductNameIndi,spCropTypeIndi,spCropGrown,spProductUsed;
    TextInputLayout tiOtherIndi, tiOtherGrp;
    TextView txtOtherVillage,txtFocussedVillage,txtTaluka,txtDist,txtState,myTextProgress;
    Button btnSubmit,btnSearch;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etFarmerGrpName, etFarmerNo,etOther,etOther1,etName,etMob,etArea ;
    String[]  categoryArray, contentArray, modeArray;
    RadioGroup radGroupActivity;
    RadioButton radGroupFarmer, radIndividualFarmer;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private Context context;
    LinearLayout llGrpFarmer,llIndividualFarmer,llOtherVillages,llFocussedVillages ;
    private long mLastClickTime = 0;
    String userCode;
    Config config;
    Prefs mPref;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial_sharing);

        initUI();
    }

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
        txtOtherVillage = findViewById(R.id.txtOtherVillage);
        txtFocussedVillage = findViewById(R.id.txtFocussedVillage);
        txtTaluka = findViewById(R.id.txtTaluka);
        txtDist = findViewById(R.id.txtDist);
        txtState = findViewById(R.id.txtState);
        etArea = findViewById(R.id.etArea);

        llFocussedVillages = findViewById(R.id.llFocussedVillages);
        llOtherVillages = findViewById(R.id.llOtherVillages);
        etFarmerGrpName = findViewById(R.id.etFarmerGrpName);
        etFarmerNo = findViewById(R.id.etFarmerNo);
        etOther = findViewById(R.id.etOther);
        etOther1 = findViewById(R.id.etOther1);
        etMob = findViewById(R.id.etMob);
        etName = findViewById(R.id.etName);
        tiOtherIndi = findViewById(R.id.tiOtherIndi);
        tiOtherGrp = findViewById(R.id.tiOtherGrp);

        llGrpFarmer = findViewById(R.id.llGrpFarmer);
        llIndividualFarmer = findViewById(R.id.llIndividualFarmer);
        radGroupFarmer = findViewById(R.id.radGroupFarmer);
        radIndividualFarmer = findViewById(R.id.radIndividualFarmer);
        radGroupActivity = findViewById(R.id.radGroupActivity);

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);

        spSharingMode = (SearchableSpinner) findViewById(R.id.spSharingMode);
        spSharedContent = (SearchableSpinner) findViewById(R.id.spSharedContent);
        spSharedContentIndi = (SearchableSpinner) findViewById(R.id.spSharedContentIndi);
        spProductName = findViewById(R.id.spProductName);
        spCropType = findViewById(R.id.spCropType);
        spProductNameIndi = findViewById(R.id.spProductNameIndi);
        spCropTypeIndi = findViewById(R.id.spCropTypeIndi);
        spCropGrown = findViewById(R.id.spCropGrown);
        spProductUsed = findViewById(R.id.spProductUsed);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);

        bindModesharing();
        bindSharedContent();
        Function.bindcroptype(spCropType, spCropTypeIndi, "C",this, mDatabase,msclass);
        Function.bindProductName(spProductName, spProductNameIndi,"", this,mDatabase,msclass);

        Function.bindcroptype(spCropGrown, "C",this, mDatabase,msclass);
        Function.bindProductName(spProductUsed,"", this,mDatabase,msclass);

     //   Function.bindState(spState,mDatabase, this,msclass);
       // bindFocussedVillage("");

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radIndividualFarmer:

                        llIndividualFarmer.setVisibility(View.VISIBLE);
                        llGrpFarmer.setVisibility(View.GONE);
                        radGroupFarmer.setChecked(false);
                        break;
                    case R.id.radGroupFarmer:
                        llGrpFarmer.setVisibility(View.VISIBLE);
                        llIndividualFarmer.setVisibility(View.GONE);
                        radIndividualFarmer.setChecked(false);
                        break;
                }
            }
        });


        spSharedContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    sharedContent = (String) parent.getSelectedItem();
                    if(sharedContent.equals("OTHERS")){
                        tiOtherGrp.setVisibility(View.VISIBLE);
                    }else {
                        tiOtherGrp.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        spSharedContentIndi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    sharedContentindi = (String) parent.getSelectedItem();
                    if(sharedContentindi.equals("OTHERS")){
                        tiOtherIndi.setVisibility(View.VISIBLE);
                        sharedContentindi = etOther1.getText().toString();
                    }else {
                        tiOtherIndi.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        onCropItemSelected();
        onSubmitBtn();
        onSearchBtn();

    }

    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);

        String str = null;
        try {

            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));

            cursor = mDatabase.getReadableDatabase().

                    rawQuery(searchQuery, null);
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


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }


    public void bindFocussedVillage(String focussedVillage) {
        spFocusedVillages.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
            Croplist.add(new GeneralMaster("SELECT FOCUSED VILLAGE",
                    "SELECT FOCUSED VILLAGE"));


            cursor = mDatabase.getReadableDatabase().
                    rawQuery(searchQuery, null);
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
            spFocusedVillages.setAdapter(adapter);

        } catch (
                Exception ex) {
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

                            doSearch();

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            container.setEnabled(false);
                            container.setClickable(false);

                }
            }
        });
    }

    private boolean validationSearch() {

        if (etName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }
        if (etMob.getText().length() == 0) {
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
            searchDetail.put("farmerName", etName.getText());
            searchDetail.put("mobileNumber", etMob.getText());


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
//

                        if(jsonObject.has("message") && jsonObject.getString("message").equals("No activities found"))
                        {
                            Intent i = new Intent(TestimonialSharingActivity.this, FarmerCallActivity.class);
                            i.putExtra("FromTestimonial", true);
                             startActivity(i);
                        }else
                        {
                            getSearchData(jsonObject);

                        }
                            progressBarVisibility();
                   } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
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

        }

    }

    private void getSearchData(JSONObject jsonObject) {

        try {

            JSONArray array = new JSONArray(jsonObject.getString("table"));
            JSONObject jsonObject1 = new JSONObject(array.get(0).toString());

           txtState.setText(jsonObject1.getString("state"));
           txtDist.setText(jsonObject1.getString("district"));
           txtTaluka.setText(jsonObject1.getString("taluka"));
           txtOtherVillage.setText(jsonObject1.getString("village"));
           txtFocussedVillage.setText(jsonObject1.getString("focussedVillage"));
            etArea.setText(jsonObject1.getString("totalLand"));

            Function.bindcroptype(spCropGrown, spCropTypeIndi,"C",this,mDatabase,msclass);

            String[] array1 ;
            List<String> croplst =  new ArrayList<String>();
            List<String> prodctlst =  new ArrayList<String>();

            List<String> croplstIndi =  new ArrayList<String>();
            List<String> prodctlstIndi =  new ArrayList<String>();
           final String[] array2;
            String[] arrayIndi1;
            String[] arrayIndi2;
            try {
                if(!jsonObject1.getString("cropGrownByFarmer").isEmpty()) {
                    String cropl = jsonObject1.getString("cropGrownByFarmer").toString();
                    Function.bindProductName(spProductUsed, "[" + cropl + "]", this, mDatabase, msclass);
                    String productlist = jsonObject1.getString("productUsedByFarmer").toString();
                    array1 = cropl.split(",");
                    array2 = (productlist.split(","));


                    for (String s : array1) {
                        croplst.add(s.trim());

                    }
                    for (String s : array2) {
                        prodctlst.add(s.trim());

                       }
                   spCropGrown.setSelection(croplst);
                    spProductUsed.setSelection(prodctlst);
                }

                if(!jsonObject1.getString("crop").isEmpty()) {
                    String cropl = jsonObject1.getString("crop").toString();
                    Function.bindProductName(spProductNameIndi, "[" + cropl + "]", this, mDatabase, msclass);
                    String productlist = jsonObject1.getString("product").toString();
                    arrayIndi1 = cropl.split(",");
                    arrayIndi2 = productlist.split(",");

                    for (String s : arrayIndi1) {
                        croplstIndi.add(s.trim());

                    }
                    for (String s : arrayIndi2) {
                        prodctlstIndi.add(s.trim());

                    }
                    spCropTypeIndi.setSelection(croplstIndi);
                    spProductNameIndi.setSelection(prodctlstIndi);


                }




            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        spCropTypeIndi.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ", String.valueOf(strings));
                croptypeIndi = String.valueOf(strings);
                Function.bindProductName(spProductName, spProductNameIndi,croptypeIndi,getBaseContext(),mDatabase,msclass);
            }
        });



        spProductNameIndi.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                productNameIndi = String.valueOf(strings);

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
                Function.bindProductName(spProductUsed,croptype,getBaseContext(),mDatabase,msclass);
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







    }


    private void bindSharedContent() {

        try {
            spSharedContent.setAdapter(null);
            spSharedContentIndi.setAdapter(null);

            try {
                contentArray = getResources().getStringArray(R.array.sharedContent);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contentArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSharedContent.setAdapter(adapter);
                spSharedContentIndi.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }




    }

    private void bindModesharing() {

        try {
            spSharingMode.setAdapter(null);

            try {
                modeArray = getResources().getStringArray(R.array.modeSharing);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modeArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSharingMode.setAdapter(adapter);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);

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

    private boolean validation() {

        if(radIndividualFarmer.isChecked()) {

            if (etName.getText().length() == 0) {
                msclass.showMessage("Please enter farmer name");
                return false;
            }
            if (etMob.getText().length() == 0) {
                msclass.showMessage("Please enter mobile no");
                return false;
            }

            if (spSharedContentIndi.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select shared content");
                return false;
            }


            if(!spSharedContentIndi.getSelectedItem().equals("OTHERS")){

                if (spCropTypeIndi.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                    msclass.showMessage("Please select crop type");
                    return false;
                }
                if (spProductNameIndi.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

                    msclass.showMessage("Please select product name");
                    return false;
                }
            }else {
                if (etOther1.getText().length() == 0) {
                    msclass.showMessage("Please enter shared content");
                    return false;
                }
            }

            if (etArea.getText().length() == 0) {
                msclass.showMessage("Please enter total land");
                return false;
            }

            if (spCropGrown.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                msclass.showMessage("Please select crop type");
                return false;
            }
            if (spProductUsed.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

                msclass.showMessage("Please select product name");
                return false;
            }
        }
        if(radGroupFarmer.isChecked()){

            if (spSharingMode.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select sharing mode");
                return false;
            }
            if (etFarmerGrpName.getText().length() == 0) {
                msclass.showMessage("Please enter farmer group name");
                return false;
            }
            if (etFarmerNo.getText().length() == 0) {
                msclass.showMessage("Please enter no of farmers");
                return false;
            }

            if (spSharedContent.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please select shared content");
                return false;
            }
            if(!spSharedContent.getSelectedItem().equals("OTHERS")){

                if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                    msclass.showMessage("Please select crop type");
                    return false;
                }
                if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {

                    msclass.showMessage("Please select product name");
                    return false;
                }
            } else {
                if (etOther.getText().length() == 0) {
                    msclass.showMessage("Please enter shared content");
                    return false;
                }
            }

        }

     return  true;
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
        JSONObject farmerDetail = new JSONObject();

        if(radIndividualFarmer.isChecked()) {

          requestParams = uploadIndividualData(farmerDetail, requestParams);
        }else if(radGroupFarmer.isChecked()){

           requestParams= uploadGroupData(farmerDetail, requestParams);
        }

        Log.d("FarmerDetail", requestParams.toString());
       // progressBarVisibility();

       new TestimonialApiCall("FarmerDetail", requestParams).execute();

    }

    private JSONObject uploadGroupData(JSONObject farmerDetail, JSONObject requestParams) {
        try {

            farmerDetail.put("id", "0");
            farmerDetail.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            farmerDetail.put("Type", radGroupFarmer.getText());
            farmerDetail.put("sharingMode", spSharingMode.getSelectedItem());
            farmerDetail.put("farmerGroupName", etFarmerGrpName.getText());
            farmerDetail.put("farmerTotalNumber", etFarmerNo.getText());

            if(spSharedContent.getSelectedItem().equals("OTHERS")) {
                farmerDetail.put("sharedContentGroup", etOther.getText());
            }else {
                farmerDetail.put("sharedContentGroup", spSharedContent.getSelectedItem());
            }

            if (spCropType.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                farmerDetail.put("cropTypeGroup", "");
            }else {
                farmerDetail.put("cropTypeGroup", spCropType.getSelectedItem());
            }
            if (spProductName.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                farmerDetail.put("productNameGroup", "");

            }
            else {
                farmerDetail.put("productNameGroup", spProductName.getSelectedItem());
            }

            farmerDetail.put("sharedContentIndividual", "");
            farmerDetail.put("cropTypeIndividual", "");
            farmerDetail.put("produtNameIndividual", "");
            farmerDetail.put("farmerName", "");
            farmerDetail.put("farmerMobile", "");
            farmerDetail.put("state", "");
            farmerDetail.put("district", "");
            farmerDetail.put("taluka", "");
            farmerDetail.put("focussedVillage", "");
            farmerDetail.put("otherVillage", "");
            farmerDetail.put("totlaLand", "");
            farmerDetail.put("cropGrownByFarmer", "");
            farmerDetail.put("productUsedByFarmer", "");

            farmerDetail.put("entryDt", Function.getCurrentDate());


            requestParams.put("Table", farmerDetail);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  requestParams;

    }

    private JSONObject uploadIndividualData(JSONObject farmerDetail, JSONObject requestParams) {

        try {

            farmerDetail.put("id", "0");
            farmerDetail.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG, ""));
            farmerDetail.put("Type", radIndividualFarmer.getText());

            if(spSharedContentIndi.getSelectedItem().equals("OTHERS")) {
                farmerDetail.put("sharedContentIndividual", etOther1.getText());
            }else {
                farmerDetail.put("sharedContentIndividual", spSharedContentIndi.getSelectedItem());
            }

            if (spCropTypeIndi.getSelectedItem().toString().equalsIgnoreCase("SELECT CROP")) {
                farmerDetail.put("cropTypeIndividual", "");
            }else {
                farmerDetail.put("cropTypeIndividual", spCropTypeIndi.getSelectedItem());
            }
            if (spProductNameIndi.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT")) {
                farmerDetail.put("produtNameIndividual", "");

            }
            else {
                farmerDetail.put("produtNameIndividual", spProductNameIndi.getSelectedItem());

            }


            farmerDetail.put("farmerName", etName.getText());
            farmerDetail.put("farmerMobile", etMob.getText());
            farmerDetail.put("state", txtState.getText());
            farmerDetail.put("district", txtDist.getText());
            farmerDetail.put("taluka", txtTaluka.getText());
            farmerDetail.put("focussedVillage", txtFocussedVillage.getText());
            farmerDetail.put("otherVillage", txtOtherVillage.getText());
            farmerDetail.put("totlaLand", etArea.getText());
            farmerDetail.put("cropGrownByFarmer", spCropGrown.getSelectedItem());
            farmerDetail.put("productUsedByFarmer", spProductUsed.getSelectedItem());

            farmerDetail.put("sharingMode", "");
            farmerDetail.put("farmerGroupName", "");
            farmerDetail.put("farmerTotalNumber", "");
            farmerDetail.put("sharedContentGroup", "");
            farmerDetail.put("cropTypeGroup", "");
            farmerDetail.put("productNameGroup", "");


            farmerDetail.put("entryDt", Function.getCurrentDate());


            requestParams.put("Table", farmerDetail);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  requestParams;

    }


    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class TestimonialApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public TestimonialApiCall(String function, JSONObject obj) {

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(TestimonialSharingActivity.this);
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialSharingActivity.this);
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
    private String uploadData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.TESTIMONIAL_SHARING_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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


}
