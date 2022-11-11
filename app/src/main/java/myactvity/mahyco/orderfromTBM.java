package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class orderfromTBM extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtIrrigationdt,txtirrigation,
            txtQty,txtComments;
    public SearchableSpinner spproductlist,spdistributor,spdistributorshipto,spdivision,spsaleorg,spdepot;
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

    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    SharedPreferences.Editor editor, locedit;
    String  saleorg,customer, name , DLV_plant, customregroup,cmbproductlist;
    int check2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfrom_tbm);
        getSupportActionBar().hide(); //<< this
        context = this;

        Toast.makeText(context,"Create Sales Order",Toast.LENGTH_LONG).show();

        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass=new Messageclass(this);
        my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        txtQty=(EditText)findViewById(R.id.txtQty);
        txttotalvalue=(TextView)findViewById(R.id.txttotalvalue);
        txtComments=(EditText)findViewById(R.id.txtComments);
        spproductlist=(SearchableSpinner)findViewById(R.id.spproductlist);
        spdistributor=(SearchableSpinner)findViewById(R.id.spdistributor);
        spdistributorshipto=(SearchableSpinner)findViewById(R.id.spdistributorshipto);
        spdepot=(SearchableSpinner)findViewById(R.id.spdepot);
        spdivision=(SearchableSpinner)findViewById(R.id.spdivision);
        spsaleorg=(SearchableSpinner)findViewById(R.id.spsaleorg);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnTakephoto=(Button)findViewById(R.id.btnTakephoto);
        lbldistributorShipto=(TextView)findViewById(R.id.lbldistributorShipto);
        card_month04=(CardView)findViewById(R.id.card_month04);
        btnadd=(Button)findViewById(R.id.btnadd);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(btnadd, this);
        Utility.setRegularFont(txtQty, this);
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

                //if (TextUtils.isEmpty(txtFertiliser.getText())) {
                //    txtFertiliser.setError("Please enter fertiliser name");
                //    return ;
                // }

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
                GeneralMaster pp = (GeneralMaster) spproductlist.getSelectedItem();
                if (pp.Code().equals("0"))
                {
                    msclass.showMessage("Please select product.") ;
                    return ;
                }
                if (TextUtils.isEmpty(txtQty.getText())) {
                    txtQty.setError("Please enter packet qauntity.");
                    return ;
                }
                if (Integer.valueOf(txtQty.getText().toString())==0) {
                    txtQty.setError("Please enter packet qauntity.");
                    return ;
                }
                int remain=0;
                if (spproductlist.getSelectedItem().toString().toLowerCase().contains("cotton"))
                {
                    //remain=Integer.valueOf(txtQty.getText().toString())%24;
                    if(remain>0)
                    {
                        // msclass.showMessage("Please enter packet quantity according company standard packing(24 pkt each box)");
                        // return ;
                    }

                }
                addrow();

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

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

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
                    GeneralMaster shipto = (GeneralMaster) spdistributorshipto.getSelectedItem();
                    if (dd.Code().equals("0"))
                    {
                        msclass.showMessage("Please select distributor.") ;
                        return ;
                    }
                    if (shipto.Code().equals("0"))
                    {
                        msclass.showMessage("Please select ship to distributor.") ;
                        return ;
                    }

                    if (my_linear_layout1.getChildCount()==0) {
                        msclass.showMessage("Please add product list");
                        return;
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

                        for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
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
                            String PRICE=prdarray[4].toString();
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
                            jo.put("PRICE", PRICE);

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


                        new Saleorderdata("1",objAsBytes,usercode,org,division,customer,name,
                                DLV_plant,customregroup,Comments,shiptocustomer).execute();


                    }
                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });


        BindIntialData();
        //Binddistributor();
    }
    private  void BindIntialData() {

        try {
            lbldistributorShipto.setVisibility(View.GONE);
            card_month04.setVisibility(View.GONE);
            spsaleorg.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "Select Organisation"));
            gm.add(new GeneralMaster("1000", "Mahyco"));
            gm.add(new GeneralMaster("1100", "Sungro"));

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spsaleorg.setAdapter(adapter);

            spdivision.setAdapter(null);
            List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
            gm2.add(new GeneralMaster("0", "Select Division"));
            gm2.add(new GeneralMaster("01", "01-Vegetable Crop"));
            gm2.add(new GeneralMaster("02", "02-Field Crop"));
            gm2.add(new GeneralMaster("03", "03-Cotton Crop"));
            ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, gm2);
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
    private void addrow()//LinearLayout my_linear_layout1)
    {

        //st
        try {

            if(my_linear_layout1.getChildCount()==0) {
                final View view1 = LayoutInflater.from(this).inflate(R.layout.saleorderrow, null);
                view1.setBackgroundColor(getResources().getColor(R.color.lightgray));
                EditText txtproduct1 = (EditText) view1.findViewById(R.id.txtproduct);
                TextView txtqty1 = (TextView) view1.findViewById(R.id.txtqty);
                TextView lblValue1 = (TextView) view1.findViewById(R.id.lblValue);
                Button btndelete = (Button) view1.findViewById(R.id.btndelete);
                btndelete.setVisibility(View.INVISIBLE);
                txtproduct1.setText("PRODUCT NAME");
                txtqty1.setText("QTY");
                lblValue1.setText("VALUE");
                txtproduct1.setTextColor(Color.WHITE);
                txtqty1.setTextColor(Color.WHITE);
                lblValue1.setTextColor(Color.WHITE);
                view1.setBackgroundColor(getResources().getColor(R.color.heback));
                my_linear_layout1.addView(view1);
            }

            int addcount=0;
            for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                View view2 = my_linear_layout1.getChildAt(i);
                EditText txtproduct = (EditText) view2.findViewById(R.id.txtproduct);
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
                txtQty.setText("");
            }
            else {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                int count = my_linear_layout1.getChildCount();
                count = count + 1;
                final View view2 = LayoutInflater.from(this).inflate(R.layout.saleorderrow, null);
                view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                EditText txtproduct = (EditText) view2.findViewById(R.id.txtproduct);
                TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
                TextView lblValue = (TextView) view2.findViewById(R.id.lblValue);
                TextView txtproducthide = (TextView) view2.findViewById(R.id.txtproducthide);
                txtproduct.setText(String.valueOf(count) + ". " + spproductlist.getSelectedItem().toString());
                GeneralMaster gm = (GeneralMaster) spproductlist.getSelectedItem();
                txtproducthide.setText(gm.Code().toString());
                cmbproductlist=gm.Code();
                prdarray=cmbproductlist.split("\\|");
                PRICE=prdarray[4].toString();
                txtqty.setText(txtQty.getText());
                lblValue.setText(String.valueOf(Integer.valueOf(txtQty.getText().toString())*
                        Integer.valueOf((int)Double.parseDouble(PRICE))));
                Button btndelete = (Button) view2.findViewById(R.id.btndelete);
                btndelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) view2.getParent()).removeView(view2);
                        ordercalculation();
                    }
                });
                my_linear_layout1.addView(view2);
                txtQty.setText("");
                Toast.makeText(context, "Product is added", Toast.LENGTH_SHORT).show();

            }
            ordercalculation();
        }

        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }



        //en
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
    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
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

            catch (Exception e) {
                //e.printStackTrace();
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
    public class Saleorderdata extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,
                returnstring,action,cmbDistributor,DLV_plant,
                name,customregroup,Comments,shiptocustomer;
        byte[] objAsBytes;

        public Saleorderdata( String action,byte[] objAsBytes, String usercode,String saleorg,
                              String division,String customer,String name ,String DLV_plant,
                              String customregroup ,String Comments,String shiptocustomer  ){
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
            this.shiptocustomer=shiptocustomer;

        }
        protected void onPreExecute() {
            // pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
             //pd.setCancelable(false);
             pd.setIndeterminate(true);
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
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"" +
                    "&name="+name+"" +
                    "&Comments="+Comments+"&actionby=TBM&shipto="+shiptocustomer+"";
            Log.i("URLs",Urlpath1);
            Log.i("Encodedata:",postParameters.toString());
            HttpPost httppost = new HttpPost(Urlpath1);
            //  HttpPost httppost = new HttpPost("");
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
                    txttotalvalue.setText("");
                    if(pref!=null){
                        logSalesOrderTotal(context);

                        if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                            String unitID = pref.getString("unit", "");
                            if(unitID.equalsIgnoreCase("VCBU")){
                                logSalesOrderVCBU(context);
                            }
                            else{
                                logSalesOrderRCBU(context);
                            }
                        }
                    }
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

    private void logSalesOrderTotal(Context context){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(context).callSaleOrderToMapHelpDeskTotal(userId,displayName);
            }
        }
    }

    private void logSalesOrderRCBU(Context context){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(context).callSaleOrderToMapHelpDeskRCBU(userId,displayName);
            }
        }
    }

    private void logSalesOrderVCBU(Context context){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(context).callSaleOrderToMapHelpDeskVCBU(userId,displayName);
            }
        }
    }

}
