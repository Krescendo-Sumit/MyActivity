package com.mahyco.customercomplaint.ccfactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.mahyco.customercomplaint.CCFConstantValues;
import com.mahyco.customercomplaint.CCFDataPreferences;
import com.mahyco.customercomplaint.CCFFirstActivity;
import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfactivities.ccfoperational.CCFOperCmplntActivity;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTCropPerfmnce;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTGeneticPurity;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTGermination;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTMtrlQuality;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTPhysicalAppear;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTPhysicalPurity;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTPoorGrading;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTRainDamage;
import com.mahyco.customercomplaint.ccfactivities.ccftechnical.CCFTSeedHealth;
import com.mahyco.customercomplaint.ccfcmplnttype.CCFCategoryTypeModel;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCommonViewInterface;
import com.mahyco.customercomplaint.ccfnetwork.CCFCategoryPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFMainCategoryList;
import com.mahyco.customercomplaint.ccfnetwork.CCFSubCategoryList;
import com.mahyco.customercomplaint.ccfpresenter.CCFCategoryPresenter;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class CCFComplaintTypesActivity extends CCFBaseActivity implements View.OnClickListener, CCFCommonViewInterface {

    private AppCompatButton mTechnicalProceedBtn;
    private AppCompatButton mOperationalProceedBtn;

    private AppCompatImageView mBackImageView;

    private androidx.appcompat.widget.AppCompatSpinner mTechMainCategorySpinner;
    private androidx.appcompat.widget.AppCompatSpinner mOprMainCategorySpinner;

    private androidx.appcompat.widget.AppCompatTextView mTechSubCategoryTextView;
    private androidx.appcompat.widget.AppCompatTextView mOprnlSubCategoryTextView;

    private CardView mTechComplaintCardView;
    private CardView mOprComplaintCardView;

    private final Context mContext = this;
    private CCFCategoryPresenter mCcfCategoryPresenter;

    private final List<CCFMainCategoryList> mMainCategoryList = new ArrayList<>();
    private final List<CCFSubCategoryList> mSubCategoryList = new ArrayList<>();
    private List<CCFSubCategoryList> mTempSubCategoryList = new ArrayList<>();

    private final List<CCFMainCategoryList> mOperationalMainCategoryList = new ArrayList<>();
    private final List<CCFSubCategoryList> mOperationalSubCategoryList = new ArrayList<>();

    private final ArrayList<CCFCategoryTypeModel> categoryModelArrayList = new ArrayList<>();
    private StringBuilder mSubCategoryIdsString = null;
    private AlertDialog mAlertDialog = null;

    @Override
    protected int getLayout() {
        return R.layout.ccft_complaint_type_activity;
    }

    @Override
    protected void init() {

        mCcfCategoryPresenter = new CCFCategoryPresenter(this, com.mahyco.customercomplaint.ccfmodel.CCFCategoryModel.getInstance());

        RadioGroup mRadioGroup = findViewById(R.id.cc_form_radio_group);
        mRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.cc_form_technical_radio_btn) {
                mTechComplaintCardView.setVisibility(View.VISIBLE);
                mOprComplaintCardView.setVisibility(View.GONE);
            } else if (i == R.id.cc_form_operational_radio_btn) {
                mTechComplaintCardView.setVisibility(View.GONE);
                mOprComplaintCardView.setVisibility(View.VISIBLE);
            }
            mTechMainCategorySpinner.setSelection(0);
            mOprMainCategorySpinner.setSelection(0);
        });

        mTechComplaintCardView = findViewById(R.id.cc_form_technical_complaint_cardview);
        mOprComplaintCardView = findViewById(R.id.cc_form_operational_complaint_cardview);

        mTechMainCategorySpinner = findViewById(R.id.cc_form_select_tech_main_category);
        mTechSubCategoryTextView = findViewById(R.id.cc_form_select_tech_sub_category);
        mTechSubCategoryTextView.setOnClickListener(this);

        mOprnlSubCategoryTextView = findViewById(R.id.cc_form_select_oper_sub_category);
        mOprnlSubCategoryTextView.setOnClickListener(this);

        mOprMainCategorySpinner = findViewById(R.id.cc_form_select_oper_main_category);

        mTechMainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (mSubCategoryList != null) {
                        mSubCategoryList.clear();
                    }
                    int categoryMainId = mMainCategoryList.get(position).getCTID();
                    for (int j = 0; j < mTempSubCategoryList.size(); j++) {
                        if (categoryMainId == mTempSubCategoryList.get(j).getCTID()) {
                            mSubCategoryList.add(mTempSubCategoryList.get(j));
                        }
                    }
                }
                setTechSubAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mOprMainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setOperationalSubAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AppCompatTextView mTitleTextView = findViewById(R.id.ccf_title);
        mTitleTextView.setText(getString(R.string.ccf_complaint_types));

        AppCompatTextView mVersionCodeTextView = findViewById(R.id.cc_form_ca_version_code);
        mVersionCodeTextView.setText(getString(R.string.ccf_version_code));

        mTechnicalProceedBtn = findViewById(R.id.cc_form_technical_proceed_btn);
        mTechnicalProceedBtn.setOnClickListener(this);

        mBackImageView = findViewById(R.id.ccf_back);
        mBackImageView.setOnClickListener(this);

        mOperationalProceedBtn = findViewById(R.id.cc_form_operational_proceed_btn);
        mOperationalProceedBtn.setOnClickListener(this);

        if (CCFDataPreferences.getDataStoredString(mContext).equalsIgnoreCase("Yes")) {
            getStoredCategoryLocalData();
        } else {
            mCcfCategoryPresenter.callApi(mContext);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mBackImageView) {
            setEnable(false);
            callFirstActivity();
        } else if (view == mTechSubCategoryTextView) {
            if (mTechMainCategorySpinner.getSelectedItemPosition() == 0
                    || mTechMainCategorySpinner.getSelectedItemPosition() == -1) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Technical Main Feedback Category");
            } else {
                techSubCategoryCheckBox();
            }
        } else if (view == mOprnlSubCategoryTextView) {
            if (mOprMainCategorySpinner.getSelectedItemPosition() == 0
                    || mOprMainCategorySpinner.getSelectedItemPosition() == -1) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Operational Main Feedback Category");
            } else {
                oprnlSubCategoryCheckBox();
            }
        } else if (view == mTechnicalProceedBtn) {
            if (mTechMainCategorySpinner.getSelectedItemPosition() == 0
                    || mTechMainCategorySpinner.getSelectedItemPosition() == -1) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Technical Main Feedback Category");
            } else if (mTechSubCategoryTextView.getText().toString().trim().equalsIgnoreCase("Select Sub Categories")) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Technical Sub Category");
            } else if (mSubCategoryIdsString.length() == 0) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Technical Sub Category");
            } else {
                if (checkInternetConnection(mContext)) {
                    if (checkAutoTimeEnabledOrNot()) {
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_MAIN_CATEGORY_NAME, mTechMainCategorySpinner.getSelectedItem().toString() + "- ");
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_MTID, "" + mMainCategoryList.get(mTechMainCategorySpinner.getSelectedItemPosition()).getMTID());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_CTID, "" + mMainCategoryList.get(mTechMainCategorySpinner.getSelectedItemPosition()).getCTID());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_SCTID, mSubCategoryIdsString.toString());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME, mTechSubCategoryTextView.getText().toString());
                        //   Log.e("temporary", "mSubCategoryIdsString " + mSubCategoryIdsString);
                        switch (mTechMainCategorySpinner.getSelectedItemPosition()) {
                            /*Technical Genetic Purity*/
                            case 1: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTGeneticPurity.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Technical Germination*/
                            case 2: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTGermination.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Technical Physical Purity*/
                            case 3: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTPhysicalPurity.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Technical Seed Health*/
                            case 4: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTSeedHealth.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Poor Grading*/
                            case 5: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTPoorGrading.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Crop performance*/
                            case 6: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTCropPerfmnce.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                            /*Rain/water damaged seeds*/
                            case 7: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTRainDamage.class);
                                startActivity(intent);
                                finish();
                            }
                            break;

                            /*Packing material Quality*/
                            case 8: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTMtrlQuality.class);
                                startActivity(intent);
                                finish();
                            }
                            break;

                            /*Technical Physical Appearance*/
                            case 9: {
                                setEnable(false);
                                Intent intent = new Intent(mContext, CCFTPhysicalAppear.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        }
                    } else {
                        showAutomaticTimeMessage(mContext, "Please update time setting to automatic");
                    }
                } else {
                    showInternetDialog(mContext, getString(R.string.ccf_err_internet));
                }
            }
        } else if (view == mOperationalProceedBtn) {
            if (mOprMainCategorySpinner.getSelectedItemPosition() == 0 ||
                    mOprMainCategorySpinner.getSelectedItemPosition() == -1) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Operational Main Feedback Category");
            } else if (mOprnlSubCategoryTextView.getText().toString().trim().equalsIgnoreCase("Select Sub Categories")) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Technical Sub Category");
            } else if (mSubCategoryIdsString.length() == 0) {
                showToast(CCFComplaintTypesActivity.this, "Please Select Operational Sub Category");
            } else {
                if (checkInternetConnection(mContext)) {
                    if (checkAutoTimeEnabledOrNot()) {
                        setEnable(false);
                     //   Log.e("temporary", "mSubCategoryIdsString " + mSubCategoryIdsString);
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_CTID, "" + mOperationalMainCategoryList.get(mOprMainCategorySpinner.getSelectedItemPosition()).getCTID());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_MTID, "" + mOperationalMainCategoryList.get(mOprMainCategorySpinner.getSelectedItemPosition()).getMTID());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_MAIN_CATEGORY_NAME, mOprMainCategorySpinner.getSelectedItem().toString() + "- ");
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_SCTID, mSubCategoryIdsString.toString());
                        CCFStoreData.putString(mContext, CCFConstantValues.CCF_SUB_CATEGORY_NAME, mOprnlSubCategoryTextView.getText().toString());
                        Intent intent = new Intent(mContext, CCFOperCmplntActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showAutomaticTimeMessage(mContext, "Please update time setting to automatic");
                    }
                } else {
                    showInternetDialog(mContext, getString(R.string.ccf_err_internet));
                }
            }
        }
    }


    private void callFirstActivity() {
        Intent intent = new Intent(mContext, CCFFirstActivity.class);
        startActivity(intent);
        finish();
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
        if (mCcfCategoryPresenter != null) {
            mCcfCategoryPresenter.destroy();
        }
        mCcfCategoryPresenter = null;

        if (response.isSuccessful()) {
            CCFCategoryPojoModel ccfCategoryResponse = (CCFCategoryPojoModel) response.body();
            if (ccfCategoryResponse != null) {
                if (ccfCategoryResponse.getSuccess()) {
                    if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists() != null && ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().size() > 0) {
                        if (ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists() != null && ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().size() > 0) {

                            mTempSubCategoryList = ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists();

                            for (int i = 0; i < ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().size(); i++) {
                                if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i).getMTID() == 2) {
                                    mOperationalMainCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i));
                                } else if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i).getMTID() != 2) {
                                    mMainCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i));
                                }
                            }
                            for (int i = 0; i < ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().size(); i++) {
                                if (ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().get(i).getCTID() == 10) {
                                    mOperationalSubCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().get(i));
                                }
                            }

                            CCFDataPreferences.storeDataArrayList3(ccfCategoryResponse, mContext);
                            CCFDataPreferences.putDataIsStoredString(mContext, "Yes");

                        } else {
                            showInternetDialog(mContext, ccfCategoryResponse.getMessage());
                        }
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
           // showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            /*01/6/2023 commented ended here*/
            /*01/6/2023 added*/
            if(response.code() == 401) {
                showInternetDialog(mContext, getString(R.string.ccf_session_expired));
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
            /*01/6/2023 added ended here*/
        }
        setAdapters();
    }

    private void setAdapters() {
        CCFMainCategoryList mainCategoryList = new CCFMainCategoryList(0, "Select Main Feedback Category", 0);

        mMainCategoryList.add(0, mainCategoryList);
        mOperationalMainCategoryList.add(0, mainCategoryList);

        setTechMainAdapter();
        setTechSubAdapter();

        setOperationalMainAdapter();
        setOperationalSubAdapter();
    }

    private void getStoredCategoryLocalData() {
        Gson g = new Gson();
        CCFCategoryPojoModel ccfCategoryResponse = g.fromJson(CCFDataPreferences.getCategoryString(mContext), CCFCategoryPojoModel.class);
        if (ccfCategoryResponse != null) {
            if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists() != null && ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().size() > 0) {
                if (ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists() != null && ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().size() > 0) {
                    mTempSubCategoryList = ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists();
                    for (int i = 0; i < ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().size(); i++) {
                        if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i).getMTID() == 2) {
                            mOperationalMainCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i));
                        } else if (ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i).getMTID() != 2) {
                            mMainCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getMainCategoryLists().get(i));
                        }
                    }
                    for (int i = 0; i < ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().size(); i++) {
                        if (ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().get(i).getCTID() == 10) {
                            mOperationalSubCategoryList.add(ccfCategoryResponse.getCategoryResponseList().getSubCategoryLists().get(i));
                        }
                    }
                } else {
                    showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
                }
            } else {
                showInternetDialog(mContext, getString(R.string.ccf_went_wrong));
            }
        }
        setAdapters();
    }


    @Override
    public void failResponse(Throwable t) {
    }

    private void setTechMainAdapter() {
        ArrayAdapter<CCFMainCategoryList> ad
                = new ArrayAdapter<>(
                CCFComplaintTypesActivity.this,
                android.R.layout.simple_spinner_item,
                mMainCategoryList);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        mTechMainCategorySpinner.setAdapter(ad);
    }

    private void setTechSubAdapter() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        if (categoryModelArrayList != null) {
            categoryModelArrayList.clear();
        }

        for (int i = 0; i < mSubCategoryList.size(); i++) {
            CCFCategoryTypeModel categoryModel = new CCFCategoryTypeModel();
            categoryModel.setSCTID("");
            categoryModel.setSelected(false);
            categoryModelArrayList.add(categoryModel);
        }

        mSubCategoryIdsString = null;
        mTechSubCategoryTextView.setText(getString(R.string.ccf_select_sub_cateroies));
    }

    private void techSubCategoryCheckBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.ccf_select_sub_cateroies));

        String[] animals = new String[mSubCategoryList.size()];
        boolean[] checkedItems = new boolean[categoryModelArrayList.size()];

        for (int i = 0; i < mSubCategoryList.size(); i++) {
            animals[i] = mSubCategoryList.get(i).getSCTDesc();
        }

        for (int i = 0; i < mSubCategoryList.size(); i++) {
            checkedItems[i] = categoryModelArrayList.get(i).isSelected();
        }

        builder.setMultiChoiceItems(animals, checkedItems, (dialog, which, isChecked) -> {
            categoryModelArrayList.get(which).setSelected(isChecked);
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (categoryModelArrayList.get(which).isSelected()) {
                editor.putString("" + which, mSubCategoryList.get(which).getSCTID() + ",");
            } else {
                editor.remove("" + which);
            }
            editor.apply();
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            StringBuilder subCategoryName = new StringBuilder();
            mSubCategoryIdsString = new StringBuilder();
            for (int i = 0; i < mSubCategoryList.size(); i++) {
                if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                    subCategoryName.append(mSubCategoryList.get(i).getSCTDesc()).append("/");
                    if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                        mSubCategoryIdsString.append(sharedPreferences.getString("" + i, ""));
                    }
                }
            }

            if (mSubCategoryIdsString.length() != 0 && mSubCategoryIdsString.substring(mSubCategoryIdsString.length() - 1).contains(",")) {
                mSubCategoryIdsString = mSubCategoryIdsString.deleteCharAt(mSubCategoryIdsString.length() - 1);
            }

            if (subCategoryName.length() != 0 && subCategoryName.substring(subCategoryName.length() - 1).contains("/")) {
                subCategoryName.deleteCharAt(subCategoryName.length() - 1);
            }

            if (!subCategoryName.toString().equals("")) {
                mTechSubCategoryTextView.setText(subCategoryName.toString());
            } else {
                mTechSubCategoryTextView.setText(getString(R.string.ccf_select_sub_cateroies));
            }
            dialog.dismiss();
        });
        /*builder.setNegativeButton("Cancel", (dialog, which) -> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            String subCategoryName = "";
            mSubCategoryIdsString = "";
            for (int i = 0; i < mSubCategoryList.size(); i++) {
                if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                    subCategoryName += mSubCategoryList.get(i).getSCTDesc() + "/";
                    if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                        mSubCategoryIdsString += sharedPreferences.getString("" + i, "");
                    }
                }
            }
            if (!subCategoryName.equals("")) {
                mTechSubCategoryTextView.setText(subCategoryName);
            } else {
                mTechSubCategoryTextView.setText("Select Sub Categories");
            }
            dialog.dismiss();
        });*/
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void setOperationalMainAdapter() {
        ArrayAdapter<CCFMainCategoryList> ad
                = new ArrayAdapter<>(
                CCFComplaintTypesActivity.this,
                android.R.layout.simple_spinner_item,
                mOperationalMainCategoryList);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        mOprMainCategorySpinner.setAdapter(ad);
    }

    private void setOperationalSubAdapter() {
        /*ArrayAdapter<CCFSubCategoryList> ad
                = new ArrayAdapter<>(
                CCFComplaintTypesActivity.this,
                android.R.layout.simple_spinner_item,
                mOperationalSubCategoryList);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        mOprSubCategorySpinner.setAdapter(ad);*/

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        if (categoryModelArrayList != null) {
            categoryModelArrayList.clear();
        }

        for (int i = 0; i < mOperationalSubCategoryList.size(); i++) {
            CCFCategoryTypeModel categoryModel = new CCFCategoryTypeModel();
            categoryModel.setSCTID("");
            categoryModel.setSelected(false);
            categoryModelArrayList.add(categoryModel);
        }

        mSubCategoryIdsString = null;
        mOprnlSubCategoryTextView.setText(getString(R.string.ccf_select_sub_cateroies));
    }

    private void oprnlSubCategoryCheckBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.ccf_select_sub_cateroies));

        String[] animals = new String[mOperationalSubCategoryList.size()];
        boolean[] checkedItems = new boolean[categoryModelArrayList.size()];

        for (int i = 0; i < mOperationalSubCategoryList.size(); i++) {
            animals[i] = mOperationalSubCategoryList.get(i).getSCTDesc();
        }

        for (int i = 0; i < mOperationalSubCategoryList.size(); i++) {
            checkedItems[i] = categoryModelArrayList.get(i).isSelected();
        }

        builder.setMultiChoiceItems(animals, checkedItems, (dialog, which, isChecked) -> {
            categoryModelArrayList.get(which).setSelected(isChecked);
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (categoryModelArrayList.get(which).isSelected()) {
                editor.putString("" + which, mOperationalSubCategoryList.get(which).getSCTID() + ",");
            } else {
                editor.remove("" + which);
            }
            editor.apply();
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            StringBuilder subCategoryName = new StringBuilder();
            mSubCategoryIdsString = new StringBuilder();
            for (int i = 0; i < mOperationalSubCategoryList.size(); i++) {
                if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                    subCategoryName.append(mOperationalSubCategoryList.get(i).getSCTDesc()).append("/");
                    if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                        mSubCategoryIdsString.append(sharedPreferences.getString("" + i, ""));
                    }
                }
            }

            if (mSubCategoryIdsString.length() != 0 && mSubCategoryIdsString.substring(mSubCategoryIdsString.length() - 1).contains(",")) {
                mSubCategoryIdsString = mSubCategoryIdsString.deleteCharAt(mSubCategoryIdsString.length() - 1);
            }

            if (subCategoryName.length() != 0 && subCategoryName.substring(subCategoryName.length() - 1).contains("/")) {
                subCategoryName.deleteCharAt(subCategoryName.length() - 1);
            }

            if (!subCategoryName.toString().equals("")) {
                mOprnlSubCategoryTextView.setText(subCategoryName.toString());
            } else {
                mOprnlSubCategoryTextView.setText(getString(R.string.ccf_select_sub_cateroies));
            }
            dialog.dismiss();
        });
        /*builder.setNegativeButton("Cancel", (dialog, which) -> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
            String subCategoryName = "";
            mSubCategoryIdsString = "";
            for (int i = 0; i < mOperationalSubCategoryList.size(); i++) {
                if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                    subCategoryName += mOperationalSubCategoryList.get(i).getSCTDesc() + "/ ";
                    if (!Objects.equals(sharedPreferences.getString("" + i, ""), "")) {
                        mSubCategoryIdsString += sharedPreferences.getString("" + i, "");
                    }
                }
            }
            if (!subCategoryName.equals("")) {
                mOprnlSubCategoryTextView.setText(subCategoryName);
            } else {
                mOprnlSubCategoryTextView.setText("Select Sub Categories");
            }
        });*/
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    @Override
    protected void onRestart() {
        setEnable(true);
        super.onRestart();
    }

    private void setEnable(Boolean setEnable) {
        mBackImageView.setEnabled(setEnable);
        mOperationalProceedBtn.setEnabled(setEnable);
        mTechnicalProceedBtn.setEnabled(setEnable);
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(mContext, FirstActivity.class);
        startActivity(intent);
        finish();*/
        callFirstActivity();
        /* callAppActivity();*/
    }

    @Override
    protected void onDestroy() {
        if (mCcfCategoryPresenter != null) {
            mCcfCategoryPresenter.destroy();
        }

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        hideKeyboard(mContext);
        hideProgressDialog();
        dismissDialog();
        super.onDestroy();
    }
}
