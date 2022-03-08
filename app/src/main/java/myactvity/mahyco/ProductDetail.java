package myactvity.mahyco;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
//import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class ProductDetail extends Fragment {
    View parentHolder;
    MyGridView gridView,gridViewVegeteble,gridViewRowCrop;
    private ArrayList<String> images;
    private ArrayList<String> names;
    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public  String [] prgmNameList;//={"Cotton"};
    public  String [] ImageList={"Cotton.jpg"};
    public  String [] ActivityName1={"Cotton"};

    public  String [] VegList;//={"Bhendi","bitter gourd","Cauliflower","Ridgourd","Watermelon","Bottlegoud","Brinjal","Cabbage","Chilli","Tomato"};
    public  String [] vegimageList={"Bhendi.jpg","BitterGaurd.jpg","Cauliflower.jpg","RIDGOURD.jpg","WATERMELON.jpg","Bottlegoud.jpg","Brinjal.jpg","CABBAGE.jpg","Chilli.jpg","Tomato.jpg"};
    public  String [] ActivityName2={"Bhendi","BitterGaurd","Cauliflower","RIDGOURD","WATERMELON","Bottlegoud","Brinjal","CABBAGE","Chilli","Tomato"};

    public  String [] rowList;//={"Maize","Bajra Hybrid","Black Mustard","Castor Hybrid","Jawar Hybrid"};
    public  String [] rowimageList={"Maize.jpg","bajra.jpg","black-mustard.jpg","mustard.jpg","castor.jpg","jowar.jpg"};

    private  List  fraglist;
    public  String [] ActivityName3={"Maize","bajra","black-mustard","mustard","castor","jowar"};
   // fraglist
   // public  Fragment [] fraglist={new ProductDetail(),new maincontainer()};


    public ProductDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentHolder = inflater.inflate(R.layout.fragment_product_detail, container,
                false);
        // Inflate the layout for this fragment
        gridView = (MyGridView)parentHolder.findViewById(R.id.gridView);
        gridViewVegeteble = (MyGridView)parentHolder.findViewById(R.id.gridViewVegeteble);
        gridViewRowCrop = (MyGridView)parentHolder.findViewById(R.id.gridViewRowCrop);
        images = new ArrayList<>();
        names = new ArrayList<>();
        fraglist =new ArrayList();
        fraglist.add(new ProductDetail());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Product List");
        getData();
        return parentHolder;// inflater.inflate(R.layout.fragment_product_detail, container, false);

    }

    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this.getContext(), "Please wait...","Fetching data...",false,false);


                loading.dismiss();
                       // showGrid(response);
                showGrid();

    }
    //private void showGrid(JSONArray jsonArray){
        private void showGrid(){
          //Adding adapter to gridview
            try {

                //prgmNameList=new String[]{getResources().getStringArray(R.array.cottonlist)};
                prgmNameList=getResources().getStringArray(R.array.cottonlist);
                VegList=getResources().getStringArray(R.array.Vegetablelist);
                rowList=getResources().getStringArray(R.array.Rowcroplist);


                gridView.setAdapter(new GridViewAdapter(this.getContext(), prgmNameList, ImageList,ActivityName1, "ProductCat",fraglist));//,prgmImages));
                gridViewVegeteble.setAdapter(new GridViewAdapter(this.getContext(), VegList, vegimageList,ActivityName2, "ProductCat",fraglist));//,prgmImages));
                gridViewRowCrop.setAdapter(new GridViewAdapter(this.getContext(), rowList, rowimageList,ActivityName3, "ProductCat",fraglist));//,prgmImages));
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    public void backButtonWasPressed() {
        Toast.makeText(this.getContext(),"gg", Toast.LENGTH_SHORT).show();
    }
    public void onButtonPressed(Uri uri) {
        Toast.makeText(this.getContext(),"gg", Toast.LENGTH_SHORT).show();
        // if (mListener != null) {
        // mListener.onFragmentInteraction(uri);
        // }
    }
}
