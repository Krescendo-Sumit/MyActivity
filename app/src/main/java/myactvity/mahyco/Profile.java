package myactvity.mahyco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import com.squareup.picasso.Picasso;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Profile extends AppCompatActivity {


    public Button btnUpdate,btnTakephoto;
    public Spinner spnlanguage;
    private ImageView ivImage;
    public EditText txtmobileno,txtAadhar,txtname;
    public Spinner spState,spDist,spTaluka,spVillage;
    private String AadharNo,name;
    private String state;
    private String dist;
    public String imagestring;
    private String taluka;
    private String village,Iagree;
    public AutoCompleteTextView email;
    ProgressDialog dialog;
    public String SERVER = "https://farm.mahyco.com/TestHandler.ashx";
    public String SERVER2 = "http://10.80.50.153/maatest/FormerApp.asmx";
    public String  langcode="",mobileno,location,username;
    SharedPreferences pref,locdata;
    SharedPreferences.Editor editor,locedit;
    SharedPreferences locsp;
    public Messageclass msclass;
    Locale locale;
    public CommonExecution cx;
    private String userChoosenTask;
    public File destination;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    //public  cx.getCrop gcrop;
    Config config;
    Bitmap image;
    public static int check = 0,check2 = 0,check3 = 0,check4 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivImage= (ImageView) findViewById(R.id.ivImage);
        btnUpdate = (Button) findViewById(myactvity.mahyco.R.id.btnUpdate);
        txtmobileno=(EditText) findViewById(myactvity.mahyco.R.id.txtmobileno);
        txtname=(EditText) findViewById(myactvity.mahyco.R.id.txtname);
        spnlanguage=(Spinner) findViewById(myactvity.mahyco.R.id.spnlanguage);
        btnTakephoto = (Button) findViewById(myactvity.mahyco.R.id.btnTakephoto);
        config = new Config(this); //Here the context is passing
        spState = (Spinner) findViewById(R.id.spState);
        spDist = (Spinner) findViewById(R.id.spDist);
        spTaluka = (Spinner) findViewById(R.id.spTaluka);
        spVillage = (Spinner) findViewById(R.id.spVillage);
        txtmobileno=(EditText)findViewById(R.id.txtmobileno);
        txtAadhar=(EditText)findViewById(R.id.txtAadhar);
        txtname=(EditText)findViewById(R.id.txtname);
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);



        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getSupportActionBar().hide(); //<< this

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        locedit= locdata.edit();

      //  spState.setOnItemSelectedListener(this);

        bindlang();
        BindState();
        intialBind();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()==true) {
                   // LoginRequest();

                    if(TextUtils.isEmpty(txtmobileno.getText()))
                    {
                        txtmobileno.setError("Please enter Mobile Number");
                        return;
                    }
                    if(txtmobileno.getText().length()!=10)
                    {
                        txtmobileno.setError(" Mobile Number should be 10 digit.");
                        return;
                    }
                    if(TextUtils.isEmpty(txtname.getText()))
                    {
                        txtmobileno.setError("Please enter name");
                        return;
                    }
                   image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
                  /* if(TextUtils.isEmpty(txtAadhar.getText()))
                    {
                        txtmobileno.setError("Please enter Aadhar number");
                        return;
                    }
                    if(txtAadhar.getText().length()!=12)
                    {
                        txtAadhar.setError(" Aadhar number should be 12 digit.");
                        return;
                    }*/
       /* if(TextUtils.isEmpty(txtVillage.getText()))
        {
            txtmobileno.setError("Please enter village name");
            return;
        }*/
                    dialog.setMessage("Loading. Please wait...");
                    dialog.show();
                    mobileno=txtmobileno.getText().toString();
                    AadharNo=txtAadhar.getText().toString();

                    GeneralMaster st = (GeneralMaster) spState.getSelectedItem();
                    GeneralMaster dt = (GeneralMaster) spDist.getSelectedItem();
                    GeneralMaster tt = (GeneralMaster) spTaluka.getSelectedItem();
                    GeneralMaster vt = (GeneralMaster) spVillage.getSelectedItem();
                    GeneralMaster sl = (GeneralMaster) spnlanguage.getSelectedItem();

                    try {
                        langcode =URLEncoder.encode(sl.Code().trim(), "UTF-8");
                        state=URLEncoder.encode(st.Code().trim(), "UTF-8");
                        dist=URLEncoder.encode(dt.Code().trim(), "UTF-8");
                        taluka=URLEncoder.encode(tt.Code().trim(), "UTF-8");
                        village=URLEncoder.encode(vt.Code().trim(), "UTF-8");
                        name= URLEncoder.encode(txtname.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.setMessage("Loading....");
                    dialog.show();
                    new UpdateGetLangDt(2,image,langcode,name,AadharNo,mobileno,state,dist,taluka,village,"Y").execute(SERVER);
                }
            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    state= URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                check = check + 1;
                if (check > 1)
                {

                    BindDist(state);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                  Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    dist=URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                check2 = check2 + 1;
                if (check2 > 1)
                {

                    BindTaluka(dist);


                }
                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
               // Toast.makeText(Profile.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    taluka=URLEncoder.encode(gm.Code().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                check3 = check3 + 1;
                if (check3 > 1)
                {

                    BindVillage(taluka) ;


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });
        btnTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                selectImage();
            }
        });

    }


    private void selectImage(){
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Profile.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        destination = new File(Environment.getExternalStorageDirectory(),
                "Save.jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }
    private void onSelectFromGalleryResult(Intent data) {


        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private void intialBind()
    {
        txtmobileno.setText(locdata.getString("MobileNo", null));
        txtname.setText(URLDecoder.decode(locdata.getString("name", "")));
        txtAadhar.setText(locdata.getString("AadharNo", null));
        mobileno=locdata.getString("MobileNo", null);
        String base=locdata.getString("ProfileImage", null);
        state=locdata.getString("state", null);
        dist=locdata.getString("dist", null);
        check = 0;
        check2 = 0;
        check3 = 0;
        check4 = 0;
       // state=null;
        if(state!=null) {
            dialog.setMessage("Loading....");
            dialog.show();
            spState.setSelection(getIndex(spState, state));
            BindDist(state);
            spDist.setSelection(getIndex(spDist, dist));
            BindTaluka(dist) ;
            spTaluka.setSelection(getIndex(spTaluka, locdata.getString("taluka", null)));
            BindVillage(locdata.getString("taluka", null));
            spVillage.setSelection(getIndex(spVillage, locdata.getString("village", null)));

        }
        else
        {
            image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
            dialog.setMessage("Loading....");
            dialog.show();

            new UpdateGetLangDt(3,image,langcode,name,AadharNo,mobileno,state,dist,taluka,village,"Y").execute(SERVER);

        }

        if(base!=null) {
            locedit.putString("ProfileImage", imagestring);
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            //ivImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            Picasso.with(this).load("https://farm.mahyco.com/IssuePhoto/" +mobileno+".jpg").transform(new TransformationCircle())//resize(150,150) // resizes the image to these dimensions (in pixel)
                    //  .centerCrop()
                    .into(ivImage);
        }



    }
    private  void bindlang() {
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("English","English"));
        gm.add(new GeneralMaster("Guj","ગુજરાતી"));
        gm.add(new GeneralMaster("Telugu","Telugu"));
        gm.add(new GeneralMaster("Hindi","हिन्दी"));
        gm.add(new GeneralMaster("Marathi","मराठी"));
        gm.add(new GeneralMaster("Kannada","ಕನ್ನಡ"));
        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnlanguage.setAdapter(adapter);

    }
    public void  BindState()
    {
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            try {
                str = cx.new getState().execute().get();
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);// out = jObject.getString("Prd_desc1");
                    Croplist.add(new GeneralMaster(jObject.getString("State_code"),
                            jObject.getString("State_desc")));
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);
                dialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // msclass.showMessage(str);
        }
        else
        {
            //showMessage("Internet network not available.");
        }
        dialog.dismiss();
    }
    public void  BindDist(String state)
    {
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            try {
                str = cx.new getDist(state).execute().get();
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Croplist.add(new GeneralMaster(jObject.getString("dist_code"),
                            jObject.getString("dist_desc")));
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                dialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else
        {
            //showMessage("Internet network not available.");
        }
        dialog.dismiss();
    }
    public void  BindTaluka(String dist)
    {
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            try {
                str = cx.new getTaluka(dist).execute().get();
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Croplist.add(new GeneralMaster(jObject.getString("talq_code"),
                            jObject.getString("talq_desc")));
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                dialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //showMessage("Internet network not available.");
        }
        dialog.dismiss();
    }
    public void  BindVillage(String taluka)
    {
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            try {
                str = cx.new getVillage(taluka,"",Profile.this).execute().get();
                List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Croplist.add(new GeneralMaster(jObject.getString("vil_code"),
                            jObject.getString("vil_desc")));
                }
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (this,android.R.layout.simple_spinner_dropdown_item, Croplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spVillage.setAdapter(adapter);
                dialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //showMessage("Internet network not available.");
        }

        dialog.dismiss();

    }
    private class UpdateGetLangDt extends AsyncTask<String, String, String> {

        private Bitmap image;
        private String AadharNo,name;
        private String mobileno;
        private String state;
        private String dist;
        private String taluka;
        private String village,Iagree,lang;
        private Integer action;

        public UpdateGetLangDt(Integer action, Bitmap image,String lang,String name,String AadharNo,String mobileno,String state,String dist,String taluka,String village,String Iagree) {
            this.image = image;
            this.name=name;
            this.AadharNo=AadharNo;
            this.mobileno=mobileno;
            this.state=state;
            this.dist=dist;
            this.taluka=taluka;
            this.village=village;
            this.Iagree=Iagree;
            this.lang=lang;
            this.action=action;


        }
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... urls) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            // encode image to base64 so that it can be picked by saveImage.php file
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            imagestring=encodeImage;

            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "CommercialFarmerSignUp"));
            postParameters.add(new BasicNameValuePair("encodedData", encodeImage));


            String Urlpath=urls[0]+"?action="+action+"&name="+name+"&Mob="+mobileno+"&Gid="+""+"&AadharNo="+AadharNo+"&state="+state+"&dist="+dist+"&taluka="+taluka+"&village="+village+"&Iagree="+lang+"";

            HttpPost httppost = new HttpPost(Urlpath);
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

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage().toString());
                dialog.dismiss();
            } catch (Exception e) {
                msclass.showMessage(e.getMessage());
                e.printStackTrace();
                dialog.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                //String[] words=result.split("~");
                if(action==2) // Update
                {
                    setJSONTData(result, lang, mobileno, AadharNo, name);
                }
                if(action==3) // Get data
                {
                    getData(result, lang, mobileno, AadharNo, name);
                }
                dialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                dialog.dismiss();
            }

        }
    }
    private class UpdateGetLangDt1 extends AsyncTask<String, String, String> {

        private Bitmap image;

        private String  lang="",mobileno,location,username;


        public UpdateGetLangDt1(String lang,String mobileno,String location,String username) {

            this.lang=lang;
            this.mobileno=mobileno;
            this.location=location;
            this.username=username;
            // this.comments=comments;

        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("Type", "UpdateUserProfileGetLang"));
            // postParameters.add(new BasicNameValuePair("encodedData", ""));
            String Urlpath=urls[0]+"?lang="+lang+"&mobileno="+mobileno+"&location="+location+"&username="+username+"";
            // showMessage(Urlpath);
            HttpPost httppost = new HttpPost(Urlpath);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");

            String  displayText="";
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
                //displayText = WebService.invokeHelloWorldWS("Guj","","getGEOTagDetails");

                //  Toast.makeText(LoginActivity.this,displayText, Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                msclass.showMessage(e.getMessage());
                e.printStackTrace();
                dialog.dismiss();

            }
            // resp.setText("rtrtyr");

            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                // JSONObject jsonObject = new JSONObject(result);

                //// builder.setMessage("Comments Post Successfully");
                //builder.show();
                //bindGetCommentdata(result);
                // showMessage(result);
               // setJSONTData(result,lang,mobileno,AadharNo);
                dialog.dismiss();
               /* String[] words=result.split("~");

                editor.putString("UserID",words[1]);
                editor.putString("Pass","");
                editor.putString("Lang",words[0]);
                editor.putString("Displayname",words[3]);
                editor.putString("UID",words[1]);
                editor.putString("Mobile",words[2]);
                editor.commit();*/




            } catch (Exception e) {
                e.printStackTrace();
                msclass.showMessage(e.getMessage());
                dialog.dismiss();
            }

        }
    }

    public void setJSONTData(String jsonData,String lang,String MobileNo,String AadharNo,String name ) {
        try {


            locedit.putString("username",username);
            locedit.putString("name",name);
            locedit.putString("lang",lang);
            locedit.putString("MobileNo",MobileNo);
            locedit.putString("AadharNo",AadharNo);
            locedit.putString("state",state);
            locedit.putString("dist",dist);
            locedit.putString("taluka",taluka);
            locedit.putString("village",village);


            locedit.putString("ProfileImage",imagestring);
            locedit.commit();
            // JSONObject data = new JSONObject(jsonData); //Convert from string to object, can also use JSONArray
            editor.putString("JsonData",jsonData);
            editor.commit();
           // msclass.showMessage(state+""+dist+""+taluka+""+village);
            msclass.showMessage("Profile Updated Successfully.");
            dialog.dismiss();
            showChangeLangDialog(lang);

        } catch (Exception ex) {
            dialog.dismiss();
        }
    }
    public void getData(String jsonData,String lang,String MobileNo,String AadharNo,String name ) {
        try
        {
            check = 0;
            check2 = 0;
            check3 = 0;
            check4 = 0;

            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    spState.setSelection(getIndex(spState, jObject.getString("state")));
                    BindDist(jObject.getString("state"));
                    spDist.setSelection(getIndex(spDist, jObject.getString("dist")));
                    BindTaluka(jObject.getString("dist")) ;
                    spTaluka.setSelection(getIndex(spTaluka, jObject.getString("taluka")));
                    BindVillage(jObject.getString("taluka"));
                    spVillage.setSelection(getIndex(spVillage, jObject.getString("village")));

                    locedit.putString("name",jObject.getString("Name"));
                    locedit.putString("AadharNo",jObject.getString("AadharNo"));
                    locedit.putString("state", jObject.getString("state"));
                    locedit.putString("dist",jObject.getString("dist"));
                    locedit.putString("taluka",jObject.getString("taluka"));
                    locedit.putString("village",jObject.getString("village"));
                    locedit.commit();
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
                msclass.showMessage(e.toString());

            } // catch (JSONException e)

        } catch (Exception ex) {
            dialog.dismiss();
        }
    }


    public void showChangeLangDialog(String lang) {

        String langpos = lang;
        switch(langpos) {
            case "Marathi": //English
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "ma").apply();
                setLangRecreate("ma");
                return;

            case "Hindi": //Hindi
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();
                setLangRecreate("hi");
                return;
            case "Guj": //Guj
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "gu").commit();
                setLangRecreate("hi");
                return;
            case "Telugu": //Telgue
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "te").commit();
                setLangRecreate("hi");
                return;
            case "Kannada": //Kannada
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "ka").commit();
                setLangRecreate("hi");
                return;

            default: //By default set to english
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                setLangRecreate("en");
                return;
        }


    }
    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }
    public void recreate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

    }
    private boolean validation()
    {

        final String name=txtname.getText().toString();
        String mobile=txtmobileno.getText().toString();
        if(mobile.length()==0)
        {
            txtmobileno.requestFocus();
            txtmobileno.setError("Please enter mobile number.");
            return false;
        }
        if(name.length()==0)
        {
            txtname.requestFocus();
            txtname.setError("Please enter user name");
            return false;
        }

        return true;
    }
    @Override
    public void onBackPressed() {

        // Write your code here
        /*Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed(); */
    }
}
