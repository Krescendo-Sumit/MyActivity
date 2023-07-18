package com.mahyco.customercomplaint;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.ccfactivities.CCFBaseActivity;
import com.mahyco.customercomplaint.ccfadapter.CCFEventsAdapter;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfmodel.CCFViewCmplntModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFViewCmplntPojoModel;
import com.mahyco.customercomplaint.ccfpresenter.CCFViewCmplntPresenter;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

public class CCFViewComplaint extends CCFBaseActivity implements CCFCommonViewInterface, View.OnClickListener {

    //    private RecyclerView mUpComingTripRecyclerView;
//    private CCFPendingCmplntAdapter mCCfViewCmplntAdapter;
    private Context mContext;
    private AppCompatImageView mBackImageView;

    private CCFViewCmplntPresenter mViewCmplntPresenter;

    private CCFEventsAdapter adapter = null;
    private ExpandableListView expandableListView = null;
    public int lastExpandedPosition = -1;

    @Override
    protected int getLayout() {
        return R.layout.ccf_view_complaints;
    }

    @Override
    protected void init() {

        mContext = this;

        mViewCmplntPresenter = new CCFViewCmplntPresenter(this, CCFViewCmplntModel.getInstance());

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.cc_form_vc_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_view_complaint));

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

       /* mUpComingTripRecyclerView = findViewById(R.id.ccf_vc_complaint_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mUpComingTripRecyclerView.setLayoutManager(linearLayoutManager);*/

        expandableListView = findViewById(R.id.ccf_vc_expendableList);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("RaisedComplaintTBM", CCFStoreData.getString(mContext, CCFConstantValues.CCF_RAISED_COMPLAINT_TBM_CODE));
        mViewCmplntPresenter.callViewCmplntApi(jsonObject, mContext);
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
            CCFViewCmplntPojoModel trips = (CCFViewCmplntPojoModel) response.body();
            if (trips != null) {
                if (trips.getSuccess()) {
                    CCFViewCmplntPojoModel.ReturnValue mData = trips.getReturnValue();
                    if (mData != null) {
                        if (mData.getTable() != null && mData.getTable().size() > 0) {

                            adapter = new CCFEventsAdapter(mContext, mData.getTable());
                            expandableListView.setAdapter(adapter);

                            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                  //  setListViewHeight(parent, groupPosition);
                                    return false;
                                }
                            });
//                            mCCfViewCmplntAdapter = new CCFPendingCmplntAdapter(mContext, mData.getTable());
//                            mUpComingTripRecyclerView.setAdapter(mCCfViewCmplntAdapter);

                            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                @Override
                                public void onGroupExpand(int groupPosition) {
                                    if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                                        expandableListView.collapseGroup(lastExpandedPosition);
                                    }
                                    lastExpandedPosition = groupPosition;
                                }
                            });
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
            /*01/6/2023 commented*/
            //  showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            /*01/6/2023 commented ended here*/
            /*01/6/2023 added*/
            if (response.code() == 401) {
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

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        Intent intent = new Intent(mContext, CCFFirstActivity.class);
        startActivity(intent);
        finish();
    }

    private void setListViewHeight(
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
}
