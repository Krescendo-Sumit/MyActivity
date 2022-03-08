package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.MySpinnerAdapter;


public class NewUserRegistration extends AppCompatActivity {
    public Button next;
    ProgressDialog dialog;
    public EditText txtmobileno, txtAadhar, txtname, txtemail,txtTotalLand;
    public SearchableSpinner spDist, spTaluka, spVillage,spState;
    private String AadharNo, name;
    private String mobileno;
    private String state;
    private String dist;
    private String taluka, email;
    private String village, Iagree;
    public String SERVER = "https://farm.mahyco.com/TestHandler.ashx";
    public Messageclass msclass;
    public CommonExecution cx;
    //public  cx.getCrop gcrop;
    Config config;
    private SqliteDatabase mDatabase;
    String usercode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);
        getSupportActionBar().hide(); //<< this
        // setTitle("User Registration ");
        config = new Config(this); //Here the context is passing
        spState= (SearchableSpinner) findViewById(R.id.spState);
        spDist = (SearchableSpinner) findViewById(R.id.spDist);
        spTaluka = (SearchableSpinner) findViewById(R.id.spTaluka);
        spVillage = (SearchableSpinner) findViewById(R.id.spVillage);
        next = (Button) findViewById(R.id.btnnext);
        txtmobileno = (EditText) findViewById(R.id.txtmobileno);
        txtAadhar = (EditText) findViewById(R.id.txtAadhar);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtname = (EditText) findViewById(R.id.txtname);
        txtTotalLand= (EditText) findViewById(R.id.txtTotalLand);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        mDatabase = SqliteDatabase.getInstance(this);
        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        usercode= pref.getString("UserID", null);
        Utility.setRegularFont(txtAadhar, NewUserRegistration.this);
        Utility.setRegularFont(txtmobileno, NewUserRegistration.this);
        Utility.setRegularFont(txtemail, NewUserRegistration.this);
        Utility.setRegularFont(txtname, NewUserRegistration.this);
        Utility.setRegularFont(next, NewUserRegistration.this);
        Utility.setRegularFont(next, NewUserRegistration.this);
        // versionChecker = new Home.VersionChecker();
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //BindDist("");
        BindState();
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state=gm.Code().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BindDist(state);
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callSave();

            }
        });

        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    dist = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BindTaluka(dist);
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka = gm.Code().trim();//URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BindVillage(taluka);
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void callSave() {

        try {

        if (TextUtils.isEmpty(txtname.getText())) {
            try {

                    txtname.setError("Please enter name");

            }
            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
            }
            return;
        }


        GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
        GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
        GeneralMaster vt = (GeneralMaster) spVillage.getSelectedItem();
      if (spState.getSelectedItem().toString().toLowerCase().equals("select state")) {
                msclass.showMessage("Please select state");
                return ;
            }
        if (dt.Code().equals("0")) {
            msclass.showMessage("Please select district");
            return;
        }
        if (tt.Code().equals("0")) {
            msclass.showMessage("Please select taluka");
            return;
        }
        if (vt.Code().equals("0")) {
            msclass.showMessage("Please select village");
            return;
        }

        if (TextUtils.isEmpty(txtmobileno.getText())) {
            txtmobileno.setError("Please enter Mobile Number");
            return;
        }
        if (txtmobileno.getText().length() != 10) {
            txtmobileno.setError(" Mobile Number should be 10 digit.");
            return;
        }
        dialog.setMessage("Loading. Please wait...");
        dialog.show();
        mobileno = txtmobileno.getText().toString();
        AadharNo = txtAadhar.getText().toString();
            dist = URLEncoder.encode(dt.Code().trim(), "UTF-8");
            taluka = tt.Code().trim();//URLEncoder.encode(tt.Code().trim(), "UTF-8");
            village = vt.Code().trim();//URLEncoder.encode(vt.Code().trim(), "UTF-8");
            name = txtname.getText().toString();//URLEncoder.encode(txtname.getText().toString(), "UTF-8");
            email = txtemail.getText().toString();//URLEncoder.encode(txtemail.getText().toString(), "UTF-8");
            String statename=spState.getSelectedItem().toString();
            String totalland=txtTotalLand.getText().toString();

        Savedata(name, statename,dist, taluka, village, AadharNo, mobileno, email,totalland);
        // String id = spState.get(spState.getSelectedItemPosition());
        // new OTPSendNo(name,AadharNo,mobileno,state,dist,taluka,village,"Y").execute(SERVER);
    } catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.getMessage());
        }

    }

    private void cleardata() {
        txtname.setText("");
        txtAadhar.setText("");
        txtemail.setText("");
        txtmobileno.setText("");
        txtTotalLand.setText("");


    }

    private void Savedata(String name,String statename, String dist, String taluka, String village, String AadharNo, String mobileno, String email,String TotalLand) {
        try {

            Date entrydate = new Date();
            String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(entrydate);
            boolean fl = mDatabase.InsertFarmerData(name,statename,dist, taluka, village, AadharNo,
                    mobileno, email, "0",TotalLand,usercode);
            Toast.makeText(this, "Save Record data successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            cleardata();
        } catch (Exception ex) {
            Toast.makeText(this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void  BindState()
    {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                Croplist.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Croplist.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            dialog.dismiss();
        }

    }
    public void BindDist(String state) {
        dialog.setMessage("Loading....");
        dialog.show();
        String str = null;
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            //String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster ";
            String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                    " where state='"+state+"' order by district asc  ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("0",
                    "Select Dist"));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDist.setAdapter(adapter);
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog.dismiss();
    }

    public void BindTaluka(String dist) {

        dialog.setMessage("Loading....");
        dialog.show();
        String str = null;
        try {

            // str = cx.new getTaluka(dist).execute().get();

            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where district='" + dist.trim() + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("0",
                    "Select Taluka"));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTaluka.setAdapter(adapter);
            dialog.dismiss();


        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void BindVillage(String taluka) {

        spVillage.setAdapter(null);
        dialog.setMessage("Loading....");
        dialog.show();
        String str = null;
        try {
            //str = cx.new getVillage(taluka).execute().get();
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String searchQuery = "SELECT distinct village,village_code  FROM VillageLevelMaster where taluka='" + taluka + "' ";
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("0",
                    "Select Village"));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Croplist.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            MySpinnerAdapter<GeneralMaster> adapter = new MySpinnerAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVillage.setAdapter(adapter);
            dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog.dismiss();

    }

    @Override
    public void onResume() {



        // receiver.setActivityHandler(OTPVerify.class);

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }


}
