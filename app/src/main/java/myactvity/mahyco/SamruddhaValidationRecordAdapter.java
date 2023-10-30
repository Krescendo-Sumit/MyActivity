package myactvity.mahyco;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;


import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.app.HttpUtils;
import myactvity.mahyco.app.MultiSelectionSpinner;
import myactvity.mahyco.app.Prefs;
import myactvity.mahyco.helper.SamruddhaKisanModel;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.myActivityRecording.preSeasonActivity.CropSeminarActivity;

public class SamruddhaValidationRecordAdapter extends RecyclerView.Adapter<SamruddhaValidationRecordAdapter.SamruddhaValidateRecord> {


    private Context context;
    private List<SamruddhaKisanModel> mlist;
    String[] statusyArray;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;
    SqliteDatabase mDatabase;
    Config config;
    Dialog dialog;
    Prefs mPref;
    //String SERVER = "https://packhouse.mahyco.com/api/generalactivity/updateSamruddhaKisanValidation";
    String SERVER = "https://packhouse.mahyco.com/api/generalactivity/samruddhaKisanValidation";
    //test URL
    //String SERVER = "http://10.80.50.153/MAAPackHouseTest/api/generalactivity/samruddhaKisanValidation";
  //  String SERVER_IMAGE = "http://10.80.50.153/MAAPackHouseTest/UploadSamfuddhaKisanFarmerPhoto/";
    String SERVER_IMAGE = "https://packhouse.mahyco.com/UploadSamfuddhaKisanFarmerPhoto/";

    int mYear, mMonth, mDay;

    public SamruddhaValidationRecordAdapter(Context context, List<SamruddhaKisanModel> mlist, SqliteDatabase mDatabase) {
        this.context = context;
        config = new Config(context); //Here the context is passing
        mPref = Prefs.with(context);
        this.mlist = mlist;
        this.mDatabase = mDatabase;
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        setHasStableIds(true);

    }


    @NonNull
    @Override
    public SamruddhaValidationRecordAdapter.SamruddhaValidateRecord onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_samruddha_record_list, null);

        return new SamruddhaValidationRecordAdapter.SamruddhaValidateRecord(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final SamruddhaValidationRecordAdapter.SamruddhaValidateRecord demoModelViewHolder, final int i) {

        if (mlist.size() > 0) {
            final SamruddhaKisanModel samruddhaKisanModel = mlist.get(i);




            demoModelViewHolder.et_dob.setEnabled(false);
            demoModelViewHolder.et_dob.setClickable(false);
            demoModelViewHolder.et_annivaesarydate.setEnabled(false);
            demoModelViewHolder.et_annivaesarydate.setClickable(false);
            demoModelViewHolder.et_landmark.setEnabled(false);
            demoModelViewHolder.et_pincode.setEnabled(false);
            demoModelViewHolder.et_currentlocation.setEnabled(false);
            demoModelViewHolder.txt_capure_image.setEnabled(false);
            demoModelViewHolder.txt_getlocation.setEnabled(false);
            demoModelViewHolder.txt_getlocation.setClickable(false);


            demoModelViewHolder.et_dob.setBackgroundResource(R.drawable.no_border_line);

            demoModelViewHolder.et_annivaesarydate.setBackgroundResource(R.drawable.no_border_line);
            demoModelViewHolder.et_landmark.setBackgroundResource(R.drawable.no_border_line);
            demoModelViewHolder.et_pincode.setBackgroundResource(R.drawable.no_border_line);
            demoModelViewHolder.et_currentlocation.setBackgroundResource(R.drawable.no_border_line);
            demoModelViewHolder.txt_capure_image.setBackgroundResource(R.drawable.no_border_line);









            demoModelViewHolder.txtFarmerName.setText(samruddhaKisanModel.getFarmerName());
            demoModelViewHolder.txtMobileNum.setText(samruddhaKisanModel.getMobileNumber());
            demoModelViewHolder.txtWANum.setText(samruddhaKisanModel.getWhatsappNumber());

            demoModelViewHolder.txtMDO.setText(samruddhaKisanModel.getMdoDesc());

           /* if(!samruddhaKisanModel.getDistrict().isEmpty()){
                demoModelViewHolder.txtDist.setText(samruddhaKisanModel.getDistrict());
            }else {
                demoModelViewHolder.txtDist.setText("NA");
            }

            if(!samruddhaKisanModel.getTaluka().isEmpty()){
                demoModelViewHolder.txtTaluka.setText(samruddhaKisanModel.getTaluka());
            }else {
                demoModelViewHolder.txtTaluka.setText("NA");
            }

            if(!samruddhaKisanModel.getVillage().isEmpty()){
                demoModelViewHolder.txtVillage.setText(samruddhaKisanModel.getVillage());
            }else {
                demoModelViewHolder.txtVillage.setText("NA");
            }

            */
            demoModelViewHolder.txtDist.setText(samruddhaKisanModel.getDistrict());
            demoModelViewHolder.txtTaluka.setText(samruddhaKisanModel.getTaluka());
            demoModelViewHolder.txtVillage.setText(samruddhaKisanModel.getVillage());
            demoModelViewHolder.txtLand.setText(samruddhaKisanModel.getTotalLand());
            demoModelViewHolder.txtCrop.setText(samruddhaKisanModel.getCrop());
            samruddhaKisanModel.setStr_aniversarydate(samruddhaKisanModel.getFarmer_anniversarydate());
            samruddhaKisanModel.setStr_dob(samruddhaKisanModel.getFarmer_dob());

            String finalDate = ConvertDateFormat(samruddhaKisanModel.getEntryDt());
            demoModelViewHolder.txtDate.setText(finalDate);


            if (samruddhaKisanModel.getTaggedAddress().contains("ADDRESS :")) {
                String[] address = samruddhaKisanModel.getTaggedAddress().split("ADDRESS :");
                demoModelViewHolder.txtGeoTag.setText(address[1]);
            } else {
                demoModelViewHolder.txtGeoTag.setText(samruddhaKisanModel.getTaggedAddress());
            }


            switch (samruddhaKisanModel.getAction()) {
                case "PENDING":
                default:
                    demoModelViewHolder.btnStatus.setText("ACCEPT/REJECT");
                    demoModelViewHolder.btnStatus.setBackgroundResource(R.drawable.gradient_error);
                    break;
                case "APPROVE":
                    demoModelViewHolder.btnStatus.setText("APPROVED");
                    demoModelViewHolder.btnStatus.setBackgroundResource(R.drawable.btn_shape);
                    break;
                case "REJECT":
                    demoModelViewHolder.btnStatus.setText("REJECTED");
                    demoModelViewHolder.btnStatus.setBackgroundResource(R.drawable.gradient_error);
                    break;
            }
          //  pref = context.getSharedPreferences("MyPref", 0);
            if(pref.getString("RoleID", null).contains("0")) {
                demoModelViewHolder.btnStatus.setVisibility(View.GONE);
            }
            demoModelViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setVisibleEditField(demoModelViewHolder);

                }
            });

            demoModelViewHolder.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (demoModelViewHolder.et_dob.getText().toString().trim().equals("")) {
                        demoModelViewHolder.et_dob.setError("Invalid Date");
                        Toast.makeText(context, "Choose Birth Date.", Toast.LENGTH_SHORT).show();
                    } else if (demoModelViewHolder.et_pincode.length() < 6) {
                        demoModelViewHolder.et_pincode.setError("Pincode must be 6 digits");
                    } else {
                        setVisibleTextField(demoModelViewHolder);
                        mlist.get(i).setFarmerName(demoModelViewHolder.edFarmerName.getText().toString());
                        mlist.get(i).setMobileNumber(demoModelViewHolder.edMobileNum.getText().toString());
                        mlist.get(i).setWhatsappNumber(demoModelViewHolder.edWANum.getText().toString());
                        mlist.get(i).setMdoDesc(demoModelViewHolder.edMDO.getText().toString());
                        mlist.get(i).setDistrict(demoModelViewHolder.edDist.getText().toString());
                        mlist.get(i).setTaluka(demoModelViewHolder.edTaluka.getText().toString());
                        mlist.get(i).setVillage(demoModelViewHolder.edVillage.getText().toString());
                        mlist.get(i).setTotalLand(demoModelViewHolder.edLand.getText().toString());
                        mlist.get(i).setCrop(demoModelViewHolder.edCrop.getText().toString());
                        mlist.get(i).setFarmer_dob(samruddhaKisanModel.getStr_dob());
                        mlist.get(i).setFarmer_anniversarydate(samruddhaKisanModel.getStr_aniversarydate());
                        mlist.get(i).setFarmer_landmark(demoModelViewHolder.et_landmark.getText().toString());
                        mlist.get(i).setFarmer_house_address(demoModelViewHolder.et_currentlocation.getText().toString());
                        mlist.get(i).setFarmer_house_latlong(samruddhaKisanModel.getFarmer_house_latlong());
                        mlist.get(i).setFarmer_pincode(demoModelViewHolder.et_pincode.getText().toString());
                        //   String serverDate =  ConvertToServerDateFormat(demoModelViewHolder.edDate.getText().toString());
                        mlist.get(i).setEntryDt(demoModelViewHolder.edDate.getText().toString());

                        String ss = mPref.getString("CapturedImage", "");

                        mlist.get(i).setFarmer_photo_name("");
                        mlist.get(i).setFarmer_photo_path(ss);

                        //  mlist.get(i).setTaggedAddress(demoModelViewHolder.edGeoTag.getText().toString());
                        try {
                            updateToDB(mlist.get(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            demoModelViewHolder.btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDialogForApprovalForm(i);
                }
            });

            demoModelViewHolder.et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                            /*      Your code   to get date and time    */

                            String ssm = "", ssd = "";
                            if ((selectedmonth + 1) < 10)
                                ssm = "0" + (selectedmonth + 1);
                            else
                                ssm = "" + (selectedmonth + 1);
                            if ((selectedday) < 10)
                                ssd = "0" + selectedday;
                            else
                                ssd = "" + selectedday;

                            String dd = selectedyear + "-" + (ssm) + "-" + ssd+"T00:00:00" ;
                            demoModelViewHolder.et_dob.setText(dd);
                            samruddhaKisanModel.setStr_dob(dd);
                            // et_age.setText(getAge(selectedyear,selectedmonth,selectedday));
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 573948000000L);
                    mDatePicker.setTitle("Choose Farmer's Birth Date");
                    mDatePicker.show();
                }
            });

            demoModelViewHolder.et_annivaesarydate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                            /*      Your code   to get date and time    */

                            String ssm = "", ssd = "";
                            if ((selectedmonth + 1) < 10)
                                ssm = "0" + (selectedmonth + 1);
                            else
                                ssm = "" + (selectedmonth + 1);
                            if ((selectedday) < 10)
                                ssd = "0" + selectedday;
                            else
                                ssd = "" + selectedday;

                            String dd = selectedyear + "-" + (ssm) + "-" + ssd+"T00:00:00" ;
                            demoModelViewHolder.et_annivaesarydate.setText(dd);
                            samruddhaKisanModel.setStr_aniversarydate(dd);
                            // et_age.setText(getAge(selectedyear,selectedmonth,selectedday));
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.setTitle("Choose Farmer's Anniversary Date");
                    mDatePicker.show();
                }
            });
// Adding These Data in New Samrudhi Changes


            if (samruddhaKisanModel.getFarmer_photo_name() == null || samruddhaKisanModel.getFarmer_photo_name().equals("") || samruddhaKisanModel.getFarmer_photo_name().equals("null")) {
                demoModelViewHolder.txt_capure_image.setText("Take SK's Photo.");

            } else {
                demoModelViewHolder.txt_capure_image.setText("View / Take Photo");
            }
            if (samruddhaKisanModel.getFarmer_dob() != null || !samruddhaKisanModel.getFarmer_dob().trim().equals("null")) {
                try {
                    // Log.i("Time :",ConvertDateFormat(samruddhaKisanModel.getFarmer_dob()));
                    if (samruddhaKisanModel.getFarmer_dob() != null || !samruddhaKisanModel.getFarmer_dob().trim().equals("null") || !samruddhaKisanModel.getFarmer_dob().trim().equals(""))
                        demoModelViewHolder.et_dob.setText(ConvertDateFormat(samruddhaKisanModel.getFarmer_dob()) != null ? ConvertDateFormat(samruddhaKisanModel.getFarmer_dob()) : "");
                    //  demoModelViewHolder.et_dob.setText(samruddhaKisanModel.getFarmer_dob());
                } catch (Exception e) {

                }
            } else
                demoModelViewHolder.et_dob.setText("");

            if (samruddhaKisanModel.getFarmer_anniversarydate() != null || !samruddhaKisanModel.getFarmer_anniversarydate().trim().equals("null")) {
                //    demoModelViewHolder.et_annivaesarydate.setText(ConvertDateFormat(samruddhaKisanModel.getFarmer_anniversarydate()));

                if (samruddhaKisanModel.getFarmer_anniversarydate() != null || !samruddhaKisanModel.getFarmer_anniversarydate().trim().equals("null") || !samruddhaKisanModel.getFarmer_anniversarydate().trim().equals(""))
                    demoModelViewHolder.et_annivaesarydate.setText(ConvertDateFormat(samruddhaKisanModel.getFarmer_anniversarydate()) != null ? ConvertDateFormat(samruddhaKisanModel.getFarmer_anniversarydate()) : "");

            } else
                demoModelViewHolder.et_annivaesarydate.setText("");

            if (samruddhaKisanModel.getFarmer_landmark() != null || !samruddhaKisanModel.getFarmer_landmark().trim().equals("null"))
                demoModelViewHolder.et_landmark.setText(samruddhaKisanModel.getFarmer_landmark());
            else
                demoModelViewHolder.et_landmark.setText("");
            if (samruddhaKisanModel.getFarmer_pincode() != null||!samruddhaKisanModel.getFarmer_pincode().trim().equals("null"))
                demoModelViewHolder.et_pincode.setText(samruddhaKisanModel.getFarmer_pincode());
            else
                demoModelViewHolder.et_pincode.setText("");
            if (samruddhaKisanModel.getFarmer_house_address() != null||!samruddhaKisanModel.getFarmer_house_address ().trim().equals("null"))
                demoModelViewHolder.et_currentlocation.setText(samruddhaKisanModel.getFarmer_house_address());
            else
                demoModelViewHolder.et_currentlocation.setText("");

      /*    if (samruddhaKisanModel.getFarmer_photo_path() != null)
                demoModelViewHolder.txt_capure_image.setText(samruddhaKisanModel.getFarmer_photo_path());
            else
                demoModelViewHolder.txt_capure_image.setText("NA6");
      */

            demoModelViewHolder.txt_getlocation.setText(Html.fromHtml("<u>Get Location (SK's House)</u>"));
            demoModelViewHolder.txt_getlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = mPref.getString("currentLocation", "");
                    if (s != null && !(s.toString().trim().equals(""))) {
                        samruddhaKisanModel.setFarmer_house_latlong(s.toLowerCase().toString().trim());
                        String cords[] = s.split("-");
                        try {
                            s = getAddress(Double.parseDouble(cords[0].trim()), Double.parseDouble(cords[1].trim()));
                        }catch(NumberFormatException e)
                        {
                            s="";
                        }
                    }
                    demoModelViewHolder.et_currentlocation.setText("" + s);
                }
            });
            String photoPath=SERVER_IMAGE + "" + samruddhaKisanModel.getFarmer_photo_name();
            try{
                if(photoPath!=null) {
                    if( !photoPath.contains("/null")&& photoPath.contains(".jpg"))
                        Glide.with(context)
                                .load(photoPath)
                                .into(demoModelViewHolder.imgSk_Photo);
                }
            }catch(Exception e)
            {

            }

            demoModelViewHolder.txt_capure_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*       Intent intent = new Intent(context, TestImage.class);
                    //   intent.putExtra("MyClass", samruddhaKisanModel);
                    intent.putExtra("url", SERVER_IMAGE + "" + samruddhaKisanModel.getFarmer_photo_name());
                    context.startActivity(intent);*/
                    PickImageDialog.build(new PickSetup())
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    //TODO: do what you have to...
                                    demoModelViewHolder.imgSk_Photo.setImageBitmap(r.getBitmap());
                                    mPref.save("CapturedImage",mDatabase.getImageDatadetail(r.getPath()));
                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show((SamruddhaKisanValidationRecords) context);
                 }
            });

            if (samruddhaKisanModel.getFarmer_dob().contains("1900"))
                demoModelViewHolder.et_dob.setText("");

            if (samruddhaKisanModel.getFarmer_anniversarydate().contains("1900"))
                demoModelViewHolder.et_annivaesarydate.setText("");


              demoModelViewHolder.btn_savechanges.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if (demoModelViewHolder.et_dob.getText().toString().trim().equals("")) {
                          demoModelViewHolder.et_dob.setError("Invalid Date");
                          Toast.makeText(context, "Choose Birth Date.", Toast.LENGTH_SHORT).show();
                      } else if (demoModelViewHolder.et_pincode.length() < 6) {
                          demoModelViewHolder.et_pincode.setError("Pincode must be 6 digits");
                      } else {
                          setVisibleTextField(demoModelViewHolder);
                          mlist.get(i).setFarmerName(demoModelViewHolder.edFarmerName.getText().toString());
                          mlist.get(i).setMobileNumber(demoModelViewHolder.edMobileNum.getText().toString());
                          mlist.get(i).setWhatsappNumber(demoModelViewHolder.edWANum.getText().toString());
                          mlist.get(i).setMdoDesc(demoModelViewHolder.edMDO.getText().toString());
                          mlist.get(i).setDistrict(demoModelViewHolder.edDist.getText().toString());
                          mlist.get(i).setTaluka(demoModelViewHolder.edTaluka.getText().toString());
                          mlist.get(i).setVillage(demoModelViewHolder.edVillage.getText().toString());
                          mlist.get(i).setTotalLand(demoModelViewHolder.edLand.getText().toString());
                          mlist.get(i).setCrop(demoModelViewHolder.edCrop.getText().toString());
                          mlist.get(i).setFarmer_dob(samruddhaKisanModel.getStr_dob());
                          mlist.get(i).setFarmer_anniversarydate(samruddhaKisanModel.getStr_aniversarydate());
                          mlist.get(i).setFarmer_landmark(demoModelViewHolder.et_landmark.getText().toString());
                          mlist.get(i).setFarmer_house_address(demoModelViewHolder.et_currentlocation.getText().toString());
                          mlist.get(i).setFarmer_house_latlong(samruddhaKisanModel.getFarmer_house_latlong());
                          mlist.get(i).setFarmer_pincode(demoModelViewHolder.et_pincode.getText().toString());
                          //   String serverDate =  ConvertToServerDateFormat(demoModelViewHolder.edDate.getText().toString());
                          mlist.get(i).setEntryDt(demoModelViewHolder.edDate.getText().toString());

                          String ss = mPref.getString("CapturedImage", "");

                          mlist.get(i).setFarmer_photo_name("");
                          mlist.get(i).setFarmer_photo_path(ss);

                          //  mlist.get(i).setTaggedAddress(demoModelViewHolder.edGeoTag.getText().toString());
                          try {
                              updateToDB(mlist.get(i));
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  }
              });

            demoModelViewHolder.btn_savechanges.setEnabled(false);
            demoModelViewHolder.btn_savechanges.setClickable(false);
            demoModelViewHolder.btnStatus.setEnabled(true);
            demoModelViewHolder.btnStatus.setClickable(true);
        }
    }


    String getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address + "\n" + city + "\n" + state + "\n" + country + "\n" + postalCode + "\n";
        } catch (Exception e) {
            return "Not Found.";
        }
    }

    private void openDialogForApprovalForm(final int position) {


        RadioGroup radGroupActivity;
        final RadioButton radApproved, radReject;
        final LinearLayout llApproval, llRejection;
        final SearchableSpinner spRejection;
        final MultiSelectionSpinner spApproval;
        Button btnSubmit;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_approval_form);


        radGroupActivity = (RadioGroup) dialog.findViewById(R.id.radGroupActivity);
        radApproved = (RadioButton) dialog.findViewById(R.id.radApproved);
        radReject = (RadioButton) dialog.findViewById(R.id.radReject);
        llApproval = (LinearLayout) dialog.findViewById(R.id.llApproval);
        llRejection = (LinearLayout) dialog.findViewById(R.id.llRejection);
        spRejection = (SearchableSpinner) dialog.findViewById(R.id.spRejection);
        spApproval = (MultiSelectionSpinner) dialog.findViewById(R.id.spApproval);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);


        bindApprovalReasons(spApproval);
        bindRejectionReasons(spRejection);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("MyActivity");
                builder.setMessage("Are You Confirmed ?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface alertdialog, int which) {


                        if (radApproved.isChecked()) {

                            mlist.get(position).setAction("APPROVE");
                            mlist.get(position).setReasons(spApproval.getSelectedItem().toString());

                        } else if (radReject.isChecked()) {

                            mlist.get(position).setAction("REJECT");
                            mlist.get(position).setReasons(spRejection.getSelectedItem().toString());
                        }
                        try {
                            updateToDB(mlist.get(position));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface alertdialog, int which) {

                        alertdialog.dismiss();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        radGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radApproved:

                        llApproval.setVisibility(View.VISIBLE);
                        llRejection.setVisibility(View.GONE);
                        radApproved.setChecked(true);
                        radReject.setChecked(false);
                        break;
                    case R.id.radReject:
                        llApproval.setVisibility(View.GONE);
                        llRejection.setVisibility(View.VISIBLE);
                        radApproved.setChecked(false);
                        radReject.setChecked(true);
                        break;
                }
            }

        });


        spApproval.setListener(new MultiSelectionSpinner.MySpinnerListener() {

            @Override
            public void onItemClicked(int which) {

            }

            @Override
            public void selectedIndices(List<Integer> indices) {
            }

            @Override
            public void selectedStrings(List<String> strings) {

                Log.d("which selectedSt:: ", String.valueOf(strings));
            }
        });


        spRejection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        dialog.show();

    }

    private String ConvertDateFormat(String entryDt) {

        Date myDate = null;
        String finalDate = "";
        SimpleDateFormat dateFormat;

        if (entryDt != null) {

            try {

                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                myDate = dateFormat.parse(entryDt);

            } catch (Exception e) {

                try {
                    dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    myDate = dateFormat.parse(entryDt);

                } catch (ParseException e1) {

                    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        myDate = dateFormat.parse(entryDt);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy");
                finalDate = timeFormat.format(myDate);
            } catch (Exception e) {
                return "NA";
            }
        }

        return finalDate;
    }

    private String ConvertToServerDateFormat(String entryDt) {

        Date myDate = null;
        String finalDate = "";
        SimpleDateFormat dateFormat;

        if (entryDt != null) {

            try {

                dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                myDate = dateFormat.parse(entryDt);

            } catch (ParseException e) {

                try {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    myDate = dateFormat.parse(entryDt);

                } catch (ParseException e1) {

                    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        myDate = dateFormat.parse(entryDt);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            finalDate = timeFormat.format(myDate);

        }

        return finalDate;
    }


    private void updateToDB(SamruddhaKisanModel samruddhaKisanModel) throws JSONException {

        Gson gson = new Gson();
        String json = gson.toJson(samruddhaKisanModel);
        JSONObject obj = new JSONObject(json);
        try {
            Log.i("JsonUpdate:", obj.toString());

            handleDataSyncResponse("KisanValidationData", obj.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void handleDataSyncResponse(String function, String resultout) throws JSONException {
        if (function.equals("KisanValidationData")) {
            JSONObject jsonObject = new JSONObject(resultout);

            boolean isUpdate = mDatabase.updateSamruddhaKisanValidationData("0", jsonObject);
            if (isUpdate) {
                //TOdo: Send data to server for updating
                mPref.save("CapturedImage", "");
                uploadData("kisanValidationData", jsonObject);
                Log.d("updated", "updation: Done");
            }
        }
        Log.d("KisanValidationData", "KisanValidationData: " + resultout);
    }

    private void bindRejectionReasons(SearchableSpinner spRejection) {
        try {
            spRejection.setAdapter(null);
            try {
                statusyArray = context.getResources().getStringArray(R.array.rejection_reasons);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, statusyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRejection.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void bindApprovalReasons(MultiSelectionSpinner spApproval) {
        try {

            String str = null;
            try {
                statusyArray = context.getResources().getStringArray(R.array.approval_reasons);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, statusyArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spApproval.setItems(statusyArray);
                spApproval.hasNoneOption(true);
                spApproval.setSelection(new int[]{0});

            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    private void setVisibleTextField(SamruddhaValidateRecord demoModelViewHolder) {
        // mPref.save("CapturedImage","");
        demoModelViewHolder.txtFarmerName.setText(demoModelViewHolder.edFarmerName.getText());
        demoModelViewHolder.txtFarmerName.setVisibility(View.VISIBLE);
        demoModelViewHolder.edFarmerName.setVisibility(View.GONE);

        demoModelViewHolder.txtMobileNum.setVisibility(View.VISIBLE);
        demoModelViewHolder.edMobileNum.setVisibility(View.GONE);
        demoModelViewHolder.txtMobileNum.setText(demoModelViewHolder.edMobileNum.getText());


        demoModelViewHolder.txtWANum.setVisibility(View.VISIBLE);
        demoModelViewHolder.edWANum.setVisibility(View.GONE);
        demoModelViewHolder.txtWANum.setText(demoModelViewHolder.edWANum.getText());

        demoModelViewHolder.txtMDO.setVisibility(View.VISIBLE);
        demoModelViewHolder.edMDO.setVisibility(View.GONE);
        demoModelViewHolder.txtMDO.setText(demoModelViewHolder.edMDO.getText());


        demoModelViewHolder.txtDist.setVisibility(View.VISIBLE);
        demoModelViewHolder.edDist.setVisibility(View.GONE);
        demoModelViewHolder.txtDist.setText(demoModelViewHolder.edDist.getText());


        demoModelViewHolder.edTaluka.setVisibility(View.GONE);
        demoModelViewHolder.txtTaluka.setVisibility(View.VISIBLE);
        demoModelViewHolder.txtTaluka.setText(demoModelViewHolder.edTaluka.getText());

        demoModelViewHolder.edVillage.setVisibility(View.GONE);
        demoModelViewHolder.txtVillage.setVisibility(View.VISIBLE);
        demoModelViewHolder.txtVillage.setText(demoModelViewHolder.edVillage.getText());


        demoModelViewHolder.edLand.setVisibility(View.GONE);
        demoModelViewHolder.txtLand.setVisibility(View.VISIBLE);
        demoModelViewHolder.txtLand.setText(demoModelViewHolder.edLand.getText());

        demoModelViewHolder.edCrop.setVisibility(View.GONE);
        demoModelViewHolder.txtCrop.setVisibility(View.VISIBLE);
        demoModelViewHolder.txtCrop.setText(demoModelViewHolder.edCrop.getText());


        demoModelViewHolder.edDate.setVisibility(View.GONE);
        demoModelViewHolder.txtDate.setVisibility(View.VISIBLE);
        demoModelViewHolder.txtDate.setText(demoModelViewHolder.edDate.getText());

        //   demoModelViewHolder.edGeoTag.setVisibility(View.GONE);
        //   demoModelViewHolder.txtGeoTag.setVisibility(View.VISIBLE);
        // demoModelViewHolder.txtGeoTag.setText(demoModelViewHolder.edGeoTag.getText());


        demoModelViewHolder.imgSave.setVisibility(View.GONE);
        demoModelViewHolder.imgEdit.setVisibility(View.VISIBLE);

        demoModelViewHolder.et_dob.setEnabled(false);
        demoModelViewHolder.et_dob.setClickable(false);
        demoModelViewHolder.et_annivaesarydate.setEnabled(false);
        demoModelViewHolder.et_annivaesarydate.setClickable(false);
        demoModelViewHolder.et_landmark.setEnabled(false);
        demoModelViewHolder.et_pincode.setEnabled(false);
        demoModelViewHolder.et_currentlocation.setEnabled(false);
        demoModelViewHolder.txt_capure_image.setEnabled(false);
        demoModelViewHolder.txt_getlocation.setEnabled(false);
        demoModelViewHolder.txt_getlocation.setClickable(false);
        demoModelViewHolder.btn_savechanges.setEnabled(false);
        demoModelViewHolder.btn_savechanges.setClickable(false);

        demoModelViewHolder.btnStatus.setEnabled(true);
        demoModelViewHolder.btnStatus.setClickable(true);

        demoModelViewHolder.et_dob.setBackgroundResource(R.drawable.no_border_line);

        demoModelViewHolder.et_annivaesarydate.setBackgroundResource(R.drawable.no_border_line);
        demoModelViewHolder.et_landmark.setBackgroundResource(R.drawable.no_border_line);
        demoModelViewHolder.et_pincode.setBackgroundResource(R.drawable.no_border_line);
        demoModelViewHolder.et_currentlocation.setBackgroundResource(R.drawable.no_border_line);
        demoModelViewHolder.txt_capure_image.setBackgroundResource(R.drawable.no_border_line);
        //  demoModelViewHolder.txt_getlocation.setBackgroundResource(R.drawable.no_border_line);

    }

    private void setVisibleEditField(SamruddhaValidateRecord demoModelViewHolder) {

        //  mPref.save("CapturedImage","");
        demoModelViewHolder.txtFarmerName.setVisibility(View.GONE);
        demoModelViewHolder.edFarmerName.setVisibility(View.VISIBLE);
        demoModelViewHolder.edFarmerName.setText(demoModelViewHolder.txtFarmerName.getText());

        demoModelViewHolder.txtMobileNum.setVisibility(View.GONE);
        demoModelViewHolder.edMobileNum.setVisibility(View.VISIBLE);
        demoModelViewHolder.edMobileNum.setText(demoModelViewHolder.txtMobileNum.getText());


        demoModelViewHolder.txtWANum.setVisibility(View.GONE);
        demoModelViewHolder.edWANum.setVisibility(View.VISIBLE);
        demoModelViewHolder.edWANum.setText(demoModelViewHolder.txtWANum.getText());


  /*      demoModelViewHolder.txtMDO.setVisibility(View.GONE);
        demoModelViewHolder.edMDO.setVisibility(View.VISIBLE);
  */
        demoModelViewHolder.edMDO.setText(demoModelViewHolder.txtMDO.getText());


        demoModelViewHolder.txtDist.setVisibility(View.GONE);
        demoModelViewHolder.edDist.setVisibility(View.VISIBLE);
        demoModelViewHolder.edDist.setText(demoModelViewHolder.txtDist.getText());


        demoModelViewHolder.txtTaluka.setVisibility(View.GONE);
        demoModelViewHolder.edTaluka.setVisibility(View.VISIBLE);
        demoModelViewHolder.edTaluka.setText(demoModelViewHolder.txtTaluka.getText());

        demoModelViewHolder.txtVillage.setVisibility(View.GONE);
        demoModelViewHolder.edVillage.setVisibility(View.VISIBLE);
        demoModelViewHolder.edVillage.setText(demoModelViewHolder.txtVillage.getText());

        demoModelViewHolder.txtLand.setVisibility(View.GONE);
        demoModelViewHolder.edLand.setVisibility(View.VISIBLE);
        demoModelViewHolder.edLand.setText(demoModelViewHolder.txtLand.getText());

        demoModelViewHolder.txtCrop.setVisibility(View.GONE);
        demoModelViewHolder.edCrop.setVisibility(View.VISIBLE);
        demoModelViewHolder.edCrop.setText(demoModelViewHolder.txtCrop.getText());


      /*  demoModelViewHolder.txtDate.setVisibility(View.GONE);
        demoModelViewHolder.edDate.setVisibility(View.VISIBLE);*/
        demoModelViewHolder.edDate.setText(demoModelViewHolder.txtDate.getText());

        //demoModelViewHolder.txtGeoTag.setVisibility(View.GONE);
        // demoModelViewHolder.edGeoTag.setVisibility(View.VISIBLE);
        // demoModelViewHolder.edGeoTag.setText(demoModelViewHolder.txtGeoTag.getText());

        demoModelViewHolder.imgEdit.setVisibility(View.GONE);
        demoModelViewHolder.imgSave.setVisibility(View.VISIBLE);

        demoModelViewHolder.et_dob.setEnabled(true);
        demoModelViewHolder.et_dob.setClickable(true);
        demoModelViewHolder.et_annivaesarydate.setEnabled(true);
        demoModelViewHolder.et_annivaesarydate.setClickable(true);
        demoModelViewHolder.btn_savechanges.setClickable(true);
        demoModelViewHolder.btn_savechanges.setEnabled(true);

        demoModelViewHolder.et_landmark.setEnabled(true);
        demoModelViewHolder.et_pincode.setEnabled(true);
        demoModelViewHolder.et_currentlocation.setEnabled(true);
        demoModelViewHolder.txt_capure_image.setEnabled(true);
        demoModelViewHolder.txt_getlocation.setEnabled(true);
        demoModelViewHolder.txt_getlocation.setClickable(true);
        demoModelViewHolder.btn_savechanges.setClickable(true);

        demoModelViewHolder.btnStatus.setEnabled(false);
        demoModelViewHolder.btnStatus.setClickable(false);

        demoModelViewHolder.et_dob.setBackgroundResource(R.drawable.bottum_line);

        demoModelViewHolder.et_annivaesarydate.setBackgroundResource(R.drawable.bottum_line);
        demoModelViewHolder.et_landmark.setBackgroundResource(R.drawable.bottum_line);
        demoModelViewHolder.et_pincode.setBackgroundResource(R.drawable.bottum_line);
        demoModelViewHolder.et_currentlocation.setBackgroundResource(R.drawable.bottum_line);
        demoModelViewHolder.txt_capure_image.setBackgroundResource(R.drawable.bottum_line);
        //demoModelViewHolder.txt_getlocation.setBackgroundResource(R.drawable.bottum_line);


    }

    @Override
    public int getItemCount() {
        return (mlist != null ? mlist.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class SamruddhaValidateRecord extends RecyclerView.ViewHolder {
        TextView txtFarmerName, txtMobileNum, txtWANum, txtMDO, txtDist, txtTaluka,
                txtVillage, txtLand, txtCrop, txtDate, txtGeoTag;
        ImageView imgEdit, imgSave;
        Button btnStatus;
        EditText edFarmerName, edMobileNum, edWANum, edMDO, edDist, edTaluka,
                edVillage, edLand, edCrop, edDate, edGeoTag;
        ImageView imgSk_Photo;

        int position;

        // For New samruddhi changes

        EditText et_dob, et_annivaesarydate, et_landmark, et_pincode, et_currentlocation;
        TextView txt_capure_image, txt_getlocation;
        Button btn_savechanges;


        SamruddhaValidateRecord(@NonNull View itemView) {
            super(itemView);


            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtMobileNum = (TextView) itemView.findViewById(R.id.txtMobileNum);
            txtWANum = (TextView) itemView.findViewById(R.id.txtWANum);
            txtMDO = (TextView) itemView.findViewById(R.id.txtMDO);
            txtDist = (TextView) itemView.findViewById(R.id.txtDist);
            txtTaluka = (TextView) itemView.findViewById(R.id.txtTaluka);
            txtVillage = (TextView) itemView.findViewById(R.id.txtVillage);
            txtLand = (TextView) itemView.findViewById(R.id.txtLand);
            txtCrop = (TextView) itemView.findViewById(R.id.txtCrop);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtGeoTag = (TextView) itemView.findViewById(R.id.txtGeoTag);

            edFarmerName = (EditText) itemView.findViewById(R.id.edFarmerName);
            edMobileNum = (EditText) itemView.findViewById(R.id.edMobileNum);
            edWANum = (EditText) itemView.findViewById(R.id.edWANum);
            edMDO = (EditText) itemView.findViewById(R.id.edMDO);
            edDist = (EditText) itemView.findViewById(R.id.edDist);
            edTaluka = (EditText) itemView.findViewById(R.id.edTaluka);
            edVillage = (EditText) itemView.findViewById(R.id.edVillage);
            edLand = (EditText) itemView.findViewById(R.id.edLand);
            edCrop = (EditText) itemView.findViewById(R.id.edCrop);
            edDate = (EditText) itemView.findViewById(R.id.edDate);
            edGeoTag = (EditText) itemView.findViewById(R.id.edGeoTag);

            imgSave = (ImageView) itemView.findViewById(R.id.imgSave);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            btnStatus = (Button) itemView.findViewById(R.id.btnStatus);

            // New samruddhi Changes

            et_dob = itemView.findViewById(R.id.et_dob);
            et_annivaesarydate = itemView.findViewById(R.id.et_anniversary);
            et_landmark = itemView.findViewById(R.id.et_landmark);
            et_pincode = itemView.findViewById(R.id.et_pincode);
            et_currentlocation = itemView.findViewById(R.id.et_currentlocation);
            txt_capure_image = itemView.findViewById(R.id.txt_capturephoto);
            txt_getlocation = itemView.findViewById(R.id.txt_getlocation);
            imgSk_Photo = itemView.findViewById(R.id.imgSk_Photo);
            btn_savechanges = itemView.findViewById(R.id.btn_savechanges);

        }
    }

    public void uploadData(String kisanValidationData, JSONObject jsonObject) {
        String str = null;
        if (config.NetworkConnection()) {

            try {
                new UploadSamruddhaDataServer(kisanValidationData, jsonObject, context).execute(SERVER).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//            builder.setTitle("MyActivity");
//            builder.setMessage("Data Saved Successfully");
//            builder.setCancelable(false);
//
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//                    // Config.refreshActivity(CropSeminarActivity.this);
//                    dialog.dismiss();
//
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
        }
    }


    //AsyncTask Class for api batch code upload call
    private class UploadSamruddhaDataServer extends AsyncTask<String, String, String> {
        private ProgressDialog p;
        JSONObject jsonObject;

        public UploadSamruddhaDataServer(String Funname, JSONObject jsonObject, Context context) {
            this.jsonObject = jsonObject;
            p = new ProgressDialog(context);
            p.setMessage("Please Wait...");
        }

        protected void onPreExecute() {
            p.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadSamruddhaData("kisanValidationData", jsonObject);
        }

        protected void onPostExecute(String result) {
            try {
                p.dismiss();
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Data updated Successfully");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
//                                    relPRogress.setVisibility(View.GONE);
//                                    container.setClickable(true);
//                                    container.setEnabled(true);
//                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // msclass.showMessage("Data Uploaded Successfully");
//                            relPRogress.setVisibility(View.GONE);
//                            container.setClickable(true);
//                            container.setEnabled(true);
//                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("MyActivity");
                        builder.setMessage("Poor Internet: Please try after sometime.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
//                                    relPRogress.setVisibility(View.GONE);
//                                    container.setClickable(true);
//                                    container.setEnabled(true);
//                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Info");
                    builder.setMessage("Something went wrong please try again later.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
//                                relPRogress.setVisibility(View.GONE);
//                                container.setClickable(true);
//                                container.setEnabled(true);
//                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String uploadSamruddhaData(String kisanValidationData, JSONObject obj) {
        String str = "";
        int action = 1;
        String searchQuery = null;
        try {
            searchQuery = "select  *  from SamruddhaKisanValidationData where id = '" + obj.getString("_id") + "' ";

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            JSONArray jsonArray = new JSONArray();
            if (count > 0) {
                try {

                    jsonArray = mDatabase.getResultsVillageDetails(searchQuery);

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("Table", jsonArray);

                    Log.d("kisanValidationData", jsonObject.toString());
                    str = syncSamruddhaData(kisanValidationData, SERVER, jsonObject);
                    handleSamruddhaDataResponse("kisanValidationData", str);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                cursor.close();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;

    }

    private void handleSamruddhaDataResponse(String function, String resultout) throws JSONException {

        if (function.equals("kisanValidationData")) {
            JSONObject jsonObject = new JSONObject(resultout);
            if (jsonObject.has("success")) {
                if (Boolean.parseBoolean(jsonObject.get("success").toString())) {
                    mDatabase.updateSamruddhaKisanUploadData("0", "1", jsonObject);

                }
            }
        }

    }

    private String syncSamruddhaData(String kisanValidationData, String server, JSONObject jsonObject) {

        return HttpUtils.POSTJSON(SERVER, jsonObject, mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));

    }
}
