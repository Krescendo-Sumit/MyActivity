package myactvity.mahyco;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.view.LayoutInflater;
        import android.view.View.OnClickListener;
        import myactvity.mahyco.app.Config;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
        import java.util.List;

        import myactvity.mahyco.helper.Messageclass;
        import myactvity.mahyco.helper.SqliteDatabase;

/**
 * Created by Belal on 12/22/2015.
 */
public class GridViewAdapter extends BaseAdapter{

    String [] result;
    String [] imageId;
    String [] ActivityName;
    List fraglist,acty;
    String From ;
    Config config;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private SqliteDatabase mDatabase;
    Messageclass  msclass;
    private static LayoutInflater inflater=null;
    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;

   /* public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
    } */
    public GridViewAdapter(Context context, String[] prgmNameList,String[] ImageList,String[] ActivityName,String From,List fraglist) //, int[] prgmImages)
    {
        // TODO Auto-generated constructor stub
        this.result=prgmNameList;
        this.context=context;
        this.imageId=ImageList;
        this.From =From;
        this.ActivityName=ActivityName;
        mDatabase = SqliteDatabase.getInstance(context);
        config = new Config(context); //Here the context is passing
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        msclass = new Messageclass(context);
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.programlist, parent, false);
        try
        {
            Holder holder=new Holder();
            holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

            holder.tv.setText(result[position]);
          //  Toast.makeText(context,"4554" , Toast.LENGTH_LONG).show();
            if (From =="UserHome") {
               //  holder.tv.setTextColor(Color.parseColor("#000000"));
               // holder.tv.setBackgroundResource(android.R.color.transparent);
               // holder.tv.setTextSize(15);
                String [] colorlist={"#5cffba","#ff6a72","#dd99ff","#ff7715","#ffcf2d"};
               // holder.img.setBackgroundColor(Color.parseColor(colorlist[position]));
                holder.tv.setTextColor(Color.parseColor("#000000"));
                holder.tv.setBackgroundResource(android.R.color.transparent);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(100,100);
               // holder.img.setLayoutParams(parms);  file:///android_asset/Images/
                Picasso.with(context).load("file:///android_asset/DashboardImages/" + imageId[position])//.transform(new TransformationCircle()) // resizes the image to these dimensions (in pixel)
                       // .centerCrop()
                        .into(holder.img);
                         rowView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                          Intent intent;
                            try {

                                if (ActivityName[position].toString() == "Myactivity") {
                                    // TBM and MDO it can login Opfline form

                                    String myTable = "Table1";//Set name of your table
                                    String searchQuery = "SELECT  *  FROM VillageLevelMaster";
                                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                    if(cursor.getCount() >0) {
                                        cursor.close();
                                        if (pref.getString("RoleID",null).equals("0")||
                                                pref.getString("RoleID",null)==null ||
                                                pref.getString("RoleID",null).equals("") ||
                                                pref.getString("RoleID",null).equals("4")) {
                                            intent = new Intent(context.getApplicationContext(), MyFieldActvity.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }
                                        else
                                        {  // intent = new Intent(context.getApplicationContext(), MyFieldActvityonline.class);
                                             intent = new Intent(context.getApplicationContext(), OfflineActivity.class);

                                           // if (config.NetworkConnection()) {
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //intent.putExtra("Pagetype", "PlotVisit");
                                                context.startActivity(intent);
                                           // }


                                        }

                                    }
                                    else
                                    {
                                        msclass.showMessage("Master data not available. \n" +
                                                "Please download the master data.");
                                    }


                                }

                                if (ActivityName[position].toString() == "MyTravel") {
                                    intent= new Intent(context.getApplicationContext(),MyTravel.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }

                                if (ActivityName[position].toString() == "RetailerTag") {
                                    intent= new Intent(context.getApplicationContext(),RetailerandDistributorTag.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Username") {
                                     intent = new Intent(context.getApplicationContext(), NewUserRegistration.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "UploadData") {
                                    intent= new Intent(context.getApplicationContext(),UploadData.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "DownloadData") {
                                    intent= new Intent(context.getApplicationContext(),DownloadMasterdata.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Innovation") {

                                    String myTable = "Table1";//Set name of your table
                                    String searchQuery = "SELECT  *  FROM VehicleMaster";
                                    Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                                     if(cursor.getCount() >0) {
                                         intent = new Intent(context.getApplicationContext(), Innovation.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                         context.startActivity(intent);
                                     }
                                     else
                                     {
                                         msclass.showMessage("Innovation day location not  found. \n" +
                                                 "Please download the master data.");
                                     }
                                }

                                if (ActivityName[position].toString() == "Report") {

                                   // intent= new Intent(context.getApplicationContext(),orderfromTBM.class);

                                    intent= new Intent(context.getApplicationContext(),ReportDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "Close") {
                                    editor.clear();
                                    editor.putString("UserID",null);
                                    editor.commit();
                                    intent= new Intent(context.getApplicationContext(),LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                                if (ActivityName[position].toString() == "SalesOrder") {
                                    // TBM and MDO it can login Opfline form
                                    //Toast.makeText(context,"Under development " , Toast.LENGTH_LONG).show();
                                        if (pref.getString("RoleID",null).equals("4")) {
                                            intent = new Intent(context.getApplicationContext(), orderfromTBM.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }
                                        else
                                        {
                                            intent = new Intent(context.getApplicationContext(), saleorderpending.class);
                                            //intent= new Intent(context.getApplicationContext(),TestImage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //intent.putExtra("Pagetype", "PlotVisit");
                                            context.startActivity(intent);
                                        }


                                }


                           }
                            catch(Exception ex)
                            {
                                Toast.makeText(context,ex.getMessage() , Toast.LENGTH_LONG).show();
                                editor.clear();
                                editor.putString("UserID",null);
                                editor.commit();
                                intent= new Intent(context.getApplicationContext(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                    }
                });




            }








        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return rowView;
    }

}