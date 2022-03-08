package myactvity.mahyco.helper;

/**
 * Created by 97260828 on 12/17/2017.
 */

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import myactvity.mahyco.R;
import com.squareup.picasso.Picasso;


public class MyCustomPagerAdapter extends PagerAdapter{
    Context context;
    int images1[];
    String images[];
    LayoutInflater layoutInflater;


    //public MyCustomPagerAdapter(Context context, int images[]) {
     public MyCustomPagerAdapter(Context context, String images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.slideimage, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);
       // Picasso.with(context).load(images[position]).fit() // resizes the image to these dimensions (in pixel)
       //         .centerCrop()
        //        .into(imageView);
        Picasso.with(context).load("https://farm.mahyco.com/DashboardImages/" + images[position]).fit() // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(imageView);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}