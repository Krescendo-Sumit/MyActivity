package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.icu.util.Calendar;
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
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class ActivityBeSurveyVCF extends AppCompatActivity {

    Context context;
    private long mLastClickTime = 0;
    private static final String TAG = "ActivityBeSurveyVCF";
    public SqliteDatabase mDatabase;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Config config;
    ScrollView container;
    String userCode;
    public Messageclass msclass;
    Button btnSubmit, btnSubmitRetailer ;
    TextInputLayout txtLyName, txtLyNumber,txtLyRetailerName,txtLyRetailerNumber;
    RadioGroup radGroupActivity;
    RadioButton radDistributorActivity;
    RadioButton radRetailerActivity;
    LinearLayout llDistributors, llRetailers;
    EditText etName, etMobileNumber, etRetailerName, etMobileNumberRetailer;

    TextView txtDistQues1, txtDistQues2, txtDistQues3, txtDistQues4, txtDistQues5, txtDistQues6, txtDistQues7, txtDistQues8, txtDistQues9, txtDistQues10
            , txtDistQues11, txtDistQues12, txtDistQues13, txtDistQues14, txtDistQues15, txtDistQues16, txtDistQues17, txtDistQues18
            , txtDistQues19, txtDistQues20, txtDistQues21, txtDistQues22, txtDistQues23, txtDistQues24, txtDistQues25;

    TextView  txtReQues1, txtReQues2, txtReQues3, txtReQues4, txtReQues5, txtReQues6, txtReQues7, txtReQues8, txtReQues9, txtReQues10
            , txtReQues11, txtReQues12, txtReQues13, txtReQues14, txtReQues15, txtReQues16, txtReQues17, txtReQues18
            , txtReQues19, txtReQues20, txtReQues21, txtReQues22, txtReQues23, txtReQues24, txtReQues25,txtReQues26,txtReQues27;


    //for distributor
    SearchableSpinner spState, spDist, spTaluka, spVillage, spHowLong, spProductPerformance, spQtySatisfactory,
            spProductRange, spComparedQty, spRatePrice, spOrderProcess, spReceivedOrderTime, spPromoAct,
            spInvite, spResponsive, spOpinionCustService, spReturnPolicy, spResolveComplaint, spPromptResponse,
            spIncentiveScheme, spCompletionTime, spSalesStaff, spInfoProvided, spPromiseHonoured, spManPowerNo,
            spComplaintTime, spRateService, spTransprency, spRestimeAccount;

    String sellingProduct, productPerformance, qtySatisfactory, productRange, comparedQty, ratePrice, orderProcess,
            receivedOrderTime, promoAct, invite, responsive, opinionCustService,
            returnPolicy, resolveComplaint, promptResponse, incentiveScheme, completionTime,
            salesStaff, infoProvided, promiseHonoured, manPowerNo, complaintTime, rateService, transprency, restimeAccount;

    //for retailer
    SearchableSpinner spHowLong1, spProductPerformance1, spQtySatisfactory1, spProductRange1, spComparedQty1,
            spQuality, spRequirement, spRateMahycoPro, spStaffVisit, spInfoProvided1, spStaffKeenness, spSalesPersonVisit,
            spOpinionCustService1, spReturnPolicy1, spSalesStaff1, spResolveComplaint1, spPromptResponse1, spPromiseHonoured1,
            spComplaintTime1, spRateService1, spInvitedForField, spPromoAct1, spInfoProvided2, spIncentiveScheme1,
            spSalesConversion, spLoyalityProgram, spBstLoyalityProgram;

    String sellingProduct1, productPerformance1, qtySatisfactory1, productRange1, comparedQty1, quality, requirement, rateMahycoPro, staffVisit, infoProvided1, staffKeenness, salesPersonVisit,
            opinionCustService1, returnPolicy1, salesStaff1, resolveComplaint1, promptResponse1, promiseHonoured1, complaintTime1, rateService1,
            invitedForField, promoAct1, infoProvided2, incentiveScheme1, salesConversion, loyalityProgram, bstLoyalityProgram;

    private Handler handler = new Handler();
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    Prefs mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_survey_vcf);

        initUI();
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mPref = Prefs.with(this);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
        container = (ScrollView) findViewById(R.id.container);

        radGroupActivity = (RadioGroup) findViewById(R.id.radGroupActivity);
        radDistributorActivity = (RadioButton) findViewById(R.id.radDistributorActivity);
        radRetailerActivity = (RadioButton) findViewById(R.id.radRetailerActivity);
        llRetailers = (LinearLayout) findViewById(R.id.llRetailers);
        llDistributors = (LinearLayout) findViewById(R.id.llDistributors);
        etName = (EditText) findViewById(R.id.etDistributorName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumberdistributor);
        etRetailerName = (EditText) findViewById(R.id.etRetailerName);
        etMobileNumberRetailer = (EditText) findViewById(R.id.etMobileNumberRetailer);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmitRetailer = (Button) findViewById(R.id.btnSubmitRetailer);
        txtLyName = (TextInputLayout) findViewById(R.id.txtLyName);
        txtLyNumber = (TextInputLayout) findViewById(R.id.txtLyNumber);
        txtLyRetailerName = (TextInputLayout) findViewById(R.id.txtLyRetailerName);
        txtLyRetailerNumber = (TextInputLayout) findViewById(R.id.txtLyRetailerNumber);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);


        manageQuesTxtId();

        manageRadioGroup();

        onBtnDistributorSubmitClicked();

        //For distributor id's
        initDistributorViewbyId();

        //For retailer id's
        initRetailerViewbyId();

        // for distributor spinner bindings
        bindSpHowLongSelling();
        bindSpProductPerformance();
        bindspQtySatisfactory();
        bindSpProductRange();
        bindSpComparedQty();
        bindSpRatePrice();
        bindSpOrderProcess();
        bindSpReceivedOrderTime();
        bindSpPromoAct();
        bindSpInvite();
        bindSpResponsive();
        bindSpOpinionCustService();
        bindSpReturnPolicy();
        bindSpResolveComplaint();
        bindSpPromptResponse();
        bindSpIncentiveScheme();
        bindSpCompletionTime();
        bindSpSalesStaff();
        bindSpInfoProvided();
        bindSpPromiseHonoured();
        bindSpManPowerNo();
        bindSpComplaintTime();
        bindSpRateService();
        bindSpTransprency();
        bindSpRestimeAccount();

        //for retailer spinner bindings
        bindspQuality();
        bindspRequirement();
        bindspRateMahycoPro();
        bindspStaffVisit();
        bindspStaffKeenness();
        bindspSalesPersonVisit();
        bindspInvitedForField();
        bindspInfoProvided2();
        bindspSalesConversion();
        bindspLoyalityProgram();
        bindspBstLoyalityProgram();


        /// for distributors item selected
        onDistributorItemSelected();


        // for Retailer Item selected
        onRetailerItemSelected();

    }

    private void onRetailerItemSelected() {
        spHowLong1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    sellingProduct1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues1);
                    Log.d("string", sellingProduct1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProductPerformance1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    productPerformance1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues2);
                    Log.d("string", productPerformance1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spQtySatisfactory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    qtySatisfactory1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues3);
                    Log.d("string", qtySatisfactory1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProductRange1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    productRange1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues4);
                    Log.d("string", productRange1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spComparedQty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    comparedQty1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues5);
                    Log.d("string", comparedQty1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    quality = gm.Desc().trim();
                    setDefaultTextColor(txtReQues6);
                    Log.d("string", quality);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRequirement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    requirement = gm.Desc().trim();
                    setDefaultTextColor(txtReQues7);
                    Log.d("string", requirement);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRateMahycoPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    rateMahycoPro = gm.Desc().trim();
                    setDefaultTextColor(txtReQues8);
                    Log.d("string", rateMahycoPro);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spStaffVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    staffVisit = gm.Desc().trim();
                    setDefaultTextColor(txtReQues9);
                    Log.d("string", staffVisit);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spInfoProvided1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    infoProvided1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues10);
                    Log.d("string", infoProvided1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spStaffKeenness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    staffKeenness = gm.Desc().trim();
                    setDefaultTextColor(txtReQues11);
                    Log.d("string", staffKeenness);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSalesPersonVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    salesPersonVisit = gm.Desc().trim();
                    setDefaultTextColor(txtReQues12);
                    Log.d("string", salesPersonVisit);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOpinionCustService1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    opinionCustService1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues13);
                    Log.d("string", opinionCustService1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spReturnPolicy1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    returnPolicy1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues14);
                    Log.d("string", returnPolicy1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSalesStaff1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    salesStaff1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues15);
                    Log.d("string", salesStaff1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spResolveComplaint1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    resolveComplaint1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues16);
                    Log.d("string", resolveComplaint1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromptResponse1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promptResponse1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues17);
                    Log.d("string", promptResponse1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromiseHonoured1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promiseHonoured1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues18);
                    Log.d("string", promiseHonoured1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spComplaintTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    complaintTime1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues19);
                    Log.d("string", complaintTime1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRateService1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    rateService1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues20);
                    Log.d("string", rateService1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spInvitedForField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    invitedForField = gm.Desc().trim();
                    setDefaultTextColor(txtReQues21);
                    Log.d("string", invitedForField);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromoAct1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promoAct1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues22);
                    Log.d("string", promoAct1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spInfoProvided2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    infoProvided2 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues23);
                    Log.d("string", infoProvided2);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spIncentiveScheme1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    incentiveScheme1 = gm.Desc().trim();
                    setDefaultTextColor(txtReQues24);
                    Log.d("string", incentiveScheme1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSalesConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    salesConversion = gm.Desc().trim();
                    setDefaultTextColor(txtReQues25);
                    Log.d("string", salesConversion);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLoyalityProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    loyalityProgram = gm.Desc().trim();
                    setDefaultTextColor(txtReQues26);
                    Log.d("string", loyalityProgram);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBstLoyalityProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    bstLoyalityProgram = gm.Desc().trim();
                    setDefaultTextColor(txtReQues27);
                    Log.d("string", bstLoyalityProgram);
//                    container.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//                    container.requestFocus();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    private void onDistributorItemSelected() {

        spHowLong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    sellingProduct = gm.Desc().trim();

                    setDefaultTextColor(txtDistQues1);
                    Log.d("string", sellingProduct);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProductPerformance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    productPerformance = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues2);
                    Log.d("string", productPerformance);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spQtySatisfactory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    qtySatisfactory = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues3);
                    Log.d("string", qtySatisfactory);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProductRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    productRange = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues4);
                    Log.d("string", productRange);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spComparedQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    comparedQty = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues5);
                    Log.d("string", comparedQty);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRatePrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    ratePrice = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues6);
                    Log.d("string", ratePrice);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOrderProcess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    orderProcess = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues7);
                    Log.d("string", orderProcess);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spReceivedOrderTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    receivedOrderTime = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues8);
                    Log.d("string", receivedOrderTime);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromoAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promoAct = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues9);
                    Log.d("string", promoAct);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spInvite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    invite = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues10);
                    Log.d("string", invite);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spResponsive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    responsive = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues11);
                    Log.d("string", responsive);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOpinionCustService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    opinionCustService = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues12);
                    Log.d("string", opinionCustService);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spReturnPolicy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    returnPolicy = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues13);
                    Log.d("string", returnPolicy);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spResolveComplaint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    resolveComplaint = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues14);
                    Log.d("string", resolveComplaint);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromptResponse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promptResponse = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues15);
                    Log.d("string", promptResponse);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spIncentiveScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    incentiveScheme = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues16);
                    Log.d("string", incentiveScheme);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCompletionTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    completionTime = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues17);
                    Log.d("string", completionTime);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSalesStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    salesStaff = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues18);
                    Log.d("string", salesStaff);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spInfoProvided.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    infoProvided = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues19);
                    Log.d("string", infoProvided);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPromiseHonoured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    promiseHonoured = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues20);
                    Log.d("string", promiseHonoured);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spManPowerNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    manPowerNo = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues21);
                    Log.d("string", manPowerNo);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spComplaintTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    complaintTime = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues22);
                    Log.d("string", complaintTime);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRateService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    rateService = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues23);
                    Log.d("string", rateService);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTransprency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    transprency = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues24);
                    Log.d("string", transprency);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRestimeAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    restimeAccount = gm.Desc().trim();
                    setDefaultTextColor(txtDistQues25);
                    Log.d("string", restimeAccount);


                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * <P>Method to change the textview color to default color</P>
     * @param txtQues
     */
    private void setDefaultTextColor(TextView txtQues) {

        txtQues.setTextColor(getResources().getColor(R.color.QRCodeBlackColor));
    }

    private void manageQuesTxtId() {

        //For Distributors
        txtDistQues1 = (TextView) findViewById(R.id.txtDistQues1);
        txtDistQues2 = (TextView) findViewById(R.id.txtDistQues2);
        txtDistQues3 = (TextView) findViewById(R.id.txtDistQues3);
        txtDistQues4 = (TextView) findViewById(R.id.txtDistQues4);
        txtDistQues5 = (TextView) findViewById(R.id.txtDistQues5);
        txtDistQues6 = (TextView) findViewById(R.id.txtDistQues6);
        txtDistQues7 = (TextView) findViewById(R.id.txtDistQues7);
        txtDistQues8 = (TextView) findViewById(R.id.txtDistQues8);
        txtDistQues9 = (TextView) findViewById(R.id.txtDistQues9);
        txtDistQues10 = (TextView) findViewById(R.id.txtDistQues10);
        txtDistQues11 = (TextView) findViewById(R.id.txtDistQues11);
        txtDistQues12 = (TextView) findViewById(R.id.txtDistQues12);
        txtDistQues13 = (TextView) findViewById(R.id.txtDistQues13);
        txtDistQues14 = (TextView) findViewById(R.id.txtDistQues14);
        txtDistQues15 = (TextView) findViewById(R.id.txtDistQues15);
        txtDistQues16 = (TextView) findViewById(R.id.txtDistQues16);
        txtDistQues17 = (TextView) findViewById(R.id.txtDistQues17);
        txtDistQues18 = (TextView) findViewById(R.id.txtDistQues18);
        txtDistQues19 = (TextView) findViewById(R.id.txtDistQues19);
        txtDistQues20 = (TextView) findViewById(R.id.txtDistQues20);
        txtDistQues21 = (TextView) findViewById(R.id.txtDistQues21);
        txtDistQues22 = (TextView) findViewById(R.id.txtDistQues22);
        txtDistQues23 = (TextView) findViewById(R.id.txtDistQues23);
        txtDistQues24 = (TextView) findViewById(R.id.txtDistQues24);
        txtDistQues25 = (TextView) findViewById(R.id.txtDistQues25);

        //For retailers
        txtReQues1 = (TextView) findViewById(R.id.txtReQues1);
        txtReQues2 = (TextView) findViewById(R.id.txtReQues2);
        txtReQues3 = (TextView) findViewById(R.id.txtReQues3);
        txtReQues4 = (TextView) findViewById(R.id.txtReQues4);
        txtReQues5 = (TextView) findViewById(R.id.txtReQues5);
        txtReQues6 = (TextView) findViewById(R.id.txtReQues6);
        txtReQues7 = (TextView) findViewById(R.id.txtReQues7);
        txtReQues8 = (TextView) findViewById(R.id.txtReQues8);
        txtReQues9 = (TextView) findViewById(R.id.txtReQues9);
        txtReQues10 = (TextView) findViewById(R.id.txtReQues10);
        txtReQues11 = (TextView) findViewById(R.id.txtReQues11);
        txtReQues12 = (TextView) findViewById(R.id.txtReQues12);
        txtReQues13 = (TextView) findViewById(R.id.txtReQues13);
        txtReQues14 = (TextView) findViewById(R.id.txtReQues14);
        txtReQues15 = (TextView) findViewById(R.id.txtReQues15);
        txtReQues16 = (TextView) findViewById(R.id.txtReQues16);
        txtReQues17 = (TextView) findViewById(R.id.txtReQues17);
        txtReQues18 = (TextView) findViewById(R.id.txtReQues18);
        txtReQues19 = (TextView) findViewById(R.id.txtReQues19);
        txtReQues20 = (TextView) findViewById(R.id.txtReQues20);
        txtReQues21 = (TextView) findViewById(R.id.txtReQues21);
        txtReQues22 = (TextView) findViewById(R.id.txtReQues22);
        txtReQues23 = (TextView) findViewById(R.id.txtReQues23);
        txtReQues24 = (TextView) findViewById(R.id.txtReQues24);
        txtReQues25 = (TextView) findViewById(R.id.txtReQues25);
        txtReQues26 = (TextView) findViewById(R.id.txtReQues26);
        txtReQues27 = (TextView) findViewById(R.id.txtReQues27);
    }

    private void bindspBstLoyalityProgram() {
        try {
            spBstLoyalityProgram.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Monsanto"));
            gm.add(new GeneralMaster("2", "Bayer crop science"));
            gm.add(new GeneralMaster("3", "Pioneer seeds"));
            gm.add(new GeneralMaster("4", "Rasi seeds"));
            gm.add(new GeneralMaster("5", "Others"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spBstLoyalityProgram.setAdapter(adapter);
            bstLoyalityProgram = spBstLoyalityProgram.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspLoyalityProgram() {
        try {
            spLoyalityProgram.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spLoyalityProgram.setAdapter(adapter);
            loyalityProgram = spLoyalityProgram.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspSalesConversion() {
        try {
            spSalesConversion.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Very poor"));
            gm.add(new GeneralMaster("2", "poor"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Good"));
            gm.add(new GeneralMaster("5", "Very Good"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSalesConversion.setAdapter(adapter);
            salesConversion = spSalesConversion.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspInfoProvided2() {
        try {
            spInfoProvided2.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spInfoProvided2.setAdapter(adapter);
            infoProvided2 = spInfoProvided2.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspInvitedForField() {
        try {
            spInvitedForField.setAdapter(null);
            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType4(adapter);

            spInvitedForField.setAdapter(adapter);
            invitedForField = spInvitedForField.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspSalesPersonVisit() {
        try {
            spSalesPersonVisit.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Not at all"));
            gm.add(new GeneralMaster("2", "Less Fequently"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Frequently"));
            gm.add(new GeneralMaster("5", "Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSalesPersonVisit.setAdapter(adapter);
            salesPersonVisit = spSalesPersonVisit.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspStaffKeenness() {
        try {
            spStaffKeenness.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Not Keen"));
            gm.add(new GeneralMaster("2", "Somewhat keen"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Keen"));
            gm.add(new GeneralMaster("5", "Very keen"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStaffKeenness.setAdapter(adapter);
            staffKeenness = spStaffKeenness.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void bindspStaffVisit() {
        try {
            spStaffVisit.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Not at all"));
            gm.add(new GeneralMaster("2", "Occasionally"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Sometimes"));
            gm.add(new GeneralMaster("5", "Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStaffVisit.setAdapter(adapter);
            staffVisit = spStaffVisit.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void bindspRateMahycoPro() {
        try {
            spRateMahycoPro.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType3(adapter);

            spRateMahycoPro.setAdapter(adapter);
            rateMahycoPro = spRateMahycoPro.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspRequirement() {
        try {
            spRequirement.setAdapter(null);

            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Badly"));
            gm.add(new GeneralMaster("2", "Fine"));
            gm.add(new GeneralMaster("3", "Neutral"));
            gm.add(new GeneralMaster("4", "Well"));
            gm.add(new GeneralMaster("5", "Very Well"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRequirement.setAdapter(adapter);
            requirement = spRequirement.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindspQuality() {
        try {
            spQuality.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spQuality.setAdapter(adapter);
            quality = spQuality.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initRetailerViewbyId() {

        spHowLong1 = (SearchableSpinner) findViewById(R.id.spHowLong1);
        spProductPerformance1 = (SearchableSpinner) findViewById(R.id.spProductPerformance1);
        spQtySatisfactory1 = (SearchableSpinner) findViewById(R.id.spQtySatisfactory1);
        spProductRange1 = (SearchableSpinner) findViewById(R.id.spProductRange1);
        spComparedQty1 = (SearchableSpinner) findViewById(R.id.spComparedQty1);
        spQuality = (SearchableSpinner) findViewById(R.id.spQuality);
        spRequirement = (SearchableSpinner) findViewById(R.id.spRequirement);
        spRateMahycoPro = (SearchableSpinner) findViewById(R.id.spRateMahycoPro);
        spStaffVisit = (SearchableSpinner) findViewById(R.id.spStaffVisit);
        spInfoProvided1 = (SearchableSpinner) findViewById(R.id.spInfoProvided1);
        spStaffKeenness = (SearchableSpinner) findViewById(R.id.spStaffKeenness);
        spSalesPersonVisit = (SearchableSpinner) findViewById(R.id.spSalesPersonVisit);
        spOpinionCustService1 = (SearchableSpinner) findViewById(R.id.spOpinionCustService1);
        spReturnPolicy1 = (SearchableSpinner) findViewById(R.id.spReturnPolicy1);
        spSalesStaff1 = (SearchableSpinner) findViewById(R.id.spSalesStaff1);
        spResolveComplaint1 = (SearchableSpinner) findViewById(R.id.spResolveComplaint1);
        spPromptResponse1 = (SearchableSpinner) findViewById(R.id.spPromptResponse1);
        spPromiseHonoured1 = (SearchableSpinner) findViewById(R.id.spPromiseHonoured1);
        spComplaintTime1 = (SearchableSpinner) findViewById(R.id.spComplaintTime1);
        spRateService1 = (SearchableSpinner) findViewById(R.id.spRateService1);
        spInvitedForField = (SearchableSpinner) findViewById(R.id.spInvitedForField);
        spPromoAct1 = (SearchableSpinner) findViewById(R.id.spPromoAct1);
        spInfoProvided2 = (SearchableSpinner) findViewById(R.id.spInfoProvided2);
        spIncentiveScheme1 = (SearchableSpinner) findViewById(R.id.spIncentiveScheme1);
        spSalesConversion = (SearchableSpinner) findViewById(R.id.spSalesConversion);
        spLoyalityProgram = (SearchableSpinner) findViewById(R.id.spLoyalityProgram);
        spBstLoyalityProgram = (SearchableSpinner) findViewById(R.id.spBstLoyalityProgram);
    }


    private void initDistributorViewbyId() {
        spHowLong = (SearchableSpinner) findViewById(R.id.spHowLong);
        spProductPerformance = (SearchableSpinner) findViewById(R.id.spProductPerformance);
        spQtySatisfactory = (SearchableSpinner) findViewById(R.id.spQtySatisfactory);
        spProductRange = (SearchableSpinner) findViewById(R.id.spProductRange);
        spComparedQty = (SearchableSpinner) findViewById(R.id.spComparedQty);
        spRatePrice = (SearchableSpinner) findViewById(R.id.spRatePrice);
        spReceivedOrderTime = (SearchableSpinner) findViewById(R.id.spReceivedOrderTime);
        spOrderProcess = (SearchableSpinner) findViewById(R.id.spOrderProcess);
        spPromoAct = (SearchableSpinner) findViewById(R.id.spPromoAct);
        spInvite = (SearchableSpinner) findViewById(R.id.spInvite);
        spResponsive = (SearchableSpinner) findViewById(R.id.spResponsive);
        spOpinionCustService = (SearchableSpinner) findViewById(R.id.spOpinionCustService);
        spReturnPolicy = (SearchableSpinner) findViewById(R.id.spReturnPolicy);
        spResolveComplaint = (SearchableSpinner) findViewById(R.id.spResolveComplaint);
        spPromptResponse = (SearchableSpinner) findViewById(R.id.spPromptResponse);
        spIncentiveScheme = (SearchableSpinner) findViewById(R.id.spIncentiveScheme);
        spCompletionTime = (SearchableSpinner) findViewById(R.id.spCompletionTime);
        spSalesStaff = (SearchableSpinner) findViewById(R.id.spSalesStaff);
        spInfoProvided = (SearchableSpinner) findViewById(R.id.spInfoProvided);
        spPromiseHonoured = (SearchableSpinner) findViewById(R.id.spPromiseHonoured);
        spManPowerNo = (SearchableSpinner) findViewById(R.id.spManPowerNo);
        spComplaintTime = (SearchableSpinner) findViewById(R.id.spComplaintTime);
        spRateService = (SearchableSpinner) findViewById(R.id.spRateService);
        spTransprency = (SearchableSpinner) findViewById(R.id.spTransprency);
        spRestimeAccount = (SearchableSpinner) findViewById(R.id.spRestimeAccount);

    }


    private void bindSpRestimeAccount() {
        try {
            spRestimeAccount.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType7(adapter);

            spRestimeAccount.setAdapter(adapter);
            restimeAccount = spRestimeAccount.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpTransprency() {

        try {
            spTransprency.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType11(adapter);

            spTransprency.setAdapter(adapter);
            transprency = spTransprency.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayAdapter<GeneralMaster> getRateListType11(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "always Ambiguous"));
        gm.add(new GeneralMaster("2", "Ambiguous"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Transparent"));
        gm.add(new GeneralMaster("5", "Highly Transparant"));

        adapter = new ArrayAdapter<GeneralMaster>(this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void bindSpRateService() {
        try {
            spRateService.setAdapter(null);
            spRateService1.setAdapter(null);
            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType3(adapter);

            spRateService.setAdapter(adapter);
            rateService = spRateService.getSelectedItem().toString();

            spRateService1.setAdapter(adapter);
            rateService1 = spRateService1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpComplaintTime() {
        try {
            spComplaintTime.setAdapter(null);
            spComplaintTime1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType7(adapter);

            spComplaintTime.setAdapter(adapter);
            complaintTime = spComplaintTime.getSelectedItem().toString();

            spComplaintTime1.setAdapter(adapter);
            complaintTime1 = spComplaintTime1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpManPowerNo() {
        try {
            spManPowerNo.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType10(adapter);

            spManPowerNo.setAdapter(adapter);
            manPowerNo = spManPowerNo.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayAdapter<GeneralMaster> getRateListType10(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Happy"));
        gm.add(new GeneralMaster("2", "Happy"));
        gm.add(new GeneralMaster("3", "No Comments"));
        gm.add(new GeneralMaster("4", "Sad"));
        gm.add(new GeneralMaster("5", "Very Sad"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void bindSpPromiseHonoured() {
        try {
            spPromiseHonoured.setAdapter(null);
            spPromiseHonoured1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType6(adapter);

            spPromiseHonoured.setAdapter(adapter);
            promiseHonoured = spPromiseHonoured.getSelectedItem().toString();

            spPromiseHonoured1.setAdapter(adapter);
            promiseHonoured1 = spPromiseHonoured1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpInfoProvided() {

        try {
            spInfoProvided.setAdapter(null);
            spInfoProvided1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType9(adapter);

            spInfoProvided.setAdapter(adapter);
            infoProvided = spInfoProvided.getSelectedItem().toString();

            spInfoProvided1.setAdapter(adapter);
            infoProvided1 = spInfoProvided1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindSpSalesStaff() {
        try {
            spSalesStaff.setAdapter(null);
            spSalesStaff1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType8(adapter);

            spSalesStaff.setAdapter(adapter);
            salesStaff = spSalesStaff.getSelectedItem().toString();

            spSalesStaff1.setAdapter(adapter);
            salesStaff1 = spSalesStaff1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindSpCompletionTime() {
        try {
            spCompletionTime.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType7(adapter);

            spCompletionTime.setAdapter(adapter);
            completionTime = spCompletionTime.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpIncentiveScheme() {
        try {
            spIncentiveScheme.setAdapter(null);
            spIncentiveScheme1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType7(adapter);

            spIncentiveScheme.setAdapter(adapter);
            incentiveScheme = spIncentiveScheme.getSelectedItem().toString();

            spIncentiveScheme1.setAdapter(adapter);
            incentiveScheme1 = spIncentiveScheme1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindSpPromptResponse() {
        try {
            spPromptResponse.setAdapter(null);
            spPromptResponse1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType6(adapter);

            spPromptResponse.setAdapter(adapter);
            promptResponse = spPromptResponse.getSelectedItem().toString();

            spPromptResponse1.setAdapter(adapter);
            promptResponse1 = spPromptResponse1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void bindSpResolveComplaint() {

        try {
            spResolveComplaint.setAdapter(null);
            spResolveComplaint1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType5(adapter);

            spResolveComplaint.setAdapter(adapter);
            resolveComplaint = spResolveComplaint.getSelectedItem().toString();

            spResolveComplaint1.setAdapter(adapter);
            resolveComplaint1 = spResolveComplaint1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void bindSpReturnPolicy() {
        try {
            spReturnPolicy.setAdapter(null);
            spReturnPolicy1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spReturnPolicy.setAdapter(adapter);
            returnPolicy = spReturnPolicy.getSelectedItem().toString();

            spReturnPolicy1.setAdapter(adapter);
            returnPolicy1 = spReturnPolicy1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpOpinionCustService() {
        try {
            spOpinionCustService.setAdapter(null);
            spOpinionCustService1.setAdapter(null);
            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType3(adapter);

            spOpinionCustService.setAdapter(adapter);
            opinionCustService = spOpinionCustService.getSelectedItem().toString();

            spOpinionCustService1.setAdapter(adapter);
            opinionCustService1 = spOpinionCustService1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpResponsive() {
        try {
            spResponsive.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Unresponsive"));
            gm.add(new GeneralMaster("2", "Less Responsive"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Somewhat responsive"));
            gm.add(new GeneralMaster("5", "Highly Responsive"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spResponsive.setAdapter(adapter);
            responsive = spResponsive.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpInvite() {

        try {
            spInvite.setAdapter(null);
            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType4(adapter);

            spInvite.setAdapter(adapter);
            invite = spInvite.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void bindSpPromoAct() {
        try {
            spPromoAct.setAdapter(null);
            spPromoAct1.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Highly ineffective"));
            gm.add(new GeneralMaster("2", "Ineffective"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Effective"));
            gm.add(new GeneralMaster("5", "Highly Effective"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPromoAct.setAdapter(adapter);
            promoAct = spPromoAct.getSelectedItem().toString();

            spPromoAct1.setAdapter(adapter);
            promoAct1 = spPromoAct1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpReceivedOrderTime() {
        try {
            spReceivedOrderTime.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Not at all"));
            gm.add(new GeneralMaster("2", "Occasionally"));
            gm.add(new GeneralMaster("3", "Neutal"));
            gm.add(new GeneralMaster("4", "Sometimes"));
            gm.add(new GeneralMaster("5", "Always"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spReceivedOrderTime.setAdapter(adapter);
            receivedOrderTime = spReceivedOrderTime.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void bindSpOrderProcess() {

        try {
            spOrderProcess.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;
            adapter = getRateListType2(adapter);

            spOrderProcess.setAdapter(adapter);
            orderProcess = spOrderProcess.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindSpRatePrice() {

        try {
            spRatePrice.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "Low"));
            gm.add(new GeneralMaster("2", "Normal"));
            gm.add(new GeneralMaster("3", "Apt"));
            gm.add(new GeneralMaster("4", "High"));
            gm.add(new GeneralMaster("5", "Very High"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRatePrice.setAdapter(adapter);
            ratePrice = spRatePrice.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void bindSpComparedQty() {
        try {
            spComparedQty.setAdapter(null);
            spComparedQty1.setAdapter(null);
            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType3(adapter);

            spComparedQty.setAdapter(adapter);
            comparedQty = spComparedQty.getSelectedItem().toString();

            spComparedQty1.setAdapter(adapter);
            comparedQty1 = spComparedQty1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpProductRange() {
        try {
            spProductRange.setAdapter(null);
            spProductRange1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spProductRange.setAdapter(adapter);
            productRange = spProductRange.getSelectedItem().toString();

            spProductRange1.setAdapter(adapter);
            productRange1 = spProductRange1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void bindspQtySatisfactory() {
        try {
            spQtySatisfactory.setAdapter(null);
            spQtySatisfactory1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;
            adapter = getRateListType2(adapter);

            spQtySatisfactory.setAdapter(adapter);
            qtySatisfactory = spQtySatisfactory.getSelectedItem().toString();

            spQtySatisfactory1.setAdapter(adapter);
            qtySatisfactory1 = spQtySatisfactory1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpProductPerformance() {
        try {
            spProductPerformance.setAdapter(null);
            spProductPerformance1.setAdapter(null);

            ArrayAdapter<GeneralMaster> adapter = null;

            adapter = getRateListType1(adapter);

            spProductPerformance.setAdapter(adapter);
            productPerformance = spProductPerformance.getSelectedItem().toString();

            spProductPerformance1.setAdapter(adapter);
            productPerformance1 = spProductPerformance1.getSelectedItem().toString();


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSpHowLongSelling() {

        try {
            spHowLong.setAdapter(null);
            spHowLong1.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT"));
            gm.add(new GeneralMaster("1", "New"));
            gm.add(new GeneralMaster("2", "> 1 Year"));
            gm.add(new GeneralMaster("3", "1-2 Year"));
            gm.add(new GeneralMaster("4", "3 Year"));
            gm.add(new GeneralMaster("5", "> 5years"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHowLong.setAdapter(adapter);
            sellingProduct = spHowLong.getSelectedItem().toString();

            spHowLong1.setAdapter(adapter);
            sellingProduct1 = spHowLong1.getSelectedItem().toString();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayAdapter<GeneralMaster> getRateListType1(ArrayAdapter<GeneralMaster> adapter) {

        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Unsatisfied"));
        gm.add(new GeneralMaster("2", "Unsatisfied"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Satisfied"));
        gm.add(new GeneralMaster("5", "Very Satisfied"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType2(ArrayAdapter<GeneralMaster> adapter) {

        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very unsatisfactory"));
        gm.add(new GeneralMaster("2", "Unsatisfactory"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Satisfactory"));
        gm.add(new GeneralMaster("5", "Very satisfactory"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType3(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Not very Good"));
        gm.add(new GeneralMaster("2", "Not Good"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Good"));
        gm.add(new GeneralMaster("5", "Very Good"));

        adapter = new ArrayAdapter<GeneralMaster>(this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType4(ArrayAdapter<GeneralMaster> adapter) {

        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Never Invited"));
        gm.add(new GeneralMaster("2", "May be invited"));
        gm.add(new GeneralMaster("3", "Neutal"));
        gm.add(new GeneralMaster("4", "Sometimes invited"));
        gm.add(new GeneralMaster("5", "Always Invited"));

        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType5(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Much"));
        gm.add(new GeneralMaster("2", "Much"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Little"));
        gm.add(new GeneralMaster("5", "Very Little"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType6(ArrayAdapter<GeneralMaster> adapter) {

        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Rarely"));
        gm.add(new GeneralMaster("2", "sometimes"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Mostly"));
        gm.add(new GeneralMaster("5", "Always"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType7(ArrayAdapter<GeneralMaster> adapter) {

        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Late"));
        gm.add(new GeneralMaster("2", "Late"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Timely"));
        gm.add(new GeneralMaster("5", "Before Time"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }


    private ArrayAdapter<GeneralMaster> getRateListType8(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Difficult"));
        gm.add(new GeneralMaster("2", "Somewhat Difficult"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Easy"));
        gm.add(new GeneralMaster("5", "Very Easy"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<GeneralMaster> getRateListType9(ArrayAdapter<GeneralMaster> adapter) {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0", "SELECT"));
        gm.add(new GeneralMaster("1", "Very Inaccurate"));
        gm.add(new GeneralMaster("2", "Inaccurate"));
        gm.add(new GeneralMaster("3", "Neutral"));
        gm.add(new GeneralMaster("4", "Accurate"));
        gm.add(new GeneralMaster("5", "Very Accurate"));
        adapter = new ArrayAdapter<GeneralMaster>
                (this, android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }


    private void manageRadioGroup() {
        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radDistributorActivity:

                        llRetailers.setVisibility(View.GONE);
                        llDistributors.setVisibility(View.VISIBLE);
                        radRetailerActivity.setChecked(false);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
                        params.addRule(RelativeLayout.BELOW, R.id.llDistributors);
                        onBtnDistributorSubmitClicked();
                        break;

                    case R.id.radRetailerActivity:
                        radDistributorActivity.setChecked(false);
                        llRetailers.setVisibility(View.VISIBLE);
                        llDistributors.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
                        params1.addRule(RelativeLayout.BELOW, R.id.llRetailers);
                        onBtnRetailerSubmitClicked();
                        break;
                }
            }
        });
    }

    /**
     * <P>Method is used to do distributor API related work on submit button clicked</P>
     */
    private void onBtnDistributorSubmitClicked() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (config.NetworkConnection()) {

                   if (validation()) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Are you sure to submit distributor data");

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
                                doDistributorWork();
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

                    }else {
                        msclass.showMessage("All distributors fields are mandatory");
                    }
                } else {

                    Utility.showAlertDialog("Mahyco", "Please Check for Internet", context);
                   progressBarVisibility();

                }
            }
        });
    }


    /**
     * <P>Method is used to do retailer API related work on submit button clicked</P>
     */
    private void onBtnRetailerSubmitClicked() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (config.NetworkConnection()) {
                   if (validationRetailer()) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Are you sure to submit retailer data");

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
                                doRetailerWork();
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
                    }else {
                        msclass.showMessage("All retailer fields are mandatory");
                    }
                } else {

                    Utility.showAlertDialog("Mahyco", "Please Check for Internet", context);
                   progressBarVisibility();

                }
            }
        });
    }


    private void doDistributorWork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {

                            uploadBeSurveyDistributor();
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


    /**
     * <P>Method to get the current date time</P>
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            today = Calendar.getInstance().getTime();
        }
        return dateFormat.format(today);
    }


    /**
     * <P>Method to create the json request for distributor params</P>
     */
    private void uploadBeSurveyDistributor() {
        JSONObject requestParams = new JSONObject();

        JSONObject surveyRequest = new JSONObject();

        try {

            surveyRequest.put("id", "0");
            surveyRequest.put("surveyType", radDistributorActivity.getText());
            surveyRequest.put("distributorName", etName.getText());
            surveyRequest.put("mobileNumber", etMobileNumber.getText());
            surveyRequest.put("sellingMahycoProduct", sellingProduct);
            surveyRequest.put("productPerformance", productPerformance);
            surveyRequest.put("qtySatisfactory", qtySatisfactory);
            surveyRequest.put("productRange", productRange);
            surveyRequest.put("productQty", comparedQty);
            surveyRequest.put("ratePricing", ratePrice);
            surveyRequest.put("orderProcess", orderProcess);
            surveyRequest.put("receivedOrderOnTime", receivedOrderTime);
            surveyRequest.put("promotionalActivity", promoAct);
            surveyRequest.put("inviteForFarmerActivity", invite);
            surveyRequest.put("responsiveInDealing", responsive);
            surveyRequest.put("customerService", opinionCustService);
            surveyRequest.put("salesReturnPolicy", returnPolicy);
            surveyRequest.put("effortsToResolveComplaint", resolveComplaint);
            surveyRequest.put("promptResponse", promptResponse);
            surveyRequest.put("incentiveSchemeComm", incentiveScheme);
            surveyRequest.put("returnProcessCompleting", completionTime);
            surveyRequest.put("reachingToSalesStaff", salesStaff);
            surveyRequest.put("informationProvided", infoProvided);
            surveyRequest.put("promiseHonoured", promiseHonoured);
            surveyRequest.put("happyWithManPowerNo", manPowerNo);
            surveyRequest.put("resolvingComplaintTime", complaintTime);
            surveyRequest.put("overallServiceRate", rateService);
            surveyRequest.put("schemeTransparency", transprency);
            surveyRequest.put("responsetimeToAccountIssue", restimeAccount);
           surveyRequest.put("entryDt",   getCurrentDate() );

            requestParams.put("Table", surveyRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("RequestDistributor", requestParams.toString());

         new BeSurveyDistributorApiCall("SurveyData", requestParams).execute();
    }


    private void doRetailerWork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        uploadBeSurveyRetailer();
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


    /**
     * <P>Method to create the json request for retailer params</P>
     */
    private void uploadBeSurveyRetailer() {
        JSONObject requestParams = new JSONObject();

        JSONObject surveyRequest = new JSONObject();

        try {

            surveyRequest.put("id", "0");
            surveyRequest.put("surveyType", radRetailerActivity.getText());
            surveyRequest.put("retailerName", etRetailerName.getText());
            surveyRequest.put("mobileNumber", etMobileNumberRetailer.getText());
            surveyRequest.put("sellingMahycoProduct", sellingProduct1);
            surveyRequest.put("productPerformance", productPerformance1);
            surveyRequest.put("qtySatisfactory", qtySatisfactory1);
            surveyRequest.put("productRange", productRange1);
            surveyRequest.put("productQty", comparedQty1);
            surveyRequest.put("quality", quality);
            surveyRequest.put("geographyRequirement", requirement);
            surveyRequest.put("rateMahycoProduct", rateMahycoPro);
            surveyRequest.put("staffVisitForPromotion", staffVisit);
            surveyRequest.put("informationProvided", infoProvided1);
            surveyRequest.put("staffKeenness", staffKeenness);
            surveyRequest.put("salesPersonVisit", salesPersonVisit);
            surveyRequest.put("customerService", opinionCustService1);
            surveyRequest.put("salesReturnPolicy", returnPolicy1);
            surveyRequest.put("reachingToSalesStaff", salesStaff1);
            surveyRequest.put("effortsToResolveComplaint", resolveComplaint1);
            surveyRequest.put("promptResponse", promptResponse1);
            surveyRequest.put("promiseHonoured", promiseHonoured1);
            surveyRequest.put("resolvingComplaintTime", complaintTime1);
            surveyRequest.put("overallServiceRate", rateService1);
            surveyRequest.put("invitedForFieldDay", invitedForField);
            surveyRequest.put("promotionalActivity", promoAct1);
            surveyRequest.put("informationProvidedforProduct", infoProvided2);
            surveyRequest.put("incentiveSchemeComm", incentiveScheme1);
            surveyRequest.put("salesConversion", salesConversion);
            surveyRequest.put("loyalityProgram", loyalityProgram);
            surveyRequest.put("bestLoyalityProgram", bstLoyalityProgram);
            surveyRequest.put("entryDt", getCurrentDate());

            requestParams.put("Table", surveyRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RequestRetailer", requestParams.toString());

       new BeSurveyRetailerApiCall("SurveyData", requestParams).execute();
    }


    /**
     * <P>Method to validate the distributor detail fields</P>
     * @return
     */
    private Boolean validation() {
        boolean ret=true;
        if (radDistributorActivity.isChecked()) {

            if (etName.getText().length() == 0) {
                txtLyName.setError("Please enter the distributor name");
                ret = false;
            }else {
                txtLyName.setError("");
                ret = true;
            }

            if (etMobileNumber.getText().length() != 10) {
                txtLyNumber.setError("Please  enter Valid Mobile Number");
                ret = false;
            }else {
                txtLyNumber.setError("");
                ret = true;
            }

            if (spHowLong.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtDistQues1);
                ret = false;

            }

            if (spProductPerformance.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtDistQues2);
                ret = false;
            }

            if (spQtySatisfactory.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtDistQues3);
                ret = false;
            }
            if (spProductRange.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtDistQues4);
                ret = false;
            }
            if (spComparedQty.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtDistQues5);
                ret = false;

            }
            if (spRatePrice.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues6);
                ret = false;
            }
            if (spOrderProcess.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtDistQues7);
                ret = false;
            }
            if (spReceivedOrderTime.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtDistQues8);
                ret = false;
            }
            if (spPromoAct.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtDistQues9);
                ret = false;
            }
            if (spInvite.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtDistQues10);
                ret = false;
            }
            if (spResponsive.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues11);
                ret = false;
            }
            if (spOpinionCustService.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues12);
                ret = false;
            }
            if (spReturnPolicy.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues13);
                ret = false;
            }
            if (spResolveComplaint.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues14);
                ret = false;
            }
            if (spPromptResponse.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues15);
                ret = false;
            }

            if (spIncentiveScheme.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues16);
                ret = false;
            }
            if (spCompletionTime.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues17);
                ret = false;
            }
            if (spSalesStaff.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues18);
                ret = false;

            }
            if (spInfoProvided.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues19);
                ret = false;
            }
            if (spPromiseHonoured.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues20);
                ret = false;
            }

            if (spManPowerNo.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues21);
                ret = false;
            }
            if (spComplaintTime.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues22);
                ret = false;
            }
            if (spRateService.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues23);
                ret = false;
            }
            if (spTransprency.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues24);
                ret = false;
            }
            if (spRestimeAccount.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtDistQues25);
                ret = false;
            }

        }

       //  ret= validationRetailer();

        return ret;
    }

    /**
     * <P>Method to validate the retailer detail fields</P>
     * @return
     */
    private  Boolean validationRetailer(){
        boolean ret=true;
        if(radRetailerActivity.isChecked()){

            if (etRetailerName.getText().length() == 0) {
                txtLyRetailerName.setError("Please enter the retailer name");
                 ret=false;
            }else {
                txtLyRetailerName.setError("");
                 ret=true;
            }

            if (etMobileNumberRetailer.getText().length() != 10) {
                txtLyRetailerNumber.setError("Please  enter Valid Mobile Number");
                ret=false;
            }else {
                txtLyRetailerNumber.setError("");
                 ret=true;
            }

            if (spHowLong1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues1);
               ret=false;
            }

            if (spProductPerformance1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtReQues2);

               ret=false;
            }

            if (spQtySatisfactory1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtReQues3);

               ret=false;
            }
            if (spProductRange1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtReQues4);

               ret=false;
            }
            if (spComparedQty1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {
                setErrorTextColor(txtReQues5);

               ret=false;
            }
            if (spQuality.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues6);
               ret=false;
            }
            if (spRequirement.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtReQues7);
               ret=false;
            }
            if (spRateMahycoPro.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtReQues8);
               ret=false;
            }
            if (spStaffVisit.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtReQues9);
               ret=false;
            }
            if (spInfoProvided1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {


                setErrorTextColor(txtReQues10);
               ret=false;
            }
            if (spStaffKeenness.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues11);
               ret=false;
            }
            if (spSalesPersonVisit.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues12);
               ret=false;
            }
            if (spOpinionCustService1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues13);
               ret=false;
            }
            if (spReturnPolicy1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues14);
               ret=false;
            }
            if (spSalesStaff1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues15);
               ret=false;
            }

            if (spResolveComplaint1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues16);
               ret=false;
            }
            if (spPromptResponse1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues17);
               ret=false;
            }
            if (spPromiseHonoured1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues18);
               ret=false;
            }
            if (spComplaintTime1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues19);
               ret=false;
            }
            if (spRateService1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues20);
               ret=false;
            }

            if (spInvitedForField.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues21);
               ret=false;
            }
            if (spPromoAct1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues22);
               ret=false;
            }
            if (spInfoProvided2.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues23);
               ret=false;
            }
            if (spIncentiveScheme1.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues24);
               ret=false;
            }
            if (spSalesConversion.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues25);
               ret=false;
            }
            if (spLoyalityProgram.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues26);
               ret=false;
            }
            if (spBstLoyalityProgram.getSelectedItem().toString().equalsIgnoreCase("SELECT")) {

                setErrorTextColor(txtReQues27);
               ret=false;
            }
        }
        return ret;
    }


    /**
     * <P>Method to change the textview color when error occured</P>
     * @param txtQues
     */
    private void setErrorTextColor(TextView txtQues) {
        txtQues.setTextColor(getResources().getColor(R.color.errorColor));
    }

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class BeSurveyDistributorApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public BeSurveyDistributorApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadBeSurveyDistributorData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(ActivityBeSurveyVCF.this);
                                dialog.dismiss();
                               progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
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
     * <P>Method to upload the distributor survey data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadBeSurveyDistributorData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.DISTRIBUTOR_SURVEY_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }

    /**
     * <P> AsyncTask Class for api call to upload retailer data</P>
     */
    private class BeSurveyRetailerApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;


        public BeSurveyRetailerApiCall(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadBeSurveyRetailerData(function,obj);
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(ActivityBeSurveyVCF.this);
                                dialog.dismiss();
                               progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBeSurveyVCF.this);
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
     * <P>Methoid to upload the retailer survey data to sever</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadBeSurveyRetailerData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.RETAILER_SURVEY_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }


    /**
     * <p>Method to manage the edit text focusable event</p>
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
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
