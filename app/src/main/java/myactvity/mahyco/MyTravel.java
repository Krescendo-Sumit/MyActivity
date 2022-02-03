package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

import static android.content.ContentValues.TAG;



public class MyTravel extends AppCompatActivity {

    Button btnStarttravel, btnAddActivity, btnendtravel;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView lblwelcome,myTextProgress;
    public Spinner spDist, spTaluka, spVillage, spCropType, spProductName, spMyactvity,spComment;
    private Context context;
    private SqliteDatabase mDatabase;
    public CommonExecution cx;
    // TextView lblmyactvityrecord,lblfarmer;
    Config config;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    ProgressDialog dialog;
    private int[] tabIcons = {
            R.drawable.start,
            R.drawable.addtravel,
            R.drawable.end
    };
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    private Handler handler = new Handler();
    Messageclass msclass;
   // ScrollView container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);
        getSupportActionBar().hide(); //<< this
        context = this;
        try {
            cx = new CommonExecution(this);
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            preferences = getSharedPreferences("MyPref", 0);
            logTravelActivityData();
            editor = preferences.edit();

            progressBar = (ProgressBar) findViewById(R.id.myProgress);
            relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
            myTextProgress = (TextView) findViewById(R.id.myTextProgress);

            // container = (ScrollView) findViewById(R.id.container);
            spDist = (Spinner) findViewById(R.id.spDist);
            spTaluka = (Spinner) findViewById(R.id.spTaluka);
            spVillage = (Spinner) findViewById(R.id.spVillage);
            mDatabase = SqliteDatabase.getInstance(this);

            config = new Config(this); //Here the context is passing
            lblwelcome = (TextView) findViewById(R.id.lblwelcome);
            //  lblwelcome.setText("MDO NAME: "+preferences.getString("Displayname",null));
            // BindIntialData();
            mViewPager = (ViewPager) findViewById(R.id.viewcontainer);
            mViewPager.setOffscreenPageLimit(0);
            setupViewPager(mViewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            setupTabIcons();
            msclass = new Messageclass(this);
            btnStarttravel = (Button) findViewById(R.id.btnStarttravel);
            btnAddActivity = (Button) findViewById(R.id.btnAddActivity);
            btnendtravel = (Button) findViewById(R.id.btnendtravel);
            btnStarttravel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date d=new Date();
                    String strdate=dateFormat.format(d);
                    if (5 > 0)
                      //Online check coupon balance amount
                        try {
                            Intent intent = new Intent(MyTravel.this, starttravelnew.class);
                            //Intent intent = new Intent(context.getApplicationContext(), imgpick.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            // new UploadCouponDataServer("mdo_couponSchemeDownloadAndUpload", context).execute(cx.MDOurlpath).get();

                        } catch (Exception e) {
                            Log.d(TAG, "onClick: Booking ");
                        }


                }
            });
            btnAddActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                            Intent intent = new Intent(MyTravel.this, MyActivityRecordingNew.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);


                    } catch (Exception ex) {
                        //msclass.showMessage(ex.getMessage());
                    }

                }
            });
            btnendtravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {


                            Intent intent = new Intent(MyTravel.this, endTravelNew.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }

                    catch (Exception ex) {
                        ex.printStackTrace();
                        //msclass.showMessage(ex.getMessage());
                    }


                }
            });

            ImageView backButton = (ImageView)this.findViewById(R.id.backbtn);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e) {
            Log.d(TAG, "onLocationChanged: "+e.toString());
            e.printStackTrace();
            //  }
        }
         //MDO_TravelData();

        UploadData(); // new change -3-03-2021

       // new SyncMDOTravel_Async("").execute(); Comment  on 22-08-202
       // new SyncDataAsync_Async("mdo_photoupdate").execute(); 22 -08-2020

        // UploadaImage2("mdo_starttravel");
       // UploadaImage2("mdo_endtravel");


    }

    public void UploadData()
    {
        if (config.NetworkConnection()) {
            relPRogress.setVisibility(View.VISIBLE);

            progressBar.setIndeterminate(true);
            new Thread(new Runnable() {
                public void run() {

                    handler.post(new Runnable() {
                        public void run() {
                            new SyncMDOTravel_Async("").execute();
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBarVisibility();
                    }
                }
            }).start();
        }
    }
    private void progressBarVisibility() {
        relPRogress.setVisibility(View.GONE);
        //container.setClickable(true);
       // container.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void setupTabIcons() {
        try {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            // TextView title = (TextView)(tabLayout.getChildAt(0));
            //title.setTextSize(10);
            // title.setTextSize(...);

            // TextView x = (TextView) tabLayout.getTabAt()getTabWidget().getChildAt(0).findViewById(android.R.id.title);
            //x.setTextSize(25);
        }
            catch (Exception e) {
            Log.d(TAG, "setupTabIcons: "+e.toString());
            e.printStackTrace();
            //  }
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        try



        {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new starttravel(), "Start Travel");
        adapter.addFragment(new addtravel(), "Add Activity");
        adapter.addFragment(new endtravel(), "End Travel");
        // adapter.addFragment(new Nutrition(),  getResources().getString(R.string.Nutrition));
        viewPager.setAdapter(adapter);
       }
     catch (Exception e)
       {
        Log.d(TAG, "setupViewPager: "+e.toString());
        e.printStackTrace();
        //  }
      }
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private  void BindIntialData() {

        spDist.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0","SELECT DISTRICT"));
        gm.add(new GeneralMaster("1000","JALNA"));

        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDist.setAdapter(adapter);

        spTaluka.setAdapter(null);
        List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
        gm2.add(new GeneralMaster("0","SELECT TALUKA"));
        gm2.add(new GeneralMaster("01","JALNA"));
        ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTaluka.setAdapter(adapter2);


        spVillage.setAdapter(null);
        List<GeneralMaster> gm3 = new ArrayList<GeneralMaster>();
        gm3.add(new GeneralMaster("0","SELECT VILLAGE"));
        gm3.add(new GeneralMaster("01","BHOKARDHAN"));
        ArrayAdapter<GeneralMaster> adapter3 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVillage.setAdapter(adapter3);


    }

    public void MDO_TravelData()
    {
        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading. Please wait...");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                Cursor cursor = null;
                String searchQuery = "";
                int count = 0;
                searchQuery = "select * from mdo_starttravel where Status='0'";
                cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                count = cursor.getCount();
                cursor.close();

                searchQuery = "select * from mdo_endtravel where Status='0'";
                cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                count = count + cursor.getCount();
                cursor.close();

                searchQuery = "select * from mdo_addplace where Status='0'";
                cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                count = count + cursor.getCount();
                cursor.close();

                searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
                cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                count = count + cursor.getCount();
                cursor.close();

                if (count > 0) {
                    try {
                        //START
                        byte[] objAsBytes = null;//new byte[10000];
                        JSONObject object = new JSONObject();
                        try
                        {

                            searchQuery = "select * from mdo_starttravel where Status='0'";
                            object.put("Table1", mDatabase.getResults(searchQuery));
                            searchQuery = "select * from mdo_endtravel where Status='0'";
                            object.put("Table2", mDatabase.getResults(searchQuery));
                            searchQuery = "select * from mdo_addplace where Status='0'";
                            object.put("Table3", mDatabase.getResults(searchQuery));
                            searchQuery = "select * from mdo_Retaileranddistributordata where Status='0'";
                            object.put("Table4", mDatabase.getResults(searchQuery));
                            searchQuery = "select * from mdo_retailerproductdetail where Status='0'";
                            object.put("Table5", mDatabase.getResults(searchQuery));




                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            objAsBytes = object.toString().getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //str = new UploadUpdatedDataServer("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "").execute(cx.MDOurlpath).get();
                         syncTraveldata("MDO_TravelData", objAsBytes, Imagestring1, Imagestring2, ImageName, "",cx.MDOurlpath);

                        //End
                        if (str.contains("True")) {
                            // msclass.showMessage("Records Uploaded successfully");

                        } else {
                            //msclass.showMessage(str);
                           ;
                        }


                    } catch (Exception ex) {
                        // msclass.showMessage(ex.getMessage());


                    }
                } else {

                }
            }
        }
        catch (Exception ex) {
            //msclass.showMessage(ex.getMessage());
           // Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    public  void UploadaImage2(String Functionname)
    {

        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                String ImageName2 = "tt";
                // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
               String searchQuery="";
                if (Functionname.contains("mdo_endtravel")) {
                    searchQuery = "select   *  from " + Functionname + " where imgstatus='0' ORDER BY enddate desc LIMIT 1   ";
               }
               else
               {
                    searchQuery = "select   *  from " + Functionname + " where imgstatus='0' ORDER BY startdate desc LIMIT 1   ";

               }
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false)
                        {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("imgname"));
                                Imagestring1 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("imgpath")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (Exception e) {
                                //Toast.makeText(this, "Uplaoding"+e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            new UploadImageData(Functionname,  Imagestring1, Imagestring2, ImageName, ImageName2,"t").execute(cx.MDOurlpath);

                            cursor.moveToNext();
                        }
                        cursor.close();
                        //End
                  /* if(str.contains("True")) {

                       dialog.dismiss();
                       msclass.showMessage("Records Uploaded successfully");

                       recordshow();
                   }
                   else
                   {
                       msclass.showMessage(str);
                       dialog.dismiss();
                   }
                    */
                    }
                    catch (Exception ex) {  // dialog.dismiss();
                       // Toast.makeText(this, "Uplaoding"+ex.toString(), Toast.LENGTH_SHORT).show();
                        //msclass.showMessage("Image Data not available for Uploading ");
                    }
                } else {
                    //dialog.dismiss();
                    //msclass.showMessage("Image Data not available for Uploading ");
                    // dialog.dismiss();

                }

            } else {

                //dialog.dismiss();
            }
            // dialog.dismiss();
        }
        catch (Exception ex)
        {
           // Toast.makeText(this, "Uplaoding"+ex.toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        }

    }
    public class UploadDataServer extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname,Intime;
        public UploadDataServer( String Funname, byte[] objAsBytes,String Imagestring1,String Imagestring2,String ImageName,String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes=objAsBytes;
            this.Imagestring1 =Imagestring1;
            this.Imagestring2 =Imagestring2;
            this.ImageName=ImageName;
            this.Funname=Funname;
            this.Intime=Intime;

        }
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", Funname));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            String Urlpath=urls[0]+"?appName=Myactivity";

            // String Urlpath=urls[0]+"?action=2&farmerid="+userID+"&croptype="+croptype+"&imagename=Profile.png&issueDescription="+IssueDesc+"&issueid=1";

            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }

            }
            catch (ClientProtocolException e) {
                e.printStackTrace();


            }
            catch (Exception e) {
                e.printStackTrace();

            }


            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                String resultout=result.trim();
                if(resultout.contains("True")) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d=new Date();
                    String strdate=dateFormat.format(d);

                    if(Funname.equals("tagdatauploadMDONew_Testold")) {

                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'"+strdate+"' and Status='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Imgname='"+ImageName+"'");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where INTime='" + Intime + "'" );

                    }
                    if(Funname.equals("MDOFarmerMasterdataInsert")) {
                        mDatabase.deleterecord("delete from FarmerMaster");

                    }
                    if(Funname.equals("InnovationData")) {
                        mDatabase.Updatedata("update InnovationData  set Status='1' where Imgname='"+ImageName+"'");

                    }
                    if(Funname.equals("MDO_TravelData")) {
                        mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                        mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                        mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'"+strdate+"'");
                        mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'"+strdate+"'");
                        mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'"+strdate+"'");
                        mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                        mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");

                    }
                }
                else
                {
                   // msclass.showMessage(result+"--E");
                    //Toast.makeText(context, result+"Uploading Error", Toast.LENGTH_SHORT).show();

                }




            }

            catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
    public class UploadImageData extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName,ImageName2;
        String Funname,Intime;
        public UploadImageData( String Funname,String Imagestring1,String Imagestring2,String ImageName,String ImageName2,String Intime) {

            //this.IssueID=IssueID;
            this.objAsBytes=objAsBytes;
            this.Imagestring1 =Imagestring1;
            this.Imagestring2 =Imagestring2;
            this.ImageName=ImageName;
            this.ImageName2=ImageName2;
            this.Funname=Funname;
            this.Intime=Intime;

        }
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... urls) {

            // encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "UploadImages"));
            //postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
            postParameters.add(new BasicNameValuePair("input1", Imagestring1));
            postParameters.add(new BasicNameValuePair("input2", Imagestring2));

            //String Urlpath=urls[0];

            String Urlpath=urls[0]+"?ImageName="+ImageName+"&ImageName2="+ImageName2;

            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }

            }
            catch (ClientProtocolException e) {
                e.printStackTrace();


            }
            catch (Exception e) {
                e.printStackTrace();

            }

            return builder.toString();
        }
        protected void onPostExecute(String result) {
            try{
                String resultout=result.trim();
                if(resultout.contains("True")) {
                    // msclass.showMessage("Data uploaded successfully.");
                    if(Funname.equals("tagdatauploadMDONew_Testold")) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d=new Date();
                        String strdate=dateFormat.format(d);
                        // String searchQuery = "select * from TagData  where  strftime( '%Y-%m-%d', INTime)<>'"+strdate+"'  ";
                        mDatabase.deleterecord("delete from TagData where strftime( '%Y-%m-%d', INTime)<>'"+strdate+"' and Status='1' and Img='1' ");
                        mDatabase.Updatedata("update TagData  set Status='1' where Status='0' ");
                        mDatabase.Updatedata("update Tempstockdata set status='2' where Status='1'" );

                    }
                    if(Funname.equals("MDOFarmerMasterdataInsert")) {
                        mDatabase.deleterecord("delete from FarmerMaster");
                        // mDatabase.Updatedata("update FarmerMaster  set Status='1' where Status='0'");

                    }
                    if(Funname.equals("UploadImages")) {
                        mDatabase.Updatedata("update TagData  set imgstatus='1' where Imgname='"+ImageName+"'");

                    }
                    if(Funname.equals("mdo_starttravel")) {
                        mDatabase.Updatedata("update mdo_starttravel  set imgstatus='1' where imgname='"+ImageName+"'");

                    }
                    if(Funname.equals("mdo_endtravel")) {
                        mDatabase.Updatedata("update mdo_endtravel  set imgstatus='1' where imgname='"+ImageName+"'");

                    }
                }
                else
                {


                }




            }

            catch (Exception e) {
                e.printStackTrace();


            }

        }
    }
    class SyncDataAsync_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        ProgressDialog progressDialog;
        public SyncDataAsync_Async(String tag) {
            this.tag = tag;
        }
        protected void onPreExecute() {
                   }
        @Override
        protected String doInBackground(Void... params) {

                uploadstart("mdo_photoupdate", cx.MDOurlpath);

            return "";
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            progressBarVisibility();
        }
    }

    class SyncMDOTravel_Async extends AsyncTask<Void, Void, String> {
        //  ProgressDialog progressDialog;
        String tag;
        ProgressDialog progressDialog;
        public SyncMDOTravel_Async(String tag) {
            this.tag = tag;
        }
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... params) {

            MDO_TravelData();
            return "";
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {
            progressBarVisibility();
            try {

              /*  relPRogress.setVisibility(View.VISIBLE);
                myTextProgress.setText("Wait");
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });*/
                progressBar.setIndeterminate(true);
                new SyncDataAsync_Async("mdo_photoupdate").execute();
            }
            catch (Exception ex)
            {
                progressBarVisibility();
            }
        }
    }

    public void uploadstart(String Functionname, String apiUrl) {
        try {
            if (config.NetworkConnection()) {
                // dialog.setMessage("Loading....");
                //dialog.show();
                String str = null;
                String Imagestring1 = "";
                String Imagestring2 = "";
                String ImageName = "";
                String ImageName2 = "tt";
                String searchQuery = "select  DISTINCT imgname,imgpath from "+Functionname+" where Status='0'";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {

                    try {

                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {

                            // for (int i=0; i<count;i++) {

                            //START
                            byte[] objAsBytes = null;//new byte[10000];
                            JSONObject object = new JSONObject();
                            try {
                                ImageName = cursor.getString(cursor.getColumnIndex("imgname"));
                                Imagestring1 = mDatabase.getImageDatadetail(cursor.getString(cursor.getColumnIndex("imgpath")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                objAsBytes = object.toString().getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            syncSingleImage(Functionname, apiUrl, ImageName, Imagestring1);

                            cursor.moveToNext();
                        }
                        cursor.close();

                    } catch (Exception ex) {  // dialog.dismiss();

                    }
                } else {

                }

            } else {
               // msclass.showMessage("Internet network not available.");
                //dialog.dismiss();
            }
            // dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
           // Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    public synchronized void syncSingleImage(String function, String urls, String ImageName, String Imagestring1) {
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", "UploadImagesUpdate"));
        postParameters.add(new BasicNameValuePair("input1", Imagestring1));
        postParameters.add(new BasicNameValuePair("input2", "tt"));

        String Urlpath = urls + "?ImageName=" + ImageName+"&ImageName2=" + ImageName;
        Log.d("rohit", "doInBackground: " + Urlpath);
        Log.d("rohit", "doInBackground:params::: " + postParameters);
        HttpPost httppost = new HttpPost(Urlpath);
        httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

        try {
            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            httppost.setEntity(formEntity);

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

            // msclass.showMessage(e.getMessage().toString());


        } catch (Exception e) {
            e.printStackTrace();
            // msclass. showMessage(e.getMessage().toString());

        }

        try {
            handleImageSyncResponse(function, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

            // msclass.showMessage(e.getMessage().toString());

        }
    }

    //NEW
    public synchronized void syncTraveldata(String Funname, byte[] objAsBytes,String Imagestring1,String Imagestring2,String ImageName,String Intime,String urls) {


        String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);


        postParameters.add(new BasicNameValuePair("Type", Funname));
        postParameters.add(new BasicNameValuePair("encodedData", encodeImage));
        postParameters.add(new BasicNameValuePair("input1", Imagestring1));
        postParameters.add(new BasicNameValuePair("input2", Imagestring2));
        String Urlpath=urls+"?appName=Myactivity";
        Log.d("rohit", "doInBackground: " + Urlpath);
        Log.d("rohit", "doInBackground:params::: " + postParameters);
        HttpPost httppost = new HttpPost(Urlpath);
        httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

        try {
            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            httppost.setEntity(formEntity);

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    //builder.append(line).append("\n");
                    builder.append(line);
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            handleImageSyncResponse(Funname, builder.toString().trim(), ImageName, "");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void handleImageSyncResponse(String function, String resultout, String ImageName, String id)
    {
        try {
            if (resultout.contains("True")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d=new Date();
                String strdate=dateFormat.format(d);
                if (function.equals("MDO_TravelData"))
                {
                    mDatabase.Updatedata("update mdo_starttravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_endtravel  set Status='1'");
                    mDatabase.Updatedata("update mdo_addplace  set Status='1'");
                    mDatabase.Updatedata("delete from mdo_starttravel  where  Status='1' and strftime( '%Y-%m-%d', startdate)<>'"+strdate+"'");
                    mDatabase.Updatedata("delete from  mdo_endtravel  where  Status='1' and strftime( '%Y-%m-%d', enddate)<>'"+strdate+"'");
                    mDatabase.Updatedata("delete from  mdo_addplace  where  Status='1' and strftime( '%Y-%m-%d', date)<>'"+strdate+"'");
                    mDatabase.Updatedata("update mdo_Retaileranddistributordata  set Status='1'");
                    mDatabase.Updatedata("update mdo_retailerproductdetail  set Status='1'");
                }
                if (!ImageName.equals("")) {
                    mDatabase.Updatedata("update mdo_photoupdate  set Status='1' where imgname='" + ImageName + "'");
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);

    }

    private void logTravelActivityData(){
        if(preferences!=null){
            String userId="", displayName="";
            if (preferences.getString("UserID", null) != null && preferences.getString("Displayname", null) != null ){
                userId = preferences.getString("UserID", "");
                displayName = preferences.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callMyTravelAndActivityRecordingEvent(userId,displayName);
            }
        }
    }

}
