package myactvity.mahyco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.retro.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TBMWiseMdoList extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    NotificationAdapter adapter1;
    RecyclerView rc_kalist;
    LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbmwise_mdo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = TBMWiseMdoList.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        rc_kalist = (RecyclerView) findViewById(R.id.rc_kalist);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);
        //   mManager.setReverseLayout(true);
        setTitle("Verify KA list");
        rc_kalist.setLayoutManager(mManager);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserCode", "97261227");
            getList(jsonObject);
        } catch (Exception exception) {
            Toast.makeText(context, "Error is " + exception.getMessage(), Toast.LENGTH_SHORT).show();
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

            Call<KAListModel> call = null;
            call = RetrofitClient.getInstance().getMyApi().GetTBMWiseKAList(jsonObject);
            call.enqueue(new Callback<KAListModel>() {
                @Override
                public void onResponse(Call<KAListModel> call, Response<KAListModel> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        KAListModel result = response.body();
                        try {
                            try {
                                adapter1 = new NotificationAdapter((ArrayList) result.getTable(), context);
                                rc_kalist.setAdapter(adapter1);
                                Toast.makeText(TBMWiseMdoList.this, "" + result.getTable().size(), Toast.LENGTH_SHORT).show();

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
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<KAListModel> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
        }


    }

    public class KAListModel {
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<KAModel> getTable() {
            return Table;
        }

        public void setTable(List<KAModel> table) {
            Table = table;
        }

        boolean success;//": true,
        String message;//": "Success",
        List<KAModel> Table;//":
    }

    class KAModel {
        public String getMDOCode() {
            return MDOCode;
        }

        public void setMDOCode(String MDOCode) {
            this.MDOCode = MDOCode;
        }

        public String getMDO_name() {
            return MDO_name;
        }

        public void setMDO_name(String MDO_name) {
            this.MDO_name = MDO_name;
        }

        public String getTBMCode() {
            return TBMCode;
        }

        public void setTBMCode(String TBMCode) {
            this.TBMCode = TBMCode;
        }

        public String getTBMName() {
            return TBMName;
        }

        public void setTBMName(String TBMName) {
            this.TBMName = TBMName;
        }

        public String getHeadQuarter() {
            return HeadQuarter;
        }

        public void setHeadQuarter(String headQuarter) {
            HeadQuarter = headQuarter;
        }

        public String getRegion() {
            return Region;
        }

        public void setRegion(String region) {
            Region = region;
        }

        String MDOCode;//": "97261227",
        String MDO_name;//": "Bhagwan.Marmat",
        String TBMCode;//": "97261227",
        String TBMName;//": "Bhagwan.Marmat",
        String HeadQuarter;//": "SILLOD",
        String Region;//": "MH (JALNA)"
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DataObjectHolder> {


        Context context;

        private static final int UNSELECTED = -1;

        ArrayList<KAModel> bhartiModelArrayList = null;


        public NotificationAdapter(ArrayList<KAModel> productModels, Context context) {

            this.bhartiModelArrayList = productModels;
            Log.i("Seller produ:", ">>" + productModels.size());
            this.context = context;

        }

        @NonNull
        @Override
        public NotificationAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_chat, parent, false);

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
                KAModel kaModel = bhartiModelArrayList.get(position);

                holder.txt_message.setText(kaModel.getMDO_name()+"("+kaModel.getMDOCode()+")");
                holder.txt_date.setText(kaModel.getHeadQuarter()+"("+kaModel.getRegion()+")");

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