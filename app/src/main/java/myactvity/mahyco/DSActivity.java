package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.webkit.WebViewClient;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class DSActivity extends AppCompatActivity {

    Prefs mPref;
    public Messageclass msclass;
    public CommonExecution cx;
    private SqliteDatabase mDatabase;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    ConstraintLayout container;
    private long mLastClickTime = 0;
    private Handler handler = new Handler();
    private Context context;

    Config config;
    WebView webview ;
    String usercode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds);
        getSupportActionBar().hide();
        initUI();
    }
  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack()) {
            this.webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/
    /**
     * Method to initialize the elements
     */
   /* @Override
    public void onBackPressed() {
        if(webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }*/
    private void initUI() {
        context = this;
        cx = new CommonExecution(this);
        mPref = Prefs.with(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        msclass = new Messageclass(this);
        mDatabase = SqliteDatabase.getInstance(this);
        config = new Config(this); //Here the context is passing
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        container =  findViewById(R.id.container);
        webview =  findViewById(R.id.webView);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        usercode= pref.getString("UserID", null);
       // WebSettings webSettings = webview.getSettings();
       // webSettings.setJavaScriptEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        //webview.setWebChromeClient(new WebChromeClient());
        //WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        //webview.setWebViewClient(webViewClient);

        Bundle bundle = getIntent().getExtras();
        String tbmcode = bundle.getString("tbmcode");
        String region = bundle.getString("region");
        String tbmHQ = bundle.getString("tbmHQ");


        webview.setWebViewClient(new WebViewClient());
        Log.i("DAS Web URL","https://das.mahyco.com/DASHome.aspx?tbmHquarter="+tbmHQ+"&DasEntry=DasEntry&Region="+region+"&TBMHQ="+tbmcode+"&EmpCode="+usercode);
        webview.loadUrl("https://das.mahyco.com/DASHome.aspx?tbmHquarter="+tbmHQ+"&DasEntry=DasEntry&Region="+region+"&TBMHQ="+tbmcode+"&EmpCode="+usercode);
      //  webview.loadUrl("https://sungroccf.mahyco.com/DASHome.aspx?tbmHquarter="+tbmHQ+"&DasEntry=DasEntry&Region="+region+"&TBMHQ="+tbmcode+"&EmpCode="+usercode);
        // webView.loadUrl("file:///android_asset/web.html");
        webview.setWebChromeClient(new WebChromeClient() {
        });


        // webview.loadUrl("https://das.mahyco.com/DASHome.aspx/?tbmHquarter="+tbmHQ+"&DasEntry=DasEntry&Region="+region+"&TBMHQ="+tbmcode+"&EmpCode="+usercode);
        // webview.addJavascriptInterface("java", "Android");



          //New

        //WebView web_view = findViewById(R.id.webview);

        /*web_view.setDomStorageEnabled(true);  // Open DOM storage function
        web_view.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        web_view.setAppCachePath(appCachePath);
        web_view.setAllowFileAccess(true);    // Readable file cache
        web_view.setAppCacheEnabled(true);*/


        /*web_view.requestFocus();
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        web_view.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        web_view.loadUrl("https://das.mahyco.com/DASHome.aspx/?tbmHquarter="+tbmHQ+"&DasEntry=DasEntry&Region="+region+"&TBMHQ="+tbmcode+"&EmpCode="+usercode);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });*/
        //end

    }

}
