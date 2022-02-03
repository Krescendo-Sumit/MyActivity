package myactvity.mahyco;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Constants;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.utils.PaymentHistoryOrderDetailModel;
import myactvity.mahyco.utils.PaymentHistoryTableModel;

public class PaymentHistoryActivityHDPS extends AppCompatActivity {

    RecyclerView recyclerViewPaymentHistoryList;
    ProgressDialog pd;
    private Context context;
    CommonExecution cx;
    public String MDOurlpath = "";
    Config config;
    SharedPreferences pref;
    CardviewOrderHistoryAdapter cardviewOrderHistoryAdapter;
    ArrayList<String> arrayListStudentList = new ArrayList<>();
    public ArrayList<PaymentHistoryTableModel> paymentHistoryTableModelArrayList = new ArrayList<PaymentHistoryTableModel>();
    public static final String TAG = "couponpaymentHistory";
    public Messageclass msclass;
    public String SERVER = "https://cmr.mahyco.com/farmmechHandler.ashx";
    int i = 0;
    String usercode = "";
    Prefs mPref;
    EditText edtSearchEntry;
    Button btnSearchEntry;
    ImageView imgRefreshHDPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_h_d_p_s);
        getSupportActionBar().hide();
        recyclerViewPaymentHistoryList = findViewById(R.id.recyclerViewPaymentHistoryList);
        context = this;
        mPref = Prefs.with(this);
        cx = new CommonExecution(context);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        config = new Config(context);
        MDOurlpath = cx.saleSERVER;
        msclass = new Messageclass(this);
        pd = new ProgressDialog(context);
        edtSearchEntry = (EditText) findViewById(R.id.edt_search_entry);
        btnSearchEntry = (Button) findViewById(R.id.btn_search_entry);
        imgRefreshHDPS = (ImageView) findViewById(R.id.img_reset_hdps);
        populateData();
        initAdapter();
        runAnimation();
        usercode = pref.getString("UserID", "");
        Log.d(TAG + " p ", usercode);
        if (config.NetworkConnection()) {
            getPaymentHistoryListListFromserver();

//            String usercode = pref.getString("UserID", null);
//            byte[] objAsBytes = new byte[1];
//           // new saleorderpending.Saleorderdata("", "2", objAsBytes, usercode, "", "", "", search, "", "").execute();
//            // new GetOrderlist(SERVER,Integer.valueOf(action), usercode, search,"", saleorderpending.this).execute(); // Get Crop Master data as action 1
        } else {
            msclass.showMessage("Internet connection not available");
        }



        /*Added on 14th May 2021 for filter*/
        btnSearchEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCHR = edtSearchEntry.getText().toString();
                if (edtSearchEntry.getText().toString().length() > 0) {
                    ArrayList<PaymentHistoryTableModel> listNew = new ArrayList<>();
                    for (int l = 0; l < paymentHistoryTableModelArrayList.size(); l++) {
                        String mobileNo = paymentHistoryTableModelArrayList.get(l).getMobileno().toLowerCase();
                        String farmerName = paymentHistoryTableModelArrayList.get(l).getFarmerName().toLowerCase();
                        if (mobileNo.contains(strCHR.toLowerCase()) || farmerName.contains(strCHR.toLowerCase())) {
                            listNew.add(paymentHistoryTableModelArrayList.get(l));
                        }
                    }
                    if (listNew.size() == 0) {
                        Toast.makeText(PaymentHistoryActivityHDPS.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerViewPaymentHistoryList.setVisibility(View.VISIBLE);
                        cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(PaymentHistoryActivityHDPS.this, listNew);
                        recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
                    }
                } else {
                    recyclerViewPaymentHistoryList.setVisibility(View.VISIBLE);
                    cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(PaymentHistoryActivityHDPS.this, paymentHistoryTableModelArrayList);
                    recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
                }
            }
        });

        imgRefreshHDPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentHistoryTableModelArrayList.size() > 0) {
                    edtSearchEntry.setText("");
                    initAdapter();
                }
            }
        });


    }

    // to add data in the list
    private void populateData() {

        for (int i = 0; i < 12; i++) {
            arrayListStudentList.add("Item " + i);
        }
    }

    // to initalize the recycler view adapter with array list
    private void initAdapter() {
        cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(context, paymentHistoryTableModelArrayList);
        recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
    }

    // to run the navigation
    private void runAnimation() {

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_right_to_left);

        recyclerViewPaymentHistoryList.setLayoutAnimation(controller);
        cardviewOrderHistoryAdapter.notifyDataSetChanged();
        recyclerViewPaymentHistoryList.scheduleLayoutAnimation();

    }

    public void getPaymentHistoryListListFromserver() {


        new PaymentHistoryActivityHDPS.GetPaymentHistoryData(1, "").execute(SERVER,
                usercode, "PaymentProcess");

    }

    private class GetPaymentHistoryData extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String Offline;
        private int action;
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentHistoryActivityHDPS.this);


        public GetPaymentHistoryData(int action, String Offline) {

            this.action = action;
            this.Offline = Offline;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(PaymentHistoryActivityHDPS.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
                jsonObjchild.put("userCode", usercode);
                //jsonObject.put("Table",jsonObjchild);
                Log.d("GET_PAYMENT_ORDER_HDPS", "URL :" + Constants.GET_PAYMENT_ORDER_DETAILS_HDPS_API);
                Log.d("GET_PAYMENT_ORDER_HDPS", jsonObjchild.toString()); /*Changed on 11th May 2021*/
                result = HttpUtils.POSTJSON(Constants.GET_PAYMENT_ORDER_DETAILS_HDPS_API, jsonObjchild, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
            } catch (Exception e) {
                Log.d("MSG", e.getMessage());
            }
            return result;
            /*End API*/



            /* //   https://cmr.mahyco.com/MDOHandler.ashx?Type=getPaymentOrderDetail&usercode=97260828
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "getPaymentOrderDetail"));
            //  postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1 = cx.MDOurlpath + "?action=" + action + "&usercode=" + usercode;
            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackground111: " + Urlpath1);
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
                Log.d(TAG, "onPostExecute: " + e.getMessage().toString());
                //  msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            } catch (Exception e) {
                Log.d(TAG, "onPostExecute: " + e.getMessage().toString());

                e.printStackTrace();
                pd.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();*/
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                Log.d(TAG, "onPostExecute: " + result);
                //      swipeRefreshLayout.setRefreshing(false);
                pd.dismiss();
                redirecttoRegisterActivity(result);
                if (result.contains("paymentOrderList")) {
                    Log.d(TAG, "if onPostExecute: " + result);
                    paymentHistoryTableModelArrayList.clear();
                    JSONObject object = new JSONObject(result);
                    JSONArray jArray = object.getJSONArray("paymentOrderList"); /*Changed on 11th May 2021 from "Table"*/
                    Log.d("itemList jArray:: ", String.valueOf(jArray.length()));
                    if (jArray.length() == 0) {
                        msclass.showMessage("No data to show");
                        return;
                    }
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        PaymentHistoryTableModel paymentHistoryTableModel = new PaymentHistoryTableModel();
                        ArrayList<PaymentHistoryOrderDetailModel> paymentHistoryOrderDetailModelArrayList = new ArrayList<PaymentHistoryOrderDetailModel>();

//                        JSONArray jArrayOrderDetail = jObject.getJSONArray("orderdetail");
//                        for (int j = 0; j < jArrayOrderDetail.length(); j++) {
//                            JSONObject jObjectOrder = jArrayOrderDetail.getJSONObject(j);
//                            PaymentHistoryOrderDetailModel paymentHistoryOrderDetailModel = new PaymentHistoryOrderDetailModel();
//                            paymentHistoryOrderDetailModel.setAmount(jObjectOrder.getString("amount"));
//                            paymentHistoryOrderDetailModel.setFarmername(jObjectOrder.getString("farmername"));
//                            paymentHistoryOrderDetailModelArrayList.add(paymentHistoryOrderDetailModel);
//                        }

                        paymentHistoryTableModel.setSerialNumber(i + 1);
                        paymentHistoryTableModel.setPaymentStatus(jObject.getString("paymentStatus"));
                        paymentHistoryTableModel.setFarmerName(jObject.getString("farmerName"));
                        paymentHistoryTableModel.setAmount(jObject.getString("amount"));
                        paymentHistoryTableModel.setPaymentDate(jObject.getString("paymentDate"));
                        paymentHistoryTableModel.setTotalCoupon(jObject.getString("totalCoupon"));
                        paymentHistoryTableModel.setMobileno(jObject.getString("mobileno"));
                        paymentHistoryTableModel.setVillage(jObject.getString("village"));
                        paymentHistoryTableModel.setProduct(jObject.getString("product"));
                        paymentHistoryTableModel.setOrderdetail(paymentHistoryOrderDetailModelArrayList);
                        if (jObject.getString("pf_payment_mode") != null)
                            paymentHistoryTableModel.setPayMode(jObject.getString("pf_payment_mode")); /*Added on 12th May 2021*/
                        paymentHistoryTableModelArrayList.add(paymentHistoryTableModel);
                        Log.d("itemList Modal:: ", String.valueOf(paymentHistoryTableModelArrayList));

                    }
                    cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(context, paymentHistoryTableModelArrayList);
                    recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
                    recyclerViewPaymentHistoryList.setLayoutManager(new LinearLayoutManager(PaymentHistoryActivityHDPS.this));

                } else {
                    Log.d(PaymentHistoryActivityHDPS.class.getName(), "RESULT : " + result);
                    msclass.showMessage(result);
                }


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
