package myactvity.mahyco;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class DistributorAsRetailerPOG extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty;
    public SearchableSpinner spDist,spnewretailer,spState, spVillage, spCropType, spProductName, spMyactvity,spComment;

    private String state;
    MultiSelectionSpinner spdistr,spBussiness;
    RadioButton rndNo,rndYes;
    EditText txtmarketplace, txtrtname,txtAge,txtmobile,txtmobilewhatup,txtBirthdate
            ,txtyearofexperiance ,txtComments,txtfirmname;
    private String dist,taluka;
    ProgressDialog dialog;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    public SearchableSpinner spproductlist,spdistributor,spdivision,spsaleorg;
    public SearchableSpinner2 spTaluka,spdistributorName;
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

    String  saleorg,customer, name , DLV_plant, customregroup,Distrcode,mobileno;
    TextView lbltaluka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_as_retailer_pog);
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
        spdistributorName= (SearchableSpinner2)findViewById(R.id.spdistributorName);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnDownRetailerms=(Button)findViewById(R.id.btnDownRetailerms);
        btnCompetitatorPOG=(Button)findViewById(R.id.btnCompetitatorPOG);
        btnPOG=(Button)findViewById(R.id.btnPOG);
        txtmarketplace=(EditText)findViewById(R.id.txtmarketplace);
        txtrtname=(EditText)findViewById(R.id.txtrtname);

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
        BindState();
        binddistributor("");
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

        spdistributorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                    txtmarketplace.setText("");
                    String[] str=gm.Desc().trim().toString().split("-");
                    if(str.length>0) {
                        txtmarketplace.setText(str[1].toString().trim());

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

        btnPOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    GeneralMaster dd = (GeneralMaster) spdistributorName.getSelectedItem();
                    Distrcode=dd.Code();
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("RetailerMobileno", "81");
                    bundle.putString("DistrCode", Distrcode); // Distributor Code
                    if (validation()==true) {
                        Intent intent = new Intent(context.getApplicationContext(), DistributorAsretailerpogupdateDetail.class);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("state", state);
                    bundle.putString("dist", dist);
                    bundle.putString("taluka", taluka );
                    bundle.putString("mktplace", txtmarketplace.getText().toString());
                    bundle.putString("RetailerMobileno", "81");
                    bundle.putString("DistrCode", Distrcode); // Distributor Code
                    if (validation()==true) {

                        Intent intent = new Intent(context.getApplicationContext(), distributorASRetailerCompetitatorPOGupdate.class);
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
                    if (config.NetworkConnection()) {
                        new SyncDownloadRetailer_Async("MDO_tagRetailerList").execute();
                    }
                    else
                    {
                        msclass.showMessage("Internet connection not available");
                    }

                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });


        //BindIntialData();


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

    private boolean validation()
    {
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
            if (spdistributorName.getSelectedItem().toString().toLowerCase().equals("select distributor")) {
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


}

