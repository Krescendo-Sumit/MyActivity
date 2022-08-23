package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.app.WebService;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class UploadData extends AppCompatActivity {
    private static final String TAG = "UploadData";
    public Button btnUpload, btnUpload2, btnUpload3, btnUpload5, btnUpload4, btnUploadRetailerdata,
            btnUploadPostSeason, btnUploadPreSeason, btnUploadATL, btnUploadGen;
    public ProgressDialog dialog, pd;
    public String SERVER = "";
    Prefs mPref;
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    public WebService wx;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private long mLastClickTime = 0;
    private Handler handler = new Handler();
    TextView lblretailerandkisanClub, lblPostSeason, lblPreSeason, lblATL, lblGeneral;
    RadioGroup radGrpRetailer;
    // TextView lblmyactvityrecord,lblfarmer;
    Config config;
    SharedPreferences pref;

    RadioButton rndmyactvity, rndfarmer, rndimages, rnd1, rnd2, rnd3, rnd4, rndVisit2, rndVisit1, rndCoupon, rndRetailerSurvey, rndKisanClub,
            rndFieldPurchaseList, rndfieldVisit, rndRetailerVisitToField, rndCropShow, rndHarvestDay, rndFieldDay, rndLivePlantDataVillage, rndLivePlantDataRetailer,
            rndTestimonialCollection, rndSanmanMela, rndVillageMeeting, rndPromotionThroughEntertainment, rndCropSeminar, rndJeepCampaigning, rndPopDisplay,
            rndPostering, rndFieldBoard, rndFieldBanner, rndWallPainting, rndTrolleyPainting, rndExhibition, rndMarketDay,
            rndDistributorVisit, rndinnovation, rndRetailerVisit, rndFarmerVisit, rndSamruddhaKisanVist, rndAddressing, rndReviewMeeting;

    SharedPreferences.Editor editor;
    private Context context;
    String Intime = "";
    RadioGroup radGrp5, radGrp6, radGrp7, radGrp8;
    String userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        getSupportActionBar().hide(); //<< this
        setTitle("Upload Data");
        initUI();
    }

    /**
     * <P>Method to initialize the elements</P>
     */
    private void initUI() {
        // setTitle("User Registration ");
        context = this;
        cx = new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        wx = new WebService();
        mPref = Prefs.with(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload2 = (Button) findViewById(R.id.btnUpload2);
        btnUpload3 = (Button) findViewById(R.id.btnUpload3);
        btnUpload4 = (Button) findViewById(R.id.btnUpload4);
        btnUpload5 = (Button) findViewById(R.id.btnUpload5);
        btnUploadRetailerdata = (Button) findViewById(R.id.btnUploadRetailerdata);
        btnUploadPostSeason = (Button) findViewById(R.id.btnUploadPostSeason);
        btnUploadPreSeason = (Button) findViewById(R.id.btnUploadPreSeason);
        btnUploadATL = (Button) findViewById(R.id.btnUploadATL);
        btnUploadGen = (Button) findViewById(R.id.btnUploadGen);
        radGrp5 = (RadioGroup) findViewById(R.id.radGrp5);
        radGrp6 = (RadioGroup) findViewById(R.id.radGrp6);
        radGrp7 = (RadioGroup) findViewById(R.id.radGrp7);
        radGrp8 = (RadioGroup) findViewById(R.id.radGrp8);

        rndmyactvity = (RadioButton) findViewById(R.id.rndmyactvity);
        rndfarmer = (RadioButton) findViewById(R.id.rndfarmer);
        rndimages = (RadioButton) findViewById(R.id.rndimages);
        rnd1 = (RadioButton) findViewById(R.id.rnd1);
        rnd2 = (RadioButton) findViewById(R.id.rnd2);
        rnd3 = (RadioButton) findViewById(R.id.rnd3);
        rnd4 = (RadioButton) findViewById(R.id.rnd4);
        rndVisit2 = (RadioButton) findViewById(R.id.rndVisit2);
        rndVisit1 = (RadioButton) findViewById(R.id.rndVisit1);
        rndCoupon = (RadioButton) findViewById(R.id.rndCoupon);
        rndRetailerSurvey = (RadioButton) findViewById(R.id.rndRetailerSurvey);
        rndKisanClub = (RadioButton) findViewById(R.id.rndKisanClub);
        //VCBU

        lblPostSeason = (TextView) findViewById(R.id.lblPostSeason);
        lblPreSeason = (TextView) findViewById(R.id.lblPreSeason);
        lblATL = (TextView) findViewById(R.id.lblATL);
        lblGeneral = (TextView) findViewById(R.id.lblGeneral);
        lblretailerandkisanClub = (TextView) findViewById(R.id.lblretailerandkisanClub);
        radGrpRetailer = (RadioGroup) findViewById(R.id.radGrpRetailer);
        if (pref.getString("unit", null).contains("VCBU")) {
            lblretailerandkisanClub.setVisibility(View.VISIBLE);
            radGrpRetailer.setVisibility(View.VISIBLE);
            btnUploadRetailerdata.setVisibility(View.VISIBLE);
            btnUploadPostSeason.setVisibility(View.GONE);
            btnUploadPreSeason.setVisibility(View.GONE);
            btnUploadATL.setVisibility(View.GONE);
            btnUploadGen.setVisibility(View.GONE);
            radGrp5.setVisibility(View.GONE);
            radGrp6.setVisibility(View.GONE);
            radGrp7.setVisibility(View.GONE);
            radGrp8.setVisibility(View.GONE);
            lblPostSeason.setVisibility(View.GONE);
            lblPreSeason.setVisibility(View.GONE);
            lblATL.setVisibility(View.GONE);
            lblGeneral.setVisibility(View.GONE);
        } else {
            btnUploadRetailerdata.setVisibility(View.GONE);
            lblretailerandkisanClub.setVisibility(View.GONE);
            radGrpRetailer.setVisibility(View.GONE);
            btnUploadPostSeason.setVisibility(View.VISIBLE);
            btnUploadPreSeason.setVisibility(View.VISIBLE);
            btnUploadATL.setVisibility(View.VISIBLE);
            btnUploadGen.setVisibility(View.VISIBLE);
            radGrp5.setVisibility(View.VISIBLE);
            radGrp6.setVisibility(View.VISIBLE);
            radGrp7.setVisibility(View.VISIBLE);
            radGrp8.setVisibility(View.VISIBLE);
            lblPostSeason.setVisibility(View.VISIBLE);
            lblPreSeason.setVisibility(View.VISIBLE);
            lblATL.setVisibility(View.VISIBLE);
            lblGeneral.setVisibility(View.VISIBLE);
        }
        //post season options
        rndFieldPurchaseList = (RadioButton) findViewById(R.id.rndFieldPurchaseList);
        rndfieldVisit = (RadioButton) findViewById(R.id.rndfieldVisit);
        rndRetailerVisitToField = (RadioButton) findViewById(R.id.rndRetailerVisitToField);
        rndCropShow = (RadioButton) findViewById(R.id.rndCropShow);
        rndHarvestDay = (RadioButton) findViewById(R.id.rndHarvestDay);
        rndFieldDay = (RadioButton) findViewById(R.id.rndFieldDay);
        rndLivePlantDataVillage = (RadioButton) findViewById(R.id.rndLivePlantDataVillage);
        rndLivePlantDataRetailer = (RadioButton) findViewById(R.id.rndLivePlantDataRetailer);

        //preseason options
        rndTestimonialCollection = (RadioButton) findViewById(R.id.rndTestimonialCollection);
        rndSanmanMela = (RadioButton) findViewById(R.id.rndSanmanMela);
        rndVillageMeeting = (RadioButton) findViewById(R.id.rndVillageMeeting);
        rndPromotionThroughEntertainment = (RadioButton) findViewById(R.id.rndPromotionThroughEntertainment);
        rndCropSeminar = (RadioButton) findViewById(R.id.rndCropSeminar);
        rndJeepCampaigning = (RadioButton) findViewById(R.id.rndJeepCampaigning);
        rndPopDisplay = (RadioButton) findViewById(R.id.rndPopDisplay);

        //ATL
        rndPostering = (RadioButton) findViewById(R.id.rndPostering);
        rndFieldBoard = (RadioButton) findViewById(R.id.rndFieldBoard);
        rndFieldBanner = (RadioButton) findViewById(R.id.rndFieldBanner);
        rndWallPainting = (RadioButton) findViewById(R.id.rndWallPainting);
        rndTrolleyPainting = (RadioButton) findViewById(R.id.rndTrolleyPainting);
        rndExhibition = (RadioButton) findViewById(R.id.rndExhibition);
        rndMarketDay = (RadioButton) findViewById(R.id.rndMarketDay);


        //General
        rndDistributorVisit = (RadioButton) findViewById(R.id.rndDistributorVisit);
        rndRetailerVisit = (RadioButton) findViewById(R.id.rndRetailerVisit);
        rndFarmerVisit = (RadioButton) findViewById(R.id.rndFarmerVisit);
        rndSamruddhaKisanVist = (RadioButton) findViewById(R.id.rndSamruddhaKisanVist);
        rndReviewMeeting = (RadioButton) findViewById(R.id.rndReviewMeeting);
        rndAddressing = (RadioButton) findViewById(R.id.rndAddressing);
        rndinnovation = (RadioButton) findViewById(R.id.rndinnovation);

        pd = new ProgressDialog(context);

        // lblmyactvityrecord=(TextView)findViewById(R.id.lblmyactvityrecord);
        //lblfarmer=(TextView)findViewById(R.id.lblfarmer);
        msclass = new Messageclass(this);
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        mDatabase.updateblacksynced();
        downloadphotopath("mdo_photoupdate");
        //checktableexist();
        //recordshowInnovation();
        // uploadAllcouponData("AllcouponData", "AllcouponData");


        rowcount();
        recordshow();
        recordshowVisit();
        recordshowCoupon();
        recordshowRetailerSurveyKisanClub();
        recordshowPostSeason();
        recordshowPreSeason();
        recordshowATL();
        recordshowGeneral();
        innovationuploadPendingdata();
        onPostSeasonBtnClicked();
        onPreSeasonBtnClicked();
        onATLBtnClicked();
        onGeneralBtnClicked();


//        radGrp5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup arg0, int id) {
//                switch (id) {
//                    case -1:
//                        // Log.v(TAG, "Choices cleared!");
//                        break;
//                    case R.id.rndFieldPurchaseList:
//
//                        break;
//                    case R.id.rndfieldVisit:
//
//                        break;
//                    case R.id.rndRetailerVisitToField:
//
//                        break;
//                    case R.id.rndCropShow:
//
//                        break;
//                    case R.id.rndHarvestDay:
//
//                        break;
//                    case R.id.rndFieldDay:
//
//                        break;
//                    case R.id.rndLivePlantDataVillage:
//
//                        break;
//                    case R.id.rndLivePlantDataRetailer:
//
//                        break;
//
//                    default:
//
//                        break;
//                }
//            }
//        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // if(rndmyactvity.isChecked()) {
                    if (config.NetworkConnection()) {

                        boolean flag = false;

                        if (rowcount() > 0) {
                            UploadalldataActvityNEW("tagdatauploadMDONew_Testold");

                            //UploadalldataActvity("tagdatauploadMDONew_Testold");
                            UploadFarmerData("MDOFarmerMasterdataInsertNew");
                            // UploadalldataInnovation("InnovationData");
                            UploadaImage("UploadImages");
                            rowcount();
                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }

                    } else {
                        msclass.showMessage("Internet connection not available");
                    }
                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });


        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection()) {

                        boolean flag = false;
                        if (recordshow() > 0) {
                            // MDO_TravelData();
                            // UploadaImage2("mdo_starttravel");
                            //  UploadaImage2("mdo_endtravel");
                            String updatestring = "";
                            updatestring = "select *  from  mdo_starttravel where  imgname='' ";
                            mDatabase.updateimageblank(updatestring, "mdo_starttravel", userCode);
                            updatestring = "select *  from  mdo_endtravel where  imgname='' ";
                            mDatabase.updateimageblank(updatestring, "mdo_endtravel", userCode);


                            new SyncMDOTravel_Async("MDO_TravelData").execute();
                            new SyncMDODemandAssessement_Async("MDO_demandassesmentservey").execute();
                            new SyncDataAsync_Async("mdo_starttravel").execute();
                            new SyncDataAsync_Async("mdo_endtravel").execute();
                            new SyncDataAsync_Async("mdo_endtravel").execute();

                            //recordshow();
                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else {
                        msclass.showMessage("Internet connection not available");
                    }


                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });


        btnUpload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection()) {

                        boolean flag = false;
                        //mDatabase.updateDemoModelData("7972213781COTTONBAHUBALIPLUS(MRC-7361)other field","0","0");

                        if (recordshowVisit() > 0) {
                            uploadRegisterDataModelRecords("mdo_demoModelVisitDetail");
                            uploadUpdatedDataModelRecords("mdo_demoModelVisitDetail");


                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else {
                        msclass.showMessage("Internet connection not available");
                    }


                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });

        btnUploadRetailerdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection()) {

                        boolean flag = false;
                        if (recordshowRetailerSurveyKisanClub() > 0) {
                            uploadRetailerSurveyData("RetailerSurvery", "RetailerSurveyData");
                            uploadKisanClubData("KisanClubData", "KisanClubData");


                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else {
                        msclass.showMessage("Internet connection not available");
                    }


                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });


        btnUpload4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection()) {

                        boolean flag = false;
                        if (recordshowCoupon() > 0) {


                            uploadCouponRecords("mdo_couponSchemeDownloadAndUpload");

                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else {

                        msclass.showMessage("Internet connection not available");
                    }


                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });
        btnUpload5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    if (config.NetworkConnection()) {

                        boolean flag = false;

                        if (innovationuploadPendingdata() > 0) {
                            UploadalldataInnovation("InnovationData");
                            //innovationuploadPendingdata();
                        } else {
                            msclass.showMessage("Data not available for uploading");
                        }

                    } else {
                        msclass.showMessage("Internet connection not available");
                    }
                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                    ex.printStackTrace();

                }

            }
        });

        logUploadDataEvent();
    }

    private void onGeneralBtnClicked() {
        btnUploadGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLastClickTime = SystemClock.elapsedRealtime();
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                builder.setTitle("MyActivity");
                builder.setMessage("Are you sure to upload data");
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
                        doworkGeneral();
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

        });
    }

    private void doworkGeneral() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveGenToServer();
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

    private void saveGenToServer() {

        try {

            if (config.NetworkConnection()) {

                boolean flag = false;

                String getTag = null;
                int radioButtonID = radGrp8.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radGrp8.findViewById(radioButtonID);
                if (radioButton != null) {
                    getTag = (String) radioButton.getTag();
                }
                if (getTag != null) {
                    if (getTag.equals(getResources().getString(R.string.rndDistributorVisit))) {
                        int count = 0;
                        String searchQuery = "";
                        searchQuery = "select  *  from DistributerVisitsData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count = cursor.getCount();
                        cursor.close();
                        //done
                        if (count > 0) {

                            uploadDistributerVisitsData("DistributerVisitsData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndSamruddhaKisanVist))) {
                        int count2 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from SamruddhaKisanVisitsData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count2 = count2 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count2 > 0) {
                            uploadSamruddhaKisanVisitsData("SamruddhaKisanVisitsData");
                        } else {
                            msclass.showMessage("Data not available for uploading");

                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndRetailerVisit))) {
                        int count3 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from RetailerVisitsData  where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count3 = count3 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count3 > 0) {
                            uploadRetailerVisitsData("RetailerVisitsData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndFarmerVisit))) {
                        int count4 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from FarmerVisitsData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count4 = count4 + cursor.getCount();
                        cursor.close();
                        // add params
                        if (count4 > 0) {
                            uploadFarmerVisitsData("FarmerVisitsData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndReviewMeeting))) {
                        int count5 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from ReviewMeetingData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count5 = count5 + cursor.getCount();
                        cursor.close();
                        // add params
                        if (count5 > 0) {
                            uploadReviewMeetingData("ReviewMeetingData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }

                    }

                    // Toast.makeText(context, "" + getTag, Toast.LENGTH_SHORT).show();
                } else {

                    msclass.showMessage("Please select general activity to upload");
                    progressBarVisibility();
                }

            } else {
                progressBarVisibility();
                msclass.showMessage("Internet connection not available");

            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    public void uploadFarmerVisitsData(String functionName) {

        String str = null;
        if (config.NetworkConnection()) {

            try {
                new uploadFarmerVisitDataServer(functionName, context).execute(Constants.FARMERVISITS_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class uploadFarmerVisitDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;


        String Funname;


        public uploadFarmerVisitDataServer(String Funname, Context context) {

            this.Funname = Funname;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadFarmerVisitData(Funname);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                        recordshowGeneral();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    private String uploadFarmerVisitData(String funname) {

        String str = null;
        String searchQuery = "select  *  from FarmerVisitsData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonArray = mDatabase.getResults(searchQuery);

                    //  for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject.put("Table", jsonArray);
                    String id = "1";//jsonArray.getJSONObject(i).getString("_id");

                    str = syncdFarmerVisitsData(funname, Constants.FARMERVISITS_SERVER_API, jsonObject);
                    handleFarmerVisitsDataSyncResponse(funname, str, id);

                    // }

                    Log.d("rhtt", "uploadFarmerVisitsData: " + jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();

            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }

        return str;
    }

    public synchronized String syncdFarmerVisitsData(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.FARMERVISITS_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }

    public void handleFarmerVisitsDataSyncResponse(String function, String resultout, String id) throws JSONException {
        //if (function.equals(function)) {
        JSONObject jsonObject = new JSONObject(resultout);
        if (jsonObject.has("success")) {
            if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                mDatabase.updateFarmerVisitsData("0", "1", id);
            } else {
            }
            //  }
        }
        Log.d("FarmerVisitsData", "FarmerVisitsData: " + resultout);
    }


    public void uploadReviewMeetingData(String functionName) {

        String str = null;
        if (config.NetworkConnection()) {

            try {
                new uploadReviewMeetingDataServer(functionName, context).execute(Constants.REVIEWMEETING_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class uploadReviewMeetingDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;


        String Funname;


        public uploadReviewMeetingDataServer(String Funname, Context context) {

            this.Funname = Funname;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadReviewMeeting(Funname);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                        recordshowGeneral();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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


    private String uploadReviewMeeting(String funname) {

        String str = null;
        String searchQuery = "select  *  from ReviewMeetingData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

//                for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Table", jsonArray);
                Log.d("ReviewMeetingData", jsonObject.toString());
                str = syncReviewMeetingData(funname, Constants.REVIEWMEETING_SERVER_API, jsonObject);
                handleReviewMeetingDataSyncResponse(funname, str);
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }

        return str;
    }

    public synchronized String syncReviewMeetingData(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.REVIEWMEETING_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    private void handleReviewMeetingDataSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("ReviewMeetingData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateReviewMeeitngData("0", "1");

                }
            }
        }

        Log.d("ReviewMeetingData", "ReviewMeetingData: " + resultout);
    }


    public void uploadRetailerVisitsData(String functionName) {
        if (config.NetworkConnection()) {

            try {
                new uploadRetailerVisitsDataServer(functionName, context).execute(Constants.RETAILERVIST_SERVER_API).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>//AsyncTask Class for api batch code upload call</p>
     */
    private class uploadRetailerVisitsDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;


        String Funname;


        public uploadRetailerVisitsDataServer(String Funname, Context context) {

            this.Funname = Funname;
        }


        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadRetailerData(Funname);

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                        recordshowGeneral();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    private String uploadRetailerData(String funname) {

        String str = null;
        String searchQuery = "select  *  from RetailerVisitsData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonArray = mDatabase.getResults(searchQuery);

                    jsonObject.put("Table", jsonArray);

                    Log.d("RetailerVisitsData", "uploadRetailerVisitsData: " + jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                str = syncdRetailerVisitsData(funname, Constants.RETAILERVIST_SERVER_API, jsonObject);
                handleRetailerVisitsDataSyncResponse(funname, str);

                cursor.close();

            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }
        return str;
    }

    public synchronized String syncdRetailerVisitsData(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.RETAILERVIST_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }

    public void handleRetailerVisitsDataSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals(function)) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateRetailerVisitsData("0", "1");
                } else {
                }
            }
        }
        Log.d("SamruddhaVistData", "SamruddhaVistData: " + resultout);
    }


    public void uploadSamruddhaKisanVisitsData(String functionName) {
        if (config.NetworkConnection()) {

            try {
                new UploadSamruddhaKisanVisitsDataServer(functionName, context).execute(Constants.SAMRUDDHA_KISAN_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * <p>//AsyncTask Class for api batch code upload call</p>
     */
    private class UploadSamruddhaKisanVisitsDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;


        String Funname;


        public UploadSamruddhaKisanVisitsDataServer(String Funname, Context context) {


            this.Funname = Funname;
        }

        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return UploadSamruddhaKisanVisitsData(Funname);
        }

        protected void onPostExecute(String result) {


            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                        recordshowGeneral();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    private String UploadSamruddhaKisanVisitsData(String funname) {


        String str = null;
        String searchQuery = "select  *  from SamruddhaKisanVisitsData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonArray = mDatabase.getResults(searchQuery);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        String id = jsonArray.getJSONObject(i).getString("_id");

                        str = syncSamruddhaData(funname, Constants.SAMRUDDHA_KISAN_SERVER_API, jsonObject);
                        handleSamruddhaVistDataSyncResponse(funname, str, id);
                    }
                    Log.d("SamruddhaVistData", "uploadSamruddhaKisanVisitsData: " + jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cursor.close();


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
        return str;
    }


    public synchronized String syncSamruddhaData(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.SAMRUDDHA_KISAN_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }

    public void handleSamruddhaVistDataSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals(function)) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateSamruddhaKisanVisitsData("0", "1", id);
                } else {
                }
            }
        }
        Log.d("SamruddhaVistData", "SamruddhaVistData: " + resultout);
    }


    private void uploadDistributerVisitsData(String distributerVisitsData) {
        if (config.NetworkConnection()) {

            try {
                new uploadDistributerVisitsDataServer(distributerVisitsData, context).execute(Constants.DISTRIBUTORVIST_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class uploadDistributerVisitsDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public uploadDistributerVisitsDataServer(String Funname, Context context) {

        }


        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadDistributorData("DistributorData");

        }

        protected void onPostExecute(String result) {

            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowGeneral();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    private String uploadDistributorData(String distributorData) {

        String str = null;
        String searchQuery = "select  *  from DistributerVisitsData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonArray = mDatabase.getResults(searchQuery);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        String id = jsonArray.getJSONObject(i).getString("_id");

                        str = syncATLMarketDayDataSingleImage(distributorData, Constants.DISTRIBUTORVIST_SERVER_API, jsonObject);
                        handleDistributorDataSyncResponse("DistributorData", str, id);
                    }

                    Log.d("rhtt", "uploadDistributerVisitsData: " + jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cursor.close();

            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }
        return str;
    }

    public synchronized String syncATLMarketDayDataSingleImage(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.DISTRIBUTORVIST_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }

    public void handleDistributorDataSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("DistributorData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateDistributerVisitsData("0", "1", id);
                }
            }
        }
        Log.d("DistributorData", "DistributorData: " + resultout);
    }


    private int recordshowGeneral() {

        int totalcount = 0;
        try {
            String searchQuery = "";


            int count2 = 0;
            searchQuery = "select  *  from DistributerVisitsData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndDistributorVisit.setText("DISTRIBUTOR VISIT" + " " + String.valueOf(count2));


            int count3 = 0;
            searchQuery = "select  *  from SamruddhaKisanVisitsData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count3 = count3 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count3;
            rndSamruddhaKisanVist.setText("SAMRUDDHA KISAN VISITS" + " " + String.valueOf(count3));


            int count4 = 0;

            searchQuery = "select  *  from RetailerVisitsData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            while(cursor.moveToNext())
            {
                String  row=cursor.getString(0)+"-"+
                        cursor.getString(1)+"-"+
                        cursor.getString(2)+"-"+
                        cursor.getString(3)+"-"+
                        cursor.getString(4)+"-"+
                        cursor.getString(5)+"-"+
                        cursor.getString(6)+"-"+
                        cursor.getString(7)+"-"+
                        cursor.getString(8)+"-"+
                        cursor.getString(9)+"-"+
                        cursor.getString(10)+"-"+
                        cursor.getString(11)+"-";
                Log.i("Valu",row);
            }
            count4 = count4 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count4;
            rndRetailerVisit.setText("RETAILER VISIT" + " " + String.valueOf(count4));

            int count5 = 0;
            searchQuery = "select  *  from FarmerVisitsData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count5 = count5 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count5;
            rndFarmerVisit.setText("FARMER VISITS (GENERAL)" + " " + String.valueOf(count5));

            int count6 = 0;
            searchQuery = "select  *  from ReviewMeetingData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count6 = count6 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count6;
            rndReviewMeeting.setText("REVIEW MEETING" + " " + String.valueOf(count6));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;

    }

    private int recordshowATL() {
        int totalcount = 0;
        try {
            String searchQuery = "";


            int count2 = 0;
            searchQuery = "select  *  from ATLVillagePosteringData where isSynced ='0'  ";
            Cursor cursor;
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndPostering.setText("POSTERING" + " " + String.valueOf(count2));


            int count3 = 0;
            searchQuery = "select  *  from FieldBoardData where  isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count3 = count3 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count3;
            rndFieldBoard.setText("FIELD BOARD" + " " + String.valueOf(count3));


            int count4 = 0;

            searchQuery = "select  *  from FieldBannerData where  isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count4 = count4 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count4;
            rndFieldBanner.setText("FIELD BANNER" + " " + String.valueOf(count4));

            int count5 = 0;
            searchQuery = "select  *  from ATLVillageWallPaintingData where isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count5 = count5 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count5;
            rndWallPainting.setText("WALL PAINTING" + " " + String.valueOf(count5));

            int count6 = 0;
            searchQuery = "select  *  from TrolleyPaintingData where  isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count6 = count6 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count6;
            rndTrolleyPainting.setText("TROLLEY PAINTING" + " " + String.valueOf(count6));

            int count7 = 0;
            searchQuery = "select  *  from ATLExhibitionData where  isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count7 = count7 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count7;
            rndExhibition.setText("EXHIBITION" + " " + String.valueOf(count7));

            int count8 = 0;
            searchQuery = "select  *  from ATLMarketDayData where  isSynced ='0' ";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count8 = count8 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count8;
            rndMarketDay.setText("MARKET DAY ACTIVITIES" + " " + String.valueOf(count8));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }

    private void onATLBtnClicked() {

        btnUploadATL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);

                builder.setTitle("MyActivity");
                builder.setMessage("Are you sure to upload data");

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
                        doworkATL();
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

        });
    }

    private void doworkATL() {

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveATLToServer();
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

    private void saveATLToServer() {

        try {

            if (config.NetworkConnection()) {

                boolean flag = false;

                String getTag = null;
                int radioButtonID = radGrp7.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radGrp7.findViewById(radioButtonID);
                if (radioButton != null) {
                    getTag = (String) radioButton.getTag();
                }
                if (getTag != null) {
                    if (getTag.equals(getResources().getString(R.string.rndPostering))) {
                        int count = 0;
                        String searchQuery = "";
                        searchQuery = "select  *  from ATLVillagePosteringData where isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count = cursor.getCount();
                        cursor.close();
                        //done
                        if (count > 0) {

                            uploadDataPostering("ATLVillagePosteringData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndFieldBoard))) {
                        int count2 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from FieldBoardData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count2 = count2 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count2 > 0) {
                            uploadFieldBoardData("MDO_FieldBoardData");
                        } else {
                            msclass.showMessage("Data not available for uploading");

                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndFieldBanner))) {
                        int count3 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from FieldBannerData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count3 = count3 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count3 > 0) {
                            uploadFieldBannerData("MDO_FieldBannerData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndWallPainting))) {
                        int count4 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from ATLVillageWallPaintingData where isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count4 = count4 + cursor.getCount();
                        cursor.close();
                        // add params
                        if (count4 > 0) {
                            uploadWallPaintingData("ATLVillageWallPaintingData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndTrolleyPainting))) {
                        int count5 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from TrolleyPaintingData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count5 = count5 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count5 > 0) {
                            uploadTrolleyDataServer("TrolleyPaintingData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndExhibition))) {
                        int count6 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from ATLExhibitionData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count6 = count6 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count6 > 0) {
                            uploadEXhibitionDataServer("mdo_ATLExhibitionData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndMarketDay))) {
                        int count9 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from ATLMarketDayData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count9 = count9 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count9 > 0) {
                            uploadMarketDataServer("MDO_ATLMarketDayData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }
                    }


                    // Toast.makeText(context, "" + getTag, Toast.LENGTH_SHORT).show();
                } else {
                    progressBarVisibility();
                    msclass.showMessage("Please select ATL activity to upload");
                }

            } else {
                relPRogress.setVisibility(View.GONE);
                msclass.showMessage("Internet connection not available");
                container.setClickable(true);
                container.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            progressBarVisibility();

        }

    }

    private void uploadWallPaintingData(String atlVillageWallPaintingData) {

        if (config.NetworkConnection()) {
            try {
                new UploadATLVillageWallPaintingDataServer(atlVillageWallPaintingData, context).execute(Constants.WALLPAINTING_SERVER_API).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadATLVillageWallPaintingData(String atlVillageWallPaintingData) {
        String str = "";
        int action = 1;

        String searchQuery = "select  *  from ATLVillageWallPaintingData where isSynced ='0' ";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String finalPopupJson = jsonArray.getJSONObject(i).getString("finalPopupJson");
                    JSONArray jsonArrayPop = new JSONArray(finalPopupJson);
                    jsonArray.getJSONObject(i).put("finalPopupJson", jsonArrayPop);

                    String id = jsonArray.getJSONObject(i).getString("_id");

                    for (int j = 0; j < jsonArrayPop.length(); j++) {
                        String imgPath = jsonArrayPop.getJSONObject(j).getString("activityImgPath");
                        Log.d("imgPath", imgPath);
                        jsonArrayPop.getJSONObject(j).put("activityImgPath", mDatabase.getImageDatadetail(imgPath));
                    }
                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("VillageWallPaintingData", jsonObject.toString());
                    if (!jsonObject.toString().isEmpty()) {
                        str = syncATLVillageWallPaintingDataSingleImage(atlVillageWallPaintingData, Constants.WALLPAINTING_SERVER_API, jsonObject);
                        // handleATLVillageWallPaintingDataDetailsImageSyncResponse("ATLVillageWallPaintingDataDetails", str);
                        handleATLVillageWallPaintingDataSyncResponse("ATLVillageWallPaintingData", str, id);
                    } else {
                        alertPoorConnection();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    //AsyncTask Class for api batch code upload call
    private class UploadATLVillageWallPaintingDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadATLVillageWallPaintingDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadATLVillageWallPaintingData("ATLVillageWallPaintingData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                redirecttoRegisterActivity(result);
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        progressBarVisibility();
                        recordshowATL();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public synchronized String syncATLVillageWallPaintingDataSingleImage(String function, String urls, JSONObject jsonObject
    ) {

        return HttpUtils.POSTJSON(Constants.WALLPAINTING_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }


    public void handleATLVillageWallPaintingDataDetailsImageSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("ATLVillageWallPaintingDataDetails")) {
            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateATLVillageWallPaintingDataDetails("0", "1", "1");

                    }
                }
            } else {
                alertPoorConnection();
            }

            Log.d("VillageWallPaintingData", "ATLVillageWallPaintingData: " + resultout);
        }
    }

    public void handleATLVillageWallPaintingDataSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("ATLVillageWallPaintingData")) {
            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateATLVillageWallPaintingData("0", "1", id);

                    } else {

                    }
                }
            } else {
                alertPoorConnection();
            }

        }
        Log.d("VillageWallPaintingData", "ATLVillageWallPaintingData: " + resultout);
    }

    private void uploadMarketDataServer(String mdo_atlMarketDayData) {
        if (config.NetworkConnection()) {

            try {
                new UploadATLMarketDayDataServer(mdo_atlMarketDayData, context).execute(Constants.MARKETDAY_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadATLMarketDayData(String atlMarketDayData) {
        String str = "";
        int action = 1;

        String searchQuery = "select  *  from ATLMarketDayData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("ATLMarketDayData", jsonObject.toString());
                    str = syncATLMarketDayDataSingleImage(atlMarketDayData, Constants.MARKETDAY_SERVER_API, jsonObject, activityImgName, activityImgPath);
                    handleATLMarketDayDataImageSyncResponse("MDO_ATLMarketDayData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    //AsyncTask Class for api batch code upload call
    private class UploadATLMarketDayDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadATLMarketDayDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadATLMarketDayData("MDO_ATLMarketDayData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        progressBarVisibility();
                        recordshowATL();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public synchronized String syncATLMarketDayDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                               String activityImgPath) {

        return HttpUtils.POSTJSON(Constants.MARKETDAY_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }


    public void handleATLMarketDayDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("MDO_ATLMarketDayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateATLMarketDayData("0", "1", "1", id);
                } else {
                }
            }
        }
        Log.d("ATLMarketDayData", "ATLMarketDayData: " + resultout);
    }

    private void uploadEXhibitionDataServer(String mdo_atlExhibitionData) {
        if (config.NetworkConnection()) {

            try {
                new UploadATLExhibitionDataServer(mdo_atlExhibitionData, context).execute(Constants.EXHIBITION_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String uploadATLExhibitionData(String atlExhibitionData) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from ATLExhibitionData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("ATLExhibitionData", jsonObject.toString());
                    str = syncATLExhibitionDataSingleImage(atlExhibitionData, Constants.EXHIBITION_SERVER_API, jsonObject, activityImgName, activityImgPath);

                    handleATLExhibitionDataImageSyncResponse("mdo_ATLExhibitionData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    //AsyncTask Class for api batch code upload call

    private class UploadATLExhibitionDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadATLExhibitionDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadATLExhibitionData("mdo_ATLExhibitionData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowATL();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public synchronized String syncATLExhibitionDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                String activityImgPath) {

        return HttpUtils.POSTJSON(Constants.EXHIBITION_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleATLExhibitionDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("mdo_ATLExhibitionData")) {


            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateATLExhibitionData("0", "1", "1", id);

                } else {

                }
            }
        }

        Log.d("ATLExhibitionData", "ATLExhibitionData: " + resultout);
    }

    private void uploadTrolleyDataServer(String trolleyPaintingData) {
        if (config.NetworkConnection()) {

            try {
                new UploadTrolleyPaintingServer(trolleyPaintingData, context).execute(Constants.TROLLEY_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//AsyncTask Class for api batch code upload call

    private class UploadTrolleyPaintingServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadTrolleyPaintingServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadTrolleyPaintingData("TrolleyPaintingData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        progressBarVisibility();
                        recordshowATL();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public String uploadTrolleyPaintingData(String function) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from TrolleyPaintingData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String leftSideImgName = jsonArray.getJSONObject(i).getString("leftSideImgName");
                    String leftSideImgPath = jsonArray.getJSONObject(i).getString("leftSideImgPath");

                    jsonArray.getJSONObject(i).put("leftSideImgPath", mDatabase.getImageDatadetail(leftSideImgPath));

                    String rightSideImgName = jsonArray.getJSONObject(i).getString("rightSideImgName");
                    String rightSideImgPath = jsonArray.getJSONObject(i).getString("rightSideImgPath");

                    jsonArray.getJSONObject(i).put("rightSideImgPath", mDatabase.getImageDatadetail(rightSideImgPath));


                    String backSideImgName = jsonArray.getJSONObject(i).getString("backSideImgName");
                    String backSideImgPath = jsonArray.getJSONObject(i).getString("backSideImgPath");

                    jsonArray.getJSONObject(i).put("backSideImgPath", mDatabase.getImageDatadetail(backSideImgPath));


                    String frontSideImgName = jsonArray.getJSONObject(i).getString("frontSideImgName");
                    String frontSideImgPath = jsonArray.getJSONObject(i).getString("frontSideImgPath");

                    jsonArray.getJSONObject(i).put("frontSideImgPath", mDatabase.getImageDatadetail(frontSideImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("TrolleyPaintingData", jsonObject.toString());
                    str = syncTrolleyPaintingDataImage(function, Constants.TROLLEY_SERVER_API, jsonObject, leftSideImgName, leftSideImgPath,
                            rightSideImgName, rightSideImgPath, backSideImgName, backSideImgPath, frontSideImgName, frontSideImgPath);
                    handleTrolleyPaintingImageSyncResponse("TrolleyPaintingData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public synchronized String syncTrolleyPaintingDataImage(String function, String urls, JSONObject jsonObject, String leftSideImgName,
                                                            String leftSideImgPath, String rightSideImgName, String rightSideImgPath,
                                                            String backSideImgName, String backSideImgPath,
                                                            String frontSideImgName, String frontSideImgPath) {

        return HttpUtils.POSTJSON(Constants.TROLLEY_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }


    public void handleTrolleyPaintingImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("TrolleyPaintingData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateTrolleyPaintingData("0", "1", "1", "1", "1", "1", id);

                } else {
                }
            }
        }

        Log.d("TrolleyPaintingData", "TrolleyPaintingData: " + resultout);
    }

    private void uploadFieldBannerData(String mdo_fieldBannerData) {

        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadFieldBannerServer(mdo_fieldBannerData, context).execute(Constants.FIELDBANNER_SERVER_API).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //AsyncTask Class for api batch code upload call
    private class UploadFieldBannerServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadFieldBannerServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadFieldBanner("MDO_FieldBannerData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        progressBarVisibility();
                        recordshowATL();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public String uploadFieldBanner(String function) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from FieldBannerData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String fieldBannerImgName = jsonArray.getJSONObject(i).getString("fieldBannerImgName");
                    String fieldBannerImgPath = jsonArray.getJSONObject(i).getString("fieldBannerImgPath");

                    jsonArray.getJSONObject(i).put("fieldBannerImgPath", mDatabase.getImageDatadetail(fieldBannerImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("FieldBannerData", jsonObject.toString());
                    str = syncFieldBannerDataSingleImage(function, Constants.FIELDBANNER_SERVER_API, jsonObject, fieldBannerImgName, fieldBannerImgPath);

                    handleFieldBannerDataImageSyncResponse("MDO_FieldBannerData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.close();


        }


        return str;
    }

    public synchronized String syncFieldBannerDataSingleImage(String function, String
            urls, JSONObject jsonObject, String fieldBannerImgName,
                                                              String fieldBannerImgPath) {
        return HttpUtils.POSTJSON(Constants.FIELDBANNER_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));


    }


    public void handleFieldBannerDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("MDO_FieldBannerData")) {

            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateFieldBannerData("0", "1", "1",
                            "1", "1", id);

                }
            }
        }

        Log.d("mdo_fieldBannerData", "mdo_fieldBannerData: " + resultout);
    }


    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container.setClickable(true);
        container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void uploadFieldBoardData(String FieldDayData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadFieldBoardServer(FieldDayData, context).execute(Constants.FIELDBOARD_SERVER_API).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //AsyncTask Class for api batch code upload call
    private class UploadFieldBoardServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadFieldBoardServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return UploadFieldData("MDO_FieldBoardData");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowATL();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public String UploadFieldData(String function) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from FieldBoardData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String fieldBoardImgPathName = jsonArray.getJSONObject(i).getString("fieldBoardImgPathName");
                    String fieldBoardImgPath = jsonArray.getJSONObject(i).getString("fieldBoardImgPath");

                    jsonArray.getJSONObject(i).put("fieldBoardImgPath", mDatabase.getImageDatadetail(fieldBoardImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("FieldBoard", jsonObject.toString());
                    str = syncFieldBoardDataSingleImage(function, Constants.FIELDBOARD_SERVER_API, jsonObject, fieldBoardImgPathName, fieldBoardImgPath);
                    handleFieldBoardDataImageSyncResponse("MDO_FieldBoardData", str, id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.close();

        }

        return str;
    }

    public synchronized String syncFieldBoardDataSingleImage(String function, String
            urls, JSONObject jsonObject, String fieldBoardImgPathName,
                                                             String fieldBoardImgPath) {

        return HttpUtils.POSTJSON(Constants.FIELDBOARD_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }

    public void handleFieldBoardDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("MDO_FieldBoardData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateFieldBoardData("0", "1", "1",
                            "1", "1", id);

                } else {
                }
            }
        }
        Log.d("MDO_FieldBoardData", "MDO_FieldBoardData: " + resultout);
    }

    private void uploadDataPostering(String atlVillagePosteringData) {

        if (config.NetworkConnection()) {
            try {
                new UploadATLVillagePosteringDataServer(atlVillagePosteringData, context).execute(Constants.POSTERING_SERVER_API).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //AsyncTask Class for api batch code upload call
    private class UploadATLVillagePosteringDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadATLVillagePosteringDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadATLVillagePosteringData("ATLVillagePosteringData");

        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                if (!resultout.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(resultout);
                    if (jsonObject.has("success")) {
                        if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                            mPref.save("isSubmitClickedPostering", "true");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                            builder.setTitle("MyActivity");
                            builder.setMessage("Data uploaded Successfully");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mPref.save("isSubmitClickedPostering", "true");
                                    // Config.refreshActivity(UploadData.this);
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
                            recordshowATL();
                        } else {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                            androidx.appcompat.app.AlertDialog alert = builder.create();
                            alert.show();
                        }

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    alertPoorConnection();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public String uploadATLVillagePosteringData(String atlVillagePosteringData) {
        String str = "";
        int action = 1;


        String searchQuery = "select * from ATLVillagePosteringData where isSynced ='0' ";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();

                    String finalPopupJson = jsonArray.getJSONObject(i).getString("finalPopupJson");

                    JSONArray jsonArrayPop = new JSONArray(finalPopupJson);
                    jsonArray.getJSONObject(i).put("finalPopupJson", jsonArrayPop);

                    String id = jsonArray.getJSONObject(i).getString("_id");
                    for (int j = 0; j < jsonArrayPop.length(); j++) {

                        String imgPath = jsonArrayPop.getJSONObject(j).getString("activityImgPath");
                        Log.d("imgPath", imgPath);
                        jsonArrayPop.getJSONObject(j).put("activityImgPath", mDatabase.getImageDatadetail(imgPath));
                    }


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("ATLVillagePosteringData", jsonObject.toString());
                    if (!jsonObject.toString().isEmpty()) {
                        str = syncATLVillagePosteringDataSingleImage(atlVillagePosteringData, Constants.POSTERING_SERVER_API, jsonObject);
                        // handleATLVillagePosteringDataDetailsImageSyncResponse("ATLVillagePosteringDataDetails", str);
                        handleATLVillagePosteringDataSyncResponse("ATLVillagePosteringData", str, id);
                    } else {

                        alertPoorConnection();
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();

            }
            cursor.close();
        }
        return str;
    }


    public synchronized String syncATLVillagePosteringDataSingleImage(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.POSTERING_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleATLVillagePosteringDataDetailsImageSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("ATLVillagePosteringDataDetails")) {

            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateATLVillagePosteringDataDetails("0", "1", "1");

                    }
                }
            } else {
                alertPoorConnection();
            }
        }

        Log.d("ATLVillagePosteringData", "ATLVillagePosteringData: " + resultout);
    }

    private void handleATLVillagePosteringDataSyncResponse(String function, String resultout, String id) throws JSONException {

        if (function.equals("ATLVillagePosteringData")) {
            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateATLVillagePosteringData("0", "1", id);

                    }
                }
            } else {
                alertPoorConnection();
            }

        }

        Log.d("ATLVillagePosteringData", "ATLVillagePosteringData: " + resultout);

    }

    private int recordshowPreSeason() {

        int totalcount = 0;
        try {
            String searchQuery = "";

            int count1 = 0;
            searchQuery = "select  *  from TestimonialCollectionData where  isSynced ='0'  ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count1 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count1;
            rndTestimonialCollection.setText("TESTIMONIAL COLLECTION" + " " + String.valueOf(count1));

            int count2 = 0;
            searchQuery = "select  *  from SanmanMelaData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndSanmanMela.setText("SANMAN MELA" + " " + String.valueOf(count2));


            int count3 = 0;
            searchQuery = "select  *  from VillageMeetingData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count3 = count3 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count3;
            rndVillageMeeting.setText("VILLAGE MEETING (DAY / NIGHT)" + " " + String.valueOf(count3));

            int count4 = 0;
            searchQuery = "select  *  from PromotionData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count4 = count4 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count4;
            rndPromotionThroughEntertainment.setText("PROMOTION THROUGH ENTERTAINMENT" + " " + String.valueOf(count4));

            int count5 = 0;
            searchQuery = "select  *  from CropSeminarData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count5 = count5 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count5;
            rndCropSeminar.setText("CROP SEMINAR" + " " + String.valueOf(count5));

            int count6 = 0;
            searchQuery = "select  *  from JeepCampaigningData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count6 = count6 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count6;
            rndJeepCampaigning.setText("JEEP CAMPAIGNING" + " " + String.valueOf(count6));

            int count7 = 0;
            searchQuery = "select  *  from PopDisplayData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count7 = count7 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count7;
            rndPopDisplay.setText("POP DISPLAY" + " " + String.valueOf(count7));


        } catch (Exception ex) {

        }
        return totalcount;
    }

    /**
     * <P>Method use to upload pre season data to server</P>
     */
    private void onPreSeasonBtnClicked() {

        btnUploadPreSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);

                builder.setTitle("MyActivity");
                builder.setMessage("Are you sure to upload data");

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

        });
    }

    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveToServer();
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

    private void saveToServer() {

        try {

            // if(rndmyactvity.isChecked()) {
            if (config.NetworkConnection()) {

                boolean flag = false;

                String getTag = null;
                int radioButtonID = radGrp6.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radGrp6.findViewById(radioButtonID);
                if (radioButton != null) {
                    getTag = (String) radioButton.getTag();
                }
                if (getTag != null) {
                    if (getTag.equals(getResources().getString(R.string.rndTestimonialCollection))) {
                        int count = 0;
                        String searchQuery = "";
                        searchQuery = "select  *  from TestimonialCollectionData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count = cursor.getCount();
                        cursor.close();
                        //done
                        if (count > 0) {

                            uploadDataTestimonialCollectionData("TestimonialCollectionData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndSanmanMela))) {
                        int count2 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from SanmanMelaData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count2 = count2 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count2 > 0) {
                            uploadDataSanmanMeladData("SanmanMelaData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndVillageMeeting))) {
                        int count3 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from VillageMeetingData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count3 = count3 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count3 > 0) {
                            uploadVillageMeeting("VillageMeetingData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndPromotionThroughEntertainment))) {
                        int count4 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from PromotionData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count4 = count4 + cursor.getCount();
                        cursor.close();
                        // add params
                        if (count4 > 0) {
                            uploadDataPromotionData("PromotionData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndCropSeminar))) {
                        int count5 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from CropSeminarData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count5 = count5 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count5 > 0) {
                            uploadCropSeminar("CropSeminarData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndJeepCampaigning))) {
                        int count6 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from JeepCampaigningData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count6 = count6 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count6 > 0) {
                            uploadJeepCampaigning("JeepCampaigningData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }


                    } else if (getTag.equals(getResources().getString(R.string.rndPopDisplay))) {
                        int count9 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from PopDisplayData where  isSynced ='0' ";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count9 = count9 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count9 > 0) {
                            uploadPopDisplayDataServer("PopDisplayData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }
                    }

                    // Toast.makeText(context, "" + getTag, Toast.LENGTH_SHORT).show();
                } else {
                    progressBarVisibility();
                    msclass.showMessage("Please select pre season activity to upload");
                }

            } else {
                progressBarVisibility();
                msclass.showMessage("Internet connection not available");
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            progressBarVisibility();
        }
    }

    private void uploadPopDisplayDataServer(String popDisplayData) {
        if (config.NetworkConnection()) {

            try {
                new UploadPopDisplayDataServer(popDisplayData, context).execute(Constants.POPDISPLAY_SERVER_API).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //AsyncTask Class for api batch code upload call

    private class UploadPopDisplayDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog p;

        public UploadPopDisplayDataServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadPopDisplayData("PopDisplayData");

        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPreSeason();
                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    private String uploadPopDisplayData(String popDisplayData) {
        String str = "";
        int action = 1;
        String searchQuery = "select * from PopDisplayData where  isSynced ='0' ";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();

                    String photoOneImgPath = jsonArray.getJSONObject(i).getString("photoOneImgPath");

                    jsonArray.getJSONObject(i).put("photoOneImgPath", mDatabase.getImageDatadetail(photoOneImgPath));

                    String photoTwoImgPath = jsonArray.getJSONObject(i).getString("photoTwoImgPath");

                    jsonArray.getJSONObject(i).put("photoTwoImgPath", mDatabase.getImageDatadetail(photoTwoImgPath));

                    String photoThreeImgPath = jsonArray.getJSONObject(i).getString("photoThreeImgPath");

                    jsonArray.getJSONObject(i).put("photoThreeImgPath", mDatabase.getImageDatadetail(photoThreeImgPath));

                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("PopDisplayData", jsonObject.toString());
                    str = syncPopDisplayImage(popDisplayData, Constants.POPDISPLAY_SERVER_API, jsonObject);
                    handlePopDisplayImageSyncResponse("PopDisplayData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    private String syncPopDisplayImage(String popDisplayData, String server, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.POPDISPLAY_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public void handlePopDisplayImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("PopDisplayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updatePopDisplayData("0", "1", "1",
                            "1", "1", id);


                } else {

                }
            }
        }

        Log.d("PopDisplayData", "PopDisplayData: " + resultout);


    }

    private void uploadJeepCampaigning(String jeepCampaigningData) {
        if (config.NetworkConnection()) {

            try {
                new UploadJeepCampaigningDataServer(jeepCampaigningData, context).execute(Constants.JEEPCAMPAIGNING_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <P>AsyncTask Class for api batch code upload call</P>
     */
    private class UploadJeepCampaigningDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadJeepCampaigningDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadJeepCampaigningData("JeepCampaigningData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");

                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String uploadJeepCampaigningData(String jeepCampaigningData) {

        String str = "";
        int action = 1;
        String searchQuery = "select  *  from JeepCampaigningData where isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String finalPopupJson = jsonArray.getJSONObject(i).getString("finalPopupJson");
                    JSONArray jsonArrayPop = new JSONArray(finalPopupJson);
                    jsonArray.getJSONObject(i).put("finalPopupJson", jsonArrayPop);

                    String id = jsonArray.getJSONObject(i).getString("_id");

                    for (int j = 0; j < jsonArrayPop.length(); j++) {

                        String imgPath = jsonArrayPop.getJSONObject(j).getString("activityImgPath");
                        Log.d("imgPath", imgPath);
                        jsonArrayPop.getJSONObject(j).put("activityImgPath", mDatabase.getImageDatadetail(imgPath));
                    }

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("JeepCampaigningData", jsonObject.toString());
                    if (!jsonObject.toString().isEmpty()) {
                        str = syncJeepCampaigningDataSingleImage(jeepCampaigningData, Constants.JEEPCAMPAIGNING_SERVER_API, jsonObject);
                        // handleJeepCampaigningImageSyncResponse("JeepCampaigningImageData", str);
                        handleJeepCampaigningSyncResponse("JeepCampaigningData", str, id);
                    } else {
                        alertPoorConnection();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public void handleJeepCampaigningImageSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("JeepCampaigningImageData")) {
            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateJeepCampaigningImageData("0", "1", "1");
                    } else {
                    }
                }
            } else {
                alertPoorConnection();
            }
        }
        Log.d("JeepCampaigningData", "JeepCampaigningData: " + resultout);

    }

    public void handleJeepCampaigningSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("JeepCampaigningData")) {

            if (!resultout.isEmpty()) {
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        mDatabase.updateJeepCampaigningData("0", "1", id);
                    }
                }
            } else {
                alertPoorConnection();
            }
        }
        Log.d("JeepCampaigningData", "JeepCampaigningData: " + resultout);

    }

    private void alertPoorConnection() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

    public synchronized String syncJeepCampaigningDataSingleImage(String jeepCampaigningData, String server, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.JEEPCAMPAIGNING_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    private void uploadCropSeminar(String cropSeminarData) {
        if (config.NetworkConnection()) {

            try {
                new UploadCropSeminarDataServer(cropSeminarData, context).execute(Constants.CROPSEMINAR_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String uploadCropSeminarData(String CropSeminarData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from CropSeminarData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));

                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhotoPath");

                    jsonArray.getJSONObject(i).put("farmerListPhotoPath", mDatabase.getImageDatadetail(farmerListPhoto));

                    String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhotoPath");

                    jsonArray.getJSONObject(i).put("retailerListPhotoPath", mDatabase.getImageDatadetail(retailerListPhoto));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("CropSeminarData", jsonObject.toString());
                    str = syncCropSeminarDataSingleImage(CropSeminarData, Constants.CROPSEMINAR_SERVER_API, jsonObject);
                    handleCropSeminarDataImageSyncResponse("CropSeminarData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    //AsyncTask Class for api batch code upload call
    private class UploadCropSeminarDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadCropSeminarDataServer(String Funname, Context context) {
        }

        protected void onPreExecute() {
//            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadCropSeminarData("CropSeminarData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized String syncCropSeminarDataSingleImage(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.CROPSEMINAR_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleCropSeminarDataImageSyncResponse(String function, String resultout, String id) throws JSONException {

        if (function.equals("CropSeminarData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateCropSeminarData("0", "1", "1",
                            "1", "1", id);

                } else {

                }
            }
        }


        Log.d("CropSeminarData", "CropSeminarData: " + resultout);
    }

    private void uploadDataPromotionData(String PromotionData) {
        if (config.NetworkConnection()) {
            try {
                new UploadPromotionDataServer(PromotionData, context).execute(Constants.PROMOTION_SERVER_API).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public String uploadPromotionData(String PromotionData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from PromotionData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();

                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("PromotionData", jsonObject.toString());
                    str = syncPromotionDataSingleImage(jsonObject);
                    handlePromotionDataImageSyncResponse("PromotionData", str, id);
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
    private class UploadPromotionDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadPromotionDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadPromotionData("PromotionData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized String syncPromotionDataSingleImage(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.PROMOTION_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    /**
     * <P>Method to update the testimonial collection data after success</P>
     *
     * @param function
     * @param resultout
     * @throws JSONException
     */
    public void handlePromotionDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("PromotionData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updatePromotionData("0", "1", "1", id);

                } else {

                }
            }
        }

        Log.d("PromotionData", "PromotionData: " + resultout);
    }


    private void uploadVillageMeeting(String VillageMeetingData) {

        if (config.NetworkConnection()) {

            try {
                new UploadVillageMeetingDataServer(VillageMeetingData, context).execute(Constants.VILLAGEMEETING_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadVillageMeetingDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;

        public UploadVillageMeetingDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
//            progressDailog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadVillageMeetingData("VillageMeetingData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadVillageMeetingData(String VillageMeetingData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from VillageMeetingData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("VillageMeetingData", jsonObject.toString());
                    str = syncVillageMeetingDataSingleImage(VillageMeetingData, Constants.VILLAGEMEETING_SERVER_API, jsonObject, activityImgName, activityImgPath);
                    handleVillageMeetingDataImageSyncResponse("VillageMeetingData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public synchronized String syncVillageMeetingDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                 String activityImgPath) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);

        return HttpUtils.POSTJSON(Constants.VILLAGEMEETING_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }


    public void handleVillageMeetingDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("VillageMeetingData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateVillageMeetingData("0", "1", "1", id);

                } else {

                }
            }
        }

        Log.d("VillageMeetingData", "VillageMeetingData: " + resultout);
    }


    private void uploadDataTestimonialCollectionData(String testimonialCollectionData) {
        if (config.NetworkConnection()) {
            try {
                new UploadTestimonialCollectionDataServer(testimonialCollectionData, context).execute(Constants.TESTIMONIAL_SERVER_API).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadTestimonialCollectionDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadTestimonialCollectionDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadTestimonialCollectionData("TestimonialCollectionData");
        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                String resultout = result.trim();
                Log.d("Response", resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }


                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    String id = jsonArray.getJSONObject(i).getString("_id");
                    jsonArray.getJSONObject(i).put("farmerPhotoPath", mDatabase.getImageDatadetail(farmerPhotoPath));

                    String successPhotoName = jsonArray.getJSONObject(i).getString("successPhotoName");
                    String successPhotoPath = jsonArray.getJSONObject(i).getString("successPhotoPath");

                    jsonArray.getJSONObject(i).put("successPhotoPath", mDatabase.getImageDatadetail(successPhotoPath));

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("TestimonialData", jsonObject.toString());
                    str = syncTestimonialCollectionDataSingleImage(jsonObject);

                    handleTestimonialCollectionDataImageSyncResponse("TestimonialCollectionData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public synchronized String syncRetailerVisitToFieldDataSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.retailerVisitToField_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public synchronized String syncPurchaseSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.PURCHASELIST_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public synchronized String syncTestimonialCollectionDataSingleImage(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.TESTIMONIAL_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    /**
     * <P>Method to update the testimonial collection data after success</P>
     *
     * @param function
     * @param resultout
     * @throws JSONException
     */
    public void handleTestimonialCollectionDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("TestimonialCollectionData")) {

            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateTestimonialCollectionData("0", "1", "1", "1", id);
                } else {
                }
            }
            Log.d("TestimonialData", "TestimonialCollectionData: " + resultout);
        }
    }

    /**
     * <P>Method use to upload post season data to server</P>
     */
    private void saveToServerPostSeason() {
        try {

            // if(rndmyactvity.isChecked()) {
            if (config.NetworkConnection()) {

                boolean flag = false;

                String getTag = null;
                int radioButtonID = radGrp5.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radGrp5.findViewById(radioButtonID);
                if (radioButton != null) {
                    getTag = (String) radioButton.getTag();
                }
                if (getTag != null) {
                    if (getTag.equals(getResources().getString(R.string.rndFieldPurchaseList))) {
                        int totalcount = 0;
                        String searchQuery = "";
                        searchQuery = "select  *  from PurchaseListData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        totalcount = cursor.getCount();
                        cursor.close();
                        //done
                        if (totalcount > 0) {
                            uploadData("PurchaseListData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }

                    } else if (getTag.equals(getResources().getString(R.string.rndRetailerVisitToField))) {
                        int count4 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from RetailerVisitToFieldData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count4 = count4 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count4 > 0) {
                            // uploadLivePlantDisplayRetailerData("PurchaseListData");
                            uploadDataRetailerVisitToFieldData("RetailerVisitToFieldData");
                        } else {
                            msclass.showMessage("Data not available for uploading");
                            progressBarVisibility();
                        }
                        //  rndRetailerVisitToField.setText("RETAILER VISIT TO FIELD" + " " + String.valueOf(count4));
                    } else if (getTag.equals(getResources().getString(R.string.rndCropShow))) {
                        int count5 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from CropShowData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count5 = count5 + cursor.getCount();
                        cursor.close();
//done
                        if (count5 > 0) {
                            uploadCropShow("CropShowData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else if (getTag.equals(getResources().getString(R.string.rndHarvestDay))) {
                        int count6 = 0;
                        String searchQuery;

                        searchQuery = "select  *  from HarvestDayData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count6 = count6 + cursor.getCount();
                        cursor.close();
                        // add params
                        if (count6 > 0) {
                            uploadHarvestDay("HarvestDayData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else if (getTag.equals(getResources().getString(R.string.rndFieldDay))) {
                        int count7 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from FieldDayData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count7 = count7 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count7 > 0) {
                            uploadFieldDay("FieldDayData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else if (getTag.equals(getResources().getString(R.string.rndLivePlantDataVillage))) {
                        int count8 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from LivePlantDisplayVillageData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count8 = count8 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count8 > 0) {
                            LivePlantDisplayVillage("LivePlantDisplayVillageData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }
                    } else if (getTag.equals(getResources().getString(R.string.rndLivePlantDataRetailer))) {
                        int count9 = 0;
                        String searchQuery;
                        searchQuery = "select  *  from LivePlantDisplayRetailerData where  isSynced ='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        count9 = count9 + cursor.getCount();
                        cursor.close();
                        //done
                        if (count9 > 0) {
                            uploadLivePlantDisplayRetailerData("LivePlantDisplayRetailerData");
                        } else {
                            progressBarVisibility();
                            msclass.showMessage("Data not available for uploading");
                        }
                    }

                    // Toast.makeText(context, "" + getTag, Toast.LENGTH_SHORT).show();
                } else {
                    progressBarVisibility();
                    msclass.showMessage("Please select post season activity to upload");
                }
            } else msclass.showMessage("Internet connection not available");
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            progressBarVisibility();
        }

    }


    private void doworkPostseason() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveToServerPostSeason();
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

    private void onPostSeasonBtnClicked() {
        btnUploadPostSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);

                builder.setTitle("MyActivity");
                builder.setMessage("Are you sure to upload data");

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
                        doworkPostseason();
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

        });
    }


    public void uploadRetailerSurveyData(String functionName, String paramKey) {
        if (config.NetworkConnection()) {
            dialog = new ProgressDialog(UploadData.this);
            dialog.setTitle("Data Uploading ...");
            dialog.setMessage("Please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            String str = null;
            String searchQuery = "select  *  from RetailerSurveyData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getResults(searchQuery);

                        jsonObject.put("Table1", jsonArray);
                        Log.d("rhtt", "uploadRetailerSurveyData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = new UploadRetailerKisanData(functionName, paramKey, jsonObject).execute(SERVER).get();

                    cursor.close();
                    if (str.contains("True")) {


                        for (int i = 0; i < jsonArray.length(); i++) {
                            String mno = jsonArray.getJSONObject(i).getString("retailerNumber");
                            mDatabase.updateRetailerSurveyData(mno, "1");
                            Log.d("mno", "onResultReceived: " + mno);
                        }
//                      Utility.showAlertDialog("Success", "Records Uploaded successfully", context);

                        Toast.makeText(context, " Retailer Survey Data Uploaded successfully.",
                                Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    } else {

                        Utility.showAlertDialog("Error", str, context);


                    }

                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            } else {
                dialog.dismiss();
                //      Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else {

            Utility.showAlertDialog("Error", "Internet network not available.", context);


        }
    }

    public void uploadAllcouponData(String functionName, String paramKey) {
        if (config.NetworkConnection()) {
            dialog = new ProgressDialog(UploadData.this);
            dialog.setTitle("Data Uploading ...");
            dialog.setMessage("Please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            String str = null;

            String searchQuery = "select  *  from CouponRecordData";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            //int count =mDatabase.getCountDt("");//cursor.getCount();
            //New comment 11-02-2021
            int count = mDatabase.getCountDt("CouponRecordData");
            cursor.close();
            mDatabase.close();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = mDatabase.getjsonResults(searchQuery);
                        jsonObject.put("Table1", jsonArray);
                        Log.d("rhtt", "AllcouponData: " + jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new UploadCoupondata(functionName, paramKey, jsonObject).execute(SERVER);
                    cursor.close();
                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            } else {
                dialog.dismiss();
                //        Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else {

            Utility.showAlertDialog("Error", "Internet network not available.", context);


        }
        dialog.dismiss();
    }

    public void uploadKisanClubData(String functionName, String paramKey) {
        if (config.NetworkConnection()) {
            dialog = new ProgressDialog(UploadData.this);
            dialog.setTitle("Data Uploading ...");
            dialog.setMessage("Please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
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
                    str = new UploadRetailerKisanData(functionName, paramKey, jsonObject).execute(SERVER).get();

                    cursor.close();
                    if (str.contains("True")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String mno = jsonArray.getJSONObject(i).getString("mobileNumber");
                            mDatabase.updateKisanData(mno, "1");
                            Log.d("mno", "onResultReceived: " + mno);
                        }
                        //      Utility.showAlertDialog("Success", "Records Uploaded successfully", context);

                        Toast.makeText(context, " Kisan Club Data Uploaded successfully.",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } else {

                        Utility.showAlertDialog("Error", str, context);


                    }

                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            } else {
                dialog.dismiss();
                //        Utility.showAlertDialog("Error", "Data not available for Uploading ", context);


            }

        } else {

            Utility.showAlertDialog("Error", "Internet network not available.", context);


        }
        dialog.dismiss();
    }

    public class UploadCoupondata extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;
        String paramKey;


        public UploadCoupondata(String Funname, String paramKey, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;
            this.paramKey = paramKey;


        }

        protected void onPreExecute() {

            dialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname)); //AllcouponData
            postParameters.add(new BasicNameValuePair(paramKey, obj.toString()));
            String userCode = pref.getString("UserID", null);
            Log.d("RequestUploadCoupondata", postParameters + "");


            String Urlpath = SERVER + "?userCode=" + userCode + "";
            Log.d("uploadUploadCoupondata", Urlpath);

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
                dialog.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

            dialog.dismiss();
            return builder.toString();
        }

        @SuppressLint("LongLogTag")
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    Log.d("ResponseUploadCoupondata", resultout);


                } else {
                    Log.d("rhtt", "uploadUploadCoupondata: " + result);

                }

                dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

        }
    }

    public class UploadRetailerKisanData extends AsyncTask<String, String, String> {

        JSONObject obj;
        String Funname;
        String paramKey;


        public UploadRetailerKisanData(String Funname, String paramKey, JSONObject obj) {

            this.obj = obj;
            this.Funname = Funname;
            this.paramKey = paramKey;


        }

        @Override
        protected void onPreExecute() {

            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair(paramKey, obj.toString()));
            String userCode = pref.getString("UserID", null);
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
//                dialog.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
//                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
//                dialog.dismiss();
            }

//            dialog.dismiss();
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    Log.d("ResponseKisanClubData", resultout);


                } else {
                    Log.d("rhtt", "uploadKisanClubData: " + result);

                }

                dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

        }
    }


    public void uploadRegisterDataModelRecords(String UploadBatchCodeData) {
        String str = null;
        String searchQuery = "select  *  from DemoModelData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        if (count > 0) {
            try {
                String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";

                str = new UploadDemoModelRegisterData(UploadBatchCodeData).execute(SERVER).get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

//AsyncTask Class for api batch code upload call

    @SuppressLint("StaticFieldLeak")
    public class UploadDemoModelRegisterData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        public UploadDemoModelRegisterData(String Funname) {


        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Data Uploading ...");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
            // progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadRegisterDataModel("mdo_demoModelVisitDetail");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.d("UploadResponse", resultout);
                recordshowVisit();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    public String uploadRegisterDataModel(String UploadBatchCodeData) {
        String str = "";

        // int action = 1; // Old
        int action = 3; // New added Entry date

        String searchQuery = "select  *  from DemoModelData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);
                    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("imgName");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        Log.d("ObjectasBytes", jsonObject.toString());
                        Log.d("ObjectasBytes", objAsBytes.toString());


                        str = syncRegisterDemoModelSingleImage(UploadBatchCodeData, SERVER, action, objAsBytes, uId, imageName, imagePath);

                        handleDemoImageSyncResponse(UploadBatchCodeData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();
                //     msclass.showMessage(ex.getMessage());


            }

        }


        return str;
    }


    public synchronized String syncRegisterDemoModelSingleImage(String function, String urls,
                                                                int action, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.toString();
    }

    public void handleDemoImageSyncResponse(String function, String resultout, String id) {
        if (function.equals("mdo_demoModelVisitDetail")) {
            if (resultout.contains("True")) {
                mDatabase.updateDemoModelData(id, "1", "1");
                //msclass.showMessage("Data Successfully Uploaded");


            } else {
                // msclass.showMessage(resultout + "mdo_demoModelVisitDetail--E");

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }


    public void uploadUpdatedDataModelRecords(String uploadReviewData) {
        String str = null;
        String searchQuery = "select  *  from DemoReviewData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        if (count > 0) {

            try {
                str = new UploadUpdatedDataServer(uploadReviewData).execute(SERVER).get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }


    public String uploadUpdatedDataModel(String uploadReviewData) {
        String str = "";
        //int action = 2; old
        int action = 4; //new  added  new coloum  Entry date

        String searchQuery = "select  *  from DemoReviewData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("imgName");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        Log.d("ObjectasBytes", jsonObject.toString());
                        Log.d("ObjectasBytes", objAsBytes.toString());
                        jsonArray = mDatabase.getResults(searchQuery);


                        str = syncUpdatedDemoVisitImage(uploadReviewData, SERVER, action, objAsBytes, uId, imageName, imagePath);

                        handleUpdatedDemoVisitImageResponse(uploadReviewData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }


        return str;
    }


    //AsyncTask Class for api batch code upload call

    public class UploadUpdatedDataServer extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        public UploadUpdatedDataServer(String Funname) {


        }

        protected void onPreExecute() {


            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Data Uploading ...");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadUpdatedDataModel("mdo_demoModelVisitDetail");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                if (progressDialog != null)
                    progressDialog.dismiss();
                try {


                } catch (Exception e) {
                    e.printStackTrace();

                }
                if (resultout.contains("True")) {


                    Log.d("Response", resultout);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public synchronized String syncUpdatedDemoVisitImage(String function, String urls,
                                                         int action, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.toString();
    }

    public void handleUpdatedDemoVisitImageResponse(String function, String resultout, String
            id) {
        if (function.equals("mdo_demoModelVisitDetail")) {
            if (resultout.contains("True")) {
                mDatabase.updateDemoReviwData(id, "1", "1");
                //msclass.showMessage("Data Successfully Uploaded");


            } else {
                //  msclass.showMessage(resultout + "--Error");

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }

    public String MDO_demandassesmentservey() {

        String str = null;
        String returnvalue = "";
        String Imagestring1 = "";
        String Imagestring2 = "";
        String ImageName = "";
        Cursor cursor = null;
        String searchQuery = "";
        int count = 0;
        searchQuery = "select * from mdo_demandassesmentsurvey where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivation where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivationtobe where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_awaremahycoproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_authoriseddistributorproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        count = count + cursor.getCount();
        cursor.close();
        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select distinct  * from mdo_demandassesmentsurvey where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivation where Status='0'";
                    object.put("Table2", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivationtobe where Status='0'";
                    object.put("Table3", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_awaremahycoproduct where Status='0'";
                    object.put("Table4", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_authoriseddistributorproduct where Status='0'";
                    object.put("Table5", mDatabase.getResults(searchQuery));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                returnvalue = syncDemandAssementdata("MDO_demandassesmentservey", objAsBytes, Imagestring1, Imagestring2, ImageName, "", cx.MDOurlpath);
                Log.i("Return Values is ",returnvalue);

            } catch (Exception ex) {
                 msclass.showMessage(ex.getMessage());
                dialog.dismiss();
            }
        } else {
            dialog.dismiss();
        }
        return returnvalue;
    }

    public String MDO_TravelData() {
        //if(config.NetworkConnection()) {
        // dialog.setMessage("Loading. Please wait...");
        //dialog.show();
        String str = null;
        String returnvalue = "";
        String Imagestring1 = "";
        String Imagestring2 = "";
        String ImageName = "sumit.jpg";
        Cursor cursor = null;
        String searchQuery = "";
        int count = 0;
        try {
            searchQuery = "select * from mdo_starttravel where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = cursor.getCount();
            cursor.close();

            searchQuery = "select * from mdo_endtravel where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = count + cursor.getCount();
            cursor.close();

            searchQuery = "select * from mdo_addplace where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = count + cursor.getCount();
            cursor.close();

            searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = count + cursor.getCount();
            cursor.close();


            if (count > 0) {
                try {
                    //START
                    byte[] objAsBytes = null;//new byte[10000];
                    JSONObject object = new JSONObject();
                    try {
                        searchQuery = "select * from mdo_starttravel where Status='0'";
                        object.put("Table1", mDatabase.getResults(searchQuery));
                        searchQuery = "select * from mdo_endtravel where Status='0'";
                        object.put("Table2", mDatabase.getResults(searchQuery));
                        searchQuery = "select * from mdo_addplace where Status='0'";
                        object.put("Table3", mDatabase.getResults(searchQuery));
                        searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
                        object.put("Table4", mDatabase.getResults(searchQuery));

                        Log.i("Retailer Data:",""+mDatabase.getResults(searchQuery).length());
                        searchQuery = "select * from mdo_retailerproductdetail where Status='0'";
                        object.put("Table5", mDatabase.getResults(searchQuery));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.i("Object is :",object.toString());
                        objAsBytes = object.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    // new UploadDataServernew("MDO_TravelData",objAsBytes, Imagestring1, Imagestring2, ImageName,"").execute(cx.MDOurlpath);
                    returnvalue = syncTraveldata("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "", cx.MDOurlpath);


                } catch (Exception ex) {
                     msclass.showMessage(ex.getMessage());

                }
            } else {
                 msclass.showMessage("Uploading data not available");

            }
        } catch (Exception ex) {

            Log.d(TAG, "MDO_TravelData: " + ex.toString());
        }
        return returnvalue;
    }

    public int rowcount() {
        int totalcount = 0;
        int count = 0;
        try {
            String searchQuery = "select  *  from TagData where Status='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = cursor.getCount();
            rndmyactvity.setText("Pending upload my activity data=" + String.valueOf(count));
            totalcount = totalcount + count;
            cursor.close();
            searchQuery = "select  *  from FarmerMaster where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = cursor.getCount();
            rndfarmer.setText("Pending upload farmer registration data=" + String.valueOf(count));
            cursor.close();
            totalcount = totalcount + count;
            searchQuery = "select  *  from TagData where imgstatus='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = cursor.getCount();
            rndimages.setText("Pending Images data=" + String.valueOf(count));
            cursor.close();
            totalcount = totalcount + count;


            int count2 = 0;
            searchQuery = "select * from mdo_starttravel where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();

            searchQuery = "select * from mdo_endtravel where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();

            searchQuery = "select * from mdo_addplace where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            rnd1.setText("Pending my travel data=" + String.valueOf(count2));

            count2 = 0;
            searchQuery = "select * from mdo_starttravel where imgstatus='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            searchQuery = "select * from mdo_endtravel where imgstatus='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            rnd3.setText("My travel start and end vehicle reading images =" + String.valueOf(count2));


            searchQuery = "select  *  from mdo_Retaileranddistributordata where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count = cursor.getCount();
            rnd2.setText("Pending retailer and distributor data=" + String.valueOf(count));
            cursor.close();


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }


    private int recordshowVisit() {
        int totalcount = 0;
        try {
            String searchQuery = "";

            int count2 = 0;
            searchQuery = "select  *  from DemoModelData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndVisit1.setText(getResources().getString(R.string.pending_registered_visit_data) + " " + String.valueOf(count2));

            searchQuery = "select  *  from DemoReviewData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndVisit2.setText(getResources().getString(R.string.pending_updated_visit_data) + " " + String.valueOf(count2));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }


    private int recordshowRetailerSurveyKisanClub() {
        int totalcount = 0;
        try {
            String searchQuery = "";

            int count2 = 0;
            searchQuery = "select  *  from RetailerSurveyData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndRetailerSurvey.setText(getResources().getString(R.string.retailer_survey) + " " + String.valueOf(count2));

            searchQuery = "select  *  from KisanClubData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndKisanClub.setText(getResources().getString(R.string.kisan_club) + " " + String.valueOf(count2));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }


    private int recordshowPostSeason() {
        int totalcount = 0;
        try {
            String searchQuery = "";


            int count2 = 0;
            searchQuery = "select  *  from PurchaseListData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndFieldPurchaseList.setText("PURCHASE LIST" + " " + String.valueOf(count2));
            int count3 = 0;
            searchQuery = "select  *  from DemoModelData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count3 = count3 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count3;
            rndfieldVisit.setText("FIELD VISIT" + " " + String.valueOf(count3));
            int count4 = 0;
            searchQuery = "select  *  from RetailerVisitToFieldData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count4 = count4 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count4;
            rndRetailerVisitToField.setText("RETAILER VISIT TO FIELD" + " " + String.valueOf(count4));
            int count5 = 0;
            searchQuery = "select  *  from CropShowData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count5 = count5 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count5;
            rndCropShow.setText("CROP SHOW" + " " + String.valueOf(count5));
            int count6 = 0;
            searchQuery = "select  *  from HarvestDayData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count6 = count6 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count6;
            rndHarvestDay.setText("HARVEST DAY" + " " + String.valueOf(count6));
            int count7 = 0;
            searchQuery = "select  *  from FieldDayData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count7 = count7 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count7;
            rndFieldDay.setText("FIELD DAY" + " " + String.valueOf(count7));

            int count8 = 0;
            searchQuery = "select  *  from LivePlantDisplayVillageData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count8 = count8 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count8;
            rndLivePlantDataVillage.setText("LIVE PLANT DATA VILLAGE" + " " + String.valueOf(count8));

            int count9 = 0;
            searchQuery = "select  *  from LivePlantDisplayRetailerData where  isSynced ='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count9 = count9 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count9;
            rndLivePlantDataRetailer.setText("LIVE PLANT DATA RETAILER COUNT" + " " + String.valueOf(count9));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }


    private int recordshowCoupon() {
        int totalcount = 0;
        try {
            String searchQuery = "";

            int count2 = 0;
            searchQuery = "select  *  from CouponRecordData where  isSynced ='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rndCoupon.setText(getResources().getString(R.string.pending_coupon_data) + " " + String.valueOf(count2));


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }

    private int recordshow() {
        int totalcount = 0;
        try {
            String searchQuery = "";

            int count2 = 0;
            searchQuery = "select * from mdo_starttravel where Status='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;

            searchQuery = "select * from mdo_endtravel where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            searchQuery = "select * from mdo_addplace where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            rnd1.setText("Pending my travel data=" + String.valueOf(count2));
            totalcount = totalcount + count2;
            count2 = 0;
            searchQuery = "select * from mdo_starttravel where imgstatus='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            searchQuery = "select * from mdo_endtravel where imgstatus='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = count2 + cursor.getCount();
            cursor.close();
            totalcount = totalcount + count2;
            rnd3.setText("My travel start and end vehicle reading images =" + String.valueOf(count2));
            searchQuery = "select  *  from mdo_Retaileranddistributordata where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            rnd2.setText("Pending retailer and distributor data=" + String.valueOf(count2));
            cursor.close();
            totalcount = totalcount + count2;

            searchQuery = "select * from mdo_demandassesmentsurvey where Status='0'";
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            count2 = cursor.getCount();
            rnd4.setText("Demand  Assessment Survey Data=" + String.valueOf(count2));
            cursor.close();
            totalcount = totalcount + count2;

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        return totalcount;
    }

    public void UploadFarmerData(String MDOFarmerMasterdataInsert) {
        //if(config.NetworkConnection()) {
        try {

            String str = null;
            String Imagestring1 = "";
            String Imagestring2 = "";
            String ImageName = "";
            String searchQuery = "select * from FarmerMaster where Status='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            if (count > 0) {
                try {
                    //START
                    byte[] objAsBytes = null;//new byte[10000];
                    JSONObject object = new JSONObject();
                    try {
                        searchQuery = "select * from FarmerMaster where Status='0'";
                        object.put("Table1", mDatabase.getResults(searchQuery));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        objAsBytes = object.toString().getBytes("UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //str = new UploadUpdatedDataServer(MDOFarmerMasterdataInsert, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(SERVER).get();
                    new UploadDataServer(MDOFarmerMasterdataInsert, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(SERVER);

                    //End


                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());


                }
            } else {

            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private int innovationuploadPendingdata() {
        int totalcount = 0;
        try {

            String str = null;
            String Imagestring1 = "";
            String Imagestring2 = "";
            String ImageName = "";
            // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
            String searchQuery = "select  *  from InnovationData where Status='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            totalcount = count;

            rndinnovation.setText("Pending Innovation Data=" + String.valueOf(count));
            cursor.close();


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return totalcount;
    }

    //Updated on 8th Sept 2021
    public void UploadalldataInnovation(String Functionname) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from InnovationData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                searchQuery = "select * from InnovationData where Status='0' and Imgname='" + ImageName + "' limit 1 ";
                                object.put("Table1", mDatabase.getResults(searchQuery));
                                Imagestring1 = mDatabase.getImageData(searchQuery, "imagepath");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //new UploadDataServer(Functionname, objAsBytes, Imagestring1, Imagestring2, ImageName, "t").execute(SERVER);
                            new UploadDataServerInnovation(Functionname, objAsBytes, Imagestring1, Imagestring2, ImageName, "t", this).execute(cx.MDOurlpath);
                            pd.dismiss();

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    } catch (Exception ex) {  // dialog.dismiss();
                        msclass.showMessage(ex.getMessage());


                    }
                } else {
                    //dialog.dismiss();
                    msclass.showMessage("Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void UploadalldataActvity(String tagdatauploadMDONew) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from TagData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                searchQuery = "select * from TagData where Status='0' and Imgname='" + ImageName + "' limit 1 ";
                                object.put("Table1", mDatabase.getResults(searchQuery));
                                Imagestring1 = mDatabase.getImageData(searchQuery, "Outcoordinate");
                                //Imagestring1 = mDatabase.getImageData(searchQuery, "Img");
                                Imagestring2 = mDatabase.getImageData(searchQuery, "OutLocation");


                                Intime = mDatabase.getInamename(searchQuery, "InTime");
                                // Stock Data
                                searchQuery = "select * from Tempstockdata where status='1' and INTime='" + Intime + "' ";
                                object.put("Table2", mDatabase.getResults(searchQuery));
                                // ImageName =mDatabase.getInamename(searchQuery,"Imgname");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new UploadDataServer(tagdatauploadMDONew, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(SERVER);
                            pd.dismiss();


                            //  new UploadUpdatedDataServer(tagdatauploadMDONew,objAsBytes, Imagestring1, Imagestring2, ImageName).execute(SERVER);//.get();;
                            //End
                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    } catch (Exception ex) {  // dialog.dismiss();
                        msclass.showMessage(ex.getMessage());


                    }
                } else {
                    //dialog.dismiss();
                    msclass.showMessage("Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void UploadalldataActvityNEW(String tagdatauploadMDONew) {
        if (config.NetworkConnection()) {
            //dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            String Imagestring1 = "";
            String Imagestring2 = "";
            String ImageName = "";
            try {
                String searchQuery = "select  *  from TagData where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {
                    //START
                    byte[] objAsBytes = null;//new byte[10000];
                    JSONObject object = new JSONObject();
                    try {
                        searchQuery = "select * from TagData where Status='0'";
                        object.put("Table1", mDatabase.getResults(searchQuery));
                        Imagestring1 = "";
                        //mDatabase.getImageData(searchQuery, "Outcoordinate");
                        Imagestring2 = "";// mDatabase.getImageData(searchQuery, "OutLocation");
                        searchQuery = "select * from Tempstockdata where status='1'";
                        object.put("Table2", mDatabase.getResults(searchQuery));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        objAsBytes = object.toString().getBytes("UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new UploadDataServernew(tagdatauploadMDONew, objAsBytes, Imagestring1, Imagestring2, ImageName, Intime).execute(cx.MDOurlpath);


                } else {
                    // msclass.showMessage("Activity data not available for uploading");
                }
                cursor.close();
                //End
            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
            }

        }
        dialog.dismiss();
    }

    public void UploadaImage2(String Functionname) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                String ImageName2 = "tt";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from " + Functionname + " where imgstatus='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("imgname"));
                                Imagestring1 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("imgpath")));
                                Log.i("Img64",Imagestring1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new UploadImageData(Functionname, Imagestring1, Imagestring2, ImageName, ImageName2, "t").execute(SERVER);
                            // new UploadImageData2(Functionname,  Imagestring1, Imagestring2, ImageName, ImageName2,"t").execute(SERVER);
                            pd.dismiss();

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    } catch (Exception ex) {  // dialog.dismiss();
                        msclass.showMessage(ex.getMessage());

                    }
                } else {
                    //dialog.dismiss();
                    //msclass.showMessage("Image Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void UploadaImage(String Functionname) {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                String ImageName2 = "";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
                String searchQuery = "select  *  from TagData where imgstatus='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();

                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("Imgname"));
                                Imagestring1 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("Outcoordinate")));

                                ImageName2 = cursor.getString(cursor.getColumnIndex("Imgname2"));
                                Imagestring2 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("OutLocation")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            new UploadImageData(Functionname, Imagestring1, Imagestring2, ImageName, ImageName2, "t").execute(SERVER);
                            pd.dismiss();

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    } catch (Exception ex) {  // dialog.dismiss();
                        msclass.showMessage(ex.getMessage());

                    }
                } else {
                    //dialog.dismiss();
                    // msclass.showMessage("Image Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {
                msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public class UploadDataServer extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;

        public UploadDataServer(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;

        }

        protected void onPreExecute() {

            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            String Urlpath = urls[0];

            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";

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
                pd.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                pd.dismiss();
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if (Funname.equals("tagdatauploadMDONew_Testold")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        String strdate = dateFormat.format(d);
                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Imgname='" + ImageName + "'");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where  INTime='" + Intime + "'");

                        pd.dismiss();

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsertNew")) {
                        mDatabase.deleterecord("delete from FarmerMaster ");
                        Toast.makeText(context, "Farmer registration Data uploaded Successfully.",
                                Toast.LENGTH_SHORT).show();
                        rndfarmer.setText("Pending upload farmer registration data=0");
                        pd.dismiss();

                    }
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");
                        pd.dismiss();
                    }


                } else {
                    pd.dismiss();
                    msclass.showMessage(result + "--E");

                }

                pd.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

    public class UploadDataServernew extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;

        public UploadDataServernew(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;

        }

        protected void onPreExecute() {

            // pd = new ProgressDialog(context);
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            String Urlpath = urls[0] + "?appName=Myactivity";

            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";

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
                pd.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());
                pd.dismiss();
            }

            // pd.dismiss();
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                String resultout = result.trim();

                if (pd != null) {
                    pd.dismiss();
                }

                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    // pd.dismiss();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);
                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' and imgstatus='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Status='0' ");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'");
                        // mDatabase.deleterecord("delete from TagData where Imgname='"+ImageName+"'");

                        Toast.makeText(context, "Activity Data uploaded Successfully.",
                                Toast.LENGTH_SHORT).show();

                        rndmyactvity.setText("Pending upload my activity data=0");

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsertNew")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        Toast.makeText(context, "Farmer registration Data uploaded Successfully.",
                                Toast.LENGTH_SHORT).show();
                        rndfarmer.setText("Pending upload farmer registration data=0");
                    }
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");
                        pd.dismiss();
                    }


                    // msclass.showMessage("data uploaded successfully.");
                    pd.dismiss();
                } else {
                    msclass.showMessage(result + "--E");
                    pd.dismiss();
                }
                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

    public class UploadImageData extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName, ImageName2;
        String Funname, Intime;

        public UploadImageData(String Funname, String Imagestring1, String Imagestring2, String ImageName, String ImageName2, String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.ImageName2 = ImageName2;
            this.Funname = Funname;
            this.Intime = Intime;

        }

        protected void onPreExecute() {

            // pd = new ProgressDialog(context);
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            // encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "UploadImages"));
            //postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            //String Urlpath=urls[0];

            String Urlpath = urls[0] + "?ImageName=" + ImageName + "&ImageName2=" + ImageName2;
Log.i("Urls_img_upld",Urlpath);
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
                pd.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                // msclass. showMessage(e.getMessage().toString());
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    pd.dismiss();
                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        String strdate = dateFormat.format(d);
                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' and Img='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Status='0' ");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'");
                        // mDatabase.deleterecord("delete from TagData where Imgname='"+ImageName+"'");
                        pd.dismiss();
                    }
                    if (Funname.equals("MDOFarmerMasterdataInsertNew")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");
                        pd.dismiss();
                    }
                    if (Funname.equals("UploadImages")) {
                        mDatabase.Updatedata("update TagData  set imgstatus='1' where Imgname='" + ImageName + "'");
                        pd.dismiss();
                    }
                    if (Funname.equals("mdo_starttravel")) {
                        mDatabase.Updatedata("update mdo_starttravel  set imgstatus='1' where imgname='" + ImageName + "'");
                        pd.dismiss();
                    }
                    if (Funname.equals("mdo_endtravel")) {
                        mDatabase.Updatedata("update mdo_endtravel  set imgstatus='1' where imgname='" + ImageName + "'");
                        pd.dismiss();
                    }
                } else {
                    msclass.showMessage(result + "--E");
                    pd.dismiss();
                }

                pd.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

    //NEW
    public synchronized String syncDemandAssementdata(String Funname, byte[] objAsBytes, String
            Imagestring1, String Imagestring2, String ImageName, String Intime, String urls) {


        String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);


        postParameters.add(new BasicNameValuePair("Type", Funname));
        postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
        String Urlpath = urls;
        Log.d("rohit", "doInBackground: " + Urlpath);
        Log.d("rohit", "doInBackground:params::: " + postParameters);
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
                    //builder.append(line).append("\n");
                    builder.append(line);
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            // handleImageSyncResponse(Funname, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.toString().trim();
    }

    public synchronized String syncTraveldata(String Funname, byte[] objAsBytes, String
            Imagestring1, String Imagestring2, String ImageName, String Intime, String urls) {


        String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);


        postParameters.add(new BasicNameValuePair("Type", Funname));
        postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
        postParameters.add(new BasicNameValuePair("input1", Imagestring1));
        postParameters.add(new BasicNameValuePair("input2", Imagestring2));
        String Urlpath = urls + "?appName=Myactivity";
        Log.d("rohit", "doInBackground: " + Urlpath);
        Log.d("rohit", "doInBackground:params::: " + postParameters);
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
                    //builder.append(line).append("\n");
                    builder.append(line);
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            //handleImageSyncResponse(Funname, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();

        }
        Log.i("Return string is ",builder.toString());
        return builder.toString();
    }


    class SyncMDODemandAssessement_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        String returnvalue = "";
        ProgressDialog progressDialog;

        public SyncMDODemandAssessement_Async(String tag) {
            this.tag = tag;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Data Uploading ...");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
            // progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            returnvalue = MDO_demandassesmentservey();
            return returnvalue;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                //if(function.equals("MDO_demandassesmentservey")) {
                if (result.contains("True")) {
                    mDatabase.Updatedata("update mdo_demandassesmentsurvey  set Status='1'");
                    mDatabase.Updatedata("update mdo_cultivation         set Status='1'");
                    mDatabase.Updatedata("update mdo_cultivationtobe     set Status='1'");
                    mDatabase.Updatedata("update mdo_awaremahycoproduct  set Status='1'");
                    mDatabase.Updatedata("update mdo_authoriseddistributorproduct  set Status='1'");
                    Toast.makeText(context, "DAS Data Uploaded successfully.",
                            Toast.LENGTH_SHORT).show();


                    rnd4.setText("Demand  Assessment Survey Data=0");


                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SyncMDOTravel_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        String returnvalue;
        ProgressDialog progressDialog;

        public SyncMDOTravel_Async(String tag) {
            this.tag = tag;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Data Uploading ...");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
            // progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            returnvalue = MDO_TravelData();
            return returnvalue;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {

            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();

                }
                Log.i("Results is ",result);
                if (result.contains("True")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);
                    mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                    mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'" + strdate + "'");
                    mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'" + strdate + "'");
                    mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'" + strdate + "'");
                    mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                    mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");

                    Toast.makeText(context, "Travel and Tag Data Uploaded successfully.",
                            Toast.LENGTH_SHORT).show();
                    rnd1.setText("Pending my travel data=0");
                    rnd2.setText("Pending retailer and distributor data=0");


                }else
                {
                    Toast.makeText(context, "Error is -"+result,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.toString(),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    class SyncDataAsync_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        ProgressDialog progressDialog;

        public SyncDataAsync_Async(String tag) {
            this.tag = tag;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Data Uploading ...");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
            // progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i("Upload Start","Go too");
            if (tag.equals("mdo_starttravel"))
                uploadstart(tag, cx.MDOurlpath);
            else if (tag.equals("mdo_endtravel")) {
                uploadstart("mdo_endtravel", cx.MDOurlpath);
            }
            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }

    public void uploadstart(String Functionname, String apiUrl) {
        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                String ImageName2 = "tt";
                String searchQuery = "select  DISTINCT imgname,imgpath from " + Functionname + " where imgstatus='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("imgname"));
                                Imagestring1 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("imgpath")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            syncSingleImage(Functionname, apiUrl, ImageName, Imagestring1);

                            cursor.moveToNext();
                        }
                        cursor.close();

                    } catch (Exception ex) {  // dialog.dismiss();
                        //msclass.showMessage(ex.getMessage());
                        Log.d("rohit", "doInBackground: " + ex.getMessage());
                    }
                } else {

                }

            } else {
                // msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
                Log.d("rohit", "Internet network not available: ");
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            // msclass.showMessage(ex.getMessage());
            Log.d("rohit", "exception: " + ex.getMessage());
            ex.printStackTrace();

        }
    }

    public synchronized void syncSingleImage(String function, String urls, String
            ImageName, String Imagestring1) {
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", "UploadImages"));
        postParameters.add(new BasicNameValuePair("input1", Imagestring1));
        postParameters.add(new BasicNameValuePair("input2", "tt"));
        String Urlpath = urls + "?ImageName=" + ImageName + "&ImageName2=" + ImageName;
        Log.d("rohit", "doInBackground: " + Urlpath);
        Log.d("rohit", "doInBackground:params::: " + postParameters);
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

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            handleImageSyncResponse(function, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void handleImageSyncResponse(String function, String resultout, String
            ImageName, String id) {
        try {


            if (resultout.contains("True")) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                String strdate = dateFormat.format(d);


                if (!ImageName.equals("")) {
                    if (function.equals("mdo_starttravel")) {
                        mDatabase.Updatedata("update mdo_starttravel  set imgstatus='1' where imgname='" + ImageName + "'");
                    }
                    if (function.equals("mdo_endtravel")) {
                        mDatabase.Updatedata("update mdo_endtravel  set imgstatus='1' where imgname='" + ImageName + "'");
                    }
                }


            } else {
                msclass.showMessage(resultout + "Uploading mdo_starttravel--E");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);

    }


    public void uploadCouponRecords(String uploadCouponRecords) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadCouponDataServer(uploadCouponRecords, context).execute(SERVER).get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadCouponRecordData(String UploadCouponRecordData) {
        String str = "";
        String Errormsg = "";
        int action = 2;// Uploadif =
        //progressDailog.show();

        String searchQuery = "select  *  from CouponRecordData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("farmerPhoto");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        Log.d("ObjectasBytes", jsonObject.toString());
                        Log.d("ObjectasBytes", objAsBytes.toString());
                        str = syncCouponSingleImage(UploadCouponRecordData, SERVER, action, objAsBytes, uId, imageName, imagePath);
                        if (str.contains("True")) {
                            handleCouponImageSyncResponse(UploadCouponRecordData, str, uId, imageName);
                        } else {
                            str = str.replaceAll("[{}]()-. ]+", "");//str.replace("\"", "").replace("{","").t;
                            str = str.replace("\"", "");
                            Errormsg = Errormsg + str;
                        }
                    }
                    Log.d("ObjectasBytes", jsonArray.toString());


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();
                //  msclass.showMessage(ex.getMessage());


            }
        }


        return Errormsg;//str;
    }


//AsyncTask Class for api batch code upload call

    private class UploadCouponDataServer extends AsyncTask<String, String, String> {

        ProgressDialog progressDailog;

        public UploadCouponDataServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadCouponRecordData("mdo_couponSchemeDownloadAndUpload");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                if (progressDailog != null) {
                    progressDailog.dismiss();

                }

                recordshowCoupon();
                // progressDailog.dismiss();
                if (resultout.contains("True")) {
                    msclass.showMessage("Data Saved Successfully .");


                } else {
                    msclass.showMessage(resultout + "Error");

                }
                Log.d("Response", resultout);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public synchronized String syncCouponSingleImage(String function, String urls, int action,
                                                     byte[] objAsBytes, String uId, String imageName, String imagePath) {
        String encodedImage = mDatabase.getImageDatadetail(imagePath);

        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));
        String userCode = pref.getString("UserID", null);
        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action + "&userCode=" + userCode;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.toString();
    }

    public void handleCouponImageSyncResponse(String function, String resultout, String id, String imageName) {
        if (function.equals("mdo_couponSchemeDownloadAndUpload")) {
            if (resultout.contains("True")) {
                mDatabase.updateCouponData(id, imageName, "1", "1");
                //msclass.showMessage("Data Successfully Uploaded");

            } else {
                try {
                    JSONObject object = new JSONObject(resultout);
                   /* String coplist = "";
                    if (object.length() > 1) {
                        JSONArray jArray1 = object.getJSONArray("Table1");
                        coplist = jArray1.toString();
                    }
                    JSONArray jArray = object.getJSONArray("Table");
                    JSONObject jObject = jArray.getJSONObject(0);
                    msclass.showMessage(jObject.getString("message").toString() + "Error\n" +
                            coplist.replace("\"", "") + ""); */
                    msclass.showMessage(resultout.replace("\"", "") + "");
                } catch (Exception ex) {
                    msclass.showMessage(ex.toString());
                }

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }

    public void uploadData(String PurchaseListData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadPurchaseListDataServer(PurchaseListData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadPurchaseListData(String LivePlantDisplayVillage) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from PurchaseListData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String id = jsonArray.getJSONObject(i).getString("_id");
                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    JSONArray jsonArray1 = new JSONArray(jsonArray.getJSONObject(i).getString("finalVillageJSON"));

                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    // String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    // String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");
                    jsonArray.getJSONObject(i).put("farmerListPhoto", mDatabase.getImageDatadetail(farmerListPhoto));
                    //jsonArray.getJSONObject(i).put("activityImgPath",  mDatabase.getImageDatadetail(activityImgPath));
                    //jsonArray.getJSONObject(i).put("retailerListPhoto",  mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("LivePlantDisplayVillage", jsonObject.toString());
                    //str = syncPurchaseSingleImage(LivePlantDisplayVillage, SERVER, jsonObject, farmerListPhotoName, farmerListPhoto);
                    //handlePurchaseImageSyncResponse("PurchaseListData", str,id);

                    str = syncPurchaseSingleImageAPI(jsonObject);
                    handlePurchaseImageSyncResponse("PurchaseListData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }
//AsyncTask Class for api batch code upload call

    private class UploadPurchaseListDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog progressDailog;

        public UploadPurchaseListDataServer(String Funname, Context context) {
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
        protected String doInBackground(String... urls) {

            return uploadPurchaseListData("PurchaseListData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncPurchaseSingleImage(String function, String urls, JSONObject
            jsonObject, String farmerListPhotoName,
                                                       String farmerListPhoto) {


        String encodedfarmerListImage = mDatabase.getImageDatadetail(farmerListPhoto);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("PurchaseListData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedfarmerListImage));

        String Urlpath = urls + "?farmerListPhotoName=" + farmerListPhotoName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handlePurchaseImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("PurchaseListData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updatePurchaseListData("0", "1", "1", id);


                } else {

                }
            }

            Log.d("PurchaseListData", "PurchaseListData: " + resultout);
        }
    }

    public void uploadLivePlantDisplayRetailerData(String LivePlantDisplayRetailerData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadLivePlantDisplayRetailerData(LivePlantDisplayRetailerData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadLivePlantDisplayRetailerDataBg(String LivePlantDisplayRetailerData) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from LivePlantDisplayRetailerData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResults(searchQuery);


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");


                    // String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    // String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    // String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    // String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");


                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    //jsonArray.getJSONObject(i).put("farmerListPhoto",  mDatabase.getImageDatadetail(farmerListPhoto));
                    // jsonArray.getJSONObject(i).put("retailerListPhoto",  mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    String id = jsonArray.getJSONObject(i).getString("_id");


                    Log.d("LivePlantDisplayVillage", jsonObject.toString());
                    // str = synchandleLivePlantDisplayRetailerSingleImage(LivePlantDisplayRetailerData, SERVER, jsonObject, activityImgName, activityImgPath);
                    str = synchandleLivePlantDisplayRetailerSingleImageAPI(jsonObject);
                    handleLivePlantDisplayRetailerImageSyncResponse("LivePlantDisplayRetailer", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    public synchronized String synchandleLivePlantDisplayRetailerSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.livePlantDisplayRetailerData_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }
//AsyncTask Class for api batch code upload call

    private class UploadLivePlantDisplayRetailerData extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadLivePlantDisplayRetailerData(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadLivePlantDisplayRetailerDataBg("LivePlantDisplayRetailerData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String synchandleLivePlantDisplayRetailerSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                             String activityImgPath) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("LivePlantDisplayRetailerData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleLivePlantDisplayRetailerImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("LivePlantDisplayRetailer")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateLivePlantDisplayRetailerData("0", "1", "1", id);


                } else {

                }
            }


            Log.d("LivePlantDisplay", "LivePlantDisplayRetailerData: " + resultout);
        }
    }

    public void LivePlantDisplayVillage(String LivePlantDisplayVillageData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadLivePlantDisplayVillageDataServer(LivePlantDisplayVillageData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadLivePlantDisplayVillageData(String LivePlantDisplayVillage) {


        String str = "";
        int action = 1;


        String searchQuery = "select  *  from LivePlantDisplayVillageData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResults(searchQuery);


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    //String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    // String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    //String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    // String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");


                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    //jsonArray.getJSONObject(i).put("farmerListPhoto",  mDatabase.getImageDatadetail(farmerListPhoto));
                    // jsonArray.getJSONObject(i).put("retailerListPhoto",  mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    String id = jsonArray.getJSONObject(i).getString("_id");
                    //str = syncLivePlantDisplayVillageDataSingleImage(LivePlantDisplayVillage, SERVER, jsonObject, activityImgName, activityImgPath);
                    str = syncLivePlantDisplayVillageDataSingleImageAPI(jsonObject);


                    handlesyncLivePlantDisplayVillageDataSingleImageResponse("LivePlantDisplayVillage",
                            str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    public synchronized String syncLivePlantDisplayVillageDataSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.livePlantDisplayVillageData_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }
//AsyncTask Class for api batch code upload call

    private class UploadLivePlantDisplayVillageDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadLivePlantDisplayVillageDataServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadLivePlantDisplayVillageData("LivePlantDisplayVillageData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncLivePlantDisplayVillageDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                          String activityImgPath) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("LivePlantDisplayVillageData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handlesyncLivePlantDisplayVillageDataSingleImageResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("LivePlantDisplayVillage")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateLivePlantDisplayVillageData("0", "1", "1", id);

                } else {

                }
            }


            Log.d("LivePlantDisplayVillage", "LivePlantDisplayVillageData: " + resultout);
        }
    }

    public void uploadDataRetailerVisitToFieldData(String RetailerVisitToFieldData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadRetailerVisitToFieldDataServer(RetailerVisitToFieldData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadRetailerVisitToFieldData(String RetailerVisitToField) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from RetailerVisitToFieldData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResults(searchQuery);


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    String farmerListImgName = jsonArray.getJSONObject(i).getString("farmerListImgName");
                    String farmerListImgPath = jsonArray.getJSONObject(i).getString("farmerListImgPath");
                    String farmerMobileNumber = jsonArray.getJSONObject(i).getString("farmerMobileNumber");

                    // String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    //  String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    //String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");
                    jsonArray.getJSONObject(i).put("farmerListImgPath", mDatabase.getImageDatadetail(farmerListImgPath));
                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    // jsonArray.getJSONObject(i).put("retailerListPhoto",  mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));


                    Log.d("RetailerVisitToField", jsonObject.toString());
                    //str = syncRetailerVisitToFieldDataSingleImage(RetailerVisitToField, SERVER, jsonObject, activityImgName, activityImgPath, farmerListImgName, farmerListImgPath);
                    str = syncRetailerVisitToFieldDataSingleImageAPI(jsonObject);
                    handleRetailerVisitToFieldDataImageSyncResponse(RetailerVisitToField, str, farmerMobileNumber);
                    //str = syncPurchaseSingleImage(jsonObject);
                    //handlePurchaseImageSyncResponse("PurchaseListData", str,id);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }
//AsyncTask Class for api batch code upload call

    private class UploadRetailerVisitToFieldDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadRetailerVisitToFieldDataServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadRetailerVisitToFieldData("RetailerVisitToField");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncRetailerVisitToFieldDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                                       String activityImgPath, String farmerListImgName, String farmerListImgPath) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);
        String encodedfarmerListImage = mDatabase.getImageDatadetail(farmerListImgPath);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("RetailerToVisitData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));
        postParameters.add(new BasicNameValuePair("encodedfarmerListImage", encodedfarmerListImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName + "&farmerListImgName=" + farmerListImgName;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleRetailerVisitToFieldDataImageSyncResponse(String function, String resultout, String farmerMobileNumber) throws JSONException {
        if (function.equals("RetailerVisitToField")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateRetailerVisitToFieldData(farmerMobileNumber, "1", "1", "1");


                } else {

                }
            }


            Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
        }
    }

    public void uploadCropShow(String CropShowData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadCropShowDataServer(CropShowData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public String uploadCropShowData(String LivePlantDisplayVillage) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from CropShowData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");


                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    jsonArray.getJSONObject(i).put("farmerListPhoto", mDatabase.getImageDatadetail(farmerListPhoto));
                    jsonArray.getJSONObject(i).put("retailerListPhoto", mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    String id = jsonArray.getJSONObject(i).getString("_id");


                    Log.d("CropShowData", jsonObject.toString());
                    //str = syncCropShowSingleImage(LivePlantDisplayVillage, SERVER, jsonObject, activityImgName, activityImgPath, farmerListPhotoName, farmerListPhoto, retailerListPhotoName, retailerListPhoto);
                    str = syncCropShowSingleImageSingleImageAPI(jsonObject);
                    handleCropShowImageSyncResponse("CropShowData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    public synchronized String syncCropShowSingleImageSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSONcontext(this, Constants.CROPSHOW_SERVER_API,
                jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }
//AsyncTask Class for api batch code upload call

    private class UploadCropShowDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadCropShowDataServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadCropShowData("CropShowData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncCropShowSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                       String activityImgPath, String farmerListPhotoName, String farmerListPhoto, String retailerListPhotoName, String retailerListPhoto) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);
        String encodedfarmerListImage = mDatabase.getImageDatadetail(farmerListPhoto);
        String encodedretailerListImage = mDatabase.getImageDatadetail(retailerListPhoto);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("CropShowData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));
        postParameters.add(new BasicNameValuePair("encodedfarmerListImage", encodedfarmerListImage));
        postParameters.add(new BasicNameValuePair("encodedretailerListImage", encodedretailerListImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName + "&farmerListPhotoName=" + farmerListPhotoName + "&retailerListPhotoName=" + retailerListPhotoName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleCropShowImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("CropShowData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateCropShowData("0", "1", "1",
                            "1", "1", id);


                } else {

                }
            }

            Log.d("LivePlantDisplayVillage", "LivePlantDisplayVillageData: " + resultout);
        }

    }

    public void uploadHarvestDay(String HarvestDayData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadHarvestDayDataServer(HarvestDayData, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public String uploadHarvestDayData(String harvestDayData) {
        String str = "";
        int action = 1;


        String searchQuery = "select  *  from HarvestDayData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {


            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();

                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");


                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    jsonArray.getJSONObject(i).put("farmerListPhoto", mDatabase.getImageDatadetail(farmerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    String id = jsonArray.getJSONObject(i).getString("_id");


                    Log.d("HarvestDayData", jsonObject.toString());
                    // str = syncHarvestDayDataSingleImage(harvestDayData, SERVER, jsonObject, activityImgName, activityImgPath, farmerListPhotoName, farmerListPhoto);
                    str = syncHarvestDayDataSingleImageAPI(jsonObject);
                    handleHarvestDayDataImageSyncResponse("HarvestDayData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            cursor.close();


        }


        return str;
    }

    public synchronized String syncHarvestDayDataSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.harvestDayData_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }
//AsyncTask Class for api batch code upload call

    private class UploadHarvestDayDataServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadHarvestDayDataServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadHarvestDayData("HarvestDayData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncHarvestDayDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                             String activityImgPath, String farmerListPhotoName, String farmerListPhoto) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);
        String encodedfarmerListImage = mDatabase.getImageDatadetail(farmerListPhoto);

        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("HarvestDayData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));
        postParameters.add(new BasicNameValuePair("encodedfarmerListImage", encodedfarmerListImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName + "&farmerListPhotoName=" + farmerListPhotoName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleHarvestDayDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("HarvestDayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateHarvestDayData("0", "1", "1", "1", id);

                } else {

                }
            }

            Log.d("HarvestDayData", "HarvestDayData: " + resultout);
        }
    }

    public void uploadFieldDay(String FieldDayData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadFieldDayServer(FieldDayData, context).execute(SERVER).get();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadFieldDayData(String FieldDayData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from FieldDayData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");
                    String farmerListPhotoName = jsonArray.getJSONObject(i).getString("farmerListPhotoName");
                    String farmerListPhoto = jsonArray.getJSONObject(i).getString("farmerListPhoto");
                    String retailerListPhotoName = jsonArray.getJSONObject(i).getString("retailerListPhotoName");
                    String retailerListPhoto = jsonArray.getJSONObject(i).getString("retailerListPhoto");


                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    jsonArray.getJSONObject(i).put("farmerListPhoto", mDatabase.getImageDatadetail(farmerListPhoto));
                    jsonArray.getJSONObject(i).put("retailerListPhoto", mDatabase.getImageDatadetail(retailerListPhoto));


                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    Log.d("FieldDayData", jsonObject.toString());
                    // str = syncFieldDayDataSingleImage(FieldDayData, SERVER, jsonObject, activityImgName, activityImgPath, farmerListPhotoName, farmerListPhoto, retailerListPhotoName, retailerListPhoto);
                    str = syncFieldDayDataSingleImageAPI(jsonObject);

                    handleFieldDayDataImageSyncResponse("FieldDayData", str, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.close();


        }


        return str;
    }

    public synchronized String syncFieldDayDataSingleImageAPI(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.fieldDayData_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }
//AsyncTask Class for api batch code upload call

    private class UploadFieldDayServer extends AsyncTask<String, String, String> {

        private ProgressDialog progressDailog;

        public UploadFieldDayServer(String Funname, Context context) {

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
        protected String doInBackground(String... urls) {

            return uploadFieldDayData("FieldDayData");

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
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
                        progressBarVisibility();
                        recordshowPostSeason();

                    } else {
                        alertPoorConnection();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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

            //end


        }

    }

    public synchronized String syncFieldDayDataSingleImage(String function, String urls, JSONObject jsonObject, String activityImgName,
                                                           String activityImgPath, String farmerListPhotoName,
                                                           String farmerListPhoto, String retailerListPhotoName,
                                                           String retailerListPhoto) {


        String encodedactivityImage = mDatabase.getImageDatadetail(activityImgPath);


        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("FieldDayData", jsonObject.toString()));
        postParameters.add(new BasicNameValuePair("encodedactivityImage", encodedactivityImage));

        String Urlpath = urls + "?activityImgName=" + activityImgName + "&farmerListPhotoName=" + farmerListPhotoName + "&retailerListPhotoName=" + retailerListPhotoName;
        Log.d("url", "image :" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();
//            progressDailog.dismiss();
        }
        return builder.toString();
    }


    public void handleFieldDayDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("FieldDayData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateFieldDayData("0", "1", "1", "1",
                            "1", id);

                } else {
                }
            }

            Log.d("LivePlantDisplayVillage", "LivePlantDisplayVillageData: " + resultout);
        }
    }

    public void uploadDataSanmanMeladData(String SanmanMeladData) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadSanmanMelaDataServer(SanmanMeladData, context).execute(Constants.SANMANMELA_SERVER_API).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <P>//AsyncTask Class for api batch code upload call</P>
     */
    private class UploadSanmanMelaDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog progressDailog;

        public UploadSanmanMelaDataServer(String Funname, Context context) {

        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadSanmanMelaData("SanmanMelaData");
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                redirecttoRegisterActivity(result);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        msclass.showMessage("Data Uploaded Successfully");
                        relPRogress.setVisibility(View.GONE);
                        container.setClickable(true);
                        container.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        recordshowPreSeason();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadData.this);
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
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String uploadSanmanMelaData(String SanmanMelaData) {
        String str = "";
        int action = 1;
        String searchQuery = "select  *  from SanmanMelaData where  isSynced ='0'";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {
            try {
                jsonArray = mDatabase.getResultsVillageDetails(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    String activityImgName = jsonArray.getJSONObject(i).getString("activityImgName");
                    String activityImgPath = jsonArray.getJSONObject(i).getString("activityImgPath");

                    jsonArray.getJSONObject(i).put("activityImgPath", mDatabase.getImageDatadetail(activityImgPath));
                    String id = jsonArray.getJSONObject(i).getString("_id");

                    jsonObject.put("Table", jsonArray.getJSONObject(i));
                    Log.d("SanmanMelaData", jsonObject.toString());
                    str = syncSanmanMelaDataSingleImage(SanmanMelaData, Constants.SANMANMELA_SERVER_API, jsonObject);
                    handleSanmanMelaDataImageSyncResponse("SanmanMelaData", str, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return str;
    }

    public synchronized String syncSanmanMelaDataSingleImage(String function, String urls, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.SANMANMELA_SERVER_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public void handleSanmanMelaDataImageSyncResponse(String function, String resultout, String id) throws JSONException {
        if (function.equals("SanmanMelaData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {

                    mDatabase.updateSanmanMelaData("0", "1", "1", id);

                } else {

                }
            }
        }
        Log.d("SanmanMelaData", "SanmanMelaData: " + resultout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler = null;
        }

    }

    public void downloadphotopath(String mdo_photoupdate) {
        if (config.NetworkConnection()) {
            String str = null;
            String uid = pref.getString("UserID", null);
            try {
                mDatabase.deleledata("mdo_photoupdate", " ");
                new MDOMasterData(6, uid, "", UploadData.this).execute(cx.MDOurlpath);
            } catch (Exception ex) {

            }
        }
    }

    public void redirecttoRegisterActivity(String result) {
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

    private class MDOMasterData extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String password;
        private ProgressDialog p;
        private Context ctx;
        public String returnstring;

        public MDOMasterData(int action, String username, String password, Context ctx) {
            this.action = action;
            this.username = username.trim();
            this.password = password;
            this.p = new ProgressDialog(ctx);
        }

        @Override
        protected void onPreExecute() {
            // dialog.setMessage("Loading....");
            //dialog.show();
            super.onPreExecute();
            //p.setMessage("Downloading ");
            //p.setIndeterminate(false);
            // p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //p.setCancelable(false);
            // p.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDOVerify_user"));
            // postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1 = cx.MDOurlpath + "?username=" + username + "&sapcode=" + action + "&password=" + password + "";
            HttpPost httppost = new HttpPost(Urlpath1);
            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");
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
                    returnstring = builder.toString();

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();


            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();


            }
            // p.dismiss();
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                JSONArray jArray4 = object.getJSONArray("Table");
                boolean f1;
                if (jArray4.length() > 0) {
                    f1 = mDatabase.InsertMyphotodata(jArray4);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logUploadDataEvent() {
        if (pref != null) {
            String userId = "", displayName = "";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null) {
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callUploadDataEvent(userId, displayName);
            }
        }
    }

    /*START -------------------------------- Added on 8th Sept 2021*/
    private class UploadDataServerInnovation extends AsyncTask<String, String, String> {
        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname, Intime;
        Context context;

        public UploadDataServerInnovation(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime, Context context) {
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;
            this.Intime = Intime;
            this.context = context;
        }

        protected void onPreExecute() {
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject obj = new JSONObject();
                String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("encodedData", encodeImage);
                jsonObject.put("input1", Imagestring1);
                jsonObject.put("input2", Imagestring2);
                obj.put("Table", jsonObject);
                Log.d("Innovation", "************ UPDATE API URL : " + Constants.POST_INNOVATION_DATA);
                Log.d("Innovation", "************ UPDATE API JSON OBJECT : " + obj);
                Log.d("Innovation", "************ UPDATE API JSON OBJECT ACCESS_TOKEN_TAG : " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                String result = HttpUtils.POSTJSON(Constants.POST_INNOVATION_DATA, obj, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                Log.d("Innovation", "************ UPDATE API RESPONSE : " + result);
                return result;
            } catch (Exception e) {
                Log.d("MSG", "MSG : " + e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                redirecttoRegisterActivity(resultout);
                if (pd != null) {
                    pd.dismiss();
                }
                Log.d("Innovation", "UPDATE API Response" + resultout);
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");
                        innovationuploadPendingdata();
                    }
                }
            } catch (Exception e) {
                Log.d("Innovation", e.getMessage());
                msclass.showMessage("Something went wrong, please try again later");
            }
        }
    }
    /*End ---------------------------------------------------------------- */
}


