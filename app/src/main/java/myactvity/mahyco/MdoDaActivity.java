package myactvity.mahyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SqliteDatabase;

public class MdoDaActivity extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtReason, txtAmount,
            txtDays, txtTodt, txtFromDt;
    public Messageclass msclass;
    public CommonExecution cx;
    String division, usercode, org, cmbDistributor, cmbDistributorshipto, shiptocustomer;
    public Button btnsave, btnTakephoto, btnadd;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    SharedPreferences locdata, pref;
    SharedPreferences.Editor loceditor;
    private SqliteDatabase mDatabase;
    private Context context;
    TextView txttotalvalue;
    public String SERVER = "";
    //    public String leaverequsturl="http://cmsapiapp.orgtix.com/api/Leave/SubmitLeave";
    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    SharedPreferences.Editor editor, locedit;
    String saleorg, customer, name, DLV_plant, customregroup, cmbproductlist;
    int check2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdo_da);
        getSupportActionBar().hide(); //<< this
        context = this;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cx = new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass = new Messageclass(this);
        // my_linear_layout1 = (LinearLayout)findViewById(R.id.my_linear_layout1);
        txtReason = (EditText) findViewById(R.id.txtReason);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtTodt = (EditText) findViewById(R.id.txtTodt);
        txtFromDt = (EditText) findViewById(R.id.txtFromDt);
        txtDays = (EditText) findViewById(R.id.txtDays);
        btnsave = (Button) findViewById(R.id.btnsave);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(txtFromDt, this);
        Utility.setRegularFont(txtReason, this);
        Utility.setRegularFont(txtTodt, this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode = pref.getString("UserID", null);
        txtTodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
                txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(), txtTodt.getText().toString())));

            }
        });
        txtFromDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);
                txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(), txtTodt.getText().toString())));

            }
        });
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (txtFromDt.getText().length() == 0) {
                    msclass.showMessage("Please  enter from date.");
                    return;
                }
                if (txtTodt.getText().length() == 0) {
                    msclass.showMessage("Please  enter To date.");
                    return;
                }
                if (txtAmount.getText().length() == 0) {
                    msclass.showMessage("Please  enter Amount.");
                    return;
                }
                if (txtReason.getText().length() == 0) {
                    msclass.showMessage("Please  enter Reason.");
                    return;
                }

            }
        });

    }

    private void setDateTimeField(View v) {
        final EditText txt = (EditText) v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));
                txtDays.setText(String.valueOf(getDaysBetweenDates(txtFromDt.getText().toString(), txtTodt.getText().toString())));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                txt.setText("");
            }
        });
        datePickerDialog.show();


    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            if (start.length() != 0 && end.length() != 0) {
                startDate = dateFormat.parse(start);
                endDate = dateFormat.parse(end);
                numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
                numberOfDays = numberOfDays + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
}
