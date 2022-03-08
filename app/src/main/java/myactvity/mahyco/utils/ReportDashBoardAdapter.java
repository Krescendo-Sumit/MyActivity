package myactvity.mahyco.utils;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import myactvity.mahyco.MDOweeklyPlanReport;
import myactvity.mahyco.R;
import myactvity.mahyco.Utility;
import myactvity.mahyco.saleorderreport;
import myactvity.mahyco.demandassessmentsurvey;


/**
 * Created by Belal on 12/22/2015.
 */
public class ReportDashBoardAdapter extends RecyclerView.Adapter<ReportDashBoardAdapter.ViewHolder> {


    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> items;
    private ArrayList<String> items2;
    public ReportDashBoardAdapter(Context context,
                                  ArrayList<String> items, ArrayList<String> items2) //, int[] prgmImages)
    {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.items = items;
        this.items2 = items2;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_report_dashboard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {

            Utility.setRegularFont(holder.tv, context);
            holder.tv.setText(items.get(position));





            holder.card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (items2.get(position).contains("weekly")) {
                        Intent intent = new Intent(context, MDOweeklyPlanReport.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("ReportType", "WeeklyPlan");
                        context.startActivity(intent);

                    }
                    if (items2.get(position).contains("planned")) {

                        Intent intent1 = new Intent(context, MDOweeklyPlanReport.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.putExtra("ReportType", "PlannedVsActualSummary");
                        context.startActivity(intent1);

                    }
                    if (items2.get(position).contains("activity")) {
                        Intent intent2 = new Intent(context, MDOweeklyPlanReport.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.putExtra("ReportType", "ActivityProgress");
                        context.startActivity(intent2);

                    }
                    if (items2.get(position).contains("mytravel")) {

                        Intent intent3 = new Intent(context, MDOweeklyPlanReport.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.putExtra("ReportType", "Innovation");
                        context.startActivity(intent3);
                    }
                    if (items2.get(position).contains("retaileranddistributor")) {

                        Intent intent = new Intent(context, MDOweeklyPlanReport.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("ReportType" ,"RetailerandDistributor");
                        context.startActivity(intent);
                    }
                    if (items2.get(position).contains("saleorder")) {
                        Intent intent3 = new Intent(context, saleorderreport.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.putExtra("ReportType", "Innovation");
                        context.startActivity(intent3);
                    }
                    if (items2.get(position).contains("survey")) {
                        Intent intent3 = new Intent(context, demandassessmentsurvey.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.putExtra("ReportType", "Innovation");
                        context.startActivity(intent3);
                    }
                    if (items2.get(position).contains("demovisit")) {
                        Intent intent3 = new Intent(context, MDOweeklyPlanReport.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.putExtra("ReportType", "demovisit");
                        context.startActivity(intent3);
                    }

                }
            });






        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;

        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textView1);
            img = (ImageView) itemView.findViewById(R.id.imageView1);
            card = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


}