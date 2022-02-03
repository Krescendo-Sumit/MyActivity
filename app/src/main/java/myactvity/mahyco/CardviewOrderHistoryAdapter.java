package myactvity.mahyco;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import myactvity.mahyco.utils.PaymentHistoryTableModel;


public class CardviewOrderHistoryAdapter extends RecyclerView.Adapter<CardviewOrderHistoryAdapter.ItemViewHolder> {

    List<PaymentHistoryTableModel> itemList;
    Context context;

    public CardviewOrderHistoryAdapter(Context context, List<PaymentHistoryTableModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        Log.d("itemList:: ", itemList.toString());
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_order_history, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder myViewHolder, int position) {
        myViewHolder.txtTotalAmount.setText(itemList.get(position).getAmount());
        myViewHolder.txtFarmerName.setText(itemList.get(position).getFarmerName());

        if (itemList.get(position).getPaymentStatus().contains("SUCCESS")) {
            myViewHolder.txtStatus.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            myViewHolder.txtStatus.setText(itemList.get(position).getPaymentStatus());
        } else {
            myViewHolder.txtStatus.setText(itemList.get(position).getPaymentStatus().toUpperCase());
            myViewHolder.txtStatus.setTextColor(ContextCompat.getColor(context,R.color.redbus2));
        }

        if(itemList.get(position).getPayMode()!=null) {
            String paymentMode = itemList.get(position).getPayMode().toString();
            Log.d("PaymentMode", "DATA : " + paymentMode);
            if (paymentMode != null && !paymentMode.equals("")) {
                myViewHolder.layoutPayMode.setVisibility(View.VISIBLE);
                myViewHolder.tvPayModeVal.setText(itemList.get(position).getPayMode());
            }
        }
        myViewHolder.txtSerialNumber.setText(itemList.get(position).getSerialNumber() + ".");
        myViewHolder.txtCreratedDate.setText(itemList.get(position).getPaymentDate());
        myViewHolder.txtProduct.setText(itemList.get(position).getProduct());
        myViewHolder.txtMobNumber.setText(itemList.get(position).getMobileno());
        myViewHolder.txtVillage.setText(itemList.get(position).getVillage());
        myViewHolder.txtTotalCoupon.setText(itemList.get(position).getTotalCoupon());
        myViewHolder.recyclerChild.setLayoutManager(new LinearLayoutManager(context));
//        CardviewOrderHistoryChildAdapter mCardviewOrderHistoryChildAdapter=new CardviewOrderHistoryChildAdapter(context,itemList.get(position).getOrderdetail());
//        myViewHolder.recyclerChild.setAdapter(mCardviewOrderHistoryChildAdapter);

    }

    @Override
    public int getItemCount() {
        // Log.d("itemList:: ", String.valueOf(itemList.size()));
        return itemList == null ? 0 : itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtTotalAmount, txtFarmerName, txtCreratedDate, txtStatus, txtSerialNumber, txtMobNumber, txtVillage, txtTotalCoupon, txtProduct;
        RecyclerView recyclerChild;
        LinearLayout layoutPayMode;
        TextView tvPayModelbl, tvPayModeVal;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmountValue);
            txtFarmerName = itemView.findViewById(R.id.txtInitiationDateValue);
            txtSerialNumber = itemView.findViewById(R.id.txtSerialNumber);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtMobNumber = itemView.findViewById(R.id.txtMobNumber);
            txtVillage = itemView.findViewById(R.id.txtVillage);
            txtTotalCoupon = itemView.findViewById(R.id.txtTotalCoupon);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtCreratedDate = itemView.findViewById(R.id.txtCreateDate);
            recyclerChild = itemView.findViewById(R.id.recyclerChild);
            layoutPayMode = (LinearLayout)itemView.findViewById(R.id.layout_pmode);
            tvPayModelbl = itemView.findViewById(R.id.txtCS_pay_mode);
            tvPayModeVal = itemView.findViewById(R.id.txt_val_pay_mode);

        }
    }
}

