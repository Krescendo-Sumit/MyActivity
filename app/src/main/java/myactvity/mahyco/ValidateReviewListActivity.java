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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.DemoModelReviewListModel;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.helper.ValidateReviewListAdapter;
import myactvity.mahyco.utils.AnimationItem;
import myactvity.mahyco.utils.GridRecyclerView;
import myactvity.mahyco.utils.ItemOffsetDecoration;

/**
 * Created by Akash Namdev on 2019-07-31.
 */
public class ValidateReviewListActivity extends AppCompatActivity {


    Button btnSubmit;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etFixedPrice;
    FloatingActionButton faButton;
    Calendar dateSelected = Calendar.getInstance();
    private Context context;
    String action = "1";
    String usercode;
    // TextView txt_noRecords;
    Config config;
    GridRecyclerView recDemoList;
    ImageView imgBtnSync;
    TextView txtNotSynced, noDataText;
    String SERVER = "http://10.80.50.153/maatest/MDOHandler.ashx";
    TextView lblheader;
    ProgressDialog dialog, progressDailog;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    ArrayList<DemoModelReviewListModel> mList = new ArrayList<>();
    boolean isRadioFixed = false;
    private AnimationItem[] mAnimationItems;
    String date, uid, mobilenumber, cordinates, address, mdoCode;
    ValidateReviewListAdapter adapterMDO;
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_validate_list);
        getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        dialog = new ProgressDialog(this);
        progressDailog = new ProgressDialog(this);
        progressDailog.setIndeterminate(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDailog.setMessage("Data Uploading");
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        getIntentData();
        recDemoList = (GridRecyclerView) findViewById(R.id.recDemoList);
        recDemoList.setLayoutManager(new GridLayoutManager(this, 1));

        faButton = (FloatingActionButton) findViewById(R.id.faButton);
        txtNotSynced = (TextView) findViewById(R.id.txtNotSynced);
        lblheader = (TextView) findViewById(R.id.lblheader);
        noDataText = (TextView) findViewById(R.id.noDataText);
        imgBtnSync = (ImageView) findViewById(R.id.imgBtnSync);
        //  txt_noRecords = (TextView) findViewById(R.id.txt_noRecords);
        createList();
        mAnimationItems = new AnimationItem[]{
                new AnimationItem("Slide from bottom", R.anim.gridlayout_animation_from_bottom)};

        usercode = pref.getString("UserID", null);
        msclass = new Messageclass(this);


        int count = mDatabase.getRowCountVAlidatedReviewNotUploadedList("ValidatedDemoReviewData",uid);
        txtNotSynced.setText(String.valueOf(count));


        runLayoutAnimation(recDemoList, mAnimationItems[0]);
        imgBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                    return;
                }
                //uploadUpdatedDataModelRecords("mdo_demoModelVisitDetail_validation");
            }

        });

    }

    public void getIntentData() {
        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        date = i.getStringExtra("date");
        mobilenumber = i.getStringExtra("mobilenumber");
        cordinates = i.getStringExtra("cordinates");
        mdoCode = i.getStringExtra("mdoCode");
        address = i.getStringExtra("address");

        Log.d("datafromprevious", uid + "::" + mobilenumber + "::" + cordinates + "::" + date + "|||" + mdoCode);
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


    // get Review Table count
    public String getCountReview(String uid) {

        int count = mDatabase.getRowCountReviewModelList("ValidatedDemoReviewData", uid);
        return String.valueOf(count);

    }


    //Get Data from db and make list for adapter
    public void createList() {

        String searchQuery = "select * from ValidatedDemoReviewData where uIdP='" + uid + "'";
        Log.d("searchQuery", "createList: " + searchQuery);
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        mList.clear();
        int count = cursor.getCount();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {


                    DemoModelReviewListModel demoModelReviewListModel = new DemoModelReviewListModel();
                    demoModelReviewListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isUploaded"));
                    demoModelReviewListModel.setArea(jsonArray.getJSONObject(i).getString("area"));
                    demoModelReviewListModel.setCoordinates(jsonArray.getJSONObject(i).getString("coordinates"));
                    demoModelReviewListModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    demoModelReviewListModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));
                    demoModelReviewListModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));
                    demoModelReviewListModel.setSno(String.valueOf(i + 1));
                    demoModelReviewListModel.setProduct(jsonArray.getJSONObject(i).getString("product"));
                    demoModelReviewListModel.setSowingDate(jsonArray.getJSONObject(i).getString("sowingDate"));
                    demoModelReviewListModel.setVisitingDate(jsonArray.getJSONObject(i).getString("visitingDate"));
                    demoModelReviewListModel.setuId(jsonArray.getJSONObject(i).getString("uId"));
                    demoModelReviewListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    demoModelReviewListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                    demoModelReviewListModel.setReviewCount(getCountReview(jsonArray.getJSONObject(i).getString("uIdP")));
                    demoModelReviewListModel.setAddress(address == null ? "" : address);


                    mList.add(demoModelReviewListModel);


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

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.gridlayout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    public void addData(ArrayList<DemoModelReviewListModel> mList) {

        try {
            final Context context = recDemoList.getContext();
            final int spacing = 4;
            adapterMDO = new ValidateReviewListAdapter(context, mList, uid, date, mobilenumber, cordinates);

            recDemoList.setAdapter(adapterMDO);
            recDemoList.addItemDecoration(new ItemOffsetDecoration(spacing));
            adapterMDO.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        createList();
    }




}
