package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;


public class saleOrderReturn extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty,txtComments;//,txtlotno,txtexpiry;
    public SearchableSpinner spReason, spproductlist,spdistributor,spdistributorshipto,spdivision,spsaleorg,spdepot;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org, cmbDistributor,cmbDistributorshipto,shiptocustomer;
    public Button btnsave,btnTakephoto,btnadd;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    SharedPreferences locdata,pref;
    SharedPreferences.Editor loceditor;
    private SqliteDatabase mDatabase;
    private Context context;
    TextView txttotalvalue,lbldistributorShipto;
    CardView card_month04;
    public String SERVER = "";
    private boolean listenForChanges = true;
    public String saleorderurl = "";
    public String testurl = "";
    LinearLayout my_linear_layout1;
    SharedPreferences.Editor editor, locedit;
    public String  saleorg,customer, name , DLV_plant, customregroup,
            cmbproductlist,productcode="",drpreason="";
    int check2=0;
    public int Numerator=0,Denominator=0;
    String lotno="";
    MultiSelectionSpinner spLotNumber,spIRNumber;
    String  lotnumberlist ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_order_return);
        getSupportActionBar().hide(); //<< this
        context = this;
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        testurl ="http://10.80.50.153/maatest/IPM.ashx"; //cx.saleSERVER;
        saleorderurl =cx.saleSERVER;
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass=new Messageclass(this);
        my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        txtQty=(EditText)findViewById(R.id.txtQty);
        txttotalvalue=(TextView)findViewById(R.id.txttotalvalue);
        txtComments=(EditText)findViewById(R.id.txtComments);
        // txtlotno=(EditText)findViewById(R.id.txtlotno);
        // txtexpiry=(EditText)findViewById(R.id.txtexpiry);
        spproductlist=(SearchableSpinner)findViewById(R.id.spproductlist);
        spdistributor=(SearchableSpinner)findViewById(R.id.spdistributor);
        spReason=(SearchableSpinner)findViewById(R.id.spReason);
        spdistributorshipto=(SearchableSpinner)findViewById(R.id.spdistributorshipto);
        spdepot=(SearchableSpinner)findViewById(R.id.spdepot);
        spdivision=(SearchableSpinner)findViewById(R.id.spdivision);
        spsaleorg=(SearchableSpinner)findViewById(R.id.spsaleorg);
        spLotNumber=(MultiSelectionSpinner) findViewById(R.id.spLotNumber);
        spIRNumber=(MultiSelectionSpinner) findViewById(R.id.spIRNumber);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnTakephoto=(Button)findViewById(R.id.btnTakephoto);
        lbldistributorShipto=(TextView)findViewById(R.id.lbldistributorShipto);
        card_month04=(CardView)findViewById(R.id.card_month04);
        btnadd=(Button)findViewById(R.id.btnadd);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(btnadd, this);
        // Utility.setRegularFont(txtQty, this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    addrow();
                }

            }
        });
        spsaleorg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
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
                        // msclass.showMessage("Only one division order are allowed at one time.");
                        // return;
                    }



                    division = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    GeneralMaster sd = (GeneralMaster) spsaleorg.getSelectedItem();
                    org=URLEncoder.encode(sd.Code(), "UTF-8");
                    cmbDistributor=URLEncoder.encode(gm.Code().toString(), "UTF-8");

                    new Getdistributor("1",usercode,org,division,cmbDistributor).execute();
                    //new Getdistributor("2",usercode,org,division).execute();

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
        spdivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
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
                        // msclass.showMessage("Only one division order are allowed at one time.");
                        // return;
                    }


                    division = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    GeneralMaster sd = (GeneralMaster) spsaleorg.getSelectedItem();
                    org=URLEncoder.encode(sd.Code(), "UTF-8");
                    cmbDistributor=URLEncoder.encode(gm.Code().toString(), "UTF-8");

                    new Getdistributor("1",usercode,org,division,cmbDistributor).execute();
                    //new Getdistributor("2",usercode,org,division).execute();

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
        spdistributor .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    // org=URLEncoder.encode(spsaleorg.getSelectedItem().toString(), "UTF-8");
                    cmbDistributor=URLEncoder.encode(gm.Code().toString(), "UTF-8");
                    String[] distr=gm.Code().toString().split("\\|");
                    DLV_plant=distr[2].toString();
                    GeneralMaster orgm = (GeneralMaster) spsaleorg.getSelectedItem();
                    org=orgm.Code();
                    GeneralMaster divgm = (GeneralMaster) spdivision.getSelectedItem();
                    division = URLEncoder.encode(divgm.Code().trim(), "UTF-8");
                    spdistributorshipto.setSelection(config.getIndex(spdistributor,spdistributor.getSelectedItem().toString()));
                    spdepot.setSelection(config.getIndex(spdepot,DLV_plant));
                    // new Getdistributor("1",usercode,org,division).execute();
                    // Call Product List
                    my_linear_layout1.removeAllViews();
                    new Getdistributor("2",usercode,org,division,cmbDistributor).execute();

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

        spdepot .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    check2 = check2 + 1;
                    if (check2 > 1)
                    {

                        GeneralMaster gm = (GeneralMaster) spdistributor.getSelectedItem();
                        cmbDistributor=URLEncoder.encode(gm.Code().toString(), "UTF-8");
                        String[] distr=gm.Code().toString().split("\\|");
                        DLV_plant=distr[2].toString();
                        GeneralMaster orgm = (GeneralMaster) spsaleorg.getSelectedItem();
                        org=orgm.Code();
                        GeneralMaster divgm = (GeneralMaster) spdivision.getSelectedItem();
                        division = URLEncoder.encode(divgm.Code().trim(), "UTF-8");
                        //spdistributorshipto.setSelection(config.getIndex(spdistributor,spdistributor.getSelectedItem().toString()));
                        // new Getdistributor("1",usercode,org,division).execute();
                        // Call Product List
                        GeneralMaster depo = (GeneralMaster) spdepot.getSelectedItem();
                        cmbDistributor=cmbDistributor.replace(DLV_plant,depo.Code());
                        new Getdistributor("2",usercode,org,division,cmbDistributor).execute();


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

        spReason .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                String distr = gm.Code().toString();
                if (distr.contains("0") || distr.contains("Other"))
                {
                    txtComments.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
                // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        //Start
        // On  selection Bind Lot number on  distributor and product wise
        spproductlist .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    // org=URLEncoder.encode(spsaleorg.getSelectedItem().toString(), "UTF-8");
                    cmbDistributor=URLEncoder.encode(gm.Code().toString(), "UTF-8");
                    String[] distr=gm.Code().toString().split("\\|");
                    DLV_plant=distr[2].toString();
                    GeneralMaster orgm = (GeneralMaster) spsaleorg.getSelectedItem();
                    org=orgm.Code();
                    GeneralMaster divgm = (GeneralMaster) spdivision.getSelectedItem();
                    division = URLEncoder.encode(divgm.Code().trim(), "UTF-8");
                    spdistributorshipto.setSelection(config.getIndex(spdistributor,spdistributor.getSelectedItem().toString()));
                    spdepot.setSelection(config.getIndex(spdepot,DLV_plant));
                    // new Getdistributor("1",usercode,org,division).execute();
                    // Call  Bind Lot number list

                    //if (validation())
                    {
                        bindLotnumberList();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
                // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spLotNumber.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ", String.valueOf(strings));
                lotnumberlist=String.valueOf(strings.toString().trim());
                lotno = lotnumberlist.substring(1, lotnumberlist.length() - 1).replaceAll("\\s","");//.split(",");
                try {
                    lotno = URLEncoder.encode(lotnumberlist.substring(1, lotnumberlist.length() - 1).trim(), "UTF-8");

                    if (validation())
                    {
                        new Salereturnorderdata("7", null, usercode, org, division, customer, name,
                                DLV_plant, customregroup, "", lotno).execute();
                    }
                }
                catch (Exception e) {
                }
            }
        });

        spIRNumber.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("which selectedSt:: ", String.valueOf(strings));
                String IRList  =String.valueOf(strings.toString().trim());
                IRList = IRList.substring(1, IRList.length() - 1).replaceAll("\\s","");//.split(",");
                try {
                    CreatereturnQtyEntryPanel(IRList);
                }
                catch (Exception e) {
                }
            }
        });
        //end
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savesalereturndata();
            }
        });


        BindIntialData();
        //Binddistributor();
    }

    public void savesalereturndata()
    {
        try
        {

            GeneralMaster sp = (GeneralMaster) spsaleorg.getSelectedItem();
            if (sp.Code().equals("0"))
            {
                msclass.showMessage("Please select organisation.") ;
                return ;
            }
            GeneralMaster sd = (GeneralMaster) spdivision.getSelectedItem();
            if (sd.Code().equals("0"))
            {
                msclass.showMessage("Please select division.") ;
                return ;
            }

            GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();
            GeneralMaster rm = (GeneralMaster) spReason.getSelectedItem();
            GeneralMaster shipto = (GeneralMaster) spdistributorshipto.getSelectedItem();
            if (dd.Code().equals("0"))
            {
                msclass.showMessage("Please select distributor.") ;
                return ;
            }
                    /*if (shipto.Code().equals("0"))
                    {
                        msclass.showMessage("Please select ship to distributor.") ;
                        return ;
                    }*/

            if (my_linear_layout1.getChildCount()==0  ||my_linear_layout1.getChildCount()==1) {
                msclass.showMessage("Please add product list");
                return;
            }
            if (rm.Code().equals("0"))
            {
                msclass.showMessage("Please select reason.") ;
                return ;
            }

            if (rm.Code().contains("Other") )
            {
                if (txtComments.length()==0) {
                    msclass.showMessage("Please enter sale return reason.");
                    return;
                }
            }

            if (config.NetworkConnection())
            {
                GeneralMaster gm = (GeneralMaster) spsaleorg.getSelectedItem();
                GeneralMaster gd = (GeneralMaster) spdivision.getSelectedItem();
                GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                //GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();

                division = URLEncoder.encode(gd.Code(), "UTF-8");
                org=URLEncoder.encode(gm.Code(), "UTF-8");
                cmbDistributor=dd.Code();
                cmbDistributorshipto=shipto.Code();
                String[] distr=cmbDistributor.split("\\|");
                String[] distrshiptto=cmbDistributorshipto.split("\\|");
                customer=distr[0].toString();

                shiptocustomer=distrshiptto[0].toString();
                name=URLEncoder.encode(distr[1].toString(), "UTF-8");
                DLV_plant=distr[2].toString();
                customregroup=distr[3].toString();
                JSONObject mainObj = new JSONObject();
                JSONArray ja = new JSONArray();
                String Comments="";
                int checkqtycoun=0;
                for (int i = 1; i < my_linear_layout1.getChildCount(); i++)
                {
                    View view2 = my_linear_layout1.getChildAt(i);
                    TextView txtproducthide=(TextView) view2.findViewById(R.id.txtproducthide);
                    TextView lblLotno1 = (TextView) view2.findViewById(R.id.lblLotno);
                    final TextView lblOpenQty1 = (TextView) view2.findViewById(R.id.lblOpenQty);
                    final EditText lblReturnQty = (EditText) view2.findViewById(R.id.lblReturnQty);
                    TextView lblIRno1 = (TextView) view2.findViewById(R.id.lblIRno);
                    cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                    String[] prdarray=cmbproductlist.split("\\|");
                    String MATNR=prdarray[0].toString();
                    String MAKTX=prdarray[1].toString();
                    String MTART=prdarray[2].toString();
                    String MATKL=prdarray[3].toString();
                    //String PRICE=prdarray[4].toString();
                    String QTYPKT=lblReturnQty.getText().toString();
                    String OPENQTYPKT=lblOpenQty1.getText().toString();
                    // String EXPIRY=txtExpiry1.getText().toString();
                    String LOTNO=lblLotno1.getText().toString();
                    String IRNO=lblIRno1.getText().toString();
                    if(QTYPKT.isEmpty()  || QTYPKT.equals("0")) {
                        checkqtycoun=checkqtycoun+1;
                    }
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
                    jo.put("OPENQTYPKT", OPENQTYPKT);
                    jo.put("EXPIRY", null);
                    jo.put("LOTNO", LOTNO);
                    jo.put("IRNO", IRNO);
                    ja.put(jo);
                }
                mainObj.put("Table1", ja);
                byte[] objAsBytes = null;//new byte[10000];
                try {
                    objAsBytes = mainObj.toString().getBytes("UTF-8");
                    Comments=URLEncoder.encode(txtComments.getText().toString(), "UTF-8");
                    drpreason=URLEncoder.encode(spReason.getSelectedItem().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (checkqtycoun>0 )
                {

                    msclass.showMessage("Please enter return packet Qty.");
                    return;

                }



                new Salereturnorderdata("1",objAsBytes,usercode,org,division,customer,name,
                        DLV_plant,customregroup,Comments,shiptocustomer).execute();


            }
        }
        catch(Exception ex)
        {
            msclass.showMessage(ex.getMessage());
        }




    }
    public void savesalereturndataold()
    {
        try
        {

            GeneralMaster sp = (GeneralMaster) spsaleorg.getSelectedItem();
            if (sp.Code().equals("0"))
            {
                msclass.showMessage("Please select organisation.") ;
                return ;
            }
            GeneralMaster sd = (GeneralMaster) spdivision.getSelectedItem();
            if (sd.Code().equals("0"))
            {
                msclass.showMessage("Please select division.") ;
                return ;
            }

            GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();
            GeneralMaster rm = (GeneralMaster) spReason.getSelectedItem();
            GeneralMaster shipto = (GeneralMaster) spdistributorshipto.getSelectedItem();
            if (dd.Code().equals("0"))
            {
                msclass.showMessage("Please select distributor.") ;
                return ;
            }
                    /*if (shipto.Code().equals("0"))
                    {
                        msclass.showMessage("Please select ship to distributor.") ;
                        return ;
                    }*/

            if (my_linear_layout1.getChildCount()==0) {
                msclass.showMessage("Please add product list");
                return;
            }
            if (rm.Code().equals("0"))
            {
                msclass.showMessage("Please select reason.") ;
                return ;
            }

            if (rm.Code().contains("Other") )
            {
                if (txtComments.length()==0) {
                    msclass.showMessage("Please enter sale return reason.");
                    return;
                }
            }

            if (config.NetworkConnection())
            {
                GeneralMaster gm = (GeneralMaster) spsaleorg.getSelectedItem();
                GeneralMaster gd = (GeneralMaster) spdivision.getSelectedItem();
                GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                //GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();

                division = URLEncoder.encode(gd.Code(), "UTF-8");
                org=URLEncoder.encode(gm.Code(), "UTF-8");
                cmbDistributor=dd.Code();
                cmbDistributorshipto=shipto.Code();
                String[] distr=cmbDistributor.split("\\|");
                String[] distrshiptto=cmbDistributorshipto.split("\\|");
                customer=distr[0].toString();
                shiptocustomer=distrshiptto[0].toString();
                name=URLEncoder.encode(distr[1].toString(), "UTF-8");
                DLV_plant=distr[2].toString();
                customregroup=distr[3].toString();
                JSONObject mainObj = new JSONObject();
                JSONArray ja = new JSONArray();
                String Comments="";
                int checkqtycoun=0;
                for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                    View view2 = my_linear_layout1.getChildAt(i);
                    TextView txtproduct = (TextView) view2.findViewById(R.id.txtproduct);
                    TextView txtproducthide=(TextView) view2.findViewById(R.id.txtproducthide);
                    TextView txtproduct1 = (TextView) view2.findViewById(R.id.txtproduct);
                    TextView txtSn1 = (TextView) view2.findViewById(R.id.txtSn);
                    TextView txtlotnumber1 = (TextView) view2.findViewById(R.id.txtlotnumber);
                    TextView txtExpiry1 = (TextView) view2.findViewById(R.id.txtExpiry);
                    TextView txtQtyvalue1 = (TextView) view2.findViewById(R.id.txtQtyvalue);
                    final TextView txtopen1 = (TextView) view2.findViewById(R.id.txtopen);
                    final SearchableSpinner spIRno1 = (SearchableSpinner) view2.findViewById(R.id.spIRno);
                    cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                    String[] prdarray=cmbproductlist.split("\\|");
                    String MATNR=prdarray[0].toString();
                    String MAKTX=prdarray[1].toString();
                    String MTART=prdarray[2].toString();
                    String MATKL=prdarray[3].toString();
                    //String PRICE=prdarray[4].toString();
                    String QTYPKT=txtQtyvalue1.getText().toString();
                    String OPENQTYPKT=txtopen1.getText().toString();
                    String EXPIRY=txtExpiry1.getText().toString();
                    String LOTNO=txtlotnumber1.getText().toString();
                    String IRNO=spIRno1.getSelectedItem().toString();
                    if(QTYPKT.isEmpty()  || QTYPKT.equals("0")) {
                        checkqtycoun=checkqtycoun+1;
                    }
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
                    jo.put("OPENQTYPKT", OPENQTYPKT);
                    jo.put("EXPIRY", EXPIRY);
                    jo.put("LOTNO", LOTNO);
                    jo.put("IRNO", IRNO);
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

                if (checkqtycoun>0 )
                {

                    msclass.showMessage("Please enter packet Qty.");
                    return;

                }



                new Salereturnorderdata("1",objAsBytes,usercode,org,division,customer,name,
                        DLV_plant,customregroup,Comments,shiptocustomer).execute();


            }
        }
        catch(Exception ex)
        {
            msclass.showMessage(ex.getMessage());
        }




    }

    public void CreatereturnQtyEntryPanel( String IRnumberlist) {


        //st if single single add"," to last (append)
        // HEADER TAG
        IRnumberlist=IRnumberlist+",";
        String[] IRNoList=IRnumberlist.split(",");
        int count =my_linear_layout1.getChildCount();
        try {
            // HEADER TAG
            if (count==0) {
                final View view3 = LayoutInflater.from(this).inflate(R.layout.salereturnentrypanel, null);
                view3.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                TextView lblLotno = (TextView) view3.findViewById(R.id.lblLotno);
                TextView lblIRno = (TextView) view3.findViewById(R.id.lblIRno);
                TextView lblOpenQty = (TextView) view3.findViewById(R.id.lblOpenQty);
                final EditText lblReturnQty = (EditText) view3.findViewById(R.id.lblReturnQty);
                Button btndelete = (Button) view3.findViewById(R.id.btndelete);
                btndelete.setVisibility(View.INVISIBLE);
                lblLotno.setText("LOT NO");
                lblLotno.setTextColor(Color.WHITE);
                lblLotno.setBackgroundColor(Color.TRANSPARENT);
                lblIRno.setText("IR NO");
                lblIRno.setTextColor(Color.WHITE);
                lblIRno.setBackgroundColor(Color.TRANSPARENT);
                // lblcomp.setTextSize(14);
                // lblunit.setTypeface(lblunit.getTypeface(),Typeface.BOLD);
                lblOpenQty.setText("OPEN QTY");
                lblOpenQty.setTextColor(Color.WHITE);
                lblOpenQty.setBackgroundColor(Color.TRANSPARENT);
                // lblcrop.setTextSize(14);
                // lblcrop.setTypeface(lblcrop.getTypeface(),Typeface.BOLD);
                lblReturnQty.setText("RET.QTY");
                lblReturnQty.setTextColor(Color.WHITE);
                lblReturnQty.setBackgroundColor(Color.TRANSPARENT);
                view3.setBackgroundColor(getResources().getColor(R.color.heback));
                my_linear_layout1.addView(view3);
            }

            int total = 0;
            String Lotno="",IRno="",matnrno="";
            for (int j = 0; j < IRNoList.length ; j++) {
                String[] Lotlist=IRNoList[j].toString().trim().split(Pattern.quote("|"));
                String[] OpenQtylist=Lotlist[1].toString().trim().split(Pattern.quote("~"));
                Lotno=OpenQtylist[0].toString().trim();
                IRno=Lotlist[0].toString().trim();

                final View view2 = LayoutInflater.from(this).inflate(R.layout.salereturnentrypanel, null);
                {
                    view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                    TextView txtproducthide = (TextView) view2.findViewById(R.id.txtproducthide);
                    TextView lblLotno1 = (TextView) view2.findViewById(R.id.lblLotno);
                    TextView lblIRno1 = (TextView) view2.findViewById(R.id.lblIRno);
                    final TextView lblOpenQty1 = (TextView) view2.findViewById(R.id.lblOpenQty);
                    lblLotno1.setText(Lotno);
                    lblIRno1.setText(IRno);
                    lblOpenQty1.setText(OpenQtylist[1].toString().trim());
                    final EditText lblReturnQty1= (EditText) view2.findViewById(R.id.lblReturnQty);
                    GeneralMaster gm = (GeneralMaster) spproductlist.getSelectedItem();
                    txtproducthide.setText(gm.Code().toString());
                    cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                    String[] prdarray=cmbproductlist.split("\\|");
                    matnrno=prdarray[0].toString();

                    //AREA CALULATION ON MANUAL Entry
                    lblReturnQty1.addTextChangedListener(new TextWatcher() {
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {



                                if (listenForChanges)
                                {
                                    listenForChanges = false;
                                    if (Float.parseFloat(lblOpenQty1.getText().toString()) > 0)
                                    {

                                        if (s.length() != 0)
                                        { //do your work here }

                                            Float currentst = Float.parseFloat(String.valueOf(s));
                                            Float receivestock = Float.parseFloat(String.valueOf(lblOpenQty1.getText()));
                                            if (currentst > receivestock) {
                                                msclass.showMessage("return packet quantity should not more than open packet  quantity.");
                                                lblReturnQty1.setText("");
                                            }


                                        }
                                    }
                                    else {
                                        msclass.showMessage("Please select IR Number.");
                                        lblReturnQty1.setText("");

                                    }

                                    listenForChanges = true;


                                }
                            }
                            catch (Exception ex)
                            {
                                msclass.showMessage(ex.getMessage());
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
                    Button btndelete = (Button) view2.findViewById(R.id.btndelete);
                    btndelete.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) view2.getParent()).removeView(view2);
                            // ordercalculation();
                        }
                    });

                    int addcount=0;
                    for (int i = 1; i < my_linear_layout1.getChildCount(); i++)
                    {
                        View viewcheck = my_linear_layout1.getChildAt(i);
                        TextView txtproducthidec=(TextView) viewcheck.findViewById(R.id.txtproducthide);
                        TextView lblLotnoc = (TextView) viewcheck.findViewById(R.id.lblLotno);
                        final TextView lblOpenQtyc = (TextView) viewcheck.findViewById(R.id.lblOpenQty);
                        TextView lblIRnoc = (TextView) viewcheck.findViewById(R.id.lblIRno);
                        cmbproductlist=txtproducthidec.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                        String[] prdarrayc=cmbproductlist.split("\\|");
                        String MATNR=prdarrayc[0].toString();
                        // String MAKTX=prdarray[1].toString();
                        // String MTART=prdarray[2].toString();
                        // String MATKL=prdarray[3].toString();
                        //String PRICE=prdarray[4].toString();
                        String QTYPKT=lblOpenQtyc.getText().toString();
                        String LOTNO=lblLotnoc.getText().toString();
                        String IRNO=lblIRnoc.getText().toString();
                        if(IRno.equalsIgnoreCase(IRNO) && Lotno.equalsIgnoreCase(LOTNO)&&
                                matnrno.equalsIgnoreCase(MATNR))
                        {
                            addcount=addcount+1;
                        }

                    }
                    if (addcount==0) {
                        my_linear_layout1.addView(view2);
                    }
                }
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }



    public boolean validation() {


        GeneralMaster sp = (GeneralMaster) spsaleorg.getSelectedItem();
        if (sp.Code().equals("0"))
        {
            msclass.showMessage("Please select organisation.") ;
            return false ;
        }

        GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();
        if (dd.Code().equals("0"))
        {
            msclass.showMessage("Please select distributor.") ;
            return false ;
        }
        GeneralMaster sd = (GeneralMaster) spdivision.getSelectedItem();
        if (sd.Code().equals("0"))
        {
            msclass.showMessage("Please select division.") ;
            return false ;
        }
        GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
        if (pp.Code().equals("0"))
        {
            msclass.showMessage("Please select product.") ;
            return false ;
        }
        productcode=pp.Code(); //Only Nine Character

               /* if (TextUtils.isEmpty(txtQty.getText())) {
                    txtQty.setError("Please enter packet qauntity.");
                    return ;
                }
                if (Integer.valueOf(txtQty.getText().toString())==0) {
                    txtQty.setError("Please enter packet qauntity.");
                    return ;
                }*/
       /* if (txtlotno.getText().toString().length()==0) {
            txtlotno.setError("Please enter Lot number .");
            return false ;
        }
        if (txtexpiry.getText().toString().length()==0) {
            txtexpiry.setError("Please enter expiry date .");
            return false ;
        }*/




//        if (!isAlreadydone(mno)) {
//            Utility.showAlertDialog("Info", "This mobile no already exists", context);
//            return false;
//        }


        return true;
    }
    private void setDateTimeField(View v) {
        final EditText txt=(EditText)v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();
        // txt.setText(dateFormatter.format(dateSelected.getTime()));


    }
    private  void BindIntialData() {

        try {






            spReason.setAdapter(null);
            List<GeneralMaster> gmR = new ArrayList<GeneralMaster>();
            gmR.add(new GeneralMaster("0", "Select the reason"));
            gmR.add(new GeneralMaster("Season Over", "Season Over"));
            gmR.add(new GeneralMaster("Product Expired", "Product Expired"));
            gmR.add(new GeneralMaster("Quality Issue", "Quality Issue"));
            gmR.add(new GeneralMaster("Lack of Demand", "Lack of Demand"));
            gmR.add(new GeneralMaster("Other", "Other"));

            ArrayAdapter<GeneralMaster> adapterR = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gmR);
            adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spReason.setAdapter(adapterR);

            lbldistributorShipto.setVisibility(View.GONE);
            card_month04.setVisibility(View.GONE);
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
            bindIRNumberDetails("");
            bindLotdetail("");

            spdepot.setAdapter(null);
            List<GeneralMaster> Croplist5 = new ArrayList<GeneralMaster>();
            Croplist5.add(new GeneralMaster("0",
                    "Select Depot"));
            ArrayAdapter<GeneralMaster> adapter5 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist5);
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdepot.setAdapter(adapter5);
            if(config.NetworkConnection()) {
                new GetDepot("1", usercode, "1", "1", "1").execute();
            }
            else
            {
                msclass.showMessage("internet connection not available.\n please check internet connection");
            }
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    private void bindLotnumberList()//LinearLayout my_linear_layout1)
    {
        if (validation()) {


            try {// 2 as get INVOICE number list from SaP
                if (config.NetworkConnection()) {

                    GeneralMaster gm = (GeneralMaster) spsaleorg.getSelectedItem();
                    GeneralMaster gd = (GeneralMaster) spdivision.getSelectedItem();
                    GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                    GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();
                    division = URLEncoder.encode(gd.Code(), "UTF-8");
                    org = URLEncoder.encode(gm.Code(), "UTF-8");
                    cmbDistributor = dd.Code();
                    String[] distr = cmbDistributor.split("\\|");
                    customer = distr[0].toString();
                    name = URLEncoder.encode(distr[1].toString(), "UTF-8");
                    DLV_plant = distr[2].toString();
                    customregroup = distr[3].toString();
                    lotno ="";// URLEncoder.encode(txtlotno.getText().toString(), "UTF-8");
                    new Salereturnorderdata("6", null, usercode, org, division, customer, name,
                            DLV_plant, customregroup, "", lotno).execute();
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }


        }

        //en
    }
    private void addrow()//LinearLayout my_linear_layout1)
    {


        try {// 2 as get INVOICE number list from SaP
            if (config.NetworkConnection()) {

                GeneralMaster gm = (GeneralMaster) spsaleorg.getSelectedItem();
                GeneralMaster gd = (GeneralMaster) spdivision.getSelectedItem();
                GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                GeneralMaster dd = (GeneralMaster) spdistributor.getSelectedItem();
                division = URLEncoder.encode(gd.Code(), "UTF-8");
                org=URLEncoder.encode(gm.Code(), "UTF-8");
                cmbDistributor=dd.Code();
                String[] distr=cmbDistributor.split("\\|");
                customer=distr[0].toString();
                name=URLEncoder.encode(distr[1].toString(), "UTF-8");
                DLV_plant=distr[2].toString();
                customregroup=distr[3].toString();
                lotno="";//URLEncoder.encode(txtlotno.getText().toString(), "UTF-8");
                new Salereturnorderdata("2", null, usercode, org, division, customer, name,
                        DLV_plant, customregroup, "",lotno).execute();
            }
            else
            {
                msclass.showMessage("Internet network not available.");
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }




        //en
    }

    public void bindLotdetail(String Result)
    {
        String[] array;


        try {

            // spIRno1.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select IRNo "));
            if (Result.isEmpty())
            {
                array = new String[1];
                array[0] = "SELECT LOT NUMBER";
                if (array.length > 0) {
                    spLotNumber.setItems(array);
                    spLotNumber.hasNoneOption(true);
                    spLotNumber.setSelection(new int[]{0});
                }
            }
            else {
                if (config.NetworkConnection()) {
                    try {
                        JSONObject object = new JSONObject(Result);
                        JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                        array = new String[jArray.length() + 1];
                        array[0] = "SELECT LOT NUMBER";
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            array[i + 1] = jObject.getString("LOTNO").toString();
                        }

                        if (array.length > 0) {
                            spLotNumber.setItems(array);
                            spLotNumber.hasNoneOption(true);
                            spLotNumber.setSelection(new int[]{0});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            }
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spLotNumber.setAdapter(adapter);

        }
        catch (Exception ex)
        {

        }



    }

    public void bindIRNumberDetails(String Result)
    {
        String[] array;


        try {


            if (Result.isEmpty())
            {
                array = new String[1];
                array[0] = "SELECT IR NUMBER";
                if (array.length > 0) {
                    spIRNumber.setItems(array);
                    spIRNumber.hasNoneOption(true);
                    spIRNumber.setSelection(new int[]{0});
                }
            }
            else {

                if (config.NetworkConnection()) {
                    try {
                        JSONObject object = new JSONObject(Result);
                        JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                        array = new String[jArray.length() + 1];
                        array[0] = "SELECT IR NUMBER";
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            array[i + 1] = jObject.getString("IRNumber").toString();
                        }

                        if (array.length > 0) {
                            spIRNumber.setItems(array);
                            spIRNumber.hasNoneOption(true);
                            spIRNumber.setSelection(new int[]{0});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            }


        }
        catch (Exception ex)
        {

        }
    }
    public void bindIRDetails(String Result)
    {
        //Star
        try {

            int addcount=0;
            for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                View view2 = my_linear_layout1.getChildAt(i);
                TextView txtproduct = (TextView) view2.findViewById(R.id.txtproduct);
                if(txtproduct.getText().toString().contains(spproductlist.getSelectedItem().toString()))
                {
                    addcount=addcount+1;
                }
            }
            GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
            cmbproductlist=pp.Code();
            String[] prdarray=null;
            String PRICE="0";

            if (addcount>0)
            {
                msclass.showMessage("This  product "+spproductlist.getSelectedItem().toString()+" already added in list .");
                // txtQty.setText("");
            }
            else {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                int count = my_linear_layout1.getChildCount();
                count = count + 1;
                final View view2 = LayoutInflater.from(this).inflate(R.layout.salereturncardview, null);
                view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                TextView txtproduct1 = (TextView) view2.findViewById(R.id.txtproduct);
                TextView txtSn1 = (TextView) view2.findViewById(R.id.txtSn);
                TextView txtlotnumber1 = (TextView) view2.findViewById(R.id.txtlotnumber);
                TextView txtExpiry1 = (TextView) view2.findViewById(R.id.txtExpiry);
                final TextView txtQtyvalue1 = (TextView) view2.findViewById(R.id.txtQtyvalue);
                final TextView txtopen1 = (TextView) view2.findViewById(R.id.txtopen);
                final SearchableSpinner spIRno1 = (SearchableSpinner) view2.findViewById(R.id.spIRno);
                BindIRNo(spIRno1,Result);
                spIRno1 .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                        try {

                            GeneralMaster orgm = (GeneralMaster) spIRno1.getSelectedItem();
                            txtopen1.setText(orgm.Code());
                            txtQtyvalue1.setText("0");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
                        // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
                    }
                });




                TextView txtproducthide = (TextView) view2.findViewById(R.id.txtproducthide);
                //  txtlotnumber1.setText(txtlotno.getText().toString());
                // txtExpiry1.setText(txtexpiry.getText().toString());
                // txtQtyvalue1.setText(txtQty.getText().toString());
                txtproduct1.setText(String.valueOf(count) + ". " + spproductlist.getSelectedItem().toString());
                GeneralMaster gm = (GeneralMaster) spproductlist.getSelectedItem();
                txtproducthide.setText(gm.Code().toString());
                cmbproductlist=gm.Code();
                prdarray=cmbproductlist.split("\\|");
                // PRICE=prdarray[4].toString();

                Button btndelete = (Button) view2.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) view2.getParent()).removeView(view2);
                        // ordercalculation();
                    }
                });

                //AREA CALULATION ON MANUAL Entry
                txtQtyvalue1.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        try {



                            if (listenForChanges)
                            {
                                listenForChanges = false;
                                if (Float.parseFloat(txtopen1.getText().toString()) > 0)
                                {

                                    if (s.length() != 0)
                                    { //do your work here }

                                        Float currentst = Float.parseFloat(String.valueOf(s));
                                        Float receivestock = Float.parseFloat(String.valueOf(txtopen1.getText()));
                                        if (currentst > receivestock) {
                                            msclass.showMessage("return packet quantity should not more than open packet  quantity.");
                                            txtQtyvalue1.setText("");
                                        }


                                    }
                                }
                                else {
                                    msclass.showMessage("Please select IR Number.");
                                    txtQtyvalue1.setText("");

                                }

                                listenForChanges = true;


                            }
                        }
                        catch (Exception ex)
                        {
                            msclass.showMessage(ex.getMessage());
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



                my_linear_layout1.addView(view2);
                //txtQty.setText("");
                // txtexpiry.setText("");
                //txtlotno.setText("");
                Toast.makeText(context, "Product is added", Toast.LENGTH_SHORT).show();

            }
            //ordercalculation();
        }

        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

        //end




    }
    public void  BindIRNo(SearchableSpinner spIRno1,String Result)
    {
        try {

            spIRno1.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select IRNo "));

            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(Result);
                    JSONArray jArray = object.getJSONArray("Table");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("OPENQTYPKT"),
                                jObject.getString("IRNO")
                        ));
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msclass.showMessage("Internet network not available.");
            }
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spIRno1.setAdapter(adapter);

        }
        catch (Exception ex)
        {

        }
    }
    public void ordercalculation()
    {   int totalvalue=0;
        String[] prdarray=null;
        String PRICE="0";
        // calculation
        for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
            View view2 = my_linear_layout1.getChildAt(i);
            TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
            TextView txtproducthide=(TextView) view2.findViewById(R.id.txtproducthide);
            cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
            prdarray=cmbproductlist.split("\\|");
            PRICE=prdarray[4].toString();
            totalvalue=totalvalue+(Integer.valueOf(txtqty.getText().toString())*
                    Integer.valueOf((int)Double.parseDouble(PRICE)));

        }

        txttotalvalue.setText("TOTAL ORDER VALUE :"+String.valueOf(totalvalue));
    }

    public void  BindDepot(String str)
    {
        try {
            spdepot.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Depot"));
            if (config.NetworkConnection()) {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Croplist.add(new GeneralMaster(jObject.getString("Depot_Code"),
                                jObject.getString("Depot_Name"))
                        );
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
            spdepot.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }
    public void  Binddistributor(String str)
    {
        try {
            spdistributor.setAdapter(null);
            spdistributorshipto.setAdapter(null);
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
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spdistributor.setAdapter(adapter);
            spdistributorshipto.setAdapter(adapter);
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

                        Croplist.add(new GeneralMaster(jObject.getString("MATNR")+"|"+jObject.getString("MAKTX")+"|"+jObject.getString("MTART")+"" +
                                "|"+jObject.getString("MATKL")+"|"+jObject.getString("PRICE"),
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
                postParameters.add(new BasicNameValuePair("Type", "GetProductListSR"));
            }
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= saleorderurl+"?usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&cmbDistributor="+cmbDistributor;
            Log.i("URL",Urlpath1);
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
                cleardata();
                pd.dismiss();
                // if (result.contains("True")) {
                if (action.equals("1"))
                {
                    Binddistributor(result);
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
    public class Salereturnorderdata extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,
                returnstring,action,cmbDistributor,DLV_plant,
                name,customregroup,Comments,lotno;
        byte[] objAsBytes;
        public Salereturnorderdata( String action,byte[] objAsBytes, String usercode,String saleorg,
                                    String division,String customer,String name ,String DLV_plant,
                                    String customregroup ,String Comments,String lotno  ){
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
            this.lotno=lotno;

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
            String encodeImage="";
            if(objAsBytes!=null) {
                encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            }
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SaleReturnOrder"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            postParameters.add(new BasicNameValuePair("productcode",productcode));
            String Urlpath1= testurl+"?orderid="+""+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&customer="+customer+"" +
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"" +
                    "&name="+name+"" +
                    "&Comments="+Comments+"&actionby=TBM&reason="+drpreason+"&lotno="+lotno+"&Expiry=20-02-2020&Qtypkt=0";
          Log.i("Lot Number url",Urlpath1+"--"+productcode);
          Log.i("Lot Number url",encodeImage);
            HttpPost httppost = new HttpPost(Urlpath1);
            // HttpPost httppost = new HttpPost("");
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
                Log.i("Result is ",result);
                // JSONObject jsonObject = new JSONObject(result);
                pd.dismiss();
                if (result.contains("True"))
                {
                    if(action=="1") {
                        msclass.showMessage("Your sales return request has been successfully submitted to " +
                                "your L1 for Approval");
                        my_linear_layout1.removeAllViews();
                        txtComments.setText("");
                        txttotalvalue.setText("");
                        cleardata();
                        spproductlist.setSelection(0);
                        spReason.setSelection(0);
                    }
                    if(action=="2")
                    {
                        bindIRDetails(result) ;
                    }
                    if(action=="6")
                    {
                        bindLotdetail(result) ;
                    }
                    if(action=="7")
                    {
                        //  spIRNumber
                        bindIRNumberDetails(result);
                    }
                }
                else
                {

                    //JSONObject object = new JSONObject(result);
                    //JSONArray jArray = object.getJSONArray("Table");
                    //JSONObject jObject = jArray.getJSONObject(0);
                    msclass.showMessage(config.returnmsg(result));
                    cleardata();

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
    public void  cleardata()
    {   String[] array;
        array = new String[1];
        array[0] = "SELECT LOT NUMBER";
        if (array.length > 0) {
            spLotNumber.setItems(array);
            spLotNumber.hasNoneOption(true);
            spLotNumber.setSelection(new int[]{0});
        }
        array[0] = "SELECT IR NUMBER";
        if (array.length > 0) {
            spIRNumber.setItems(array);
            spIRNumber.hasNoneOption(true);
            spIRNumber.setSelection(new int[]{0});
        }
    }
    public class GetDepot extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,returnstring,action,cmbDistributor;

        public GetDepot( String action, String usercode,String saleorg,String division,String cmbDistributor  ){
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
            postParameters.add(new BasicNameValuePair("Type", "MDO_GetDepotmaster"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            //String Urlpath1= saleorderurl+"?usercode="+usercode+"&saleorg="+saleorg+"" +
            //       "&division="+division+"&cmbDistributor="+cmbDistributor;
            String Urlpath1= cx.MDOurlpath+"?usercode="+usercode+"&saleorg="+saleorg+"" +
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

                if(result.contains("OK"))
                {
                    lbldistributorShipto.setVisibility(View.VISIBLE);
                    card_month04.setVisibility(View.VISIBLE);
                }
                BindDepot(result);

            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }


}

