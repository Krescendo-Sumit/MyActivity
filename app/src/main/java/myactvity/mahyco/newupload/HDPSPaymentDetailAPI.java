package myactvity.mahyco.newupload;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HDPSPaymentDetailAPI {
    Context context;
    ProgressDialog progressDialog;
    HDPSPaymentDepositListener newUploadListener;
    public HDPSPaymentDetailAPI(Context context, HDPSPaymentDepositListener newUploadListener) {
        this.context = context;
        this.newUploadListener = newUploadListener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
    }



    public interface HDPSPaymentDepositListener{
       public void onPaymnentDetails(PaymentModel result);
    }

    public void getAllPaymentDetails(JsonObject jsonObject) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<PaymentModel> call = null;
            call = RetrofitClient.getInstance().getMyApi().getHDPSUserwisePaymentDetails(jsonObject);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        PaymentModel result = response.body();
                        try {
                            newUploadListener.onPaymnentDetails(result);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }
    }

    public class PaymentModel {
       boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public Returnval getReturnval() {
            return returnval;
        }

        public void setReturnval(Returnval returnval) {
            this.returnval = returnval;
        }

        String  Message;
        Returnval returnval;
    }
    public class Returnval{
        public List<child> getTable() {
            return Table;
        }

        public void setTable(List<child> table) {
            Table = table;
        }

        List<child> Table;
    }
    public class child{
        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPaystatus() {
            return Paystatus;
        }

        public void setPaystatus(String paystatus) {
            Paystatus = paystatus;
        }

        String Title;//": "Atulbhau Dhurve",
        double amount;//": 1640,
        String date;//": "2024-04-18T12:07:08.513",
        String Paystatus;//": "credit"
    }


}
