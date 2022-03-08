package myactvity.mahyco;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.TableMainLayout;

public class Report  extends AppCompatActivity {

    private TableMainLayout tmain ;
    public RelativeLayout RelativeLayout1;
    public Button next;
    ProgressDialog dialog;
    private TextView recyclableTextView;
    public String SERVER = "https://farm.mahyco.com/RetailerHandler.ashx";
    public Messageclass msclass;
    public CommonExecution cx;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String headers[] = {
            "Sr.No",
            "MDO_name",
            "Product",
            "Purpose",
            "VisitNo ",
            "Visit Time","Location","Comments"};

    int consize[] = {
            75,
            200,
            200,
            200, 200, 200,
            200,
            200, 200};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        RelativeLayout1 = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        // getSupportActionBar().hide(); //<< this
         setTitle("MDO Visit Report ");
        config = new Config(this); //Here the context is passing
        msclass=new Messageclass(this);
        cx=new CommonExecution(this);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        binddata();
    }

    public void binddata()
    {
        RelativeLayout1.removeAllViews();
        tmain=new TableMainLayout(this,RelativeLayout1,headers,consize);
        addHeaders();
        tmain.resizeBodyTableRowHeight();
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
        recyclableTextView.setTextSize(12);
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
                str = cx.new GetMDOVisitReport(2,pref.getString("UserID",null),"","").execute().get();
                // msclass.showMessage(str);
                JSONObject object = new JSONObject(str);
                JSONArray jArray = object.getJSONArray("Table");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    row1 = new TableRow(this);
                    row = new TableRow(this);
                    row1.addView(makeTableRowWithText(String.valueOf(i), 0));

                    row.addView(makeTableRowWithText(jObject.getString("MDO_name").toString(), 1));
                    row.addView(makeTableRowWithText(jObject.getString("Product").toString(), 2));
                    row.addView(makeTableRowWithText(jObject.getString("Purpose").toString(), 3));
                    row.addView(makeTableRowWithText(jObject.getString("VisitNo").toString(), 4));
                    row.addView(makeTableRowWithText(jObject.getString("Time").toString(), 5));
                    row.addView(makeTableRowWithText(jObject.getString("Location").toString(), 6));
                    row.addView(makeTableRowWithText(jObject.getString("Comments").toString(), 7));
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

}

