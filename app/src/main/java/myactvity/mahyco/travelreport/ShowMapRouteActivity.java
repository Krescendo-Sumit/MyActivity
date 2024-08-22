package myactvity.mahyco.travelreport;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map_route);
        context = ShowMapRouteActivity.this;
        webView = findViewById(R.id.web);
        progressDialog = new ProgressDialog(ShowMapRouteActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserCode", "97261227");
            getList(jsonObject);
        } catch (Exception exception) {
            Toast.makeText(context, "Error is 123 " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // opening the html file in webview
        webView.loadUrl("http://krescendo.co.in/maproute.html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.addJavascriptInterface(this, "Dialog");

    }

    @JavascriptInterface
    public String showMsg(String fname, String pswd) {
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
        return AllData;
    }

    void getList(JsonObject jsonObject) {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            Log.i("Pass","1");
            call = RetrofitClient.getInstance().getMyApi().GetTravelGTVReportString(jsonObject);
            Log.i("Pass","2");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.body() != null)
                        AllData = response.body().toString().trim();
                    else
                        Toast.makeText(ShowMapRouteActivity.this, "No Reponse", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}