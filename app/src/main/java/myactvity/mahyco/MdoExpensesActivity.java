package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import myactvity.mahyco.app.CommonExecution;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;

public class MdoExpensesActivity extends AppCompatActivity {

    ProgressDialog pd;
    public EditText txtExpenseDate,txtBills,
            txtExpenseAmount,txtExpensePlace,txtDiscription;
    public SearchableSpinner spexpenseType,spPaidBy;
    public Messageclass msclass;
    public CommonExecution cx;
    String division,usercode,org, cmbDistributor;
    public Button btnsave,btnTakephoto,btnadd;
    Config config;
    private SimpleDateFormat dateFormatter;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    SharedPreferences locdata,pref;
    private SqliteDatabase mDatabase;
    private Context context;
    public String SERVER = "";
//    public String leaverequsturl="http://cmsapiapp.orgtix.com/api/Leave/SubmitLeave";
    public String saleorderurl = "";
    LinearLayout my_linear_layout1;
    SharedPreferences.Editor editor, locedit;
    String  saleorg,customer, name , DLV_plant, customregroup,cmbproductlist;
    int check2=0;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdo_expenses);
        Objects.requireNonNull(getSupportActionBar()).hide(); //<< this
        context = this;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cx=new CommonExecution(this);
        SERVER = cx.MDOurlpath;
        saleorderurl = cx.saleSERVER;
        pd = new ProgressDialog(context);
        mDatabase = SqliteDatabase.getInstance(this);
        msclass=new Messageclass(this);




        txtExpenseDate=(EditText)findViewById(R.id.txtExpenseDate);
        txtBills=(EditText)findViewById(R.id.txtBills);
        txtExpenseAmount=(EditText)findViewById(R.id.txtExpenseAmount);
        txtExpensePlace=(EditText)findViewById(R.id.txtExpensePlace);
        txtDiscription=(EditText)findViewById(R.id.txtDiscription);
        btnsave=(Button)findViewById(R.id.btnsave);
        Utility.setRegularFont(btnsave, this);
        Utility.setRegularFont(txtExpenseAmount, this);
        Utility.setRegularFont(txtExpenseDate, this);
        Utility.setRegularFont(txtExpensePlace, this);
        Utility.setRegularFont(txtDiscription, this);

        spexpenseType=(SearchableSpinner)findViewById(R.id.spexpenseType);
        spPaidBy=(SearchableSpinner)findViewById(R.id.spPaidBy);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        config = new Config(context); //Here the context is passing
        usercode=pref.getString("UserID", null);
        txtExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(v);

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

                GeneralMaster gm = (GeneralMaster) spexpenseType.getSelectedItem();

                if (gm.Code()=="0") {
                    msclass.showMessage("Please select expense type ");
                    return;
                }
                if (txtExpenseDate.getText().length()==0) {
                    msclass.showMessage("Please  enter from date.");
                    return;
                }
                if (txtBills.getText().length()==0) {
                    msclass.showMessage("Please  enter bills");
                    return;
                }
                if (txtExpenseAmount.getText().length()==0) {
                    msclass.showMessage("Please  enter expense amount");
                    return;
                }
                if (txtExpensePlace.getText().length()==0) {
                    msclass.showMessage("Please  enter expense place");
                    return;
                }if (txtDiscription.getText().length()==0) {
                    msclass.showMessage("Please  enter discription");
                    return;
                }
                if (spPaidBy.getSelectedItemPosition() == 0) {
                    msclass.showMessage("Please Select paid by type");
                    return;
                }





            }
        });


        BindIntialData();
    }

    private void setDateTimeField(View v) {
        final EditText txt=(EditText)v;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                txt.setText(dateFormatter.format(dateSelected.getTime()));

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

    private  void BindIntialData() {

        try {
            spexpenseType.setAdapter(null);
            List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
            gm.add(new GeneralMaster("0", "Select Expense Type"));
            gm.add(new GeneralMaster("1", "Options"));
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spexpenseType.setAdapter(adapter);

            spPaidBy.setAdapter(null);
            List<String> gm2 = new ArrayList<String>();
            gm2.add(new String( "Select Paid By"));
            gm2.add(new String( "Options"));
            gm2.add(new String( "Self"));
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                    (this.context, android.R.layout.simple_spinner_dropdown_item, gm2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPaidBy.setAdapter(adapter2);





        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
