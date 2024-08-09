package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-08-22.
 */
public class ActivityBeSurvey extends AppCompatActivity {

    Context context;
    private long mLastClickTime = 0;

    private static final String TAG = "ActivityBeSurvey";
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    ScrollView container;
    String userCode;
    private String state, dist, taluka;

    String ratingYieldVal = "0";
    String ratingPriceVal = "0";
    String ratingQualityVal = "0";
    String ratingOthersVal = "0";
    String likelyTobuy = "";

    Switch switchYNchoice, switchYNRecommandation, switchCompanyVisit, switchSatisfied;


    TextInputLayout tiOtherSpecific, tiotherSpecifyProcure, tiRecommandDiscription;
    EditText etFarmerName, etMobileNumber, etVillage, etImprovements, etfeatures,
            etPrimeryBenifits,
            etotherSpecifyProcure, etRecommendDescription, etSuggestions,
            etOtherBrandSow, etLikeAboutOtherBrands,
            etOurThanOthers;
    SearchableSpinner spState, spDist, spTaluka, spVillage, spFromWhere, spPurchaseDescision, spKnowAboutUs, splikelyTobuy, spCompared, spCommitments, spMeetings, spResponsive;

    RadioGroup radGroupResponsive, radGroupTBMMDO, radGroupMeetings, radGroupComparedCompetitors;

    //    RadioButton radResponsive1, radCommitments1, radResponsive2, radResponsive3,
//            radResponsive4, radResponsive5, radCommitments2, radCommitments3,
//            radCommitments4, radCommitments5, radMeetings1,
//            radMeetings2, radMeetings3, radMeetings4, radMeetings5, radCompared1,
//            radCompared2, radCompared3, radCompared4,
//            radCompared5;
    Button btnSubmit;

    RatingBar ratingYield, ratingPrice, ratingQuality, ratingOthers;

    String seedPurchaseFactor,
            purchaseFrom, concernRating,
            rateTBMMDO, meetingRating,
            knowOurProducts, purchaseDecision,
            productQualityThenOthers;

    String retailShelf = "No";
    String promotionCompanyVisit = "No";
    String setisfiedlastYield = "No";
    String otheBrandSow = "";
    String ourProductsThanOthers = "";
    String likesaboutotherbrands = "";
    String suggestions = "";
    String features = "";
    String improvements = "";
    String primeryBenifits = "";
    String recomandationdesc = "No";
    String farmerName = "";
    String mobileNumber = "";
    String village = "";


    String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    private Handler handler = new Handler();
    ProgressBar progressBar;
    RelativeLayout relPRogress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_be_survey);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing

        userCode = pref.getString("UserID", null);

        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);


        spFromWhere = (SearchableSpinner) findViewById(R.id.spFromWhere);
        spPurchaseDescision = (SearchableSpinner) findViewById(R.id.spPurchaseDescision);
        spKnowAboutUs = (SearchableSpinner) findViewById(R.id.spKnowAboutUs);
        spResponsive = (SearchableSpinner) findViewById(R.id.spResponsive);
        spCompared = (SearchableSpinner) findViewById(R.id.spCompared);
        spCommitments = (SearchableSpinner) findViewById(R.id.spCommitments);
        spMeetings = (SearchableSpinner) findViewById(R.id.spMeetings);


        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);


        etFarmerName = (EditText) findViewById(R.id.etFarmerName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etVillage = (EditText) findViewById(R.id.etVillage);
        etImprovements = (EditText) findViewById(R.id.etImprovements);
        etfeatures = (EditText) findViewById(R.id.etfeatures);
        etotherSpecifyProcure = (EditText) findViewById(R.id.etotherSpecifyProcure);
        etRecommendDescription = (EditText) findViewById(R.id.etRecommendDescription);
        etSuggestions = (EditText) findViewById(R.id.etSuggestions);
        etOtherBrandSow = (EditText) findViewById(R.id.etOtherBrandSow);
        etLikeAboutOtherBrands = (EditText) findViewById(R.id.etLikeAboutOtherBrands);
        etOurThanOthers = (EditText) findViewById(R.id.etOurThanOthers);
        etPrimeryBenifits = (EditText) findViewById(R.id.etPrimeryBenifits);

        tiotherSpecifyProcure = (TextInputLayout) findViewById(R.id.tiotherSpecifyProcure);
        tiRecommandDiscription = (TextInputLayout) findViewById(R.id.tiRecommandDiscription);


        switchYNchoice = (Switch) findViewById(R.id.switchYNchoice);
        switchYNRecommandation = (Switch) findViewById(R.id.switchYNRecommandation);
        switchCompanyVisit = (Switch) findViewById(R.id.switchCompanyVisit);
        switchSatisfied = (Switch) findViewById(R.id.switchSatisfied);


        splikelyTobuy = (SearchableSpinner) findViewById(R.id.splikelyTobuy);

//
//        radGroupResponsive = (RadioGroup) findViewById(R.id.radGroupResponsive);
//        radGroupTBMMDO = (RadioGroup) findViewById(R.id.radGroupTBMMDO);
//        radGroupMeetings = (RadioGroup) findViewById(R.id.radGroupMeetings);

//        radGroupComparedCompetitors = (RadioGroup) findViewById(R.id.radGroupComparedCompetitors);


//        radResponsive1 = (RadioButton) findViewById(R.id.radResponsive1);
//        radResponsive2 = (RadioButton) findViewById(R.id.radResponsive2);
//        radResponsive3 = (RadioButton) findViewById(R.id.radResponsive3);
//        radResponsive4 = (RadioButton) findViewById(R.id.radResponsive4);
//        radResponsive5 = (RadioButton) findViewById(R.id.radResponsive5);
//
//
//        radCommitments1 = (RadioButton) findViewById(R.id.radCommitments1);
//        radCommitments2 = (RadioButton) findViewById(R.id.radCommitments2);
//        radCommitments3 = (RadioButton) findViewById(R.id.radCommitments3);
//        radCommitments4 = (RadioButton) findViewById(R.id.radCommitments4);
//        radCommitments5 = (RadioButton) findViewById(R.id.radCommitments5);
//
//
//        radMeetings1 = (RadioButton) findViewById(R.id.radMeetings1);
//        radMeetings2 = (RadioButton) findViewById(R.id.radMeetings2);
//        radMeetings3 = (RadioButton) findViewById(R.id.radMeetings3);
//        radMeetings4 = (RadioButton) findViewById(R.id.radMeetings4);
//        radMeetings5 = (RadioButton) findViewById(R.id.radMeetings5);


//        radCompared1 = (RadioButton) findViewById(R.id.radCompared1);
//        radCompared2 = (RadioButton) findViewById(R.id.radCompared2);
//        radCompared3 = (RadioButton) findViewById(R.id.radCompared3);
//        radCompared4 = (RadioButton) findViewById(R.id.radCompared4);
//        radCompared5 = (RadioButton) findViewById(R.id.radCompared5);
//
        ratingYield = (RatingBar) findViewById(R.id.ratingYield);
        ratingPrice = (RatingBar) findViewById(R.id.ratingPrice);
        ratingQuality = (RatingBar) findViewById(R.id.ratingQuality);
        ratingOthers = (RatingBar) findViewById(R.id.ratingOthers);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        bindLikelytoBuy();
        bindBuyProcureSpinner();
        bindPurchaseDescisionSpinner();
        bindCometoKnowSpinner();
        binResponsive();
        bindCommitments();
        bindCompared();
        bindMeetings();


        spFromWhere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    purchaseFrom = gm.Desc().trim();
                    Log.d("string",purchaseFrom);

                    if (purchaseFrom.contains("Other")) {

                        tiotherSpecifyProcure.setVisibility(View.VISIBLE);


                    } else {

                        tiotherSpecifyProcure.setVisibility(View.GONE);
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ratingYield.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                ratingYieldVal = String.valueOf(Math.round(ratingYield.getRating()));
                //    Toast.makeText(context, "Yield : "+ratingYieldVal , Toast.LENGTH_SHORT).show();


            }
        });
        ratingPrice.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                ratingPriceVal = String.valueOf(Math.round(ratingPrice.getRating()));
                // Toast.makeText(context, "Price : "+ratingPriceVal , Toast.LENGTH_SHORT).show();

            }
        });
        ratingQuality.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                ratingQualityVal = String.valueOf(Math.round(ratingQuality.getRating()));
                // Toast.makeText(context, "Quality : "+ratingQualityVal , Toast.LENGTH_SHORT).show();

            }
        });
        ratingOthers.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                ratingOthersVal = String.valueOf(Math.round(ratingOthers.getRating()));
//                Toast.makeText(context, "Others : "+ratingOthersVal , Toast.LENGTH_SHORT).show();

            }
        });


        spCommitments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    rateTBMMDO = gm.Desc().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spMeetings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    meetingRating = gm.Desc().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spCompared.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    productQualityThenOthers = gm.Desc().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        switchYNRecommandation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tiRecommandDiscription.setVisibility(View.VISIBLE);
                    recomandationdesc = "Yes";
                } else {
                    recomandationdesc = "No";
                    tiRecommandDiscription.setVisibility(View.GONE);
                }
            }
        });


        switchYNchoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    retailShelf = "Yes";
                } else {
                    retailShelf = "No";
                }

            }
        });
        switchCompanyVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    promotionCompanyVisit = "Yes";
                } else {
                    promotionCompanyVisit = "No";
                }

            }
        });


        switchSatisfied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    setisfiedlastYield = "Yes";
                } else {
                    setisfiedlastYield = "No";
                }

            }
        });


        splikelyTobuy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    likelyTobuy = gm.Desc().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurvey.this);

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
        bindState();


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

                // bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase()
            // .rawQuery(searchQuery, null);
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

            Croplist.add(new

                    GeneralMaster("OTHER",
                    "OTHER"));
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

    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        uploadBeSurvey();
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

    public boolean validation() {


        if (etFarmerName.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter farmer name", context);
            return false;
        }
        if (etMobileNumber.getText().length() != 10) {
            Utility.showAlertDialog("Info", "Please  enter Valid Mobile Number", context);
            return false;
        }
        if (spState.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select State", context);
            return false;
        }
        if (spDist.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select District", context);
            return false;
        }
        if (spTaluka.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select Taluka", context);
            return false;
        }
//        if (spVillage.getSelectedItemPosition() == 0) {
//            Utility.showAlertDialog("Info", "Please  enter village name",context);
//            return false;
//        }


        if (etVillage.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please  enter Village name", context);
            return false;
        }
//
        if (spFromWhere.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please  select buy/procure from place", context);
            return false;
        }


        if (purchaseFrom.contains("Other") && etotherSpecifyProcure.getText().toString().length() == 0) {
            Utility.showAlertDialog("Info", "Please enter specific procure/buy from", context);
            return false;
        }
        if (etImprovements.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter improvements ", context);
            return false;
        }
        if (etfeatures.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter 3 Features ", context);
            return false;
        }
        if (splikelyTobuy.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select", context);
            return false;
        }

        if (switchYNRecommandation.isChecked() && etRecommendDescription.getText().toString().length() == 0) {

            Utility.showAlertDialog("Info", "Please Enter Description", context);
            return false;

        }

        if (spResponsive.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please rate our responsive", context);
            return false;
        }
        if (spCommitments.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please rate our TBM/MDO", context);
            return false;
        }
        if (spMeetings.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please rate meetings", context);
            return false;
        }

        if (etPrimeryBenifits.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please enter primery benifits", context);
            return false;
        }
        if (etSuggestions.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter suggestions ", context);
            return false;
        }


        if (spKnowAboutUs.getSelectedItemPosition() == 0) {

            Utility.showAlertDialog("Info", "Please Select how you know about our products", context);
            return false;
        }


        if (spPurchaseDescision.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please Select your factors that affects your purchase decision", context);
            return false;
        }
        if (etOtherBrandSow.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter Other Brand Sow ", context);
            return false;
        }

        if (etLikeAboutOtherBrands.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter your likes for choosing Other Brands", context);
            return false;
        }

        if (spCompared.getSelectedItemPosition() == 0) {
            Utility.showAlertDialog("Info", "Please select our product quality", context);
            return false;
        }

        if (etOurThanOthers.getText().length() == 0) {
            Utility.showAlertDialog("Info", "Please Enter why you choose out product then other brands", context);
            return false;
        }


        return true;
    }


    public void bindBuyProcureSpinner() {


        try {
            spFromWhere.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Co-operative Marketing societies (CMS)"));
            gm.add(new GeneralMaster("2", "Primary Agricultural Co-operative  Societies (PACS)"));
            gm.add(new GeneralMaster("3", "Retail shops/outlets"));
            gm.add(new GeneralMaster("4", "Private dealers"));
            gm.add(new GeneralMaster("5", "Other"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFromWhere.setAdapter(adapter);


            purchaseFrom = spFromWhere.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void bindCometoKnowSpinner() {

        try {

            spKnowAboutUs.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Cooperative Marketing societies (CMS)"));
            gm.add(new GeneralMaster("2", "Primary Agricultural Cooperative Societies (PACS)"));
            gm.add(new GeneralMaster("3", "Radio/newspaper/advertisement"));
            gm.add(new GeneralMaster("4", "Relatives/friends/neighbors"));
            gm.add(new GeneralMaster("5", "Commission agents"));
            gm.add(new GeneralMaster("6", "Employees of the fed"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spKnowAboutUs.setAdapter(adapter);


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void bindPurchaseDescisionSpinner() {

        try {


            spPurchaseDescision.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Farm Demos"));
            gm.add(new GeneralMaster("2", "Reference from friends"));
            gm.add(new GeneralMaster("3", "Reference from progressive farmer in the area"));
            gm.add(new GeneralMaster("4", "Dealer or Retailer"));
            gm.add(new GeneralMaster("5", "Marketing campaigns by Company"));
            gm.add(new GeneralMaster("6", "Farmer meet"));
            gm.add(new GeneralMaster("7", "Neighbour"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPurchaseDescision.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void bindLikelytoBuy() {

        try {

            splikelyTobuy.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1.Never"));
            gm.add(new GeneralMaster("2", "2.May be"));
            gm.add(new GeneralMaster("3", "3.Not decided"));
            gm.add(new GeneralMaster("4", "4.Mostly"));
            gm.add(new GeneralMaster("5", "5.Surely"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            splikelyTobuy.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void bindCompared() {

        try {


            spCompared.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1.Poor"));
            gm.add(new GeneralMaster("2", "2.Average"));
            gm.add(new GeneralMaster("3", "3.Neutral"));
            gm.add(new GeneralMaster("4", "4.Good"));
            gm.add(new GeneralMaster("5", "5. Very Good"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCompared.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void binResponsive() {

        try {

            spResponsive.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1.Never"));
            gm.add(new GeneralMaster("2", "2.Some times"));
            gm.add(new GeneralMaster("3", "3.Neutral"));
            gm.add(new GeneralMaster("4", "4.Mostly"));
            gm.add(new GeneralMaster("5", "5.Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spResponsive.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void bindCommitments() {

        try {

            spCommitments.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1.Never"));
            gm.add(new GeneralMaster("2", "2.Some times"));
            gm.add(new GeneralMaster("3", "3.Neutral"));
            gm.add(new GeneralMaster("4", "4.Mostly"));
            gm.add(new GeneralMaster("5", "5.Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCommitments.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void bindMeetings() {

        try {

            spMeetings.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "1.Never"));
            gm.add(new GeneralMaster("2", "2.Some times"));
            gm.add(new GeneralMaster("3", "3.Neutral"));
            gm.add(new GeneralMaster("4", "4.Mostly"));
            gm.add(new GeneralMaster("5", "5.Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMeetings.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


//    public void bindBenifits() {
//
//        try {
//            String[] array2 = {"SELECT", "OTHERS", "NO"};
//            spPrimaryBenefit.setItems(array2);
//            spPrimaryBenefit.hasNoneOption(true);
//            spPrimaryBenefit.setSelection(new int[]{0});
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//
//    }


    public void uploadBeSurvey() {


        ourProductsThanOthers = etOurThanOthers.getText().toString();
        likesaboutotherbrands = etLikeAboutOtherBrands.getText().toString();
        suggestions = etSuggestions.getText().toString();
        features = etfeatures.getText().toString();
        improvements = etImprovements.getText().toString();
        primeryBenifits=etPrimeryBenifits.getText().toString();

        if (!recomandationdesc.contains("No")) {
            recomandationdesc = recomandationdesc + ":" + etRecommendDescription.getText().toString();
        } else {
            recomandationdesc = "No";
        }
        otheBrandSow = etOtherBrandSow.getText().toString();

        state = spState.getSelectedItem().toString();
        dist = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        farmerName = etFarmerName.getText().toString();
        mobileNumber = etMobileNumber.getText().toString();
        village = etVillage.getText().toString();

        ratingYieldVal = String.valueOf(Math.round(ratingYield.getRating()));

        ratingPriceVal = String.valueOf(Math.round(ratingPrice.getRating()));

        ratingQualityVal = String.valueOf(Math.round(ratingQuality.getRating()));

        ratingOthersVal = String.valueOf(Math.round(ratingOthers.getRating()));


        if (config.NetworkConnection()) {

            JSONObject requestParams = new JSONObject();

            JSONObject surveyRequest = new JSONObject();


            try {


                surveyRequest.put("farmerName", farmerName);
                surveyRequest.put("mobileNumber", mobileNumber);
                surveyRequest.put("state", state);
                surveyRequest.put("district", dist);
                surveyRequest.put("taluka", taluka);
                surveyRequest.put("village", village);


                surveyRequest.put("choiceMahycoProduct", retailShelf);
                surveyRequest.put("seedPurchaseFactorYield", ratingYieldVal);
                surveyRequest.put("seedPurchaseFactorPrice", ratingPriceVal);
                surveyRequest.put("seedPurchaseFactorQuality", ratingQualityVal);
                surveyRequest.put("seedPurchaseFactorOthers", ratingOthersVal);
//
                if (purchaseFrom.contains("Other")) {

                    surveyRequest.put("purchaseFrom", purchaseFrom + ":" + etotherSpecifyProcure.getText().toString());
                } else {

                    surveyRequest.put("purchaseFrom", purchaseFrom);


                }

                surveyRequest.put("improvements", improvements);
                surveyRequest.put("importantFeatures", features);
                surveyRequest.put("buyAgain", likelyTobuy);

                surveyRequest.put("recomandation", recomandationdesc);

                surveyRequest.put("concernRating", concernRating);
                surveyRequest.put("rateTBMMDO", rateTBMMDO);
                surveyRequest.put("meetingRating", meetingRating);
                surveyRequest.put("promotionVisit", promotionCompanyVisit);
                surveyRequest.put("lastSeasonSatisfaction", setisfiedlastYield);
                surveyRequest.put("primaryBenifits", primeryBenifits);
                surveyRequest.put("suggestion", suggestions);
                surveyRequest.put("knowOurProducts", knowOurProducts);
                surveyRequest.put("purchaseDecision", purchaseDecision);
                surveyRequest.put("otherBrandsName", otheBrandSow);
                surveyRequest.put("likeAboutOtherBrands", likesaboutotherbrands);
                surveyRequest.put("productQualityThenOthers", productQualityThenOthers);
                surveyRequest.put("productPreferenceOverOthers", ourProductsThanOthers);

                requestParams.put("SurveyData", surveyRequest);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Requestapppa", requestParams.toString());
            new BeSurveyApiCall("SurveyData", requestParams).execute();
        } else {

            Utility.showAlertDialog("Mahyco", "Please Check for Internet", context);
            relPRogress.setVisibility(View.GONE);
            container.setClickable(true);
            container.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

    }


    private class BeSurveyApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;


        public BeSurveyApiCall(String function, JSONObject obj) {


            this.function = function;
            this.obj = obj;


        }

        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", function));
            postParameters.add(new BasicNameValuePair("SurveyData", obj.toString()));
            String Urlpath1 = SERVER + "?userCode=" + userCode;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackgroundbeSurveyApiCall " + postParameters.toString());

            Log.d(TAG, "doInBackgroundbeSurveyApiCallUrl " + Urlpath1);
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
            } catch (ClientProtocolException e) {
                e.printStackTrace();

            } catch (Exception e) {

                e.printStackTrace();


            }

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            try {

                String resultout = result.trim();
                String message = "";
                if (!result.equals("")) {


                    if (resultout.contains("True")) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurvey.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Submitted Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                etFarmerName.setText("");
                                etMobileNumber.setText("");
                                relPRogress.setVisibility(View.GONE);
                                container.setClickable(true);
                                container.setEnabled(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                            }
                        });


                        AlertDialog alert = builder.create();
                        alert.show();

                    } else if (resultout.contains("False")) {

                        try {


                            JSONObject object = new JSONObject(result);
                            JSONArray jsonArray = object.getJSONArray("Table1");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObject = jsonArray.getJSONObject(i);

                                message = jObject.getString("Message");


                            }
                            Log.d("jObjectMessage", "onPostExecute:Message " + message);

                            Utility.showAlertDialog("Sync Server failed", message, context);
                            relPRogress.setVisibility(View.GONE);
                            container.setClickable(true);
                            container.setEnabled(true);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {


                        Utility.showAlertDialog("Info", "Something Went Wrong" + resultout.toString().trim(), context);
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    Log.d("Response", result);

                } else {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurvey.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Poor Internet: Please try after sometime");
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

}
