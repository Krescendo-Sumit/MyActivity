package myactvity.mahyco;

import android.Manifest;
import android.app.Dialog;
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
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTMtrlQuality;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.ActivityModel;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.model.GTVMasterDataModel;
import myactvity.mahyco.model.GTVTravelActivityDataModel;
import myactvity.mahyco.myActivityRecording.atlActivity.ATLExhibitionActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.ATLPosteringActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.ATLWallPaintingActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.FieldBannerActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.FieldBoardActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.MarketDayActivity;
import myactvity.mahyco.myActivityRecording.atlActivity.TrolleyPaintingActivity;
import myactvity.mahyco.myActivityRecording.digitalMarketing.CallValidation;
import myactvity.mahyco.myActivityRecording.digitalMarketing.DistributorCallActivity;
import myactvity.mahyco.myActivityRecording.digitalMarketing.FarmerCallActivity;
import myactvity.mahyco.myActivityRecording.digitalMarketing.Mahakisan;
import myactvity.mahyco.myActivityRecording.digitalMarketing.RetailerCallActivity;
import myactvity.mahyco.myActivityRecording.digitalMarketing.TestimonialSharingActivity;
import myactvity.mahyco.myActivityRecording.digitalMarketing.WhatsappGrpCreatedActivity;
import myactvity.mahyco.myActivityRecording.generalActivity.AddressComplaintActivity;
import myactvity.mahyco.myActivityRecording.generalActivity.ReviewMeetingActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.CropSeminarActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.JeepCampaigningActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.POPDisplayActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.PromotionActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.SanmanMelaActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.TestimonialCollectionActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.VillageMeetingActivity;
import myactvity.mahyco.newupload.UploadDataNew;
import myactvity.mahyco.travelreport.GTVTravelAPI;

import static android.content.ContentValues.TAG;


public class MyTravel extends AppCompatActivity implements GTVTravelAPI.GTVListener {

    Button btnStarttravel, btnAddActivity, btnendtravel;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView lblwelcome, myTextProgress;
    public Spinner spDist, spTaluka, spVillage, spCropType, spProductName, spMyactvity, spComment;
    private Context context;
    private SqliteDatabase mDatabase;
    public CommonExecution cx;
    // TextView lblmyactvityrecord,lblfarmer;
    Config config;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    ProgressDialog dialog;
    private int[] tabIcons = {
            R.drawable.start,
            R.drawable.addtravel,
            R.drawable.end
    };
    Prefs mPref;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private Handler handler = new Handler();
    Messageclass msclass;
    SearchableSpinner sp_villagegtv2, sp_villagegtv1;
    String focusedVillage = "";
    double lati;
    double longi;
    String cordinates;
    String address = "";
    Button btnPunchInGTV1;
    Button btnAddActivityGtv1;
    Button btnPunchOutGTV1;
    Button btnPunchInGTV2;
    Button btnAddActivityGtv2;
    Button btnPunchOutGTV2;
    Button btn_sync, btn_sync_traveldata;
    Button btn_clearpunchdata, btn_clearactivitydata;
    String userCode = "";
    String selectedGTV1Village = "";
    String selectedGTV2Village = "";
    String selectedGTV1VillageCode = "";
    String selectedGTV2VillageCode = "";
    String selectedGtvtype = "";
    String selectedGtvSession = "";
    long mLastClickTime = 0;
    int gtv1InStatus = 0;
    int gtv1OutStatus = 0;
    int gtv2InStatus = 0;
    int gtv2OutStatus = 0;

    private FusedLocationProviderClient fusedLocationClient;
    Dialog dialog_activity_list;
    List<ActivityModel> activityModels;

    // ScrollView container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);
        getSupportActionBar().hide(); //<< this
        context = this;
        mPref = Prefs.with(context);
        try {
            cx = new CommonExecution(this);
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            preferences = getSharedPreferences("MyPref", 0);
            logTravelActivityData();
            editor = preferences.edit();

            progressBar = (ProgressBar) findViewById(R.id.myProgress);
            relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
            myTextProgress = (TextView) findViewById(R.id.myTextProgress);

            // container = (ScrollView) findViewById(R.id.container);
            spDist = (Spinner) findViewById(R.id.spDist);
            spTaluka = (Spinner) findViewById(R.id.spTaluka);
            spVillage = (Spinner) findViewById(R.id.spVillage);
            sp_villagegtv1 = (SearchableSpinner) findViewById(R.id.sp_villagegtv1);
            sp_villagegtv2 = (SearchableSpinner) findViewById(R.id.sp_villagegtv2);

            btnPunchInGTV1 = (Button) findViewById(R.id.btnPunchInGTV1);
            btnAddActivityGtv1 = (Button) findViewById(R.id.btnAddActivityGtv1);
            btnPunchOutGTV1 = (Button) findViewById(R.id.btnPunchOutGTV1);
            btnPunchInGTV2 = (Button) findViewById(R.id.btnPunchInGTV2);
            btnAddActivityGtv2 = (Button) findViewById(R.id.btnAddActivityGtv2);
            btnPunchOutGTV2 = (Button) findViewById(R.id.btnPunchOutGTV2);
            btn_sync = (Button) findViewById(R.id.btn_sync);
            btn_sync_traveldata = (Button) findViewById(R.id.btn_sync_traveldata);
            btn_clearpunchdata = (Button) findViewById(R.id.btn_clearpunchindata);
            btn_clearactivitydata = (Button) findViewById(R.id.btn_clearactivitydata);
            activityModels = new ArrayList<>();
            addActivityInList(1); // 1 for GTV 2 for Market
            mDatabase = SqliteDatabase.getInstance(this);
            mDatabase.addGTVActivityTable();
            checkGTVStatus(0);
            bindFocussedVillage();


            LatLng sourceLoc = new LatLng(19.873669, 75.366930);
            LatLng destinationLoc = new LatLng(19.887079, 75.368781);
            double d = SphericalUtil.computeDistanceBetween(sourceLoc, destinationLoc);
            Log.i("Distance is ", d + "");

            checkGTVStatus(0);

            config = new Config(this); //Here the context is passing
            lblwelcome = (TextView) findViewById(R.id.lblwelcome);
            userCode = preferences.getString("UserID", null);
            //  lblwelcome.setText("MDO NAME: "+preferences.getString("Displayname",null));
            // BindIntialData();
            mViewPager = (ViewPager) findViewById(R.id.viewcontainer);
            mViewPager.setOffscreenPageLimit(0);
            setupViewPager(mViewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            setupTabIcons();
            msclass = new Messageclass(this);
            btnStarttravel = (Button) findViewById(R.id.btnStarttravel);
            btnAddActivity = (Button) findViewById(R.id.btnAddActivity);
            //  Toast.makeText(context, "User Unit Name : "+preferences.getString("unit", null), Toast.LENGTH_SHORT).show();

            // We are hiding Add Activity Button for Veg Users
            // Consern Person : Mr. Munjaji Sir / Nitish Kumar.
            // Developer :  Sumit
            if (preferences.getString("unit", null).contains("VCBU"))
                btnAddActivity.setVisibility(View.GONE);


            btnendtravel = (Button) findViewById(R.id.btnendtravel);
            btnStarttravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);
                    if (5 > 0)
                        //Online check coupon balance amount
                        try {
                            if (config.NetworkConnection()) {
                                mPref.save(AppConstant.GTVSELECTEDBUTTON, "Start");
                                Intent intent = new Intent(MyTravel.this, starttravelnew.class);
                                //Intent intent = new Intent(context.getApplicationContext(), imgpick.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            } else {
                                new AlertDialog.Builder(context)
                                        .setMessage("Please check internet connection.")
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }
                            // new UploadCouponDataServer("mdo_couponSchemeDownloadAndUpload", context).execute(cx.MDOurlpath).get();

                        } catch (Exception e) {
                            Log.d(TAG, "onClick: Booking ");
                        }

                }
            });
            btnAddActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (isTourStareted()) {
                         /*   Intent intent = new Intent(MyTravel.this, MyActivityRecordingNew.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);*/
                            mPref.save(AppConstant.GTVSELECTEDBUTTON, "Market");
                            addActivityInList(2);// GTV activity 1
                            showActivityDialog(context);

                        } else {
                            Toast.makeText(context, "Please start tour before proceed.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //msclass.showMessage(ex.getMessage());
                    }

                }
            });
            btnendtravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!isTourStareted())
                            return;
                        mPref.save(AppConstant.GTVSELECTEDBUTTON, "End Travel");

                        int count = mDatabase.getUploadCount();
                        if (count > 0) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage(count + " records founds to be upload.\nPlease upload it and then end the travel,Otherwise it will not consider in day work.")
                                    .setPositiveButton("Go to upload", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intent = new Intent(MyTravel.this, UploadDataNew.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            if (config.NetworkConnection()) {
                                                Intent intent = new Intent(MyTravel.this, endTravelNew.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                context.startActivity(intent);
                                            } else {
                                                new AlertDialog.Builder(context)
                                                        .setMessage("Please check internet connection.")
                                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        }).show();
                                            }
                                        }
                                    }).show();
                        } else {
                            if (config.NetworkConnection()) {
                                Intent intent = new Intent(MyTravel.this, endTravelNew.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            } else {
                                new AlertDialog.Builder(context)
                                        .setMessage("Please check internet connection.")
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //msclass.showMessage(ex.getMessage());
                    }


                }
            });

            ImageView backButton = (ImageView) this.findViewById(R.id.backbtn);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }
        //MDO_TravelData();
        // adding this code to redirect the user download master data if data is not downloaded.
        int dbcount = mDatabase.getVillageCount();
        if (dbcount <= 0) {
            new AlertDialog.Builder(context)
                    .setMessage("It seems master data is not downloaded. Please download master data .")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            Intent intent = new Intent(context, DownloadMasterdata.class);
                            startActivity(intent);

                        }
                    })
                    .setCancelable(false)

                    .show();
        }
        //UploadData(); // new change -3-03-2021

        // new SyncMDOTravel_Async("").execute(); Comment  on 22-08-202
        // new SyncDataAsync_Async("mdo_photoupdate").execute(); 22 -08-2020

        // UploadaImage2("mdo_starttravel");
        // UploadaImage2("mdo_endtravel");

        sp_villagegtv1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    selectedGTV1Village = gm.Desc().trim();
                    selectedGTV1VillageCode = gm.Code().trim();

                    Log.d("Gtv fv 1", focusedVillage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_villagegtv2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    selectedGTV2Village = gm.Desc().trim();
                    selectedGTV2VillageCode = gm.Code().trim();
                    Log.d("Gtv fv 2", focusedVillage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        updateLocation();
        GTVButtonClicked();
    }

    public void addActivityInList(int type) {
        try {
            if (type == 1) {
                activityModels = null;
                activityModels = new ArrayList<ActivityModel>();
                activityModels.add(new ActivityModel(1, "1", "Testimonial Collection"));
                activityModels.add(new ActivityModel(2, "2", "Village Meeting (Day/Night)"));
                activityModels.add(new ActivityModel(3, "3", "Promotion Through Entertainment"));
                activityModels.add(new ActivityModel(4, "4", "Crop Seminar"));
                activityModels.add(new ActivityModel(5, "5", "Jeep Campaigning"));
                activityModels.add(new ActivityModel(6, "6", "Projector Meeting"));
                activityModels.add(new ActivityModel(7, "7", "Mahyco Maha Kisan Farmer"));
                activityModels.add(new ActivityModel(8, "8", "Plot Management"));
                activityModels.add(new ActivityModel(9, "9", "Crop Show"));
                activityModels.add(new ActivityModel(10, "10", "Field Day"));
                activityModels.add(new ActivityModel(11, "11", "Shelling Day"));
                activityModels.add(new ActivityModel(12, "12", "Utpadan Mohatsav"));
                activityModels.add(new ActivityModel(13, "13", "Retailer Visit To Field"));
                activityModels.add(new ActivityModel(14, "14", "Live Plant Display (Village)"));
                activityModels.add(new ActivityModel(15, "15", "Harvest Day"));
                activityModels.add(new ActivityModel(16, "16", "Field Banner"));
                activityModels.add(new ActivityModel(17, "17", "Trolley Painting"));
                activityModels.add(new ActivityModel(18, "18", "Field Board"));
                activityModels.add(new ActivityModel(19, "19", "Farmer  Visit( General)"));
                activityModels.add(new ActivityModel(20, "20", "Addressing Complaints"));
                activityModels.add(new ActivityModel(21, "21", "Sanman Mela"));
                activityModels.add(new ActivityModel(22, "22", "Jumbo/Innovation Day Registration"));
                activityModels.add(new ActivityModel(23, "23", "Postering"));
                activityModels.add(new ActivityModel(24, "24", "Wall Painting"));
                activityModels.add(new ActivityModel(25, "25", "Call Validation"));
                activityModels.add(new ActivityModel(26, "26", "Farmer Call"));
                activityModels.add(new ActivityModel(27, "27", "Retailer Call"));
                activityModels.add(new ActivityModel(28, "28", "Testimonial Sharing"));
                activityModels.add(new ActivityModel(29, "29", "Whatapp Group Creation"));
                activityModels.add(new ActivityModel(30, "30", "Distributor Call"));
            } else {
                activityModels = null;
                activityModels = new ArrayList<ActivityModel>();
                activityModels.add(new ActivityModel(1, "31", "POP display"));
                activityModels.add(new ActivityModel(2, "32", "Nursery Visit"));
                activityModels.add(new ActivityModel(3, "33", "Farmer/Purchase List Collection"));
                activityModels.add(new ActivityModel(4, "34", "Live Plant Display (Retail counter)"));
                activityModels.add(new ActivityModel(5, "35", "Exhibitions"));
                activityModels.add(new ActivityModel(6, "36", "Market Day Activities"));
                activityModels.add(new ActivityModel(7, "37", "Mandi Meeting"));
                activityModels.add(new ActivityModel(8, "38", "Distributor Visit"));
                activityModels.add(new ActivityModel(9, "39", "TMT Tag"));
                activityModels.add(new ActivityModel(10, "40", "Review Meeting"));
                activityModels.add(new ActivityModel(11, "41", "Retailer Visit"));
                activityModels.add(new ActivityModel(21, "21", "Sanman Mela"));
                activityModels.add(new ActivityModel(22, "22", "Jumbo/Innovation Day Registration"));
                activityModels.add(new ActivityModel(23, "23", "Postering"));
                activityModels.add(new ActivityModel(24, "24", "Wall Painting"));
                activityModels.add(new ActivityModel(25, "25", "Call Validation"));
                activityModels.add(new ActivityModel(26, "26", "Farmer Call"));
                activityModels.add(new ActivityModel(27, "27", "Retailer Call"));
                activityModels.add(new ActivityModel(28, "28", "Testimonial Sharing"));
                activityModels.add(new ActivityModel(29, "29", "Whatapp Group Creation"));
                activityModels.add(new ActivityModel(30, "30", "Distributor Call"));
            }

        } catch (Exception e) {

        }
    }

    public void showActivityDialog(Context context) {
        try {
            EditText et_searchtext;
            Button btn_close;
            ListView lst_activitylist;
            dialog_activity_list = new Dialog(context);
            dialog_activity_list.setContentView(R.layout.dialog_actvitylist);
            et_searchtext = dialog_activity_list.findViewById(R.id.et_searchtext);
            btn_close = dialog_activity_list.findViewById(R.id.btn_close);
            lst_activitylist = dialog_activity_list.findViewById(R.id.lst_activitylist);
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, activityModels);
            lst_activitylist.setAdapter(adapter);
            lst_activitylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ActivityModel activityModel = (ActivityModel) adapterView.getItemAtPosition(i);
                    if (activityModel != null && activityModel.getCode() != null)
                        redirectToSelectedActivity(activityModel.getCode());
                    else
                        Toast.makeText(context, "Activity not found.", Toast.LENGTH_SHORT).show();
                }
            });
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_activity_list.dismiss();
                }
            });
            et_searchtext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            dialog_activity_list.setTitle("Selecte Activity");
            dialog_activity_list.setCanceledOnTouchOutside(false);
            dialog_activity_list.show();


        } catch (Exception e) {

        }
    }

    public void redirectToSelectedActivity(String code) {
        try {
            int act = Integer.parseInt(code.trim());
            Intent intent = null;
            switch (act) {
                case 1:
                    intent = new Intent(context, TestimonialCollectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(context, VillageMeetingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(context, PromotionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(context, CropSeminarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(context, JeepCampaigningActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 6:
                    intent = new Intent(context, ProjectorMeetingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 7:
                    intent = new Intent(context, Mahakisan.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 8:
                    intent = new Intent(context, DemoModelVisit.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 9:
                    intent = new Intent(context, CropShowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 10:
                    intent = new Intent(context, FieldDayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 11:
                    intent = new Intent(context, ShellingDayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 12:
                    intent = new Intent(context, UtpadanMohatsavActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 13:
                    intent = new Intent(context, RetailerToVisitActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 14:
                    intent = new Intent(context, LivePlantDisplayVillageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 15:
                    intent = new Intent(context, HarvestDayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 16:
                    intent = new Intent(context, FieldBannerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 17:
                    intent = new Intent(context, TrolleyPaintingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 18:
                    intent = new Intent(context, FieldBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 19:
                    intent = new Intent(context, FarmerVisitsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 20:
                    intent = new Intent(context, AddressComplaintActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 21:
                    intent = new Intent(context, SanmanMelaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 22:
                    intent = new Intent(context, Innovation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 23:
                    intent = new Intent(context, ATLPosteringActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 24:
                    intent = new Intent(context, ATLWallPaintingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 25:
                    intent = new Intent(context, CallValidation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 26:
                    intent = new Intent(context, FarmerCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 27:
                    intent = new Intent(context, RetailerCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 28:
                    intent = new Intent(context, TestimonialSharingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 29:
                    intent = new Intent(context, WhatsappGrpCreatedActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 30:
                    intent = new Intent(context, DistributorCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 31:
                    intent = new Intent(context, POPDisplayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 32:
                    intent = new Intent(context, POPDisplayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 33:
                    intent = new Intent(context, PurchaseListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 34:
                    intent = new Intent(context, LivePlantDisplayVillageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 35:
                    intent = new Intent(context, ATLExhibitionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 36:
                    intent = new Intent(context, MarketDayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 37:
                    intent = new Intent(context, MarketDayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 38:
                    intent = new Intent(context, DistributerVisitsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 39:
                    intent = new Intent(context, RetailerandDistributorTag.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 40:
                    intent = new Intent(context, ReviewMeetingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 41:
                    intent = new Intent(context, RetailerVisitsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(context, "Invalid Activity", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

        }
    }

    public void GTVButtonClicked() {
        try {

            btnPunchInGTV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!isTourStareted()) {
                        return;
                    }

                    if (selectedGTV1Village.trim().equals("") || selectedGTV1Village.toLowerCase().contains("select")) {
                        Toast.makeText(context, "Please select focus village.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to continue punch in with " + selectedGTV1Village + " Village ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();
                                    //  Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
                                    Date entrydate = new Date();
                                    final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                                    selectedGtvtype = "GTV1";
                                    selectedGtvSession = "IN";


                                    GTVMasterDataModel gtvMasterDataModel = new GTVMasterDataModel();

                                    gtvMasterDataModel.setId(0);
                                    gtvMasterDataModel.setMdocode(userCode);
                                    gtvMasterDataModel.setCoordinate(cordinates);
                                    gtvMasterDataModel.setStartaddress(address);
                                    gtvMasterDataModel.setStartdate(InTime);
                                    gtvMasterDataModel.setDist("");
                                    gtvMasterDataModel.setTaluka("");
                                    gtvMasterDataModel.setVillage(selectedGTV1Village);
                                    gtvMasterDataModel.setImgname("GTV1.jpg");
                                    gtvMasterDataModel.setImgpath("path");
                                    gtvMasterDataModel.setTxtkm("0");
                                    gtvMasterDataModel.setPlace("");
                                    gtvMasterDataModel.setVehicletype("");
                                    gtvMasterDataModel.setSdate(0);
                                    gtvMasterDataModel.setGTVType(selectedGtvtype);
                                    gtvMasterDataModel.setGTVSession(selectedGtvSession);
                                    gtvMasterDataModel.setRemark("test");
                                    gtvMasterDataModel.setParentId(0);
                                    gtvMasterDataModel.setIsSynced(0);


                                    if (mDatabase.InsertGTVMaster(gtvMasterDataModel)) {
                                        mPref.save(AppConstant.GTVType, selectedGtvtype);
                                        mPref.save(AppConstant.ACTIVITYTYPE, "GTV");
                                        mPref.save(AppConstant.GTVSession, selectedGtvSession);
                                        mPref.save(AppConstant.GTVPastCoordinates, cordinates);
                                        mPref.save(AppConstant.GTVSelectedVillage, selectedGTV1Village);
                                        mPref.save(AppConstant.GTVSelectedVillageCode, selectedGTV1VillageCode);
                                        mPref.save(AppConstant.GTVPunchIdCoordinates, cordinates);
                                        mPref.save(AppConstant.GTVSelectedVillage1, selectedGTV1Village);
                                        mPref.save(AppConstant.GTVSelectedVillageCode1, selectedGTV1VillageCode);
                                        mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");
                                        if (CommonUtil.addGTVActivity(context, "0", "Punch In", cordinates, "Start Village", "GTV")) {
                                            //  Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                                        }
                                        showSharePreference();
                                        checkGTVStatus(1);
                                        Toast.makeText(context, "Punch in successfully.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).show();


                }
            });

            btnAddActivityGtv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //     Toast.makeText(context, "GTV1 Actvity", Toast.LENGTH_SHORT).show();
                    mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");
                    addActivityInList(1);// GTV activity 1
                    showActivityDialog(context);

                }
            });
            btnPunchOutGTV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    Date entrydate = new Date();
                    final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    selectedGtvtype = "GTV1";
                    selectedGtvSession = "OUT";


                    GTVMasterDataModel gtvMasterDataModel = new GTVMasterDataModel();

                    gtvMasterDataModel.setId(0);
                    gtvMasterDataModel.setMdocode(userCode);
                    gtvMasterDataModel.setCoordinate(cordinates);
                    gtvMasterDataModel.setStartaddress(address);
                    gtvMasterDataModel.setStartdate(InTime);
                    gtvMasterDataModel.setDist("");
                    gtvMasterDataModel.setTaluka("");
                    gtvMasterDataModel.setVillage(selectedGTV1Village);
                    gtvMasterDataModel.setImgname("GTV1.jpg");
                    gtvMasterDataModel.setImgpath("path");
                    gtvMasterDataModel.setTxtkm("0");
                    gtvMasterDataModel.setPlace("");
                    gtvMasterDataModel.setVehicletype("");
                    gtvMasterDataModel.setSdate(0);
                    gtvMasterDataModel.setGTVType(selectedGtvtype);
                    gtvMasterDataModel.setGTVSession(selectedGtvSession);
                    gtvMasterDataModel.setRemark("test");
                    gtvMasterDataModel.setParentId(0);

                    if (mDatabase.InsertGTVMaster(gtvMasterDataModel)) {
                        mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");

                        if (CommonUtil.addGTVActivity(context, "1111", "Punch Out", cordinates, "Punch Out Village", "GTV")) {
                            mPref.save(AppConstant.GTVType, "");
                            mPref.save(AppConstant.ACTIVITYTYPE, "");
                            mPref.save(AppConstant.GTVSession, "");
                            mPref.save(AppConstant.GTVPastCoordinates, cordinates);
                            mPref.save(AppConstant.GTVSelectedVillage, "");
                            mPref.save(AppConstant.GTVSelectedVillageCode, "");
                            mPref.save(AppConstant.GTVPunchIdCoordinates, "");


                        }

                        showSharePreference();
                        checkGTVStatus(2);
                        Toast.makeText(context, "Punch Out Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnPunchInGTV2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isTourStareted()) {
                        return;
                    }
                    if (selectedGTV2Village.trim().equals("") || selectedGTV2Village.toLowerCase().contains("select")) {
                        Toast.makeText(context, "Please select focus village.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to continue with " + selectedGTV2Village + " Village ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();
                                    //  Toast.makeText(context, "GTV2 IN", Toast.LENGTH_SHORT).show();
                                    Date entrydate = new Date();
                                    final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);

                                    selectedGtvtype = "GTV2";
                                    selectedGtvSession = "IN";


                                    GTVMasterDataModel gtvMasterDataModel = new GTVMasterDataModel();

                                    gtvMasterDataModel.setId(0);
                                    gtvMasterDataModel.setMdocode(userCode);
                                    gtvMasterDataModel.setCoordinate(cordinates);
                                    gtvMasterDataModel.setStartaddress(address);
                                    gtvMasterDataModel.setStartdate(InTime);
                                    gtvMasterDataModel.setDist("");
                                    gtvMasterDataModel.setTaluka("");
                                    gtvMasterDataModel.setVillage(selectedGTV2Village);
                                    gtvMasterDataModel.setImgname("GTV1.jpg");
                                    gtvMasterDataModel.setImgpath("path");
                                    gtvMasterDataModel.setTxtkm("0");
                                    gtvMasterDataModel.setPlace("");
                                    gtvMasterDataModel.setVehicletype("");
                                    gtvMasterDataModel.setSdate(0);
                                    gtvMasterDataModel.setGTVType(selectedGtvtype);
                                    gtvMasterDataModel.setGTVSession(selectedGtvSession);
                                    gtvMasterDataModel.setRemark("test");
                                    gtvMasterDataModel.setParentId(0);

                                    if (mDatabase.InsertGTVMaster(gtvMasterDataModel)) {
                                        mPref.save(AppConstant.GTVType, selectedGtvtype);
                                        mPref.save(AppConstant.ACTIVITYTYPE, "GTV");
                                        mPref.save(AppConstant.GTVSession, selectedGtvSession);
                                        mPref.save(AppConstant.GTVPastCoordinates, cordinates);
                                        mPref.save(AppConstant.GTVSelectedVillage, selectedGTV2Village);
                                        mPref.save(AppConstant.GTVSelectedVillageCode, selectedGTV2VillageCode);
                                        mPref.save(AppConstant.GTVPunchIdCoordinates, cordinates);
                                        mPref.save(AppConstant.GTVSelectedVillage2, selectedGTV2Village);
                                        mPref.save(AppConstant.GTVSelectedVillageCode2, selectedGTV2VillageCode);
                                        mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");
                                        if (CommonUtil.addGTVActivity(context, "0", "Punch In", cordinates, "Start Village", "GTV")) {
                                        }
                                        showSharePreference();
                                        checkGTVStatus(3);
                                        Toast.makeText(context, "Punch In Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).show();
                }
            });
            btnAddActivityGtv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    //  Toast.makeText(context, "GTV2 Actvity", Toast.LENGTH_SHORT).show();
                    mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");
                    addActivityInList(1);// GTV activity 1
                    showActivityDialog(context);
                }
            });
            btnPunchOutGTV2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    //   Toast.makeText(context, "GTV2 out", Toast.LENGTH_SHORT).show();
                    Date entrydate = new Date();
                    final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    selectedGtvtype = "GTV2";
                    selectedGtvSession = "OUT";


                    GTVMasterDataModel gtvMasterDataModel = new GTVMasterDataModel();

                    gtvMasterDataModel.setId(0);
                    gtvMasterDataModel.setMdocode(userCode);
                    gtvMasterDataModel.setCoordinate(cordinates);
                    gtvMasterDataModel.setStartaddress(address);
                    gtvMasterDataModel.setStartdate(InTime);
                    gtvMasterDataModel.setDist("");
                    gtvMasterDataModel.setTaluka("");
                    gtvMasterDataModel.setVillage(selectedGTV2Village);
                    gtvMasterDataModel.setImgname("GTV1.jpg");
                    gtvMasterDataModel.setImgpath("path");
                    gtvMasterDataModel.setTxtkm("0");
                    gtvMasterDataModel.setPlace("");
                    gtvMasterDataModel.setVehicletype("");
                    gtvMasterDataModel.setSdate(0);
                    gtvMasterDataModel.setGTVType(selectedGtvtype);
                    gtvMasterDataModel.setGTVSession(selectedGtvSession);
                    gtvMasterDataModel.setRemark("test");
                    gtvMasterDataModel.setParentId(0);

                    if (mDatabase.InsertGTVMaster(gtvMasterDataModel)) {

                        mPref.save(AppConstant.GTVSELECTEDBUTTON, "GTV");
                        if (CommonUtil.addGTVActivity(context, "1111", "Punch Out", cordinates, "Punch Out Village", "GTV")) {
                            mPref.save(AppConstant.GTVType, "");
                            mPref.save(AppConstant.ACTIVITYTYPE, "");
                            mPref.save(AppConstant.GTVSession, "");
                            mPref.save(AppConstant.GTVPastCoordinates, cordinates);
                            mPref.save(AppConstant.GTVSelectedVillage, "");
                            mPref.save(AppConstant.GTVSelectedVillageCode, "");
                            mPref.save(AppConstant.GTVPunchIdCoordinates, "");

                        }
                        showSharePreference();
                        checkGTVStatus(4);
                        Toast.makeText(context, "Punch Out Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadGtvMaster();
                }
            });

            btn_sync_traveldata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadGtvTravelData();
                }
            });

            btn_clearpunchdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.UpdateStatus("delete from GTVMasterData");
                    checkGTVStatus(0);
                    bindFocussedVillage();
                }
            });
            btn_clearactivitydata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.UpdateStatus("delete from GTVTravelActivityData");
                    checkGTVStatus(0);
                    bindFocussedVillage();
                }
            });


        } catch (Exception e) {

        }
    }

    public void uploadGtvTravelData() {
        try {
            String searchQuery12 = "select  *  from  GTVTravelActivityData where isSynced='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if (count > 0) {
                JsonArray jsonArray;

                String searchQuery = "select id,ActivityId,KACode,GTVType,ActivityName,ActivityType,ActivityDt,VillageCode,VillageName,LastCoordinates,Coordinates,GTVActivityKM,AppVersion,Remark,isSynced from GTVTravelActivityData where isSynced=0";
                jsonArray = mDatabase.getResultsRetro(searchQuery);
/*
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    jsonObject.addProperty("ActivityId","0");
                }*/
                JsonObject jsonFinal = new JsonObject();
                jsonFinal.add("gTVUserActivityModels", jsonArray);
                new GTVTravelAPI(context, this).UploadGTVTravelData(jsonFinal);
                Log.i("GTV  Travel Data ", jsonFinal.toString());

            } else {
                Toast.makeText(context, "No Data for upload.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    private void uploadGtvMaster() {

        try {
            String searchQuery12 = "select  *  from  GTVMasterData where isSynced='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if (count > 0) {
                JsonArray jsonArray;

                String searchQuery = "select id as _id,mdocode,coordinate,startaddress,startdate,dist,taluka,village,imgname as imgname,imgpath as imgpath1,isSynced,txtkm,place,'0' imgstatus,vehicletype,datetime() as entrydate,replace(date('now'),'-','') as sdate,GTVType,GTVSession,Remark,ParentId from GTVMasterData where isSynced='0'";
                jsonArray = mDatabase.getResultsRetro(searchQuery);

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    Log.i("Image path", jsonObject.get("imgpath1").toString().replace("\"", ""));
                    jsonObject.addProperty("imgpath", mDatabase.getImageDatadetail(jsonObject.get("imgpath1").toString().replace("\"", "")));
                }
                JsonObject jsonFinal = new JsonObject();
                jsonFinal.add("starttravelModels", jsonArray);
                new GTVTravelAPI(context, this).UploadGTV(jsonFinal);
                Log.i("Strat Travel ", jsonFinal.toString());

            } else {
                Toast.makeText(context, "No Data for upload.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    public void showSharePreference() {
        try {
            String TYPE = mPref.getString(AppConstant.GTVType, "");
            String SESSION = mPref.getString(AppConstant.GTVSession, "");
            String Village = mPref.getString(AppConstant.GTVSelectedVillage, "");
            String lastcoordinates = mPref.getString(AppConstant.GTVPastCoordinates, "");
            Toast.makeText(context, TYPE + "\n" + SESSION + "\n" + Village + "\n" + lastcoordinates, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

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

    public void bindFocussedVillage() {
        sp_villagegtv1.setAdapter(null);
        sp_villagegtv2.setAdapter(null);
        boolean b1 = true;
        boolean b2 = true;
        if (gtv1InStatus > 0) {
            b1 = false;
            String vname = mPref.getString(AppConstant.GTVSelectedVillage1, "");
            String vcode = mPref.getString(AppConstant.GTVSelectedVillageCode1, "");
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster(vcode, vname));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_villagegtv1.setAdapter(adapter);


        }

        if (gtv2InStatus > 0) {
            b2 = false;
            String vname = mPref.getString(AppConstant.GTVSelectedVillage2, "");
            String vcode = mPref.getString(AppConstant.GTVSelectedVillageCode2, "");
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster(vcode, vname));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_villagegtv2.setAdapter(adapter);

        }

        if (b1 || b2) {


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
                    Croplist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));
                    cursor.moveToNext();
                }
                cursor.close();

                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (b1)
                    sp_villagegtv1.setAdapter(adapter);
                if (b2)
                    sp_villagegtv2.setAdapter(adapter);

            } catch (
                    Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void UploadData() {
        if (config.NetworkConnection()) {
            relPRogress.setVisibility(View.VISIBLE);

            progressBar.setIndeterminate(true);
            new Thread(new Runnable() {
                public void run() {

                    handler.post(new Runnable() {
                        public void run() {
                            new SyncMDOTravel_Async("").execute();
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBarVisibility();
                    }
                }
            }).start();
        }
    }

    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        //container.setClickable(true);
        // container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setupTabIcons() {
        try {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            // TextView title = (TextView)(tabLayout.getChildAt(0));
            //title.setTextSize(10);
            // title.setTextSize(...);

            // TextView x = (TextView) tabLayout.getTabAt()getTabWidget().getChildAt(0).findViewById(android.R.id.title);
            //x.setTextSize(25);
        } catch (Exception e) {
            Log.d(TAG, "setupTabIcons: " + e.toString());
            e.printStackTrace();
            //  }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        try {


            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment(new starttravel(), "Start Travel");
            adapter.addFragment(new addtravel(), "Add Activity");
            adapter.addFragment(new endtravel(), "End Travel");
            // adapter.addFragment(new Nutrition(),  getResources().getString(R.string.Nutrition));
            viewPager.setAdapter(adapter);
        } catch (Exception e) {
            Log.d(TAG, "setupViewPager: " + e.toString());
            e.printStackTrace();
            //  }
        }
    }

    @Override
    public void OnGTVMasterUpload(String result) {
        Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnGTVTravelDataUpload(String result) {
        Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
        mDatabase.UpdateStatus("Update GTVTravelActivityData set isSynced=1 where isSynced=0");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void BindIntialData() {

        spDist.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT DISTRICT"));
        gm.add(new GeneralMaster("1000", "JALNA"));

        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDist.setAdapter(adapter);

        spTaluka.setAdapter(null);
        List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
        gm2.add(new GeneralMaster("0", "SELECT TALUKA"));
        gm2.add(new GeneralMaster("01", "JALNA"));
        ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                (this.context, android.R.layout.simple_spinner_dropdown_item, gm2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTaluka.setAdapter(adapter2);


        spVillage.setAdapter(null);
        List<GeneralMaster> gm3 = new ArrayList<GeneralMaster>();
        gm3.add(new GeneralMaster("0", "SELECT VILLAGE"));
        gm3.add(new GeneralMaster("01", "BHOKARDHAN"));
        ArrayAdapter<GeneralMaster> adapter3 = new ArrayAdapter<GeneralMaster>
                (this.context, android.R.layout.simple_spinner_dropdown_item, gm3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVillage.setAdapter(adapter3);


    }

    public void MDO_TravelData() {
        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading. Please wait...");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                Cursor cursor = null;
                String searchQuery = "";
                int count = 0;
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
                            searchQuery = "select * from mdo_retailerproductdetail where Status='0'";
                            object.put("Table5", mDatabase.getResults(searchQuery));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            objAsBytes = object.toString().getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //str = new UploadUpdatedDataServer("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "").execute(cx.MDOurlpath).get();
                        syncTraveldata("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "", cx.MDOurlpath);

                        //End
                        if (str.contains("True")) {
                            // msclass.showMessage("Records Uploaded successfully");

                        } else {
                            //msclass.showMessage(str);
                            ;
                        }


                    } catch (Exception ex) {
                        // msclass.showMessage(ex.getMessage());


                    }
                } else {

                }
            }
        } catch (Exception ex) {
            //msclass.showMessage(ex.getMessage());
            // Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
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
                String searchQuery = "";
                if (Functionname.contains("mdo_endtravel")) {
                    searchQuery = "select   *  from " + Functionname + " where imgstatus='0' ORDER BY enddate desc LIMIT 1   ";
                } else {
                    searchQuery = "select   *  from " + Functionname + " where imgstatus='0' ORDER BY startdate desc LIMIT 1   ";

                }
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
                            } catch (Exception e) {
                                //Toast.makeText(this, "Uplaoding"+e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            new UploadImageData(Functionname, Imagestring1, Imagestring2, ImageName, ImageName2, "t").execute(cx.MDOurlpath);

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
                        // Toast.makeText(this, "Uplaoding"+ex.toString(), Toast.LENGTH_SHORT).show();
                        //msclass.showMessage("Image Data not available for Uploading ");
                    }
                } else {
                    //dialog.dismiss();
                    //msclass.showMessage("Image Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {

                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            // Toast.makeText(this, "Uplaoding"+ex.toString(), Toast.LENGTH_SHORT).show();
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

            } catch (ClientProtocolException e) {
                e.printStackTrace();


            } catch (Exception e) {
                e.printStackTrace();

            }


            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);

                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Imgname='" + ImageName + "'");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where INTime='" + Intime + "'");

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsert")) {
                        mDatabase.deleterecord("delete from FarmerMaster");

                    }
                    if (Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='" + ImageName + "'");

                    }
                    if (Funname.equals("MDO_TravelData")) {
                        mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                        mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'" + strdate + "'");
                        mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'" + strdate + "'");
                        mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'" + strdate + "'");
                        mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                        mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");

                    }
                } else {
                    // msclass.showMessage(result+"--E");
                    //Toast.makeText(context, result+"Uploading Error", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception e) {
                e.printStackTrace();

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

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if (Funname.equals("tagdatauploadMDONew_Testold")) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        String strdate = dateFormat.format(d);
                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'" + strdate + "' and Status='1' and Img='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Status='0' ");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'");

                    }
                    if (Funname.equals("MDOFarmerMasterdataInsert")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");

                    }
                    if (Funname.equals("UploadImages")) {
                        mDatabase.Updatedata("update TagData  set imgstatus='1' where Imgname='" + ImageName + "'");

                    }
                    if (Funname.equals("mdo_starttravel")) {
                        mDatabase.Updatedata("update mdo_starttravel  set imgstatus='1' where imgname='" + ImageName + "'");

                    }
                    if (Funname.equals("mdo_endtravel")) {
                        mDatabase.Updatedata("update mdo_endtravel  set imgstatus='1' where imgname='" + ImageName + "'");

                    }
                } else {


                }


            } catch (Exception e) {
                e.printStackTrace();


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
        }

        @Override
        protected String doInBackground(Void... params) {

            uploadstart("mdo_photoupdate", cx.MDOurlpath);

            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            progressBarVisibility();
        }
    }

    class SyncMDOTravel_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        ProgressDialog progressDialog;

        public SyncMDOTravel_Async(String tag) {
            this.tag = tag;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            MDO_TravelData();
            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            progressBarVisibility();
            try {

              /*  relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Wait");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });*/
                progressBar.setIndeterminate(true);
                new SyncDataAsync_Async("mdo_photoupdate").execute();
            } catch (Exception ex) {
                progressBarVisibility();
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
                String searchQuery = "select  DISTINCT imgname,imgpath from " + Functionname + " where Status='0'";
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

                    }
                } else {

                }

            } else {
                // msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public synchronized void syncSingleImage(String function, String urls, String ImageName, String Imagestring1) {
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", "UploadImagesUpdate"));
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
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

            // msclass.showMessage(e.getMessage().toString());


        } catch (Exception e) {
            e.printStackTrace();
            // msclass. showMessage(e.getMessage().toString());

        }

        try {
            handleImageSyncResponse(function, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

            // msclass.showMessage(e.getMessage().toString());

        }
    }

    //NEW
    public synchronized void syncTraveldata(String Funname, byte[] objAsBytes, String Imagestring1, String Imagestring2, String ImageName, String Intime, String urls) {


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
            handleImageSyncResponse(Funname, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void handleImageSyncResponse(String function, String resultout, String ImageName, String id) {
        try {
            if (resultout.contains("True")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                String strdate = dateFormat.format(d);
                if (function.equals("MDO_TravelData")) {
                    mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                    mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'" + strdate + "'");
                    mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'" + strdate + "'");
                    mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'" + strdate + "'");
                    mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                    mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");
                }
                if (!ImageName.equals("")) {
                    mDatabase.Updatedata("update mdo_photoupdate  set Status='1' where imgname='" + ImageName + "'");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);

    }

    private void logTravelActivityData() {
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callMyTravelAndActivityRecordingEvent(userId, displayName);
            }
        }
    }

    public boolean isTourStareted() {
        try {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0);
            String usercode = sp.getString("UserID", null);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String strdate = dateFormat.format(d);

            JSONObject object = new JSONObject();
            object.put("Table1", mDatabase.getResults("select  * from mdo_endtravel  " +
                    "where strftime( '%Y-%m-%d', enddate)='" + strdate + "' and upper(mdocode)='" + usercode.toUpperCase() + "'"));
            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);

            JSONObject object2 = new JSONObject();
            object2.put("Table2", mDatabase.getResults("select  * from mdo_starttravel  " +
                    "where strftime( '%Y-%m-%d', startdate)='" + strdate + "' and" +
                    " upper(mdocode)='" + usercode.toUpperCase() + "'"));
            JSONArray jArray2 = object2.getJSONArray("Table2");//new JSONArray(result);
            if (jArray2.length() > 0) {
                // Started
                return true;
            } else {
                // Not Started
                msclass.showMessage("Tour is not started ,please fill tour started data.");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void checkGTVStatus(int typeid) {
        try {
            String dd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int snt = 0;
            gtv1InStatus = mDatabase.getGtvStatus("GTV1", "IN", dd);
            gtv1OutStatus = mDatabase.getGtvStatus("GTV1", "OUT", dd);
            gtv2InStatus = mDatabase.getGtvStatus("GTV2", "IN", dd);
            gtv2OutStatus = mDatabase.getGtvStatus("GTV2", "OUT", dd);

            if (gtv1InStatus == 0 && gtv1OutStatus == 0 && gtv2InStatus == 0 && gtv2OutStatus == 0) {
                typeid = 5;
            }


      /*      btnPunchOutGTV1.setEnabled(false);
            btnPunchOutGTV2.setEnabled(false);
            btnPunchInGTV1.setEnabled(false);
            btnPunchInGTV2.setEnabled(false);
            btnAddActivityGtv1.setEnabled(false);
            btnAddActivityGtv2.setEnabled(false);*/

            if (typeid == 5) {
                btnPunchOutGTV1.setEnabled(false);
                btnPunchOutGTV2.setEnabled(false);
                btnPunchInGTV1.setEnabled(true);
                btnPunchInGTV2.setEnabled(true);
                btnAddActivityGtv1.setEnabled(false);
                btnAddActivityGtv2.setEnabled(false);
            }
            if (typeid == 0) {

                typeid = Integer.parseInt(mPref.getString(AppConstant.GTVActiveActivity, "0"));

                btnPunchOutGTV1.setEnabled(false);
                btnPunchOutGTV2.setEnabled(false);
                btnPunchInGTV1.setEnabled(true);
                btnPunchInGTV2.setEnabled(false);
                btnAddActivityGtv1.setEnabled(false);
                btnAddActivityGtv2.setEnabled(false);
            }
            if (typeid == 1) {
                btnPunchOutGTV1.setEnabled(true);
                btnPunchOutGTV2.setEnabled(false);
                btnPunchInGTV1.setEnabled(false);
                btnPunchInGTV2.setEnabled(false);
                btnAddActivityGtv1.setEnabled(true);
                btnAddActivityGtv2.setEnabled(false);
            }

            if (typeid == 2) {
                btnPunchOutGTV1.setEnabled(false);
                btnPunchOutGTV2.setEnabled(false);
                btnPunchInGTV1.setEnabled(false);
                btnPunchInGTV2.setEnabled(true);
                btnAddActivityGtv1.setEnabled(false);
                btnAddActivityGtv2.setEnabled(false);
            }

            if (typeid == 3) {
                btnPunchOutGTV1.setEnabled(false);
                btnPunchOutGTV2.setEnabled(true);
                btnPunchInGTV1.setEnabled(false);
                btnPunchInGTV2.setEnabled(false);
                btnAddActivityGtv1.setEnabled(false);
                btnAddActivityGtv2.setEnabled(true);
            }
            if (typeid == 4) {
                btnPunchOutGTV1.setEnabled(false);
                btnPunchOutGTV2.setEnabled(false);
                btnPunchInGTV1.setEnabled(false);
                btnPunchInGTV2.setEnabled(false);
                btnAddActivityGtv1.setEnabled(false);
                btnAddActivityGtv2.setEnabled(false);
            }
            mPref.save(AppConstant.GTVActiveActivity, "" + typeid);

        } catch (Exception e) {
            Log.d("Test", e.getMessage());
        }
    }

}
