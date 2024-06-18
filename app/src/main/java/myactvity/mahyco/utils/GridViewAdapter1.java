package myactvity.mahyco.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mahyco.customercomplaint.CCFFirstActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.ActivityBeSurvey;
import myactvity.mahyco.ActivityBeSurveyVCF;
import myactvity.mahyco.ActivityKisanClub;
import myactvity.mahyco.ActivityRetailerSurvey;
import myactvity.mahyco.ActivityVOFP;
import myactvity.mahyco.BCFCallTBM;
import myactvity.mahyco.DemoModelVisit;
import myactvity.mahyco.DownloadMasterdata;
import myactvity.mahyco.FirebaseAnalyticsHelper;
import myactvity.mahyco.HDPSCouponDashboardActivity;
import myactvity.mahyco.HDPSNewActivity;
import myactvity.mahyco.Innovation;
import myactvity.mahyco.LoginActivity;
import myactvity.mahyco.MyActivityRecordingNew;
import myactvity.mahyco.MyFieldActvity;
import myactvity.mahyco.MyTravel;
import myactvity.mahyco.NewUserRegistration;
import myactvity.mahyco.OfflineActivity;
import myactvity.mahyco.R;
import myactvity.mahyco.ReportDashboard;
import myactvity.mahyco.RetailerandDistributorTag;
import myactvity.mahyco.SalesServiceActivity;
import myactvity.mahyco.SalesWebData;
import myactvity.mahyco.SamruddhaKisanValidation;
import myactvity.mahyco.TempUserDashboard;
import myactvity.mahyco.UploadData;
import myactvity.mahyco.Utility;
import myactvity.mahyco.VocpvegetablewebviewActivity;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.couponDashboard;
import myactvity.mahyco.daslogin;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.leaveapply;
import myactvity.mahyco.mdoSurvey;
import myactvity.mahyco.pogDashboard;
import myactvity.mahyco.saleorderpending;
import myactvity.mahyco.voiceofchanelpartner_be;
import myactvity.mahyco.voiceofcustomerDashboard;
import myactvity.mahyco.saleOrderDashboard;
import myactvity.mahyco.wawActivity;


/**
 * Created by Belal on 12/22/2015.
 */
public class GridViewAdapter1 extends RecyclerView.Adapter<GridViewAdapter1.ViewHolder> {

    String[] result;
    String[] imageId;
    String[] ActivityName;
    List fraglist, acty;
    String From;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private SqliteDatabase mDatabase;
    Messageclass msclass;
    private static LayoutInflater inflater = null;
    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;

    /* public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names){
         //Getting all the values
         this.context = context;
         this.images = images;
         this.names = names;
     } */
    public GridViewAdapter1(Context context, String[] prgmNameList, String[] ImageList, String[] ActivityName, String From, List fraglist) //, int[] prgmImages)
    {
        // TODO Auto-generated constructor stub
        this.result = prgmNameList;
        this.context = context;
        this.imageId = ImageList;
        this.From = From;
        this.ActivityName = ActivityName;
        mDatabase = SqliteDatabase.getInstance(context);
        config = new Config(context); //Here the context is passing
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        msclass = new Messageclass(context);
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

    }

    /*@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }*/

    @Override
    public GridViewAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.programlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewAdapter1.ViewHolder holder, final int position) {
        View rowView;
        // rowView = inflater.inflate(R.layout.programlist, parent, false);
        try {
            //  Holder holder=new Holder();
            //  holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            // holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
            /*Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/JosefinSans-Regular.ttf");
            holder.tv.setTypeface(face);*/
            Utility.setRegularFont(holder.tv, context);
            holder.tv.setText(result[position]);
            //  Toast.makeText(context,"4554" , Toast.LENGTH_LONG).show();
            if (From == "UserHome") {
                //  holder.tv.setTextColor(Color.parseColor("#000000"));
                // holder.tv.setBackgroundResource(android.R.color.transparent);
                // holder.tv.setTextSize(15);
                String[] colorlist = {"#5cffba", "#ff6a72", "#dd99ff", "#ff7715", "#ffcf2d"};
                // holder.img.setBackgroundColor(Color.parseColor(colorlist[position]));
                holder.tv.setTextColor(Color.parseColor("#000000"));
                holder.tv.setBackgroundResource(android.R.color.transparent);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(100, 100);
                // holder.img.setLayoutParams(parms);  file:///android_asset/Images/
                Picasso.with(context).load("file:///android_asset/DashboardImages/" + imageId[position])//.transform(new TransformationCircle()) // resizes the image to these dimensions (in pixel)
                        // .centerCrop()
                        .into(holder.img);
                holder.card.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        AppConstant.packagename = context.getApplicationContext().getPackageName();

                        try {

                            if (ActivityName[position].toString() == "Myactivity") {

                                // TBM and MDO it can login Opfline form
                                String myTable = "Table1";//Set name of your table
                                String searchQuery = "SELECT  *  FROM VillageLevelMaster";
                                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                if (cursor.getCount() > 0) {
                                    cursor.close();
                                    if (pref.getString("unit", null).contains("RCTEMP")) {
                                        intent = new Intent(context.getApplicationContext(), TempUserDashboard.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    } else {
                                        if (pref.getString("RoleID", null).equals("0") ||
                                                pref.getString("RoleID", null) == null ||
                                                pref.getString("RoleID", null).equals("") ||
                                                pref.getString("RoleID", null).equals("4")) {
                                            intent = new Intent(context.getApplicationContext(), MyFieldActvity.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        } else {  // intent = new Intent(context.getApplicationContext(), MyFieldActvityonline.class);
                                            intent = new Intent(context.getApplicationContext(), OfflineActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }
                                    }

                                } else {
                                    msclass.showMessage("Master data not available. \n" +
                                            "Please download the master data.");
                                }


                            }

                            if (ActivityName[position].toString() == "MyTravel") {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date d = new Date();
                                String strdate = dateFormat.format(d);
                                if (pref.getString("RoleID", null).equals("0")) {
                                    // Comment for Online  off now (without Comment is  offline)
                                    // if (mDatabase.getrowcount("select * from couponMaster where entryDate ='"+strdate+"'") > 0) {

                                    intent = new Intent(context.getApplicationContext(), MyTravel.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                    // }
                                    // else
                                    // {
                                    //   msclass.showMessage("Coupon list not available ,Please download master data");
                                    // }
                                } else {
                                    intent = new Intent(context.getApplicationContext(), MyTravel.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }

                            if (ActivityName[position].toString() == "RetailerTag") {
                                intent = new Intent(context.getApplicationContext(), RetailerandDistributorTag.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].toString() == "VoiceOfCustomer") {
                                //intent = new Intent(context.getApplicationContext(), demandassessmentsurvey.class);
                                intent = new Intent(context.getApplicationContext(), voiceofcustomerDashboard.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("FIELD VISITS")) {

                                // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                intent = new Intent(context.getApplicationContext(), DemoModelVisit.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].toString() == "UploadData") {
                                intent = new Intent(context.getApplicationContext(), UploadData.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "DownloadData") {
                                intent = new Intent(context.getApplicationContext(), DownloadMasterdata.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "Innovation") {

                                String myTable = "Table1";//Set name of your table
                                String searchQuery = "SELECT  *  FROM VehicleMaster";
                                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                if (cursor.getCount() > 0) {
                                    intent = new Intent(context.getApplicationContext(), Innovation.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                } else {
                                    msclass.showMessage("Innovation day location not  found. \n" +
                                            "Please download the master data.");
                                }
                            }

                            if (ActivityName[position].toString() == "Report") {

                                // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                intent = new Intent(context.getApplicationContext(), ReportDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "WOW") {

                                intent = new Intent(context.getApplicationContext(), wawActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "Close") {
                                editor.clear();
                                editor.putString("UserID", null);
                                editor.commit();
                                intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "ApproveOrder") {

                                // 2 RBM  5 ZBM 7ZMM
                                if (pref.getString("RoleID", null).equals("2") ||
                                        pref.getString("RoleID", null).equals("5") ||
                                        pref.getString("RoleID", null).equals("7")) {
                                    intent = new Intent(context.getApplicationContext(), saleorderpending.class);
                                    //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //intent.putExtra("Pagetype", "PlotVisit");
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Your are not authorized to access this tab.", Toast.LENGTH_SHORT).show();
                                }

                            }
                            if (ActivityName[position].toString() == "POG") {

                                // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                intent = new Intent(context.getApplicationContext(), pogDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "LEAVE") {
                                intent = new Intent(context.getApplicationContext(), leaveapply.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }


                            if (ActivityName[position].toString() == "Sales Service") {
                                intent = new Intent(context.getApplicationContext(), SalesServiceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }


                            if (ActivityName[position].toString() == "COUPON") {

                                // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                intent = new Intent(context.getApplicationContext(), couponDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].toString() == "Username") {
                                intent = new Intent(context.getApplicationContext(), NewUserRegistration.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].equals("Retailer Survey")) {

                                // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                intent = new Intent(context.getApplicationContext(), ActivityRetailerSurvey.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);

                            }

                            if (ActivityName[position].contains("BCFCALLTBM")) {


                                intent = new Intent(context.getApplicationContext(), BCFCallTBM.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("Kisan Club (MAGiK)")) {


                                intent = new Intent(context.getApplicationContext(), ActivityKisanClub.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("BE-Survey")) {


                                intent = new Intent(context.getApplicationContext(), ActivityBeSurvey.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("VCF")) {


                                intent = new Intent(context.getApplicationContext(), ActivityBeSurveyVCF.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("samruddhakisanvalidation")) {


                                intent = new Intent(context.getApplicationContext(), SamruddhaKisanValidation.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("VEGVOCP")) {

                                intent = new Intent(context.getApplicationContext(), VocpvegetablewebviewActivity.class);
                                // intent= new Intent(context.getApplicationContext(), DSActivity.class);
                                intent.putExtra("FormName", "RetailerForm");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("VEGVOCP1")) {

                                intent = new Intent(context.getApplicationContext(), VocpvegetablewebviewActivity.class);
                                intent.putExtra("FormName", "VOFPSurvey");
                                // intent= new Intent(context.getApplicationContext(), DSActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("DAS")) {
                                // Toast.makeText(context, "Not Active", Toast.LENGTH_SHORT).show();
                                intent = new Intent(context.getApplicationContext(), daslogin.class);
                                // intent= new Intent(context.getApplicationContext(), DSActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("VOF")) {


                                intent = new Intent(context.getApplicationContext(), ActivityVOFP.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("MDOSURVEY")) {


                                intent = new Intent(context.getApplicationContext(), mdoSurvey.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("MY ACTIVITY RECORDING")) {
                                intent = new Intent(context.getApplicationContext(), MyActivityRecordingNew.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            if (ActivityName[position].contains("websales")) {


                                intent = new Intent(context.getApplicationContext(), SalesWebData.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("VCP")) {
                                Toast.makeText(context, "Not Active", Toast.LENGTH_SHORT).show();

//                                intent= new Intent(context.getApplicationContext(), voiceofchanelpartner_be.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(intent);
                            }

                            if (ActivityName[position].contains("HDPSCouponDashboardActivity")) {


                                intent = new Intent(context.getApplicationContext(), HDPSCouponDashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
//  Adding this code for CCF 2.0 Form For RBM and TBM .
                            if (ActivityName[position].contains("CCF")) {


                                intent = new Intent(context, CCFFirstActivity.class);
                                intent.putExtra("ccfUserId", "1");
                                intent.putExtra("ccfToken", "addd");
                                intent.putExtra("ccfContactNo", "9420181669");
                                intent.putExtra("ccfTBMOrRBMCode", pref.getString("UserID", null));
//ccfUserRoleID == 2 RBM, ccfUserRoleID == 4 TBM ,
// ccfUserRoleID == 5 ZBM, ccfUserRoleID == 7 ZMM
                                intent.putExtra("ccfUserRoleID", pref.getString("RoleID", null));
                                //    Toast.makeText(context, pref.getString("UserID", null)+" / "+pref.getString("RoleID", null), Toast.LENGTH_SHORT).show();
                                Log.i("User Details", pref.getString("UserID", null) + " / " + pref.getString("RoleID", null));

                                context.startActivity(intent);
                            }

                            if (ActivityName[position].toString() == "SalesOrder") {
                                // TBM and MDO it can login Opfline form

                                /* if (pref.getString("RoleID",null).equals("4")) {
                                    intent = new Intent(context.getApplicationContext(), orderfromTBM.class);
                                    //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //intent.putExtra("Pagetype", "PlotVisit");
                                    context.startActivity(intent);
                                }
                                else
                                {   // RBM
                                    if (pref.getString("RoleID",null).equals("2")) {
                                        intent = new Intent(context.getApplicationContext(), orderfromRBM.class);
                                        //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //intent.putExtra("Pagetype", "PlotVisit");
                                        context.startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Your are not authorized to access this tab.", Toast.LENGTH_SHORT).show();
                                    }
                                }*/

                                // This code is commented to redirect to Glass run application

                             /*   intent = new Intent(context.getApplicationContext(), saleOrderDashboard.class);
                                //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //intent.putExtra("Pagetype", "PlotVisit");
                                context.startActivity(intent);*/

                                // Glass run application redirection code

                                if (pref.getString("unit", null).trim().contains("VCBU")) {
                                    intent = new Intent(context.getApplicationContext(), saleOrderDashboard.class);
                                    //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //intent.putExtra("Pagetype", "PlotVisit");
                                    context.startActivity(intent);
                                } else {

                                    try {
                                        intent = context.getPackageManager().getLaunchIntentForPackage("com.disrptiv.glassrun.ordermanagement");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(context, "Please Install MCart application", Toast.LENGTH_SHORT).show();
                                        final String appPackageName = "com.disrptiv.glassrun.ordermanagement"; // getPackageName() from Context or Activity object
                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }catch(Exception e2)
                                        {

                                        }
                                    }
                                }
                            }


                        } catch (Exception ex) {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                            editor.clear();
                            editor.putString("UserID", null);
                            editor.commit();
                            intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return ActivityName.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }
   /* @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.programlist, parent, false);
        try
        {
            Holder holder=new Holder();
            holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
            *//*Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/JosefinSans-Regular.ttf");
            holder.tv.setTypeface(face);*//*
            Utility.setRegularFont(holder.tv,context);
            holder.tv.setText(result[position]);
          //  Toast.makeText(context,"4554" , Toast.LENGTH_LONG).show();
            if (From =="UserHome") {
               //  holder.tv.setTextColor(Color.parseColor("#000000"));
               // holder.tv.setBackgroundResource(android.R.color.transparent);
               // holder.tv.setTextSize(15);
                String [] colorlist={"#5cffba","#ff6a72","#dd99ff","#ff7715","#ffcf2d"};
               // holder.img.setBackgroundColor(Color.parseColor(colorlist[position]));
                holder.tv.setTextColor(Color.parseColor("#000000"));
                holder.tv.setBackgroundResource(android.R.color.transparent);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(100,100);
               // holder.img.setLayoutParams(parms);  file:///android_asset/Images/
                Picasso.with(context).load("file:///android_asset/DashboardImages/" + imageId[position])//.transform(new TransformationCircle()) // resizes the image to these dimensions (in pixel)
                       // .centerCrop()
                        .into(holder.img);
                         rowView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                          Intent intent;
                            try {

                                if (ActivityName[position].toString() == "Myactivity") {
                                    // TBM and MDO it can login Opfline form

                                    String myTable = "Table1";//Set name of your table
                                    String searchQuery = "SELECT  *  FROM VillageLevelMaster";
                                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                    if(cursor.getCount() >0) {
                                        cursor.close();
                                        if (pref.getString("RoleID",null).equals("0")||
                                                pref.getString("RoleID",null)==null ||
                                                pref.getString("RoleID",null).equals("") ||
                                                pref.getString("RoleID",null).equals("4")) {
                                            intent = new Intent(context.getApplicationContext(), MyFieldActvity.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }
                                        else
                                        {  // intent = new Intent(context.getApplicationContext(), MyFieldActvityonline.class);
                                             intent = new Intent(context.getApplicationContext(), OfflineActivity.class);

                                            if (config.NetworkConnection()) {
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //intent.putExtra("Pagetype", "PlotVisit");
                                                context.startActivity(intent);
                                            }
                                            else
                                            {
                                                msclass.showMessage("Internet connection not available ,\n" +
                                                        "please check internet connection.");
                                            }

                                        }

                                    }
                                    else
                                    {
                                        msclass.showMessage("Master data not available. \n" +
                                                "Please download the master data.");
                                    }


                                }

                                if (ActivityName[position].toString() == "MyTravel") {
                                    intent= new Intent(context.getApplicationContext(),MyTravel.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }

                                if (ActivityName[position].toString() == "RetailerTag") {
                                    intent= new Intent(context.getApplicationContext(),RetailerTag.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Username") {
                                     intent = new Intent(context.getApplicationContext(), NewUserRegistration.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "UploadData") {
                                    intent= new Intent(context.getApplicationContext(),UploadData.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "DownloadData") {
                                    intent= new Intent(context.getApplicationContext(),DownloadMasterdata.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Innovation") {

                                    String myTable = "Table1";//Set name of your table
                                    String searchQuery = "SELECT  *  FROM VehicleMaster";
                                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                     if(cursor.getCount() >0) {
                                         intent = new Intent(context.getApplicationContext(), Innovation.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                         context.startActivity(intent);
                                     }
                                     else
                                     {
                                         msclass.showMessage("Innovation day location not  found. \n" +
                                                 "Please download the master data.");
                                     }
                                }

                                if (ActivityName[position].toString() == "Report") {

                                   // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                    intent= new Intent(context.getApplicationContext(),ReportDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Close") {
                                    editor.clear();
                                    editor.putString("UserID",null);
                                    editor.commit();
                                    intent= new Intent(context.getApplicationContext(),LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "SalesOrder") {
                                    // TBM and MDO it can login Opfline form

                                        if (pref.getString("RoleID",null).equals("4")) {
                                            intent = new Intent(context.getApplicationContext(), orderfromTBM.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }
                                        else
                                        {
                                            intent = new Intent(context.getApplicationContext(), saleorderpending.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }


                                }


                           }
                            catch(Exception ex)
                            {
                                Toast.makeText(context,ex.getMessage() , Toast.LENGTH_LONG).show();
                                editor.clear();
                                editor.putString("UserID",null);
                                editor.commit();
                                intent= new Intent(context.getApplicationContext(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                    }
                });




            }








        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return rowView;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;
        TextView name;
        TextView gstpercent, price;
        EditText et_value;
        int ref = 0;
        ImageView img_minus;
        ImageView img_plus;
        LinearLayout lin_qty;
        ImageView img_sku;
        LinearLayout lin_add_to_cart;
        TextView add_cart_btn;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textView1);
            img = (ImageView) itemView.findViewById(R.id.imageView1);
            card = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    private void logIMPFData() {
        if (pref != null) {
            String userId = "", displayName = "";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null) {
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(context).callIMPFEvent(userId, displayName);
            }
        }
    }
}
