package com.mahyco.customercomplaint.ccfspinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CCFSearchSpinnerAdapter extends ArrayAdapter<Object> implements Filterable {
    List<Object> modelValues;
    private List<Object> mOriginalValues;

    public CCFSearchSpinnerAdapter(Context context, int resource, List<Object> items) {
        super(context, resource, items);
        this.modelValues = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        return v;
    }

    public Object getItem(int position) {
        return modelValues.get(position);
    }

    @Override
    public int getCount() {
        return modelValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modelValues = (List<Object>) results.values; // has
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                List<Object> FilteredArrList = new ArrayList<Object>();
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Object>(modelValues); // saves
                }
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (Object s : mOriginalValues)
                        if (s.toString().toUpperCase().contains(constraint.toString().toUpperCase()))
                            FilteredArrList.add(s);
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);
        return v;
    }
    /*extends ArrayAdapter<String> implements Filterable {
    List<String> modelValues;
    private List<String> mOriginalValues;

    public CCFSearchSpinnerAdapter(Context context, int resource, List<String> items) {

        super(context, resource, items);
        this.modelValues = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        return v;
    }

    public String getItem(int position) {
        return modelValues.get(position);
    }

    @Override
    public int getCount() {
        return modelValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modelValues = (List<String>) results.values; // has
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                List<String> FilteredArrList = new ArrayList<String>();
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<String>(modelValues); // saves
                }
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (String s : mOriginalValues)
                        if (s.toString().toUpperCase().contains(constraint.toString().toUpperCase()))
                            FilteredArrList.add(s);
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);
        return v;
    }*/

}
