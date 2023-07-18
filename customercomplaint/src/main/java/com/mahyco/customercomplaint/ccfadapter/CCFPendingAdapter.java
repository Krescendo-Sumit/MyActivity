/*
package com.mahyco.customercomplaint.ccfadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.mahyco.customercomplaint.CCFPendingRbmCmplnt;
import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfnetwork.CCFPendingCmplntPojoMOdel;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.util.List;

public class CCFPendingAdapter extends BaseExpandableListAdapter {

    private List<CCFPendingCmplntPojoMOdel.Data> items;
    private Context context;
    public */
/*AppCompat*//*
 EditText editText;
    private CCFPendingRbmCmplnt ccfPendingRbmCmplntActivity;

    public CCFPendingAdapter(Context context, List<CCFPendingCmplntPojoMOdel.Data> items,
                             CCFPendingRbmCmplnt ccfPendingRbmCmplntActivity) {
        this.items = items;
        this.context = context;
        this.ccfPendingRbmCmplntActivity = ccfPendingRbmCmplntActivity;
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
            listTitleTextView.setText("Feedback No:- " + items.get(groupPosition).getRegisterComplaintID());
        } catch (Exception e) {
            Toast.makeText(context, "grpview " + e.getCause(), Toast.LENGTH_SHORT).show();
        }
        return convertViews;
    }

    public String comment = "";

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View convertViews = convertView;
        try {
            if (convertViews == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertViews = layoutInflater.inflate(R.layout.ccf_pending_complaint_adapter_layout, null);
            }
            AppCompatTextView complaintNo = convertViews.findViewById(R.id.ccf_complaint_no);
            AppCompatTextView complaintType = convertViews.findViewById(R.id.ccf_pen_cmplnt_type);
            AppCompatTextView mainCatoegory = convertViews.findViewById(R.id.ccf_pen_cmplnt_sub_type);
            AppCompatTextView subCategory = convertViews.findViewById(R.id.ccf_pen_cmplnt_sub_categoty);
            AppCompatTextView lotNo = convertViews.findViewById(R.id.ccf_pen_lot_no);
            AppCompatTextView depot = convertViews.findViewById(R.id.ccf_pen_depot);
            AppCompatTextView businessUnit = convertViews.findViewById(R.id.ccf_pen_no_business_unit);
            AppCompatTextView crop = convertViews.findViewById(R.id.ccf_pen_crop);
            AppCompatTextView hybridVariety = convertViews.findViewById(R.id.ccf_pen_hybrid_variety);
            AppCompatTextView packingSize = convertViews.findViewById(R.id.ccf_pen_packing_size);
            AppCompatTextView complaintDate = convertViews.findViewById(R.id.ccf_pen_cmplnt_date);
            AppCompatTextView farmerName = convertViews.findViewById(R.id.ccf_pen_farmer_name);
            AppCompatTextView mobileNo = convertViews.findViewById(R.id.ccf_pen_mobile_no);
            AppCompatTextView state = convertViews.findViewById(R.id.ccf_pen_state);
            AppCompatTextView district = convertViews.findViewById(R.id.ccf_pen_district);
            AppCompatTextView taluka = convertViews.findViewById(R.id.ccf_pen_taluka);
            AppCompatTextView vilalge = convertViews.findViewById(R.id.ccf_pen_village);
            AppCompatTextView sowingDate = convertViews.findViewById(R.id.ccf_pen_sowing_date);
            AppCompatTextView cropStage = convertViews.findViewById(R.id.ccf_pen_crop_stage);
            AppCompatTextView operationalDefectPkts = convertViews.findViewById(R.id.ccf_pen_oprnl_deftct_pkts);

            AppCompatTextView otherManualDetails = convertViews.findViewById(R.id.ccf_pen_other_details);
            LinearLayout otherManualDetailsLayout = convertViews.findViewById(R.id.ccf_pen_other_details_layout);

            LinearLayout admixtureDescriptionLayout = convertViews.findViewById(R.id.ccf_pen_gp_admixture_desc_layout);
            AppCompatTextView admixtureDesc = convertViews.findViewById(R.id.ccf_pen_gp_admixture_desc);

            LinearLayout admixturePercentLayout = convertViews.findViewById(R.id.ccf_pen_gp_admixture_percent_layout);
            AppCompatTextView admixturePercent = convertViews.findViewById(R.id.ccf_pen_gp_admixture_percent);

            LinearLayout offTypeDescLayout = convertViews.findViewById(R.id.ccf_pen_gp_off_type_desc_layout);
            AppCompatTextView offTypeDesc = convertViews.findViewById(R.id.ccf_pen_gp_off_type_desc);

            LinearLayout offTypePercentLayout = convertViews.findViewById(R.id.ccf_pen_gp_off_type_percent_layout);
            AppCompatTextView offTypePercent = convertViews.findViewById(R.id.ccf_pen_gp_off_type_percent);

            LinearLayout belowStandardLayout = convertViews.findViewById(R.id.ccf_pen_gm_below_std_layout);
            AppCompatTextView germPercent = convertViews.findViewById(R.id.ccf_pen_gm_germ_percent);

            LinearLayout lateGermLayout = convertViews.findViewById(R.id.ccf_pen_gm_late_germ_layout);
            AppCompatTextView lateGermDetails = convertViews.findViewById(R.id.ccf_pen_gm_late_germ_details);

            LinearLayout pestAttackLayout = convertViews.findViewById(R.id.ccf_pen_cp_pest_attack_layout);
            AppCompatTextView pestAttackDetails = convertViews.findViewById(R.id.ccf_pen_cp_pest_attack_details);

            LinearLayout diseaseInfestationLayout = convertViews.findViewById(R.id.ccf_pen_cp_dise_infest_layout);
            AppCompatTextView diseaseInfestationDetails = convertViews.findViewById(R.id.ccf_pen_cp_dise_infest_details);

            LinearLayout rainWaterdamageLayout = convertViews.findViewById(R.id.ccf_pen_rw_defect_pkts_layout);
            AppCompatTextView rainWaterDefectPkts = convertViews.findViewById(R.id.ccf_pen_rw_defect_pkts_details);

            LinearLayout operationaldefectPktsLayout = convertViews.findViewById(R.id.ccf_pen_oprnl_deftct_pkts_layout);

            AppCompatTextView remarks = convertViews.findViewById(R.id.ccf_pen_remarks);
            LinearLayout remarksLayout = convertViews.findViewById(R.id.ccf_pen_remarks_layout);

            LinearLayout photoFirstLayout = convertViews.findViewById(R.id.ccf_pen_photo_first_layout);
            LinearLayout photoSecondLayout = convertViews.findViewById(R.id.ccf_pen_photo_second_layout);
            LinearLayout photoThirdLayout = convertViews.findViewById(R.id.ccf_pen_photo_third_layout);
            LinearLayout photoFourthLayout = convertViews.findViewById(R.id.ccf_pen_photo_fourth_layout);

            ImageView photoFirstImageView = convertViews.findViewById(R.id.ccf_pen_photo_first);
            ImageView photoSecondImageView = convertViews.findViewById(R.id.ccf_pen_photo_second);
            ImageView photoThirdImageView = convertViews.findViewById(R.id.ccf_pen_photo_third);
            ImageView photoFourthImageView = convertViews.findViewById(R.id.ccf_pen_photo_fourth);

            AppCompatTextView tbmCode = convertViews.findViewById(R.id.ccf_pen_cmplt_tbm_code);

            AppCompatTextView tbmContact = convertViews.findViewById(R.id.ccf_pen_cmplt_tbm_contact);
            LinearLayout tbmContactLayout = convertViews.findViewById(R.id.ccf_pen_complaint_tbm_contact);

            AppCompatTextView sowingDays = convertViews.findViewById(R.id.ccf_pen_sowing_days);
            editText = convertViews.findViewById(R.id.ccf_pen_remarks_input_edittext);

            */
/*commented below code on 15/06/2017*//*

            */
/*editText.setFocusableInTouchMode(true);*//*

            */
/*commented below code on 15/06/2017*//*


            */
/*added below code on 15/06/2017*//*

            if (!CCFStoreData.getStringPendingComment(context, "" + groupPosition).equals(""))
                editText.setText(CCFStoreData.getStringPendingComment(context, "" + groupPosition));
            else
                editText.setText("");
            */
/*added code ended here 15/06/2017*//*


            AppCompatButton submit = convertViews.findViewById(R.id.ccf_pen_submit_btn);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    */
/*commented code 14/6/2023 before 2.00pm*//*

                    */
/*if (editText != null && !editText.getText().toString().trim().equalsIgnoreCase("")) {*//*

                    */
/*commented code ended here 14/6/2023 before 2.00pm*//*

                    Log.e("temporary",  "submit onClick comment  "+ comment);
                    */
/*new code added 14/6/2023 2.00pm*//*

                    if (!comment.trim().equalsIgnoreCase("")) {
                        */
/*new code added ended here 14/6/2023 2.00pm*//*


                        */
/*new code added 15/6/2023*//*

                        CCFStoreData.putStringForPendingComment(context, "" + groupPosition, comment);
                        comment = CCFStoreData.getStringPendingComment(context, "" + groupPosition);
                        */
/*new code added 15/6/2023 ended here *//*

                        hideKeyboard();
                        Toast.makeText(context, "position " + groupPosition + comment, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Please Enter Comment " + items.get(groupPosition).getRegisterComplaintID(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            */
/*new code added 14/6/2023 2.00pm*//*

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    */
/*commented below code on 15/06/2017*//*

                    */
/*comment = s.toString().trim();*//*

                    */
/*commented code ended here 15/06/2017*//*


                    */
/*added below code on 15/06/2017*//*

                   // Log.e("temporary",  "before comment  "+ comment);
                    comment = s.toString().trim();
                    Log.e("temporary",  "after comment  "+ comment);
//                    if (CCFPendingRbmCmplnt.lastExpandedPosition != -1 && groupPosition == CCFPendingRbmCmplnt.lastExpandedPosition) {
//                        if (!comment.equalsIgnoreCase("")) {
//                            Log.e("temporary",  "after comment  "+ comment);
//                            CCFStoreData.putStringForPendingComment(context, "" + groupPosition, comment);
//                        }
//                    }
                    */
/*added code on 15/06/2017 ended here*//*

                }
            });
            */
/*new code added ended here 14/6/2023 2.00pm*//*


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
            complaintDate.setText(this.items.get(groupPosition).getDateOfComplaint() != null ? this.items.get(groupPosition).getDateOfComplaint() : "");
            farmerName.setText(this.items.get(groupPosition).getFarmerName() != null ? this.items.get(groupPosition).getFarmerName() : "");
            mobileNo.setText(this.items.get(groupPosition).getFarmerContact() != null ? this.items.get(groupPosition).getFarmerContact() : "");
            state.setText(this.items.get(groupPosition).getState() != null ? this.items.get(groupPosition).getState() : "");
            district.setText(this.items.get(groupPosition).getDistrict() != null ? this.items.get(groupPosition).getDistrict() : "");
            taluka.setText(this.items.get(groupPosition).getTaluka() != null ? this.items.get(groupPosition).getTaluka() : "");
            vilalge.setText(this.items.get(groupPosition).getVillage() != null ? this.items.get(groupPosition).getVillage() : "");
            sowingDate.setText(this.items.get(groupPosition).getSowingDate() != null ? this.items.get(groupPosition).getSowingDate() : "");
            cropStage.setText(this.items.get(groupPosition).getCropStage() != null ? this.items.get(groupPosition).getCropStage() : "");
            tbmCode.setText(this.items.get(groupPosition).getRaisedComplaintTBM() != null ? this.items.get(groupPosition).getRaisedComplaintTBM() : "");
            sowingDays.setText(String.valueOf(this.items.get(groupPosition).getDaysAfterSowing()));

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
                if (subCategory.getText().toString().toLowerCase().contains("pest attack-comment box")) {
                    pestAttackLayout.setVisibility(View.VISIBLE);
                    pestAttackDetails.setText(this.items.get(groupPosition).getPestAttackComment() != null ? this.items.get(groupPosition).getPestAttackComment() : "");
                } else {
                    pestAttackLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("disease infestation-comment box")) {
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

           */
/* if (this.items.get(groupPosition).getContactNoTBM() != null && !this.items.get(groupPosition).getContactNoTBM().equalsIgnoreCase("")) {
                tbmContactLayout.setVisibility(View.VISIBLE);
                tbmContact.setText(this.items.get(groupPosition).getContactNoTBM() != null ? this.items.get(groupPosition).getContactNoTBM() : "");
            } else {
                tbmContactLayout.setVisibility(View.GONE);
            }*//*



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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = ccfPendingRbmCmplntActivity.getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
*/
