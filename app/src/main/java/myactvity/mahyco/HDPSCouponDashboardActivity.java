package myactvity.mahyco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.newupload.HDPSPaymentDepositAPI;

public class HDPSCouponDashboardActivity extends AppCompatActivity implements HDPSPaymentDepositAPI.HDPSPaymentDepositListener {
    private Context context;
    Button  btncouponpayment, btnCouponPaymentHistory,btndAdvanceBooking;
    Config config;
    private SqliteDatabase mDatabase;
    public CommonExecution cx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public String userCode;
    public Messageclass msclass;
    final String TAG="couponDashboard";
    TextView txt_totalcoupons, txtsoldcoupons, txtbalancecoupons, txttotalamt, txtpaidamt, txtbalanceamt;
    HDPSPaymentDepositAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d_p_s_coupon_dashboard);
        getSupportActionBar().hide(); //<< this
        context = this;
        config = new Config(this); //Here the context is passing
        mDatabase = SqliteDatabase.getInstance(this.context);
        // mDatabase.open();
        cx = new CommonExecution(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        logMFDCData();
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);
        btncouponpayment = (Button) findViewById(R.id.btncouponpayment_hdps);
        btnCouponPaymentHistory = (Button) findViewById(R.id.btncouponpaymentHistory_hdps);
        btndAdvanceBooking = (Button) findViewById(R.id.btndAdvanceBooking_hdps);

        api = new HDPSPaymentDepositAPI(context, this);
        txt_totalcoupons = findViewById(R.id.txt_totalcoupons);
        txtsoldcoupons = findViewById(R.id.txtsoldcoupons);
        txtbalancecoupons = findViewById(R.id.txtbalancecoupons);

        txttotalamt = findViewById(R.id.txttotalamt);
        txtpaidamt = findViewById(R.id.txtpaidamt);
        txtbalanceamt = findViewById(R.id.txtbalanceamt);


        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        try {

            getUserCouponDetails(userCode);
        } catch (Exception e) {

        }
        btndAdvanceBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date d=new Date();
                String strdate=dateFormat.format(d);
                //if (5 > 0)

                if (mDatabase.getCountDt("HDPSCouponRecords") > 0) {
                    //Online check coupon balance amount
                    try {
                        Intent intent = new Intent(context.getApplicationContext(), HDPSNewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    } catch (Exception e) {
                        Log.d(TAG, "onClick: Booking ");
                    }
                }
                else
                {
                    msclass.showMessage("Coupon list not available ,Please download master data");
                }
            }
        });
        btncouponpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection()) {
                        Intent intent = new Intent(context.getApplicationContext(), HDPSPaymentDeposit.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else {
                        msclass.showMessage("Please check the internet connection");
                    }
                    /*
                    // this code is Commented to Open New Payment Flow to add manual Trasaction Ref.
                    // Code added By Sumit on 23/04/2024

                    if (config.NetworkConnection()) {
                        Intent intent = new Intent(context.getApplicationContext(), couponpaymentHDPS.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else {
                        msclass.showMessage("Please check the internet connection");
                    }
                        */
                } catch (Exception ex) {
                    //msclass.showMessage(ex.getMessage());
                    Log.d("HDPS","MSG : "+ex.getMessage());
                }

            }
        });
        btnCouponPaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (config.NetworkConnection()) {

                       /* Intent intent = new Intent(HDPSCouponDashboardActivity.this, PaymentHistoryActivityHDPS.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);*/

                        Intent intent = new Intent(HDPSCouponDashboardActivity.this, HDPSPaymentDetails.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        
                    } else
                    {
                   /*     Intent intent = new Intent(HDPSCouponDashboardActivity.this, PaymentHistoryActivityHDPS.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);*/
                        Toast.makeText(context, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //msclass.showMessage(ex.getMessage());
                }


            }
        });
    }

    private class UploadCouponDataServer extends AsyncTask<String, String, String> {
        ProgressDialog progressDailog;
        String  action="";
        public UploadCouponDataServer(String Funname, Context context) {
            // this.action="3"; // for check coupon amount exccced old
            this.action="5"; // for check coupon amount exccced Online check
        }
        protected void onPreExecute() {
            progressDailog = new ProgressDialog(context);
            progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDailog.setCanceledOnTouchOutside(false);
            progressDailog.setCancelable(false);
            progressDailog.setMessage("Data Uploading");
            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "mdo_couponSchemeDownloadAndUpload"));
            String Urlpath = cx.MDOurlpath + "?imgName=blank&action=" + action + "&userCode=" + userCode;
            Log.d("url", "image" + Urlpath);
            Log.d("parameters", "params " + postParameters);
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
                        builder.append(line).append("\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return builder.toString();

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();
                if (progressDailog != null) {
                    progressDailog.dismiss();

                }

                // progressDailog.dismiss();
                if (resultout.contains("True")) {
                    /*Intent intent = new Intent(context.getApplicationContext(), AdvanceBookingCouponActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);*/
                    updatepaymentamount(resultout);

                } else
                {
                    // updatepaymentamount(resultout);
                    JSONObject object = new JSONObject(resultout);
                    JSONArray jArray = object.getJSONArray("Table");
                    JSONObject jObject = jArray.getJSONObject(0);
                    msclass.showMessage(jObject.getString("message").toString() + "Error");

                }
                Log.d("Response", resultout);


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.toString()+result+ "Error");
            }

        }

    }
    public void updatepaymentamount ( String resultout)
    {
        try {
            JSONObject object = new JSONObject(resultout);
            JSONArray jArray = object.getJSONArray("Table");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String str=jObject.getString("paymentAmount").toString();
                mDatabase.deleledata("couponPaymentAmount"," ");
                mDatabase.InsertCouponPaymentAmount(jObject.getString("paymentAmount").toString(),
                        jObject.getString("remainingAmount").toString());
            }
            Log.d("Response", resultout);
        } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString()+ "Error");
        }
        checkamountbalance();
    }

    public void checkamountbalance()
    {
        try {
            int amount =0;
            String st="";
            String searchQuery = " select IFNULL( SUM(amount) ,0) as amount from" +
                    " ( SELECT  a.*, a.couponsIssued  *  (case when a.crop='PADDY' " +
                    " then 60 else  50 end) as amount    FROM CouponRecordData  a ) as aa ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            int dd= cursor.getCount();
            while (cursor.isAfterLast() == false) {
                st=cursor.getString(0);
                amount=Integer.parseInt(cursor.getString(0).toString());
                cursor.moveToNext();
            }
            cursor.close();
            int paymentamount  =0;
            int remainingAmount=0;
            int couponscanamt=0;
            String searchQuery2 = " select IFNULL( SUM(paymentAmount) ,0) as paymentAmount" +
                    ",IFNULL( SUM(remainingAmount) ,0) as remainingAmount," +
                    "IFNULL( SUM(newCouponScanAmount) ,0) as newCouponScanAmount" +
                    "  from couponPaymentAmount  ";
            Cursor cursor2 = mDatabase.getReadableDatabase().rawQuery(searchQuery2, null);
            cursor2.moveToFirst();
            while (cursor2.isAfterLast() == false) {
                paymentamount=Integer.parseInt(cursor2.getString(0).toString());
                remainingAmount=Integer.parseInt(cursor2.getString(1).toString());
                couponscanamt=Integer.parseInt(cursor2.getString(2).toString());
                cursor2.moveToNext();
            }
            cursor2.close();
            int balanceamount =0;
            if (remainingAmount>=2000) {
                balanceamount=remainingAmount;
            }
            else
            {
                balanceamount=(couponscanamt+remainingAmount);//-paymentamount;
            }
            //  balanceamount=(amount+remainingAmount)-paymentamount;
            if (balanceamount >= 2000)
            {
                msclass.showMessage("Your payment for MFDC Coupons is pending and has reached the set money transfer limit of 2000/-.\n" +
                        "Kindly pay the amount online and then proceed to issue new coupons.");
            }
            else
            {
                Intent intent = new Intent(context.getApplicationContext(), AdvanceBookingCouponActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.toString()+ "Error");
        }
    }

    private void logMFDCData(){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callHDPSDashboardEvent(userId,displayName);
            }
        }
    }


    public void getUserCouponDetails(String userCode) {
        try {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            String otherData = "-" + versionName + "-" + versionCode;
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("Mdocode", userCode);//: "string",
            jsonObject.addProperty("UPIRefNo", "");//: "string",
            jsonObject.addProperty("Amount", "");//: 0,
            jsonObject.addProperty("Remark1", "");//: "string",
            jsonObject.addProperty("Remark2", "");//: "string"


            api.getHDPSUserwiseReport(jsonObject);


        } catch (Exception e) {

        }
    }


    @Override
    public void onPaymentDeposit(String result) {

    }

    @Override
    public void getHDPSUserwiseReport(String result) {
        try {

            if (result != null) {
                JSONObject jsonObject = new JSONObject(result.trim());
                if (jsonObject.getBoolean("success")) {
                    JSONObject returnval = jsonObject.getJSONObject("returnval");
                    if (returnval != null) {
                        JSONArray jsonArray = returnval.getJSONArray("Table");
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            Toast.makeText(context, "" + jsonObject1.getString("Total Amount"), Toast.LENGTH_SHORT).show();
                            int balc = jsonObject1.getInt("CouponAllocate") - jsonObject1.getInt("TotCouponSold");

                            txt_totalcoupons.setText("" + jsonObject1.getInt("CouponAllocate"));
                            txtsoldcoupons.setText("" + jsonObject1.getInt("TotCouponSold"));
                            txtbalancecoupons.setText("" + balc);
                            txttotalamt.setText("" + jsonObject1.getString("Total Amount"));
                            txtpaidamt.setText("" + jsonObject1.getString("Paid Amount"));
                            txtbalanceamt.setText(jsonObject1.getString("Balance Amount"));


                        } else {

                        }
                    }
                } else {
                    //showMessage(jsonObject.getString("Message"));
                }

            } else {
                Toast.makeText(context, "unable to get userwise coupon details.", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

        }

    }
}

