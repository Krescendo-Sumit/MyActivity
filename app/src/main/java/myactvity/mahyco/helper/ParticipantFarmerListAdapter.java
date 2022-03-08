package myactvity.mahyco.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import myactvity.mahyco.R;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class ParticipantFarmerListAdapter extends RecyclerView.Adapter<ParticipantFarmerListAdapter.ParticipantFarmerListHolder> {

    private Context context;
    private ArrayList<ParticipantFarmerListModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    public ParticipantFarmerListAdapter(Context context, ArrayList<ParticipantFarmerListModel> mlist) {
        this.context = context;
        this.mlist = mlist;

        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        setHasStableIds(true);

    }

    @NonNull
    @Override
    public ParticipantFarmerListAdapter.ParticipantFarmerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_participant_farmer, null);

        return new ParticipantFarmerListHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ParticipantFarmerListHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final ParticipantFarmerListModel participantFarmerListModel = mlist.get(i);



            demoModelViewHolder.txtVillageName.setText(participantFarmerListModel.getLastVisit());
            demoModelViewHolder.txtSNo.setText(participantFarmerListModel.getSno());
            demoModelViewHolder.txtFarmerNo.setText(participantFarmerListModel.getPlotType());
            demoModelViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Edit Functionality", Toast.LENGTH_SHORT).show();
                }
            });




        }


    }

    @Override
    public int getItemCount() {
        return (mlist != null ? mlist.size() : 0);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ParticipantFarmerListHolder extends RecyclerView.ViewHolder {
        TextView txtSNo;
        TextView txtVillageName,txtFarmerNo;
        CardView cardAddFarmer;
        ImageView imgEdit;

        ParticipantFarmerListHolder(@NonNull View itemView) {
            super(itemView);
            txtVillageName = (TextView) itemView.findViewById(R.id.txtVillageName);
            txtFarmerNo = (TextView) itemView.findViewById(R.id.txtFarmerNo);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardAddFarmer = (CardView) itemView.findViewById(R.id.cardAddFarmer);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);



        }
    }
}
