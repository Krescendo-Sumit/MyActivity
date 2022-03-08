package myactvity.mahyco;

import android.app.DatePickerDialog;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.regex.Pattern;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class distributorASRetailerCompetitatorPOGupdate extends AppCompatActivity {

    ProgressDialog pd;
    public SearchableSpinner spCropType;
    private SqliteDatabase mDatabase;
    Context context;
    public Messageclass msclass;
    Button btnsave;
    EditText txtcompname,txtbalance,txtrecstock,txtsoldtofarmer;
    private SimpleDateFormat dateFormatter;
    LinearLayout my_linear_layout1;
    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    String croptype,Crop_Code;
    String state,dist,taluka,mktplace,RetailerMobileno,DistrCode;
    private boolean listenForChanges = true;
    String strdate,usercode;
    int check=0;
    public CommonExecution cx;
    Config config;
    byte[] objAsBytes = null;//new byte[10000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_asretailer_competitator_pogupdate) ;
        getSupportActionBar().hide(); //<< this
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode=pref.getString("UserID", null);
        cx=new CommonExecution(this);
        config = new Config(this); //Here the context is passing

        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        btnsave= (Button)findViewById(R.id.btnsave);
        my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        context=this;
        txtcompname=(EditText)findViewById(R.id.txtcompname);
        txtrecstock=(EditText)findViewById(R.id.txtrecstock);
        txtsoldtofarmer=(EditText)findViewById(R.id.txtsoldtofarmer);
        txtbalance=(EditText)findViewById(R.id.txtbalance);
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
        DistrCode=bundle.getString("DistrCode");
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
                    Toast.makeText(distributorASRetailerCompetitatorPOGupdate.this,ex.toString(),Toast.LENGTH_LONG).show();
                }


                 check = check + 1;
                 if (check > 1)
                {
                   // BindSaveRecord(Crop_Code,croptype);
                    JSONObject mainObj = new JSONObject();
                    //byte[] objAsBytes = null;//new byte[10000];
                    String cropcode="-";
                    String cropname="";
                    try {
                        objAsBytes = cropcode.getBytes("UTF-8");
                        cropcode= URLEncoder.encode(Crop_Code, "UTF-8");
                        cropname=URLEncoder.encode(croptype, "UTF-8");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(config.NetworkConnection()) {
                        new UpdatedPOG("5", objAsBytes, usercode, cropcode,
                                cropname,DistrCode).execute();

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
                Toast.makeText(distributorASRetailerCompetitatorPOGupdate.this, "gngvnv", Toast.LENGTH_LONG).show();
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



                    UpdateRetailerPOG(Crop_Code);






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

    public void UpdateRetailerPOG(String crop_Code)
    {
        try {
            final String crop = spCropType.getSelectedItem().toString();
            //St
            int count=0;

            JSONObject object = new JSONObject();
           /* String searchQuery = "SELECT * from DistributorAsRetailerCompetitatorPOGTable " +
                    " where CropName='" + croptype + "' " +
                    "and  DistrCode='"+DistrCode+"'" +
                   // "and CompanyName='"+txtcompname.getText().toString().trim()+"'" +
                    "  ";*/
            String searchQuery = "SELECT * from DistributorAsRetailerCompetitatorPOGTable " +
                    " where CropCode='" + crop_Code + "' " +
                    "and  DistrCode='"+DistrCode+"'" +
                    // "and CompanyName='"+txtcompname.getText().toString().trim()+"'" +
                    "  ";
            object.put("Table1", mDatabase.getResults( searchQuery));

            JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            // (jArray.length()==0) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            final String strdate = dateFormat.format(d);
            // Bundle bundle = data.getExtras();
           // mDatabase.deleterecord("delete from DistributorAsRetailerCompetitatorPOGTable" +
             //       " where CropName='" + croptype + "' " +
              //      "and  DistrCode='"+DistrCode+"' ");
            mDatabase.deleterecord("delete from DistributorAsRetailerCompetitatorPOGTable" +
                    " where CropCode='" + crop_Code + "' " +
                    "and  DistrCode='"+DistrCode+"' ");

            for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {

                View view2 = my_linear_layout1.getChildAt(i);
                final EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                final EditText txtsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                final EditText txtcompname1 = (EditText) view2.findViewById(R.id.txtcompname);
                final EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);

                String compname = txtcompname1.getText().toString();
                 if(compname.length() !=0 && txtsoldtofarmer1.getText().length() !=0 &&
                         lblreceived1.getText().length() !=0 &&
                         lblbal1.getText().length() !=0) {
                     boolean f = mDatabase.InsertDistributorAsRetailerCompetitatorPOGData(pref.getString("UserID", null),
                             state, dist, taluka, mktplace, DistrCode,
                             crop.trim(), crop_Code, compname.trim(),
                             lblreceived1.getText().toString(), "",
                             "", txtsoldtofarmer1.getText().toString(),
                             lblbal1.getText().toString(), strdate.trim()
                     );
                     if (f == true) {
                         count++;
                     } else {

                     }
                 }
            }

            //ene

            if (count > 0) {
                //msclass.showMessage("Retailer POG data updated successfully.");
                new SyncPOGData_Async("MDO_POGData","DistributorAsRetailer3").execute();

                // spCropType.setSelection(0);
                //txtrecstock.setText("");
                //txtsoldtofarmer.setText("");
                //txtbalance.setText("");
                // txtrecstock.setText("");
                // my_linear_layout1.removeAllViews();
               // BindSaveRecord(crop_Code,croptype);

            } else {
                msclass.showMessage("Please check entry data (product name ,received stock,sold stock should not empty)");
            }

            //}
            //  else
            //  {
            //    msclass.showMessage("company of '"+txtcompname.getText().toString()+"' already saved crop "+spCropType.getSelectedItem()+" data.");

            // }
        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.toString());

        }
    }



    public void BindSaveRecord(final String cropcode,final String crop,String result)
    {
        try {

            my_linear_layout1.removeAllViews();                              //add this too
            //st
            try {
                //JSONObject object = new JSONObject();
               /* String searchQuery = "SELECT distinct  CropName," +
                        "CompanyName,RecStock,SoldStock,BalStock   " +
                        " from  DistributorAsRetailerCompetitatorPOGTable  " +
                        " where CropName='" + croptype + "'" +
                        " and DistrCode='"+DistrCode+"'  ";*/
                String searchQuery = "SELECT distinct  CropName,CropCode," +
                        "CompanyName,RecStock,SoldStock,BalStock   " +
                        " from  DistributorAsRetailerCompetitatorPOGTable  " +
                        " where CropCode='" + cropcode + "'" +
                        " and DistrCode='"+DistrCode+"'  ";
                JSONObject object = new JSONObject(result);
                // JSONObject object = new JSONObject();
                //object.put("Table1", mDatabase.getResults(searchQuery));
                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                String  str="a";
                String[] splittedValues = str.split(",");//spcrop.getSelectedItemsAsString().split(",");

                //HEADER START
                final View view1 = LayoutInflater.from(this).inflate(R.layout.competitatorpoggridviewshow, null);
                view1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                EditText lblreceived = (EditText) view1.findViewById(R.id.lblreceived);
                EditText lblsoldtofarmer = (EditText) view1.findViewById(R.id.lblsoldtofarmer);
                EditText txtcompname = (EditText) view1.findViewById(R.id.txtcompname);
                EditText lblbal = (EditText) view1.findViewById(R.id.lblbal);
                //Button btnEdit = (Button) view1.findViewById(R.id.btnEdit);
                // btnEdit.setVisibility(View.INVISIBLE);
                txtcompname.setText("COMPANY NAME");
                txtcompname.setEnabled(false);
                txtcompname.setTextColor(Color.WHITE);
                txtcompname.setBackgroundColor(Color.TRANSPARENT);
                txtcompname.setTypeface(txtcompname.getTypeface(), Typeface.BOLD);

                lblreceived.setText("RECEIVED STOCK");
                lblreceived.setEnabled(false);
                lblreceived.setSingleLine(false);
                lblreceived.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblreceived.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblreceived.setMaxLines(10);
                lblreceived.setTextColor(Color.WHITE);
                lblreceived.setBackgroundColor(Color.TRANSPARENT);
                // lblcomp.setTextSize(14);
                lblreceived.setTypeface(lblreceived.getTypeface(), Typeface.BOLD);

                lblsoldtofarmer.setText("SOLD STOCK");
                lblsoldtofarmer.setEnabled(false);
                lblsoldtofarmer.setSingleLine(false);
                lblsoldtofarmer.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblsoldtofarmer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblsoldtofarmer.setMaxLines(10);
                lblsoldtofarmer.setTextColor(Color.WHITE);
                lblsoldtofarmer.setBackgroundColor(Color.TRANSPARENT);

                lblsoldtofarmer.setTypeface(lblsoldtofarmer.getTypeface(), Typeface.BOLD);

                lblbal.setText("BALANCE STOCK");
                lblbal.setEnabled(false);
                lblbal.setSingleLine(false);
                lblbal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
                lblbal.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblbal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblbal.setMaxLines(10);
                lblbal.setTextColor(Color.WHITE);
                lblbal.setBackgroundColor(Color.TRANSPARENT);
                //  lblqty.setTextSize(14);
                lblbal.setTypeface(lblbal.getTypeface(), Typeface.BOLD);
                view1.setBackgroundColor(getResources().getColor(R.color.heback));
                my_linear_layout1.addView(view1);
                // HEADER END

                //Str
                for (int i = 0; i < 4; i++) {

                    int total = 0;
                    final View view2 = LayoutInflater.from(this).inflate(R.layout.competitatorpoggridviewshow, null);
                    view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    final  EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                    final  EditText lblsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                    final  EditText txtcompname1 = (EditText) view2.findViewById(R.id.txtcompname);
                    final   EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);

                    if(i==3)
                    {
                        txtcompname1.setHint(" OTHERS");
                    }
                    if(crop.toUpperCase().contains("COTTON"))
                    {
                        lblbal1.setHint("PKT");
                        lblsoldtofarmer1.setHint("PKT");
                        lblreceived1.setHint("PKT");

                    }
                    else
                    {
                        lblbal1.setHint("KG");
                        lblsoldtofarmer1.setHint("KG");
                        lblreceived1.setHint("KG");
                    }
                    lblreceived1.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {

                                if (listenForChanges)
                                {
                                    listenForChanges = false;
                                    if (s.length() != 0) { //do your work here }

                                        if(lblbal1.getText().length()==0)
                                        {
                                            lblbal1.setText("0");
                                            //lblsoldtofarmer1.setText(lblreceived1.getText());
                                            lblsoldtofarmer1.setText("0");
                                        }
                                        if (lblreceived1.getText().length() != 0) {

                                            if (Integer.valueOf(lblsoldtofarmer1.getText().toString())>Integer.valueOf(lblreceived1.getText().toString()))
                                            {

                                                lblsoldtofarmer1.setText("0");
                                                lblbal1.setText(lblreceived1.getText().toString());
                                                lblreceived1.setSelection(lblreceived1.getText().length());
                                            }
                                            else {
                                                int cont = Integer.valueOf(lblreceived1.getText().toString()) - Integer.valueOf(lblsoldtofarmer1.getText().toString());
                                                lblbal1.setText(String.valueOf(cont));
                                                lblreceived1.setSelection(lblreceived1.getText().length());
                                            }

                                        }
                                        // txtCurrentStock.setText("");
                                    } else {
                                        lblsoldtofarmer1.setText("0");
                                        lblbal1.setText("0");
                                        lblreceived1.setSelection(lblreceived1.getText().length());
                                    }
                                    listenForChanges = true;
                                }
                                // listenForChanges = true;
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
                    lblsoldtofarmer1.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            try {

                                if (listenForChanges)
                                {
                                    listenForChanges = false;
                                    if (s.length() != 0) { //do your work here }

                                        if(lblreceived1.getText().length()==0)
                                        {
                                            lblreceived1.setText("0");
                                            lblsoldtofarmer1.setText(lblreceived1.getText());
                                            lblbal1.setText(lblreceived1.getText());
                                        }
                                        if (lblsoldtofarmer1.getText().length() != 0) {

                                            if (Integer.valueOf(lblsoldtofarmer1.getText().toString())>Integer.valueOf(lblreceived1.getText().toString()))
                                            {

                                                lblsoldtofarmer1.setText("0");
                                                lblsoldtofarmer1.setText(lblreceived1.getText());
                                                int cont = Integer.valueOf(lblreceived1.getText().toString()) - Integer.valueOf(lblsoldtofarmer1.getText().toString());
                                                lblbal1.setText(String.valueOf(cont));
                                                lblsoldtofarmer1.setSelection(lblsoldtofarmer1.getText().length());
                                            }
                                            else {
                                                int cont = Integer.valueOf(lblreceived1.getText().toString()) - Integer.valueOf(lblsoldtofarmer1.getText().toString());
                                                lblbal1.setText(String.valueOf(cont));

                                            }

                                        }
                                        // txtCurrentStock.setText("");
                                    } else {
                                        lblsoldtofarmer1.setText("0");
                                        lblbal1.setText(lblreceived1.getText());
                                        lblsoldtofarmer1.setSelection(lblsoldtofarmer1.getText().length());
                                    }
                                    listenForChanges = true;
                                }
                                // listenForChanges = true;
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
                    my_linear_layout1.addView(view2);


                }
                //end

                if(jArray.length()>0) {
                    for (int i = 0; i < jArray.length(); i++) {

                        View view2 = my_linear_layout1.getChildAt(i+1);
                        final EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                        final EditText lblsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                        final EditText txtcompname1 = (EditText) view2.findViewById(R.id.txtcompname);
                        final EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);

                        JSONObject jObject = jArray.getJSONObject(i);
                        txtcompname1.setText(jObject.getString("CompanyName").toString());
                        lblreceived1.setText(jObject.getString("RecStock").toString());
                        lblsoldtofarmer1.setText(jObject.getString("SoldStock").toString());
                        lblbal1.setText(jObject.getString("BalStock").toString());
                    }
                }


              /*  for (int i = 0; i < jArray.length(); i++) {

                    int total = 0;

                    final View view2 = LayoutInflater.from(this).inflate(R.layout.competitatorpoggridviewshow, null);

                    view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    final EditText lblreceived1 = (EditText) view2.findViewById(R.id.lblreceived);
                    final EditText lblsoldtofarmer1 = (EditText) view2.findViewById(R.id.lblsoldtofarmer);
                    final TextView txtcompname1 = (TextView) view2.findViewById(R.id.txtcompname);
                    final EditText lblbal1 = (EditText) view2.findViewById(R.id.lblbal);
                    Button btnEdit1 = (Button) view2.findViewById(R.id.btnEdit);
                    JSONObject jObject = jArray.getJSONObject(i);
                    txtcompname1.setText(jObject.getString("CompanyName").toString());
                    lblreceived1.setText(jObject.getString("RecStock").toString());
                    lblsoldtofarmer1.setText(jObject.getString("SoldStock").toString());
                    lblbal1.setText(jObject.getString("BalStock").toString());
                    btnEdit1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //UPDATE THE COMPATITator Retaler Data

                            mDatabase.Updatedata("update RetailerCompetitatorPOGTable " +
                                    "set RecStock='" + lblreceived1.getText().toString() + "'," +
                                    "SoldStock='" + lblsoldtofarmer1.getText().toString() + "'," +
                                    "BalStock='" + lblbal1.getText().toString() + "'," +
                                    " Status=0 where CropName='" + croptype + "'" +
                                    " and  RetailerMobileno='" + RetailerMobileno + "'");
                            msclass.showMessage("Record Updated Successfully.");
                        }
                    });
                    my_linear_layout1.addView(view2);


                }*/
            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();

            }
        }catch (Exception ex) {
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
            String encodeImage="A";
            if(objAsBytes!=null) {
                encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            }
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "UpdatedPOG"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= cx.MDOurlpath+"?action="+action+"&usercode="+usercode+"&Crop_Code="+Crop_Code+"" +
                    "&cropname="+cropname+"&distcode="+DistrCode+"";
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

                    BindSaveRecord(Crop_Code,croptype,result);

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
                    Toast.makeText(context, "Distributor as Retailer  Competitator data updated successfully.",
                            Toast.LENGTH_SHORT).show();
                    check =0;
                    spCropType.setSelection(0);
                    my_linear_layout1.removeAllViews();


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

                returnvalue= syncPOGdata("MDO_POGData", objAsBytes, Imagestring1, Imagestring2, ImageName, "",cx.MDOurlpath);



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

}
