package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.security.cert.CertificateException;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class MdoLeaveActivity extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtReason,txtTodt,
            txtFromDt,txtDays;
    public SearchableSpinner spleavetype,spdistributor,spdistributorshipto,spdivision,spsaleorg,spdepot;
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
    TextView txttotalvalue;
    public String SERVER = "";
//    public String leaverequsturl="http://cmsapiapp.orgtix.com/api/Leave/SubmitLeave";
    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    SharedPreferences.Editor editor, locedit;
    String  saleorg,customer, name , DLV_plant, customregroup,cmbproductlist;
    int check2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdo_leave);
        getSupportActionBar().hide(); //<< this
        context = this;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass=new Messageclass(this);
       // my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        txtReason=(EditText)findViewById(R.id.txtReason);
        txtTodt=(EditText)findViewById(R.id.txtTodt);
        txtFromDt=(EditText)findViewById(R.id.txtFromDt);
        txtDays=(EditText)findViewById(R.id.txtDays);
        btnsave=(Button)findViewById(R.id.btnsave);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(txtFromDt, this);
        Utility.setRegularFont(txtReason, this);
        Utility.setRegularFont(txtTodt, this);
        spleavetype=(SearchableSpinner)findViewById(R.id.spleavetype);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        txtTodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
                //txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(),txtTodt.getText().toString())));

            }
        });
        txtFromDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
               // txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(),txtTodt.getText().toString())));

            }
        });
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
       });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GeneralMaster gm = (GeneralMaster) spleavetype.getSelectedItem();

                if (gm.Code()=="0") {
                    msclass.showMessage("Please select leave type ");
                    return;
                }
                if (txtFromDt.getText().length()==0) {
                    msclass.showMessage("Please  enter from date.");
                    return;
                }
                if (txtTodt.getText().length()==0) {
                    msclass.showMessage("Please  enter To date.");
                    return;
                }
                if (txtReason.getText().length()==0) {
                    msclass.showMessage("Please  enter leave reason.");
                    return;
                }
//                callleavesubmit(gm.Code());
            }
        });


        BindIntialData();
    }
    public void callhistory(View view)
    {
        // Do something in response to button click
        Intent intent = new Intent(context.getApplicationContext(),leaveHistory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    private void setDateTimeField(View v) {
        final EditText txt=(EditText)v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // view.setMinDate(System.currentTimeMillis() - 1000);
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
               // txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(),txtTodt.getText().toString())));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();



    }
    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
             if(start.length()!=0 && end.length()!=0) {
                 startDate = dateFormat.parse(start);
                 endDate = dateFormat.parse(end);
                 numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
                 numberOfDays=numberOfDays+1;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
//    public void callleavesubmit(String leavetype)
//    {
//        try {
//            JSONObject mainObj = new JSONObject();
//            //JSONArray ja = new JSONArray();
//            JSONObject jo = new JSONObject();
//            jo.put("loginEmpID", "90000000");
//            jo.put("loginEmpCompanyCodeNo", "1000");
//            jo.put("loginEmpGroupId", "CE");
//            mainObj.put("loginDetails", jo);
//
//            JSONObject jo2 = new JSONObject();
//            jo2.put("FromDate", txtFromDt.getText().toString().trim());
//            jo2.put("ToDate",  txtTodt.getText().toString().trim());
//            jo2.put("actualLeaveStartHours", "00:00:00");
//            jo2.put("actualLeaveEndHours", "00:00:00");
//            jo2.put("NoOfDays", "1");
//            jo2.put("LeaveType", leavetype);
//            jo2.put("leavePeriodFrom", "FULL DAY");
//            jo2.put("leavePeriodTo", "FULL DAY");
//            jo2.put("Reason", txtReason.getText().toString().trim());
//            mainObj.put("leaveData", jo2);
//            if(config.NetworkConnection()) {
//                new SubmitleaveRequest(leaverequsturl, mainObj).execute();
//            }
//            else
//            {
//                msclass.showMessage("internet connection not available.\n please check internet connection");
//            }
//        }
//        catch (Exception ex) {
//            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
    private  void BindIntialData() {

        try {
            spleavetype.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "Select Leave Type"));
            gm.add(new GeneralMaster("7201", "Annual Leave"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spleavetype.setAdapter(adapter);


            if(config.NetworkConnection()) {
              //  new GetDepot("1", usercode, "1", "1", "1").execute();
            }
            else
            {
               // msclass.showMessage("internet connection not available.\n please check internet connection");
            }
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

   private String getHttpPostJsonParameter(String url, JSONObject jobj) throws CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {

            //String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            httpPost.setEntity(new StringEntity(jobj.toString(), "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else if (statusCode == 401) {

            }
        } catch (ClientProtocolException e) {
            //LogHelper.appendLog("HttpPost ClientProtocolException", e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            //LogHelper.appendLog("HttpPost IOException", e.getMessage());

            e.printStackTrace();
        }
        return str.toString();
    }




    public class SubmitleaveRequest extends AsyncTask<String, String, String> {

        private String url,saleorg,division,returnstring,action,cmbDistributor;
        JSONObject obj=null;
        public SubmitleaveRequest( String url, JSONObject obj  ){
            this.url=url;
            this.obj=obj;


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

            try {
                returnstring= getHttpPostJsonParameter(url,obj);
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
            return returnstring;
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);

                pd.dismiss();
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
               // BindDepot(result);

            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }
    public class GetLeaveData extends AsyncTask<String, String, String> {

        private String url,saleorg,division,returnstring,action,cmbDistributor;
        JSONObject obj=null;
        public GetLeaveData( String url, JSONObject obj  ){
            this.url=url;
            this.obj=obj;


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

            try {
                returnstring= getHttpPostJsonParameter(url,obj);
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
            return returnstring;
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);

                pd.dismiss();
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                // BindDepot(result);

            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }

}
