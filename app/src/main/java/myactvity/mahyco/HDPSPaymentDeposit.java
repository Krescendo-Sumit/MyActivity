package myactvity.mahyco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Locale;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.newupload.HDPSPaymentDepositAPI;

public class HDPSPaymentDeposit extends AppCompatActivity implements HDPSPaymentDepositAPI.HDPSPaymentDepositListener {
    EditText et_amt;
    Spinner sp_trasactionmode;
    EditText et_trasactionid;
    EditText et_remark;
    Button btn_submit;
    Context context;
    String str_amt, str_trasactionid, str_remark, str_trasactionmode, userCode;
    double amt = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    HDPSPaymentDepositAPI api;
    long mLastClickTime=0;
    int cnt=0;
    TextView textView13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdpspayment_deposit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("HDPS Payment Transfer");
        context = HDPSPaymentDeposit.this;
        api = new HDPSPaymentDepositAPI(context, this);
        et_amt = findViewById(R.id.et_amt);
        sp_trasactionmode = findViewById(R.id.sp_trasactionmode);
        et_trasactionid = findViewById(R.id.et_trasactionid);
        et_remark = findViewById(R.id.et_remark);
        btn_submit = findViewById(R.id.btnsave);
        textView13 = findViewById(R.id.textView13);
        try {
            textView13.setText("" + BuildConfig.VERSION_NAME);
        }catch (Exception e)
        {

        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.isInternetConnected(context)) {



                    Log.i("Clickec","Yes"+(cnt++));

                    submit();
                }else
                {
                    showMessage("Internet connection loss.");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {

        if (validateUI()) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            deposit();

        }

    }

    public void deposit() {
        try {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            String otherData = "-" + versionName + "-" + versionCode;
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("Mdocode", userCode);//: "string",
            jsonObject.addProperty("UPIRefNo", str_trasactionid);//: "string",
            jsonObject.addProperty("Amount", amt);//: 0,
            jsonObject.addProperty("Remark1", str_remark + otherData);//: "string",
            jsonObject.addProperty("Remark2", str_trasactionmode);//: "string"

            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObject);

            api.uploadDepositData(jsonArray);


        } catch (Exception e) {

        }
    }

    public boolean validateUI() {
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        userCode = pref.getString("UserID", null);

        str_amt = et_amt.getText().toString().trim();
        str_trasactionid = et_trasactionid.getText().toString().trim();
        str_remark = et_remark.getText().toString().trim();
        str_trasactionmode = sp_trasactionmode.getSelectedItem().toString().trim();
        if (userCode == null || userCode.trim().equals("")) {
            Toast.makeText(context, "User code is null", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_amt.equals("")) {
            et_amt.setError("Enter Valid Amount.");
            return false;
        }  else if (str_trasactionmode.equals("") || str_trasactionmode.toLowerCase().contains("select")) {
            Toast.makeText(context, "Select Payment APP ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_trasactionid.equals("") || str_trasactionmode.length() < 3) {
            et_trasactionid.setError("Enter Valid Transaction Id.");
            return false;
        } else {
            try {
                amt = Double.parseDouble(str_amt);
                if (amt < 1) {
                    et_amt.setError("Amount should be greater than 0");
                    return false;
                } else {
                    return true;
                }
            }catch(NumberFormatException e)
            {
                et_amt.setError("Valid amount");

                return false;
            }
        }
    }

    public void showMessage(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle("Payment Deposit")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onPaymentDeposit(String result) {
        try {
            if (result != null) {
                JSONObject object = new JSONObject(result.trim());
                if (object.getBoolean("success")) {

                    showMessage("Thank you ! "+object.getString("Message")+".\nWe will validate this transaction.");
                    et_trasactionid.setText("");
                    et_amt.setText("");
                    et_remark.setText("");
                    sp_trasactionmode.setSelection(0);
                } else {
                    showMessage(object.getString("Message"));
                }

            } else {
                showMessage("" + result);
            }

        } catch (Exception e) {
            showMessage("Data is not is proper format.");
        }
    }
}