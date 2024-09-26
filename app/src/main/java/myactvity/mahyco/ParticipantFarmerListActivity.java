package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.ParticipantFarmerListAdapter;
import myactvity.mahyco.helper.ParticipantFarmerListModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class ParticipantFarmerListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    MultiSelectionSpinner spVillage;
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
    String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    SharedPreferences.Editor loceditor, editor;
    ArrayList<ParticipantFarmerListModel> mList = new ArrayList<>();


    ParticipantFarmerListAdapter adapterMDO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_participant_list);
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
        spVillage = (MultiSelectionSpinner) findViewById(R.id.spVillage);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        recDemoList = (RecyclerView) findViewById(R.id.recDemoList);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        footer = (RelativeLayout) findViewById(R.id.footer);

        LinearLayoutManager lllm = new LinearLayoutManager(this);
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
        msclass = new Messageclass(this);


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

                    createList("");

                }
                System.out.println("afterTextChanged " + s.toString());


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
        spVillage.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {
            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {
            }
        });
//

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radFocused:

                        if (radFocused.isChecked()) {
                            villageType = "focussed";
                            cardTaluka.setVisibility(View.GONE);
                        } else {
                            villageType = "other";
                            cardTaluka.setVisibility(View.VISIBLE);

                        }
                        radOther.setChecked(false);
                        break;
                    case R.id.radOther:
                        if (radOther.isChecked()) {
                            villageType = "other";
                            cardTaluka.setVisibility(View.VISIBLE);
                        } else {
                            villageType = "focussed";
                            cardTaluka.setVisibility(View.GONE);


                        }
                        break;

                }
                Log.d("Villagetype", villageType);
            }
        });


        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spVillage.setSelection(0);
                etFarmerNo.setText("");
                createList("");

            }
        });
        bindTaluka();


    }


    //Get Data from db and make list for adapter
    public void createList(String whereCondition) {

        String searchQuery = "SELECT *  FROM DemoModelData " + whereCondition + "";


        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);


        int count = cursor.getCount();

        mList.clear();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {

                    ParticipantFarmerListModel participantFarmerListModel = new ParticipantFarmerListModel();


                    participantFarmerListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
                    participantFarmerListModel.setSno(jsonArray.getJSONObject(i).getString("_id"));
                    participantFarmerListModel.setArea(jsonArray.getJSONObject(i).getString("area"));
                    participantFarmerListModel.setCoordinates(jsonArray.getJSONObject(i).getString("coordinates"));
                    participantFarmerListModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    participantFarmerListModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                    participantFarmerListModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    participantFarmerListModel.setWhatsappNumber(jsonArray.getJSONObject(i).getString("whatsappNumber"));
                    participantFarmerListModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    participantFarmerListModel.setPlotType(jsonArray.getJSONObject(i).getString("plotType"));
                    participantFarmerListModel.setProduct(jsonArray.getJSONObject(i).getString("product"));
                    participantFarmerListModel.setSeedQuantity(jsonArray.getJSONObject(i).getString("seedQuantity"));
                    participantFarmerListModel.setSowingDate(jsonArray.getJSONObject(i).getString("sowingDate"));
                    participantFarmerListModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    participantFarmerListModel.setuId(jsonArray.getJSONObject(i).getString("uId"));


                    if (jsonArray.getJSONObject(i).getString("imgPath").equals("")) {
                        participantFarmerListModel.setImgPath("");
                    } else {
                        participantFarmerListModel.setImgPath(jsonArray.getJSONObject(i).getString("imgPath"));
                    }
                    participantFarmerListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    participantFarmerListModel.setState(jsonArray.getJSONObject(i).getString("state"));
                    participantFarmerListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));


                    mList.add(participantFarmerListModel);


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


    public void decrement(View view) {

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

    public void increment(View view) {
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

    public void addData(ArrayList<ParticipantFarmerListModel> mList) {

        try {
//            final Context context = recDemoList.getContext();
            //   if (adapterMDO == null) {


            recDemoList.setLayoutManager(new LinearLayoutManager(this));
            recDemoList.setAdapter(new ParticipantFarmerListAdapter(this, mList));


            // } else {adapterMDO.notifyDataSetChanged(); }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
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


        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' order by  village ";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT VILLAGE";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("village").toString();
                }
                array[jArray.length() + 1] = "OTHER";
                if (array.length > 0) {
                    spVillage.setItems(array);
                    spVillage.hasNoneOption(true);
                    spVillage.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();
        }


    }


    public void bindFocussedVillage() {


        String str = null;
        try {
            String searchQuery = "";
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster order by  village ";


            String[] array;
            try {
                JSONObject object = new JSONObject();
                object.put("Table1", mDatabase.getResults(searchQuery));

                JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                array = new String[jArray.length() + 2];
                array[0] = "SELECT FOCUSED VILLAGE";
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    array[i + 1] = jObject.getString("village").toString();
                }
                array[jArray.length() + 1] = "OTHER";
                if (array.length > 0) {
                    spVillage.setItems(array);
                    spVillage.hasNoneOption(true);
                    spVillage.setSelection(new int[]{0});
                    // spdistr.setListener(this);
                }
            } catch (Exception ex) {
                Utility.showAlertDialog("Error", ex.getMessage(), context);
                ex.printStackTrace();

            }


            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), context);
            ex.printStackTrace();

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        bindFocussedVillage();
        createList("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        // createList("");

    }


}
