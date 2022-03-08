package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;

public class saleorderreport extends AppCompatActivity {
    ProgressDialog pd;

    Config config;
    public Button btnReport;
    public Messageclass msclass;
    SharedPreferences locdata,pref;
    SharedPreferences.Editor loceditor,editor;
    private Context context;
    public String SERVER="",usercode;
    CommonExecution ex;
    public EditText txtFromdt,txtTodate;
    public SearchableSpinner spdivision,spsaleorg;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleorderreport);
        getSupportActionBar().hide(); //<< this
        msclass=new Messageclass(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context = this;
        ex=new CommonExecution(context);
        SERVER =ex.saleSERVER;
        pd = new ProgressDialog(context);
        config = new Config(this); //Here the context is passing
        btnReport=(Button) findViewById(R.id.btnReport);
        txtFromdt=(EditText)findViewById(R.id.txtFromdt);
        txtTodate=(EditText)findViewById(R.id.txtTodate);
        spdivision=(SearchableSpinner)findViewById(R.id.spdivision);
        spsaleorg=(SearchableSpinner)findViewById(R.id.spsaleorg);
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

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // online

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
                if (txtFromdt.getText().length() ==0)
                {
                    msclass.showMessage("Please enter  from date.") ;
                    return ;
                }
                if (txtTodate.getText().length() ==0)
                {
                    msclass.showMessage("Please enter to date.") ;
                    return ;
                }
                if (config.NetworkConnection()) {
                    String usercode =pref.getString("UserID", null);
                    byte[] objAsBytes = new byte[1];
                    GeneralMaster orgm = (GeneralMaster) spsaleorg.getSelectedItem();
                    String org=orgm.Code();
                    GeneralMaster divgm = (GeneralMaster) spdivision.getSelectedItem();
                    String  division = divgm.Code().trim();
                    new saleorderstatus("","1",objAsBytes,usercode,org,division,"",txtFromdt.getText().toString(),txtTodate.getText().toString()).execute();
                 }
                else
                {
                    msclass.showMessage("Internet connection not available");
                }
            }
        });
        txtFromdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });
        txtTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        BindIntialData();
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

        spsaleorg.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0","Select Organisation"));
        gm.add(new GeneralMaster("1000","Mahyco"));
        gm.add(new GeneralMaster("1100","Sungro"));

        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsaleorg.setAdapter(adapter);

        spdivision.setAdapter(null);
        List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
        gm2.add(new GeneralMaster("0","Select Division"));
        gm2.add(new GeneralMaster("01","01-Vegetable Crop"));
        gm2.add(new GeneralMaster("02","02-Field Crop"));
        gm2.add(new GeneralMaster("03","03-Cotton Crop"));
        ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdivision.setAdapter(adapter2);

    }
    public void Onlinelist(String str)
    {
        // RelativeLayout1.removeAllViews();
        boolean fl=false;
        int tot=0;
        try {
            // Action 8  as Get data Job card History data
            //search=inputSearch.getText().toString();
            String  totamount="";
            //Strt
            try {
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
               // my_linear_layout.removeAllViews();
                if(jArray.length()==0 )
                {
                    TextView lblList= new TextView(this.context);
                    lblList.setText("pendig order list not available");
                    lblList.setTextColor(getResources().getColor(R.color.QRCodeBlackColor));
                    lblList.setTextSize(12);
                    lblList.setGravity(Gravity.CENTER);
                   // my_linear_layout.addView(lblList);
                }
                int a=0;
                for(int i=0; i <jArray.length(); i++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.pendingorderlist_item,null);
                    JSONObject jObject = jArray.getJSONObject(i);
                    TextView grower_name = (TextView) view.findViewById(R.id.grower_name);
                    TextView lblgrcode = (TextView) view.findViewById(R.id.lblgrcode);
                    TextView lblsavestatus = (TextView) view.findViewById(R.id.lblsavestatus);
                    TextView lblqty = (TextView) view.findViewById(R.id.lblqty);
                    a=a+1;
                    grower_name.setText(a+"-"+jObject.getString("name"));
                    lblgrcode.setText(jObject.getString("usercode"));
                    lblqty.setText("Total Order :"+jObject.getString("count"));
                    final String grcode=lblgrcode.getText().toString();
                    final String grname =grower_name.getText().toString();
                    //my_linear_layout.addView(view);

                }

            } catch (Exception e) {
                Log.e("JSONException", "Error: " + e.toString());

            }

        }
        catch (Exception e) {
            e.printStackTrace();

        }


    }


    public void REPORTLIST(final String compname)
    {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.addretailerproduct);
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);

            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button btnaddmore = (Button) dialog.findViewById(R.id.btnaddmore);
            final Spinner spCropType = (Spinner) dialog.findViewById(R.id.spCropType);
            final Spinner spproduct = (Spinner) dialog.findViewById(R.id.spProductName);
            final EditText txtqty = (EditText) dialog.findViewById(R.id.txtqty);
            final EditText txtcompname = (EditText) dialog.findViewById(R.id.txtcompname);
            final EditText txtproduct = (EditText) dialog.findViewById(R.id.txtproduct);




            final WebView webView = (WebView) dialog.findViewById(R.id.webView);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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

    public void bindresultdata(String result) {
        final Dialog dialog = new Dialog(context);
        try {
          dialog.setContentView(R.layout.reportview);
            final WebView wb1 = (WebView) dialog.findViewById(R.id.wb1);
            ImageView backbtn= (ImageView) dialog.findViewById(R.id.backbtn);
            WebSettings webSettings = wb1.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // wb1.loadData(result, "text/html", null);
            wb1.loadDataWithBaseURL(null, result, null, "utf-8", null);

            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
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
        }
         catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }


    public class saleorderstatus extends AsyncTask<String, String, String> {
        private String usercode,saleorg,division,customer,fromdt,pattern,todate,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,orderid;
        byte[] objAsBytes;
        public saleorderstatus(String orderid, String action,byte[] objAsBytes, String usercode,String saleorg,
                             String division,String pattern,String fromdt ,String todate  ){
            this.objAsBytes=objAsBytes;
            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.customregroup=customregroup;
            this.todate=todate;
            this.pattern=pattern;
            this.fromdt=fromdt;
            this.orderid=orderid;

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
            postParameters.add(new BasicNameValuePair("Type", "MDOSaleOrderStatusReport"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= SERVER+"?orderid="+orderid+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&pattern="+pattern+"" +
                    "&fromdt="+fromdt+"&todate="+todate;
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
                //if (result.contains("True")) {

                    bindresultdata(result);
                    // if (action==1) {
                    //Onlinelist(result);
                    // }
               // }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

}
