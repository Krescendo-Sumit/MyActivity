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

import myactvity.mahyco.MdoDaActivity;
import myactvity.mahyco.MdoExpensesActivity;
import myactvity.mahyco.MdoLeaveActivity;
import myactvity.mahyco.R;
import myactvity.mahyco.Utility;


/**
 * Created by Belal on 12/22/2015.
 */
public class SalesServiceAdapter extends RecyclerView.Adapter<SalesServiceAdapter.ViewHolder> {


    //Context
    private Context context;

    private ArrayList<String> items;
    private ArrayList<String> items2;
    public SalesServiceAdapter(Context context,
                               ArrayList<String> items, ArrayList<String> items2) //, int[] prgmImages)
    {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.items = items;
        this.items2 = items2;

    }

    @Override
    public SalesServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sales_service_dashboard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesServiceAdapter.ViewHolder holder, final int position) {
        try {

            Utility.setRegularFont(holder.tv, context);
            holder.tv.setText(items.get(position));





            holder.card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (items2.get(position).contains("leave")) {
                        Intent intent = new Intent(context, MdoLeaveActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    }
                    if (items2.get(position).contains("da")) {

                        Intent intent1 = new Intent(context, MdoDaActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent1);

                    }
                    if (items2.get(position).contains("expenses")) {
                        Intent intent2 = new Intent(context, MdoExpensesActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent2);

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