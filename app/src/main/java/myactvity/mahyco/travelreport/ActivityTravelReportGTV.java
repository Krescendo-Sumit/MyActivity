package myactvity.mahyco.travelreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTravelReportGTV extends AppCompatActivity implements GTVTravelAPI.GTVListener {
    ProgressDialog progressDialog;
    Context context;

    Button btnSubmit;
    Button btnMap;
    RadioButton rbYes, rbNo;
    EditText et_remark;
    RadioGroup rgStatus;
    TextView txtTotalKM;
    TableLayout tbl;
    TextView txtScreenTitle, txt_heading;
    SqliteDatabase mDatabase;
    String userCode = "";
    String InTime = "";
    WebView webView;
    String HTMLData = "";
    String GTVKAName = "-";
    String GTVStartTravelTime = "-";
    String GTVEndTravelTime = "-";
    String GTVKAKM = "-";
    String GTVAttendance = "-";
    String GTVSystemKM = "-";
    String GTVVillage1 = "-";
    String GTVVillage1PUNCHINTIME = "-";
    String GTVVillage1PUNCHOUTTIME = "-";
    String GTVVillage1TIMESpent = "-";
    String GTVVillage2 = "-";
    String GTVVillage2PUNCHINTIME = "-";
    String GTVVillage2PUNCHOUTTIME = "-";
    String GTVVillage2TIMESpent = "-";
    String GTVVillage1Activities = "-";
    String GTVVillage2Activities = "-";
    String GTV1Market1Activities = "-";
    String GTV2Market1Activities = "-";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_report_gtv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ActivityTravelReportGTV.this;
        mDatabase = SqliteDatabase.getInstance(this);
        tbl = findViewById(R.id.tbl_details);
        webView = findViewById(R.id.web);
        webView.clearCache(true);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        txtTotalKM = (TextView) findViewById(R.id.txtTotalKM);
        txtScreenTitle = (TextView) findViewById(R.id.txt_heading);

        progressDialog = new ProgressDialog(context);

        rbYes = findViewById(R.id.rbYes);
        rbYes.setChecked(true);
        rbNo = findViewById(R.id.rbNo);
        rgStatus = findViewById(R.id.rgStatus);
        et_remark = findViewById(R.id.et_remark);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnMap = findViewById(R.id.btn_map);

        //   mManager.setReverseLayout(true);
        setTitle("Verify Summary Report");
        Date entrydate = new Date();
        InTime = new SimpleDateFormat("yyyy-MM-dd").format(entrydate);
        //  final String InTime = "2024-08-13";


        txtScreenTitle.setText("Summary Report - " + new SimpleDateFormat("dd-MM-yyyy").format(entrydate));

        SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
        userCode = sp.getString("UserID", null);
        userCode = userCode.replace(" ", "%20");
        PrepareReport();
        try {
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
            } else {
                try {
                    txtTotalKM.setText("");
                    JsonObject jsonObject = new JsonObject();
                    //InTime = "2024-08-28";
                    jsonObject.addProperty("FilterValue", userCode + "," + InTime);
                    jsonObject.addProperty("FilterOption", "GetTravelReport");
                    getList(jsonObject);
                } catch (Exception exception) {
                    Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowMapRouteActivity.class);
                startActivity(intent);
            }
        });

        // Calling API to get System Distance
        getSystemDistance();


    }

    void getSystemDistance() {
        try {

            int endTravelCount = mDatabase.getActivityDoneCount("End Travel", InTime);
            int systemDistanceCount = mDatabase.getActivityDoneCount("System Distance", InTime);
            Toast.makeText(context, systemDistanceCount + " and " + endTravelCount, Toast.LENGTH_SHORT).show();


            if (endTravelCount > 0 && systemDistanceCount == 0) {
                Toast.makeText(context, "Calling Distance API", Toast.LENGTH_SHORT).show();
                String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String searchQuery12 = "select  Coordinates  from  GTVTravelActivityData where ActivityName!='System Distance' and ActivityDt like '%" + curdate + "%'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
                int count = cursor.getCount();
                if (count > 0) {
                    JsonArray jsonArray;

                    String searchQuery = "select  Coordinates  from  GTVTravelActivityData where ActivityName!='System Distance' and ActivityDt like '%" + curdate + "%'";
                    jsonArray = mDatabase.getResultsRetro(searchQuery);

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        String co = jsonObject.get("Coordinates").toString().trim();

                        Log.i("Data is ", co);
                        co = co.replace("\"", "");
                        String cosp[] = co.split("-");

                        if (cosp.length > 1) {
                            jsonObject.addProperty("lat", Double.parseDouble(cosp[0]));
                            jsonObject.addProperty("lng", Double.parseDouble(cosp[1]));

                        }

                    }
                    new GTVTravelAPI(context, this).GetSystemDistance(jsonArray);
                    //  Log.i("GTV  Travel Data ", jsonFinal.toString());

                } else {
                    Toast.makeText(context, "No Data for upload.", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (Exception e) {

        }
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
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.getBoolean("Status")) {
                                new AlertDialog.Builder(context).setMessage("" + object.getString("Message"))
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }
                            // Toast.makeText(ActivityTravelReportGTV.this, "" + response.body(), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {

                        }

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
                                    if (root.Result.kAModel != null) {
                                        GTVKAName = root.Result.kAModel.KAName + " HQ-" + root.Result.kAModel.KAHQ;
                                    } else {
                                        GTVKAName = "-";
                                    }
                                    if (root.Result.kAKMModel != null) {
                                        GTVKAKM = root.Result.kAKMModel.TotalKM + "KM";
                                    } else {
                                        GTVKAKM = "-";
                                    }
                                    if (root.Result.GetAttendanceModel != null) {
                                        GTVAttendance = root.Result.GetAttendanceModel.getGTVAttendance();
                                    } else {
                                        GTVAttendance = "-";
                                    }
                                    if (root.Result.myTravelKMModel != null) {
                                        GTVSystemKM = root.Result.myTravelKMModel.TotalKM + " KM";
                                    } else {
                                        GTVSystemKM = " -";
                                    }
                                    if (root.Result.myTravelModel != null) {
                                        MyTravelModel myTravelModel = root.Result.getMyTravelModel();
                                        GTVStartTravelTime = myTravelModel.getStartTravelTime();
                                        GTVEndTravelTime = myTravelModel.getEndTravelTime();
                                    }
                                    if (root.Result.gTV1Model != null) {
                                        GTV1Model gtv1Model = root.Result.getgTV1Model();
                                        GTVVillage1PUNCHINTIME = gtv1Model.getGTV1PunchIn();
                                        GTVVillage1PUNCHOUTTIME = gtv1Model.getGTV1PunchOut();
                                        GTVVillage1TIMESpent = gtv1Model.getGTV1TimeSpent();
                                    }
                                    if (root.Result.gTV2Model != null) {
                                        GTV2Model gtv2Model = root.Result.getgTV2Model();
                                        GTVVillage2PUNCHINTIME = gtv2Model.getGTV2PunchIn();
                                        GTVVillage2PUNCHOUTTIME = gtv2Model.getGTV2PunchOut();
                                        GTVVillage2TIMESpent = gtv2Model.getGTV2TimeSpent();
                                    }
                                    if (root.getResult().gTV1ActivityModels != null || root.getResult().gTV1ActivityModels.size() == 0) {
                                        GTVVillage1Activities = "";
                                        for (int i = 0; i < root.getResult().gTV1ActivityModels.size(); i++) {
                                            GTV1ActivityModel activityModel = root.getResult().gTV1ActivityModels.get(i);
                                            if (activityModel.ActivityName.toLowerCase().contains("punch")) {
                                                GTVVillage1 = activityModel.VillageName;
                                                continue;
                                            }
                                            if (activityModel.ActivityName.toLowerCase().contains("system")) {
                                                continue;
                                            }
                                            GTVVillage1Activities += activityModel.ActivityName + "(" + activityModel.ActivityCount + "), ";
                                        }
                                    } else {
                                        GTVVillage1Activities = "No Activity Found.";
                                    }
                                    if (root.getResult().gTV2ActivityModels != null || root.getResult().gTV2ActivityModels.size() == 0) {
                                        GTVVillage2Activities = "";
                                        for (int i = 0; i < root.getResult().gTV2ActivityModels.size(); i++) {
                                            GTV2ActivityModel activityModel = root.getResult().gTV2ActivityModels.get(i);
                                            if (activityModel.ActivityName.toLowerCase().contains("punch")) {
                                                GTVVillage2 = activityModel.VillageName;
                                                continue;
                                            }
                                            GTVVillage2Activities += activityModel.ActivityName + "(" + activityModel.ActivityCount + "), ";
                                        }
                                    } else {
                                        GTVVillage2Activities = "No Activity Found.";
                                    }

                                    if (root.getResult().marketGTV1ActivityModels != null) {
                                        GTV1Market1Activities="";
                                        for (int i = 0; i < root.getResult().marketGTV1ActivityModels.size(); i++) {
                                            MarketModel activityModel = root.getResult().marketGTV1ActivityModels.get(i);
                                            GTV1Market1Activities += activityModel.ActivityName + "(" + activityModel.ActivityCount + "), ";
                                        }

                                    }else {
                                        GTV1Market1Activities="No Activity Found.";
                                    }

                                    if (root.getResult().marketGTV2ActivityModels != null) {
                                        GTV2Market1Activities = "";
                                        for (int i = 0; i < root.getResult().marketGTV2ActivityModels.size(); i++) {
                                            MarketModel activityModel = root.getResult().marketGTV2ActivityModels.get(i);
                                            GTV2Market1Activities += activityModel.ActivityName + "(" + activityModel.ActivityCount + "), ";
                                        }

                                    }else {
                                        GTV2Market1Activities="No Activity Found.";
                                    }
                                    PrepareReport();

                                } catch (Exception e) {
                                    new androidx.appcompat.app.AlertDialog.Builder(context)
                                            .setMessage("Something went wrong." + e.getMessage())
                                            .setTitle("Exception")
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();
                                    PrepareReport();
                                }
                            }
                        } catch (NullPointerException e) {
                            PrepareReport();
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            PrepareReport();
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

    public void PrepareReport() {
        try {

            //  HTMLData = HTMLDataHeader;
// this code commented   we are getting time spent details gtv as well as start and end travel from local db .
// which now getting from API bcoz from local we can show only todays record only.
           /* GTVStartTravelTime = mDatabase.getGtvTravelDate(InTime, "Start Travel");
            GTVEndTravelTime = mDatabase.getGtvTravelDate(InTime, "End Travel");
            GTVVillage1PUNCHINTIME = mDatabase.getGtvPunchDate(InTime, "Punch In", "GTV1");
            GTVVillage1PUNCHOUTTIME = mDatabase.getGtvPunchDate(InTime, "Punch Out", "GTV1");
            GTVVillage2PUNCHINTIME = mDatabase.getGtvPunchDate(InTime, "Punch In", "GTV2");
            GTVVillage2PUNCHOUTTIME = mDatabase.getGtvPunchDate(InTime, "Punch Out", "GTV2");
            try {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(GTVVillage1PUNCHINTIME);
                Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(GTVVillage1PUNCHOUTTIME);

                long millis = date2.getTime() - date1.getTime();
                int hours = (int) (millis / (1000 * 60 * 60));
                int mins = (int) ((millis / (1000 * 60)) % 60);

                String diff = hours + "h:" + mins+" m";
                GTVVillage1TIMESpent=diff;
                Log.i("Time Difference is ", "--->" + diff);

                date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(GTVVillage2PUNCHINTIME);
                date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(GTVVillage2PUNCHOUTTIME);

                millis = date2.getTime() - date1.getTime();
                hours = (int) (millis / (1000 * 60 * 60));
                mins = (int) ((millis / (1000 * 60)) % 60);

                diff = hours + "h:" + mins+" m";
                GTVVillage2TIMESpent=diff;


                String s[];

                s=GTVStartTravelTime.split(" ");
                if(s.length>0)
                    GTVStartTravelTime=s[1];

                s=GTVEndTravelTime.split(" ");
                if(s.length>0)
                    GTVEndTravelTime=s[1];

                s=GTVVillage1PUNCHINTIME.split(" ");
                if(s.length>0)
                    GTVVillage1PUNCHINTIME=s[1];

                s=GTVVillage2PUNCHINTIME.split(" ");
                if(s.length>0)
                    GTVVillage2PUNCHINTIME=s[1];

                s=GTVVillage1PUNCHOUTTIME.split(" ");
                if(s.length>0)
                    GTVVillage1PUNCHOUTTIME=s[1];

                s=GTVVillage2PUNCHOUTTIME.split(" ");
                if(s.length>0)
                    GTVVillage2PUNCHOUTTIME=s[1];



            }catch (Exception e)
            {

            }

*/

            HTMLData = "<style>table, td, th {border: 1px solid;padding:3px;}table{border-collapse: collapse;}</style>" +
                    "<table width='100%'>" +
                    "<tr>" +
                    "<td colspan='6' style='background-color:green;color:white;text-align:center;'><b>Name : " + GTVKAName + "<b></td>" +

                    "</tr>" +
                    "<tr>" +
                    "<td colspan='3'><b>Start Travel :</b> <br>" + GTVStartTravelTime + " </td>" +
                    "<td colspan='3'><b>End Travel :</b> <br>" + GTVEndTravelTime + " </td>" +

                    "</tr>" +
                    "<tr>" +
                    "<td colspan='2'><b>KA KM :</b> <br>" + GTVKAKM + "</td> " +
                    "<td colspan='2'><b>System KM :</b> <br>" + GTVSystemKM + "</td> " +
                    "<td colspan='2'><b>Attendance :</b> <br>" + GTVAttendance + "</td> " +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan='6' style='text-align:center;background-color:FloralWhite;'><b>GTV-I Village : <i>" + GTVVillage1 + "</i></b> </td> " +
                    "</tr>" +
                    "<tr> " +
                    "<td colspan='2'><b>Punch_In :</b> <br>" + GTVVillage1PUNCHINTIME + " </td> " +
                    "<td colspan='2'><b>Punch_Out :</b> <br>" + GTVVillage1PUNCHOUTTIME + " </td> " +
                    "<td colspan='2'><b>Time_Spent :</b> <br>" + GTVVillage1TIMESpent + " </td> " +
                    "</tr>" +
                    "<tr> " +
                    "<td colspan='2'><b>Village Activities</b></td>  " +
                    "<td colspan='4'>" + GTVVillage1Activities + "</td> " +
                    "</tr>" +
                    "<tr> " +
                    "<td colspan='2'><b>Other Than FV Activities</b></td>" +
                    "<td colspan='4'>" + GTV1Market1Activities + "</td> " +
                    "</tr>" +
                    "<tr> " +
                    "<td colspan='6' style='text-align:center;background-color:FloralWhite;'><b>GTV-II Village : <i>" + GTVVillage2 + "</i></b></td> " +
                    "</tr> " +
                    "<tr> " +
                    "<td colspan='2'><b>Punch_In :</b> <br>" + GTVVillage2PUNCHINTIME + "</td> " +
                    "<td colspan='2'><b>Punch_Out :</b> <br>" + GTVVillage2PUNCHOUTTIME + "</td> " +
                    "<td colspan='2'><b>Time_Spent :</b> <br>" + GTVVillage2TIMESpent + " </td> " +
                    "</tr> " +
                    "<tr> " +
                    "<td colspan='2'><b>Village Activities</b></td> " +
                    "<td colspan='4'>" + GTVVillage2Activities + "</td> " +
                    "</tr>" +

                    "<tr> " +
                    "<td colspan='2'><b>Other Than FV Activities</b></td>" +
                    "<td colspan='4'>" + GTV2Market1Activities + "</td> " +
                    "" +
                    "</tr>" +
                    "</table>";
            ;


            webView.loadData(HTMLData, "text/html", "UTF-8");
            Toast.makeText(context, "Data Loading", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

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

        public ArrayList<MarketModel> getMarketGTV1ActivityModels() {
            return marketGTV1ActivityModels;
        }

        public void setMarketGTV1ActivityModels(ArrayList<MarketModel> marketGTV1ActivityModels) {
            this.marketGTV1ActivityModels = marketGTV1ActivityModels;
        }

        public ArrayList<MarketModel> getMarketGTV2ActivityModels() {
            return marketGTV2ActivityModels;
        }

        public void setMarketGTV2ActivityModels(ArrayList<MarketModel> marketGTV2ActivityModels) {
            this.marketGTV2ActivityModels = marketGTV2ActivityModels;
        }

        public ArrayList<MarketModel> marketGTV1ActivityModels;
        public ArrayList<MarketModel> marketGTV2ActivityModels;
        public MyTravelKMModel myTravelKMModel;
        public KAKMModel kAKMModel;

        public MyTravelModel getMyTravelModel() {
            return myTravelModel;
        }

        public void setMyTravelModel(MyTravelModel myTravelModel) {
            this.myTravelModel = myTravelModel;
        }

        public GTV1Model getgTV1Model() {
            return gTV1Model;
        }

        public void setgTV1Model(GTV1Model gTV1Model) {
            this.gTV1Model = gTV1Model;
        }

        public GTV2Model getgTV2Model() {
            return gTV2Model;
        }

        public void setgTV2Model(GTV2Model gTV2Model) {
            this.gTV2Model = gTV2Model;
        }

        public MyTravelModel myTravelModel;
        public GTV1Model gTV1Model;
        public GTV2Model gTV2Model;
        public GetAttendanceModel GetAttendanceModel;

    }
public class GetAttendanceModel{
    String KACode;

    public String getKACode() {
        return KACode;
    }

    public void setKACode(String KACode) {
        this.KACode = KACode;
    }

    public String getGTVAttendance() {
        return GTVAttendance;
    }

    public void setGTVAttendance(String GTVAttendance) {
        this.GTVAttendance = GTVAttendance;
    }

    String GTVAttendance;
}
    public class MyTravelModel {
        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getStartTravelTime() {
            return StartTravelTime;
        }

        public void setStartTravelTime(String startTravelTime) {
            StartTravelTime = startTravelTime;
        }

        public String getEndTravelTime() {
            return EndTravelTime;
        }

        public void setEndTravelTime(String endTravelTime) {
            EndTravelTime = endTravelTime;
        }

        String KACode;
        String StartTravelTime;
        String EndTravelTime;
    }

    public class GTV1Model {
        String KACode;

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getGTV1PunchIn() {
            return GTV1PunchIn;
        }

        public void setGTV1PunchIn(String GTV1PunchIn) {
            this.GTV1PunchIn = GTV1PunchIn;
        }

        public String getGTV1PunchOut() {
            return GTV1PunchOut;
        }

        public void setGTV1PunchOut(String GTV1PunchOut) {
            this.GTV1PunchOut = GTV1PunchOut;
        }

        public String getGTV1TimeSpent() {
            return GTV1TimeSpent;
        }

        public void setGTV1TimeSpent(String GTV1TimeSpent) {
            this.GTV1TimeSpent = GTV1TimeSpent;
        }

        public String getActivityDt() {
            return ActivityDt;
        }

        public void setActivityDt(String activityDt) {
            ActivityDt = activityDt;
        }

        String GTV1PunchIn;
        String GTV1PunchOut;
        String GTV1TimeSpent;
        String ActivityDt;
    }

    public class GTV2Model {
        String KACode;

        public String getKACode() {
            return KACode;
        }

        public void setKACode(String KACode) {
            this.KACode = KACode;
        }

        public String getGTV2PunchIn() {
            return GTV2PunchIn;
        }

        public void setGTV2PunchIn(String GTV2PunchIn) {
            this.GTV2PunchIn = GTV2PunchIn;
        }

        public String getGTV2PunchOut() {
            return GTV2PunchOut;
        }

        public void setGTV2PunchOut(String GTV2PunchOut) {
            this.GTV2PunchOut = GTV2PunchOut;
        }

        public String getGTV2TimeSpent() {
            return GTV2TimeSpent;
        }

        public void setGTV2TimeSpent(String GTV2TimeSpent) {
            this.GTV2TimeSpent = GTV2TimeSpent;
        }

        public String getActivityDt() {
            return ActivityDt;
        }

        public void setActivityDt(String activityDt) {
            ActivityDt = activityDt;
        }

        String GTV2PunchIn;
        String GTV2PunchOut;
        String GTV2TimeSpent;
        String ActivityDt;
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

                String searchQuery = "select id,ActivityId,KACode,GTVType,ActivityName,ActivityType,ActivityDt,VillageCode,VillageName,LastCoordinates,Coordinates,GTVActivityKM,AppVersion,Remark,isSynced,RefrenceId,ActualKM,DistanceFromPunchKm from GTVTravelActivityData where isSynced=0";
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
        try {
            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
            JSONObject jsonObjectResult=new JSONObject(result.trim());
            if(jsonObjectResult.getBoolean("ResultFlag") && jsonObjectResult.getString("status").toLowerCase().equals("success"))
            {
                mDatabase.UpdateStatus("Update GTVTravelActivityData set isSynced=1 where isSynced=0");
                new AlertDialog.Builder(context)
                        .setMessage(jsonObjectResult.getString("Comment"))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }else {
                new AlertDialog.Builder(context)
                        .setTitle("Something went wrong")
                        .setMessage(jsonObjectResult.getString("Comment"))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
            txtTotalKM.setText("");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("FilterValue", userCode + "," + InTime);
            jsonObject.addProperty("FilterOption", "GetTravelReport");
            getList(jsonObject);
        } catch (Exception exception) {
            Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnDistanceRetrive(String result) {
        Toast.makeText(context, "Get Location " + result, Toast.LENGTH_SHORT).show();
        if (CommonUtil.addGTVActivity(context, "5555", "System Distance", "0-0", "System Punch Out", "GTV", "" + result)) {

        }
    }
}