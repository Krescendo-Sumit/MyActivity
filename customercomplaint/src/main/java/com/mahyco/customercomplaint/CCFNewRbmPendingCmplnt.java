package com.mahyco.customercomplaint;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfadapter.CCFPendingCmplntAdapter;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFRecyclerViewClickListener;
import com.mahyco.customercomplaint.ccfmodel.CCFViewCmplntModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFPendingCmplntPojoMOdel;
import com.mahyco.customercomplaint.ccfpresenter.CCFViewCmplntPresenter;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Response;

public class CCFNewRbmPendingCmplnt extends CCFBaseActivity implements CCFCommonViewInterface, View.OnClickListener, CCFRecyclerViewClickListener {

    private Context mContext;
    private AppCompatImageView mBackImageView;

    private CCFViewCmplntPresenter mViewCmplntPresenter;

    private RecyclerView mUpComingTripRecyclerView;
    public static Context cntxofParent;
    private ArrayList<CCFPendingCmplntPojoMOdel.Data> arraylist = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.ccf_new_pending_rdm_complaint;
    }

    @Override
    protected void init() {

        mContext = this;
        cntxofParent = this;

        mViewCmplntPresenter = new CCFViewCmplntPresenter(this, CCFViewCmplntModel.getInstance());

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.ccf_rbm_pending_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_pending_complaint));

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

        mUpComingTripRecyclerView = findViewById(R.id.ccf_pending_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mUpComingTripRecyclerView.setLayoutManager(linearLayoutManager);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("RaisedTBM_RBMCode", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_RBM_CODE));
        mViewCmplntPresenter.callPendingCmplntApi(jsonObject, mContext);
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
        hideProgressDialog();
    }



    @Override
    public void successResponse(Response<?> response) {
        if (response.isSuccessful()) {
            CCFPendingCmplntPojoMOdel trips = (CCFPendingCmplntPojoMOdel) response.body();
            if (trips != null) {
                if (trips.getSuccess()) {
                    ArrayList<CCFPendingCmplntPojoMOdel.Data> mData = trips.getData();
                    if (mData != null) {
                        if (mData.size() > 0) {
                            arraylist = mData;
                            CCFPendingCmplntAdapter mUpcomingTripsAdapter = new CCFPendingCmplntAdapter(mContext, mData, this);
                            mUpComingTripRecyclerView.setAdapter(mUpcomingTripsAdapter);
                        }
                    } else {
                        showInternetDialog(mContext, trips.getMessage());
                    }
                } else {
                    showInternetDialog(mContext, trips.getMessage());
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
    public void failResponse(Throwable error) {
        if (error instanceof SocketTimeoutException) {
            showInternetDialog(mContext, "Request Timeout!!!");
        } else if (error instanceof UnknownHostException) {
            showInternetDialog(mContext, getString(R.string.ccf_err_internet));
        } else {
            showInternetDialog(mContext, getString(R.string.ccf_err_common));
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        cntxofParent = null;
        if (mViewCmplntPresenter != null) {
            mViewCmplntPresenter.destroy();
        }
        hideKeyboard(mContext);
        hideProgressDialog();
        dismissDialog();
        super.onDestroy();
    }

    private void finishActivity() {
        Intent intent = new Intent(mContext, CCFFirstActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void ccfRecyclerViewListClicked(View v, int groupPosition) {
//        CCFStoreData.clearSubmitComment(mContext);
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_ID, String.valueOf(this.arraylist.get(groupPosition).getRegisterComplaintID()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TYPE, this.arraylist.get(groupPosition).getComplaintType() != null ? this.arraylist.get(groupPosition).getComplaintType() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SUB_TYPE, this.arraylist.get(groupPosition).getComplaintSubType() != null ? this.arraylist.get(groupPosition).getComplaintSubType() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SUB_CATEGORY_DESC, this.arraylist.get(groupPosition).getSubCategoryDesc() != null ? this.arraylist.get(groupPosition).getSubCategoryDesc() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_LOT_NUMBER, this.arraylist.get(groupPosition).getLotNumber() != null ? this.arraylist.get(groupPosition).getLotNumber() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DEPOT, this.arraylist.get(groupPosition).getDepot() != null ? this.arraylist.get(groupPosition).getDepot() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_BU, this.arraylist.get(groupPosition).getBusinessUnit() != null ? this.arraylist.get(groupPosition).getBusinessUnit() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_CROP_DTL, this.arraylist.get(groupPosition).getCropDtl() != null ? this.arraylist.get(groupPosition).getCropDtl() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_HYBRID_VARIETY, this.arraylist.get(groupPosition).getHybrid_Variety() != null ? this.arraylist.get(groupPosition).getHybrid_Variety() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PACK_SIZE, String.valueOf(this.arraylist.get(groupPosition).getPackingSize()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DATE_COMPLAINT, this.arraylist.get(groupPosition).getDateOfComplaint() != null ? this.arraylist.get(groupPosition).getDateOfComplaint() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_FARMER_NAME, this.arraylist.get(groupPosition).getFarmerName() != null ? this.arraylist.get(groupPosition).getFarmerName() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_FARMER_CONTACT, this.arraylist.get(groupPosition).getFarmerContact() != null ? this.arraylist.get(groupPosition).getFarmerContact() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_STATE, this.arraylist.get(groupPosition).getState() != null ? this.arraylist.get(groupPosition).getState() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DIST, this.arraylist.get(groupPosition).getDistrict() != null ? this.arraylist.get(groupPosition).getDistrict() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TALUKA, this.arraylist.get(groupPosition).getTaluka() != null ? this.arraylist.get(groupPosition).getTaluka() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_VILLAGE, this.arraylist.get(groupPosition).getVillage() != null ? this.arraylist.get(groupPosition).getVillage() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SOWING_DATE, this.arraylist.get(groupPosition).getSowingDate() != null ? this.arraylist.get(groupPosition).getSowingDate() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_CROP_STAGE, this.arraylist.get(groupPosition).getCropStage() != null ? this.arraylist.get(groupPosition).getCropStage() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TBM_CODE, this.arraylist.get(groupPosition).getRaisedComplaintTBM() != null ? this.arraylist.get(groupPosition).getRaisedComplaintTBM() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_SOWING_DAYS, String.valueOf(this.arraylist.get(groupPosition).getDaysAfterSowing()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_REMARKS, this.arraylist.get(groupPosition).getRemarksInput() != null ? this.arraylist.get(groupPosition).getRemarksInput() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH1, this.arraylist.get(groupPosition).getPhoto1UploadedPath() != null ? this.arraylist.get(groupPosition).getPhoto1UploadedPath() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH2, this.arraylist.get(groupPosition).getPhoto2UploadedPath() != null ? this.arraylist.get(groupPosition).getPhoto2UploadedPath() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH3, this.arraylist.get(groupPosition).getPhoto3UploadedPath() != null ? this.arraylist.get(groupPosition).getPhoto3UploadedPath() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PATH4, this.arraylist.get(groupPosition).getPhoto4UploadedPath() != null ? this.arraylist.get(groupPosition).getPhoto4UploadedPath() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_OTH_MANUAL_TY_BOX, this.arraylist.get(groupPosition).getOtherManualTypingBox() != null ? this.arraylist.get(groupPosition).getOtherManualTypingBox() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_ADMIXTURE_TYPE, this.arraylist.get(groupPosition).getGp_AdmixtureType() != null ? this.arraylist.get(groupPosition).getGp_AdmixtureType() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_ADMIXTURE_PERCENT, String.valueOf(this.arraylist.get(groupPosition).getGp_AdmixturePercent()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_OFF_TYPE, this.arraylist.get(groupPosition).getGp_OffType() != null ? this.arraylist.get(groupPosition).getGp_OffType() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GP_OFF_PERCENT, String.valueOf(this.arraylist.get(groupPosition).getGp_OffTypePercent()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_PEST_COMMENT, this.arraylist.get(groupPosition).getPestAttackComment() != null ? this.arraylist.get(groupPosition).getPestAttackComment() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_DISEASE_COMMENT, this.arraylist.get(groupPosition).getDiseaseInfestationComment() != null ? this.arraylist.get(groupPosition).getDiseaseInfestationComment() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_GERM_PERCENT, String.valueOf(this.arraylist.get(groupPosition).getGrmPercent()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_LATE_GERM, this.arraylist.get(groupPosition).getLateGrm() != null ? this.arraylist.get(groupPosition).getLateGrm() : "");
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_RAIN_NO_PKTS_DAMAGED, String.valueOf(this.arraylist.get(groupPosition).getRain_NoPktsOrBagDamaged()));
        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_OPERATIONAL_NO_PKTS_DAMAGED, String.valueOf(this.arraylist.get(groupPosition).getNo_PKtsBagDamagedOperationalComplaint()));

        CCFStoreData.putStringForSubmitComment(mContext, CCFConstantValues.CCF_SUBMIT_COMPLAINT_TBM_CONTACT, this.arraylist.get(groupPosition).getContactNoTBM() != null ? this.arraylist.get(groupPosition).getContactNoTBM() : "");

        Intent intent = new Intent(this, CCFSubmitPendingCmplnt.class);
        startActivity(intent);
    }
}
