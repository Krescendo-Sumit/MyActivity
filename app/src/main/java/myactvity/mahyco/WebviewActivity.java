package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class WebviewActivity extends AppCompatActivity {
    WebView view;
    private Handler handler = null;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    private ProgressDialog mProgressDialog;
    private DownloadManager downloadManager;
    private long downloadReference;
    // SharedPreferences PreferenceManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    URL url;
    URLConnection urlconnection ;
    int FileSize;
    InputStream inputstream;
    OutputStream outputstream;
    byte dataArray[] = new byte[1024];
    long totalSize = 0;
    ImageView imageview;
    String GetPath ;
    Locale locale;

    ProgressDialog bar;
    private static String TAG = "BarcodeAcitivity";
    private int AppVersion = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_webview);
        String myurl="file:///android_asset/login.html";
        view=(WebView)this.findViewById(myactvity.mahyco.R.id.webview);
        view.getSettings().setJavaScriptEnabled(true);

        //view.getSettings().setAllowUniversalAccessFromFileURLs(true);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        view.addJavascriptInterface(new WebviewActivity.AppJavaScriptProxy(this), "androidAppProxy");
        view.getSettings().setDomStorageEnabled(true);
        handler = new Handler();

        if(getIntent().getStringExtra("Pagetype").equals("PlotVisit"))
        { loadPage("LeavePage.htm");
        }
        if(getIntent().getStringExtra("Pagetype").equals("SeedWeight"))
        { loadPage("Rcpt.htm");}
        if(getIntent().getStringExtra("Pagetype").equals("Quality"))
        { loadPage("Quality.htm");}
        if(getIntent().getStringExtra("Pagetype").equals("MyCoupon"))
        { loadPage("coupon.htm");}



    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, url, Toast.LENGTH_SHORT);
            toast.show();
            // if (Uri.parse(url).getHost().equals("file:///android_asset/Index.htm")) {
            //    return false;
            //}
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("file:///android_asset/"+url));
            //startActivity(intent);

            view.loadUrl(url);
            return true;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.view.canGoBack()) {
            this.view.goBack();
            // return true;
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }



    public void loadPage(String in){
        //Toast.makeText(mContext, "fgfdgdfh", Toast.LENGTH_SHORT).show();
        final String url = "file:///android_asset/" + in;
        //loadURL(url);
        view.loadUrl(url);
    }

    private void loadURL(final String in){
        handler.post(new Runnable() {
            public void run() {

                view.loadUrl(in);
            }
        });
    }


    //***********************
    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void showToast(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            // String myurl="file:///android_asset/login.html";
            // view=(WebView)BarcodeAcitivity.findViewById(R.id.webview);
            // view.loadUrl(myurl);
        }




    }

    public class AppJavaScriptProxy {
        private Activity activity = null;

        public AppJavaScriptProxy(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void showMessage2(String message) {

            Toast toast = Toast.makeText(this.activity.getApplicationContext(),
                    message,
                    Toast.LENGTH_SHORT);

            toast.show();
        }
        @JavascriptInterface
        public void showMessage(String message)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this.activity).create();

            // Setting Dialog Title
            alertDialog.setTitle("Farmer App");

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
        @JavascriptInterface
        public void loadPage(String in){
            final String url = "file:///android_asset/" + in;
            loadURL(url);
            // loadURL(in);
        }

        @JavascriptInterface
        public void Logout(String in){
            final String url = "file:///android_asset/" + in;

           // editor.clear();
            editor.putString("UserID",null);
            editor.commit();

            //Intent i = new Intent(BarcodeAcitivity.this, CaptureDate.class);
            //startActivity(i);

            loadURL(url);

        }

        @JavascriptInterface
        private void loadURL(final String in){
            handler.post(new Runnable() {
                public void run() {
                    view.loadUrl(in);
                    //String htmlString = getHtmlFromAsset(in);

                    //if (htmlString != null)

                    //   view.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "UTF-8", null);

                    // else

                    //Toast.makeText(this, "no_such_page", Toast.LENGTH_LONG).show();

                }
            });
        }
        public String getHtmlFromAsset(String in) {
            InputStream is;
            StringBuilder builder = new StringBuilder();
            String htmlString = null;
            try {
                is = getAssets().open(in);
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    htmlString = builder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return htmlString;
        }


        @JavascriptInterface
        public void SaveUserIDandPassword(String userid,String Pass,String Lang,String Displayname,String UID,String Mobile)
        {
            editor.putString("UserID",userid);
            editor.putString("Pass",Pass);
            editor.putString("Lang",Lang);
            editor.putString("Displayname",Displayname);
            editor.putString("UID",UID);
            editor.putString("Mobile",Mobile);
            editor.commit();

        }
        @JavascriptInterface
        // public String[]  GetSaveData() {
        public String  GetSaveData() {

            String rtn="";
            String[] data_array = new String[5];
            rtn=pref.getString("UserID", null); // getting String
            rtn=rtn+"$"+pref.getString("Pass", null); // getting String

            rtn=rtn+"$"+pref.getString("Lang", null); // getting String
            rtn=rtn+"$"+pref.getString("Displayname", null); // getting String
            rtn=rtn+"$"+pref.getString("UID", null); // getting String
            rtn=rtn+"$"+pref.getString("Mobile", null); // getting String
            return rtn ;//""+data_array.length;


        }
        @JavascriptInterface
        public void setJSONTData(String jsonData,String lang) {
            try {
                // JSONObject data = new JSONObject(jsonData); //Convert from string to object, can also use JSONArray
                editor.putString("JsonData",jsonData);
                editor.commit();
            } catch (Exception ex) {}
        }

        @JavascriptInterface
        public void setProductDetailData(String picname,String productname,String Prddesc) {
            try {
                editor.putString("picname",picname);
                editor.putString("productname",productname);
                editor.putString("Prddesc",Prddesc);
                editor.commit();
            } catch (Exception ex) {}
        }
        @JavascriptInterface
        public String  getProductDetailData() {

            String rtn="";
            String[] data_array = new String[3];
            rtn=pref.getString("picname", null); // getting String
            rtn=rtn+"$"+pref.getString("productname", null); // getting String
            rtn=rtn+"$"+pref.getString("Prddesc", null); // getting String
            return rtn ;//""+data_array.length;
        }
        @JavascriptInterface
        public String getJSONTData() {
            String rtn="";
            try {
                JSONObject data = new JSONObject(pref.getString("JsonData", null)); //Convert from string to object, can also use JSONArray
                rtn=pref.getString("JsonData", null);
            } catch (Exception ex) {}
            return rtn;
        }
        @JavascriptInterface
        public void closeActivity() {

        }
        @JavascriptInterface
        public void BackActivity() {

            //this.finish();
            Intent intent = new Intent(getApplicationContext(), UserHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }




        @JavascriptInterface
        public void Dialogmeassageconfirm(String message) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(WebviewActivity.this);
            dialog.setCancelable(false);
            dialog.setTitle("Farmer App");
            dialog.setMessage(message );
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    //  new DownloadNewVersion().execute();
                }
            })
                    .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                        }
                    });

            final AlertDialog alert = dialog.create();
            alert.show();


        }

        public void Dialogmeassage(String message) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this.activity).create();

            // Setting Dialog Title
            alertDialog.setTitle("Farmer App");

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

        @JavascriptInterface
        public boolean  NetworkConnection() {

            ConnectivityManager cm = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;


        }
        @JavascriptInterface
        private boolean haveNetworkConnection() {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }






    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Progress_Dialog_Progress:

                progressdialog = new ProgressDialog(WebviewActivity.this);
                progressdialog.setMessage("Downloading  Farmer App From Server...");
                //progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressdialog.setCancelable(false);
                progressdialog.setIndeterminate(false);
                progressdialog.show();
                return progressdialog;

            default:

                return null;
        }
    }

}
