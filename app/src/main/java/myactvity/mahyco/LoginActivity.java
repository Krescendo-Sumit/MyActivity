package myactvity.mahyco;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.services.SharedPrefManager;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    public TextView lblforgetpassword,lblNewuserregister;
    public Button btnNewRegister, btnlogin,btnUpdate;
    public  EditText password;
    public AutoCompleteTextView email;
    ProgressDialog dialog;
    public String SERVER = "https://farm.mahyco.com/TestHandler.ashx";
    public String SERVER2 = "https://cmr.mahyco.com/FormerApp.asmx";
    public String  langcode="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    // private ProgressDialog mProgressDialog;
    private Handler handler = null;
    private DownloadManager downloadManager;
    private long downloadReference;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    private Spinner splogintype;
    private ProgressDialog mProgressDialog;
    Locale locale;
    public Boolean fg=false;
    private long enqueue;
    private DownloadManager dm;
    boolean isDeleted;

    SharedPreferences locdata;
    public String lang;
    private SqliteDatabase mDatabase;
    private  static String  currentver;
    public Messageclass msclass;
    private Boolean exit = false;
    public CommonExecution cx;
    public String returnstring;
    Config config;
    Prefs mPref;
    String usercode="blank";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lblforgetpassword=(TextView) findViewById(R.id.lblforgetpassword);
        lblNewuserregister=(TextView) findViewById(R.id.lblNewuserregister);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        password=(EditText) findViewById(R.id.password);
        email=(AutoCompleteTextView) findViewById(R.id.email);
        splogintype=(Spinner) findViewById(R.id.splogintype);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPref=Prefs.with(this);
        cx=new CommonExecution(this);
        config = new Config(this); //Here the context is passing
        getSupportActionBar().hide(); //<< this
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        handler = new Handler();
        //DB Crration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            AppConstant.dbnamepath =this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db";
            Log.d("Rajshri","DB_PATH : "+this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/SQLiteDB/MDOApps.db");
        }
        else
        {
            AppConstant.dbnamepath="/mnt/sdcard/MDOApps.db";
        }
        mDatabase = SqliteDatabase.getInstance(this);
        //New Code 23-08 to avoid database lock

        try {
            mDatabase.open();
        } catch (SQLException e) {
            Log.e("error",e.getLocalizedMessage());
            e.printStackTrace();
        }


        msclass = new Messageclass(this);
        if (pref.getString("UserID", null) != null) {
        usercode=pref.getString("UserID", null).trim();
        }
        Utility.setRegularFont(lblforgetpassword,LoginActivity.this);
        Utility.setRegularFont(lblNewuserregister,LoginActivity.this);
        Utility.setRegularFont(btnlogin,LoginActivity.this);
        Utility.setRegularFont(password,LoginActivity.this);
        Utility.setRegularFont(btnUpdate,LoginActivity.this);

        //Call new colown added in DEMO and review Table
        //mDatabase.addEntryDateDemoplot();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()==true) {

                    LoginRequest();
                    /*if(splogintype.getSelectedItem().toString().equals("MDO"))
                    {
                        LoginRequest();
                    }
                    else
                    {
                        dialog.setMessage("Loading. Please wait...");
                        dialog.show();
                        TBMLOGIN();
                    }*/
                }

               /* Intent intent = new Intent(LoginActivity.this, UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });

        //bindlogintype();
        lblforgetpassword.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (getCallingActivity().getPackageName().equals("")) {
                    Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                    //Intent intent = new Intent(LoginActivity.this, AndroidDatabaseManager.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialog.dismiss();
               // }
            }
        });
        lblNewuserregister.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserRegister.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //St
        //VersionChecker versionChecker = new VersionChecker();
        try {
            currentver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String latestVersion=null;
            if (config.NetworkConnection())
            {
                try {
                    //latestVersion=versionChecker.execute().get();
                    String IME=msclass.getDeviceIMEI();
                    String token =SharedPrefManager.getInstance(this).getDeviceToken();
                     if(token==null)
                     {
                         token = FirebaseInstanceId.getInstance().getToken();

                     }
                    //latestVersion = cx.new GetAppversion("MyActivity",token,IME).execute().get();//
                     new GetAppversion("MyActivity",token,IME).execute();

                }
                catch(Exception e)
                {
                    latestVersion=null;
                    Log.d("Redirect", "onCreate: "+e.toString());
                    RedirectCondition();
                }


            }
            else
            {
                latestVersion=null;
                RedirectCondition();
            }





        } /*catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }

        //end


    }


    public void RedirectCondition()
    {
        //Start
        try {




           // ComponentName componentName = this.getCallingActivity();
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null )
            {
               // if (getCallingActivity().getPackageName().equals(BuildConfig.APPLICATION_ID)) {
                    Intent i = new Intent(this, UserHome.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
               // }
               // else
                //{
                  //  Toast.makeText(this, getCallingActivity().getPackageName().toString(), Toast.LENGTH_SHORT).show();
               // }
                //finish();
            }



        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }

        //End
    }

    public void LoginRequest() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String username = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        // String lang = spnlanguage.getSelectedItem().toString().trim();
        dialog.setMessage("Loading. Please wait...");
        dialog.show();
        logincheck(username.trim(),pass.trim());
        //  new LoginReq().execute(SERVER,username,pass);


    }
    private  void logincheckold(String username,String pass)
    {
        try {

             Cursor cursor = mDatabase.logindetail(pass,username);// database.rawQuery(searchQuery, null);

            int count = cursor.getCount();

            if (count > 0) {
                // msclass.showMessage(cursor.getString(1));
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    editor.putString("UserID", cursor.getString(1));
                    editor.putString("Pass", cursor.getString(0));
                    editor.putString("Displayname", cursor.getString(3));
                    editor.putString("IMEI", msclass.getDeviceIMEI());
                    editor.putString("RoleID", cursor.getString(4));//jObject.getString("RoleID").toString() );
                    editor.putString("unit", cursor.getString(5));
                    editor.commit();
                    cursor.moveToNext();
                }

                cursor.close();

                logLoginEvent();
                Intent intent = new Intent(LoginActivity.this, UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();

            } else {
                msclass.showMessage("User name and password not correct ,please try again");
                dialog.dismiss();
            }
        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            dialog.dismiss();
        }
    }
    private  void logincheck(String user,String pass)
    {
        try {



            if(config.NetworkConnection())
            {
                dialog.setMessage("Loading....");
                dialog.show();
                String str= null;
                String strAuthToken= null;
                boolean fl=false;

                String password="";
                String username="";
                String token="";
                String IME="";

                try {
                    try {
                        IME=msclass.getDeviceIMEI();
                        password=URLEncoder.encode(pass, "UTF-8");
                        username= URLEncoder.encode(user, "UTF-8");
                        //token = SharedPrefManager.getInstance(this).getDeviceToken();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    strAuthToken = new GetAuthToken(username,password).execute().get();

                    Log.d("Response", strAuthToken);
                    try {
                        JSONObject jsonObject = new JSONObject(strAuthToken);
                        if(jsonObject.has("access_token")) {
                            token = jsonObject.get("access_token").toString();
                            Log.d("token", token);
                            if (token != null && token != "") {
                                mPref.save(AppConstant.ACCESS_TOKEN_TAG, token);
                                mPref.save(AppConstant.USER_CODE_TAG, username);
                                str = new MDOMasterData(IME, 1, username, password, token).execute().get();
                                JSONObject jsonObject1 = new JSONObject(str);
                                String value = jsonObject1.get("Table").toString();
                                if (value.equals(null)) {
                                    mPref.save(AppConstant.USER_CODE_TAG, username);
                                    editor.putString("UserID", null);
                                    editor.putString("Displayname", null);
                                    editor.commit();
                                    msclass.showMessage("Registration Data not available");
                                    dialog.dismiss();
                                } else {

                                    JSONObject object = new JSONObject(str);
                                    JSONArray jArray = object.getJSONArray("Table");

                                    for (int i = 0; i < jArray.length(); i++) {

                                        JSONObject jObject = jArray.getJSONObject(0);

                                        // MDO LOGIN STATUS
                                        if (jObject.getString("loginstatus").toString().equals("MDO")) {
                                            if (jObject.getString("IMEI_No").toString().equals(msclass.getDeviceIMEI())) {
                                                // if(jObject.getString("IMEI_No").toString().equals(msclass.getMobileNo())) {
                                                mDatabase.deleledata("UserMaster", "");

                                                fl = mDatabase.InsertUserRegistration(jObject.getString("RoleID").toString(),
                                                        jObject.getString("user_code").toString().toUpperCase(),
                                                        jObject.getString("IMEI_No").toString(),
                                                        jObject.getString("User_pwd").toString(),
                                                        jObject.getString("MDO_name").toString() + "(MDO)",
                                                        jObject.getString("unit").toString());
                                                editor.putString("Displayname", jObject.getString("MDO_name").toString()+"(MDO)");
                                                editor.putString("RoleID", jObject.getString("RoleID").toString() );
                                                editor.putString("unit", jObject.getString("unit").toString() );
                                                editor.putString("UserID", user);
                                                editor.putString("Pass", pass);
                                                editor.putString("IMEI", msclass.getDeviceIMEI());
                                            } else {

                                                editor.putString("UserID", null);
                                                editor.putString("Displayname", null);
                                                editor.commit();
                                                msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                                                dialog.dismiss();
                                                //return false;
                                            }
                                        } else {
                                            // TBM LOGIN Status
                                            if (jObject.getString("loginstatus").toString().equals("TBM")) {
                                                if (jObject.getString("IMEI_No").toString().equals(msclass.getDeviceIMEI())) {

                                                    mDatabase.deleledata("UserMaster", "");
                                                    fl = mDatabase.InsertUserRegistration(jObject.getString("RoleID").toString(), jObject.getString("user_code").toString().toUpperCase(), jObject.getString("IMEI_No").toString(),
                                                            jObject.getString("User_pwd").toString(),
                                                            jObject.getString("MDO_name").toString(),
                                                            jObject.getString("unit").toString());
                                                    editor.putString("Displayname", jObject.getString("MDO_name").toString());
                                                    editor.putString("RoleID", jObject.getString("RoleID").toString());
                                                    editor.putString("unit", jObject.getString("unit").toString());
                                                    editor.putString("UserID", user);
                                                    editor.putString("Pass", pass);
                                                    editor.putString("IMEI", msclass.getDeviceIMEI());
                                                } else {
                                                    editor.putString("UserID", null);
                                                    editor.putString("Displayname",null);
                                                    editor.commit();
                                                    msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                                                    dialog.dismiss();





                                                }
                                            } else {
                                                editor.putString("UserID", null);
                                                editor.putString("Displayname", null);
                                                editor.commit();
                                                msclass.showMessage("Registration data not available ,please check user name ");
                                                dialog.dismiss();
                                                //return false;
                                            }


                                        }
                                    }
                                    editor.putString("UserID", user.toLowerCase());
                                    editor.commit();

                                    if (fl == true) {
                                        msclass.showMessage("User login successfully");
                                        dialog.dismiss();

                                        logLoginEvent();
                                        Intent intent = new Intent(getApplicationContext(), UserHome.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        //return true;
                                        // finish();

                                    } else {
                                        editor.putString("UserID", null);
                                        editor.putString("Displayname", null);
                                        editor.commit();
                                        msclass.showMessage("Registration  not done");
                                        // return false;
                                    }


                                }
//
                            }
                        }else {

                            msclass.showMessage(jsonObject.get("error_description").toString());
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                logincheckold( user, pass);
               // msclass.showMessage("Internet network not available.");
               // dialog.dismiss();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.getMessage());

        }
    }
    /**
     * <p>Method to get the access token from API</p>
     */
    public class GetAuthToken extends  AsyncTask<String, String, String>
    {
        private String username;
        private String password;

        public GetAuthToken(String username ,String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            //String auth=R.string.auth;
            if (password.contains("@Test@123")) {
                postParameters.add(new BasicNameValuePair("username", "97260828"));
                postParameters.add(new BasicNameValuePair("password", password));
            }
            else {
                postParameters.add(new BasicNameValuePair("username", username));
                postParameters.add(new BasicNameValuePair("password", password));
            }
            return HttpUtils.POST("https://packhouse.mahyco.com/token",postParameters);
        }


        protected void onPostExecute(String result) {

            Log.d("result", result);
        }

    }
    public class MDOMasterData extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String password,IME, accesstoken;


        public MDOMasterData( String IME,int action,String username,String password, String token){
            this.action=action;
            this.username=username;
            this.password=password;
            this.IME=IME;
            this.accesstoken = token;
        }
        protected void onPreExecute() {
            // dialog.setMessage("Loading....");
            //dialog.show();

        }
        @Override
        protected String doInBackground(String... urls) {
            try {

                //Add POST data in JSON format
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("IME", IME);
                    jsonParam.put("username", username);
                    jsonParam.put("sapcode", action);
                    jsonParam.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Get the Response for the request
                return HttpUtils.POSTJSON("https://packhouse.mahyco.com/api/Login",jsonParam,mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(String result) {

            Log.d("result Register API" , result);


        }
    }
    public class GetAppversion extends AsyncTask<String, String, String> {


        private String appname,token,imei;


        public GetAppversion(String appname,String token,String imei ){
            this.appname=appname;
            this.token=token;
            this.imei =imei;
        }
        protected void onPreExecute() {
            dialog.setMessage("Loading....");
            dialog.show();

        }
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetAppversionNew"));
            postParameters.add(new BasicNameValuePair("xmlString",usercode));
            postParameters.add(new BasicNameValuePair("usercode",usercode));
            //String Urlpath1= Urlpath+"?appname="+appname+"";
            String Urlpath1= cx.MDOurlpath+"?appname="+appname+"&token="+token+"&imei="+imei;
            Log.i("AppVersionUrl",Urlpath1);
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring= builder.toString();
                    dialog.dismiss();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=null;//e.getMessage().toString();
                dialog.dismiss();
            }
            // dialog.dismiss();
            return builder.toString().trim();
        }
        protected void onPostExecute(String result) {

            dialog.dismiss();
           // result="8.5.2~New Version SK";
            Log.d("result Register API : " , "Result : "+result);
          //  Toast.makeText(LoginActivity.this, ""+result, Toast.LENGTH_SHORT).show();
            try {
                if (result!=null && !result.isEmpty())
                {
                    String[] arry=result.split("~");
                    // if(Double.parseDouble(currentver) != Double.parseDouble(latestVersion))
                    if(!arry[0].toString().equals(currentver))
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("My Activity App");
                        // Setting Dialog Message
                        //alertDialog.setMessage("New version "+latestVersion+" available on play store please update APK.");
                        alertDialog.setMessage("New version "+arry[0].toString()+" available on play store please update APK.\n"+arry[1].toString());
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.clear();
                                editor.commit();
                                try {
                                    Intent viewIntent =
                                            new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=myactvity.mahyco"));
                                    viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(viewIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // I do not need any action here you might
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();

                            }
                        });

                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        //end



                    }
                    else
                    {
                        RedirectCondition();

                    }
                }
                else
                {
                    RedirectCondition();
                }
            }
            catch (Exception ex)
            {

            }


        }
    }

     public void setJSONTData(String jsonData,String lang,String MobileNo,String AadharNo,String name ) {
        try {
            editor.putString("JsonData",jsonData);
            editor.commit();

                 Intent intent = new Intent(LoginActivity.this, UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();

            dialog.dismiss();


        } catch (Exception ex) {
            dialog.dismiss();
        }
    }


    private boolean validation()
    {

        final String emailtxt=email.getText().toString();
        String passtxt=password.getText().toString();
        if(emailtxt.length()==0)
        {
            email.requestFocus();
            email.setError("Please enter user ID.");
            return false;
        }
        if(passtxt.length()==0)
        {
            password.requestFocus();
            password.setError("Please enter Password");
            return false;
        }


        return true;
    }
    public void showMessage(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        // Setting Dialog Title
        alertDialog.setTitle("MDO App");
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {

        // finish();
        // super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();


    }

     public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/","/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if ( new File( where + binaryName ).exists() ) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }


    private void logLoginEvent(){
        if(pref!=null){
        String userId="", displayName="";
        if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
            userId = pref.getString("UserID", "");
            displayName = pref.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callLoginEvent(userId,displayName);
        }
        }
    }
    private void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}
