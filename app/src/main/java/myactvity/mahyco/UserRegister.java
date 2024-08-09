package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.app.WebService;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class UserRegister extends AppCompatActivity {
    public Button btnSave;
    ProgressDialog dialog;
    public String SERVER = "";
    public String  MDOurlpath="";
    private final static HttpClient mHhttpclient = new DefaultHttpClient();
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    public WebService wx;
    TextView lblheader;
    EditText txtPassword,txtUsername;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Prefs mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        getSupportActionBar().hide(); //<< this
        setTitle("User Registration");
        // setTitle("User Registration ");


        btnSave = (Button) findViewById(R.id.btnSave);
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        SERVER=cx.Urlpath;
        MDOurlpath=cx.MDOurlpath;
        wx=new WebService();
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        txtUsername=(EditText)findViewById(R.id.txtUsername);
        lblheader=(TextView)findViewById(R.id.lblheader);

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
        // mDatabase.getWritableDatabase();
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPref=Prefs.with(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == true) {
                    dialog.setMessage("Loading. Please wait...");
                    dialog.show();
                    UserRegisteration();
                }

            }
        });
    }
    private boolean validation()
    {
        boolean flag=true;
        if(txtUsername.getText().length()==0)
        {
            msclass.showMessage("Please enter user name ") ;
            return false;

        }
        if(txtPassword.getText().length()==0)
        {
            msclass.showMessage("Please  enter password") ;
            return false;
        }

        return true;
    }
    private  void UserRegisteration()
    {
        try {

            AppConstant.packagename=this.getApplicationContext().getPackageName();
            if(config.NetworkConnection()) {
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
                        password=URLEncoder.encode(txtPassword.getText().toString(), "UTF-8");
                        username= URLEncoder.encode(txtUsername.getText().toString(), "UTF-8");
                        //token = SharedPrefManager.getInstance(this).getDeviceToken();


                    } catch (UnsupportedEncodingException e) {
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
                                                        jObject.getString("user_code").toString().toUpperCase().trim(),
                                                        jObject.getString("IMEI_No").toString(),
                                                        jObject.getString("User_pwd").toString(),
                                                        jObject.getString("MDO_name").toString() + "(KA)",
                                                        jObject.getString("unit").toString());
                                                editor.putString("Displayname", jObject.getString("MDO_name").toString()+"(KA)");
                                                editor.putString("RoleID", jObject.getString("RoleID").toString() );
                                                editor.putString("unit", jObject.getString("unit").toString() );
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
                                                } else {
                                                    editor.putString("UserID", null);
                                                    editor.putString("Displayname",null);
                                                    editor.commit();
                                                    msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                                                    dialog.dismiss();
                                                    //return false;
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
                                    editor.putString("UserID", txtUsername.getText().toString().toLowerCase().trim());
                                    editor.commit();

                                    if (fl == true) {
                                        msclass.showMessage("User Registration successfully");
                                        logRegisterEvent();
                                        dialog.dismiss();
                                        txtPassword.setText("");
                                        txtUsername.setText("");
                                        //intent.setComponent(new ComponentName(AppConstant.packagename,AppConstant.packagename+"."+LoginActivity.class));
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                msclass.showMessage("Internet network not available.");
                dialog.dismiss();
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
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("password",password));
            Log.d("ACCESS_TOKEN","URL : "+"https://maapackhousenxg.mahyco.com/token");
            Log.d("ACCESS_TOKEN","Parameters : "+postParameters);
            return HttpUtils.POST("https://maapackhousenxg.mahyco.com/token",postParameters);
        }


        protected void onPostExecute(String result) {
            Log.d("ACCESS_TOKEN","Result : "+result);
            Log.d("result", result);
        }

    }
    @Override
    public void onBackPressed() {

        // finish();
        // super.onBackPressed();

        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();
        //System.exit(0);
      /*  if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }*/
    }

    /**
     * <P>Method to post the register data to server</P>
     */
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
                return HttpUtils.POSTJSON("https://maapackhousenxg.mahyco.com/api/Login",jsonParam,accesstoken);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(String result) {

            Log.d("result Register API" , result);

        }
    }

    private void logRegisterEvent(){
        if(pref!=null){
        String userId="", displayName="";
        if (pref.getString("UserID", "") != null && pref.getString("Displayname", "") != null ){
            userId = pref.getString("UserID", "");
            displayName = pref.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callRegisterUser(userId,displayName);
        }
        }
    }
}

