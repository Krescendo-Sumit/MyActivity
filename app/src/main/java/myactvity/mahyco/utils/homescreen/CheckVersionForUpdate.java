package myactvity.mahyco.utils.homescreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import myactvity.mahyco.newupload.HDPSPaymentDetailAPI;
import myactvity.mahyco.retro.RetrofitClient;
import myactvity.mahyco.travelreport.ActivityTravelReportTriggered;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckVersionForUpdate {

    Context context;
    ProgressDialog progressDialog;
    CheckVersionForUpdateListener newUploadListener;

    public CheckVersionForUpdate(Context context, CheckVersionForUpdateListener newUploadListener) {
        this.context = context;
        this.newUploadListener = newUploadListener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
    }


    public interface CheckVersionForUpdateListener {
        public void onResultVersionCode(String result);
    }

    public void checkVersion(String packageName, String userCode, String IMEICode) {
        try {
            Log.i("pass ","1");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing())
                progressDialog.show();
            Log.i("pass ","2");
            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().getAppVersion(packageName, userCode, IMEICode);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String result = response.body();
                        try {
                            Log.i("pass ","3");
                            newUploadListener.onResultVersionCode(result);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

}
