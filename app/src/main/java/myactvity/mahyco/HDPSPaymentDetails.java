package myactvity.mahyco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import myactvity.mahyco.app.Config;
import myactvity.mahyco.newupload.HDPSPaymentDetailAPI;

public class HDPSPaymentDetails extends AppCompatActivity implements HDPSPaymentDetailAPI.HDPSPaymentDepositListener {
    TableLayout tbl;
    HDPSPaymentDetailAPI api;
    Context context;
    TextView txt_details;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdpspayment_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=HDPSPaymentDetails.this;
        tbl=findViewById(R.id.tbl_details);
        txt_details=findViewById(R.id.txt_title);
        setTitle("HDPS Payment History");
        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        userCode = pref.getString("UserID", null);
         api=new HDPSPaymentDetailAPI(HDPSPaymentDetails.this,this);

        if(new Config(context).NetworkConnection())
         getData();
        else
        {
            Toast.makeText(context, "Check connection", Toast.LENGTH_SHORT).show();
        }

        txt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new Config(context).NetworkConnection())
                    getData();
                else
                {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        try{

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("Mdocode",userCode);
            api.getAllPaymentDetails(jsonObject);




        }catch (Exception e)
        {

        }
    }

    @Override
    public void onPaymnentDetails(HDPSPaymentDetailAPI.PaymentModel result) {
          if(result!=null)
          {
              if(result.isSuccess())
              {
                  try {
                      HDPSPaymentDetailAPI.Returnval data = result.getReturnval();
                //      Toast.makeText(this, "Size is " + data.getTable().size(), Toast.LENGTH_SHORT).show();
                      tbl.removeAllViews();

                      TableRow tr_header = new TableRow(context);

                      TextView txt_title_header = new TextView(context);
                      TextView txt_amount_header = new TextView(context);
                      TextView txt_date_header = new TextView(context);
                      TextView txt_Paystatus_header = new TextView(context);
                      TextView txt_balance_header = new TextView(context);

                      txt_title_header.setText("Details");
                      txt_amount_header.setText("Amount");
                      txt_date_header.setText("Date");
                      txt_Paystatus_header.setText("Type");
                      txt_balance_header.setText("Balance");

                      txt_title_header.setTextColor(Color.WHITE);
                      txt_amount_header.setTextColor(Color.WHITE);
                      txt_date_header.setTextColor(Color.WHITE);
                      txt_Paystatus_header.setTextColor(Color.WHITE);
                      txt_balance_header.setTextColor(Color.WHITE);

                      txt_title_header.setPadding(5,5,5,5);
                      txt_amount_header.setPadding(5,5,5,5);
                      txt_date_header.setPadding(5,5,5,5);
                      txt_Paystatus_header.setPadding(5,5,5,5);
                      txt_balance_header.setPadding(5,5,5,5);


                      txt_amount_header.setGravity(Gravity.RIGHT);
                      txt_balance_header.setGravity(Gravity.RIGHT);

                      tr_header.addView(txt_title_header);
                      tr_header.addView(txt_amount_header);
                      //   tr.addView(txt_date);
                      tr_header.addView(txt_Paystatus_header);
                      tr_header.addView(txt_balance_header);


                      txt_title_header.setTypeface(txt_title_header.getTypeface());
                      txt_amount_header.setTypeface(txt_title_header.getTypeface());
                      txt_date_header.setTypeface(txt_title_header.getTypeface());
                      txt_Paystatus_header.setTypeface(txt_title_header.getTypeface());
                      txt_balance_header.setTypeface(txt_title_header.getTypeface());
                      tr_header.setBackgroundColor(Color.parseColor("#39803e"));


                      tbl.addView(tr_header);
                      double finalbal=0;
                      for (int i = 0; i < data.getTable().size(); i++) {
                          HDPSPaymentDetailAPI.child c = data.getTable().get(i);

                          Log.i("Daaaa",c.getTitle());

                          TableRow tr = new TableRow(context);

                          TextView txt_title = new TextView(context);
                          TextView txt_amount = new TextView(context);
                          TextView txt_date = new TextView(context);
                          TextView txt_Paystatus = new TextView(context);
                          TextView txt_balance = new TextView(context);
                          txt_title.setWidth(300);
                          txt_title.setTypeface(txt_details.getTypeface());

                          txt_title.setPadding(5,5,5,5);
                                  txt_amount.setPadding(5,5,5,5);
                          txt_date.setPadding(5,5,5,5);
                                  txt_Paystatus.setPadding(5,5,5,5);
                          txt_balance.setPadding(5,5,5,5);

                          txt_amount.setGravity(Gravity.RIGHT);
                                  txt_balance.setGravity(Gravity.RIGHT);

                          txt_title.setText("" + c.getTitle()+"\n"+ConvertDateFormat(c.getDate()));
                          txt_amount.setText("" + c.getAmount());
                          txt_date.setText("" + c.getDate());
                          txt_Paystatus.setText("" + c.getPaystatus());

                          if(c.getPaystatus().toLowerCase().equals("credit"))
                          {
                              double amt= c.getAmount();
                              finalbal+=amt;

                          }else if(c.getPaystatus().toLowerCase().equals("debit"))
                          {
                              double amt= c.getAmount();
                              finalbal-=amt;
                              txt_amount.setTypeface(txt_details.getTypeface());

                              txt_amount.setTextColor(Color.parseColor("#39803e"));

                          }else
                          {

                          }
                          txt_balance.setText(""+finalbal);

                          tr.addView(txt_title);
                          tr.addView(txt_amount);
                       //   tr.addView(txt_date);
                          tr.addView(txt_Paystatus);
                          tr.addView(txt_balance);

                          if(i%2==0)
                              tr.setBackgroundColor(Color.parseColor("#bcb5b2a8"));

                          if(c.getPaystatus().toLowerCase().equals("debit"))
                          {
                              tr.setBackgroundColor(Color.parseColor("#80a6deaa"));

                          }
                          tbl.addView(tr);


                      }
                  }catch (Exception e)
                  {
                      Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
                  }


              }
          }
    }
    private String ConvertDateFormat(String entryDt) {

        Date myDate = null;
        String finalDate = "";
        SimpleDateFormat dateFormat;

        if (entryDt != null) {

            try {

                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                myDate = dateFormat.parse(entryDt);

            } catch (Exception e) {

                try {
                    dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    myDate = dateFormat.parse(entryDt);

                } catch (ParseException e1) {

                    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        myDate = dateFormat.parse(entryDt);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy");
                finalDate = timeFormat.format(myDate);
            } catch (Exception e) {
                return "NA";
            }
        }

        return finalDate;
    }

}