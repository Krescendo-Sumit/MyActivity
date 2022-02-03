
package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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

import myactvity.mahyco.R;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.MySpinnerAdapter;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class daslogin extends AppCompatActivity {

    ProgressDialog pd;
    public EditText
            txtpassword;
    public SearchableSpinner spRegion,spHQ;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org,region,tbmcode,password,tbmHQ;
    public Button btnsave;
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
    SharedPreferences.Editor editor, locedit;
    String  saleorg,customer, name , DLV_plant, customregroup,cmbproductlist;
    int check2=0;
    String  action="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daslogin);
        getSupportActionBar().hide(); //<< this
        context = this;
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass=new Messageclass(this);
        txtpassword=(EditText)findViewById(R.id.txtpassword);
        spHQ=(SearchableSpinner)findViewById(R.id.spHQ);
        spRegion=(SearchableSpinner)findViewById(R.id.spRegion);
        btnsave=(Button)findViewById(R.id.btnsave);
        card_month04=(CardView)findViewById(R.id.card_month04);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(txtpassword, this);
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

        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    int addcount=0;


                    check2 = check2 + 1;
                    if (check2 > 1)
                    {
                        action="2";
                        region =gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                        new GetRegion().execute();
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

        spHQ .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {

                        GeneralMaster gm = (GeneralMaster) spHQ.getSelectedItem();
                        tbmcode=gm.Code().trim();//URLEncoder.encode(gm.Code().toString(), "UTF-8");
                        tbmHQ=gm.Desc().trim();//URLEncoder.encode(gm.Desc().toString(), "UTF-8");


                } catch (Exception e) {
                    e.printStackTrace();
                }


                // check2 = check2 + 1;
                //if (check2 > 1)
                {

                    //  Binddistributor(division);


                }
              }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
              }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {



                    GeneralMaster sp = (GeneralMaster) spRegion.getSelectedItem();
                    if (sp.Code().equals("0"))
                    {
                        msclass.showMessage("Please select Region.") ;
                        return ;
                    }
                    GeneralMaster sd = (GeneralMaster) spHQ.getSelectedItem();
                    if (sd.Code().equals("0"))
                    {
                        msclass.showMessage("Please select TBM HQ.") ;
                        return ;
                    }
                    if (txtpassword.getText().length()==0)
                    {
                        msclass.showMessage("Please enter password") ;
                        return ;
                    }



                    if (config.NetworkConnection())
                    {
                        GeneralMaster gm = (GeneralMaster) spRegion.getSelectedItem();
                        GeneralMaster gd = (GeneralMaster) spHQ.getSelectedItem();
                        action="3";
                        tbmcode = gd.Code().trim();//URLEncoder.encode(gd.Code(), "UTF-8");
                        region=gm.Code().trim();//URLEncoder.encode(gm.Code(), "UTF-8");
                        password=txtpassword.getText().toString().trim();//URLEncoder.encode(txtpassword.getText().toString(), "UTF-8");
                        JSONObject mainObj = new JSONObject();
                        JSONArray ja = new JSONArray();
                        String Comments="";


                           /* JSONObject jo = new JSONObject();
                            jo.put("MATNR", MATNR);
                            jo.put("MAKTX", MAKTX);
                            jo.put("MTART", MTART);
                            jo.put("MATKL", MATKL);
                            jo.put("QTYPKT", QTYPKT);
                            jo.put("PKTSIZE", PKTSIZE);
                            jo.put("PRICE", PRICE);
                            ja.put(jo);*/

                        mainObj.put("Table1", ja);
                        byte[] objAsBytes = null;//new byte[10000];
                        try {
                            objAsBytes = mainObj.toString().getBytes("UTF-8");
                            new GetRegion().execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                       // new Saleorderdata("1",objAsBytes,usercode,org,division,customer,name,
                              //  DLV_plant,customregroup,Comments,"spHQ").execute();


                    }
                    else
                    {
                     msclass.showMessage("check your internet connection and try again. ");
                    }
                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });
        BindIntialData();
        }

    private  void BindIntialData() {

        try {
            

            spRegion.setAdapter(null);
            List<GeneralMaster> Croplist5 = new ArrayList<GeneralMaster>();
            Croplist5.add(new GeneralMaster("0",
                    "SELECT REGION"));
            ArrayAdapter<GeneralMaster> adapter5 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist5);
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRegion.setAdapter(adapter5);

            spHQ.setAdapter(null);
            List<GeneralMaster> Croplist6 = new ArrayList<GeneralMaster>();
            Croplist6.add(new GeneralMaster("0",
                    "SELECT TBM HQ"));
            ArrayAdapter<GeneralMaster> adapter6 = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist6);
            adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHQ.setAdapter(adapter6);
            if(config.NetworkConnection())
            {
                check2=0;
                action="1";
                new GetRegion().execute();
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
    public void  bindregion(String str)
    {
        try {
            spRegion.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select Region"));
           // if (config.NetworkConnection())
            {
                try {
                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    if (jArray.length()>0) {

                        Croplist.clear();

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            Croplist.add(new GeneralMaster(jObject.getString("regionid"),
                                    jObject.getString("region"))
                            );
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           // else {
             //   msclass.showMessage("Internet network not available.");
           // }
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRegion.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void  bindHQ(String str)
    {
        try {
            spHQ.setAdapter(null);
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0",
                    "Select TBM HQ"));
            if (config.NetworkConnection()) {
                try {

                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");
                    if (jArray.length()>0) {
                        Croplist.clear();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            Croplist.add(new GeneralMaster(jObject.getString("usercode"),
                                    jObject.getString("headQuarter"))
                            );
                        }
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
            spHQ.setAdapter(adapter);
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
     public class GetRegion extends AsyncTask<String, String, String> {
        private String returnstring;
        public GetRegion(){
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
            postParameters.add(new BasicNameValuePair("Type", "mdo_getRegionandHeadQuarterDetail"));
            postParameters.add(new BasicNameValuePair("usercode",usercode));
            postParameters.add(new BasicNameValuePair("region",region));
            postParameters.add(new BasicNameValuePair("tbmcode",tbmcode));
            postParameters.add(new BasicNameValuePair("password",password));
            postParameters.add(new BasicNameValuePair("action",action));
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

            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }

            pd.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {

            try{
                pd.dismiss();
                // JSONObject jsonObject = new JSONObject(result);
                if (result.contains("True") ) {
                    pd.dismiss();
                    if (action.equals("1"))
                    {
                        bindregion(result);
                    }
                    if (action.equals("2")) {
                        bindHQ(result);
                    }

                    if (action.equals("3")) {
                        Intent intent = new Intent(context.getApplicationContext(), DSActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("tbmcode", tbmcode);
                        intent.putExtra("region", region);
                        intent.putExtra("tbmHQ", tbmHQ);
                        context.startActivity(intent);

                    }
                }
                else
                {
                    msclass.showMessage("Something went wrong please try again later.");

                }

            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
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
