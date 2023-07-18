package com.mahyco.customercomplaint.ccfactivities.ccftechnical;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.CCFApplicationUtil;
import com.mahyco.customercomplaint.CCFConstantValues;
import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfinterfaces.CCFAllListListener;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFLotNumberModel;
import com.mahyco.customercomplaint.ccfmodel.CCFRegstComplntModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFApiClass;
import com.mahyco.customercomplaint.ccfnetwork.CCFBaseApiResponse;
import com.mahyco.customercomplaint.ccfnetwork.CCFDepotListData;
import com.mahyco.customercomplaint.ccfnetwork.CCFDepotPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFDistrictListData;
import com.mahyco.customercomplaint.ccfnetwork.CCFDistrictPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFLotPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFStateListData;
import com.mahyco.customercomplaint.ccfnetwork.CCFStatePojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFTalukaListData;
import com.mahyco.customercomplaint.ccfnetwork.CCFTalukaPojoModel;
import com.mahyco.customercomplaint.ccfpresenter.CCFLotNumberPresenter;
import com.mahyco.customercomplaint.ccfpresenter.CCFRegstComplntPresenter;
import com.mahyco.customercomplaint.ccfspinner.CCFSerachSpinner;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.GalleryCameraPickImageDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Response;


public class CCFTMtrlQuality extends CCFBaseActivity implements View.OnClickListener, CCFCommonViewInterface, CCFLotViewInterface,
        CCFAllListListener {

    private final Context mContext = this;

    private CCFSerachSpinner mStateSpinner;
    private CCFSerachSpinner mMarketDepotSpinner;
    private CCFSerachSpinner mDistrictSpinner;
    private CCFSerachSpinner mTalukaSpinner;

    private AppCompatTextView mCropTextView;
    private AppCompatTextView mHybridOrVarietyTextView;
    private AppCompatTextView mPackingSizeTextView;
    private AppCompatTextView mHeadingTitleTextView;
    private AppCompatTextView mComplaintDateTextView;
    private AppCompatTextView mTbmOrRbmDateTextView;

    private File mPhotoFile = null;
    private String mFirstPhotoBase64String = "";
    private String mSecondPhotoBase64String = "";

    private AppCompatImageView mFirstPhotoImageView = null;
    private AppCompatImageView mSecondPhotoImageView = null;
    private AppCompatImageView mBackImageView;

    private AppCompatEditText mFarmerNameEditText;
    private AppCompatEditText mVillageEditText;
    private AppCompatEditText mLotNumberEditText;
    private AppCompatEditText mRemarkEditText;
    private AppCompatEditText mMobileNoEditTextView;

    private AppCompatButton mFirstPhotoBtn;
    private AppCompatButton mSecondPhotoBtn;
    private AppCompatButton mSubmitButton;
    private AppCompatButton mDownloadButton;

    private DatePickerDialog mDatePickerDialog = null;

    private CCFRegstComplntPresenter mRegstComplntPresenter;
    private CCFLotNumberPresenter mLotNumberPresenter;

    private boolean mIsDataDownload = false;

    private AppCompatTextView mLotQtyTextView;
    private AppCompatTextView mNoOfPktsTextView;
    private CCFSerachSpinner mCropStageSpinner;
    private AppCompatTextView mBuTextView;

    private String mThirdPhotoBase64String = "";
    private String mFourthPhotoBase64String = "";

    private AppCompatImageView mThirdPhotoImageView = null;
    private AppCompatImageView mFourthPhotoImageView = null;
    private AppCompatButton mThirdPhotoBtn;
    private AppCompatButton mFourthPhotoBtn;
    private AppCompatTextView mLabelForOther;
    private AppCompatTextView mSowingDateTextView;

    private String mFirstPhotoPath = "";
    private String mSecondPhotoPath = "";
    private String mThirdPhotoPath = "";
    private String mFourthPhotoPath = "";

    private String mFirstPhotoPathUsingDialog = "";
    private String mSecondPhotoPathUsingDialog = "";
    private String mThirdPhotoPathUsingDialog = "";
    private String mFourthPhotoPathUsingDialog = "";

    private CardView mOtherDetailsCardView;
    private AppCompatEditText mOthersEditTextView;

    private AppCompatTextView mTotalLotQty;
    private AppCompatTextView mTotalNoOfPkts;

    @Override
    protected int getLayout() {
        return R.layout.ccft_pking_mtrl_qlty_activity;
    }

    @Override
    protected void init() {

        mRegstComplntPresenter = new CCFRegstComplntPresenter(this, CCFRegstComplntModel.getInstance());
        mLotNumberPresenter = new CCFLotNumberPresenter(this, CCFLotNumberModel.getInstance());

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.ccf_form_tpkmq_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        mHeadingTitleTextView = findViewById(R.id.ccf_tpkmq_heading_title);

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_complaint_type_details));

        mFarmerNameEditText = findViewById(R.id.ccf_tpkmq_farmer_name_edittext);
        mVillageEditText = findViewById(R.id.ccf_tpkmq_village_edittext);
        mLotNumberEditText = findViewById(R.id.ccf_tpkmq_lot_number_edittext);
        mRemarkEditText = findViewById(R.id.ccf_tpkmq_remarks_other_input_edittext);

        mHeadingTitleTextView.setText(CCFStoreData.getString(mContext, CCFConstantValues.CCF_MAIN_CATEGORY_NAME) + CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME));

        mComplaintDateTextView = findViewById(R.id.ccf_tpkmq_complaint_date);
        mTbmOrRbmDateTextView = findViewById(R.id.ccf_tpkmq_tbm_rbm_date);

        mFirstPhotoImageView = findViewById(R.id.ccf_tpkmq_first_photo_image_view);
        mSecondPhotoImageView = findViewById(R.id.ccf_tpkmq_second_photo_image_view);

        mComplaintDateTextView.setOnClickListener(this);
        mTbmOrRbmDateTextView.setText(getCurrentDate());

        mFirstPhotoBtn = findViewById(R.id.ccf_tpkmq_first_photo_btn);
        mSecondPhotoBtn = findViewById(R.id.ccf_tpkmq_second_photo_btn);

        mFirstPhotoBtn.setOnClickListener(this);
        mSecondPhotoBtn.setOnClickListener(this);

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

        mSubmitButton = findViewById(R.id.ccf_tpkmq_submit_btn);
        mSubmitButton.setOnClickListener(this);

        mStateSpinner = findViewById(R.id.ccf_tpkmq_state_spinner);
        mMarketDepotSpinner = findViewById(R.id.ccf_tpkmq_market_spinner);
        mDistrictSpinner = findViewById(R.id.ccf_tpkmq_district_spinner);
        mTalukaSpinner = findViewById(R.id.ccf_tpkmq_taluka_spinner);

        mCropTextView = findViewById(R.id.ccf_tpkmq_crop_textview);
        mHybridOrVarietyTextView = findViewById(R.id.ccf_tpkmq_hybrid_variety_textview);
        mPackingSizeTextView = findViewById(R.id.ccf_tpkmq_packing_size_textview);


        mThirdPhotoBtn = findViewById(R.id.ccf_tpkmq_third_photo_btn);
        mFourthPhotoBtn = findViewById(R.id.ccf_tpkmq_fourth_photo_btn);
        mLotQtyTextView = findViewById(R.id.ccf_tpkmq_lot_quantity_textview);
        mNoOfPktsTextView = findViewById(R.id.ccf_tpkmq_no_pkts_textview);

        mCropStageSpinner = findViewById(R.id.ccf_tpkmq_crop_stage_spinner);
        mThirdPhotoImageView = findViewById(R.id.ccf_tpkmq_third_photo_image_view);
        mFourthPhotoImageView = findViewById(R.id.ccf_tpkmq_fourth_photo_image_view);
        mBuTextView = findViewById(R.id.ccf_tpkmq_bu_textview);
        mLabelForOther = findViewById(R.id.ccf_tpkmq_label_for_other);

        mThirdPhotoBtn.setOnClickListener(this);
        mFourthPhotoBtn.setOnClickListener(this);

        mSowingDateTextView = findViewById(R.id.ccf_tpkmq_sowing_date_textview);
        mSowingDateTextView.setOnClickListener(this);

        mOtherDetailsCardView = findViewById(R.id.ccf_tpkmq_other_edittext_layout);
        mOthersEditTextView = findViewById(R.id.ccf_tpkmq_other_edittext);

        mTotalLotQty = findViewById(R.id.ccf_tpkmq_TotalLotQty);
        mTotalNoOfPkts = findViewById(R.id.ccf_tpkmq_TotalNoOfPkts);

        String[] categoryArray = CCFStoreData.getString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME).split("/");
        for (String s : categoryArray) {
            if (s.equalsIgnoreCase("Others")) {
                mOtherDetailsCardView.setVisibility(View.VISIBLE);
            }
        }

        setStateAdapter();
        setDistrictAdapter();
        setTalukaAdapter(false);
        setDepotAdapter();
        //  mMarketDepotSpinner.setTitle("Select Market Depot");

        mDownloadButton = findViewById(R.id.ccf_tpkmq_download_data);
        mDownloadButton.setOnClickListener(this);

        mMobileNoEditTextView = findViewById(R.id.ccf_tpkmq_mobile_no_edittext);

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

        callStateApi();
    }

    private void resetData() {
        mFarmerNameEditText.setText("");
        mVillageEditText.setText("");
        mTalukaSpinner.setSelection(0);
        mDistrictSpinner.setSelection(0);
        mStateSpinner.setSelection(0);
      //  mMarketDepotSpinner.setSelection(0);
        setDepotAdapter();
        mCropTextView.setText("");
        mHybridOrVarietyTextView.setText("");
        mPackingSizeTextView.setText("");
        mMobileNoEditTextView.setText("");
        mRemarkEditText.setText("");
        mLotQtyTextView.setText("");
        mNoOfPktsTextView.setText("");
        mBuTextView.setText("");
        mTotalLotQty.setText("");
        mTotalNoOfPkts.setText("");
        if (mOtherDetailsCardView.getVisibility() == View.VISIBLE) {
            mOthersEditTextView.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBackImageView) {
            setEnable(false);
            callComplaintTypeActivity(mContext);
        } else if (view == mDownloadButton) {
            if (checkInternetConnection(mContext)) {
                if (!TextUtils.isEmpty(Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim())) {
                    mIsDataDownload = true;
                    hideKeyboard(mContext);
                    setEnable(false);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("CHARG_LotBatch", mLotNumberEditText.getText().toString());
                    CCFApiClass.getDepot(this, this, jsonObject);
//                    mLotNumberPresenter.callApi(mLotNumberEditText.getText().toString());
                } else {
                    showToast(mContext, "Please Enter Lot Number");
                }
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_err_internet));
            }
        } else if (view == mComplaintDateTextView) {
            showDatePickerDialog(false);
        } else if (view == mFirstPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_FIRST_PHOTO);
        } else if (view == mSecondPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_SECOND_PHOTO);
        } else if (view == mThirdPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_THIRD_PHOTO);
        } else if (view == mFourthPhotoBtn) {
            capturePhoto(CCFConstantValues.CCF_COMPLAINT_FOURTH_PHOTO);
        } else if (view == mSowingDateTextView) {
            showDatePickerDialog(true);
        } else if (view == mSubmitButton) {
            if (validation()) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    JsonArray array = new JsonArray();

                    JsonObject object = new JsonObject();
                    object.addProperty("MTID", Integer.parseInt(CCFStoreData.getString(mContext, CCFConstantValues.CCF_MTID)));
                    object.addProperty("CTID", Integer.parseInt(CCFStoreData.getString(mContext, CCFConstantValues.CCF_CTID)));
                    object.addProperty("SCTID", CCFStoreData.getString(mContext, CCFConstantValues.CCF_SCTID));
                    object.addProperty("LotNumber", Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim());
                    object.addProperty("BusinessUnit", Objects.requireNonNull(mBuTextView.getText()).toString().trim());
                    object.addProperty("CropDtl", Objects.requireNonNull(mCropTextView.getText()).toString().trim());
                    object.addProperty("Hybrid_Variety", Objects.requireNonNull(mHybridOrVarietyTextView.getText()).toString().trim());
                    object.addProperty("PackingSize", Float.parseFloat(Objects.requireNonNull(mPackingSizeTextView.getText()).toString().trim()));
                    object.addProperty("LotQty",/* Float.parseFloat(*/Objects.requireNonNull(mLotQtyTextView.getText()).toString().trim()/*)*/);
                    object.addProperty("NoOfPkts", Integer.parseInt(Objects.requireNonNull(mNoOfPktsTextView.getText()).toString().trim()));
                    object.addProperty("FarmerName", Objects.requireNonNull(mFarmerNameEditText.getText()).toString().trim());
                    object.addProperty("FarmerContact", Objects.requireNonNull(mMobileNoEditTextView.getText()).toString().trim());
                    object.addProperty("Depot", Objects.requireNonNull(mMarketDepotSpinner.getSelectedItem().toString().trim()));
                    object.addProperty("State", Objects.requireNonNull(mStateSpinner.getSelectedItem().toString().trim()));
                    object.addProperty("District", Objects.requireNonNull(mDistrictSpinner.getSelectedItem().toString().trim()));
                    object.addProperty("Taluka", Objects.requireNonNull(mTalukaSpinner.getSelectedItem().toString().trim()));
                    object.addProperty("Village", Objects.requireNonNull(mVillageEditText.getText()).toString().trim());
                    object.addProperty("SowingDate", Objects.requireNonNull(mSowingDateTextView.getText().toString().trim()));
                    object.addProperty("CropStage", Objects.requireNonNull(mCropStageSpinner.getSelectedItem().toString().trim()));
                    object.addProperty("RemarksInput", Objects.requireNonNull(mRemarkEditText.getText()).toString().trim());
                    object.addProperty("DateOfComplaint", getCurrentDate());
                    object.addProperty("Photo1UploadedPath", mFirstPhotoBase64String);
                    object.addProperty("Photo2UploadedPath", mSecondPhotoBase64String);
                    object.addProperty("Photo3UploadedPath", mThirdPhotoBase64String);
                    object.addProperty("Photo4UploadedPath", mFourthPhotoBase64String);
                    object.addProperty("PhotoUploadedBy", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE));
                    object.addProperty("TBMSubmissionDate", getCurrentDate());
                    object.addProperty("ContactNoTBM", CCFStoreData.getString(mContext, CCFConstantValues.CCF_CONTACT_NO_TBM));
                    object.addProperty("RaisedComplaintTBM", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE));

                    /*Extra fields in Genetic Purity Complaint*/
                    object.addProperty("Gp_AdmixtureType", "");
                    object.addProperty("Gp_AdmixturePercent", 0);
                    object.addProperty("Gp_OffType", "");
                    object.addProperty("Gp_OffTypePercent", 0);

                    /*Extra fields in Germination Complaint*/
//                    object.addProperty("GrmBelowStd", "");
                    object.addProperty("GrmPercent", 0);
                    object.addProperty("LateGrm", "");

                    /*Extra field in Operational Complaint*/
                    object.addProperty("No_PKtsBagDamagedOperationalComplaint", 0);

                    /*Extra field in Rain Water Damaged Complaint*/
                    object.addProperty("Rain_NoPktsOrBagDamaged", 0);

                    /*Extra field in crop performance*/
                    object.addProperty("PestAttackComment", "");

                    /*Extra field in crop performance*/
                    object.addProperty("DiseaseInfestationComment", "");

                    /*Extra field in crop performance*/
                    if (mOtherDetailsCardView.getVisibility() == View.VISIBLE) {
                        object.addProperty("OtherManualTypingBox", Objects.requireNonNull(mOthersEditTextView.getText()).toString().trim());
                    } else {
                        object.addProperty("OtherManualTypingBox", "");
                    }

                    object.addProperty("OtherfarmersDtl", "");
                    object.addProperty("RaisedComplaintTBM_RBM", "");
                    object.addProperty("ComplaintStatus", "");
                    object.addProperty("OthersInput", "");

                    object.addProperty("GrmBelowStd", "");
                    object.addProperty("DaysAfterSowing", 0);
                    object.addProperty("TotalLotQty", Objects.requireNonNull(mTotalLotQty.getText()).toString().trim());
                    object.addProperty("TotalNoOfPkts", Float.parseFloat(Objects.requireNonNull(mTotalNoOfPkts.getText()).toString().trim()));

                    array.add(object);
                    jsonObject.add("_complaintDetails", array);

                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.add("complaint", jsonObject);

                    mRegstComplntPresenter.callApi(jsonObject1, mContext);
                } catch (Exception e) {
                    Toast.makeText(mContext, "upload error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setEnable(true);
                    e.printStackTrace();
                }
            }
        }
    }

    private Boolean validation() {
        if (TextUtils.isEmpty(Objects.requireNonNull(mLotNumberEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Lot Number");
            return false;
        } else if (mMarketDepotSpinner.getSelectedItemPosition() == 0
                || mMarketDepotSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select Market Depot");
            return false;
        } else if (mBuTextView.getText().toString().trim().equals("")) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (mCropTextView.getText().toString().trim().equals("")) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (mHybridOrVarietyTextView.getText().toString().trim().equals("")) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mPackingSizeTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mLotQtyTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mNoOfPktsTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mTotalLotQty.getText()).toString().trim())) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mTotalNoOfPkts.getText()).toString().trim())) {
            showToast(mContext, "Please Download Lot Information Data First");
            return false;
        } else if (mOtherDetailsCardView.getVisibility() == View.VISIBLE &&
                TextUtils.isEmpty(Objects.requireNonNull(mOthersEditTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Other Details");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mFarmerNameEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Farmer Name");
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mMobileNoEditTextView.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Mobile No.");
            return false;
        } else if (Objects.requireNonNull(mMobileNoEditTextView.getText()).toString().trim().length() != 10) {
            showToast(mContext, "Please Enter Valid Mobile No.");
            return false;
        } /*else if (mMarketDepotSpinner.getSelectedItemPosition() == 0 ||
                mMarketDepotSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select Market Depot");
            return false;
        }*/ else if (mStateSpinner.getSelectedItemPosition() == 0 ||
                mStateSpinner.getSelectedItemPosition() == -1) {
            showToast(mContext, "Please Select State");
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
        } else if ((mDistrictSpinner.getSelectedItem().toString().equalsIgnoreCase("Other") &&
                mTalukaSpinner.getSelectedItem().toString().equalsIgnoreCase("Other")) &&
                TextUtils.isEmpty(Objects.requireNonNull(mRemarkEditText.getText()).toString().trim())) {
            showToast(mContext, "Please Enter Remarks/Other Input");
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

    private void showDatePickerDialog(Boolean isSowingDateTextView) {
        hideKeyboard(mContext);
        Calendar mCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "yyyy-MM-dd";
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

    private void capturePhoto(String photoType) {
        hideKeyboard(mContext);
        try {
            GalleryCameraPickImageDialog.build(new PickSetup())
                    .setOnPickResult(r -> {
                        try {
                            mPhotoFile = createImageFile(CCFStoreData.getString(mContext, CCFConstantValues.CCF_USER_ID)
                                    + "_CCT_MQ_");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mPhotoFile != null && r.getBitmap() != null) {
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(mPhotoFile.getAbsolutePath());
                                r.getBitmap().compress(
                                        Bitmap.CompressFormat.PNG, 100,
                                        fileOutputStream
                                );
                                if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_FIRST_PHOTO)) {
                                    mFirstPhotoPathUsingDialog = r.getPath();
                                    mFirstPhotoPath = mPhotoFile.getAbsolutePath();
                                    mFirstPhotoImageView.setVisibility(View.VISIBLE);
                                    mFirstPhotoBase64String = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                                    mFirstPhotoImageView.setImageBitmap(r.getBitmap());
                                } else if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_SECOND_PHOTO)) {
                                    mSecondPhotoPathUsingDialog = r.getPath();
                                    mSecondPhotoPath = mPhotoFile.getAbsolutePath();
                                    mSecondPhotoImageView.setVisibility(View.VISIBLE);
                                    mSecondPhotoBase64String = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                                    mSecondPhotoImageView.setImageBitmap(r.getBitmap());
                                } else if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_THIRD_PHOTO)) {
                                    mThirdPhotoPathUsingDialog = r.getPath();
                                    mThirdPhotoPath = mPhotoFile.getAbsolutePath();
                                    mThirdPhotoImageView.setVisibility(View.VISIBLE);
                                    mThirdPhotoBase64String = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                                    mThirdPhotoImageView.setImageBitmap(r.getBitmap());
                                } else if (photoType.equalsIgnoreCase(CCFConstantValues.CCF_COMPLAINT_FOURTH_PHOTO)) {
                                    mFourthPhotoPathUsingDialog = r.getPath();
                                    mFourthPhotoPath = mPhotoFile.getAbsolutePath();
                                    mFourthPhotoImageView.setVisibility(View.VISIBLE);
                                    mFourthPhotoBase64String = CCFApplicationUtil.getImageDatadetail(mPhotoFile.getAbsolutePath());
                                    mFourthPhotoImageView.setImageBitmap(r.getBitmap());
                                }

                               /* CCFConstantValues.printLog("captured mFirstPhotoPathUsingDialog "+  mFirstPhotoPathUsingDialog
                                        + " mSecondPhotoPathUsingDialog "+  mSecondPhotoPathUsingDialog
                                        +  " mThirdPhotoPathUsingDialog "+  mThirdPhotoPathUsingDialog
                                        +  " mFourthPhotoPathUsingDialog "+  mFourthPhotoPathUsingDialog);

                                CCFConstantValues.printLog("captured r "+ r +" getbitmap "+r.getBitmap() + " uri "+r.getUri()+"" +
                                        " uri path "+ r.getUri().getPath());*/
                                /*delete photo from local storage*/
                                // deleteImagesAfterPhotoTaken();

                                /*29-06-2023 added*/
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                /*29-06-2023 added ended here*/
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            showToast(mContext, getString(R.string.ccf_went_wrong));
                        }
                    }).setOnPickCancel(() -> showToast(mContext, getString(R.string.ccf_photo_not_captured))).show(CCFTMtrlQuality.this);
      /*  } else {
            showToast(mContext, getString(R.string.ccf_went_wrong));
        }*/
        } catch (Exception e) {
            showToast(mContext, getString(R.string.ccf_went_wrong));
        }
    }


    private void setEnable(Boolean setEnable) {
        mBackImageView.setEnabled(setEnable);
        mSubmitButton.setEnabled(setEnable);
        mDownloadButton.setEnabled(setEnable);
    }

    @Override
    public void showLotDialog() {
        showProgressDialog(this);
    }

    @Override
    public void hideLotDialog() {
        setEnable(true);
        hideProgressDialog();
    }

    @Override
    public void successLotResponse(Response<?> response) {
        if (response.isSuccessful()) {
            CCFLotPojoModel ccfCategoryResponse = (CCFLotPojoModel) response.body();
            if (ccfCategoryResponse != null) {
                if (ccfCategoryResponse.getSuccess()) {
                    if (ccfCategoryResponse.getLotResponseList() != null &&
                            ccfCategoryResponse.getLotResponseList().getLotDataList() != null &&
                            ccfCategoryResponse.getLotResponseList().getLotDataList().size() > 0) {
                        mHybridOrVarietyTextView.setText(ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getHybrid());
                        mCropTextView.setText(ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getCrop());
                        mPackingSizeTextView.setText("" + ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getPackingSize());
                        mLotQtyTextView.setText("" + ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getLotQty());
                        mNoOfPktsTextView.setText("" + ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getNoOfPkts());
                        mBuTextView.setText(ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getBusinessUnitCode());
                        mTotalLotQty.setText("" + ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getTotalLotQty());
                        mTotalNoOfPkts.setText("" + ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getTotalNoOfPkts());
                        //setDepotAdapter(ccfCategoryResponse.getLotResponseList().getLotDataList().get(0).getDepotDataList());
                    } else {
                        showInternetDialog(mContext, ccfCategoryResponse.getMessage());
                    }
                } else {
                    showInternetDialog(mContext, ccfCategoryResponse.getMessage());
                }
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
        } else {
            /*01/6/2023 commented*/
            //  showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            /*01/6/2023 commented ended here*/
            /*01/6/2023 added*/
            if(response.code() == 401) {
                showInternetDialog(mContext, getString(R.string.ccf_session_expired));
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_server_error));
            }
            /*01/6/2023 added ended here*/
        }
    }

    @Override
    public void failLotResponse(Throwable error) {
        if (error instanceof SocketTimeoutException) {
            showInternetDialog(mContext, "Request Timeout!!!");
        } else if (error instanceof UnknownHostException) {
            showInternetDialog(mContext, getString(R.string.ccf_err_internet));
        } else {
            showInternetDialog(mContext, getString(R.string.ccf_err_common));
        }
    }

    @Override
    public void showDialog() {
        showProgressDialog(this);
    }

    @Override
    public void hideDialog() {
        setEnable(true);
        hideProgressDialog();
    }

    @Override
    public void successResponse(Response<?> response) {
        if (response.isSuccessful()) {
            CCFBaseApiResponse baseApiResponse = (CCFBaseApiResponse) response.body();
            if (baseApiResponse != null) {
                if (baseApiResponse.getSuccess()) {
                    deletePhotos();
                    showAfterGettingResponseDialog(mContext, baseApiResponse.getMessage());
                } else {
                    showInternetDialog(mContext, baseApiResponse.getMessage());
                }
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
        } else {
            /*01/6/2023 commented*/
            //  showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            /*01/6/2023 commented ended here*/
            /*01/6/2023 added*/
            if(response.code() == 401) {
                showInternetDialog(mContext, getString(R.string.ccf_session_expired));
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_server_error));
            }
            /*01/6/2023 added ended here*/
        }
    }

    @Override
    public void failResponse(Throwable error) {
        if (error instanceof SocketTimeoutException) {
            showInternetDialog(mContext, "Request Timeout!!!");
        } else if (error instanceof UnknownHostException) {
            showInternetDialog(mContext, getString(R.string.ccf_err_internet));
        } else {
            showInternetDialog(mContext, getString(R.string.ccf_err_common));
        }
    }

    private void deletePhotos() {
      /*  CCFConstantValues.printLog("DELETE mFirstPhotoPathUsingDialog " + mFirstPhotoPathUsingDialog
                + " mSecondPhotoPathUsingDialog " + mSecondPhotoPathUsingDialog
                + " mThirdPhotoPathUsingDialog " + mThirdPhotoPathUsingDialog
                + " mFourthPhotoPathUsingDialog " + mFourthPhotoPathUsingDialog);*/
        if (mFirstPhotoPathUsingDialog != null && !mFirstPhotoPathUsingDialog.equals("")) {
            deleteSinglePhoto(mFirstPhotoPathUsingDialog);
        }
        if (mSecondPhotoPathUsingDialog != null && !mSecondPhotoPathUsingDialog.equals("")) {
            deleteSinglePhoto(mSecondPhotoPathUsingDialog);
        }
        if (mThirdPhotoPathUsingDialog != null && !mThirdPhotoPathUsingDialog.equals("")) {
            deleteSinglePhoto(mThirdPhotoPathUsingDialog);
        }
        if (mFourthPhotoPathUsingDialog != null && !mFourthPhotoPathUsingDialog.equals("")) {
            deleteSinglePhoto(mFourthPhotoPathUsingDialog);
        }
        if (mFirstPhotoPath != null && !mFirstPhotoPath.equals("")) {
            deleteSinglePhoto(mFirstPhotoPath);
        }
        if (mSecondPhotoPath != null && !mSecondPhotoPath.equals("")) {
            deleteSinglePhoto(mSecondPhotoPath);
        }
        if (mThirdPhotoPath != null && !mThirdPhotoPath.equals("")) {
            deleteSinglePhoto(mThirdPhotoPath);
        }
        if (mFourthPhotoPath != null && !mFourthPhotoPath.equals("")) {
            deleteSinglePhoto(mFourthPhotoPath);
        }
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
        if (mRegstComplntPresenter != null) {
            mRegstComplntPresenter.destroy();
        }
        if (mLotNumberPresenter != null) {
            mLotNumberPresenter.destroy();
        }
        hideKeyboard(mContext);
        hideProgressDialog();
        dismissDialog();
        super.onDestroy();
    }

    private void callStateApi() {
        CCFApiClass.getState(this, this);
    }

    private void callDistrictApi(JsonObject jsonObject) {
        CCFApiClass.getDistrict(this, this, jsonObject);
    }

    private void callTalukaApi(JsonObject jsonObject) {
        CCFApiClass.getTaluka(this, this, jsonObject);
    }

    private void setDepotAdapter() {
        try {
            mMarketDepotSpinner.setAdapter(null);
            ArrayList<CCFDepotListData> ccfDepotList = new ArrayList<>();
            ccfDepotList.add(0,
                    new CCFDepotListData("Select Market Depot")
            );

            ArrayAdapter<CCFDepotListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, ccfDepotList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mMarketDepotSpinner.setAdapter(ad);
        } catch (java.lang.Exception e) {
            Log.e("temporary", "setDepotAdapter " + e.getCause());
        }
    }

    private void setStateAdapter() {
        try {
            mStateSpinner.setAdapter(null);
            ArrayList<CCFStateListData> stateList = new ArrayList<>();
            stateList.add(
                    new CCFStateListData("Select State")
            );

            ArrayAdapter<CCFStateListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, stateList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mStateSpinner.setAdapter(ad);
        } catch (java.lang.Exception e) {
            Log.e("temporary", "setStateAdapter " + e.getCause());
        }
    }

    private void setDistrictAdapter() {
        try {
            mDistrictSpinner.setAdapter(null);
            ArrayList<CCFDistrictListData> districtList = new ArrayList<>();
            districtList.add(
                    new CCFDistrictListData("Select District")
            );
            ArrayAdapter<CCFDistrictListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, districtList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mDistrictSpinner.setAdapter(ad);
        } catch (java.lang.Exception e) {
            Log.e("temporary", "setDistrictAdapter " + e.getCause());
        }
    }

    private void setTalukaAdapter(boolean isSelectDistrictOther) {
        try {
            mTalukaSpinner.setAdapter(null);
            ArrayList<CCFTalukaListData> talukaList = new ArrayList<>();
            if (isSelectDistrictOther) {
                mLabelForOther.setVisibility(View.GONE);
                talukaList.add(
                        new CCFTalukaListData("Select Taluka")
                );

                talukaList.add(
                        new CCFTalukaListData("Other")
                );
            } else {
                talukaList.add(
                        new CCFTalukaListData("Select Taluka")
                );
            }

            ArrayAdapter<CCFTalukaListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, talukaList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mTalukaSpinner.setAdapter(ad);

            mTalukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0 && mTalukaSpinner.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        showInternetDialog(mContext, "You Have Selected District And Taluka Other, You Need To Provide More Information About District And Taluka In The Remarks Field.");
                        mLabelForOther.setVisibility(View.VISIBLE);
                    } else {
                        mLabelForOther.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (java.lang.Exception e) {
            Log.e("temporary", "setTalukaAdapter " + e.getCause());
        }
    }

    private List<CCFStateListData> mStateList = new ArrayList<>();
    private List<CCFDistrictListData> mDistrictList = new ArrayList<>();
    private List<CCFTalukaListData> mTalukaList = new ArrayList<>();
    private List<CCFDepotListData> mDepotList = new ArrayList<>();

    @Override
    public void onStateListResponse(CCFStatePojoModel ccfStatePojoModel) {
        try {
            mStateSpinner.setAdapter(null);

            if (mStateList != null) {
                mStateList.clear();
            }

            if (ccfStatePojoModel.isSuccess()) {
                // CCFApiClass.getDepot(this, this/*, jsonObject*/);
                mStateList = ccfStatePojoModel.getStateList().getTable();
                showToast(mContext, ccfStatePojoModel.getMessage());
            } else {
                showInternetDialog(mContext, ccfStatePojoModel.getMessage());
            }

            mStateList.add(0,
                    new CCFStateListData("Select State")
            );

            ArrayAdapter<CCFStateListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, mStateList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mStateSpinner.setAdapter(ad);
            mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setDistrictAdapter();
                    setTalukaAdapter(false);
                    if (position != 0) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("state_name", mStateList.get(position).getState_name());
                        callDistrictApi(jsonObject);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (java.lang.Exception e) {
            Log.e("temporary", " state spinner " + e.getCause());
        }
    }

    @Override
    public void onDistrictListResponse(CCFDistrictPojoModel ccfDistrictPojoModel) {
        try {
            mDistrictSpinner.setAdapter(null);

            if (mDistrictList != null) {
                mDistrictList.clear();
            }

            if (ccfDistrictPojoModel.isSuccess()) {
                mDistrictList = ccfDistrictPojoModel.getDistrictList().getTable();
                showToast(mContext, ccfDistrictPojoModel.getMessage());
            } else {
                showInternetDialog(mContext, ccfDistrictPojoModel.getMessage());
            }

            mDistrictList.add(0,
                    new CCFDistrictListData("Select District")
            );

            mDistrictList.add(
                    new CCFDistrictListData("Other")
            );

            ArrayAdapter<CCFDistrictListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, mDistrictList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mDistrictSpinner.setAdapter(ad);
            mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!mDistrictList.get(position).getDistrict_name().equalsIgnoreCase("Other")) {
                        setTalukaAdapter(false);
                        if (position != 0) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("District_name", mDistrictList.get(position).getDistrict_name());
                            callTalukaApi(jsonObject);
                        }
                    } else {
                        setTalukaAdapter(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (java.lang.Exception e) {
            Log.e("temporary", " district spinner " + e.getCause());
        }
    }

    @Override
    public void onTalukaListResponse(CCFTalukaPojoModel ccfTalukaPojoModel) {
        try {
            mTalukaSpinner.setAdapter(null);

            if (mTalukaList != null) {
                mTalukaList.clear();
            }

            if (ccfTalukaPojoModel.isSuccess()) {
                mTalukaList = ccfTalukaPojoModel.getTalukaList().getTable();
                showToast(mContext, ccfTalukaPojoModel.getMessage());
            } else {
                showInternetDialog(mContext, ccfTalukaPojoModel.getMessage());
            }

            mTalukaList.add(0,
                    new CCFTalukaListData("Select Taluka")
            );

            ArrayAdapter<CCFTalukaListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, mTalukaList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mTalukaSpinner.setAdapter(ad);

            mTalukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0 && mTalukaSpinner.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        showInternetDialog(mContext, "You Have Selected District And Taluka Other, You Need To Provide More Information About District And Taluka In The Remarks Field.");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (java.lang.Exception e) {
            Log.e("temporary", " taluka spinner " + e.getCause());
        }
    }

    @Override
    public void onDepotListResponse(CCFDepotPojoModel ccfDepotPojoModel) {
        setEnable(true);
        try {
            mMarketDepotSpinner.setAdapter(null);

            if (mDepotList != null) {
                mDepotList.clear();
            }

            if (ccfDepotPojoModel.isSuccess()) {
                Toast.makeText(mContext, ccfDepotPojoModel.getMessage(), Toast.LENGTH_SHORT).show();
                mDepotList = ccfDepotPojoModel.getDepotList().getTable();
            } else {
                showInternetDialog(mContext, ccfDepotPojoModel.getMessage());
            }

            mDepotList.add(0,
                    new CCFDepotListData("Select Market Depot")
            );

            ArrayAdapter<CCFDepotListData> ad = new ArrayAdapter<>(mContext, R.layout.ccf_spinner_dropdown, mDepotList);
            ad.setDropDownViewResource(R.layout.ccf_spinner_dropdown);
            mMarketDepotSpinner.setAdapter(ad);
            mMarketDepotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    afterSelectDepotChangeText();
                    if (position != 0) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("CHARG_LotBatch", mLotNumberEditText.getText().toString().trim());
                        jsonObject.addProperty("WERKS_DepoPlant", mMarketDepotSpinner.getSelectedItem().toString());
                        mLotNumberPresenter.callApi(jsonObject, mContext);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (java.lang.Exception e) {
            Log.e("temporary", " market spinner " + e.getCause());
        }
    }

    private void afterSelectDepotChangeText() {
        mBuTextView.setText("");
        mCropTextView.setText("");
        mHybridOrVarietyTextView.setText("");
        mPackingSizeTextView.setText("");
        mLotQtyTextView.setText("");
        mNoOfPktsTextView.setText("");
        mTotalLotQty.setText("");
        mTotalNoOfPkts.setText("");
    }

    @Override
    public void onFailure(String str, Throwable error) {
        setEnable(true);
        if (str != null && str.equalsIgnoreCase("")) {
            if (error instanceof SocketTimeoutException) {
                showInternetDialog(mContext, "Request Timeout!!!");
            } else if (error instanceof UnknownHostException) {
                showInternetDialog(mContext, getString(R.string.ccf_err_internet));
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
        } else {
            showInternetDialog(mContext, str);
        }
    }
}
