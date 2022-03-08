package myactvity.mahyco;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

public class DistributorPOGVeg extends AppCompatActivity {

    ProgressDialog pd,rd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty;
    public SearchableSpinner spDist,spState, spVillage, spCropType, spProductName, spMyactvity,spComment;

    private String state;
    MultiSelectionSpinner spdistr,spBussiness;
    RadioButton rndNo,rndYes;
    EditText txtmarketplace, txtrtname
            ,txtyearofexperiance ,txtComments;
    private String dist,taluka;
    ProgressDialog dialog;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spproductlist,spdistributor,spdivision,spsaleorg,spTBM;
    public SearchableSpinner2 spTaluka,spdistributorName,spCompanyName;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org, cmbDistributor,unit,roleid,displayname,TBMCode;
    public Button btnsave,btnCompetitatorPOG,btnPOG;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";

    public String saleorderurl = "";
    LinearLayout my_linear_layout1;

    String  saleorg,customer, name , DLV_plant, customregroup, Distrcode,compcode;
    TextView lbltaluka;
    int  tbmspinnerevent=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_pogveg);
        getSupportActionBar().hide(); //<< this
        context = this;
        cx=new CommonExecution(this);
        TBMCode="";
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        // Inflate the layout for this fragment
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lbltaluka=(TextView)findViewById(R.id.lbltaluka);
        spTBM=(SearchableSpinner)findViewById(R.id.spTBM);
        spDist = (SearchableSpinner)findViewById(R.id.spDist);
        spState= (SearchableSpinner)findViewById(R.id.spState);
        spTaluka= (SearchableSpinner2)findViewById(R.id.spTaluka);
        spCompanyName=(SearchableSpinner2)findViewById(R.id.spCompanyName);
        spdistributorName= (SearchableSpinner2)findViewById(R.id.spdistributorName);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnCompetitatorPOG=(Button)findViewById(R.id.btnCompetitatorPOG);
        btnPOG=(Button)findViewById(R.id.btnPOG);
        txtmarketplace=(EditText)findViewById(R.id.txtmarketplace);
        txtrtname=(EditText)findViewById(R.id.txtrtname);
        rd = new ProgressDialog(context);


        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Utility.setRegularFont(btnsave, this);


        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        unit=pref.getString("unit", null);
        roleid=pref.getString("RoleID", null);
        displayname= pref.getString("Displayname", null);
        BindState();
        // callTBM();
        binddistributor("");
       // bindintialdata();
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
        spTBM .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    tbmspinnerevent=tbmspinnerevent+1;
                    if (tbmspinnerevent>1) {
                        if (!gm.Code().equals("0")) {
                            TBMCode = gm.Code();
                            callDistributorlistAPI(gm.Code().toString());
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    //  Binddistributor(division);


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
                    // BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        btnPOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    GeneralMaster dd = (GeneralMaster) spdistributorName.getSelectedItem();
                    Distrcode=dd.Code();
                    GeneralMaster dcm = (GeneralMaster) spCompanyName.getSelectedItem();
                    compcode=dcm.Code();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("retailermobileno", "81");
                    bundle.putString("DistrCode", Distrcode); // Distributor Code
                    bundle.putString("TBMCode", TBMCode); // Distributor Code
                    bundle.putString("compcode", compcode); // Distributor Code
                    if (validation() == true) {
                        Intent intent = new Intent(context.getApplicationContext(), distributorpogupdateDetailveg.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    GeneralMaster dd = (GeneralMaster) spdistributorName.getSelectedItem();
                    Distrcode=dd.Code();
                    GeneralMaster dcm = (GeneralMaster) spCompanyName.getSelectedItem();
                    compcode=dcm.Code();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("retailermobileno", "81");
                    bundle.putString("DistrCode", Distrcode); // Distributor Code
                    bundle.putString("TBMCode", TBMCode); // Distributor Code
                    bundle.putString("compcode", compcode); // Distributor Code
                    if (validation() == true) {
                        Intent intent = new Intent(context.getApplicationContext(), distributorCompetitatorPOGupdateveg.class);
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
        spdistributorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                    txtmarketplace.setText("");

                    if(!gm.Code().equals("0") ) {
                        String[] str = gm.Desc().trim().toString().split("-");
                        if (str.length > 0) {
                            txtmarketplace.setText(str[1].toString().trim());

                        }
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
    public void BindIntialData()
    {

        try { showwait();
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
    private boolean validation()
    {
        try {
            boolean flag = true;
            GeneralMaster gm = (GeneralMaster) spTBM.getSelectedItem();
            GeneralMaster dd = (GeneralMaster) spdistributorName.getSelectedItem();

           /* if (gm.Code().equals("0")) {
                msclass.showMessage("Please select TBM");
                return false;
            }*/

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

            GeneralMaster dcm = (GeneralMaster) spCompanyName.getSelectedItem();
            compcode=dcm.Code();
            if (dcm.Code().equals("0")) {
                msclass.showMessage("Please select company name ");
                return false;
            }
            if (spdistributorName.getSelectedItem().toString().toLowerCase().equals("select distributor")) {
                msclass.showMessage("Please select distributor");
                return false;
            }

            if (dd.Code().equals("0")) {
                msclass.showMessage("Please select distributor");
                return false;
            }




        }catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return true;
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
            String retailermobileno = bundle.getString("retailermobileno");

            //Log.d(TAG, "onActivityResult: +"+txnAmount);
            for (String key : bundle.keySet()) {
                Log.d("transaction", key + " is a key in the bundle");
            }
        }
    }
    public void  BindRetailer(String taluka)
    {
        try {
            spdistributorName.setAdapter(null);
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();
                if(taluka==null) {
                    taluka="";
                }
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String  searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Retailer'" +
                        " and  upper(taluka)='" + taluka.toUpperCase() + "' order by  RetailerName ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT RETAILER",
                        "SELECT RETAILER"));
                Croplist.add(new GeneralMaster("OTHER",
                        "OTHER"));

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
                spdistributorName.setAdapter(adapter);
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
    public void callTBM()
    {    byte[] objAsBytes = new byte[1];
        if (roleid.contains("4")) //  4 as TBM
        {
            spTBM .setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select TBM Name "));
            Croplist.add(new GeneralMaster(usercode,
                    displayname));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTBM.setAdapter(adapter);
            spTBM.setSelection(1);
            TBMCode=usercode;
            callDistributorlistAPI(usercode);
        }
        else { // OTHER THAN TBM  like RBM
            if (config.NetworkConnection()) {
                // Get TBMLIST
                new callTBMList("1", objAsBytes, usercode, unit).execute();

            } else {
                msclass.showMessage("Please check internet connection");
            }
        }

    }
    public void callDistributorlistAPI(String TBMCode)
    {
        try {
            if (config.NetworkConnection()) {
                // Get TBMLIST

                new callDistributorList("1", TBMCode, unit) .execute();

            } else {
                msclass.showMessage("Please check internet connection");
            }
        }
        catch(Exception  ex)
        {
            msclass.showMessage(ex.toString());
        }
    }
    public void  BindState()
    {

        try {
            showwait();
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("0",
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
  hidewait();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                hidewait();
            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
        hidewait();
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
                Croplist.add(new GeneralMaster("0",
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

        hidewait();
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
        hidewait();
    }

    public void  binddistributor(String dist)
    {
        spdistributorName.setAdapter(null);

        showwait();
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
           hidewait();
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
        hidewait();
    }

    public void BindDistributorList(String str)
    {
        try {
            spdistributorName .setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "SELECT DISTRIBUTOR "));
            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("code"),
                                jObject.getString("distributorname")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // msclass.showMessage("Internet network not available.");
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdistributorName.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            Toast.makeText(context, ex.toString() , Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        }


    }
    public void BindTBMList(String str)
    {
        try {
            spTBM .setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select TBM Name "));
            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("TBMCode"),
                                jObject.getString("TBMName")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // msclass.showMessage("Internet network not available.");
            }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTBM.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            Toast.makeText(context, ex.toString() , Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        }


    }
    public void  BinddistributorSAP(String str)
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
    public class Getdistributor extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,returnstring,action,cmbDistributor;

        public Getdistributor( String action, String usercode,String saleorg,String division,String cmbDistributor  ){
            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.cmbDistributor=cmbDistributor;

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
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            if (action.equals("1")) {
                postParameters.add(new BasicNameValuePair("Type", "GetDistributorlist"));
            }
            if (action.equals("2")) {
                postParameters.add(new BasicNameValuePair("Type", "GetProductList"));
            }
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= saleorderurl+"?usercode="+usercode+"&saleorg="+saleorg+"" +
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
                    // Binddistributor(result);
                }
                if (action.equals("2"))
                {
                    BindProductList(result);
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
    public class callTBMList extends AsyncTask<String, String, String> {

        private String usercode,Crop_Code,cropname,customer,DistrCode,
                returnstring,action,unit;
        byte[] objAsBytes;

        public callTBMList( String action,byte[] objAsBytes, String usercode,String unit  ){
            this.objAsBytes=objAsBytes;
            this.usercode=usercode;
            this.unit=unit;



        }
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            //String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "TBMList"));
            // postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            // postParameters.add(new BasicNameValuePair("TBMCode", TBMCode));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("action", action));

            String Urlpath1= cx.MDOurlpath+"?action="+action+"&usercode="+usercode+"&unit="+unit;
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
                    BindTBMList(result);

                }
                else
                {
                    JSONObject object = new JSONObject(result);
                    String coplist="";

                    JSONArray jArray = object.getJSONArray("Table");
                    JSONObject jObject = jArray.getJSONObject(0);
                    msclass.showMessage(jObject.getString("message").toString() + "Error");
                    // msclass.showMessage(resultout + "Error");


                }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString() +result);
                pd.dismiss();
            }

        }
    }
    public class callDistributorList extends AsyncTask<String, String, String> {

        private String TBMCode,Crop_Code,cropname,customer,DistrCode,
                returnstring,action,unit;
        byte[] objAsBytes;

        public callDistributorList( String action, String TBMCode,String unit  ){

            this.TBMCode=TBMCode;
            this.unit=unit;



        }
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            // String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "DistributorList"));
            postParameters.add(new BasicNameValuePair("TBMCode", TBMCode));
            postParameters.add(new BasicNameValuePair("unit", unit));
            postParameters.add(new BasicNameValuePair("action", action));
            //postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= cx.MDOurlpath;
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
                    BindDistributorList(result);

                }
                else
                {
                    JSONObject object = new JSONObject(result);
                    String coplist="";

                    JSONArray jArray = object.getJSONArray("Table");
                    JSONObject jObject = jArray.getJSONObject(0);
                    msclass.showMessage(jObject.getString("message").toString() + "Error");
                    // msclass.showMessage(resultout + "Error");


                }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString() +result);
                pd.dismiss();
            }

        }
    }


}
