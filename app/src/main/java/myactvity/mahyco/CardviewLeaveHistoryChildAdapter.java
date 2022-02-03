package myactvity.mahyco;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import myactvity.mahyco.utils.PaymentHistoryOrderDetailModel;


public class CardviewLeaveHistoryChildAdapter extends RecyclerView.Adapter<CardviewLeaveHistoryChildAdapter.ItemViewHolder> {

    List<PaymentHistoryOrderDetailModel> itemList;
    Context context;
    public CardviewLeaveHistoryChildAdapter(Context context, List<PaymentHistoryOrderDetailModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        Log.d("itemList:: ", itemList.toString());
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_order_history_child, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder myViewHolder, int position) {
        myViewHolder.txtAmountValue.setText(itemList.get(position).getAmount());
        myViewHolder.txtFarmerValue.setText(itemList.get(position).getFarmername());
       // myViewHolder.txtCreratedDate.setText(itemList.get(position).getBooktype());


    }

    @Override
    public int getItemCount() {
        Log.d("itemList:: ", String.valueOf(itemList.size()));
        return itemList == null ? 0 : itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtFarmerValue, txtAmountValue, txtCreratedDate;
RecyclerView recyclerChild;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmountValue = itemView.findViewById(R.id.txtAmountValue);
            txtFarmerValue = itemView.findViewById(R.id.txtFarmerValue);
           // txtCreratedDate = itemView.findViewById(R.id.txtCreateDate);

        }
    }
}

