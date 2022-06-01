package myactvity.mahyco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.AddFarmerListAdapter;
import myactvity.mahyco.helper.AddFarmerListModel;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public final class AddFarmerDialogActivity extends Dialog {


    SearchableSpinner spVillage;
    SearchableSpinner spTaluka;
    Button decrementFarmers, incrementFarmers, btnAdd;
    RadioGroup radGroup;
    RadioButton radFocused, radOther;
    String villageType = "focussed";
    String taluka = "";
    CardView cardTaluka;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etFarmerNo;
    TextView clearSearch;
    Context context;
    String mobileNumber;
    String usercode;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    RelativeLayout footer;
    private Handler handler = new Handler();
    private int farmerNumber;


    // TextView txt_noRecords;
    Config config;
    RecyclerView recDemoList;
    TextView noDataText;
    TextView lblheader;

    SharedPreferences locdata, pref;
    ProgressDialog dialog;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    SharedPreferences.Editor loceditor, editor;
    ArrayList<AddFarmerListModel> mList = new ArrayList<>();


    AddFarmerListAdapter adapterMDO;

    public AddFarmerDialogActivity(@NonNull Context context, String mobileNumber) {
        super(context);
        this.context = context;
        this.mobileNumber = mobileNumber;

    }

    public AddFarmerDialogActivity(@NonNull Context context, int themeResId) {
        super(context, themeResId);


    }

    protected AddFarmerDialogActivity(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_addfarmer_purchase_list);
        //   getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        mDatabase = SqliteDatabase.getInstance(context);

        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        recDemoList = (RecyclerView) findViewById(R.id.recDemoList);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        footer = (RelativeLayout) findViewById(R.id.footer);

        LinearLayoutManager lllm = new LinearLayoutManager(context);
        lllm.setOrientation(LinearLayoutManager.VERTICAL);
        recDemoList.setLayoutManager(lllm);
        lblheader = (TextView) findViewById(R.id.lblheader);
        noDataText = (TextView) findViewById(R.id.noDataText);
        clearSearch = (TextView) findViewById(R.id.clearSearch);
        etFarmerNo = (EditText) findViewById(R.id.etFarmerNo);
        decrementFarmers = (Button) findViewById(R.id.decrementFarmers);
        incrementFarmers = (Button) findViewById(R.id.incrementFarmers);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        radGroup = (RadioGroup) findViewById(R.id.radGroup);
        radFocused = (RadioButton) findViewById(R.id.radFocused);
        radOther = (RadioButton) findViewById(R.id.radOther);
        cardTaluka = (CardView) findViewById(R.id.cardTaluka);


        usercode = pref.getString("UserID", null);
        msclass = new Messageclass(context);

        bindTaluka();
        etFarmerNo.addTextChangedListener(new TextWatcher() {

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

                if (String.valueOf(farmerNumber).length() >= 1) {
                    mList.clear();
//                    createList(" where farmers" + " LIKE  '%" + etFarmerNo.getText().toString().toLowerCase().trim() + "%'" + " ");
                    Toast.makeText(context, "Functionality to search", Toast.LENGTH_SHORT).show();


                } else if (s.length() == 0) {


                }
                System.out.println("afterTextChanged " + s.toString());


            }
        });
//
        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        incrementFarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });
        decrementFarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radFocused:

                        if (radFocused.isChecked()) {
                            villageType = "focussed";
                            bindFocussedVillage();
                            cardTaluka.setVisibility(View.GONE);
                        } else {
                            villageType = "other";
                            bindTaluka();
                            cardTaluka.setVisibility(View.VISIBLE);

                        }
                        radOther.setChecked(false);
                        break;
                    case R.id.radOther:
                        if (radOther.isChecked()) {
                            villageType = "other";
                            bindTaluka();
                            cardTaluka.setVisibility(View.VISIBLE);
                        } else {
                            villageType = "focussed";
                            bindFocussedVillage();
                            cardTaluka.setVisibility(View.GONE);


                        }
                        break;

                }
                Log.d("Villagetype", villageType);
            }
        });

        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!villageType.isEmpty()) {

                    if (villageType.contains("other")) {
                        bindVillage(taluka);
                    } else {
                        bindFocussedVillage();
                    }
                } else {

                    Toast.makeText(context, "Please select village type", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Log.d("MobNumberDialog", mobileNumber);
    }


    //Get Data from db and make list for adapter
    public void createList(String whereCondition) {

        String searchQuery = "SELECT *  FROM Dem " + whereCondition + "";


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {

                    AddFarmerListModel addFarmerListModel = new AddFarmerListModel();


                    addFarmerListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
                    addFarmerListModel.setSno(jsonArray.getJSONObject(i).getString("_id"));
                    addFarmerListModel.setArea(jsonArray.getJSONObject(i).getString("area"));
                    addFarmerListModel.setCoordinates(jsonArray.getJSONObject(i).getString("coordinates"));
                    addFarmerListModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    addFarmerListModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                    addFarmerListModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    addFarmerListModel.setWhatsappNumber(jsonArray.getJSONObject(i).getString("whatsappNumber"));
                    addFarmerListModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    addFarmerListModel.setPlotType(jsonArray.getJSONObject(i).getString("plotType"));
                    addFarmerListModel.setProduct(jsonArray.getJSONObject(i).getString("product"));
                    addFarmerListModel.setSeedQuantity(jsonArray.getJSONObject(i).getString("seedQuantity"));
                    addFarmerListModel.setSowingDate(jsonArray.getJSONObject(i).getString("sowingDate"));
                    addFarmerListModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    addFarmerListModel.setuId(jsonArray.getJSONObject(i).getString("uId"));


                    if (jsonArray.getJSONObject(i).getString("imgPath").equals("")) {
                        addFarmerListModel.setImgPath("");
                    } else {
                        addFarmerListModel.setImgPath(jsonArray.getJSONObject(i).getString("imgPath"));
                    }
                    addFarmerListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    addFarmerListModel.setState(jsonArray.getJSONObject(i).getString("state"));
                    addFarmerListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));


                    mList.add(addFarmerListModel);


                }
                recDemoList.setVisibility(View.VISIBLE);
                noDataText.setVisibility(View.GONE);

//                addData(mList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            recDemoList.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);

        }

    }


    public void decrement() {

        if (!etFarmerNo.getText().toString().isEmpty()) {

            farmerNumber = Integer.valueOf(etFarmerNo.getText().toString());
            if (farmerNumber > 1) {
                farmerNumber--;
                etFarmerNo.setText(String.valueOf(farmerNumber));
            }

        } else {
            if (farmerNumber > 1) {
                farmerNumber--;
                etFarmerNo.setText(String.valueOf(farmerNumber));
            }
        }
    }

    public void increment() {
        if (!etFarmerNo.getText().toString().isEmpty()) {

            farmerNumber = Integer.valueOf(etFarmerNo.getText().toString());
            if (farmerNumber < 99) {
                farmerNumber++;
                etFarmerNo.setText(String.valueOf(farmerNumber));

            } else {


                Toast.makeText(context, "Cannot search more", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (farmerNumber < 99) {
                farmerNumber++;
                etFarmerNo.setText(String.valueOf(farmerNumber));

            } else {


                Toast.makeText(context, "Cannot search more", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void bindTaluka() {
        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster  order by  taluka";
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
                        (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
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
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }


    public void bindFocussedVillage() {


        spVillage.setAdapter(null);


        String str = null;
        try {


            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Cursor cursor;
            searchQuery = "SELECT distinct vil_desc,vil_code  FROM FocussedVillageMaster order by vil_desc asc  ";
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
                    (context, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);


        } catch (
                Exception ex) {

            ex.printStackTrace();

        }


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (handler != null) {
            handler = null;
        }
    }

//    public void addData(ArrayList<AddFarmerListModel> mList) {
//
//        try {
////            final Context context = recDemoList.getContext();
//            //   if (adapterMDO == null) {
//
//
//            recDemoList.setLayoutManager(new LinearLayoutManager(context));
//            recDemoList.setAdapter(new AddFarmerListAdapter(context, mList));
//
//
//            // } else {adapterMDO.notifyDataSetChanged(); }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        bindFocussedVillage();
        createList("");
    }

}
