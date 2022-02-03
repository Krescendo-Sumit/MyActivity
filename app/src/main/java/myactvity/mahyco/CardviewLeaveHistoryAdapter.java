package myactvity.mahyco;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import myactvity.mahyco.utils.LeaveHistoryModel;


public class CardviewLeaveHistoryAdapter extends RecyclerView.Adapter<CardviewLeaveHistoryAdapter.ItemViewHolder> {

    List<LeaveHistoryModel> itemList;
    Context context;
    public CardviewLeaveHistoryAdapter(Context context, List<LeaveHistoryModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        Log.d("itemList:: ", itemList.toString());
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_leave_history, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder myViewHolder, int position) {
        myViewHolder.txtInitiationDateValue.setText(itemList.get(position).getInitiationDate());
        myViewHolder.txtAbsenceDaysValue.setText(itemList.get(position).getAbsenceDays());
        myViewHolder.txtFromDate.setText(itemList.get(position).getFromDate());
        myViewHolder.txtToValue.setText(itemList.get(position).getToDate());
        myViewHolder.txtStatusValue.setText(itemList.get(position).getStatus());


    }

    @Override
    public int getItemCount() {
       // Log.d("itemList:: ", String.valueOf(itemList.size()));
        return itemList == null ? 0 : itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtInitiationDateValue, txtAbsenceDaysValue, txtFromDate,txtToValue,txtStatusValue;
Button btnPullOut;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtInitiationDateValue = itemView.findViewById(R.id.txtInitiationDateValue);
            txtAbsenceDaysValue = itemView.findViewById(R.id.txtAbsenceDaysValue);
            txtFromDate = itemView.findViewById(R.id.txtFromDate);
            txtToValue = itemView.findViewById(R.id.txtToValue);
            txtStatusValue = itemView.findViewById(R.id.txtStatusValue);
            btnPullOut = itemView.findViewById(R.id.btnPullOut);
            //recyclerChild = itemView.findViewById(R.id.recyclerChild);
        }
    }
}

