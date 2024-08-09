package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.TableMainLayout;

public class DetailSummaryReport extends AppCompatActivity  {
    String returnstring ;
    private TableMainLayout tmain,tmain2,tmain3,tmain4 ;
    public RelativeLayout RelativeLayout1,RelativeLayout2,RelativeLayout3,RelativeLayout4;
    public Button btnReport;
    public TextView lbl1,lbl2,lbl3,lbl4;
    public EditText txtDate;
    public  ProgressDialog dialog,pd;
    private TextView recyclableTextView;
    public String MDOurlpath ="http://10.80.50.153/maatest/MDOHandler.ashx";
    public Messageclass msclass;
    public CommonExecution cx;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private int mYear,mMonth,mDay;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private WebView wb1;
    private Context context;
    String headers[] = {
            "Sr.No",
            "MDO_name",
            "Planned_Villages",
            "Planned_Villagename",
            "Plan_Activites ",
            "Planned_Farmers","Actual_Villages",
            "Actual_Activites",
            "Actual_Farmer",
            "visitDate"};

    String headers2[] = {
            "Sr.No",
            "MDO_name",
            "Planned_Retailer",
            "Actual_Retailers",
            "visitDate"};
    String headers3[] = {
            "Sr.No",
            "MDO_name",
            "Planned_Nursery",
            "Actual_Nursery",
            "visitDate"};
    String headers4[] = {
            "Sr.No",
            "MDO_name",
            "Planned_Distributor",
            "Actual_Distributor",
            "visitDate"};
    int consize[] = {
            75,
            200,
            200,
            200, 200, 200,
            200,
            200,
            200, 200};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_summary_report);
        RelativeLayout1 = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        RelativeLayout2 = (RelativeLayout)findViewById(R.id.RelativeLayout2);
        RelativeLayout3 = (RelativeLayout)findViewById(R.id.RelativeLayout3);
        RelativeLayout4 = (RelativeLayout)findViewById(R.id.RelativeLayout4);
        lbl1  = (TextView) findViewById(R.id.lbl1);
        lbl2  = (TextView) findViewById(R.id.lbl2);
        lbl3  = (TextView) findViewById(R.id.lbl3);
        lbl4  = (TextView) findViewById(R.id.lbl4);
        wb1=(WebView)  findViewById(R.id.wb1);
        context = this;
        // getSupportActionBar().hide(); //<< this
        setTitle("MDO Days Details Summary Visit Report ");
        config = new Config(this); //Here the context is passing
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        btnReport=(Button) findViewById(R.id.btnReport);
        txtDate=(EditText) findViewById(R.id.txtDate);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        MDOurlpath =cx.MDOurlpath;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(txtDate.getText()))
                {
                    msclass.showMessage("Please enter date");
                    txtDate.clearFocus();
                    return;
                }

                binddata();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
            }
        });
    }

    private void setDateTimeField(View v) {
        final EditText txt=(EditText)v;
        Calendar newCalendar = dateSelected;


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
            }


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();
        // txt.setText(dateFormatter.format(dateSelected.getTime()));


    }
    public void binddata()
    {
       // dialog.setMessage("Loading....");
       // dialog.show();
       /* RelativeLayout1.removeAllViews();
        RelativeLayout2.removeAllViews();
        RelativeLayout3.removeAllViews();
        RelativeLayout4.removeAllViews();
        tmain=new TableMainLayout(this,RelativeLayout1,headers,consize);
        tmain2=new TableMainLayout(this,RelativeLayout2,headers2,consize);
        tmain3=new TableMainLayout(this,RelativeLayout3,headers3,consize);
        tmain4=new TableMainLayout(this,RelativeLayout4,headers4,consize);
        addHeaders();
        tmain.resizeBodyTableRowHeight();
        tmain2.resizeBodyTableRowHeight();
        tmain3.resizeBodyTableRowHeight();
        tmain4.resizeBodyTableRowHeight(); */

        binddataintoWEBVIEW();
    }
    public void binddataintoWEBVIEW()
    {
        if(config.NetworkConnection()) {
            String str= null;
            boolean fl=false;
            try {
             new GetMDOVisitReport(6,pref.getString("UserID",null),txtDate.getText().toString(),"",DetailSummaryReport.this).execute();
            }
            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
               // dialog.dismiss();
            }
        }
        else
        {
            msclass.showMessage("Internet network not available.");
          //  dialog.dismiss();

        }

    }
    public void bindresultdata(String result) {
        try {
             // JSONObject object = new JSONObject(result);
            //// Table 1 Data display
           // JSONArray jArray = object.getJSONArray("Table");
           /* String myHtmlString = "<html><body>" +
                    "<h4>Following is MDO wise Days visit details Report</h4>\n" +
                    "<table  border='1px' cellpadding='5' cellspacing='0' style='border-collapse:collapse; font-size: x-small;' width=\"100%\">\n" +
                    "  <tr align='left' valign='top'>\n" +
                    "    <td>Sr.No</td>\n" +
                    "    <td>TBM_Name</td>\t\t\n" +
                    "    <td>MDO_Name</td>\t\t\n" +
                    "    <td>Date</td>\t\t\n" +
                    "    <td>Planned Village Name</td>\t\t\n" +
                    "    <td>Actual Villages</td>\t\t\n" +
                    "    <td>Actual Activites</td>\t\t\n" +
                    "    <td>A.Farmer Total</td>\t\t\n" +
                    "    <td>Planned Nursery</td>\t\t\n" +
                    "    <td>Actual Nursery</td>\t\t\n" +
                    "    <td>Planned Distributor</td>\t\t\n" +
                    "    <td>Actual Distributor</td>\t\t\n" +
                    "    <td>planned Retailer</td>\t\t\n" +
                    "    <td>Actual Retailers</td>\n" +
                    "  </tr>\n";
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                myHtmlString = myHtmlString + "  <tr align='left' valign='top'>\n" +
                        "    <td>" + String.valueOf(i + 1) + "</td>\n" +
                        "    <td>" + jObject.getString("TBMcode").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("MDO_name").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("date").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("planned_villagename").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("Actual_villages").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("actual_activites").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("actualfarmer").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("planned_nursery").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("actual_nursery").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("planned_distributor").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("actual_distributor").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("planned_retailer").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("Actual_retailers").toString() + "</td>\t\t\n" +
                        "  </tr>\n";
            }
            */
            //wb1.loadData(myHtmlString, "text/html", null);
            wb1.loadData(result, "text/html", null);
            // dialog.dismiss();

        }
        //catch (JSONException e) {
         //   e.printStackTrace();
         //   msclass.showMessage(e.getMessage());

        //}
        catch (Exception e) {
            e.printStackTrace();
            msclass.showMessage(e.getMessage());
            // dialog.dismiss();
        }
    }
    public TextView makeTableRowWithText(String text, int a) {
        // TableRow.LayoutParams params3 = new TableRow.LayoutParams( tmain.headerCellsWidth[a],//70);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams( tmain.consize[a],//70);
                75);
        params3.setMargins(2, 1, 0, 0);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setBackgroundColor(Color.WHITE);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(10);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setLayoutParams(params3);
        // recyclableTextView.setEllipsize(TextUtils.TruncateAt.END);
        // recyclableTextView.setSingleLine(true);
        recyclableTextView.setHorizontallyScrolling(true);
        recyclableTextView.setMaxLines(2);
        // recyclableTextView.setVerticalScrollBarEnabled(true);
        return recyclableTextView;
    }
    public void addHeaders(){

        lbl1.setVisibility(View.VISIBLE);
        lbl2.setVisibility(View.VISIBLE);
        lbl3.setVisibility(View.VISIBLE);
        lbl4.setVisibility(View.VISIBLE);
        // RelativeLayout1.removeAllViews();
        TableRow row;
        TableRow row1;
        TableRow.LayoutParams params = new TableRow.LayoutParams( tmain.headerCellsWidth[0], RelativeLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        //
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            boolean fl=false;
            try {
                // Action 4 as days Details summary Report data
                str = new GetMDOVisitReport(4,pref.getString("UserID",null),txtDate.getText().toString(),"",DetailSummaryReport.this).execute().get();
                JSONObject object = new JSONObject(str);
                // Table 1 Data display
                JSONArray jArray = object.getJSONArray("Table");
                if (jArray.length()==0)
                {
                    //RelativeLayout1.removeAllViews();
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText("NA", 0));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    tmain.gnerateCD(row1,row);
                }
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText(String.valueOf(i), 0));
                    row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                    row.addView(makeTableRowWithText(jObject.getString("planned_villages").toString(), 2));
                    row.addView(makeTableRowWithText(jObject.getString("planned_villagename").toString(), 3));
                    row.addView(makeTableRowWithText(jObject.getString("plan_activites").toString(), 4));
                    row.addView(makeTableRowWithText(jObject.getString("planned_farmers").toString(), 5));
                    row.addView(makeTableRowWithText(jObject.getString("Actual_villages").toString(), 6));
                    row.addView(makeTableRowWithText(jObject.getString("actual_activites").toString(), 7));
                    row.addView(makeTableRowWithText(jObject.getString("actualfarmer").toString(), 8));
                    row.addView(makeTableRowWithText(txtDate.getText().toString(), 9));
                    tmain.gnerateCD(row1,row);

                }

                // Table 2 Data display
                JSONArray jArray2 = object.getJSONArray("Table1");
                if (jArray2.length()==0)
                {
                    //RelativeLayout2.removeAllViews();
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText("", 0));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    tmain2.gnerateCD(row1,row);
                }
                else {
                    for (int i = 0; i < jArray2.length(); i++) {

                        JSONObject jObject = jArray2.getJSONObject(i);
                        row1 = new TableRow(this);
                        row = new TableRow(this);
                        row1.addView(makeTableRowWithText(String.valueOf(i), 0));
                        row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                        row.addView(makeTableRowWithText(jObject.getString("planned_retailer").toString(), 2));
                        row.addView(makeTableRowWithText(jObject.getString("Actual_retailers").toString(), 3));
                        row.addView(makeTableRowWithText(txtDate.getText().toString(), 4));
                        tmain2.gnerateCD(row1, row);

                    }
                }
                // Table 3 Data display
                JSONArray jArray3 = object.getJSONArray("Table2");
                if (jArray3.length()==0)
                {
                   // RelativeLayout3.removeAllViews();
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText("", 0));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                ;
                    tmain3.gnerateCD(row1,row);
                }
                else {
                    for (int i = 0; i < jArray3.length(); i++) {

                        JSONObject jObject = jArray3.getJSONObject(i);
                        row1 = new TableRow(this);
                        row = new TableRow(this);
                        row1.addView(makeTableRowWithText(String.valueOf(i), 0));
                        row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                        row.addView(makeTableRowWithText(jObject.getString("planned_Nursery").toString(), 2));
                        row.addView(makeTableRowWithText(jObject.getString("Actual_Nursery").toString(), 3));
                        row.addView(makeTableRowWithText(txtDate.getText().toString(), 4));
                        tmain3.gnerateCD(row1, row);

                    }
                }
                // Table4 Data display
                JSONArray jArray4 = object.getJSONArray("Table3");
                if (jArray4.length()==0)
                {
                    //RelativeLayout4.removeAllViews();
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText("1", 0));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    row.addView(makeTableRowWithText("NA", 1));
                    tmain4.gnerateCD(row1,row);
                }
                else {
                    for (int i = 0; i < jArray4.length(); i++) {

                        JSONObject jObject = jArray4.getJSONObject(i);
                        row1 = new TableRow(this);
                        row = new TableRow(this);

                        row1.addView(makeTableRowWithText(String.valueOf(i), 0));
                        row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                        row.addView(makeTableRowWithText(jObject.getString("planned_distributor").toString(), 2));
                        row.addView(makeTableRowWithText(jObject.getString("actual_distributor").toString(), 3));
                        row.addView(makeTableRowWithText(txtDate.getText().toString(), 4));
                        tmain4.gnerateCD(row1, row);

                    }
                }


            }
            catch (InterruptedException e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                dialog.dismiss();
            } catch (ExecutionException e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                dialog.dismiss();
            }
            catch (JSONException e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                dialog.dismiss();
            }
            dialog.dismiss();
        }
        else
        {
            msclass.showMessage("Internet network not available.");
            dialog.dismiss();

        }

    }
    public class GetMDOVisitReport extends AsyncTask<String, String, String> {

        private int action;
        private String retailerfirm,name,mobileno,seedlicense;
        private String password,To,from,usercode;
       // private ProgressDialog p;
        private Context ctx;

        public GetMDOVisitReport(int action,String usercode,String from,String To,Context ctx ){
            this.action=action;
            this.usercode=usercode;
            this.from=from;
            this.To=To;
           // this.p=new ProgressDialog(ctx);
            this.ctx=ctx;

        }
        protected void onPreExecute() {
            super.onPreExecute();
           /* p.setMessage("Loading  ..... ");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show(); */
            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
           // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

        }
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit

            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetMDOVISITReport"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?action="+action+"&userId="+usercode+"&FromDT="+from+"&ToDT="+To+"";
            HttpPost httppost = new HttpPost(Urlpath1);
            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);
            //getdiscoundDetails
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                //Thread.sleep(5000);
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
                        builder.append(line).append("");
                    }
                    returnstring= builder.toString();
                   // p.dismiss();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                pd.dismiss();
            }

            // dialog.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{

                if (action==6) {
                    txtDate.setText("");
                    Toast.makeText(ctx, "Report Download completed", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    bindresultdata(result);
                }
            }

            catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();
            }

        }
    }

}
