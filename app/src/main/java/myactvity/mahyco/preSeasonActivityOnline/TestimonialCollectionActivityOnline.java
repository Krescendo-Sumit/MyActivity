package myactvity.mahyco.preSeasonActivityOnline;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
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
import myactvity.mahyco.UserRegister;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class TestimonialCollectionActivityOnline extends AppCompatActivity {

    CheckBox chkBoxHardCopy, chkBoxVideo;
    SearchableSpinner spState, spDist, spTaluka, spVillage, spFocusedVillages;
    private String state, dist, croptype, taluka, focussedVillage;
    Button btnFarmerPhoto, btnSuccessStoryPhoto, btnSubmit;
    EditText edtFarmerName, edtFarmerMobile;
    LinearLayout llOtherVillages, llFocussedVillages;
    ImageView ivImage, ivImage2;
    private int imageselect;
    public String Imagepath1 = "";
    public String Imagepath2 = "";
    public String userCode = "";
    private static final String IMAGE_DIRECTORY_NAME = "TESTIMONIAL_COLLECTION";
    File photoFile = null;
    File photoFile2 = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    private Context context;
    Config config;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    List<GeneralMaster> stateList, distList, talukaList, vilageList, focussedVillageList;
    private long mLastClickTime = 0;
    RelativeLayout relPRogress;
    ScrollView container;
    ProgressBar progressBar;
    private Handler handler = new Handler();
    Dialog dialog;
   // String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    String SERVER = "https://maapackhousenxg.mahyco.com/api/preseason/testimonial";
    private final static HttpClient mHhttpclient = new DefaultHttpClient();
    Prefs mPref;
    RadioGroup radGroupActivity;
    RadioButton radOffline;
    RadioButton radOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial_collection_online);

        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        mPref = Prefs.with(this);
        chkBoxHardCopy = (CheckBox) findViewById(R.id.chkBoxHardCopy);
        chkBoxVideo = (CheckBox) findViewById(R.id.chkBoxVideo);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnFarmerPhoto = (Button) findViewById(R.id.btnFarmerPhoto);
        btnSuccessStoryPhoto = (Button) findViewById(R.id.btnSuccessStoryPhoto);
        llOtherVillages = (LinearLayout) findViewById(R.id.llOtherVillages);
        edtFarmerName = (EditText) findViewById(R.id.edtFarmerName);
        edtFarmerMobile = (EditText) findViewById(R.id.edtFarmerMobile);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spFocusedVillages = (SearchableSpinner) findViewById(R.id.spFocusedVillages);
        msclass = new Messageclass(this);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
       // userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        userCode =  pref.getString("UserID", null);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radOffline = (RadioButton) findViewById(R.id.radOffline);
        radOnline = (RadioButton) findViewById(R.id.radOnline);
        llFocussedVillages = (LinearLayout) findViewById(R.id.llFocussedVillages);

       // bindFocussedVillage();

        bindState();

        onBtnSubmitClicked();

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radOffline:

                        if (radOffline.isChecked()) {
                            // villageType = "focussed";
                            // bindFocussedVillage(spVillage);
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        } else {
                            // villageType = "other";
                            bindState();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        }

                        radOnline.setChecked(false);
                        break;
                    case R.id.radOnline:
                        if (radOffline.isChecked()) {
                            //   villageType = "other";
                            bindState();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        } else {
                            //  villageType = "focussed";
                            //  bindFocussedVillage(spVillage);
                            bindFocussedVillage();
                            llFocussedVillages.setVisibility(View.VISIBLE);
                            llOtherVillages.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });


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
                if (radOnline.isChecked())
                {
                    bindVillageApi();
                }
                else {
                    bindVillage(taluka);
                    bindFocussedVillage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spVillage .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                   spFocusedVillages.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spFocusedVillages .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    spVillage.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnFarmerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TestimonialCollectionActivityOnline.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 1;
                try {
                    //selectImage();
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
        });

        btnSuccessStoryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TestimonialCollectionActivityOnline.this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 2;
                try {
                    //selectImage();
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
        });


    }

    /**
     * <P>Method is used to do API related work on submit button clicked</P>
     */
    private void onBtnSubmitClicked() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialCollectionActivityOnline.this);

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
     * <P>// Validation for all fields</P>
     * @return
     */
    public boolean validation() {

        if (chkBoxHardCopy.isChecked() == false && chkBoxVideo.isChecked() == false) {
            msclass.showMessage("Please Select Testimonial Type");
            return false;
        }

       /* if (radFocusedActivity.isChecked() && radOtherActivity.isChecked()) {
            msclass.showMessage("Please Select Village");
            return false;
        }*/

       // if (radOtherActivity.isChecked())
        {

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
            if (spVillage.getSelectedItemPosition() == 0 && spFocusedVillages.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select village name or Focussed Village name.");
                return false;
            }
        }

        if (edtFarmerName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }
        if (edtFarmerMobile.getText().length() == 0) {

            msclass.showMessage("Please enter farmer mobile number");
            return false;
        }
        if(edtFarmerMobile.getText().length()<10) {
            msclass.showMessage("Mobile number should be of 10 digits");
            return false;
        }

        if (chkBoxHardCopy.isChecked()) {
            if (ivImage2.getDrawable() == null) {
                msclass.showMessage("Please click success story image");
                return false;
            }
        }
        if (ivImage.getDrawable() == null) {
            msclass.showMessage("Please Click Farmer Photo");
            return false;
        }
      /* if (!isAlreadydone(edtFarmerMobile.getText().toString())) {
           Utility.showAlertDialog("Info", "This farmer testimonial entry already exists", context);
           return false;
        }*/

        return true;
    }
    //if mob no already exist
    private boolean isAlreadydone(String retailerNumber) {
        boolean isExist = false;
        Cursor data = mDatabase.fetchAlreadyExistsTestimonialCollectionData(retailerNumber);
        if (data.getCount() == 0) {
            isExist = true;
        }
        data.close();
        return isExist;
    }


    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                stateList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                stateList.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    stateList.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, stateList);
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
                distList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";
                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                distList.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    distList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, distList);
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
            String str = null;
            try {
                talukaList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                talukaList.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    talukaList.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, talukaList);
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


    /**
     * <P>//bind Village to spinner</P>
     * @param taluka
     */
    public  void bindVillageApi()
    {
        if (config.NetworkConnection()) {
            new GetVillageList("", this).execute();
        }
        else
        {
            msclass.showMessage("Internet connection not available");
        }

    }
    public void bindFocussedVillage() {
        spFocusedVillages.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            focussedVillageList = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code " +
                    " FROM FocussedVillageMaster where upper(taluka)='"+taluka.toUpperCase()+"' order by vil_desc asc  ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            focussedVillageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));
            cursor = mDatabase.getReadableDatabase().
                    rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                focussedVillageList.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (context, android.R.layout.simple_spinner_dropdown_item, focussedVillageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFocusedVillages.setAdapter(adapter);
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }
    public void bindVillageOnline(String result ) {
        spVillage.setAdapter(null);
        spFocusedVillages.setAdapter(null);
        try {
            vilageList = new ArrayList<GeneralMaster>();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jArray = jsonObject.getJSONArray("Table");
            vilageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));
            vilageList.add(new GeneralMaster("OTHER",
                    "OTHER"));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                vilageList.add(new GeneralMaster(jObject.getString("name").toString(),
                        jObject.getString("name").toString()));
            }
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, vilageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);

            // Bind Focus Village list

            focussedVillageList = new ArrayList<GeneralMaster>();
            JSONObject jsonObject1 = new JSONObject(result);
            JSONArray jArray1 = jsonObject.getJSONArray("Table1");
            focussedVillageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));
            focussedVillageList.add(new GeneralMaster("OTHER",
                    "OTHER"));
            for (int i = 0; i < jArray1.length(); i++) {
                JSONObject jObject = jArray1.getJSONObject(i);
                focussedVillageList.add(new GeneralMaster(jObject.getString("vil_code").toString(),
                        jObject.getString("vil_desc").toString()));
            }
            ArrayAdapter<GeneralMaster> adapter1 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, focussedVillageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFocusedVillages.setAdapter(adapter1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Call to bind Village Spinner
                //JSONObject object = new JSONObject(result);

    }
    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            vilageList = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster " +
                    " where upper(taluka)='" + taluka.toUpperCase()+ "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            vilageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));
            vilageList.add(new GeneralMaster("OTHER",
                    "OTHER"));
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                vilageList.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, vilageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }


    //Get Online Village List
    private class GetVillageList extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public GetVillageList(String Funname, Context context) {

        }

        protected void onPreExecute() {
            progressDailog = new ProgressDialog(context);
            progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDailog.setCanceledOnTouchOutside(false
            );
            progressDailog.setCancelable(false);
            progressDailog.setMessage("Data Uploading");
            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("taluka", taluka);
                jsonObject.put("Table", jsonParam);
                Log.d("Village List ", jsonObject.toString());

                //Get the Response for the request
                return HttpUtils.POSTJSON(Constants.getVillageListByTaluka_SERVER_API,jsonObject,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));



            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {

            if (progressDailog != null) {
                progressDailog.dismiss();
            }
            redirecttoRegisterActivity(result);
            //Staet
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (resultout.contains("True")) {
                    if (resultout.contains("True")) {

                     // Call to bind Village Spinner
                        //JSONObject object = new JSONObject(result);

                        //JSONArray jArray = object.getJSONArray("Table");
                        bindVillageOnline(resultout);
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                          //  progressBarVisibility();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //end


        }

    }

    private void alertPoorConnection() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
        builder.setTitle("MyActivity");
        builder.setMessage("Poor Internet: Please try after sometime.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //progressBarVisibility();
            }
        });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
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
    // Camera Functions
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

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //   startFusedLocationService();
        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }
        if (ivImage.getDrawable() != null) {
            ivImage.setVisibility(View.VISIBLE);
        } else {
            ivImage.setVisibility(View.GONE);
        }
        if (ivImage2.getDrawable() != null) {
            ivImage2.setVisibility(View.VISIBLE);
        } else {
            ivImage2.setVisibility(View.GONE);
        }
    }

    @Override
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
    }

    private void onCaptureImageResult(Intent data) {
        try {
            if (imageselect == 1) {
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
            ivImage2.setImageBitmap(bm);
        }
    }

    //////////////////////////////
    /**
     * <P>Method to save the data to DB</P>
     */
    public void saveToDb() {
        String focussedVillage = "";
        String state = "";
        String district = "";
        String taluka = "";
        String othervillage = "", farmerCount = "";
        String villagecode="";
        if (spFocusedVillages.getSelectedItemPosition() != 0) {
            focussedVillage = spFocusedVillages.getSelectedItem().toString();
            villagecode=config.getvalue(spFocusedVillages);
        }
        if (spVillage.getSelectedItemPosition() != 0) {
            othervillage = spVillage.getSelectedItem().toString();
            villagecode=config.getvalue(spVillage);
        }


            state = spState.getSelectedItem().toString();
            district = spDist.getSelectedItem().toString();
            taluka = spTaluka.getSelectedItem().toString();
            othervillage = spVillage.getSelectedItem().toString();

        String hardCopyTestimonialType = "", videoTestimonialType = "";

        if (chkBoxHardCopy.isChecked() == true) {
            hardCopyTestimonialType = "HARDCOPY / SUCCESS STORY";
        } else {
            hardCopyTestimonialType = "";
        }
        if (chkBoxVideo.isChecked() == true) {
            videoTestimonialType = "TESTIMONIAL VIDEO";
        } else {
            videoTestimonialType = "";
        }
        String farmerName = edtFarmerName.getText().toString();
        String farmerMobile = edtFarmerMobile.getText().toString();

        String isSynced = "0";
        String farmerPhotoStatus = "0", successPhotoStatus = "0";

        Date entrydate = new Date();
        final String farmerPhotoPath, successPhotoPath;
        farmerPhotoPath =Imagepath1;

        successPhotoPath = Imagepath2;

        final String farmerPhotoName = "TestimonialCollectionFarmerPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        final String successPhotoName = "TestimonialCollectionSuccessPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        boolean fl = mDatabase.insertTestimonialCollectionData(userCode, focussedVillage, state, district, taluka, othervillage,
                hardCopyTestimonialType, videoTestimonialType, farmerName, farmerMobile, farmerPhotoName, Imagepath1,
                farmerPhotoStatus, successPhotoName, Imagepath2, successPhotoStatus, isSynced,villagecode);

        if (fl) {
            uploadData("TestimonialCollectionData");
          //  msclass.showMessage("data saved successfully.");
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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

    public void uploadData(String TestimonialCollectionData) {
        String str = null;
       /* if (config.NetworkConnection()) {
            try {
                new UploadTestimonialCollectionDataServer(TestimonialCollectionData, context).execute(SERVER).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else */{
            AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                   Config.refreshActivity(TestimonialCollectionActivityOnline.this);
                    Config.refreshActivity(TestimonialCollectionActivityOnline.this);
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

    public String uploadTestimonialCollectionData(String testimonialCollectionData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from TestimonialCollectionData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String farmerPhotoName = jsonArray.getJSONObject(i).getString("farmerPhotoName");
                    String farmerPhotoPath = jsonArray.getJSONObject(i).getString("farmerPhotoPath");

                    jsonArray.getJSONObject(i).put("farmerPhotoPath",  mDatabase.getImageDatadetail(farmerPhotoPath));

                    String successPhotoName = jsonArray.getJSONObject(i).getString("successPhotoName");
                    String successPhotoPath = jsonArray.getJSONObject(i).getString("successPhotoPath");

                    jsonArray.getJSONObject(i).put("successPhotoPath",  mDatabase.getImageDatadetail(successPhotoPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                   jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("TestimonialData", jsonObject.toString());
                    str = syncTestimonialCollectionDataSingleImage(jsonObject);

                    handleTestimonialCollectionDataImageSyncResponse("TestimonialCollectionData", str,id);
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
    private class UploadTestimonialCollectionDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadTestimonialCollectionDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadTestimonialCollectionData("TestimonialCollectionData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                if(jsonObject.has("success"))
                {
                    if(Boolean.parseBoolean(jsonObject.get("success").toString())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(TestimonialCollectionActivityOnline.this);
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
                }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestimonialCollectionActivityOnline.this);
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

    /**
     *
     * @param jsonObject
     * @return
     */
    public synchronized String syncTestimonialCollectionDataSingleImage(JSONObject jsonObject) {

//        String encodedfarmerPhotoPath = mDatabase.getImageDatadetail(farmerPhotoPath);
//        String encodedsuccessPhotoPath = mDatabase.getImageDatadetail(successPhotoPath);

        return HttpUtils.POSTJSON(SERVER,jsonObject,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }


    /**
     * <P>Method to update the testimonial collection data after success</P>
     * @param function
     * @param resultout
     * @throws JSONException
     */
    public void handleTestimonialCollectionDataImageSyncResponse(String function, String resultout,String id ) throws JSONException {
        if (function.equals("TestimonialCollectionData")) {

            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateTestimonialCollectionData("0", "1",
                            "1", "1",id);
                } else {
                }
            }
            Log.d("TestimonialData", "TestimonialCollectionData: " + resultout);
        }
    }
    ///////////////////
}
