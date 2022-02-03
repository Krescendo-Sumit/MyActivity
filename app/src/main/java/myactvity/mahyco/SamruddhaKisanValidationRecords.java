package myactvity.mahyco;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SamruddhaKisanModel;
import myactvity.mahyco.helper.SqliteDatabase;

public class SamruddhaKisanValidationRecords extends AppCompatActivity {

    RecyclerView recRecordList;
    LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    Prefs mPref;
    public SqliteDatabase mDatabase;
    Config config;
    public Messageclass msclass;
    private Context context;
    String status, focussedVillage, state, district, taluka, village, farmerDetail, tbm, mdo;
    ImageView backbtn;
    TextView lblNoRecords;
    Button btn_search;
    EditText txt_searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samruddha_kisan_validation_records);
        getSupportActionBar().hide();
        getBundleValue();
        intiUI();

    }

    private void getBundleValue() {
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("status").equals("ALL")){
            status = "";
        }else {
            status = bundle.getString("status");
        }
        focussedVillage = bundle.getString("focussedVillage");
        state = bundle.getString("state");
        district = bundle.getString("district");
        taluka = bundle.getString("taluka");
        village = bundle.getString("village");
        farmerDetail = bundle.getString("farmerDetail");
        tbm = bundle.getString("tbm");

        if(bundle.getString("mdo").equals("ALL")){
            mdo = "";
        }else {
            mdo = bundle.getString("mdo");
        }
    }

    private void intiUI() {

        mPref = Prefs.with(this);
        context = this;
        mDatabase = new SqliteDatabase(this);
        config = new Config(this); //Here the context is passing
        msclass = new Messageclass(this);
        recRecordList = (RecyclerView) findViewById(R.id.recRecordList);
        backbtn = (ImageView) findViewById(R.id.backbtn);
        lblNoRecords = (TextView) findViewById(R.id.lblNoRecords);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recRecordList.setLayoutManager(layoutManager);
        btn_search=findViewById(R.id.btn_search);
        txt_searchtext=findViewById(R.id.et_searchtext);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=txt_searchtext.getText().toString().trim();
                getRecordsFromDB(str);
            }
        });

        getRecordsFromDB();

    }

    private void getRecordsFromDB() {

        try {
            ArrayList<SamruddhaKisanModel> samruddhaKisanModelList = new ArrayList<SamruddhaKisanModel>();


            String searchQuery = getQuery();

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                JSONObject jsonObject = new JSONObject();
                jsonArray = mDatabase.getResults(searchQuery);

                jsonObject.put("Table", jsonArray);

                Log.d("list", "SamruddhaKisanValidationData: " + jsonObject);


                for (int i = 0; i < jsonArray.length(); i++) {

                    SamruddhaKisanModel samruddhaKisanModel = new SamruddhaKisanModel();

                    samruddhaKisanModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    samruddhaKisanModel.setId(jsonArray.getJSONObject(i).getString("id"));
                    samruddhaKisanModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    samruddhaKisanModel.setWhatsappNumber(jsonArray.getJSONObject(i).getString("whatsappNumber"));
                    samruddhaKisanModel.setMdoDesc(jsonArray.getJSONObject(i).getString("mdoDesc"));
                    samruddhaKisanModel.setMdoCode(jsonArray.getJSONObject(i).getString("mdoCode"));
                    samruddhaKisanModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                    samruddhaKisanModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                    samruddhaKisanModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    samruddhaKisanModel.setTotalLand(jsonArray.getJSONObject(i).getString("totalLand"));
                    samruddhaKisanModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    samruddhaKisanModel.setEntryDt(jsonArray.getJSONObject(i).getString("EntryDt"));
                    samruddhaKisanModel.setTaggedAddress(jsonArray.getJSONObject(i).getString("taggedAddress"));
                    samruddhaKisanModel.setAction(jsonArray.getJSONObject(i).getString("action"));
                    samruddhaKisanModel.setReasons(jsonArray.getJSONObject(i).getString("reasons"));
                    samruddhaKisanModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
/*
 Adding These Field For the new Data Added Facilities

 */
                    samruddhaKisanModel.setFarmer_dob(jsonArray.getJSONObject(i).getString("farmer_dob"));
                    samruddhaKisanModel.setFarmer_anniversarydate(jsonArray.getJSONObject(i).getString("farmer_anniversarydate"));
                    samruddhaKisanModel.setFarmer_pincode(jsonArray.getJSONObject(i).getString("farmer_pincode"));
                    samruddhaKisanModel.setFarmer_landmark(jsonArray.getJSONObject(i).getString("farmer_landmark"));
                    samruddhaKisanModel.setFarmer_house_latlong(jsonArray.getJSONObject(i).getString("farmer_house_latlong"));
                    samruddhaKisanModel.setFarmer_house_address(jsonArray.getJSONObject(i).getString("farmer_house_address"));
                    samruddhaKisanModel.setFarmer_photo_path(jsonArray.getJSONObject(i).getString("farmer_photo_path"));
                    samruddhaKisanModel.setFarmer_photo_name(jsonArray.getJSONObject(i).getString("farmer_photo_name"));




                    samruddhaKisanModelList.add(samruddhaKisanModel);
                }
                lblNoRecords.setVisibility(View.GONE);
                recRecordList.setVisibility(View.VISIBLE);
                recRecordList.setAdapter(new SamruddhaValidationRecordAdapter(this, samruddhaKisanModelList, mDatabase));

            }else {
                lblNoRecords.setVisibility(View.VISIBLE);
                recRecordList.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    private String getQuery() {
        String searchQuery = null;
        StringBuilder sb = new StringBuilder();

        // Initial Query
        sb.append("SELECT *  FROM SamruddhaKisanValidationData");

        // FOR ALL
        if ( tbm.equals("") && mdo.equals("") &&   focussedVillage.equals("")   && status.equals("")  && district.equals("") && taluka.equals("") && village.equals("") && farmerDetail.equals("")) {
            return " " + sb.toString()+ " order by farmerName" ;
        }
        if ( !tbm.equals("") && !mdo.equals("") && !focussedVillage.equals("") && !status.equals("")  && !district.equals("") && !taluka.equals("") && !village.equals("") && !farmerDetail.equals("")) {
            return " " + sb.toString()+ " order by farmerName"  ;
            //return " " + sb.toString() + "  order by farmerName asc  ";
        }

        sb.append(" WHERE ");

        // tbm
        if (!tbm.equals("")) {
            sb.append("upper(tbmCode) = '" + tbm.toUpperCase() + "'");
            sb.append(" AND ");
        }

        // mdo
        if (!mdo.equals("")) {
            sb.append("upper(mdoCode) = '" + mdo.toUpperCase() + "'");
            sb.append(" AND ");
        }


        // focussedVillage
        if (!focussedVillage.equals("")) {
            sb.append("focussedVillage = '" + focussedVillage.toUpperCase() + "'");
            sb.append(" AND ");
        }

        // status
        if ((status !=null) &&  !status.equals("")) {

            sb.append(" action = '" + status.toUpperCase() + "'");
            sb.append(" AND ");
        }

        // state
        if ((state !=null) &&  !state.equals("")) {

            sb.append(" state = '" + state.toUpperCase() + "'");
            sb.append(" AND ");
        }


        // district
        if ((district !=null ) &&!district.equals("")) {
            sb.append(" district = '" + district.toUpperCase() + "'");
            sb.append(" AND ");
        }

        // taluka
        if ( (taluka !=null) && !taluka.equals("") ) {
            sb.append(" taluka = '" + taluka.toUpperCase() + "'");
            sb.append(" AND ");
        }

        // village
        if (  (village !=null) && !village.equals("") ) {
            sb.append(" village = '" + village.toUpperCase() + "'");
            sb.append(" AND ");
        }


        // farmer detail
        if (!farmerDetail.equals("")){
            String[] array = farmerDetail.split("-");
            sb.append("farmerName = '" + array[0] + "'");
            sb.append(" AND ");
            sb.append("mobileNumber = '" + array[1] + "' ");
            sb.append(" AND ");
        }

        sb.delete(sb.length() - 4, sb.length());

        return sb.toString() + " order by farmerName" ;
    }
    private void getRecordsFromDB(String searchText) {

        try {
            ArrayList<SamruddhaKisanModel> samruddhaKisanModelList = new ArrayList<SamruddhaKisanModel>();
            String searchQuery ="SELECT *  FROM SamruddhaKisanValidationData where farmerName || mobileNumber || village  like '%"+searchText+"%' order by farmerName";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {

                JSONObject jsonObject = new JSONObject();
                jsonArray = mDatabase.getResults(searchQuery);

                jsonObject.put("Table", jsonArray);

                Log.d("list", "SamruddhaKisanValidationData: " + jsonObject);


                for (int i = 0; i < jsonArray.length(); i++) {

                    SamruddhaKisanModel samruddhaKisanModel = new SamruddhaKisanModel();

                    samruddhaKisanModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    samruddhaKisanModel.setId(jsonArray.getJSONObject(i).getString("id"));
                    samruddhaKisanModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    samruddhaKisanModel.setWhatsappNumber(jsonArray.getJSONObject(i).getString("whatsappNumber"));
                    samruddhaKisanModel.setMdoDesc(jsonArray.getJSONObject(i).getString("mdoDesc"));
                    samruddhaKisanModel.setMdoCode(jsonArray.getJSONObject(i).getString("mdoCode"));
                    samruddhaKisanModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                    samruddhaKisanModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                    samruddhaKisanModel.setVillage(jsonArray.getJSONObject(i).getString("village"));
                    samruddhaKisanModel.setTotalLand(jsonArray.getJSONObject(i).getString("totalLand"));
                    samruddhaKisanModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    samruddhaKisanModel.setEntryDt(jsonArray.getJSONObject(i).getString("EntryDt"));
                    samruddhaKisanModel.setTaggedAddress(jsonArray.getJSONObject(i).getString("taggedAddress"));
                    samruddhaKisanModel.setAction(jsonArray.getJSONObject(i).getString("action"));
                    samruddhaKisanModel.setReasons(jsonArray.getJSONObject(i).getString("reasons"));
                    samruddhaKisanModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));
/*
 Adding These Field For the new Data Added Facilities

 */
                    samruddhaKisanModel.setFarmer_dob(jsonArray.getJSONObject(i).getString("farmer_dob"));
                    samruddhaKisanModel.setFarmer_anniversarydate(jsonArray.getJSONObject(i).getString("farmer_anniversarydate"));
                    samruddhaKisanModel.setFarmer_pincode(jsonArray.getJSONObject(i).getString("farmer_pincode"));
                    samruddhaKisanModel.setFarmer_landmark(jsonArray.getJSONObject(i).getString("farmer_landmark"));
                    samruddhaKisanModel.setFarmer_house_latlong(jsonArray.getJSONObject(i).getString("farmer_house_latlong"));
                    samruddhaKisanModel.setFarmer_house_address(jsonArray.getJSONObject(i).getString("farmer_house_address"));
                    samruddhaKisanModel.setFarmer_photo_path(jsonArray.getJSONObject(i).getString("farmer_photo_path"));




                    samruddhaKisanModelList.add(samruddhaKisanModel);
                }
                lblNoRecords.setVisibility(View.GONE);
                recRecordList.setVisibility(View.VISIBLE);
                recRecordList.setAdapter(new SamruddhaValidationRecordAdapter(this, samruddhaKisanModelList, mDatabase));

            }else {
                lblNoRecords.setVisibility(View.VISIBLE);
                recRecordList.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }


}
