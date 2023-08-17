package com.mahyco.customercomplaint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfactivities.CCFComplaintTypesActivity;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.util.Objects;

public class CCFFirstActivity extends CCFBaseActivity implements View.OnClickListener {

    private CardView mViewComplaintCardView;
    private CardView mNewComplaintCardView;
    private CardView mPendingWithRbmCardView;

    private final Context mContext = this;
    private AppCompatImageView mBackImageView;

    @Override
    protected int getLayout() {
        return R.layout.ccf_first_activity;
    }

    @Override
    protected void init() {

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.ccf_first_activity_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        mViewComplaintCardView = findViewById(R.id.ccf_view_my_complaint);
        mViewComplaintCardView.setOnClickListener(this);

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);


        mNewComplaintCardView = findViewById(R.id.ccf_new_complaint);
        mNewComplaintCardView.setOnClickListener(this);

        mPendingWithRbmCardView = findViewById(R.id.ccf_ccf_pending_with_rbm);
        mPendingWithRbmCardView.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
          //   Log.e("temporary","extras "+extras);
            if (extras != null/* && extras.getString("ccfFromMyActivity") != null*/) {
                CCFStoreData.clearString(this);
            }

            if (extras != null && extras.getString("ccfContactNo") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_CONTACT_NO_TBM, intent.getStringExtra("ccfContactNo"));
            }

            if (extras != null && extras.getString("ccfUserId") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_USER_ID, intent.getStringExtra("ccfUserId"));
            }

            if (extras != null && extras.getString("ccfTBMOrRBMCode") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE, intent.getStringExtra("ccfTBMOrRBMCode"));
            }

            if (extras != null && extras.getString("ccfToken") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_CUST_TOKEN, intent.getStringExtra("ccfToken"));
            }

            //ccfUserRoleID == 4 TBM, 2 == RBM
            if (extras != null && extras.getString("ccfUserRoleID") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_USER_ROLE_ID, intent.getStringExtra("ccfUserRoleID"));
            }

            if (extras != null && extras.getString("ccfTBMOrRBMCode") != null) {
                CCFStoreData.putString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_RBM_CODE, intent.getStringExtra("ccfTBMOrRBMCode"));
            }

            //ccfUserRoleID == 4 TBM, 2 == RBM
            if (extras != null && extras.getString("ccfUserRoleID") != null &&
                    Objects.requireNonNull(extras.getString("ccfUserRoleID")).equalsIgnoreCase("4")) {
                mTitleTextView.setText(getString(R.string.ccf_view_new_complaint));
                mViewComplaintCardView.setVisibility(View.VISIBLE);
                mNewComplaintCardView.setVisibility(View.VISIBLE);
                mPendingWithRbmCardView.setVisibility(View.GONE);
            } else if (extras != null && extras.getString("ccfUserRoleID") != null &&
                    Objects.requireNonNull(extras.getString("ccfUserRoleID")).equalsIgnoreCase("2")) {
                mTitleTextView.setText(getString(R.string.ccf_pending_new_complaint));
                mViewComplaintCardView.setVisibility(View.VISIBLE);
                mNewComplaintCardView.setVisibility(View.VISIBLE);
                mPendingWithRbmCardView.setVisibility(View.VISIBLE);
            }

            if (extras == null && CCFStoreData.getString(mContext, CCFConstantValues.CCF_USER_ROLE_ID).equalsIgnoreCase("4")) {
                mTitleTextView.setText(getString(R.string.ccf_view_new_complaint));
                mViewComplaintCardView.setVisibility(View.VISIBLE);
                mNewComplaintCardView.setVisibility(View.VISIBLE);
                mPendingWithRbmCardView.setVisibility(View.GONE);
            } else if (extras == null && CCFStoreData.getString(mContext, CCFConstantValues.CCF_USER_ROLE_ID).equalsIgnoreCase("2")) {
                mTitleTextView.setText(getString(R.string.ccf_pending_new_complaint));
                mViewComplaintCardView.setVisibility(View.VISIBLE);
                mNewComplaintCardView.setVisibility(View.VISIBLE);
                mPendingWithRbmCardView.setVisibility(View.VISIBLE);
            }

//            CCFStoreData.putString(mContext, CCFConstantValues.CCF_VERSION_CODE, "1.0.0");

//            if (extras != null && extras.getString("ccfUserId") != null
//                    /*&& extras.getString("ccfVersionCode") != null
//                    && extras.getString("ccfActivityPath") != null*/
//                    && extras.getString("ccfTBMCode") != null
//                    /*&& extras.getString("ccfTBMRBMCode") != null*/
//                    && extras.getString("ccfContactNo") != null
//                    && extras.getString("ccfToken") != null) {
//                CCFStoreData.putString(mContext, CCFConstantValues.CCF_USER_ID, intent.getStringExtra("ccfUserId"));
//                CCFStoreData.putString(mContext, CCFConstantValues.CCF_CUST_TOKEN, intent.getStringExtra("ccfToken"));
//                CCFStoreData.putString(mContext, CCFConstantValues.CCF_CONTACT_NO_TBM, intent.getStringExtra("ccfContactNo"));
//                CCFStoreData.putString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE, intent.getStringExtra("ccfTBMCode"));
//                CCFStoreData.putString(mContext, CCFConstantValues.CCF_VERSION_CODE, /*intent.getStringExtra("ccfVersionCode")*/"1.0.0");
//                /*CCFStoreData.putString(mContext, CCFConstantValues.CCF_ACTIVITY_PATH, intent.getStringExtra("ccfActivityPath"));
//               CCFStoreData.putString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_RBM, intent.getStringExtra("ccfTBMRBMCode"));*/
//            }
        }
    }

    @Override
    public void onClick(View view) {
//        Log.e("temporary","CCF_RAISED_COMPLAINT_TBM_CODE "+ CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE));
//        Log.e("temporary","CCF_RAISED_COMPLAINT_RBM_CODE "+ CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_RBM_CODE));
//        Log.e("temporary","userid "+ CCFStoreData.getString(mContext, CCFConstantValues.CCF_USER_ID));
//        Log.e("temporary", "CCF_CONTACT_NO_TBM " + CCFStoreData.getString(mContext, CCFConstantValues.CCF_CONTACT_NO_TBM));
//        Log.e("temporary","CCF_CUST_TOKEN "+ CCFStoreData.getString(mContext, CCFConstantValues.CCF_CUST_TOKEN));

        if (view == mBackImageView) {
            finishActivity();
        } else if (view == mViewComplaintCardView) {
            if (checkInternetConnection(mContext)) {
                Intent intent = new Intent(mContext, CCFViewComplaint.class);
                startActivity(intent);
                finish();
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_err_internet));
            }
        } else if (view == mNewComplaintCardView) {
            if (checkInternetConnection(mContext)) {
                CCFDataPreferences.putDataIsStoredString(mContext, "No");
                CCFDataPreferences.getClearCategoryData(mContext);
                Intent intent = new Intent(mContext, CCFComplaintTypesActivity.class);
                startActivity(intent);
                finish();
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_err_internet));
            }
        } else if (view == mPendingWithRbmCardView) {
            if (checkInternetConnection(mContext)) {
                Intent intent = new Intent(mContext, /*CCFPendingRbmCmplnt*/CCFNewRbmPendingCmplnt.class);
                startActivity(intent);
                finish();
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_err_internet));
            }
        }
    }

    private void finishActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
