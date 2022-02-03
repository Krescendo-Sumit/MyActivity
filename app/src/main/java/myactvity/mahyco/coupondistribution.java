package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class coupondistribution extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty;
    public SearchableSpinner spDist,spnewretailer,spState, spVillage, spCropType,
            spProductName, spMyactvity,spComment;
    EditText txtVillage, txtFarmername, txtAddress, txtCouponcode, txtMobileNo,txtAmount;
    RadioGroup radioGroup;
    public static final String TAG = "farmerRegistration";

    String selectedProduct = "";
    String strFarmerName = "", strUserCode = "", strMobileNo = "", strState = "", strDistrict = "", strTaluka = "", strVillage = "", strAddress = "",
            strCrop = "Cotton", strProduct = "MRC7373", strBookType = "", strCouponCode = "", strAmount = "200";

    private String state;
    MultiSelectionSpinner spdistr,spBussiness;
    RadioButton rndNo,rndYes;

    private String dist,taluka;
    ProgressDialog dialog;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spproductlist,spdistributor,spdivision,spsaleorg;
    public SearchableSpinner2 spTaluka,spdistributorName;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org, cmbDistributor;
    public Button btnsave;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";

    public String saleorderurl = "",croptype;
    LinearLayout my_linear_layout1;

    String  saleorg,customer, name , DLV_plant, customregroup,Distrcode,mobileno;
    TextView lbltaluka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupondistribution);
        getSupportActionBar().hide(); //<< this
        context = this;
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        // Inflate the layout for this fragment
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka=(TextView)findViewById(R.id.lbltaluka);
        msclass = new Messageclass(this);
        spDist = (SearchableSpinner)findViewById(R.id.spDist);
        spState= (SearchableSpinner)findViewById(R.id.spState);
        spTaluka= (SearchableSpinner2)findViewById(R.id.spTaluka);
        spCropType= (SearchableSpinner)findViewById(R.id.spCropType);
        spProductName= (SearchableSpinner)findViewById(R.id.spProductName);
        btnsave=(Button)findViewById(R.id.btnsave);
        txtCouponcode = (EditText) findViewById(R.id.txtCouponcode);
        txtMobileNo = (EditText) findViewById(R.id.etMobileNo);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtFarmername = (EditText) findViewById(R.id.etFarmername);
        txtVillage = (EditText) findViewById(R.id.txtVillage);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        radioGroup = (RadioGroup) findViewById(R.id.radGrp);
         Utility.setRegularFont(btnsave, this);


        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        BindState();
        bindcroptype(spCropType,"C");
       // binddistributor("");
        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (Exception ex)
                {
                    Toast.makeText(coupondistribution.this,ex.toString(),Toast.LENGTH_LONG).show();
                }


                // check = check + 1;
                // if (check > 1)
                {

                    BindProductName(spProductName,croptype,null,1);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(coupondistribution.this, "gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state=gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BindDist(state);
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
                try {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    // binddistributor(dist); // Accodding to district
                    //BindRetailer(dist);
                    BindTaluka(dist);

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
                try {
                    taluka =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check3 = check3 + 1;
                //if (check3 > 1)
                {

                    // binddistributor(taluka);
                    //BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });




        //BindIntialData();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new FarmerRegistrationAPICalling(0, "").execute();
                if(config.NetworkConnection()) {
                    strAddress = txtAddress.getText().toString();
                    strVillage = txtVillage.getText().toString();
                    strFarmerName = txtFarmername.getText().toString();
                    strMobileNo = txtMobileNo.getText().toString();
                    strCouponCode = txtCouponcode.getText().toString();
                    strState = spState.getSelectedItem().toString();
                    strDistrict = spDist.getSelectedItem().toString();
                    strTaluka = spTaluka.getSelectedItem().toString();
                    strCrop = spCropType.getSelectedItem().toString();
                    strProduct = spProductName.getSelectedItem().toString();
                    strAmount = txtAmount.getText().toString();


                    validation();
                }
                else
                {
                    msclass.showMessage(" internet connection not available.");
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult: " + data.getExtras().toString());

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
                && data != null) {
            Bundle bundle = data.getExtras();

            String state = bundle.getString("state");
            String dist = bundle.getString("dist");
            String taluka = bundle.getString("taluka");
            String mktplace = bundle.getString("mktplace");
            String DistrCode = bundle.getString("DistrCode");

            //Log.d(TAG, "onActivityResult: +"+txnAmount);
            for (String key : bundle.keySet()) {
                Log.d("transaction", key + " is a key in the bundle");
            }
        }
    }

    private void BindProductName(Spinner spProductName,String croptype,LinearLayout my_linear_layout1, int flag)
    {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
            cursor.moveToFirst();
            if (flag == 2) {
                my_linear_layout1.removeAllViews();
                // for (int i = 0; i < cursor.getCount(); i++) {
                // JSONObject jObject = jArray.getJSONObject(i);


                while (cursor.isAfterLast() == false) {

                    View view2 = LayoutInflater.from(this).inflate(R.layout.stockrow, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    TextView text = (TextView) view2.findViewById(R.id.text);
                    final EditText txtStock = (EditText) view2.findViewById(R.id.txtStock);
                    final EditText txtsalestock = (EditText) view2.findViewById(R.id.txtsalestock);
                    final  EditText txtCurrentStock = (EditText) view2.findViewById(R.id.txtCurrentStock);
                    txtsalestock.setEnabled(false);
                    //Start

                    txtStock.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }

                                    //double cal = Double.parseDouble(txtGuntha.getText().toString()) * (Double.parseDouble(String.valueOf(s)));
                                    //txtcharges.setText(String.valueOf(cal));
                                    // txtsalestock.setText("");
                                    if (txtCurrentStock.getText().length()!=0)
                                    {
                                        int cont=Integer.valueOf(txtStock.getText().toString())-Integer.valueOf(txtCurrentStock.getText().toString());
                                        txtsalestock.setText(String.valueOf(cont));
                                    }
                                    // txtCurrentStock.setText("");
                                } else {
                                    //txtStock.setText("");
                                    txtsalestock.setText("");
                                    txtCurrentStock.setText("");
                                }
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }


                    });

                    //AREA CALULATION ON MANUAL Entry
                    txtCurrentStock.addTextChangedListener(new TextWatcher() {
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {


                                if (s.length() != 0) { //do your work here }
                                    //String gunthaarea = String.valueOf(Math.round(Double.parseDouble(String.valueOf(s)) * 40.00871459694989));
                                    int  currentst=Integer.parseInt(String.valueOf(s));
                                    int  receivestock=Integer.parseInt(String.valueOf(txtStock.getText()));
                                    if (currentst>receivestock)
                                    {
                                        msclass.showMessage("Current stock should not more than received stock.");
                                        txtCurrentStock.setText("");
                                    }
                                    else
                                    {
                                        txtsalestock.setText(String.valueOf(receivestock-currentst));
                                    }

                                } else {
                                    //txtCurrentStock.setText("");
                                    txtsalestock.setText("");
                                }
                            }
                            catch (Exception ex)
                            {
                                msclass.showMessage(ex.getMessage());
                                ex.printStackTrace();
                            }

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        public void afterTextChanged(Editable s) {

                        }
                    });
                    //end




                    if (croptype.equals("Cotton")) {
                        txtStock.setHint("In Pkts");
                        txtsalestock.setHint("In Pkts");
                        txtCurrentStock.setHint("In Pkts");
                    } else {
                        txtStock.setHint("In Bags");
                        txtsalestock.setHint("In Bags");
                        txtCurrentStock.setHint("In Bags");
                    }
                    text.setText(cursor.getString(0));
                    cursor.moveToNext();
                    my_linear_layout1.addView(view2);
                }
                cursor.close();

                // }
            }
            if (flag == 1) {
                while (cursor.isAfterLast() == false) {

                    Croplist.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProductName.setAdapter(adapter);
                dialog.dismiss();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }



        //en
    }
    private void bindcroptype(Spinner spCropType, String Croptype)
    {
        try {
            //st
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
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

    }
    private boolean validation() {
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
            /*if (txtmarketplace.getText().length() == 0) {
                msclass.showMessage("Please  enter market place .");
                return false;

            }*/

            if (spCropType.getSelectedItem().toString().toLowerCase().equals("select crop")) {
                msclass.showMessage("Please select crop");
                return false;
            }
            if (spProductName.getSelectedItem().toString().toLowerCase().equals("select product")) {
                msclass.showMessage("Please select product");
                return false;
            }
            if (strMobileNo.length()<10) {
                msclass.showMessage("please enter 10 digit mobile number ");
                return false;
            }

            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
            if (radioGroup.getCheckedRadioButtonId() <= 0) {
                // Toast.makeText(coupondistribution.this, "Please select Product Type", Toast.LENGTH_SHORT).show();
                //  msclass.showMessage("Please select Product Type");
            } else {
                selectedProduct = (String) radioButton.getText();
                Log.d("radiobutton: ", selectedProduct);
            }
            Log.d("enter data: ", "vil " + strVillage + "name " + strFarmerName + "mob " + strMobileNo + "coupon " + strCouponCode + "addr " + strAddress + "prod " + selectedProduct);
            if (strVillage.length() == 0 || strFarmerName.length() == 0 || strMobileNo.length() == 0 || strAddress.length() == 0 ||
                    strCouponCode.length() == 0 || radioGroup.getCheckedRadioButtonId() <= 0|| txtAmount.length() ==0) {
                msclass.showMessage("Please Enter All the fields");
                return false;
            } else {
                new FarmerRegistrationAPICalling(0, "").execute();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }

    public void  binddistributor(String dist)
    {
        spdistributorName.setAdapter(null);


        String str= null;
        try {
            String searchQuery="";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;

            //searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
            //        "and  dist='" + dist.toUpperCase() + "' order by  RetailerName ";

            searchQuery = "SELECT distinct RetailerName,code  FROM RetailerMaster where activity='Distributor' " +
                    " order by  RetailerName ";

            // searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' and  taluka='" + spDist.getSelectedItem().toString().trim()+ "' order by  RetailerName ";

            String[] array;
            try {
                Croplist.add(new GeneralMaster("0",
                        "SELECT DISTRIBUTOR"));
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));


                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);

                    try
                    {
                        // String  code=jObject.getString("RetailerName").toString().substring(jObject.getString("RetailerName").toString().indexOf("("),jObject.getString("RetailerName").toString().indexOf(")"));

                        Croplist.add(new GeneralMaster(jObject.getString("code"),
                                jObject.getString("RetailerName")
                        ));

                    }
                    catch (Exception ex) {
                        msclass.showMessage(ex.getMessage()+"\n Please download master data ");
                        break;
                    }

                }

            }
            catch (Exception ex)
            {
                if(ex.getMessage().contains("no such column"))
                {
                    msclass.showMessage("Distributor list not there.Please download master data");
                }
                else {
                    msclass.showMessage(ex.getMessage());
                }
                ex.printStackTrace();

            }



            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<>
                    (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdistributorName.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            if(ex.getMessage().contains("no such column"))
            {
                msclass.showMessage("Distributor list not there.Please download master data");
            }
            else {
                msclass.showMessage(ex.getMessage());
            }
            ex.printStackTrace();
            dialog.dismiss();
        }


        dialog.dismiss();

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

            } catch (Exception e) {
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


    public void  BindTaluka(String dist)
    {
        try {
            spTaluka.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
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
                MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
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
    private  void BindIntialData() {

        try {
            spsaleorg.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "Select Organisation"));
            gm.add(new GeneralMaster("1000", "Mahyco"));
            gm.add(new GeneralMaster("1100", "Sungro"));

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spsaleorg.setAdapter(adapter);

            spdivision.setAdapter(null);
            List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
            gm2.add(new GeneralMaster("0", "Select Division"));
            gm2.add(new GeneralMaster("01", "01-Vegetable Crop"));
            gm2.add(new GeneralMaster("02", "02-Field Crop"));
            gm2.add(new GeneralMaster("03", "03-Cotton Crop"));
            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdivision.setAdapter(adapter2);

            spdistributor.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Distributor "));
            ArrayAdapter<GeneralMaster> adapter3 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdistributor.setAdapter(adapter3);

            spproductlist.setAdapter(null);
            List<GeneralMaster> Croplist4 = new ArrayList<GeneralMaster>();
            Croplist4.add(new GeneralMaster("0",
                    "Select Product List  "));
            ArrayAdapter<GeneralMaster> adapter4 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist4);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spproductlist.setAdapter(adapter4);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    private void addrow()//LinearLayout my_linear_layout1)
    {




        //st
        try {
            int addcount=0;
            for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                View view2 = my_linear_layout1.getChildAt(i);
                EditText txtproduct = (EditText) view2.findViewById(R.id.txtproduct);
                if(txtproduct.getText().toString().contains(spproductlist.getSelectedItem().toString()))
                {
                    addcount=addcount+1;
                }
            }
            if (addcount>0)
            {
                msclass.showMessage("This  product "+spproductlist.getSelectedItem().toString()+" already added in list .");
                txtQty.setText("");
            }
            else {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                int count = my_linear_layout1.getChildCount();
                count = count + 1;
                final View view2 = LayoutInflater.from(this).inflate(R.layout.saleorderrow, null);
                view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                EditText txtproduct = (EditText) view2.findViewById(R.id.txtproduct);
                TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
                TextView txtproducthide = (TextView) view2.findViewById(R.id.txtproducthide);
                txtproduct.setText(String.valueOf(count) + ". " + spproductlist.getSelectedItem().toString());
                GeneralMaster gm = (GeneralMaster) spproductlist.getSelectedItem();
                txtproducthide.setText(gm.Code().toString());
                txtqty.setText(txtQty.getText());
                Button btndelete = (Button) view2.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) view2.getParent()).removeView(view2);
                    }
                });
                my_linear_layout1.addView(view2);
                txtQty.setText("");
                Toast.makeText(context, "Product is added", Toast.LENGTH_SHORT).show();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }



        //en
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
                    boolean  f1 = mDatabase.InsertMDO_tagRetailerList(jArray);
                    if (f1 == true) {
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

    private class FarmerRegistrationAPICalling extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String Offline;
        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(coupondistribution.this);


        public FarmerRegistrationAPICalling(int action, String Offline) {

            this.action = action;
            this.Offline = Offline;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(coupondistribution.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
//            strAddress = txtAddress.getText().toString();
//            strVillage = txtVillage.getText().toString();
//            strFarmerName = etFarmername.getText().toString();
//            strMobileNo = etMobileNo.getText().toString();
//            strCouponCode = txtCouponcode.getText().toString();
//            strState = spState.getSelectedItem().toString();
//            strDistrict = spDist.getSelectedItem().toString();
//            strTaluka = spTerritory.getSelectedItem().toString();
//
//            int radioButtonID = radioGroup.getCheckedRadioButtonId();
//            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
//            selectedProduct = (String) radioButton.getText();
//
//            Log.d("radiobutton: ", selectedProduct);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDO_farmerRegistrationforCoupon"));
            postParameters.add(new BasicNameValuePair("farmername", strFarmerName));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("mobileno", strMobileNo));
            postParameters.add(new BasicNameValuePair("village", strVillage));
            postParameters.add(new BasicNameValuePair("address", strAddress));
            postParameters.add(new BasicNameValuePair("crop", strCrop));
            postParameters.add(new BasicNameValuePair("product", strProduct));
            postParameters.add(new BasicNameValuePair("couponcode", strCouponCode));
            postParameters.add(new BasicNameValuePair("amount", strAmount));
            postParameters.add(new BasicNameValuePair("state", strState));
            postParameters.add(new BasicNameValuePair("district", strDistrict));
            postParameters.add(new BasicNameValuePair("taluka", strTaluka));
            postParameters.add(new BasicNameValuePair("booktype", selectedProduct));

            String Urlpath1 = cx.MDOurlpath + "?action=" + "";
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackground: " + postParameters.toString());
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

                pd.dismiss();
            } catch (Exception e) {

                e.printStackTrace();
                pd.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                Log.d("SyncPaymentStatus", "onPostExecute: " + result);
                pd.dismiss();
                if (result.contains("True") || result.contains("true")) {
                    JSONObject object = new JSONObject(result);

                    msclass.showMessage("Booking Done Succcessfully");
                    clearitem();
                } else {
                    Utility.showAlertDialog("Error", result, context);
                    //  setAnimation();
                }
                //   lblusername.setText(result) ;


            } catch (Exception e) {
                e.printStackTrace();
                Utility.showAlertDialog("Error1", result, context);
                pd.dismiss();
                //  setAnimation();
            }

        }
    }
    public void clearitem()
    {
        txtAddress.setText("");
        txtVillage.setText("");
        txtFarmername.setText("");
        txtMobileNo.setText("");
        txtCouponcode.setText("");
        strState = spState.getSelectedItem().toString();
        strDistrict = spDist.getSelectedItem().toString();
        strTaluka = spTaluka.getSelectedItem().toString();
        strCrop = spCropType.getSelectedItem().toString();
        strProduct = spProductName.getSelectedItem().toString();
        txtAmount.setText("");
        radioGroup.clearCheck();
    }

}

