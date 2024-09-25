package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.UserRegister;
import myactvity.mahyco.Utility;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.Function;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Indentcreate;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.AddPurchaseDetailAdapter;
import myactvity.mahyco.helper.CustomMySpinnerAdapter;
import myactvity.mahyco.helper.FileUtilImage;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.PurchaseDetailModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;

public class Mahakisan extends AppCompatActivity implements
        IPickResult,View.OnClickListener {
    private static final String TAG ="MAHAKISAN" ;
    TextView dtPurchase;
    TextInputLayout tiOther;
    EditText etFarmerName,etMobileNumber,etWhatsappNumber,etQty,etOther;
    SearchableSpinner spState, spDist, spTaluka,  spVillage ,spProductPurchase,spCropType,spRetailer;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    private String state, dist, croptype, taluka,FarmerName, productName;
    List<GeneralMaster> retailerList,stateList, distList, talukaList, vilageList, focussedVillageList, productList, croplist;
    ImageView ivImage;
    ProgressDialog pd;
    public CommonExecution cx;
    String division="",org="", cmbDistributor="";
    int imageselect;
    File photoFile = null;
    public String Imagepath1 = "",userCode;
    private static final String IMAGE_DIRECTORY_NAME = "MAHAKISANPHOTO";
    Button btnFarmerPhoto,btnSubmit,btnAdd;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Prefs mPref;
    private Context context;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    private long mLastClickTime = 0;
    ArrayList<PurchaseDetailModel> purchaseDetailModelArrayList;
    RecyclerView recDemoList;
    LinearLayoutManager layoutManager;
    String  RetailerName,retailerfirmname, customer, name ,
            DLV_plant, customregroup,cmbproductlist,mobileno,retailerCategory,marketplace;
Config config;
    public String SERVER = "";
    private String pkRetailerMobileNumber;
    private String village;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private FusedLocationProviderClient fusedLocationClient;
    double lati;
    double longi;
    String cordinates;
    String address = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahakisan);
        initUI();
        updateLocation();
    }

    private void initUI() {
        context = this;
        cx=new CommonExecution(this);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SERVER = cx.MDOurlpath;

        config = new Config(context); //Here the context is passing
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        mPref = Prefs.with(this);
        tiOther = findViewById(R.id.tiOther);
        etOther = findViewById(R.id.etOther);
        etFarmerName = findViewById(R.id.etFarmerName);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etQty = findViewById(R.id.etQty);
        etWhatsappNumber = findViewById(R.id.etWhatsappNumber);
        dtPurchase = findViewById(R.id.tvSelectedDate);
        spState = (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spRetailer = (SearchableSpinner) findViewById(R.id.spRetailer);
        spCropType =  findViewById(R.id.spCropType);
        spProductPurchase = findViewById(R.id.spProductPurchase);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnFarmerPhoto =  findViewById(R.id.btnFarmerPhoto);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userCode =  pref.getString("UserID", null);
        mPref.save(AppConstant.USER_CODE_TAG, userCode);
        //userCode = mPref.getString(AppConstant.USER_CODE_TAG, "");
       // userCode
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        purchaseDetailModelArrayList = new ArrayList<>();
        recDemoList = (RecyclerView) findViewById(R.id.recDemoList);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recDemoList.setLayoutManager(layoutManager);

        Function.bindState(spState,mDatabase, this,msclass);
        bindcroptype("",this, mDatabase,msclass);
        bindProductName("");
        onFromDateSelected();
        btnFarmerPhoto.setOnClickListener(this);
       // uploadFarmerPhoto();
        if (config.NetworkConnection() ) {
            onSubmitButton();
        }else {
            msclass.showMessage("Please check internet connection  ");
        }
        onAddBtn();
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    hideKeyboard(view);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                  Function.bindDist(state, spDist, mDatabase, getBaseContext(), msclass);
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
                    hideKeyboard(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                    Function.getbindTalukalist(dist,spTaluka,getBaseContext(),mDatabase, msclass);

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
                    hideKeyboard(view);hideKeyboard(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    bindVillage(taluka);
                bindRetailer();
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
                     village =   gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    if(village.equals("OTHER")){
                        //OPEN OPTION TO WRITE
                        tiOther.setVisibility(View.VISIBLE);
                    }else {
                        tiOther.setVisibility(View.GONE);
                    }

                    hideKeyboard(view);


                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                     croptype =   gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    bindProductName(croptype);
                    hideKeyboard(view);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spProductPurchase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                  gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");


                    hideKeyboard(view);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spRetailer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                   String retailerName = gm.toString();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    pkRetailerMobileNumber = gm.Code().trim();
                    
                     if(retailerName.equals("RETAILER DETAILS NOT FOUND")){
                         CallNewRetailerPOP();
                     }


                 

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        /*dtPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });*/

    }

    public void CallNewRetailerPOP()
    {
        try
        {
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_newretailer_popup);
             /* s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L); */
            final LinearLayout tblLayout = (LinearLayout)dialog.findViewById(R.id.li1);
            // TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            //TableRow row = (TableRow)tblLayout.getChildAt(1);
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final EditText txtretailername = (EditText)dialog.findViewById(R.id.txtretailername);
            final EditText txtretailerfirmname = (EditText) dialog.findViewById(R.id.txtretailerfirmname);
            final EditText txtretailermobileno = (EditText)dialog.findViewById(R.id.txtretailermobileno);
            final EditText txtMarketPlace = (EditText)dialog.findViewById(R.id.txtMarketPlace);



            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    spRetailer.setSelection(0);
                }
            });
            String [] array=null;
            array = new String[1];
            array[0]="SELECT PRODUCT";
            retailerfirmname =txtretailerfirmname.getText().toString().trim();
            RetailerName=txtretailername.getText().toString().trim();
            mobileno =txtretailermobileno.getText().toString().trim();



            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        // call Save Retailer Api
                        if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                            msclass.showMessage("Please select state");
                            return ;
                        }
                        if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                            msclass.showMessage("Please select district");
                            return ;
                        }
                        if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                            msclass.showMessage("Please Select Taluka");
                            return ;
                        }
                        if (txtretailername.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer name ");
                            return;
                        }
                        if (txtretailerfirmname.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer firm name ");
                            return;
                        }
                        if (txtretailermobileno.getText().length()==0) {
                            msclass.showMessage("Please  enter retailer mobile number  ");
                            return;
                        }
                        if (txtretailermobileno.getText().length() != 10) {
                            Utility.showAlertDialog("Info", "Please Enter Valid Mobile Number", context);
                            return ;
                        }
                        if (txtMarketPlace.getText().length()==0) {
                            Utility.showAlertDialog("Info", "Please Enter market place", context);
                            return ;
                        }

                        retailerfirmname =txtretailerfirmname.getText().toString().trim();
                        RetailerName=txtretailername.getText().toString().trim();
                        mobileno =txtretailermobileno.getText().toString().trim();
                        marketplace =txtMarketPlace.getText().toString().trim();

                       // uploadNewRetailer();
                        String str= addPOGretaill();
                        if (str.contains("True"))
                        {
                            txtretailername.setText("");
                            txtretailermobileno.setText("");
                            txtretailerfirmname.setText("");
                            txtMarketPlace.setText("");
                            msclass.showMessage("New retailer data saved successfully");
                            dialog.dismiss();
                            BindRetailerList_online(str);
                        }
                        else
                        {
                            msclass.showMessage("Mobile no already exits");
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.toString());

                    }
                }
            });

            dialog.show();
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            //Window window = dialog.getWindow();
            //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        } catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }



    }



    public String  addPOGretaill()
    {
        String  str="";
        if (config.NetworkConnection() )
        {
            try {
                str = new GetRetailerListOnline("2", userCode, org, division, cmbDistributor).execute().get();

            }
            catch (Exception ex )
            {
                str=ex.getMessage();
            }
        }
        else
        {
            str="Please check internet connection";
        }
        return str;
    }

    public void BindRetaileronline(String taluka,String retailerCategory)
    {
        if (config.NetworkConnection() )
        {
            new GetRetailerListOnline("1",userCode,org,division,cmbDistributor).execute();

        }
        else
        {
            msclass.showMessage("Please check internet connection");
        }
    }
    public class GetRetailerListOnline  extends AsyncTask<String, String, String> {

        private String usercode,saleorg="",division="",returnstring,action,cmbDistributor="";

        public GetRetailerListOnline( String action, String usercode,String saleorg,String division,String cmbDistributor  ){
            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.cmbDistributor=cmbDistributor;

        }
        protected void onPreExecute() {

            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SetGetRetailerListOnline"));
            postParameters.add(new BasicNameValuePair("Taluka",taluka));
            postParameters.add(new BasicNameValuePair("Dist",dist));
            postParameters.add(new BasicNameValuePair("State",state));
            postParameters.add(new BasicNameValuePair("retailerName",RetailerName));
            postParameters.add(new BasicNameValuePair("retailerFirmName",retailerfirmname));
            postParameters.add(new BasicNameValuePair("retailerMobileNo",mobileno));
            postParameters.add(new BasicNameValuePair("retailerCategory",retailerCategory));
            postParameters.add(new BasicNameValuePair("usercode",userCode));
            postParameters.add(new BasicNameValuePair("action",action));
            postParameters.add(new BasicNameValuePair("cordinates",""));
            postParameters.add(new BasicNameValuePair("address",""));
            postParameters.add(new BasicNameValuePair("marketplace",marketplace));

//            String Urlpath1=cx.MDOurlpath +"?usercode=97190469"+"&saleorg="+saleorg+"" +
//                    "&division="+division+"&cmbDistributor="+cmbDistributor;

            String Urlpath1=cx.MDOurlpath +"?usercode="+userCode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&cmbDistributor="+cmbDistributor;

            HttpPost httppost = new HttpPost(Urlpath1);
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
                    returnstring= builder.toString();
                }
            }
            catch (UnsupportedEncodingException | ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);

                pd.dismiss();
                // if (result.contains("True")) {
                if (action.equals("1"))
                {
                    BindRetailerList_online(result);
                }
                if (action.equals("2"))
                {
                    if(result.contains("True"))
                    {
                        // msclass.showMessage("New retailer data saved successfully.");
                        // BindRetailerList_online(result);

                    }

                }



            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }


    public void  BindRetailerList_online(String str)
    {
        pd.dismiss();

        try {
            spRetailer.setAdapter(null);


            try {

                boolean res = mDatabase.InsertMDO_NEWRetailer(state.toUpperCase(), "Retailer",userCode,marketplace,
                        dist.toUpperCase(), taluka.toUpperCase(),mobileno,retailerfirmname.toUpperCase() + "(" + mobileno+ ")" ,RetailerName.toUpperCase(),mobileno);
                if(res){
                    retailerList = new ArrayList<GeneralMaster>();

                    String searchQuery = "SELECT distinct mobileno,name,firmname,marketplace " +
                            "FROM MDO_tagRetailerList where taluka='" + taluka.trim().toUpperCase() + "' ";

                    retailerList.add(new GeneralMaster("SELECT RETAILER (BY NAME/ MOBILE NUMBER)",
                            "SELECT RETAILER (BY NAME/ MOBILE NUMBER)"));

                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

                    retailerList.add(1,new GeneralMaster("RETAILER DETAILS NOT FOUND",
                            "RETAILER DETAILS NOT FOUND"));
//
//                retailerList.add(2, new GeneralMaster(etRetailerName.getText().toString().toUpperCase() + "(" + etRetailerMob.getText() +")",
//                        etRetailerName.getText().toString().toUpperCase() + "(" + etRetailerMob.getText() + ")"));

                    if (cursor != null && cursor.getCount() > 0) {
                       // newRetailer.setVisibility(View.GONE);

                        if (cursor.moveToFirst()) {
                            do {
                                retailerList.add(new GeneralMaster(cursor.getString(0),
                                        cursor.getString(1).toUpperCase() + ", " + cursor.getString(2).toUpperCase()+", " + cursor.getString(3).toUpperCase()));
                            } while (cursor.moveToNext());
                        }


                        cursor.close();
                    }


                    CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                            (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRetailer.setAdapter(adapter);
                    spRetailer.setSelection(retailerList.size()-1);

                }



            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
       /* spRetailer.setAdapter(null);

        try {
            spRetailer.setAdapter(null);

            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "SELECT RETAILER"));

            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("mobileno"),jObject.getString("firmname")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msclass.showMessage("Internet network not available.");
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRetailer.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

        */



    }


    private void onAddBtn() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchaseDetailValidation()) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());

                    PurchaseDetailModel purchaseDetailModel = new PurchaseDetailModel();
                    if (purchaseDetailModelArrayList != null) {
                        if (purchaseDetailModelArrayList.size() > 0) {
                            for (int i = 0; i < purchaseDetailModelArrayList.size(); i++) {

                                purchaseDetailModel.setCropType(spCropType.getSelectedItem().toString());
                                purchaseDetailModel.setProductPurchase(spProductPurchase.getSelectedItem().toString());
                                purchaseDetailModel.setQty(etQty.getText().toString());
                                purchaseDetailModel.setUserCode(mPref.getString(AppConstant.USER_CODE_TAG,""));
                                purchaseDetailModel.setEntryDt(Function.getCurrentDate());

                            }
                            purchaseDetailModelArrayList.add(purchaseDetailModel);
                        }else {

                            purchaseDetailModel.setCropType(spCropType.getSelectedItem().toString());
                            purchaseDetailModel.setProductPurchase(spProductPurchase.getSelectedItem().toString());
                            purchaseDetailModel.setUserCode(mPref.getString(AppConstant.USER_CODE_TAG,""));
                            purchaseDetailModel.setQty(etQty.getText().toString());
                            purchaseDetailModel.setEntryDt(Function.getCurrentDate());
                            purchaseDetailModelArrayList.add(purchaseDetailModel);
                        }

                        recDemoList.setLayoutManager(new LinearLayoutManager(context));
                        recDemoList.setAdapter(new AddPurchaseDetailAdapter(context, mDatabase, purchaseDetailModelArrayList));

                    }

                    etQty.setText("");
                    spProductPurchase.setSelection(0);
                    spCropType.setSelection(0);
                    }
                }
        });
    }

    private void onSubmitButton() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Mahakisan.this);

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

        JSONObject mahaKisanDetail = new JSONObject();


        try {

            mahaKisanDetail.put("id", "0");
            mahaKisanDetail.put("UserCode", mPref.getString(AppConstant.USER_CODE_TAG,""));
            mahaKisanDetail.put("farmerName", etFarmerName.getText());
            mahaKisanDetail.put("farmerMobileNumber", etMobileNumber.getText());
            mahaKisanDetail.put("whatsappNumber", etWhatsappNumber.getText());
            mahaKisanDetail.put("purchaseDt", dtPurchase.getText());
            mahaKisanDetail.put("state", spState.getSelectedItem());
            mahaKisanDetail.put("district", spDist.getSelectedItem());
            mahaKisanDetail.put("taluka", spTaluka.getSelectedItem());
            if(!spVillage.getSelectedItem().equals("OTHER")){

                mahaKisanDetail.put("village", spVillage.getSelectedItem());
            }else {
                mahaKisanDetail.put("village", etOther.getText());
            }

            mahaKisanDetail.put("retailerName", spRetailer.getSelectedItem());
            mahaKisanDetail.put("pkRetailerMobileNumber", pkRetailerMobileNumber);

            String  farmerPhotoStatus = "0";
            Date entrydate = new Date();
            final String farmerPhoto;
            farmerPhoto = Imagepath1;
            final String farmerPhotoName = AppConstant.Imagename;// "MahaKisanPhoto" + userCode + String.valueOf(entrydate.getTime());
            mDatabase.getImageDatadetail(farmerPhoto);

            mahaKisanDetail.put("farmerPhotoName", farmerPhotoName);
            mahaKisanDetail.put("farmerPhoto",  mDatabase.getImageDatadetail(farmerPhoto));
            mahaKisanDetail.put("farmerPhotoStatus", farmerPhotoStatus);


            JSONArray jsonArray = new JSONArray();

            for (int i=0; i < purchaseDetailModelArrayList.size(); i++) {
                JSONObject purchase = new JSONObject();// /sub Object

            purchase.put("id", "0");
            purchase.put("mmkID", "0");
            purchase.put("userCode", purchaseDetailModelArrayList.get(i).getUserCode());
            purchase.put("crop", purchaseDetailModelArrayList.get(i).getCropType());
            purchase.put("productPurchase", purchaseDetailModelArrayList.get(i).getProductPurchase());
            purchase.put("qty", purchaseDetailModelArrayList.get(i).getQty());
            purchase.put("entryDt", purchaseDetailModelArrayList.get(i).getEntryDt());


                jsonArray.put(purchase);

                mahaKisanDetail.put("purchaseDetails", jsonArray);

            }
            mahaKisanDetail.put("entryDt",   Function.getCurrentDate() );

            requestParams.put("Table", mahaKisanDetail);


        } catch (JSONException e) {
            e.printStackTrace();
            progressBarVisibility();
        }

        Log.d("mahakisan", requestParams.toString());
        // progressBarVisibility();

       new MahaKisanApiCall("mahakisan", requestParams).execute();


    }


    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class MahaKisanApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public MahaKisanApiCall(String function, JSONObject obj) {

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

                    if (CommonUtil.addGTVActivity(context, "7", "Mahyco Maha kisan", cordinates, etFarmerName.getText()+" "+etMobileNumber.getText(),"GTV","0",0.0)) {
                        // Toast.makeText(context, "Good Going", Toast.LENGTH_SHORT).show();
                    }

                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Mahakisan.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data uploaded Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.refreshActivity(Mahakisan.this);
                                dialog.dismiss();
                                progressBarVisibility();


                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Mahakisan.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Mahakisan.this);
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
     * <P>Method to upload the   data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String uploadData(String function, JSONObject obj) {
        return HttpUtils.POSTJSON(Constants.MAHA_KISAN_SERVER_API,obj,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
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


    public   boolean purchaseDetailValidation()
    {
        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductPurchase.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT PURCHASE")) {
            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
            return false;
        }
        if (etQty.getText().length() == 0) {
            msclass.showMessage("Please enter quantity no");
            return false;
        }else {
          /*  switch (croptype){
                case  "COTTON":
                case  "CASTOR":
                    if(Integer.parseInt(etQty.getText().toString())<10){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                case  "MAIZE":
                    if(Integer.parseInt(etQty.getText().toString())<20){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                    case  "BAJRA":
                    case  "MUSTARD":
                    if(Integer.parseInt(etQty.getText().toString())<7.5){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                    case  "WHEAT":
                    if(Integer.parseInt(etQty.getText().toString())<100){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                    case  "PADDY":
                    if(Integer.parseInt(etQty.getText().toString())<30){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
            }

           */
        }

        return  true;
    }
    public   boolean purchaseDetailValidationEdit(SearchableSpinner spCropType, SearchableSpinner spProductPurchase, EditText etQty, String crop)
    {
        if (spCropType.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select Crop");
            return false;
        }
        if (spProductPurchase.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT PURCHASE")) {
            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
            return false;
        }
        if (etQty.getText().length() == 0) {
            msclass.showMessage("Please enter quantity no");
            return false;
        }
           /* switch (crop){
                case  "COTTON":
                case  "CASTER":
                    if(Integer.parseInt(etQty.getText().toString())<10){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                case  "MAIZE":
                case  "PADDY":
                    if(Integer.parseInt(etQty.getText().toString())<20){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                case  "BAJRA":
                case  "MUSTARD":
                    if(Integer.parseInt(etQty.getText().toString())<7.5){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
                case  "WHEAT":
                    if(Integer.parseInt(etQty.getText().toString())<100){
                        msclass.showMessage("Please enter min crop quantity");
                        return  false;
                    }
                    break;
            }

            */


        return  true;
    }
    private boolean validation() {

        if (etFarmerName.getText().length() == 0) {
            msclass.showMessage("Please enter farmer name");
            return false;
        }
        if (etMobileNumber.getText().length() == 0) {
            msclass.showMessage("Please enter mobile no");
            return false;
        }else if(etMobileNumber.getText().length()<10) {
            msclass.showMessage("Please enter valid mobile no");
            return false;

        }
        if (etWhatsappNumber.getText().length() != 0) {

            if (etWhatsappNumber.getText().length() < 10) {
                msclass.showMessage("Please enter valid mobile no");
                return false;

            }
        }
        if (dtPurchase.getText().length() == 0) {
            msclass.showMessage("Please enter date purchased");
            return false;
        }

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
            msclass.showMessage("Please  enter village name");
            return false;
        }else if(spVillage.getSelectedItem().equals("OTHER")){
            if (etOther.getText().length() == 0) {
            msclass.showMessage("Please enter other village");
            return false;
           }
        }

        if (spRetailer.getSelectedItemPosition() == 0) {
            msclass.showMessage("Please Select retailer name");
            return false;
        }else if(spRetailer.getSelectedItem().equals("RETAILER DETAILS NOT FOUND")){
            msclass.showMessage("Please Select retailer name");
            return false;
        }


        if (purchaseDetailModelArrayList.size() == 0) {
            msclass.showMessage("Please Add Purchase Details");
            return false;
        }else {
            float totalCottonQty =0 ;
            float totalCasterQty =0 ;
            float totalMaizeQty =0 ;
            float totalPaddyQty =0 ;
            float totalWheatQty =0 ;
            float totalBajraQty =0 ;
            float totalMustardQty =0 ;

            for(int i=0; i<purchaseDetailModelArrayList.size();i++){

                String cropName = purchaseDetailModelArrayList.get(i).getCropType();
                String qty = purchaseDetailModelArrayList.get(i).getQty();

                switch (cropName){
                    case  "COTTON":
                    case  "CASTOR":

                        if(cropName.equals("COTTON")){

                            totalCottonQty = Float.parseFloat( qty)+totalCottonQty;

                            break;
                        }else {
                            totalCasterQty = Float.parseFloat( qty)+totalCasterQty;
                            break;
                        }


                    case  "MAIZE":

                        totalMaizeQty = Float.parseFloat( qty)+totalMaizeQty;

                        break;


                    case  "BAJRA":
                    case  "MUSTARD":


                        if(cropName.equals("BAJRA")){

                            totalBajraQty = Float.parseFloat( qty)+totalBajraQty;

                            break;
                        }else {
                            totalMustardQty = Float.parseFloat( qty)+totalMustardQty;
                            break;
                        }

                    case  "WHEAT":
                        totalWheatQty = Float.parseFloat( qty)+totalWheatQty;

                        break;
                    case  "PADDY":
                        totalPaddyQty = Float.parseFloat( qty)+totalPaddyQty;
                        break;
                }



            }
            if(totalCottonQty!=0 && totalCottonQty<10){
                msclass.showMessage("Please enter min cotton crop quantity");
                return  false;
            }
            if( totalCasterQty!=0 &&  totalCasterQty<10){
                msclass.showMessage("Please enter min caster crop quantity");
                return  false;
            }
            if( totalMaizeQty!=0 &&   totalMaizeQty<20){
                msclass.showMessage("Please enter min maize crop quantity");
                return  false;
            }

            if(totalBajraQty!=0 &&  totalBajraQty<7.5){
                msclass.showMessage("Please enter min bajra crop quantity");
                return  false;
            }
            if(totalMustardQty!=0 &&  totalMustardQty<7.5){
                msclass.showMessage("Please enter min mustard crop quantity");
                return  false;
            }
            if(totalWheatQty!=0 &&totalWheatQty<100){
                msclass.showMessage("Please enter min wheat crop quantity");
                return  false;
            }

            if( totalPaddyQty!=0 &&   totalPaddyQty<20){
                msclass.showMessage("Please enter min paddy quantity");
                return  false;
            }


        }






//        if (spCropType.getSelectedItemPosition() == 0) {
//            msclass.showMessage("Please Select Crop");
//            return false;
//        }
//        if (spProductPurchase.getSelectedItem().toString().equalsIgnoreCase("SELECT PRODUCT PURCHASE")) {
//            Utility.showAlertDialog("Info", "Please Select  Product Name", context);
//            return false;
//        }
//        if (etQty.getText().length() == 0) {
//            msclass.showMessage("Please enter quantity no");
//            return false;
//        }

        return  true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void uploadFarmerPhoto() {
        btnFarmerPhoto.setOnClickListener(new View.OnClickListener() {
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
                        selectImage();
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
        });
    }
    private void selectImage() {
        try {
            if (Indentcreate.getPickImageIntent(this) != null) {
                photoFile = Indentcreate.fileobj;
                startActivityForResult(Indentcreate.getPickImageIntent(this), REQUEST_CAMERA);//100
            } else {
                Toast.makeText(this, "Picker intent not found", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "selectImage(): "+ex.toString());
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
            ivImage.setImageBitmap(bm);
        }

    }

    private void onCaptureImageResult(Intent data) {

        try {
            if (imageselect == 1) {

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inJustDecodeBounds = true;
                    options.inSampleSize =2;
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
                    // myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // this only get capture photo
                    //************
                    Date entrydate = new Date();
                    String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
                    AppConstant.queryImageUrl = photoFile.getAbsolutePath();
                    AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));
                    AppConstant.Imagename=this.getClass().getSimpleName()+pref.getString("UserID", null)+String.valueOf(entrydate.getTime()) ;
                    FileUtilImage.compressImageFile( AppConstant.queryImageUrl, AppConstant.imageUri,
                            this,AppConstant.Imagename);
                    // need to set commpress image path
                    Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
                    ivImage.setImageBitmap(myBitmap);
                    //**************
                }
                catch (Exception e) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFarmerPhoto:
                imageselect=1;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
           /* case R.id.btnSuccessStoryPhoto:
                imageselect=2;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.btnPhoto3:
                imageselect=3;
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;*/

        }
    }
    @Override
    public void onPickResult(PickResult r) {

        if (r.getError() == null) {


            if (imageselect == 1)
            {
                ivImage.setImageBitmap(r.getBitmap());
                if (ivImage.getDrawable() != null) {
                    ivImage.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
          /*  if (imageselect == 2)
            {
                ivImage2.setImageBitmap(r.getBitmap());
                if (ivImage2.getDrawable() != null) {
                    ivImage2.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImage2.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }
            if (imageselect == 3)
            {
                ivImagePhoto3.setImageBitmap(r.getBitmap());
                if (ivImagePhoto3.getDrawable() != null) {
                    ivImagePhoto3.setVisibility(View.VISIBLE);
                    //crdImage.setVisibility(View.VISIBLE);
                } else {
                    ivImagePhoto3.setVisibility(View.GONE);
                    // crdImage.setVisibility(View.GONE);
                }
            }*/

            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();
            ////
            AppConstant.queryImageUrl = pathImage;
            AppConstant.imageUri = Uri.fromFile(new File(AppConstant.queryImageUrl));

            if (imageselect == 1) {
                AppConstant.Imagename = "Crop" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename);
                Imagepath1 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
           /* if (imageselect == 2) {
                AppConstant.Imagename = "Cropfrmlist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename2);
                Imagepath2 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }
            if (imageselect == 3) {
                AppConstant.Imagename = "crpfrmrelist" + this.getClass().getSimpleName() + pref.getString("UserID", null) + String.valueOf(entrydate.getTime());
                FileUtilImage.compressImageFile(AppConstant.queryImageUrl, AppConstant.imageUri,
                        this, AppConstant.Imagename3);
                Imagepath3 = FileUtilImage.savefilepath;// photoFile.getAbsolutePath();  old ssave
            }*/

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
  /*  @Override
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
    }*/
    @Override
    protected void onResume() {
        super.onResume();

        if (ivImage.getDrawable() != null) {

            ivImage.setVisibility(View.VISIBLE);


        } else {

            ivImage.setVisibility(View.GONE);

        }

    }
    public  void bindcroptype( String Croptype, Context mContext, SqliteDatabase mDatabase, Messageclass msclass) {
        try {
            spCropType.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT CROP",
                    "SELECT CROP"));

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mContext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public  void bindProductName( String croptype) {
        try {
            spProductPurchase.setAdapter(null);
            List<GeneralMaster> list = new ArrayList<GeneralMaster>();

            String searchQuery = "";
            Cursor cursor;
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName='" + croptype + "'  ORDER BY 'CropName'";


            } else {
                Log.d("Crop type", "First time");
            }

            list.add(new GeneralMaster("SELECT PRODUCT PURCHASE",
                    "SELECT PRODUCT PURCHASE"));
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                list.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProductPurchase.setAdapter(adapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * <P>//bind Village to spinner</P>
     * @param taluka
     */
    public List<GeneralMaster> bindVillage(String taluka) {
        spVillage.setAdapter(null);
        String str = null;
        try {
            String searchQuery = "";
            vilageList = new ArrayList<GeneralMaster>();
            Cursor cursor;
            String tal = taluka;//.substring(0, 1).toUpperCase() + taluka.substring(1).toLowerCase();

            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where upper(taluka)='" + tal.toUpperCase() + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            vilageList.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));
            vilageList.add(1,new GeneralMaster("OTHER",
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
        return vilageList;
    }

    private void onFromDateSelected() {

        dtPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // setDateTimeField(view);
                  final Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                int cmonth = calendar.get(Calendar.MONTH);
                int cday = calendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(Mahakisan.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(0);
                                cal.set(year, month, day, 0, 0, 0);
                                Date chosenDate = cal.getTime();
                                Date myDate = null;

                                DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
                                String strDate = df_medium_uk.format(chosenDate);

                                //  05/07/2020
                                dtPurchase.setText(month+1 + "/" + day  + "/" + year);
                             //  datePicker.setMaxDate(System.currentTimeMillis());

                            }
                        }, cyear, cmonth, cday);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }

        });
    }

    private void setDateTimeField(View v) {
        final TextView txt=(TextView)v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
       // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();
        // txt.setText(dateFormatter.format(dateSelected.getTime()));



        //

    }
    public void bindRetailer() {
        try {
            spRetailer.setAdapter(null);

            String str = null;
            try {

                retailerList = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileno,name,firmname,marketplace  " +
                        "FROM MDO_tagRetailerList where upper(taluka)='" + taluka.trim().toUpperCase() + "' " +
                        "order by name asc  ";

                // String searchQuery = "SELECT distinct mobileno,name,firmname" +
                //  "  FROM MDO_tagRetailerList order by mobileno asc ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                retailerList.add(new GeneralMaster("SELECT RETAILER (BY NAME/ MOBILE NUMBER)",
                        "SELECT RETAILER (BY NAME/ MOBILE NUMBER)"));

                retailerList.add(1,new GeneralMaster("RETAILER DETAILS NOT FOUND",
                        "RETAILER DETAILS NOT FOUND"));

                if (cursor != null && cursor.getCount() > 0) {
                    //newRetailer.setVisibility(View.GONE);

                    if (cursor.moveToFirst()) {
                        do {
                            retailerList.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(1).toUpperCase() + ", " + cursor.getString(2).toUpperCase() +", " + cursor.getString(3).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }


                CustomMySpinnerAdapter<GeneralMaster> adapter = new CustomMySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, retailerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailer.setAdapter(adapter);


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }


    public void hideKeyboard1(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
public void hideKeyboard(View view) {
        if (view != null) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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


}
