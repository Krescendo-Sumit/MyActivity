package myactvity.mahyco.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import myactvity.mahyco.R;

/**
 * Created by Akash Namdev on 2019-07-19.
 */
public class ValidateReviewListAdapter extends RecyclerView.Adapter<ValidateReviewListAdapter.DemoPlotReviewViewHolder> {

    private Context context;
    private ArrayList<DemoModelReviewListModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;
    String uid, date, mobilenumber, cordinates;


    @SuppressLint("CommitPrefEdits")
    public ValidateReviewListAdapter(Context context, ArrayList<DemoModelReviewListModel> mlist, String uid, String date, String mobilenumber, String cordinates) {
        this.context = context;
        this.mlist = mlist;
        this.uid = uid;
        this.date = date;
        this.mobilenumber = mobilenumber;
        this.cordinates = cordinates;


        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();

    }

    @NonNull
    @Override
    public ValidateReviewListAdapter.DemoPlotReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_validate_review, null);

        return new DemoPlotReviewViewHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DemoPlotReviewViewHolder demoPlotReviewViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final DemoModelReviewListModel demoModelReviewListModel = mlist.get(i);


            demoPlotReviewViewHolder.txtFarmerName.setText(demoModelReviewListModel.getFarmerName());
            demoPlotReviewViewHolder.txtTaluka.setText(demoModelReviewListModel.getTaluka());
            demoPlotReviewViewHolder.txtMobileNo.setText(demoModelReviewListModel.getMobileNumber());
            demoPlotReviewViewHolder.txtCrop.setText(demoModelReviewListModel.getCrop());
            demoPlotReviewViewHolder.txtSowingDate.setText(date);
            demoPlotReviewViewHolder.txtProduct.setText(demoModelReviewListModel.getProduct());
            demoPlotReviewViewHolder.txtSNo.setText(demoModelReviewListModel.getSno());
            demoPlotReviewViewHolder.txtVisitingDate.setText(demoModelReviewListModel.getVisitingDate());
            demoPlotReviewViewHolder.txtNoOfVisits.setText(demoModelReviewListModel.getReviewCount());

            String cordinates = demoModelReviewListModel.getCoordinates();


            String Address = "";
           /* if (cordinates != null && cordinates.contains("ADDRESS")) {
                String[] parts = cordinates.split("-");
                if (parts != null && parts.length > 0)
                    Address = parts[1];
                if (Address != null && !Address.contains("null")) {
                    Address = parts[1];

                } else Address = "";


            }
            demoPlotReviewViewHolder.txtAddress.setText(Address);
           */
            if (cordinates != null && cordinates.contains("ADDRESS")) {
                String[] parts = cordinates.split("-");
                if (parts != null && parts.length > 0) {
                    for (String item : parts) {
                        Address = item;
                    }
                }
                if (Address != null && !Address.contains("null")) {
                    demoPlotReviewViewHolder.txtAddress.setText(Address);
                }


            }


            if (demoModelReviewListModel.getIsSynced().equals("1")) {

                demoPlotReviewViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.issyncedcolor));


            } else if (demoModelReviewListModel.getIsSynced().equals("0")) {

                demoPlotReviewViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.isnotsyncedcolor));

            } else {


                demoPlotReviewViewHolder.cardMdo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent));


            }


//            demoPlotReviewViewHolder.imgBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Dialog builder = new Dialog(context);
//                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    builder.getWindow().setBackgroundDrawable(
//                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            //nothing;
//                        }
//                    });
//
//                    ImageView imageView = new ImageView(context);
//                    imageView.setImageURI(Uri.parse(demoModelReviewListModel.getImgPath()));
//                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT));
//                    builder.show();
//
//                }
//            });


//            demoPlotReviewViewHolder.bindimage(demoPlotReviewViewHolder.imgBtn,demoModelReviewListModel.getImgPath());

        }


    }

    @Override
    public int getItemCount() {
        return (mlist != null ? mlist.size() : 0);

    }

    class DemoPlotReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmerName;
        TextView txtMobileNo;
        TextView txtTaluka;
        TextView txtCrop;
        TextView txtSowingDate;
        TextView txtProduct;
        TextView txtSNo;
        TextView txtVisitingDate;
        TextView txtNoOfVisits;
        TextView txtAddress;

        CardView cardMdo;
        ImageView imgBtn;


        DemoPlotReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtTaluka = (TextView) itemView.findViewById(R.id.txtTaluka);
            txtCrop = (TextView) itemView.findViewById(R.id.txtCrop);
            txtSowingDate = (TextView) itemView.findViewById(R.id.txtSowingDate);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
            txtVisitingDate = (TextView) itemView.findViewById(R.id.txtVisitingDate);
            txtNoOfVisits = (TextView) itemView.findViewById(R.id.txtNoOfVisits);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardMdo = (CardView) itemView.findViewById(R.id.cardMdo);
            imgBtn = (ImageView) itemView.findViewById(R.id.imgBtn);


        }


        public void bindimage(ImageView img, String Path) {
            try {
                File imgFile = new File(Path);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
