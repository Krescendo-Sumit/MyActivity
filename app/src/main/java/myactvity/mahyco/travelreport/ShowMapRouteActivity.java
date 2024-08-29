package myactvity.mahyco.travelreport;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import myactvity.mahyco.R;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMapRouteActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    Context context;
    String AllData = "";
    String InTime="";
    String userCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map_route);
        context = ShowMapRouteActivity.this;
        webView = findViewById(R.id.web);
        progressDialog = new ProgressDialog(ShowMapRouteActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        Date entrydate = new Date();
        InTime = new SimpleDateFormat("yyyy-MM-dd").format(entrydate);
        SharedPreferences sp = context.getSharedPreferences("MyPref", 0);
        userCode = sp.getString("UserID", null);
        userCode = userCode.replace(" ", "%20");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("FilterValue", userCode+","+InTime);
            jsonObject.addProperty("FilterOption", "GetGTVByUser&Date");
            getList(jsonObject);
        } catch (Exception exception) {
        }

        // opening the html file in webview

        webView.clearCache(true);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.addJavascriptInterface(this, "KAActivityAPI");

    }

    @JavascriptInterface
    public String getKAActivityLocations() {
   /*

       AlertDialog.Builder builder = new AlertDialog.Builder(ShowMapRouteActivity.this);
        builder.setTitle("Confirmation").setMessage("UserName:\t" + fname +
                "\nPassword:\t" + pswd)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Data Saved Locally", Toast.LENGTH_SHORT).show();
                        // You can use shared preference or db here to store The Data
                    }
                })
        ;
           builder.create().show();
        */


        //AllData = "Hello World!";
       // Toast.makeText(context, "" + AllData, Toast.LENGTH_SHORT).show();
        return AllData;
    }

    void getList(JsonObject jsonObject) {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().GetTravelGTVReportString(jsonObject);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.body() != null)
                        AllData = response.body().toString().trim();
                    else
                        Toast.makeText(ShowMapRouteActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                    webView.loadUrl("https://mahyco-datalens.azurewebsites.net/#/auth/ka-mobile-activity-map");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}