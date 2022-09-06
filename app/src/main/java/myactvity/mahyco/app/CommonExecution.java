package myactvity.mahyco.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.ImageView;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97260828 on 1/9/2018.
 */

public class CommonExecution {

    Context context;
    String returnstring ;
    ProgressDialog dialog;
    public String saleSERVER,Urlpath,MDOurlpath;
    public CommonExecution(Context context) {

        this.context = context;
        dialog = new ProgressDialog( this.context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Urlpath="https://farm.mahyco.com/TestHandler.ashx";
        MDOurlpath="https://cmr.mahyco.com/MDOHandler.ashx";
        saleSERVER = "https://farm.mahyco.com/SaleOrder_new.ashx"; // Upldate Link as material conditonal values

    }

    public class getAssignState extends AsyncTask<String, String, String> {
        private Bitmap image;
        private String name;
        private String userID;
        private String croptype;
        private String Action;
        public getAssignState( String userID ){
            this.userID=userID;
        }
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetAssignStateMaster"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?userID="+userID;
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
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

            dialog.dismiss();
            return builder.toString();
        }
         }
    public class getState extends AsyncTask<String, String, String> {
        private Bitmap image;
        private String name;
        private String userID;
        private String croptype;
        private String Action;
        public getState( ){
        }
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetStateMaster"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1=MDOurlpath;
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
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

            dialog.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }
    public class getDist extends AsyncTask<String, String, String> {

        private String state;

        public getDist(String state ){
            this.state=state;
        }
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetDistrict_Master"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?State="+state;
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
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

            dialog.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }
    public class getTaluka extends AsyncTask<String, String, String> {

        private String dist;

        public getTaluka(String dist ){
            this.dist=dist;
        }
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetTalukaMaster"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?Dist="+dist;
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
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

            dialog.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }
    public class getVillage extends AsyncTask<String, String, String> {

        private String taluka,Type;
        private ProgressDialog p;
        public getVillage(String taluka,String Type,Context ctx ){
            this.taluka=taluka;
            this.Type=Type;
            this.p=new ProgressDialog(ctx);
        }
        protected void onPreExecute() {

            super.onPreExecute();
            p.setMessage("Wait... ");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetVillageMaster"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?taluka="+taluka+"&type="+Type;
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
                p.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                p.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                p.dismiss();
            }

            p.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }

    public class MDOMasterData extends AsyncTask<String, String, String> {

        private int action;
        private String username;
        private String password,IME;


        public MDOMasterData( String IME,int action,String username,String password){
            this.action=action;
            this.username=username;
            this.password=password;
            this.IME=IME;
        }
        protected void onPreExecute() {
           // dialog.setMessage("Loading....");
            //dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit

            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "MDOVerify_user_new"));
           // postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1= MDOurlpath+"?IME="+IME+"&username="+username+"&sapcode="+action+"&password="+password+"";
            HttpPost httppost = new HttpPost(Urlpath1);

           // StringEntity entity;
           // entity = new StringEntity(request, HTTP.UTF_8);

            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
           // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");
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
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

           // dialog.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }

    public String PostData() {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("https://cmr.mahyco.com/FormerApp.asmx/MDOVerify_user?username=1&sapcode=2&password=1");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("from", ""));
            //list.add(new BasicNameValuePair("pass",valuse[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);

        }
        catch(Exception exception)  {}
        return s;


    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;

    }
    public class GetMDOVisitReport extends AsyncTask<String, String, String> {

        private int action;
        private String retailerfirm,name,mobileno,seedlicense;
        private String password,To,from,usercode;


        public GetMDOVisitReport(int action,String usercode,String from,String To ){
            this.action=action;
            this.usercode=usercode;
            this.from=from;
            this.To=To;

        }
        protected void onPreExecute() {
             dialog.setMessage("Loading....");
             dialog.show();

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
                    dialog.dismiss();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=e.getMessage().toString();
                dialog.dismiss();
            }

            // dialog.dismiss();
            return builder.toString();
        }
      /*  protected void onPostExecute(String result) {
            String weatherInfo="Weather Report  is: \n";
            try{
                // JSONObject jsonObject = new JSONObject(result);
                String resultout="True";
                String out="";
                List<ProductMaster> Croplist = new ArrayList<ProductMaster>();

                JSONObject object = new JSONObject(result);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    // out = jObject.getString("Prd_desc1");
                    Croplist.add(new ProductMaster(jObject.getString("Prd_Code"), jObject.getString("Prd_desc1")));
                }
                ArrayAdapter<ProductMaster> adapter = new ArrayAdapter<ProductMaster>
                        (context.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCroptype.setAdapter(adapter);
                dialog.dismiss();


            }

            catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage().toString());
                dialog.dismiss();
            }

        } */
    }
    public class GetAppversion extends AsyncTask<String, String, String> {


        private String appname,token,imei;


        public GetAppversion(String appname,String token,String imei ){
            this.appname=appname;
            this.token=token;
            this.imei =imei;
        }
        protected void onPreExecute() {
            dialog.setMessage("Loading....");
            dialog.show();

        }
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "GetAppversionNew"));
            postParameters.add(new BasicNameValuePair("xmlString",""));
            //String Urlpath1= Urlpath+"?appname="+appname+"";
            String Urlpath1= MDOurlpath+"?appname="+appname+"&token="+token+"&imei="+imei;
            HttpPost httppost = new HttpPost(Urlpath1);
           
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring= builder.toString();
                    dialog.dismiss();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                returnstring=null;//e.getMessage().toString();
                dialog.dismiss();
            }
            catch (ClientProtocolException e)
            {   e.printStackTrace();
                returnstring=null;//e.getMessage().toString();
                dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
                returnstring=null;//e.getMessage().toString();
                dialog.dismiss();
            }
            // dialog.dismiss();
            return builder.toString().trim();
        }

    }
    public void bindimage(ImageView img, String Path)
    {
        try {
            File imgFile = new File(Path);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);

            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public class BreederMasterDataIsFeedGiven extends AsyncTask<String, String, String> {

        private int action;
        private String userCode;
        private String packageName;

        public BreederMasterDataIsFeedGiven(int action, String userCode,String packageName) {
            this.action = action;
            this.userCode = userCode;
            this.packageName = packageName;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("from", "insertbreederData"));
            //String Urlpath1 = Constants.IS_FEEDBACK_GIVEN + "?UserCode=" + userCode;
            String Urlpath1 = Constants.IS_FEEDBACK_NEW + "?UserCode=" + userCode +"&packageName="+packageName;
            Log.d("Is FeedbackGiven","Urlpath1 :"+Urlpath1);

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
                    returnstring = builder.toString();
                }
            } catch (UnsupportedEncodingException e) {
                Log.d("MSG",e.getMessage());
                returnstring = e.getMessage().toString();
            } catch (ClientProtocolException e) {
                Log.d("MSG",e.getMessage());
                Log.d("MSG",e.getMessage());
            } catch (Exception e) {
                Log.d("MSG",e.getMessage());
                returnstring = e.getMessage().toString();
            }
            Log.d("Is FeedbackGiven","Return String :"+builder.toString());
            return builder.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
