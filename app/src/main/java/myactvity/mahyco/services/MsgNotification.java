package myactvity.mahyco.services;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import myactvity.mahyco.R;
import myactvity.mahyco.helper.SqliteDatabase;


public class MsgNotification  extends AppCompatActivity {

    TextView tv;
    private SqliteDatabase mDatabase;
    boolean flag=false;
    public LinearLayout my_linear_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_notification);
        mDatabase = SqliteDatabase.getInstance(this);
        getSupportActionBar().hide(); //<< this
        flag=mDatabase.isTableExists("NotificationDetail");
        if (flag ==false)
        {
            mDatabase.CreateTable("NotificationDetail");
        }
        my_linear_layout = (LinearLayout) findViewById(R.id.my_linear_layout);
        // Bundle extras = getIntent().getExtras();
         /* if(extras != null){
            String data1 = extras.getString("title");
            String data2 = extras.getString("message");
            System.out.println("Ddata1 : " + data1);
            tv.setText("Data Sent from Clicking Notification nData 1 : " + data1 + "nData 2 : " + data2);
        } */
        bindjsondata();
    }

    public void  bindjsondata()
    {

        String selectQuery = "SELECT * FROM  NotificationDetail  order by Time desc";
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery(selectQuery,null);
        try {

            if(cursor.getCount() >0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();

                    View view = LayoutInflater.from(this).inflate(R.layout.notificationpanel,null);
                    TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                    TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                    TextView txtdate = (TextView) view.findViewById(R.id.txtdate);
                    //ImageView imgprofile=(ImageView) view.findViewById(com.farmer.myproject.R.id.imgprofile);

                    txtMessage.setText(cursor.getString(cursor.getColumnIndex("Message")));
                    txtTitle.setText(cursor.getString(cursor.getColumnIndex("Title")));
                    txtdate.setText("Notification On:"+cursor.getString(cursor.getColumnIndex("Time")));

                    my_linear_layout.addView(view);
                }
            }

            // this.progressDialog.dismiss();
        } catch (Exception e) {
            Log.e("JSONException", "Error: " + e.toString());

        } // catch (JSONException e)


    }
}
