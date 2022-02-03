package myactvity.mahyco;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import com.google.android.material.tabs.TabLayout;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
import myactvity.mahyco.helper.SqliteDatabase;

public class RetailerandDistributorTag  extends AppCompatActivity {
    private SqliteDatabase mDatabase;
    Config config;
    public CommonExecution cx;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.start,
            R.drawable.addtravel

    };
    SharedPreferences pref;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailerand_distributor_tag);
        getSupportActionBar().hide(); //<< this
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        cx = new CommonExecution(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
         tabLayout = (TabLayout) findViewById(R.id.tabLayout);
         tabLayout.setupWithViewPager(viewPager);
        if(config.NetworkConnection()) {

            MDO_demandassesmentservey();

        }
        //setupTabIcons();

        context = this;
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        logTradeMappingAndTaggingData();
    }
    public void MDO_demandassesmentservey()
    {

        String str= null;
        String Imagestring1="";
        String Imagestring2="";
        String ImageName="";
        Cursor cursor=null;
        String searchQuery="";
        int count=0;
        searchQuery = "select * from mdo_demandassesmentsurvey where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivation where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_cultivationtobe where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_awaremahycoproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();

        searchQuery = "select * from mdo_authoriseddistributorproduct where Status='0'";
        cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null );
        count=count+cursor.getCount();
        cursor.close();
        if (count > 0) {
            try {
                //START
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                try {
                    searchQuery = "select distinct  * from mdo_demandassesmentsurvey where Status='0'";
                    object.put("Table1", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivation where Status='0'";
                    object.put("Table2", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_cultivationtobe where Status='0'";
                    object.put("Table3", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_awaremahycoproduct where Status='0'";
                    object.put("Table4", mDatabase.getResults(searchQuery));
                    searchQuery = "select distinct * from mdo_authoriseddistributorproduct where Status='0'";
                    object.put("Table5", mDatabase.getResults(searchQuery));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new UploadDataServer("MDO_demandassesmentservey",objAsBytes, Imagestring1, Imagestring2, ImageName,"").execute(cx.MDOurlpath);

            } catch (Exception ex) {
                // msclass.showMessage(ex.getMessage());

            }
        }
        else
        {

        }
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RetailerData() , "RETAILER DATA");
        adapter.addFragment(new DistributorData() , "DISTRIBUTOR DATA");

        // adapter.addFragment(new Nutrition(),  getResources().getString(R.string.Nutrition));

        viewPager.setAdapter(adapter);
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
            String Urlpath=urls[0];
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
                // msclass.showMessage(e.getMessage().toString());

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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d=new Date();
                    String strdate=dateFormat.format(d);
                    if(Funname.equals("MDO_demandassesmentservey")) {
                        mDatabase.Updatedata("update mdo_demandassesmentsurvey  set Status='1'");
                        mDatabase.Updatedata("update mdo_cultivation         set Status='1'");
                        mDatabase.Updatedata("update mdo_cultivationtobe     set Status='1'");
                        mDatabase.Updatedata("update mdo_awaremahycoproduct  set Status='1'");
                        mDatabase.Updatedata("update mdo_authoriseddistributorproduct  set Status='1'");
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

    private void logTradeMappingAndTaggingData(){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callTradeMappingAndTaggingEvent(userId,displayName);
            }
        }
    }
}
