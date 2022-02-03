package myactvity.mahyco.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;
import myactvity.mahyco.app.GeneralMaster;
import myactvity.mahyco.myActivityRecording.digitalMarketing.Mahakisan;

public class AddPurchaseDetailAdapter extends RecyclerView.Adapter<AddPurchaseDetailAdapter.AddFarmerListHolder> {

    private Context context;
    private ArrayList<PurchaseDetailModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;
    Dialog dialog;
    SqliteDatabase  mDatabase;

    public AddPurchaseDetailAdapter(Context context, SqliteDatabase mDatabase, ArrayList<PurchaseDetailModel> mlist) {
        this.context = context;
        this.mlist = mlist;

        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        setHasStableIds(true);
        this.mDatabase=mDatabase;
    }



    @NonNull
    @Override
    public AddFarmerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_purchase_detail, null);

        return new AddFarmerListHolder(view);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AddFarmerListHolder demoModelViewHolder, final int i) {

        if (!mlist.isEmpty()) {
            final PurchaseDetailModel purchaseDetailModel = mlist.get(i);


            demoModelViewHolder.txtCrop.setText(purchaseDetailModel.getCropType());
            demoModelViewHolder.txtSNo.setText(String.valueOf(i + 1));
            demoModelViewHolder.txtProductName.setText(purchaseDetailModel.getProductPurchase());
            demoModelViewHolder.txtQtyNo.setText(purchaseDetailModel.getQty());

            demoModelViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // ((Mahakisan)context).editPurchaseDetail(purchaseDetailModel.getCropType(),purchaseDetailModel.getProductPurchase(),purchaseDetailModel.getQty());
                    openDialogForEdit(i,demoModelViewHolder);

                }
            });

            demoModelViewHolder.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mlist.get(i).setFarmerCount(demoModelViewHolder.edtFarmerNo.getText().toString());
//                    demoModelViewHolder.txtFarmerNo.setText(purchaseDetailModel.getFarmerCount());
//                    demoModelViewHolder.txtFarmerNo.setVisibility(View.VISIBLE);
//                    demoModelViewHolder.edtFarmerNo.setVisibility(View.GONE);
//                    demoModelViewHolder.imgSave.setVisibility(View.GONE);
//                    demoModelViewHolder.imgEdit.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void openDialogForEdit(final int i, final AddFarmerListHolder demoModelViewHolder) {

       final EditText etQty;
        final SearchableSpinner spProductPurchase,spCropType;
        Button btnUpdate;
        final String product;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_purchase_detail_edit);

        etQty = dialog.findViewById(R.id.etQty);
        spCropType =  dialog.findViewById(R.id.spCropType);
        spProductPurchase = dialog.findViewById(R.id.spProductPurchase);
        btnUpdate = dialog.findViewById(R.id.btnUpdate);

        List<GeneralMaster> croplst = bindcroptype("", spCropType, context);
       //bindProductName("", spProductPurchase);
        PurchaseDetailModel purchaseDetailModel = mlist.get(i);

            final String cropl = purchaseDetailModel.getCropType();
        List<GeneralMaster> productlst =    bindProductName(cropl,spProductPurchase);
             product = purchaseDetailModel.getProductPurchase();


        for (int j = 0; j < croplst.size(); j++) {
            if (cropl.equalsIgnoreCase(croplst.get(j).Desc())) {

                spCropType.setSelection(j);
            }
        }
        for (int k = 0; k < productlst.size(); k++) {
            if (product.trim().equalsIgnoreCase(productlst.get(k).Desc().trim())) {

                spProductPurchase.setSelection(k);
            }
        }

            etQty.setText(purchaseDetailModel.getQty());

            spCropType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    String croptype = gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");

                   if(!cropl.equals(croptype)) {
                       bindProductName(croptype, spProductPurchase);
                   }


                    ((Mahakisan) context).hideKeyboard(view);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spProductPurchase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {
                    gm.Desc().trim();// URLEncoder.encode(gm.Code().trim(), "UTF-8");


                    ((Mahakisan) context).hideKeyboard(view);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Mahakisan)context).purchaseDetailValidationEdit(spCropType,spProductPurchase,etQty,spCropType.getSelectedItem().toString())) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());

                    PurchaseDetailModel purchaseDetailModel = new PurchaseDetailModel();

                                purchaseDetailModel.setCropType(spCropType.getSelectedItem().toString());
                                purchaseDetailModel.setProductPurchase(spProductPurchase.getSelectedItem().toString());
                                purchaseDetailModel.setQty(etQty.getText().toString());

                                mlist.get(i).setQty(etQty.getText().toString());
                                mlist.get(i).setProductPurchase(spProductPurchase.getSelectedItem().toString());
                                mlist.get(i).setCropType(spCropType.getSelectedItem().toString());
                               demoModelViewHolder.txtQtyNo.setText(etQty.getText().toString());
                               demoModelViewHolder.txtProductName.setText(spProductPurchase.getSelectedItem().toString());
                               demoModelViewHolder.txtCrop.setText(spCropType.getSelectedItem().toString());
                               dialog.dismiss();
                }
            }
        });

        dialog.show();
    }



    public  List<GeneralMaster>  bindProductName(String croptype, SearchableSpinner spProductPurchase) {
        List<GeneralMaster> list=null;
        try {
            spProductPurchase.setAdapter(null);
           list = new ArrayList<GeneralMaster>();

            String searchQuery = "";
            Cursor cursor;
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                searchQuery = "SELECT * FROM CropMaster WHERE CropName='" + croptype + "'  ORDER BY 'CropName'";


            } else {
                Log.d("Crop type", "First time");
            }

            list.add(new GeneralMaster("SELECT PRODUCT PURCHASE",
                    "SELECT PRODUCT PURCHASE"));
            cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                list.add(new GeneralMaster(cursor.getString(0),
                        cursor.getString(0).toUpperCase()));
                cursor.moveToNext();
            }
            cursor.close();

            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (context, android.R.layout.simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProductPurchase.setAdapter(adapter);



        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  list;
    }
    public   List<GeneralMaster> bindcroptype( String Croptype, SearchableSpinner spCropType, Context mContext) {
        List<GeneralMaster> Croplist =null;
        try {
            spCropType.setAdapter(null);
             Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            Croplist.add(new GeneralMaster("SELECT CROP",
                    "SELECT CROP"));

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mContext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCropType.setAdapter(adapter);

        } catch (Exception ex) {
          // msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return Croplist;
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
        TextView txtSNo,txtCrop,txtProductName,txtQtyNo;
        CardView cardAddFarmer;
        ImageView imgEdit;
        ImageView imgSave;

        AddFarmerListHolder(@NonNull View itemView) {
            super(itemView);
            txtCrop = (TextView) itemView.findViewById(R.id.txtCrop);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtQtyNo = (TextView) itemView.findViewById(R.id.txtQtyNo);

            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            cardAddFarmer = (CardView) itemView.findViewById(R.id.cardAddFarmer);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgSave = (ImageView) itemView.findViewById(R.id.imgSave);


        }
    }
}