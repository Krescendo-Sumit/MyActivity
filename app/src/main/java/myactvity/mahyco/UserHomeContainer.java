package myactvity.mahyco;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.utils.AnimationItem;
import myactvity.mahyco.utils.GridViewAdapter1;
import myactvity.mahyco.utils.ItemOffsetDecoration;

public  class UserHomeContainer  extends Fragment {
    View parentHolder;
    RecyclerView gridView;
    private ArrayList<String> images;
    private ArrayList<String> names;
    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";

    public  String [] prgmNameList;//={this.getResources().getString(R.string.Seedplot),this.getResources().getString(R.string.unprocessseed),this.getResources().getString(R.string.mybank),this.getResources().getString(R.string.mycoupon),this.getResources().getString(R.string.quality),this.getResources().getString(R.string.forum),this.getResources().getString(R.string.sowingandharvest)};
    public  String [] ImageList;//{"actvity.png","user.png","upload.png","download.png","report.png",
    // "journey.png","retailermap.png"
    // ,"saleorder.png"};
    public  String [] ActivityName;//={"Myactivity","Username","UploadData","DownloadData","Report",
    // "MyTravel","RetailerTag",
    // "SalesOrder"
    // };

    private List fraglist;
    private List acty;
    TextView lblusername;
    SharedPreferences pref;
    private AnimationItem[] mAnimationItems;
    public Messageclass msclass;
    public UserHomeContainer() {
        // Required empty public constructor
        //Toast.makeText(this.getContext(), getResources().getString(R.string.Seedplot), Toast.LENGTH_SHORT).show();
    }
    // protected abstract int getLayoutResId();

    /**
     * Get the layout manager to use for the demo
     * @param context the context
     * @return the layout manager
     */
    //protected  RecyclerView.LayoutManager getLayoutManager(Context context);

    /**
     * Get the array of animations to choose from
     * @return the array
     */
    // protected abstract AnimationItem[] getAnimationItems();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentHolder = inflater.inflate(R.layout.fragment_user_home_container, container,
                false);
        pref = this.getActivity().getSharedPreferences("MyPref", 0);
        msclass = new Messageclass(this.getActivity());
        mAnimationItems =      new AnimationItem[] {
                new AnimationItem("Slide from bottom", R.anim.grid_layout_animation_from_bottom)};

        gridView = (RecyclerView)parentHolder.findViewById(R.id.gridView);
        gridView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lblusername=(TextView) parentHolder.findViewById(R.id.lblusername);
        // lblusername.setText("Welcome: "+sp.getString("Displayname",null));
        fraglist =new ArrayList();
        fraglist.add(new ProductDetail());
        acty =new ArrayList();
        getData();
        return parentHolder;// inflater.inflate(R.layout.fragment_product_detail, container, false);

    }
    private void getData(){
        // Toast.makeText(this.getContext(), getResources().getString(R.string.Seedplot), Toast.LENGTH_SHORT).show();

         //start

        try {
            if (pref.getString("unit", null).contains("RCTEMP"))
            {


                prgmNameList = new String[]{"MY ACTIVITY RECORDING",
                        "UPLOAD DATA", "DOWNLOAD MASTER DATA", "REPORT"

                };
                ImageList = new String[]{"actvity.png",
                        "upload.png", "download.png"
                };
                ActivityName = new String[]{"Myactivity",  "UploadData",
                        "DownloadData"
                };

            }
           //New change for Temp user login
            else {
                if (pref.getString("unit", null).contains("VCBU"))
                {
                    if (pref.getString("RoleID", null).contains("0")) // MDO Role Id
                    {
                        prgmNameList = new String[]{"MY ACTIVITY RECORDING", "MY TRAVEL",
                                "UPLOAD DATA", "DOWNLOAD MASTER DATA", "REPORT"
                                , "BCF CALL LOG", "POG", "WOW","VOFP"
                        };//Removing  "VOF", "MDO SURVEY"
                        ImageList = new String[]{"actvity.png", "journey.png",
                                "upload.png", "download.png", "report.png", "field.png",
                                "pog.png", "wow.png","actvity.png"
                        };// "voiceofcustomer.png", "mdoservey.png"
                        ActivityName = new String[]{"Myactivity", "MyTravel", "UploadData",
                                "DownloadData", "Report", "Kisan Club (MAGiK)", "POG", "WOW","VEGVOCP1"
                        }; // Removing "VOF", "MDOSURVEY"
                    } // Other Than MDO only for VCBU
                    else {

                        prgmNameList = new String[]{"MY ACTIVITY RECORDING", "MY TRAVEL",
                                "SALES ORDER", "UPLOAD DATA", "DOWNLOAD MASTER DATA", "REPORT"
                                , "BCF CALL LOG(TBM)", "IPMF", "POG", "VOF", "WOW"//,"Retailer Profiling Survey","VOFP"
                        };// Changing VOCP to Retailer Profiling Survey
                        ImageList = new String[]{"actvity.png", "journey.png",
                                "saleorder.png", "upload.png", "download.png", "report.png",
                                "field.png", "ipmf.png", "pog.png", "voiceofcustomer.png", "wow.png"//,"actvity.png","actvity.png"
                        };
                        ActivityName = new String[]{"Myactivity", "MyTravel", "SalesOrder",
                                "UploadData", "DownloadData", "Report", "BCFCALLTBM", "IPMF", "POG", "VOF",
                                "WOW"//,"VEGVOCP","VEGVOCP1"
                        };

                }
            }

            else // RCBU
            {
               if (pref.getString("RoleID", null).contains("0")) // MDO Role Id
               {
                   prgmNameList = new String[]{
                           "UPLOAD DATA", "DOWNLOAD MASTER DATA",
                           "MY TRAVEL & ACTIVITY RECORDING",
                           "TRADE MAPPING & TAGGING "
                           ,"SALES ORDER" ,"MFDC","MDO SURVEY",
                           "REPORT","POG","HDPS\\NEW PRODUCT","SAMRUDDHA KISAN VALIDATION" //,"IPMF"//,"DAS"//,"SAMRUDDHA KISAN VALIDATION"

                   };
                   ImageList = new String[]{
                           "upload.png", "download.png",
                           "journey.png", "retailermap.png"
                           , "saleorder.png", "discount.png",
                           "mdoservey.png" ,
                           "report.png","pog.png","discount.png","field.png"//,"ipmf.png"///, "voiceofcustomer.png"// ,"field.png"

                   };
                   ActivityName = new String[]{
                           "UploadData", "DownloadData",
                           "MyTravel", "RetailerTag",
                           "SalesOrder", "COUPON","MDOSURVEY",
                           "Report","POG","HDPSCouponDashboardActivity","samruddhakisanvalidation"//"IPMF"//,"DAS"//,"samruddhakisanvalidation"

                   };
               }
                else {
                   prgmNameList = new String[]{//"MY ACTIVITY RECORDING" ,//"FIELD VISITS(Demo/Model/Jumbo/Farmer Field)",
                           "UPLOAD DATA", "DOWNLOAD MASTER DATA",//"MY ACTIVITY RECORDING",
                           "MY TRAVEL & ACTIVITY RECORDING", "TRADE MAPPING & TAGGING "
                           ,"SALES ORDER" , "MFDC", "DAS",
                           "REPORT","POG","IPMF","VCP","HDPS\\NEW PRODUCT","SAMRUDDHA KISAN VALIDATION"
                           //"BOOKING AGAINST COUPON"
                           //"SAMRUDDHA KISAN REGISTRATION","JUMBO FIELD / INNOVATION DAY"//,"Voice Of Customer","POG",
                           //"Retailer Survey" //"Kisan Club (MAGiK)"
                           //"BE-Survey(VOF)",
                           //,"BE-Survey(VCF)","SAMRUDDHA KISAN VALIDATION"
//                        "LEAVE"
   //                   ,"Sales Service"
                   };
                   ImageList = new String[]{//"field.png",
                           // "field.png" ,
                           "upload.png", "download.png", //"field.png",
                           "journey.png", "retailermap.png"
                           ,"saleorder.png","discount.png", "voiceofcustomer.png"
                           //,"user.png","innovation.png"//," ,"pog.png",voiceofcustomer.png"
                           , "report.png"//, "field.png"//,"field.png"
                           , "pog.png","ipmf.png","vcp.png","discount.png" ,"field.png"
                   };
//               ,"leave.png"
                   ActivityName = new String[]{//"Myactivity"//,"FIELD VISITS(Demo/Model/Jumbo/Farmer Feild)"
                           "UploadData", "DownloadData",//"MY ACTIVITY RECORDING",
                           "MyTravel", "RetailerTag",
                           "SalesOrder", "COUPON","DAS",
                           "Report"//,,"POG",
                           //,"Username","Innovation"
                            ,"POG","IPMF","VCP","HDPSCouponDashboardActivity","samruddhakisanvalidation"//,"websales"//,"VoiceOfCustomer",
                           //"Retailer Survey","Kisan Club (MAGiK)",
                           //"BE-Survey",
                           //, "VCF","samruddhakisanvalidation"
                   };
               }
            }

            /////
            try {

                //Showing a progress dialog while our app fetches the data from url
                final ProgressDialog loading = ProgressDialog.show(this.getContext(), "Please wait...", "Fetching data...", false, false);
                loading.dismiss();
                showGrid();
                runLayoutAnimation(gridView, mAnimationItems[0]);
            }
            catch (Exception ex)
            {
                //msclass.showMessage("Please "
                msclass.showMessage("Something went wrong  ,please try  later  again ");
            }
            //
        }
        } catch (Exception ex)
        {
            //msclass.showMessage("Please "
            msclass.showMessage("Something went wrong  ,please try  later  again ");
        }
    }
            //}
         /*  prgmNameList = new String[]{//"MY ACTIVITY RECORDING" ,//"FIELD VISITS(Demo/Model/Jumbo/Farmer Field)",
                    "UPLOAD DATA", "DOWNLOAD MASTER DATA",//"MY ACTIVITY RECORDING",
                    "MY TRAVEL & ACTIVITY RECORDING", "TRADE MAPPING & TAGGING "
                    , "SALES ORDER" , "MFDC",// "DAS",
                    "REPORT"

            };
            ImageList = new String[]{//"field.png",
                    // "field.png" ,
                    "upload.png", "download.png", //"field.png",
                    "journey.png", "retailermap.png"
                    , "saleorder.png", "discount.png"//, "voiceofcustomer.png"
                    //,"user.png","innovation.png"//," ,"pog.png",voiceofcustomer.png"
                    , "report.png", "field.png"//,"field.png"
                    , "field.png" //, "field.png","field.png"
            };
//               ,"leave.png"
            ActivityName = new String[]{//"Myactivity"//,"FIELD VISITS(Demo/Model/Jumbo/Farmer Feild)"
                    "UploadData", "DownloadData",//"MY ACTIVITY RECORDING",
                    "MyTravel", "RetailerTag",
                    "SalesOrder", "COUPON", //"DAS",
                    "Report"

            };*/
        //}
        //end

        /*try {

          //Showing a progress dialog while our app fetches the data from url
            final ProgressDialog loading = ProgressDialog.show(this.getContext(), "Please wait...", "Fetching data...", false, false);
            loading.dismiss();
            showGrid();
            runLayoutAnimation(gridView, mAnimationItems[0]);
        }
        catch (Exception ex)
        {
            //msclass.showMessage("Please "
        }*/

    //}
            // private void showGrid(JSONArray jsonArray){
    private void showGrid(){
        //Adding adapter to gridview
        try { final Context context = gridView.getContext();
            final int spacing = 4;//getResources().getDimensionPixelOffset(R.dimen.default_spacing_small);
            // Toast.makeText(this.getContext(), "trest", Toast.LENGTH_SHORT).show();
            gridView.setAdapter(null);
            gridView.setAdapter(new GridViewAdapter1(this.getContext(),
                    prgmNameList, ImageList,ActivityName, "UserHome",fraglist)
            );//,prgmImages));
            gridView.addItemDecoration(new ItemOffsetDecoration(spacing));

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        try {
            final Context context = recyclerView.getContext();

            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom);

            recyclerView.setLayoutAnimation(controller);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
