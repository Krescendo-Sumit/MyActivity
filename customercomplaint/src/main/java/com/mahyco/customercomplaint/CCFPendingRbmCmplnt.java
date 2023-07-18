/*
package com.mahyco.customercomplaint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfadapter.CCFPendingAdapter;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFViewCmplntModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFPendingCmplntPojoMOdel;
import com.mahyco.customercomplaint.ccfpresenter.CCFViewCmplntPresenter;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Response;

public class CCFPendingRbmCmplnt extends CCFBaseActivity implements CCFCommonViewInterface, View.OnClickListener {

    private Context mContext;
    private AppCompatImageView mBackImageView;

    private CCFViewCmplntPresenter mViewCmplntPresenter;

    private CCFPendingAdapter adapter = null;
    private ExpandableListView expandableListView = null;

    @Override
    protected int getLayout() {
        return R.layout.ccf_pending_rdm_complaint;
    }

    @Override
    protected void init() {

        mContext = this;

        mViewCmplntPresenter = new CCFViewCmplntPresenter(this, CCFViewCmplntModel.getInstance());

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.cc_form_vc_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_pending_complaint));

        expandableListView = findViewById(R.id.ccf_pending_rbm_expendableList);

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

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

    public static int lastExpandedPosition = -1;

    @Override
    public void successResponse(Response<?> response) {
        if (response.isSuccessful()) {
            CCFPendingCmplntPojoMOdel trips = (CCFPendingCmplntPojoMOdel) response.body();
            if (trips != null) {
                if (trips.getSuccess()) {
                    ArrayList<CCFPendingCmplntPojoMOdel.Data> mData = trips.getData();
                    if (mData != null) {
                        if (mData.size() > 0) {

                            */
/*added below code on 15/06/2017*//*

                            CCFStoreData.clearPendingComment(mContext);
                            */
/*added code ended here 15/06/2017*//*


                            adapter = new CCFPendingAdapter(mContext, mData, CCFPendingRbmCmplnt.this);
                            expandableListView.setAdapter(adapter);

                            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                  //  Log.e("temporary","adapter.comment "+ adapter.comment);
                                    */
/*added below code on 15/06/2017*//*

                                    adapter.comment = "";
                                    CCFStoreData.clearPendingComment(mContext);
                                    */
/*added code ended here 15/06/2017*//*

                                    hideKeyboard(mContext);

                                    */
/*commented below code on 15/06/2017*//*

                                    */
/*if (adapter.editText != null && (adapter.editText.isFocused()|| !adapter.comment.equalsIgnoreCase(""))) {
                                        Log.e("temporary","inside adapter.editText.isFocused() "+ adapter.editText.isFocused());
                                        *//*
*/
/*new code added 14/6/2023 2.00pm*//*
*/
/*
                                        adapter.editText.setText("");
                                        adapter.comment = "";
                                        *//*
*/
/*new code added ended here 14/6/2023 2.00pm*//*
*/
/*
                                        adapter.editText.clearFocus();
                                    }*//*

                                    */
/*commented code on 15/06/2017 ended here*//*

                                  //  Log.e("temporary","after adapter.comment "+ adapter.comment);
                                    return false;
                                }
                            });
                            */
/*new code added 14/6/2023 2.00pm*//*

                            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                @Override
                                public void onGroupExpand(int groupPosition) {
                                    if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                                        expandableListView.collapseGroup(lastExpandedPosition);
                                    }
                                    lastExpandedPosition = groupPosition;
                                }
                            });
                            */
/*new code added ended here 14/6/2023 2.00pm*//*

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

   */
/* private void setListViewHeight(
            ExpandableListView listView,
            int group
    ) {
        try {
            ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                    listView.getWidth(),
                    View.MeasureSpec.EXACTLY
            );

            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View groupItem = listAdapter.getGroupView(i, false, null, listView);
                groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += groupItem.getMeasuredHeight();

                if (listView.isGroupExpanded(i) && i != group
                        || !listView.isGroupExpanded(i) && i == group
                ) {
                    for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                        View listItem = listAdapter.getChildView(
                                i, j, false, null,
                                listView
                        );

                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                }
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = (totalHeight
                    + listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            if (height < 10) height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            Toast.makeText(mContext, "lstheight " + e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }*//*


    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
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
}
*/
