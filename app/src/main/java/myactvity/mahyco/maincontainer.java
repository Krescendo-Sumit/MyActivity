package myactvity.mahyco;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class maincontainer extends Fragment {
    View parentHolder;
    GridView gridView;
    private ArrayList<String> images;
    private ArrayList<String> names;
    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public  String [] prgmNameList;//={"Mahyco Product","Mahyco Farmer Login","Weather","Crop Guideline","MarketRate","Retailer","App Information"};
    public  String [] ImageList={"ProductIcon.png","MahycoFarmer.png","Weather.png","coupon.png","fieldarea.jpg","CropGuideline.jpg","MarketRate.png","Retailer.png","AppInformation.png"}; //,"farmerdicussion.png"
    private List fraglist;
    public  String [] ActivityName={"ProductList","Login","Weather","Coupon","FieldArea","GuideLine","MarketRate","Retailer","About"}; //,"FarmerDiscussion"

    public maincontainer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentHolder = inflater.inflate(R.layout.fragment_maincontainer, container,
                false);
        // Inflate the layout for this fragment
        gridView = (GridView)parentHolder.findViewById(R.id.gridView);
        images = new ArrayList<>();
        names = new ArrayList<>();
        fraglist =new ArrayList();
        fraglist.add(new ProductDetail());
        //Calling the getData method
        getData();
        return parentHolder;// inflater.inflate(R.layout.fragment_product_detail, container, false);
    }
    private void getData(){
        prgmNameList=getResources().getStringArray(R.array.dashboard);
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this.getContext(), "Please wait...","Fetching data...",false,false);
        loading.dismiss();
        showGrid();

    }
    private void showGrid(){
        gridView.setAdapter(new GridViewAdapter(this.getContext(), prgmNameList,ImageList,ActivityName,"mainContain",fraglist));//,prgmImages));

    }

}