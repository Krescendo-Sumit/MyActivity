package com.mahyco.customercomplaint.ccfadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfnetwork.CCFViewCmplntPojoModel;

import java.util.List;

public class CCFEventsAdapter extends BaseExpandableListAdapter {

    private List<CCFViewCmplntPojoModel.ReturnValue.UpComingTrips> items;
    private Context context;

    public CCFEventsAdapter(Context context, List<CCFViewCmplntPojoModel.ReturnValue.UpComingTrips> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return this.items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.items;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View convertViews = convertView;
        try {
        if (convertViews == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertViews = layoutInflater.inflate(R.layout.ccf_list_item_title, null);
        }
        TextView listTitleTextView = convertViews.findViewById(R.id.textViewTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText("Feedback No:- " + String.valueOf(items.get(groupPosition).getRegisterComplaintID()));
        } catch (Exception e) {
            Toast.makeText(context, "grpview " + e.getCause(), Toast.LENGTH_SHORT).show();
        }
        return convertViews;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View convertViews = convertView;
        try {
            if (convertViews == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertViews = layoutInflater.inflate(R.layout.ccf_view_complaint_adapter_layout, null);
            }
            AppCompatTextView complaintNo = convertViews.findViewById(R.id.ccf_complaint_no);
            AppCompatTextView complaintType = convertViews.findViewById(R.id.ccf_vc_cmplnt_type);
            AppCompatTextView mainCatoegory = convertViews.findViewById(R.id.ccf_vc_cmplnt_sub_type);
            AppCompatTextView subCategory = convertViews.findViewById(R.id.ccf_vc_cmplnt_sub_categoty);
            AppCompatTextView lotNo = convertViews.findViewById(R.id.ccf_vc_lot_no);
            AppCompatTextView depot = convertViews.findViewById(R.id.ccf_vc_depot);
            AppCompatTextView businessUnit = convertViews.findViewById(R.id.ccf_vc_no_business_unit);
            AppCompatTextView crop = convertViews.findViewById(R.id.ccf_vc_crop);
            AppCompatTextView hybridVariety = convertViews.findViewById(R.id.ccf_vc_hybrid_variety);
            AppCompatTextView packingSize = convertViews.findViewById(R.id.ccf_vc_packing_size);
         //   AppCompatTextView lotQty = convertViews.findViewById(R.id.ccf_vc_lot_qty);
         //   AppCompatTextView noOfPkts = convertViews.findViewById(R.id.ccf_vc_no_of_pkts);
         //   AppCompatTextView totalLotQty = convertViews.findViewById(R.id.ccf_vc_total_lot_qty);
          //  AppCompatTextView totalNoOfPkts = convertViews.findViewById(R.id.ccf_vc_total_no_of_pkts);
            AppCompatTextView complaintDate = convertViews.findViewById(R.id.ccf_vc_cmplnt_date);
            AppCompatTextView farmerName = convertViews.findViewById(R.id.ccf_vc_farmer_name);
            AppCompatTextView mobileNo = convertViews.findViewById(R.id.ccf_vc_mobile_no);
            AppCompatTextView state = convertViews.findViewById(R.id.ccf_vc_state);
            AppCompatTextView district = convertViews.findViewById(R.id.ccf_vc_district);
            AppCompatTextView taluka = convertViews.findViewById(R.id.ccf_vc_taluka);
            AppCompatTextView vilalge = convertViews.findViewById(R.id.ccf_vc_village);
            AppCompatTextView sowingDate = convertViews.findViewById(R.id.ccf_vc_sowing_date);
            AppCompatTextView cropStage = convertViews.findViewById(R.id.ccf_vc_crop_stage);
            AppCompatTextView operationalDefectPkts = convertViews.findViewById(R.id.ccf_vc_oprnl_deftct_pkts);

            AppCompatTextView otherManualDetails = convertViews.findViewById(R.id.ccf_vc_other_details);
            LinearLayout otherManualDetailsLayout = convertViews.findViewById(R.id.ccf_vc_other_details_layout);

            LinearLayout admixtureDescriptionLayout = convertViews.findViewById(R.id.ccf_vc_gp_admixture_desc_layout);
            AppCompatTextView admixtureDesc = convertViews.findViewById(R.id.ccf_vc_gp_admixture_desc);

            LinearLayout admixturePercentLayout = convertViews.findViewById(R.id.ccf_vc_gp_admixture_percent_layout);
            AppCompatTextView admixturePercent = convertViews.findViewById(R.id.ccf_vc_gp_admixture_percent);

            LinearLayout offTypeDescLayout = convertViews.findViewById(R.id.ccf_vc_gp_off_type_desc_layout);
            AppCompatTextView offTypeDesc = convertViews.findViewById(R.id.ccf_vc_gp_off_type_desc);

            LinearLayout offTypePercentLayout = convertViews.findViewById(R.id.ccf_vc_gp_off_type_percent_layout);
            AppCompatTextView offTypePercent = convertViews.findViewById(R.id.ccf_vc_gp_off_type_percent);

            LinearLayout belowStandardLayout = convertViews.findViewById(R.id.ccf_vc_gm_below_std_layout);
            AppCompatTextView germPercent = convertViews.findViewById(R.id.ccf_vc_gm_germ_percent);

            LinearLayout lateGermLayout = convertViews.findViewById(R.id.ccf_vc_gm_late_germ_layout);
            AppCompatTextView lateGermDetails = convertViews.findViewById(R.id.ccf_vc_gm_late_germ_details);

            LinearLayout pestAttackLayout = convertViews.findViewById(R.id.ccf_vc_cp_pest_attack_layout);
            AppCompatTextView pestAttackDetails = convertViews.findViewById(R.id.ccf_vc_cp_pest_attack_details);

            LinearLayout diseaseInfestationLayout = convertViews.findViewById(R.id.ccf_vc_cp_dise_infest_layout);
            AppCompatTextView diseaseInfestationDetails = convertViews.findViewById(R.id.ccf_vc_cp_dise_infest_details);

            LinearLayout rainWaterdamageLayout = convertViews.findViewById(R.id.ccf_vc_rw_defect_pkts_layout);
            AppCompatTextView rainWaterDefectPkts = convertViews.findViewById(R.id.ccf_vc_rw_defect_pkts_details);

            LinearLayout operationaldefectPktsLayout = convertViews.findViewById(R.id.ccf_vc_oprnl_deftct_pkts_layout);

            AppCompatTextView remarks = convertViews.findViewById(R.id.ccf_vc_remarks);
            LinearLayout remarksLayout = convertViews.findViewById(R.id.ccf_vc_remarks_layout);

            LinearLayout photoFirstLayout = convertViews.findViewById(R.id.ccf_vc_photo_first_layout);
            LinearLayout photoSecondLayout = convertViews.findViewById(R.id.ccf_vc_photo_second_layout);
            LinearLayout photoThirdLayout = convertViews.findViewById(R.id.ccf_vc_photo_third_layout);
            LinearLayout photoFourthLayout = convertViews.findViewById(R.id.ccf_vc_photo_fourth_layout);

            ImageView photoFirstImageView = convertViews.findViewById(R.id.ccf_vc_photo_first);
            ImageView photoSecondImageView = convertViews.findViewById(R.id.ccf_vc_photo_second);
            ImageView photoThirdImageView = convertViews.findViewById(R.id.ccf_vc_photo_third);
            ImageView photoFourthImageView = convertViews.findViewById(R.id.ccf_vc_photo_fourth);

            AppCompatTextView complaintStatus = convertViews.findViewById(R.id.ccf_vc_cmplt_status);
            LinearLayout complaintStatusLayout = convertViews.findViewById(R.id.ccf_vc_cmplt_status_layout);

            complaintNo.setText(String.valueOf(this.items.get(groupPosition).getRegisterComplaintID()));
            complaintType.setText(this.items.get(groupPosition).getComplaintType() != null ? this.items.get(groupPosition).getComplaintType() : "");
            mainCatoegory.setText(this.items.get(groupPosition).getComplaintSubType() != null ? this.items.get(groupPosition).getComplaintSubType() : "");
            subCategory.setText(this.items.get(groupPosition).getSubCategoryDesc() != null ? this.items.get(groupPosition).getSubCategoryDesc() : "");
            lotNo.setText(this.items.get(groupPosition).getLotNumber() != null ? this.items.get(groupPosition).getLotNumber() : "");
            depot.setText(this.items.get(groupPosition).getDepot() != null ? this.items.get(groupPosition).getDepot() : "");
            businessUnit.setText(this.items.get(groupPosition).getBusinessUnit() != null ? this.items.get(groupPosition).getBusinessUnit() : "");
            crop.setText(this.items.get(groupPosition).getCropDtl() != null ? this.items.get(groupPosition).getCropDtl() : "");
            hybridVariety.setText(this.items.get(groupPosition).getHybrid_Variety() != null ? this.items.get(groupPosition).getHybrid_Variety() : "");
            packingSize.setText(String.valueOf(this.items.get(groupPosition).getPackingSize()));
         //   lotQty.setText(String.valueOf(this.items.get(groupPosition).getLotQty()));
          //  noOfPkts.setText(String.valueOf(this.items.get(groupPosition).getNoOfPkts()));
         //   totalLotQty.setText(String.valueOf(this.items.get(groupPosition).getTotalLotQty()));
       //     totalNoOfPkts.setText(String.valueOf(this.items.get(groupPosition).getTotalNoOfPkts()));
            complaintDate.setText(this.items.get(groupPosition).getDateOfComplaint() != null ? this.items.get(groupPosition).getDateOfComplaint() : "");
//        complaintStatus.setText(this.items.get(groupPosition).getComplaintStatus() != null ? this.items.get(groupPosition).getComplaintStatus() : "");
            farmerName.setText(this.items.get(groupPosition).getFarmerName() != null ? this.items.get(groupPosition).getFarmerName() : "");
            mobileNo.setText(this.items.get(groupPosition).getFarmerContact() != null ? this.items.get(groupPosition).getFarmerContact() : "");
            state.setText(this.items.get(groupPosition).getState() != null ? this.items.get(groupPosition).getState() : "");
            district.setText(this.items.get(groupPosition).getDistrict() != null ? this.items.get(groupPosition).getDistrict() : "");
            taluka.setText(this.items.get(groupPosition).getTaluka() != null ? this.items.get(groupPosition).getTaluka() : "");
            vilalge.setText(this.items.get(groupPosition).getVillage() != null ? this.items.get(groupPosition).getVillage() : "");
            sowingDate.setText(this.items.get(groupPosition).getSowingDate() != null ? this.items.get(groupPosition).getSowingDate() : "");
            cropStage.setText(this.items.get(groupPosition).getCropStage() != null ? this.items.get(groupPosition).getCropStage() : "");
//        operationalDefectPkts.setText(String.valueOf(this.items.get(groupPosition).getNo_PKtsBagDamagedOperationalComplaint()));
//        otherManualDetails.setText(this.items.get(groupPosition).getOtherManualTypingBox() != null ? this.items.get(groupPosition).getOtherManualTypingBox() : "");
//        admixtureDesc.setText(this.items.get(groupPosition).getGp_AdmixtureType() != null ? this.items.get(groupPosition).getGp_AdmixtureType() : "");
//        admixturePercent.setText(String.valueOf(this.items.get(groupPosition).getGp_AdmixturePercent()));
//        offTypeDesc.setText(this.items.get(groupPosition).getGp_OffType() != null ? this.items.get(groupPosition).getGp_OffType() : "");
//        offTypePercent.setText(String.valueOf(this.items.get(groupPosition).getGp_OffTypePercent()));
//        germPercent.setText(String.valueOf(this.items.get(groupPosition).getGrmPercent()));
//        lateGermDetails.setText(this.items.get(groupPosition).getLateGrm() != null ? this.items.get(groupPosition).getLateGrm() : "");
//        pestAttackDetails.setText(this.items.get(groupPosition).getPestAttackComment() != null ? this.items.get(groupPosition).getPestAttackComment() : "");
//        diseaseInfestationDetails.setText(this.items.get(groupPosition).getDiseaseInfestationComment() != null ? this.items.get(groupPosition).getDiseaseInfestationComment() : "");
//        rainWaterDefectPkts.setText(String.valueOf(this.items.get(groupPosition).getRain_NoPktsOrBagDamaged()));
//        remarks.setText(this.items.get(groupPosition).getRemarksInput() != null ? this.items.get(groupPosition).getRemarksInput() : "");
            if (!complaintType.getText().toString().equalsIgnoreCase("Operational Feedback") && subCategory.getText().toString().toLowerCase().contains("Others".toLowerCase())) {
                otherManualDetailsLayout.setVisibility(View.VISIBLE);
                otherManualDetails.setText(this.items.get(groupPosition).getOtherManualTypingBox() != null ? this.items.get(groupPosition).getOtherManualTypingBox() : "");
            } else {
                otherManualDetailsLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Genetic Purity")) {
                if (subCategory.getText().toString().toLowerCase().contains("admixture")) {
                    admixturePercentLayout.setVisibility(View.VISIBLE);
                    admixtureDescriptionLayout.setVisibility(View.VISIBLE);
                    admixtureDesc.setText(this.items.get(groupPosition).getGp_AdmixtureType() != null ? this.items.get(groupPosition).getGp_AdmixtureType() : "");
                    admixturePercent.setText(String.valueOf(this.items.get(groupPosition).getGp_AdmixturePercent()));
                } else {
                    admixtureDescriptionLayout.setVisibility(View.GONE);
                    admixturePercentLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("off types")) {
                    offTypeDescLayout.setVisibility(View.VISIBLE);
                    offTypePercentLayout.setVisibility(View.VISIBLE);
                    offTypeDesc.setText(this.items.get(groupPosition).getGp_OffType() != null ? this.items.get(groupPosition).getGp_OffType() : "");
                    offTypePercent.setText(String.valueOf(this.items.get(groupPosition).getGp_OffTypePercent()));
                } else {
                    offTypeDescLayout.setVisibility(View.GONE);
                    offTypePercentLayout.setVisibility(View.GONE);
                }
            } else {
                admixtureDescriptionLayout.setVisibility(View.GONE);
                admixturePercentLayout.setVisibility(View.GONE);
                offTypeDescLayout.setVisibility(View.GONE);
                offTypePercentLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Crop Performance")) {
                if (subCategory.getText().toString().toLowerCase().contains("pest attack-comment box")
                || subCategory.getText().toString().toLowerCase().contains("pest attack")) {
                    pestAttackLayout.setVisibility(View.VISIBLE);
                    pestAttackDetails.setText(this.items.get(groupPosition).getPestAttackComment() != null ? this.items.get(groupPosition).getPestAttackComment() : "");
                } else {
                    pestAttackLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("disease infestation-comment box")
                || subCategory.getText().toString().toLowerCase().contains("disease infestation")) {
                    diseaseInfestationLayout.setVisibility(View.VISIBLE);
                    diseaseInfestationDetails.setText(this.items.get(groupPosition).getDiseaseInfestationComment() != null ? this.items.get(groupPosition).getDiseaseInfestationComment() : "");
                } else {
                    diseaseInfestationLayout.setVisibility(View.GONE);
                }
            } else {
                pestAttackLayout.setVisibility(View.GONE);
                diseaseInfestationLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Germination")) {
                if (subCategory.getText().toString().toLowerCase().contains("germination below std")) {
                    belowStandardLayout.setVisibility(View.VISIBLE);
                    germPercent.setText(String.valueOf(this.items.get(groupPosition).getGrmPercent()));
                } else {
                    belowStandardLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("late germination")) {
                    lateGermLayout.setVisibility(View.VISIBLE);
                    lateGermDetails.setText(this.items.get(groupPosition).getLateGrm() != null ? this.items.get(groupPosition).getLateGrm() : "");
                } else {
                    lateGermLayout.setVisibility(View.GONE);
                }
            } else {
                belowStandardLayout.setVisibility(View.GONE);
                lateGermLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Rain/water damaged seeds")) {
                rainWaterdamageLayout.setVisibility(View.VISIBLE);
                rainWaterDefectPkts.setText(String.valueOf(this.items.get(groupPosition).getRain_NoPktsOrBagDamaged()));
            } else {
                rainWaterdamageLayout.setVisibility(View.GONE);
            }

            if (complaintType.getText().toString().equalsIgnoreCase("Operational Feedback")) {
                operationaldefectPktsLayout.setVisibility(View.VISIBLE);
                operationalDefectPkts.setText(String.valueOf(this.items.get(groupPosition).getNo_PKtsBagDamagedOperationalComplaint()));
            } else {
                operationaldefectPktsLayout.setVisibility(View.GONE);
            }

            if (this.items.get(groupPosition).getRemarksInput() != null && !this.items.get(groupPosition).getRemarksInput().equalsIgnoreCase("")) {
                remarksLayout.setVisibility(View.VISIBLE);
                remarks.setText(this.items.get(groupPosition).getRemarksInput() != null ? this.items.get(groupPosition).getRemarksInput() : "");
            } else {
                remarksLayout.setVisibility(View.GONE);
            }

            if (this.items.get(groupPosition).getComplaintStatus() != null && !this.items.get(groupPosition).getComplaintStatus().equalsIgnoreCase("")) {
                complaintStatusLayout.setVisibility(View.VISIBLE);
                complaintStatus.setText(this.items.get(groupPosition).getComplaintStatus() != null ? this.items.get(groupPosition).getComplaintStatus() : "");
            } else {
                complaintStatusLayout.setVisibility(View.GONE);
            }


            if (this.items.get(groupPosition).getPhoto1UploadedPath() != null && !this.items.get(groupPosition).getPhoto1UploadedPath().equalsIgnoreCase("")) {
                photoFirstLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(this.items.get(groupPosition).getPhoto1UploadedPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoFirstImageView.setImageBitmap(decodedByte);
            } else {
                photoFirstLayout.setVisibility(View.GONE);
            }

            if (this.items.get(groupPosition).getPhoto2UploadedPath() != null && !this.items.get(groupPosition).getPhoto2UploadedPath().equalsIgnoreCase("")) {
                photoSecondLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(this.items.get(groupPosition).getPhoto2UploadedPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoSecondImageView.setImageBitmap(decodedByte);
            } else {
                photoSecondLayout.setVisibility(View.GONE);
            }

            if (this.items.get(groupPosition).getPhoto3UploadedPath() != null && !this.items.get(groupPosition).getPhoto3UploadedPath().equalsIgnoreCase("")) {
                photoThirdLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(this.items.get(groupPosition).getPhoto3UploadedPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoThirdImageView.setImageBitmap(decodedByte);
            } else {
                photoThirdLayout.setVisibility(View.GONE);
            }

            if (this.items.get(groupPosition).getPhoto4UploadedPath() != null && !this.items.get(groupPosition).getPhoto4UploadedPath().equalsIgnoreCase("")) {
                photoFourthLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(this.items.get(groupPosition).getPhoto4UploadedPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoFourthImageView.setImageBitmap(decodedByte);
            } else {
                photoFourthLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Toast.makeText(context, "chldview " + e.getCause(), Toast.LENGTH_SHORT).show();
        }
        return convertViews;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
