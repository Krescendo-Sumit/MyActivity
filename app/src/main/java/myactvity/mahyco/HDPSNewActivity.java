package myactvity.mahyco;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.barcode.BarcodeCaptureActivity;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.helper.UnderLineTextView;
import myactvity.mahyco.paymentlink.requestobj.Checkout;
import myactvity.mahyco.paymentlink.requestobj.Customer;
import myactvity.mahyco.paymentlink.requestobj.HDPSRequest;
import myactvity.mahyco.paymentlink.requestobj.Notes;
import myactvity.mahyco.paymentlink.requestobj.Notify;
import myactvity.mahyco.paymentlink.requestobj.Options;
import myactvity.mahyco.paymentlink.responserazorpay.RazorPayResponse;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class HDPSNewActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback, IPickResult, View.OnClickListener {

    /*Test Data for Razorpay*/
    /*public static final String KEY_ID = "rzp_test_r5iV7i0pWqztsO";
    public static final String SECRET_KEY = "eTPy1zZOkJayqffjP9nMNKQ0";*/

    /*LIVE DATA*/
    public static final String KEY_ID = "rzp_live_IOOb8jGwOwlpbG";
    public static final String SECRET_KEY = "LzIIGL87fSBP0DhZZVcYWHFO";

    public static final String PAYMENT_URL = "https://api.razorpay.com/v1/payment_links/";
    //public static final String CALLBACK_URL = "https://wap.mahyco.com/TransactionHtmlpage.html"; /*OLD*/
    public static final String CALLBACK_URL = "https://packhouse.mahyco.com/TransactionHtmlpage.html"; /*New*/

    ProgressDialog pd;
    public TextView txtQty, toolbar_title, txtAmountDeclaration, txtCouponDeclaration;
    public SearchableSpinner spDist, spState, spVillage, spCropType,
            spProductName, spMyactvity, spComment, spTalukaDL;
    Spinner spUnits;
    Switch switchYNCoupon, switchYN;
    EditText txtVillage, etFarmername, txtAddress, txtCouponcode, txtCouponcode2,
            txtCouponcode3, txtCouponcode4, txtCouponcode5, txtCouponcode6, etMobileNo, etWhatsappNumber, txtAmountOnly;
    UnderLineTextView txtAmount;
    CardView cardScans, crdImage;
    // ProgressDialog progressDailog;
    RadioGroup radioGroup;
    LinearLayout product1, product2, product3, product4, product5, product6;
    public static final String TAG = "farmerRegistration";
    ProgressDialog dialog;
    String state, dist, taluka, imagePath, talukaDL;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spdistributor, spdivision, spsaleorg, spTaluka, spRetailerDetails;
    public Messageclass msclass;
    public CommonExecution cx;
    String division, usercode, org, cmbDistributor;
    public Button btnSubmit, btnTakePhoto, btnPOG, btnPOG2, btnPOG3, btnPOG4, btnPOG5, btnTakeFarmerPhoto;
    Config config;
    File photoFile = null;
    int imageselect;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private static final int RC_BARCODE_CAPTURE = 9001;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage, roundedImageView;
    private static final String IMAGE_DIRECTORY_NAME = "COUPONADVANCEBOOKING";
    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";
    boolean isSwitchOn = false;
    public String saleorderurl = "", croptype, units;
    LinearLayout my_linear_layout1;
    private long mLastClickTime = 0;

    String saleorg, customer, name, DLV_plant, customregroup, Distrcode, mobileno;
    TextView lbltaluka, lblQrCoupon;
    String selectedProduct="";
    String productcode;
    UnderLineTextView tvNoCouponIssued;
    String couponFromData;
    public String userCode;
    boolean isNeedToshowAlert = true;

    //GPS
    public String cordinates = "";
    public String address = "";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS = 101;
    double lati;
    double longi;

    // New verialbe Declaration     use this data to  anaysis of MDO utilisation area

    String uId, timeStamp, district, village, farmerName, mobileNumber, whatsappNumber, crop, product, quantityPacket, packetsBooked, couponsIssued, totalAmount, couponQr1, couponQr2, couponQr3, couponQr4, couponQr5, couponQr6,
            imgPath, imgStatus, isSynced, cropType, tempImagePath,
            farmerPhoto, retailerDetails, allowpacket, perCouponPkt, retailerContraint; //Added on 8th Sept 2021
    List<GeneralMaster> retailerList;
    RadioButton rdOnlinePayment, rdPaymentByCash;
    LinearLayout llEmail, layoutPrefRetailer;
    EditText edEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d_p_s_new);
        getSupportActionBar().hide(); //<< this

        context = this;
        cx = new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka = (TextView) findViewById(R.id.lbltaluka);
        lblQrCoupon = (TextView) findViewById(R.id.lblQrCoupon);
        msclass = new Messageclass(this);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spTalukaDL = (SearchableSpinner) findViewById(R.id.spTalukaDL);
        spRetailerDetails = (SearchableSpinner) findViewById(R.id.spRetailerDL);
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (SearchableSpinner) findViewById(R.id.spProductName);
        spUnits = (Spinner) findViewById(R.id.spUnits);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        roundedImageView = (ImageView) findViewById(R.id.roundedImageView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnPOG = (Button) findViewById(R.id.btnPOG);
        btnPOG2 = (Button) findViewById(R.id.btnPOG2);
        btnPOG3 = (Button) findViewById(R.id.btnPOG3);
        btnPOG4 = (Button) findViewById(R.id.btnPOG4);
        btnPOG5 = (Button) findViewById(R.id.btnPOG5);
        btnTakeFarmerPhoto = (Button) findViewById(R.id.btnTakeFarmerPhoto);
        product1 = (LinearLayout) findViewById(R.id.product1);
        product2 = (LinearLayout) findViewById(R.id.product2);
        product3 = (LinearLayout) findViewById(R.id.product3);
        product4 = (LinearLayout) findViewById(R.id.product4);
        product5 = (LinearLayout) findViewById(R.id.product5);
        product6 = (LinearLayout) findViewById(R.id.product6);

        txtCouponcode = (EditText) findViewById(R.id.txtCouponcode);
        txtCouponcode2 = (EditText) findViewById(R.id.txtCouponcode2);
        txtCouponcode3 = (EditText) findViewById(R.id.txtCouponcode3);
        txtCouponcode4 = (EditText) findViewById(R.id.txtCouponcode4);
        txtCouponcode5 = (EditText) findViewById(R.id.txtCouponcode5);
        txtCouponcode6 = (EditText) findViewById(R.id.txtCouponcode6);

        txtAmount = (UnderLineTextView) findViewById(R.id.txtAmount);
        tvNoCouponIssued = (UnderLineTextView) findViewById(R.id.tvNoCouponIssued);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtAmountDeclaration = (TextView) findViewById(R.id.txtAmountDeclaration);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        txtCouponDeclaration = (TextView) findViewById(R.id.txtCouponDeclaration);
//        progressDailog = new ProgressDialog(this);
//        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDailog.setCanceledOnTouchOutside(false
//        );
//        progressDailog.setCancelable(false);
//        progressDailog.setMessage("Data Uploading");

        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etWhatsappNumber = (EditText) findViewById(R.id.etWhatsappNumber);
        etFarmername = (EditText) findViewById(R.id.etFarmername);
        txtVillage = (EditText) findViewById(R.id.txtVillage);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        radioGroup = (RadioGroup) findViewById(R.id.radGrp);
        switchYNCoupon = (Switch) findViewById(R.id.switchYNCoupon);
        cardScans = (CardView) findViewById(R.id.cardScans);
        crdImage = (CardView) findViewById(R.id.crdImage);
        switchYN = (Switch) findViewById(R.id.switchYN);
        Utility.setRegularFont(btnSubmit, this);

        rdOnlinePayment = (RadioButton) findViewById(R.id.rd_online_payment);
        rdPaymentByCash = (RadioButton) findViewById(R.id.rd_payment_by_cash);
        llEmail = (LinearLayout) findViewById(R.id.llEmail);
        edEmail = (EditText) findViewById(R.id.edEmailID);
        layoutPrefRetailer = (LinearLayout) findViewById(R.id.layout_pref_retailer);

        bindSpinnerData("", "", "");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode = pref.getString("UserID", null);
        btnTakeFarmerPhoto.setOnClickListener((View.OnClickListener) this);

        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent i = new Intent(context, AndroidDatabaseManager.class);
                // startActivity(i);

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
                bindTalukaDL(dist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spRetailerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                Intent intent;
                try {
                    retailerDetails = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        btnPOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BarcodeCaptureActivity.class);
                //Intent intent = new Intent(context.getApplicationContext(), ScannedBarcodeActivity.class);
                intent.putExtra(BarcodeCaptureActivity.captureFrom, "1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);


            }
        });
        btnPOG2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.captureFrom, "2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);


            }
        });
        btnPOG3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.captureFrom, "3");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);


            }
        });
        btnPOG4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.captureFrom, "4");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);


            }
        });
        btnPOG5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.captureFrom, "5");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);


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

                bindVillage(taluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spTalukaDL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    talukaDL = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bindRetailer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();

                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                bindProductName(spProductName, croptype);
                bindSpinnerData(croptype, "", "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GeneralMaster gm = (GeneralMaster) adapterView.getSelectedItem();
                try {
                    productcode = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    selectedProduct = spProductName.getSelectedItem().toString();

                    //Get spinner data

                    Cursor cursor = mDatabase.getProductData(productcode, selectedProduct);// database.rawQuery(searchQuery, null);

                    int count = cursor.getCount();

                    if (count > 0) {
                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {
                            allowpacket = cursor.getString(0);
                            perCouponPkt = cursor.getString(1);
                            retailerContraint = cursor.getString(2);
                            cursor.moveToNext();
                        }
                        cursor.close();
                    }
                    Log.d("HDPS", "allowpacket: " + allowpacket);
                    Log.d("HDPS", "perCouponPkt: " + perCouponPkt);
                    Log.d("HDPS", "retailerContraint: " + retailerContraint);
                    //txtQty.setText(getQuantity(selectedProduct)); /*TODO need to check*/
                    String cropData = spCropType.getSelectedItem().toString();
                    Log.d("Data SP_PRODUCT", "spProductName ONCLICK DATA cropType: " + cropData);
                    Log.d("Data SP_PRODUCT", "spProductName ONCLICK DATA ITEM ID: " + spProductName.getSelectedItemId() + " Product: " + spProductName.getSelectedItem() + " DESC : " + gm.Desc() + " CODE : " + gm.Code());

                    Log.d("DATA", "GM DATA : " + gm.Code() + " " + gm.Desc());

                    String retailerConstraint = "0";
                    int valRC = Integer.parseInt(retailerConstraint);
                    if (valRC == 0) {
                        //No constraints so hide
                        layoutPrefRetailer.setVisibility(View.GONE);
                    } else {
                        //Retailer Constraints so show layout
                        layoutPrefRetailer.setVisibility(View.VISIBLE);
                    }

                    //Commented on 8th Sept 2021
                    /*if (gm.Code().equalsIgnoreCase("115000191") || gm.Code().equalsIgnoreCase("115000190")){
                        layoutPrefRetailer.setVisibility(View.GONE);
                    }
                    else{ //Visible
                        layoutPrefRetailer.setVisibility(View.VISIBLE);
                    }*/

                    if(!selectedProduct.equalsIgnoreCase("SELECT PRODUCT")) {
                        if (allowpacket == null || allowpacket.equalsIgnoreCase("null")) {
                            //Toast.makeText(HDPSNewActivity.this,"Please DOWNLOAD HDPS COUPON MASTER",Toast.LENGTH_LONG).show();
                            msclass.showMessage("Please DOWNLOAD HDPS COUPON MASTER");
                        }
                        else{
                            bindSpinnerData(cropData, allowpacket, perCouponPkt);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                GeneralMaster gm = (GeneralMaster) adapterView.getSelectedItem();
                try {
                    units = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  Toast.makeText(AdvanceBookingCouponActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }


                if (units.toLowerCase().trim().equals("1")) {
//                    lblQrCoupon.setVisibility(View.VISIBLE);

                    txtCouponcode2.setText("");
                    txtCouponcode3.setText("");
                    txtCouponcode4.setText("");
                    txtCouponcode5.setText("");
                    txtCouponcode6.setText("");

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.GONE);
                    product3.setVisibility(View.GONE);
                    product4.setVisibility(View.GONE);
                    product5.setVisibility(View.GONE);
                    product6.setVisibility(View.GONE);
                } else if (units.toLowerCase().trim().equals("2")) {


                    txtCouponcode3.setText("");
                    txtCouponcode4.setText("");
                    txtCouponcode5.setText("");
                    txtCouponcode6.setText("");

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.VISIBLE);
                    product3.setVisibility(View.GONE);
                    product4.setVisibility(View.GONE);
                    product5.setVisibility(View.GONE);
                    product6.setVisibility(View.GONE);


                } else if (units.toLowerCase().trim().equals("3")) {


                    txtCouponcode4.setText("");
                    txtCouponcode5.setText("");
                    txtCouponcode6.setText("");

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.VISIBLE);
                    product3.setVisibility(View.VISIBLE);
                    product4.setVisibility(View.GONE);
                    product5.setVisibility(View.GONE);
                    product6.setVisibility(View.GONE);

                } else if (units.toLowerCase().trim().equals("4")) {

                    txtCouponcode5.setText("");
                    txtCouponcode6.setText("");

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.VISIBLE);
                    product3.setVisibility(View.VISIBLE);
                    product4.setVisibility(View.VISIBLE);
                    product5.setVisibility(View.GONE);
                    product6.setVisibility(View.GONE);


                } else if (units.toLowerCase().trim().equals("5")) {

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.VISIBLE);
                    product3.setVisibility(View.VISIBLE);
                    product4.setVisibility(View.VISIBLE);
                    product5.setVisibility(View.VISIBLE);
                    product6.setVisibility(View.GONE);

                } else if (units.toLowerCase().trim().equals("6")) {

                    product1.setVisibility(View.VISIBLE);
                    product2.setVisibility(View.VISIBLE);
                    product3.setVisibility(View.VISIBLE);
                    product4.setVisibility(View.VISIBLE);
                    product5.setVisibility(View.VISIBLE);
                    product6.setVisibility(View.VISIBLE);

                } else {
                    txtCouponcode.setText("");
                    txtCouponcode2.setText("");
                    txtCouponcode3.setText("");
                    txtCouponcode4.setText("");
                    txtCouponcode5.setText("");
                    txtCouponcode6.setText("");

                    lblQrCoupon.setVisibility(View.GONE);
                    product1.setVisibility(View.GONE);
                    product1.setVisibility(View.GONE);
                    product2.setVisibility(View.GONE);
                    product3.setVisibility(View.GONE);
                    product4.setVisibility(View.GONE);
                    product5.setVisibility(View.GONE);
                    product6.setVisibility(View.GONE);


                }
                if (!units.equals("SELECT")) {
                    tvNoCouponIssued.setText(units);
                } else {

                    tvNoCouponIssued.setText("");
                }

                if (units != null && !units.isEmpty() && !units.equals("SELECT")) {
                    if (selectedProduct != null && !selectedProduct.isEmpty() && !selectedProduct.equals("SELECT PRODUCT")) {
                        int totalAmount = Integer.valueOf(units) * Integer.valueOf(getPricePerPacket(selectedProduct));

                        txtAmount.setText(String.valueOf(totalAmount) + " Rs ");
                        txtAmountDeclaration.setText("ARE YOU SURE YOU HAVE COLLECTED " + String.valueOf(totalAmount) + " RUPEES FROM THE FARMER ?");

                        txtCouponDeclaration.setText("ARE YOU SURE YOU HAVE HANDED OVER " + units + " COUPONS TO THE FARMER?");


                    } else {


                        txtAmount.setText("");

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (units != null && !units.isEmpty() && !units.equals("SELECT")) {
                    if (isChecked && Integer.valueOf(units) > 0) {

                        // Toast.makeText(context, "YES ", Toast.LENGTH_SHORT).show();
                        cardScans.setVisibility(View.VISIBLE);
                        lblQrCoupon.setVisibility(View.VISIBLE);

                    } else {
                        cardScans.setVisibility(View.GONE);
                        lblQrCoupon.setVisibility(View.GONE);
                        // Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
                        msclass.showMessage("PLEASE COLLECT CASH FIRST");


                    }
                }
            }
        });


        switchYNCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (units != null && !units.isEmpty() && !units.equals("SELECT")) {

                    if (isChecked) {
                        btnSubmit.setVisibility(View.VISIBLE);
                        // Toast.makeText(context, "YES ", Toast.LENGTH_SHORT).show();
                    } else {
                        btnSubmit.setVisibility(View.GONE);
                        //Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
                        msclass.showMessage("PLEASE HAND OVER THE COUPON FIRST");
                    }
                }
            }
        });


       /* btnTakeFarmerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    //  ActivityCompat.requestPermissions(context, new String[] {android.Manifest.permission.CAMERA}, 101);
                }
                imageselect = 1;
                // selectImage();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                       // selectImage();
                        PickImageDialog.build(new PickSetup()).show(this);

                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            captureImage();
                        } else {
                            captureImage2();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.getMessage());
                }
            }
        });  */

        //BindIntialData();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HDPSNewActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Are you sure to submit data");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // builder.setCancelable(true);

                            dialog.dismiss();
                            dialog.cancel();
                            saveToDb();

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


        bindcroptype(spCropType, "C");

        bindState();
        if (

                checkPlayServices()) {
            // startFusedLocationService();
        } else {
            msclass.showMessage("This device google play services not supported for Devices location");
        }

        //String result = callPayLinkAPI(); /*API COMMENTED FOR NOW*/
        Log.d("RAZORPAY", "PAYMENT RESULT START --------------------------------------");
        //Log.d("RAZORPAY","PAYMENT RESULT : "+result);
        Log.d("RAZORPAY", "PAYMENT RESULT END --------------------------------------");

        logHDPSData();
    }

    private void logHDPSData() {
        if (pref != null) {
            String userId = "", displayName = "";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null) {
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callHDPSDataEvent(userId, displayName);
            }
        }
    }


    private String callPayLinkAPI(String transactionID, String farmerName, String mobileno,
                                  int amount, String email, String jsonRazorPayID) {
        try {
            HDPSRequest request = new HDPSRequest();
            //request.setAmount(1000);
            //amount = 1; /*FOR TESTING ONLY TODO REMOVE LATER*/
            int amountFinal = 100 * amount;
            request.setAmount(amountFinal);
            request.setCurrency("INR");
            request.setAcceptPartial(false); /*true*/
            request.setFirstMinPartialAmount(100);
            //long unixTime = System.currentTimeMillis() / 1000L;
            Date datePlus = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(16));
            int expireTime = (int) (datePlus.getTime() / 1000L);
            request.setExpireBy(expireTime);
            Log.d("HDPS", "Pay Link expire by in milli's : " + expireTime);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
            Log.d("HDPS", "Pay Link expire by in time: " + df.format(datePlus)); //datePlus
            /*int min = 2;
            int max = 99;
            Random r = new Random();
            int i1 = r.nextInt(max - min + 1) + min;*/
            //request.setReferenceId(("TS1989"+i1));
            //String referenceId = transactionID + i1;
            //request.setReferenceId(referenceId); /*Transaction id with Random number, 8th April 2021*/
            request.setReferenceId(transactionID); //jsonRazorPayID
            request.setDescription("Payment for reference id " + transactionID); //jsonRazorPayID
            Customer customer = new Customer();
            customer.setName(farmerName);
            customer.setContact(mobileno);
            //-------------TEST USER-----------//
            /*customer.setName("Gaurav Kumar");
            //customer.setContact("+917972213781");
            customer.setContact("+919999999999");
            customer.setEmail("gaurav.kumar@example.com");*/
            //customer.setName("Mahendra Mahajan");
            //customer.setEmail("mahendra.mahajan@mahyco.com");
            //customer.setEmail("rajshri.shukla@mahyco.com"); /*Rajshri details*/
            customer.setEmail(email); /*Rajshri details*/
            request.setCustomer(customer);
            Notify notify = new Notify();
            notify.setSms(true);
            notify.setEmail(true);
            request.setNotify(notify);
            request.setReminderEnable(true);
            Notes notes = new Notes();
            //notes.setPolicyName("HDPS Coupon Payment");
            notes.setReferenceId(transactionID);
            request.setNotes(notes);
            //request.setCallbackUrl("https://example-callback-url.com/");
            request.setCallbackUrl(CALLBACK_URL);
            request.setCallbackMethod("get");
            /*Added on 17th May 2021*/
            Options options = new Options();
            Checkout checkout = new Checkout();
            checkout.setName(" Mahyco Private Limited");
            options.setCheckout(checkout);
            request.setOptions(options);
            String result = "";
            Log.d("HDPS", "START PAYMENT CALL ____________________________");
            result = new RazorPaymentMasterData(request).execute().get();
            Log.d("HDPS", "END PAYMENT CALL ____________________________");
            Log.d("HDPS", "CONVERT TO RESPONSE TO POJO ____________________________");
            Gson gson = new GsonBuilder().create();
            RazorPayResponse response = gson.fromJson(result, RazorPayResponse.class);
            if (response != null) {
                Log.d("HDPS", "POJO ____________________________:" + response.toString());
                String pLinkId = response.getId();
                Log.d("HDPS", "CALL UPDATE PLINK ID API START ----------------------");
                Log.d("HDPS", "PLINK ID : " + pLinkId);
                Log.d("HDPS", "TRANSACTION ID : " + transactionID);
                if (pLinkId != null && !pLinkId.equalsIgnoreCase("")) {
                    callUpdatePLINKIDAPI(pLinkId, transactionID);
                    Log.d("HDPS", "CALL UPDATE PLINK ID API END ----------------------");
                }
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    void callUpdatePLINKIDAPI(String plinkID, String transactionID) {
        try {
            Log.d("HDPS", "CALLINK UPDATE PLINK ID API  ----------------------");
            new HDPSNewActivity.UpdateHDPSData(plinkID, transactionID, context).execute(SERVER);

        } catch (Exception e) {
            Log.d("HDPS", "MSG : " + e.getMessage());
        }
    }

    private class UpdateHDPSData extends AsyncTask<String, String, String> {

        ProgressDialog progressDailog;
        String pLinkId;
        String transactionID;
        Context context;

        public UpdateHDPSData(String pLinkId, String transactionID, Context context) {
            this.pLinkId = pLinkId;
            this.transactionID = transactionID;
            this.context = context;
        }

        protected void onPreExecute() {
            progressDailog = new ProgressDialog(context);
            progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDailog.setCanceledOnTouchOutside(false
            );
            progressDailog.setCancelable(false);
            progressDailog.setMessage("Payment data updating..");
            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject obj = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                long transID = Long.parseLong(transactionID);
                jsonObject.put("transaction_id", transID);
                jsonObject.put("plink_id", pLinkId);
                obj.put("Table", jsonObject);
                Log.d("HDPS", "************ UPDATE API URL : " + Constants.HDPS_UPDATE_API);
                Log.d("HDPS", "************ UPDATE API JSON OBJECT : " + obj);
                String result = HttpUtils.POSTJSON(Constants.HDPS_UPDATE_API, obj, "");
                Log.d("HDPS", "************ UPDATE API RESPONSE : " + result);
                return result;
            } catch (Exception e) {
                Log.d("MSG", "MSG : " + e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (progressDailog != null) {
                    progressDailog.dismiss();
                }

                Log.d("HDPS", "UPDATE API Response" + resultout);

                JSONObject obj = new JSONObject(resultout);
                String success = obj.getString("success");
                String message = obj.getString("message");
                Log.d("HDPS", "UPDATE plinkID ******** success : " + success);
                Log.d("HDPS", "UPDATE pLinkID ******** message : " + message);


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.toString() + "-");

            }

        }

    }


    /**
     * <P>Method to post the payment link data to server</P>
     */
    public class RazorPaymentMasterData extends AsyncTask<String, String, String> {

        HDPSRequest request;

        public RazorPaymentMasterData(HDPSRequest request) {
            this.request = request;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                /*//Add POST data in JSON format
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("", request.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                if (request != null) {
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(request);// obj is your object
                    JSONObject jsonObj = new JSONObject(json);

                    Log.d("HDPS", "URL : " + PAYMENT_URL);
                    Log.d("HDPS", "Request JSON OBJECT : " + jsonObj);

                    //Get the Response for the request
                    return HttpUtils.POSTJSONForPayment(PAYMENT_URL, jsonObj, KEY_ID, SECRET_KEY);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (result.contains("true")) {
                msclass.showMessage("Payment link shared successfully.");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (result.contains("error")) {
                        JSONObject jError = jsonObject.getJSONObject("error");
                        String error = jError.getString("description").toString();
                        if (!error.equalsIgnoreCase("")) {
                            msclass.showMessage(error);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("HDPS", "RESPONSE RAZOR PAY API : " + result);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.btnTakeFarmerPhoto:
                // msclass.showMessage("message");
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
        }
    }


    @Override
    public void onPickResult(PickResult r) {
        try {
            if (r.getError() == null) {
                roundedImageView.setImageBitmap(r.getBitmap());
                if (roundedImageView.getDrawable() != null) {
                    roundedImageView.setVisibility(View.VISIBLE);
                    crdImage.setVisibility(View.VISIBLE);
                } else {
                    roundedImageView.setVisibility(View.GONE);
                    crdImage.setVisibility(View.GONE);
                }
                Date entrydate = new Date();
                //Image path
                String pathImage = r.getPath();
                ////
                AppConstant.queryImageUrl = pathImage;
                AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                // need to set commpress image path
                //   Imagename=AppConstant.Imagename;
                imagePath = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave

                //roundedImageView.setImageBitmap(myBitmap);
                ////
                // encodedImage = FileUtilImage.getImageDatadetail(pathImage);
                //Log.d("encodedImage::", encodedImage);
            } else {
                //Handle possible errors
                //TODO: do what you have to do with r.getError();
                // Log.e(TAG, "onPickResult: ", r.getError());
                //Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show(); /*Remark 9th Sept 2021*/
            }
        } catch (Exception ex) {
            //Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show(); /*Remark 9th Sept 2021*/
            // Object tr = null;
            // Log.d("error ::"+ex.toString(), encodedImage);

        }
    }


    private void selectImage() {
        try {
            if (Indentcreate.getPickImageIntent(this) != null) {
                photoFile = Indentcreate.fileobj;
                startActivityForResult(Indentcreate.getPickImageIntent(this), REQUEST_CAMERA);//100
            } else {
                Toast.makeText(this, "Picker intent not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.d(TAG, "selectImage(): " + ex.toString());
        }
    }

    private boolean checkPlayServices() {
        boolean flag = false;
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(this,
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public String uniqueId() {

        String croptype = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();
        String pricePerpacket = "20";


        String desiredproduct = "";

        String desiredcroptype = croptype.trim();


        //  product = product.replaceAll("([^a-zA-Z]|\\s)+", "");
        product = product.replaceAll(" ", "");
        desiredproduct = product.trim();


        String uid = desiredcroptype + desiredproduct + pricePerpacket;

        Log.d("UID", uid);
        return uid;
    }


    public void saveToDb() {

// Old offline
    /*    String uId = uniqueId();
        String mobileNumber = etMobileNo.getText().toString();
        String whatsappNumber = "";
        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsappNumber = "";

        } else
            {

            whatsappNumber = etWhatsappNumber.getText().toString();
        }
        String farmerName = etFarmername.getText().toString();
        String village = spVillage.getSelectedItem().toString();
        String cropType = spCropType.getSelectedItem().toString();
        String product = spProductName.getSelectedItem().toString();
        String state = spState.getSelectedItem().toString();
        String district = spDist.getSelectedItem().toString();
        String taluka = spTaluka.getSelectedItem().toString();
        String quantityPacket = txtQty.getText().toString();
        String packetsBooked = spUnits.getSelectedItem().toString();
        String couponsIssued = tvNoCouponIssued.getText().toString();
        String totalAmount = txtAmount.getText().toString().replace("Rs", "").trim();

        String couponQr1 = txtCouponcode.getText().toString().trim().length()==0?"0":txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim().length()==0?"0":txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim().length()==0?"0":txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim().length()==0?"0":txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim().length()==0?"0":txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim().length()==0?"0":txtCouponcode6.getText().toString().trim();


        String isSynced = "0";
        String imgStatus = "0";


        Date entrydate = new Date();
        final String tempImagePath;
        final String farmerPhoto = "CouponRecordFarmerPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        tempImagePath = imagePath;
        long epoch = entrydate.getTime();
        System.out.println(epoch);

        String timeStamp = String.valueOf(epoch);
*/
        //end old

        //ONLINE Start
        uId = uniqueId();
        mobileNumber = etMobileNo.getText().toString();
        whatsappNumber = "";
        if (etWhatsappNumber.getText().toString().isEmpty() || etWhatsappNumber.getText().toString().equals("")) {

            whatsappNumber = "";

        } else {

            whatsappNumber = etWhatsappNumber.getText().toString();
        }
        farmerName = etFarmername.getText().toString();
        village = spVillage.getSelectedItem().toString();
        cropType = spCropType.getSelectedItem().toString();
        crop = cropType;
        product = spProductName.getSelectedItem().toString();
        state = spState.getSelectedItem().toString();
        district = spDist.getSelectedItem().toString();
        taluka = spTaluka.getSelectedItem().toString();
        quantityPacket = txtQty.getText().toString();
        packetsBooked = spUnits.getSelectedItem().toString();
        couponsIssued = tvNoCouponIssued.getText().toString();
        totalAmount = txtAmount.getText().toString().replace("Rs", "").trim();
        couponQr1 = txtCouponcode.getText().toString().trim().length() == 0 ? "0" : txtCouponcode.getText().toString().trim();
        couponQr2 = txtCouponcode2.getText().toString().trim().length() == 0 ? "0" : txtCouponcode2.getText().toString().trim();
        couponQr3 = txtCouponcode3.getText().toString().trim().length() == 0 ? "0" : txtCouponcode3.getText().toString().trim();
        couponQr4 = txtCouponcode4.getText().toString().trim().length() == 0 ? "0" : txtCouponcode4.getText().toString().trim();
        couponQr5 = txtCouponcode5.getText().toString().trim().length() == 0 ? "0" : txtCouponcode5.getText().toString().trim();
        couponQr6 = txtCouponcode6.getText().toString().trim().length() == 0 ? "0" : txtCouponcode6.getText().toString().trim();


        //  String isSynced = "0";
        // String imgStatus = "0";

        isSynced = "2"; // fpr online
        imgStatus = "2"; // for online

        Date entrydate = new Date();

        farmerPhoto = AppConstant.Imagename;// "CouponRecordFarmerPhoto" + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
        tempImagePath = imagePath;
        long epoch = entrydate.getTime();
        System.out.println(epoch);
        timeStamp = String.valueOf(epoch);

        //END ONLINEa
        // if (couponentryalredySaved()==true) {

        boolean fl = true; /*mDatabase.insertCouponRecordData(uId, timeStamp, usercode, state,
                   district, taluka, village, farmerName, mobileNumber, whatsappNumber,
                   cropType, product, quantityPacket, packetsBooked, couponsIssued, totalAmount, couponQr1,
                   couponQr2, couponQr3, couponQr4, couponQr5,couponQr6,
                   farmerPhoto, tempImagePath, imgStatus, isSynced, address, cordinates);
                   */
        if (fl) {
          /*  try {
                    int  amt=0;
                    amt = Integer.valueOf(getPricePerPacket(selectedProduct));

                     mDatabase.updateCouponscancurrentamount(amt);
            }
            catch (Exception ex)
            {

            }*/

            // uploadCouponRecords("mdo_couponSchemeDownloadAndUpload");
            uploadCouponRecordsOnline("mdo_couponSchemeDownloadAndUpload");


        } else {

            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
        // }

    }


    void clearDataAfterSuccess() {
        etMobileNo.setText("");
        etWhatsappNumber.setText("");
        etFarmername.setText("");
        spUnits.setSelection(0);

        /*Added 12th April 2021*/
        spState.setSelection(0);
        spDist.setSelection(0);
        spVillage.setSelection(0);
        spTaluka.setSelection(0);
        spCropType.setSelection(0);
        spProductName.setSelection(0);
        spRetailerDetails.setSelection(0);
        spTalukaDL.setSelection(0);
        edEmail.setText("");

        units = "";
        txtCouponcode.setText("");
        txtCouponcode2.setText("");
        txtCouponcode3.setText("");
        txtCouponcode4.setText("");
        txtCouponcode5.setText("");
        isNeedToshowAlert = false;
        switchYN.setChecked(false);
        switchYNCoupon.setChecked(false);
        imagePath = "";
        roundedImageView.setImageDrawable(null);
        roundedImageView.setVisibility(View.GONE);
        crdImage.setVisibility(View.GONE);
    }

    private void bindSpinnerData(String crop, String allowpacket1, String perCouponPkt1) {
        try {
            Log.d("bindSpinnerData", "crop : " + crop);
            if (crop == null || crop.equalsIgnoreCase("")) {
                return;
            }

            int allowPacketData = Integer.parseInt(allowpacket1); //2 //5 Conditions
            int perCouponPktData = Integer.parseInt(perCouponPkt1); //1 //3 Conditions

            spUnits.setAdapter(null);
            List<GeneralMaster> gm1 = new ArrayList<GeneralMaster>();
            Log.d("Data SP_PRODUCT", "bindSpinner DATA ITEM ID: " + spProductName.getSelectedItemId() + " Product: " + spProductName.getSelectedItem());
            GeneralMaster gm = (GeneralMaster) spProductName.getSelectedItem();
            Log.d("DATA", "GM DATA : " + gm.Code() + " " + gm.Desc());

            int packetVal = 0;
            gm1.add(new GeneralMaster(("0"), "SELECT"));
            for (int i = 1; i <= allowPacketData; i++) {
                packetVal = packetVal + (perCouponPktData);
                gm1.add(new GeneralMaster(("" + i), ("" + packetVal)));
            }


            /*if (gm.Code().equalsIgnoreCase("115000191") || gm.Code().equalsIgnoreCase("115000190")){
                Log.d("SP_PRODUCT","Reset Coupon data");
                gm1.add(new GeneralMaster("0", "SELECT"));
                gm1.add(new GeneralMaster("1", "1"));
                gm1.add(new GeneralMaster("2", "2"));
            }
            else
                {
                gm1.add(new GeneralMaster("0", "SELECT"));
                gm1.add(new GeneralMaster("1", "3"));
                gm1.add(new GeneralMaster("2", "6"));
                gm1.add(new GeneralMaster("3", "9"));
                gm1.add(new GeneralMaster("4", "12"));
                gm1.add(new GeneralMaster("5", "15"));
                gm1.add(new GeneralMaster("6", "18"));
                if (crop.toUpperCase().contains("PADDY")) {
                    gm1.add(new GeneralMaster("7", "21"));
                }
            }*/

            ArrayAdapter<GeneralMaster> adapter1 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spUnits.setAdapter(adapter1);

        } catch (Exception ex) {
           // Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show(); /*Remark 9th Sept 2021*/
        }


    }

    public void bindState() {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                SQLiteDatabase db = mDatabase.getReadableDatabase();

                Cursor cursor = db.rawQuery(searchQuery, null);
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
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }


    //    Validation for all fields
    public boolean validation() {


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
        if (spVillage.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Enter Village Name");
            return false;
        }

        if (etFarmername.getText().length() == 0) {
            msclass.showMessage("Please  Enter Farmer Name");
            return false;
        }
        if (etMobileNo.getText().length() != 10) {
            msclass.showMessage("Please Enter Valid Mobile Number");
            return false;
        }
        if (roundedImageView.getDrawable() == null) {
            msclass.showMessage("Please Click your photo");
            return false;
        }
        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductName.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Product Name");
            return false;
        }

        if (spUnits.getSelectedItemPosition() == 0 || spUnits.getSelectedItem().toString().toUpperCase().contains("SELECT")) {
            msclass.showMessage("Please Select units.");
            return false;
        }

        GeneralMaster gm = (GeneralMaster) spProductName.getSelectedItem();
        Log.d("DATA", "GM DATA : " + gm.Code() + " " + gm.Desc());
        //if (gm.Code().equalsIgnoreCase("115000191") || gm.Code().equalsIgnoreCase("115000190")) {
        Log.d("HDPS","retailerContraint : "+retailerContraint);
        if(retailerContraint.equals("0")){
            /*No check for Preferred Taluka & Retailers*/
        } else {
            /*Added 7 April 2021*/
            if (spTalukaDL.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select preferred Taluka.");
                return false;
            }

            if (spRetailerDetails.getSelectedItemPosition() == 0) {
                msclass.showMessage("Please Select preferred Retailer.");
                return false;
            }
        }

        /*End*/

        if (!switchYN.isEnabled()) {
            msclass.showMessage("Please Collect Cash First");
            return false;
        }
        if (!switchYNCoupon.isEnabled()) {
            msclass.showMessage("Please Hand Over The Coupon First");
            return false;
        }
        if (!rdOnlinePayment.isChecked() && !rdPaymentByCash.isChecked()) {
            msclass.showMessage("Please select payment mode ");
            return false;
        }
        // Check same crop product  and mobile  number
        try {
            String searchQuery = "select ifnull(sum(couponsIssued),0) as total  from CouponRecordData where " +
                    " mobileNumber ='" + etMobileNo.getText().toString().trim() + "' and " +
                    " crop ='" + spCropType.getSelectedItem().toString().trim() + "' and " +
                    " product ='" + spProductName.getSelectedItem().toString().trim() + "' " +
                    " group by mobileNumber,crop,product";

            //Start
            int allotcount = 0;
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getjsonResults(searchQuery);


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    String total = jsonArray.getJSONObject(i).getString("total");
                    allotcount = Integer.parseInt(total);

                }
                Log.d("ObjectasBytes", jsonArray.toString());

            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.toString());
                return false;
            }
            allotcount = allotcount + Integer.parseInt(spUnits.getSelectedItem().toString());

            //Strt
            /*
                if(spCropType.getSelectedItem().toString().trim().equalsIgnoreCase("paddy"))
                {
                    if (allotcount > 6)
                    {
                        msclass.showMessage("one farmer allow only 6 coupon of per product . , " +
                                " please confirm it.");
                        return false;
                    }
                }
                else {
                    if (allotcount > 5) {
                        msclass.showMessage("one farmer allow only 5 coupon of per product . , " +
                                " please confirm it.");
                        return false;
                    }
                }
             */
            //end


            // end
           /* Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0) {
                int allotcount = Integer.parseInt(cursor.getString(0).toString());// total
                allotcount = allotcount + Integer.parseInt(spUnits.getSelectedItem().toString());
                if (allotcount > 5) {
                    msclass.showMessage("one farmer allow only 5 coupon of per product . , " +
                            " please confirm it.-");
                    return false;
                }
            }
            cursor.moveToNext();
            cursor.close();
            */
        } catch (Exception e) {
            msclass.showMessage(e.toString());
            return false;
        }
        // if (!checkmobilenoProductsame()) return false;
        /*Commented on 12 April 2021 START*/
        if (!checkcorrectcouponfromList()) return false;
        if (!checkCouponsEntered()) return false;
        if (!checkIfSameCouponsEntered()) return false;
        if (!checkIfCouponAlreadySubmitted()) return false;
        /*END*/
        // if (!checkamountbalance()) return false;

        return true;
    }

    public boolean checkamountbalance() {
        try {
            int noselectedcoupon = Integer.parseInt(spUnits.getSelectedItem().toString());
            int amount = noselectedcoupon * 50;
            String st = "";
            // String searchQuery = " select IFNULL( SUM(amount) ,0) as amount from " +
            //        " ( SELECT  a.couponsIssued  *  (case when a.crop='PADDY'  " +
            //       " then 60 else  50 end) as amount    FROM CouponRecordData  a ) as aa ";
            String searchQuery = " SELECT  IFNULL(SUM(a.couponsIssued),0)  * 50 as amount " +
                    " FROM CouponRecordData  a  ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            int dd = cursor.getCount();
            while (cursor.isAfterLast() == false) {
                st = cursor.getString(0);
                amount = amount + Integer.parseInt(cursor.getString(0).toString());
                cursor.moveToNext();
            }
            cursor.close();
            int paymentamount = 0;
            int remainingAmount = 0;
            int couponscanamt = 0;
            String searchQuery2 = " select IFNULL( SUM(paymentAmount) ,0) as paymentAmount" +
                    ",IFNULL( SUM(remainingAmount) ,0) as remainingAmount," +
                    "IFNULL( SUM(newCouponScanAmount) ,0) as newCouponScanAmount" +
                    "  from couponPaymentAmount  ";
            Cursor cursor2 = mDatabase.getReadableDatabase().rawQuery(searchQuery2, null);
            cursor2.moveToFirst();
            while (cursor2.isAfterLast() == false) {
                paymentamount = Integer.parseInt(cursor2.getString(0).toString());
                remainingAmount = Integer.parseInt(cursor2.getString(1).toString());
                couponscanamt = Integer.parseInt(cursor2.getString(2).toString());
                //
                cursor2.moveToNext();
            }
            cursor2.close();
            int balanceamount = 0;
            if (remainingAmount >= 2000) {
                balanceamount = remainingAmount;
            } else {
                balanceamount = (couponscanamt + remainingAmount);//-paymentamount;
            }
            if (balanceamount >= 2000) {
                msclass.showMessage("Your payment for MFDC Coupons is pending and has reached the set money transfer limit of 2000/-.\n" +
                        "Kindly pay the amount online and then proceed to issue new coupons.\n" +
                        "(note- Before online payment ,please uploading pending coupon list..)");
                return false;

            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean checkmobilenoProductsame() {
        try {
            String couponQr1 = txtCouponcode.getText().toString().trim();
            String couponQr2 = txtCouponcode2.getText().toString().trim();
            String couponQr3 = txtCouponcode3.getText().toString().trim();
            String couponQr4 = txtCouponcode4.getText().toString().trim();
            String couponQr5 = txtCouponcode5.getText().toString().trim();
            String searchQuery = "select sum(couponsIssued) as total  from CouponRecordData where " +
                    "mobileNumber ='" + etMobileNo.getText().toString().trim() + "' and " +
                    "crop ='" + spCropType.getSelectedItem().toString().trim() + "' and " +
                    "product ='" + spProductName.getSelectedItem().toString().trim() + "' " +
                    " group by mobileNumber,crop,product";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            while (cursor.isAfterLast() == false) {
                if (count > 0) {
                    int allotcount = Integer.parseInt(cursor.getString(0).toString());// total
                    allotcount = allotcount + Integer.parseInt(spUnits.getSelectedItem().toString());
                    if (spCropType.getSelectedItem().toString().trim().equalsIgnoreCase("paddy")) {
                        if (allotcount > 6) {
                            msclass.showMessage("same farmer not allow to more than 6 coupon , " +
                                    " please confirm it.");
                            return false;
                        }
                    } else {
                        if (allotcount > 5) {
                            msclass.showMessage("same farmer not allow to more than 5 coupon , " +
                                    " please confirm it.");
                            return false;
                        }
                    }
                }

                cursor.moveToNext();
            }
            cursor.close();


        } catch (Exception ex) {
            return false;
        }

        return true;

    }

    public boolean checkCouponsEntered() {

        boolean isValid = true;
        String couponQr1 = txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim();

        if (units != null && !units.equals("")) {
            if (units.toLowerCase().trim().equals("1")) {
                if (couponQr1.length() == 0 || couponQr1.length() > 13) {
                    msclass.showMessage("Please Enter First Coupon 13 digit number ");
                    return false;
                }
            } else if (units.toLowerCase().trim().equals("2")) {

                if (couponQr1.length() == 0 || couponQr1.length() > 13) {
                    msclass.showMessage("Please Enter First Coupon 13 digit number");
                    return false;
                } else if (couponQr2.length() == 0 || couponQr2.length() > 13) {
                    msclass.showMessage("Please Enter Second Coupon 13 digit number");
                    return false;
                }
            } else if (units.toLowerCase().trim().equals("3")) {
                if (couponQr1.length() == 0 || couponQr1.length() > 13) {
                    msclass.showMessage("Please Enter First Coupon 13 digit number");
                    return false;
                } else if (couponQr2.length() == 0 || couponQr2.length() > 13) {
                    msclass.showMessage("Please Enter Second Coupon 13 digit number");
                    return false;
                } else if (couponQr3.length() == 0 || couponQr3.length() > 13) {
                    msclass.showMessage("Please Enter Third Coupon 13 digit number");
                    return false;
                }

            } else if (units.toLowerCase().trim().equals("4")) {

                if (couponQr1.length() == 0 || couponQr1.length() > 13) {
                    msclass.showMessage("Please Enter First Coupon 13 digit number");
                    return false;
                } else if (couponQr2.length() == 0 || couponQr2.length() > 13) {
                    msclass.showMessage("Please Enter Second Coupon 13 digit number");
                    return false;
                } else if (couponQr3.length() == 0 || couponQr3.length() > 13) {
                    msclass.showMessage("Please Enter Third Coupon 13 digit number");
                    return false;
                } else if (couponQr4.length() == 0 || couponQr4.length() > 13) {
                    msclass.showMessage("Please Enter Fourth Coupon 13 digit number");
                    return false;
                }

            } else if (units.toLowerCase().trim().equals("5")) {
                if (couponQr1.length() == 0 || couponQr1.length() > 13) {
                    msclass.showMessage("Please Enter First Coupon 13 digit number");
                    return false;
                } else if (couponQr2.length() == 0 || couponQr2.length() > 13) {
                    msclass.showMessage("Please Enter Second Coupon 13 digit number");
                    return false;
                } else if (couponQr3.length() == 0 || couponQr3.length() > 13) {
                    msclass.showMessage("Please Enter Third Coupon 13 digit number");
                    return false;
                } else if (couponQr4.length() == 0 || couponQr4.length() > 13) {
                    msclass.showMessage("Please Enter Fourth Coupon 13 digit number");
                    return false;
                } else if (couponQr5.length() == 0 || couponQr5.length() > 13) {
                    msclass.showMessage("Please Enter Fifth Coupon 13 digit number");
                    return false;
                }

            }
        }

        return isValid;
    }

    public boolean checkIfSameCouponsEntered() {

        boolean isValid = true;
        String couponQr1 = txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim();

        if (units.toLowerCase().trim().equals("2")) {
            if (couponQr1.equals(couponQr2)) {
                msclass.showMessage("Coupon already entered");
                return false;
            }
        } else if (units.toLowerCase().trim().equals("3")) {
            if (couponQr1.equals(couponQr2) || couponQr2.equals(couponQr3) || couponQr1.equals(couponQr3)) {
                msclass.showMessage("Coupon already entered");
                return false;
            }
        } else if (units.toLowerCase().trim().equals("4")) {
            if (couponQr1.equals(couponQr2) || couponQr1.equals(couponQr3) || couponQr1.equals(couponQr4) || couponQr2.equals(couponQr3) || couponQr2.equals(couponQr4) || couponQr3.equals(couponQr4)) {
                msclass.showMessage("Coupon already entered");
                return false;
            }
        }
        if (units.toLowerCase().trim().equals("5")) {
            if (couponQr1.equals(couponQr2) || couponQr1.equals(couponQr3) || couponQr1.equals(couponQr4) || couponQr1.equals(couponQr5)
                    || (couponQr2.equals(couponQr3) || couponQr2.equals(couponQr4) || couponQr2.equals(couponQr5)) ||
                    (couponQr3.equals(couponQr4) || couponQr3.equals(couponQr5) || couponQr4.equals(couponQr5))) {
                msclass.showMessage("Coupon already entered");
                return false;
            }
        } else if (units.toLowerCase().trim().equals("6")) {
            if (couponQr1.equals(couponQr2) || couponQr1.equals(couponQr3) || couponQr1.equals(couponQr4) ||
                    couponQr1.equals(couponQr5)
                    || (couponQr1.equals(couponQr6)
                    || (couponQr2.equals(couponQr3)
                    || couponQr2.equals(couponQr4)
                    || couponQr2.equals(couponQr5))
                    || couponQr2.equals(couponQr6))
                    || (couponQr3.equals(couponQr4)
                    || couponQr3.equals(couponQr5)
                    || couponQr3.equals(couponQr6)
                    || couponQr4.equals(couponQr5)
                    || couponQr4.equals(couponQr6)
                    || couponQr5.equals(couponQr6))) {
                msclass.showMessage("Coupon already entered");
                return false;
            }

        }


        //}

        return isValid;
    }


    boolean couponentryalredySaved() {


        String couponQr1 = txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim();

        if (couponQr1.length() > 0) {
            String searchQuery = "select count(*)  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr1 + "' and  " +
                    "couponQr2 ='" + couponQr2 + "' and  " +
                    "couponQr3 ='" + couponQr3 + "' and  " +
                    "couponQr4 ='" + couponQr4 + "' and  " +
                    "couponQr5 ='" + couponQr5 + "' and  " +

                    "couponQr6 ='" + couponQr6 + "'";

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            int count = cursor.getCount();
            if (count > 0) {
                msclass.showMessage("This coupons alredy given to farmer.");
                return false;
            }
            cursor.close();

        }

        return true;
    }

    boolean checkIfCouponAlreadySubmitted() {


        String couponQr1 = txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim();

        if (couponQr1.length() > 0) {
            String searchQuery = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr1 + "' OR " +
                    "couponQr2 ='" + couponQr1 + "' OR " +
                    "couponQr3 ='" + couponQr1 + "' OR " +
                    "couponQr4 ='" + couponQr1 + "' OR " +
                    "couponQr5 ='" + couponQr1 + "' OR " +
                    "couponQr6 ='" + couponQr1 + "'";

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            int count = cursor.getCount();
            if (count > 0) {
                msclass.showMessage("First coupon: " + couponQr1 + " have been used already.");
                return false;
            }
            cursor.close();

        }


        if (couponQr2.length() > 0) {

            String searchQuery2 = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr2 + "' OR " +
                    "couponQr2 ='" + couponQr2 + "' OR " +
                    "couponQr3 ='" + couponQr2 + "' OR " +
                    "couponQr4 ='" + couponQr2 + "' OR " +
                    "couponQr5 ='" + couponQr2 + "' OR " +
                    "couponQr6 ='" + couponQr2 + "'";

            Cursor cursor2 = mDatabase.getReadableDatabase().rawQuery(searchQuery2, null);

            int count2 = cursor2.getCount();
            if (count2 > 0) {
                msclass.showMessage("Second coupon: " + couponQr2 + " have been used already.");
                return false;
            }
            cursor2.close();
        }

        if (couponQr3.length() > 0) {

            String searchQuery3 = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr3 + "' OR " +
                    "couponQr2 ='" + couponQr3 + "' OR " +
                    "couponQr3 ='" + couponQr3 + "' OR " +
                    "couponQr4 ='" + couponQr3 + "' OR " +
                    "couponQr5 ='" + couponQr3 + "' OR " +
                    "couponQr6 ='" + couponQr3 + "'";

            Cursor cursor3 = mDatabase.getReadableDatabase().rawQuery(searchQuery3, null);

            int count3 = cursor3.getCount();
            if (count3 > 0) {
                msclass.showMessage("Third coupon: " + couponQr3 + " have been used already.");
                return false;
            }
            cursor3.close();
        }


        if (couponQr4.length() > 0) {

            String searchQuery4 = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr4 + "' OR " +
                    "couponQr2 ='" + couponQr4 + "' OR " +
                    "couponQr3 ='" + couponQr4 + "' OR " +
                    "couponQr4 ='" + couponQr4 + "' OR " +
                    "couponQr5 ='" + couponQr4 + "' OR " +
                    "couponQr6 ='" + couponQr4 + "'";

            Cursor cursor4 = mDatabase.getReadableDatabase().rawQuery(searchQuery4, null);

            int count4 = cursor4.getCount();
            if (count4 > 0) {
                msclass.showMessage("Fourth coupon: " + couponQr4 + " have been used already.");
                return false;
            }
            cursor4.close();
        }

        if (couponQr5.length() > 0) {

            String searchQuery5 = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr5 + "' OR " +
                    "couponQr2 ='" + couponQr5 + "' OR " +
                    "couponQr3 ='" + couponQr5 + "' OR " +
                    "couponQr4 ='" + couponQr5 + "' OR " +
                    "couponQr5 ='" + couponQr5 + "' OR " +
                    "couponQr6 ='" + couponQr5 + "'";

            Cursor cursor5 = mDatabase.getReadableDatabase().rawQuery(searchQuery5, null);

            int count5 = cursor5.getCount();
            if (count5 > 0) {
                msclass.showMessage("Fifth coupon: " + couponQr5 + " have been used already.");
                return false;
            }
            cursor5.close();
        }
        if (couponQr6.length() > 0) {

            String searchQuery6 = "select uId  from CouponRecordData where " +
                    "couponQr1 ='" + couponQr6 + "' OR " +
                    "couponQr2 ='" + couponQr6 + "' OR " +
                    "couponQr3 ='" + couponQr6 + "' OR " +
                    "couponQr4 ='" + couponQr6 + "' OR " +
                    "couponQr5 ='" + couponQr6 + "' OR " +
                    "couponQr6 ='" + couponQr6 + "' ";

            Cursor cursor6 = mDatabase.getReadableDatabase().rawQuery(searchQuery6, null);

            int count6 = cursor6.getCount();
            if (count6 > 0) {
                msclass.showMessage("Fifth coupon: " + couponQr6 + " have been used already.");
                return false;
            }
            cursor6.close();
        }

        return true;
    }

    boolean checkcorrectcouponfromList() {


        String couponQr1 = txtCouponcode.getText().toString().trim();
        String couponQr2 = txtCouponcode2.getText().toString().trim();
        String couponQr3 = txtCouponcode3.getText().toString().trim();
        String couponQr4 = txtCouponcode4.getText().toString().trim();
        String couponQr5 = txtCouponcode5.getText().toString().trim();
        String couponQr6 = txtCouponcode6.getText().toString().trim();


        if (couponQr1.length() > 0) {
            String searchQuery = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr1 + "' and productCode='" + productcode + "'";

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            int count = cursor.getCount();
            if (count == 0) {
                msclass.showMessage("First coupon: " + couponQr1 + " not correct,please check coupon code number.");
                return false;
            }
            cursor.close();

        }


        if (couponQr2.length() > 0) {

            String searchQuery2 = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr2 + "' and productCode='" + productcode + "'";

            Cursor cursor2 = mDatabase.getReadableDatabase().rawQuery(searchQuery2, null);

            int count2 = cursor2.getCount();
            if (count2 == 0) {
                msclass.showMessage("Second coupon:" + couponQr2 + " not correct,please check coupon code number.");
                return false;
            }
            cursor2.close();
        }

        if (couponQr3.length() > 0) {

            String searchQuery3 = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr3 + "' and productCode='" + productcode + "'";

            Cursor cursor3 = mDatabase.getReadableDatabase().rawQuery(searchQuery3, null);

            int count3 = cursor3.getCount();
            if (count3 == 0) {
                msclass.showMessage("Third coupon: " + couponQr3 + " not correct,please check coupon code number.");
                return false;
            }
            cursor3.close();
        }


        if (couponQr4.length() > 0) {

            String searchQuery4 = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr4 + "' and productCode='" + productcode + "'";

            Cursor cursor4 = mDatabase.getReadableDatabase().rawQuery(searchQuery4, null);

            int count4 = cursor4.getCount();
            if (count4 == 0) {
                msclass.showMessage("Fourth coupon: " + couponQr4 + " not correct,please check coupon code number.");
                return false;
            }
            cursor4.close();
        }

        if (couponQr5.length() > 0) {

            String searchQuery5 = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr5 + "' and productCode='" + productcode + "'";

            Cursor cursor5 = mDatabase.getReadableDatabase().rawQuery(searchQuery5, null);

            int count5 = cursor5.getCount();
            if (count5 == 0) {
                msclass.showMessage("Fifth coupon: " + couponQr5 + " not correct,please check coupon code number.");
                return false;
            }
            cursor5.close();
        }
        if (couponQr6.length() > 0) {

            String searchQuery6 = "select *  from HDPSCouponRecords where " +
                    "couponCode ='" + couponQr6 + "' and productCode='" + productcode + "'";

            Cursor cursor6 = mDatabase.getReadableDatabase().rawQuery(searchQuery6, null);

            int count6 = cursor6.getCount();
            if (count6 == 0) {
                msclass.showMessage("Fifth coupon: " + couponQr6 + " not correct,please check coupon code number.");
                return false;
            }
            cursor6.close();
        }

        return true;
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
            msclass.showMessage(ex.getMessage());
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
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }

    public void bindTalukaDL(String dist) {
        try {
            spTalukaDL.setAdapter(null);
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
                spTalukaDL.setAdapter(adapter);
                // dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
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


    private void bindcroptype(Spinner spCropType, String Croptype) {
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            searchQuery = "SELECT distinct crop  FROM HDPSMasterScheme";


            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT CROP",
                    "SELECT CROP"));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

    }


    public String getQuantity(String product) {
        String qty = "";

        qty = mDatabase.fetchQtyCoupon(product);

        return qty;

    }


    public String getPricePerPacket(String product) {
        String price = "";

        price = mDatabase.fetchPerPriceCouponHDPS(product);


        return price;

    }

    private void bindProductName(Spinner spProductName, String croptype) {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct product,productCode  FROM HDPSMasterScheme where crop='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
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
            spProductName.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


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
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roundedImageView.getDrawable() != null) {

            roundedImageView.setVisibility(View.VISIBLE);
            crdImage.setVisibility(View.VISIBLE);


        } else {

            roundedImageView.setVisibility(View.GONE);
            crdImage.setVisibility(View.GONE);

        }
        try {

            startFusedLocationService();

        } catch (Exception ex) {
            msclass.showMessage("Funtion name :onresume" + ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (progressDailog != null) {
//            progressDailog.dismiss();
//        }


        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Toast.makeText(this, "OnPause called", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fusedlocationRecieved = false;
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    Log.d(TAG, "Fused api connected: ");
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
                }

            } else {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                PendingResult result =
                        LocationServices.SettingsApi.checkLocationSettings(
                                googleApiClient,
                                builder.build()
                        );

                result.setResultCallback(this);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onConnected: " + e.toString());
        }


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }

    @Override
    public synchronized void onLocationChanged(Location arg0) {

        try {
            if (arg0 == null) {
                // isLocationReceivedByGAPI = false;
                return;
            }
            if (arg0.getLatitude() == 0 && arg0.getLongitude() == 0) {
                return;
            }
            lati = arg0.getLatitude();
            longi = arg0.getLongitude();
            location = arg0;
            Log.d(TAG, "onLocationChanged: " + String.valueOf(longi));
            cordinates = String.valueOf(lati) + "-" + String.valueOf(longi);
            if(address.equals("")) {
                if (config.NetworkConnection()) {
                    address = getCompleteAddressString(lati, longi);
                }
            }
            Log.d(TAG, "onlocation" + cordinates);
        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null)
                address = "ADDRESS : " + addresses.get(0).getAddressLine(0);

            strAdd = "ADDRESS : " + addresses.get(0).getAddressLine(0);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }


    private synchronized void startFusedLocationService() {
        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                IsGPSEnabled = true;
            } else {
                IsGPSEnabled = false;
            }
            if (IsGPSEnabled) {
                locationRequest = new LocationRequest();//.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(INTERVAL);
                locationRequest.setSmallestDisplacement(0f);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);
                //  mLocationRequest = new LocationRequest();

                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API).addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                try {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "startFusedLocationService: " + e.toString());
                }
                GpsEnabled = true;

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(HDPSNewActivity.this);

                builder.setTitle("MyActivity");
                builder.setMessage("Please enable location and Gps");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
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
            //     Log.d(TAG, "startFusedLocationService: ");
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public int getLocationMode() {
        int val = 0;
        try {
            val = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            Log.d(TAG, "getLocationMode status: " + val);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return val;

    }

    public void stopFusedApi() {
        try {
            if (googleApiClient != null && (googleApiClient.isConnected())) {
                FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
                googleApiClient.disconnect();
            }
        } catch (Exception ex) {
            FirebaseCrash.report(ex);
            ex.printStackTrace(); // Ignore error

            // ignore the exception
        } finally {

            googleApiClient = null;
            locationRequest = null;
        }
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


    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageselect == 1) {
            roundedImageView.setImageBitmap(bm);

        }

    }

    private void onCaptureImageResult(Intent data) {

        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize = 2;
                    //Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);

                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // this only get capture photo
                    //************
                    Date entrydate = new Date();
                    String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename = this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                    // roundedImageView.setImageBitmap(myBitmap);


                    Glide.with(context)
                            .asDrawable()//asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .load(AppConstant.queryImageUrl)
                            .into(roundedImageView);

                    FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                            this, AppConstant.Imagename);
                    // need to set commpress image path
                    imagePath = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave

                    //**************
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }

        } catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    private void onCaptureImageResultold(Intent data) {


        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 2;

                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                    imagePath = photoFile.getAbsolutePath();
                    roundedImageView.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    msclass.showMessage(e.toString());
                    e.printStackTrace();
                }
                //end
            }

        } catch (Exception e) {
            msclass.showMessage(e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
/*
            Log.d(TAG, couponFromData + "Recieved"+requestCode);
            Log.d(TAG, couponFromData + "Recieved"+resultCode);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA)
                    //  Toast.makeText(getApplicationContext(), REQUEST_CAMERA, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString());
        }


        if (requestCode == RC_BARCODE_CAPTURE) {
            // Toast.makeText(getApplicationContext(), requestCode, Toast.LENGTH_SHORT).show();

            if (resultCode == CommonStatusCodes.SUCCESS) {
                // Toast.makeText(getApplicationContext(), CommonStatusCodes.SUCCESS, Toast.LENGTH_SHORT).show();

                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    couponFromData = data.getStringExtra("captureFrom");
                    Log.d(TAG, couponFromData + "Recieved");

                    if (!couponFromData.isEmpty()) {


                        Log.d(TAG, "Barcode read: " + barcode.displayValue);


                        switch (couponFromData) {
                            case "1":
                                txtCouponcode.setText(barcode.displayValue);
                                break;
                            case "2":
                                txtCouponcode2.setText(barcode.displayValue);
                                break;
                            case "3":
                                txtCouponcode3.setText(barcode.displayValue);
                                break;
                            case "4":
                                txtCouponcode4.setText(barcode.displayValue);
                                break;
                            case "5":
                                txtCouponcode5.setText(barcode.displayValue);
                                break;
                        }


                    }
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }


    public void uploadCouponRecordsOnline(String uploadCouponRecords) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                //new UploadCouponDataServer(uploadCouponRecords, context).execute(SERVER).get();
                new HDPSNewActivity.UploadCouponDataServer(uploadCouponRecords, context).execute(SERVER);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            msclass.showMessage("Please check the internet connection");

          /*      clearDataAfterSuccess();
            AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceBookingCouponActivity.this);

            builder.setTitle("MyActivity");
            builder.setMessage("Data Saved Successfully");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();


                }
            });


            AlertDialog alert = builder.create();
            alert.show();
          */
        }


    }

    public String uploadCouponRecordDataOnline(String UploadCouponRecordData) {
        String str = "";
        int action = 10;// Uploadif = // changes in 6 coupon allow
        //progressDailog.show();

        try {
            byte[] objAsBytes = null;//new byte[10000];
            try {
                JSONObject jsonObject = new JSONObject();
                uId = uId;
                String imageName = farmerPhoto;
                imgPath = imagePath;
                //stat
                //Add POST data in JSON format
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("action", action);
                    jsonParam.put("uId", uId);
                    jsonParam.put("timeStamp", timeStamp);
                    jsonParam.put("userCode", userCode);
                    jsonParam.put("state", state);
                    jsonParam.put("district", district);
                    jsonParam.put("taluka", taluka);
                    jsonParam.put("village", village);
                    jsonParam.put("farmerName", farmerName);
                    jsonParam.put("mobileNumber", mobileNumber);
                    jsonParam.put("whatsappNumber", whatsappNumber);
                    jsonParam.put("crop", crop);
                    jsonParam.put("product", product);
                    jsonParam.put("quantityPacket", quantityPacket);
                    jsonParam.put("packetsBooked", packetsBooked);
                    jsonParam.put("couponsIssued", couponsIssued);
                    jsonParam.put("totalAmount", totalAmount);
                    jsonParam.put("couponQr1", couponQr1);
                    jsonParam.put("couponQr2", couponQr2);
                    jsonParam.put("couponQr3", couponQr3);
                    jsonParam.put("couponQr4", couponQr4);
                    jsonParam.put("couponQr5", couponQr5);
                    jsonParam.put("couponQr6", couponQr6);

                    jsonParam.put("farmerPhoto", farmerPhoto);
                    jsonParam.put("imgPath", imgPath);
                    jsonParam.put("imgStatus", imgStatus);
                    jsonParam.put("isSynced", isSynced);
                    jsonParam.put("address", address);
                    jsonParam.put("cordinates", cordinates);

                    jsonParam.put("product_code", productcode);
                    jsonParam.put("pf_taluka", spTalukaDL.getSelectedItem());
                    jsonParam.put("pf_retailer", spRetailerDetails.getSelectedItem());
                    if (rdPaymentByCash.isChecked())
                        jsonParam.put("pf_payment_mode", "cash");
                    else if (rdOnlinePayment.isChecked())
                        jsonParam.put("pf_payment_mode", "online");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //end

                jsonObject.put("Table", jsonParam);
                objAsBytes = jsonObject.toString().getBytes("UTF-8");
                Log.d("ObjectasBytes", jsonObject.toString());
                Log.d("ObjectasBytes", objAsBytes.toString());

                /*str = syncCouponSingleImage(UploadCouponRecordData, SERVER, action,
                        objAsBytes, uId, imageName, imagePath);*/

                //handleCouponImageSyncResponse(UploadCouponRecordData, str, uId,imageName);

                String encodedImage = mDatabase.getImageDatadetail(imagePath);

                String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);

                JSONObject jsonObjectMain = new JSONObject();

                JSONObject jsonObjectSub = new JSONObject();

                jsonObjectSub.put("action", "10");
                jsonObjectSub.put("userCode", userCode);
                jsonObjectSub.put("encodedData", encodeData);
                jsonObjectSub.put("imgName", encodedImage);

                jsonObjectMain.put("Table", jsonObjectSub);

                Log.d("UploadHDPSAPICALL", "encodedData : " + encodeData);
                Log.d("UploadHDPSAPICALL", "imgName : " + encodedImage);
                Log.d("UploadHDPSAPICALL", "UploadHDPSAPICALL MAIN OBJECT : \n" + jsonObjectMain);


                /*Comment for now 8th April 2021*/
                //new UploadHDPSAPICALL(UploadCouponRecordData, jsonObjectMain).execute("");

                try {
                /*result = syncCouponSingleImage(Funname, SERVER, action,
                        objAsBytes, uId, imageName, imagePath);*/
                    return uploadHDPSData(UploadCouponRecordData, jsonObjectMain);

                } catch (Exception ex) {

                }

                Log.d("ObjectasBytes", objAsBytes.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //  msclass.showMessage(ex.getMessage());
        }
        return str;
    }

    private String uploadHDPSData(String function, JSONObject obj) {
        Log.d("uploadHDPSData", "URL : " + Constants.HDPS_SERVER_API);
        Log.d("uploadHDPSData", "JSON OBJECT : " + obj);
        String result = HttpUtils.POSTJSON(Constants.HDPS_SERVER_API, obj, "");
        Log.d("uploadHDPSData", "RESPONSE : " + result);
        return result;
    }

    public class UploadHDPSAPICALL extends AsyncTask<String, String, String> { //MyTravel
        ProgressDialog pd;
        String Funname;
        JSONObject obj;


        public UploadHDPSAPICALL(String Funname, JSONObject obj) {
            this.Funname = Funname;
            this.obj = obj;
        }

        protected void onPreExecute() {

            pd = new ProgressDialog(HDPSNewActivity.this);
            pd.setTitle("Data Uploading ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                /*result = syncCouponSingleImage(Funname, SERVER, action,
                        objAsBytes, uId, imageName, imagePath);*/
                return uploadHDPSData(Funname, obj);

            } catch (Exception ex) {

            }
            return result;
        }

        protected void onPostExecute(String result) {

        }
    }


    public String uploadCouponRecordData(String UploadCouponRecordData) {
        String str = "";
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
                    jsonArray = mDatabase.getjsonResults(searchQuery);// mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("farmerPhoto");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        Log.d("ObjectasBytes", jsonObject.toString());
                        Log.d("ObjectasBytes", objAsBytes.toString());


                        str = syncCouponSingleImage(UploadCouponRecordData, SERVER, action,
                                objAsBytes, uId, imageName, imagePath);

                        handleCouponImageSyncResponse(UploadCouponRecordData, str, uId, imageName);
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


        return str;
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

            // return uploadCouponRecordData("mdo_couponSchemeDownloadAndUpload");
            return uploadCouponRecordDataOnline("mdo_couponSchemeDownloadAndUpload");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                if (progressDailog != null) {
                    progressDailog.dismiss();
                }

                JSONObject obj = new JSONObject(resultout);
                String success = obj.getString("success");
                Log.d("HDPS", "RESPONSE HDPS success : " + success);
                JSONObject jsonMSG = obj.getJSONObject("message");
                JSONArray jsonTable = jsonMSG.getJSONArray("Table");
                JSONObject userMSGJson = jsonTable.getJSONObject(0);
                String userMsg = userMSGJson.getString("message");
                JSONArray jsonTable1 = jsonMSG.getJSONArray("Table1");
                //JSONArray jsonTable2 = jsonMSG.getJSONArray("Table2");
                JSONObject jsonObjectstatus = jsonTable1.getJSONObject(0);
                String status = jsonObjectstatus.getString("status");
                Log.d("HDPS", "RESPONSE HDPS json Status : " + status);

                if (status.equalsIgnoreCase("true")) {
                    try {
                        String transactionID = "" + userMsg;  //jsonObj.getString("trID");
                        Log.d("HDPS", "RESPONSE HDPS transactionID : " + transactionID);
                        String jsonRazorPayID = obj.getString("razorpayid");
                        Log.d("HDPS", "RESPONSE HDPS razorpayid : " + jsonRazorPayID.toString());

                        if (rdOnlinePayment.isChecked()) {
                            String totalAmountINR = txtAmount.getText().toString().replace("Rs", "").trim();
                            int amount = Integer.parseInt(totalAmountINR);
                            String email = edEmail.getText().toString();
                            //Log.d("HDPS", "PAYMENT PARAM TR ID: " + ("6123456" + transactionID));
                            Log.d("HDPS", "PAYMENT PARAM TR ID: " + transactionID);
                            Log.d("HDPS", "PAYMENT PARAM farmerName: " + farmerName);
                            Log.d("HDPS", "PAYMENT PARAM mobileNumber: " + mobileNumber);
                            Log.d("HDPS", "PAYMENT PARAM amount: " + amount);
                            Log.d("HDPS", "PAYMENT PARAM email: " + email);

                            callPayLinkAPI(transactionID, farmerName, mobileNumber, amount, email, jsonRazorPayID);
                        }

                        clearDataAfterSuccess();
                        msclass.showMessage("Data Saved Successfully.");

                    } catch (Exception e) {
                        Log.d("MSG", "DATA : " + e.getMessage());
                    }
                } else if (status.equalsIgnoreCase("false")) {
                    if (userMsg != null || !userMsg.equals(""))
                        msclass.showMessage(userMsg);
                    else
                        msclass.showMessage("Something went wrong, please try later.");
                } else {
                    msclass.showMessage("Something went wrong, please try later.");
                }


                Log.d("Response", resultout);

            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.toString() + "-");

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

    public void handleCouponImageSyncResponse(String function, String resultout, String
            id, String imageName) {
        if (function.equals("mdo_couponSchemeDownloadAndUpload")) {
            if (resultout.contains("True")) {
                mDatabase.updateCouponData(id, imageName, "1", "1");
                //msclass.showMessage("Data Successfully Uploaded");


            } else {
                //   msclass.showMessage(resultout + "mdo_demoModelVisitDetail--E");

            }
        }
        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }


    public void bindRetailer() {
        try {
            spRetailerDetails.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {
                String rttaluka = spTalukaDL.getSelectedItem().toString().trim();
                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname  " +
                        "FROM MDO_tagRetailerList where upper(taluka)='" + rttaluka.trim().toUpperCase() + "' " +
                        "order by name asc  ";

                // String searchQuery = "SELECT distinct mobileno,name,firmname" +
                //  "  FROM MDO_tagRetailerList order by mobileno asc ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                retailerList.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));


                if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(2).toUpperCase() + ", " + cursor.getString(1).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }

                retailerList.add(1, new

                        GeneralMaster("New Retailer (Tag the Retailer)",
                        "New Retailer (Tag the Retailer)"));
                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailerDetails.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }

}

