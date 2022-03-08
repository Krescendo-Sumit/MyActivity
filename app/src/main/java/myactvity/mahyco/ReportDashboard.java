package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.WebService;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.AnimationItem;
import myactvity.mahyco.utils.ItemOffsetDecoration;
import myactvity.mahyco.utils.ReportDashBoardAdapter;

public class ReportDashboard extends AppCompatActivity {
    public Button btnshort,btndetailreport,btnsaleorderrpt,btnWeeklyRpt,btnretaileranddistributor,btnPVAReport,btnactivityProgress,btnInnovationrpt;
    ProgressDialog dialog;

    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    public WebService wx;
    TextView lblmsg;
    String returnstring ;
    public String MDOurlpath="";
    Config config;
    SharedPreferences sp;
    RecyclerView gridView;
    ArrayList<String> mlist = new ArrayList<>();
    ArrayList<String> mlist2 = new ArrayList<>();
    private AnimationItem[] mAnimationItems;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_dashboard);
        getSupportActionBar().hide(); //<< this
        btnshort = (Button) findViewById(R.id.btnshort);
        btndetailreport = (Button) findViewById(R.id.btndetailreport);
        btnWeeklyRpt = (Button) findViewById(R.id.btnWeeklyRpt);
        btnPVAReport = (Button) findViewById(R.id.btnPVAReport);
        btnactivityProgress = (Button) findViewById(R.id.btnactivityProgress);
        btnInnovationrpt= (Button) findViewById(R.id.btnInnovationrpt);
        btnretaileranddistributor=(Button) findViewById(R.id.btnretaileranddistributor);
        btnsaleorderrpt=(Button) findViewById(R.id.btnsaleorderrpt);
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        MDOurlpath=cx.MDOurlpath;
        wx=new WebService();
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        lblmsg=(TextView)findViewById(R.id.lblmsg) ;
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sp = getApplicationContext().getSharedPreferences("MyPref", 0);
        logReportData();
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setList();
        mAnimationItems = new AnimationItem[]{
                new AnimationItem("Slide from bottom", R.anim.grid_layout_animation_from_bottom)};

        gridView = (RecyclerView) findViewById(R.id.gridView);
        gridView.setLayoutManager(new GridLayoutManager(this, 1));
       /* btnshort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportDashboard.this,ReportDisplay.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);


            }
        });
        btndetailreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportDashboard.this, DetailSummaryReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
        btnWeeklyRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ReportType" ,"WeeklyPlan");
                startActivity(intent);


            }
        });
        btnPVAReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ReportType" ,"PlannedVsActualSummary");
                startActivity(intent);


            }
        });
        btnactivityProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ReportType" ,"ActivityProgress");
                startActivity(intent);


            }
        });
        btnInnovationrpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //msclass.showMessage("On Working");

                Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ReportType" ,"Innovation");
               startActivity(intent);


            }
        });

        btnretaileranddistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //msclass.showMessage("On Working");

                Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ReportType" ,"RetailerandDistributor");
                startActivity(intent);


            }
        });
        btnsaleorderrpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msclass.showMessage("On Working");

                // Intent intent = new Intent(ReportDashboard.this, MDOweeklyPlanReport.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("ReportType" ,"RetailerandDistributor");
                // startActivity(intent);


            }
        });*/
         showGrid();
        runLayoutAnimation(gridView, mAnimationItems[0]);
    }
    private void showGrid() {
        //Adding adapter to gridview
        try {
            final Context context = gridView.getContext();
            final int spacing = 4;


            gridView.setAdapter(new ReportDashBoardAdapter(this, mlist,mlist2));//,prgmImages));
            gridView.addItemDecoration(new ItemOffsetDecoration(spacing));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setList() {
        mlist.clear();
        mlist.add("WEEKLY VISIT PLAN REPORT ");
        mlist.add("PLANNED VS ACTUAL REPORT");
        mlist.add("ACTIVITY PROGRESS REPORT");
        mlist.add("MY TRAVEL");
        mlist.add("RETAILER AND DISTRIBUTOR MAPPING AND TAGGING");
        mlist.add("SALES ORDER REPORT");
        mlist.add("DEMO AND MODEL PLOT VISIT REPORT");

        mlist2.clear();
        mlist2.add("weekly");
        mlist2.add("planned");
        mlist2.add("activity");
        mlist2.add("mytravel");
        mlist2.add("retaileranddistributor");
        mlist2.add("saleorder");
        mlist2.add("demovisit");

    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void logReportData(){
        if(sp!=null){
            String userId="", displayName="";
            if (sp.getString("UserID", null) != null && sp.getString("Displayname", null) != null ){
                userId = sp.getString("UserID", "");
                displayName = sp.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callReportEvent(userId,displayName);
            }
        }
    }
   }
