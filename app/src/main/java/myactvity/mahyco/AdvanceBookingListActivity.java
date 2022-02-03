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
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.DemoModelListAdapter;
import myactvity.mahyco.helper.DemoModelPlotListModel;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class AdvanceBookingListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SearchableSpinner spCropType, spProductName, spMobNumber;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etTaluka;
    TextView clearSearch;
    FloatingActionButton faButton;
    Calendar dateSelected = Calendar.getInstance();
    private Context context;
    String action = "1";
    String usercode;
    // TextView txt_noRecords;
    Config config;
    RecyclerView recDemoList;
    ImageView imgBtnSync;
    TextView txtNotSynced, noDataText;
    TextView lblheader;
    private String mno, croptype;

    SharedPreferences locdata, pref;
    ProgressDialog dialog, progressDailog;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    SharedPreferences.Editor loceditor, editor;
    ArrayList<DemoModelPlotListModel> mList = new ArrayList<>();
    String selectedCrop;
    String selectedProduct;
    private long mLastClickTime = 0;
    String selectedMobNumber;
    String mNumberFilter;
    int check = 0;

    DemoModelListAdapter adapterMDO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advance_booking_list);
        //   getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);


        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        spCropType = (SearchableSpinner) findViewById(R.id.spCropType);
        spProductName = (SearchableSpinner) findViewById(R.id.spProductName);
        spMobNumber = (SearchableSpinner) findViewById(R.id.spMobNumber);
        recDemoList = (RecyclerView) findViewById(R.id.recDemoList);

        LinearLayoutManager lllm = new LinearLayoutManager(this);
        lllm.setOrientation(LinearLayoutManager.VERTICAL);
        recDemoList.setLayoutManager(lllm);
        dialog = new ProgressDialog(this);
        faButton = (FloatingActionButton) findViewById(R.id.faButton);
        txtNotSynced = (TextView) findViewById(R.id.txtNotSynced);
        lblheader = (TextView) findViewById(R.id.lblheader);
        noDataText = (TextView) findViewById(R.id.noDataText);
        clearSearch = (TextView) findViewById(R.id.clearSearch);
        etTaluka = (EditText) findViewById(R.id.etTaluka);
        bindcroptype(spCropType, "C");
        bindMobilenumber();
        progressDailog = new ProgressDialog(this);
        progressDailog.setIndeterminate(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDailog.setMessage("Data Uploading");
        spMobNumber.setOnItemSelectedListener(this);
        spCropType.setOnItemSelectedListener(this);
        spProductName.setOnItemSelectedListener(this);
        imgBtnSync = (ImageView) findViewById(R.id.imgBtnSync);
        //  txt_noRecords = (TextView) findViewById(R.id.txt_noRecords);


        usercode = pref.getString("UserID", null);
        msclass = new Messageclass(this);


        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");


                    selectedCrop = spCropType.getSelectedItem().toString();
                    bindProductName(spProductName, selectedCrop);
                    if (!selectedCrop.equals(getString(R.string.selectedCrop)) &&
                            !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
//                        !selectedProduct.equals(getString(R.string.selectedProduct))&&
                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' ");
//                        "' AND product='" + selectedProduct+
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "AND mobileNumber='" + mNumberFilter + "' ");

                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
//                        !selectedProduct.equals(getString(R.string.selectedProduct))&&
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
//                        AND product='" + selectedProduct +
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");


                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' ");
//


                    } else {

                        mList.clear();
                        createList("");

                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Toast.makeText(DemoModelListActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GeneralMaster gm = (GeneralMaster) adapterView.getSelectedItem();
                try {
                    selectedProduct = URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    selectedProduct = spProductName.getSelectedItem().toString();
                    if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedProduct.equals(getString(R.string.selectedProduct))) {
                        mList.clear();

                        createList(" where crop='" + selectedCrop + "' AND product='" + selectedProduct + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' ");

                    } else {
                        mList.clear();
                        createList("");

                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spMobNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    mno = URLEncoder.encode(gm.Code().trim(), "UTF-8");

                    selectedMobNumber = spMobNumber.getSelectedItem().toString();


                    Pattern pattern = Pattern.compile("- *");
                    Matcher matcher = pattern.matcher(selectedMobNumber);
                    if (matcher.find()) {
                        mNumberFilter = selectedMobNumber.substring(0, matcher.start());

                    }

//                    !selectedProduct.equals(getString(R.string.selectedProduct))&&
                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber)) &&
                            !selectedCrop.equals(getString(R.string.selectedCrop))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' ");
//                        "' AND product='" + selectedProduct+
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' AND mobileNumber='" + mNumberFilter + "'");
//                        !selectedProduct.equals(getString(R.string.selectedProduct))&&
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
//                        AND product='" + selectedProduct +
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
                    } else {

                        mList.clear();
                        createList("");

                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        etTaluka.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("ONtext changed " + s.toString());


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //    Toast.makeText(context, "afterTextChanged", Toast.LENGTH_SHORT).show();

                if (s.length() >= 4) {
                    mList.clear();
                    createList(" where taluka" + " LIKE  '%" + etTaluka.getText().toString().toLowerCase().trim() + "%'" + " ");


                } else if (s.length() == 0) {

                    createList("");

                }
                System.out.println("afterTextChanged " + s.toString());


            }
        });
//


        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rotateFabForward();
                Intent intent = new Intent(AdvanceBookingListActivity.this, DemoModelRecordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        int count = mDatabase.getRowCount("DemoModelData");
        txtNotSynced.setText(String.valueOf(count));


        imgBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                    return;

                }
                mLastClickTime = SystemClock.elapsedRealtime();


                uploadDataModelRecords("mdo_demoModelVisitDetail");

            }

        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spCropType.setSelection(0);
                spProductName.setSelection(0);
                spMobNumber.setSelection(0);
                etTaluka.setText("");
                createList("");

            }
        });


    }

    private void bindcroptype(Spinner spCropType, String Croptype) {
        try {
            //st
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster(getString(R.string.selectedCrop),
                    getString(R.string.selectedCrop)));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }


        //en

    }


    public void bindMobilenumber() {

        try {
            spMobNumber.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> mobilelist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct mobileNumber,farmerName  FROM DemoModelData order by mobileNumber asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


                mobilelist.add(new GeneralMaster(getString(R.string.selectedMobNumber),
                        getString(R.string.selectedMobNumber)));


                if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            mobilelist.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(0).toUpperCase() + "-" + cursor.getString(1).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, mobilelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMobNumber.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void bindProductName(Spinner spProductName, String croptype) {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "SELECT distinct ProductName, CropName  FROM CropMaster where CropName='" + croptype + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT PRODUCT",
                    "SELECT PRODUCT"));
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProductName.setAdapter(adapter);
            dialog.dismiss();

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }


    public void rotateFabForward() {
        ViewCompat.animate(faButton)
                .rotation(135.0F)
                .withLayer()
                .setDuration(10L)
                .setInterpolator(new OvershootInterpolator(10.0F))
                .start();
    }


    public void rotateFabBackward() {
        ViewCompat.animate(faButton)
                .rotation(0.0F)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(10.0F))
                .start();
    }


    // get Review Table count
    public String getCountReviewModelList(String uid) {

        int count = mDatabase.getRowCountReviewModelList("DemoReviewData", uid);
        return String.valueOf(count);

    }


    public String getLastVisitDate(String uid) {
        String visitingdate = "";


        visitingdate= mDatabase.fetchLastDate(uid);

        return visitingdate;

    }


    //Get Data from db and make list for adapter
    public void createList(String whereCondition) {

        String searchQuery = "SELECT *  FROM DemoModelData " + whereCondition + "";


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        Log.d("searchQuery", "createList: " + searchQuery);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {

                    Log.d("Table data", jsonArray.toString());
                    DemoModelPlotListModel demoModelPlotListModel = new DemoModelPlotListModel();


                    demoModelPlotListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
                    demoModelPlotListModel.setSno(jsonArray.getJSONObject(i).getString("_id"));
                    demoModelPlotListModel.setArea(jsonArray.getJSONObject(i).getString("area"));
                    demoModelPlotListModel.setCoordinates(jsonArray.getJSONObject(i).getString("coordinates"));
                    demoModelPlotListModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    demoModelPlotListModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                    demoModelPlotListModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    demoModelPlotListModel.setWhatsappNumber(jsonArray.getJSONObject(i).getString("whatsappNumber"));
                    demoModelPlotListModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    demoModelPlotListModel.setPlotType(jsonArray.getJSONObject(i).getString("plotType"));
                    demoModelPlotListModel.setProduct(jsonArray.getJSONObject(i).getString("product"));
                    demoModelPlotListModel.setSeedQuantity(jsonArray.getJSONObject(i).getString("seedQuantity"));
                    demoModelPlotListModel.setSowingDate(jsonArray.getJSONObject(i).getString("sowingDate"));
                    demoModelPlotListModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    demoModelPlotListModel.setuId(jsonArray.getJSONObject(i).getString("uId"));

                    demoModelPlotListModel.setReviewCount(getCountReviewModelList(jsonArray.getJSONObject(i).getString("uId")));

                    demoModelPlotListModel.setLastVisit(getLastVisitDate(jsonArray.getJSONObject(i).getString("uId")));

                    if (jsonArray.getJSONObject(i).getString("imgPath").equals("")) {
                        demoModelPlotListModel.setImgPath("");
                    } else {
                        demoModelPlotListModel.setImgPath(jsonArray.getJSONObject(i).getString("imgPath"));
                    }
                    demoModelPlotListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    demoModelPlotListModel.setState(jsonArray.getJSONObject(i).getString("state"));
                    demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));


                    mList.add(demoModelPlotListModel);


                }
                recDemoList.setVisibility(View.VISIBLE);
                noDataText.setVisibility(View.GONE);

                addData(mList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            recDemoList.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progressDailog != null) {
            progressDailog.dismiss();
            progressDailog = null;
        }

    }

    public void addData(ArrayList<DemoModelPlotListModel> mList) {

        try {
//            final Context context = recDemoList.getContext();
            //   if (adapterMDO == null) {


            recDemoList.setLayoutManager(new LinearLayoutManager(this));
            recDemoList.setAdapter(new DemoModelListAdapter(this, mList));


            // } else {adapterMDO.notifyDataSetChanged(); }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        createList("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        // createList("");

    }

    public void uploadDataModelRecords(String UploadBatchCodeData) {
        String str = null;
        String searchQuery = "select  *  from DemoModelData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        if (config.NetworkConnection()) {
            progressDailog.show();
            if (count > 0) {
                try {
                    str = new UploadDataServer(UploadBatchCodeData).execute(SERVER).get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {

                progressDailog.dismiss();
                msclass.showMessage("Data not available for Sync");

            }

        } else {
            progressDailog.dismiss();
            msclass.showMessage("Internet network not available.");
        }
    }


    public String uploadDataModel(String UploadBatchCodeData) {
        String str = "";
        // int action = 1; // Old
        int action = 3; // New added Entry date

        String searchQuery = "select  *  from DemoModelData where  isSynced ='0'";

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
                        Log.d("ObjectasBytes", jsonObject.toString());
                        Log.d("ObjectasBytes", objAsBytes.toString());


                        str = syncSingleImage(UploadBatchCodeData, SERVER, action, objAsBytes, uId, imageName, imagePath);

                        handleImageSyncResponse(UploadBatchCodeData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());


            }

        }


        return str;
    }


    //AsyncTask Class for api batch code upload call

    @SuppressLint("StaticFieldLeak")
    public class UploadDataServer extends AsyncTask<String, String, String> {


        public UploadDataServer(String Funname) {


        }

        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadDataModel("mdo_demoModelVisitDetail");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();


                try {


                } catch (Exception e) {
                    e.printStackTrace();

                }
                if (resultout.contains("True")) {

//                    if (progressDailog.isShowing() && progressDailog != null) {

//
                    progressDailog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceBookingListActivity.this);

                    builder.setTitle("MyActivity");
                    builder.setMessage("Data Synced Successfully");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });


                    AlertDialog alert = builder.create();
                    alert.show();

//                    }


                    int count = mDatabase.getRowCount("DemoModelData");
                    txtNotSynced.setText(String.valueOf(count));
                    mList.clear();
                    createList("");


                } else {
                    msclass.showMessage(resultout + "--E");

                }
                Log.d("Response", resultout);


            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }


    public synchronized String syncSingleImage(String function, String urls, int action, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action;
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

    public void handleImageSyncResponse(String function, String resultout, String id) {
        if (function.equals("mdo_demoModelVisitDetail")) {
            if (resultout.contains("True")) {
                mDatabase.updateDemoModelData(id, "1", "1");
                //msclass.showMessage("Data Successfully Uploaded");


            } else {
                msclass.showMessage(resultout + "mdo_demoModelVisitDetail--E");

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }


}
