package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-08-05.
 */
public class DemoModelVisit extends AppCompatActivity {

    Button btnAdd, btnUpdate, btnDownloadVal, btnFieldVal, btnFieldValUpload;
    Config config;
    public CommonExecution cx;
    TextView lblheader;
    TextView myTextProgress;
    private SqliteDatabase mDatabase;
    SharedPreferences sp;
    public Messageclass msclass;
    String MDOurlpath = "";
    String returnstring = "";
    public ProgressDialog dialog;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    RelativeLayout footer;
    String userRole;
    private Handler handler = new Handler();


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_demo_model_plot_visit);
        getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        MDOurlpath = cx.MDOurlpath;
        sp = getApplicationContext().getSharedPreferences("MyPref", 0);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Downloading ");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        footer = (RelativeLayout) findViewById(R.id.footer);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        lblheader = (TextView) findViewById(R.id.lblheader);
        myTextProgress = (TextView) findViewById(R.id.myTextProgress);
        btnDownloadVal = (Button) findViewById(R.id.btnDownloadVal);
        btnFieldVal = (Button) findViewById(R.id.btnFieldVal);
        btnFieldValUpload = (Button) findViewById(R.id.btnFieldValUpload);


        Cursor data = mDatabase.fetchusercode();

        if (data.getCount() == 0) {
            userRole = "0";


        } else {
            data.moveToFirst();
            if (data != null) {
                do {
                    userRole = data.getString((data.getColumnIndex("RoleID")));


                } while (data.moveToNext());
                Log.d("Role", "RoleMainMenu" + userRole);

            }
            data.close();


        }

       userRole = "4";
        if (userRole.contains("4")) {

            btnDownloadVal.setVisibility(View.VISIBLE);
            btnFieldVal.setVisibility(View.VISIBLE);
            btnFieldValUpload.setVisibility(View.VISIBLE);

        } else {

            btnDownloadVal.setVisibility(View.GONE);
            btnFieldVal.setVisibility(View.GONE);
            btnFieldValUpload.setVisibility(View.GONE);

        }

        lblheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DemoModelVisit.this, AndroidDatabaseManager.class);
                startActivity(i);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoModelVisit.this, DemoModelRecordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
          }
        });
      btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(DemoModelVisit.this, DemoModelListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnDownloadVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isDataPending() > 0) {
                    msclass.showMessage("Please Upload the Pending records first");
                } else {

                    downloadValData();
                }
            }
        });
        btnFieldVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoModelVisit.this, ValidateDemoListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        btnFieldValUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relPRogress.setVisibility(View.VISIBLE);
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });
                dowork();

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                footer.setEnabled(false);
                footer.setClickable(false);


            }
        });


        //Check 2019 entry and delere
        try {
            int cnt = mDatabase.getrowcount("select * from DemoModelData where sowingDate like '%2019%'");
            if (cnt > 0) {
                mDatabase.deleterecord("delete from DemoModelData where sowingDate like '%2019%'");
                mDatabase.deleterecord("delete from DemoReviewData where visitingDate like '%2019%'");
                mDatabase.deleterecord("delete from ValidatedDemoModelData where sowingDate like '%2019%'");
                mDatabase.deleterecord("delete from ValidatedDemoReviewData where visitingDate like '%2019%'");
            }
        }
        catch (Exception ex)
        {

        }

    }


    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        uploadUpdatedDataModelRecords("mdo_demoModelVisitDetail_validationNew");

                    }
                });
                try {

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isDataPending();


    }


    public int isDataPending() {


        int count = mDatabase.getRowCountVAlidatedReviewNotUploaded("ValidatedDemoReviewData");
        btnFieldValUpload.setText("Upload Field Validation (" + String.valueOf(count) + ")");


        if (count > 0) {

            btnFieldValUpload.setVisibility(View.VISIBLE);

        } else {
            btnFieldValUpload.setVisibility(View.GONE);


        }

        return count;

    }


    public void downloadValData() {
        try {


            if (config.NetworkConnection()) {
                String str = null;
                String uid = sp.getString("UserID", null);

                try {
                    new MDODemoDataForValidationDownload(uid, DemoModelVisit.this).execute(MDOurlpath);

                } catch (Exception ex) {
                    msclass.showMessage(ex.getMessage());
                }
            } else {
                msclass.showMessage("Internet network not available.");
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }
    }

    private class MDODemoDataForValidationDownload extends AsyncTask<String, String, String> {

        private String username;
        private String allData;
        private Context ctx;

        public MDODemoDataForValidationDownload(String username, Context ctx) {
            this.username = username;
            this.allData = allData;
            this.ctx = ctx;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDO_DownloadDemoDataForValidation"));
            String Urlpath1 = MDOurlpath + "?Type=" + "MDO_DownloadDemoDataForValidation&userCode=" + username + "";
            HttpPost httppost = new HttpPost(Urlpath1);
            Log.i("URL_Plot Mgnt",Urlpath1);
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

                mDatabase.deleledata("ValidatedDemoModelData", " ");
                mDatabase.deleledata("ValidatedDemoReviewData", " ");
                mDatabase.deleledata("MDOListData", " ");


                JSONObject object = new JSONObject(result);
                Log.d("Validation data", object.toString());
                JSONArray jArray = object.getJSONArray("Table");
                JSONArray jArray2 = object.getJSONArray("Table1");
                JSONArray jArray3 = object.getJSONArray("Table2");
                JSONArray jArray4 = object.getJSONArray("Table3");
                boolean f1 = false;


                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);


                    f1 = mDatabase.insertDemoValidationData(jObject.getString("uId").toString(),
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
                            , jObject.getString("imgPath"), "1", "1", "", "", "",
                            jObject.getString("entrydate"), "1");

                }

                for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject jObject2 = jArray2.getJSONObject(i);
                    f1 = mDatabase.insertReviewValidationData(jObject2.getString("uId"), jObject2.getString("uIdP"), jObject2.getString("userCode"), jObject2.getString("taluka"),
                            jObject2.getString("farmerName"), jObject2.getString("mobileNumber"), jObject2.getString("crop")
                            , jObject2.getString("product"), "", "", jObject2.getString("visitingDate"), jObject2.getString("coordinates"), jObject2.getString("purposeVisit")
                            , jObject2.getString("comment"),
                            jObject2.getString("imgName"), jObject2.getString("imgPath"),
                            "1", "1", "",
                            jObject2.getString("entrydate"),
                            "1","","","",
                            "","","","");
                }
                for (int i = 0; i < jArray3.length(); i++) {
                    JSONObject jObject3 = jArray3.getJSONObject(i);
                    f1 = mDatabase.insertMDOlistData(jObject3.getString("user_code").toString(), jObject3.getString("MDO_name").toString());
                }


                if (f1 == true) {
                    msclass.showMessage("Data download successfully");
                    dialog.dismiss();


                } else {
                    msclass.showMessage("Data Not available");
                    dialog.dismiss();
                }


            } catch (JSONException e1) {
                e1.printStackTrace();

            }


        }
    }


    public void uploadUpdatedDataModelRecords(String uploadReviewData) {
        String str = null;
        String searchQuery = "select  *  from ValidatedDemoReviewData where  isUploaded ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        if (config.NetworkConnection()) {


            if (count > 0) {

                try {
                    str = new UploadUpdatedDataServer(uploadReviewData).execute(cx.MDOurlpath).get();


                } catch (Exception e) {
                    e.printStackTrace();
                    msclass.showMessage("Error" + e);
                    relPRogress.setVisibility(View.GONE);
                    footer.setClickable(true);
                    footer.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
            } else {
                relPRogress.setVisibility(View.GONE);

                msclass.showMessage("Data not available to sync");
                footer.setClickable(true);
                footer.setEnabled(true);
            }

        } else {
            relPRogress.setVisibility(View.GONE);
            msclass.showMessage("Internet Network not available");
            footer.setClickable(true);
            footer.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public String uploadUpdatedDataModel(String uploadReviewData) {
        String str = "";

        //int action = 1; old
        int action = 3 ; //new  added  new coloum  Entry date

        String searchQuery = "select  *  from ValidatedDemoReviewData where  isUploaded ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("imgName");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        jsonArray = mDatabase.getResults(searchQuery);


                        str = syncUpdatedDemoVisitImage(uploadReviewData, cx.MDOurlpath, objAsBytes, uId, imageName, imagePath);

                        handleUpdatedDemoVisitImageResponse(uploadReviewData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }


        return str;
    }


    //AsyncTask Class for api batch code upload call

    public class UploadUpdatedDataServer extends AsyncTask<String, String, String> {


        public UploadUpdatedDataServer(String Funname) {

        }

        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadUpdatedDataModel("mdo_demoModelVisitDetail_validationNew");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();



                if (!resultout.equals("")) {

                    if (resultout.contains("True")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelVisit.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Uploaded Successfully");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                relPRogress.setVisibility(View.GONE);
                                footer.setClickable(true);
                                footer.setEnabled(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                isDataPending();



                            }
                        });


                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {
                        relPRogress.setVisibility(View.GONE);
                        msclass.showMessage(resultout + "mdo_demoModelVisitDetail_validationNew--E");
                        footer.setClickable(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        footer.setEnabled(true);

                    }
                    Log.d("Response", resultout);

                } else {
                    msclass.showMessage("Poor Internet: Please try after sometime.");
                    relPRogress.setVisibility(View.GONE);
                    footer.setClickable(true);
                    footer.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } catch (Exception e) {
                relPRogress.setVisibility(View.GONE);
                footer.setClickable(true);
                footer.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                e.printStackTrace();
            }

        }
    }


    public synchronized String syncUpdatedDemoVisitImage(String function, String urls, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName;
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
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
        return builder.toString();
    }

    public void handleUpdatedDemoVisitImageResponse(String function, String resultout, String id) {
        if (function.equals("mdo_demoModelVisitDetail_validationNew")) {
            if (resultout.contains("True")) {
                mDatabase.updateValidatedReviwData(id, "1", "1", "1");


            } else {

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }
}
