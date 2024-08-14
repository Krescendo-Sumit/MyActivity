package myactvity.mahyco.travelreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTravelReportGTV extends AppCompatActivity implements GTVTravelAPI.GTVListener {
    ProgressDialog progressDialog;
    Context context;

    Button btnSubmit;
    RadioButton rbYes, rbNo;
    EditText et_remark;
    RadioGroup rgStatus;
    TextView txtTotalKM;
    TableLayout tbl;
    TextView txtScreenTitle;
    SqliteDatabase mDatabase;
    String userCode="";
    String InTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_report_gtv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ActivityTravelReportGTV.this;
        mDatabase = SqliteDatabase.getInstance(this);
        tbl = findViewById(R.id.tbl_details);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        txtTotalKM = (TextView) findViewById(R.id.txtTotalKM);
        txtScreenTitle = (TextView) findViewById(R.id.textView19);

        progressDialog = new ProgressDialog(context);

        rbYes = findViewById(R.id.rbYes);
        rbYes.setChecked(true);
        rbNo = findViewById(R.id.rbNo);
        rgStatus = findViewById(R.id.rgStatus);
        et_remark = findViewById(R.id.et_remark);
        btnSubmit = findViewById(R.id.btnSubmit);

        //   mManager.setReverseLayout(true);
        setTitle("Verify Summary Report");
        Date entrydate = new Date();
        InTime = new SimpleDateFormat("yyyy-MM-dd").format(entrydate);
      //  final String InTime = "2024-08-13";

        SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
        userCode = sp.getString("UserID", null);
        userCode = userCode.replace(" ", "%20");

        try{
            String searchQuery12 = "select  *  from  GTVTravelActivityData where isSynced='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if (count > 0) {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setMessage("Upload GTV Travel Data ")
                        .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                uploadGtvTravelData();
                            }
                        }).show();
            }else
            {
                try {
                    txtTotalKM.setText("");
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("FilterValue", userCode + "," + InTime);
                    jsonObject.addProperty("FilterOption", "GetTravelReport");
                    getList(jsonObject);
                } catch (Exception exception) {
                    Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e)
        {

        }






        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        try {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0);
            String userCode = sp.getString("UserID", null);
            userCode = userCode.replace(" ", "%20");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            boolean status = true;
            if (rbYes.isChecked())
                status = true;
            if (rbNo.isChecked())
                status = false;

            String remark = et_remark.getText().toString().trim();
            if (rbNo.isChecked()) {
                if (remark.trim().equals("") || remark.trim().length() < 5) {
                    Toast.makeText(context, "Please enter proper remark.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MDOCode", "" + userCode);//: "mh248",
            jsonObject.addProperty("IsApproved", status);//: true,
            jsonObject.addProperty("ApprovedDt", currentDateandTime);//: "",
            jsonObject.addProperty("Remark", remark);//: "Test Remark",
            jsonObject.addProperty("CreatedBy", "" + userCode);//: "Sumit"
            SubmitRemark(jsonObject);
        } catch (Exception e) {

        }
    }

    private void SubmitRemark(JsonObject jsonObject) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().submitMyTravelReportTriggeredRemark(jsonObject);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        Toast.makeText(ActivityTravelReportGTV.this, "" + response.body(), Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getList(JsonObject jsonObject) {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<Root> call = null;
            call = RetrofitClient.getInstance().getMyApi().GetTravelGTVReport(jsonObject);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        Root root = response.body();

                        try {
                            if (root.Status) {
                                try {
                                    Toast.makeText(ActivityTravelReportGTV.this, "" + root.getResult().gTV1ActivityModels.size(), Toast.LENGTH_SHORT).show();
                                    tbl.removeAllViews();




                                    TableRow tr_kadetails = new TableRow(context);
                                    tr_kadetails.setBackgroundColor(Color.parseColor("#bcb5b2a8"));
                                    TextView txt_title = new TextView(context);
                                    txt_title.setText("Name of KA with HQ");
                                    txt_title.setTypeface(txtScreenTitle.getTypeface());
                                    txt_title.setPadding(5,5,5,5);
                                    txt_title.setTextColor(Color.BLUE);
                                    TextView txt_code = new TextView(context);
                                    txt_code.setPadding(5,5,5,5);
                                    txt_code.setTypeface(txtScreenTitle.getTypeface());
                                    txt_code.setText(":"+root.Result.kAModel.KAName + " HQ-" + root.Result.kAModel.KAHQ);
                                    tr_kadetails.addView(txt_title);
                                    tr_kadetails.addView(txt_code);
                                    tbl.addView(tr_kadetails);


                                    TableRow tr_EmpID = new TableRow(context);
                                    TextView txt_titleEMP = new TextView(context);
                                    txt_titleEMP.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleEMP.setTextColor(Color.BLUE);
                                    txt_titleEMP.setPadding(5,5,5,5);
                                    txt_titleEMP.setText("Employee ID");
                                    TextView txt_codeEMP = new TextView(context);
                                    txt_codeEMP.setTypeface(txtScreenTitle.getTypeface());
                                    txt_codeEMP.setPadding(5,5,5,5);
                                    txt_codeEMP.setText(":"+root.Result.kAModel.KACode);
                                    tr_EmpID.addView(txt_titleEMP);
                                    tr_EmpID.addView(txt_codeEMP);
                                    tbl.addView(tr_EmpID);


                                    TableRow tr_KATravel = new TableRow(context);
                                    tr_KATravel.setBackgroundColor(Color.parseColor("#bcb5b2a8"));
                                    TextView txt_titleKATravel = new TextView(context);
                                    TextView txt_codeKATravel = new TextView(context);

                                    txt_titleKATravel.setText("KM Travelled (As per KA)");

                                    txt_titleKATravel.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleKATravel.setTextColor(Color.BLUE);
                                    txt_titleKATravel.setPadding(5,5,5,5);

                                    txt_codeKATravel.setText(":"+""+root.Result.kAKMModel.TotalKM+"KM");
                                    txt_codeKATravel.setTypeface(txtScreenTitle.getTypeface());
                                    txt_codeKATravel.setPadding(5,5,5,5);
                                    tr_KATravel.addView(txt_titleKATravel);
                                    tr_KATravel.addView(txt_codeKATravel);

                                    tbl.addView(tr_KATravel);


                                    TableRow tr_KATravelSys = new TableRow(context);
                                    TextView txt_titleKATravelSys = new TextView(context);
                                    TextView txt_codeKATravelSys = new TextView(context);

                                    txt_titleKATravelSys.setText("KM Travelled (As per System)");


                                    txt_titleKATravelSys.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleKATravelSys.setTextColor(Color.BLUE);
                                    txt_titleKATravelSys.setPadding(5,5,5,5);
                                    txt_codeKATravelSys.setTypeface(txtScreenTitle.getTypeface());
                                    txt_codeKATravelSys.setText(":"+""+root.Result.myTravelKMModel.TotalKM+"KM");
                                    txt_codeKATravelSys.setPadding(5,5,5,5);
                                    tr_KATravelSys.addView(txt_titleKATravelSys);
                                    tr_KATravelSys.addView(txt_codeKATravelSys);

                                    tbl.addView(tr_KATravelSys);

                                    TableRow tr_GTV = new TableRow(context);
                                    tr_GTV.setBackgroundColor(Color.parseColor("#bcb5b2a8"));
                                    TextView txt_titleGTV = new TextView(context);
                                    txt_titleGTV.setText("GTV1 Activity");
                                    txt_titleGTV.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleGTV.setTextColor(Color.BLUE);
                                    txt_titleGTV.setPadding(5,5,5,5);
                                    TableLayout tbl_GTV1=new TableLayout(context);
                                    for(int i=0;i<root.getResult().gTV1ActivityModels.size();i++)
                                    {
                                        GTV1ActivityModel activityModel=root.getResult().gTV1ActivityModels.get(i);
                                        if(activityModel.ActivityName.toLowerCase().contains("punch"))
                                            continue;

                                        TableRow tr=new TableRow(context);

                                        TextView activityNameKey=new TextView(context);
                                        activityNameKey.setId(i+500);
                                        activityNameKey.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameKey.setText(activityModel.ActivityName);
                                        TextView activityNameValue=new TextView(context);
                                        activityNameValue.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameValue.setText("-"+activityModel.ActivityCount);
                                        activityNameValue.setTextColor(Color.RED);
                                        activityNameValue.setPadding(5,5,5,5);
                                        tr.addView(activityNameKey);
                                        tr.addView(activityNameValue);
                                        tbl_GTV1.addView(tr);

                                    }
                                    tr_GTV.addView(txt_titleGTV);
                                    tr_GTV.addView(tbl_GTV1);

                                    tbl.addView(tr_GTV);




                                    TableRow tr_GTV2 = new TableRow(context);
                                    TextView txt_titleGTV2 = new TextView(context);
                                    txt_titleGTV2.setText("GTV2 Activity");
                                    txt_titleGTV2.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleGTV2.setTextColor(Color.BLUE);
                                    txt_titleGTV2.setPadding(5,5,5,5);
                                    TableLayout tbl_GTV2=new TableLayout(context);
                                    for(int i=0;i<root.getResult().gTV2ActivityModels.size();i++)
                                    {
                                        GTV2ActivityModel activityModel=root.getResult().gTV2ActivityModels.get(i);
                                        if(activityModel.ActivityName.toLowerCase().contains("punch"))
                                            continue;
                                        TableRow tr=new TableRow(context);

                                        TextView activityNameKey=new TextView(context);
                                        activityNameKey.setId(i+500);
                                        activityNameKey.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameKey.setText(activityModel.ActivityName);
                                        TextView activityNameValue=new TextView(context);
                                        activityNameValue.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameValue.setText("-"+activityModel.ActivityCount);
                                        activityNameValue.setTextColor(Color.RED);
                                        activityNameValue.setPadding(5,5,5,5);
                                        tr.addView(activityNameKey);
                                        tr.addView(activityNameValue);
                                        tbl_GTV2.addView(tr);

                                    }
                                    tr_GTV2.addView(txt_titleGTV2);
                                    tr_GTV2.addView(tbl_GTV2);

                                    tbl.addView(tr_GTV2);




                                    TableRow tr_GTVMarket = new TableRow(context);
                                    tr_GTVMarket.setBackgroundColor(Color.parseColor("#bcb5b2a8"));
                                    TextView txt_titleGTVMarket = new TextView(context);
                                    txt_titleGTVMarket.setText("Market Activity");
                                    txt_titleGTVMarket.setTypeface(txtScreenTitle.getTypeface());
                                    txt_titleGTVMarket.setTextColor(Color.BLUE);
                                    txt_titleGTVMarket.setPadding(5,5,5,5);
                                    TableLayout tbl_GTVMarket=new TableLayout(context);
                                    for(int i=0;i<root.getResult().marketActivityModels.size();i++)
                                    {
                                        MarketModel activityModel=root.getResult().marketActivityModels.get(i);

                                        TableRow tr=new TableRow(context);

                                        TextView activityNameKey=new TextView(context);
                                        activityNameKey.setId(i+500);
                                        activityNameKey.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameKey.setText(activityModel.ActivityName);
                                        TextView activityNameValue=new TextView(context);
                                        activityNameValue.setTypeface(txtScreenTitle.getTypeface());
                                        activityNameValue.setText("-"+activityModel.ActivityCount);
                                        activityNameValue.setTextColor(Color.RED);
                                        activityNameValue.setPadding(5,5,5,5);
                                        tr.addView(activityNameKey);
                                        tr.addView(activityNameValue);
                                        tbl_GTVMarket.addView(tr);

                                    }
                                    tr_GTVMarket.addView(txt_titleGTVMarket);
                                    tr_GTVMarket.addView(tbl_GTVMarket);

                                    tbl.addView(tr_GTVMarket);



                                } catch (Exception e) {
                                    new androidx.appcompat.app.AlertDialog.Builder(context)
                                            .setMessage("Something went wrong."+e.getMessage())
                                            .setTitle("Exception")
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }


    }

    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
    public class GTV1ActivityModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getGTVType() {
            return GTVType;
        }

        public void setGTVType(String GTVType) {
            this.GTVType = GTVType;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public void setActivityName(String activityName) {
            ActivityName = activityName;
        }

        public int getActivityCount() {
            return ActivityCount;
        }

        public void setActivityCount(int activityCount) {
            ActivityCount = activityCount;
        }

        public String getVillageName() {
            return VillageName;
        }

        public void setVillageName(String villageName) {
            VillageName = villageName;
        }

        public String KACode;

        public String GTVType;

        public String ActivityName;

        public int ActivityCount;

        public String VillageName;
    }

    public class GTV2ActivityModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getGTVType() {
            return GTVType;
        }

        public void setGTVType(String GTVType) {
            this.GTVType = GTVType;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public void setActivityName(String activityName) {
            ActivityName = activityName;
        }

        public int getActivityCount() {
            return ActivityCount;
        }

        public void setActivityCount(int activityCount) {
            ActivityCount = activityCount;
        }

        public String getVillageName() {
            return VillageName;
        }

        public void setVillageName(String villageName) {
            VillageName = villageName;
        }

        public String KACode;

        public String GTVType;

        public String ActivityName;

        public int ActivityCount;

        public String VillageName;
    }

    public class MarketModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getGTVType() {
            return GTVType;
        }

        public void setGTVType(String GTVType) {
            this.GTVType = GTVType;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public void setActivityName(String activityName) {
            ActivityName = activityName;
        }

        public int getActivityCount() {
            return ActivityCount;
        }

        public void setActivityCount(int activityCount) {
            ActivityCount = activityCount;
        }

        public String getVillageName() {
            return VillageName;
        }

        public void setVillageName(String villageName) {
            VillageName = villageName;
        }

        public String KACode;

        public String GTVType;

        public String ActivityName;

        public int ActivityCount;

        public String VillageName;
    }



    public class KAKMModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public int getTotalKM() {
            return TotalKM;
        }

        public void setTotalKM(int totalKM) {
            TotalKM = totalKM;
        }

        public String KACode;

        public int TotalKM;
    }

    public class KAModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getKAName() {
            return KAName;
        }

        public void setKAName(String KAName) {
            this.KAName = KAName;
        }

        public String getTBMCode() {
            return TBMCode;
        }

        public void setTBMCode(String TBMCode) {
            this.TBMCode = TBMCode;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public String getKAHQ() {
            return KAHQ;
        }

        public void setKAHQ(String KAHQ) {
            this.KAHQ = KAHQ;
        }

        public String KACode;

        public String KAName;

        public String TBMCode;

        public String State;

        public String District;

        public String KAHQ;
    }

    public class MyTravelKMModel {

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public double getTotalKM() {
            return TotalKM;
        }

        public void setTotalKM(double totalKM) {
            TotalKM = totalKM;
        }

        public String KACode;

        public double TotalKM;
    }

    public class Result {
        public KAModel getkAModel() {
            return kAModel;
        }

        public void setkAModel(KAModel kAModel) {
            this.kAModel = kAModel;
        }

        public ArrayList<GTV1ActivityModel> getgTV1ActivityModels() {
            return gTV1ActivityModels;
        }

        public void setgTV1ActivityModels(ArrayList<GTV1ActivityModel> gTV1ActivityModels) {
            this.gTV1ActivityModels = gTV1ActivityModels;
        }

        public ArrayList<GTV2ActivityModel> getgTV2ActivityModels() {
            return gTV2ActivityModels;
        }

        public void setgTV2ActivityModels(ArrayList<GTV2ActivityModel> gTV2ActivityModels) {
            this.gTV2ActivityModels = gTV2ActivityModels;
        }



        public MyTravelKMModel getMyTravelKMModel() {
            return myTravelKMModel;
        }

        public void setMyTravelKMModel(MyTravelKMModel myTravelKMModel) {
            this.myTravelKMModel = myTravelKMModel;
        }

        public KAKMModel getkAKMModel() {
            return kAKMModel;
        }

        public void setkAKMModel(KAKMModel kAKMModel) {
            this.kAKMModel = kAKMModel;
        }

        public KAModel kAModel;
        public ArrayList<GTV1ActivityModel> gTV1ActivityModels;
        public ArrayList<GTV2ActivityModel> gTV2ActivityModels;

        public void setMarketActivityModels(ArrayList<MarketModel> marketActivityModels) {
            this.marketActivityModels = marketActivityModels;
        }

        public ArrayList<MarketModel> getMarketActivityModels() {
            return marketActivityModels;
        }

        public ArrayList<MarketModel> marketActivityModels;
        public MyTravelKMModel myTravelKMModel;
        public KAKMModel kAKMModel;
    }

    public class Root {

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public ActivityTravelReportGTV.Result getResult() {
            return Result;
        }

        public void setResult(ActivityTravelReportGTV.Result result) {
            Result = result;
        }

        public boolean Status;

        public String Message;

        public Result Result;
    }

    public void uploadGtvTravelData() {
        try {
            String searchQuery12 = "select  *  from  GTVTravelActivityData where isSynced='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if (count > 0) {
                JsonArray jsonArray;

                String searchQuery = "select id,ActivityId,KACode,GTVType,ActivityName,ActivityType,ActivityDt,VillageCode,VillageName,LastCoordinates,Coordinates,GTVActivityKM,AppVersion,Remark,isSynced from GTVTravelActivityData where isSynced=0";
                jsonArray = mDatabase.getResultsRetro(searchQuery);
/*
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    jsonObject.addProperty("ActivityId","0");
                }*/
                JsonObject jsonFinal = new JsonObject();
                jsonFinal.add("gTVUserActivityModels", jsonArray);
                new GTVTravelAPI(context, this).UploadGTVTravelData(jsonFinal);
                Log.i("GTV  Travel Data ", jsonFinal.toString());

            } else {
                Toast.makeText(context, "No Data for upload.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void OnGTVMasterUpload(String result) {

    }

    @Override
    public void OnGTVTravelDataUpload(String result) {
        Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
        mDatabase.UpdateStatus("Update GTVTravelActivityData set isSynced=1 where isSynced=0");
        try {
            txtTotalKM.setText("");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("FilterValue", userCode + "," + InTime);
            jsonObject.addProperty("FilterOption", "GetTravelReport");
            getList(jsonObject);
        } catch (Exception exception) {
            Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}