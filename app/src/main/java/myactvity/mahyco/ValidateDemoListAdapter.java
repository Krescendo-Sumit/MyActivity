package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myactvity.mahyco.helper.DemoModelPlotListModel;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class ValidateDemoListAdapter extends RecyclerView.Adapter<ValidateDemoListAdapter.DemoModelViewHolder> {

    private Context context;
    private ArrayList<DemoModelPlotListModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    @SuppressLint("CommitPrefEdits")
    public ValidateDemoListAdapter(Context context, ArrayList<DemoModelPlotListModel> mlist) {
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
    public DemoModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_demo_model, null);

        return new DemoModelViewHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final DemoModelViewHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final DemoModelPlotListModel demoModelPlotListModel = mlist.get(i);


            demoModelViewHolder.txtFarmerName.setText(demoModelPlotListModel.getFarmerName());
            demoModelViewHolder.txtMobileNo.setText(demoModelPlotListModel.getMobileNumber());
            demoModelViewHolder.txtCrop.setText(demoModelPlotListModel.getCrop());
            demoModelViewHolder.txtSowingDate.setText(demoModelPlotListModel.getSowingDate());
            //    if (!demoModelPlotListModel.getVillage().isEmpty() && !demoModelPlotListModel.getTaluka().isEmpty()) {
//                demoModelViewHolder.txtVillage.setVisibility(View.VISIBLE);
//                demoModelViewHolder.txtTaluka.setVisibility(View.VISIBLE);
//                demoModelViewHolder.lblTaluka.setVisibility(View.VISIBLE);
//                demoModelViewHolder.lblVillage.setVisibility(View.VISIBLE);
            demoModelViewHolder.txtVillage.setText(demoModelPlotListModel.getVillage());
            demoModelViewHolder.txtTaluka.setText(demoModelPlotListModel.getTaluka());
            demoModelViewHolder.lblFocussedVillage.setVisibility(View.GONE);
            demoModelViewHolder.txtFocussedVillage.setVisibility(View.GONE);
            demoModelViewHolder.txtFocussedVillage.setText(demoModelPlotListModel.getFocussedVillage());

//            }else {
//                demoModelViewHolder.txtVillage.setVisibility(View.GONE);
//                demoModelViewHolder.txtTaluka.setVisibility(View.GONE);
//                demoModelViewHolder.lblVillage.setVisibility(View.GONE);
//                demoModelViewHolder.lblTaluka.setVisibility(View.GONE);
//                demoModelViewHolder.lblFocussedVillage.setVisibility(View.VISIBLE);
//                demoModelViewHolder.txtFocussedVillage.setVisibility(View.VISIBLE);
//                demoModelViewHolder.txtFocussedVillage.setText(demoModelPlotListModel.getFocussedVillage());
//            }

            demoModelViewHolder.txtProduct.setText(demoModelPlotListModel.getProduct());
            demoModelViewHolder.txtNoOfVisits.setText(demoModelPlotListModel.getReviewCount());
            demoModelViewHolder.txtVisitingDate.setText(demoModelPlotListModel.getLastVisit());
            demoModelViewHolder.txtSNo.setText(demoModelPlotListModel.getSno());
            demoModelViewHolder.txtPlotType.setText(demoModelPlotListModel.getPlotType());

            if (demoModelPlotListModel.getIsSynced().equals("1")) {


                demoModelViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.issyncedcolor));


            } else if (demoModelPlotListModel.getIsSynced().equals("0")) {


                demoModelViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.isnotsyncedcolor));


            } else {


                demoModelViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent));


            }


//            if (!demoModelPlotListModel.getImgPath().equals("")){
//                demoModelViewHolder.imgBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Dialog builder = new Dialog(context);
//                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        builder.getWindow().setBackgroundDrawable(
//                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                //nothing;
//                            }
//                        });
//
//                        ImageView imageView = new ImageView(context);
//                        imageView.setImageURI(Uri.parse(demoModelPlotListModel.getImgPath()));
//                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                        builder.show();
//
//                    }
//                });
//
//            }else {
//
//                demoModelViewHolder.imgBtn.setVisibility(View.GONE);
//            }


            demoModelViewHolder.cardMdo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context.getApplicationContext(), DemoPlotReviewListActivity.class);


                    intent.putExtra("date", mlist.get(i).getSowingDate());
                    intent.putExtra("uid", mlist.get(i).getuId());
                    intent.putExtra("mobilenumber", mlist.get(i).getMobileNumber());
                    intent.putExtra("cordinates", mlist.get(i).getCoordinates());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

            demoModelViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context.getApplicationContext(), DemoModelReviewActivity.class);
                    intent.putExtra("date", mlist.get(i).getSowingDate());
                    intent.putExtra("uid", mlist.get(i).getuId());
                    intent.putExtra("mdoCode", mlist.get(i).getUserCode());
                    intent.putExtra("mobilenumber", mlist.get(i).getMobileNumber());
                    intent.putExtra("cordinates", mlist.get(i).getCoordinates());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    context.startActivity(intent);


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

    class DemoModelViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmerName;
        TextView txtMobileNo;
        TextView txtTaluka;
        TextView lblTaluka;
        TextView txtVillage;
        TextView lblVillage;
        TextView txtFocussedVillage;
        TextView lblFocussedVillage;
        TextView txtCrop;
        TextView txtSowingDate;
        TextView txtProduct;
        TextView txtNoOfVisits;
        TextView txtVisitingDate;
        TextView txtSNo;
        TextView txtPlotType;
        CardView cardMdo;
        ImageView imgBtn;
        Button btnAdd;

        DemoModelViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtTaluka = (TextView) itemView.findViewById(R.id.txtTaluka);
            txtVillage = (TextView) itemView.findViewById(R.id.txtVillage);
            txtFocussedVillage = (TextView) itemView.findViewById(R.id.txtFocussedVillage);
            lblFocussedVillage = (TextView) itemView.findViewById(R.id.lblFocussedVillage);
            lblTaluka = (TextView) itemView.findViewById(R.id.lblTaluka);
            lblVillage = (TextView) itemView.findViewById(R.id.lblVillage);
            txtCrop = (TextView) itemView.findViewById(R.id.txtCrop);
            txtSowingDate = (TextView) itemView.findViewById(R.id.txtSowingDate);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
            txtNoOfVisits = (TextView) itemView.findViewById(R.id.txtNoOfVisits);
            txtVisitingDate = (TextView) itemView.findViewById(R.id.txtVisitingDate);
            txtPlotType = (TextView) itemView.findViewById(R.id.txtPlotType);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardMdo = (CardView) itemView.findViewById(R.id.cardMdo);
            imgBtn = (ImageView) itemView.findViewById(R.id.imgBtn);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);


        }
    }
}
