package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class saleorderpending extends AppCompatActivity {
    // List view
    ProgressDialog pd;
    private ListView lv;
    Config config;
    // Listview Adapter
    ArrayAdapter<String> adapter;
    // Search EditText
    EditText inputSearch;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    public Messageclass msclass;
    SharedPreferences locdata,pref;
    SharedPreferences.Editor loceditor,editor;
    private SqliteDatabase mDatabase;
    public String  search="";
    LinearLayout my_linear_layout;
    TextView lblheader,lgltotl;
    private Context context;
    CommonExecution cx;
    public String MDOurlpath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleorderpending);
        getSupportActionBar().hide(); //<< this
        msclass=new Messageclass(this);
        mDatabase = SqliteDatabase.getInstance(this);
        lblheader=(TextView) findViewById(R.id.lblheader);
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context = this;
        cx=new CommonExecution(context);
        MDOurlpath =cx.saleSERVER;
        pd = new ProgressDialog(context);
        config = new Config(this); //Here the context is passing
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        lgltotl=(TextView) findViewById(R.id.lgltotl);
        my_linear_layout = (LinearLayout) findViewById(R.id.my_linear_layout);
        //lblheader.setText("Grower List("+locdata.getString("PageName",null)+")");
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                // customAdapter.getFilter().filter(text);

                try {

                    if (cs.length() > 3) { //do your work here }
                        BindFarmerlistData(inputSearch.getText().toString(),locdata.getString("action",null));

                    }
                    else
                    {
                        if (cs.length() == 0) { //do your work here }
                            BindFarmerlistData("",locdata.getString("action",null));

                        }
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // Set an item click listener for ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //  String selectedItem = (String) parent.getItemAtPosition(position);
                // Display the selected item text on TextView
                // tv.setText("Your favorite : " + selectedItem);
                TextView lblgrcode =((TextView)view.findViewById(R.id.lblgrcode));
                TextView grower_name =((TextView)view.findViewById(R.id.grower_name));


                //msclass.showMessage(lblgrcode.getText().toString());
            }
        });
        // OflineList(locdata.getString("tablename",null));
        BindFarmerlistData("",locdata.getString("action",null));
    }


    private void BindFarmerlistData( String search,String action)
    {

        try {
            search=  URLEncoder.encode(search, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // online
        if (config.NetworkConnection()) {
            String usercode =pref.getString("UserID", null);
            byte[] objAsBytes = new byte[1];
            new Saleorderdata("","2",objAsBytes,usercode,"","","",search,"","").execute();
           // new GetOrderlist(SERVER,Integer.valueOf(action), usercode, search,"", saleorderpending.this).execute(); // Get Crop Master data as action 1
        }
        else
        {
            msclass.showMessage("Internet connection not available");
        }

    }




    public void Onlinelist(String str)
    {


        // RelativeLayout1.removeAllViews();
        boolean fl=false;
        int tot=0;
        try {
            // Action 8  as Get data Job card History data
            search=inputSearch.getText().toString();
            String  totamount="";


            //Strt
            try {

                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                my_linear_layout.removeAllViews();

                if(jArray.length()==0 )
                {
                    // LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    //        LinearLayout.LayoutParams.MATCH_PARENT);
                    //param.gravity=1;
                    TextView lblList= new TextView(this.context);
                    lblList.setText("pendig order list not available");
                    lblList.setTextColor(getResources().getColor(R.color.QRCodeBlackColor));
                    lblList.setTextSize(12);
                    lblList.setGravity(Gravity.CENTER);
                    my_linear_layout.addView(lblList);
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
                    //lblsavestatus.setText(jObject.getString("savestatus"));
                    final String grcode=lblgrcode.getText().toString();
                    final String grname =grower_name.getText().toString();


                    view.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            CallPage(grcode, grname);

                        }
                    });
                    my_linear_layout.addView(view);

                }

            } catch (Exception e) {
                Log.e("JSONException", "Error: " + e.toString());

            }

        }

        catch (Exception e) {
            e.printStackTrace();

        }


    }
    public void  CallPage(String grcode,String grname)
    {
        Intent intent;
            Log.i("SData",grcode+" and "+grname);
       // if(locdata.getString("Pagetype",null).equals("SampleTesting"))
        {
            intent = new Intent(saleorderpending.this,orderlist.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Pagetype", "SampleTesting");
            intent.putExtra("grname", grname.trim());
            intent.putExtra("grcode", grcode.trim());
            intent.putExtra("Header", "Sample Testing Details");
           // loceditor.putString("grname",grname);
            //loceditor.putString("grcode",grcode);
           // loceditor.commit();
            startActivity(intent);
            // finish();
        }



    }


    public class GetOrderlist extends AsyncTask<String, String, String> {
        private String usercode;
        private String mobileno,pattern,OTP,grower_code,url,actionby;
        private int action;
        private ProgressDialog p;
        private String returnstring,personname;

        // private Context ctx;
        public GetOrderlist(String url,int action, String usercode,String pattern,String grower_code, Context ctx) {
            this.usercode = usercode;
            this.action = action;
            this.pattern = pattern;
            this.grower_code = grower_code;
            this.url=url;
            this.p = new ProgressDialog(ctx);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            p.setMessage("Wait........... ");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "IPMCommonwindowreport"));
            // postParameters.add(new BasicNameValuePair("xmlString", ""));
            String Urlpath1 = url + "?action=" + action + "&userCode=" + usercode + "" +
                    "&search=" + pattern+"&grower_code="+grower_code;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring = builder.toString();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                p.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                p.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                p.dismiss();
            }

            p.dismiss();
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                p.dismiss() ;
                if (result.contains("True")) {


                    // if (action==1) {
                    Onlinelist(result);
                    // }
                }
            }
            catch(Exception ex)
            {
                p.dismiss() ;
            }

        }
    }
    public class Saleorderdata extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,Comments,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,orderid;
        byte[] objAsBytes;

        public Saleorderdata(String orderid, String action,byte[] objAsBytes, String usercode,String saleorg,
                              String division,String customer,String name ,String DLV_plant,String customregroup   ){
            this.objAsBytes=objAsBytes;

            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.customregroup=customregroup;
            this.customer=customer;
            this.DLV_plant=DLV_plant;
            this.name=name;
            this.orderid=orderid;
            this.Comments="1";


        }
        protected void onPreExecute() {
            // pd = new ProgressDialog(context);
            pd.setTitle("Data Uploading ...");
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
            String Urlpath1= MDOurlpath+"?orderid="+orderid+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&customer="+customer+"" +
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"&name="+name+"&Comments="+Comments+"";
             Log.i("SaleorderPedingLIst",Urlpath1);
             Log.i("Paramters",postParameters.toString());

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


                    // if (action==1) {
                    Onlinelist(result);
                    // }
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




}