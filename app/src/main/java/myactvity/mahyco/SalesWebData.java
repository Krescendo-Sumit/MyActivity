package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;

public class SalesWebData extends AppCompatActivity {

   // String SERVER = "http://ipmf.orgtix.com/webapi/api/Sales/GetData";
    String SERVER = "https://compensation.mahyco.com/api/Sales/GetRecActualData";
    String fechdataserver  = "https://compensation.mahyco.com/api/Sales/GetSalesData";
    String GetABSActualData="https://compensation.mahyco.com/api/Sales/GetABSActualData";
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ScrollView container;
    private Handler handler = new Handler();
    Config config;
    public Messageclass msclass;

    SharedPreferences pref, locdata;
    SharedPreferences.Editor editor, locedit;

    SearchableSpinner spSeason, spCountry, spCompanyName, spBUDivision, spDistributor, spRetailer, spTerritory,spRegion, spZone,
    spCropName, spProductCode, spCropCategoryCode, spRBM, spMDO, spTBM;
    Button btnGetSaleData,btnFetchSalesData;
   LinearLayout llSpinner;
   String usercode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_web_data);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        usercode=pref.getString("UserID", null);
        initUI();
        btnGetSaleData=(Button) findViewById(R.id.btnGetSaleData);
        btnFetchSalesData=(Button) findViewById(R.id.btnFetchSalesData);

        btnGetSaleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdrowdownlist();

            }
        });
        btnFetchSalesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechsaledatalist();

            }
        });
    }

    /**
     * <P>Method used to initalize the elements</P>
     */
    private void initUI() {

        config = new Config(this); //Here the context is passing
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container = (ScrollView) findViewById(R.id.container);
        msclass = new Messageclass(this);
        spSeason = findViewById(R.id.spSeason);
        spCountry = findViewById(R.id.spCountry);
        spCompanyName = findViewById(R.id.spCompanyName);
        spBUDivision = findViewById(R.id.spBUDivision);
        spDistributor = findViewById(R.id.spDistributor);
        spRetailer = findViewById(R.id.spRetailer);
        spTerritory = findViewById(R.id.spTerritory);
        spRegion = findViewById(R.id.spRegion);
        spZone = findViewById(R.id.spZone);
        spCropName = findViewById(R.id.spCropName);
        spProductCode = findViewById(R.id.spProductCode);
        spCropCategoryCode = findViewById(R.id.spCropCategoryCode);
        spRBM = findViewById(R.id.spRBM);
        spMDO = findViewById(R.id.spMDO);
        spTBM =  findViewById(R.id.spTBM);
        llSpinner =  findViewById(R.id.llSpinner);
      //  btnGetSaleData= findViewById(R.id.btnGetSaleData);
       // btnFetchSalesData=findViewById(R.id.btnFetchSalesData);



       doWork();


    }
    private void fechsaledatalist() {
        relPRogress.setVisibility(View.VISIBLE);
        relPRogress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        fechsaledata();
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
    private void getdrowdownlist() {
        relPRogress.setVisibility(View.VISIBLE);
        relPRogress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        Getdrowlist();
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
    private void Getdrowlist() {

        JSONObject requestParams = new JSONObject();

        JSONObject loginDetails = new JSONObject();

        JSONObject reportData = new JSONObject();





        try {



            loginDetails.put("UserID", usercode);
            reportData.put("Action", "1");
           // reportData.put("UserId", "97190469");
            reportData.put("Country", "'IN'");
            reportData.put("CompanyCode", "1000");
            reportData.put("BU_Division", "");
            reportData.put("Season", "");
            reportData.put("Zone", "");
            reportData.put("Region", "");
            reportData.put("Territory", "");
            reportData.put("RBMId", "");
            reportData.put("TBMId", "");
            reportData.put("Customer", "");
            reportData.put("PlaceOfWorkCategory", "");
            reportData.put("CropCategory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("ReportView", "1");
            reportData.put("BUType", "RB");
            reportData.put("UserID", usercode);
            requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);

           /* loginDetails.put("LoginEmpID", "30000110");
            loginDetails.put("LoginEmpCompanyCodeNo", "1000");


            reportData.put("Action", "100");
            reportData.put("UserId", "");
            reportData.put("Year", "");
            reportData.put("Season", "");
            reportData.put("Date", "");
            reportData.put("Country", "");
            reportData.put("CompanyCode", "");
            reportData.put("BU_Division", "");
            reportData.put("Zone", "");
            reportData.put("region", "");
            reportData.put("Territory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("CropCategory", "");
            reportData.put("TBM ID", "");
            reportData.put("RBM ID", "");
            reportData.put("MDO ID", "");

            requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);
            */

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("SalesData", requestParams.toString());

        new SalesDataApiCall("SalesData", requestParams,fechdataserver).execute();

    }
    private void fechsaledata() {

        JSONObject requestParams = new JSONObject();

        JSONObject loginDetails = new JSONObject();

        JSONObject reportData = new JSONObject();





        try {

            loginDetails.put("UserID", usercode);
            reportData.put("Action", "2");
            //reportData.put("UserId", "97190469");
            reportData.put("Country", "");
            reportData.put("CompanyCode", "");
            reportData.put("BU_Division", "");
            reportData.put("Season", "");
            reportData.put("Zone", "");
            reportData.put("Region", "");
            reportData.put("Territory", "");
            reportData.put("RBMId", "");
            reportData.put("TBMId", "");
            reportData.put("Customer", "");
            reportData.put("PlaceOfWorkCategory", "");
            reportData.put("CropCategory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("ReportView", "1");
            reportData.put("BUType", "RB");
            reportData.put("UserID", usercode);
            requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);

           /* loginDetails.put("LoginEmpID", "30000110");
            loginDetails.put("LoginEmpCompanyCodeNo", "1000");


            reportData.put("Action", "100");
            reportData.put("UserId", "");
            reportData.put("Year", "");
            reportData.put("Season", "");
            reportData.put("Date", "");
            reportData.put("Country", "");
            reportData.put("CompanyCode", "");
            reportData.put("BU_Division", "");
            reportData.put("Zone", "");
            reportData.put("region", "");
            reportData.put("Territory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("CropCategory", "");
            reportData.put("TBM ID", "");
            reportData.put("RBM ID", "");
            reportData.put("MDO ID", "");

            requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);
           */

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("SalesData", requestParams.toString());

        new SalesDataApiCall("SalesData", requestParams,fechdataserver).execute();

    }
    private void doWork() {
        relPRogress.setVisibility(View.VISIBLE);
        relPRogress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        getDataFromServer();
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

    private void getDataFromServer() {

        JSONObject requestParams = new JSONObject();

        JSONObject loginDetails = new JSONObject();

        JSONObject reportData = new JSONObject();





        try {

            loginDetails.put("UserID", usercode);
            reportData.put("Action", "1");
            // reportData.put("UserId", "97190469");
            reportData.put("Country", "'IN'");
            reportData.put("CompanyCode", "1000");
            reportData.put("BU_Division", "");
            reportData.put("Season", "");
            reportData.put("Zone", "");
            reportData.put("Region", "");
            reportData.put("Territory", "");
            reportData.put("RBMId", "");
            reportData.put("TBMId", usercode);
            reportData.put("Customer", "");
            reportData.put("PlaceOfWorkCategory", "");
            reportData.put("CropCategory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("ReportView", "1");
            reportData.put("BUType", "RB");
            reportData.put("UserID", usercode);
            requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);

           /* loginDetails.put("LoginEmpID", "30000110");
            loginDetails.put("LoginEmpCompanyCodeNo", "1000");


            reportData.put("Action", "100");
            reportData.put("UserId", "");
            reportData.put("Year", "");
            reportData.put("Season", "");
            reportData.put("Date", "");
            reportData.put("Country", "");
            reportData.put("CompanyCode", "");
            reportData.put("BU_Division", "");
            reportData.put("Zone", "");
            reportData.put("region", "");
            reportData.put("Territory", "");
            reportData.put("Crop", "");
            reportData.put("Product", "");
            reportData.put("CropCategory", "");
            reportData.put("TBM ID", "");
            reportData.put("RBM ID", "");
            reportData.put("MDO ID", "");
         requestParams.put("loginDetails", loginDetails);
            requestParams.put("reportData", reportData);
          */

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("SalesData", requestParams.toString());

        new SalesDataApiCall("SalesData", requestParams,fechdataserver).execute();

    }

    /**
     * <P> AsyncTask Class for api call to upload distributor data</P>
     */
    private class SalesDataApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;

        JSONObject obj;
        String APIurl;

        public SalesDataApiCall(String function, JSONObject obj,String APIurl) {

            this.function = function;
            this.obj = obj;
            this.APIurl =APIurl;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {


            return uploadSalesData(function,obj,APIurl);
        }
        protected void onPostExecute(String result) {
            try {
               // String resultout = result.trim();

                msclass.showMessage(result);
                Log.d("Response", result);
                final JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("SalesData")) {

                    llSpinner.setVisibility(View.VISIBLE);
                    bindData(jsonObject);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SalesWebData.this);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Fetched Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                               progressBarVisibility();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressBarVisibility();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesWebData.this);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                          progressBarVisibility();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesWebData.this);
                builder.setTitle("Info");
                builder.setMessage("Something went wrong please try again later.");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        progressBarVisibility();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }

    }

    private String uploadSalesData(String function, JSONObject obj,String SERVER) {

        return HttpUtils.POSTJSON(SERVER, obj,"");

    }

    /**
     * <P>Manage the progressbar visibility</P>
     */
    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        container.setClickable(true);
        container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void bindData(JSONObject Object) throws JSONException {


        JSONObject jsonObject = (Object.getJSONArray("SalesData")).getJSONObject(0);


        season(jsonObject);
        country(jsonObject);
        companyName(jsonObject);
        bUDivision(jsonObject);
        distributor(jsonObject);
       // retailer(jsonObject);
        territory(jsonObject);
        region(jsonObject);
        zone(jsonObject);
        cropName(jsonObject);
        productCode(jsonObject);
        cropCategoryCode(jsonObject);
        tbm(jsonObject);
        rbm(jsonObject);
       // mdo(jsonObject);


    }

    private void mdo(JSONObject jsonObject) {

        try {
            spMDO.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT MDO");

                jsonArray = jsonObject.getJSONArray("Table14");

                for (int i = 0; i < jsonArray.length(); i++) {

                    list.add(jsonArray.getJSONObject(i).getString("MDO Name") + " - " + jsonArray.getJSONObject(i).getString("MDO ID"));
                }  bindToAdpater(spMDO,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void rbm(JSONObject jsonObject) {
        try {
            spRBM.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT RBM");

                jsonArray = jsonObject.getJSONArray("Table11");

                for (int i = 0; i < jsonArray.length(); i++) {

                    list.add(jsonArray.getJSONObject(i).getString("Text") + " - " + jsonArray.getJSONObject(i).getString("Value"));
                }  bindToAdpater(spRBM,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void tbm(JSONObject jsonObject) {
        try {
            spTBM.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT TBM");

                jsonArray = jsonObject.getJSONArray("Table13");

                for (int i = 0; i < jsonArray.length(); i++) {

                    list.add(jsonArray.getJSONObject(i).getString("Text") + " - " + jsonArray.getJSONObject(i).getString("Value"));
                }  bindToAdpater(spTBM,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void cropCategoryCode(JSONObject jsonObject) {
        try {
            spCropCategoryCode.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT CROP CATEGORY CODE");

                jsonArray = jsonObject.getJSONArray("Table8");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }  bindToAdpater(spCropCategoryCode,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void productCode(JSONObject jsonObject) {
        try {
            spProductCode.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT PRODUCT CODE");

                jsonArray = jsonObject.getJSONArray("Table6");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Value")+"-"+jsonArray.getJSONObject(i).getString("Text"));
                }  bindToAdpater(spProductCode,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void cropName(JSONObject jsonObject) {
        try {
            spCropName.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT CROP NAME");

                jsonArray = jsonObject.getJSONArray("Table5");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }  bindToAdpater(spCropName,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void zone(JSONObject object) {
        try {
            spZone.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT ZONE");

                jsonArray = object.getJSONArray("Table9");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }
                bindToAdpater(spZone,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void region(JSONObject object) {
        try {
            spRegion.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT REGION");

                jsonArray = object.getJSONArray("Table7");

                for(int i=0; i<jsonArray.length();i++){
                    list.add(jsonArray.getJSONObject(i).getString("Text") + " - " + jsonArray.getJSONObject(i).getString("Value") );
                }

                bindToAdpater(spRegion,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void territory(JSONObject object) {
        try {
            spTerritory.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT TERRITORY");


                jsonArray = object.getJSONArray("Table6");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }
                bindToAdpater(spTerritory,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void retailer(JSONObject object) {
        try {
            spRetailer.setAdapter(null);
            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT RETAILER");

                jsonArray = object.getJSONArray("Table5");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Retailer") + " - " + jsonArray.getJSONObject(i).getString("RetailerCode") );
                }

                bindToAdpater(spRetailer,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void distributor(JSONObject object) {
        try {
            spDistributor.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();


                list.add("SELECT DISTRIBUTOR");

                jsonArray = object.getJSONArray("Table4");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text") + " - " + jsonArray.getJSONObject(i).getString("Value") );
                }
                bindToAdpater(spDistributor,list);


            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void bUDivision(JSONObject object) {

        try {
            spBUDivision.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT BU DIVISION");

                jsonArray = object.getJSONArray("Table3");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }
                bindToAdpater(spBUDivision,list);


            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void companyName(JSONObject object) {

        try {
            spCompanyName.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT COMPANY NAME");

                jsonArray = object.getJSONArray("Table1");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text") + " - " + jsonArray.getJSONObject(i).getString("Value") );
                }
                bindToAdpater(spCompanyName,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void country(JSONObject object) {
        try {
            spCountry.setAdapter(null);

            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT COUNTRY");

                jsonArray = object.getJSONArray("Table");

                for(int i=0; i<jsonArray.length();i++){

                    list.add(jsonArray.getJSONObject(i).getString("Text"));
                }

                bindToAdpater(spCountry,list);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }

    }

    private void season(JSONObject object) {
      try {
            spSeason.setAdapter(null);
            try {
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray();

                list.add("SELECT SEASON");
                 jsonArray = object.getJSONArray("Table2");

              for(int i=0; i<jsonArray.length();i++){

                  list.add(jsonArray.getJSONObject(i).getString("Season"));
              }
              bindToAdpater(spSeason,list);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    /**
     * <P>Method is used to bind the list data into spinner via adapter</P>
     * @param spinner
     * @param list
     */
    private void bindToAdpater(SearchableSpinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
