package com.mahyco.customercomplaint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFSbmtCmplntModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFBaseApiResponse;
import com.mahyco.customercomplaint.ccfpresenter.CCFSbmtPenCmplntPresenter;
import com.mahyco.customercomplaint.ccfspinner.CCFSerachSpinner;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;
import com.mahyco.zoomimagelib.LibShowZoomDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import retrofit2.Response;

public class CCFSubmitPendingCmplnt extends CCFBaseActivity implements CCFCommonViewInterface, View.OnClickListener {

    private Context mContext;
    private AppCompatImageView mBackImageView;
    private CCFSbmtPenCmplntPresenter mSbmtCmplntPresenter;
    private AppCompatButton mForwardButton;
    private AppCompatButton mRejectButton;
    private Dialog mDialog = null;
    private CCFSerachSpinner mFeedbackTypeSpinner;

    @Override
    protected int getLayout() {
        return R.layout.ccf_submit_pending_complaint;
    }

    @Override
    protected void init() {
        try {
            mContext = this;

            mSbmtCmplntPresenter = new CCFSbmtPenCmplntPresenter(this, CCFSbmtCmplntModel.getInstance());

            AppCompatTextView mVersionCodeTextView = findViewById(R.id.ccf_rbm_submit_pending_version_code);
            mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

            AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
            mTitleTextView.setText(getString(R.string.ccf_submit_complaint));

            mBackImageView = findViewById(R.id.ccf_back);
            mBackImageView.setOnClickListener(this);

            mFeedbackTypeSpinner = findViewById(R.id.ccf_pen_feedback_type_spinner);

            AppCompatTextView complaintNo = findViewById(R.id.ccf_pen_submit_complaint_no);
            AppCompatTextView complaintType = findViewById(R.id.ccf_pen_submit_cmplnt_type);
            AppCompatTextView mainCatoegory = findViewById(R.id.ccf_pen_submit_cmplnt_sub_type);
            AppCompatTextView subCategory = findViewById(R.id.ccf_pen_submit_cmplnt_sub_categoty);
            AppCompatTextView lotNo = findViewById(R.id.ccf_pen_submit_lot_no);
            AppCompatTextView depot = findViewById(R.id.ccf_pen_submit_depot);
            AppCompatTextView businessUnit = findViewById(R.id.ccf_pen_submit_no_business_unit);
            AppCompatTextView crop = findViewById(R.id.ccf_pen_submit_crop);
            AppCompatTextView hybridVariety = findViewById(R.id.ccf_pen_submit_hybrid_variety);
            AppCompatTextView packingSize = findViewById(R.id.ccf_pen_submit_packing_size);
            AppCompatTextView complaintDate = findViewById(R.id.ccf_pen_submit_cmplnt_date);
            AppCompatTextView farmerName = findViewById(R.id.ccf_pen_submit_farmer_name);
            AppCompatTextView mobileNo = findViewById(R.id.ccf_pen_submit_mobile_no);
            AppCompatTextView state = findViewById(R.id.ccf_pen_submit_state);
            AppCompatTextView district = findViewById(R.id.ccf_pen_submit_district);
            AppCompatTextView taluka = findViewById(R.id.ccf_pen_submit_taluka);
            AppCompatTextView vilalge = findViewById(R.id.ccf_pen_submit_village);
            AppCompatTextView sowingDate = findViewById(R.id.ccf_pen_submit_sowing_date);
            AppCompatTextView cropStage = findViewById(R.id.ccf_pen_submit_crop_stage);
            AppCompatTextView operationalDefectPkts = findViewById(R.id.ccf_pen_submit_oprnl_deftct_pkts);

            AppCompatTextView otherManualDetails = findViewById(R.id.ccf_pen_submit_other_details);
            LinearLayout otherManualDetailsLayout = findViewById(R.id.ccf_pen_submit_other_details_layout);

            LinearLayout admixtureDescriptionLayout = findViewById(R.id.ccf_pen_submit_gp_admixture_desc_layout);
            AppCompatTextView admixtureDesc = findViewById(R.id.ccf_pen_submit_gp_admixture_desc);

            LinearLayout admixturePercentLayout = findViewById(R.id.ccf_pen_submit_gp_admixture_percent_layout);
            AppCompatTextView admixturePercent = findViewById(R.id.ccf_pen_submit_gp_admixture_percent);

            LinearLayout offTypeDescLayout = findViewById(R.id.ccf_pen_submit_gp_off_type_desc_layout);
            AppCompatTextView offTypeDesc = findViewById(R.id.ccf_pen_submit_gp_off_type_desc);

            LinearLayout offTypePercentLayout = findViewById(R.id.ccf_pen_submit_gp_off_type_percent_layout);
            AppCompatTextView offTypePercent = findViewById(R.id.ccf_pen_submit_gp_off_type_percent);

            LinearLayout belowStandardLayout = findViewById(R.id.ccf_pen_submit_gm_below_std_layout);
            AppCompatTextView germPercent = findViewById(R.id.ccf_pen_submit_gm_germ_percent);

            LinearLayout lateGermLayout = findViewById(R.id.ccf_pen_submit_gm_late_germ_layout);
            AppCompatTextView lateGermDetails = findViewById(R.id.ccf_pen_submit_gm_late_germ_details);

            LinearLayout pestAttackLayout = findViewById(R.id.ccf_pen_submit_cp_pest_attack_layout);
            AppCompatTextView pestAttackDetails = findViewById(R.id.ccf_pen_submit_cp_pest_attack_details);

            LinearLayout diseaseInfestationLayout = findViewById(R.id.ccf_pen_submit_cp_dise_infest_layout);
            AppCompatTextView diseaseInfestationDetails = findViewById(R.id.ccf_pen_submit_cp_dise_infest_details);

            LinearLayout rainWaterdamageLayout = findViewById(R.id.ccf_pen_submit_rw_defect_pkts_layout);
            AppCompatTextView rainWaterDefectPkts = findViewById(R.id.ccf_pen_submit_rw_defect_pkts_details);

            LinearLayout operationaldefectPktsLayout = findViewById(R.id.ccf_pen_submit_oprnl_deftct_pkts_layout);

            AppCompatTextView remarks = findViewById(R.id.ccf_pen_submit_remarks);
            LinearLayout remarksLayout = findViewById(R.id.ccf_pen_submit_remarks_layout);

            LinearLayout photoFirstLayout = findViewById(R.id.ccf_pen_submit_photo_first_layout);
            LinearLayout photoSecondLayout = findViewById(R.id.ccf_pen_submit_photo_second_layout);
            LinearLayout photoThirdLayout = findViewById(R.id.ccf_pen_submit_photo_third_layout);
            LinearLayout photoFourthLayout = findViewById(R.id.ccf_pen_submit_photo_fourth_layout);

            ImageView photoFirstImageView = findViewById(R.id.ccf_pen_submit_photo_first);
            ImageView photoSecondImageView = findViewById(R.id.ccf_pen_submit_photo_second);
            ImageView photoThirdImageView = findViewById(R.id.ccf_pen_submit_photo_third);
            ImageView photoFourthImageView = findViewById(R.id.ccf_pen_submit_photo_fourth);

            AppCompatTextView tbmCode = findViewById(R.id.ccf_pen_submit_cmplt_tbm_code);

            AppCompatTextView tbmContact = findViewById(R.id.ccf_pen_submit_cmplt_tbm_contact);
            LinearLayout tbmContactLayout = findViewById(R.id.ccf_pen_submit_complaint_tbm_contact);

            AppCompatTextView sowingDays = findViewById(R.id.ccf_pen_submit_sowing_days);
            AppCompatEditText editText = findViewById(R.id.ccf_pen_submit_remarks_input_edittext);
            /*added on 7/9/2023*/
            photoFirstLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoFirstImageView.getDrawable() != null) {
                        LibShowZoomDialog.showZoomDialog(((BitmapDrawable) photoFirstImageView.getDrawable()).getBitmap(), mContext);
                    }
                }
            });

            photoSecondLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoSecondImageView.getDrawable() != null) {
                        LibShowZoomDialog.showZoomDialog(((BitmapDrawable) photoSecondImageView.getDrawable()).getBitmap(), mContext);
                    }
                }
            });

            photoThirdLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoThirdImageView.getDrawable() != null) {
                        LibShowZoomDialog.showZoomDialog(((BitmapDrawable) photoThirdImageView.getDrawable()).getBitmap(), mContext);
                    }
                }
            });

            photoFourthLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoFourthImageView.getDrawable() != null) {
                        LibShowZoomDialog.showZoomDialog(((BitmapDrawable) photoFourthImageView.getDrawable()).getBitmap(), mContext);
                    }
                }
            });
            /*added ended here on 7/9/2023*/
            mForwardButton = findViewById(R.id.ccf_pen_forward_btn);
            mForwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (editText != null && !editText.getText().toString().trim().equalsIgnoreCase("")
                                && mFeedbackTypeSpinner.getSelectedItemPosition() != 0 && mFeedbackTypeSpinner.getSelectedItemPosition() != -1) {
                            mForwardButton.setEnabled(false);
                            mRejectButton.setEnabled(false);
                            hideKeyboard(mContext);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("RbmCode", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_RBM_CODE));
                            jsonObject.addProperty("Releated", mainCatoegory.getText().toString().trim() + "|" + subCategory.getText().toString().trim());
                            jsonObject.addProperty("Comment", editText.getText().toString().trim());
                            jsonObject.addProperty("Severity", Objects.requireNonNull(mFeedbackTypeSpinner.getSelectedItem().toString().trim()));
                            jsonObject.addProperty("RCID", Integer.parseInt(complaintNo.getText().toString().trim()));
                            jsonObject.addProperty("Action", "forward");
                            mSbmtCmplntPresenter.callApi(jsonObject, mContext);
                        } else {
                            if (mFeedbackTypeSpinner.getSelectedItemPosition() == 0 ||
                                    mFeedbackTypeSpinner.getSelectedItemPosition() == -1) {
                                Toast.makeText(mContext, "Please Select Severity", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Please Enter Comment", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "forward click " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mRejectButton = findViewById(R.id.ccf_pen_reject_btn);
            mRejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (editText != null && !editText.getText().toString().trim().equalsIgnoreCase("")) {
                            mRejectButton.setEnabled(false);
                            mForwardButton.setEnabled(false);
                            mDialog = null;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Customer Feedback");
                            alertDialog.setMessage("Are you sure you want to reject this feedback.");
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mRejectButton.setEnabled(true);
                                    mForwardButton.setEnabled(true);
                                    dismissLocalDialog();
                                    hideKeyboard(mContext);
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("RbmCode", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_RBM_CODE));
                                    jsonObject.addProperty("Releated", mainCatoegory.getText().toString().trim() + "|" + subCategory.getText().toString().trim());
                                    jsonObject.addProperty("Comment", editText.getText().toString().trim());
                                    jsonObject.addProperty("Severity", "");
                                    jsonObject.addProperty("RCID", Integer.parseInt(complaintNo.getText().toString().trim()));
                                    jsonObject.addProperty("Action", "reject");
                                    mSbmtCmplntPresenter.callApi(jsonObject, mContext);
                                }
                            });
                            mDialog = alertDialog.create();
                            mDialog.show();
                        } else {
                           /* if (mFeedbackTypeSpinner.getSelectedItemPosition() == 0 ||
                                    mFeedbackTypeSpinner.getSelectedItemPosition() == -1) {
                                Toast.makeText(mContext, "Please Select Severity", Toast.LENGTH_SHORT).show();
                            } else {*/
                                Toast.makeText(mContext, "Please Enter Comment", Toast.LENGTH_SHORT).show();
                            /*}*/
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Reject click " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            complaintNo.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_ID));
            complaintType.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TYPE));
            mainCatoegory.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SUB_TYPE));
            subCategory.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SUB_CATEGORY_DESC));
            lotNo.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_LOT_NUMBER));
            depot.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DEPOT));
            businessUnit.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_BU));
            crop.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_CROP_DTL));
            hybridVariety.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_HYBRID_VARIETY));
            packingSize.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PACK_SIZE));
            complaintDate.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DATE_COMPLAINT));
            farmerName.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_FARMER_NAME));
            mobileNo.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_FARMER_CONTACT));
            state.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_STATE));
            district.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DIST));
            taluka.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TALUKA));
            vilalge.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_VILLAGE));
            sowingDate.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SOWING_DATE));
            cropStage.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_CROP_STAGE));
            tbmCode.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TBM_CODE));
            sowingDays.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SOWING_DAYS));

            if (!complaintType.getText().toString().equalsIgnoreCase("Operational Feedback") && subCategory.getText().toString().toLowerCase().contains("Others".toLowerCase())) {
                otherManualDetailsLayout.setVisibility(View.VISIBLE);
                otherManualDetails.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_OTH_MANUAL_TY_BOX));
            } else {
                otherManualDetailsLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Genetic Purity")) {
                if (subCategory.getText().toString().toLowerCase().contains("admixture")) {
                    admixturePercentLayout.setVisibility(View.VISIBLE);
                    admixtureDescriptionLayout.setVisibility(View.VISIBLE);
                    admixtureDesc.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_ADMIXTURE_TYPE));
                    admixturePercent.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_ADMIXTURE_PERCENT));
                } else {
                    admixtureDescriptionLayout.setVisibility(View.GONE);
                    admixturePercentLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("off types")) {
                    offTypeDescLayout.setVisibility(View.VISIBLE);
                    offTypePercentLayout.setVisibility(View.VISIBLE);
                    offTypeDesc.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_OFF_TYPE));
                    offTypePercent.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_OFF_PERCENT));
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
                    pestAttackDetails.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PEST_COMMENT));
                } else {
                    pestAttackLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("disease infestation-comment box")
                        || subCategory.getText().toString().toLowerCase().contains("disease infestation")) {
                    diseaseInfestationLayout.setVisibility(View.VISIBLE);
                    diseaseInfestationDetails.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DISEASE_COMMENT));
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
                    germPercent.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GERM_PERCENT));
                } else {
                    belowStandardLayout.setVisibility(View.GONE);
                }
                if (subCategory.getText().toString().toLowerCase().contains("late germination")) {
                    lateGermLayout.setVisibility(View.VISIBLE);
                    lateGermDetails.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_LATE_GERM));
                } else {
                    lateGermLayout.setVisibility(View.GONE);
                }
            } else {
                belowStandardLayout.setVisibility(View.GONE);
                lateGermLayout.setVisibility(View.GONE);
            }

            if (mainCatoegory.getText().toString().equalsIgnoreCase("Rain/water damaged seeds")) {
                rainWaterdamageLayout.setVisibility(View.VISIBLE);
                rainWaterDefectPkts.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_RAIN_NO_PKTS_DAMAGED));
            } else {
                rainWaterdamageLayout.setVisibility(View.GONE);
            }

            if (complaintType.getText().toString().equalsIgnoreCase("Operational Feedback")) {
                operationaldefectPktsLayout.setVisibility(View.VISIBLE);
                operationalDefectPkts.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_OPERATIONAL_NO_PKTS_DAMAGED));
            } else {
                operationaldefectPktsLayout.setVisibility(View.GONE);
            }

            if (!CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_REMARKS).equalsIgnoreCase("")) {
                remarksLayout.setVisibility(View.VISIBLE);
                remarks.setText(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_REMARKS));
            } else {
                remarksLayout.setVisibility(View.GONE);
            }

            /*if (!CCFStoreData.getStringSubmitComment(mContext,CCFConstantValues.CCF_SUBMIT_COMPLAINT_TBM_CONTACT).equalsIgnoreCase("")) {
                tbmContactLayout.setVisibility(View.VISIBLE);
                tbmContact.setText(CCFStoreData.getStringSubmitComment(mContext,CCFConstantValues.CCF_SUBMIT_COMPLAINT_TBM_CONTACT));
            } else {
                tbmContactLayout.setVisibility(View.GONE);
            }*/


            if (!CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH1).equalsIgnoreCase("")) {
                photoFirstLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH1), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoFirstImageView.setImageBitmap(decodedByte);
            } else {
                photoFirstLayout.setVisibility(View.GONE);
            }

            if (!CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH2).equalsIgnoreCase("")) {
                photoSecondLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH2), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoSecondImageView.setImageBitmap(decodedByte);
            } else {
                photoSecondLayout.setVisibility(View.GONE);
            }

            if (!CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH3).equalsIgnoreCase("")) {
                photoThirdLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH3), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoThirdImageView.setImageBitmap(decodedByte);
            } else {
                photoThirdLayout.setVisibility(View.GONE);
            }

            if (!CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH4).equalsIgnoreCase("")) {
                photoFourthLayout.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(CCFStoreData.getStringSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH4), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoFourthImageView.setImageBitmap(decodedByte);
            } else {
                photoFourthLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Toast.makeText(mContext, "chldview " + e.getCause(), Toast.LENGTH_SHORT).show();
        }

    }

    private void dismissLocalDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBackImageView) {
            finishActivity();
        }
    }

    @Override
    public void showDialog() {
        showProgressDialog(this);
    }

    @Override
    public void hideDialog() {
        mForwardButton.setEnabled(true);
        mRejectButton.setEnabled(true);
        hideProgressDialog();
    }

    private void refreshPreviousActivity() {
        try {
            if (CCFNewRbmPendingCmplnt.cntxofParent != null) {
                AppCompatActivity activity = (AppCompatActivity) CCFNewRbmPendingCmplnt.cntxofParent;
                if (!activity.isDestroyed()) {
                    activity.finish();
                }
            }
            Intent intent = new Intent(mContext, CCFFirstActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            showToast(mContext, "PreviousActivity " + e.getMessage());
        }
    }

    @Override
    public void successResponse(Response<?> response) {
        mForwardButton.setEnabled(true);
        mRejectButton.setEnabled(true);
        if (response.isSuccessful()) {
            CCFBaseApiResponse baseApiResponse = (CCFBaseApiResponse) response.body();
            if (baseApiResponse != null) {
                if (baseApiResponse.getSuccess()) {
                    mDialog = null;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Customer Feedback");
                    alertDialog.setMessage(baseApiResponse.getMessage());
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismissLocalDialog();
                            ;
                            refreshPreviousActivity();
                        }
                    });
                    mDialog = alertDialog.create();
                    mDialog.show();
                } else {
                    showInternetDialog(mContext, baseApiResponse.getMessage());
                }
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
        } else {
            if (response.code() == 401) {
                showInternetDialog(mContext, getString(R.string.ccf_session_expired));
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_server_error));
            }
        }
    }

    @Override
    public void failResponse(Throwable t) {
        mForwardButton.setEnabled(true);
        mRejectButton.setEnabled(true);
        if (t instanceof SocketTimeoutException) {
            showInternetDialog(mContext, "Request Timeout!!!");
        } else if (t instanceof UnknownHostException) {
            showInternetDialog(mContext, getString(R.string.ccf_err_internet));
        } else {
            showInternetDialog(mContext, getString(R.string.ccf_err_common));
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mSbmtCmplntPresenter != null) {
            mSbmtCmplntPresenter.destroy();
        }

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        /*added on 7/9/2023*/
        LibShowZoomDialog.hideZoomDialog();
        /*added on 7/9/2023 ended here*/
        hideKeyboard(mContext);
        hideProgressDialog();
        dismissDialog();
        super.onDestroy();
    }
}
