package myactvity.mahyco.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myactvity.mahyco.DemoModelReviewActivity;
import myactvity.mahyco.DemoPlotReviewListActivity;
import myactvity.mahyco.R;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class AdvanceBookingListAdapter extends RecyclerView.Adapter<AdvanceBookingListAdapter.DemoModelViewHolder> {

    private Context context;
    private ArrayList<DemoModelPlotListModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    @SuppressLint("CommitPrefEdits")
    public AdvanceBookingListAdapter(Context context, ArrayList<DemoModelPlotListModel> mlist) {
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
    public AdvanceBookingListAdapter.DemoModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_demo_model, null);

        return new DemoModelViewHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AdvanceBookingListAdapter.DemoModelViewHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final DemoModelPlotListModel demoModelPlotListModel = mlist.get(i);


            demoModelViewHolder.txtFarmerName.setText(demoModelPlotListModel.getFarmerName());
            demoModelViewHolder.txtTaluka.setText(demoModelPlotListModel.getTaluka());
            demoModelViewHolder.txtMobileNo.setText(demoModelPlotListModel.getMobileNumber());
            demoModelViewHolder.txtCrop.setText(demoModelPlotListModel.getCrop());
            demoModelViewHolder.txtSowingDate.setText(demoModelPlotListModel.getSowingDate());
            demoModelViewHolder.txtVillage.setText(demoModelPlotListModel.getVillage());
            demoModelViewHolder.txtProduct.setText(demoModelPlotListModel.getProduct());
            demoModelViewHolder.txtNoOfVisits.setText(demoModelPlotListModel.getReviewCount());
            demoModelViewHolder.txtVisitingDate.setText(demoModelPlotListModel.getLastVisit());
            demoModelViewHolder.txtSNo.setText(demoModelPlotListModel.getSno());
            demoModelViewHolder.txtPlotType.setText(demoModelPlotListModel.getPlotType());

            if (demoModelPlotListModel.getIsSynced().equals("1")) {
                demoModelViewHolder.cardMdo.getBackground().setTint(Color.argb( 23,127, 180, 70));

            }else if (demoModelPlotListModel.getIsSynced().equals("0")){

                demoModelViewHolder.cardMdo.getBackground().setTint(Color.argb(23,248,69,99));
            }else {


                demoModelViewHolder.cardMdo.getBackground().setTint(Color.argb(53,255, 255, 255));

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



                    Intent intent= new Intent(context.getApplicationContext(), DemoPlotReviewListActivity.class);


                    intent.putExtra("date",mlist.get(i).getSowingDate());
                    intent.putExtra("uid",mlist.get(i).getuId());
                    intent.putExtra("mobilenumber",mlist.get(i).getMobileNumber());
                    intent.putExtra("cordinates",mlist.get(i).getCoordinates());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

            demoModelViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    Intent intent= new Intent(context.getApplicationContext(), DemoModelReviewActivity.class);
                    intent.putExtra("date",mlist.get(i).getSowingDate());
                    intent.putExtra("uid",mlist.get(i).getuId());
                    intent.putExtra("mobilenumber",mlist.get(i).getMobileNumber());
                    intent.putExtra("cordinates",mlist.get(i).getCoordinates());
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
        TextView txtVillage;
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
            txtMobileNo = (TextView) itemView.findViewById(R.id.etMobileNo);
            txtTaluka = (TextView) itemView.findViewById(R.id.txtTaluka);
            txtVillage = (TextView) itemView.findViewById(R.id.txtVillage);
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
