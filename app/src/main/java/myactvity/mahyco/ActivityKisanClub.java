package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class ActivityKisanClub extends AppCompatActivity {

    Context context;
    private long mLastClickTime = 0;

    private static final String TAG = "ActivityRetailerSurvey";
    SearchableSpinner spState, spDist, spTerritory, spVillage, spTehsil;
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
 //   ProgressDialog dialog;

    Config config;
    Switch switchYN;
    Button btnSubmit;
    MultiSelectionSpinner spCropSown;
    EditText etArea, etWhatsappNumber, etFarmerName, etMobileNo,etComments;
    String userCode, state, taluka, dist;
    RadioGroup radGroupUnit, radGroupVeg, radGroupAware, radGroupPerMAhyco, radGroupPerSungro;
    RadioButton radVEGBU, radRCBU, radYes, radNo, radMahyco, radSungro, radBoth, radNone, radZero, radOne, radTwo, radSunZero, radSunOne, radSunTwo;
    String unitType = "", territory;
    String reply;
    String ratingMahyco;
    String ratingSungro;
    String productAwareness;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_kisan_club);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTehsil = (SearchableSpinner) findViewById(R.id.spTehsil);
        spTerritory = (SearchableSpinner) findViewById(R.id.spTerritory);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spCropSown = (MultiSelectionSpinner) findViewById(R.id.spCropSown);
        radGroupUnit = (RadioGroup) findViewById(R.id.radGroupUnit);


        radGroupVeg = (RadioGroup) findViewById(R.id.radGroupVeg);
        radGroupAware = (RadioGroup) findViewById(R.id.radGroupAware);
        radGroupPerMAhyco = (RadioGroup) findViewById(R.id.radGroupPerMAhyco);
        radGroupPerSungro = (RadioGroup) findViewById(R.id.radGroupPerSungro);
        switchYN = (Switch) findViewById(R.id.switchYN);


        radVEGBU = (RadioButton) findViewById(R.id.radVEGBU);
        radRCBU = (RadioButton) findViewById(R.id.radRCBU);
        radYes = (RadioButton) findViewById(R.id.radYes);
        radNo = (RadioButton) findViewById(R.id.radNo);
        radMahyco = (RadioButton) findViewById(R.id.radMahyco);
        radSungro = (RadioButton) findViewById(R.id.radSungro);
        radBoth = (RadioButton) findViewById(R.id.radBoth);
        radNone = (RadioButton) findViewById(R.id.radNone);
        radZero = (RadioButton) findViewById(R.id.radZero);
        radOne = (RadioButton) findViewById(R.id.radOne);
        radTwo = (RadioButton) findViewById(R.id.radTwo);
        radSunZero = (RadioButton) findViewById(R.id.radSunZero);
        radSunOne = (RadioButton) findViewById(R.id.radSunOne);
        radSunTwo = (RadioButton) findViewById(R.id.radSunTwo);
        etArea = (EditText) findViewById(R.id.etArea);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etComments= (EditText) findViewById(R.id.etComments);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
//
//        dialog = new ProgressDialog(context);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       userCode = pref.getString("UserID", null);


        bindState();
        bindVegCrops();


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
                bindTerritory(state);

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


                bindTehsil(dist);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spTehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    territory = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spCropSown.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {


                if (indices.size() > 3) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKisanClub.this);

                    builder.setTitle("Info");
                    builder.setMessage("Please select only three Crops");
                    builder.setCancelable(false);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            spCropSown.performClick();
                        }
                    });


                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });

        radGroupUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radVEGBU:

                        if (radVEGBU.isChecked()) {
                            unitType = "VEGBU";

                        }
                        radRCBU.setChecked(false);
                        break;
                    case R.id.radRCBU:
                        if (radVEGBU.isChecked()) {

                            unitType = "VEGBU";
                        } else {
                            unitType = "RCBU";

                        }
                        radVEGBU.setChecked(false);
                        break;


                }
                Log.d("unitType", unitType);
            }
        });


        radGroupVeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radYes:

                        if (radYes.isChecked()) {
                            reply = "Yes";

                        }
                        radNo.setChecked(false);
                        break;
                    case R.id.radNo:
                        if (radYes.isChecked()) {

                            reply = "Yes";
                        } else {
                            reply = "No";

                        }
                        radYes.setChecked(false);
                        break;


                }
                Log.d("reply", reply);
            }
        });
        radGroupPerMAhyco.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radZero:

                        if (radZero.isChecked()) {
                            ratingMahyco = "0";
                        } else if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else {
                            ratingMahyco = "2";

                        }
                        radOne.setChecked(false);
                        radTwo.setChecked(false);
                        break;
                    case R.id.radOne:
                        if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else if (radTwo.isChecked()) {
                            ratingMahyco = "2";
                        } else {
                            ratingMahyco = "0";

                        }
                        radZero.setChecked(false);
                        radTwo.setChecked(false);
                        break;
                    case R.id.radTwo:
                        if (radTwo.isChecked()) {
                            ratingMahyco = "2";
                        } else if (radOne.isChecked()) {
                            ratingMahyco = "1";
                        } else {
                            ratingMahyco = "0";

                        }
                        break;

                }
                Log.d("ratingMahyco", ratingMahyco);
            }
        });
        radGroupPerSungro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radSunZero:

                        if (radSunZero.isChecked()) {
                            ratingSungro = "0";
                        } else if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else {
                            ratingSungro = "2";

                        }
                        radSunOne.setChecked(false);
                        radSunTwo.setChecked(false);
                        break;
                    case R.id.radSunOne:
                        if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else if (radSunTwo.isChecked()) {
                            ratingSungro = "2";
                        } else {
                            ratingSungro = "0";

                        }
                        radSunZero.setChecked(false);
                        radSunTwo.setChecked(false);
                        break;
                    case R.id.radSunTwo:
                        if (radSunTwo.isChecked()) {
                            ratingSungro = "2";
                        } else if (radSunOne.isChecked()) {
                            ratingSungro = "1";
                        } else {
                            ratingSungro = "0";

                        }
                        break;

                }
                Log.d("ratingSungro", ratingSungro);
            }
        });


        radGroupAware.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radMahyco:

                        if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";
                        } else if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";
                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else {
                            productAwareness = "None";

                        }
                        radSungro.setChecked(false);
                        radBoth.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radSungro:
                        if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";

                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";
                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else {
                            productAwareness = "None";

                        }
                        radMahyco.setChecked(false);
                        radBoth.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radBoth:
                        if (radBoth.isChecked()) {
                            productAwareness = "Both";

                        } else if (radSungro.isChecked()) {
                            productAwareness = "Only Sungro";
                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";

                        } else {
                            productAwareness = "None";

                        }
                        radMahyco.setChecked(false);
                        radSungro.setChecked(false);
                        radNone.setChecked(false);
                        break;
                    case R.id.radNone:
                        if (radNone.isChecked()) {
                            productAwareness = "None";

                        } else if (radBoth.isChecked()) {
                            productAwareness = "Both";
                        } else if (radMahyco.isChecked()) {
                            productAwareness = "Only Mahyco";

                        } else {
                            productAwareness = "Only Sungro";

                        }
                        break;

                }
                Log.d("productAwareness", productAwareness);
            }
        });


        switchYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    etWhatsappNumber.setText(etMobileNo.getText().toString());

                    etMobileNo.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("ONtext changed " + s.toString());
                            etWhatsappNumber.setText(etMobileNo.getText().toString());

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            System.out.println("beforeTextChanged " + s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            System.out.println("afterTextChanged " + s.toString());

                        }
                    });

                    etWhatsappNumber.setFocusable(false);
                    etWhatsappNumber.setFocusableInTouchMode(false);
                } else if (!switchYN.isChecked() || etMobileNo.getText().toString().equals("")) {

                    etMobileNo.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("ONtext changed " + s.toString());
                            etWhatsappNumber.setText("");

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            System.out.println("beforeTextChanged " + s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            System.out.println("afterTextChanged " + s.toString());

                        }
                    });


                    etWhatsappNumber.setText("");
                    etWhatsappNumber.setFocusable(true);
                    etWhatsappNumber.setFocusableInTouchMode(true);


                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKisanClub.this);

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


    //validate all fields
    public boolean validation() {
//        if (radGroupUnit.getCheckedRadioButtonId() == -1) {
//            Utility.showAlertDialog("", "Please Select Unit", context);
//
//            return false;
//        }

        if (spState.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select State", context);
            return false;
        }
       /* if (spTerritory.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Territory", context);
            return false;
        }*/
        if (spDist.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select District", context);
            return false;
        }
        if (spTehsil.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Tehsil", context);
            return false;
        }
        if (spVillage.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Village", context);
            return false;
        }

        if (etFarmerName.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  Enter Farmer Name", context);
            return false;
        }


        if (etMobileNo.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
            return false;
        }

        if (etWhatsappNumber.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please Enter Whatsapp Number", context);
            return false;
        }

       /* if (radGroupVeg.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }*/

        if ( etComments.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter  comments.", context);

            return false;
        }
      /*  if (etArea.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter  Area.", context);

            return false;
        }

        if (spCropSown.getSelectedItem().toString().equals("SELECT CROP")) {
            Utility.showAlertDialog("Info", "Please Select Crops", context);
            return false;
        }


        if (spCropSown.getSelectedIndices().size() > 3) {
            Utility.showAlertDialog("Info", "Please select only three Crops", context);
            return false;
        }


        if (radGroupAware.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }
        if (radGroupPerMAhyco.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }
        if (radGroupPerSungro.getCheckedRadioButtonId() == -1) {
            Utility.showAlertDialog("Info", "Please select your choice", context);
            return false;
        }*/
        //  String mno = etMobileNo.getText().toString() != null ? etMobileNo.getText().toString() : "";;
        String mno = etMobileNo.getText().toString();

        /*if (!isAlreadydone(mno)) {
            Utility.showAlertDialog("Info", "This mobile no already exists", context);
            return false;
        }*/


        return true;
    }

    //if data already exit
    private boolean isAlreadydone(String mno) {


        boolean isExist = false;

        Cursor data = mDatabase.fetchAlreadyExistsKisanClub(mno);

        if (data.getCount() == 0) {

            isExist = true;

        }
        data.close();


        return isExist;


    }

    //bind crops to multiselection spinner
    public void bindVegCrops() {

        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;


            searchQuery = "SELECT distinct CropName  FROM CropMaster";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                array = new String[jArray.length() + 1];
                array[0] = "SELECT CROP";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("CropName").toString();
                }
                if (array.length > 0) {
                    spCropSown.setItems(array);
                    spCropSown.hasNoneOption(true);
                    spCropSown.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }


    }

    //bind state soinner
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
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }

    }

    //bind district spinner
    public void bindDist(String state) {
        try {
            spDist.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";

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
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    //bind Tehsil spinner
    public void bindTehsil(String dist) {
        try {
            spTehsil.setAdapter(null);
            String str = null;
            try {


                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TEHSIL",
                        "SELECT TEHSIL"));

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
                spTehsil.setAdapter(adapter);


            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
        }


    }


    public void bindTerritory(String state) {
        try {
            spTerritory.setAdapter(null);

            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct territory  FROM StateTerritoryMaster" +
                        " where state_code='" + state + "' order by territory asc  ";

                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TERRITORY",
                        "SELECT TERRITORY"));

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
                spTerritory.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
        }


    }

    //Bind vallage to spinner
    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }


    //save data to db//

    public void saveToDb() {


        String farmerName = etFarmerName.getText().toString();
        String mobileNumber = etMobileNo.getText().toString();
        String whatsappNumber = "";


        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsappNumber = "";

        } else {


            whatsappNumber = etWhatsappNumber.getText().toString();
        }


        String village = spVillage.getSelectedItem().toString();
        String state = spState.getSelectedItem().toString();
        String district = spDist.getSelectedItem().toString();
        String territory = spTerritory.getSelectedItem().toString();
        String tehsil = spTehsil.getSelectedItem().toString();
        String comments =etComments.getText().toString()+"-LD";
        String vegetableChoice = reply;
        String area = etArea.getText().toString();
        String cropSown = spCropSown.getSelectedItemsAsString();
        String isSynced = "0";
        Date entrydate = new Date();
        long epoch = entrydate.getTime();
        System.out.println(epoch);
        String timeStamp = String.valueOf(epoch);

        boolean fl = mDatabase.insertToKisanClub(entrydate.toString(), timeStamp, userCode, state,
                territory, district, tehsil, village, unitType, farmerName, mobileNumber, whatsappNumber, vegetableChoice, area,
                cropSown, productAwareness, ratingMahyco, ratingSungro, isSynced,comments);

        if (fl) {

            //     Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            uploadKisanClubData("KisanClubData");

        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

    //kisan club data fetch and upload api calling method with data fetch
    public void uploadKisanClubData(String functionName) {
      if (config.NetworkConnection())
      {
//            dialog = new ProgressDialog(ActivityKisanClub.this);
//            dialog.setTitle("Data Uploading ...");
//            dialog.setMessage("Please wait.");
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.show();
            String str = null;
            String searchQuery = "select  *  from KisanClubData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table1", jsonArray);
                        Log.d("rhtt", "uploadKisanClubData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadKisanData(functionName, jsonObject).execute(SERVER).get();

                    cursor.close();


                    if (!str.equals("")) {

                        if (str.contains("True")) {

                        etFarmerName.setText("");
                        etMobileNo.setText("");
                        etWhatsappNumber.setText("");
                          etComments.setText("");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String mno = jsonArray.getJSONObject(i).getString("mobileNumber");
                            mDatabase.updateKisanData(mno, "1");
                            Log.d("mno", "onResultReceived: " + mno);
                        }

                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Utility.showAlertDialog("Success", "Records Uploaded successfully", context);


                           // dialog.dismiss();

                    } else {
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Utility.showAlertDialog("Error", str, context);


                    }
                    } else {
                        Utility.showAlertDialog("Error","Poor Internet: Please try after sometime.",context);
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }


                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            } else {
              //  dialog.dismiss();
                relPRogress.setVisibility(View.GONE);
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else {
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Utility.showAlertDialog("Info", "Data Saved Successfully", context);
            etFarmerName.setText("");
            etMobileNo.setText("");
            etWhatsappNumber.setText("");
             etComments.setText("");

        }
       // dialog.dismiss();
    }


//upload api for kisan club

    public class UploadKisanData extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;


        public UploadKisanData(String Funname, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;


        }

        protected void onPreExecute() {

           // dialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("KisanClubData", obj.toString()));

            Log.d("RequestKisanClubData", postParameters + "");


            String Urlpath = SERVER + "?userCode=" + userCode + "";
            Log.d("uploadKisanClubData", Urlpath);

            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }
              //  dialog.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
               // dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                //dialog.dismiss();
            }

            //dialog.dismiss();
            return builder.toString();
        }

        @SuppressLint("LongLogTag")
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    Log.d("ResponseKisanClubData", resultout);


                } else {
                    Log.d("rhtt", "uploadKisanClubData: " + result);

                }

               // dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
              //  dialog.dismiss();
            }

        }
    }


}
