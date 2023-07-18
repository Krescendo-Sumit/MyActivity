/*
package com.mahyco.customercomplaint;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFLotNumberModel;
import com.mahyco.customercomplaint.ccfpresenter.CCFLotNumberPresenter;
import com.mahyco.customercomplaint.ccfspinner.CCFSerachSpinner;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Response;

public class CCFTempGPActivity extends CCFBaseActivity implements View.OnClickListener, CCFCommonViewInterface, CCFLotViewInterface {

    private AppCompatImageView mBackImageView;
    private CCFSerachSpinner mStateSpinner;
    private CCFSerachSpinner mMarketDepotSpinner;
    private CCFSerachSpinner mDistrictSpinner;
    private CCFSerachSpinner mTalukaSpinner;
    private AppCompatTextView mCropTextview;
    private AppCompatTextView mHybridOrVarietyTextview;

    private AppCompatTextView mHeadingTitleTextView;

    private AppCompatEditText mAdmixTypeEditText;

    private AppCompatEditText mAdmixPercentEditText;

    private DatePickerDialog mDatePickerDialog = null;

    private AppCompatTextView mSowingDateTextView;
    private AppCompatTextView mComplaintDateTextView;
    private AppCompatTextView mTbmOrRbmDateTextView;

    private AppCompatButton mFirstPhotoBtn;
    private AppCompatButton mSecondPhotoBtn;

    private File mPhotoFile = null;
    private */
/*File*//*
 String mFirstPhotoFile = null;
    private */
/*File*//*
 String mSecondPhotoFile = null;

    private AppCompatImageView mFirstPhotoImageView = null;
    private AppCompatImageView mSecondPhotoImageView = null;

    private AppCompatButton mSubmitButton;

    private AppCompatEditText mFarmerNameEditText;
    private AppCompatEditText mVillageEditText;
    private AppCompatEditText mLotNumberEditText;
    private AppCompatTextView mPackingSizeTextView;
    private AppCompatEditText mMobileNoEditTextView;
    private AppCompatEditText mRemarkEditText;

    private CCFSerachSpinner mCropStageSpinner;

    private final Context mContext = this;

    private CCFTGenericPurityPresenter mGenericPurityPresenter;
    private CCFLotNumberPresenter mLotNumberPresenter;

    private CardView mAdmixPercentCardView;
    private AppCompatButton mDownloadButton;
    private boolean mIsDataDownload = false;

    @Override
    protected int getLayout() {
        return R.layout.ccft_generic_purity_activity;
    }

    @Override
    protected void init() {

        mGenericPurityPresenter = new CCFTGenericPurityPresenter(this, CCFTGenericPurityModel.getInstance());
        mLotNumberPresenter = new CCFLotNumberPresenter(this, CCFLotNumberModel.getInstance());

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.ccf_form_tgp_version_code);
        //mVersionCodeTextView.setText(getString(R.string.ccf_version_code, 1));
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        mHeadingTitleTextView = findViewById(R.id.ccf_tgp_heading_title);

        mAdmixTypeEditText = findViewById(R.id.ccf_tgp_admixture_type_edittext);

        mAdmixPercentCardView = findViewById(R.id.admixture_layout);
        mAdmixPercentEditText = findViewById(R.id.ccf_tgp_admixture_percent_edittext);

        mSowingDateTextView = findViewById(R.id.ccf_tgp_sowing_date);
        mComplaintDateTextView = findViewById(R.id.ccf_tgp_complaint_date);
        mTbmOrRbmDateTextView = findViewById(R.id.ccf_tgp_tbm_rbm_date);

        mSowingDateTextView.setOnClickListener(this);
        mComplaintDateTextView.setOnClickListener(this);

        mTbmOrRbmDateTextView.setText(getCurrentDate());

        mFirstPhotoBtn = findViewById(R.id.ccf_tgp_first_photo_btn);
        mSecondPhotoBtn = findViewById(R.id.ccf_tgp_second_photo_btn);

        mFirstPhotoBtn.setOnClickListener(this);
        mSecondPhotoBtn.setOnClickListener(this);

        mFirstPhotoImageView = findViewById(R.id.ccf_tgp_first_photo_image_view);
        mSecondPhotoImageView = findViewById(R.id.ccf_tgp_second_photo_image_view);

        mSubmitButton = findViewById(R.id.ccf_tgp_submit_btn);
        mSubmitButton.setOnClickListener(this);

        mFarmerNameEditText = findViewById(R.id.ccf_tgp_farmer_name_edittext);
        mVillageEditText = findViewById(R.id.ccf_tgp_village_edittext);
        mLotNumberEditText = findViewById(R.id.ccf_tgp_lot_number_edittext);
        mPackingSizeTextView = findViewById(R.id.ccf_tgp_packing_size_textview);
        mRemarkEditText = findViewById(R.id.ccf_tgp_remarks_other_input_edittext);
        mMobileNoEditTextView = findViewById(R.id.ccf_tgp_mobile_no_edittext);

        mCropStageSpinner = findViewById(R.id.ccf_tgp_crop_stage_spinner);

        mDownloadButton = findViewById(R.id.ccf_tgp_download_data);
        mDownloadButton.setOnClickListener(this);

        if (CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME).equalsIgnoreCase("Admixture")) {
            mAdmixPercentCardView.setVisibility(View.VISIBLE);
        }
        mHeadingTitleTextView.setText(CCFStoreData.getString(mContext, CCFConstantValues.CCF_MAIN_CATEGORY_NAME) + CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME));

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_complaint_type_details));

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

        mStateSpinner = findViewById(R.id.ccf_tgp_state_spinner);
        mStateSpinner.setTitle("Select State");

        mMarketDepotSpinner = findViewById(R.id.ccf_tgp_market_spinner);
        mMarketDepotSpinner.setTitle("Select Market Depot");

        mDistrictSpinner = findViewById(R.id.ccf_tgp_district_spinner);
        mDistrictSpinner.setTitle("Select District");

        mTalukaSpinner = findViewById(R.id.ccf_tgp_taluka_spinner);
        mTalukaSpinner.setTitle("Select Taluka");

        mCropTextview = findViewById(R.id.ccf_tgp_crop_textview);

        mHybridOrVarietyTextview = findViewById(R.id.ccf_tgp_hybrid_variety_textview);

        List<String> contentList = new ArrayList<>();
        contentList.add("Select State");
        contentList.add("Maharashtra");

        ArrayAdapter<String> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, contentList);

        ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
        mStateSpinner.setAdapter(ad);

        mLotNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mIsDataDownload) {
                    resetData();
                    mIsDataDownload = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void resetData() {
        mCropTextview.setText("");
        mHybridOrVarietyTextview.setText("");
        mPackingSizeTextView.setText("");
        mFarmerNameEditText.setText("");
        mMobileNoEditTextView.setText("");
        mStateSpinner.setSelection(0);
        mMarketDepotSpinner.setSelection(0);
        mDistrictSpinner.setSelection(0);
        mTalukaSpinner.setSelection(0);
        mVillageEditText.setText("");
        mSowingDateTextView.setText("");
        mCropStageSpinner.setSelection(0);
        mRemarkEditText.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view == mBackImageView) {
            setEnable(false);
            callComplaintTypeActivity(mContext);
        } else if (view == mDownloadButton) {
            if (!TextUtils.isEmpty(Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim())) {
                mIsDataDownload = true;
                mLotNumberPresenter.callApi(mLotNumberEditText.getText().toString());
            } else {
                showToast(mContext, "Please Enter Lot Number");
            }
        } else if (view == mSowingDateTextView) {
            showDatePickerDialog(true);
        } else if (view == mComplaintDateTextView) {
            showDatePickerDialog(false);
        } else if (view == mFirstPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_FIRST_PHOTO);
        } else if (view == mSecondPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_SECOND_PHOTO);
        } else if (view == mSubmitButton) {
            if (validation()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("farmerName", Objects.requireNonNull(mFarmerNameEditText.getText()).toString().trim());
                jsonObject.addProperty("mobileNo", Objects.requireNonNull(mMobileNoEditTextView.getText()).toString().trim());
                jsonObject.addProperty("state", mStateSpinner.getSelectedItem().toString().trim());
                jsonObject.addProperty("marketDepot", mMarketDepotSpinner.getSelectedItem().toString().trim());
                jsonObject.addProperty("district", mDistrictSpinner.getSelectedItem().toString().trim());
                jsonObject.addProperty("taluka", mTalukaSpinner.getSelectedItem().toString().trim());
                jsonObject.addProperty("village", Objects.requireNonNull(mVillageEditText.getText()).toString().trim());
                jsonObject.addProperty("dateOfComplaint", mComplaintDateTextView.getText().toString().trim());
                jsonObject.addProperty("tbmRbmDate", mTbmOrRbmDateTextView.getText().toString().trim());
                jsonObject.addProperty("crop", mCropTextview.toString().trim());
                jsonObject.addProperty("hybridVariety", mHybridOrVarietyTextview.toString().trim());
                jsonObject.addProperty("lotNumber", Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim());
                jsonObject.addProperty("packingSize", Objects.requireNonNull(mPackingSizeTextView.getText()).toString().trim());
                jsonObject.addProperty("sowingDate", mSowingDateTextView.getText().toString().trim());

                if (mAdmixPercentCardView.getVisibility() == View.VISIBLE) {
                    jsonObject.addProperty("admixtureType", Objects.requireNonNull(mAdmixTypeEditText.getText()).toString().trim());
                    jsonObject.addProperty("admixturePercent", Objects.requireNonNull(mAdmixPercentEditText.getText()).toString().trim());
                }
                jsonObject.addProperty("cropState", */
/*cropState*//*
mCropStageSpinner.getSelectedItem().toString().trim());
                jsonObject.addProperty("photoFirst", mFirstPhotoFile);
                jsonObject.addProperty("photoSecond", mSecondPhotoFile);
                jsonObject.addProperty("remark", Objects.requireNonNull(mRemarkEditText.getText()).toString().trim());
                mGenericPurityPresenter.callApi(jsonObject);
                finish();
            }
        }
    }

    private void showDatePickerDialog(Boolean isSowingDateTextView) {
        hideKeyboard(mContext);
        Calendar mCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
            if (isSowingDateTextView) {
                mSowingDateTextView.setText(dateFormat.format(mCalendar.getTime()));
            } else {
                mComplaintDateTextView.setText(dateFormat.format(mCalendar.getTime()));
            }
        };
        mDatePickerDialog = new DatePickerDialog(mContext, onDateSetListener,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        mDatePickerDialog.show();
    }

    private void setEnable(Boolean setEnable) {
        mBackImageView.setEnabled(setEnable);
        mSubmitButton.setEnabled(setEnable);
        mDownloadButton.setEnabled(setEnable);
    }

    private static final int CAPTURE_IMAGE_REQUEST = 1;

    private String photoType;

    private void capturePhoto(String photoType) {
        this.photoType = photoType;
        hideKeyboard(mContext);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                mPhotoFile = createImageFile(CCFStoreData.getString(mContext, CCFConstantValues.CCF_USER_ID)
                        + "_CCT_GP_");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                //  try {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFile);
                startActivityForResult(takePicture, 1);
               */
/* } catch (Exception ex) {
                    showToast(mContext, " error " + ex.getMessage());
                    Log.e("temporary","cause "+ ex.getCause());
                    ex.printStackTrace();
                }*//*

            } else {
                showToast(mContext, getString(R.string.ccf_went_wrong));
            }
        } else {
            showToast(mContext, getString(R.string.ccf_went_wrong));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            mFirstPhotoImageView.setImageURI(selectedImage);
            Bitmap myBitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
            if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_FIRST_PHOTO)) {
                mFirstPhotoImageView.setVisibility(View.VISIBLE);
                mFirstPhotoFile = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                mFirstPhotoImageView.setImageBitmap(myBitmap);
            } else if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_SECOND_PHOTO)) {
                mSecondPhotoImageView.setVisibility(View.VISIBLE);
                mSecondPhotoFile = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                mSecondPhotoImageView.setImageBitmap(myBitmap);
            }
        } else {
            showToast(mContext, "Request cancelled or something went wrong.");
        }
    }

    private Boolean validation() {
        if (TextUtils.isEmpty(Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Lot Number");
            return false;
        } else if (mCropTextview.getText().toString().trim().equals("")) {
            // showToast(mContext, "Please Select Crop");
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (mHybridOrVarietyTextview.getText().toString().trim().equals("")) {
//            showToast(mContext, "Please Select Hybrid/Variety");
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mPackingSizeTextView.getText()).toString().trim())) {
//            showToast(mContext, "Please Enter Packing Size");
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (*/
/*mHeadingTitleTextView.getText().toString()*//*
CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME).equalsIgnoreCase("Genetic Purity- Admixture")
                && TextUtils.isEmpty(Objects.requireNonNull(mAdmixTypeEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Admixture Type");
            return false;
        } else if (*/
/*mHeadingTitleTextView.getText().toString()*//*
CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME).equalsIgnoreCase("Genetic Purity- Admixture")
                && TextUtils.isEmpty(Objects.requireNonNull(mAdmixPercentEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Admixture Percentage");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mFarmerNameEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Farmer Name");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mMobileNoEditTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Mobile No.");
            return false;
        } else if (mStateSpinner.getSelectedItemPosition() == 0 ||
                mStateSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select State");
            return false;
        } else if (mMarketDepotSpinner.getSelectedItemPosition() == 0 ||
                mMarketDepotSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select Market Depot");
            return false;
        } else if (mDistrictSpinner.getSelectedItemPosition() == 0 ||
                mDistrictSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select District");
            return false;
        } else if (mTalukaSpinner.getSelectedItemPosition() == 0 ||
                mTalukaSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select Taluka");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mVillageEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Village");
            return false;
        } else if (TextUtils.isEmpty(mSowingDateTextView.getText().toString().trim())) {
            showToast(mContext, "Please Select Sowing Date");
            return false;
        } else if (mCropStageSpinner.getSelectedItemPosition() == 0 ||
                mCropStageSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select Crop Stage");
            return false;
        } else {
            if (checkInternetConnection(mContext)) {
                hideKeyboard(mContext);
                setEnable(false);
                return true;
            } else {
                setEnable(true);
                showToast(mContext, getString(R.string.ccf_err_internet));
                return false;
            }
        }
    }

    @Override
    public void showLotDialog() {
        showProgressDialog(this);
    }

    @Override
    public void hideLotDialog() {
        hideProgressDialog();
    }

    @Override
    public void successLotResponse(Response<?> response) {
        if (response.isSuccessful()) {

        }
        Intent intent = new Intent("CustomerComplaintResponse");
        intent.putExtra("IS_FEEDBACK_GIVEN", true);
        intent.setComponent(null);
        mContext.sendBroadcast(intent);

        if (mLotNumberPresenter != null) {
            mLotNumberPresenter.destroy();
        }

        mLotNumberPresenter = null;
        finish();
    }

    @Override
    public void failLotResponse(Throwable t) {

    }

    @Override
    public void showDialog() {
        showProgressDialog(this);
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public void successResponse(Response<?> response) {
        if (response.isSuccessful()) {

        }
        Intent intent = new Intent("CustomerComplaintResponse");
        intent.putExtra("IS_FEEDBACK_GIVEN", true);
        intent.setComponent(null);
        mContext.sendBroadcast(intent);

        if (mGenericPurityPresenter != null) {
            mGenericPurityPresenter.destroy();
        }

        mGenericPurityPresenter = null;

        finish();
    }

    @Override
    public void failResponse(Throwable t) {

    }

    @Override
    public void onBackPressed() {
        if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
            mDatePickerDialog.dismiss();
        }
        callComplaintTypeActivity(mContext);
    }

    @Override
    protected void onDestroy() {
        if (mGenericPurityPresenter != null) {
            mGenericPurityPresenter.destroy();
        }
        hideKeyboard(mContext);
        hideProgressDialog();
        dismissDialog();
        super.onDestroy();
    }

}*/
