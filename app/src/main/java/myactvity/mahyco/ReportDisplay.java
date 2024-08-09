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

public class ReportDisplay extends AppCompatActivity  {
    String returnstring ;
    private TableMainLayout tmain ;
    public RelativeLayout RelativeLayout1;
    public Button btnReport;
    public EditText txtDate;
    public ProgressDialog dialog,pd;
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
            "Plan No.Farmer",
            "Plan Village Visit",
            "Plan Retailer Visit ",
            "Plan Distributor Visit","Plan Nursery Visit",
            "A.No.Farmer",
            "A.Village Visit",
            "A.Retailer Visit ",
            "A.Distributor Visit","A.Nursery Visit","visitDate"};

    int consize[] = {
            75,
            200,
            200,
            200, 200, 200,
            200,
            200,
            200, 200, 200,
            200,
            200, 200};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_display);
        RelativeLayout1 = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        // getSupportActionBar().hide(); //<< this
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setTitle("MDO Days Summary Visit Report ");
        config = new Config(this); //Here the context is passing
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        btnReport=(Button) findViewById(R.id.btnReport);
        txtDate=(EditText) findViewById(R.id.txtDate);
        wb1=(WebView)  findViewById(R.id.wb1);
        context = this;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
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
       /* dialog.setMessage("Loading....");
        dialog.show();
        RelativeLayout1.removeAllViews();
        tmain=new TableMainLayout(this,RelativeLayout1,headers,consize);
        addHeaders();
        tmain.resizeBodyTableRowHeight(); */
        binddataintoWEBVIEW();
    }
    public void binddataintoWEBVIEW()
    {
        if(config.NetworkConnection()) {
            String str= null;
            boolean fl=false;
            try {
            new GetMDOVisitReport(3,pref.getString("UserID",null),txtDate.getText().toString(),"",this).execute();
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

           /* JSONObject object = new JSONObject(result);
            // Table 1 Data display
            JSONArray jArray = object.getJSONArray("Table");
            String myHtmlString = "<html><body>" +
                    "<h4>Following is MDO wise Days visit summary details Report</h4>\n" +
                    "<table  border='1px' cellpadding='5' cellspacing='0' style='border-collapse:collapse; font-size: x-small;' width=\"100%\">\n" +
                    "  <tr align='left' valign='top'>\n" +
                    "    <td>Sr.No</td>\n" +
                    "    <td>MDO_Name</td>\t\t\n" +
                    "    <td>Tot.Plan Farmer</td>\t\t\n" +
                    "    <td>Planned Village count </td>\t\t\n" +
                    "    <td>Planned Retailer Count</td>\t\t\n" +
                    "    <td>Planned Distributor Count</td>\t\t\n" +
                    "    <td>Planned Nursery Count</td>\t\t\n" +
                    "    <td>Actual No of Farmer</td>\t\t\n" +
                    "    <td>Actual Village Visit Count</td>\t\t\n" +
                    "    <td>Actual Retailer Visit Count</td>\t\t\n" +
                    "    <td>Actual Distributor visit Count </td>\t\t\n" +
                    "    <td>Actual Nursery Visit Count</td>\t\t\n" +
                    "    <td>Visit Date</td>\n" +
                    "  </tr>\n";
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                myHtmlString = myHtmlString + "  <tr align='left' valign='top'>\n" +
                        "    <td>" + String.valueOf(i + 1) + "</td>\n" +

                        "    <td>" + jObject.getString("MDO_name").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("pnofarmer").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("pVillageVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("pRetailerVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("pDistributorVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("pNurseryVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("nofarmer").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("VillageVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("RetailerVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("DistributorVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("NurseryVisit").toString() + "</td>\t\t\n" +
                        "    <td>" + jObject.getString("visitDate").toString() + "</td>\t\t\n" +
                        "  </tr>\n";



            }
            wb1.loadData(myHtmlString, "text/html", null);
   */
            wb1.loadData(result, "text/html", null);
            // dialog.dismiss();

        } //catch (JSONException e) {
           // e.printStackTrace();
          //  msclass.showMessage(e.getMessage());

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
        if(text.equals("0") || text.isEmpty() )
        {
            recyclableTextView.setBackgroundColor(Color.WHITE);
        }
        else
        {
            recyclableTextView.setBackgroundColor(getResources().getColor(R.color.lightgray));
        }

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
                // Action 3 as days short summary Report data
                str = new GetMDOVisitReport(3,pref.getString("UserID",null),txtDate.getText().toString(),"",this).execute().get();
                // msclass.showMessage(str);
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText(String.valueOf(i+1), 0));

                    row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                    row.addView(makeTableRowWithText(jObject.getString("pnofarmer").toString(), 2));
                    row.addView(makeTableRowWithText(jObject.getString("pVillageVisit").toString(), 3));
                    row.addView(makeTableRowWithText(jObject.getString("pRetailerVisit").toString(), 4));
                    row.addView(makeTableRowWithText(jObject.getString("pDistributorVisit").toString(), 5));
                    row.addView(makeTableRowWithText(jObject.getString("pNurseryVisit").toString(), 6));
                    row.addView(makeTableRowWithText(jObject.getString("nofarmer").toString(), 7));
                    row.addView(makeTableRowWithText(jObject.getString("VillageVisit").toString(), 8));
                    row.addView(makeTableRowWithText(jObject.getString("RetailerVisit").toString(), 9));
                    row.addView(makeTableRowWithText(jObject.getString("DistributorVisit").toString(), 10));
                    row.addView(makeTableRowWithText(jObject.getString("NurseryVisit").toString(), 11));
                    row.addView(makeTableRowWithText(jObject.getString("visitDate").toString(), 12));
                    tmain.gnerateCD(row1,row);

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
                        builder.append(line).append("\n");
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

                if (action==3) {
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

