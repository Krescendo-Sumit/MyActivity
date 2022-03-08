package myactvity.mahyco.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public  class MySpinnerAdapter<String> extends ArrayAdapter<String> {
    // Initialise custom font, for example:
   // Typeface font = Typeface.createFromAsset(getContext().getAssets(),"fonts/Blambot.otf");

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public MySpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*TextView view = (TextView) super.getView(position, convertView, parent);
        Utility.setRegularFont(view,getContext());
        //view.setTypeface(font);
        return view;*/

        View v = super.getView(position, convertView, parent);




        Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Regular.ttf");


        ((TextView) v).setTypeface(externalFont);
        return v;

    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v =super.getDropDownView(position, convertView, parent);




        Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Regular.ttf");


        ((TextView) v).setTypeface(externalFont);




        return v;
    }

}