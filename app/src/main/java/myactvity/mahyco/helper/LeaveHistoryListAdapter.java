package myactvity.mahyco.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.cert.CertificateException;

import myactvity.mahyco.R;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.leaveHistory;
import myactvity.mahyco.utils.LeaveHistoryModel;


/**
 * Created by Akash Namdev on 2019-05-21.
 */
public class LeaveHistoryListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<LeaveHistoryModel> expandableListTitle;
    private HashMap<LeaveHistoryModel, ArrayList<LeaveHistoryModel>> expandableListDetail;
    Config config;
    public Messageclass msclass;

    String leaveType;

    String absenceDays;
    String reason;
    String status;
    String leavePeriod;
    String initiationDate;
    String fromDate;
    String toDate;
    String UWLId;
    Button btnAction;
    boolean btnActive;

    public String SERVER = "http://cmsapiapp.orgtix.com/api/Leave/LeavePullout?97";

    public LeaveHistoryListAdapter(Context context, ArrayList<LeaveHistoryModel> expandableListTitle, HashMap<LeaveHistoryModel, ArrayList<LeaveHistoryModel>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }


    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }


    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if ((!expandableListTitle.get(listPosition).getLeaveType().isEmpty()) || (!expandableListTitle.get(listPosition).getAbsenceDays().isEmpty()) || (!expandableListTitle.get(listPosition).getReason().isEmpty()) || (!expandableListTitle.get(listPosition).getStatus().isEmpty()) || (!expandableListTitle.get(listPosition).getPeriodFrom().isEmpty())) {
            {

                leaveType = expandableListTitle.get(listPosition).getLeaveType();

                absenceDays = expandableListTitle.get(listPosition).getAbsenceDays();
                reason = expandableListTitle.get(listPosition).getReason();
                status = expandableListTitle.get(listPosition).getStatus();
                leavePeriod = expandableListTitle.get(listPosition).getPeriodFrom();
                UWLId = expandableListTitle.get(listPosition).getUWLId();

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    convertView = layoutInflater.inflate(R.layout.child_result_row_new, null);
                }
                TextView txtLeaveType = (TextView) convertView.findViewById(R.id.txtLeaveType);

                TextView txtAbsenceDays = (TextView) convertView.findViewById(R.id.txtAbsenceDays);

                TextView txtReason = (TextView) convertView.findViewById(R.id.txtReason);

                TextView txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
                btnAction = (Button) convertView.findViewById(R.id.btnAction);

//                TextView txtLeavePeriod = (TextView) convertView.findViewById(R.id.txtLeavePeriod);

                btnAction.setVisibility(View.GONE);

                if (status.equals("Submitted")) {

                    btnAction.setVisibility(View.VISIBLE);




                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            pullOutLeave();


                        }
                    });

//                    if (btnActive){
//
//                        btnAction.setEnabled(false);
//
//
//                    }else {
//
//                        btnAction.setEnabled(true);
//                    }


                }
                txtLeaveType.setText(leaveType);
                txtAbsenceDays.setText(absenceDays);
                txtReason.setText(reason);
                txtStatus.setText(status);
                // txtLeavePeriod.setText(leavePeriod);
            }

        }
        return convertView;
    }


    public void pullOutLeave() {
        config = new Config(context);
        msclass = new Messageclass(context);


        try {
            JSONObject mainObj = new JSONObject();
            JSONObject jo = new JSONObject();
            jo.put("loginEmpID", "90000000");
            jo.put("loginEmpCompanyCodeNo", "1000");
            mainObj.put("loginDetails", jo);


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UWLId", UWLId);
            jsonObject.put("FromDate", fromDate);
            jsonObject.put("ToDate", toDate);
            jsonObject.put("FromTime", "00:00:00");
            jsonObject.put("ToTime", "00:00:00");
            jsonObject.put("NoOfDays", absenceDays);
            jsonObject.put("LeaveType", leaveType);
            jsonObject.put("Reason", reason);
            mainObj.put("leavePulloutData", jsonObject);


            if (config.NetworkConnection()) {
                new LeaveHistoryListAdapter.PullOutLeave("leavePulloutData", mainObj).execute();
            } else {
                msclass.showMessage("internet connection not available.\n please check internet connection");
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class PullOutLeave extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String function, returnstring;
        JSONObject obj = null;


        public PullOutLeave(String function, JSONObject obj) {

            this.function = function;
            this.obj = obj;

        }

        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                returnstring = getHttpPostJsonParameter(SERVER, obj);

            } catch (Exception e) {
                e.printStackTrace();
                returnstring = e.getMessage().toString();
                pd.dismiss();
            }

            pd.dismiss();
            return returnstring;
        }

        protected void onPostExecute(String result) {


            if (result.contains("S$Action Completed Successfully")) {
                Log.d("Adapter", "if onPostExecute: " + result);

                Log.d("JSONarray:: ", result);

                msclass.showMessage("Action Completed Successfully");

            ((leaveHistory)context).getLeaveHistory();


//              btnActive = true;


            } else if (obj.equals(result)){

                ((leaveHistory)context).getLeaveHistory();


               // btnActive = false;
            }
            else {
                msclass.showMessage("Something Went Wrong");

            }


        }

    }


    private String getHttpPostJsonParameter(String url, JSONObject jobj) throws CertificateException
    {
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

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public int getChildrenCount(int listPosition) {

        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        initiationDate = expandableListTitle.get(listPosition).getInitiationDate();
        fromDate = expandableListTitle.get(listPosition).getFromDate();
        toDate = expandableListTitle.get(listPosition).getToDate();


        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.

                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.group_result_heading, null);
        }
        TextView txtInitiationDate = (TextView) convertView

                .findViewById(R.id.txtInitiationDate);

        TextView txtFromDate = (TextView) convertView

                .findViewById(R.id.txtFromDate);

        TextView txtToDate = (TextView) convertView

                .findViewById(R.id.txtToDate);

        txtInitiationDate.setText(initiationDate);
        txtFromDate.setText(fromDate);
        txtToDate.setText(toDate);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
