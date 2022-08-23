package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.DecimalDigitsInputFilter;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class retailerpogupdateDetailVeg extends AppCompatActivity {
    ProgressDialog pd;

    public SearchableSpinner  spCropType;
    private SqliteDatabase mDatabase;
    Context context;
    public Messageclass msclass;
    Button btnsave;
    EditText txtpridate;
    private SimpleDateFormat dateFormatter;
    LinearLayout my_linear_layout1;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    String croptype,Crop_Code;
    Config config;
    TextView txtDescription;
    String state,dist,taluka,mktplace,RetailerMobileno,RetailerName,RetailerType,compcode;
    private boolean listenForChanges = true;
    String strdate,usercode;
    int check=0;
    public CommonExecution cx;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailerpogupdate_detail_veg);
        getSupportActionBar().hide(); //<< this
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        btnsave= (Button)findViewById(R.id.btnsave);
        my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        context=this;
        usercode=pref.getString("UserID", null);
        cx=new CommonExecution(this);

        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container2 = (ScrollView) findViewById(R.id.container2);
        txtpridate=(EditText)findViewById(R.id.txtpridate);
        txtpridate.setEnabled(false);
        txtDescription=(TextView)findViewById(R.id.txtDescription);
        Date dt = new Date();
        strdate = dateFormatter.format(dt);

        txtpridate.setText(strdate.toString());
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        state=bundle.getString("state");
        dist=bundle.getString("dist");
        taluka=bundle.getString("taluka");
        mktplace=bundle.getString("mktplace");
        RetailerMobileno=bundle.getString("RetailerMobileno");
        RetailerType=bundle.getString("RetailerType");
        RetailerName=bundle.getString("RetailerName");
        compcode=bundle.getString("compcode");
        String htmstring2 ="<p> <b> <font color=#FF0000>RCVD STOCK :</font></b> RECEIVED STOCK  TILL DATE <br/> \n" +
                "<b><font color=#FF0000>SHIFTED STOCK :</font></b>SHIFTED / SOLD TO OTHERS  <br/>\n" +
                "<b><font color=#FF0000> SOLD STOCK- :</font></b>SOLD TO FARMERS <br/> \n" +
                "<b><font color=#FF0000> BAL STOCK- :</font></b>BALANCE STOCK <br/> \n</p>";

        String pkt = "<font color='#EE0000'>(pkt)</font>";
        String kg = "<font color='#EE0000'>(kg)</font>";
        String htmstring ="<p> <b> NOTE-Cummulative stock details to be recorded<br/>" +
                "Stock Details for cotton should be recorded in " +pkt+"<br/> \n" +
                "Stock Details for other crop should be recorded in " +kg+"<br/> \n" +
                "\n</p>";
        htmstring="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDescription.setText(Html.fromHtml(htmstring,Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            txtDescription.setText(Html.fromHtml(htmstring));
        }
        //txtDescription.setText("REC S-RECEIVED STOCK  TILL DATE,SHIFT S-SHIFTED / SOLD TO OTHERS" +
        //       "\n NET S-NET STOCK ,SOLD S-SOLD TO FARMERS,BAL S-BALANCE STOCK\n");
        BindCROPMaster();
        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = gm.Desc().trim();//URLEncoder.encode(gm.Desc().trim(), "UTF-8");
                    Crop_Code=gm.Code().trim();
                }
                catch (Exception ex)
                {
                    Toast.makeText(retailerpogupdateDetailVeg.this,ex.toString(),Toast.LENGTH_LONG).show();
                }


                check = check + 1;
                if (check > 1)
                {
                    //updatePOG(Crop_Code,croptype);
                    JSONObject mainObj = new JSONObject();
                    byte[] objAsBytes = null;//new byte[10000];
                    String cropcode="-";
                    String cropname="";
                    try {
                        objAsBytes = cropcode.getBytes("UTF-8");
                        cropcode=URLEncoder.encode(Crop_Code, "UTF-8");
                        cropname=URLEncoder.encode(croptype, "UTF-8");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(config.NetworkConnection()) {
                        if(Crop_Code.contains("Select"))
                        {
                            Toast.makeText(context, "Please Select the crop.", Toast.LENGTH_SHORT).show();
                        }else {
                            new UpdatedPOG("2", objAsBytes, usercode, Crop_Code,
                                    cropname, RetailerMobileno).execute();
                        }
                    }
                    else
                    {
                        msclass.showMessage("Please check internet connection");
                    }



                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(retailerpogupdateDetailVeg.this, "gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        txtpridate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String crop = spCropType.getSelectedItem().toString();
                    int count = 0;
                    String product = "";
                    if (crop.equals("SELECT CROP")) {
                        msclass.showMessage("Please select crop");
                        return;
                    }
                    String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
                    Pattern pattern = Pattern.compile(regex);
                    if(txtpridate.getText().length() ==0)
                    {
                        msclass.showMessage("Please enter POG Date ");
                        return;
                    }
                    if (txtpridate.getText().length()>0)
                    {
                        Matcher matcher = pattern.matcher(txtpridate.getText().toString());
                        if (matcher.matches())
                        {

                        }
                        else
                        {
                            msclass.showMessage("Please enter POG DD/MM/YYYY format date.. ");
                            return ;
                        }
                        //Date javaDate = dateFormatter.parse(txtBirthdate.getText().toString());
                    }

                    UpdateRetailerPOG();






                } catch (Exception ex) {
                    ex.printStackTrace();
                    msclass.showMessage(ex.toString());

                }
            }
        });
    }
    private void setDateTimeField(View v) {
        final EditText txt=(EditText)v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // view.setMinDate(System.currentTimeMillis() - 1000);
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();
        // txt.setText(dateFormatter.format(dateSelected.getTime()));


    }


    public void UpdateRetailerPOG()
    {
        try {
            final String crop = spCropType.getSelectedItem().toString().trim();
            GeneralMaster gm = (GeneralMaster) spCropType.getSelectedItem();
            Crop_Code=gm.Code();
            //St
            int count=0;

            //int countrow= mDatabase.getrowcount("select * from RetailerPOGTable where" +
            //        " RetailerMobileno='"+RetailerMobileno+"' and CropName ='"+crop+"'");
            int countrow= mDatabase.getrowcount("select * from RetailerPOGTable where" +
                    " RetailerMobileno='"+RetailerMobileno+"' and CropCode ='"+Crop_Code+"'");
            if (countrow>0)
            {
                // mDatabase.deleterecord("delete from RetailerPOGTable where" +
                //        " RetailerMobileno='"+RetailerMobileno+"' and CropName ='"+crop+"'");
                mDatabase.deleterecord("delete from RetailerPOGTable where" +
                        " RetailerMobileno='"+RetailerMobileno+"' and CropCode ='"+Crop_Code+"'");
            }


            JSONObject mainObj = new JSONObject();
            JSONArray ja = new JSONArray();
            for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
                View view2 = my_linear_layout1.getChildAt(i);

                final EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                final EditText lblsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                final TextView txtprdname1 = (TextView) view2.findViewById(R.id.txtprdname);
                final  EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);
                final TextView txtprdcode1 = (TextView) view2.findViewById(R.id.txtprdcode);
                // dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date d = new Date();
                final String strdate = dateFormat.format(d);
                //if(lblreceived1.getText().length()>1 &&
                       // lblbal1.getText().length()!=0
                if(lblreceived1.getText().length()>1 &&
                        !TextUtils.isEmpty(lblreceived1.getText().toString())
               && Double.valueOf(lblreceived1.getText().toString())>0)
                {
                    JSONObject jo = new JSONObject();
                    jo.put("usercode", usercode);
                    jo.put("state", state);
                    jo.put("dist", dist);
                    jo.put("Taluka", taluka);
                    jo.put("MktPlace", mktplace);
                    jo.put("RetailerName", RetailerName);
                    jo.put("RetailerMobileno", RetailerMobileno);
                    jo.put("CropName", crop.trim());
                    jo.put("ProductName", txtprdname1.getText().toString());
                    jo.put("CropCode", Crop_Code);
                    jo.put("ProductCode", txtprdcode1.getText().toString());
                    jo.put("RecStock", lblreceived1.getText().toString());
                    jo.put("ShiftStock", "0");
                    jo.put("NetStock", "0");
                    jo.put("SoldStock", lblsoldtofarmer1.getText().toString());
                    jo.put("BalStock", lblbal1.getText().toString());
                    jo.put("stockDate", strdate);
                    jo.put("Status", "0");
                    jo.put("compcode", compcode);
                    ja.put(jo);
                    count++;

                }



            }
            mainObj.put("detail", ja);


            //ene

            if (count > 0) {
                // msclass.showMessage("Retailer POG data updated successfully.");
                // spCropType.setSelection(0);
                // my_linear_layout1.removeAllViews();
                // new SyncPOGData_Async("MDO_POGData","DistributorAsRetailer3").execute();
                                /*msclass.showMessage("Distributor POG data updated successfully.");
                check =0;
                spCropType.setSelection(0);
                my_linear_layout1.removeAllViews();*/
                JSONObject requestParams = new JSONObject();
                JSONObject objheader  = new JSONObject();
                JSONObject obj3  = new JSONObject();
                try
                {
                    JSONObject requestary = new JSONObject();
                    requestary.put("UserCode", usercode);
                    requestary.put("Action","1");
                    requestary.put("Type","MDO_VegRetailerPOGData");
                    JSONArray jaheader = new JSONArray();
                    jaheader.put(requestary) ;
                    String searchQuery = "select * from RetailerNextBtPOGTable where Status='0'";
                    obj3.put("Table3", mDatabase.getResults(searchQuery));
                    requestParams.put("Table", jaheader);
                    requestParams.put("Table2", ja);
                    requestParams.put("Table3", mDatabase.getResults(searchQuery));
                    new VegRetailerPOG("MDO_VegRetailerPOGData", requestParams).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }

               // new SyncPOGData_Async("MDO_VegRetailerPOGData","DistributorAsRetailer3").execute();


            } else {
                msclass.showMessage("Please enter product details");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.toString());

        }
    }

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class VegRetailerPOG extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;

        public VegRetailerPOG(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            return POGVegData(function,obj);
        }
        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                //msclass.showMessage(resultout);

                JSONObject jsonObject = new JSONObject(resultout);
                // if (jsonObject.has("True")) {
                if (resultout.contains("True")) {

                    if (resultout.contains("True") ) {

//

                       /* if(jsonObject.has("message") && jsonObject.getString("message").equals("No activities found"))
                        {
                            Intent i = new Intent(farmerDigitalAgreement.this, FarmerCallActivity.class);
                            i.putExtra("FromTestimonial", true);
                             startActivity(i);
                        }else*/
                        {
                            msclass.showMessage("Data saved successfully");
                            check =0;
                            spCropType.setSelection(0);
                            my_linear_layout1.removeAllViews();
                        }
                        progressBarVisibility();
                    } else
                    {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(retailerpogupdateDetailVeg.this);
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

                }else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(retailerpogupdateDetailVeg.this);
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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(retailerpogupdateDetailVeg.this);
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

        }

    }
    /**
     * <P>Method to get the search  data to server</P>
     * @param function
     * @param obj
     * @return
     */
    private String POGVegData(String function, JSONObject obj) {
        return HttpUtils.POSTDatabyte(function, cx.MDOurlpath,
                1,obj);


    }

    public void updatePOG(final String cropcode,final String crop,String result)
    {
        try {



            my_linear_layout1.removeAllViews();                              //add this too
            //st
            try {


                String searchQuery2 = "SELECT distinct a.Prod_Name as ProductName, a.Crop_Name as CropName,  " +
                        " FROM mdo_pogsapdata a " +
                        " where upper(a.Crop_Name)=upper('" + croptype + "')" +
                        " and Prod_Name <>'' ";

               /* String searchQuery = "SELECT distinct a.Prod_Name as ProductName, a.Crop_Name as CropName,  " +
                        " b.RecStock,ShiftStock,NetStock ,SoldStock," +
                        " BalStock,stockDate FROM mdo_pogsapdata a left outer join" +
                        " ( select distinct ProductName,CropName, RecStock," +
                        "ShiftStock,NetStock ,SoldStock," +
                        "BalStock,stockDate  from  RetailerPOGTable " +
                        " where upper(CropName)=upper('" + croptype + "')" +
                        " and  RetailerMobileno='"+RetailerMobileno+"'  ) b " +
                        "on ltrim(a.Crop_Name)=ltrim(b.CropName) " +
                        " and ltrim(a.Prod_Name)=ltrim(b.ProductName) " +
                        "where upper(a.Crop_Name)=upper('" + croptype + "')" +
                        " and Prod_Name <>'' ";*/
                String searchQuery = "SELECT distinct a.Prod_Name as ProductName, a.Crop_Name as CropName,  " +
                        " a.Crop_Code,a.Prod_Code,b.RecStock,ShiftStock,NetStock ,SoldStock," +
                        " BalStock,stockDate FROM mdo_pogsapdata a left outer join" +
                        " ( select distinct ProductName,ProductCode,CropCode,CropName, RecStock," +
                        "ShiftStock,NetStock ,SoldStock," +
                        "BalStock,stockDate  from  RetailerPOGTable " +
                        " where upper(CropCode)=upper('" + cropcode + "')" +
                        " and  RetailerMobileno='"+RetailerMobileno+"'  ) b " +
                        "on ltrim(a.Crop_Code)=ltrim(b.CropCode) " +
                        " and ltrim(a.Prod_Code)=ltrim(b.ProductCode) " +
                        "where upper(a.Crop_Code)=upper('" + cropcode + "')" +
                        " and Prod_Name <>'' ";

                JSONObject object = new JSONObject(result);
                // JSONObject object = new JSONObject();
                //object.put("Table1", mDatabase.getResults(searchQuery));
                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                String  str="a";
                String[] splittedValues = str.split(",");//spcrop.getSelectedItemsAsString().split(",");

                //HEADER START
                final View view1 = LayoutInflater.from(this).inflate(R.layout.showupdatepogveg, null);
                view1.setBackgroundColor(getResources().getColor(R.color.lightgray));
                EditText  lblreceived = (EditText) view1.findViewById(R.id.lblreceived);
                EditText lblsoldtofarmer = (EditText) view1.findViewById(R.id.lblsoldtofarmer);
                TextView txtprdname = (TextView) view1.findViewById(R.id.txtprdname);
                EditText lblbal = (EditText) view1.findViewById(R.id.lblbal);
                Button btnnext = (Button) view1.findViewById(R.id.btnnext);
                btnnext.setVisibility(View.INVISIBLE);

                ViewGroup.LayoutParams params = txtprdname.getLayoutParams();
                params.height  = 100;
                txtprdname.setLayoutParams(params);
                txtprdname.setGravity(Gravity.CENTER);
                txtprdname.setText("PRODUCT NAME");
                txtprdname.setEnabled(false);
                txtprdname.setTextColor(Color.WHITE);
                txtprdname.setTextSize(10);
                //.setBackgroundColor(getResources().getColor(R.color.redbus));
                txtprdname.setTypeface(txtprdname.getTypeface(), Typeface.BOLD);
                //txtprdname.setHeight(55);
                 lblreceived.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
                lblreceived.setText("RECEIVED STOCK TILL DATE");
                lblreceived.setEnabled(false);
                // lblreceived.setMaxLines(2);
                //lblreceived.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                // lblreceived.setSingleLine(false);
                // lblreceived.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

                lblreceived.setSingleLine(false);
                lblreceived.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblreceived.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblreceived.setMaxLines(10);
                ViewGroup.LayoutParams params2 = lblreceived.getLayoutParams();
                params2.height  = 100;
                lblreceived.setGravity(Gravity.CENTER);
                lblreceived.setLayoutParams(params2);
                //lblreceived.setHeight(55);

                lblreceived.setTextColor(Color.WHITE);
                lblreceived.setBackgroundColor(Color.TRANSPARENT);

                // lblreceived.setBackgroundColor(getResources().getColor(R.color.redbus));
                //lblreceived.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                // lblreceived.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                // lblcomp.setTextSize(14);
                lblreceived.setTypeface(lblreceived.getTypeface(), Typeface.BOLD);

                //lblshipt.setHeight(55);
                ViewGroup.LayoutParams params5= lblsoldtofarmer.getLayoutParams();
                params5.height  = 100;

                lblsoldtofarmer.setLayoutParams(params5);
                lblsoldtofarmer.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});

                lblsoldtofarmer.setGravity(Gravity.CENTER);
                lblsoldtofarmer.setText("STOCK SOLD TO FARMERS");
                lblsoldtofarmer.setEnabled(false);
                //lblsoldtofarmer.setHeight(55);
                lblsoldtofarmer.setSingleLine(false);
                lblsoldtofarmer.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblsoldtofarmer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblsoldtofarmer.setMaxLines(10);
                lblsoldtofarmer.setTextColor(Color.WHITE);
                lblsoldtofarmer.setBackgroundColor(Color.TRANSPARENT);

                // lblsoldtofarmer.setBackgroundColor(getResources().getColor(R.color.redbus));
                lblsoldtofarmer.setTypeface(lblsoldtofarmer.getTypeface(), Typeface.BOLD);
                ViewGroup.LayoutParams params6= lblbal.getLayoutParams();
                params6.height  = 100;

                lblbal.setLayoutParams(params6);
                lblbal.setGravity(Gravity.CENTER);
                lblbal.setText("BALANCE STOCK");
                lblbal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});

                lblbal.setEnabled(false);
                lblbal.setSingleLine(false);
                lblbal.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblbal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblbal.setMaxLines(10);

                lblbal.setTextColor(Color.WHITE);
                lblbal.setBackgroundColor(Color.TRANSPARENT);

                //lblbal.setBackgroundColor(getResources().getColor(R.color.redbus));
                //  lblqty.setTextSize(14);
                lblbal.setTypeface(lblbal.getTypeface(), Typeface.BOLD);
                view1.setBackgroundColor(getResources().getColor(R.color.heback));
                my_linear_layout1.addView(view1);
                // HEADER END

                //  if(jArray.length()>0)
                {
                    for (int i = 0; i < jArray.length(); i++) {

                        int total = 0;

                        final View view2 = LayoutInflater.from(this).inflate(R.layout.showupdatepogveg, null);

                        view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        final EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                        final EditText lblsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                        final TextView txtprdname1 = (TextView) view2.findViewById(R.id.txtprdname);
                        final EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);
                        final TextView txtprdcode1 = (TextView) view2.findViewById(R.id.txtprdcode);
                        Button btnnext1 = (Button) view2.findViewById(R.id.btnnext);
                        lblsoldtofarmer1.setEnabled(false);
                        lblreceived1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
                        lblbal1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
                        lblsoldtofarmer1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

                        //Start

                        lblreceived1.addTextChangedListener(new TextWatcher() {

                            public void onTextChanged(CharSequence s, int start, int before,
                                                      int count) {
                                try {

                                    if (listenForChanges) {
                                        listenForChanges = false;
                                        if (s.length() != 0) { //do your work here }

                                            if (lblreceived1.getText().length() != 0)
                                            {

                                                if (lblsoldtofarmer1.getText().length() == 0) {
                                                    lblsoldtofarmer1.setText("0");
                                                }
                                                if (lblbal1.getText().length() == 0) {
                                                    lblbal1.setText("0");
                                                }

                                                double godownstock = Double.parseDouble(String.valueOf(lblbal1.getText()));
                                                double  receivedstock = Double.parseDouble(String.valueOf(s));

                                                if (godownstock>receivedstock)
                                                {
                                                    lblsoldtofarmer1.setText("0");
                                                    lblbal1.setText("0");
                                                }
                                                else {
                                                    double  bal = receivedstock
                                                            - Double.valueOf(lblbal1.getText().toString());
                                                    lblsoldtofarmer1.setText(String.valueOf(bal));
                                                }

                                            }
                                            // txtCurrentStock.setText("");
                                        } else {

                                            lblsoldtofarmer1.setText("");
                                            lblbal1.setText("0");
                                        }
                                        listenForChanges = true;
                                    }
                                    // listenForChanges = true;
                                }catch(NumberFormatException we)
                                {
                                    Toast.makeText(context, "Number Format exception "+we.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception ex) {
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

                        //
                        lblbal1.addTextChangedListener(new TextWatcher() {
                            public void onTextChanged(CharSequence s, int start, int before,
                                                      int count) {
                                try {
                                    if (listenForChanges) {
                                        listenForChanges = false;

                                        if (s.length() != 0) { //do your work here }

                                            if (lblbal1.getText().length() == 0) {
                                                lblbal1.setText("0");
                                            }
                                            double godownbalance = Double.parseDouble(String.valueOf(s));
                                            double netstock = Double.parseDouble(String.valueOf(lblreceived1.getText()));
                                            if (godownbalance > netstock) {
                                                msclass.showMessage("godown stock should not more than received stock.");
                                                lblsoldtofarmer1.setText("");
                                                lblbal1.setText("0");
                                            } else {
                                                lblsoldtofarmer1.setText(String.valueOf(netstock - godownbalance));
                                            }

                                        } else {
                                            //txtCurrentStock.setText("");
                                            lblbal1.setText("0");
                                            lblsoldtofarmer1.setText("0");
                                        }
                                        listenForChanges = true;
                                    }

                                } catch (Exception ex) {
                                    //msclass.showMessage(ex.getMessage());
                                    ex.printStackTrace();
                                    listenForChanges = true;
                                }

                            }

                            public void beforeTextChanged(CharSequence s, int start, int count,
                                                          int after) {

                            }

                            public void afterTextChanged(Editable s) {

                            }
                        });
                        //end


                        btnnext1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lblbal1.getText().length() == 0) {
                                    msclass.showMessage(" please check stock balance");
                                } else {
                                    addproduct(croptype,txtprdcode1.getText().toString(), txtprdname1.getText().toString(),
                                            lblbal1.getText().toString());
                                }
                            }
                        });
                        JSONObject jObject = jArray.getJSONObject(i);
                        txtprdname1.setText(jObject.getString("ProductName").toString());
                        txtprdcode1.setText(jObject.getString("Prod_Code").toString());
                        lblreceived1.setText(jObject.getString("RecStock").toString());
                        lblsoldtofarmer1.setText(jObject.getString("SoldStock").toString());
                        lblbal1.setText(jObject.getString("BalStock").toString());
                        if (jObject.getString("stockDate").toString().length() != 0) {
                            txtpridate.setText(jObject.getString("stockDate").toString());
                        }

                        if (crop.toLowerCase().contains("cotton")) {
                            lblreceived1.setHint("PKT");
                            lblsoldtofarmer1.setHint("PKT");
                            lblbal1.setHint("PKT");
                        } else {
                            lblreceived1.setHint("KG");
                            lblsoldtofarmer1.setHint("KG");
                            lblbal1.setHint("KG");
                        }
                        my_linear_layout1.addView(view2);
                    }
                }

                if (txtpridate.length()==0)
                {

                    txtpridate.setText(strdate.toString());

                }

            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }
        }catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void addproduct( final String crop,final String prd_code,final String productname,String balancestock)
    {
        try {

            // Check Record Exist
            JSONObject object = new JSONObject();
           /* String searchQuery = "SELECT distinct a.ProductName, a.ProductCode,  " +
                    " a.BalStock,actionstatus,Qty,Date," +
                    " AssociateDistributor FROM RetailerNextBtPOGTable a " +
                    " where a.ProductName='" + productname + "' and  RetailerMobileno='"+RetailerMobileno+"' ";
*/
            String searchQuery = "SELECT distinct a.ProductName, a.ProductCode,  " +
                    " a.BalStock,actionstatus,Qty,Date," +
                    " AssociateDistributor FROM RetailerNextBtPOGTable a " +
                    " where a.ProductCode='" + prd_code + "' and  RetailerMobileno='"+RetailerMobileno+"' ";


            object.put("Table1", mDatabase.getResults( searchQuery));
            final JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.onnextbuttonpopupveg);
            final HorizontalScrollView s = (HorizontalScrollView)dialog.findViewById(R.id.horizontalView);
           /* s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L); */
            final TableLayout tblLayout = (TableLayout)dialog.findViewById(R.id.tableLayout);
            TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            TableRow row = (TableRow)tblLayout.getChildAt(1);
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final MultiSelectionSpinner spdistr = (MultiSelectionSpinner) row.findViewById(R.id.spdistr);
            binddistributor(spdistr);
            final SearchableSpinner  spstatus=(SearchableSpinner) row.findViewById(R.id.spstatus);
            BindststusData(spstatus);
            final EditText txtqty = (EditText) row.findViewById(R.id.txtqty);
            final EditText txtprdname = (EditText) row.findViewById(R.id.txtprdname);
            final EditText txtbalance = (EditText) row.findViewById(R.id.txtbalance);
            final EditText txtdate = (EditText) row.findViewById(R.id.txtdate);
            final TextView lblQty = (TextView) row0.findViewById(R.id.lblVillage);

            if (crop.toLowerCase().contains("cotton"))
            {
                lblQty.setText("QTY(pkts)");
                txtqty.setHint("pkt");
            }
            else
            {
                lblQty.setText("QTY(kgs)");
                txtqty.setHint("kg");
            }
            txtbalance.setEnabled(false);
            txtprdname.setEnabled(false);
            txtprdname.setText(productname);
            txtbalance.setText(balancestock);
            if(jArray.length() >0)
            {
                JSONObject jObject = jArray.getJSONObject(0);
                txtdate.setText(jObject.getString("Date").toString());
                txtqty.setText(jObject.getString("Qty").toString());
                spstatus.setSelection((config.getIndex(spstatus,jObject.getString("actionstatus").toString()))) ;
                // txtdate.setText(jObject.getString("ProductName").toString());


                List<String> list = new ArrayList<String>();
                String[] str=jObject.getString("AssociateDistributor").toString().split(",");
                if(str.length>0)
                {
                    for(int i=0;i<str.length ;i++)
                    {
                        list.add(str[i].toString().trim());
                    }

                }
                else
                {
                    list.add(jObject.getString("AssociateDistributor").toString());
                }

                spdistr.setSelection(list) ;
            }

            txtdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDateTimeField(v);
                }
            });
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
            //spproduct .setItems(array);
            //spproduct.hasNoneOption(true);
            // spproduct.setSelection(new int[]{0});
            spdistr.setListener(new MultiSelectionSpinner.MySpinnerListener(){

                @Override
                public void onItemClicked(int which) {
                }

                @Override
                public void selectedIndices(List<Integer> indices) {
                }

                @Override
                public void selectedStrings(List<String> strings) {
                }
            });

            btnaddmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String status = spstatus.getSelectedItem().toString();
                        int count = 0;
                        String product = "";
                        if (status.equals("SELECT STATUS")) {
                            msclass.showMessage("Please select stock  status");
                            return;
                        }



                        if (!status.equals("SUFFICIENT"))
                        {

                            if (txtqty.getText().length()==0) {
                                msclass.showMessage("Please enter qty");
                                return;
                            }
                            if (Double.valueOf(txtqty.getText().toString())>Double.valueOf(txtbalance.getText().toString()))
                            {
                                msclass.showMessage("Sales return ,not allow to more than balance qty.");
                                return;
                            }
                            if (txtdate.getText().length()==0) {
                                msclass.showMessage("Please  enter date");
                                return;
                            }
                            if (spdistr.getSelectedStrings().contains("SELECT DISTRIBUTOR")) {
                                msclass.showMessage("Please  enter distributor");
                                return;
                            }
                        }


                        //St

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        // Date d = new Date();
                        //final String strdate = dateFormat.format(d);
                        if(jArray.length() >0) {
                           /* mDatabase.deleterecord("delete from  RetailerNextBtPOGTable where " +
                                    " RetailerMobileno='"+RetailerMobileno+"' " +
                                    "and ProductName='"+productname+"'");
                                    */
                            mDatabase.deleterecord("delete from  RetailerNextBtPOGTable where " +
                                    " RetailerMobileno='"+RetailerMobileno+"' " +
                                    "and ProductCode='"+prd_code+"'");
                        }
                        boolean f = mDatabase.InsertRetailerNextBtPOGData(pref.getString("UserID",
                                null), RetailerMobileno,
                                txtprdname.getText().toString().trim(),prd_code,
                                txtbalance.getText().toString().trim(), spstatus.getSelectedItem().toString(),
                                txtqty.getText().toString(),txtdate.getText().toString(),
                                spdistr.getSelectedItemsAsString().toString());
                        if (f == true) {
                            count++;
                        } else {

                        }



                        //ene

                        if (count > 0) {
                            msclass.showMessage("product detail added successfully.");
                            dialog.dismiss();

                        } else {
                            msclass.showMessage("Please enter product details");
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

    private  void BindststusData(SearchableSpinner spstatus) {

        try {
            spstatus.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "SELECT STATUS"));
            gm.add(new GeneralMaster("NEED MORE QTY", "NEED MORE QTY"));
            gm.add(new GeneralMaster("SALES RETURN", "SALES RETURN"));
            gm.add(new GeneralMaster("SUFFICIENT", "SUFFICIENT"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spstatus.setAdapter(adapter);





        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    private void BindCROPMaster()
    {
        try {
            //st
            spCropType.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            searchQuery = "SELECT distinct upper(Crop_Name) as CropName,Crop_Code  FROM mdo_pogsapdata ";
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
            Toast.makeText(this,ex.toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


        //en

    }
    public void  binddistributor(MultiSelectionSpinner spdistr )
    {
        // spdistr.setAdapter(null);


        String str= null;
        try {
            String searchQuery="";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;

            //searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
            //        "and  dist='" + dist.toUpperCase() + "' order by  RetailerName ";

            searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' " +
                    " order by  RetailerName ";

            // searchQuery = "SELECT distinct RetailerName  FROM RetailerMaster where activity='Distributor' and  taluka='" + spDist.getSelectedItem().toString().trim()+ "' order by  RetailerName ";

            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                array = new String[jArray.length()+2];
                array[0]="SELECT DISTRIBUTOR";
                for(int i=0; i <jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i+1]=jObject.getString("RetailerName").toString();
                }
                array[jArray.length()+1]="OTHER";
                if(array.length>0) {
                    spdistr.setItems(array);
                    spdistr.hasNoneOption(true);
                    spdistr.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            }
            catch (Exception ex)
            {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }



            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spTehsil.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }




    }
    public class UpdatedPOG extends AsyncTask<String, String, String> {

        private String usercode,Crop_Code,cropname,customer,DistrCode,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,Comments;
        byte[] objAsBytes;

        public UpdatedPOG( String action,byte[] objAsBytes, String usercode,String Crop_Code,
                           String cropname,String DistrCode  ){
            this.objAsBytes=objAsBytes;
            this.usercode=usercode;
            this.Crop_Code=Crop_Code;
            this.cropname=cropname;
            this.action=action;
            this.DistrCode=DistrCode;


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

            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "VegUpdatedPOG"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            postParameters.add(new BasicNameValuePair("RetailerType",RetailerType));
            postParameters.add(new BasicNameValuePair("compcode",compcode));

            String Urlpath1= cx.MDOurlpath+"?action="+action+"&usercode="+usercode+"&Crop_Code="+Crop_Code+"" +
                    "&cropname="+cropname+"&distcode="+DistrCode+"";
            HttpPost httppost = new HttpPost(Urlpath1.replace(" ","%20"));
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
                    updatePOG(Crop_Code,croptype,result);

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
    class SyncPOGData_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag,classname;
        String returnvalue;
        ProgressDialog progressDialog;
        public SyncPOGData_Async(String tag, String classname) {
            this.tag = tag;
            this.classname =classname;
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

            returnvalue=MDO_POGData();
            return returnvalue;
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {

            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();

                }

                if (result.contains("True")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    String strdate = dateFormat.format(d);
                    mDatabase.Updatedata("update RetailerPOGTable  set Status='1'");
                    mDatabase.Updatedata("update RetailerNextBtPOGTable  set Status='1'");
                    mDatabase.Updatedata("update RetailerCompetitatorPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorNextBtPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorCompetitatorPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorAsRetailerPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorAsRetailerCompetitatorPOGTable  set Status='1'");
                    mDatabase.Updatedata("update DistributorAsRetailerNextBtPOGTable  set Status='1'");
                    Toast.makeText(context, "POG Data Uploaded successfully.",
                            Toast.LENGTH_SHORT).show();
                    check =0;
                    spCropType.setSelection(0);
                    my_linear_layout1.removeAllViews();
                    msclass.showMessage("Retailer POG data updated successfully.");


                }
                else
                {
                    msclass.showMessage(result);
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
    public String  MDO_POGData()
    {
        //if(config.NetworkConnection()) {
        // dialog.setMessage("Loading. Please wait...");
        //dialog.show();
        String str= null;
        String returnvalue="";
        String Imagestring1="";
        String Imagestring2="";
        String ImageName="";
        Cursor cursor=null;
        String searchQuery="";
        int count=0;
        searchQuery = "select * from RetailerPOGTable  where  Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=cursor.getCount();
        cursor.close();

        searchQuery = "select * from RetailerNextBtPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from RetailerCompetitatorPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from DistributorPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();
        searchQuery = "select * from DistributorNextBtPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();


        searchQuery = "select * from DistributorCompetitatorPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from DistributorAsRetailerPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();
        searchQuery = "select * from DistributorAsRetailerCompetitatorPOGTable where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select * from RetailerPOGTable where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from RetailerNextBtPOGTable where Status='0'";
                    object.put("Table2", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from RetailerCompetitatorPOGTable where Status='0'";
                    object.put("Table3", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorPOGTable where Status='0'";
                    object.put("Table4", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorNextBtPOGTable where Status='0'";
                    object.put("Table5", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorCompetitatorPOGTable where Status='0'";
                    object.put("Table6", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorAsRetailerPOGTable where Status='0'";
                    object.put("Table7", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorAsRetailerNextBtPOGTable where Status='0'";
                    object.put("Table8", mDatabase.getResults(searchQuery));
                    searchQuery = "select * from DistributorAsRetailerCompetitatorPOGTable where Status='0'";
                    object.put("Table9", mDatabase.getResults(searchQuery));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // returnvalue= syncPOGdata("MDO_POGData", objAsBytes, Imagestring1, Imagestring2, ImageName, "",cx.MDOurlpath);
                //returnvalue= syncPOGdata("MDO_POGData", objAsBytes, Imagestring1, Imagestring2, ImageName, "",cx.MDOurlpath);
                returnvalue= syncPOGdata("MDO_RetailerPOGData", objAsBytes, Imagestring1, Imagestring2, ImageName, "",cx.MDOurlpath);




            } catch (Exception ex) {
                // msclass.showMessage(ex.getMessage());

            }
        }
        else
        {
            // msclass.showMessage("Uploading data not available");

        }
        return returnvalue;
    }
    public synchronized String  syncPOGdata(String Funname, byte[] objAsBytes,String Imagestring1,String Imagestring2,String ImageName,String Intime,String urls) {

        String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);


        postParameters.add(new BasicNameValuePair("Type", Funname));
        postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
        postParameters.add(new BasicNameValuePair("RetailerType", RetailerType));
        String Urlpath=urls+"?appName=Myactivity";
        Log.d("mahi", "doInBackground: " + Urlpath);
        Log.d("mahi", "doInBackground:params::: " + postParameters);
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

    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container2.setClickable(true);
        container2.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}