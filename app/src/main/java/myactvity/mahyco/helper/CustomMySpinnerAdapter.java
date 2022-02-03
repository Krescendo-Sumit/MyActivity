package myactvity.mahyco.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import myactvity.mahyco.R;

public class CustomMySpinnerAdapter<String> extends ArrayAdapter<String> implements Filterable{
    List<String> modelValues;
    private List<String> mOriginalValues;

    public CustomMySpinnerAdapter(Context context, int resource, List<String> items) {

        super(context, resource, items);
        this.modelValues = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Regular.ttf");
        ((TextView) v).setTypeface(externalFont);
        // ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        if (position != 1) { // we're on an even row
            ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.QRCodeBlackColor));
            //  view.setBackgroundColor(getResources(R.color.colorPrimary));
            // tv.setTextAppearance(context, R.style.SpinnerDialog);
            // tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            // tv.setTextAppearance(context, R.style.SpinnerDialog);
            // tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
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
    }

}