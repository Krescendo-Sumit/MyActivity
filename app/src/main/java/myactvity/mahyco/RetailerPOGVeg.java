package myactvity.mahyco;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.firebase.crash.FirebaseCrash;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SearchableSpinner2;
import myactvity.mahyco.helper.SqliteDatabase;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class RetailerPOGVeg extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {

    private static final String TAG ="RetailerPOG" ;
    ProgressDialog pd,rd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty;
    public SearchableSpinner spDist,spnewretailer,spState, spVillage,
            spCropType, spProductName, spMyactvity,spComment;


    MultiSelectionSpinner spdistr,spBussiness;
    RadioButton rndNo,rndYes;
    EditText txtmarketplace, txtrtname,txtAge,txtmobile,txtmobilewhatup,txtBirthdate
            ,txtyearofexperiance ,txtComments,txtfirmname;
    ProgressDialog dialog;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spproductlist,spdistributor,spdivision,spsaleorg;
    public SearchableSpinner2 spTaluka,spRetailerName,spRetailerCategory,spCompanyName;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org, cmbDistributor;
    public Button btnsave,btnCompetitatorPOG,btnPOG,btnDownRetailerms;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";

    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    private String dist,taluka,state;
    String  RetailerName,retailerfirmname, customer, name ,
            DLV_plant, customregroup,cmbproductlist,mobileno,retailerCategory,marketplace,compcode;
    TextView  lbltaluka;

    //For GPs location
    String cordinates;
    String address="";
    String croptype;
    String cordinatesmsg = "ADDRESS TAG : *";
    LinearLayout llOtherVillages;
    File photoFile = null;
    private TextInputLayout tiOtherVillage;
    Location location;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    boolean IsGPSEnabled = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = FusedLocationApi;
    boolean fusedlocationRecieved;
    boolean GpsEnabled;
    int REQUEST_CHECK_SETTINGS = 101;
    private Handler handler = new Handler();
    double lati;
    double longi;
    int check3=0;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_pogveg);
        getSupportActionBar().hide(); //<< this
        context = this;
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        rd = new ProgressDialog(context);
        // Inflate the layout for this fragment
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        dialog = new ProgressDialog(this);
        pd = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka=(TextView)findViewById(R.id.lbltaluka);
        msclass = new Messageclass(this);
        spDist = (SearchableSpinner)findViewById(R.id.spDist);
        spState= (SearchableSpinner)findViewById(R.id.spState);
        spTaluka= (SearchableSpinner2)findViewById(R.id.spTaluka);
        spRetailerCategory= (SearchableSpinner2)findViewById(R.id.spRetailerCategory);
        spRetailerName= (SearchableSpinner2)findViewById(R.id.spRetailerName);
        spCompanyName=(SearchableSpinner2)findViewById(R.id.spCompanyName);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnDownRetailerms=(Button)findViewById(R.id.btnDownRetailerms);
        btnCompetitatorPOG=(Button)findViewById(R.id.btnCompetitatorPOG);
        btnPOG=(Button)findViewById(R.id.btnPOG);
        txtmarketplace=(EditText)findViewById(R.id.txtmarketplace);
        txtrtname=(EditText)findViewById(R.id.txtrtname);
        Utility.setRegularFont(btnsave, this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        taluka="";
        bindcomplist();
        BindRetailerCategory();
        BindIntialData();
        BindState();
        address="";
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state=gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if(!(gm.Code().equals("SELECT STATE"))) {
                        BindDist(state);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try
                {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    if(!(gm.Code().equals("SELECT DISTRICT"))) {
                        BindTaluka(dist);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    // binddistributor(dist); // Accodding to district
                    //BindRetailer(dist);


                }
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
                // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                // BindRetailerCategory();
                //BindIntialData();
                try {
                    taluka =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    if (!gm.Code().equals("0")) {
                        BindRetaileronline(taluka, retailerCategory);
                    }
                   // spRetailerCategory.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                check3 = check3 + 1;
                if (check3 > 1)
                {
                   // BindRetaileronline(taluka, retailerCategory);
                    //BindRetailer(taluka);
                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        spRetailerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                    mobileno =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    String[] distr=gm.Desc().toString().split("\\|");
                    if (distr.length==3) {
                        txtmarketplace.setText(distr[2].toString());
                        txtmarketplace.setEnabled(false);
                    }
                   /* JSONObject object = new JSONObject();
                    String searchQuery2 = "select * from MDO_tagRetailerList where mobileno ='"+mobileno+"' ";
                    object.put("Table1", mDatabase.getResults( searchQuery2));
                    JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);*/

                    if (spRetailerName.getSelectedItem().toString().toUpperCase().contains("ADD NEW RETAILER") )
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RetailerPOGVeg.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("My Activity App");
                        // Setting Dialog Message
                        alertDialog.setMessage("ADD NEW RETAILER FOR POG . ");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                if (validationnotfound()!=true)
                                {
                                    return ;
                                }
                                CallNewRetailerPOP();
                              /*  Bundle bundle = new Bundle();
                                bundle.putString("state", state);
                                bundle.putString("dist", dist);
                                bundle.putString("taluka", taluka );
                                bundle.putString("mktplace", txtmarketplace.getText().toString());
                                bundle.putString("From", "RetailerPOG");

                                Intent intent= new Intent(context.getApplicationContext(),RetailerandDistributorTag.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.putExtras(bundle);
                                startActivityForResult(intent, 2);*/
                            }


                        });

                       /* alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();

                            }
                        }); */

                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.weight = 10;
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);
                        //end




                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                {


                    // BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                   /* GeneralMaster sp = (GeneralMaster) spsaleorg.getSelectedItem();
                    if (sp.Code().equals("0"))
                    {
                        msclass.showMessage("Please select organisation.") ;
                        return ;
                    }*/




                    if (config.NetworkConnection())
                    {
                        GeneralMaster gm = (GeneralMaster) spsaleorg.getSelectedItem();
                        GeneralMaster gd = (GeneralMaster) spdivision.getSelectedItem();
                        GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                        //GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();

                        division = URLEncoder.encode(gd.Code(), "UTF-8");
                        org=URLEncoder.encode(gm.Code(), "UTF-8");

                        String[] distr=cmbDistributor.split("\\|");
                        customer=distr[0].toString();
                        name=URLEncoder.encode(distr[1].toString(), "UTF-8");
                        DLV_plant=distr[2].toString();
                        customregroup=distr[3].toString();
                        JSONObject mainObj = new JSONObject();
                        JSONArray ja = new JSONArray();
                        String Comments="";

                        for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                            View view2 = my_linear_layout1.getChildAt(i);
                            EditText txtproduct = (EditText) view2.findViewById(R.id.txtproduct);
                            TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
                            TextView txtproducthide=(TextView) view2.findViewById(R.id.txtproducthide);
                            //txtproducthide.setText(gm.Code().toString());
                            //txtqty.setText( txtQty.getText());

                            cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                            String[] prdarray=cmbproductlist.split("\\|");
                            String MATNR=prdarray[0].toString();
                            String MAKTX=prdarray[1].toString();
                            String MTART=prdarray[2].toString();
                            String MATKL=prdarray[3].toString();
                            String QTYPKT=txtqty.getText().toString();
                            String PKTSIZE="";
                            if(division.equals("03"))
                            {
                                MATNR=String.valueOf(Long.valueOf(MATNR));
                            }
                            JSONObject jo = new JSONObject();
                            jo.put("MATNR", MATNR);
                            jo.put("MAKTX", MAKTX);
                            jo.put("MTART", MTART);
                            jo.put("MATKL", MATKL);
                            jo.put("QTYPKT", QTYPKT);
                            jo.put("PKTSIZE", PKTSIZE);

                            ja.put(jo);
                        }
                        mainObj.put("Table1", ja);
                        byte[] objAsBytes = null;//new byte[10000];
                        try {
                            objAsBytes = mainObj.toString().getBytes("UTF-8");
                            Comments=URLEncoder.encode(txtComments.getText().toString(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        new Saleorderdata("1",objAsBytes,usercode,org,division,customer,name,DLV_plant,customregroup,Comments).execute();

                    }
                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });

        btnPOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    GeneralMaster dd = (GeneralMaster) spRetailerName .getSelectedItem();
                    GeneralMaster rc = (GeneralMaster) spRetailerCategory .getSelectedItem();
                    GeneralMaster dcm = (GeneralMaster) spCompanyName.getSelectedItem();
                    compcode=dcm.Code();
                    mobileno=dd.Code();
                    RetailerName=dd.Desc();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("RetailerMobileno", mobileno);
                    bundle.putString("RetailerName", RetailerName);
                    bundle.putString("RetailerType", rc.Desc());
                    bundle.putString("compcode", compcode);


                    if (validation()==true) {
                        Intent intent = new Intent(context.getApplicationContext(), retailerpogupdateDetailVeg.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }


                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });
        btnCompetitatorPOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    GeneralMaster dd = (GeneralMaster) spRetailerName.getSelectedItem();
                    GeneralMaster rc = (GeneralMaster) spRetailerCategory .getSelectedItem();
                    RetailerName=dd.Desc();
                    mobileno=dd.Code();

                    GeneralMaster dcm = (GeneralMaster) spCompanyName.getSelectedItem();
                    compcode=dcm.Code();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("RetailerMobileno", mobileno);
                    bundle.putString("RetailerName", RetailerName);
                    bundle.putString("RetailerType", rc.Desc());
                    bundle.putString("compcode", compcode);
                    if (validation()==true) {

                        Intent intent = new Intent(context.getApplicationContext(), retailerCompetitatorPOGupdateveg.class);
                        // Intent intent = new Intent(context.getApplicationContext(), tablelayouttest.class);

                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //context.startActivity(intent);

                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });
        btnDownRetailerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (config.NetworkConnection())
                    {
                        String searchQuery = "select  *  from mdo_Retaileranddistributordata where Status='0'";
                        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if(count>0)
                        {
                            msclass.showMessage("Before download tag retailer list ,Please upload the pending retailer taging data.");
                        }
                        else {

                            new SyncDownloadRetailer_Async("MDO_tagRetailerList").execute();
                        }
                    }

                    else {
                        msclass.showMessage("Internet connection not available");
                    }
                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Log.i("Coordinates",cordinates);
                    address = getCompleteAddressString(lati, longi);
                    //      Toast.makeText(context, "Location Latitude : " + location.getLatitude() + " Longitude :" + location.getLongitude()+" Hello :" +address, Toast.LENGTH_SHORT).show();
                    //  edGeoTagging.setText(location.getLatitude() + "," + location.getLongitude());
                }
            }
        });


        BindIntialData();


    }
    public void showwait()
    {
        rd.setTitle("Wait ...");
        rd.setMessage("Please wait.");
        rd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        rd.show();
    }
    public void hidewait()
    {
        rd.dismiss();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult: " + data.getExtras().toString());
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK
                    && data != null) {
                Bundle bundle = data.getExtras();
                String state = bundle.getString("state");
                String dist = bundle.getString("dist");
                String taluka = bundle.getString("taluka");
                String mktplace = bundle.getString("mktplace");
                String retailermobileno = bundle.getString("retailermobileno");


                //Log.d(TAG, "onActivityResult: +"+txnAmount);
                for (String key : bundle.keySet()) {
                    Log.d("transaction", key + " is a key in the bundle");
                }
            }

            if (requestCode == 2 ){
                BindRetailer(taluka.trim());
            }
        }
        catch(Exception ex)
        {

        }
    }


    public void CallNewRetailerPOP()
    {
        try
        {
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.pognewretailerpopup);
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
                        String str= addPOGretaill();
                        if (str.contains("True"))
                        {
                            txtretailername.setText("");
                            txtretailermobileno.setText("");
                            txtretailerfirmname.setText("");
                            txtMarketPlace.setText("");
                            msclass.showMessage("New retailer data saved successfully");
                            BindRetailerList_online(str);
                        }
                        else
                        {
                            msclass.showMessage(str.replace("\"",""));
                        }
                       /* final String status = spstatus.getSelectedItem().toString();
                        int count = 0;
                        String product = "";
                        if (status.equals("SELECT STATUS")) {
                            msclass.showMessage("Please select stock  status");
                            return;
                        }
                        if (txtqty.getText().length()==0) {
                            msclass.showMessage("Please  enter qty");
                            return;
                        }


                        if (status.contains("SALES RETURN") )
                        {
                            if (Integer.valueOf(txtqty.getText().toString())>Integer.valueOf(txtbalance.getText().toString()))
                            {
                                msclass.showMessage("Sales return ,not allow to more than balance qty.");
                                return;
                            }
                        }
                        if (txtdate.getText().length()==0) {
                            msclass.showMessage("Please  enter date");
                            return;
                        }
                        if (spdistr.getSelectedStrings().contains("SELECT DISTRIBUTOR")) {
                            msclass.showMessage("Please  enter distributor");
                            return;
                        }
                        */
                        //St

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        // Date d = new Date();
                        //final String strdate = dateFormat.format(d);




                        //ene

                       /* if (count > 0) {
                            msclass.showMessage("product detail added successfully.");
                            dialog.dismiss();

                        } else {
                            msclass.showMessage("Please enter product details");
                        }
  */

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
                str = new GetRetailerListOnline("2", usercode, org, division, cmbDistributor).execute().get();

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
    private boolean validation()
    {
        try {
            boolean flag = true;
           /* if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                msclass.showMessage("Please select state");
                return false;
            }
            if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                msclass.showMessage("Please select district");
                return false;
            }
            if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }


            GeneralMaster rc = (GeneralMaster) spRetailerCategory .getSelectedItem();
            if (rc.Code().toLowerCase().equals("0")) {
                msclass.showMessage("Please select retailer  category.");
                return false;
            }*/
            GeneralMaster rr = (GeneralMaster) spRetailerName .getSelectedItem();

            if (validationnotfound()!=true)
            {
                return false;
            }
            if (spRetailerName.getSelectedItem().toString().toLowerCase().equals("select retailer")) {
                msclass.showMessage("Please select retailer");
                return false;
            }
            if(spRetailerName.getSelectedItem().toString().toLowerCase().contains("not found")||
                    spRetailerName.getSelectedItem().toString().toLowerCase().contains("tag retailer") ||
                    rr.Code().toLowerCase().equals("0")) {
                msclass.showMessage("Please select retailer");
                return false;
            }



        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }
    private boolean validationnotfound() {
        try {
            boolean flag = true;
            if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                msclass.showMessage("Please select state");
                return false;
            }
            if (spDist.getSelectedItem().toString().toLowerCase().equals("select district")) {
                msclass.showMessage("Please select district");
                return false;
            }
            if (spTaluka.getSelectedItem().toString().toLowerCase().equals("select taluka")) {
                msclass.showMessage("Please Select Taluka");
                return false;
            }


            GeneralMaster rc = (GeneralMaster) spRetailerCategory .getSelectedItem();
            GeneralMaster rr = (GeneralMaster) spRetailerName .getSelectedItem();




        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }
    public void  BindRetailer(String taluka)
    {
        try {
            spRetailerName.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();
                if(taluka==null) {
                    taluka="";
                }
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                //String  searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster " +
                //        "where activity='Retailer'" +
                //      " and  upper(taluka)='" + taluka.toUpperCase() + "' order by  RetailerName ";

                String  searchQuery = "SELECT distinct mobileno,firmname as RetailerName  FROM MDO_tagRetailerList " +
                        "where  upper(taluka)='" + taluka.toUpperCase() + "' order by  firmname ";



                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));
                Croplist.add(new GeneralMaster("0",
                        "TAG RETAILER (If not tagged earlier)"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(1).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailerName.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    public void  BindState()
    {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state  FROM VillageLevelMaster order by state asc  ";
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
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    public void  BindDist(String state)
    {
        try {
            showwait();
            spDist.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state='"+state+"' order by district asc  ";
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
             hidewait();
            } catch (Exception e)
            {
                e.printStackTrace();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }




    public void  BindRetailerCategory()
    {
        try {
            spRetailerCategory.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                Croplist.add(new GeneralMaster("0",
                        "SELECT RETAILER CATEGORY"));
                Croplist.add(new GeneralMaster("FOCUS RETAILER",
                        "FOCUS RETAILER"));
                Croplist.add(new GeneralMaster("OTHER RETAILER",
                        "OTHER RETAILER"));
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRetailerCategory.setAdapter(adapter);
                dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    public void  BindTaluka(String dist)
    {
        try {
            showwait();
            spTaluka.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("0",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                dialog.dismiss();
hidewait();

            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    private void bindcomplist()
    {
        try {
            showwait();
            spCompanyName .setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "SELECT COMPANY NAME "));
            if (config.NetworkConnection()) {
                try {

                    Croplist.add(new GeneralMaster("1000",
                            "MAHYCO"));
                    Croplist.add(new GeneralMaster("1100",
                            "SUNGRO"));

                }  catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // msclass.showMessage("Internet network not available.");
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCompanyName.setAdapter(adapter);
            hidewait();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, ex.toString() , Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        }
    }
    private  void BindIntialData() {

        try {

            //spRetailerCategory.setSelection(0);
           /* spRetailerName.setAdapter(null);
            List<GeneralMaster> Croplist4 = new ArrayList<GeneralMaster>();
            Croplist4.add(new GeneralMaster("0",
                    "SELECT RETAILER"));
            ArrayAdapter<GeneralMaster> adapter4 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist4);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRetailerName.setAdapter(adapter4);
  */

            showwait();
            spRetailerName.setAdapter(null);

            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "SELECT RETAILER"));
            Croplist.add(new GeneralMaster("ADD NEW RETAILER",
                    "ADD NEW RETAILER"));

            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRetailerName.setAdapter(adapter);
 hidewait();
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public void  Binddistributor(String str)
    {
        try {
            spdistributor.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Distributor "));
            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("CUSTOMER")+"|"+jObject.getString("NAME")+"-"+jObject.getString("CITY")+"|"+jObject.getString("DLV_PLANT")+"|"+jObject.getString("GROUP"),
                                jObject.getString("NAME")+"-"+jObject.getString("CITY")+"\n"+jObject.getString("CUSTOMER")
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
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdistributor.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }
    public void  BindProductList(String str)
    {
        try {
            spproductlist.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Product "));

            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("MATNR")+"|"+jObject.getString("MAKTX")+"|"+jObject.getString("MTART")+"|"+jObject.getString("MATKL"),
                                jObject.getString("MAKTX")+"\n"+jObject.getString("MATNR")
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
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spproductlist.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }
    public void  BindRetailerList_online(String str)
    {
        pd.dismiss();
        showwait();
        spRetailerName.setAdapter(null);

        try {
            spRetailerName.setAdapter(null);

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

                    Croplist.add(new GeneralMaster("ADD NEW RETAILER",
                            "ADD NEW RETAILER"));
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
            spRetailerName.setAdapter(adapter);
         hidewait();
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            hidewait();
        }



    }
    public void BindRetaileronline(String taluka,String retailerCategory)
    {
        if (config.NetworkConnection() )
        {
            new GetRetailerListOnline("1",usercode,org,division,cmbDistributor).execute();

        }
        else
        {
            msclass.showMessage("Please check internet connection");
        }
    }
    public class GetRetailerListOnline  extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,returnstring,action,cmbDistributor;

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
            postParameters.add(new BasicNameValuePair("Type", "VegSetGetRetailerListOnline"));
            postParameters.add(new BasicNameValuePair("Taluka",taluka));
            postParameters.add(new BasicNameValuePair("Dist",dist));
            postParameters.add(new BasicNameValuePair("State",state));
            postParameters.add(new BasicNameValuePair("retailerName",RetailerName));
            postParameters.add(new BasicNameValuePair("retailerFirmName",retailerfirmname));
            postParameters.add(new BasicNameValuePair("retailerMobileNo",mobileno));
            postParameters.add(new BasicNameValuePair("retailerCategory",retailerCategory));
            postParameters.add(new BasicNameValuePair("usercode",usercode));
            postParameters.add(new BasicNameValuePair("action",action));
            postParameters.add(new BasicNameValuePair("cordinates",cordinates));
            postParameters.add(new BasicNameValuePair("address",address));
            postParameters.add(new BasicNameValuePair("marketplace",marketplace));

            String Urlpath1=cx.MDOurlpath +"?usercode="+usercode+"&saleorg="+saleorg+"" +
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
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (Exception e) {
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
    public class Saleorderdata extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,Comments;
        byte[] objAsBytes;

        public Saleorderdata( String action,byte[] objAsBytes, String usercode,String saleorg,
                              String division,String customer,String name ,String DLV_plant,
                              String customregroup ,String Comments  ){
            this.objAsBytes=objAsBytes;

            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.customregroup=customregroup;
            this.customer=customer;
            this.DLV_plant=DLV_plant;
            this.name=name;
            this.Comments=Comments;


        }
        protected void onPreExecute() {
            // pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SaleOrder"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= saleorderurl+"?orderid="+""+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&customer="+customer+"" +
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"&name="+name+"&Comments="+Comments+"";
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
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (Exception e) {
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
                if (result.contains("True")) {
                    msclass.showMessage("Order are submitted successully");
                    my_linear_layout1.removeAllViews();
                    txtComments.setText("");
                }
                else
                {
                    msclass.showMessage(result);

                }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

    public synchronized String  syncRetailerdata(String Funname,String usercode,String urls) {

        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", Funname));
        String Urlpath=urls+"?UserCode="+usercode+"";
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
        return builder.toString();
    }

    public String  downloadTagRetailerdata()
    { String returnvalue="";
        returnvalue= syncRetailerdata("MDO_tagRetailerList", pref.getString("UserID", null),cx.MDOurlpath);
        return returnvalue;
    }
    class SyncDownloadRetailer_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        String returnvalue;
        ProgressDialog progressDialog;
        public SyncDownloadRetailer_Async(String tag) {
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

            returnvalue=downloadTagRetailerdata();
            return returnvalue;
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {

            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();

                }

                if (result.contains("False") || result.length()==0)
                {
                    Toast.makeText(context, result,
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mDatabase.deleledata("MDO_tagRetailerList"," ") ;
                    JSONObject object = new JSONObject(result);
                    JSONArray jArray = object.getJSONArray("Table");
                    JSONArray jArray2 = object.getJSONArray("Table1");

                    boolean  f1 = mDatabase.InsertMDO_tagRetailerList(jArray);
                    if (f1 == true) {
                        if(jArray2.length()>0) {
                            mDatabase.deleledata("mdo_pogsapdata"," ");
                            f1 = mDatabase.insertmdopogsapdata(jArray2);
                        }
                        Toast.makeText(context, "Tag Retailer data  Downloaded successfully.",
                                Toast.LENGTH_SHORT).show();
                    }


                }




            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context, ex.toString(),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();


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

        if (handler != null) {
            handler = null;
        }
        try {
            stopFusedApi();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        try {

         //   startFusedLocationService();

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", "Funtion name :onresume" + ex.getMessage(), context);
        }
    }
    private void manageGeoTag() {
        if (cordinates != null && !cordinates.contains("null")) {


        //    startFusedLocationService();


        } else {
            Utility.showAlertDialog("Info", "Please wait fetching location", context);
          //  startFusedLocationService();
        }
    }

    //Stop location fuseApi
    public void stopFusedApi() {
        try {
            if (googleApiClient != null && (googleApiClient.isConnected())) {
                FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationListener) this);
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

    //fetch address from cordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
                strAdd=addresses.get(0).getAddressLine(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My", "Canont get Address!");
        }
        return strAdd;
    }


    //start fusedApi location
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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RetailerPOGVeg .this);

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

                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();
            }
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
                    fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
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


    public int getLocationMode() {
        try {
            return Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
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
           if(address.trim().equals("")) {
               address = getCompleteAddressString(lati, longi);
           }else
           {
               Log.d("Address 123",""+address);
           }
            Log.d(TAG, "onlocation" + cordinates);
            manageGeoTag();

        } catch (Exception e) {
            Log.d(TAG, "onLocationChanged: " + e.toString());
            e.printStackTrace();
            //  }
        }

    }


}
