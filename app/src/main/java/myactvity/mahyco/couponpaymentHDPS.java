package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.CheckBoxAnimated;
import myactvity.mahyco.utils.FarmerPaymentModel;

public class couponpaymentHDPS extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PaymentResultWithDataListener {
    public ArrayList<FarmerPaymentModel> farmerPaymentModelArrayList = new ArrayList<FarmerPaymentModel>();
    Activity activity;
    private static final int SELECT_PICTURE = 1;

    SharedPreferences sp;
    public static final String MY_PREF = "MyPreferences";
    public static final String TAG = "couponpaymentHDPS";
    /**
     * Created by Admin on 04-05-2015.
     */
    SharedPreferences locdata;
    //public String SERVER = "https://cmr.mahyco.com/farmmechHandler.ashx";
    public Messageclass msclass;

    static ImageView expandedImageView;
    ListView list;
    CustomAdapter adapter;

    TextView txtNoUploads;
    TextView txt_total_with_tax;
    TextView txt_selectedCount;
    String usercode;
    CommonExecution cx;
    CheckBoxAnimated cb_delete_all;
    TextView txt_delete;

    private SwipeRefreshLayout swipeRefreshLayout;
    CheckBox checkbox;
    RelativeLayout rel_delt_all;
    RelativeLayout rel_records;

    Config config;
    BottomSheetBehavior sheetBehavior;
    private SqliteDatabase mDatabase;

    RelativeLayout bottom_sheet;
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView imgArrowBottom;
    SharedPreferences.Editor loceditor;
    Button btn_order_action;
    JSONArray farmerListJsonArray;
    SharedPreferences pref;
    String rzpOrderId;
    String actualAmountInRupees;
    public ProgressDialog dialog;
    private long mLastClickTime = 0;
    Prefs mPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_couponpayment_h_d_p_s);
        getSupportActionBar().hide(); //<< this

        mPref = Prefs.with(this);

        rel_delt_all = (RelativeLayout) findViewById(R.id.rel_delt_all);
        rel_records = (RelativeLayout) findViewById(R.id.rel_records);
        cb_delete_all = (CheckBoxAnimated) findViewById(R.id.cb_delete_all);
        txt_delete = (TextView) findViewById(R.id.txt_delete);
        list = (ListView) findViewById(R.id.list);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        config = new Config(this); //Here the context is passing
        mDatabase = SqliteDatabase.getInstance(this);

        context = this;
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        imgArrowBottom = (ImageView) findViewById(R.id.imgArrowBottom);
        btn_order_action = (Button) findViewById(R.id.btn_order_action);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        //loceditor = locdata.edit();
        usercode = pref.getString("UserID", null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.QRCodeBlackColor, R.color.colorPrimary2);
        swipeRefreshLayout.setOnRefreshListener(this);

        txtNoUploads = (TextView) findViewById(R.id.No_uploads);
        txt_selectedCount = (TextView) findViewById(R.id.txt_selectedCount);
        txt_total_with_tax = (TextView) findViewById(R.id.txt_total_with_tax);

        txt_delete = (TextView) findViewById(R.id.txt_delete);
        cb_delete_all = (CheckBoxAnimated) findViewById(R.id.cb_delete_all);
        rel_delt_all = (RelativeLayout) findViewById(R.id.rel_delt_all);
        bottom_sheet = (RelativeLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Checkout.preload(getApplicationContext());
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (list == null || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });
        btn_order_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object[] values = isAnyItemSelected();
                int count = 0;

                if (values != null && values.length > 0) {
                    count = (Integer) values[0];

                }
                if (count > 0) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    fetchOrderIdfromRazorPay();


                } else {

                    Utility.showAlertDialog("Error", "Please select atleast one farmer to pay", context);
                }
            }
        });


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        imgArrowBottom.setRotation(90);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        imgArrowBottom.setRotation(270);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        getFarmersListFromserver();

    }

    private void fetchOrderIdfromRazorPay() {


        if (config.NetworkConnection()) {
            try {
                dialog.setMessage("Loading....wait");
                dialog.show();
                RazorpayClient razorpay = new RazorpayClient(getResources().getString(R.string.rzp_key), getString(R.string.rzp_key_secret));

                JSONObject orderRequest = new JSONObject();


                String amt = txt_total_with_tax.getText().toString();
                amt = amt.equals("") ? "0" : amt;

                actualAmountInRupees = amt;
                String amountInPaisa = String.valueOf(Double.parseDouble(amt) * 100);


                double doubleValue = Double.parseDouble(amountInPaisa);
                int amountValue = (int) doubleValue;


                JSONObject notes = new JSONObject();
                notes.put("usercode", usercode);
                notes.put("farmersList", farmerListJsonArray.toString());

                orderRequest.put("amount", amountValue); // amount in paise
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "test_1");
                orderRequest.put("payment_capture", 1);

                Log.d("REQUESTED ORDERID", orderRequest.toString());
                Order order = razorpay.Orders.create(orderRequest);
                JSONObject jsonObject = new JSONObject(String.valueOf(order));


                Log.d("FROM ORDERIDRZR", jsonObject.toString());
                String id = jsonObject.getString("id");

                Log.d("REQUESTED ORDERID", id);

                new initialOrderServerResponse(1, "orderCompletion", actualAmountInRupees, id, "", "").execute();


            } catch (RazorpayException e) {
                System.out.println(e.getMessage());
                dialog.dismiss();
                Utility.showAlertDialog("Error", e.getMessage(), context);

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        } else {

            Utility.showAlertDialog("Error", "Please Connect to Internet", context);


        }

    }

    private class initialOrderServerResponse extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;
        String rzp_payment_status;
        String response;
        String rzpOrderId;
        String razorPaymentId;
        String actualAmount;


        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        public initialOrderServerResponse(int action, String function, String actualAmount, String rzpOrderId, String rzp_payment_status, String razorPaymentId) {

            this.action = action;
            this.function = function;
            this.rzpOrderId = rzpOrderId;
            this.rzp_payment_status = rzp_payment_status;
            this.razorPaymentId = razorPaymentId;
            this.actualAmount = actualAmount;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();


        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            /*Start API Call*/
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObjchild = new JSONObject();
                jsonObjchild.put("action", "1");
                jsonObjchild.put("usercode", usercode);
                jsonObjchild.put("rzpOrderId", rzpOrderId);
                jsonObjchild.put("amount", actualAmount);
                jsonObjchild.put("rzpPaymentStatus", rzp_payment_status);
                JSONArray array = new JSONArray();

                /*for(int i=0;i<farmerPaymentModelArrayList.size();i++){
                    JSONObject obj = new JSONObject();
                    obj.put("couponcode",farmerPaymentModelArrayList.get(i).getCouponcode());
                    obj.put("farmerid",farmerPaymentModelArrayList.get(i).getFarmerId());
                    array.put(obj);
                }*/
                for(int i=0;i<farmerListJsonArray.length();i++){
                    JSONObject obj = new JSONObject();
                    JSONObject c = farmerListJsonArray.getJSONObject(i);
                    obj.put("couponcode",c.get("couponcode"));
                    obj.put("farmerid",c.get("farmerid"));
                    array.put(obj);
                }

                String farmerList = (array.toString()).replaceAll("\\\\", "");
                Log.d("initialOrderServerRespo", "farmerList : " + farmerList);
                jsonObjchild.put("farmersList",new JSONArray(farmerList));


                jsonObjchild.put("rzpPaymentData", "");
                jsonObjchild.put("error", "");
                jsonObject.put("Table", jsonObjchild);
                Log.d("initialOrderServerRespo", "URL : " + Constants.COUPON_ORDER_COMPLETION_HDPS_API);
                Log.d("initialOrderServerRespo", "JSON OBJECT : " + jsonObject.toString());
                result = HttpUtils.POSTJSON(Constants.COUPON_ORDER_COMPLETION_HDPS_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
            /*End API*/


            /*HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "orderCompletion"));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("rzpOrderId", rzpOrderId));
            postParameters.add(new BasicNameValuePair("farmersList", farmerListJsonArray.toString()));
            postParameters.add(new BasicNameValuePair("amount", actualAmount));
            postParameters.add(new BasicNameValuePair("rzpPaymentStatus", rzp_payment_status));
            String Urlpath1 = cx.MDOurlpath + "?action=" + action;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackgroundinitialOrderServerResponse: " + postParameters.toString());

            Log.d(TAG, "doInBackgroundinitialOrderServerUrl " + Urlpath1);
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

            return builder.toString();*/
        }

        protected void onPostExecute(String result) {
            try {
                Log.d("ResponseInitialOrder", "onPostExecute: " + result);
                pd.dismiss();
                dialog.dismiss();
                redirecttoRegisterActivity(result);
                if (result.contains("Table")) {


                    String initialServerOrderId = "";
                    String initialrzpOrderId = "";

                    try {
                        if (result.contains("True")) {
                            JSONObject object = new JSONObject(result);
                            JSONArray jArray = object.getJSONArray("Table");

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);
                                initialServerOrderId = jObject.getString("orderID");
                                initialrzpOrderId = jObject.getString("rzpOrderId");
                                Log.d("jObject", "onPostExecute: " + initialServerOrderId + "||" + initialrzpOrderId);
                            }
                            if (!initialServerOrderId.isEmpty() && !initialrzpOrderId.isEmpty()) {
                                initiatePayment(initialServerOrderId, initialrzpOrderId);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("ResponseInitialOrder", "onPostExecute: " + result);

                    String serverOrderID = "fromResponse";

                } else {


                }

            } catch (
                    Exception e) {
                e.printStackTrace();
                Utility.showAlertDialog("Error1", result, context);
                pd.dismiss();
            }

        }
    }

    public void initiatePayment(String orderId, String rzp_order_id) {


        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Mahyco");
            options.put("description", "Demoing Charges");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            String amt = txt_total_with_tax.getText().toString();
            amt = amt.equals("") ? "0" : amt;
            actualAmountInRupees = amt;
            rzpOrderId = rzp_order_id;
            String amountInPaisa = String.valueOf(Double.parseDouble(amt) * 100);
            options.put("amount", amountInPaisa);
            options.put("order_id", rzp_order_id);
            options.put("server_order_id", orderId);
            options.put("usercode", usercode);
            //String farmerList = (farmerPaymentModelArrayList.toString()).replaceAll("\\\\", "");
            options.put("farmersList", farmerListJsonArray.toString());
            JSONObject preFill = new JSONObject();
            JSONObject notes = new JSONObject();
            notes.put("usercode", usercode);
            notes.put("server_order_id", orderId);
            options.put("theme", new JSONObject("{color: '#38ef7d'}"));
            options.put("prefill", preFill);
            Log.d("paramsPayment", options.toString());
            co.setFullScreenDisable(true);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData data) {
        try {

            if (data != null) {

                Log.d(TAG, "onPaymentSuccess: " + razorpayPaymentID);
                Log.d(TAG, "onPaymentSuccess:getData " + razorpayPaymentID + "::" + data.getData().toString());
                String successData = (data.getData().toString()).replaceAll("\\\\", "");
                Log.d("onPaymentSuccess", "onPaymentSuccess : " + successData);
                JSONObject error = new JSONObject();
                new couponpaymentHDPS.FinalOrderServerResponse(2, "orderCompletion", "success", "", "", error.toString(), data.getPaymentId(), successData).execute();

            }

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @Override
    public void onPaymentError(int code, String response, PaymentData data) {
        Log.d(TAG, "onPaymentError: i::" + code + "::s::" + response + "::" + data.getData().toString());
        try {
            JSONObject error = new JSONObject();
            error.put("errorcode", code);
            error.put("errormessage", response);
            error.put("errorDetails", data.getData());
            String erroData = (data.getData().toString()).replaceAll("\\\\", "");
            Log.d("onPaymentError", "erroData : " + erroData);
            new couponpaymentHDPS.FinalOrderServerResponse(2, "orderCompletion", "failed", String.valueOf(code), response, error.toString(), "", data.getData().toString()).execute();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private class FinalOrderServerResponse extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function;
        String rzp_payment_status;
        String razorPaymentId;
        String errorData;
        String errorcode;
        String response;
        String data;

        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        public FinalOrderServerResponse(int action, String function, String rzp_payment_status, String errorcode, String response, String errorData, String razorPaymentId, String data) {

            this.action = action;
            this.function = function;
            this.rzp_payment_status = rzp_payment_status;
            this.razorPaymentId = razorPaymentId;
            this.errorData = errorData;
            this.errorcode = errorcode;
            this.response = response;
            this.data = data;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            /*Start API Call*/
            String result = "";
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObjchild = new JSONObject();
                jsonObjchild.put("action", action);
                jsonObjchild.put("usercode", usercode);
                jsonObjchild.put("rzpOrderId", rzpOrderId);
                jsonObjchild.put("amount", actualAmountInRupees);
                jsonObjchild.put("rzpPaymentStatus", rzp_payment_status);
                JSONArray array = new JSONArray();
                for(int i=0;i<farmerPaymentModelArrayList.size();i++){
                    JSONObject obj = new JSONObject();
                    obj.put("couponcode",farmerPaymentModelArrayList.get(i).getCouponcode());
                    obj.put("farmerid",farmerPaymentModelArrayList.get(i).getFarmerId());
                    array.put(obj);
                }
                String farmerList = (array.toString()).replaceAll("\\\\", "");
                Log.d("FinalOrderServerRespo", "farmerList : " + farmerList);
                jsonObjchild.put("farmersList", new JSONArray(farmerList));
                jsonObjchild.put("rzpPaymentData", new JSONObject(data));
                jsonObjchild.put("error", errorData);
                jsonObject.put("Table", jsonObjchild);
                Log.d("FinalOrderServerRespo", jsonObject.toString());
                Log.d("FinalOrderServerRespo", "URL : "+Constants.COUPON_ORDER_COMPLETION_HDPS_API);

                Log.d("FinalOrderServer", jsonObject.toString());
                result = HttpUtils.POSTJSON(Constants.COUPON_ORDER_COMPLETION_HDPS_API, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
            /*End API*/

            /*HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "orderCompletion"));
            postParameters.add(new BasicNameValuePair("usercode", usercode));
            postParameters.add(new BasicNameValuePair("farmersList", farmerListJsonArray.toString()));
            postParameters.add(new BasicNameValuePair("rzpOrderId", rzpOrderId));
            postParameters.add(new BasicNameValuePair("amount", actualAmountInRupees));
            postParameters.add(new BasicNameValuePair("rzpPaymentStatus", rzp_payment_status));
            postParameters.add(new BasicNameValuePair("rzpPaymentData", data));
            postParameters.add(new BasicNameValuePair("error", errorData));

            String Urlpath1 = cx.MDOurlpath + "?action=" + action;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackgroundFinalOrderServerResponse: " + postParameters.toString());

            Log.d(TAG, "doInBackgroundFinalOrderUrl " + Urlpath1);
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

            return builder.toString();*/
        }

        protected void onPostExecute(String result) {
            try {
                Log.d("SyncPaymentStatus", "onPostExecute: " + result);
                pd.dismiss();

                if (result.contains("Table") && rzp_payment_status.contains("success")) {
                    Intent intent = new Intent(getApplicationContext(), PaymentCompletionActivity.class);
                    intent.putExtra("usercode", usercode);
                    intent.putExtra("rzpPaymentId", razorPaymentId);
                    intent.putExtra("farmersIdList", farmerListJsonArray.toString());
                    intent.putExtra("status", "1");
                    intent.putExtra("amount", txt_total_with_tax.getText().toString());
                    startActivity(intent);


                } else if (result.contains("Node")) {
                    String message = "";

                    try {


                        JSONObject object = new JSONObject(result);
                        JSONArray jArray = object.getJSONArray("Node");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            message = jObject.getString("Message");


                        }
                        Log.d("jObjectMessage", "onPostExecute:Message " + message);
                        Utility.showAlertDialog("Sync Server failed", message, context);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    if (rzp_payment_status.contains("failed")) {


                        try {
                            if (Checkout.NETWORK_ERROR == Integer.parseInt(errorcode)) {

                                Toast.makeText(couponpaymentHDPS.this, "Payment failed: NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                                Utility.showAlertDialog("Payment failed: NETWORK_ERROR", response, context);
                            } else if (Checkout.PAYMENT_CANCELED == Integer.parseInt(errorcode)) {

                                Toast.makeText(couponpaymentHDPS.this, "Payment failed: CANCELLED", Toast.LENGTH_SHORT).show();
                                Utility.showAlertDialog("Payment failed: CANCELLED", response, context);


                            } else if (Checkout.TLS_ERROR == Integer.parseInt(errorcode)) {

                                Toast.makeText(couponpaymentHDPS.this, "Payment failed: TLS_ERROR", Toast.LENGTH_SHORT).show();
                                Utility.showAlertDialog("Payment failed: TLS_ERROR", result, context);


                            } else {
                                Toast.makeText(couponpaymentHDPS.this, "Payment failed: " + Integer.parseInt(errorcode) + " " + response, Toast.LENGTH_SHORT).show();
                                Utility.showAlertDialog("Payment failed:" + Integer.parseInt(errorcode) + " ", response, context);

                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Exception in onPaymentError", e);
                        }
                    } else {
                        Utility.showAlertDialog("Error", result, context);
                    }
                }

            } catch (
                    Exception e) {
                e.printStackTrace();
                Utility.showAlertDialog("Error1", result, context);
                pd.dismiss();
            }

        }
    }


    public void setListData() {
        Resources res = getResources();
        list = (ListView) findViewById(R.id.list);
        if (adapter == null) {
            adapter = new couponpaymentHDPS.CustomAdapter(couponpaymentHDPS.this, farmerPaymentModelArrayList, res);

            list.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
    }

    public Object[] isAnyItemSelected() {
        int counter = 0;
        double amount = 0;
        farmerListJsonArray = new JSONArray();
        try {
            for (int i = 0; i < farmerPaymentModelArrayList.size(); i++) {
                if (farmerPaymentModelArrayList.get(i).isChecked()) {

                    amount = amount + Double.parseDouble(!farmerPaymentModelArrayList.get(i).getAmount().equals("")
                            ? farmerPaymentModelArrayList.get(i).getAmount() : "0");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("couponcode", farmerPaymentModelArrayList.get(i).getCouponcode());
                    jsonObject.put("farmerid", farmerPaymentModelArrayList.get(i).getFarmerId());
                    farmerListJsonArray.put(jsonObject);
                    counter++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{counter, amount};
    }


    public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

        public Resources res;
        FarmerPaymentModel tempValues = null;
        int i = 0;
        //DatabaseAdapter db;
        String imagePath, imagePathdelete;
        /**********
         * Declare Used Variables
         *********/
        private Activity activity;
        private ArrayList data;
        private LayoutInflater inflater = null;

        /************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(Activity a, ArrayList d, Resources resLocal) {
            //db = new DatabaseAdapter(FarmerPaymentActivity.this.getBaseContext());
            /********** Take passed values **********/
            activity = a;
            data = d;
            res = resLocal;
            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /*******
         * What is the size of Passed Arraylist Size
         ************/
        public int getCount() {

            if (data.size() <= 0)
                return 1;
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        /*********
         * Depends upon data size called for each row , Create each ListView row
         ***********/
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            View vi = convertView;
            final couponpaymentHDPS.CustomAdapter.ViewHolder holder;

            if (convertView == null) {

                /********** Inflate tabitem.xml file for each row ( Defined below ) ************/
                vi = inflater.inflate(R.layout.tabitem, null);
            }
            /******** View Holder Object to contain tabitem.xml file elements ************/
            holder = new couponpaymentHDPS.CustomAdapter.ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);
            holder.cb = (CheckBoxAnimated) vi.findViewById(R.id.scb);
            holder.txtCardId = (TextView) vi.findViewById(R.id.txtCardId);
            holder.txtAmount = (TextView) vi.findViewById(R.id.txtAmount);
            holder.txtSerial = (TextView) vi.findViewById(R.id.txtSerial);
            holder.txtMobilenumber = (TextView) vi.findViewById(R.id.txtMobilenumber);
            holder.txtNoOfCoupon = (TextView) vi.findViewById(R.id.txtNoOfCoupon);
            holder.txtVillage = (TextView) vi.findViewById(R.id.txtVillage);
            holder.txtProduct = (TextView) vi.findViewById(R.id.txtProduct);


            // holder.tv_image_used = (TextView) vi.findViewById(R.id.tv_image_used);
            // holder.bttn_delete = (ImageView) vi.findViewById(R.id.bttn_delete);
            holder.card_view = (CardView) vi.findViewById(R.id.card_view);
            holder.rel_top = (RelativeLayout) vi.findViewById(R.id.rel_top);
            holder.card_view.setCardBackgroundColor(getResources().getColor(R.color.lightgrey));


            if (data.size() <= 0) {
                //list.setVisibility(View.GONE);
                rel_records.setVisibility(View.GONE);
                txtNoUploads.setVisibility(View.VISIBLE);
            } else {
                rel_records.setVisibility(View.VISIBLE);
                txtNoUploads.setVisibility(View.GONE);
                /***** Get each Model object from Arraylist ********/
                tempValues = null;
                tempValues = (FarmerPaymentModel) data.get(position);

                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getName());
                holder.txtAmount.setText(tempValues.getAmountText());
                holder.txtSerial.setText(String.valueOf(position + 1));
                holder.txtCardId.setText(tempValues.getCardId());
                holder.txtProduct.setText(tempValues.getproduct());

                holder.txtMobilenumber.setText(tempValues.getmobileNumber());
                holder.txtNoOfCoupon.setText(tempValues.gettotalCoupon());
                holder.txtVillage.setText(tempValues.getvillagename());


                vi.setOnLongClickListener(new couponpaymentHDPS.OnItemLongClickListener(position, vi));
                vi.setOnClickListener(new couponpaymentHDPS.OnItemClickListener(position, vi));

                vi.setTag(tempValues.getCardId());
            }

            if (farmerPaymentModelArrayList != null && farmerPaymentModelArrayList.size() > 0) {
                final FarmerPaymentModel bean = farmerPaymentModelArrayList.get(position);
                holder.cb.setOnCheckedChangeListener(new CheckBoxAnimated.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CheckBoxAnimated checkBox, boolean isChecked) {

                        bean.setChecked(isChecked);
                        Object[] values = isAnyItemSelected();
                        int count = 0;
                        double amount = 0;

                        if (values != null && values.length > 0) {
                            count = (Integer) values[0];
                            amount = (double) values[1];
                        }
                        if (count > 0) {
                            txt_selectedCount.setText("" + count);
                            rel_delt_all.setVisibility(View.VISIBLE);
                        } else rel_delt_all.setVisibility(View.GONE);

                        txt_total_with_tax.setText(String.valueOf(amount));
                    }


                });

                holder.cb.setChecked(bean.isChecked());
            }

            return vi;
        }

        @Override
        public void onClick(View v) {
            Log.v("CustomAdapter", "=====Row button clicked");
        }

        /********
         * Create a holder to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text, txtCardId, txtAmount, txtSerial, txtNoOfCoupon, txtVillage, txtMobilenumber, txtProduct;
            public ImageView image;
            public ImageView bttn_delete;
            public CardView card_view;
            public RelativeLayout rel_top;
            public CheckBoxAnimated cb;


        }

        /*******
         * Called when Item click in ListView
         ************/
    }

    private class OnItemLongClickListener implements View.OnLongClickListener {
        private int mPosition;

        OnItemLongClickListener(int position, View view) {
            mPosition = position;
        }


        @Override
        public boolean onLongClick(View v) {
            //  Toast.makeText(FarmerPaymentActivity.this, "ddddddd", Toast.LENGTH_SHORT).show();


            FarmerPaymentModel bean = farmerPaymentModelArrayList.get(mPosition);
            bean.setChecked(!bean.isChecked());
            CheckBoxAnimated checkBox = (CheckBoxAnimated) v.findViewById(R.id.scb);
            checkBox.setChecked(bean.isChecked(), true);

            Object[] values = isAnyItemSelected();
            int count = 0;
            double amount = 0;

            if (values != null && values.length > 0) {
                count = (Integer) values[0];
                amount = (double) values[1];
            }

            if (count > 0) {
                txt_selectedCount.setText("" + count);
                rel_delt_all.setVisibility(View.VISIBLE);
            } else {
                rel_delt_all.setVisibility(View.GONE);
            }
            txt_total_with_tax.setText(String.valueOf(amount));
            Log.v("long clicked", "pos: " + mPosition);
            return false;
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position, View view) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            // callFragment(mPosition);}

            // Toast.makeText(context, "Pos" + mPosition, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRefresh() {
        // Toast.makeText(this, "Nothing to refresh.", Toast.LENGTH_SHORT).show();

        // if (farmerPaymentModelArrayList != null && farmerPaymentModelArrayList.size() > 0) {
        if (config.NetworkConnection())
            getFarmersListFromserver();
        else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(couponpaymentHDPS.this, "No Internet is available", Toast.LENGTH_SHORT).show();
        }
        // } else {Toast.makeText(this, "Nothing to refresh.", Toast.LENGTH_SHORT).show();}
    }

    public void getFarmersListFromserver() {
        if (config.NetworkConnection()) {

            /*new couponpaymentHDPS.GetFarmerData(1, "").execute(SERVER,
                    usercode, "PaymentProcess");*/
            //last is 1 action now it 5
            new couponpaymentHDPS.GetFarmerData(5, "").execute("",
                    usercode, "PaymentProcess");
        } else {
            Utility.showAlertDialog("Error", "Connect To Internet", context);
            onBackPressed();


        }
    }

    private class GetFarmerData extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String Offline;
        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(couponpaymentHDPS.this);


        public GetFarmerData(int action, String Offline) {

            this.action = action;
            this.Offline = Offline;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(couponpaymentHDPS.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            /*API START*/
            try {
                //JSONObject jsonObject = new JSONObject();
                JSONObject jsonObjchild = new JSONObject();
                jsonObjchild.put("action", action);
                jsonObjchild.put("userCode", usercode);
                /*jsonObjchild.put("rzpOrderId", rzpOrderId);
                jsonObject.put("Table",jsonObjchild);*/
                Log.d("getdistributorlist", jsonObjchild.toString());
                result = HttpUtils.POSTJSON(Constants.COUPON_FARMER_LIST_HDPS_API, jsonObjchild, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
            /*API End*/

            /*HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "Couponfarmerlist"));
            //  postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1 = cx.MDOurlpath + "?action=" + action + "&usercode=" + usercode;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackgroundGetFarmerData: " + Urlpath1);
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
                // msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            } catch (Exception e) {
                // msclass.showMessage(e.getMessage());
                e.printStackTrace();
                pd.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();*/

        }

        protected void onPostExecute(String result) {
            try {
                redirecttoRegisterActivity(result);
                Log.d("GetFarmerData", "onPostExecute: " + result);
                swipeRefreshLayout.setRefreshing(false);
                pd.dismiss();
                redirecttoRegisterActivity(result);
                if (result.contains("true")) {
                    farmerPaymentModelArrayList.clear();
                    JSONObject object = new JSONObject(result);
                    JSONArray jArray = object.getJSONArray("farmerCouponList"); /*Table changed on 11th May*/
                    double amount = 0;
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        FarmerPaymentModel farmerPaymentModel = new FarmerPaymentModel();
                        farmerPaymentModel.setName(jObject.getString("farmerName"));
                        String amountInRuppee = !jObject.isNull("amount") ? jObject.getString("amount") : "0";
                        farmerPaymentModel.setAmount(amountInRuppee);
                        farmerPaymentModel.setAmountText("AMOUNT IN RUPPEES :" + amountInRuppee);
                        // farmerPaymentModel.setSerialNo(String.valueOf(i+1));
                        farmerPaymentModel.setFarmerId(jObject.getString("farmerid"));
                        //farmerPaymentModel.setCardId(jObject.getString("id"));
                        farmerPaymentModel.setCardId(jObject.getString("product"));
                        farmerPaymentModel.setCouponcode(jObject.getString("couponcode"));
                        farmerPaymentModel.setmobileNumber("MOBILE NUMBER :" + jObject.getString("mobileNumber"));
                        farmerPaymentModel.setvillagename("VILLAGE :" + jObject.getString("village"));
                        farmerPaymentModel.settotalCoupon("NO OF COUPONS :" + jObject.getString("totalCoupon"));
                        farmerPaymentModel.setproduct("PRODUCT NAME :" + jObject.getString("product"));
                        farmerPaymentModel.setCrop("Crop :" + jObject.getString("crop"));
                        farmerPaymentModel.setChecked(false);

                        farmerPaymentModelArrayList.add(farmerPaymentModel);
                        amount = amount + Double.parseDouble(farmerPaymentModelArrayList.get(i).getAmount() != null && !farmerPaymentModelArrayList.get(i).getAmount().equals("")
                                ? farmerPaymentModelArrayList.get(i).getAmount() : "0");
                    }
                    //St
                    /*TODO NOT COMING*/
                    /*JSONArray jArray2 = object.getJSONArray("Table1");
                    for (int i = 0; i < jArray2.length(); i++) {
                        JSONObject jObject2 = jArray2.getJSONObject(i);
                        mDatabase.deleledata("couponPaymentAmount", " ");
                        mDatabase.InsertCouponPaymentAmount(jObject2.getString("paymentAmount").toString(),
                                jObject2.getString("remainingAmount").toString());
                    }*/
                    //end
                    txt_total_with_tax.setText(String.valueOf(amount) == "" ? "0" : String.valueOf(amount));
                    setListData();
                } else {
                    //msclass.showMessage(result); /*TO DO Send Custom Message*/
                    msclass.showMessage("Something went wrong, please try later.");
                }
                //   lblusername.setText(result) ;


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                pd.dismiss();
            }
        }
    }

    public void redirecttoRegisterActivity(String result) {
        if (result.toLowerCase().contains("authorization")) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("MyActivity");
            builder.setMessage("Your login session is  expired, please register user again. ");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent1 = new Intent(context.getApplicationContext(), UserRegister.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);
                }
            });
            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }


}
