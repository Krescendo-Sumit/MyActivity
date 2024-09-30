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
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
public class DemoModelListActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    SearchableSpinner spCropType, spProductName, spMobNumber, spTaluka, spVillage;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    TextView clearSearch;
    FloatingActionButton faButton;
    Calendar dateSelected = Calendar.getInstance();
    private Context context;
    String action = "1";
    String usercode;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    RelativeLayout footer;
    private Handler handler = new Handler();

    // TextView txt_noRecords;
    Config config;
    RecyclerView recDemoList;
    ImageView imgBtnSync;
    TextView txtNotSynced, noDataText;
    TextView lblheader;
    private String mno, croptype;

    SharedPreferences locdata, pref;
    ProgressDialog dialog;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    SharedPreferences.Editor loceditor, editor;
    ArrayList<DemoModelPlotListModel> mList = new ArrayList<>();
    String selectedCrop;
    String selectedProduct, taluka, village;
    private long mLastClickTime = 0;
    String selectedMobNumber;
    String mNumberFilter;
    int check = 0;

    DemoModelListAdapter adapterMDO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo_model_list);
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
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        recDemoList = (RecyclerView) findViewById(R.id.recDemoList);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        footer = (RelativeLayout) findViewById(R.id.footer);

        LinearLayoutManager lllm = new LinearLayoutManager(this);
        lllm.setOrientation(LinearLayoutManager.VERTICAL);
        recDemoList.setLayoutManager(lllm);
        dialog = new ProgressDialog(this);
        faButton = (FloatingActionButton) findViewById(R.id.faButton);
        txtNotSynced = (TextView) findViewById(R.id.txtNotSynced);
        lblheader = (TextView) findViewById(R.id.lblheader);
        noDataText = (TextView) findViewById(R.id.noDataText);
        clearSearch = (TextView) findViewById(R.id.clearSearch);
        bindcroptype(spCropType, "C");
        bindMobilenumber();
        bindTaluka();

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
                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' ");
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber)) && selectedMobNumber != null) {
//                        !selectedProduct.equals(getString(R.string.selectedProduct))&&
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
//                        AND product='" + selectedProduct +
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' ");
                    } else {
                        mList.clear();
                        createList("");
                    }
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bindVillage(taluka);
                if (spTaluka != null && !taluka.equalsIgnoreCase("SELECT TALUKA")) {
                    mList.clear();
                    createList(" where taluka='" + taluka.toUpperCase().trim() + "' ");
                } else {
                    mList.clear();
                    createList(" where taluka='" + "" + "' ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    village = gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!taluka.equalsIgnoreCase("SELECT TALUKA") && !village.equalsIgnoreCase("SELECT VILLAGE")) {
                    mList.clear();
                    createList(" where taluka='" + taluka.toUpperCase().trim() + "' AND village='" + village.toUpperCase().trim() + "' ");
                } else if (!taluka.equalsIgnoreCase("SELECT TALUKA") && village.equalsIgnoreCase("SELECT VILLAGE")) {
                    mList.clear();
                    createList(" where taluka='" + taluka.toUpperCase().trim() + "' ");
                } else if (taluka.equalsIgnoreCase("SELECT TALUKA") && !village.equalsIgnoreCase("SELECT VILLAGE")) {
                    mList.clear();
                    createList(" where village='" + village.toUpperCase().trim() + "' ");
                } else {
                    mList.clear();
                    createList("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spMobNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    mno = URLEncoder.encode(gm.Code().trim(), "UTF-8");


                    selectedMobNumber = spMobNumber.getSelectedItem().toString();

                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {

                        Pattern pattern1 = Pattern.compile("- *");
                        Matcher matcher1 = pattern1.matcher(selectedMobNumber);
                        if (matcher1.find()) {
                            mNumberFilter = selectedMobNumber.substring(0, matcher1.start());

                        }
                    } else {

                        selectedMobNumber = mNumberFilter = getString(R.string.selectedMobNumber);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoModelListActivity.this, DemoModelRecordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        int count = mDatabase.getRowCount("DemoModelData");
        txtNotSynced.setText(String.valueOf(count));

        imgBtnSync.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                    return;

                }
                mLastClickTime = SystemClock.elapsedRealtime();
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
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spCropType.setSelection(0);
                spProductName.setSelection(0);
                spMobNumber.setSelection(0);
                spTaluka.setSelection(0);
                spVillage.setSelection(0);
                createList("");
            }
        });
    }

    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        uploadDataModelRecords("mdo_demoModelVisitDetail");

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
    }

    public void bindTaluka() {
        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            // dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

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
                spTaluka.setAdapter(adapter);
                // dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }


    }

    public void bindVillage(String taluka) {
        spVillage.setAdapter(null);

        String str = null;
        try {

            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";
            //cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT VILLAGE",
                    "SELECT VILLAGE"));

            cursor = mDatabase.getReadableDatabase().

                    rawQuery(searchQuery, null);
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
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();
        }
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

        new MyDemoLoading(context,whereCondition).execute();


/*

        String searchQuery = "SELECT *  FROM DemoModelData " + whereCondition + "";


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {

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


                    if (jsonArray.getJSONObject(i).getString("village").equals("")) {
                        String focussvillage="";
                        JSONArray jsonArray1 = new JSONArray();
                        try {
                            demoModelPlotListModel.setFocussedVillage(jsonArray.getJSONObject(i).getString("focussedVillage"));


                            focussvillage=jsonArray.getJSONObject(i).getString("focussedVillage");
                            String  talukaquery ="select * from FocussedVillageMaster  where upper(vil_desc)='"+focussvillage.toUpperCase().trim()+"'";
                            jsonArray1 = mDatabase.getResults(talukaquery);
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                demoModelPlotListModel.setTaluka(jsonArray1.getJSONObject(0).getString("taluka"));
                                break;
                            }
                        }
                            catch(Exception ex)
                            {

                            }
                        demoModelPlotListModel.setVillage("");

                    } else {
                        demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));

                        demoModelPlotListModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    }

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
                   // demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));

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
*/

    }

    public void createList1(String whereCondition) {

        String searchQuery = "SELECT *  FROM DemoModelData " + whereCondition;


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {

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


                    if (jsonArray.getJSONObject(i).getString("village").equals("")) {
                        String focussvillage="";
                        JSONArray jsonArray1 = new JSONArray();
                        try {
                            demoModelPlotListModel.setFocussedVillage(jsonArray.getJSONObject(i).getString("focussedVillage"));


                            focussvillage=jsonArray.getJSONObject(i).getString("focussedVillage");
                            String  talukaquery ="select * from FocussedVillageMaster  where upper(vil_desc)='"+focussvillage.toUpperCase().trim()+"'";
                            jsonArray1 = mDatabase.getResults(talukaquery);
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                demoModelPlotListModel.setTaluka(jsonArray1.getJSONObject(0).getString("taluka"));
                                break;
                            }
                        }
                        catch(Exception ex)
                        {

                        }
                        demoModelPlotListModel.setVillage("");

                    } else {
                        demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));

                        demoModelPlotListModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    }

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
                    // demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));

                    mList.add(demoModelPlotListModel);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
//            recDemoList.setVisibility(View.GONE);
//            noDataText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler = null;
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
       // createList("");
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
//            progressDailog.show();
            if (count > 0) {
                try {
                    str = new UploadDataServer(UploadBatchCodeData).execute(SERVER).get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {

//                progressDailog.dismiss();
                msclass.showMessage("Data not available for Sync");
                relPRogress.setVisibility(View.GONE);
                footer.setClickable(true);
                footer.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }

        } else {
//            progressDailog.dismiss();
            msclass.showMessage("Internet network not available.");
            relPRogress.setVisibility(View.GONE);
            footer.setClickable(true);
            footer.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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


                        str = syncSingleImage(UploadBatchCodeData, SERVER, action, objAsBytes, uId, imageName, imagePath);
                        if (!str.equals(""))
                            handleImageSyncResponse(UploadBatchCodeData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
//                msclass.showMessage(ex.getMessage());


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
                Log.d("rrrrr", "onPostExecute: 11" + result);

                if (!result.equals("")) {
                    String resultout = result.trim();


                    if (resultout.contains("True")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(DemoModelListActivity.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Synced Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {


                                dialog.dismiss();
                                relPRogress.setVisibility(View.GONE);
                                footer.setClickable(true);
                                footer.setEnabled(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
//                        progressDailog.dismiss();
                        msclass.showMessage(resultout + "--E");
                        relPRogress.setVisibility(View.GONE);
                        footer.setClickable(true);
                        footer.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }


                } else {

                    msclass.showMessage("Poor Internet: Please try after sometime.");
                    relPRogress.setVisibility(View.GONE);
                    footer.setClickable(true);
                    footer.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    progressDailog.dismiss();
                }
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
                // msclass.showMessage(resultout + "mdo_demoModelVisitDetail--E");

            }
        }



    }
    public class MyDemoLoading extends AsyncTask
    {
        Context context;
        String query;
        ProgressDialog dialog;
        MyDemoLoading()
        {

        }
        MyDemoLoading(Context context,String query)
        {
            this.context=context;
            this.query=query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(context);
            dialog.setMessage("Please Wait");
            if(!dialog.isShowing())
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            createList1(query);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            recDemoList.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);

            addData(mList);
        }
    }


}
