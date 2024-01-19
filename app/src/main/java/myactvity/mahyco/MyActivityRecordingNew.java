package myactvity.mahyco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
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
import myactvity.mahyco.myActivityRecording.generalActivity.ReviewMeetingActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.CropSeminarActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.JeepCampaigningActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.POPDisplayActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.PromotionActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.SanmanMelaActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.TestimonialCollectionActivity;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.VillageMeetingActivity;
import myactvity.mahyco.myActivityRecording.generalActivity.AddressComplaintActivity;


/**
 * Created by Akash Namdev on 2019-07-19.
 */


public class MyActivityRecordingNew extends AppCompatActivity {
//
//        implements

    private static final String TAG = "MyActivityRecordingNew";
    SearchableSpinner spActivity;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    String userCode;
    String activityType = "1";
    String screenName = "";
    String activityName = "1";
    Config config;
    Context context;
    Intent intent;
    Button openActivity, openActivityWallPainting, openExhibitionActivity;
    Button openFieldActivity, openFieldBannerActivity, openFieldTrollyActivity;
    Button openPreTestimonial, openPreSanmanMela, openPreVillageMeeting, openPrePromotion, openPreCropSeminar, openPreJeepCampaigning;
    Button openRetailerVisits, openDistVisits, openFarmerVisits;
    Button openSamruddhaKisanVisits, openAddressComplaint;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    RadioGroup radGroup;
    RadioButton radPost, radPre, radAlt, radGeneral, radDigital;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_myactivity_recording_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDatabase = SqliteDatabase.getInstance(this);
        context = this;
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        radGroup = (RadioGroup) findViewById(R.id.radGroup);

        spActivity = (SearchableSpinner) findViewById(R.id.spActivity);
        radPost = (RadioButton) findViewById(R.id.radPost);
        radPre = (RadioButton) findViewById(R.id.radPre);
        radAlt = (RadioButton) findViewById(R.id.radAlt);
        radGeneral = (RadioButton) findViewById(R.id.radGeneral);
        radDigital = (RadioButton) findViewById(R.id.radDigital);
        openActivity = (Button) findViewById(R.id.openActivity);
        openActivityWallPainting = (Button) findViewById(R.id.openActivityWallPainting);
        openExhibitionActivity = (Button) findViewById(R.id.openExhibitionActivity);
        openFieldActivity = (Button) findViewById(R.id.openFieldActivity);
        openFieldBannerActivity = (Button) findViewById(R.id.openFieldBannerActivity);
        openFieldTrollyActivity = (Button) findViewById(R.id.openFieldTrollyActivity);
        openPreTestimonial = (Button) findViewById(R.id.openPreTestimonial);
        openPreSanmanMela = (Button) findViewById(R.id.openPreSanmanMela);
        openPreVillageMeeting = (Button) findViewById(R.id.openPreVillageMeeting);
        openPrePromotion = (Button) findViewById(R.id.openPrePromotion);
        openPreCropSeminar = (Button) findViewById(R.id.openPreCropSeminar);
        openPreJeepCampaigning = (Button) findViewById(R.id.openPreJeepCampaigning);
        openRetailerVisits = (Button) findViewById(R.id.openRetailerVisits);
        openSamruddhaKisanVisits = (Button) findViewById(R.id.openSamruddhaKisanVisits);
        openDistVisits = (Button) findViewById(R.id.openDistVisits);
        openAddressComplaint = (Button) findViewById(R.id.openAddressComplaint);
        openFarmerVisits = (Button) findViewById(R.id.openFarmerVisits);
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);


      /*  openActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, ATLPosteringActivity.class);
                startActivity(intent);

            }
        });

        openFieldActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, FieldBoardActivity.class);
                startActivity(intent);

            }
        });
        openFieldBannerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, FieldBannerActivity.class);
                startActivity(intent);

            }
        });
        openFieldTrollyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, TrolleyPaintingActivity.class);
                startActivity(intent);

            }
        });
        openRetailerVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, RetailerVisitsActivity.class);
                startActivity(intent);

            }
        });
        openFarmerVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, FarmerVisitsActivity.class);
                startActivity(intent);

            }
        });

        openSamruddhaKisanVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, SamruddhaKisanVisitsActivity.class);
                startActivity(intent);
            }
        });

        openAddressComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, AddressComplaintActivity.class);
                startActivity(intent);
            }
        });
        openDistVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, DistributerVisitsActivity.class);
                startActivity(intent);

            }
        });

        openActivityWallPainting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, ATLWallPaintingActivity.class);
                startActivity(intent);

            }
        });

        openExhibitionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, ATLExhibitionActivity.class);
                startActivity(intent);
            }
        });

        openPreTestimonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, TestimonialCollectionActivity.class);
                startActivity(intent);
            }
        });

        openPreSanmanMela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, SanmanMelaActivity.class);
                startActivity(intent);
            }
        });

        openPreVillageMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, VillageMeetingActivity.class);
                startActivity(intent);
            }
        });

        openPrePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, PromotionActivity.class);
                startActivity(intent);
            }
        });

        openPreCropSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, CropSeminarActivity.class);
                startActivity(intent);
            }
        });

        openPreJeepCampaigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityRecordingNew.this, JeepCampaigningActivity.class);
                startActivity(intent);
            }
        });

       */
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

                    .show();
        }

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radPost:
                        if (radPost.isChecked()) {
                            activityType = "1";
                            activityName = "1";
                            bindSpinnerData(activityType);
                        } else if (radPre.isChecked()) {
                            activityType = "2";
                            activityName = "2";
                            bindSpinnerData(activityType);
                        } else if (radAlt.isChecked()) {
                            activityType = "3";
                            activityName = "3";
                            bindSpinnerData(activityType);
                        } else if (radGeneral.isChecked()) {
                            activityType = "4";
                            activityName = "4";
                            bindSpinnerData(activityType);

                        } else {
                            activityType = "5";
                            activityName = "5";
                            bindSpinnerData(activityType);

                        }
                        radPre.setChecked(false);
                        radAlt.setChecked(false);
                        radGeneral.setChecked(false);
                        radDigital.setChecked(false);
                        break;
                    case R.id.radPre:
                        if (radPre.isChecked()) {
                            activityType = "2";
                            activityName = "2";
                            bindSpinnerData(activityType);
                        } else if (radPost.isChecked()) {
                            activityType = "1";
                            activityName = "1";
                            bindSpinnerData(activityType);
                        } else if (radAlt.isChecked()) {
                            activityType = "3";
                            activityName = "3";
                            bindSpinnerData(activityType);
                        } else if (radGeneral.isChecked()) {
                            activityType = "4";
                            activityName = "4";
                            bindSpinnerData(activityType);

                        } else {
                            activityType = "5";
                            activityName = "5";
                            bindSpinnerData(activityType);

                        }
                        radAlt.setChecked(false);
                        radPost.setChecked(false);
                        radGeneral.setChecked(false);
                        radDigital.setChecked(false);
                        break;
                    case R.id.radAlt:
                        if (radAlt.isChecked()) {
                            activityType = "3";
                            activityName = "3";
                            bindSpinnerData(activityType);
                        } else if (radPost.isChecked()) {
                            activityType = "1";
                            activityName = "1";
                            bindSpinnerData(activityType);
                        } else if (radPre.isChecked()) {
                            activityType = "2";
                            activityName = "2";
                            bindSpinnerData(activityType);
                        } else if (radGeneral.isChecked()) {
                            activityType = "4";
                            activityName = "4";
                            bindSpinnerData(activityType);

                        } else {
                            activityType = "5";
                            activityName = "5";
                            bindSpinnerData(activityType);

                        }
                        radPost.setChecked(false);
                        radPre.setChecked(false);
                        radGeneral.setChecked(false);
                        radDigital.setChecked(false);
                        break;
                    case R.id.radGeneral:
                        if (radGeneral.isChecked()) {
                            activityType = "4";
                            activityName = "4";
                            bindSpinnerData(activityType);
                        } else if (radPost.isChecked()) {
                            activityType = "1";
                            activityName = "1";
                            bindSpinnerData(activityType);
                        } else if (radPre.isChecked()) {
                            activityType = "2";
                            activityName = "2";
                            bindSpinnerData(activityType);
                        } else if (radAlt.isChecked()) {
                            activityType = "3";
                            activityName = "3";
                            bindSpinnerData(activityType);

                        } else {
                            activityType = "5";
                            activityName = "5";
                            bindSpinnerData(activityType);

                        }
                        radPost.setChecked(false);
                        radPre.setChecked(false);
                        radGeneral.setChecked(true);
                        radDigital.setChecked(false);
                        break;
                    case R.id.radDigital:
                        if (radDigital.isChecked()) {
                            activityType = "5";
                            activityName = "5";
                            bindSpinnerData(activityType);

                        } else if (radGeneral.isChecked()) {
                            activityType = "4";
                            activityName = "4";
                            bindSpinnerData(activityType);
                        } else if (radPost.isChecked()) {
                            activityType = "1";
                            activityName = "1";
                            bindSpinnerData(activityType);
                        } else if (radPre.isChecked()) {
                            activityType = "2";
                            activityName = "2";
                            bindSpinnerData(activityType);
                        } else if (radAlt.isChecked()) {
                            activityType = "3";
                            activityName = "3";
                            bindSpinnerData(activityType);
                        }
                        radPost.setChecked(false);
                        radPre.setChecked(false);
                        radGeneral.setChecked(false);
                        radDigital.setChecked(true);
                        break;

                }
                Log.d("activityType", activityType);
            }
        });

        spActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    screenName = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    Log.d("ScreenName", screenName + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!screenName.isEmpty() && !(screenName.contains("SELECT POST SEASON ACTIVITY") ||
                        screenName.contains("SELECT PRE SEASON ACTIVITY") ||
                        screenName.contains("SELECT ATL ACTIVITY") ||
                        screenName.contains("SELECT GENERAL ACTIVITY") ||
                        screenName.contains("SELECT DIGITAL MARKETING") ||
                        screenName.contains("SELECT ACTIVITY"))) {

                    if (activityType.equalsIgnoreCase("1")) {
                        navigateToPostSeasonScreen(screenName);
                    } else if (activityType.equalsIgnoreCase("2")) {
                        navigateToPreSeasonScreen(screenName);
                    } else if (activityType.equalsIgnoreCase("3")) {
                        navigateToATLScreen(screenName);
                    } else if (activityType.equalsIgnoreCase("4")) {
                        navigateToGeneralScreen(screenName);
                    } else if (activityType.equalsIgnoreCase("5")) {
                        navigateToDigitalscreen(screenName);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void navigateToDigitalscreen(String screenName) {

        if (pref.getString("RoleID", null).equals("2")
                || pref.getString("RoleID", null).equals("9")
                || pref.getString("RoleID", null).equals("7")
                || pref.getString("RoleID", null).equals("5")
                || pref.getString("RoleID", null).equals("8")) {
            if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), FarmerCallActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                // intent = new Intent(context.getApplicationContext(), CallValidation.class);
                intent = new Intent(context.getApplicationContext(), RetailerCallActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                intent = new Intent(context.getApplicationContext(), DistributorCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), WhatsappGrpCreatedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), TestimonialSharingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), CallValidation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), FarmerCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                // intent = new Intent(context.getApplicationContext(), CallValidation.class);
                intent = new Intent(context.getApplicationContext(), RetailerCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                intent = new Intent(context.getApplicationContext(), DistributorCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), WhatsappGrpCreatedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), TestimonialSharingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), CallValidation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        bindSpinnerData(activityType);
    }

    private void bindSpinnerData(String activityTypeCode) {

        try {
            spActivity.setAdapter(null);
            List<GeneralMaster> list = new ArrayList<GeneralMaster>();
            String searchQuery = "SELECT activityNameCode,ActivityName   FROM MyActivityMaster where activityTypeCode='" + activityTypeCode + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            switch (activityName) {
                case "1":
                    list.add(new GeneralMaster("SELECT POST SEASON ACTIVITY",
                            "SELECT POST SEASON ACTIVITY"));
                    break;
                case "2":
                    list.add(new GeneralMaster("SELECT PRE SEASON ACTIVITY",
                            "SELECT PRE SEASON ACTIVITY"));
                    break;
                case "3":
                    list.add(new GeneralMaster("SELECT ATL ACTIVITY",
                            "SELECT ATL ACTIVITY"));
                    break;
                case "4":
                    list.add(new GeneralMaster("SELECT GENERAL ACTIVITY",
                            "SELECT GENERAL ACTIVITY"));
                    break;
                case "5":
                    list.add(new GeneralMaster("SELECT DIGITAL MARKETING",
                            "SELECT DIGITAL MARKETING"));
                    break;

                default:
                    list.add(new GeneralMaster("SELECT ACTIVITY",
                            "SELECT ACTIVITY"));
                    break;
            }


            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                list.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(1).toUpperCase()));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spActivity.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * <P>Method to naviagte Post season screen to respective activity</P>
     *
     * @param screenName
     */
    public void navigateToPostSeasonScreen(String screenName) {

        if (pref.getString("RoleID", null).equals("2")
                || pref.getString("RoleID", null).equals("9")
                || pref.getString("RoleID", null).equals("7")
                || pref.getString("RoleID", null).equals("5")
                || pref.getString("RoleID", null).equals("8")) {
//        FARMER/PURCHASE LIST COLLECTION
            if (screenName.equalsIgnoreCase("8")) {
                // intent = new Intent(context.getApplicationContext(), LivePlantDisplayRetailCounterActivityOnline.class);
                intent = new Intent(context.getApplicationContext(), LivePlantDisplayRetailCounterActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), PurchaseListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("2")) {

                intent = new Intent(context.getApplicationContext(), DemoModelVisit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), RetailerToVisitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("7")) {
                intent = new Intent(context.getApplicationContext(), LivePlantDisplayVillageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), FieldDayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("9")) {
                intent = new Intent(context.getApplicationContext(), HarvestDayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("3")) {

                //intent = new Intent(context.getApplicationContext(), CropShowActivityOnline.class);
                intent = new Intent(context.getApplicationContext(), CropShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {

                intent = new Intent(context.getApplicationContext(), Innovation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("10")) {
                intent = new Intent(context.getApplicationContext(), Mahakisan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        } else {
//        FARMER/PURCHASE LIST COLLECTION
            if (screenName.equalsIgnoreCase("8")) {
                intent = new Intent(context.getApplicationContext(), LivePlantDisplayRetailCounterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), PurchaseListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("2")) {

                intent = new Intent(context.getApplicationContext(), DemoModelVisit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), RetailerToVisitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("7")) {
                intent = new Intent(context.getApplicationContext(), LivePlantDisplayVillageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), FieldDayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("9")) {
                intent = new Intent(context.getApplicationContext(), HarvestDayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("3")) {

                intent = new Intent(context.getApplicationContext(), CropShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {

                intent = new Intent(context.getApplicationContext(), Innovation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("10")) {
                intent = new Intent(context.getApplicationContext(), Mahakisan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * <P>Method to naviagte ATL season screen to respective activity</P>
     *
     * @param screenName
     */
    private void navigateToATLScreen(String screenName) {

        if (screenName.equalsIgnoreCase("1")) {

            //   Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();

            intent = new Intent(context.getApplicationContext(), ATLPosteringActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if (screenName.equalsIgnoreCase("2")) {
            intent = new Intent(context.getApplicationContext(), FieldBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if (screenName.equalsIgnoreCase("3")) {
            intent = new Intent(context.getApplicationContext(), FieldBannerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if (screenName.equalsIgnoreCase("4")) {

            //Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
            intent = new Intent(context.getApplicationContext(), ATLWallPaintingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if (screenName.equalsIgnoreCase("5")) {

            intent = new Intent(context.getApplicationContext(), TrolleyPaintingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if (screenName.equalsIgnoreCase("7")) {
            intent = new Intent(context.getApplicationContext(), ATLExhibitionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else if (screenName.equalsIgnoreCase("8")) {
            intent = new Intent(context.getApplicationContext(), MarketDayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <P>Method to naviagte pre season screen to respective activity</P>
     *
     * @param screenName
     */
    private void navigateToPreSeasonScreen(String screenName) {
        // RBM 2 ,BU Head 9 ,NMM 8,ZBM 7
        //  Toast.makeText(this, ""+screenName, Toast.LENGTH_SHORT).show();
        if (pref.getString("RoleID", null).equals("2")
                || pref.getString("RoleID", null).equals("9")
                || pref.getString("RoleID", null).equals("7")
                || pref.getString("RoleID", null).equals("5")
                || pref.getString("RoleID", null).equals("8")) {
            if (screenName.equalsIgnoreCase("1")) {
                // intent = new Intent(context.getApplicationContext(), TestimonialCollectionActivityOnline.class);
                intent = new Intent(context.getApplicationContext(), TestimonialCollectionActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                intent = new Intent(context.getApplicationContext(), SanmanMelaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                // intent = new Intent(context.getApplicationContext(), VillageMeetingActivityOnline.class);

                intent = new Intent(context.getApplicationContext(), VillageMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), PromotionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {

                // Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                intent = new Intent(context.getApplicationContext(), JeepCampaigningActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), CropSeminarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("7")) {

                //    Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                intent = new Intent(context.getApplicationContext(), POPDisplayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("8")) {

                //    Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                //        intent = new Intent(context.getApplicationContext(), POPDisplayActivity.class);
                intent = new Intent(context.getApplicationContext(), ProjectorMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), TestimonialCollectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                intent = new Intent(context.getApplicationContext(), SanmanMelaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                intent = new Intent(context.getApplicationContext(), VillageMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), PromotionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {

                // Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                intent = new Intent(context.getApplicationContext(), JeepCampaigningActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), CropSeminarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("7")) {

                //    Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                intent = new Intent(context.getApplicationContext(), POPDisplayActivity.class);
                //   intent = new Intent(context.getApplicationContext(), ProjectorMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("8")) {

                //    Toast.makeText(getApplicationContext(), "Module under development", Toast.LENGTH_LONG).show();
                //        intent = new Intent(context.getApplicationContext(), POPDisplayActivity.class);
                intent = new Intent(context.getApplicationContext(), ProjectorMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * <P>Method to naviagte general season screen to respective activity</P>
     *
     * @param screenName
     */
    private void navigateToGeneralScreen(String screenName) {

        if (pref.getString("RoleID", null).equals("2")
                || pref.getString("RoleID", null).equals("9")
                || pref.getString("RoleID", null).equals("7")
                || pref.getString("RoleID", null).equals("5")
                || pref.getString("RoleID", null).equals("8")) {
            if (screenName.equalsIgnoreCase("1")) {
                //intent = new Intent(context.getApplicationContext(), DistributerVisitsActivityOnline.class);
                intent = new Intent(context.getApplicationContext(), DistributerVisitsActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                //  intent = new Intent(context.getApplicationContext(), RetailerVisitsActivityOnline.class);
                intent = new Intent(context.getApplicationContext(), RetailerVisitsActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                intent = new Intent(context.getApplicationContext(), FarmerVisitsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                //intent = new Intent(context.getApplicationContext(), SamruddhaKisanVisitsActivity.class);
                intent = new Intent(context.getApplicationContext(), RetailerandDistributorTag.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), AddressComplaintActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), ReviewMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (screenName.equalsIgnoreCase("1")) {
                intent = new Intent(context.getApplicationContext(), DistributerVisitsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("2")) {
                intent = new Intent(context.getApplicationContext(), RetailerVisitsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else if (screenName.equalsIgnoreCase("3")) {
                intent = new Intent(context.getApplicationContext(), FarmerVisitsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("4")) {
                intent = new Intent(context.getApplicationContext(), RetailerandDistributorTag.class);

                // intent = new Intent(context.getApplicationContext(), SamruddhaKisanVisitsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("5")) {
                intent = new Intent(context.getApplicationContext(), AddressComplaintActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else if (screenName.equalsIgnoreCase("6")) {
                intent = new Intent(context.getApplicationContext(), ReviewMeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Screen not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
