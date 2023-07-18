package com.mahyco.customercomplaint.ccfcmplnttype;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mahyco.customercomplaint.CCFConstantValues;
import com.mahyco.customercomplaint.R;
import com.mahyco.customercomplaint.ccfnetwork.CCFSubCategoryList;

import java.util.ArrayList;
import java.util.List;

public class CCFCategoryMyAdapter extends ArrayAdapter<CCFSubCategoryList> {

    private final Context mContext;
    private final ArrayList<CCFSubCategoryList> mSubCategoryLists;
    private boolean isFromView = false;

    private final ArrayList<CCFCategoryTypeModel> mCategoryModelList;

    public CCFCategoryMyAdapter(Context context, int resource, List<CCFSubCategoryList> objects, List<CCFCategoryTypeModel> object) {
        super(context, resource, objects);
        this.mContext = context;
        this.mSubCategoryLists = (ArrayList<CCFSubCategoryList>) objects;
        this.mCategoryModelList = (ArrayList<CCFCategoryTypeModel>) object;
    }

    @Override
    public int getCount() {
        return mSubCategoryLists.size();
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.ccf_spinner_list, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(mSubCategoryLists.get(position).getSCTDesc());
        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(mCategoryModelList.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // int getPosition = (Integer) buttonView.getTag();
            if (!isFromView) {
                mCategoryModelList.get(position).setSelected(isChecked);
               // Log.e("temporary", "" + mSubCategoryLists.get(position).getSCTDesc() +" position "+ position);
               // Log.e("temporary", "check " + isFromView);
                if (mCategoryModelList.get(position).isSelected()) {
                  //  Log.e("temporary", "You are beast " + mSubCategoryLists.get(position).getSCTID() + ",");
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(""+position, mSubCategoryLists.get(position).getSCTID() + ",");
                    editor.apply();
                } else {
                   // Log.e("temporary", "You are Nut");
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(CCFConstantValues.CCF_SUBCATEGORY_IDS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(""+position);
                    editor.apply();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        public CheckBox mCheckBox;
    }
}
