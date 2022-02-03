package myactvity.mahyco;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.cert.CertificateException;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.LeaveHistoryListAdapter;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.utils.LeaveHistoryModel;
import myactvity.mahyco.utils.PaymentHistoryTableModel;

public class leaveHistory extends AppCompatActivity {

    RecyclerView recyclerViewPaymentHistoryList;
    ProgressDialog pd;
    private Context context;
    CommonExecution cx;
    public String MDOurlpath = "";
    Config config;
    SharedPreferences pref;


    ExpandableListView expandableListView;
    LinearLayout lllabel;
    TextView noDataText;
    LeaveHistoryListAdapter expandableListAdapter;
    ArrayList<LeaveHistoryModel> expandableListTitle;
    ArrayList<LeaveHistoryModel> leaveHistoryObjectList;
    HashMap<LeaveHistoryModel, ArrayList<LeaveHistoryModel>> expandableListDetail;
    CardviewOrderHistoryAdapter cardviewOrderHistoryAdapter;
    public ArrayList<LeaveHistoryModel> leaveHistoryModelArrayList = new ArrayList<LeaveHistoryModel>();
    public ArrayList<PaymentHistoryTableModel> paymentHistoryTableModelArrayList = new ArrayList<PaymentHistoryTableModel>();
    public static final String TAG = "couponpaymentHistory";
    public Messageclass msclass;
    public String SERVER = "http://cmsapiapp.orgtix.com/api/Leave/";
    int i = 0;
    String usercode = "";
    int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        getSupportActionBar().hide();
        recyclerViewPaymentHistoryList = findViewById(R.id.recyclerViewPaymentHistoryList);
        lllabel = findViewById(R.id.lllabel);
        noDataText = findViewById(R.id.noDataText);
        noDataText.setVisibility(View.VISIBLE);
        lllabel.setVisibility(View.GONE);
        context = this;
        cx = new CommonExecution(context);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        config = new Config(context);
        MDOurlpath = cx.saleSERVER;
        msclass = new Messageclass(this);
        pd = new ProgressDialog(context);
//        populateData();
//        initAdapter();
//        runAnimation();
        expandableListDetail = new HashMap<LeaveHistoryModel, ArrayList<LeaveHistoryModel>>();
        expandableListTitle=new ArrayList<>();
        leaveHistoryObjectList=new ArrayList<LeaveHistoryModel>();
        usercode = pref.getString("UserID", "");
        Log.d(TAG + " p ", usercode);

        expandableListView = (ExpandableListView) findViewById(R.id.myLeaveHistoryList);
        expandableListView.setVisibility(View.GONE);
       // expandableListView.setGroupIndicator(null);
        //expandableListView.setChildIndicator(null);

        if (config.NetworkConnection()) {
            getLeaveHistory();

        } else {
            msclass.showMessage("Internet connection not available");
        }


    }
    // to add data in the list
//    private void populateData() {
////
////        for (int i = 0; i < 12; i++) {
////            arrayListStudentList.add("Item " + i);
////        }
////    }

    // to initalize the recycler view adapter with array list
//    private void initAdapter() {
//        cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(context,paymentHistoryTableModelArrayList);
//        recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
//    }

    // to run the navigation
//    private void runAnimation() {
//
//        final LayoutAnimationController controller =
//                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_right_to_left);
//
//        recyclerViewPaymentHistoryList.setLayoutAnimation(controller);
//        cardviewOrderHistoryAdapter.notifyDataSetChanged();
//        recyclerViewPaymentHistoryList.scheduleLayoutAnimation();
//
//    }

    public void getLeaveHistory() {
        try {
            JSONObject mainObj = new JSONObject();
            JSONObject jo = new JSONObject();
            jo.put("loginEmpID", "90000000");
            jo.put("loginEmpCompanyCodeNo", "1000");
            jo.put("loginEmpGroupId", "CE");
            mainObj.put("loginDetails", jo);


            if (config.NetworkConnection()) {
                new GetLeaveHistoryData("GetLeaveData", mainObj).execute();
            } else {
                msclass.showMessage("internet connection not available.\n please check internet connection");
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetLeaveHistoryData extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function, returnstring;
        private int action;
        JSONObject obj = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(leaveHistory.this);


        public GetLeaveHistoryData(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;
            // this.comments=comments;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(leaveHistory.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                returnstring = getHttpPostJsonParameter(SERVER + function, obj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            }

            pd.dismiss();
            return returnstring;
        }

        protected void onPostExecute(String result) {

            try {
                Log.d(TAG, "onPostExecute: " + result);
//                msclass.showMessage(result);
//                pd.dismiss();
                if (result.contains("leaveRecords")) {
                    noDataText.setVisibility(View.GONE);
                    Log.d(TAG, "if onPostExecute: " + result);
                    JSONObject object = new JSONObject(result);
                    JSONArray jArray = object.getJSONArray("leaveRecords");
                    JSONArray innerJSONArray = jArray.getJSONArray(0);

                    lllabel.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.VISIBLE);
                    Log.d("itemList jArray:: ", String.valueOf(jArray.length()));
                    Log.d("itemList jArray:: ", innerJSONArray.toString());
                    expandableListTitle.clear();
                    expandableListDetail.clear();

                    for (int i = 0; i < innerJSONArray.length(); i++) {
                        JSONObject jObject = innerJSONArray.getJSONObject(i);
                        LeaveHistoryModel leaveHistoryModel = new LeaveHistoryModel();
                        leaveHistoryModel.setInitiationDate(jObject.getString("InitiationDate"));
                        leaveHistoryModel.setFromDate(jObject.getString("FromDate"));
                        leaveHistoryModel.setToDate(jObject.getString("ToDate"));
                        leaveHistoryModel.setAbsenceDays(jObject.getString("AbsenceDays"));
                        leaveHistoryModel.setReason(jObject.getString("Reason"));
                        leaveHistoryModel.setLeaveType(jObject.getString("LeaveType"));
                        leaveHistoryModel.setStatus(jObject.getString("Status"));
                        leaveHistoryModel.setPeriodFrom(jObject.getString("leavePeriodFrom"));
                        leaveHistoryModel.setUWLId(jObject.getString("UWLId"));


                        expandableListTitle.add(leaveHistoryModel);


//                        leaveHistoryObjects.add(leaveHistoryModel);

//                        leaveHistoryModelArrayList.add(leaveHistoryModel);
//
//                        Log.d("itemList Modal:: ", String.valueOf(leaveHistoryModelArrayList));
//
//
//                        expandableListDetail.put(leaveHistoryModel.getInitiationDate(), leaveHistoryModelArrayList);

//                        expandableListDetail.put("Akash", leaveHistoryObjects);
                        ArrayList<LeaveHistoryModel> leaveHistoryModels = new ArrayList<LeaveHistoryModel>();
                        leaveHistoryModels.add(leaveHistoryModel);

                        expandableListDetail.put(expandableListTitle.get(i),
                                leaveHistoryModels);


                    }


                    setAdapterExpandablelist();

                    expandableListView.invalidateViews();
//                    cardviewOrderHistoryAdapter = new CardviewOrderHistoryAdapter(context,paymentHistoryTableModelArrayList);
//                    recyclerViewPaymentHistoryList.setAdapter(cardviewOrderHistoryAdapter);
//                    recyclerViewPaymentHistoryList.setLayoutManager(new LinearLayoutManager(leaveHistory.this));
//                    // txt_total_with_tax.setText(String.valueOf(amount));
                    // setListData();
                } else {
                    noDataText.setVisibility(View.VISIBLE);
                    msclass.showMessage(result);
                }
                //   lblusername.setText(result) ;


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                pd.dismiss();
            }

        }
    }


    public void setAdapterExpandablelist() {

        Log.d("itemList Count:: ", String.valueOf(expandableListTitle.size()));

        expandableListAdapter = new LeaveHistoryListAdapter(this, expandableListTitle, expandableListDetail);


        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                expandableListAdapter.notifyDataSetChanged();
                expandableListView.invalidateViews();

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                expandableListAdapter.notifyDataSetChanged();
                expandableListView.invalidateViews();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });


    }


    private String getHttpPostJsonParameter(String url, JSONObject jobj) throws CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {

            //String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            httpPost.setEntity(new StringEntity(jobj.toString(), "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else if (statusCode == 401) {

            }
        } catch (ClientProtocolException e) {
            //LogHelper.appendLog("HttpPost ClientProtocolException", e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            //LogHelper.appendLog("HttpPost IOException", e.getMessage());

            e.printStackTrace();
        }
        return str.toString();
    }


}
