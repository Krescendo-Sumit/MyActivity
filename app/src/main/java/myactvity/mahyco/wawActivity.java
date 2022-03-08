package myactvity.mahyco;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.WebViewClientImpl;

public class wawActivity  extends AppCompatActivity {

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
    //String urlWebView = "https://das.mahyco.com?EmpCode=97190469";
    Config config;
    WebView webview ;
    String usercode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waw);
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
        // usercode= mPref.getString(AppConstant.USER_CODE_TAG, "");
        container =  findViewById(R.id.container);
        webview =  findViewById(R.id.webView);
        usercode= pref.getString("UserID", null);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // disable Web SQL


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webview.setWebViewClient(webViewClient);

        //webview.loadUrl(urlWebView+ "userCode=" + usercode);
        if (pref.getString("RoleID", null).contains("0")) // MDO Role Id
        {
            webview.loadUrl(" https://wow.mahyco.com/index-MDO.html");///Login/IndexMobile?tfacode="+usercode);
        }
        if (pref.getString("RoleID", null).contains("4")) // TBM Role Id
        {
            webview.loadUrl(" https://wow.mahyco.com/index-TBM.html");///Login/IndexMobile?tfacode="+usercode);
        }
        if (pref.getString("RoleID", null).contains("2")) // RBM Role Id
        {
            webview.loadUrl(" https://wow.mahyco.com/index-RBM.html");///Login/IndexMobile?tfacode="+usercode);
        }
        if (pref.getString("RoleID", null).contains("1")) // TBM Role Id
        {
            webview.loadUrl(" https://wow.mahyco.com/index-RBM.html");///Login/IndexMobile?tfacode="+usercode);
        }
    }
}