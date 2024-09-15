package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.DemoModelPlotListModel;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.helper.ValidateDemoListAdapter;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class ValidateDemoListActivity extends AppCompatActivity {

    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    SearchableSpinner spCropType, spProductName, spMobNumber, spMdo;
    EditText etTaluka;
    TextView clearSearch;
    FloatingActionButton faButton;
    Calendar dateSelected = Calendar.getInstance();
    String action = "1";
    String usercode;
    // TextView txt_noRecords;
    Config config;
    RecyclerView recDemoList;
    ImageView imgBtnSync;
    TextView txtNotSynced, noDataText;
    TextView lblheader;
    SharedPreferences locdata, pref;
    ProgressDialog dialog, progressDailog;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    SharedPreferences.Editor loceditor, editor;
    ArrayList<DemoModelPlotListModel> mList = new ArrayList<>();
    String selectedCrop;
    String selectedProduct;
    String selectedMobNumber;
    String selectedMdoCode;
    String mNumberFilter = "";
    String mdoCodeFilter = "";
    int check = 0;
    private Context context;
    private String mno, croptype, mdoCode;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo_validate_list);
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
        spCropType = findViewById(R.id.spCropType);
        spProductName = findViewById(R.id.spProductName);
        spMobNumber = findViewById(R.id.spMobNumber);
        spMdo = findViewById(R.id.spMdo);
        recDemoList = findViewById(R.id.recDemoList);

        LinearLayoutManager lllm = new LinearLayoutManager(this);
        lllm.setOrientation(LinearLayoutManager.VERTICAL);
        recDemoList.setLayoutManager(lllm);
        dialog = new ProgressDialog(this);
        faButton = findViewById(R.id.faButton);
        txtNotSynced = findViewById(R.id.txtNotSynced);
        lblheader = findViewById(R.id.lblheader);
        noDataText = findViewById(R.id.noDataText);
        clearSearch = findViewById(R.id.clearSearch);
        etTaluka = findViewById(R.id.etTaluka);
        bindcroptype(spCropType, "C");
        bindMobilenumber();
        bindMDoListSpinner();
        progressDailog = new ProgressDialog(this);
        progressDailog.setIndeterminate(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDailog.setMessage("Data Uploading");
        imgBtnSync = findViewById(R.id.imgBtnSync);


        usercode = pref.getString("UserID", null);
        msclass = new Messageclass(this);


        spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    croptype = URLEncoder.encode(gm.Desc().trim(), "UTF-8");

                    selectedMdoCode = spMdo.getSelectedItem().toString();
                    selectedMobNumber = spMobNumber.getSelectedItem().toString();


                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {

                        Pattern pattern1 = Pattern.compile("- *");
                        Matcher matcher1 = pattern1.matcher(selectedMobNumber);
                        if (matcher1.find()) {
                            mNumberFilter = selectedMobNumber.substring(0, matcher1.start());

                        }
                    } else {

                        mNumberFilter = getString(R.string.selectedMobNumber);
                    }

                    if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        Pattern pattern = Pattern.compile("- *");
                        Matcher matcher = pattern.matcher(selectedMdoCode);

                        if (matcher.find()) {
                            mdoCodeFilter = selectedMdoCode.substring(0, matcher.start());

                        }
                    } else {

                        mdoCodeFilter = getString(R.string.selectMDO);
                    }

                    selectedCrop = spCropType.getSelectedItem().toString();
                    bindProductName(spProductName, selectedCrop);


                    selectedProduct = spProductName.getSelectedItem().toString();


                    if (!selectedCrop.equals(getString(R.string.selectedCrop)) &&
                            !selectedMobNumber.equals(getString(R.string.selectedMobNumber)) && !selectedProduct.equals(getString(R.string.selectedProduct))) {

                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' AND product='" + selectedProduct + "' ");


                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedProduct.equals(getString(R.string.selectedProduct)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();

                        createList(" where crop='" + selectedCrop + "' AND product='" + selectedProduct + "' AND userCode='" + mdoCodeFilter + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "'AND mobileNumber='" + mNumberFilter + "' ");

                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "'AND userCode='" + mdoCodeFilter + "' ");

                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
                    } else if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where userCode='" + mdoCodeFilter + "' ");


                    }
//                    else if (!selectedCrop.equals(getString(R.string.selectedCrop))) {
//                        mList.clear();
//                        createList(" where crop='" + selectedCrop + "' ");
////
//
//
//                    }

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
                    selectedMdoCode = spMdo.getSelectedItem().toString();
                    selectedMobNumber = spMobNumber.getSelectedItem().toString();


                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {

                        Pattern pattern1 = Pattern.compile("- *");
                        Matcher matcher1 = pattern1.matcher(selectedMobNumber);
                        if (matcher1.find()) {
                            mNumberFilter = selectedMobNumber.substring(0, matcher1.start());

                        }
                    } else {

                        mNumberFilter = getString(R.string.selectedMobNumber);
                    }

                    if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        Pattern pattern = Pattern.compile("- *");
                        Matcher matcher = pattern.matcher(selectedMdoCode);

                        if (matcher.find()) {
                            mdoCodeFilter = selectedMdoCode.substring(0, matcher.start());

                        }
                    } else {

                        mdoCodeFilter = getString(R.string.selectMDO);
                    }

                    if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedProduct.equals(getString(R.string.selectedProduct)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();

                        createList(" where crop='" + selectedCrop + "' AND product='" + selectedProduct + "' AND userCode='" + mdoCodeFilter + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedProduct.equals(getString(R.string.selectedProduct)) && !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();

                        createList(" where crop='" + selectedCrop + "' AND product='" + selectedProduct + "' AND mobileNumber='" + mNumberFilter + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedProduct.equals(getString(R.string.selectedProduct))) {
                        mList.clear();

                        createList(" where crop='" + selectedCrop + "' AND product='" + selectedProduct + "' ");
                    }

//                    else if (!selectedCrop.equals(getString(R.string.selectedCrop))) {
//                        mList.clear();
//                        createList(" where crop='" + selectedCrop + "' ");
//
//                    }


                } catch (Exception e) {
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

                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {

                        Pattern pattern1 = Pattern.compile("- *");
                        Matcher matcher1 = pattern1.matcher(selectedMobNumber);
                        if (matcher1.find()) {
                            mNumberFilter = selectedMobNumber.substring(0, matcher1.start());

                        }
                    } else {

                        mNumberFilter = getString(R.string.selectedMobNumber);
                    }

                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber)) &&
                            !selectedCrop.equals(getString(R.string.selectedCrop))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' AND mobileNumber='" + mNumberFilter + "'");
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {
                        mList.clear();
                        createList(" where mobileNumber='" + mNumberFilter + "' ");
//                        AND product='" + selectedProduct +
                    } else if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where userCode='" + mdoCodeFilter + "' ");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spMdo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    mdoCode = URLEncoder.encode(gm.Code().trim(), "UTF-8");
                    selectedMdoCode = spMdo.getSelectedItem().toString();
                    selectedMobNumber = spMobNumber.getSelectedItem().toString();
                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber))) {

                        Pattern pattern1 = Pattern.compile("- *");
                        Matcher matcher1 = pattern1.matcher(selectedMobNumber);
                        if (matcher1.find()) {
                            mNumberFilter = selectedMobNumber.substring(0, matcher1.start());

                        }
                    } else {

                        mNumberFilter = getString(R.string.selectedMobNumber);
                    }

                    if (spCropType.getSelectedItemPosition() != 0) {
                        selectedCrop = spCropType.getSelectedItem().toString();
                    } else {

                        selectedCrop = getString(R.string.selectedCrop);
                    }
//                    selectedMobNumber = spMobNumber.getSelectedItem().toString();
//                    selectedMdoCode = spMdo.getSelectedItem().toString();

                    if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        Pattern pattern = Pattern.compile("- *");
                        Matcher matcher = pattern.matcher(selectedMdoCode);

                        if (matcher.find()) {
                            mdoCodeFilter = selectedMdoCode.substring(0, matcher.start());

                        }
                    } else {

                        mdoCodeFilter = getString(R.string.selectMDO);
                    }
                    if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber)) &&
                            !selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop +
                                "' AND mobileNumber='" + mNumberFilter + "' AND userCode='" + mdoCodeFilter + "' ");
                    } else if (!selectedCrop.equals(getString(R.string.selectedCrop)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where crop='" + selectedCrop + "' AND userCode='" + mdoCodeFilter + "'");
                    } else if (!selectedMobNumber.equals(getString(R.string.selectedMobNumber)) && !selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where mobileNumber='" + selectedMobNumber + "' AND userCode='" + mdoCodeFilter + "'");

                    } else if (!selectedMdoCode.equals(getString(R.string.selectMDO))) {
                        mList.clear();
                        createList(" where userCode='" + mdoCodeFilter + "' ");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                Intent intent = new Intent(ValidateDemoListActivity.this, DemoModelRecordActivity.class);
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
            // spCropType.setSelected(false);  // must
            // spCropType.setSelection(0, true);
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
                String searchQuery = "SELECT distinct mobileNumber,farmerName  FROM ValidatedDemoModelData order by mobileNumber asc  ";
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
                //spMobNumber.setSelected(false);  // must
                // spMobNumber.setSelection(0, true);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public void bindMDoListSpinner() {

        try {
            spMdo.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> mdolist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct user_code,MDO_name  FROM MDOListData order by user_code asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


                mdolist.add(new GeneralMaster("0", "SELECT MDO"));


                if (cursor != null && cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {
                        do {
                            mdolist.add(new GeneralMaster(cursor.getString(0),
                                    cursor.getString(0).toUpperCase() + "-" + cursor.getString(1).toUpperCase()));
                        } while (cursor.moveToNext());
                    }


                    cursor.close();
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, mdolist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMdo.setAdapter(adapter);
                // spMdo.setSelected(false);  // must
                // spMdo.setSelection(0, true);
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
            spProductName.setSelected(false);  // must
            spProductName.setSelection(0, true);
            dialog.dismiss();

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }


    }


    // get Review Table count
    public String getCountReviewModelList(String uid) {

        int count = mDatabase.getRowCountReviewModelList("ValidatedDemoReviewData", uid);
        return String.valueOf(count);

    }


    //Get Data from db and make list for adapter
    public void createList(String whereCondition) {
//        Toast.makeText(context, "createlistCalled", Toast.LENGTH_SHORT).show();
        String searchQuery = "SELECT isSynced,area,coordinates,crop,district,mobileNumber" +
                ",whatsappNumber,farmerName,plotType,product,seedQuantity,sowingDate,village,uId,entrydate,userCode,state,taluka  FROM ValidatedDemoModelData " + whereCondition + "";


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        Log.d("searchQuery", "createList: " + searchQuery);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {


                    DemoModelPlotListModel demoModelPlotListModel = new DemoModelPlotListModel();


                    demoModelPlotListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
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
                    demoModelPlotListModel.setSno(String.valueOf(i + 1));
                    demoModelPlotListModel.setLastVisit(jsonArray.getJSONObject(i).getString("entrydate"));
                    demoModelPlotListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    demoModelPlotListModel.setState(jsonArray.getJSONObject(i).getString("state"));
                    demoModelPlotListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                    demoModelPlotListModel.setMDOCode(jsonArray.getJSONObject(i).getString("userCode"));
                    demoModelPlotListModel.setLastVisit(getLastVisitDate(jsonArray.getJSONObject(i).getString("uId")));
                    demoModelPlotListModel.setReviewCount(getCountReviewModelList(jsonArray.getJSONObject(i).getString("uId")));
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


    public String getLastVisitDate(String uid) {
        String visitingdate = "";

        Cursor data = mDatabase.fetchLastDateValidate(uid);


        if (data.getCount() == 0) {

        } else {
            data.moveToFirst();

            visitingdate = data.getString((data.getColumnIndex("visitingDate")));


        //    Log.d("typerr", "visitingDate" + visitingdate);


        }
        data.close();

        if (visitingdate.equals("")) {

            visitingdate = "NO VISITS";
        }
        return visitingdate;

    }


    public void addData(ArrayList<DemoModelPlotListModel> mList) {

        try {


            recDemoList.setLayoutManager(new LinearLayoutManager(this));
            recDemoList.setAdapter(new ValidateDemoListAdapter(this, mList));


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


}
