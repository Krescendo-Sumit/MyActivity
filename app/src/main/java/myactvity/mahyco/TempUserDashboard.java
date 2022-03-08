 package myactvity.mahyco;
        import android.app.ProgressDialog;
        import android.content.ContentProvider;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.os.AsyncTask;

        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.StatusLine;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.concurrent.ExecutionException;

        import myActivityRecordingOnline.imgpick;
        import myactvity.mahyco.app.CommonExecution;
        import myactvity.mahyco.app.Config;
        import myactvity.mahyco.helper.Messageclass;
        import myactvity.mahyco.helper.SqliteDatabase;
        import myactvity.mahyco.myActivityRecording.atlActivity.FieldBannerActivity;
        import myactvity.mahyco.myActivityRecording.atlActivity.FieldBoardActivity;

 public class TempUserDashboard extends AppCompatActivity {
    private Context context;
    Button btnDownloadmaster, btnFieldBanner, btnupload,btnFieldBoard;
    Config config;
    private SqliteDatabase mDatabase;
    public CommonExecution cx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public String userCode;
    public Messageclass msclass;
    final String TAG="couponDashboard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_user_dashboard);
        getSupportActionBar().hide(); //<< this
        context = this;
        config = new Config(this); //Here the context is passing
        mDatabase = SqliteDatabase.getInstance(this.context);
        // mDatabase.open();
        cx = new CommonExecution(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        logMFDCData();
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
        msclass = new Messageclass(this);
        btnDownloadmaster = (Button) findViewById(R.id.btnDownloadmaster);
        btnupload = (Button) findViewById(R.id.btnupload);
        btnFieldBoard = (Button) findViewById(R.id.btnFieldBoard);
        btnFieldBanner = (Button) findViewById(R.id.btnFieldBanner);
        btnDownloadmaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (config.NetworkConnection())
                    {

                        // Uploaded POG DATA

                        Intent intent = new Intent(context.getApplicationContext(), DownloadMasterdata.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else
                        {
                         msclass.showMessage("Please check internet connectivity ");
                    }

                } catch (Exception ex) {
                    // msclass.showMessage(ex.getMessage());
                }


            }
        });


        btnupload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date d=new Date();
                String strdate=dateFormat.format(d);

                    try {
                        Intent intent = new Intent(context.getApplicationContext(), UploadData.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        // new UploadCouponDataServer("mdo_couponSchemeDownloadAndUpload", context).execute(cx.MDOurlpath).get();

                    } catch (Exception e) {
                        Log.d(TAG, "onClick: Booking ");
                    }


                }


        });
        btnFieldBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        Intent intent = new Intent(context.getApplicationContext(), FieldBannerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);


                } catch (Exception ex) {
                    //msclass.showMessage(ex.getMessage());
                }

            }
        });
        btnFieldBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                        Intent intent = new Intent(TempUserDashboard.this, FieldBoardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);


                } catch (Exception ex) {
                    ex.printStackTrace();
                    //msclass.showMessage(ex.getMessage());
                }


            }
        });
    }


    private void logMFDCData(){
        if(pref!=null){
            String userId="", displayName="";
            if (pref.getString("UserID", null) != null && pref.getString("Displayname", null) != null ){
                userId = pref.getString("UserID", "");
                displayName = pref.getString("Displayname", "");
                FirebaseAnalyticsHelper.getInstance(this).callMFDCEvent(userId,displayName);
            }
        }
    }
}

