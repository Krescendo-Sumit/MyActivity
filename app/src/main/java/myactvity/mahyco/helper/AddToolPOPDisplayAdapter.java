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

public class AddToolPOPDisplayAdapter extends RecyclerView.Adapter<AddToolPOPDisplayAdapter.AddToolListHolder> {

    private Context context;
    private ArrayList<POPDisplayModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    public AddToolPOPDisplayAdapter(Context context, ArrayList<POPDisplayModel> mlist) {
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
    public AddToolPOPDisplayAdapter.AddToolListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_tool_pop_display, null);

        return new AddToolPOPDisplayAdapter.AddToolListHolder(view);


    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final AddToolPOPDisplayAdapter.AddToolListHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final POPDisplayModel popDisplayModel = mlist.get(i);


            demoModelViewHolder.txtCropType.setText(popDisplayModel.getCropType());
            demoModelViewHolder.txtSNo.setText(String.valueOf(i + 1));
            demoModelViewHolder.txtNum.setText(popDisplayModel.getCount());
            demoModelViewHolder.txtCategoryName.setText(popDisplayModel.getToolCategory());
            demoModelViewHolder.txtToolName.setText(popDisplayModel.getToolName());

            switch (popDisplayModel.getToolCategory()){
                case "ADD POSTERS":
                    demoModelViewHolder.lblNum.setText(R.string.no_of_posters);
                    demoModelViewHolder.lblToolName.setVisibility(View.GONE);
                    demoModelViewHolder.txtToolName.setVisibility(View.GONE);
                    break;
                case  "ADD LEAFLET":
                    demoModelViewHolder.lblNum.setText(R.string.no_of_leaflet);
                    demoModelViewHolder.lblToolName.setVisibility(View.GONE);
                    demoModelViewHolder.txtToolName.setVisibility(View.GONE);
                    break;
                case  "ADD BUNTING":
                    demoModelViewHolder.lblNum.setText(R.string.no_of_bunting);
                    demoModelViewHolder.lblToolName.setVisibility(View.GONE);
                    demoModelViewHolder.txtToolName.setVisibility(View.GONE);
                    break;
                case "OTHERS":
                    demoModelViewHolder.lblNum.setText(R.string.number);
                    demoModelViewHolder.lblToolName.setVisibility(View.VISIBLE);
                    demoModelViewHolder.txtToolName.setVisibility(View.VISIBLE);
                    break;
            }



            demoModelViewHolder.txtProductName.setText(popDisplayModel.getProductName());

            demoModelViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    demoModelViewHolder.txtNum.setVisibility(View.GONE);
                    demoModelViewHolder.edtNo.setVisibility(View.VISIBLE);
                    demoModelViewHolder.imgSave.setVisibility(View.VISIBLE);
                    demoModelViewHolder.imgEdit.setVisibility(View.GONE);


                }
            });

            demoModelViewHolder.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlist.get(i).setCount(demoModelViewHolder.edtNo.getText().toString());
                    demoModelViewHolder.txtNum.setText(popDisplayModel.getCount());
                    demoModelViewHolder.txtNum.setVisibility(View.VISIBLE);
                    demoModelViewHolder.edtNo.setVisibility(View.GONE);
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

    class AddToolListHolder extends RecyclerView.ViewHolder {
        TextView txtSNo;
        TextView txtCategoryName, txtCropType, txtProductName,txtNum, lblNum ,lblToolName , txtToolName ;
        CardView cardAddFarmer;
        EditText edtNo , edtToolName;
        ImageView imgEdit;
        ImageView imgSave;

        AddToolListHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);
            txtCropType = (TextView) itemView.findViewById(R.id.txtCropType);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            lblToolName = (TextView) itemView.findViewById(R.id.lblToolName);
            txtToolName = (TextView) itemView.findViewById(R.id.txtToolName);
            txtNum = (TextView) itemView.findViewById(R.id.txtNum);
            edtNo = (EditText) itemView.findViewById(R.id.edtNo);
            edtToolName = (EditText) itemView.findViewById(R.id.edtToolName);
            lblNum = (TextView) itemView.findViewById(R.id.lblNum);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardAddFarmer = (CardView) itemView.findViewById(R.id.cardAddFarmer);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgSave = (ImageView) itemView.findViewById(R.id.imgSave);


        }
    }
}