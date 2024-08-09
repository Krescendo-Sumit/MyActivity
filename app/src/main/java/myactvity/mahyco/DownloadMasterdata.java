package myactvity.mahyco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.WebService;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class DownloadMasterdata extends AppCompatActivity {

    public Button btnDownload, btncoupondata, btnDemoplot, btnhdps, btnDownloadRBM;
    public ProgressDialog dialog;

    public String SERVER = "";
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    public WebService wx;
    TextView lblmsg;
    String returnstring;
    public String MDOurlpath = "";
    Config config;
    SharedPreferences sp;
    String InTime, userCode;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_masterdata);
        getSupportActionBar().hide(); //<< this
        setTitle("Download Master Data");
        // setTitle("User Registration ");
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownloadRBM = (Button) findViewById(R.id.btnDownloadRBM);
        btncoupondata = (Button) findViewById(R.id.btncoupondata);
        btnDemoplot = (Button) findViewById(R.id.btnDemoplot);
        btnhdps = (Button) findViewById(R.id.btnhdps);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        MDOurlpath = cx.MDOurlpath;
        SERVER = cx.Urlpath;
        wx = new WebService();
        mDatabase = SqliteDatabase.getInstance(this);

        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        lblmsg = (TextView) findViewById(R.id.lblmsg);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sp = getApplicationContext().getSharedPreferences("MyPref", 0);
        userCode = sp.getString("UserID", null);
        Date entrydate = new Date();
        // String  InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        InTime = new SimpleDateFormat("dd-MM-yyyy").format(entrydate);
        context = DownloadMasterdata.this;
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        if (sp.getString("unit", null).contains("VCBU")) {
            btncoupondata.setVisibility(View.GONE);
            btnDemoplot.setVisibility(View.GONE);
            btnhdps.setVisibility(View.GONE);
        }


        if (config.NetworkConnection()) {
            try {
                String IME = msclass.getDeviceIMEI();
                SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0);
                String userCode = sp.getString("UserID", null);
                userCode = userCode.replace(" ", "%20");
                IME = IME.replace(" ", "%20");
                Log.i("Token", "Bearer " + sp.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                // new CheckVersion().execute("https://feedbackapi.mahyco.com/api/Feedback/getAppFeedbackStatus?packageName=myactvity.mahyco&userCode="+userCode+"&IMEICode="+IME+"");
                new CheckVersion().execute("https://feedbackapi.mahyco.com/api/Feedback/getAppFeedbackStatus?packageName=myactvity.mahyco&userCode=" + userCode + "");
            } catch (Exception e) {

            }
        } else {

        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblmsg.setText("Master data downloading on process 1");
                Downloadmaster();
            }
        });
        btnDownloadRBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DownloadMasterdataRBM.class);
                startActivity(intent);


            }
        });
        btnDemoplot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblmsg.setText("Master data downloading on process 2");
                downloadplotdata();

            }
        });

        btncoupondata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblmsg.setText("Master data downloading on process 3");
                downloadcoupondata();
            }
        });

        btnhdps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblmsg.setText("Master data downloading on process 4");
                downloadHDPSDATA();
            }
        });
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logDownloadMasterData();

    }

    public void downloadplotdata() {
        try {

            //Before   upload coupon data
           /* if (recordshowCoupon() > 0) {
                msclass.showMessage("before download, please upload your coupon scan pending data. ");
            }
            else*/
            {
                if (config.NetworkConnection()) {
                    String str = null;

                    lblmsg.setText("Master data downloading on process ..3");
                    try {
                        new MDOMyplotDataDownload(1, userCode, DownloadMasterdata.this, "result").execute(MDOurlpath);

                    } catch (Exception ex) {
                        msclass.showMessage(ex.getMessage());
                    }
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            }


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }

    public void downloadcoupondata() {
        try {

            //Before   upload coupon data
           /* if (recordshowCoupon() > 0) {
                msclass.showMessage("before download, please upload your coupon scan pending data. ");
            }
            else*/
            {
                if (config.NetworkConnection()) {
                    String str = null;

                    lblmsg.setText("Master data downloading on process ..3");
                    try {
                        new MDOCouponSchemeDataDownload(1, userCode, DownloadMasterdata.this, "result").execute(MDOurlpath);

                    } catch (Exception ex) {
                        msclass.showMessage(ex.getMessage());
                    }
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            }


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }


    public void Downloadmaster() {
        try {

            //Before   upload coupon data
           /* if (recordshowCoupon() > 0) {
                msclass.showMessage("before download, please upload your coupon scan pending data. ");
            }
            else*/
            {
                if (config.NetworkConnection()) {
                    String str = null;
                    String uid = sp.getString("UserID", null);
                   /* mDatabase.deleterecord("delete from DemoModelData where sowingDate like '%2019%'");
                    mDatabase.deleterecord("delete from DemoReviewData where visitingDate like '%2019%'");
                    mDatabase.deleterecord("delete from ValidatedDemoModelData where sowingDate like '%2019%'");
                    mDatabase.deleterecord("delete from ValidatedDemoReviewData where visitingDate like '%2019%'");
*/

                    lblmsg.setText("Master data downloading on process");
                    try {
                        //new MDOMasterData(4, uid, "", DownloadMasterdata.this).execute(MDOurlpath);
                        new MDOMasterData(5, uid, "", DownloadMasterdata.this).execute(MDOurlpath);

                        //str = cx.PostData();
                        // str = wx.MDOMAsterdata("","MDOVerify_user");
                    } catch (Exception ex) {
                        msclass.showMessage(ex.getMessage());
                    }
                } else {
                    msclass.showMessage("Internet network not available.");
                }
            }


        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        // dismissProgressDialog();
        try {
            dialog.dismiss();
            // accessTokenTracker.stopTracking();
        } catch (Exception ex) {

        }
        super.onDestroy();
    }

    private class MDOMasterData extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String password;
        private ProgressDialog p;
        private Context ctx;

        public MDOMasterData(int action, String username, String password, Context ctx) {
            this.action = action;
            this.username = username;
            this.password = password;
            this.p = new ProgressDialog(ctx);
        }

        @Override
        protected void onPreExecute() {
            // dialog.setMessage("Loading....");
            //dialog.show();
            super.onPreExecute();
            p.setMessage("Downloading ");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit

            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDOVerify_user"));
            // postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1 = MDOurlpath + "?username=" + username.trim() + "&sapcode=" + action + "&password=" + password + "";
            Log.d("URLMAsterDAta", Urlpath1);

            HttpPost httppost = new HttpPost(Urlpath1);

            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);

            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");
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
                    returnstring = builder.toString();

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                // dialog.dismiss();
                p.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                dialog.dismiss();
                p.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                // dialog.dismiss();
                p.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                // dialog.dismiss();
                p.dismiss();
            } catch (Throwable e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                // dialog.dismiss();
                p.dismiss();
            }

            // p.dismiss();
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DOWNLOAD", "RESPONSE : " + result);
            try {

                lblmsg.setText("Master data downloading on process,Wait step1....");
                if (handleAllResponse(p, result) == true) {
                    p.dismiss();
                    //   new MDOCouponSchemeDataDownload(1, username, DownloadMasterdata.this, result).execute(MDOurlpath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class MDOCouponSchemeDataDownload extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String allData;
        private ProgressDialog p;
        private Context ctx;

        public MDOCouponSchemeDataDownload(int action, String username, Context ctx, String allData) {
            this.action = action;
            this.username = username.trim();
            this.allData = allData;


            this.p = new ProgressDialog(ctx);
        }

        @Override
        protected void onPreExecute() {
            // dialog.setMessage("Loading....");
            //dialog.show();
            super.onPreExecute();
            p.setMessage("Downloading 2 ");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit

            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "mdo_couponSchemeDownloadAndUpload"));
            // postParameters.add(new BasicNameValuePair("xmlString",""));

            String Urlpath1 = MDOurlpath + "?Type=" + "mdo_couponSchemeDownloadAndUpload" + "&action=" + action + "&userCode=" + username + "";
            Log.i("Urls", Urlpath1);
            HttpPost httppost = new HttpPost(Urlpath1);

            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);

            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");

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
                    returnstring = builder.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                // dialog.dismiss();
                p.dismiss();
            }

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                lblmsg.setText("Master data downloading on process,Wait step2....");
                mDatabase.deleledata("CouponSchemeMaster", " ");
                mDatabase.deleledata("couponMaster", " ");
                mDatabase.deleledata("CheckUploadCoupon", " ");
                mDatabase.deleledata("MDO_checkHybridMaster", " ");
                mDatabase.deleledata("couponPaymentAmount", " ");
                boolean f = true;  // mDatabase.deletetable();

                if (f == true) {
                    JSONObject object = new JSONObject(result);

                    JSONArray jArray = object.getJSONArray("Table");
                    JSONArray jArray2 = object.getJSONArray("Table1");
                    JSONArray jArray3 = object.getJSONArray("Table2");
                    JSONArray jArray4 = object.getJSONArray("Table4");
                    boolean f1 = false;

                    f1 = mDatabase.InsertCouponSchemeMasternew(jArray);
                    f1 = mDatabase.InsertCouponMasternew(jArray2, InTime);

                    /*
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        f1 = mDatabase.InsertCouponSchemeMaster(jObject.getString("year").toString(),
                                jObject.getString("season").toString(), jObject.getString("state").toString()
                                , jObject.getString("region"), jObject.getString("crop")
                                , jObject.getString("product"), jObject.getString("productCode")
                                , jObject.getString("schemeName"), jObject.getString("schemeUnit")
                                , jObject.getString("advAmtPerUnit"), jObject.getString("perUnitBenifitOffer")
                                , jObject.getString("couponValue"), jObject.getString("schemeFrom")
                                , jObject.getString("schemeEnd"));
                    }
                    for (int i = 0; i < jArray2.length(); i++) {
                        JSONObject jObject2 = jArray2.getJSONObject(i);
                        f1 = mDatabase.InsertCouponMaster(jObject2.getString("couponCode").toString(),
                                jObject2.getString("productCode").toString(),InTime);
                    }
                 */
                    if (result.contains("updateTrue")) {
                        f1 = mDatabase.InsertCouponCheckDatanew(jArray3);
                        /*for (int j = 0; j < jArray3.length(); j++) {
                            JSONObject jObject3 = jArray3.getJSONObject(j);
                            f1 = mDatabase.InsertCouponCheckData(jObject3.getString("userCode").toString(),
                                    jObject3.getString("mobileNumber").toString(), jObject3.getString("crop").toString()
                                    ,jObject3.getString("product").toString(), jObject3.getString("farmerPhoto").toString());
                        }*/
                        //mDatabase.Updatedata("update  CouponRecordData set isSynced=0,imgStatus=0 where farmerPhoto " +
                        //        " not in (select farmerPhoto from CheckUploadCoupon) and userCode='"+username+"' ");
                        // mDatabase.deleterecord("delete  from  CouponRecordData  where farmerPhoto " +
                        //        "  in (select farmerPhoto from CheckUploadCoupon where mobileNumber='1') and userCode='"+username+"' ");
                    }

                    // NEW Change

                   /* for (int i = 0; i < 1; i++) {
                        JSONObject jObject4 = jArray4.getJSONObject(i);
                        mDatabase.deleledata("couponPaymentAmount"," ");
                        mDatabase.InsertCouponPaymentAmount(jObject4.getString("paymentAmount").toString(),
                                jObject4.getString("remainingAmount").toString());
                    }*/

                    //if(handleAllResponse( p,allData)==true) old
                    {
                        if (f1 == true) {
                            p.dismiss();
                            lblmsg.setText("coupon data download successfully....");
                            msclass.showMessage("Coupon data download successfully....");
                        }
                        // new MDOMyplotDataDownload(1, username, DownloadMasterdata.this, result).execute(MDOurlpath);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                msclass.showMessage(e1.getMessage());
                p.dismiss();
            }

        }
    }

    public boolean handleAllResponse(ProgressDialog p, String result) {
        boolean f1 = false;
        try {
            mDatabase.deleledata("mdo_pogsapdata", " ");
            mDatabase.deleledata("FocussedVillageMaster", " ");
            boolean ff = mDatabase.deletetable();
//mdotag retailer list
            //rbm and tbm

            if (ff == true) {
                JSONObject object = new JSONObject(result);

                JSONArray jArray = object.getJSONArray("Table");
                JSONArray jArray2 = object.getJSONArray("Table1");
                JSONArray jArray3 = object.getJSONArray("Table2");
                JSONArray jArray4 = object.getJSONArray("Table3");
                JSONArray jArray5 = object.getJSONArray("Table4");
                JSONArray jArray6 = object.getJSONArray("Table5");
                JSONArray jArray7 = object.getJSONArray("Table6");
                JSONArray jArray8 = object.getJSONArray("Table7");
                JSONArray jArray9 = object.getJSONArray("Table8");
                JSONArray jArray10 = object.getJSONArray("Table9");
                JSONArray jArray11 = object.getJSONArray("Table10");
                JSONArray jArray12 = object.getJSONArray("Table11");
                JSONArray jArray13 = object.getJSONArray("Table12");

                Log.d("dataMainObject", object.toString());
                Log.d("dataTable1", jArray.toString());
                Log.d("dataTable2", jArray2.toString());
                Log.d("dataTable3", jArray3.toString());
                Log.d("dataTable4", jArray4.toString());
                Log.d("dataTable5", jArray5.toString());
                Log.d("dataTable6", jArray6.toString());
                Log.d("dataTable7", jArray7.toString());
                Log.d("dataTable8", jArray8.toString());
                Log.d("dataTable9", jArray9.toString());
                Log.d("dataTable10", jArray10.toString());
                Log.d("dataTable11", jArray11.toString());
                Log.d("dataTable12", jArray12.toString());
                Log.d("dataTable13", jArray13.toString());
               /* for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                   boolean fl = mDatabase.insertVillageMasterdata("1","1",jObject.getString("District").toString(),jObject.getString("District_code").toString(),jObject.getString("Taluka").toString(),jObject.getString("Taluka_code"),jObject.getString("Village"),jObject.getString("Village_code"));
                } */

                f1 = mDatabase.InsertCropMasterDatanew(jArray2);
                f1 = mDatabase.InsertMyActivityDatanew(jArray3);
                f1 = mDatabase.InsertCommentrDatanew(jArray5);

               /* for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject jObject2 = jArray2.getJSONObject(i);
                    f1 = mDatabase.InsertCropMasterData(jObject2.getString("ProductName").toString(), jObject2.getString("Cropname").toString(),
                            jObject2.getString("CropType").toString(), jObject2.getString("Crop_Code").toString(),
                            jObject2.getString("Prod_Code").toString());
                }
                for (int i = 0; i < jArray3.length(); i++) {
                    JSONObject jObject3 = jArray3.getJSONObject(i);
                    f1 = mDatabase.InsertMyActivityData(jObject3.getString("ActivityName").toString(), jObject3.getString("activityType").toString()
                            , jObject3.getString("activityTypeCode").toString(), jObject3.getString("activityNameCode").toString());//jArray3);
                }
                for (int i = 0; i < jArray5.length(); i++) {
                    JSONObject jObject5 = jArray5.getJSONObject(i);
                    f1 = mDatabase.InsertCommentrData(jObject5.getString("commentlist").toString());
                }*/
                for (int i = 0; i < jArray6.length(); i++) {
                    JSONObject jObject6 = jArray6.getJSONObject(i);
                    f1 = mDatabase.InsertVehicle(jObject6.getString("VehicleNo").toString());
                }
                for (int i = 0; i < jArray8.length(); i++) {
                    JSONObject jObject8 = jArray8.getJSONObject(i);
                    f1 = mDatabase.insertStateTerritoryMasterData(jObject8.getString("ZONE").toString(), jObject8.getString("TERRITORY").toString()
                            , jObject8.getString("STATE").toString(), jObject8.getString("STATE_CODE").toString());
                }

                for (int i = 0; i < jArray9.length(); i++) {
                    JSONObject jObject9 = jArray9.getJSONObject(i);
                    f1 = mDatabase.insertFocussedVillageMasterData(jObject9.getString("vil_code").toString(), jObject9.getString("vil_desc").toString(),
                            jObject9.getString("taluka").toString());
                }
                for (int i = 0; i < jArray11.length(); i++) {
                    JSONObject jObject11 = jArray11.getJSONObject(i);
                    f1 = mDatabase.insertMdoTbm(jObject11.getString("MDOCode")
                                    .toString(), jObject11.getString("MDO_name").toString(),
                            jObject11.getString("TBMCode")
                                    .toString(), jObject11.getString("TBMName").toString());
                }

                for (int i = 0; i < jArray12.length(); i++) {
                    JSONObject jObject12 = jArray12.getJSONObject(i);
                    f1 = mDatabase.insertRbm(jObject12.getString("RBMCode").
                                    toString(), jObject12.getString("RBMName").toString(),
                            jObject12.getString("TBMCode")
                                    .toString(), jObject12.getString("TBMName").toString());
                }
                for (int i = 0; i < jArray13.length(); i++) {
                    JSONObject jObject13 = jArray13.getJSONObject(i);
                    f1 = mDatabase.insertCheckHybridMaster(jObject13.getString("hybridName").toString());
                }

                for (int i = 0; i < jArray10.length(); i++) {
                    JSONObject jObject10 = jArray10.getJSONObject(i);

                    f1 = mDatabase.InsertMDO_NEWRetailer(jObject10.getString("state").toString()
                            , jObject10.getString("type").toString(), jObject10.getString("mdocode").toString()
                            , jObject10.getString("marketplace").toString(), jObject10.getString("dist").toString()
                            , jObject10.getString("taluka").toString(), jObject10.getString("mobileno").toString()
                            , jObject10.getString("firmname").toString(), jObject10.getString("name").toString(),
                            jObject10.getString("retailercode").toString());
                }


                f1 = mDatabase.InsertRetailerDatanew(jArray4);
                if (f1 == true) {
                    //for(int i=0; i < jArray4.length(); i++) {
                    //   JSONObject jObject4 = jArray4.getJSONObject(i);
                    //f1= mDatabase.InsertRetailerData(jObject4.getString("Taluka").toString(),jObject4.getString("Taluka_code").toString(),jObject4.getString("RetailerName").toString(),jObject4.getString("actvity").toString());
                    // }
                    int total = jArray.length() + jArray4.length();
                    f1 = mDatabase.insertVillageMasterdata1(jArray);
                    if (f1 == true) {

                        f1 = mDatabase.insertmdopogsapdata(jArray7);

                        if (f1 == true) {
                            //msclass.showMessage("Master data download successfully(" + total + ")");
                            //Toast.makeText(this, "Master data download successfully", Toast.LENGTH_SHORT).show();
                            p.dismiss();


                            lblmsg.setText("Master data download Done.");
                            msclass.showMessage("Master data download successfuly");
                        } else {
                            msclass.showMessage("Master data not downloaded-SAP Dist.Stock step3");
                            //Toast.makeText(this, "Master data download successfully", Toast.LENGTH_SHORT).show();
                            p.dismiss();
                        }

                    } else {
                        msclass.showMessage("Master data not downloaded step3");
                        //Toast.makeText(this, "Master data download successfully", Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }
                } else {
                    msclass.showMessage("Master data not downloaded-R");
                    //Toast.makeText(this, "Master data download successfully", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            }
        }
            /*catch (InterruptedException e) {
                e.printStackTrace();
                dialog.dismiss();
                lblmsg.setText(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                dialog.dismiss();
                lblmsg.setText(e.getMessage());
            }*/ catch (JSONException e) {
            e.printStackTrace();
            p.dismiss();
            lblmsg.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            p.dismiss();
            lblmsg.setText(e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
            p.dismiss();
            lblmsg.setText(e.getMessage());
        }
        return f1;

    }


    private class MDOMyplotDataDownload extends AsyncTask<String, String, String> {

        private String username;
        private String allData;
        private Context ctx;
        private int action;
        private ProgressDialog p;

        public MDOMyplotDataDownload(String username, Context ctx) {
            this.username = username;
            this.allData = allData;
            this.ctx = ctx;
        }

        public MDOMyplotDataDownload(int action, String username, Context ctx, String allData) {
            this.action = action;
            this.username = username;
            this.allData = allData;
            //this.p = new ProgressDialog(ctx);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading (4)");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDOMyplotDataDownload"));
            String Urlpath1 = MDOurlpath + "?userCode=" + username + "";
            Log.i("Url", Urlpath1);
            Log.i("PARAM", postParameters.toString());
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
                    returnstring = builder.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                dialog.dismiss();

            }

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                mDatabase.deleterecord("delete from DemoModelData where sowingDate like '%2019%'");
                mDatabase.deleterecord("delete from DemoReviewData where visitingDate like '%2019%'");
                mDatabase.deleterecord("delete from ValidatedDemoModelData where sowingDate like '%2019%'");
                mDatabase.deleterecord("delete from ValidatedDemoReviewData where visitingDate like '%2019%'");

                mDatabase.deleledata("DemoModelData", " where isSynced= '1'  ");
                mDatabase.deleledata("DemoReviewData", " where isSynced= '1' ");
                JSONObject object = new JSONObject(result);
                Log.d("My plot  data", object.toString());
                JSONArray jArray = object.getJSONArray("Table");
                JSONArray jArray2 = object.getJSONArray("Table1");
                JSONArray jArray3 = object.getJSONArray("Table2");
                boolean f1 = false;

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);

                    f1 = mDatabase.insertDemoModelData(jObject.getString("uId").toString(),
                            jObject.getString("userCode").toString(), jObject.getString("plotType").toString()
                            , jObject.getString("state"), jObject.getString("district")
                            , jObject.getString("taluka"), jObject.getString("village"), jObject.getString("farmerName")
                            , jObject.getString("mobileNumber"), jObject.getString("whatsappNumber")
                            , jObject.getString("crop"), jObject.getString("product")
                            , jObject.getString("area"), jObject.getString("seedQuantity"), ""
                            , jObject.getString("sowingDate"), jObject.getString("coordinates"),
                            jObject.getString("soilType"), jObject.getString("irrigationMode")
                            , jObject.getString("spacingRow"), jObject.getString("spacingPlan"),
                            jObject.getString("imgName")
                            , jObject.getString("imgPath"), "1", "1",
                            jObject.getString("checkHybrids"),
                            jObject.getString("remarks"),
                            jObject.has("checkHybridsSelected") && !jObject.getString("checkHybridsSelected").equals("null") ? jObject.getString("checkHybridsSelected") : "",
                            jObject.has("focussedVillage") && !jObject.getString("focussedVillage").equals("null") ? jObject.getString("focussedVillage") : "",
                            jObject.has("vill_code") && !jObject.getString("vill_code").equals("null") ? jObject.getString("vill_code") : "");

                }

                for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject jObject2 = jArray2.getJSONObject(i);
                    f1 = mDatabase.insertDemoModelReview(jObject2.getString("uId"), jObject2.getString("uIdP"), jObject2.getString("userCode"), jObject2.getString("taluka"),
                            jObject2.getString("farmerName"), jObject2.getString("mobileNumber"), jObject2.getString("crop")
                            , jObject2.getString("product"), "", "", jObject2.getString("visitingDate"), jObject2.getString("coordinates"), jObject2.getString("purposeVisit")
                            , jObject2.getString("comment"), jObject2.getString("imgName"),
                            jObject2.getString("imgPath"), "1", "1",
                            jObject2.has("focussedVillage") && !jObject2.getString("focussedVillage").equals("null") ? jObject2.getString("focussedVillage") : "",
                            jObject2.has("fieldPest") && !jObject2.getString("fieldPest").equals("null") ? jObject2.getString("fieldPest") : "",
                            jObject2.has("fieldDiseases") && !jObject2.getString("fieldDiseases").equals("null") ? jObject2.getString("fieldDiseases") : "",
                            jObject2.has("fieldNutrients") && !jObject2.getString("fieldNutrients").equals("null") ? jObject2.getString("fieldNutrients") : "",
                            jObject2.has("fieldOther") && !jObject2.getString("fieldOther").equals("null") ? jObject2.getString("fieldOther") : "",
                            jObject2.has("cropCondition") && !jObject2.getString("cropCondition").equals("null") ? jObject2.getString("cropCondition") : "",
                            jObject2.has("recommendations") && !jObject2.getString("recommendations").equals("null") ? jObject2.getString("recommendations") : ""
                    );
                }


                if (f1 == true) {
                    msclass.showMessage("plot/model Data download successfully");
                    dialog.dismiss();


                } else {
                    msclass.showMessage("Data Not available");
                    dialog.dismiss();
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
                dialog.dismiss();
            }


        }
    }

    private void logDownloadMasterData() {
        if (sp != null) {
            String userId = "", displayName = "";
            if (sp.getString("UserID", null) != null && sp.getString("Displayname", null) != null) {
                userId = sp.getString("UserID", "");
                displayName = sp.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callDownloadMasterDataEvent(userId, displayName);
            }
        }
    }

    private void downloadHDPSDATA() {
        //Toast.makeText(DownloadMasterdata.this,"Download HDPS Data",Toast.LENGTH_SHORT).show();
        new DownloadHDPSDATA("", this).execute(SERVER);

    }


    private class DownloadHDPSDATA extends AsyncTask<String, String, String> {
        ProgressDialog progressDailog;
        Context context;

        public DownloadHDPSDATA(String Funname, Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progressDailog = new ProgressDialog(context);
            progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDailog.setCanceledOnTouchOutside(false);
            progressDailog.setCancelable(false);
            progressDailog.setMessage("Data Downloading");
            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject table = new JSONObject();
                table.put("userId", userCode);
                jsonObject.put("Table", table);
                String result = HttpUtils.POSTJSON(Constants.HDPS_DOWNLOAD_MASTER_API, jsonObject, "");
                Log.d("HDPS", "DOWNLOAD URL : " + Constants.HDPS_DOWNLOAD_MASTER_API);
                Log.d("HDPS", "DOWNLOAD JSON OBJECT : " + jsonObject);
                Log.d("HDPS", "DOWNLOAD USER CODE : " + userCode);
                Log.d("HDPS", "DOWNLOAD RESPONSE : " + result);
                return result;
            } catch (Exception e) {
                Log.d("HDPS", "MSG : " + e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();

                if (progressDailog != null) {
                    progressDailog.dismiss();
                }

                if (resultout.contains("true")) {
                    msclass.showMessage("Data downloaded Successfully.");
                    lblmsg.setText("HDPS data download successfully....");
                    mDatabase.deleledata("HDPSMasterScheme", " ");
                    mDatabase.deleledata("HDPSCouponRecords", " ");
                    boolean f = true;  // mDatabase.deletetable();
                    if (f == true) {
                        JSONObject object = new JSONObject(result);
                        JSONArray jArrayT1 = object.getJSONArray("Table1");
                        JSONArray jArrayT2 = object.getJSONArray("Table2");
                        Log.d("HDPS", "InsertHDPSMasterScheme : " + jArrayT1);
                        Log.d("HDPS", "InsertHDPSCouponRecords : " + jArrayT2);
                        boolean f1 = mDatabase.InsertHDPSMasterScheme(jArrayT1);
                        boolean f2 = mDatabase.InsertHDPSCouponRecords(jArrayT2, InTime);
                        Log.d("HDPS", "INSERT RESULT Table1 : " + f1);
                        Log.d("HDPS", "INSERT RESULT Table2 : " + f2);
                        mDatabase.printColumnNoHDPSDownload();
                    }
                } else {
                    msclass.showMessage("Something went wrong, please try later.");

                }
                Log.d("Response", resultout);
            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.toString() + "-");
            }
        }

    }

    SharedPreferences.Editor editor;


    private class CheckVersion extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(context);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
            //   uiUpdate.setText("Output : ");
            //  Dialog.setMessage("Please Wait..");
            // Dialog.show();
            //pb.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {

                Log.d("Url", urls[0]);
                Log.d("Url", urls[0]);
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.

                // Server url call by GET method
                HttpPost httpget = new HttpPost(urls[0]);
                //     httpget.setHeader("Authorization", "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);

            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            editor = preferences.edit();
            // Close progress dialog
            //Dialog.dismiss();

            if (Error != null) {

                //  uiUpdate.setText("Output : "+Error);

            } else {
                //pb.setVisibility(View.GONE);
                //   uiUpdate.setText("Output : "+Content);
                // loadFromServer(Content.toString().trim());
                Log.i("Details123", "" + Content);
                //   Toast.makeText(getApplicationContext(), ""+Content, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jsonVersionDetails = new JSONObject(Content.trim());
                    String vcode = BuildConfig.VERSION_NAME;

                    if (jsonVersionDetails.getBoolean("success")) {

                        if (!(vcode.trim().equals(jsonVersionDetails.getString("AppVersion").trim()))) {
                            showUpdateDialog();
                        }
                        if (jsonVersionDetails.getInt("UserStatus") == 0) {

                            new androidx.appcompat.app.AlertDialog.Builder(DownloadMasterdata.this)
                                    .setMessage("Session Expired . Please login again.")
                                    .setTitle("Information")
                                    .setPositiveButton("Login Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putString("UserID", null);
                                            editor.commit();
                                            //finish();
                                            //System.exit(0);
                                            logLogOutEvent();
                                            Intent intent = new Intent(DownloadMasterdata.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                            return;
                        }


                    } else  //  Coming False from the Version API
                    {
                        Toast.makeText(context, "" + jsonVersionDetails.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.i("Error is ", e.getMessage());
                }
            }
        }

    }

    SharedPreferences preferences;

    private void logLogOutEvent() {
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        if (preferences != null) {
            String userId = "", displayName = "";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null) {
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callLogoutEvent(userId, displayName);
            }
        }
    }

    private void showUpdateDialog() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("A new update is available.");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=myactvity.mahyco")));
                dialog.dismiss();
            }
        });

        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });*/

        builder.setCancelable(false); //Update 17 Jan. 2022
        dialog1 = builder.show();
    }

    Dialog dialog1;

}

            /*catch (InterruptedException e) {
                e.printStackTrace();
                dialog.dismiss();
                lblmsg.setText(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                dialog.dismiss();
                lblmsg.setText(e.getMessage());
            }*/





