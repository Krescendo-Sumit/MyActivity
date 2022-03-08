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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myactvity.mahyco.R;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class AddFarmerListCropShowAdapter extends RecyclerView.Adapter<AddFarmerListCropShowAdapter.AddFarmerListHolder> {

    private Context context;
    private ArrayList<CropShowModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    public AddFarmerListCropShowAdapter(Context context, ArrayList<CropShowModel> mlist) {
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
    public AddFarmerListCropShowAdapter.AddFarmerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_farmer_village_crop_show, null);

        return new AddFarmerListHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AddFarmerListHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final CropShowModel cropShowModel = mlist.get(i);


            demoModelViewHolder.txtVillageName.setText(cropShowModel.getVillageName());
            demoModelViewHolder.txtSNo.setText(String.valueOf(i + 1));
            demoModelViewHolder.txtFarmerNo.setText(cropShowModel.getFarmerCount());
            demoModelViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    demoModelViewHolder.txtFarmerNo.setVisibility(View.GONE);
                    demoModelViewHolder.edtFarmerNo.setVisibility(View.VISIBLE);
                    demoModelViewHolder.imgSave.setVisibility(View.VISIBLE);
                    demoModelViewHolder.imgEdit.setVisibility(View.GONE);


                }
            });

            demoModelViewHolder.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlist.get(i).setFarmerCount(demoModelViewHolder.edtFarmerNo.getText().toString());
                    demoModelViewHolder.txtFarmerNo.setText(cropShowModel.getFarmerCount());
                    demoModelViewHolder.txtFarmerNo.setVisibility(View.VISIBLE);
                    demoModelViewHolder.edtFarmerNo.setVisibility(View.GONE);
                    demoModelViewHolder.imgSave.setVisibility(View.GONE);
                    demoModelViewHolder.imgEdit.setVisibility(View.VISIBLE);


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

    class AddFarmerListHolder extends RecyclerView.ViewHolder {
        TextView txtSNo;
        TextView txtVillageName, txtFarmerNo;
        CardView cardAddFarmer;
        EditText edtFarmerNo;
        ImageView imgEdit;
        ImageView imgSave;

        AddFarmerListHolder(@NonNull View itemView) {
            super(itemView);
            txtVillageName = (TextView) itemView.findViewById(R.id.txtVillageName);
            txtFarmerNo = (TextView) itemView.findViewById(R.id.txtFarmerNo);
            edtFarmerNo = (EditText) itemView.findViewById(R.id.edtFarmerNo);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardAddFarmer = (CardView) itemView.findViewById(R.id.cardAddFarmer);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgSave = (ImageView) itemView.findViewById(R.id.imgSave);


        }
    }
}
