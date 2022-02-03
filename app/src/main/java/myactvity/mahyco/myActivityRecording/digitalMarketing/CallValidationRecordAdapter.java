package myactvity.mahyco.myActivityRecording.digitalMarketing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.R;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.DistributorCallModel;
import myactvity.mahyco.helper.FarmerCallModel;
import myactvity.mahyco.helper.RetailerCallModel;
import myactvity.mahyco.helper.SamruddhaKisanModel;
import myactvity.mahyco.helper.SqliteDatabase;

public class CallValidationRecordAdapter extends RecyclerView.Adapter<CallValidationRecordAdapter.CallValidationRecord> {

    private Context context;
    private List<SamruddhaKisanModel> mlist;
    private List<RetailerCallModel> retailerlist = new ArrayList<RetailerCallModel>();
    private List<FarmerCallModel> farmerlist = new ArrayList<FarmerCallModel>();
    private List<DistributorCallModel> distributorlist = new ArrayList<DistributorCallModel>();
    SqliteDatabase mDatabase;
    Config config;
    Prefs mPref;
    int tag;
    JSONArray jsonArray;
    FarmerCallModel farmerCallModel1;
    String mdo ="";

    public CallValidationRecordAdapter(Context context, String entity, SqliteDatabase mDatabase, String mdo, int tag) {
        this.context = context;
        config = new Config(context); //Here the context is passing
        mPref = Prefs.with(context);
        this.mlist = mlist;
        this.mDatabase = mDatabase;
        this.tag = tag;
        this.mdo= mdo;
        setHasStableIds(true);
        this.farmerCallModel1 = null;

        try {
            JSONArray jsonArray = new JSONArray(entity);
            this.jsonArray= new JSONArray(entity);

            switch (tag) {
                case 0:
                    getFarmerData(jsonArray);
                    break;

                case 1:
                    getDistributorData(jsonArray);
                    break;

                case 2:
                    getRetailerData(jsonArray);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getFarmerData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {

                FarmerCallModel farmercallModel = new FarmerCallModel();

            try {
                farmercallModel.setId(jsonArray.getJSONObject(i).getString("id"));
                farmercallModel.setUserCode(jsonArray.getJSONObject(i).getString("UserCode"));
                farmercallModel.setFarmerName(jsonArray.getJSONObject(i).getString("farmerName"));

                farmercallModel.setFarmerMobile(jsonArray.getJSONObject(i).getString("farmerMobile"));
                farmercallModel.setFarmerType(jsonArray.getJSONObject(i).getString("farmerType"));

                farmercallModel.setState(jsonArray.getJSONObject(i).getString("state"));
                farmercallModel.setCallTypeProductPromotion(jsonArray.getJSONObject(i).getString("CallTypeProductPromotion"));
                farmercallModel.setDistrict(jsonArray.getJSONObject(i).getString("district"));
                farmercallModel.setTaluka(jsonArray.getJSONObject(i).getString("taluka"));
                farmercallModel.setOtherVillage(jsonArray.getJSONObject(i).getString("otherVillage"));
                farmercallModel.setCallTypeOtherActivity(jsonArray.getJSONObject(i).getString("CallTypeOtherActivity"));
                farmercallModel.setCropDiscussed(jsonArray.getJSONObject(i).getString("CropDiscussed"));
                farmercallModel.setProductDiscussed(jsonArray.getJSONObject(i).getString("ProductDiscussed"));
                farmercallModel.setEntryDt(jsonArray.getJSONObject(i).getString("entryDt"));
                farmercallModel.setFarmerResponse(jsonArray.getJSONObject(i).getString("farmerResponse"));
                farmercallModel.setCallSummary(jsonArray.getJSONObject(i).getString("callSummary"));

                farmerlist.add(farmercallModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getDistributorData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {

            DistributorCallModel distributorCallModel = new DistributorCallModel();

            try {
                distributorCallModel.setId(jsonArray.getJSONObject(i).getString("id"));
                distributorCallModel.setUserCode(jsonArray.getJSONObject(i).getString("UserCode"));
                distributorCallModel.setTbm(jsonArray.getJSONObject(i).getString("tbm"));

                distributorCallModel.setDistributorName(jsonArray.getJSONObject(i).getString("DistributorName"));
                distributorCallModel.setDistributorMobile(jsonArray.getJSONObject(i).getString("distributorMobile"));
                distributorCallModel.setDistributorFirmName(jsonArray.getJSONObject(i).getString("distributorFirmName"));

                distributorCallModel.setState(jsonArray.getJSONObject(i).getString("State"));
                distributorCallModel.setCallTypeProductPromotion(jsonArray.getJSONObject(i).getString("CallTypeProductPromotion"));
                distributorCallModel.setDistrict(jsonArray.getJSONObject(i).getString("District"));
                distributorCallModel.setTaluka(jsonArray.getJSONObject(i).getString("Taluka"));
                distributorCallModel.setCallTypeOtherActivity(jsonArray.getJSONObject(i).getString("CallTypeOtherActivity"));
                distributorCallModel.setCropType(jsonArray.getJSONObject(i).getString("CropType"));
                distributorCallModel.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                distributorCallModel.setEntryDt(jsonArray.getJSONObject(i).getString("entryDt"));
                distributorCallModel.setDistributorResponse(jsonArray.getJSONObject(i).getString("DistributorResponse"));
                distributorCallModel.setCallSummary(jsonArray.getJSONObject(i).getString("CallSummary"));

                distributorlist.add(distributorCallModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void getRetailerData(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            RetailerCallModel retailerCallModel = new RetailerCallModel();

            try {
                retailerCallModel.setId(jsonArray.getJSONObject(i).getString("id"));
                retailerCallModel.setUserCode(jsonArray.getJSONObject(i).getString("UserCode"));
                retailerCallModel.setRetailerName(jsonArray.getJSONObject(i).getString("RetailerName"));
                    retailerCallModel.setState(jsonArray.getJSONObject(i).getString("State"));
                retailerCallModel.setCallTypeProductPromotion(jsonArray.getJSONObject(i).getString("CallTypeProductPromotion"));
                retailerCallModel.setDistrict(jsonArray.getJSONObject(i).getString("District"));
                retailerCallModel.setTaluka(jsonArray.getJSONObject(i).getString("Taluka"));
                retailerCallModel.setCallTypeOtherActivity(jsonArray.getJSONObject(i).getString("CallTypeOtherActivity"));
                retailerCallModel.setCropType(jsonArray.getJSONObject(i).getString("CropType"));
                retailerCallModel.setProductName(jsonArray.getJSONObject(i).getString("ProductName"));
                retailerCallModel.setEntryDt(jsonArray.getJSONObject(i).getString("entryDt"));
                retailerCallModel.setRetailerResponse(jsonArray.getJSONObject(i).getString("RetailerResponse"));
                retailerCallModel.setCallSummary(jsonArray.getJSONObject(i).getString("CallSummary"));
                retailerCallModel.setRetailerType(jsonArray.getJSONObject(i).getString("RetailerTypeMaster"));
                retailerCallModel.setRetailerMobile(jsonArray.getJSONObject(i).getString("RetailerMobile"));
                retailerCallModel.setRetailerFirmName(jsonArray.getJSONObject(i).getString("RetailerFirmName"));
                retailerCallModel.setValidateDt(jsonArray.getJSONObject(i).getString("validateDt"));

                retailerlist.add(retailerCallModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public CallValidationRecord onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        switch (tag) {
            case 0:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_farmer_validation_row, null);
                break;

            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_distributor_validation_row, null);
                break;

            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_retailer_validation_row, null);
                break;
        }


        return new CallValidationRecord(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final CallValidationRecord demoModelViewHolder, final int i) {
       final FarmerCallModel   farmercallModel ;
        try {
        switch (tag) {
            case 0:
                if (farmerlist.size() > 0) {
                       farmercallModel = farmerlist.get(i);
                    farmerCallModel1 = farmercallModel;
                    demoModelViewHolder.txtFarmer.setText(farmercallModel.getFarmerName());
                    demoModelViewHolder.txtFarmerMobNum.setText(farmercallModel.getFarmerMobile());
                    demoModelViewHolder.txtFarmerTaluka.setText(farmercallModel.getTaluka().equals("null")? "":farmercallModel.getTaluka());
                    demoModelViewHolder.txtFarmerDist.setText(farmercallModel.getDistrict().equals("null")? "":farmercallModel.getDistrict());
                    demoModelViewHolder.txtFarmerVillage.setText(farmercallModel.getOtherVillage().equals("null")? "":farmercallModel.getOtherVillage());
                    demoModelViewHolder.txtFarmerType.setText(farmercallModel.getFarmerType().equals("null")? "":farmercallModel.getFarmerType());

                }

                demoModelViewHolder.relRowFarmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                              Intent  intent    = new Intent(context, FarmerValidationRecordActivity.class);
                                try {
                                    intent.putExtra("farmercallModel",jsonArray.get(i).toString());
                                    // intent.putExtra("farmercallModel",farmercallModel.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                        v.getContext().startActivity(intent);

                    }

                });
                break;

            case 1:
                if (distributorlist.size() > 0) {
                    final DistributorCallModel distributorcalllModel = distributorlist.get(i);
                    int firstBracket = distributorcalllModel.getDistributorName().indexOf("("); // detect the first bracket character
                    String firmName = distributorcalllModel.getDistributorName().substring(0, firstBracket);


                    demoModelViewHolder.txtDisFirmName.setText(firmName);
                    demoModelViewHolder.txtDistributorDist.setText(distributorcalllModel.getDistrict());
                     demoModelViewHolder.txtDisTaluka.setText(distributorcalllModel.getTaluka().equals("null")? "":distributorcalllModel.getTaluka());
                    //demoModelViewHolder.txtVillage.setText(distributorcalllModel.());
                    demoModelViewHolder.txtDistMobNum.setText(distributorcalllModel.getDistributorMobile().equals("null")? "":distributorcalllModel.getDistributorMobile());
                }

                demoModelViewHolder.relRowDist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent    = new Intent(context, DistributorValidationRecordActivity.class);
                        try {
                            intent.putExtra("distributorcallModel",jsonArray.get(i).toString());
                            intent.putExtra("MDO",mdo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        v.getContext().startActivity(intent);
                    }
                });


                break;

            case 2:
                if (retailerlist.size() > 0) {
                    final RetailerCallModel retailercallModel = retailerlist.get(i);

                    if (retailercallModel.getValidateDt().equals("null")){
                        String[] name = retailercallModel.getRetailerName().split(","); //
                        String[] firm = name[1].split("\\(");

                        String mobile = firm[1].substring(0, firm[1].indexOf(")"));
                        demoModelViewHolder.txtFirmName.setText(firm[0]);
                        demoModelViewHolder.txtMobNum.setText(mobile);
                }else {
                        demoModelViewHolder.txtFirmName.setText(retailercallModel.getRetailerFirmName());
                        demoModelViewHolder.txtMobNum.setText(retailercallModel.getRetailerMobile());
                    }

                    demoModelViewHolder.txtDist.setText(retailercallModel.getDistrict());
                     demoModelViewHolder.txtTaluka.setText(retailercallModel.getTaluka().equals("null")? "":retailercallModel.getTaluka());
                    //demoModelViewHolder.txtVillage.setText(retailercallModel.());

                      demoModelViewHolder.txtRetailerType.setText(retailercallModel.getRetailerType().equals("null")? "":retailercallModel.getRetailerType());
                }


                demoModelViewHolder.relRowRetailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       Intent intent    = new Intent(context, RetailerValidationRecordActivity.class);
                        try {
                            intent.putExtra("retailercallModel",jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        v.getContext().startActivity(intent);
                    }
                });
                break;
        }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public int getItemCount() {

        int count = 0;

        switch (tag) {
            case 0:
                count = farmerlist.size();
                break;

            case 1:
                count = distributorlist.size();
                break;

            case 2:
                count = retailerlist.size();
                break;
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class CallValidationRecord extends RecyclerView.ViewHolder {
        TextView txtFarmerName, txtRetailerType, txtMobNum, txtDist, txtTaluka, txtVillage, txtLand, txtCrop, txtDate, txtFirmName,
                txtDisFirmName, txtDisTaluka, txtDistributorDist, txtDistMobNum,
                txtFarmerDist, txtFarmerTaluka, txtFarmerVillage, txtFarmer, txtFarmerMobNum, txtFarmerType;

        GridLayout cardView;
        RelativeLayout relRowFarmer,relRowDist,relRowRetailer;


        CallValidationRecord(@NonNull View itemView) {
            super(itemView);

            //Farmer detail
            relRowFarmer = itemView.findViewById(R.id.relRowFarmer);
            txtFarmerDist = (TextView) itemView.findViewById(R.id.txtFarmerDist);
            txtFarmerTaluka = (TextView) itemView.findViewById(R.id.txtFarmerTaluka);
            txtFarmerVillage = (TextView) itemView.findViewById(R.id.txtFarmerVillage);
            txtFarmer = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtFarmerMobNum = (TextView) itemView.findViewById(R.id.txtFarmerMobNum);
            txtFarmerType = (TextView) itemView.findViewById(R.id.txtFarmerType);


            // Retailer validation
            cardView = itemView.findViewById(R.id.gridview);
            relRowRetailer = itemView.findViewById(R.id.relRowRetailer);
            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtRetailerType = (TextView) itemView.findViewById(R.id.txtRetailerType);
            txtMobNum = (TextView) itemView.findViewById(R.id.txtMobNum);
            txtFirmName = (TextView) itemView.findViewById(R.id.txtFirmName);
            txtDist = (TextView) itemView.findViewById(R.id.txtDist);
            txtTaluka = (TextView) itemView.findViewById(R.id.txtTaluka);
            txtVillage = (TextView) itemView.findViewById(R.id.txtVillage);

            //Distributor validation
            relRowDist = itemView.findViewById(R.id.relRowDist);
            txtDisFirmName = (TextView) itemView.findViewById(R.id.txtDisFirmName);
            txtDisTaluka = (TextView) itemView.findViewById(R.id.txtDisTaluka);
            txtDistributorDist = (TextView) itemView.findViewById(R.id.txtDistributorDist);
            txtDistMobNum = (TextView) itemView.findViewById(R.id.txtDistMobNum);


        }


    }

}
