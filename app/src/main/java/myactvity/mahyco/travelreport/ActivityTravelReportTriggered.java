package myactvity.mahyco.travelreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.TBMWiseMdoList;
import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTravelReportTriggered extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    NotificationAdapter adapter1;
    RecyclerView rc_kalist;
    LinearLayoutManager mManager;
    Button btnSubmit;
    RadioButton rbYes, rbNo;
    EditText et_remark;
    RadioGroup rgStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_report_triggered);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ActivityTravelReportTriggered.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        rc_kalist = (RecyclerView) findViewById(R.id.rc_kalist);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);

        rbYes = findViewById(R.id.rbYes);
        rbYes.setChecked(true);
        rbNo = findViewById(R.id.rbNo);
        rgStatus = findViewById(R.id.rgStatus);
        et_remark = findViewById(R.id.et_remark);
        btnSubmit = findViewById(R.id.btnSubmit);

        //   mManager.setReverseLayout(true);
        setTitle("Verify Travel Report");
        rc_kalist.setLayoutManager(mManager);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("FilterValue", "mh095,2024-04-29");
            jsonObject.addProperty("FilterOption", "GetTravelReport");
            getList(jsonObject);
        } catch (Exception exception) {
            Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        try {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0);
            String userCode = sp.getString("UserID", null);
            userCode = userCode.replace(" ", "%20");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            boolean status = true;
            if (rbYes.isChecked())
                status = true;
            if (rbNo.isChecked())
                status = false;

            String remark = et_remark.getText().toString().trim();
            if (rbNo.isChecked()) {
                if (remark.trim().equals("") || remark.trim().length() < 5) {
                    Toast.makeText(context, "Please enter proper remark.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MDOCode", "" + userCode);//: "mh248",
            jsonObject.addProperty("IsApproved", status);//: true,
            jsonObject.addProperty("ApprovedDt", currentDateandTime);//: "",
            jsonObject.addProperty("Remark", remark);//: "Test Remark",
            jsonObject.addProperty("CreatedBy", "" + userCode);//: "Sumit"
            SubmitRemark(jsonObject);
        } catch (Exception e) {

        }
    }

    private void SubmitRemark(JsonObject jsonObject) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<String> call = null;
            call = RetrofitClient.getInstance().getMyApi().submitMyTravelReportTriggeredRemark(jsonObject);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        Toast.makeText(ActivityTravelReportTriggered.this, "" + response.body(), Toast.LENGTH_SHORT).show();


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
        }
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

    void getList(JsonObject jsonObject) {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<MyTravelModel> call = null;
            call = RetrofitClient.getInstance().getMyApi().GetTravelReportTriggered(jsonObject);
            call.enqueue(new Callback<MyTravelModel>() {
                @Override
                public void onResponse(Call<MyTravelModel> call, Response<MyTravelModel> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        MyTravelModel result = response.body();

                        try {
                            if (result.Status) {
                                try {
                                    adapter1 = new NotificationAdapter((ArrayList) result.getResult().getMyTravelReportModels(), context);
                                    rc_kalist.setAdapter(adapter1);
                                    //  Toast.makeText(context, "" + result..getResult().getMyTravelReportModels().size(), Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    new androidx.appcompat.app.AlertDialog.Builder(context)
                                            .setMessage("Something went wrong.")
                                            .setTitle("Exception")
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MyTravelModel> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }


    }

    public class MyTravelModel {
        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public ResultModel getResult() {
            return Result;
        }

        public void setResult(ResultModel result) {
            Result = result;
        }

        boolean Status;
        String Message;
        ResultModel Result;
    }

    public class ResultModel {
        public List<MyTravelReportModels> getMyTravelReportModels() {
            return myTravelReportModels;
        }

        public void setMyTravelReportModels(List<MyTravelReportModels> myTravelReportModels) {
            this.myTravelReportModels = myTravelReportModels;
        }

        public MyTravelKMModel getMyTravelKMModel() {
            return myTravelKMModel;
        }

        public void setMyTravelKMModel(MyTravelKMModel myTravelKMModel) {
            this.myTravelKMModel = myTravelKMModel;
        }

        List<MyTravelReportModels> myTravelReportModels;
        MyTravelKMModel myTravelKMModel;
    }

    public class MyTravelReportModels {
        public String getUserCode() {
            return UserCode;
        }

        public void setUserCode(String userCode) {
            UserCode = userCode;
        }

        public String getStartDt() {
            return StartDt;
        }

        public void setStartDt(String startDt) {
            StartDt = startDt;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public void setActivityName(String activityName) {
            ActivityName = activityName;
        }

        public double getKM() {
            return KM;
        }

        public void setKM(double KM) {
            this.KM = KM;
        }

        String UserCode;//": "mh095",
        String StartDt;//": "4/29/2024 9:38:46 PM",
        String ActivityName;//": "Mdo_Endtravell",
        double KM;//": 0
    }

    public class MyTravelKMModel {
        public String getUserCode() {
            return UserCode;
        }

        public void setUserCode(String userCode) {
            UserCode = userCode;
        }

        public double getTotalKM() {
            return TotalKM;
        }

        public void setTotalKM(double totalKM) {
            TotalKM = totalKM;
        }

        String UserCode;//": "mh095",
        double TotalKM;//": 0
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DataObjectHolder> {


        Context context;

        private static final int UNSELECTED = -1;

        ArrayList<MyTravelReportModels> bhartiModelArrayList = null;


        public NotificationAdapter(ArrayList<MyTravelReportModels> productModels, Context context) {

            this.bhartiModelArrayList = productModels;
            Log.i("Seller produ:", ">>" + productModels.size());
            this.context = context;

        }

        @NonNull
        @Override
        public NotificationAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_chat1, parent, false);

            return new NotificationAdapter.DataObjectHolder(view);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            //  if (mSellerProductlist.size() > 0) {
            return bhartiModelArrayList.size();
            //} else {
            //  return 0;
            // }
        }


        @Override
        public void onBindViewHolder(final NotificationAdapter.DataObjectHolder holder, final int position) {
            try {
                MyTravelReportModels ResultModel = bhartiModelArrayList.get(position);

                holder.txt_message.setText(ResultModel.getActivityName());
                holder.txt_date.setText(ResultModel.getStartDt());

            } catch (Exception e) {

                Log.i("Error ", e.getMessage());

            }
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView txt_message, txt_date;
            LinearLayout linearLayout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                txt_message = itemView.findViewById(R.id.txt_message);
                txt_date = itemView.findViewById(R.id.txt_date);
                linearLayout = itemView.findViewById(R.id.cc);


            }
        }


    }

}