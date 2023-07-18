package com.mahyco.customercomplaint.ccfadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfinterfaces.CCFRecyclerViewClickListener;
import com.mahyco.customercomplaint.ccfnetwork.CCFPendingCmplntPojoMOdel;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.util.ArrayList;

public class CCFPendingCmplntAdapter extends RecyclerView.Adapter<CCFPendingCmplntAdapter.MyViewHolder> {

    private ArrayList<CCFPendingCmplntPojoMOdel.Data> mArrayList;

    private CCFRecyclerViewClickListener itemListener;
    private Context mContext;

    public CCFPendingCmplntAdapter(Context mContext, ArrayList<CCFPendingCmplntPojoMOdel.Data> mArrayList, CCFRecyclerViewClickListener itemListener) {
        this.itemListener = itemListener;
        this.mArrayList = mArrayList;
        this.mContext = mContext;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView mComplaintNumber;
        AppCompatTextView mComplaintDate;

        MyViewHolder(View v) {
            super(v);

            mComplaintNumber = v.findViewById(R.id.ccf_rbm_pending_complaint_no);
            mComplaintDate = v.findViewById(R.id.ccf_rbm_pending_cmplnt_type);

            CardView mUpcomingTrips = v.findViewById(R.id.ccf_rbm_pending_cmplnt_cardview);
            mUpcomingTrips.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CCFStoreData.clearSubmitComment(mContext);
            itemListener.ccfRecyclerViewListClicked(v, getLayoutPosition());
        }
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ccf_new_pending_complaint_adapter, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mComplaintNumber.setText(String.valueOf(mArrayList.get(position).getRegisterComplaintID()));
        holder.mComplaintDate.setText(mArrayList.get(position).getDateOfComplaint() != null ? mArrayList.get(position).getDateOfComplaint() : "");
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
