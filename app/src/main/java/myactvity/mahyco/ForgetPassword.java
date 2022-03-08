package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.WebService;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class ForgetPassword extends AppCompatActivity {
    public Button btnSave;
    ProgressDialog dialog;
    public String SERVER = "";
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    public WebService wx;
    EditText txtPassword,txtUsername;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide(); //<< this
        setTitle("Set New Password");
        // setTitle("User Registration ");
        btnSave = (Button) findViewById(R.id.btnSave);
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        SERVER=cx.Urlpath;
        wx=new WebService();

        txtPassword=(EditText)findViewById(R.id.txtPassword);
        txtUsername=(EditText)findViewById(R.id.txtUsername);
        mDatabase = SqliteDatabase.getInstance(this);
        // mDatabase.getWritableDatabase();
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
    private  boolean UserRegisteration()
    {
        int cn=mDatabase.getnocoloumncount("Select * from UserMaster");
        if (cn==4)
        {
            mDatabase.getWritableDatabase().execSQL("ALTER TABLE UserMaster ADD COLUMN  RoleID TEXT");
        }
        int cn5=mDatabase.getnocoloumncount("Select * from UserMaster");
        if (cn5==5)
        {
            mDatabase.getWritableDatabase().execSQL("ALTER TABLE UserMaster ADD COLUMN  unit TEXT");
        }

        int cnt=mDatabase.getnocoloumncount("Select * from TagData");
        if (cnt==22)
        {
            mDatabase.getWritableDatabase().execSQL("ALTER TABLE TagData ADD COLUMN  commentdecription TEXT");
        }
        int cnt23=mDatabase.getnocoloumncount("Select * from TagData");
        if (cnt23==23)
        {
            mDatabase.getWritableDatabase().execSQL("ALTER TABLE TagData ADD COLUMN  imgstatus TEXT");
        }

        boolean flag2 = mDatabase.isTableExists("Tempstockdata");
        if (flag2 == false) {
            mDatabase.CreateTable2("Tempstockdata");
        }



        boolean f1 = mDatabase.isTableExists("mdo_test");
        if (f1 == false) {
            mDatabase.deletetablenewtable();
            mDatabase.CreateTable6();
            mDatabase.CreateTable7();
            mDatabase.CreateTable8();
            mDatabase.CreateTable9();
            mDatabase.CreateTable5();
        }
        else
        {
            int cnt13=mDatabase.getnocoloumncount("Select * from mdo_starttravel");
            if (cnt13==14)
            {
                mDatabase.getWritableDatabase().execSQL("ALTER TABLE mdo_starttravel ADD COLUMN  vehicletype TEXT");
                mDatabase.getWritableDatabase().execSQL("ALTER TABLE mdo_endtravel ADD COLUMN  vehicletype TEXT");

            }

        }
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            boolean fl=false;
            String password="";
            String username="";
            String token="";
            String IME="";

            try {

                 IME=msclass.getDeviceIMEI();


                try {
                    password= URLEncoder.encode(txtPassword.getText().toString(), "UTF-8");
                    username= URLEncoder.encode(txtUsername.getText().toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
               // str = cx.new MDOMasterData(IME,1,txtUsername.getText().toString(),txtPassword.getText().toString()).execute().get();
                str = cx.new MDOMasterData(IME,1,username,password).execute().get();


                if(str.contains("False"))
                {
                    msclass.showMessage("Registration Data not available");
                    dialog.dismiss();
                }
                else {

                    JSONObject object = new JSONObject(str);
                    JSONArray jArray = object.getJSONArray("Table");

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(0);

                        // MDO LOGIN STATUS
                        if(jObject.getString("loginstatus").toString().equals("MDO")) {
                            if (jObject.getString("IMEI_No").toString().equals(msclass.getDeviceIMEI())) {
                                // if(jObject.getString("IMEI_No").toString().equals(msclass.getMobileNo())) {
                                mDatabase.deleledata("UserMaster", "");
                                fl = mDatabase.InsertUserRegistration(jObject.getString("RoleID").toString(),
                                        jObject.getString("user_code").toString().toUpperCase(),
                                        jObject.getString("IMEI_No").toString(),
                                        jObject.getString("User_pwd").toString(),
                                        jObject.getString("MDO_name").toString()+"(MDO)",
                                        jObject.getString("unit").toString());
                                editor.putString("Displayname", jObject.getString("MDO_name").toString()+"(MDO)");
                                editor.putString("RoleID", jObject.getString("RoleID").toString() );
                                editor.putString("unit", jObject.getString("unit").toString() );
                            } else {
                                msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                                dialog.dismiss();
                                return false;
                            }
                        }
                        else
                        {
                            // TBM LOGIN Status
                            if (jObject.getString("loginstatus").toString().equals("TBM")) {
                                if(jObject.getString("IMEI_No").toString().equals(msclass.getDeviceIMEI())) {

                                    mDatabase.deleledata("UserMaster", "");
                                    fl = mDatabase.InsertUserRegistration(jObject.getString("RoleID").toString(), jObject.getString("user_code").toString().toUpperCase(), jObject.getString("IMEI_No").toString(),
                                            jObject.getString("User_pwd").toString(),
                                            jObject.getString("MDO_name").toString(),
                                            jObject.getString("unit").toString());
                                    editor.putString("Displayname", jObject.getString("MDO_name").toString());
                                    editor.putString("RoleID", jObject.getString("RoleID").toString());
                                    editor.putString("unit", jObject.getString("unit").toString());
                                }
                                else
                                {
                                    msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                                    dialog.dismiss();
                                    return false;
                                }
                            } else {
                                msclass.showMessage("Registration data not available ,please check user name ");
                                dialog.dismiss();
                                return false;
                            }


                        }
                    }
                    editor.putString("UserID", txtUsername.getText().toString().toUpperCase());
                    editor.commit();

                    if (fl == true) {
                        msclass.showMessage("User pasword set  successfully");
                        logForgotPassword();
                        dialog.dismiss();
                        txtPassword.setText("");
                        txtUsername.setText("");
                        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        return true;
                        // finish();

                    } else {
                        msclass.showMessage("Registration  not done");
                        return false;
                    }


                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else
        {
            msclass.showMessage("Internet network not available.");
            dialog.dismiss();
            return false;
        }
        return true;
    }

    private void logForgotPassword(){
        String userId="", displayName="";
        if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
            userId = pref.getString("UserID", "");
            displayName = pref.getString("Displayname", "");
            FirebaseAnalyticsHelper.getInstance(this).callForgotPasswordEvent(userId,displayName);
        }
    }
}


