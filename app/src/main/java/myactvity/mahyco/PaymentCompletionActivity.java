package myactvity.mahyco;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
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
import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.SqliteDatabase;


public class PaymentCompletionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REQUIRED = "Required";
    EditText et_phone, et_otp;

    Activity activity;
    String TAG = "PaymentCompletionActivity";
    private CardView card;
    private RelativeLayout relativeLayout;
    Context context;
    LottieAnimationView animationView1;
    String usercode = "";
    String orderQuantity = "";
    String amount, paymentId, farmersIdList, paymentStatus;
    TextView noorder;
    TextView txtreceiverName;
    TextView txtAddress;
    TextView txtcity;
    TextView txtpin;
    TextView txtshippingMobile;
    TextView txtorderId;
    TextView txtamount;
    TextView txtpayMode;
    TextView txtTopOrderStatus;
    TextView txtOrderStatus;
    TextView textdate;
    // ArrayList<BookOrder> bookOrderArrayList = new ArrayList<>();
    RecyclerView recycler;
    RelativeLayout relData;
    // CardviewBookOrderAdapter cardviewOrderHistoryAdapter;
    Button button_submit;
    ImageView backbtn;
    double lat = 0, lng = 0;
    CommonExecution cx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_completion);
        activity = this;
        context = this;
        cx = new CommonExecution(context);
        inIt();
        usercode = getIntent().getStringExtra("usercode");
        amount = getIntent().getStringExtra("amount");
        paymentId = getIntent().getStringExtra("rzpPaymentId");
        farmersIdList = getIntent().getStringExtra("farmersIdList");
        paymentStatus = getIntent().getStringExtra("status");
        Config config=new Config(context);
//        if(config.NetworkConnection())
//        new SyncPaymentStatus(0, "").execute();

//        else Utility.showAlertDialog("Error","No internet connection is available,Payment status will be updated once internet is back",context);
        //  submitOrder();
    }


    private void inIt() {
        backbtn = (ImageView) findViewById(R.id.backbtn);
        //card = (android.support.v7.widget.CardView) findViewById(R.id.card);

        animationView1 = (LottieAnimationView) findViewById(R.id.lottieAnimationView1);
        // relData = (RelativeLayout) findViewById(R.id.relData);
        button_submit = (Button) findViewById(R.id.button_submit);
        //  recycler = (RecyclerView) findViewById(R.id.recyclerList);
        txtTopOrderStatus = (TextView) findViewById(R.id.txtTopOrderStatus);
        txtreceiverName = (TextView) findViewById(R.id.txtreceiverName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtcity = (TextView) findViewById(R.id.txtcity);
        txtpin = (TextView) findViewById(R.id.txtpin);
        txtshippingMobile = (TextView) findViewById(R.id.txtshippingMobile);

        txtorderId = (TextView) findViewById(R.id.txtorderId);
        txtpayMode = (TextView) findViewById(R.id.txtpayMode);
        //txtamount = (TextView) findViewById(R.id.txtamount);
        txtOrderStatus = (TextView) findViewById(R.id.txtOrderStatus);
        textdate = (TextView) findViewById(R.id.textdate);

        final LinearLayoutManager lllm = new LinearLayoutManager(this);
        lllm.setOrientation(LinearLayoutManager.VERTICAL);
//        recycler.setLayoutManager(lllm);
        button_submit.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backbtn:
                onBackPressed();
                break;
            case R.id.button_submit:
                Intent intent = new Intent(this, UserHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        //startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateUI() {
        // txtamount.setText(bookOrderArrayList.get(0).getTotalAmount());


    }

    private class SyncPaymentStatus extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String Offline;
        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCompletionActivity.this);


        public SyncPaymentStatus(int action, String Offline) {

            this.action = action;
            this.Offline = Offline;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(PaymentCompletionActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SendPaymentDetails"));
            postParameters.add(new BasicNameValuePair("rzpPaymentId", paymentId));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("farmersList", farmersIdList));
            postParameters.add(new BasicNameValuePair("status", paymentStatus));
            postParameters.add(new BasicNameValuePair("amount", amount));

            String Urlpath1 = cx.MDOurlpath + "?action=" + "";
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackground: " + postParameters.toString());
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();

                pd.dismiss();
            } catch (Exception e) {

                e.printStackTrace();
                pd.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                Log.d("SyncPaymentStatus", "onPostExecute: " + result);
                pd.dismiss();
                if (result.contains("Table")) {
                    setAnimation();
                    // farmerPaymentModelArrayList.clear();
                    JSONObject object = new JSONObject(result);
                     JSONArray jArray = object.getJSONArray("Table");
                    double amount = 0;
                   // for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(0);
                        String orderId=  jObject.getString("orderID");
                        String paymentID=  jObject.getString("paymentID");

                    SqliteDatabase database = SqliteDatabase.getInstance(context);
                    database.updatePaymentTable(paymentID,orderId);

                } else {
                    Utility.showAlertDialog("Error", result, context);
                  //  setAnimation();
                }
                //   lblusername.setText(result) ;


            } catch (Exception e) {
                e.printStackTrace();
                Utility.showAlertDialog("Error1", result, context);
                pd.dismiss();
           //     setAnimation();
            }

        }
    }

    public void setAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView1.setProgress((Float) valueAnimator.getAnimatedValue());
            }

        });
        animator.start();
        animator.setRepeatCount(30);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        animationView1.cancelAnimation();
        animationView1.clearAnimation();
    }
}
