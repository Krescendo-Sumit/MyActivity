package myactvity.mahyco.travelreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import myactvity.mahyco.BuildConfig;
import myactvity.mahyco.R;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.model.CommonUtil;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTravelReportGTVNew extends AppCompatActivity implements GTVTravelAPI.GTVListener {
    ProgressDialog progressDialog;
    Context context;

    Button btnSubmit;
    Button btnMap;
    RadioButton rbYes, rbNo;
    EditText et_remark;
    RadioGroup rgStatus;
    TextView txtTotalKM;
    TextView txt_retryagain;
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
    TextView txt_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_report_gtv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ActivityTravelReportGTVNew.this;
        mDatabase = SqliteDatabase.getInstance(this);
        tbl = findViewById(R.id.tbl_details);
        txt_version = findViewById(R.id.txt_version);
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
        txt_retryagain = (TextView) findViewById(R.id.txt_retryagain);
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
        InTime = new SimpleDateFormat("dd/MM/yyyy").format(entrydate);
        //  final String InTime = "2024-08-13";
        txtScreenTitle.setText("Summary Report - " + new SimpleDateFormat("dd-MM-yyyy").format(entrydate));

        SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
        userCode = sp.getString("UserID", null);
        userCode = userCode.replace(" ", "%20");
        PrepareReport();
        tryforupload();
        try{
            txt_version.setText(""+ BuildConfig.VERSION_NAME);
        }catch (Exception e)
        {

        }

        txt_retryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSystemDistance();
                tryforupload();
            }
        });

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

    public void tryforupload() {
        try {
            String searchQuery12 = "select  *  from  GTVTravelActivityData where isSynced='0'";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
            int count = cursor.getCount();
            if (count > 0) {
                new AlertDialog.Builder(context)
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
/*                    InTime = "30/09/2024";
                    userCode="ap2062";*/
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("FilterValue", userCode + "," + InTime);
                    jsonObject.addProperty("FilterOption", "GetTravelReport");
                    getList(jsonObject);
                } catch (Exception exception) {
                    Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }
    }

    void getSystemDistance() {
        try {
            Date entrydate = new Date();
            String InTime1 = new SimpleDateFormat("yyyy-MM-dd").format(entrydate);
            int endTravelCount = mDatabase.getActivityDoneCount("End Travel", InTime1);
            int systemDistanceCount = mDatabase.getActivityDoneCount("System Distance", InTime1);
            Toast.makeText(context, systemDistanceCount + " and " + endTravelCount, Toast.LENGTH_SHORT).show();


            if (endTravelCount > 0 && systemDistanceCount == 0) {
                Toast.makeText(context, "Calling Distance API", Toast.LENGTH_SHORT).show();
                String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String searchQuery12 = "select  Coordinates  from  GTVTravelActivityData where ActivityName!='System Distance' and  ActivityName!='Focus Village Tagging'  and ActivityDt like '%" + curdate + "%'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery12, null);
                int count = cursor.getCount();
                if (count > 0) {
                    JsonArray jsonArray;

                    String searchQuery = "select  Coordinates  from  GTVTravelActivityData where ActivityName!='System Distance' and  ActivityName!='Focus Village Tagging' and ActivityDt like '%" + curdate + "%'";
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
            call = RetrofitClient.getInstance().getMyApi().GetTravelGTVReportNew(jsonObject);
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
                                    tbl.removeAllViews();
                                    if (root.Result.kAActivityModel != null) {
                                        String KANAME="";
                                        //GTVKAName = root.Result.kAModel.KAName + " HQ-" + root.Result.kAModel.KAHQ;
                                        if(root.Result.kAActivityModel.KACode!=null) {
                                            KANAME += root.Result.kAActivityModel.KACode;
                                        }

                                        if(root.Result.kAActivityModel.KAName!=null) {
                                            KANAME += root.Result.kAActivityModel.KAName;
                                        }

                                        if(root.Result.kAActivityModel.KAHeadQuartor!=null) {
                                            KANAME +=" HQ-"+ root.Result.kAActivityModel.KAHeadQuartor+"";
                                        }
                                        GTVKAName=KANAME;
                                        if (root.Result.kAActivityModel.KmByKA != null) {
                                            GTVKAKM = root.Result.kAActivityModel.KmByKA + "KM";
                                        } else {
                                            GTVKAKM = "-";
                                        }

                                        if (root.Result.kAActivityModel.Attendance != null) {
                                            GTVAttendance = root.Result.kAActivityModel.Attendance;
                                        } else {
                                            GTVAttendance = "-";
                                        }


                                        if (root.Result.kAActivityModel.KmBySystem != null) {
                                            GTVSystemKM = root.Result.kAActivityModel.KmBySystem + " KM";
                                        } else {
                                            GTVSystemKM = " -";
                                        }

                                        if (root.Result.kAActivityModel.StartTravelTime != null) {
                                            GTVStartTravelTime = root.Result.kAActivityModel.StartTravelTime;
                                        }

                                        if (root.Result.kAActivityModel.EndTravelTime != null) {
                                            GTVEndTravelTime = root.Result.kAActivityModel.EndTravelTime;
                                        }

                                        if (root.Result.kAActivityModel.GTV1VillageName != null) {
                                            GTVVillage1 = root.Result.kAActivityModel.GTV1VillageName;
                                        }

                                        if (root.Result.kAActivityModel.GTV1PunchInTime != null) {
                                            GTVVillage1PUNCHINTIME = root.Result.kAActivityModel.GTV1PunchInTime;
                                        }

                                        if (root.Result.kAActivityModel.GTV1PunchOutTime != null) {
                                            GTVVillage1PUNCHOUTTIME = root.Result.kAActivityModel.GTV1PunchOutTime;
                                        }
                                        if (root.Result.kAActivityModel.GTV1TimeSpent != null) {
                                            GTVVillage1TIMESpent = root.Result.kAActivityModel.GTV1TimeSpent;
                                        }
                                        if (root.Result.kAActivityModel.GTV2VillageName != null) {
                                            GTVVillage2 = root.Result.kAActivityModel.GTV2VillageName;
                                        }
                                        if (root.Result.kAActivityModel.GTV2PunchInTime != null) {
                                            GTVVillage2PUNCHINTIME = root.Result.kAActivityModel.GTV2PunchInTime;
                                        }
                                        if (root.Result.kAActivityModel.GTV2PunchOutTime != null) {
                                            GTVVillage2PUNCHOUTTIME = root.Result.kAActivityModel.GTV2PunchOutTime;
                                        }
                                        if (root.Result.kAActivityModel.GTV2TimeSpent != null) {
                                            GTVVillage2TIMESpent = root.Result.kAActivityModel.GTV2TimeSpent;
                                        }


                                    } else {
                                        GTVKAName = "-";
                                    }



                                    if (root.getResult().gTV1ActivityModels != null) {
                                        GTVVillage1Activities = "";
                                        for (int i = 0; i < root.getResult().gTV1ActivityModels.size(); i++) {
                                            GTV1ActivityModel activityModel = root.getResult().gTV1ActivityModels.get(i);
                                            if (activityModel.ActivityName.toLowerCase().contains("punch")) {
                                               // GTVVillage1 = activityModel.VillageName;
                                                continue;
                                            }
                                            if (activityModel.ActivityName.toLowerCase().contains("system")) {
                                                continue;
                                            }
                                            GTVVillage1Activities += activityModel.ActivityName + "(" + activityModel.No_of_activities + "), ";
                                        }
                                    } else {
                                        GTVVillage1Activities = "No Activity Found.";
                                    }
                                    if (root.getResult().gTV2ActivityModels != null) {
                                        GTVVillage2Activities = "";
                                        for (int i = 0; i < root.getResult().gTV2ActivityModels.size(); i++) {
                                            GTV2ActivityModel activityModel = root.getResult().gTV2ActivityModels.get(i);
                                            if (activityModel.ActivityName.toLowerCase().contains("punch")) {
                                             //   GTVVillage2 = activityModel.VillageName;
                                                continue;
                                            }
                                            GTVVillage2Activities += activityModel.ActivityName + "(" + activityModel.No_of_activities + "), ";
                                        }
                                    } else {
                                        GTVVillage2Activities = "No Activity Found.";
                                    }

                                    if (root.getResult().MarketActivityModel != null) {
                                        GTV2Market1Activities = "";
                                        for (int i = 0; i < root.getResult().MarketActivityModel.size(); i++) {
                                            MarketModel activityModel = root.getResult().MarketActivityModel.get(i);
                                            GTV2Market1Activities += activityModel.ActivityName + "(" + activityModel.No_of_activities + "), ";
                                        }

                                    } else {
                                        GTV2Market1Activities = "No Activity Found.";
                                    }
                                    PrepareReport();

                                } catch (Exception e) {
                                    new AlertDialog.Builder(context)
                                            .setMessage("Something went wrong.1234" + e.getMessage())
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
                            }else
                            {
                                 new AlertDialog.Builder(context)
                                         .setMessage(""+root.Message)
                                         .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                 dialogInterface.dismiss();
                                             }
                                         }).show();
                            }
                        } catch (NullPointerException e) {
                            PrepareReport();
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            PrepareReport();
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        new AlertDialog.Builder(context)
                                .setMessage("Something Went Wrong.")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
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
  /*                  "<tr> " +
                    "<td colspan='2'><b>Other Than FV Activities</b></td>" +
                    "<td colspan='4'>" + GTV1Market1Activities + "</td> " +
                    "</tr>" +*/
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
                    "<td colspan='6' style='text-align:center;background-color:FloralWhite;'><b>Other Than FV Activities</b></td> " +
                    "</tr> " +
                    "<tr> " +
                //    "<td colspan='2'><b>Other Than FV Activities</b></td>" +
                    "<td colspan='6'>" + GTV2Market1Activities + "</td> " +
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
        public String getActivityType() {
            return ActivityType;
        }

        public void setActivityType(String activityType) {
            ActivityType = activityType;
        }

        public String getNo_of_activities() {
            return No_of_activities;
        }

        public void setNo_of_activities(String no_of_activities) {
            No_of_activities = no_of_activities;
        }

        public String KACode;
        public String ActivityType;
        public String GTVType;


        public String ActivityName;
        public String No_of_activities;

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


        public String getActivityType() {
            return ActivityType;
        }

        public void setActivityType(String activityType) {
            ActivityType = activityType;
        }

        public String getNo_of_activities() {
            return No_of_activities;
        }

        public void setNo_of_activities(String no_of_activities) {
            No_of_activities = no_of_activities;
        }

        public String KACode;

        public String GTVType;
        public String ActivityType;
        public String ActivityName;
        public int ActivityCount;
        public String VillageName;
        public String No_of_activities;
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
        public String getNo_of_activities() {
            return No_of_activities;
        }

        public void setNo_of_activities(String no_of_activities) {
            No_of_activities = no_of_activities;
        }
        public String getActivityType() {
            return ActivityType;
        }

        public void setActivityType(String activityType) {
            ActivityType = activityType;
        }



        public String KACode;
        public String ActivityType;
        public String GTVType;
        public String ActivityName;
        public String No_of_activities;

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
        public KAActivityModel getkAActivityModel() {
            return kAActivityModel;
        }

        public void setkAActivityModel(KAActivityModel kAActivityModel) {
            this.kAActivityModel = kAActivityModel;
        }
        public ArrayList<MarketModel> getMarketActivityModel() {
            return MarketActivityModel;
        }

        public void setMarketActivityModel(ArrayList<MarketModel> marketActivityModel) {
            MarketActivityModel = marketActivityModel;
        }
        public KAActivityModel kAActivityModel;
        public ArrayList<GTV1ActivityModel> gTV1ActivityModels;
        public ArrayList<GTV2ActivityModel> gTV2ActivityModels;
        public ArrayList<MarketModel> MarketActivityModel;
        public ArrayList<MarketModel> marketGTV1ActivityModels;
        public ArrayList<MarketModel> marketGTV2ActivityModels;
        public MyTravelKMModel myTravelKMModel;
        public KAKMModel kAKMModel;
        public KAModel kAModel;


        public MyTravelModel myTravelModel;
        public GTV1Model gTV1Model;
        public GTV2Model gTV2Model;
        public GetAttendanceModel GetAttendanceModel;





    }

    public class KAActivityModel{
         String KACode;//": "mh248",
        String StartTravelTime;//": "04:29 PM",
        String EndTravelTime;//": "04:35 PM",

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

        public String getKmByKA() {
            return KmByKA;
        }

        public void setKmByKA(String kmByKA) {
            KmByKA = kmByKA;
        }

        public String getKmBySystem() {
            return KmBySystem;
        }

        public void setKmBySystem(String kmBySystem) {
            KmBySystem = kmBySystem;
        }

        public String getAttendance() {
            return Attendance;
        }

        public void setAttendance(String attendance) {
            Attendance = attendance;
        }

        public String getGTV1VillageName() {
            return GTV1VillageName;
        }

        public void setGTV1VillageName(String GTV1VillageName) {
            this.GTV1VillageName = GTV1VillageName;
        }

        public String getGTV1PunchInTime() {
            return GTV1PunchInTime;
        }

        public void setGTV1PunchInTime(String GTV1PunchInTime) {
            this.GTV1PunchInTime = GTV1PunchInTime;
        }

        public String getGTV1PunchOutTime() {
            return GTV1PunchOutTime;
        }

        public void setGTV1PunchOutTime(String GTV1PunchOutTime) {
            this.GTV1PunchOutTime = GTV1PunchOutTime;
        }

        public String getGTV1TimeSpent() {
            return GTV1TimeSpent;
        }

        public void setGTV1TimeSpent(String GTV1TimeSpent) {
            this.GTV1TimeSpent = GTV1TimeSpent;
        }

        public String getGTV2VillageName() {
            return GTV2VillageName;
        }

        public void setGTV2VillageName(String GTV2VillageName) {
            this.GTV2VillageName = GTV2VillageName;
        }

        public String getGTV2PunchInTime() {
            return GTV2PunchInTime;
        }

        public void setGTV2PunchInTime(String GTV2PunchInTime) {
            this.GTV2PunchInTime = GTV2PunchInTime;
        }

        public String getGTV2PunchOutTime() {
            return GTV2PunchOutTime;
        }

        public void setGTV2PunchOutTime(String GTV2PunchOutTime) {
            this.GTV2PunchOutTime = GTV2PunchOutTime;
        }

        public String getGTV2TimeSpent() {
            return GTV2TimeSpent;
        }

        public void setGTV2TimeSpent(String GTV2TimeSpent) {
            this.GTV2TimeSpent = GTV2TimeSpent;
        }

        String KmByKA;//": "500.00",
        String KmBySystem;//": "0.00",
        String Attendance;//": "0.0",
        String GTV1VillageName;//": "",
        String GTV1PunchInTime;//": "",
        String GTV1PunchOutTime;//": "",
        String GTV1TimeSpent;//": "",
        String GTV2VillageName;//": "CHAHARDI",
        String GTV2PunchInTime;//": "04:30 PM",
        String GTV2PunchOutTime;//": "04:34 PM",
        String GTV2TimeSpent;//": "00:04"

        public String getKAName() {
            return KAName;
        }

        public void setKAName(String KAName) {
            this.KAName = KAName;
        }

        public String getKAHeadQuartor() {
            return KAHeadQuartor;
        }

        public void setKAHeadQuartor(String KAHeadQuartor) {
            this.KAHeadQuartor = KAHeadQuartor;
        }

        String KAName;//": "PRAVIN NARAYAN CHAUDHARI",
        String KAHeadQuartor;//": "CHOPADA",


    }
    public class GetAttendanceModel {
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

        public ActivityTravelReportGTVNew.Result getResult() {
            return Result;
        }

        public void setResult(ActivityTravelReportGTVNew.Result result) {
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

                String searchQuery = "select id,ActivityId,KACode,GTVType,ActivityName,ActivityType,ActivityDt,VillageCode,VillageName,LastCoordinates,Coordinates,GTVActivityKM,AppVersion,Remark,isSynced,RefrenceId,ActualKM,DistanceFromPunchKm,Attendance,TimeSpend,Info1,Info2,Info3 from GTVTravelActivityData where isSynced=0";
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
            Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
            JSONObject jsonObjectResult = new JSONObject(result.trim());
            if (jsonObjectResult.getBoolean("ResultFlag") && jsonObjectResult.getString("status").toLowerCase().equals("success")) {
                mDatabase.UpdateStatus("Update GTVTravelActivityData set isSynced=1 where isSynced=0");
                new AlertDialog.Builder(context)
                        .setMessage(jsonObjectResult.getString("Comment"))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            } else {
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
        if (CommonUtil.addGTVActivity(context, "5555", "System Distance", "0-0", "System Punch Out", "GTV", "" + result,0.0)) {
            uploadGtvTravelData();
        }
    }
}