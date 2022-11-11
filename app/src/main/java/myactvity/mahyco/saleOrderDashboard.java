package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class saleOrderDashboard extends AppCompatActivity {
    private Context context;
    Button btncreatesaleorder,btnapprovalsaleprder,btnsalesorder,btnsalesreturnorderApp;
    Config config;
    private SqliteDatabase mDatabase;
    public CommonExecution cx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Intent intent;
    public Messageclass msclass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_order_dashboard);
        getSupportActionBar().hide(); //<< this
        context = this;

        config = new Config(this); //Here the context is passing
        mDatabase = SqliteDatabase.getInstance(this);
        cx=new CommonExecution(this);
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        msclass = new Messageclass(this);
        logSalesOrderData();
        btnsalesreturnorderApp=(Button)findViewById(R.id.btnsalesreturnorderApp);
        btncreatesaleorder=(Button)findViewById(R.id.btncreatesaleorder);
        btnapprovalsaleprder=(Button)findViewById(R.id.btnapprovalsaleprder);
        btnsalesorder=(Button)findViewById(R.id.btnsalesorder);
        if(pref.getString("unit", null).contains("VCBU"))
        {
            btnsalesreturnorderApp.setVisibility(View.VISIBLE);
            btnsalesorder.setVisibility(View.VISIBLE);
            //btnsalesreturnorderApp.setVisibility(View.GONE);
            //btnsalesorder.setVisibility(View.GONE);


        }
        else
        {
            btnsalesreturnorderApp.setVisibility(View.GONE);
            btnsalesorder.setVisibility(View.GONE);
        }


        btncreatesaleorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (pref.getString("RoleID",null).equals("4")) {
                        intent = new Intent(context.getApplicationContext(), orderfromTBM.class);
                        //intent= new Intent(context.getApplicationContext(),TestImage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("Pagetype", "PlotVisit");
                        context.startActivity(intent);
                    }
                    else
                    {   // RBM
                        if (pref.getString("RoleID",null).equals("2")) {
                            intent = new Intent(context.getApplicationContext(), orderfromRBM.class);
                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //intent.putExtra("Pagetype", "PlotVisit");
                            context.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(context, "Your are not authorized to access this tab.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                catch(Exception ex)
                {
                    // msclass.showMessage(ex.getMessage());
                }



            }
        });


        btnsalesreturnorderApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    // 2 RBM  5 ZBM 7ZMM
                    if (pref.getString("RoleID",null).equals("2")||
                        pref.getString("RoleID",null).equals("5")||
                        pref.getString("RoleID",null).equals("7")) {
                        intent = new Intent(context.getApplicationContext(), saleOrderReturnApproved.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Your are not authorized to access this tab.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    // msclass.showMessage(ex.getMessage());
                }



            }
        });
        btnapprovalsaleprder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    // 2 RBM  5 ZBM 7 -ZMM
                    if (pref.getString("RoleID",null).equals("2")||
                            pref.getString("RoleID",null).equals("5")||
                            pref.getString("RoleID",null).equals("7")) {
                        intent = new Intent(context.getApplicationContext(),saleorderpending.class);
                        //intent= new Intent(context.getApplicationContext(),TestImage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("Pagetype", "PlotVisit");
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Your are not authorized to access this tab.", Toast.LENGTH_SHORT).show();
                    }


                }
                catch(Exception ex)
                {
                    // msclass.showMessage(ex.getMessage());
                }



            }
        });
        btnsalesorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                        Intent intent = new Intent(context.getApplicationContext(), saleOrderReturn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                }
                catch(Exception ex)
                {
                    msclass.showMessage(ex.getMessage());
                }



            }
        });
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


                }
                Intent intent=null;
                if (classname.toString().contains("DistributorAsRetailer3")) {
                    intent = new Intent(context.getApplicationContext(), DistributorAsRetailerPOG.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                if (classname.toString().contains("Retailer1")) {
                    intent = new Intent(context.getApplicationContext(), RetailerPOG.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                if (classname.toString().contains("Distributor2")) {
                    intent = new Intent(context.getApplicationContext(), DistributorPOG.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
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

    private void logSalesOrderData(){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callSaleOrderEvent(userId,displayName);
            }
        }
    }
}
