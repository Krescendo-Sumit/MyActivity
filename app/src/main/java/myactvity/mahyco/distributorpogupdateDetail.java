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
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.RelativeLayout;
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
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.helper.MySpinnerAdapter;

public class distributorpogupdateDetail extends AppCompatActivity {
    ProgressDialog pd;
    public SearchableSpinner spCropType;
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
    TextView txtDescription;
    Config config;
    String state,dist,taluka,mktplace,RetailerMobileno,DistrCode,TBMCode;
    private boolean listenForChanges = true;
    String strdate,usercode;
    int check=0;
    public CommonExecution cx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributorpogupdate_detail);
        getSupportActionBar().hide(); //<< this
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        config = new Config(this); //Here the context is passing
        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode=pref.getString("UserID", null);
        cx=new CommonExecution(this);

        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        btnsave= (Button)findViewById(R.id.btnsave);
        my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        context=this;
        txtpridate=(EditText)findViewById(R.id.txtpridate);
        txtpridate.setEnabled(false);
        Date dt = new Date();
        strdate = dateFormatter.format(dt);

        txtpridate.setText(strdate.toString());

        txtDescription=(TextView)findViewById(R.id.txtDescription);
        Bundle bundle = getIntent().getExtras();


        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        state=bundle.getString("state");
        dist=bundle.getString("dist");
        taluka=bundle.getString("taluka");
        mktplace=bundle.getString("mktplace");
        RetailerMobileno=bundle.getString("RetailerMobileno");
        DistrCode=bundle.getString("DistrCode");
        TBMCode=bundle.getString("TBMCode");
        String htmstring2="<p><b><font color=#FF0000>RCVD STOCK DEPOT :</font></b>CUMMULATIVE RECEIVED STOCK FROM DEPOT <br/>\n" +
                "<b><font color=#FF0000>RCVD STOCK DISTR</font></b>CUMMULATIVE RECEIVED STOCK FROM OTHER DISTRIBUTOR <br/>\n" +
                "<b><font color=#FF0000> TRAN TO DISTR:</font></b>CUMMULATIVE TRANSFER TO OTHER DISTRIBUTOR <br/>\n" +
                "<b><font color=#FF0000> NET STOCK- :</font></b>CUMMULATIVE NET RECEIVED STOCK <br/>\n" +
                "<b><font color=#FF0000>BAL STOCK:</font></b>BALANCE STOCK IN GODOWN <br/>\n" +
                 "<b><font color=#FF0000>PLACE STOCK:</font></b>PLACEMENT <br/> TO RETAILERS \n</p>";


        String pkt = "<font color='#EE0000'>(pkt)</font>";
        String kg = "<font color='#EE0000'>(kg)</font>";
        String htmstring ="<p> <b> NOTE-Cummulative stock details to be recorded<br/>" +
                "Stock Details for cotton should be recorded in " +pkt+"<br/> \n" +
                "Stock Details for other crop should be recorded in " +kg+"<br/> \n" +
                "\n</p>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDescription.setText(Html.fromHtml(htmstring,Html.FROM_HTML_MODE_LEGACY));
        }

        else
        {
            txtDescription.setText(Html.fromHtml(htmstring));
        }

        /*txtDescription.setText("NOTE-Cummulative stock details to be recorded.\n " +
                "Stock Details for cotton should be recorded in pkt. \n" +
                "Stock Details for other crop should be recorded in kgs");
      */
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
                    Toast.makeText(distributorpogupdateDetail.this,ex.toString(),Toast.LENGTH_LONG).show();
                }


                 check = check + 1;
                 if (check > 1)
                {
                   // updatePOG(Crop_Code,croptype);
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
                        new UpdatedPOG("1", objAsBytes, usercode, Crop_Code,
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
                Toast.makeText(distributorpogupdateDetail.this, "gngvnv", Toast.LENGTH_LONG).show();
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
                        if (matcher.matches()) {

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
            final String crop = spCropType.getSelectedItem().toString();
            GeneralMaster gm = (GeneralMaster) spCropType.getSelectedItem();
             Crop_Code=gm.Code();
            //St
            int count=0;
            // mDatabase.deleterecord("delete from DistributorPOGTable where" +
              //      " DistrCode='"+DistrCode+"' and CropName ='"+crop+"'");
            mDatabase.deleterecord("delete from DistributorPOGTable where" +
                    " DistrCode='"+DistrCode+"' and CropCode ='"+gm.Code()+"'");

            for (int i = 1; i < my_linear_layout1.getChildCount(); i++) {
                View view2 = my_linear_layout1.getChildAt(i);
                final EditText lblplan = (EditText) view2.findViewById(R.id.lblplan);
                final EditText lblreceivedfrmdepo = (EditText) view2.findViewById(R.id.lblreceivedfrmdepo);
                final EditText lblreceivedfrmdistr = (EditText) view2.findViewById(R.id.lblreceivedfrmdistr);
                final EditText lblshipttodistr = (EditText) view2.findViewById(R.id.lblshipttodistr);
                final   EditText lblnetstock = (EditText) view2.findViewById(R.id.lblnetstock);
                final TextView txtprdname = (TextView) view2.findViewById(R.id.txtprdname);
                final TextView txtprdcode = (TextView) view2.findViewById(R.id.txtprdcode);
                final EditText lblbalgoddown = (EditText) view2.findViewById(R.id.lblbalgoddown);
                final  EditText lblplacement = (EditText) view2.findViewById(R.id.lblplacement);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

               // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                final String strdate = dateFormat.format(d);
                boolean f = mDatabase.InsertDistributorPOGData(pref.getString("UserID", null),
                        state,dist,taluka,mktplace,RetailerMobileno,crop.trim(),txtprdname.getText().toString(),
                        Crop_Code,txtprdcode.getText().toString().trim(),lblplan.getText().toString(),lblreceivedfrmdepo.getText().toString(),
                        lblreceivedfrmdistr.getText().toString(),lblshipttodistr.getText().toString(),
                        lblnetstock.getText().toString(),lblbalgoddown.getText().toString(),
                        lblplacement.getText().toString(),strdate,DistrCode);

                if (f == true) {
                    count++;
                } else {

                }


            }

            //ene

            if (count > 0) {
                /*msclass.showMessage("Distributor POG data updated successfully.");
                check =0;
                spCropType.setSelection(0);
                my_linear_layout1.removeAllViews();*/
                new SyncPOGData_Async("MDO_POGData","DistributorAsRetailer3").execute();

            } else {
                msclass.showMessage("Please enter product details");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            msclass.showMessage(ex.toString());

        }
    }



    public void updatePOG(final String cropcode,final String cropname,String result )
    {
        try {

            my_linear_layout1.removeAllViews();                              //add this too
            //st
            try {

                String searchQuery = "SELECT distinct Cust_Code,Prod_Name as ProductName," +
                        " Crop_Name as CropName" +
                        ",Prod_Code,Plan_Qty,QTY_Unit,QTY_Base,ifnull(RecStockdepot,0) as RecStockdepot," +
                        " ifnull(RecStockdistr,0) as RecStockdistr," +
                        " ifnull(ShiftStockToDistr,0) as ShiftStockToDistr ," +
                        " ifnull(NetStock,0) as NetStock," +
                        " ifnull(BalStock,0) as BalStock ,ifnull(placementstock,0) as placementstock," +
                        " stockDate " +
                        " FROM  mdo_pogsapdata a left outer join" +
                        " DistributorPOGTable b on a.Crop_Code= b.CropCode" +
                        " and a.Prod_Code=b.ProductCode where a.Crop_Code='" + cropcode + "'" +
                        " and Cust_Code='"+DistrCode+"' and a.QTY_Unit <>'0' and Prod_Name <>'' ";

             /*   String searchQuery = "SELECT distinct Cust_Code,Prod_Name as ProductName," +
                        " Crop_Name as CropName" +
                        ",Plan_Qty,QTY_Unit,QTY_Base,ifnull(RecStockdepot,0) as RecStockdepot," +
                        " ifnull(RecStockdistr,0) as RecStockdistr," +
                        " ifnull(ShiftStockToDistr,0) as ShiftStockToDistr ," +
                        " ifnull(NetStock,0) as NetStock," +
                        " ifnull(BalStock,0) as BalStock ,ifnull(placementstock,0) as placementstock," +
                        " stockDate " +
                        " FROM  mdo_pogsapdata a left outer join" +
                        " DistributorPOGTable b on a.Crop_Name= b.CropName" +
                        " and a.Prod_Name=b.ProductName where upper(a.Crop_Name)=upper('" + croptype + "')" +
                        " and Cust_Code='"+DistrCode+"' and a.QTY_Unit <>'0' and Prod_Name <>'' ";
*/
                // " a.ProductName =b.prod_Name where a.Crop_Code='" + cropcode + "' ";


                JSONObject object = new JSONObject(result);
                // JSONObject object = new JSONObject();
                //object.put("Table1", mDatabase.getResults(searchQuery));
                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                String  str="a";
                String[] splittedValues = str.split(",");//spcrop.getSelectedItemsAsString().split(",");

                //HEADER START
                final View view1 = LayoutInflater.from(this).inflate(R.layout.showupdatedistributorpog, null);
                view1.setBackgroundColor(getResources().getColor(R.color.Whitecl));

                EditText lblplan = (EditText) view1.findViewById(R.id.lblplan);
                EditText lblreceivedfrmdepo = (EditText) view1.findViewById(R.id.lblreceivedfrmdepo);
                EditText lblreceivedfrmdistr = (EditText) view1.findViewById(R.id.lblreceivedfrmdistr);
                EditText lblshipttodistr = (EditText) view1.findViewById(R.id.lblshipttodistr);
                EditText lblnetstock = (EditText) view1.findViewById(R.id.lblnetstock);
                TextView txtprdname = (TextView) view1.findViewById(R.id.txtprdname);
                EditText lblbalgoddown = (EditText) view1.findViewById(R.id.lblbalgoddown);
                EditText lblplacement = (EditText) view1.findViewById(R.id.lblplacement);
                Button btnnext = (Button) view1.findViewById(R.id.btnnext);
                btnnext.setVisibility(View.INVISIBLE);
                txtprdname.setText("PRODUCT NAME");
                txtprdname.setTextColor(Color.WHITE);
                txtprdname.setTextSize(10);
                txtprdname.setEnabled(false);
                txtprdname.setBackgroundColor(Color.TRANSPARENT);
                txtprdname.setTypeface(txtprdname.getTypeface(), Typeface.BOLD);

                lblplan.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
                lblplan.setText("PLAN");
                lblplan.setEnabled(false);
                lblplan.setMaxLines(2);
                lblplan.setTextColor(Color.WHITE);
                lblplan.setBackgroundColor(Color.TRANSPARENT);
                //lblreceived.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblplan.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                // lblcomp.setTextSize(14);
                lblplan.setTypeface(lblplan.getTypeface(), Typeface.BOLD);


                lblreceivedfrmdepo.setText("RECEIVED STOCK FROM DEPOT");
                lblreceivedfrmdepo.setEnabled(false);
                lblreceivedfrmdepo.setSingleLine(false);
                lblreceivedfrmdepo.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblreceivedfrmdepo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblreceivedfrmdepo.setMaxLines(10);

                lblreceivedfrmdepo.setTextColor(Color.WHITE);
                lblreceivedfrmdepo.setBackgroundColor(Color.TRANSPARENT);
                //lblreceived.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                lblreceivedfrmdepo.setTypeface(lblreceivedfrmdepo.getTypeface(), Typeface.BOLD);

                lblreceivedfrmdistr.setText("RECEIVED STOCK FROM OTHER DISTRIBUTOR");
                lblreceivedfrmdistr.setEnabled(false);
                lblreceivedfrmdistr.setSingleLine(false);
                lblreceivedfrmdistr.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblreceivedfrmdistr.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblreceivedfrmdistr.setMaxLines(10);
                lblreceivedfrmdistr.setTextColor(Color.WHITE);
                lblreceivedfrmdistr.setBackgroundColor(Color.TRANSPARENT);
                lblreceivedfrmdistr.setTypeface(lblreceivedfrmdistr.getTypeface(), Typeface.BOLD);

                lblshipttodistr.setText("STOCK TRANSFER TO OTHER DISTRIBUTOR");
                lblshipttodistr.setEnabled(false);
                lblshipttodistr.setSingleLine(false);
                lblshipttodistr.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblshipttodistr.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblshipttodistr.setMaxLines(10);
                lblshipttodistr.setTextColor(Color.WHITE);
                lblshipttodistr.setBackgroundColor(Color.TRANSPARENT);
                lblshipttodistr.setTypeface(lblnetstock.getTypeface(), Typeface.BOLD);
                // lblcrop.setTextSize(14);

                lblnetstock.setText("NET RECEIVED STOCK");
                lblnetstock.setEnabled(false);
                lblnetstock.setSingleLine(false);
                lblnetstock.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblnetstock.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblnetstock.setMaxLines(10);
                lblnetstock.setTextColor(Color.WHITE);
                lblnetstock.setBackgroundColor(Color.TRANSPARENT);
                // lblproduct.setTextSize(14);
                lblnetstock.setTypeface(lblnetstock.getTypeface(), Typeface.BOLD);

                lblbalgoddown.setText("BALANCE STOCK IN GODOWN");
                lblbalgoddown.setEnabled(false);
                lblbalgoddown.setSingleLine(false);
                lblbalgoddown.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblbalgoddown.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblbalgoddown.setMaxLines(10);
                lblbalgoddown.setTextColor(Color.WHITE);
                lblbalgoddown.setBackgroundColor(Color.TRANSPARENT);
                //  lblqty.setTextSize(14);
                lblbalgoddown.setTypeface(lblbalgoddown.getTypeface(), Typeface.BOLD);

                 lblplacement.setText("PLACEMENT TO RETAILERS");
                lblplacement.setEnabled(false);
                lblplacement.setSingleLine(false);
                lblplacement.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                //Of particular importance, do not leave out the InputType.TYPE_CLASS_TEXT; it will not work without it!
                lblplacement.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lblplacement.setMaxLines(10);
                lblplacement.setTextColor(Color.WHITE);
                lblplacement.setBackgroundColor(Color.TRANSPARENT);
                //  lblqty.setTextSize(14);
                lblplacement.setTypeface(lblplacement.getTypeface(), Typeface.BOLD);


                view1.setBackgroundColor(getResources().getColor(R.color.heback));
                my_linear_layout1.addView(view1);
                // HEADER END
              if(jArray.length()>0) {
                  for (int i = 0; i < jArray.length(); i++) {

                      int total = 0;

                      final View view2 = LayoutInflater.from(this).inflate(R.layout.showupdatedistributorpog, null);

                      view2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                      final EditText lblplan1 = (EditText) view2.findViewById(R.id.lblplan);
                      final EditText lblreceivedfrmdepo1 = (EditText) view2.findViewById(R.id.lblreceivedfrmdepo);
                      final EditText lblreceivedfrmdistr1 = (EditText) view2.findViewById(R.id.lblreceivedfrmdistr);
                      final EditText lblshipttodistr1 = (EditText) view2.findViewById(R.id.lblshipttodistr);
                      final EditText lblnetstock1 = (EditText) view2.findViewById(R.id.lblnetstock);
                      final TextView txtprdname1 = (TextView) view2.findViewById(R.id.txtprdname);
                      final EditText lblbalgoddown1 = (EditText) view2.findViewById(R.id.lblbalgoddown);
                      final EditText lblplacement1 = (EditText) view2.findViewById(R.id.lblplacement);
                      final TextView txtprdcode1 = (TextView) view2.findViewById(R.id.txtprdcode);


                      Button btnnext1 = (Button) view2.findViewById(R.id.btnnext);


                      btnnext1.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              if (lblbalgoddown1.getText().length() == 0) {
                                  msclass.showMessage(" please check goddown balance stock ");
                              } else {
                                  addproduct(croptype, txtprdcode1.getText().toString(), txtprdname1.getText().toString(), lblbalgoddown1.getText().toString());

                              }

                          }
                      });

                      JSONObject jObject = jArray.getJSONObject(i);
                      txtprdname1.setText(jObject.getString("ProductName").toString());
                      txtprdcode1.setText(jObject.getString("Prod_Code").toString());
                      lblplan1.setEnabled(false);
                      lblreceivedfrmdepo1.setEnabled(false);
                      lblnetstock1.setEnabled(false);

                      lblreceivedfrmdistr1.setText(jObject.getString("RecStockdistr").toString());
                      lblshipttodistr1.setText(jObject.getString("ShiftStockToDistr").toString());
                      lblnetstock1.setText(jObject.getString("NetStock").toString());
                      lblbalgoddown1.setText(jObject.getString("BalStock").toString());
                      lblplacement1.setText(jObject.getString("placementstock").toString());
                      //if(txtpridate.getText().length() ==0)
                      if (jObject.getString("stockDate").toString().length() != 0)

                      {
                          txtpridate.setText(jObject.getString("stockDate").toString());
                      }

                      if (cropname.toLowerCase().contains("cotton")) {
                          lblplan1.setText(jObject.getString("Plan_Qty").toString());
                          lblreceivedfrmdepo1.setText(jObject.getString("QTY_Unit").toString());

                          lblreceivedfrmdepo1.setHint("PKT");
                          lblreceivedfrmdistr1.setHint("PKT");
                          lblshipttodistr1.setHint("PKT");
                          lblshipttodistr1.setHint("PKT");
                          lblnetstock1.setHint("PKT");
                          lblplacement1.setHint("PKT");
                          lblbalgoddown1.setHint("PKT");
                      } else {
                          lblreceivedfrmdepo1.setHint("KG");
                          lblreceivedfrmdistr1.setHint("KG");
                          lblshipttodistr1.setHint("KG");
                          lblshipttodistr1.setHint("KG");
                          lblnetstock1.setHint("KG");
                          lblplacement1.setHint("KG");
                          lblbalgoddown1.setHint("KG");
                          lblplan1.setText(jObject.getString("Plan_Qty").toString());
                          lblreceivedfrmdepo1.setText(jObject.getString("QTY_Base").toString());
                      }

                      //Intial calulation
                      if (listenForChanges) {
                          listenForChanges = false;
                          if (lblreceivedfrmdepo1.getText().length() != 0) { //do your work here }
                              if (lblreceivedfrmdistr1.getText().length() == 0) {
                                  lblreceivedfrmdistr1.setText("0");
                              }
                              if (lblshipttodistr1.getText().length() == 0) {
                                  lblshipttodistr1.setText("0");
                              }
                              if (lblnetstock1.getText().length() == 0) {
                                  lblnetstock1.setText("0");
                              }
                              if (lblbalgoddown1.getText().length() == 0) {
                                  lblbalgoddown1.setText("0");
                              }


                              int cont = Integer.valueOf(lblreceivedfrmdepo1.getText().toString()) + Integer.valueOf(lblreceivedfrmdistr1.getText().toString());
                              int bal = Integer.valueOf(cont) - Integer.valueOf(lblshipttodistr1.getText().toString());
                              lblnetstock1.setText(String.valueOf(bal));
                              int vv = Integer.valueOf(bal) - Integer.valueOf(lblbalgoddown1.getText().toString());
                              lblplacement1.setText(String.valueOf(vv));


                          }

                          listenForChanges = true;
                          lblplacement1.setEnabled(false);
                      }

                      //end Calulation
                      //Start

                      lblreceivedfrmdistr1.addTextChangedListener(new TextWatcher() {

                          public void onTextChanged(CharSequence s, int start, int before,
                                                    int count) {
                              try {

                                  if (listenForChanges) {
                                      listenForChanges = false;
                                      if (s.length() != 0) { //do your work here }

                                          if (lblreceivedfrmdepo1.getText().length() == 0) {
                                              lblreceivedfrmdepo1.setText("0");
                                          }
                                          if (lblreceivedfrmdistr1.getText().length() == 0) {
                                              lblreceivedfrmdistr1.setText("0");
                                          }
                                          if (lblshipttodistr1.getText().length() == 0) {
                                              lblshipttodistr1.setText("0");
                                          }
                                          if (lblnetstock1.getText().length() == 0) {
                                              lblnetstock1.setText("0");
                                          }

                                          if (lblreceivedfrmdistr1.getText().length() != 0) {
                                              int cont = Integer.valueOf(lblreceivedfrmdepo1.getText().toString()) + Integer.valueOf(lblreceivedfrmdistr1.getText().toString());
                                              int bal = Integer.valueOf(cont) - Integer.valueOf(lblshipttodistr1.getText().toString());
                                              lblnetstock1.setText(String.valueOf(bal));
                                              //lblplacement1.setText(Integer.valueOf(lblnetstock1.getText().toString()) + Integer.valueOf(lblbalgoddown1.getText().toString()));
                                              int vv = Integer.valueOf(bal) - Integer.valueOf(lblbalgoddown1.getText().toString());
                                              lblplacement1.setText(String.valueOf(vv));
                                          }

                                      }
                                      // txtCurrentStock.setText("");

                                      listenForChanges = true;
                                  }

                              } catch (Exception ex) {
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
                      lblshipttodistr1.addTextChangedListener(new TextWatcher() {

                          public void onTextChanged(CharSequence s, int start, int before,
                                                    int count) {
                              try {

                                  if (listenForChanges) {
                                      listenForChanges = false;
                                      if (s.length() != 0) { //do your work here }

                                          if (lblreceivedfrmdepo1.getText().length() == 0) {
                                              lblreceivedfrmdepo1.setText("0");
                                          }
                                          if (lblreceivedfrmdistr1.getText().length() == 0) {
                                              lblreceivedfrmdistr1.setText("0");
                                          }
                                          if (lblshipttodistr1.getText().length() == 0) {
                                              lblshipttodistr1.setText("0");
                                          }
                                          if (lblnetstock1.getText().length() == 0) {
                                              lblnetstock1.setText("0");
                                          }

                                          if (lblreceivedfrmdistr1.getText().length() != 0) {
                                              int cont = Integer.valueOf(lblreceivedfrmdepo1.getText().toString()) + Integer.valueOf(lblreceivedfrmdistr1.getText().toString());
                                              int bal = Integer.valueOf(cont) - Integer.valueOf(lblshipttodistr1.getText().toString());
                                              //New change 21-04 -2020
                                              if (bal < 0) {
                                                  msclass.showMessage("stock transfer to other distributor  should not more than net received stock.");
                                                  lblshipttodistr1.setText("0");
                                                  //lblbalgoddown1.setText("0");

                                              }
                                              else {

                                                  lblnetstock1.setText(String.valueOf(bal));
                                                  // lblplacement1.setText(Integer.valueOf(lblnetstock1.getText().toString()) + Integer.valueOf(lblbalgoddown1.getText().toString()));
                                                  int vv = Integer.valueOf(bal) - Integer.valueOf(lblbalgoddown1.getText().toString());
                                                  lblplacement1.setText(String.valueOf(vv));
                                              }
                                          }

                                      }
                                      // txtCurrentStock.setText("");

                                      listenForChanges = true;
                                  }

                              } catch (Exception ex) {
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
                      lblbalgoddown1.addTextChangedListener(new TextWatcher() {

                          public void onTextChanged(CharSequence s, int start, int before,
                                                    int count) {
                              try {

                                  if (listenForChanges) {
                                      listenForChanges = false;
                                      if (s.length() != 0)
                                      { //do your work here }
                                          if (lblbalgoddown1.getText().length() != 0) {
                                              if (lblnetstock1.getText().length() == 0) {
                                                  lblnetstock1.setText("0");
                                              }

                                              int bal = Integer.parseInt(String.valueOf(s));
                                              int netstock = Integer.parseInt(String.valueOf(lblnetstock1.getText()));

                                              if (bal > netstock) {
                                                  msclass.showMessage("goddown balance stock should not more " +
                                                          "than net  stock.");
                                                  int cont = Integer.valueOf(lblreceivedfrmdepo1.getText().toString()) +
                                                             Integer.valueOf(lblreceivedfrmdistr1.getText().toString());

                                                  lblnetstock1.setText(String.valueOf(cont));
                                                  lblbalgoddown1.setText("0");

                                              } else {
                                                  int cont = Integer.valueOf(netstock) - Integer.valueOf(bal);
                                                  lblplacement1.setText(String.valueOf(cont));

                                              }
                                          }
                                          // txtCurrentStock.setText("");
                                      } else {


                                      }
                                      listenForChanges = true;
                                  }


                              } catch (Exception ex) {
                                  ex.printStackTrace();
                                  listenForChanges = true;
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
                  if (txtpridate.length() == 0) {

                      txtpridate.setText(strdate.toString());

                  }
              }

                  else
                {
                    msclass.showMessage("Received stock from depot not available this crop.\n" +
                            "please confirm this data or download master data");
                    // Toast.makeText(retailerpogupdateDetail.this,ex.toString(),Toast.LENGTH_LONG).show();

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
            /*String searchQuery = "SELECT distinct a.ProductName, a.ProductCode,  " +
                    " a.BalStock,actionstatus,Qty ,Date" +
                    " FROM DistributorNextBtPOGTable a " +
                    " where a.ProductName='" + productname + "' and DistrCode='"+DistrCode+"' ";
                    */
            String searchQuery = "SELECT distinct a.ProductName, a.ProductCode,  " +
                    " a.BalStock,actionstatus,Qty ,Date" +
                    " FROM DistributorNextBtPOGTable a " +
                    " where a.ProductCode='" + prd_code + "' and DistrCode='"+DistrCode+"' ";

            object.put("Table1", mDatabase.getResults( searchQuery));
            final JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.onnextbuttonpopup);
            final HorizontalScrollView s = (HorizontalScrollView)dialog.findViewById(R.id.horizontalView);
          /*  s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L);*/
            final TableLayout tblLayout = (TableLayout)dialog.findViewById(R.id.tableLayout);
            TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            TableRow row = (TableRow)tblLayout.getChildAt(1);
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final MultiSelectionSpinner spdistr = (MultiSelectionSpinner) row.findViewById(R.id.spdistr);
            final CardView card_month10= (CardView) row.findViewById(R.id.card_month10);
            final TextView lbldistributor = (TextView) row0.findViewById(R.id.lbldistributor);
            final TextView lblQty = (TextView) row0.findViewById(R.id.lblVillage);

            final SearchableSpinner  spstatus=(SearchableSpinner) row.findViewById(R.id.spstatus);
            BindststusData(spstatus);
            final EditText txtqty = (EditText) row.findViewById(R.id.txtqty);
            final EditText txtprdname = (EditText) row.findViewById(R.id.txtprdname);
            final EditText txtbalance = (EditText) row.findViewById(R.id.txtbalance);
            final EditText txtdate = (EditText) row.findViewById(R.id.txtdate);

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
            txtbalance.setHint("BALANCE STOCK IN GODOWN");
            txtprdname.setEnabled(false);
            card_month10.setVisibility(View.GONE);
            lbldistributor.setVisibility(View.GONE);
            txtprdname.setText(productname);
            txtbalance.setText(balancestock);
            if(jArray.length() >0)
            {
                JSONObject jObject = jArray.getJSONObject(0);
                txtdate.setText(jObject.getString("Date").toString());
                txtqty.setText(jObject.getString("Qty").toString());
                spstatus.setSelection((config.getIndex(spstatus,jObject.getString("actionstatus").toString()))) ;
                // txtdate.setText(jObject.getString("ProductName").toString());
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
                        if (!status.equals("SUFFICIENT")) {
                            if (txtqty.getText().length()==0) {
                                msclass.showMessage("Please  enter qty");
                                return;
                            }
                            if (txtdate.getText().length()==0) {
                                msclass.showMessage("Please  enter date");
                                return;
                            }
                        }

                        if (status.contains("SALES RETURN") )
                        {
                            if (Integer.valueOf(txtqty.getText().toString())>Integer.valueOf(txtbalance.getText().toString()))
                            {
                                msclass.showMessage("sales return ,not allow to more than balance qty.");
                                return;
                            }
                        }


                        //St

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        // Date d = new Date();
                        //final String strdate = dateFormat.format(d);
                        if(jArray.length() >0) {
                          //  mDatabase.deleterecord("delete from  DistributorNextBtPOGTable where " +
                          //          " DistrCode='"+DistrCode+"' and ProductName='"+productname+"'");
                            mDatabase.deleterecord("delete from  DistributorNextBtPOGTable where " +
                                    " DistrCode='"+DistrCode+"' and ProductCode='"+prd_code+"'");
                        }
                        boolean f = mDatabase.InsertDistributorNextBtPOGData(pref.getString("UserID",
                                null), RetailerMobileno,
                                txtprdname.getText().toString().trim(),prd_code,
                                txtbalance.getText().toString().trim(), spstatus.getSelectedItem().toString(),
                                txtqty.getText().toString(),txtdate.getText().toString(),
                                DistrCode);
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

    public void addproductold(final String crop,final String productname,String balancestock)
    {
        try {

            // Check Record Exist
            JSONObject object = new JSONObject();
            String searchQuery = "SELECT distinct a.ProductName, a.ProductCode,  " +
                    " a.BalStock,actionstatus,Qty ,Date" +
                    " FROM DistributorNextBtPOGTable a " +
                    " where a.ProductName='" + productname + "' and DistrCode='"+DistrCode+"' ";
            object.put("Table1", mDatabase.getResults( searchQuery));
            final JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);




            boolean flag = false;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.retailernextclick);
            // dialog.setTitle("Service Operator Assignment");
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            // showdata(my_linear_layout1,web1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final MultiSelectionSpinner spdistr = (MultiSelectionSpinner) dialog.findViewById(R.id.spdistr);
            final CardView card_month10= (CardView) dialog.findViewById(R.id.card_month10);
            final TextView lbldistributor = (TextView) dialog.findViewById(R.id.lbldistributor);
            final SearchableSpinner  spstatus=(SearchableSpinner) dialog.findViewById(R.id.spstatus);
            BindststusData(spstatus);
            final EditText txtqty = (EditText) dialog.findViewById(R.id.txtqty);
            final EditText txtprdname = (EditText) dialog.findViewById(R.id.txtprdname);
            final EditText txtbalance = (EditText) dialog.findViewById(R.id.txtbalance);
            final EditText txtdate = (EditText) dialog.findViewById(R.id.txtdate);
            final TextView  lblunit = (TextView) dialog.findViewById(R.id.lblunit);

            if (crop.toLowerCase().contains("cotton"))
            {
                lblunit.setText("QTY(pkts)");
            }
            else
            {
                lblunit.setText("QTY(kgs)");
            }
            txtbalance.setEnabled(false);
            txtbalance.setHint("BALANCE STOCK IN GODOWN");
            txtprdname.setEnabled(false);
            card_month10.setVisibility(View.GONE);
            lbldistributor.setVisibility(View.GONE);
            txtprdname.setText(productname);
            txtbalance.setText(balancestock);
            if(jArray.length() >0)
            {
                JSONObject jObject = jArray.getJSONObject(0);
                txtdate.setText(jObject.getString("Date").toString());
                txtqty.setText(jObject.getString("Qty").toString());
                spstatus.setSelection((config.getIndex(spstatus,jObject.getString("actionstatus").toString()))) ;
                // txtdate.setText(jObject.getString("ProductName").toString());
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
                        if (txtqty.getText().length()==0) {
                            msclass.showMessage("Please  enter qty");
                            return;
                        }
                        if (txtdate.getText().length()==0) {
                            msclass.showMessage("Please  enter date");
                            return;
                        }

                        //St

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        // Date d = new Date();
                        //final String strdate = dateFormat.format(d);
                        if(jArray.length() >0) {
                            mDatabase.deleterecord("delete from  DistributorNextBtPOGTable where " +
                                    " DistrCode='"+DistrCode+"' and ProductName='"+productname+"'");
                        }
                        boolean f = mDatabase.InsertDistributorNextBtPOGData(pref.getString("UserID",
                                null), RetailerMobileno,
                                txtprdname.getText().toString().trim(),"",
                                txtbalance.getText().toString().trim(), spstatus.getSelectedItem().toString(),
                                txtqty.getText().toString(),txtdate.getText().toString(),
                                DistrCode);
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
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
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
                     updatePOG(Crop_Code,croptype,result);

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
        postParameters.add(new BasicNameValuePair("TBMCode", TBMCode));

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
