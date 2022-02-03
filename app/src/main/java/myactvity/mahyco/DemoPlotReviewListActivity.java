package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.DemoModelReviewListModel;
import myactvity.mahyco.helper.DemoPlotReviewListAdapter;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.AnimationItem;
import myactvity.mahyco.utils.GridRecyclerView;
import myactvity.mahyco.utils.ItemOffsetDecoration;

/**
 * Created by Akash Namdev on 2019-07-31.
 */
public class DemoPlotReviewListActivity extends AppCompatActivity {


    Button btnSubmit;
    public Messageclass msclass;
    public SqliteDatabase mDatabase;
    EditText etFixedPrice;
    FloatingActionButton faButton;
    Calendar dateSelected = Calendar.getInstance();
    private Context context;
    String action = "1";
    String usercode;
    // TextView txt_noRecords;
    Config config;
    GridRecyclerView recDemoList;
    ImageView imgBtnSync;
    TextView txtNotSynced, noDataText;
    String SERVER = "https://cmr.mahyco.com/MDOHandler.ashx";
    TextView lblheader;
    ProgressDialog dialog;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor, editor;
    ArrayList<DemoModelReviewListModel> mList = new ArrayList<>();
    boolean isRadioFixed = false;
    private AnimationItem[] mAnimationItems;
    String date, uid, mobilenumber, cordinates, address;
    DemoPlotReviewListAdapter adapterMDO;
    private long mLastClickTime = 0;
    ProgressBar progressBar;
    RelativeLayout relPRogress;
    RelativeLayout footer;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo_review_list);
        getSupportActionBar().hide(); //<< this
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mDatabase = SqliteDatabase.getInstance(this);

        dialog = new ProgressDialog(this);
//        progressDailog = new ProgressDialog(this);
//        progressDailog.setIndeterminate(false);
//        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar = (ProgressBar) findViewById(R.id.myProgress);
        relPRogress = (RelativeLayout) findViewById(R.id.relPRogress);
        footer = (RelativeLayout) findViewById(R.id.footer);

//        progressDailog.setMessage("Data Uploading");
        locdata = getApplicationContext().getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(this); //Here the context is passing
        getIntentData();
        recDemoList = (GridRecyclerView) findViewById(R.id.recDemoList);
        recDemoList.setLayoutManager(new GridLayoutManager(this, 1));

        faButton = (FloatingActionButton) findViewById(R.id.faButton);
        txtNotSynced = (TextView) findViewById(R.id.txtNotSynced);
        lblheader = (TextView) findViewById(R.id.lblheader);
        noDataText = (TextView) findViewById(R.id.noDataText);
        imgBtnSync = (ImageView) findViewById(R.id.imgBtnSync);
        //  txt_noRecords = (TextView) findViewById(R.id.txt_noRecords);
        createList();
        mAnimationItems = new AnimationItem[]{
                new AnimationItem("Slide from bottom", R.anim.gridlayout_animation_from_bottom)};

        usercode = pref.getString("UserID", null);
        msclass = new Messageclass(this);


        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPlotReviewListActivity.this, DemoModelReviewActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("uid", uid);
                intent.putExtra("mobilenumber", mobilenumber);
                intent.putExtra("cordinates", cordinates);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        int count = mDatabase.getRowCountReviewNotSynced("DemoReviewData", uid);
        txtNotSynced.setText(String.valueOf(count));


        runLayoutAnimation(recDemoList, mAnimationItems[0]);
        imgBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 8000) {
                    return;
                }


                relPRogress.setVisibility(View.VISIBLE);
                relPRogress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });
                dowork();

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                footer.setEnabled(false);
                footer.setClickable(false);


            }

        });

    }

    private void dowork() {
        progressBar.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {
                        uploadUpdatedDataModelRecords("mdo_demoModelVisitDetail");

                    }
                });
                try {

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void getIntentData() {
        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        date = i.getStringExtra("date");
        mobilenumber = i.getStringExtra("mobilenumber");
        cordinates = i.getStringExtra("cordinates");
        address = i.getStringExtra("address");

        Log.d("datafromprevious", uid + "::" + mobilenumber + "::" + cordinates + "::" + date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {

            handler = null;
        }
    }

    public String getLastVisitDate(String uid) {
        String visitingdate = "";

        visitingdate = mDatabase.fetchLastDate(uid);

        return visitingdate;

    }

    // get Review Table count
    public String getCountReview(String uid) {

        int count = mDatabase.getRowCountReviewModelList("DemoReviewData", uid);
        return String.valueOf(count);

    }


    //Get Data from db and make list for adapter
    public void createList() {

        String searchQuery = "select * from DemoReviewData  where uIdP='" + uid + "'";
        Log.d("searchQuery", "createList: " + searchQuery);
        @SuppressLint("Recycle")
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        mList.clear();
        int count = cursor.getCount();

        if (count > 0) {


            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = mDatabase.getResults(searchQuery);
                for (int i = 0; i < jsonArray.length(); i++) {


                    DemoModelReviewListModel demoModelReviewListModel = new DemoModelReviewListModel();


                    demoModelReviewListModel.setIsSynced(jsonArray.getJSONObject(i).getString("isSynced"));

                    demoModelReviewListModel.setArea(jsonArray.getJSONObject(i).getString("area"));
                    demoModelReviewListModel.setCoordinates(jsonArray.getJSONObject(i).getString("coordinates"));
                    demoModelReviewListModel.setCrop(jsonArray.getJSONObject(i).getString("crop"));
                    demoModelReviewListModel.setCropCondition(jsonArray.getJSONObject(i).getString("cropCondition"));
                    demoModelReviewListModel.setFieldObservation(jsonArray.getJSONObject(i).getString("fieldOther"));
                    demoModelReviewListModel.setMobileNumber(jsonArray.getJSONObject(i).getString("mobileNumber"));

                    demoModelReviewListModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));

                    demoModelReviewListModel.setReviewCount(getCountReview(jsonArray.getJSONObject(i).getString("uIdP")));
                    demoModelReviewListModel.setSno(String.valueOf(i + 1));
                    demoModelReviewListModel.setLastVisit(getLastVisitDate(jsonArray.getJSONObject(i).getString("uIdP")));

                    demoModelReviewListModel.setProduct(jsonArray.getJSONObject(i).getString("product"));
                    demoModelReviewListModel.setSowingDate(jsonArray.getJSONObject(i).getString("sowingDate"));
                    demoModelReviewListModel.setVisitingDate(jsonArray.getJSONObject(i).getString("visitingDate"));
                    demoModelReviewListModel.setuId(jsonArray.getJSONObject(i).getString("uId"));
                    demoModelReviewListModel.setUserCode(jsonArray.getJSONObject(i).getString("userCode"));
                    demoModelReviewListModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                    demoModelReviewListModel.setImgPath(jsonArray.getJSONObject(i).getString("imgPath"));
                    demoModelReviewListModel.setImgName(jsonArray.getJSONObject(i).getString("imgName"));
                    demoModelReviewListModel.setAddress(address == null ? "" : address);


                    mList.add(demoModelReviewListModel);


                }
                recDemoList.setVisibility(View.VISIBLE);
                noDataText.setVisibility(View.GONE);

                addData(mList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            recDemoList.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);

        }


    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.gridlayout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    public void addData(ArrayList<DemoModelReviewListModel> mList) {

        try {
            final Context context = recDemoList.getContext();
            final int spacing = 4;
            adapterMDO = new DemoPlotReviewListAdapter(context, mList, uid, date, mobilenumber, cordinates);
            recDemoList.setAdapter(adapterMDO);
            recDemoList.addItemDecoration(new ItemOffsetDecoration(spacing));
            adapterMDO.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createList();
    }

    public void uploadUpdatedDataModelRecords(String uploadReviewData) {
        String str = null;
        String searchQuery = "select  *  from DemoReviewData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        if (config.NetworkConnection()) {
//            progressDailog.show();
            if (count > 0) {

                try {
                    str = new UploadUpdatedDataServer(uploadReviewData).execute(SERVER).get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {

//                progressDailog.dismiss();
                msclass.showMessage("Data not available to sync");
                relPRogress.setVisibility(View.GONE);
                footer.setClickable(true);
                footer.setEnabled(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

        } else {
//            progressDailog.dismiss();
            msclass.showMessage("Internet Network not available");
            relPRogress.setVisibility(View.GONE);
            footer.setClickable(true);
            footer.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }


    }


    public String uploadUpdatedDataModel(String uploadReviewData) {
        String str = "";
        //int action = 2; old
        int action = 4 ; //new  added  new coloum  Entry date

        String searchQuery = "select  *  from DemoReviewData where  isSynced ='0'";

        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

        int count = cursor.getCount();
        JSONArray jsonArray = new JSONArray();
        if (count > 0) {

            try {
                byte[] objAsBytes = null;//new byte[10000];


                try {
                    jsonArray = mDatabase.getResults(searchQuery);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        String uId = jsonArray.getJSONObject(i).getString("uId");
                        String imageName = jsonArray.getJSONObject(i).getString("imgName");
                        String imagePath = jsonArray.getJSONObject(i).getString("imgPath");
                        jsonObject.put("Table", jsonArray.getJSONObject(i));
                        objAsBytes = jsonObject.toString().getBytes("UTF-8");
                        jsonArray = mDatabase.getResults(searchQuery);


                        str = syncUpdatedDemoVisitImage(uploadReviewData, SERVER, action, objAsBytes, uId, imageName, imagePath);

                        handleUpdatedDemoVisitImageResponse(uploadReviewData, str, uId);
                    }


                    Log.d("ObjectasBytes", objAsBytes.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cursor.close();


            } catch (Exception ex) {
                ex.printStackTrace();


            }
        }


        return str;
    }


    //AsyncTask Class for api batch code upload call

    public class UploadUpdatedDataServer extends AsyncTask<String, String, String> {


        public UploadUpdatedDataServer(String Funname) {


        }

        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {

            return uploadUpdatedDataModel("mdo_demoModelVisitDetail");

        }

        protected void onPostExecute(String result) {
            try {
                String resultout = result.trim();


                if (!result.equals("")) {

                    if (resultout.contains("True")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(DemoPlotReviewListActivity.this);

                        builder.setTitle("MyActivity");
                        builder.setMessage("Data Synced Successfully");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                relPRogress.setVisibility(View.GONE);
                                footer.setClickable(true);
                                footer.setEnabled(true);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                dialog.dismiss();

                            }
                        });


                        AlertDialog alert = builder.create();
                        alert.show();
//                    progressDailog.dismiss();

                        int count = mDatabase.getRowCountReviewNotSynced("DemoReviewData", uid);
                        txtNotSynced.setText(String.valueOf(count));
                        mList.clear();
                        createList();


                    } else {
                        msclass.showMessage(resultout + "mdo_demoModelVisitDetail--E");
                        relPRogress.setVisibility(View.GONE);
                        footer.setClickable(true);
                        footer.setEnabled(true);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }
                    Log.d("Response", resultout);
                } else {
                    msclass.showMessage("Poor Internet: Please try after sometime.");
                    relPRogress.setVisibility(View.GONE);
                    footer.setClickable(true);
                    footer.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public synchronized String syncUpdatedDemoVisitImage(String function, String urls, int action, byte[] objAsBytes, String uId, String imageName, String imagePath) {


        String encodedImage = mDatabase.getImageDatadetail(imagePath);


        String encodeData = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
        HttpClient httpclient = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
        postParameters.add(new BasicNameValuePair("Type", function));
        postParameters.add(new BasicNameValuePair("encodedData", encodeData));
        postParameters.add(new BasicNameValuePair("encodeImage", encodedImage));

        String Urlpath = urls + "?imgName=" + imageName + "&action=" + action;
        Log.d("url", "image" + Urlpath);
        Log.d("parameters", "params " + postParameters);
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

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.toString();
    }

    public void handleUpdatedDemoVisitImageResponse(String function, String resultout, String id) {
        if (function.equals("mdo_demoModelVisitDetail")) {
            if (resultout.contains("True")) {
                mDatabase.updateDemoReviwData(id, "1", "1");
                //msclass.sfhowMessage("Data Successfully Uploaded");


            } else {
                //  msclass.showMessage(resultout + "--Error");

            }
        }


        Log.d("rohitt", "syncUpdatedDemoVisitImage: " + resultout);
    }

}
