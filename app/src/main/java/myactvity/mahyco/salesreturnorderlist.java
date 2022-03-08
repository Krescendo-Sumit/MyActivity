package myactvity.mahyco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.ExpandableListAdapter;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;


public class salesreturnorderlist extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LinearLayout liorderlist;
    SharedPreferences locdata,pref;
    SharedPreferences.Editor loceditor,editor;
    private SqliteDatabase mDatabase;
    public String  search="";
    LinearLayout my_linear_layout;
    TextView lblheader,lgltotl;
    private Context context;
    ProgressDialog pd;
    private ListView lv;
    Config config;
    public Messageclass msclass;

    public String SERVER = "";
    public String SERVERsaleorder = "";
    public String testurl = "";
    public String cmbproductlist,usercode;
    CommonExecution cx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesreturnorderlist);
        getSupportActionBar().hide(); //<< this
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context = this;
        cx=new CommonExecution(this);
        msclass=new Messageclass(this);
        pd = new ProgressDialog(context);
        config = new Config(this); //Here the context is passing
        SERVER =cx.MDOurlpath;
        SERVERsaleorder=cx.saleSERVER;
        testurl ="https://cmr.mahyco.com/IPM.ashx"; //cx.saleSERVER;
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        liorderlist= (LinearLayout) findViewById(R.id.liorderlist);
        usercode=pref.getString("UserID", null);

        // preparing list data
        prepareListData();
      /*  listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });




        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }); */

    }

    /*
     * Preparing the list data
     */

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();

        Intent intent = new Intent(context.getApplicationContext(), saleOrderReturnApproved.class);
        //intent= new Intent(context.getApplicationContext(),TestImage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("Pagetype", "PlotVisit");
        context.startActivity(intent);
        finish();
    }
    public void bindorderlist(String str)
    {
        try {
            JSONObject object = new JSONObject(str);
            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);

            liorderlist.removeAllViews();


            int a=0;
            if(jArray.length()==0 )
            {
                TextView lblList= new TextView(this.context);
                lblList.setText("sale return order list not found");
                lblList.setTextColor(getResources().getColor(R.color.QRCodeBlackColor));
                lblList.setTextSize(12);
                lblList.setGravity(Gravity.CENTER);
                liorderlist.addView(lblList);
            }
            for(int i=0; i <jArray.length(); i++) {
                LayoutInflater infalInflater = (LayoutInflater) this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = infalInflater.inflate(R.layout.ordergroup, null);
                JSONObject jObject = jArray.getJSONObject(i);
                TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
                TextView lblorderno = (TextView) view.findViewById(R.id.lblorderno);
                TextView lblcreatedt = (TextView) view.findViewById(R.id.lblcreatedt);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText("Order By -"+jObject.getString("name"));
                lblorderno.setText("Order No-"+jObject.getString("orderid"));
                final String orderid =jObject.getString("orderid").toString();
                final String sales_org=jObject.getString("saleorg").toString();
                final String division=jObject.getString("division").toString();
                final String customer=jObject.getString("customer").toString();
                final String DLV_plant=jObject.getString("DLV_plant").toString();
                final String customergroup=jObject.getString("customergroup").toString();
                lblcreatedt.setText("Order Date:"+jObject.getString("entrydate").toString());
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Orderlistsubmit(orderid,sales_org,division,customer,DLV_plant,customergroup);

                    }
                });

                liorderlist.addView(view);
            }
        }
        catch (Exception ex)
        {

        }
    }

    private void bindproductlist(String str,LinearLayout my_linear_layout1)
    {
        //st
        try {
            my_linear_layout1.removeAllViews();
            JSONObject object = new JSONObject(str);
            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);


            for(int i=0; i <jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);

                View view2 = LayoutInflater.from(this).inflate(R.layout.salereturnordersave, null);
                view2.setBackgroundColor(getResources().getColor(R.color.Whitecl));
                EditText lblListItem = (EditText) view2.findViewById(R.id.lblListItem);
                TextView txtqty = (TextView) view2.findViewById(R.id.txtqty);
                final TextView txtproducthide = (TextView) view2.findViewById(R.id.txtproducthide);
                final EditText txtqtypkt = (EditText) view2.findViewById(R.id.txtqtypkt);
                final CheckBox chktag = (CheckBox) view2.findViewById(R.id.chktag);
                final EditText txtReason = (EditText) view2.findViewById(R.id.txtReason);
                final TextView txtbillingDt = (TextView) view2.findViewById(R.id.txtbillingDt);
                final TextView txtopenqty = (TextView) view2.findViewById(R.id.txtopenqty);


                chktag.setChecked(true);
                txtqtypkt.setHint("In Pkts");
               // txtqtypkt.setText(jObject.getString("QTYPKT"));
                txtqtypkt.setVisibility(View.GONE);
                lblListItem.setText(jObject.getString("MAKTX"));
                txtqty.setText(jObject.getString("QTYPKT"));
                txtopenqty.setText(jObject.getString("OPEN_QTYPKT"));
                txtbillingDt.setText(jObject.getString("Invoice_Date"));

                txtproducthide.setText(jObject.getString("MATNR")+"|"+
                        jObject.getString("MAKTX")+"|"
                        +jObject.getString("MTART")+"|"+jObject.getString("MATKL")+"|"+jObject.getString("DetailID"));


                chktag.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (chktag.isChecked())
                        {
                            chktag.setText("Approved");
                            txtReason.setVisibility(View.GONE);
                            //Perform action when you touch on checkbox and it change to selected state
                        }
                        else
                        {   chktag.setText("Reject");
                            txtReason.setVisibility(View.VISIBLE);
                            //Perform action when you touch on checkbox and it change to unselected state
                        }
                    }
                });


                my_linear_layout1.addView(view2);


            }

        }
        catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }



        //en
    }
    public void Orderlistsubmit(final String orderid,final String sales_org,
                                final String division,final String customer,final String DLV_plant,
                                final String customergroup)
    {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.salereturnpanel);
            // dialog.setTitle("Service Operator Assignment");
            RelativeLayout Rl1 = (RelativeLayout) dialog.findViewById(R.id.Rl1);
            final LinearLayout my_linear_layout1 = (LinearLayout) dialog.findViewById(R.id.my_linear_layout1);
            Rl1.setBackgroundColor(getResources().getColor(R.color.Whitecl));
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        int count = 0;
                        int count24 = 0;
                         int  rejectrejoncount=0;
                        // String crop = "";
                        String product = "";
                        String stock = "";
                        String salestock = "";
                        String currentstock = "";
                        String status="";
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        JSONObject mainObj = new JSONObject();
                        JSONArray ja = new JSONArray();
                        final String strdate = dateFormat.format(d);
                        for (int i = 0; i < my_linear_layout1.getChildCount(); i++) {
                            View view = my_linear_layout1.getChildAt(i);
                            EditText lblListItem = (EditText) view.findViewById(R.id.lblListItem);
                            TextView txtqty = (TextView) view.findViewById(R.id.txtqty);
                            final EditText txtqtypkt = (EditText) view.findViewById(R.id.txtqtypkt);
                            final CheckBox chktag = (CheckBox) view.findViewById(R.id.chktag);
                            final EditText txtReason = (EditText) view.findViewById(R.id.txtReason);
                            TextView txtproducthide = (TextView) view.findViewById(R.id.txtproducthide);
                            txtqtypkt.setVisibility(View.GONE);
                            if(txtqty.getText().length()==0)// || Integer.valueOf(txtqtypkt.getText().toString())==0 )
                            {

                                count=count+1;
                            }

                            if (txtproducthide.getText().toString().toLowerCase().contains("cotton"))
                            {
                                if(txtqty.getText().length()>0) {
                                    // int remain = Integer.valueOf(txtqtypkt.getText().toString()) % 24;
                                    // if (remain > 0) {
                                    //   count24 = count24 + 1;
                                    // }
                                }
                            }

                            if (chktag.isChecked())
                            {
                                status="Approved" ;
                            }
                            else
                            {
                                status="Reject" ;
                                if (txtReason.getText().length()==0)
                                {
                                    rejectrejoncount=rejectrejoncount+1;
                                }
                            }
                            cmbproductlist=txtproducthide.getText().toString();//URLEncoder.encode(dd.Code(), "UTF-8");
                            String[] prdarray=cmbproductlist.split("\\|");
                            String MATNR=prdarray[0].toString();
                            String MAKTX=prdarray[1].toString();
                            String MTART=prdarray[2].toString();
                            String MATKL=prdarray[3].toString();
                            String DetailID=prdarray[4].toString();
                            String QTYPKT=txtqty.getText().toString();
                            if(txtqty.getText().length()!=0) {
                                JSONObject jo = new JSONObject();
                                jo.put("MATNR", MATNR);
                                jo.put("MAKTX", MAKTX);
                                jo.put("MTART", MTART);
                                jo.put("MATKL", MATKL);
                                jo.put("QTYPKT", QTYPKT);
                                jo.put("Status", status);
                                jo.put("Reason", txtReason.getText().toString());
                                jo.put("DetailID",DetailID);
                                ja.put(jo);
                            }
                        }
                        mainObj.put("Table1", ja);
                        byte[] objAsBytes = null;//new byte[10000];

                        try {
                            objAsBytes = mainObj.toString().getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // SUBMIT ORDER BY RBM
                        if (count==0 && rejectrejoncount==0) {

                            //msclass.showMessage("Working On SAP service");
                            new Saleordersumbit(orderid, "1", objAsBytes, usercode, sales_org, division, customer, "",
                                   DLV_plant, customergroup, my_linear_layout1).execute();

                        }
                        else
                        {
                            if (count>0)
                            {
                                msclass.showMessage("Please enter order quantity");
                            }
                            if (rejectrejoncount>0)
                            {
                                msclass.showMessage("Please enter order rejected reason ");
                            }
                            /*else
                            {
                                msclass.showMessage("Please enter packet quantity according company standard packing(24 pkt each box)");

                            }*/
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msclass.showMessage(ex.getMessage());
                    }
                }
            });

            dialog.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            byte[] objAsBytes = new byte[1];
            String str=  new Salereturnorderdata(orderid,"5",objAsBytes,orderid,"","","","","","").execute().get();
            if(str.contains("True"))
            {
                bindproductlist(str,my_linear_layout1) ;
            }

        } catch (Exception ex)
        {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }


    }

    private void prepareListData() {
        //View view = LayoutInflater.from(this).inflate(R.layout.pendingorderlist_item,null);
        // online
        if (config.NetworkConnection()) {
            String usercode =pref.getString("UserID", null);
            String TBMcode=getIntent().getStringExtra("grcode");
            if (TBMcode==null)
            {
                TBMcode="0";
            }
            byte[] objAsBytes = new byte[1];
            new Salereturnorderdata(TBMcode,"4",objAsBytes,usercode,"","","","","","").execute();
        }
        else
        {
            msclass.showMessage("Internet connection not available");
        }





    }
    public class Salereturnorderdata extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,Comments,lotno,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,orderid;
        byte[] objAsBytes;

        public Salereturnorderdata(String orderid, String action,byte[] objAsBytes, String usercode,String saleorg,
                             String division,String customer,String name ,String DLV_plant,String customregroup   ){
            this.objAsBytes=objAsBytes;
            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.customregroup=customregroup;
            this.customer=customer;
            this.DLV_plant=DLV_plant;
            this.name=name;
            this.orderid=orderid;
            this.Comments="1";
            this.lotno="";

        }
        protected void onPreExecute() {
            // pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            // pd.setCancelable(false);
            // pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "SaleReturnOrder"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= testurl+"?orderid="+orderid+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&customer="+customer+"" +
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"" +
                    "&name="+name+"" +
                    "&Comments="+Comments+"&actionby=TBM&reason="+Comments+"&lotno="+lotno+"";

            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
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
                    returnstring= builder.toString();
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

            pd.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                pd.dismiss();
                if (result.contains("True")) {
                    // if (action==1) {
                    if(action.equals("6")) // Order Submit
                    {
                        msclass.showMessage("Order submitted Successfully");
                        my_linear_layout.removeAllViews();
                    }
                    if(action.equals("4")) // Pending Order list
                    {
                        bindorderlist(result);
                    }
                    // }
                }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }
    public class Saleordersumbit extends AsyncTask<String, String, String> {

        private String usercode,saleorg,division,customer,
                returnstring,action,cmbDistributor,DLV_plant,name,customregroup,orderid;
        byte[] objAsBytes;
        LinearLayout my_linear_layout1;

        public Saleordersumbit(String orderid, String action,byte[] objAsBytes, String usercode,String saleorg,
                               String division,String customer,String name ,String DLV_plant,String customregroup,LinearLayout my_linear_layout1  ){
            this.objAsBytes=objAsBytes;
            this.usercode=usercode;
            this.saleorg=saleorg;
            this.division=division;
            this.action=action;
            this.customregroup=customregroup;
            this.customer=customer;
            this.DLV_plant=DLV_plant;
            this.name=name;
            this.orderid=orderid;
            this.my_linear_layout1=my_linear_layout1;

        }
        protected void onPreExecute() {
            // pd = new ProgressDialog(context);
            pd.setTitle("Wait ...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            String encodeImage = Base64.encodeToString(objAsBytes,Base64.DEFAULT);
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "submitSaleReturnOder"));//"SubmitToCreateSO"));
            postParameters.add(new BasicNameValuePair("encodedData",encodeImage));
            String Urlpath1= testurl+"?orderid="+orderid+"&action="+action+"&usercode="+usercode+"&saleorg="+saleorg+"" +
                    "&division="+division+"&customer="+customer+"" +
                    "&customregroup="+customregroup+"&DLV_plant="+DLV_plant+"&name="+name+"";

            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
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
                    returnstring= builder.toString();
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

            pd.dismiss();
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                pd.dismiss();
                if (result.contains("True")) {


                    if(action.equals("1")) // Order Submit
                    {
                        if (result.contains("Reject")) {
                            msclass.showMessageview("Order Rejected Successfully.");
                        }
                        else {
                            //JSONObject object = new JSONObject(result);
                            //JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);
                            //JSONObject jObject = jArray.getJSONObject(0);
                            String mailmsg = "Order Submitted Successfully\n\n\n";
                            //mailmsg = mailmsg + jObject.getString("popup");
                            msclass.showMessageview(mailmsg);
                        }
                        this.my_linear_layout1.removeAllViews();
                        // Bind after saved data
                        bindorderlist(result);
                    }
                    else {

                    }
                    // }
                }
                else
                {
                    msclass.showMessage(result);

                }
                // }


            }

            catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                pd.dismiss();
            }

        }
    }



}
