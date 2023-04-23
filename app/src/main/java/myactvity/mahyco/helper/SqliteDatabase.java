package myactvity.mahyco.helper;

/**
 * Created by 97260828 on 12/22/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

import myactvity.mahyco.app.AppConstant;

import static android.content.ContentValues.TAG;

public class SqliteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 39; /*Updated on 7th September 2021*/
    /* last 33 16-08-2021 */
    private static final String DATABASE_NAME = "MDOApps";
    private static final String TABLE_PRODUCTS = "UserMaster";
    private static final String TABLE_PRODUCTS2 = "punchdata";
    private static final String TABLE_PRODUCTS3 = "TagData";
    private static final String TABLE_PRODUCTS4 = "CropMaster";
    private static final String TABLE_PRODUCTS5 = "MyActivityMaster";
    private static final String TABLE_PRODUCTS6 = "FarmerMaster";
    private static final String TABLE_PRODUCTS7 = "VillageLevelMaster";
    private static final String TABLE_PRODUCTS8 = "RetailerMaster";
    private static final String TABLE_PRODUCTS9 = "Tempstockdata";
    private static final String TABLE_PRODUCTS10 = "Commentlist";
    private static final String TABLE_PRODUCTS11 = "InnovationData";
    private static final String TABLE_PRODUCTS12 = "VehicleMaster";
    private static final String TABLE_PRODUCTS13 = "mdo_starttravel";
    private static final String TABLE_PRODUCTS14 = "mdo_addplace";
    private static final String TABLE_PRODUCTS15 = "mdo_Retaileranddistributordata";
    private static final String TABLE_PRODUCTS16 = "mdo_retailerproductdetail";
    private static final String TABLE_PRODUCTS17 = "mdo_endtravel";
    private static final String TABLE_PRODUCTS18 = "mdo_test";
    private static final String TABLE_PRODUCTS22 = "mdo_authoriseddistributorproduct";
    private static final String TABLE_PRODUCTS23 = "NotificationDetail";
    private static final String TABLE_DEMOMODELDATA = "DemoModelData";
    private static final String TABLE_DEMOREVIEWDATA = "DemoReviewData";
    private static final String TABLE_COUPONSCHEMEMASTER = "CouponSchemeMaster";
    /* private static final String TABLE_HDPSSCHEMEMASTER = "HDPSSchemeMaster";
     private static final String TABLE_HDPSCOUPONSCHEMEMASTER = "HDPSCouponSchemeMaster"; *//*Added on 21 April 2021*/
    private static final String TABLE_HDPSMASTERSCEME = "HDPSMasterScheme";  /*Added on 5th May 2021*/
    private static final String TABLE_HDPSCOUPONRECORDS = "HDPSCouponRecords";  /*Added on 5th May 2021*/
    private static final String TABLE_COUPONRECORDDATA = "CouponRecordData";
    private static final String TABLE_VALIDATEDDEMOMODELDATA = "ValidatedDemoModelData";
    private static final String TABLE_VALIDATEDDEMOREVIEWDATA = "ValidatedDemoReviewData";
    private static final String TABLE_MDOLISTDATA = "MDOListData";
    private static final String TABLE_RETAILERTOVISITDATA = "RetailerVisitToFieldData";
    private static final String TABLE_LIVEPLANTDISPLAYVILLAGEDATA = "LivePlantDisplayVillageData";
    private static final String TABLE_LIVEPLANTDISPLAYRETAILERDATA = "LivePlantDisplayRetailerData";
    private static final String TABLE_FIELDDAYDATA = "FieldDayData";
    private static final String TABLE_CROPSHOWDATA = "CropShowData";
    private static final String TABLE_HARVESTDAYDATA = "HarvestDayData";
    private static final String TABLE_FOCUSSEDVILLAGEMASTER = "FocussedVillageMaster";
    private static final String TABLE_RBM_MASTER = "RbmMaster";
    private static final String TABLE_MDO_TBM = "MdoTbmMaster";
    private static final String TABLE_RETAILERDETAILSMASTER = "RetailerDetailsMaster";
    private static final String TABLE_PURCHASELISTDATA = "PurchaseListData";

    private static final String TABLE_PAYMENT = "PaymentTable";
    private static final String TABLE_RETAILERSURVEYDATA = "RetailerSurveyData";
    private static final String TABLE_KISANCLUBDATA = "KisanClubData";
    private static final String TABLE_STATETERRITORYMASTER = "StateTerritoryMaster";

    // Pre Season Data
    private static final String TABLE_TESTIMONIAL_COLLECTION = "TestimonialCollectionData";
    private static final String TABLE_SANMAN_MELA = "SanmanMelaData";
    private static final String TABLE_VILLAGE_MEETING = "VillageMeetingData";
    private static final String TABLE_PROMOTION = "PromotionData";
    private static final String TABLE_CROP_SEMINAR = "CropSeminarData";
    private static final String TABLE_JEEP_CAMPAIGNING = "JeepCampaigningData";
    private static final String TABLE_JEEP_CAMPAIGNING_LOC_DETAILS = "JeepCampaigningLocDetails";
    private static final String TABLE_POP_DISPLAY = "PopDisplayData";

    //ATL
    private static final String TABLE_FIELDBOARDDATA = "FieldBoardData";
    private static final String TABLE_FIELDBANNERDATA = "FieldBannerData";
    private static final String TABLE_ATLEXHIBITIONDATA = "ATLExhibitionData";
    private static final String TABLE_ATL_MARKETDAY_DATA = "ATLMarketDayData";
    private static final String TABLE_ATL_WALL_PAINTING_DATA = "ATLVillageWallPaintingData";
    private static final String TABLE_ATL_WALL_PAINTING_DETAILS = "ATLVillageWallPaintingDetails";
    private static final String TABLE_ATL_POSTERING_DATA = "ATLVillagePosteringData";
    private static final String TABLE_ATL_POSTERING_DETAILS = "ATLVillagePosteringDetails";
    private static final String TABLE_ATL_TROLLEY_PAINTING_DATA = "TrolleyPaintingData";

    // General Activities
    private static final String TABLE_DISTRIBUTOR_VISIT_DATA = "DistributerVisitsData";
    private static final String TABLE_RETAILER_VISIT_DATA = "RetailerVisitsData";
    private static final String TABLE_FARMER_VISIT_DATA = "FarmerVisitsData";
    private static final String TABLE_SAMRUDDHA_KISAN_VISIT_DATA = "SamruddhaKisanVisitsData";

    //Created by nikita
    private static final String TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA = "SamruddhaKisanValidationData";
    private static final String TABLE_REVIEW_MEETING_DATA = "ReviewMeetingData";

    //post  Season Demo and model plot
    private static final String TABLE_CHECKHYBRIDMASTER_DATA = "MDO_checkHybridMaster"; // Mahendra

    // Merging data
    // User feedback status
    private static final String TABLE_FEEDBACK = "MDO_UserFeedBackStatus"; // Mahendra


    private static final String COLUMN_ID = "_id";
    private static final String UserCode = "UserCode";
    private static final String IMEINo = "IMEINo";
    // private SQLiteDatabase myDataBase;
    private static SqliteDatabase sInstance;
    // private static final String dbpath = Environment.getExternalStorageDirectory() + "/MyAttendance";//"/mnt/sdcard/VisitorDB";

    //  private static final String dbpath = "/mnt/sdcard/MDOApps"; // original live path
    private static final String dbpath = "MDOApps.db"; // original live path

    //private    static final String    dbpath = "MDOApps.db";

    //private    static final File folder1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    //private    static final String    dbpath = folder1.getAbsolutePath()+"/MDOApps.db";///mnt/sdcard/FarmMech";

    //  private    static final File folder1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    // private    static final String    dbpath = folder1.getAbsolutePath()+"/MDOAppsnew.db";///mnt/sdcard/FarmMech";


    //Check Entry  Saved
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date();
    String strdate = dateFormat.format(d);
    String searchQuery = "";
    //private static  String dbnamepath="";
    private Context context;

    /* public SqliteDatabase(Context context) {
         //super(context, DATABASE_NAME, null, DATABASE_VERSION);

         super(context, AppConstant.dbnamepath, null, DATABASE_VERSION);
         //this.context = context;
        // super(context, "MDOApps.db", null, DATABASE_VERSION);

     }*/
    public static synchronized SqliteDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SqliteDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public SqliteDatabase(@Nullable Context context) {

        super(context, dbpath, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db= SQLiteDatabase.openDatabase("Log", null, Context.MODE_PRIVATE);
        //Merging data
        String CREATE_TABLE_UserFeedback = " CREATE  TABLE IF NOT EXISTS " + TABLE_FEEDBACK + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,userCode TEXT,status TEXT" +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_UserFeedback);


        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + TABLE_PRODUCTS + "(Password TEXT," + UserCode + " TEXT," + IMEINo + " TEXT" + ",DisplayName TEXT,RoleID TEXT,unit TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
        String CREATE_PRODUCTS_TABLE2 = "CREATE    TABLE " + TABLE_PRODUCTS2 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + UserCode + " TEXT,imeino TEXT,coordinates TEXT,address TEXT,entrydate DATE,Flag TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE2);
        String CREATE_PRODUCTS_TABLE3 = "CREATE    TABLE " + TABLE_PRODUCTS3 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + UserCode + " TEXT,VisitNo TEXT,imeino TEXT,VisitType TEXT,coordinate TEXT,Outcoordinate TEXT,Location TEXT,OutLocation TEXT,CropType TEXT,ProductName TEXT,NoofFormer TEXT,InTime DATE,Comments TEXT,Status TEXT,Dist TEXT,Taluka TEXT,Village TEXT,Imgname TEXT,Img BLOB,Imgname2 TEXT,Img2 BLOB,commentdecription TEXT,imgstatus TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE3);
        String CREATE_PRODUCTS_TABLE4 = "CREATE    TABLE " + TABLE_PRODUCTS4 + "(ProductName TEXT," +
                "CropName TEXT,CropType TEXT,Crop_Code TEXT,Prod_Code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE4);
        String CREATE_PRODUCTS_TABLE5 = "CREATE    TABLE " + TABLE_PRODUCTS5 + "(ActivityName TEXT,activityType TEXT,activityTypeCode TEXT,activityNameCode TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE5);
        String CREATE_PRODUCTS_TABLE6 = "CREATE    TABLE " + TABLE_PRODUCTS6 + "(FarmerName TEXT,TalukaName TEXT," +
                "VillageName TEXT,AadharNo TEXT,MobileNo TEXT,Email TEXT,Status TEXT," +
                "statename TEXT,dist TEXT,TotalLand TEXT,usercode TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE6);
        String CREATE_PRODUCTS_TABLE7 = "CREATE    TABLE " + TABLE_PRODUCTS7 + "(state TEXT,state_code TEXT,district TEXT,district_code TEXT,taluka TEXT,taluka_code TEXT,village TEXT,village_code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE7);
        String CREATE_PRODUCTS_TABLE8 = "CREATE    TABLE " + TABLE_PRODUCTS8 + "(RetailerName TEXT,taluka TEXT," +
                "taluka_code TEXT,activity TEXT,dist TEXT,code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE8);
        String CREATE_PRODUCTS_TABLE9 = "CREATE    TABLE " + TABLE_PRODUCTS9 + "(id TEXT,visitno TEXT,crop TEXT,product TEXT,stock TEXT,currentstock TEXT,salestock TEXT,status TEXT,INTime DATE)";
        db.execSQL(CREATE_PRODUCTS_TABLE9);
        String CREATE_PRODUCTS_TABLE10 = "CREATE    TABLE " + TABLE_PRODUCTS10 + "(comment TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE10);
        String CREATE_PRODUCTS_TABLE11 = "CREATE    TABLE " + TABLE_PRODUCTS11 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,UserCode TEXT,Name TEXT,Name2 TEXT ,Name3 TEXT,Type TEXT,VisitNo TEXT," +
                "Mobileno TEXT,Dist TEXT,Taluka TEXT,Village TEXT,Imgname TEXT,TotalPkt TEXT," +
                "TotalMahycopkt TEXT,Remark TEXT,VehicleNo TEXT,Status TEXT,imagepath TEXT," +
                "location TEXT,firmname TEXT,PaddyTotalpkt TEXT,PaddyMahycototalPkt TEXT," +
                " MaizeTotalpkt TEXT, MaizeMahycototalPkt TEXT,Year TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE11);
        String CREATE_PRODUCTS_TABLE12 = "CREATE    TABLE " + TABLE_PRODUCTS12 + "(VehicleNo TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE12);
        String CREATE_PRODUCTS_TABLE13 = "CREATE    TABLE " + TABLE_PRODUCTS13 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,startdate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE13);
        String CREATE_PRODUCTS_TABLE14 = "CREATE    TABLE " + TABLE_PRODUCTS14 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,place TEXT,coordinate TEXT,startaddress TEXT,date TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE14);
        String CREATE_PRODUCTS_TABLE15 = "CREATE    TABLE " + TABLE_PRODUCTS15 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT,dist TEXT,taluka TEXT,marketplace TEXT," +
                "retailername TEXT ,retailerfirm TEXT,age TEXT, mobileno TEXT,asscoi_distributor TEXT,keyretailer TEXT," +
                "otherbussiness TEXT,experianceSeedwork TEXT, comments TEXT,Status TEXT,type TEXT," +
                "newfirm TEXT,birthdate TEXT,WhatsappNo TEXT,state TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE15);
        String CREATE_PRODUCTS_TABLE16 = "CREATE    TABLE " + TABLE_PRODUCTS16 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE16);


        String CREATE_PRODUCTS_TABLE17 = "CREATE    TABLE " + TABLE_PRODUCTS17 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,enddate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE17);


        String CREATE_TABLE_DEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT DEFAULT '',district TEXT DEFAULT '',taluka TEXT DEFAULT '',village TEXT DEFAULT ''," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,area TEXT," +
                "seedQuantity TEXT,unit TEXT,sowingDate TEXT,coordinates TEXT,soilType Text,irrigationMode Text," +
                "spacingRow TEXT,spacingPlan TEXT,imgName TEXT" +
                ",imgPath TEXT,imgStatus TEXT,isSynced TEXT, checkHybrids TEXT," +
                "remarks TEXT,checkHybridsSelected TEXT, " +
                " focussedVillage TEXT DEFAULT '',vill_code TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_DEMOMODELDATA);


        String CREATE_TABLE_DEMOREVIEWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DEMOREVIEWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate TEXT," +
                "sowingDate TEXT,coordinates TEXT,purposeVisit TEXT,comment TEXT,imgName TEXT,imgPath TEXT," +
                "imgStatus TEXT," +
                "isSynced TEXT,focussedVillage TEXT,fieldPest TEXT,fieldDiseases TEXT," +
                "fieldNutrients TEXT,fieldOther TEXT,cropCondition TEXT,recommendations TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_DEMOREVIEWDATA);

        String CREATE_TABLE_VALIDATED_DEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, " +
                "product TEXT,area TEXT,seedQuantity TEXT,unit TEXT,sowingDate TEXT," +
                "coordinates TEXT,soilType Text,irrigationMode Text,spacingRow TEXT,spacingPlan TEXT," +
                "imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT, checkHybrids TEXT,remarks TEXT," +
                "mdoCode TEXT DEFAULT '',entrydate TEXT,isUploaded TEXT," +
                "checkHybridsSelected TEXT,focussedVillage TEXT DEFAULT '')";
        db.execSQL(CREATE_TABLE_VALIDATED_DEMOMODELDATA);

        String CREATE_TABLE_VALIDATED_DEMOREVIEWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOREVIEWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate" +
                " TEXT,sowingDate TEXT,coordinates TEXT,purposeVisit TEXT,comment TEXT,imgName TEXT," +
                "imgPath TEXT,imgStatus TEXT,isSynced TEXT,mdoCode TEXT  DEFAULT '',entrydate TEXT," +
                "isUploaded TEXT,focussedVillage TEXT,fieldPest TEXT,fieldDiseases TEXT," +
                "fieldNutrients TEXT,fieldOther TEXT,cropCondition TEXT,recommendations TEXT)";
        db.execSQL(CREATE_TABLE_VALIDATED_DEMOREVIEWDATA);


        String CREATE_TABLE_MDOLISTDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_MDOLISTDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,user_code TEXT,MDO_name TEXT)";
        db.execSQL(CREATE_TABLE_MDOLISTDATA);

        String CREATE_TABLE_COUPONSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_COUPONSCHEMEMASTER);

       /* String CREATE_TABLE_HDPSSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_HDPSSCHEMEMASTER);

        String CREATE_TABLE_HDPSCOUPONSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSCOUPONSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_HDPSCOUPONSCHEMEMASTER);*/

        /*Created on 5th May 2021*/
        String CREATE_TABLE_HDPSMASTERSCEME = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSMASTERSCEME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,bigint TEXT,year TEXT,season TEXT,state TEXT," +
                "region TEXT,crop TEXT,product TEXT,productCode TEXT,productname TEXT," +
                "schemeName TEXT,schemeUnit TEXT,advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,"
                + " handleChargeRetailer TEXT,handleChargeDistributor TEXT,schemeFrom TEXT,schemeEnd TEXT,entrydate TEXT, allowpacket TEXT, perCouponPkt TEXT, retailerContraint TEXT)";
        db.execSQL(CREATE_TABLE_HDPSMASTERSCEME); /*Added 3 column on 9th Sept 2021 allowpacket,perCouponPkt,retailerContraint*/

        String CREATE_HDPSCOUPONRECORDS = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSCOUPONRECORDS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "couponCode TEXT,productCode TEXT, entryDate TEXT)";
        db.execSQL(CREATE_HDPSCOUPONRECORDS);

        String CREATE_TABLE_COUPONRECORDDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONRECORDDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT," +
                "quantityPacket TEXT,packetsBooked TEXT,couponsIssued TEXT,totalAmount TEXT," +
                "couponQr1 TEXT DEFAULT '',couponQr2 TEXT DEFAULT '',couponQr3 TEXT DEFAULT ''," +
                "couponQr4 TEXT DEFAULT '',couponQr5 TEXT DEFAULT '',couponQr6 TEXT DEFAULT ''" +
                ",farmerPhoto TEXT," +
                "imgPath TEXT,imgStatus TEXT,isSynced TEXT,address TEXT,cordinates TEXT)";
        db.execSQL(CREATE_TABLE_COUPONRECORDDATA);

        String CREATE_TABLE_COUPONcheckupload = " CREATE  TABLE IF NOT EXISTS  CheckUploadCoupon(userCode TEXT," +
                "mobileNumber TEXT,crop TEXT, product TEXT," +
                "farmerPhoto TEXT )";
        db.execSQL(CREATE_TABLE_COUPONcheckupload);

        String CREATE_TABLE_RETAILERSURVEYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERSURVEYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,territory TEXT,tehsil TEXT," +
                "unitType TEXT,retailerFirmName TEXT,contactPersonName TEXT,retailerNumber TEXT,retailerAddress TEXT, retailerCordinates TEXT," +
                "whatsappNumber TEXT DEFAULT '' ,asscoidistributor TEXT,majorVegCrops TEXT,estimatedAvgVolumeSeedSold TEXT,estimatedVolumeMahycoSungro TEXT DEFAULT ''" +
                ",mahycoProductSold TEXT ,sungroProductSold TEXT " +
                ",retailerOtherCompany TEXT ,businessOtherThenSeed TEXT ,isSynced TEXT)";
        db.execSQL(CREATE_TABLE_RETAILERSURVEYDATA);


        String CREATE_TABLE_KISANCLUBDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_KISANCLUBDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT, timeStamp TEXT,userCode TEXT,state TEXT,territory TEXT,district TEXT,tehsil TEXT,village TEXT," +
                "unitType TEXT, farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,vegetableChoice TEXT," +
                "area TEXT DEFAULT '' ,cropSown TEXT,productAwareness TEXT,ratingMahyco TEXT  DEFAULT '',ratingSungro TEXT  DEFAULT '' ," +
                "isSynced TEXT,comments TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_KISANCLUBDATA);


        String CREATE_TABLE_STATETERRITORYMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_STATETERRITORYMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,zone TEXT,territory TEXT,state TEXT,state_code TEXT )";
        db.execSQL(CREATE_TABLE_STATETERRITORYMASTER);


        String CREATE_TABLE_FOCUSSEDVILLAGEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_FOCUSSEDVILLAGEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,vil_code TEXT,vil_desc TEXT,taluka TEXT)";
        db.execSQL(CREATE_TABLE_FOCUSSEDVILLAGEMASTER);

        String CREATE_RBM_MASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_RBM_MASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,code TEXT,description TEXT, TBMCode TEXT, TBMName TEXT)";
        db.execSQL(CREATE_RBM_MASTER);

        String CREATE_MDO_TBM_MASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_MDO_TBM + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdoCode TEXT,mdoDesc TEXT,tbmCode TEXT,tbmDesc TEXT)";
        db.execSQL(CREATE_MDO_TBM_MASTER);

        String CREATE_TABLE_RETAILERDETAILSMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERDETAILSMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,  state TEXT,  type TEXT,  mdoCode TEXT,  marketPlace TEXT,  dist TEXT,  taluka TEXT, mobileNo TEXT, firmName TEXT,  retailercode TEXT,  retailerName TEXT)";
        db.execSQL(CREATE_TABLE_RETAILERDETAILSMASTER);


        String CREATE_TABLE_LIVEPLANTDISPLAYVILLAGEDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_LIVEPLANTDISPLAYVILLAGEDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,uId TEXT , focussedVillage TEXT DEFAULT '',  state TEXT DEFAULT '', " +
                " district TEXT DEFAULT '',  taluka TEXT DEFAULT '',  village TEXT DEFAULT ''," +
                " farmerCount TEXT,cropType TEXT,  product TEXT,taggedAddress TEXT DEFAULT ''," +
                "coordinates TEXT DEFAULT '', activityImgName TEXT, activityImgPath TEXT,  " +
                "activityImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_LIVEPLANTDISPLAYVILLAGEDATA);


        String CREATE_TABLE_LIVEPLANTDISPLAYRETAILERDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_LIVEPLANTDISPLAYRETAILERDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT,  state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',  marketPlace TEXT DEFAULT '', retailerDetails TEXT DEFAULT '', " +
                "farmerCount TEXT,cropType TEXT,  product TEXT,taggedAddress TEXT DEFAULT ''," +
                "coordinates TEXT DEFAULT '', activityImgName TEXT, activityImgPath TEXT,  " +
                "activityImgStatus TEXT, isSynced TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_LIVEPLANTDISPLAYRETAILERDATA);

        // Demand Servery Data
        String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE mdo_demandassesmentsurvey(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,state TEXT,dist TEXT,taluka TEXT,village TEXT," +
                "mobileno TEXT,awaremahycoprd TEXT,entrydate DATE," +
                "Status TEXT,farmername TEXT,crop TEXT,coordinate TEXT,address TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE18);
        String CREATE_PRODUCTS_TABLE19 = "CREATE    TABLE mdo_cultivation(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,performance TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE19);
        String CREATE_PRODUCTS_TABLE20 = "CREATE    TABLE mdo_cultivationtobe(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE20);


        String CREATE_PRODUCTS_TABLE21 = "CREATE    TABLE mdo_awaremahycoproduct(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,hybridsown TEXT," +
                "mobileno TEXT,typestatus TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE21);
        String CREATE_PRODUCTS_TABLE22 = "CREATE    TABLE " + TABLE_PRODUCTS22 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE22);
        String CREATE_PRODUCTS_TABLE23 = "CREATE    TABLE mdo_photoupdate(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "mdocode TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE23);

        String CREATE_PRODUCTS_TABLE24 = "CREATE    TABLE  CompanyNamemaster(" + COLUMN_ID + " INTEGER PRIMARY KEY,Compname TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE24);

        String CREATE_PRODUCTS_TABLE25 = "CREATE   TABLE SAPCROPMaster(ProductName TEXT,PRoductCode TEXT," +
                "CropName TEXT,CropCode TEXT,CropType TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE25);

        String CREATE_PRODUCTS_TABLE26 = "CREATE  TABLE RetailerPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerName TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE26);

        String CREATE_PRODUCTS_TABLE27 = "CREATE  TABLE RetailerCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE27);

        String CREATE_PRODUCTS_TABLE28 = "CREATE  TABLE RetailerNextBtPOGTable(usercode TEXT," +
                "RetailerMobileno TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date TEXT,AssociateDistributor TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE28);

        String CREATE_PRODUCTS_TABLE29 = "CREATE  TABLE DistributorCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT," +
                "BalStock TEXT ,placementstock TEXT, stockDate TEXT,Status TEXT,DistrCode TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE29);

        String CREATE_PRODUCTS_TABLE30 = "CREATE  TABLE DistributorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,Mobileno TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT,plan TEXT," +
                "RecStockdepot TEXT,RecStockdistr TEXT,ShiftStockToDistr TEXT,NetStock TEXT," +
                "BalStock TEXT ,placementstock TEXT,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE30);

        String CREATE_PRODUCTS_TABLE31 = "CREATE  TABLE DistributorNextBtPOGTable(usercode TEXT," +
                "Mobileno TEXT,DistrCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date  TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE31);

        String CREATE_PRODUCTS_TABLE32 = "CREATE  TABLE mdo_pogsapdata(Cust_Code TEXT," +
                "Crop_Name TEXT ,Crop_Code TEXT,Prod_Code TEXT," +
                "Prod_Name TEXT,QTY_Unit TEXT," +
                "QTY_Base TEXT, Plan_Qty TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE32);

        String CREATE_PRODUCTS_TABLE33 = "CREATE  TABLE MDO_tagRetailerList(state TEXT," +
                "type TEXT ,mdocode TEXT,marketplace TEXT," +
                "dist TEXT,taluka TEXT," +
                "mobileno TEXT,firmname TEXT,name TEXT ,retailercode TEXT, retailerCategory TEXT )";
        db.execSQL(CREATE_PRODUCTS_TABLE33);

        // DistributorAsRetailer
        String CREATE_PRODUCTS_TABLE34 = "CREATE  TABLE DistributorAsRetailerPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE34);

        //DistributorAsRetailerCompetitator
        String CREATE_PRODUCTS_TABLE35 = "CREATE  TABLE DistributorAsRetailerCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE35);


        //DistributorAsRetailer BT
        String CREATE_PRODUCTS_TABLE36 = "CREATE  TABLE DistributorAsRetailerNextBtPOGTable(usercode TEXT," +
                "DistrCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date TEXT,AssociateDistributor TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE36);
//
// PaymentTable
        String createPaymentTable = "CREATE  TABLE IF NOT EXISTS PaymentTable(" +
                "usercode TEXT," +
                "rzpPaymentId TEXT," +
                "orderId TEXT DEFAULT ''," +
                "farmersIdList TEXT," +
                "status TEXT," +
                "amount TEXT)";
        db.execSQL(createPaymentTable);
        String CREATE_couponmaster_TABLE = "CREATE  TABLE IF NOT EXISTS couponMaster(couponCode TEXT," +
                "productCode TEXT,entryDate TEXT)";
        db.execSQL(CREATE_couponmaster_TABLE);

        String CREATE_couponpaymentamount_TABLE = "CREATE  TABLE IF NOT EXISTS " +
                "couponPaymentAmount(paymentAmount TEXT,remainingAmount TEXT,newCouponScanAmount TEXT )";
        db.execSQL(CREATE_couponpaymentamount_TABLE);


        String CREATE_TABLE_CHECKHYBRIDMASTER_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CHECKHYBRIDMASTER_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "hybridName TEXT )";
        db.execSQL(CREATE_TABLE_CHECKHYBRIDMASTER_DATA);

        String CREATE_TABLE_RETAILERTOVISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERTOVISITDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT,district TEXT,farmerName TEXT,farmerMobileNumber TEXT," +
                "farmerVillage TEXT,farmerTaluka TEXT,cropType TEXT, product TEXT,retailerCount TEXT,coordinates TEXT,address TEXT," +
                "activityImgName TEXT,activityImgPath TEXT ,farmerListImgName TEXT, farmerListImgPath TEXT, activityImgStatus TEXT, farmerListImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_RETAILERTOVISITDATA);
        //db.close();


        // Field Daya Data
        String CREATE_TABLE_FIELDDAYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDDAYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', pkFarmerMobileNumber TEXT, retailerCount TEXT, cropType TEXT,  " +
                "product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhoto TEXT,  " +
                "retailerListPhotoStatus TEXT,isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDDAYDATA);


        // Crop Show Data
        String CREATE_TABLE_CROPSHOWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CROPSHOWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', retailerDetails TEXT,pkRetailerMobileNumber TEXT, cropType TEXT,  " +
                "talukaMDO TEXT, product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhoto TEXT, " +
                " retailerListPhotoStatus TEXT,isSynced TEXT," +
                " EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_CROPSHOWDATA);

        // Harvest Daya Data
        String CREATE_TABLE_HARVESTDAYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_HARVESTDAYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', retailerDetails TEXT, pkRetailerMobileNumber TEXT, cropType TEXT,  " +
                "product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  " +
                "farmerListPhotoStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_HARVESTDAYDATA);


        // Purchase List Data
        String CREATE_TABLE_PURCHASELISTDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_PURCHASELISTDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', marketPlace TEXT DEFAULT '', retailerDetails TEXT, retailerNumber TEXT, taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', " +
                "finalVillageJSON TEXT DEFAULT '', " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_PURCHASELISTDATA);

        // Pre Season - Testimonial Collection Data
        String CREATE_TABLE_TESTIMONIAL_COLLECTION_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_TESTIMONIAL_COLLECTION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', hardCopyTestimonialType TEXT DEFAULT '', videoTestimonialType TEXT, farmerName TEXT, farmerMobile TEXT,  " +
                "farmerPhotoName TEXT, farmerPhotoPath TEXT,  farmerPhotoStatus TEXT, " +
                "successPhotoName TEXT, successPhotoPath TEXT,  successPhotoStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_TESTIMONIAL_COLLECTION_DATA);

        // Pre Season - Sanman Mela Data
        String CREATE_TABLE_SANMAN_MELA_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SANMAN_MELA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "finalFarmerJSON TEXT, farmerCount TEXT,  " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_SANMAN_MELA_DATA);


        // Pre Season - Village Meeting Data
        String CREATE_TABLE_VILLAGE_MEETING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VILLAGE_MEETING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "selectRBM TEXT,  selectTBM TEXT, selectMDO TEXT, retailerDetails TEXT, talukaRetailer TEXT, farmerCount TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_VILLAGE_MEETING_DATA);

        // Pre Season - Promotion Data
        String CREATE_TABLE_PROMOTION_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_PROMOTION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, programName TEXT DEFAULT '', focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "retailerDetails TEXT, talukaRetailer TEXT, farmerCount TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_PROMOTION_DATA);

        // Pre Season - Crop Seminar Data
        String CREATE_TABLE_CROP_SEMINAR_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CROP_SEMINAR + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, state TEXT DEFAULT '',  district TEXT DEFAULT '', taluka TEXT DEFAULT '', " +
                "eventVenue TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "finalVillageJSON TEXT, selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhotoPath TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhotoPath TEXT,  retailerListPhotoStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_CROP_SEMINAR_DATA);

        // Pre Season - Jeep campaigning
        String CREATE_TABLE_JEEP_CAMPAIGNING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_JEEP_CAMPAIGNING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                "userCode TEXT, state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '', cropType TEXT, product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "taggedAddressEnd TEXT DEFAULT '', taggedCordinatesEnd TEXT DEFAULT '', " +
                "OdometerReading TEXT DEFAULT '', startLocation TEXT DEFAULT '', endLocation TEXT DEFAULT '', rtoRegistrationNumber TEXT DEFAULT '', " +
                "OdometerReadingEnd TEXT DEFAULT '', finalPopupJson TEXT DEFAULT '', " +
                "isSynced TEXT DEFAULT '0',FinalSubmit TEXT DEFAULT '0',EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_JEEP_CAMPAIGNING_DATA);


        //Pre season- Jeep campaigning location detail
        String CREATE_TABLE_JEEP_CAMPAIGNING_LOCATION_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_JEEP_CAMPAIGNING_LOC_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                "userCode TEXT, state TEXT DEFAULT '',villagePopup TEXT DEFAULT '', statePopup TEXT DEFAULT '', districtPopup TEXT DEFAULT ''," +
                "talukaPopup TEXT DEFAULT '',  startTime TEXT DEFAULT '', endTime TEXT DEFAULT '', taggedAddressPopup TEXT DEFAULT '', taggedCordinatesPopup TEXT DEFAULT ''," +
                "noOfFarmers TEXT DEFAULT '',  activityImgName TEXT DEFAULT '',  activityImgPath TEXT DEFAULT '',  activityImgStatus TEXT DEFAULT ''," +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_JEEP_CAMPAIGNING_LOCATION_DETAILS);


        // Pre Season - POP display
        String CREATE_TABLE_POP_DISPLAY_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_POP_DISPLAY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '',   marketName TEXT DEFAULT '', retailerDetails TEXT DEFAULT '' ,finalVillageJSON TEXT DEFAULT ''," +
                "photoOneImgName TEXT, photoOneImgPath TEXT,  photoOneImgStatus TEXT," +
                "photoTwoImgName TEXT, photoTwoImgPath TEXT,  photoTwoImgStatus TEXT," +
                "photoThreeImgName TEXT, photoThreeImgPath TEXT,  photoThreeImgStatus TEXT,isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_POP_DISPLAY_DATA);


        //ATL
        String CREATE_TABLE_FIELDBOARDDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDBOARDDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  boardCount TEXT DEFAULT '', " +
                " remarks TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '', farmerDetails TEXT, pkFarmerMobileNumber TEXT,cropType TEXT,product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "fieldBoardImgPathName TEXT, fieldBoardImgPath TEXT,  " +
                "fieldBoardImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDBOARDDATA);


        String CREATE_TABLE_FIELDBANNERDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDBANNERDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',bannerCount TEXT DEFAULT '',remarks TEXT DEFAULT '', farmerDetails TEXT, pkFarmerMobileNumber TEXT,cropType TEXT,product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "fieldBannerImgName TEXT, fieldBannerImgPath TEXT,  fieldBannerImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDBANNERDATA);


        String CREATE_TABLE_ATLEXHIBITIONDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATLEXHIBITIONDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',numberOfDays TEXT DEFAULT '',numberOfVisitors TEXT DEFAULT ''," +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "selectRBM TEXT DEFAULT '', selectTBM TEXT DEFAULT '', selectMDO TEXT DEFAULT ''," +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATLEXHIBITIONDATA);

        String CREATE_TABLE_ATL_MARKETDAY_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_MARKETDAY_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',numberOfVisitors TEXT DEFAULT ''," +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, isSynced TEXT," +
                " EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_MARKETDAY_DATA);


        String CREATE_TABLE_ATL_WALL_PAINTING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_WALL_PAINTING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  selectedWallPaintingType TEXT DEFAULT '', focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT ''," +
                "strMandiName TEXT DEFAULT '',taggedAddress TEXT DEFAULT ''," +
                "taggedCordinates TEXT DEFAULT '',taggedAddressMandiNameStart TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameStart TEXT DEFAULT '',taggedAddressMandiNameEnd TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameEnd TEXT DEFAULT '', finalPopupJson TEXT , " +
                "isSynced TEXT DEFAULT '0',FinalSubmit TEXT DEFAULT '0'," +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_WALL_PAINTING_DATA);


        String CREATE_TABLE_ATL_WALL_PAINTING_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_WALL_PAINTING_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, croptypePopup TEXT DEFAULT ''," +
                "productNamePopup TEXT DEFAULT ''," +
                "taggedAddressPopup TEXT DEFAULT ''," +
                "taggedCordinatesPopup TEXT DEFAULT '',strWidthInFt TEXT DEFAULT ''," +
                "strHeightInFt TEXT DEFAULT '',strTotalInSqFt TEXT DEFAULT ''," +
                "activityImgName TEXT DEFAULT '',activityImgPath TEXT DEFAULT '', activityImgStatus TEXT DEFAULT ''," +
                "strTotalWallPainted TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_ATL_WALL_PAINTING_DETAILS);


        String CREATE_TABLE_ATL_POSTERING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_POSTERING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  selectedPosteringType TEXT DEFAULT '', focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT ''," +
                "strMandiName TEXT DEFAULT '', numberOfSpots TEXT DEFAULT '' ,taggedAddress TEXT DEFAULT ''," +
                "taggedCordinates TEXT DEFAULT '',taggedAddressMandiNameStart TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameStart TEXT DEFAULT '',taggedAddressMandiNameEnd TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameEnd TEXT DEFAULT '', finalPopupJson TEXT, " +
                "isSynced TEXT DEFAULT '0',FinalSubmit TEXT DEFAULT '0'," +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_POSTERING_DATA);


        String CREATE_TABLE_ATL_POSTERING_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_POSTERING_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, croptypePopup TEXT DEFAULT ''," +
                "productNamePopup TEXT DEFAULT ''," +
                "taggedAddressPopup TEXT DEFAULT ''," +
                "taggedCordinatesPopup TEXT DEFAULT ''," +
                "activityImgName TEXT DEFAULT '',activityImgPath TEXT DEFAULT '', activityImgStatus TEXT DEFAULT ''," +
                "strTotalPosters TEXT, totalSpots TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_ATL_POSTERING_DETAILS);

        String CREATE_TABLE_ATL_TROLLEY_PAINTING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_TROLLEY_PAINTING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT '', cropType TEXT DEFAULT ''," +
                "product TEXT DEFAULT '',farmerName TEXT DEFAULT '', farmerMobileNumber TEXT DEFAULT '', " +
                "trolleyNumber TEXT DEFAULT '', totalDimensionLeft TEXT DEFAULT '', totalDimensionRight TEXT DEFAULT ''," +
                "totalDimensionBack TEXT DEFAULT '', totalDimensionFront TEXT DEFAULT '',  " +
                "leftSideImgName TEXT, leftSideImgPath TEXT,  leftSideImgStatus TEXT," +
                "rightSideImgName TEXT, rightSideImgPath TEXT,  rightSideImgStatus TEXT," +
                "backSideImgName TEXT, backSideImgPath TEXT,  backSideImgStatus TEXT," +
                "frontSideImgName TEXT, frontSideImgPath TEXT,  frontSideImgStatus TEXT," +
                " isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_TROLLEY_PAINTING_DATA);


        // General Activities, Distributor Visit
        String CREATE_TABLE_DISTRIBUTOR_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DISTRIBUTOR_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, marketPlace TEXT, distributerDetails TEXT, " +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_DISTRIBUTOR_VISITDATA);

        // General Activities, Retailer Visit
        String CREATE_TABLE_RETAILER_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILER_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, marketPlace TEXT, RetailerDetails TEXT, " +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_RETAILER_VISITDATA);

        // General Activities, Farmer Visit
        String CREATE_TABLE_FARMER_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FARMER_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,focussedVillage TEXT, state TEXT, district TEXT, taluka TEXT, village TEXT, crop TEXT, " +
                "product TEXT,farmerName TEXT, mobileNumber TEXT, whatsappNumber TEXT, totalLand TEXT," +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, " +
                "isSynced TEXT, isSamruddhaKisan TEXT,aadharNumber TEXT, emailID TEXT," +
                " EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT )";
        db.execSQL(CREATE_TABLE_FARMER_VISITDATA);

        // General Activities, Samruddha Kisan Visit
        String CREATE_TABLE_SAMRUDDHA_KISAN_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SAMRUDDHA_KISAN_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,focussedVillage TEXT, state TEXT, district TEXT, taluka TEXT, village TEXT, crop TEXT, " +
                "product TEXT,farmerName TEXT, mobileNumber TEXT, whatsappNumber TEXT, totalLand TEXT," +
                "aadharNumber TEXT, emailID TEXT, taggedAddress TEXT, taggedCordinates TEXT," +
                " isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_SAMRUDDHA_KISAN_VISITDATA);


        //  Samruddha Kisan validate
        String CREATE_TABLE_SAMRUDDHA_KISAN_VALIDATE_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "id TEXT,userCode TEXT DEFAULT '',focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '', village TEXT DEFAULT '', crop TEXT DEFAULT '', " +
                "product TEXT DEFAULT '',farmerName TEXT DEFAULT '', mobileNumber TEXT DEFAULT '', whatsappNumber TEXT DEFAULT '', totalLand TEXT DEFAULT ''," +
                " taggedAddress TEXT DEFAULT '', taggedCordinates TEXT DEFAULT '',  comment TEXT DEFAULT '', commentDescription TEXT DEFAULT '',  mdoCode TEXT DEFAULT '',tbmCode TEXT DEFAULT '' ,mdoDesc ,tbmDesc TEXT,   validateBy TEXT DEFAULT '', action TEXT DEFAULT '', reasons TEXT DEFAULT '',  isSamruddhaKisan  TEXT DEFAULT '',isSynced TEXT DEFAULT '', EntryDt DATETIME DEFAULT (datetime('now','localtime'))" +
                "," +
                "farmer_dob TEXT DEFAULT ''," +
                "farmer_anniversarydate TEXT DEFAULT ''," +
                "farmer_pincode TEXT DEFAULT ''," +
                "farmer_landmark TEXT DEFAULT ''," +
                "farmer_house_latlong TEXT DEFAULT ''," +
                "farmer_house_address TEXT DEFAULT ''," +
                "farmer_photo_name TEXT DEFAULT '',\n" +
                "farmer_photo_path TEXT DEFAULT '')";
        db.execSQL(CREATE_TABLE_SAMRUDDHA_KISAN_VALIDATE_DATA); //  Samruddha Kisan validate


        // General Activities, Review meeting
        String CREATE_TABLE_REVIEW_MEETING = " CREATE  TABLE IF NOT EXISTS " + TABLE_REVIEW_MEETING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, meetingPlace TEXT, meetingPurpose TEXT, " +
                "comments TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_REVIEW_MEETING);

        // db.close();


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        //  onCreate(db);


        switch (oldVersion) {

            case 24:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                break;
            case 25:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                break;
            case 26:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                break;
            case 27:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                break;
            case 28:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                break;
            case 29:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                break;
            case 30:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                break;
            case 31:
                upgrate47(db);
                upgrate48(db);
                upgrade50(db);
                upgrate001(db);
                upgrateRetailerTableUpdate(db);
                updateATLWallpainting(db);
                UpdateFOCUSSEDVILLAGEMASTER(db);
                addCoupon6CouponRecordData(db);
                addnewcolomn(db);
                addnewDemomodelData_vill_code(db);
                userfeedback(db);
                AddATLActivityvillcode(db);
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 32:
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 33:
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 34:
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 35:
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 36:
                createHDPSMasterSchemeTable(db);
                createHDPSCouponMasterRecordsTable(db);
                addEntryDateDemoplot(db);
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 37:
                alterHDPSMasterSchemeTable(db); //*Added on 9th Sept 2021*//*
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 38:
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            case 39:
                alterSamrudhhaKisanTable(db);   // Added on 2nd Feb 2022
                break;
            default:
                break;
        }
    }


    public void addEntryDateDemoplottest(SQLiteDatabase db) {

        try {
            //SQLiteDatabase db = getWritableDatabase();
            // open();
            if (!checkColumnExist1(db, "DemoModelData", "EntryDt")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN  EntryDt TEXT");
            }
            if (!checkColumnExist1(db, "DemoReviewData", "EntryDt")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN  EntryDt TEXT");
            }
            //db.close();
            //closeDB();

        } catch (Exception ex) {
            Log.d(TAG, "addEntryDateDemoplot: " + ex.toString());
        }


    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void open() throws SQLException {
        close();
        this.getWritableDatabase();
    }

    private void AddATLActivityvillcode(SQLiteDatabase db) {
        //Start
        //end
        try {

            if (!checkColumnExist1(db, "FieldBoardData", "villagecode")) {
                // ATL
                db.execSQL("ALTER TABLE FieldBoardData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE FieldBannerData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE ATLExhibitionData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE ATLMarketDayData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE ATLVillageWallPaintingData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE ATLVillagePosteringData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE TrolleyPaintingData ADD COLUMN villagecode TEXT");
                // General
                db.execSQL("ALTER TABLE FarmerVisitsData ADD COLUMN villagecode TEXT");
                // Pre Season
                db.execSQL("ALTER TABLE TestimonialCollectionData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE SanmanMelaData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE VillageMeetingData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE PromotionData ADD COLUMN villagecode TEXT");

                // POST Season

                db.execSQL("ALTER TABLE FieldDayData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE CropShowData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE HarvestDayData ADD COLUMN villagecode TEXT");
                db.execSQL("ALTER TABLE LivePlantDisplayVillageData ADD COLUMN villagecode TEXT");

            }


        } catch (Exception ex) {
        }
    }
    /*private void createHDPSTable(SQLiteDatabase db){
        String CREATE_TABLE_HDPSSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_HDPSSCHEMEMASTER);
    }

    private void createHDPSCouponMasterTable(SQLiteDatabase db){ *//*Added on 21 April 2021*//*
        String CREATE_TABLE_HDPSCOUPONSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSCOUPONSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_HDPSCOUPONSCHEMEMASTER);
    }*/


    private void createHDPSMasterSchemeTable(SQLiteDatabase db) {
        try {


            String CREATE_TABLE_HDPSMASTERSCEME = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSMASTERSCEME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,bigint TEXT,year TEXT,season TEXT,state TEXT," +
                    "region TEXT,crop TEXT,product TEXT,productCode TEXT,productname TEXT," +
                    "schemeName TEXT,schemeUnit TEXT,advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,"
                    + " handleChargeRetailer TEXT,handleChargeDistributor TEXT,schemeFrom TEXT,schemeEnd TEXT,entrydate TEXT)";
            db.execSQL(CREATE_TABLE_HDPSMASTERSCEME);
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    private void createHDPSCouponMasterRecordsTable(SQLiteDatabase db) {
        try {
            String CREATE_TABLE_HDPSCOUPONRECORDS = " CREATE  TABLE IF NOT EXISTS " + TABLE_HDPSCOUPONRECORDS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + "couponCode TEXT, productCode TEXT, entryDate TEXT" + ")";
            db.execSQL(CREATE_TABLE_HDPSCOUPONRECORDS);
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    private void addnewDemomodelData_vill_code(SQLiteDatabase db) {

        //Start
        //end
        try {

            if (!checkColumnExist1(db, "DemoModelData", "vill_code")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN vill_code TEXT");
                //Delete 2019  demo Record from User local devices
                deleterecord("delete from DemoModelData where sowingDate like '%2019%'");
                deleterecord("delete from DemoReviewData where visitingDate like '%2019%'");
                deleterecord("delete from ValidatedDemoModelData where sowingDate like '%2019%'");
                deleterecord("delete from ValidatedDemoReviewData where visitingDate like '%2019%'");
            }


        } catch (Exception ex) {

        }


    }

    private void userfeedback(SQLiteDatabase db) {
        try {

            String CREATE_TABLE_UserFeedback = " CREATE  TABLE IF NOT EXISTS " + TABLE_FEEDBACK + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,userCode TEXT,status TEXT," +
                    "EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
            db.execSQL(CREATE_TABLE_UserFeedback);


        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    private void alterHDPSMasterSchemeTable(SQLiteDatabase db) {
        try {
            Log.d("onUpgrade37", "alterHDPSMasterSchemeTable called ................");
            // open();
            if (!checkColumnExist1(db, "HDPSMasterScheme", "allowpacket")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add alterHDPSMasterSchemeTable called ................");
                db.execSQL("ALTER TABLE HDPSMasterScheme ADD COLUMN  allowpacket TEXT");
            }
            if (!checkColumnExist1(db, "HDPSMasterScheme", "perCouponPkt")) {
                Log.d("onUpgrade37", "perCouponPkt Column not exist so add alterHDPSMasterSchemeTable called ................");
                db.execSQL("ALTER TABLE HDPSMasterScheme ADD COLUMN  perCouponPkt TEXT");
            }
            if (!checkColumnExist1(db, "HDPSMasterScheme", "retailerContraint")) {
                Log.d("onUpgrade37", "retailerContraint Column not exist so add alterHDPSMasterSchemeTable called ................");
                db.execSQL("ALTER TABLE HDPSMasterScheme ADD COLUMN  retailerContraint TEXT");
            }
//            db.close();
            //closeDB();

        } catch (Exception ex) {
            Log.d(TAG, "alterHDPSMasterSchemeTable: " + ex.toString());
        }
    }


    private void alterSamrudhhaKisanTable(SQLiteDatabase db) {
        try {
            Log.d("onUpgrade38", "alterHDPSMasterSchemeTable called ................");
            // open();


            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_dob")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN  farmer_dob TEXT DEFAULT ''");
            }
            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_anniversarydate")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_anniversarydate TEXT DEFAULT '' ");
            }
            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_pincode")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_pincode TEXT DEFAULT '' ");
            }
            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_landmark")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_landmark TEXT DEFAULT '' ");
            }

            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_house_latlong")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_house_latlong TEXT DEFAULT '' ");
            }

            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_house_address")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_house_address TEXT DEFAULT '' ");
            }

            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_photo_name")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_photo_name TEXT DEFAULT '' ");
            }

            if (!checkColumnExist1(db, "SamruddhaKisanValidationData", "farmer_photo_path")) {
                Log.d("onUpgrade37", "allowpacket Column not exist so add SamruddhaKisanValidationData called ................");
                db.execSQL("ALTER TABLE SamruddhaKisanValidationData ADD COLUMN farmer_photo_path TEXT DEFAULT '' ");
            }

//            db.close();
            //closeDB();

        } catch (Exception ex) {
            Log.d(TAG, "alterHDPSMasterSchemeTable: " + ex.toString());
        }
    }


    public void addEntryDateDemoplot(SQLiteDatabase db) {

        try {
            //SQLiteDatabase db = getWritableDatabase();
            open();
            if (!checkColumnExist1(db, "DemoModelData", "EntryDt")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN  EntryDt TEXT");
            }
            if (!checkColumnExist1(db, "DemoReviewData", "EntryDt")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN  EntryDt TEXT");
            }
            db.close();
            closeDB();

        } catch (Exception ex) {
            Log.d(TAG, "addEntryDateDemoplot: " + ex.toString());
        }


    }

    private void addnewcolomn(SQLiteDatabase db) {

        //Start
        //end
        try {

            if (!checkColumnExist1(db, "couponMaster", "entryDate")) {
                db.execSQL("ALTER TABLE couponMaster ADD COLUMN entryDate TEXT");
            }
            if (!checkColumnExist1(db, "couponPaymentAmount", "remainingAmount")) {
                db.execSQL("ALTER TABLE couponPaymentAmount ADD COLUMN remainingAmount TEXT");
            }
            if (!checkColumnExist1(db, "couponPaymentAmount", "newCouponScanAmount")) {
                db.execSQL("ALTER TABLE couponPaymentAmount ADD COLUMN newCouponScanAmount TEXT");
            }

            if (!checkColumnExist1(db, "KisanClubData", "EntryDt")) {
                db.execSQL("ALTER TABLE KisanClubData ADD COLUMN  EntryDt DATETIME DEFAULT (datetime('now','localtime'))");
            }

        } catch (Exception ex) {

        }


    }

    private void addCoupon6CouponRecordData(SQLiteDatabase db) {

        //Start
        //end
        try {
            if (!checkColumnExist1(db, "CouponRecordData", "couponQr6")) {
                db.execSQL("ALTER TABLE CouponRecordData ADD COLUMN couponQr6 TEXT");
            }
            if (!checkColumnExist1(db, "couponMaster", "entryDate")) {
                db.execSQL("ALTER TABLE couponMaster ADD COLUMN entryDate TEXT");
            }
            if (!checkColumnExist1(db, "couponPaymentAmount", "remainingAmount")) {
                db.execSQL("ALTER TABLE couponPaymentAmount ADD COLUMN remainingAmount TEXT");
            }
            if (!checkColumnExist1(db, "couponPaymentAmount", "newCouponScanAmount")) {
                db.execSQL("ALTER TABLE couponPaymentAmount ADD COLUMN newCouponScanAmount TEXT");
            }
            if (!checkColumnExist1(db, "KisanClubData", "comments")) {
                db.execSQL("ALTER TABLE KisanClubData ADD COLUMN comments TEXT");
            }
            if (!checkColumnExist1(db, "KisanClubData", "EntryDt")) {
                db.execSQL("ALTER TABLE KisanClubData ADD COLUMN  EntryDt DATETIME DEFAULT (datetime('now','localtime'))");
            }

        } catch (Exception ex) {

        }


    }

    private void UpdateFOCUSSEDVILLAGEMASTER(SQLiteDatabase db) {

        //Start

        //end
        try {
            if (!checkColumnExist1(db, "FocussedVillageMaster", "taluka")) {
                db.execSQL("ALTER TABLE FocussedVillageMaster ADD COLUMN taluka TEXT");
            }

        } catch (Exception ex) {

        }


    }

    private void updateATLWallpainting(SQLiteDatabase db) {

        //Start
        //end
        try {
            if (!checkColumnExist1(db, "ATLVillageWallPaintingData", "isSynced")) {
                db.execSQL("ALTER TABLE ATLVillageWallPaintingData ADD COLUMN isSynced TEXT");
            }


        } catch (Exception ex) {

        }


    }

    private void upgrateRetailerTableUpdate(SQLiteDatabase db) {

        //Start

        //end
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "type")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN type TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "mdocode")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN mdocode TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "coordinate")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN coordinate TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "startaddress")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN startaddress TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "marketplace")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN marketplace TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "retailername")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN retailername TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "age")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN age TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "dist")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN dist TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "taluka")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN taluka TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "retailerfirm")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN retailerfirm TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "mobileno")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN mobileno TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "asscoi_distributor")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN asscoi_distributor TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "keyretailer")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN keyretailer TEXT");
            }

            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "otherbussiness")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN otherbussiness TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "experianceSeedwork")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN experianceSeedwork TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "comments")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN comments TEXT");
            }
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "Status")) {
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN Status TEXT");
            }
        } catch (Exception ex) {

        }

        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "newfirm"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN newfirm TEXT");

        } catch (Exception e) {
            Log.e(TAG, "WhatsappNo..." + e.getMessage());
        }
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "birthdate"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN birthdate TEXT");

        } catch (Exception e) {
            Log.e(TAG, "WhatsappNo..." + e.getMessage());
        }
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "WhatsappNo"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN WhatsappNo TEXT");

        } catch (Exception e) {
            Log.e(TAG, "WhatsappNo..." + e.getMessage());
        }
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "state"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN state TEXT");

        } catch (Exception e) {
            Log.e(TAG, "state..." + e.getMessage());
        }
    }

    public void upgrate001(SQLiteDatabase db) // mahendra
    {


        try {
            String CREATE_couponmaster_TABLE = "CREATE  TABLE IF NOT EXISTS couponMaster(couponCode TEXT," +
                    "productCode TEXT,entryDate TEXT)";
            db.execSQL(CREATE_couponmaster_TABLE);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }


        try {
            String CREATE_couponpaymentamount_TABLE = "CREATE  TABLE IF NOT EXISTS " +
                    "couponPaymentAmount(paymentAmount TEXT,remainingAmount TEXT,newCouponScanAmount TEXT )";
            db.execSQL(CREATE_couponpaymentamount_TABLE);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }

        try {
            if (!checkColumnExist1(db, "CouponRecordData", "address"))
                db.execSQL("ALTER TABLE CouponRecordData ADD COLUMN address TEXT DEFAULT ''");
            Log.d(TAG, "CouponRecordData");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "CouponRecordData", "cordinates"))
                db.execSQL("ALTER TABLE CouponRecordData ADD COLUMN cordinates TEXT DEFAULT ''");
            Log.d(TAG, "CouponRecordData");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }

        try {
            String CREATE_TABLE_COUPONcheckupload = " CREATE  TABLE IF NOT EXISTS  CheckUploadCoupon(userCode TEXT," +
                    "mobileNumber TEXT,crop TEXT, product TEXT," +
                    "farmerPhoto TEXT )";
            db.execSQL(CREATE_TABLE_COUPONcheckupload);
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }


        try {
            String CREATE_TABLE_CHECKHYBRIDMASTER_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CHECKHYBRIDMASTER_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    "hybridName TEXT )";
            db.execSQL(CREATE_TABLE_CHECKHYBRIDMASTER_DATA);

            if (!checkColumnExist1(db, TABLE_FARMER_VISIT_DATA, "isSamruddhaKisan"))
                db.execSQL("ALTER TABLE " + TABLE_FARMER_VISIT_DATA + " ADD COLUMN isSamruddhaKisan TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_FARMER_VISIT_DATA + " ADD COLUMN aadharNumber TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_FARMER_VISIT_DATA + " ADD COLUMN emailID TEXT DEFAULT ''");
            Log.d(TAG, "focussedVillage");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }


        try {
            String CREATE_TABLE_CHECKHYBRIDMASTER_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CHECKHYBRIDMASTER_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    "hybridName TEXT )";
            db.execSQL(CREATE_TABLE_CHECKHYBRIDMASTER_DATA);

            if (!checkColumnExist1(db, TABLE_VALIDATEDDEMOMODELDATA, "focussedVillage"))
                db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOMODELDATA + " ADD COLUMN focussedVillage TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOMODELDATA + " ADD COLUMN checkHybridsSelected TEXT DEFAULT ''");

            Log.d(TAG, "focussedVillage");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
        try {
            if (!checkColumnExist1(db, TABLE_VALIDATEDDEMOREVIEWDATA, "focussedVillage"))
                db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN focussedVillage TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN fieldPest TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN fieldDiseases TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN fieldNutrients TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN fieldOther TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN cropCondition TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_VALIDATEDDEMOREVIEWDATA + " ADD COLUMN recommendations TEXT DEFAULT ''");

            Log.d(TAG, "focussedVillage");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    // change by mahendra 46
    public void upgrate46(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "InnovationData", "Year"))
                db.execSQL("ALTER TABLE InnovationData ADD COLUMN Year TEXT DEFAULT ''");
            Log.d(TAG, "InnovationData Year");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    // for Pre Season Activity
    public void upgrade50(SQLiteDatabase db) {
        // Testimonial Collection Data
        String CREATE_TABLE_TESTIMONIAL_COLLECTION_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_TESTIMONIAL_COLLECTION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', hardCopyTestimonialType TEXT DEFAULT '', videoTestimonialType TEXT, farmerName TEXT, farmerMobile TEXT,  " +
                "farmerPhotoName TEXT, farmerPhotoPath TEXT,  farmerPhotoStatus TEXT, " +
                "successPhotoName TEXT, successPhotoPath TEXT,  successPhotoStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_TESTIMONIAL_COLLECTION_DATA);


        // Pre Season - Sanman Mela Data
        String CREATE_TABLE_SANMAN_MELA_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SANMAN_MELA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "finalFarmerJSON TEXT, farmerCount TEXT,  " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_SANMAN_MELA_DATA);

        // Pre Season - Village Meeting Data
        String CREATE_TABLE_VILLAGE_MEETING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VILLAGE_MEETING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "selectRBM TEXT,  selectTBM TEXT, selectMDO TEXT, retailerDetails TEXT, talukaRetailer TEXT, farmerCount TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_VILLAGE_MEETING_DATA);

        // Pre Season - Promotion Data
        String CREATE_TABLE_PROMOTION_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_PROMOTION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, programName TEXT DEFAULT '', focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "retailerDetails TEXT, talukaRetailer TEXT, farmerCount TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_PROMOTION_DATA);


        // Pre Season - Crop Seminar Data
        String CREATE_TABLE_CROP_SEMINAR_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CROP_SEMINAR + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, state TEXT DEFAULT '',  district TEXT DEFAULT '', taluka TEXT DEFAULT '', " +
                "eventVenue TEXT DEFAULT '', cropType TEXT, product TEXT, taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT '',  " +
                "finalVillageJSON TEXT, selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, retailerCount TEXT, " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhotoPath TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhotoPath TEXT,  retailerListPhotoStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_CROP_SEMINAR_DATA);

        // Pre Season - Jeep campaigning
        String CREATE_TABLE_JEEP_CAMPAIGNING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_JEEP_CAMPAIGNING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                "userCode TEXT, state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '', cropType TEXT, product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "taggedAddressEnd TEXT DEFAULT '', taggedCordinatesEnd TEXT DEFAULT '', " +
                "OdometerReading TEXT DEFAULT '', startLocation TEXT DEFAULT '', endLocation TEXT DEFAULT '', rtoRegistrationNumber TEXT DEFAULT '', " +
                "OdometerReadingEnd TEXT DEFAULT '', finalPopupJson TEXT DEFAULT ''," +
                "isSynced TEXT DEFAULT '0',FinalSubmit TEXT DEFAULT '0', EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_JEEP_CAMPAIGNING_DATA);

//Pre season- Jeep campaigning location detail
        String CREATE_TABLE_JEEP_CAMPAIGNING_LOCATION_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_JEEP_CAMPAIGNING_LOC_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                "userCode TEXT, state TEXT DEFAULT '',villagePopup TEXT DEFAULT '', statePopup TEXT DEFAULT '', districtPopup TEXT DEFAULT ''," +
                "talukaPopup TEXT DEFAULT '',  startTime TEXT DEFAULT '', endTime TEXT DEFAULT '', taggedAddressPopup TEXT DEFAULT '', taggedCordinatesPopup TEXT DEFAULT ''," +
                "noOfFarmers TEXT DEFAULT '',  activityImgName TEXT DEFAULT '',  activityImgPath TEXT DEFAULT '',  activityImgStatus TEXT DEFAULT ''," +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_JEEP_CAMPAIGNING_LOCATION_DETAILS);

        // Pre Season - POP display
        String CREATE_TABLE_POP_DISPLAY_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_POP_DISPLAY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '',   marketName TEXT DEFAULT '', retailerDetails TEXT DEFAULT '' ,finalVillageJSON TEXT DEFAULT ''," +
                "photoOneImgName TEXT, photoOneImgPath TEXT,  photoOneImgStatus TEXT," +
                "photoTwoImgName TEXT, photoTwoImgPath TEXT,  photoTwoImgStatus TEXT," +
                "photoThreeImgName TEXT, photoThreeImgPath TEXT,  photoThreeImgStatus TEXT,isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_POP_DISPLAY_DATA);

    }

    public void upgrate47(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "MyActivityMaster", "activityType"))
                db.execSQL("ALTER TABLE MyActivityMaster ADD COLUMN activityType TEXT DEFAULT ''");
            Log.d(TAG, "MyActivityMaster activityType");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "MyActivityMaster", "activityTypeCode"))
                db.execSQL("ALTER TABLE MyActivityMaster ADD COLUMN activityTypeCode TEXT DEFAULT ''");
            Log.d(TAG, "MyActivityMaster activityTypeCode");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }


        String CREATE_TABLE_RETAILERTOVISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERTOVISITDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT,district TEXT,farmerName TEXT,farmerMobileNumber TEXT," +
                "farmerVillage TEXT,farmerTaluka TEXT,cropType TEXT, product TEXT,retailerCount TEXT,coordinates TEXT,address TEXT," +
                "activityImgName TEXT,activityImgPath TEXT ,farmerListImgName TEXT, farmerListImgPath TEXT, activityImgStatus TEXT, farmerListImgStatus TEXT, isSynced TEXT)";
        db.execSQL(CREATE_TABLE_RETAILERTOVISITDATA);


        String CREATE_TABLE_FOCUSSEDVILLAGEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_FOCUSSEDVILLAGEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,vil_code TEXT,vil_desc TEXT,taluka TEXT)";
        db.execSQL(CREATE_TABLE_FOCUSSEDVILLAGEMASTER);


        String CREATE_TABLE_LIVEPLANTDISPLAYVILLAGEDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_LIVEPLANTDISPLAYVILLAGEDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,uId TEXT, focussedVillage TEXT DEFAULT '',  state TEXT DEFAULT '',  " +
                "district TEXT DEFAULT '',  taluka TEXT DEFAULT '',  village TEXT DEFAULT '', " +
                "farmerCount TEXT,cropType TEXT,  product TEXT,taggedAddress TEXT DEFAULT ''," +
                "coordinates TEXT DEFAULT '', activityImgName TEXT, activityImgPath TEXT,  " +
                "activityImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_LIVEPLANTDISPLAYVILLAGEDATA);


        String CREATE_TABLE_LIVEPLANTDISPLAYRETAILERDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_LIVEPLANTDISPLAYRETAILERDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT,  state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',  marketPlace TEXT DEFAULT '', retailerDetails TEXT DEFAULT '', " +
                "farmerCount TEXT,cropType TEXT,  product TEXT,taggedAddress TEXT DEFAULT ''," +
                "coordinates TEXT DEFAULT '', activityImgName TEXT, activityImgPath TEXT,  " +
                "activityImgStatus TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_LIVEPLANTDISPLAYRETAILERDATA);


        String CREATE_TABLE_RETAILERDETAILSMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERDETAILSMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY ,  state TEXT,  type TEXT,  mdoCode TEXT,  marketPlace TEXT,  dist TEXT,  taluka TEXT, mobileNo TEXT, firmName TEXT,  retailercode TEXT,  retailerName TEXT)";
        db.execSQL(CREATE_TABLE_RETAILERDETAILSMASTER);

        String CREATE_RBM_MASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_RBM_MASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,code TEXT,description TEXT, TBMCode TEXT, TBMName TEXT)";
        db.execSQL(CREATE_RBM_MASTER);
        String CREATE_MDO_TBM_MASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_MDO_TBM + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdoCode TEXT,mdoDesc TEXT,tbmCode TEXT,tbmDesc TEXT)";
        db.execSQL(CREATE_MDO_TBM_MASTER);

        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "focussedVillage")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN focussedVillage TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData focussedVillage");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "fieldPest")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN fieldPest TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData fieldPest");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "fieldDiseases")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN fieldDiseases TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData fieldDiseases");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "fieldNutrients")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN fieldNutrients TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData fieldNutrients");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "fieldOther")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN fieldOther TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData fieldOther");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "cropCondition")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN cropCondition TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData cropCondition");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "recommendations")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN recommendations TEXT DEFAULT ''");
                Log.d(TAG, "DemoReviewData recommendations");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }


        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "checkHybridsSelected")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN checkHybridsSelected TEXT DEFAULT ''");
                Log.d(TAG, "TABLE_DEMOMODELDATA checkHybridsSelected");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }

        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "focussedVillage")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN focussedVillage TEXT DEFAULT ''");
                Log.d(TAG, "TABLE_DEMOMODELDATA focussedVillage");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }


        // Field Daya Data
        String CREATE_TABLE_FIELDDAYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDDAYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', pkFarmerMobileNumber TEXT, retailerCount TEXT, cropType TEXT,  " +
                "product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhoto TEXT, " +
                " retailerListPhotoStatus TEXT,isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),vollagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDDAYDATA);


        // Crop Show Data
        String CREATE_TABLE_CROPSHOWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_CROPSHOWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', retailerDetails TEXT,pkRetailerMobileNumber TEXT, cropType TEXT,  " +
                "talukaMDO TEXT, product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, " +
                "retailerListPhotoName TEXT, retailerListPhoto TEXT,  " +
                "retailerListPhotoStatus TEXT,isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_CROPSHOWDATA);

        // Harvest Daya Data
        String CREATE_TABLE_HARVESTDAYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_HARVESTDAYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, focussedVillage TEXT, otherVillage TEXT,   state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', farmerDetails TEXT DEFAULT '', retailerDetails TEXT, pkRetailerMobileNumber TEXT, cropType TEXT,  " +
                "product TEXT,taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', selectRBM TEXT, selectTBM TEXT, selectMDO TEXT, finalVillageJSON TEXT DEFAULT '', " +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT, " +
                " farmerListPhotoStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_HARVESTDAYDATA);

        // Purchase List Data
        String CREATE_TABLE_PURCHASELISTDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_PURCHASELISTDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', marketPlace TEXT DEFAULT '', retailerDetails TEXT, retailerNumber TEXT, taggedAddress TEXT DEFAULT '',coordinates TEXT DEFAULT '', " +
                "finalVillageJSON TEXT DEFAULT '', " +
                "farmerListPhotoName TEXT, farmerListPhoto TEXT,  farmerListPhotoStatus TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_PURCHASELISTDATA);

    }


    public void upgrate48(SQLiteDatabase db) {

        String CREATE_TABLE_FIELDBOARDDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDBOARDDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  boardCount TEXT DEFAULT '', " +
                " remarks TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '', farmerDetails TEXT, pkFarmerMobileNumber TEXT,cropType TEXT,product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "fieldBoardImgPathName TEXT, fieldBoardImgPath TEXT,  " +
                "fieldBoardImgStatus TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDBOARDDATA);


        String CREATE_TABLE_FIELDBANNERDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FIELDBANNERDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',bannerCount TEXT DEFAULT '',remarks TEXT DEFAULT '', farmerDetails TEXT, pkFarmerMobileNumber TEXT,cropType TEXT,product TEXT, " +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "fieldBannerImgName TEXT, fieldBannerImgPath TEXT,  fieldBannerImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FIELDBANNERDATA);

        String CREATE_TABLE_ATLEXHIBITIONDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATLEXHIBITIONDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',numberOfDays TEXT DEFAULT '',numberOfVisitors TEXT DEFAULT ''," +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "selectRBM TEXT DEFAULT '', selectTBM TEXT DEFAULT '', selectMDO TEXT DEFAULT ''," +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATLEXHIBITIONDATA);

        String CREATE_TABLE_ATL_MARKETDAY_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_MARKETDAY_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '', othervillage TEXT DEFAULT '',numberOfVisitors TEXT DEFAULT ''," +
                "taggedAddress TEXT DEFAULT '',taggedCordinates TEXT DEFAULT ''," +
                "activityImgName TEXT, activityImgPath TEXT,  activityImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_MARKETDAY_DATA);

        String CREATE_TABLE_ATL_WALL_PAINTING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_WALL_PAINTING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  selectedWallPaintingType TEXT DEFAULT '', focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT ''," +
                "strMandiName TEXT DEFAULT '',taggedAddress TEXT DEFAULT ''," +
                "taggedCordinates TEXT DEFAULT '',taggedAddressMandiNameStart TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameStart TEXT DEFAULT '',taggedAddressMandiNameEnd TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameEnd TEXT DEFAULT '',finalPopupJson TEXT," +
                "isSynced TEXT DEFAULT '0',FinalSubmit TEXT DEFAULT '0'," +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_WALL_PAINTING_DATA);


        String CREATE_TABLE_ATL_WALL_PAINTING_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_WALL_PAINTING_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, croptypePopup TEXT DEFAULT ''," +
                "productNamePopup TEXT DEFAULT ''," +
                "taggedAddressPopup TEXT DEFAULT ''," +
                "taggedCordinatesPopup TEXT DEFAULT '',strWidthInFt TEXT DEFAULT ''," +
                "strHeightInFt TEXT DEFAULT '',strTotalInSqFt TEXT DEFAULT ''," +
                "activityImgName TEXT DEFAULT '',activityImgPath TEXT DEFAULT '', activityImgStatus TEXT DEFAULT ''," +
                "strTotalWallPainted TEXT, isSynced TEXT, " +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_ATL_WALL_PAINTING_DETAILS);


        String CREATE_TABLE_ATL_POSTERING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_POSTERING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  selectedPosteringType TEXT DEFAULT '', focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT ''," +
                "strMandiName TEXT DEFAULT '',numberOfSpots TEXT DEFAULT '' , taggedAddress TEXT DEFAULT ''," +
                "taggedCordinates TEXT DEFAULT '',taggedAddressMandiNameStart TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameStart TEXT DEFAULT '',taggedAddressMandiNameEnd TEXT DEFAULT ''," +
                "taggedCordinatesMandiNameEnd TEXT DEFAULT '',finalPopupJson TEXT, " +
                " isSynced TEXT,FinalSubmit TEXT DEFAULT '0'," +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_POSTERING_DATA);


        String CREATE_TABLE_ATL_POSTERING_DETAILS = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_POSTERING_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT, croptypePopup TEXT DEFAULT ''," +
                "productNamePopup TEXT DEFAULT ''," +
                "taggedAddressPopup TEXT DEFAULT ''," +
                "taggedCordinatesPopup TEXT DEFAULT ''," +
                "activityImgName TEXT DEFAULT '',activityImgPath TEXT DEFAULT '', activityImgStatus TEXT DEFAULT ''," +
                "strTotalPosters TEXT, totalSpots TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_ATL_POSTERING_DETAILS);

        String CREATE_TABLE_ATL_TROLLEY_PAINTING_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_ATL_TROLLEY_PAINTING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,  focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '',  district TEXT DEFAULT '',  " +
                "taluka TEXT DEFAULT '',othervillage TEXT DEFAULT '', cropType TEXT DEFAULT ''," +
                "product TEXT DEFAULT '',farmerName TEXT DEFAULT '', farmerMobileNumber TEXT DEFAULT '', " +
                "trolleyNumber TEXT DEFAULT '', totalDimensionLeft TEXT DEFAULT '', totalDimensionRight TEXT DEFAULT ''," +
                "totalDimensionBack TEXT DEFAULT '', totalDimensionFront TEXT DEFAULT '',  " +
                "leftSideImgName TEXT, leftSideImgPath TEXT,  leftSideImgStatus TEXT," +
                "rightSideImgName TEXT, rightSideImgPath TEXT,  rightSideImgStatus TEXT," +
                "backSideImgName TEXT, backSideImgPath TEXT,  backSideImgStatus TEXT," +
                "frontSideImgName TEXT, frontSideImgPath TEXT,  frontSideImgStatus TEXT, " +
                "isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_ATL_TROLLEY_PAINTING_DATA);

        // General Activities, Distributor Visit
        String CREATE_TABLE_DISTRIBUTOR_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DISTRIBUTOR_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, marketPlace TEXT, distributerDetails TEXT, " +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_DISTRIBUTOR_VISITDATA);

        // General Activities, Retailer Visit
        String CREATE_TABLE_RETAILER_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILER_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, marketPlace TEXT, RetailerDetails TEXT, " +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_RETAILER_VISITDATA);

        // General Activities, Farmer Visit
        String CREATE_TABLE_FARMER_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_FARMER_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,focussedVillage TEXT, state TEXT, district TEXT, taluka TEXT, village TEXT, crop TEXT, " +
                "product TEXT,farmerName TEXT, mobileNumber TEXT, whatsappNumber TEXT, totalLand TEXT," +
                "comments TEXT, commentDesc TEXT, taggedAddress TEXT, taggedCordinates TEXT, " +
                "isSynced TEXT,isSamruddhaKisan TEXT,aadharNumber TEXT, emailID TEXT," +
                "EntryDt DATETIME DEFAULT (datetime('now','localtime')),villagecode TEXT)";
        db.execSQL(CREATE_TABLE_FARMER_VISITDATA);

        // General Activities, Samruddha Kisan Visit
        String CREATE_TABLE_SAMRUDDHA_KISAN_VISITDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SAMRUDDHA_KISAN_VISIT_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,focussedVillage TEXT, state TEXT, district TEXT, taluka TEXT, village TEXT, crop TEXT, " +
                "product TEXT,farmerName TEXT, mobileNumber TEXT, whatsappNumber TEXT, totalLand TEXT," +
                "aadharNumber TEXT, emailID TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_SAMRUDDHA_KISAN_VISITDATA);

        //  Samruddha Kisan validate
        String CREATE_TABLE_SAMRUDDHA_KISAN_VALIDATE_DATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "id TEXT,userCode TEXT DEFAULT '',focussedVillage TEXT DEFAULT '', state TEXT DEFAULT '', district TEXT DEFAULT '', taluka TEXT DEFAULT '', village TEXT DEFAULT '', crop TEXT DEFAULT '', " +
                "product TEXT DEFAULT '',farmerName TEXT DEFAULT '', mobileNumber TEXT DEFAULT '', whatsappNumber TEXT DEFAULT '', totalLand TEXT DEFAULT ''," +
                " taggedAddress TEXT DEFAULT '', taggedCordinates TEXT DEFAULT '',  comment TEXT DEFAULT '', commentDescription TEXT DEFAULT '',  mdoCode TEXT DEFAULT '',tbmCode TEXT DEFAULT '' ,mdoDesc ,tbmDesc TEXT,   validateBy TEXT DEFAULT '', action TEXT DEFAULT '', reasons TEXT DEFAULT '',  isSamruddhaKisan  TEXT DEFAULT '',isSynced TEXT DEFAULT '', EntryDt DATETIME DEFAULT (datetime('now','localtime'))" +
                "," +
                "farmer_dob TEXT DEFAULT '',\n" +
                "farmer_anniversarydate TEXT DEFAULT '',\n" +
                "farmer_pincode TEXT DEFAULT '',\n" +
                "farmer_landmark TEXT DEFAULT '',\n" +
                "farmer_house_latlong TEXT DEFAULT '',\n" +
                "farmer_house_address TEXT DEFAULT '',\n" +
                "farmer_photo_name TEXT DEFAULT '',\n" +
                "farmer_photo_path TEXT DEFAULT '')";
        db.execSQL(CREATE_TABLE_SAMRUDDHA_KISAN_VALIDATE_DATA); //  Samruddha Kisan validate


        // General Activities, Review meeting
        String CREATE_TABLE_REVIEW_MEETING = " CREATE  TABLE IF NOT EXISTS " + TABLE_REVIEW_MEETING_DATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "userCode TEXT,state TEXT, district TEXT, taluka TEXT, meetingPlace TEXT, meetingPurpose TEXT, " +
                "comments TEXT, taggedAddress TEXT, taggedCordinates TEXT, isSynced TEXT, EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_REVIEW_MEETING);

    }


    // Change by Mahendra
    public void upgrate45(SQLiteDatabase db) {


        try {
            if (!checkColumnExist1(db, "InnovationData", "PaddyTotalpkt"))
                db.execSQL("ALTER TABLE InnovationData ADD COLUMN PaddyTotalpkt TEXT DEFAULT ''");
            Log.d(TAG, "InnovationData PaddyTotalpkt");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "InnovationData", "PaddyMahycototalPkt"))
                db.execSQL("ALTER TABLE InnovationData ADD COLUMN PaddyMahycototalPkt TEXT DEFAULT '' ");
            Log.d(TAG, "InnovationData PaddyMahycototalPkt");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "InnovationData", "MaizeTotalpkt"))
                db.execSQL("ALTER TABLE InnovationData ADD COLUMN MaizeTotalpkt TEXT DEFAULT '' ");
            Log.d(TAG, "InnovationData MaizeTotalpkt");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "InnovationData", "MaizeMahycototalPkt"))
                db.execSQL("ALTER TABLE InnovationData ADD COLUMN MaizeMahycototalPkt TEXT DEFAULT '' ");
            Log.d(TAG, "InnovationData MaizeMahycototalPkt");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
    }

    public void upgrate41(SQLiteDatabase db) {


        try {
            String CREATE_TABLE_COUPONSCHEMEMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                    "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                    "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
            db.execSQL(CREATE_TABLE_COUPONSCHEMEMASTER);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
        try {
            String CREATE_TABLE_COUPONRECORDDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONRECORDDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                    "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT," +
                    "quantityPacket TEXT,packetsBooked TEXT,couponsIssued TEXT,totalAmount TEXT," +
                    "couponQr1 TEXT DEFAULT '',couponQr2 TEXT DEFAULT '',couponQr3 TEXT DEFAULT ''," +
                    "couponQr4 TEXT DEFAULT '',couponQr5 TEXT DEFAULT '',couponQr6 TEXT DEFAULT ''" +
                    ",farmerPhoto TEXT," +
                    "imgPath TEXT,imgStatus TEXT,isSynced TEXT,address TEXT,cordinates TEXT)";
            db.execSQL(CREATE_TABLE_COUPONRECORDDATA);


        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate42(SQLiteDatabase db) {


        try {
            String CREATE_TABLE_RETAILERSURVEYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERSURVEYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT ,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,territory TEXT,tehsil TEXT," +
                    "unitType TEXT DEFAULT '',retailerFirmName TEXT,contactPersonName TEXT,retailerNumber TEXT,retailerAddress TEXT, retailerCordinates TEXT," +
                    "whatsappNumber TEXT DEFAULT '' ,asscoidistributor TEXT,majorVegCrops TEXT,estimatedAvgVolumeSeedSold TEXT,estimatedVolumeMahycoSungro TEXT DEFAULT ''" +
                    ",mahycoProductSold TEXT ,sungroProductSold TEXT " +
                    ",retailerOtherCompany TEXT ,businessOtherThenSeed TEXT ,isSynced TEXT)";
            db.execSQL(CREATE_TABLE_RETAILERSURVEYDATA);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
        try {

            String CREATE_TABLE_KISANCLUBDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_KISANCLUBDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT ,timeStamp TEXT,userCode TEXT,state TEXT,territory TEXT,district TEXT,tehsil TEXT,village TEXT," +
                    "unitType TEXT DEFAULT '' , farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,vegetableChoice TEXT," +
                    "area TEXT DEFAULT '' ,cropSown TEXT,productAwareness TEXT,ratingMahyco TEXT  DEFAULT ''," +
                    "ratingSungro TEXT  DEFAULT '' ,isSynced TEXT,comments TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
            db.execSQL(CREATE_TABLE_KISANCLUBDATA);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
        try {

            String CREATE_TABLE_STATETERRITORYMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_STATETERRITORYMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,zone TEXT,territory TEXT,state TEXT,state_code TEXT )";
            db.execSQL(CREATE_TABLE_STATETERRITORYMASTER);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }


    }

    public void upgrate43(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "DemoModelData", "checkHybrids"))
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN checkHybrids TEXT DEFAULT ''");
            Log.d(TAG, "DemoModelData checkHybrids");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, "DemoModelData", "remarks"))
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN remarks TEXT DEFAULT '' ");
            Log.d(TAG, "DemoModelData remarks");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }

    }

    public void upgrate44(SQLiteDatabase db) {

        try {

            String CREATE_TABLE_VALIDATEDDEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                    "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,area TEXT,seedQuantity TEXT,unit TEXT,sowingDate TEXT,coordinates TEXT,soilType Text,irrigationMode Text,spacingRow TEXT,spacingPlan TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT, checkHybrids TEXT,remarks TEXT,mdoCode TEXT,entrydate TEXT,isUploaded TEXT)";
            db.execSQL(CREATE_TABLE_VALIDATEDDEMOMODELDATA);

            String CREATE_TABLE_VALIDATEDDEMOREVIEWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOREVIEWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                    "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate TEXT,sowingDate TEXT,coordinates TEXT,purposeVisit TEXT,comment TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT,mdoCode TEXT,entrydate TEXT,isUploaded TEXT)";
            db.execSQL(CREATE_TABLE_VALIDATEDDEMOREVIEWDATA);

            String CREATE_TABLE_MDOLISTDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_MDOLISTDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,user_code TEXT,MDO_name TEXT)";
            db.execSQL(CREATE_TABLE_MDOLISTDATA);

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate36(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "RetailerPOGTable", "RetailerName"))
                db.execSQL("ALTER TABLE RetailerPOGTable ADD COLUMN RetailerName TEXT");


        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate37(SQLiteDatabase db) {

        try {
            String createPaymentTable = "CREATE  TABLE IF NOT EXISTS PaymentTable(" +
                    "usercode TEXT," +
                    "rzpPaymentId TEXT," +
                    "orderId TEXT DEFAULT ''," +

                    "farmersIdList TEXT," +
                    "status TEXT," +
                    "amount TEXT)";
            db.execSQL(createPaymentTable);
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate38(SQLiteDatabase db) {

        try {
            String CREATE_TABLE_DEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                    "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,area TEXT,seedQuantity TEXT,unit TEXT,sowingDate TEXT,coordinates TEXT,soilType Text,irrigationMode Text,spacingRow TEXT,spacingPlan TEXT,imgName TEXT,imgPath TEXT" +
                    ",imgStatus TEXT,isSynced TEXT, checkHybrids TEXT,remarks TEXT,checkHybridsSelected TEXT,  focussedVillage TEXT)";
            db.execSQL(CREATE_TABLE_DEMOMODELDATA);


            String CREATE_TABLE_DEMOREVIEWDATA = "CREATE  TABLE IF NOT EXISTS DemoReviewData(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                    "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate TEXT,sowingDate TEXT,coordinates TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT)";
            db.execSQL(CREATE_TABLE_DEMOREVIEWDATA);
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }

    }

    public void upgrate39(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "DemoReviewData", "uIdP"))
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN uIdP TEXT");
            Log.d(TAG, "DemoReviewData uidp");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
    }


    public void upgrate40(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "purposeVisit")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN purposeVisit TEXT");
                Log.d(TAG, "DemoReviewData purposeVisit");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOREVIEWDATA, "comment")) {
                db.execSQL("ALTER TABLE DemoReviewData ADD COLUMN comment TEXT");
                Log.d(TAG, "DemoReviewData comment");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }

        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "soilType")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN soilType TEXT");
                Log.d(TAG, "TABLE_DEMOMODELDATA soilType");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "irrigationMode")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN irrigationMode TEXT");
                Log.d(TAG, "TABLE_DEMOMODELDATA irrigationMode");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }
        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "spacingRow")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN spacingRow TEXT");
                Log.d(TAG, "TABLE_DEMOMODELDATA spacingRow");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }

        try {
            if (!checkColumnExist1(db, TABLE_DEMOMODELDATA, "spacingPlan")) {
                db.execSQL("ALTER TABLE DemoModelData ADD COLUMN spacingPlan TEXT");
                Log.d(TAG, "TABLE_DEMOMODELDATA spacingPlan");
            }
        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());


        }


    }
//

    public void upgrate35(SQLiteDatabase db) {
        if (!DBTableExists(db, "DistributorAsRetailerNextBtPOGTable")) {
            String CREATE_PRODUCTS_TABLE36 = "CREATE  TABLE DistributorAsRetailerNextBtPOGTable(usercode TEXT," +
                    "DistrCode TEXT," +
                    "ProductName TEXT,ProductCode TEXT," +
                    "BalStock TEXT," +
                    "actionstatus TEXT,Qty TEXT,Date TEXT,AssociateDistributor TEXT," +
                    "Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE36);

        }
    }

    public void upgrate34(SQLiteDatabase db) {
        if (!DBTableExists(db, "DistributorAsRetailerCompetitatorPOGTable")) {
            String CREATE_PRODUCTS_TABLE35 = "CREATE  TABLE DistributorAsRetailerCompetitatorPOGTable(usercode TEXT," +
                    "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                    "CropName TEXT,CropCode TEXT," +
                    "CompanyName TEXT," +
                    "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                    "BalStock TEXT ,stockDate TEXT,Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE35);

        }
    }

    public void upgrate33(SQLiteDatabase db) {
        if (!DBTableExists(db, "DistributorAsRetailerPOGTable")) {
            String CREATE_PRODUCTS_TABLE34 = "CREATE  TABLE DistributorAsRetailerPOGTable(usercode TEXT," +
                    "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                    "CropName TEXT,CropCode TEXT," +
                    "ProductName TEXT,ProductCode TEXT," +
                    "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                    "BalStock TEXT ,stockDate TEXT,Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE34);

        }
    }

    public void upgrate32(SQLiteDatabase db) {

        try {
            if (!checkColumnExist1(db, "mdo_pogsapdata", "Plan_Qty"))
                db.execSQL("ALTER TABLE mdo_pogsapdata ADD COLUMN Plan_Qty TEXT");

            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate31(SQLiteDatabase db) {
        if (!DBTableExists(db, "MDO_tagRetailerList")) {
            String CREATE_PRODUCTS_TABLE33 = "CREATE  TABLE MDO_tagRetailerList(state TEXT," +
                    "type TEXT ,mdocode TEXT,marketplace TEXT," +
                    "dist TEXT,taluka TEXT," +
                    "mobileno TEXT,firmname TEXT,name TEXT ,retailercode TEXT,retailerCategory TEXT )";
            db.execSQL(CREATE_PRODUCTS_TABLE33);
        }
    }

    private void upgrate30(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "CropMaster", "Crop_Code"))
                db.execSQL("ALTER TABLE CropMaster ADD COLUMN Crop_Code TEXT");
            db.execSQL("ALTER TABLE CropMaster ADD COLUMN Prod_Code TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }
    }

    public void upgrate29(SQLiteDatabase db) {
        if (!DBTableExists(db, "mdo_pogsapdata")) {
            String CREATE_PRODUCTS_TABLE32 = "CREATE  TABLE mdo_pogsapdata(Cust_Code TEXT," +
                    "Crop_Name TEXT,Crop_Code TEXT,Prod_Code TEXT," +
                    "Prod_Name TEXT,QTY_Unit TEXT," +
                    "QTY_Base TEXT,Plan_Qty TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE32);
        }
    }

    public void upgrate28(SQLiteDatabase db) {
        if (!DBTableExists(db, "DistributorNextBtPOGTable")) {
            String CREATE_PRODUCTS_TABLE31 = "CREATE  TABLE DistributorNextBtPOGTable(usercode TEXT," +
                    "Mobileno TEXT,DistrCode TEXT," +
                    "ProductName TEXT,ProductCode TEXT," +
                    "BalStock TEXT," +
                    "actionstatus TEXT,Qty TEXT,Date  TEXT," +
                    "Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE31);
        }
    }

    public void upgrate27(SQLiteDatabase db) {
        if (!DBTableExists(db, "DistributorPOGTable")) {
            String CREATE_PRODUCTS_TABLE30 = "CREATE  TABLE DistributorPOGTable(usercode TEXT," +
                    "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,Mobileno TEXT,DistrCode TEXT," +
                    "CropName TEXT,CropCode TEXT," +
                    "ProductName TEXT,ProductCode TEXT, plan TEXT," +
                    "RecStockdepot TEXT,RecStockdistr TEXT,ShiftStockToDistr TEXT,NetStock TEXT," +
                    "BalStock TEXT ,placementstock TEXT,stockDate TEXT,Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE30);
        }
    }


    public void upgrate22(SQLiteDatabase db) {
        if (!DBTableExists(db, "RetailerCompetitatorPOGTable")) {
            String CREATE_PRODUCTS_TABLE27 = "CREATE  TABLE RetailerCompetitatorPOGTable(usercode TEXT," +
                    "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                    "CropName TEXT,CropCode TEXT," +
                    "CompanyName TEXT," +
                    "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                    "BalStock TEXT ,stockDate TEXT,Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE27);
        }
    }

    public void upgrate21(SQLiteDatabase db) {
        if (!DBTableExists(db, "RetailerPOGTable")) {
            String CREATE_PRODUCTS_TABLE25 = "CREATE  TABLE RetailerPOGTable(usercode TEXT," +
                    "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                    "CropName TEXT,CropCode TEXT," +
                    "ProductName TEXT,ProductCode TEXT," +
                    "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                    "BalStock TEXT ,stockDate TEXT,Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE25);
        }
    }

    public void upgrate20(SQLiteDatabase db) {
        if (!DBTableExists(db, "SAPCROPMaster")) {
            String CREATE_PRODUCTS_TABLE25 = "CREATE  TABLE SAPCROPMaster(ProductName TEXT,PRoductCode TEXT," +
                    "CropName TEXT,CropCode TEXT,CropType TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE25);
        }
    }

    private void upgrate19(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "mdo_demandassesmentsurvey", "address"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN address TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "address..." + e.getMessage());
        }
    }

    public Cursor fetchStateDistTaluka(String txtmobile) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select distinct state,dist,taluka from mdo_Retaileranddistributordata where mobileno='" + txtmobile + "'  and type ='Retailer'", null);
        return cursor;


    }

    public Cursor logindetail(String pass, String username) {
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "select  *  from UserMaster where Password='" + pass.trim() + "' and UserCode='" + username.toUpperCase().trim() + "'";
        Cursor cursor = db.rawQuery(searchQuery, null);
        return cursor;


    }


    private void upgrate18(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "mdo_demandassesmentsurvey", "coordinate"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN coordinate TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "coordinate..." + e.getMessage());
        }
    }

    private void upgrate17(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "WhatsappNo"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN WhatsappNo TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "WhatsappNo..." + e.getMessage());
        }
    }

    private void upgrate16(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "birthdate"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN birthdate TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "birthdate..." + e.getMessage());
        }
    }

    private void upgrate15(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "mdo_Retaileranddistributordata", "newfirm"))
                db.execSQL("ALTER TABLE mdo_Retaileranddistributordata ADD COLUMN newfirm TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }

    private void upgrate14(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "FarmerMaster", "usercode"))
                db.execSQL("ALTER TABLE FarmerMaster ADD COLUMN usercode TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }

    private void upgrate13(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "UserMaster", "unit"))
                db.execSQL("ALTER TABLE UserMaster ADD COLUMN unit TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }

    public void upgrate12(SQLiteDatabase db) {
        if (!DBTableExists(db, TABLE_PRODUCTS15)) {
            String CREATE_PRODUCTS_TABLE15 = "CREATE    TABLE " + TABLE_PRODUCTS15 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT,dist TEXT,taluka TEXT,marketplace TEXT," +
                    "retailername TEXT ,retailerfirm TEXT,age TEXT, mobileno TEXT,asscoi_distributor TEXT,keyretailer TEXT," +
                    "otherbussiness TEXT,experianceSeedwork TEXT, comments TEXT,Status TEXT,type TEXT,newfirm TEXT,birthdate TEXT,WhatsappNo TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE15);
        }
    }

    public void upgrate11(SQLiteDatabase db) {
        if (!DBTableExists(db, TABLE_PRODUCTS16)) {
            String CREATE_PRODUCTS_TABLE16 = "CREATE    TABLE " + TABLE_PRODUCTS16 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                    "qty TEXT,retailermobile TEXT," +
                    "Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE16);
        }
    }

    public void upgrate10(SQLiteDatabase db) {
        if (!DBTableExists(db, "Commentlist")) {
            String CREATE_PRODUCTS_TABLE10 = "CREATE   TABLE  Commentlist(comment TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE10);
        }
    }

    public void upgrate9(SQLiteDatabase db) {
        if (!DBTableExists(db, "Tempstockdata")) {
            String CREATE_PRODUCTS_TABLE9 = "CREATE    TABLE Tempstockdata (id TEXT,visitno TEXT,crop TEXT,product TEXT,currentstock TEXT,stock TEXT,salestock TEXT,status TEXT,INTime DATE)";
            db.execSQL(CREATE_PRODUCTS_TABLE9);
        }
    }

    private void upgrate8(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "TagData", "commentdecription"))
                db.execSQL("ALTER TABLE TagData ADD COLUMN commentdecription TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }


    private void upgrate7(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "UserMaster", "RoleID"))
                db.execSQL("ALTER TABLE UserMaster ADD COLUMN RoleID TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }

    public void CompanyNamemaster(SQLiteDatabase db) {
        if (!DBTableExists(db, "CompanyNamemaster")) {
            String CREATE_PRODUCTS_TABLE23 = "CREATE    TABLE  CompanyNamemaster(" + COLUMN_ID + " INTEGER PRIMARY KEY,Compname TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE23);
            //db.close();
        }
    }

    public void authoriseddistributorproduct(SQLiteDatabase db) {
        if (!DBTableExists(db, "mdo_authoriseddistributorproduct")) {
            String CREATE_PRODUCTS_TABLE22 = "CREATE    TABLE " + TABLE_PRODUCTS22 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                    "qty TEXT,retailermobile TEXT," +
                    "Status TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE22);
            //db.close();
        }
    }

    public void mdo_cultivation(SQLiteDatabase db) {
        try {
            if (!DBTableExists(db, "mdo_cultivation")) {
                String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE mdo_demandassesmentsurvey(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,state TEXT,dist TEXT,taluka TEXT,village TEXT," +
                        "mobileno TEXT,awaremahycoprd TEXT,entrydate DATE," +
                        "Status TEXT,farmername TEXT,crop TEXT)";
                db.execSQL(CREATE_PRODUCTS_TABLE18);

                String CREATE_PRODUCTS_TABLE19 = "CREATE  TABLE mdo_cultivation (" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                        "mobileno TEXT,underhydarea TEXT,performance TEXT,entrydate DATE," +
                        "Status TEXT)";
                db.execSQL(CREATE_PRODUCTS_TABLE19);
                String CREATE_PRODUCTS_TABLE20 = "CREATE    TABLE mdo_cultivationtobe(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                        "mobileno TEXT,underhydarea TEXT,reason TEXT,entrydate DATE," +
                        "Status TEXT)";
                db.execSQL(CREATE_PRODUCTS_TABLE20);

                String CREATE_PRODUCTS_TABLE21 = "CREATE    TABLE mdo_awaremahycoproduct(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,hybridsown TEXT," +
                        "mobileno TEXT,typestatus TEXT,reason TEXT,entrydate DATE," +
                        "Status TEXT)";
                db.execSQL(CREATE_PRODUCTS_TABLE21);

                // db.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "mdo_cultivation..." + e.getMessage());
        }

    }

    private void upgrate6(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "RetailerMaster", "dist"))
                db.execSQL("ALTER TABLE RetailerMaster ADD COLUMN dist TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void upgrate4(SQLiteDatabase db) {
        try {


            if (!checkColumnExist1(db, "FarmerMaster", "statename"))
                db.execSQL("ALTER TABLE FarmerMaster ADD COLUMN statename TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");
        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }

    }

    private void upgrate5(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "FarmerMaster", "dist"))
                db.execSQL("ALTER TABLE FarmerMaster ADD COLUMN dist TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");
        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }

    }

    public void mdophoto(SQLiteDatabase db) {
        try {


            if (!DBTableExists(db, "mdo_photoupdate")) {
                String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE mdo_photoupdate(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                        "mdocode TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
                db.execSQL(CREATE_PRODUCTS_TABLE18);
                // db.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }


    private void upgradeto2and3(SQLiteDatabase db) {
        try {
            if (!checkColumnExist1(db, "FarmerMaster", "TotalLand"))
                db.execSQL("ALTER TABLE FarmerMaster ADD COLUMN TotalLand TEXT");
            //if (!isColumnExists(db, "DutyDetails", "locationProviderOffDuty"))

            //   db.execSQL("ALTER TABLE DutyDetails ADD COLUMN locationProviderOffDuty TEXT");

        } catch (Exception e) {
            Log.e(TAG, "totalland..." + e.getMessage());
        }
    }

    private boolean DBTableExists(SQLiteDatabase db, String tableName) {
        boolean result = false;
        Cursor cursor = null;
        int cnt = 0;
        try {
            //Query a line
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='" + tableName + "'"
                    , null);
            cnt = cursor.getCount();
            if ((cursor != null && cursor.getCount() == 1)) {
                result = true;
            }

        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists1..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        //db.close();
        return result;
    }

    private boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        int cnt = 0;
        try {
            //Query a line
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
            cnt = cursor.getColumnIndex(columnName);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
            // db.close();
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists1..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // db.close();
        return result;
    }

    public synchronized JSONArray getjsonResults(String Query) {

        SQLiteDatabase db = getReadableDatabase();
        JSONArray resultSet = new JSONArray();
        try {


            String myTable = "Table1";//Set name of your table
            String searchQuery = Query;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {

                            if (cursor.getString(i) != null) {


                                Log.d("TAG_NAME", cursor.getString(i));
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));

                            } else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }

                        } catch (Exception e) {
                            Log.d("TAG_NAME", e.getMessage());
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //db.close();
        return resultSet;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            //sqlDB.close();
            return alc;
        }
    }

    public boolean insertNotification(String Title, String Message, String Time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title", Title);
        contentValues.put("Message", Message);
        contentValues.put("Time", Time);
        db.insert(TABLE_PRODUCTS23, null, contentValues);
        // db.close();
        return true;
    }

    public boolean insertPaymentTable(String usercode, String rzpPaymentId, JSONArray jsonArray, String status, String amount) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("rzpPaymentId", rzpPaymentId);
        contentValues.put("farmersIdList", jsonArray.toString());
        contentValues.put("status", status);
        contentValues.put("amount", amount);
        db.insert(TABLE_PAYMENT, null, contentValues);
        // db.close();
        return true;
    }

    public boolean updatePaymentTable(String paymentId, String orderID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("orderId", orderID);


        boolean result = db.update(TABLE_PAYMENT, initialValues, "rzpPaymentId" + "='"
                + paymentId + "'", null) > 0;

        db.close();
        return result;

    }

    public synchronized void deleledata(String TABLE_NAME, String Where) {
        // SQLiteDatabase db = getWritableDatabase();
        try {

            //db.execSQL("delete from  " + TABLE_NAME + "  " + Where);
            //db.close();
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            db.close();
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public synchronized void Updatedata(String updatesting) {
        SQLiteDatabase db = getWritableDatabase();
        try {

            db.execSQL(updatesting);
            // db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            //db.close();
        }
    }

    public synchronized void deleterecord(String updatesting) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(updatesting);
            // db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public synchronized JSONArray getResults(String Query) {
        SQLiteDatabase db = getReadableDatabase();
        JSONArray resultSet = new JSONArray();
        try {


            String myTable = "Table1";//Set name of your table
            String searchQuery = Query;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            // img.setImageBitmap(getImageFromBLOB(cursor.getBlob(cursor.getColumnIndex("image"))));
                            if (i == 19 || i == 21) {
                                if (cursor.getBlob(i) != null) {
                                    // rowObject.put(cursor.getColumnName(i), cursor.getBlob(i).toString());
                                    // String str=Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT);
                                    // rowObject.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT));
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                } else {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                            } else {
                                if (cursor.getString(i) != null) {
                                    Log.d("TAG_NAME", cursor.getString(i));
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                } else {
                                    rowObject.put(cursor.getColumnName(i), "");
                                }
                            }
                        } catch (Exception e) {
                            Log.d("TAG_NAME", e.getMessage());
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //db.close();
        return resultSet;
    }

    public synchronized JSONArray getResultsVillageDetails(String Query) {
        SQLiteDatabase db = getReadableDatabase();
        JSONArray resultSet = new JSONArray();
        try {
            String myTable = "Table1";//Set name of your table
            String searchQuery = Query;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                Log.d("TAG_NAME", cursor.getString(i));
                                if (cursor.getColumnName(i).equalsIgnoreCase("finalVillageJSON")) {
                                    JSONArray jsonArray = new JSONArray(cursor.getString(i));
                                    rowObject.put(cursor.getColumnName(i), jsonArray);
                                } else {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                            } else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }
                        } catch (Exception e) {
                            Log.d("TAG_NAME", e.getMessage());
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // db.close();
        return resultSet;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return context.getCacheDir() + fileName;
    }

    public Bitmap BITMAP_RESIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    private static Bitmap createScaledBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return scaledBitmap;

    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


    //Start
    public static Bitmap compressImage(String str) {
        Bitmap scaledBitmap = null;
        try {
            File f = new File(str);


            BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
            // float maxHeight = 816.0f;
            //float maxWidth = 612.0f;
            float maxHeight = 516.0f;
            float maxWidth = 412.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                //bmp = BitmapFactory.decodeFile(filePath, options);
                bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif = null;
            try {
                try {
                    exif = new ExifInterface(f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }
    //end

    public String getImageDatadetail(String path) {
        String myTable = "Table1";//Set name of your table
        String str = "";
        try {
            if (path != null || path.length() > 0) {
                str = path;//Base64.encodeToString(cursor.getBlob(cursor.getColumnIndex(colname)),Base64.DEFAULT);
                // rowObject.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT));
                File f = new File(str);
                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                // original measurements
                int origWidth = b.getWidth();
                int origHeight = b.getHeight();
                final int destWidth = 200;//or the width you need
                if (origWidth > destWidth) {
                    // picture is wider than we want it, we calculate its target height
                    int destHeight = origHeight / (origWidth / destWidth);
                    // we create an scaled bitmap so it reduces the image, not just trim it
                    // Bitmap b2 = Bitmap.createScaledBitmap(b, 400, 350, false);
                    Bitmap b2 = compressImage(str);//scaleBitmap(b,400,400);


                    // 70 is the 0-100 quality percentage
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    b2.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } else {
                    // 70 is the 0-100 quality percentage
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
            }


        } catch (Exception e) {
            Log.d("TAG_NAME", e.getMessage());
            //Balnk image 64 data
            str = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABgCAAAAADOGIieAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAAFLaVRYdFhNTDpjb20uYWRvYmUueG1wAAAAAAA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzEzOCA3OS4xNTk4MjQsIDIwMTYvMDkvMTQtMDE6MDk6MDEgICAgICAgICI+CiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiLz4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InIiPz4gSa46AAAEzElEQVRo3u3Y6VMTSRjAYf/O3RJFhAUMYHGICoEgkAnXElgQgqxoRJCoeIBmWUVRUNSEcAQCAYGSSyAqCYI5Z377IUCpJbU4GfbD1vSXnu7pmic9M+873TnCf1COqIiKqIiKqIiKAJdK9PpSYe1QkdamD8vLq66s8CEiUkq0Lpo8zJkkR6v6qcNB1jvMZvO15M6rZrPZktvQat6ntC/KRx5pHg2NDg9NO+x2u93mHLbvUxzP0jvlIssnJZj2Mef59zuSMSwT0b2FqZ5m97POpf2GfPJ6vd6NL/A5Wx4SOQUYCJYGXY59hlguCIIgGHSLkOSVh6TC3G3vk8FJ28A+U8lAEkWRP1+CZkMu4njc2Vtnvdjf2zX3wyFZ0ar5FWhkz+Tx+q1b7wOz9ZNvJnZ7w35/aG9IZrRqGYwF4eFVD2BrH9rps5p0Wm1hffeOkzNms9lsTn1MM4FqfJ7A6Fi0py3r8rjfHwhMXc8xRQBW9NXV1dUVFik2ZLr77/6eh+sACxkdn3fPBaypE9+OjgUhCEQA5hO/efjLmhHlkL2wO/bdVYKJ84ojaTu3Z8U1sRw9mj+pNDJgAGCtrKDRVFT8Nvrm3lIWkTQigDtxDGD2lA2ApE1FkalzABvHd16vSPx7AMOoosjdLgDTk932SCnAYIuiyB92gLS9D8t2EsB4haKIcQQgRdzryI0Ac8XKIg6AxOBXSR54W6oo0toPoN2L+TUNgKNGUeSFCeBR+W67+Q7Avb8URdajx8LtaPPpWREgc07ZiC9wAVBYOAMLhjMhgI+nFU4r279JALjLz5/T7yx/Ti8qnSBb6r4/31GueBZG6PhueZmr/PcE8r/55aYs8TAQelKsvp0n1JdhgUNBWDJnlrY9tN4wZDbNogwSTv1B55r1pqXrR+vJU/JWkKT/zEYncUsecrnz4MaLKrmboPjXBzWmfg3LRfxCnvEgpUar88WwMV2aP1BZUP/2UJGfR0SISLC5Hc1moRCIIoSBjxKiXwwHJALemBDn8VUKeqks1rYAS8fK8hswmPH8wlaWMXtkWptTIGzeyNPrY0HcKRWUvXHmsaVZgZWzkUCa7+JttpKptjB6FCpespn0CV1vDIirqnyuduhuHRgG4X32jD0OU/m1lkSKZthIAGGA8SK40hALUsoZw/D9WtC/gtW0xnondVcXncnoJtk4BsIArgK4bIoBGc+nO/71hyTvbJwflrMBjB34Emgt5146lDyHo+PbqVMxINNGglV2HhcWjQCrwhfgygM28qAp3+iBi29gorjggRqMX8UjIB0icv4maDy8FhiOA3caxn6Allq4V3ih0ruZCbhzi/OuyEfCJ9JB46DyDkK6E9dxhD5gS1syT1sdjaZgMuDSIYbkI9cfVE3TVyMlQ86IgckEyp4B9pqe+3RULhS4vqQCk2efWz3ykXx3eyMrSWuZdFWJyVvTCZQ9B2qsdg0dwlirJRRFnnSvy0bcWc3N53yUlQ1Q0tB0ZmQ2HsNTKUB8Q1OSaKljMEVKkQLS5IVoXpaHtLVCuoP+dM+7OHhR8u4EtcUVDX2lcKmz67xRO/q5sEJofXf695J22UgQiIgQRgoBQUJEQoFgOAJSWAoHJIgEAyHCwUBIjXgVUREVUREV+V8g/wDYLyRpT80RmQAAAABJRU5ErkJggg==";

        }
        //  }


        return str;
    }

    public String getImageData(String Query, String colname) {

        SQLiteDatabase db = getReadableDatabase();
        String myTable = "Table1";//Set name of your table
        String str = "";
        String searchQuery = Query;
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        try {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                // for( int i=0 ;  i< totalColumn ; i++ )
                for (int i = 0; i < 1; i++) {
                    // if( cursor.getColumnName(i) != null )
                    // {
                    try {

                        // img.setImageBitmap(getImageFromBLOB(cursor.getBlob(cursor.getColumnIndex("image"))));
                        //if (cursor.getBlob(cursor.getColumnIndex(colname))!=null) {
                        if (cursor.getString(cursor.getColumnIndex(colname)) != null) {
                            // rowObject.put(cursor.getColumnName(i), cursor.getBlob(i).toString());
                            str = cursor.getString(cursor.getColumnIndex(colname));//Base64.encodeToString(cursor.getBlob(cursor.getColumnIndex(colname)),Base64.DEFAULT);
                            // rowObject.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT));
                            File f = new File(str);
                            Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());


                            // original measurements
                            int origWidth = b.getWidth();
                            int origHeight = b.getHeight();
                            final int destWidth = 200;//or the width you need
                            if (origWidth > destWidth) {
                                // picture is wider than we want it, we calculate its target height
                                int destHeight = origHeight / (origWidth / destWidth);
                                // we create an scaled bitmap so it reduces the image, not just trim it
                                // Bitmap b2 = Bitmap.createScaledBitmap(b, 400, 350, false);
                                Bitmap b2 = compressImage(str);//scaleBitmap(b,400,400);
                                // Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
                                //
                                //b is the Bitmap

                                //calculate how many bytes our image consists of.
                                // int bytes = b2.getByteCount();
                                // ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                                // b2.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                                //  byte[] array = buffer.array(); //Get the underlying array containing the data.
                                //
                                // 70 is the 0-100 quality percentage
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                b2.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            } else {
                                // 70 is the 0-100 quality percentage
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                b.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            }


                        } else {
                            str = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABgCAAAAADOGIieAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAAFLaVRYdFhNTDpjb20uYWRvYmUueG1wAAAAAAA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzEzOCA3OS4xNTk4MjQsIDIwMTYvMDkvMTQtMDE6MDk6MDEgICAgICAgICI+CiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiLz4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InIiPz4gSa46AAAEzElEQVRo3u3Y6VMTSRjAYf/O3RJFhAUMYHGICoEgkAnXElgQgqxoRJCoeIBmWUVRUNSEcAQCAYGSSyAqCYI5Z377IUCpJbU4GfbD1vSXnu7pmic9M+873TnCf1COqIiKqIiKqIiKAJdK9PpSYe1QkdamD8vLq66s8CEiUkq0Lpo8zJkkR6v6qcNB1jvMZvO15M6rZrPZktvQat6ntC/KRx5pHg2NDg9NO+x2u93mHLbvUxzP0jvlIssnJZj2Mef59zuSMSwT0b2FqZ5m97POpf2GfPJ6vd6NL/A5Wx4SOQUYCJYGXY59hlguCIIgGHSLkOSVh6TC3G3vk8FJ28A+U8lAEkWRP1+CZkMu4njc2Vtnvdjf2zX3wyFZ0ar5FWhkz+Tx+q1b7wOz9ZNvJnZ7w35/aG9IZrRqGYwF4eFVD2BrH9rps5p0Wm1hffeOkzNms9lsTn1MM4FqfJ7A6Fi0py3r8rjfHwhMXc8xRQBW9NXV1dUVFik2ZLr77/6eh+sACxkdn3fPBaypE9+OjgUhCEQA5hO/efjLmhHlkL2wO/bdVYKJ84ojaTu3Z8U1sRw9mj+pNDJgAGCtrKDRVFT8Nvrm3lIWkTQigDtxDGD2lA2ApE1FkalzABvHd16vSPx7AMOoosjdLgDTk932SCnAYIuiyB92gLS9D8t2EsB4haKIcQQgRdzryI0Ac8XKIg6AxOBXSR54W6oo0toPoN2L+TUNgKNGUeSFCeBR+W67+Q7Avb8URdajx8LtaPPpWREgc07ZiC9wAVBYOAMLhjMhgI+nFU4r279JALjLz5/T7yx/Ti8qnSBb6r4/31GueBZG6PhueZmr/PcE8r/55aYs8TAQelKsvp0n1JdhgUNBWDJnlrY9tN4wZDbNogwSTv1B55r1pqXrR+vJU/JWkKT/zEYncUsecrnz4MaLKrmboPjXBzWmfg3LRfxCnvEgpUar88WwMV2aP1BZUP/2UJGfR0SISLC5Hc1moRCIIoSBjxKiXwwHJALemBDn8VUKeqks1rYAS8fK8hswmPH8wlaWMXtkWptTIGzeyNPrY0HcKRWUvXHmsaVZgZWzkUCa7+JttpKptjB6FCpespn0CV1vDIirqnyuduhuHRgG4X32jD0OU/m1lkSKZthIAGGA8SK40hALUsoZw/D9WtC/gtW0xnondVcXncnoJtk4BsIArgK4bIoBGc+nO/71hyTvbJwflrMBjB34Emgt5146lDyHo+PbqVMxINNGglV2HhcWjQCrwhfgygM28qAp3+iBi29gorjggRqMX8UjIB0icv4maDy8FhiOA3caxn6Allq4V3ih0ruZCbhzi/OuyEfCJ9JB46DyDkK6E9dxhD5gS1syT1sdjaZgMuDSIYbkI9cfVE3TVyMlQ86IgckEyp4B9pqe+3RULhS4vqQCk2efWz3ykXx3eyMrSWuZdFWJyVvTCZQ9B2qsdg0dwlirJRRFnnSvy0bcWc3N53yUlQ1Q0tB0ZmQ2HsNTKUB8Q1OSaKljMEVKkQLS5IVoXpaHtLVCuoP+dM+7OHhR8u4EtcUVDX2lcKmz67xRO/q5sEJofXf695J22UgQiIgQRgoBQUJEQoFgOAJSWAoHJIgEAyHCwUBIjXgVUREVUREV+V8g/wDYLyRpT80RmQAAAABJRU5ErkJggg==";


                        }

                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                        str = "iVBORw0KGgoAAAANSUhEUgAAAGQAAABgCAAAAADOGIieAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAAFLaVRYdFhNTDpjb20uYWRvYmUueG1wAAAAAAA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzEzOCA3OS4xNTk4MjQsIDIwMTYvMDkvMTQtMDE6MDk6MDEgICAgICAgICI+CiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiLz4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InIiPz4gSa46AAAEzElEQVRo3u3Y6VMTSRjAYf/O3RJFhAUMYHGICoEgkAnXElgQgqxoRJCoeIBmWUVRUNSEcAQCAYGSSyAqCYI5Z377IUCpJbU4GfbD1vSXnu7pmic9M+873TnCf1COqIiKqIiKqIiKAJdK9PpSYe1QkdamD8vLq66s8CEiUkq0Lpo8zJkkR6v6qcNB1jvMZvO15M6rZrPZktvQat6ntC/KRx5pHg2NDg9NO+x2u93mHLbvUxzP0jvlIssnJZj2Mef59zuSMSwT0b2FqZ5m97POpf2GfPJ6vd6NL/A5Wx4SOQUYCJYGXY59hlguCIIgGHSLkOSVh6TC3G3vk8FJ28A+U8lAEkWRP1+CZkMu4njc2Vtnvdjf2zX3wyFZ0ar5FWhkz+Tx+q1b7wOz9ZNvJnZ7w35/aG9IZrRqGYwF4eFVD2BrH9rps5p0Wm1hffeOkzNms9lsTn1MM4FqfJ7A6Fi0py3r8rjfHwhMXc8xRQBW9NXV1dUVFik2ZLr77/6eh+sACxkdn3fPBaypE9+OjgUhCEQA5hO/efjLmhHlkL2wO/bdVYKJ84ojaTu3Z8U1sRw9mj+pNDJgAGCtrKDRVFT8Nvrm3lIWkTQigDtxDGD2lA2ApE1FkalzABvHd16vSPx7AMOoosjdLgDTk932SCnAYIuiyB92gLS9D8t2EsB4haKIcQQgRdzryI0Ac8XKIg6AxOBXSR54W6oo0toPoN2L+TUNgKNGUeSFCeBR+W67+Q7Avb8URdajx8LtaPPpWREgc07ZiC9wAVBYOAMLhjMhgI+nFU4r279JALjLz5/T7yx/Ti8qnSBb6r4/31GueBZG6PhueZmr/PcE8r/55aYs8TAQelKsvp0n1JdhgUNBWDJnlrY9tN4wZDbNogwSTv1B55r1pqXrR+vJU/JWkKT/zEYncUsecrnz4MaLKrmboPjXBzWmfg3LRfxCnvEgpUar88WwMV2aP1BZUP/2UJGfR0SISLC5Hc1moRCIIoSBjxKiXwwHJALemBDn8VUKeqks1rYAS8fK8hswmPH8wlaWMXtkWptTIGzeyNPrY0HcKRWUvXHmsaVZgZWzkUCa7+JttpKptjB6FCpespn0CV1vDIirqnyuduhuHRgG4X32jD0OU/m1lkSKZthIAGGA8SK40hALUsoZw/D9WtC/gtW0xnondVcXncnoJtk4BsIArgK4bIoBGc+nO/71hyTvbJwflrMBjB34Emgt5146lDyHo+PbqVMxINNGglV2HhcWjQCrwhfgygM28qAp3+iBi29gorjggRqMX8UjIB0icv4maDy8FhiOA3caxn6Allq4V3ih0ruZCbhzi/OuyEfCJ9JB46DyDkK6E9dxhD5gS1syT1sdjaZgMuDSIYbkI9cfVE3TVyMlQ86IgckEyp4B9pqe+3RULhS4vqQCk2efWz3ykXx3eyMrSWuZdFWJyVvTCZQ9B2qsdg0dwlirJRRFnnSvy0bcWc3N53yUlQ1Q0tB0ZmQ2HsNTKUB8Q1OSaKljMEVKkQLS5IVoXpaHtLVCuoP+dM+7OHhR8u4EtcUVDX2lcKmz67xRO/q5sEJofXf695J22UgQiIgQRgoBQUJEQoFgOAJSWAoHJIgEAyHCwUBIjXgVUREVUREV+V8g/wDYLyRpT80RmQAAAABJRU5ErkJggg==";

                    }
                    //  }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //db.close();
        return str;
    }

    public String getInamename(String Query, String colname) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        String searchQuery = Query;
        Cursor cursor = db.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        // img.setImageBitmap(getImageFromBLOB(cursor.getBlob(cursor.getColumnIndex("image"))));
                        if (cursor.getString(cursor.getColumnIndex(colname)) != null) {
                            // rowObject.put(cursor.getColumnName(i), cursor.getBlob(i).toString());
                            str = cursor.getString(cursor.getColumnIndex(colname));
                            // rowObject.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT));

                        } else {
                            str = "";

                        }

                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        //db.close();
        return str;
    }

    public boolean insertPerson(String UserCode, String IMEINo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserCode", UserCode);
        contentValues.put("IMEINo", IMEINo);
        db.insert("UserMaster", null, contentValues);
        db.close();
        return true;
    }

    /* public SQLiteDatabase openDataBase() throws SQLException {

         //Open the database
         String myPath = dbpath;
         myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
         return myDataBase;

     }
 */
    public synchronized boolean insertmdopogsapdata(JSONArray jArray) {
        boolean flag = false;
        try {

            // db.beginTransaction();

            SQLiteDatabase db = this.getWritableDatabase();
            // db.beginTransactionNonExclusive();
            db.beginTransaction();

            for (int i = 0; i < jArray.length(); i++) {
                // SQLiteDatabase db = this.getWritableDatabase();
                // db.beginTransactionNonExclusive();
                //  db.beginTransaction();
             /*   SQLiteStatement insert = db.compileStatement(sql);

                JSONObject jObject = jArray.getJSONObject(i);
                        insert.bindString(1, jObject.getString("State").toString());
                        insert.bindString(2, jObject.getString("State_Code").toString());
                        insert.bindString(3, jObject.getString("District").toString());
                        insert.bindString(4, jObject.getString("District_code").toString());
                        insert.bindString(5, jObject.getString("Taluka").toString());
                        insert.bindString(6, jObject.getString("Taluka_code").toString());
                        insert.bindString(7, jObject.getString("Village").toString());
                        insert.bindString(8, jObject.getString("Village_code").toString());
                        insert.execute();
                        insert.clearBindings();
                        insert.close();
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();*/
                JSONObject jObject = jArray.getJSONObject(i);

                ContentValues newValues = new ContentValues();
                newValues.put("Cust_Code", jObject.getString("Cust_Code").toString());
                newValues.put("Crop_Name", jObject.getString("Crop_Name").toString());
                newValues.put("Crop_Code", jObject.getString("Crop_Code").toString());
                newValues.put("Prod_Code", jObject.getString("Prod_Code").toString());
                newValues.put("Prod_Name", jObject.getString("Prod_Name").toString());
                newValues.put("QTY_Unit", jObject.getString("QTY_Unit").toString());
                newValues.put("QTY_Base", jObject.getString("QTY_Base").toString());
                newValues.put("Plan_Qty", jObject.getString("Plan_Qty").toString());

                long res = db.insert("mdo_pogsapdata", null, newValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();

            //db.close();
            // db.endTransaction();

            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }

    // insert data using transaction and prepared statement
    public synchronized boolean insertVillageMasterdata1(JSONArray jArray) {
        String sql = "Insert  into VillageLevelMaster (state, state_code,district, district_code,taluka,taluka_code,village,village_code) values(?,?,?,?,?,?,?,?)";
        boolean flag = false;
        try {

            // db.beginTransaction();

            SQLiteDatabase db = this.getWritableDatabase();
            // db.beginTransactionNonExclusive();
            db.beginTransaction();

            for (int i = 0; i < jArray.length(); i++) {
                // SQLiteDatabase db = this.getWritableDatabase();
                // db.beginTransactionNonExclusive();
                //  db.beginTransaction();
             /*   SQLiteStatement insert = db.compileStatement(sql);

                JSONObject jObject = jArray.getJSONObject(i);
                        insert.bindString(1, jObject.getString("State").toString());
                        insert.bindString(2, jObject.getString("State_Code").toString());
                        insert.bindString(3, jObject.getString("District").toString());
                        insert.bindString(4, jObject.getString("District_code").toString());
                        insert.bindString(5, jObject.getString("Taluka").toString());
                        insert.bindString(6, jObject.getString("Taluka_code").toString());
                        insert.bindString(7, jObject.getString("Village").toString());
                        insert.bindString(8, jObject.getString("Village_code").toString());
                        insert.execute();
                        insert.clearBindings();
                        insert.close();
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();*/
                JSONObject jObject = jArray.getJSONObject(i);
                  Log.i("JsonFor Village",jObject.toString());
                ContentValues newValues = new ContentValues();
                newValues.put("state", jObject.getString("State").toString());
                newValues.put("state_code", jObject.getString("State_Code").toString());
                newValues.put("district", jObject.getString("District").toString());
                newValues.put("district_code", jObject.getString("District_code").toString());
                newValues.put("taluka", jObject.getString("Taluka").toString());
                newValues.put("taluka_code", jObject.getString("Taluka_code").toString());
                newValues.put("village", jObject.getString("Village").toString());
                newValues.put("village_code", jObject.getString("Village_code").toString());

                long res = db.insert("VillageLevelMaster", null, newValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();

            // db.close();
            // db.endTransaction();

            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }
    public synchronized boolean insertVillageMasterdata1(JSONArray jArray,Context context) {
        String sql = "Insert  into VillageLevelMaster (state, state_code,district, district_code,taluka,taluka_code,village,village_code) values(?,?,?,?,?,?,?,?)";
        boolean flag = false;
        try {

            // db.beginTransaction();

            SQLiteDatabase db = this.getWritableDatabase();
            // db.beginTransactionNonExclusive();
            db.beginTransaction();

            for (int i = 0; i < jArray.length(); i++) {
                // SQLiteDatabase db = this.getWritableDatabase();
                // db.beginTransactionNonExclusive();
                //  db.beginTransaction();
             /*   SQLiteStatement insert = db.compileStatement(sql);

                JSONObject jObject = jArray.getJSONObject(i);
                        insert.bindString(1, jObject.getString("State").toString());
                        insert.bindString(2, jObject.getString("State_Code").toString());
                        insert.bindString(3, jObject.getString("District").toString());
                        insert.bindString(4, jObject.getString("District_code").toString());
                        insert.bindString(5, jObject.getString("Taluka").toString());
                        insert.bindString(6, jObject.getString("Taluka_code").toString());
                        insert.bindString(7, jObject.getString("Village").toString());
                        insert.bindString(8, jObject.getString("Village_code").toString());
                        insert.execute();
                        insert.clearBindings();
                        insert.close();
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();*/
                JSONObject jObject = jArray.getJSONObject(i);
                Log.i("JsonFor Village",jObject.toString());
                ContentValues newValues = new ContentValues();
                newValues.put("state", jObject.getString("State").toString());
                newValues.put("state_code", jObject.getString("State_Code").toString());
                newValues.put("district", jObject.getString("District").toString());
                newValues.put("district_code", jObject.getString("District_code").toString());
                newValues.put("taluka", jObject.getString("Taluka").toString());
                newValues.put("taluka_code", jObject.getString("Taluka_code").toString());
                newValues.put("village", jObject.getString("Village").toString());
                newValues.put("village_code", jObject.getString("Village_code").toString());

                long res = db.insert("VillageLevelMaster", null, newValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();

            // db.close();
            // db.endTransaction();

            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }


    public boolean insertVillageMasterdata(String state,String state_code, String district, String district_code, String taluka, String taluka_code, String village, String village_code) {
        SQLiteDatabase db = getWritableDatabase();
        boolean flag = false;
        try {
            // db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("state", state);
            contentValues.put("state_code", state_code);
            contentValues.put("district", district);
            contentValues.put("district_code", district_code);
            contentValues.put("taluka", taluka);
            contentValues.put("taluka_code", taluka_code);
            contentValues.put("village", village);
            contentValues.put("village_code", village_code);

            db.insertOrThrow("villageLevelMaster", null, contentValues);
            db.close();
            flag = true;
            //db.endTransaction();
        } catch (Exception ex) {
            flag = false;

        }
        return flag;

    }

    public boolean inserpunchdata(String UserCode, String IMEINo, String coordinates, String address, String entrydate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", UserCode);
        contentValues.put("imeino", IMEINo);
        contentValues.put("coordinates", coordinates);
        contentValues.put("address", address);
        contentValues.put("entrydate", entrydate);
        contentValues.put("Flag", "0");
        db.insert("punchdata", null, contentValues);
        db.close();
        return true;
    }


    public boolean InnovationDataDetail(String Type, String UserCode, String Name, String Name2, String Name3, String Mobileno, String Dist,
                                        String Taluka, String Village,
                                        String Imgname, String TotalPkt, String TotalMahycopkt,
                                        String Remark, String location, String VehicleNo, String imagepath, String firmname) {
        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UserCode", UserCode);
            contentValues.put("Type", Type);
            contentValues.put("Name", Name);
            contentValues.put("Name2", Name2);
            contentValues.put("Name3", Name3);
            contentValues.put("Mobileno", Mobileno);
            contentValues.put("Dist", Dist);
            contentValues.put("Taluka", Taluka);
            contentValues.put("Village", Village);
            contentValues.put("Imgname", Imgname);
            contentValues.put("TotalPkt", TotalPkt);
            contentValues.put("TotalMahycopkt", TotalMahycopkt);
            contentValues.put("Remark", Remark);
            contentValues.put("VehicleNo", VehicleNo);
            contentValues.put("Status", "0");
            contentValues.put("imagepath", imagepath);
            contentValues.put("location", location);
            contentValues.put("firmname", firmname);

            db.insert("InnovationData", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean InsertTagData(String UserCode, String VisitNo, String IMEINo, String VisitType, String coordinate,
                                              String Outcoordinate, String Location, String OutLocation, String CropType,
                                              String ProductName, String NoofFormer, String InTime, String Comments,
                                              String Status, String Dist, String Taluka, String Village,
                                              String Imgname, byte[] Img, String Imgname2, byte[] Img2, String commentdecription) {
        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UserCode", UserCode);
            contentValues.put("VisitNo", VisitNo);
            contentValues.put("imeino", IMEINo);
            contentValues.put("VisitType", VisitType);
            contentValues.put("coordinate", coordinate);
            contentValues.put("Outcoordinate", Outcoordinate);
            contentValues.put("Location", Location);
            contentValues.put("OutLocation", OutLocation);
            contentValues.put("CropType", CropType);
            contentValues.put("ProductName", ProductName);
            contentValues.put("NoofFormer", NoofFormer);
            contentValues.put("InTime", InTime);
            contentValues.put("Comments", Comments);
            contentValues.put("Status", Status);
            contentValues.put("Dist", Dist);
            contentValues.put("Taluka", Taluka);
            contentValues.put("Village", Village);
            contentValues.put("Imgname", Imgname);
            contentValues.put("Img", 0);
            contentValues.put("Imgname2", Imgname2);
            contentValues.put("Img2", 0);
            contentValues.put("commentdecription", commentdecription);
            contentValues.put("imgstatus", 0);
            db.insert("TagData", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean InsertRetailerdata(String mdocode, String coordinate, String startaddress, String dist,
                                                   String taluka, String marketplace,
                                                   String retailername, String retailerfirm, String mobileno, String age,
                                                   String asscoi_distributor, String keyretailer, String otherbussiness,
                                                   String experianceSeedwork, String comments, String type, String newfirm
            , String birthdate, String WhatsappNo, String state) {


        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("type", type);
            contentValues.put("mdocode", mdocode);
            contentValues.put("coordinate", coordinate);
            contentValues.put("startaddress", startaddress);
            contentValues.put("marketplace", marketplace);
            contentValues.put("retailername", retailername);
            contentValues.put("age", age);
            contentValues.put("dist", dist);
            contentValues.put("taluka", taluka);
            contentValues.put("retailerfirm", retailerfirm);
            contentValues.put("mobileno", mobileno);
            contentValues.put("asscoi_distributor", asscoi_distributor);
            contentValues.put("keyretailer", keyretailer);
            contentValues.put("otherbussiness", otherbussiness);
            contentValues.put("experianceSeedwork", experianceSeedwork);
            contentValues.put("comments", comments);
            contentValues.put("Status", "0");
            contentValues.put("newfirm", newfirm);
            contentValues.put("birthdate", birthdate);
            contentValues.put("WhatsappNo", WhatsappNo);
            contentValues.put("state", state);

            db.insert("mdo_Retaileranddistributordata", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean InsertTravelTime(String mdocode, String coordinate, String startaddress, String startdate,
                                                 String dist, String taluka, String village,
                                                 String imgname, String imgpath, String txtkm, String place, String vehicletype) {


        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("mdocode", mdocode);
            contentValues.put("coordinate", coordinate);
            contentValues.put("startaddress", startaddress);
            contentValues.put("startdate", startdate);
            contentValues.put("coordinate", coordinate);
            contentValues.put("dist", dist);
            contentValues.put("taluka", taluka);
            contentValues.put("village", village);
            contentValues.put("imgname", imgname);
            contentValues.put("imgpath", imgpath);
            contentValues.put("txtkm", txtkm);
            contentValues.put("Status", "0");
            contentValues.put("place", place);
            contentValues.put("imgstatus", "0");
            contentValues.put("vehicletype", vehicletype);


            db.insert("mdo_starttravel", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean InsertendTravelTime(String mdocode, String coordinate, String startaddress, String enddate,
                                                    String dist, String taluka, String village,
                                                    String imgname, String imgpath, String txtkm, String place, String vehicletype) {


        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("mdocode", mdocode);
            contentValues.put("coordinate", coordinate);
            contentValues.put("startaddress", startaddress);
            contentValues.put("enddate", enddate);
            contentValues.put("coordinate", coordinate);
            contentValues.put("dist", dist);
            contentValues.put("taluka", taluka);
            contentValues.put("village", village);
            contentValues.put("imgname", imgname);
            contentValues.put("imgpath", imgpath);
            contentValues.put("txtkm", txtkm);
            contentValues.put("Status", "0");
            contentValues.put("place", place);
            contentValues.put("imgstatus", "0");
            contentValues.put("vehicletype", vehicletype);

            db.insert("mdo_endtravel", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean insertAddplace(String mdocode, String place, String coordinate, String startaddress, String date,

                                               String imgname, String imgpath) {

        boolean flag = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("mdocode", mdocode);
            contentValues.put("place", place);
            contentValues.put("coordinate", coordinate);
            contentValues.put("startaddress", startaddress);
            contentValues.put("date", date);
            contentValues.put("imgname", imgname);
            contentValues.put("imgpath", imgpath);
            contentValues.put("Status", "0");
            db.insert("mdo_addplace", null, contentValues);
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

    public synchronized boolean InsertFarmerData(String FarmerName, String statename, String dist,
                                                 String TalukaName, String VillageName, String AadharNo,
                                                 String MobileNo, String Email,
                                                 String Status, String TotalLand, String usercode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FarmerName", FarmerName);
        contentValues.put("statename", statename);
        contentValues.put("dist", dist);
        contentValues.put("TalukaName", TalukaName);
        contentValues.put("VillageName", VillageName);
        contentValues.put("AadharNo", AadharNo);
        contentValues.put("MobileNo", MobileNo);
        contentValues.put("Email", Email);
        contentValues.put("Status", Status);
        contentValues.put("TotalLand", TotalLand);
        contentValues.put("usercode", usercode);
        db.insert("FarmerMaster", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean Insertstockdata(String id, String visitno, String crop, String product, String stock, String currentstock, String salestock, String status) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("visitno", visitno);
        contentValues.put("crop", crop);
        contentValues.put("product", product);
        contentValues.put("stock", stock);
        contentValues.put("currentstock", currentstock);
        contentValues.put("salestock", salestock);
        contentValues.put("status", status);
        db.insert("Tempstockdata", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean deletetable() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS4 + "");
            String CREATE_PRODUCTS_TABLE4 = "CREATE    TABLE " + TABLE_PRODUCTS4 + "(ProductName TEXT,CropName TEXT,CropType TEXT,Crop_Code TEXT,Prod_Code TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE4);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS5 + "");
            String CREATE_PRODUCTS_TABLE5 = "CREATE    TABLE " + TABLE_PRODUCTS5 + "(ActivityName TEXT,activityType TEXT,activityTypeCode TEXT ,activityNameCode TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE5);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS7 + "");
            String CREATE_PRODUCTS_TABLE7 = "CREATE    TABLE " + TABLE_PRODUCTS7 + "(state TEXT,state_code TEXT,district TEXT,district_code TEXT,taluka TEXT,taluka_code TEXT,village TEXT,village_code TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE7);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS8 + "");
            String CREATE_PRODUCTS_TABLE8 = "CREATE    TABLE " + TABLE_PRODUCTS8 + "(RetailerName TEXT,taluka TEXT," +
                    "taluka_code TEXT,activity TEXT,dist TEXT,code TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE8);


            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS10 + "");
            String CREATE_PRODUCTS_TABLE10 = "CREATE    TABLE " + TABLE_PRODUCTS10 + "(comment TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE10);


            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS12 + "");
            String CREATE_PRODUCTS_TABLE12 = "CREATE    TABLE " + TABLE_PRODUCTS12 + "(VehicleNo TEXT)";
            db.execSQL(CREATE_PRODUCTS_TABLE12);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATETERRITORYMASTER + "");
            String CREATE_TABLE_STATETERRITORYMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_STATETERRITORYMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,zone TEXT,territory TEXT,state TEXT,state_code TEXT )";
            db.execSQL(CREATE_TABLE_STATETERRITORYMASTER);


            db.execSQL("DELETE FROM " + TABLE_RBM_MASTER + " ");
            db.execSQL("DELETE FROM " + TABLE_MDO_TBM + " ");
            db.execSQL("DELETE FROM MDO_tagRetailerList ");

            // db.close();
        } catch (Exception ex) {

        }
        return true;
    }

    public synchronized boolean InsertCropMasterData(String ProductName, String CropName, String CropType
            , String Crop_Code, String Prod_Code)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductName", ProductName);
        contentValues.put("CropName", CropName);
        contentValues.put("CropType", CropType);
        contentValues.put("Crop_Code", Crop_Code);
        contentValues.put("Prod_Code", Prod_Code);
        db.insert("CropMaster", null, contentValues);
        //db.close();
        return true;

       /* String sql = "Insert  into CropMaster (ProductName,CropName,CropType) values(?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag=false;
        try {
            db.beginTransactionNonExclusive();
            // db.beginTransaction();
            SQLiteStatement insert = db.compileStatement(sql);
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                insert.bindString(1, jObject.getString("ProductName").toString());
                insert.bindString(2, jObject.getString("Cropname").toString());
                insert.bindString(3, jObject.getString("CropType").toString());

                insert.execute();
                insert.clearBindings();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag=true;
        }
        catch (Exception ex)
        {
            flag= false;

        }
        return flag; */
    }

    public synchronized boolean InsertCropMasterDatanew(JSONArray jArray2) {
        SQLiteDatabase db = getWritableDatabase();
        try {

            for (int i = 0; i < jArray2.length(); i++) {
                JSONObject jObject2 = jArray2.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("ProductName", jObject2.getString("ProductName").toString());
                contentValues.put("CropName", jObject2.getString("Cropname").toString());
                contentValues.put("CropType", jObject2.getString("CropType").toString());
                contentValues.put("Crop_Code", jObject2.getString("Crop_Code").toString());
                contentValues.put("Prod_Code", jObject2.getString("Prod_Code").toString());
                db.insert("CropMaster", null, contentValues);
            }
        } catch (Exception ex) {

        }
        db.close();
        return true;

       /* String sql = "Insert  into CropMaster (ProductName,CropName,CropType) values(?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag=false;
        try {
            db.beginTransactionNonExclusive();
            // db.beginTransaction();
            SQLiteStatement insert = db.compileStatement(sql);
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                insert.bindString(1, jObject.getString("ProductName").toString());
                insert.bindString(2, jObject.getString("Cropname").toString());
                insert.bindString(3, jObject.getString("CropType").toString());

                insert.execute();
                insert.clearBindings();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag=true;
        }
        catch (Exception ex)
        {
            flag= false;

        }
        return flag; */
    }


    public synchronized boolean InsertUserRegistration(String RoleID, String UserCode, String IMEINo,
                                                       String User_pwd, String DisplayName, String unit) {
        SQLiteDatabase db = null;
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Password", User_pwd);
        contentValues.put("UserCode", UserCode);
        contentValues.put("IMEINo", IMEINo);

        contentValues.put("DisplayName", DisplayName);
        contentValues.put("RoleID", RoleID);
        contentValues.put("unit", unit);
        db.insert("UserMaster", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertMyActivityData(String ActivityName, String activityType, String activityTypeCode, String activityNameCode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ActivityName", ActivityName);
        contentValues.put("activityType", activityType);
        contentValues.put("activityTypeCode", activityTypeCode);
        contentValues.put("activityNameCode", activityNameCode);
        db.insert(TABLE_PRODUCTS5, null, contentValues);
        //db.close();
        return true;


    }

    public synchronized boolean InsertMyActivityDatanew(JSONArray jArray3) {
        SQLiteDatabase db = getWritableDatabase();
        try {


            for (int i = 0; i < jArray3.length(); i++) {
                JSONObject jObject3 = jArray3.getJSONObject(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put("ActivityName", jObject3.getString("ActivityName").toString());
                contentValues.put("activityType", jObject3.getString("activityType").toString());
                contentValues.put("activityTypeCode", jObject3.getString("activityTypeCode").toString());
                contentValues.put("activityNameCode", jObject3.getString("activityNameCode").toString());
                db.insert(TABLE_PRODUCTS5, null, contentValues);
            }
        } catch (Exception ex) {
            Log.d(TAG, "InsertMyActivityDatanew: ");
        }
        db.close();
        return true;


    }

    public synchronized boolean InsertDistributorCompetitatorPOGData(String usercode, String state,
                                                                     String dist, String Taluka,
                                                                     String MktPlace, String RetailerMobileno,
                                                                     String CropName, String CropCode,
                                                                     String CompanyName,
                                                                     String RecStock, String ShiftStock,
                                                                     String NetStock, String placementstock,
                                                                     String BalStock, String stockDate,
                                                                     String DistrCode)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("RetailerMobileno", RetailerMobileno);
        contentValues.put("CropName", CropName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("CompanyName", CompanyName);
        contentValues.put("RecStock", RecStock);
        contentValues.put("placementstock", placementstock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("DistrCode", DistrCode);
        contentValues.put("Status", "0");
        db.insert("DistributorCompetitatorPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertDistributorAsRetailerCompetitatorPOGData(String usercode, String state,
                                                                               String dist, String Taluka,
                                                                               String MktPlace, String DistrCode,
                                                                               String CropName, String CropCode,
                                                                               String CompanyName,
                                                                               String RecStock, String ShiftStock,
                                                                               String NetStock, String SoldStock,
                                                                               String BalStock, String stockDate)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("DistrCode", DistrCode);
        contentValues.put("CropName", CropName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("CompanyName", CompanyName);
        contentValues.put("RecStock", RecStock);
        contentValues.put("SoldStock", SoldStock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("Status", "0");
        db.insert("DistributorAsRetailerCompetitatorPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertRetailerCompetitatorPOGData(String usercode, String state,
                                                                  String dist, String Taluka,
                                                                  String MktPlace, String RetailerMobileno,
                                                                  String CropName, String CropCode,
                                                                  String CompanyName,
                                                                  String RecStock, String ShiftStock,
                                                                  String NetStock, String SoldStock,
                                                                  String BalStock, String stockDate)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("RetailerMobileno", RetailerMobileno);
        contentValues.put("CropName", CropName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("CompanyName", CompanyName);
        contentValues.put("RecStock", RecStock);
        contentValues.put("SoldStock", SoldStock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("Status", "0");
        db.insert("RetailerCompetitatorPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertDistributorNextBtPOGData(String usercode, String RetailerMobileno,
                                                               String ProductName,
                                                               String ProductCode,
                                                               String BalStock, String actionstatus,
                                                               String Qty, String Date,
                                                               String DistrCode)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("Mobileno", RetailerMobileno);
        contentValues.put("ProductName", ProductName);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("BalStock", BalStock);
        contentValues.put("actionstatus", actionstatus);
        contentValues.put("Qty", Qty);
        contentValues.put("Date", Date);
        contentValues.put("DistrCode", DistrCode);
        contentValues.put("Status", "0");
        db.insert("DistributorNextBtPOGTable", null, contentValues);
        db.close();
        return true;
    }


    public synchronized boolean InsertDistributorPOGData(String usercode, String state,
                                                         String dist, String Taluka,
                                                         String MktPlace, String RetailerMobileno,
                                                         String CropName,
                                                         String ProductName,
                                                         String CropCode,
                                                         String ProductCode, String plan,
                                                         String RecStockdepot, String RecStockdistr,
                                                         String ShiftStockToDistr,
                                                         String NetStock,
                                                         String BalStock, String placementstock, String stockDate,
                                                         String DistrCode)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("Mobileno", RetailerMobileno);
        contentValues.put("CropName", CropName);
        contentValues.put("ProductName", ProductName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("plan", plan);
        contentValues.put("RecStockdepot", RecStockdepot);
        contentValues.put("RecStockdistr", RecStockdistr);
        contentValues.put("ShiftStockToDistr", ShiftStockToDistr);
        contentValues.put("NetStock", NetStock);
        contentValues.put("placementstock", placementstock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("Status", "0");
        contentValues.put("DistrCode", DistrCode);


        db.insert("DistributorPOGTable", null, contentValues);
        db.close();
        return true;
    }


    public synchronized boolean InsertDistributorAsRetailerNextBtPOGData(String usercode, String DistrCode,
                                                                         String ProductName,
                                                                         String ProductCode,
                                                                         String BalStock, String actionstatus,
                                                                         String Qty, String Date,
                                                                         String AssociateDistributor)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("DistrCode", DistrCode);
        contentValues.put("ProductName", ProductName);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("BalStock", BalStock);
        contentValues.put("actionstatus", actionstatus);
        contentValues.put("Qty", Qty);
        contentValues.put("Date", Date);
        contentValues.put("AssociateDistributor", AssociateDistributor);
        contentValues.put("Status", "0");
        db.insert("DistributorAsRetailerNextBtPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertRetailerNextBtPOGData(String usercode, String RetailerMobileno,
                                                            String ProductName,
                                                            String ProductCode,
                                                            String BalStock, String actionstatus,
                                                            String Qty, String Date,
                                                            String AssociateDistributor)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("RetailerMobileno", RetailerMobileno);
        contentValues.put("ProductName", ProductName);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("BalStock", BalStock);
        contentValues.put("actionstatus", actionstatus);
        contentValues.put("Qty", Qty);
        contentValues.put("Date", Date);
        contentValues.put("AssociateDistributor", AssociateDistributor);
        contentValues.put("Status", "0");
        db.insert("RetailerNextBtPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertDistributorAsRetailerPOGData(String usercode, String state,
                                                                   String dist, String Taluka,
                                                                   String MktPlace, String DistrCode,
                                                                   String CropName,
                                                                   String ProductName,
                                                                   String CropCode,
                                                                   String ProductCode,
                                                                   String RecStock, String ShiftStock,
                                                                   String NetStock, String SoldStock,
                                                                   String BalStock, String stockDate)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("DistrCode", DistrCode);
        contentValues.put("CropName", CropName);
        contentValues.put("ProductName", ProductName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("RecStock", RecStock);
        contentValues.put("ShiftStock", ShiftStock);
        contentValues.put("NetStock", NetStock);
        contentValues.put("SoldStock", SoldStock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("Status", "0");
        db.insert("DistributorAsRetailerPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertRetailerPOGData(String usercode, String state,
                                                      String dist, String Taluka,
                                                      String MktPlace, String RetailerName, String RetailerMobileno,
                                                      String CropName,
                                                      String ProductName,
                                                      String CropCode,
                                                      String ProductCode,
                                                      String RecStock, String ShiftStock,
                                                      String NetStock, String SoldStock,
                                                      String BalStock, String stockDate)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("Taluka", Taluka);
        contentValues.put("MktPlace", MktPlace);
        contentValues.put("RetailerName", RetailerName);
        contentValues.put("RetailerMobileno", RetailerMobileno);
        contentValues.put("CropName", CropName);
        contentValues.put("ProductName", ProductName);
        contentValues.put("CropCode", CropCode);
        contentValues.put("ProductCode", ProductCode);
        contentValues.put("RecStock", RecStock);
        contentValues.put("ShiftStock", ShiftStock);
        contentValues.put("NetStock", NetStock);
        contentValues.put("SoldStock", SoldStock);
        contentValues.put("BalStock", BalStock);
        contentValues.put("stockDate", stockDate);
        contentValues.put("Status", "0");
        db.insert("RetailerPOGTable", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertCommentrDatanew(JSONArray jArray5) {


        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray5.length(); i++) {
                JSONObject jObject5 = jArray5.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("comment", jObject5.getString("commentlist").toString());
                db.insert("Commentlist", null, contentValues);
            }
        } catch (Exception ex) {
            Log.d(TAG, "InsertCommentrDatanew:c ");
        }
        db.close();
        return true;


    }

    public synchronized boolean InsertCommentrData(String comment)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("comment", comment);
        db.insert("Commentlist", null, contentValues);
        // db.close();
        return true;

/*
        String sql = "Insert  into RetailerMaster (RetailerName,taluka,taluka_code,activity) values(?,?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag=false;
        try {
            db.beginTransactionNonExclusive();
            // db.beginTransaction();
            SQLiteStatement insert = db.compileStatement(sql);
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                insert.bindString(1, jObject.getString("RetailerName").toString());
                insert.bindString(2, jObject.getString("taluka").toString());
                insert.bindString(3, jObject.getString("taluka_code").toString());
                insert.bindString(4, jObject.getString("activity").toString());
                insert.execute();
                insert.clearBindings();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag=true;
        }
        catch (Exception ex)
        {
            flag= false;

        }
        return flag; */
    }

    public synchronized boolean InsertRetailerproductData(String mdocode, String compname, String crop, String product,
                                                          String qty, String retailermobile)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("compname", compname);
        contentValues.put("product", product);
        contentValues.put("qty", qty);
        contentValues.put("retailermobile", retailermobile);
        contentValues.put("crop", crop);
        contentValues.put("Status", "0");
        db.insert("mdo_retailerproductdetail", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean InsertMyphotodata(JSONArray jArray) {

        String CREATE_PRODUCTS_TABLE23 = "CREATE    TABLE mdo_photoupdate(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "mdocode TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        boolean flag = false;
        try {
            // db.beginTransaction();
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < jArray.length(); i++) {
                //SQLiteDatabase db = this.getWritableDatabase();
                // db.beginTransactionNonExclusive();
               /* SQLiteStatement insert = db.compileStatement(sql);
                JSONObject jObject = jArray.getJSONObject(i);
                insert.bindString(1, jObject.getString("RetailerName").toString());
                insert.bindString(2, jObject.getString("Taluka").toString());
                insert.bindString(3, jObject.getString("Taluka_code").toString());
                insert.bindString(4, jObject.getString("actvity").toString());
                insert.bindString(5, jObject.getString("district").toString());
                insert.execute();
                insert.clearBindings();
                insert.close();*/
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("mdocode", jObject.getString("mdocode").toString());
                contentValues.put("imgname", jObject.getString("imgname").toString());
                contentValues.put("imgpath", jObject.getString("imgpath").toString());
                contentValues.put("Status", "0");
                db.insert("mdo_photoupdate", null, contentValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }

    public synchronized boolean InsertMDO_NEWRetailer(String state, String type, String mdocode,
                                                      String marketplace, String dist, String taluka,
                                                      String mobileno, String firmname,
                                                      String name, String retailercode
                                                      // , String retailerCategory

    ) {
        boolean flag = false;
        try {
            // db.beginTransaction();
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("state", state);
            contentValues.put("type", type);
            contentValues.put("mdocode", mdocode);
            contentValues.put("marketplace", marketplace);
            contentValues.put("dist", dist);
            contentValues.put("taluka", taluka);
            contentValues.put("mobileno", mobileno);
            contentValues.put("firmname", firmname);
            contentValues.put("name", name);
            contentValues.put("retailercode", retailercode);
            //  contentValues.put("retailerCategory", retailerCategory);
            db.insert("MDO_tagRetailerList", null, contentValues);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }

    public synchronized boolean InsertMDO_tagRetailerList(JSONArray jArray) {
        boolean flag = false;
        try {
            // db.beginTransaction();
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();

            String CREATE_PRODUCTS_TABLE33 = "CREATE  TABLE MDO_tagRetailerList(state TEXT," +
                    "type TEXT ,mdocode TEXT,marketplace TEXT," +
                    "dist TEXT,taluka TEXT," +
                    "mobileno TEXT,firmname TEXT,name TEXT ,retailercode TEXT )";
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("state", jObject.getString("state").toString());
                contentValues.put("type", jObject.getString("type").toString());
                contentValues.put("mdocode", jObject.getString("mdocode").toString());
                contentValues.put("marketplace", jObject.getString("marketplace").toString());
                contentValues.put("dist", jObject.getString("dist").toString());
                contentValues.put("taluka", jObject.getString("taluka").toString());
                contentValues.put("mobileno", jObject.getString("mobileno").toString());
                contentValues.put("firmname", jObject.getString("firmname").toString());
                contentValues.put("name", jObject.getString("name").toString());
                contentValues.put("retailercode", jObject.getString("retailercode").toString());
                db.insert("MDO_tagRetailerList", null, contentValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }

    public synchronized boolean InsertRetailerDatanew(JSONArray jArray) {
        String sql = "Insert  into RetailerMaster (RetailerName, taluka,taluka_code, activity,dist) values(?,?,?,?,?)";
        boolean flag = false;
        try {
            // db.beginTransaction();
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();

            for (int i = 0; i < jArray.length(); i++) {
                //SQLiteDatabase db = this.getWritableDatabase();
                // db.beginTransactionNonExclusive();

               /* SQLiteStatement insert = db.compileStatement(sql);

                JSONObject jObject = jArray.getJSONObject(i);
                insert.bindString(1, jObject.getString("RetailerName").toString());
                insert.bindString(2, jObject.getString("Taluka").toString());
                insert.bindString(3, jObject.getString("Taluka_code").toString());
                insert.bindString(4, jObject.getString("actvity").toString());
                insert.bindString(5, jObject.getString("district").toString());

                insert.execute();
                insert.clearBindings();
                insert.close();*/
                JSONObject jObject = jArray.getJSONObject(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put("RetailerName", jObject.getString("RetailerName").toString());
                contentValues.put("taluka", jObject.getString("Taluka").toString());
                contentValues.put("taluka_code", jObject.getString("Taluka_code").toString());
                contentValues.put("activity", jObject.getString("actvity").toString());
                contentValues.put("dist", jObject.getString("district").toString());
                contentValues.put("code", jObject.getString("code").toString());
                db.insert("RetailerMaster", null, contentValues);


            }
            db.setTransactionSuccessful();
            db.endTransaction();
            // db.close();
            flag = true;
        } catch (Exception ex) {
            flag = false;

        }
        return flag;
    }

    public boolean InsertRetailerData(String taluka, String taluka_code, String RetailerName, String activity)//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RetailerName", RetailerName);
        contentValues.put("taluka", taluka);
        contentValues.put("taluka_code", taluka_code);
        contentValues.put("activity", activity);
        db.insert("RetailerMaster", null, contentValues);
        db.close();
        return true;


    }


    public String getToDoCount() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "SELECT  * FROM UserMaster";
        Cursor cursor = db.query("UserMaster", new String[]{"UserCode"}, null, null, null, null, null);
        String data = "";

        ///Cursor cursor = db.rawQuery(countQuery, null);

        // int count = cursor.getCount();
        //data=cursor.getString(0);
        //cursor.close();
        // db.close();
        // return count
        // return data;//count;


        if (cursor.moveToFirst()) {
            do {
                data = cursor.getString(0); // Here you can get data from table and stored in string if it has only one string.


            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return data;

    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "    = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public synchronized boolean isTableExists(String tableName) {
        SQLiteDatabase db = getReadableDatabase();

        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        db.close();
        return isExist;
    }

    public synchronized void CreateTable(String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + TABLE_NAME + "(Title TEXT, Message TEXT,Time TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.close();
    }

    public synchronized void CreateTable2(String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        //String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + TABLE_NAME + "(Title TEXT, Message TEXT,Time TEXT)";
        //db.execSQL(CREATE_PRODUCTS_TABLE);
        String CREATE_PRODUCTS_TABLE9 = "CREATE    TABLE " + TABLE_NAME + "(id TEXT,visitno TEXT,crop TEXT,product TEXT,currentstock TEXT,stock TEXT,salestock TEXT,status TEXT,INTime DATE)";
        db.execSQL(CREATE_PRODUCTS_TABLE9);
        db.close();
    }

    public synchronized void CreateTable3(String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();

        String CREATE_PRODUCTS_TABLE10 = "CREATE    TABLE " + TABLE_NAME + "(comment TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE10);
        db.close();

    }

    public synchronized void CreateTable4(String InnovationData) {
        SQLiteDatabase db = getWritableDatabase();

        String CREATE_PRODUCTS_TABLE11 = "CREATE    TABLE " + InnovationData + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,UserCode TEXT,Name TEXT,Name2 TEXT ,Name3 TEXT,Type TEXT,VisitNo TEXT," +
                "Mobileno TEXT,Dist TEXT,Taluka TEXT,Village TEXT,Imgname TEXT,TotalPkt TEXT," +
                "TotalMahycopkt TEXT,Remark TEXT,VehicleNo TEXT,Status TEXT,imagepath TEXT,location TEXT,firmname TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE11);
        //db.close();

        String CREATE_PRODUCTS_TABLE12 = "CREATE    TABLE " + TABLE_PRODUCTS12 + "(VehicleNo TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE12);
        db.close();

    }

    public synchronized void CreateTable5() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE13 = "CREATE    TABLE " + TABLE_PRODUCTS13 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,startdate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE13);
        db.close();

    }

    public synchronized void CreateTable6() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE14 = "CREATE    TABLE " + TABLE_PRODUCTS14 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,place TEXT,coordinate TEXT,startaddress TEXT,date TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE14);
        db.close();

    }

    public synchronized void CreateTable7() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE15 = "CREATE    TABLE " + TABLE_PRODUCTS15 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT,dist TEXT,taluka TEXT,marketplace TEXT," +
                "retailername TEXT ,retailerfirm TEXT,age TEXT, mobileno TEXT,asscoi_distributor TEXT,keyretailer TEXT," +
                "otherbussiness TEXT,experianceSeedwork TEXT, comments TEXT,Status TEXT,type TEXT,newfirm TEXT,birthdate TEXT,WhatsappNo TEXT )";
        db.execSQL(CREATE_PRODUCTS_TABLE15);
        db.close();

    }

    public synchronized void CreateTable8() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE16 = "CREATE    TABLE " + TABLE_PRODUCTS16 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE16);
        db.close();

    }

    public synchronized void CreateTable9() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE13 = "CREATE    TABLE " + TABLE_PRODUCTS17 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,enddate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE13);
        db.close();

    }

    public synchronized boolean Insertcultivationdata(String mdocode, String year, String crop, String area,
                                                      String hybridsown, String mobileno, String underhydarea,
                                                      String performance, String entrydate, String Status)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("year", year);
        contentValues.put("crop", crop);
        contentValues.put("area", area);
        contentValues.put("hybridsown", hybridsown);
        contentValues.put("mobileno", mobileno);
        contentValues.put("underhydarea", underhydarea);
        contentValues.put("performance", performance);
        contentValues.put("entrydate", entrydate);
        contentValues.put("Status", "0");
        db.insert("mdo_cultivation", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean Insertcultivationtobedata(String mdocode, String year, String crop, String area,
                                                          String hybridsown, String mobileno, String underhydarea,
                                                          String reason, String entrydate, String Status)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("year", year);
        contentValues.put("crop", crop);
        contentValues.put("area", area);
        contentValues.put("hybridsown", hybridsown);
        contentValues.put("mobileno", mobileno);
        contentValues.put("underhydarea", underhydarea);
        contentValues.put("reason", reason);
        contentValues.put("entrydate", entrydate);
        contentValues.put("Status", "0");
        db.insert("mdo_cultivationtobe", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean Insertdemandassesmentsurveydata(String mdocode, String farmername, String state, String dist, String taluka,
                                                                String village, String mobileno, String awaremahycoprd,
                                                                String entrydate, String Status, String crop,
                                                                String coordinate, String address)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("farmername", farmername);
        contentValues.put("state", state);
        contentValues.put("dist", dist);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("mobileno", mobileno);
        contentValues.put("awaremahycoprd", awaremahycoprd);
        contentValues.put("entrydate", entrydate);
        contentValues.put("Status", "0");
        contentValues.put("crop", crop);
        contentValues.put("coordinate", coordinate);
        contentValues.put("address", address);


        db.insert("mdo_demandassesmentsurvey", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean Insertawaremahycoproduct(String mdocode,
                                                         String hybridsown, String mobileno, String typestatus,
                                                         String reason, String entrydate, String Status)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("hybridsown", hybridsown);
        contentValues.put("mobileno", mobileno);
        contentValues.put("typestatus", typestatus);
        contentValues.put("reason", reason);
        contentValues.put("entrydate", entrydate);
        contentValues.put("Status", "0");
        db.insert("mdo_awaremahycoproduct", null, contentValues);
        db.close();
        return true;
    }


    public synchronized void mdophotoupdate() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE mdo_photoupdate(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "mdocode TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE18);
        db.close();
    }

    public synchronized void createdemandassenmenttables() {
        SQLiteDatabase db = getWritableDatabase();

        String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE mdo_demandassesmentsurvey(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,state TEXT,dist TEXT,taluka TEXT,village TEXT," +
                "mobileno TEXT,awaremahycoprd TEXT,entrydate DATE," +
                "Status TEXT,farmername TEXT,crop TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE18);
        String CREATE_PRODUCTS_TABLE19 = "CREATE  TABLE mdo_cultivation (" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,performance TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE19);
        String CREATE_PRODUCTS_TABLE20 = "CREATE    TABLE mdo_cultivationtobe(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE20);

        String CREATE_PRODUCTS_TABLE21 = "CREATE    TABLE mdo_awaremahycoproduct(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,hybridsown TEXT," +
                "mobileno TEXT,typestatus TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE21);
        db.close();
    }

    public synchronized int getnocoloumncount(String Query) {
        String str = "";
        SQLiteDatabase db = getReadableDatabase();
        int totalColumn = 0;
        try {
            String searchQuery = Query;
            Cursor cursor = getReadableDatabase().rawQuery(searchQuery, null);
            JSONArray resultSet = new JSONArray();
            cursor.moveToFirst();
            ////while (cursor.isAfterLast() == false) {

            totalColumn = cursor.getColumnCount();
            // cursor.moveToNext();
            // }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Log.d("MAA", "getnocoloumncount() : " + ex.toString());
        }
        return totalColumn;
    }

    public int getrowcountno(String TABLE_NAME) {
        int totalrow = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        // db.beginTransaction();
        try {

            String str = "";

            Long lnum = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            totalrow = lnum.intValue();


        } catch (Exception ex) {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : " + ex.toString());
        } finally {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : ");
            //db.endTransaction();
        }
        db.close();
        return totalrow;
    }

    public int getrowno(String query) {
        int totalrow = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        // db.beginTransaction();
        try {

            String str = "";
            Long taskCount = DatabaseUtils.longForQuery(db, query,
                    null);

            //Long lnum=DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            totalrow = taskCount.intValue();


        } catch (Exception ex) {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : " + ex.toString());
        } finally {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : ");
            //db.endTransaction();
        }
        db.close();
        return totalrow;
    }

    public int getCountDt(String TABLE_NAME) {
        SQLiteDatabase db = sInstance.getReadableDatabase();

        int i = 0;//l.intValue();
        try {

            // long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            //String Query="select * from "+TABLE_NAME+"";
            //i=db.rawQuery(Query, null).getCount();//(int)count;

            Cursor cursor = db.query(TABLE_NAME, //Table to query
                    null,                    //columns to return
                    null,                  //columns for the WHERE clause
                    null,              //The values for the WHERE clause
                    null,                       //group the rows
                    null,                      //filter by row groups
                    null);                      //The sort order
            i = cursor.getCount();
            cursor.close();


            //db.close();
        } catch (Exception ex) {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : " + ex.toString());
        }
        return i;
    }

    // Last new 22-2-2021
    public int getrowcountlastnew(String Query) {


        int totalColumn = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        // db.beginTransaction();
        try {

            String str = "";
            String searchQuery = Query;
            Cursor cursor = db.rawQuery(searchQuery, null);
            JSONArray resultSet = new JSONArray();
            cursor.moveToFirst();
            totalColumn = cursor.getCount();
            // cursor.moveToNext();
            // }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : " + ex.toString());
        } finally {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : ");
            //db.endTransaction();
        }
        return totalColumn;
    }

    //New create 22-2-2021
    public int getrowcount(String Query) {


        int totalColumn = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        // db.beginTransaction();
        try {

            totalColumn = db.rawQuery(Query, null).getCount();
            // cursor.moveToNext();
            // }

            db.close();
        } catch (Exception ex) {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : " + ex.toString());
        } finally {
            //totalColumn=0;
            Log.d("MAA", "Check feedback count  : ");
            //db.endTransaction();
        }
        return totalColumn;
    }

    public synchronized boolean InsertVehicle(String vehicleno)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VehicleNo", vehicleno);
        db.insert("VehicleMaster", null, contentValues);
        //db.close();
        return true;

    }

    public synchronized boolean authoriseddistributorproducttable()//JSONArray jArray)
    {
        //mdo_authoriseddistributorproduct
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE22 = "CREATE    TABLE " + TABLE_PRODUCTS22 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE22);
        db.close();
        return true;
    }

    public synchronized boolean CreateCompanyTable()//JSONArray jArray)
    {

        SQLiteDatabase db = getWritableDatabase();
        String[] comlist = new String[]{"Syngenta", "Monsanto",
                "PHI", "Bayer", "UPL", "RASI", "Nuziveedu-NSL", "Kaveri"
                , "Ankur", "Dhaanya", "Ajit", "US Agri", "GK", "VNR Seeds", "JK "
        };
        for (int i = 0; i < comlist.length; i++) {
            // JSONObject jObject = jArray.getJSONObject(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("Compname", comlist[i].toString());
            db.insert("CompanyNamemaster", null, contentValues);


        }
        db.close();


        return true;
    }


    public synchronized boolean insertauthoriseddistributorproduct(String mdocode, String compname, String crop, String product,
                                                                   String qty, String retailermobile)//JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdocode", mdocode);
        contentValues.put("compname", compname);
        contentValues.put("product", product);
        contentValues.put("qty", qty);
        contentValues.put("retailermobile", retailermobile);
        contentValues.put("crop", crop);
        contentValues.put("Status", "0");
        db.insert("mdo_authoriseddistributorproduct", null, contentValues);
        db.close();
        return true;
    }

    public synchronized boolean deletetablenewtable() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE12 = "CREATE  TABLE mdo_test (test TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE12);
        db.execSQL(" DROP TABLE IF EXISTS mdo_addplace ");
        db.execSQL(" DROP TABLE IF EXISTS mdo_Retaileranddistributordata ");
        db.execSQL(" DROP TABLE IF EXISTS mdo_retailerproductdetail ");
        db.execSQL(" DROP TABLE IF EXISTS mdo_endtravel ");
        db.execSQL(" DROP TABLE IF EXISTS mdo_starttravel ");
        db.close();
        return true;
    }

    ////RowCount of Table
    public int getRowCount(String TABLE_NAME) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE isSynced='0'  ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public int getRowCountReviewNotSynced(String TABLE_NAME, String uIdP) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE uIdP='" + uIdP + "' and isSynced='0' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public int getRowCountVAlidatedReviewNotUploaded(String TABLE_NAME) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE isUploaded='0' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getRowCountVAlidatedReviewNotUploadedList(String TABLE_NAME, String uIdP) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE uIdP='" + uIdP + "' and isUploaded='0'";
        Log.d(TAG, countQuery + "countedfor reviewlist");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getRowCountReviewModelList(String TABLE_NAME, String uIdP) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE uIdP='" + uIdP + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public String fetchLastDate(String uid) {
        String visitingdate = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "select visitingDate from DemoReviewData where uIdP = '" + uid + "' order by  visitingDate desc  limit 1 ";
        Cursor data = db.rawQuery(query, null);
        Log.d("query=====", query);

        if (data.getCount() == 0) {

        } else {
            data.moveToFirst();

            visitingdate = data.getString((data.getColumnIndex("visitingDate")));


            Log.d("typerr", "visitingDate" + visitingdate);


        }
        data.close();

        if (visitingdate.equals("")) {

            visitingdate = "NO VISITS";
        }
        db.close();
        return visitingdate;
    }


    public Cursor fetchAlreadyExists(String uId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select uId from DemoModelData where uId = '" + uId + "'", null);
        //db.close();
        return cursor;
    }


    public Cursor fetchAlreadyExistsKisanClub(String mno) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select mobileNumber from KisanClubData where mobileNumber = '" + mno + "'", null);
        //db.close();
        return cursor;
    }


    public Cursor fetchAlreadyExistsInReview(String uId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select uId from DemoReviewData where uId = '" + uId + "'", null);


        // db.close();
        return cursor;
    }


    public Cursor fetchAlreadyExistsInValidateReview(String uId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select uId from ValidatedDemoReviewData where uId = '" + uId + "'", null);
        //db.close();
        return cursor;
    }

    public Cursor fetchLastDateValidate(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select visitingDate from ValidatedDemoReviewData where uIdP = '" + uid + "' order by  visitingDate desc  limit 1 ";
        Cursor cursor = db.rawQuery(query, null);
        Log.d("query=====", query);
        // db.close();
        return cursor;
    }


    public Cursor fetchAlreadyExistsRetailerSurvey(String mno) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select retailerNumber from RetailerSurveyData where retailerNumber = '" + mno + "'", null);
        //db.close();
        return cursor;
    }

    public Cursor fetchAlreadyExistsRetailerVisitToFieldData(String mno) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select farmerMobileNumber from RetailerVisitToFieldData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and farmerMobileNumber = '" + mno + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsLivePlantDisplayVillage(String uId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select uId from LivePlantDisplayVillageData" +
                " where uId = '" + uId + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsFarmerVisitsDataCount(String mobileNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select mobileNumber from FarmerVisitsData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and mobileNumber = '" + mobileNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsRetailerVisitsDataCount(String retailerDetails) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select retailerDetails from RetailerVisitsData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and retailerDetails = '" + retailerDetails + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsDistributerVisitsDataCount(String distributerDetails) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select distributerDetails from DistributerVisitsData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and distributerDetails = '" + distributerDetails + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsLivePlantRetailerCount(String retailerDetails) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from LivePlantDisplayRetailerData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and retailerDetails = '" + retailerDetails + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistVillageMeetingCount(String otherVillage) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from VillageMeetingData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and otherVillage = '" + otherVillage + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistVillageMeetingCountFocusssed(String otherVillage) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from VillageMeetingData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and  focussedVillage = '" + otherVillage + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsPurchaseListData(String retailerNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select retailerNumber from PurchaseListData" +
                " where retailerNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsCropShowData(String retailerNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select pkRetailerMobileNumber from CropShowData" +
                " where pkRetailerMobileNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsHarvestDayData(String retailerNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select pkRetailerMobileNumber from HarvestDayData" +
                " where pkRetailerMobileNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsFieldBoardData(String retailerNumber, String productName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select pkFarmerMobileNumber from FieldBoardData " +
                "where  product='" + productName + "'  and " +
                " strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and pkFarmerMobileNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsFieldDayData(String retailerNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select pkFarmerMobileNumber from FieldDayData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and pkFarmerMobileNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsTestimonialCollectionData(String farmerMobile) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select farmerMobile from TestimonialCollectionData " +
                "where strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and farmerMobile = '" + farmerMobile + "'", null);
        return cursor;
    }

    public Cursor fetchAlreadyExistsFieldBannerData(String retailerNumber, String productName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select pkFarmerMobileNumber from FieldBannerData " +
                "where product='" + productName + "' and" +
                "  strftime( '%Y-%m-%d', EntryDt)='" + strdate + "' and pkFarmerMobileNumber = '" + retailerNumber + "'", null);
        return cursor;
    }

    public boolean insertDemoModelData(String uId, String userCode, String plotType,
                                       String state, String district,
                                       String taluka, String village, String farmerName,
                                       String mobileNumber, String whatsappNumber, String cropType,
                                       String product, String area, String seedQuantity, String unit,
                                       String sowingDate, String coordinates, String soilType, String irrigationMode,
                                       String spacingRow, String spacingPlan, String imgName, String imgPath, String imgStatus, String isSynced, String checkHybrids, String remarks,
                                       String checkHybridsSelected, String focussedVillage, String vil_code) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uId", uId);
        contentValues.put("userCode", userCode);
        contentValues.put("plotType", plotType);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("crop", cropType);
        contentValues.put("product", product);

        contentValues.put("area", area);
        contentValues.put("seedQuantity", seedQuantity);
        contentValues.put("unit", unit);
        contentValues.put("sowingDate", sowingDate);
        contentValues.put("coordinates", coordinates);
        contentValues.put("soilType", soilType);
        contentValues.put("irrigationMode", irrigationMode);
        contentValues.put("spacingRow", spacingRow);
        contentValues.put("spacingPlan", spacingPlan);

        contentValues.put("imgName", imgName);
        contentValues.put("imgPath", imgPath);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("checkHybrids", checkHybrids == null ? "" : checkHybrids);
        contentValues.put("checkHybridsSelected", checkHybridsSelected == null ? "" : checkHybridsSelected);
        contentValues.put("focussedVillage", focussedVillage == null ? "" : focussedVillage);
        contentValues.put("remarks", remarks == null ? "" : remarks);
        contentValues.put("vill_code", vil_code);
        Date entrydate = new Date();
        final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        contentValues.put("EntryDt", InTime);
        db.insert(TABLE_DEMOMODELDATA, null, contentValues);
        // db.close();
        return true;
    }

    public boolean insertReviewValidationData(String uId, String uIdP, String userCode,
                                              String taluka, String farmerName,
                                              String mobileNumber, String cropType,
                                              String product, String area, String sowingDate, String visitingDate,
                                              String coordinates, String purposeVisit, String comment, String imgName, String imgPath,
                                              String imgStatus, String isSynced, String mdoCode, String entrydate,
                                              String isUploaded, String focussedVillage,
                                              String fieldPest, String fieldDiseases,
                                              String fieldNutrients, String fieldOther,
                                              String cropCondition,
                                              String recommendations) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uId", uId);
        contentValues.put("uIdP", uIdP);
        contentValues.put("userCode", userCode);
        contentValues.put("taluka", taluka);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("crop", cropType);
        contentValues.put("product", product);
        contentValues.put("area", area);
        contentValues.put("visitingDate", visitingDate);
        contentValues.put("sowingDate", sowingDate);
        contentValues.put("coordinates", coordinates);
        contentValues.put("purposeVisit", purposeVisit);
        contentValues.put("comment", comment);
        contentValues.put("imgName", imgName);
        contentValues.put("imgPath", imgPath);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("mdoCode", mdoCode);

        Date entrydate1 = new Date();
        final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate1);
        contentValues.put("entrydate", InTime);

        //contentValues.put("entrydate", entrydate);
        contentValues.put("isUploaded", isUploaded);
        contentValues.put("focussedVillage", focussedVillage == null ? "" : focussedVillage);
        contentValues.put("fieldPest", fieldPest == null ? "" : fieldPest);
        contentValues.put("fieldDiseases", fieldDiseases == null ? "" : fieldDiseases);
        contentValues.put("fieldNutrients", fieldNutrients == null ? "" : fieldNutrients);
        contentValues.put("fieldOther", fieldOther == null ? "" : fieldOther);
        contentValues.put("cropCondition", cropCondition == null ? "" : cropCondition);
        contentValues.put("recommendations", recommendations == null ? "" : recommendations);


        db.insert(TABLE_VALIDATEDDEMOREVIEWDATA, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertDemoModelReview(String uId, String uIdP, String userCode,
                                         String taluka, String farmerName,
                                         String mobileNumber, String cropType,
                                         String product, String area, String sowingDate, String visitingDate,
                                         String coordinates, String purposeVisit, String comment, String imgName, String imgPath,
                                         String imgStatus, String isSynced,
                                         String focussedVillage, String fieldPest, String fieldDiseases,
                                         String fieldNutrients, String fieldOther, String cropCondition,
                                         String recommendations) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uId", uId);
        contentValues.put("uIdP", uIdP);
        contentValues.put("userCode", userCode);
        contentValues.put("taluka", taluka);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("crop", cropType);
        contentValues.put("product", product);
        contentValues.put("area", area);
        contentValues.put("visitingDate", visitingDate);
        contentValues.put("sowingDate", sowingDate);
        contentValues.put("coordinates", coordinates);
        contentValues.put("purposeVisit", purposeVisit);
        contentValues.put("comment", comment);
        contentValues.put("imgName", imgName);
        contentValues.put("imgPath", imgPath);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("focussedVillage", focussedVillage == null ? "" : focussedVillage);
        contentValues.put("fieldPest", fieldPest == null ? "" : fieldPest);
        contentValues.put("fieldDiseases", fieldDiseases == null ? "" : fieldDiseases);
        contentValues.put("fieldNutrients", fieldNutrients == null ? "" : fieldNutrients);
        contentValues.put("fieldOther", fieldOther == null ? "" : fieldOther);
        contentValues.put("cropCondition", cropCondition == null ? "" : cropCondition);
        contentValues.put("recommendations", recommendations == null ? "" : recommendations);
        Date entrydate1 = new Date();


        final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate1);
        contentValues.put("EntryDt", InTime);
        db.insert(TABLE_DEMOREVIEWDATA, null, contentValues);
        //db.close();
        return true;
    }

    public void updateDemoModelData(String uId, String isSyncedUpdate, String imgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSyncedUpdate);
        contentValues.put("imgStatus", imgStatus);
        db.update(TABLE_DEMOMODELDATA, contentValues, "uId='" + uId + "'", null);
        db.close();
    }


    public void updateDemoReviwData(String uId, String isSyncedUpdate, String imgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSyncedUpdate);
        contentValues.put("imgStatus", imgStatus);
        db.update(TABLE_DEMOREVIEWDATA, contentValues, "uId='" + uId + "'", null);
        db.close();
    }


    public void updateValidatedReviwData(String uId, String isSyncedUpdate, String imgStatus, String isUploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSyncedUpdate);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isUploaded", isUploaded);
        db.update(TABLE_VALIDATEDDEMOREVIEWDATA, contentValues, "uId='" + uId + "'", null);
        db.close();
    }

    public void updateRetailerSurveyData(String mno, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        db.update(TABLE_RETAILERSURVEYDATA, contentValues, "retailerNumber='" + mno + "'", null);
        db.close();
    }


    public void updateRetailerVisitToFieldData(String mno, String isSynced, String activityImgStatus, String farmerListImgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListImgStatus", farmerListImgStatus);
        // String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        //db.update(TABLE_LIVEPLANTDISPLAYVILLAGEDATA, contentValues, whereClause, null);
        db.update(TABLE_RETAILERTOVISITDATA, contentValues, "farmerMobileNumber='" + mno + "'", null);
        db.close();
    }


    public void updateLivePlantDisplayVillageData(String isNotSynced, String isSynced,
                                                  String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_LIVEPLANTDISPLAYVILLAGEDATA, contentValues, whereClause, null);

        //db.update(TABLE_LIVEPLANTDISPLAYVILLAGEDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public void updateLivePlantDisplayRetailerData(String isNotSynced, String isSynced,
                                                   String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_LIVEPLANTDISPLAYRETAILERDATA, contentValues, whereClause, null);

        //db.update(TABLE_LIVEPLANTDISPLAYRETAILERDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }


    public void updateKisanData(String mno, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        db.update(TABLE_KISANCLUBDATA, contentValues, "mobileNumber='" + mno + "'", null);
        db.close();
    }

    public ArrayList<DemoModelPlotListModel> getSearchResults(String crop, String product, String mobNo, String taluka) {
        ArrayList<DemoModelPlotListModel> list;
        list = new ArrayList<DemoModelPlotListModel>();

        String selectQuery = "SELECT *  FROM DemoModelData where crop='" + crop + "' OR product='" + product + "' OR mobileNumber='" + mobNo + "' " +
                "OR taluka='" + taluka + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //JSONObject jobj = new JSONObject();
                DemoModelPlotListModel demoModelPlotListModel = new DemoModelPlotListModel();
                try {


                    demoModelPlotListModel.setIsSynced(cursor.getString(20));
                    demoModelPlotListModel.setArea(cursor.getString(13));
                    demoModelPlotListModel.setCoordinates(cursor.getString(16));
                    demoModelPlotListModel.setCrop(cursor.getString(11));
                    demoModelPlotListModel.setDistrict(cursor.getString(5));
                    demoModelPlotListModel.setMobileNumber(cursor.getString(9));
                    demoModelPlotListModel.setWhatsappNumber(cursor.getString(10));
                    demoModelPlotListModel.setFarmerName(cursor.getString(8));
                    demoModelPlotListModel.setPlotType(cursor.getString(3));
                    demoModelPlotListModel.setProduct(cursor.getString(12));
                    demoModelPlotListModel.setSeedQuantity(cursor.getString(14));
                    demoModelPlotListModel.setSowingDate(cursor.getString(15));
                    demoModelPlotListModel.setVillage(cursor.getString(7));
                    demoModelPlotListModel.setuId(cursor.getString(1));
                    demoModelPlotListModel.setUserCode(cursor.getString(2));
                    demoModelPlotListModel.setState(cursor.getString(4));
                    demoModelPlotListModel.setTaluka(cursor.getString(6));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                list.add(demoModelPlotListModel);
            } while (cursor.moveToNext());
        }
        return list;
    }


    public JSONObject getJson(String setoutletId) {
        SQLiteDatabase db = getWritableDatabase();
        // Calendar c = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // String CUrrrentDate = df.format(c.getTime());

        Cursor cursor = null;
        JSONObject jsonObject = null;
        // Array for adding json value
        //  JSONArray OrderListArray = new JSONArray();
        // if (outletName.equals("")) {
        cursor = db.rawQuery(
                "SELECT * FROM PaymentTable WHERE orderId = '' ORDER BY pkId DESC LIMIT 1"

                , null);
        //}
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("usercode", cursor.getString(cursor.getColumnIndex("usercode")));
                        jsonObject.put("rzpPaymentId", cursor.getString(cursor.getColumnIndex("rzpPaymentId")));
                        jsonObject.put("farmersIdList", cursor.getString(cursor.getColumnIndex("farmersIdList")));
                        jsonObject.put("orderId", cursor.getString(cursor.getColumnIndex("orderId")));
                        ;
                        jsonObject.put("status", cursor.getString(cursor.getColumnIndex("status")));
                        jsonObject.put("amount", cursor.getString(cursor.getColumnIndex("amount")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                close();
            }

        }
        db.close();
        return jsonObject;

    }

    public String fetchQtyCoupon(String product) {
        SQLiteDatabase db = null;
        String qty = "";
        db = getWritableDatabase();
        String query = "select distinct schemeUnit from CouponSchemeMaster where product = '" + product + "' ";
        Cursor data = db.rawQuery(query, null);


        if (data.getCount() == 0) {

        } else {
            data.moveToFirst();

            qty = data.getString((data.getColumnIndex("schemeUnit")));


            Log.d("typerr", "schemeUnit" + qty);


        }
        data.close();


        Log.d("query=====", query);
        db.close();
        return qty;
    }


    public String fetchPerPriceCoupon(String product) {
        SQLiteDatabase db = null;
        String price = "";
        db = getWritableDatabase();
        String query = "select distinct advAmtPerUnit from CouponSchemeMaster where product = '" + product + "' ";
        Cursor data = db.rawQuery(query, null);
        if (data.getCount() == 0) {

        } else {
            data.moveToFirst();

            price = data.getString((data.getColumnIndex("advAmtPerUnit")));


            Log.d("typerr", "schemeUnit" + price);


        }
        data.close();
        Log.d("query=====", query);
        db.close();
        return price;
    }

    public String fetchPerPriceCouponHDPS(String product) {
        SQLiteDatabase db = null;
        String price = "";
        db = getWritableDatabase();
        String query = "select distinct advAmtPerUnit from HDPSMasterScheme where product = '" + product + "' ";
        Cursor data = db.rawQuery(query, null);
        if (data.getCount() == 0) {

        } else {
            data.moveToFirst();

            price = data.getString((data.getColumnIndex("advAmtPerUnit")));


            Log.d("HDPS", "HDPSMasterScheme schemeUnit" + price);


        }
        data.close();
        Log.d("query=====", query);
        db.close();
        return price;
    }


    public synchronized boolean InsertCouponMaster(String couponCode, String productCode, String entrydate) {

        SQLiteDatabase db = null;
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("couponCode", couponCode);
        contentValues.put("productCode", productCode);
        contentValues.put("entryDate", entrydate);

        db.insert("couponMaster", null, contentValues);
        // db.close();
        return true;

    }

    public synchronized boolean InsertCouponCheckData(String userCode, String mobileNumber
            , String crop, String product, String farmerPhoto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("crop", crop);
        contentValues.put("product", product);
        contentValues.put("farmerPhoto", farmerPhoto);
        db.insert("CheckUploadCoupon", null, contentValues);
        // db.close();
        return true;

    }

    public synchronized boolean InsertCouponPaymentAmount(String paymentAmount, String remainingAmount) {

        SQLiteDatabase db = null;
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("paymentAmount", paymentAmount);
        contentValues.put("remainingAmount", remainingAmount);
        contentValues.put("newCouponScanAmount", "0");

        db.insert("couponPaymentAmount", null, contentValues);
        db.close();
        return true;

    }

    public synchronized boolean InsertCouponCheckDatanew(JSONArray jArray3) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray3.length(); i++) {
                JSONObject jObject3 = jArray3.getJSONObject(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put("userCode", jObject3.getString("userCode").toString());
                contentValues.put("mobileNumber", jObject3.getString("mobileNumber").toString());
                contentValues.put("crop", jObject3.getString("crop").toString());
                contentValues.put("product", jObject3.getString("product").toString());
                contentValues.put("farmerPhoto", jObject3.getString("farmerPhoto").toString());
                db.insert("CheckUploadCoupon", null, contentValues);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;

    }

    public synchronized boolean InsertCouponMasternew(JSONArray jArray2, String InTime) {

        SQLiteDatabase db = null;
        db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray2.length(); i++) {
                JSONObject jObject2 = jArray2.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("couponCode", jObject2.getString("couponCode").toString());
                contentValues.put("productCode", jObject2.getString("productCode").toString());
                contentValues.put("entryDate", InTime);
                db.insert("couponMaster", null, contentValues);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;

    }

    public synchronized boolean InsertCouponSchemeMasternew(JSONArray jArray) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("year", jObject.getString("year").toString());
                contentValues.put("season", jObject.getString("season").toString());
                contentValues.put("state", jObject.getString("state").toString());
                contentValues.put("region", jObject.getString("region"));
                contentValues.put("crop", jObject.getString("crop"));
                contentValues.put("product", jObject.getString("product"));
                contentValues.put("productCode", jObject.getString("productCode"));
                contentValues.put("schemeName", jObject.getString("schemeName"));
                contentValues.put("schemeUnit", jObject.getString("schemeUnit"));
                contentValues.put("advAmtPerUnit", jObject.getString("advAmtPerUnit"));
                contentValues.put("perUnitBenifitOffer", jObject.getString("perUnitBenifitOffer"));
                contentValues.put("couponValue", jObject.getString("couponValue"));
                contentValues.put("schemeFrom", jObject.getString("schemeFrom"));
                contentValues.put("schemeEnd", jObject.getString("schemeEnd"));
                db.insert(TABLE_COUPONSCHEMEMASTER, null, contentValues);

            }
            db.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;
    }

    public void printColumnNoHDPSDownload() {
        try {
            SQLiteDatabase db2 = this.getReadableDatabase();
            searchQuery = "select * from HDPSMasterScheme ";
            Cursor cursor = db2.rawQuery(searchQuery, null);
            int count = cursor.getCount();
            Log.d("HDPS", ">>>>>>>>>>>>>>>>>>>>>>>>>>> HDPSMasterScheme TOTAL Row added : " + count);
            cursor.close();
            SQLiteDatabase db3 = this.getReadableDatabase();
            String searchQuery2 = "select * from HDPSCouponRecords ";
            Cursor cursor1 = db3.rawQuery(searchQuery2, null);
            int count1 = cursor1.getCount();
            Log.d("HDPS", ">>>>>>>>>>>>>>>>>>>>>>>>>>>> HDPSCouponRecords TOTAL Row added : " + count1);
            cursor1.close();
        } catch (Exception e) {
            Log.d("HDPS", "MSG : " + e.getMessage());
        }
    }

    /*public synchronized boolean InsertHDPSCouponMaster(JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("year", jObject.getString("year").toString());
                contentValues.put("season", jObject.getString("season").toString());
                contentValues.put("state", jObject.getString("state").toString());
                contentValues.put("region", jObject.getString("region"));
                contentValues.put("crop", jObject.getString("crop"));
                contentValues.put("product", jObject.getString("product"));
                contentValues.put("productCode", jObject.getString("productCode"));
                contentValues.put("schemeName", jObject.getString("schemeName"));
                contentValues.put("schemeUnit", jObject.getString("schemeUnit"));
                contentValues.put("advAmtPerUnit", jObject.getString("advAmtPerUnit"));
                contentValues.put("perUnitBenifitOffer", jObject.getString("perUnitBenifitOffer"));
                contentValues.put("couponValue", jObject.getString("couponValue"));
                contentValues.put("schemeFrom", jObject.getString("schemeFrom"));
                contentValues.put("schemeEnd", jObject.getString("schemeEnd"));
                db.insert(TABLE_HDPSSCHEMEMASTER, null, contentValues);

            }
            db.close();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;
    }*/

   /* public synchronized boolean InsertHDPSSchemeMasternew(JSONArray jArray)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("year", jObject.getString("year").toString());
                contentValues.put("season", jObject.getString("season").toString());
                contentValues.put("state", jObject.getString("state").toString());
                contentValues.put("region", jObject.getString("region"));
                contentValues.put("crop", jObject.getString("crop"));
                contentValues.put("product", jObject.getString("product"));
                contentValues.put("productCode", jObject.getString("productCode"));
                contentValues.put("schemeName", jObject.getString("schemeName"));
                contentValues.put("schemeUnit", jObject.getString("schemeUnit"));
                contentValues.put("advAmtPerUnit", jObject.getString("advAmtPerUnit"));
                contentValues.put("perUnitBenifitOffer", jObject.getString("perUnitBenifitOffer"));
                contentValues.put("couponValue", jObject.getString("couponValue"));
                contentValues.put("schemeFrom", jObject.getString("schemeFrom"));
                contentValues.put("schemeEnd", jObject.getString("schemeEnd"));
                db.insert(TABLE_HDPSCOUPONSCHEMEMASTER, null, contentValues);

            }
            db.close();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;
    }*/

    public synchronized boolean InsertHDPSCouponRecords(JSONArray jArray, String InTime) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("couponCode", jObject.getString("couponCode").toString());
                contentValues.put("productCode", jObject.getString("productCode").toString());
                contentValues.put("entryDate", InTime);
                db.insert(TABLE_HDPSCOUPONRECORDS, null, contentValues);
            }
            db.close();
            return true;
        } catch (Exception e) {
            Log.d("InsertHDPSCouponRecords", "MSG : " + e.getMessage());
            db.close();
            return false;
        }
    }

    //8th Sept. 2021 To get the data
    public synchronized Cursor getProductData(String productCode, String productName) {
        Log.d("HDPS", "getProductData : " + " productCode:" + productCode + " productName:" + productName);
        SQLiteDatabase db = getWritableDatabase();
        String searchQuery = "select  allowpacket,perCouponPkt,retailerContraint from HDPSMasterScheme where productCode='" + productCode + "'";
        Cursor cursor = db.rawQuery(searchQuery, null);
        return cursor;
    }


    public synchronized boolean InsertHDPSMasterScheme(JSONArray jArray) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("bigint", jObject.getString("bigint").toString());
                contentValues.put("year", jObject.getString("year").toString());
                contentValues.put("season", jObject.getString("season").toString());
                contentValues.put("state", jObject.getString("state"));
                contentValues.put("region", jObject.getString("region"));
                contentValues.put("crop", jObject.getString("crop"));
                contentValues.put("product", jObject.getString("product"));
                contentValues.put("productCode", jObject.getString("productCode"));
                contentValues.put("productname", jObject.getString("productname"));
                contentValues.put("schemeName", jObject.getString("schemeName"));
                contentValues.put("schemeUnit", jObject.getString("schemeUnit"));
                contentValues.put("advAmtPerUnit", jObject.getString("advAmtPerUnit"));
                contentValues.put("perUnitBenifitOffer", jObject.getString("perUnitBenifitOffer"));
                contentValues.put("couponValue", jObject.getString("couponValue"));
                contentValues.put("handleChargeRetailer", jObject.getString("handleChargeRetailer"));
                contentValues.put("handleChargeDistributor", jObject.getString("handleChargeDistributor"));
                contentValues.put("schemeFrom", jObject.getString("schemeFrom"));
                contentValues.put("schemeEnd", jObject.getString("schemeEnd"));
                contentValues.put("entrydate", jObject.getString("entrydate"));
                //Added on 9th Sept 2021
                contentValues.put("allowpacket", jObject.getString("allowpacket"));
                contentValues.put("perCouponPkt", jObject.getString("perCouponPkt"));
                contentValues.put("retailerContraint", jObject.getString("retailerContraint"));
                Log.d("HDPS", "HDPSMasterScheme Content Values : " + contentValues);
                db.insert(TABLE_HDPSMASTERSCEME, null, contentValues);

            }
            db.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        db.close();
        return true;
    }

    public synchronized boolean InsertCouponSchemeMaster(String year
            , String season, String state, String region, String crop
            , String product, String productCode, String schemeName
            , String schemeUnit, String advAmtPerUnit, String perUnitBenifitOffer
            , String couponValue, String schemeFrom, String schemeEnd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("year", year);
        contentValues.put("season", season);
        contentValues.put("state", state);
        contentValues.put("region", region);
        contentValues.put("crop", crop);
        contentValues.put("product", product);
        contentValues.put("productCode", productCode);
        contentValues.put("schemeName", schemeName);
        contentValues.put("schemeUnit", schemeUnit);
        contentValues.put("advAmtPerUnit", advAmtPerUnit);
        contentValues.put("perUnitBenifitOffer", perUnitBenifitOffer);
        contentValues.put("couponValue", couponValue);
        contentValues.put("schemeFrom", schemeFrom);
        contentValues.put("schemeEnd", schemeEnd);
        db.insert(TABLE_COUPONSCHEMEMASTER, null, contentValues);
        // db.close();
        return true;

    }

    public synchronized boolean insertCouponRecordData(String uId, String timeStamp, String userCode, String state, String district, String taluka, String village
            , String farmerName, String mobileNumber, String whatsappNumber
            , String crop, String product, String quantityPacket, String packetsBooked
            , String couponsIssued, String totalAmount, String couponQr1, String couponQr2
            , String couponQr3, String couponQr4, String couponQr5, String couponQr6, String farmerPhoto,
                                                       String imgPath, String imgStatus, String isSynced,
                                                       String address, String cordinates) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uId", uId);
        contentValues.put("timeStamp", timeStamp);
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("crop", crop);
        contentValues.put("product", product);
        contentValues.put("quantityPacket", quantityPacket);
        contentValues.put("packetsBooked", packetsBooked);
        contentValues.put("couponsIssued", couponsIssued);
        contentValues.put("totalAmount", totalAmount);
        contentValues.put("couponQr1", couponQr1);
        contentValues.put("couponQr2", couponQr2);
        contentValues.put("couponQr3", couponQr3);
        contentValues.put("couponQr4", couponQr4);
        contentValues.put("couponQr5", couponQr5);
        contentValues.put("couponQr6", couponQr6);

        contentValues.put("farmerPhoto", farmerPhoto);
        contentValues.put("imgPath", imgPath);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("address", address);
        contentValues.put("cordinates", cordinates);
        db.insert(TABLE_COUPONRECORDDATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateCouponData(String uId, String imageName, String isSyncedUpdate, String imgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSyncedUpdate);
        contentValues.put("imgStatus", imgStatus);
        //     db.update(TABLE_COUPONRECORDDATA, contentValues, "uId='" + uId + "'", null);
        db.update(TABLE_COUPONRECORDDATA, contentValues, "farmerPhoto='" + imageName + "'", null);

        db.close();
    }

    public void updateCouponscancurrentamount(int amt) {

        try {


            SQLiteDatabase db = getWritableDatabase();
            SQLiteDatabase db2 = this.getReadableDatabase();
            int count = 0;
            int totalamount = 0;
            searchQuery = "select  *  from couponPaymentAmount ";
            // Cursor cursor = db.rawQuery(searchQuery, null);
            Cursor cursor = db2.rawQuery(searchQuery, null);
            count = cursor.getCount();
            if (count > 0) {
                JSONArray jArray = new JSONArray();
                jArray = getResultsVillageDetails(searchQuery);
                JSONObject jObject = jArray.getJSONObject(0);
                totalamount = Integer.parseInt(jObject.getString("newCouponScanAmount").toString());//cursor.getColumnIndex("newCouponScanAmount")));
                totalamount = totalamount + amt;
            } else {
                totalamount = amt;

            }


            ContentValues contentValues = new ContentValues();
            contentValues.put("newCouponScanAmount", totalamount);
            //     db.update(TABLE_COUPONRECORDDATA, contentValues, "uId='" + uId + "'", null);
            db.update("couponPaymentAmount", contentValues, null, null);


            cursor.close();
            db.close();
            db2.close();
        } catch (Exception ex) {

        }
    }

    public boolean insertToRetailerSurvey(String entryDate, String timeStamp,
                                          String userCode, String state, String district,
                                          String territory, String tehsil, String unitType,
                                          String retailerFirmName, String contactPersonName,
                                          String retailerNumber, String retailerAddress, String retailerCordinates,
                                          String whatsappNumber, String asscoidistributor,
                                          String majorVegCrops, String estimatedAvgVolumeSeedSold,
                                          String estimatedVolumeMahycoSungro, String mahycoProductSold,
                                          String sungroProductSold, String retailerOtherCompany,
                                          String businessOtherThenSeed, String isSynced) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("entryDate", entryDate);
        contentValues.put("timeStamp", timeStamp);
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("territory", territory);
        contentValues.put("tehsil", tehsil);
        contentValues.put("unitType", unitType);
        contentValues.put("retailerFirmName", retailerFirmName);
        contentValues.put("contactPersonName", contactPersonName);
        contentValues.put("retailerNumber", retailerNumber);
        contentValues.put("retailerAddress", retailerAddress);
        contentValues.put("retailerCordinates", retailerCordinates);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("asscoidistributor", asscoidistributor);
        contentValues.put("majorVegCrops", majorVegCrops);
        contentValues.put("estimatedAvgVolumeSeedSold", estimatedAvgVolumeSeedSold);
        contentValues.put("estimatedVolumeMahycoSungro", estimatedVolumeMahycoSungro);
        contentValues.put("mahycoProductSold", mahycoProductSold);
        contentValues.put("sungroProductSold", sungroProductSold);
        contentValues.put("retailerOtherCompany", retailerOtherCompany);
        contentValues.put("businessOtherThenSeed", businessOtherThenSeed);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_RETAILERSURVEYDATA, null, contentValues);
        db.close();
        return true;


    }

    public boolean insertToKisanClub(String entrydate, String timeStamp, String userCode,
                                     String state, String territory, String district,
                                     String tehsil, String village, String unitType,
                                     String farmerName, String mobileNumber, String whatsappNumber,
                                     String vegetableChoice, String area, String cropSown,
                                     String productAwareness, String ratingMahyco, String ratingSungro,
                                     String isSynced, String comments) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("entryDate", entrydate);
        contentValues.put("timeStamp", timeStamp);
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("territory", territory);
        contentValues.put("district", district);

        contentValues.put("tehsil", tehsil);
        contentValues.put("village", village);
        contentValues.put("unitType", unitType);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("vegetableChoice", vegetableChoice);
        contentValues.put("area", area);
        contentValues.put("cropSown", cropSown);
        contentValues.put("productAwareness", productAwareness);
        contentValues.put("ratingMahyco", ratingMahyco);
        contentValues.put("ratingSungro", ratingSungro);
        contentValues.put("isSynced", isSynced);
        contentValues.put("comments", comments);
        db.insert(TABLE_KISANCLUBDATA, null, contentValues);
        db.close();
        return true;


    }

    public boolean insertStateTerritoryMasterData(String zone, String territory, String state, String state_code) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zone", zone);
        contentValues.put("territory", territory);
        contentValues.put("state", state);
        contentValues.put("state_code", state_code);
        db.insert(TABLE_STATETERRITORYMASTER, null, contentValues);
        //db.close();
        return true;


    }

    public boolean insertFocussedVillageMasterData(String vil_code, String vil_desc, String taluka) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("vil_code", vil_code);
        contentValues.put("vil_desc", vil_desc);
        contentValues.put("taluka", taluka);
        db.insert(TABLE_FOCUSSEDVILLAGEMASTER, null, contentValues);
        //db.close();
        return true;


    }

    public boolean insertCheckHybridMaster(String hybridName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hybridName", hybridName);
        db.insert(TABLE_CHECKHYBRIDMASTER_DATA, null, contentValues);
        // db.close();
        return true;
    }

    public boolean insertRbm(String code, String desc, String TBMCode, String TBMName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("description", desc);
        contentValues.put("TBMName", TBMName);
        contentValues.put("TBMCode", TBMCode);
        db.insert(TABLE_RBM_MASTER, null, contentValues);
        // db.close();
        return true;
    }

    public boolean insertMdoTbm(String mdoCode, String mdoDesc, String tbmCode, String tbmDesc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mdoCode", mdoCode);
        contentValues.put("mdoDesc", mdoDesc);
        contentValues.put("tbmCode", tbmCode);
        contentValues.put("tbmDesc", tbmDesc);
        db.insert(TABLE_MDO_TBM, null, contentValues);
        //db.close();
        return true;
    }

    public boolean insertRetailerDetailsMaster(String state, String type, String mdoCode, String marketPlace, String dist, String taluka,
                                               String mobileNo, String firmName, String retailercode, String retailerName) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", state);
        contentValues.put("type", type);
        contentValues.put("mdoCode", mdoCode);
        contentValues.put("marketPlace", marketPlace);
        contentValues.put("dist", dist);
        contentValues.put("taluka", taluka);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("firmName", firmName);
        contentValues.put("retailerCode", retailercode);
        contentValues.put("retailerName", retailerName);
        db.insert(TABLE_RETAILERDETAILSMASTER, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertDemoValidationData(String uId, String userCode, String plotType,
                                            String state, String district,
                                            String taluka, String village, String farmerName,
                                            String mobileNumber, String whatsappNumber, String cropType,
                                            String product, String area, String seedQuantity, String unit,
                                            String sowingDate, String coordinates, String soilType, String irrigationMode,
                                            String spacingRow, String spacingPlan, String imgName, String imgPath, String imgStatus, String isSynced, String checkHybrids, String remarks, String mdoCode, String entrydate, String isUploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uId", uId);
        contentValues.put("userCode", userCode);
        contentValues.put("plotType", plotType);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("crop", cropType);
        contentValues.put("product", product);
        contentValues.put("area", area);
        contentValues.put("seedQuantity", seedQuantity);
        contentValues.put("unit", unit);
        contentValues.put("sowingDate", sowingDate);
        contentValues.put("coordinates", coordinates);
        contentValues.put("soilType", soilType);
        contentValues.put("irrigationMode", irrigationMode);
        contentValues.put("spacingRow", spacingRow);
        contentValues.put("spacingPlan", spacingPlan);
        contentValues.put("imgName", imgName);
        contentValues.put("imgPath", imgPath);
        contentValues.put("imgStatus", imgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("checkHybrids", checkHybrids);
        contentValues.put("remarks", remarks);
        contentValues.put("mdoCode", mdoCode);
        contentValues.put("entrydate", entrydate);
        contentValues.put("isUploaded", isUploaded);
        db.insert(TABLE_VALIDATEDDEMOMODELDATA, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertMDOlistData(String userCode, String mdoname) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_code", userCode);
        contentValues.put("MDO_name", mdoname);
        db.insert(TABLE_MDOLISTDATA, null, contentValues);
        db.close();
        return true;
    }


    public boolean insertRetailerVisitToFieldData(String userCode,
                                                  String state, String district,
                                                  String farmerName,
                                                  String farmerMobileNumber,
                                                  String farmerVillage, String farmerTaluka, String cropType,
                                                  String product,
                                                  String retailerCount,
                                                  String coordinates,
                                                  String address, String activityImgName,
                                                  String activityImgPath, String farmerListImgName,
                                                  String farmerListImgPath, String activityImgStatus,
                                                  String farmerListImgStatus, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("farmerName", farmerName);
        contentValues.put("farmerMobileNumber", farmerMobileNumber);
        contentValues.put("farmerVillage", farmerVillage);
        contentValues.put("farmerTaluka", farmerTaluka);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("retailerCount", retailerCount);
        contentValues.put("coordinates", coordinates);
        contentValues.put("address", address);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("farmerListImgName", farmerListImgName);
        contentValues.put("farmerListImgPath", farmerListImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListImgStatus", farmerListImgStatus);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_RETAILERTOVISITDATA, null, contentValues);
        db.close();
        return true;
    }


    public boolean insertLivePlantDisplayVillageData(String userCode, String uId,
                                                     String focussedVillage, String state, String district, String taluka, String village,
                                                     String farmerCount, String cropType,
                                                     String product,
                                                     String taggedAddress,
                                                     String coordinates,
                                                     String activityImgName,
                                                     String activityImgPath, String activityImgStatus,
                                                     String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("uId", uId);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("farmerCount", farmerCount);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("coordinates", coordinates);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_LIVEPLANTDISPLAYVILLAGEDATA, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertLivePlantDisplayRetailerData(String userCode,
                                                      String focussedVillage, String state, String district, String taluka, String marketPlace, String retailerDetails,
                                                      String farmerCount, String cropType,
                                                      String product,
                                                      String taggedAddress,
                                                      String coordinates,
                                                      String activityImgName,
                                                      String activityImgPath, String activityImgStatus,
                                                      String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("marketPlace", marketPlace);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("farmerCount", farmerCount);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("coordinates", coordinates);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_LIVEPLANTDISPLAYRETAILERDATA, null, contentValues);
        db.close();
        return true;
    }


    public Cursor fetchusercode() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT RoleID FROM UserMaster", null);
        //db.close();
        return mCursor;
    }

    public boolean insertFieldDayData(String userCode, String focussedVillage, String state, String district,
                                      String taluka, String othervillage, String retailerCount, String farmerDetails,
                                      String pkFarmerMobileNumber, String cropType, String product, String taggedAddress, String taggedCordinates,
                                      String finalVillageJSON, String selectRBM, String selectTBM, String selectMDO, String activityImgName,
                                      String activityImgPath, String activityImgStatus, String farmerListPhotoName,
                                      String farmerListPhoto, String farmerListPhotoStatus,
                                      String retailerListPhotoName, String retailerListPhoto, String retailerListPhotoStatus,
                                      String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("othervillage", othervillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("retailerCount", retailerCount);
        contentValues.put("farmerDetails", farmerDetails);
        contentValues.put("pkFarmerMobileNumber", pkFarmerMobileNumber);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("coordinates", taggedCordinates);
        contentValues.put("selectRBM", selectRBM);
        contentValues.put("selectTBM", selectTBM);
        contentValues.put("selectMDO", selectMDO);
        contentValues.put("finalVillageJSON", finalVillageJSON);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoName", farmerListPhotoName);
        contentValues.put("farmerListPhoto", farmerListPhoto);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoName", retailerListPhotoName);
        contentValues.put("retailerListPhoto", retailerListPhoto);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_FIELDDAYDATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateFieldBoardData(String isNotSynced, String isSynced, String activityImgStatus,
                                     String farmerListPhotoStatus, String retailerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("fieldBoardImgStatus", activityImgStatus);
//        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        //    contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);

        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_FIELDBOARDDATA, contentValues, whereClause, null);

        //db.update(TABLE_FIELDBOARDDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }


    public void updateATLVillagePosteringDataDetails(String isNotSynced, String isSynced, String activityImgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        db.update(TABLE_ATL_POSTERING_DETAILS, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public void updateFieldBannerData(String isNotSynced, String isSynced, String activityImgStatus,
                                      String farmerListPhotoStatus, String retailerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("fieldBannerImgStatus", activityImgStatus);
//        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
//        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_FIELDBANNERDATA, contentValues, whereClause, null);

        //db.update(TABLE_FIELDBANNERDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public void updateFieldDayData(String isNotSynced, String isSynced, String activityImgStatus,
                                   String farmerListPhotoStatus, String retailerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_FIELDDAYDATA, contentValues, whereClause, null);

        // db.update(TABLE_FIELDDAYDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertCropShowData(String userCode, String focussedVillage, String state, String district, String taluka,
                                      String othervillage, String farmerDetails, String retailerDetails, String pkRetailerMobileNumber, String talukaMDO, String cropType,
                                      String product, String taggedAddress, String taggedCordinates, String finalVillageJSON, String activityImgName,
                                      String activityImgPath, String activityImgStatus, String farmerListPhotoName, String farmerListPhoto,
                                      String farmerListPhotoStatus, String retailerListPhotoName, String retailerListPhoto,
                                      String retailerListPhotoStatus,
                                      String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("othervillage", othervillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("pkRetailerMobileNumber", pkRetailerMobileNumber);
        contentValues.put("farmerDetails", farmerDetails);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("talukaMDO", talukaMDO);
        contentValues.put("coordinates", taggedCordinates);
        contentValues.put("finalVillageJSON", finalVillageJSON);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoName", farmerListPhotoName);
        contentValues.put("farmerListPhoto", farmerListPhoto);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoName", retailerListPhotoName);
        contentValues.put("retailerListPhoto", retailerListPhoto);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_CROPSHOWDATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateCropShowData(String isNotSynced, String isSynced, String activityImgStatus,
                                   String farmerListPhotoStatus, String retailerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_CROPSHOWDATA, contentValues, whereClause, null);

        // db.update(TABLE_CROPSHOWDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertHarvestDayData(String userCode, String focussedVillage, String state, String district, String taluka, String othervillage,
                                        String farmerDetails, String retailerDetails, String pkRetailerMobileNumber, String cropType, String product, String taggedAddress,
                                        String taggedCordinates, String finalVillageJSON, String selectRBM, String selectTBM, String selectMDO,
                                        String activityImgName, String activityImgPath, String activityImgStatus, String farmerListPhotoName,
                                        String farmerListPhoto,
                                        String farmerListPhotoStatus,
                                        String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("othervillage", othervillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("farmerDetails", farmerDetails);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("pkRetailerMobileNumber", pkRetailerMobileNumber);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("coordinates", taggedCordinates);
        contentValues.put("selectRBM", selectRBM);
        contentValues.put("selectTBM", selectTBM);
        contentValues.put("selectMDO", selectMDO);
        contentValues.put("finalVillageJSON", finalVillageJSON);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoName", farmerListPhotoName);
        contentValues.put("farmerListPhoto", farmerListPhoto);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_HARVESTDAYDATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateHarvestDayData(String isNotSynced, String isSynced, String activityImgStatus,
                                     String farmerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_HARVESTDAYDATA, contentValues, whereClause, null);

        //  db.update(TABLE_HARVESTDAYDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertPurchaseListData(String userCode, String state, String district, String taluka, String marketPlace,
                                          String retailerDetail, String retailerNumber, String taggedAddress, String taggedCordinates, String finalVillageJSON,
                                          String farmerListPhotoName, String farmerListPhoto, String farmerListPhotoStatus, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("marketPlace", marketPlace);
        contentValues.put("retailerDetails", retailerDetail);
        contentValues.put("retailerNumber", retailerNumber);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("coordinates", taggedCordinates);
        contentValues.put("finalVillageJSON", finalVillageJSON);
        contentValues.put("farmerListPhotoName", farmerListPhotoName);
        contentValues.put("farmerListPhoto", farmerListPhoto);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_PURCHASELISTDATA, null, contentValues);
        db.close();
        return true;
    }

    public void updatePurchaseListData(String isNotSynced, String isSynced,
                                       String farmerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_PURCHASELISTDATA, contentValues, whereClause, null);

        //db.update(TABLE_PURCHASELISTDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - Testimonial Collection
    public boolean insertTestimonialCollectionData(String userCode, String focussedVillage, String state, String district, String taluka, String otherVillage,
                                                   String hardCopyTestimonialType, String videoTestimonialType, String farmerName, String farmerMobile,
                                                   String farmerPhotoName, String farmerPhotoPath, String farmerPhotoStatus,
                                                   String successPhotoName,
                                                   String successPhotoPath,
                                                   String successPhotoStatus,
                                                   String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("otherVillage", otherVillage);
        contentValues.put("hardCopyTestimonialType", hardCopyTestimonialType);
        contentValues.put("videoTestimonialType", videoTestimonialType);
        contentValues.put("farmerName", farmerName);
        contentValues.put("farmerMobile", farmerMobile);
        contentValues.put("farmerPhotoName", farmerPhotoName);
        contentValues.put("farmerPhotoPath", farmerPhotoPath);
        contentValues.put("farmerPhotoStatus", farmerPhotoStatus);
        contentValues.put("successPhotoName", successPhotoName);
        contentValues.put("successPhotoPath", successPhotoPath);
        contentValues.put("successPhotoStatus", successPhotoStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_TESTIMONIAL_COLLECTION, null, contentValues);
        db.close();
        return true;
    }

    public void updateTestimonialCollectionData(String isNotSynced, String isSynced, String farmerPhotoStatus,
                                                String successPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("farmerPhotoStatus", farmerPhotoStatus);
        contentValues.put("successPhotoStatus", successPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_TESTIMONIAL_COLLECTION, contentValues, whereClause, null);
        //db.update(TABLE_TESTIMONIAL_COLLECTION, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - SANMAN Mela
    public boolean insertSanmanMelaData(String userCode, String focussedVillage, String state, String district, String taluka, String otherVillage,
                                        String cropType, String product, String taggedAddress,
                                        String taggedCordinates, String finalFarmerJSON,
                                        String activityImgName,
                                        String activityImgPath, String activityImgStatus,
                                        String farmerCount, String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("otherVillage", otherVillage);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("farmerCount", farmerCount);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("finalFarmerJSON", finalFarmerJSON);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_SANMAN_MELA, null, contentValues);
        db.close();
        return true;
    }

    public void updateSanmanMelaData(String isNotSynced, String isSynced, String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_SANMAN_MELA, contentValues, whereClause, null);

        //  db.update(TABLE_SANMAN_MELA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - Village Meeting
    public boolean insertVillageMeetingData(String userCode, String focussedVillage, String state, String district, String taluka, String otherVillage,
                                            String cropType, String product, String taggedAddress, String taggedCordinates, String selectRBM,
                                            String selectTBM, String selectMDO, String retailerDetails, String talukaRetailer,
                                            String activityImgName, String activityImgPath, String activityImgStatus, String farmerCount, String retailerCount,
                                            String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("otherVillage", otherVillage);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("selectRBM", selectRBM);
        contentValues.put("selectTBM", selectTBM);
        contentValues.put("selectMDO", selectMDO);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("talukaRetailer", talukaRetailer);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerCount", farmerCount);
        contentValues.put("retailerCount", retailerCount);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_VILLAGE_MEETING, null, contentValues);
        db.close();
        return true;
    }

    public void updateVillageMeetingData(String isNotSynced, String isSynced, String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_VILLAGE_MEETING, contentValues, whereClause, null);

        // db.update(TABLE_VILLAGE_MEETING, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - Promotion
    public boolean insertPromotionData(String userCode, String programName, String focussedVillage, String state, String district, String taluka, String otherVillage,
                                       String cropType, String product, String taggedAddress, String taggedCordinates, String retailerDetails,
                                       String talukaRetailer, String activityImgName, String activityImgPath, String activityImgStatus,
                                       String farmerCount,
                                       String retailerCount, String isSynced, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("programName", programName);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("otherVillage", otherVillage);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("talukaRetailer", talukaRetailer);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerCount", farmerCount);
        contentValues.put("retailerCount", retailerCount);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_PROMOTION, null, contentValues);
        db.close();
        return true;
    }

    public void updatePromotionData(String isNotSynced, String isSynced, String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_PROMOTION, contentValues, whereClause, null);

        //db.update(TABLE_PROMOTION, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - Crop Seminar
    public boolean insertCropSeminarData(String userCode, String state, String district, String taluka, String eventVenue,
                                         String cropType, String product, String taggedAddress, String taggedCordinates, String finalVillageJSON,
                                         String selectRBM, String selectTBM, String selectMDO, String retailerCount,
                                         String activityImgName, String activityImgPath, String activityImgStatus,
                                         String farmerListPhotoName, String farmerListPhotoPath, String farmerListPhotoStatus,
                                         String retailerListPhotoName, String retailerListPhotoPath, String retailerListPhotoStatus,
                                         String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("eventVenue", eventVenue);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("finalVillageJSON", finalVillageJSON);
        contentValues.put("selectRBM", selectRBM);
        contentValues.put("selectTBM", selectTBM);
        contentValues.put("selectMDO", selectMDO);
        contentValues.put("retailerCount", retailerCount);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoName", farmerListPhotoName);
        contentValues.put("farmerListPhotoPath", farmerListPhotoPath);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoName", retailerListPhotoName);
        contentValues.put("retailerListPhotoPath", retailerListPhotoPath);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_CROP_SEMINAR, null, contentValues);
        db.close();
        return true;
    }

    public void updateCropSeminarData(String isNotSynced, String isSynced, String activityImgStatus,
                                      String farmerListPhotoStatus, String retailerListPhotoStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("farmerListPhotoStatus", farmerListPhotoStatus);
        contentValues.put("retailerListPhotoStatus", retailerListPhotoStatus);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_CROP_SEMINAR, contentValues, whereClause, null);

        //  db.update(TABLE_CROP_SEMINAR, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    // Pre Season - jeep Campagining
    public boolean insertJeepCampaginingData(String userCode, String state, String district, String taluka,
                                             String cropType, String product, String taggedAddress, String taggedCordinates,
                                             String taggedAddressEnd, String taggedCordinatesEnd,
                                             String OdometerReading, String startLocation, String endLocation, String rtoRegistrationNumber,
                                             String OdometerReadingEnd, String finalPopupJson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedAddressEnd", taggedAddressEnd);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("taggedCordinatesEnd", taggedCordinatesEnd);
        contentValues.put("OdometerReading", OdometerReading);
        contentValues.put("startLocation", startLocation);
        contentValues.put("endLocation", endLocation);
        contentValues.put("rtoRegistrationNumber", rtoRegistrationNumber);
        contentValues.put("OdometerReadingEnd", OdometerReadingEnd);
        contentValues.put("finalPopupJson", finalPopupJson);
        contentValues.put("isSynced", "0");
        db.insert(TABLE_JEEP_CAMPAIGNING, null, contentValues);
        db.close();
        return true;
    }

    // Pre Season - pop display
    public boolean insertPopDisplayData(String userCode, String state, String district, String taluka, String marketName,
                                        String retailerDetails, String finalToolJSON, String shopPhoto1Name,
                                        String shopImg1Path, String shopPhoto1Status, String shopPhoto2Name, String shopImg2Path,
                                        String shopPhoto2Status, String shopPhoto3Name, String shopImg3Path, String shopPhoto3Status, String isSynced) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("marketName", marketName);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("finalVillageJSON", finalToolJSON);
        contentValues.put("photoOneImgName", shopPhoto1Name);
        contentValues.put("photoOneImgPath", shopImg1Path);
        contentValues.put("photoOneImgStatus", shopPhoto1Status);
        contentValues.put("photoTwoImgName", shopPhoto2Name);
        contentValues.put("photoTwoImgPath", shopImg2Path);
        contentValues.put("photoTwoImgStatus", shopPhoto2Status);
        contentValues.put("photoThreeImgName", shopPhoto3Name);
        contentValues.put("photoThreeImgPath", shopImg3Path);
        contentValues.put("photoThreeImgStatus", shopPhoto3Status);
        contentValues.put("isSynced", isSynced);

        db.insert(TABLE_POP_DISPLAY, null, contentValues);
        db.close();
        return true;
    }


    public boolean insertFieldBoardData(String userCode, String focussedVillage, String state, String district, String taluka, String othervillage,
                                        String boardCount, String remarks, String farmerDetails, String pkFarmerMobileNumber,
                                        String cropType, String product, String taggedAddress, String taggedCordinates,
                                        String fieldBoardImgPathName, String fieldBoardImgPath, String fieldBoardImgStatus,
                                        String isSynced, String villagecode) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("boardCount", boardCount);
        contentValues.put("remarks", remarks);
        contentValues.put("farmerDetails", farmerDetails);
        contentValues.put("pkFarmerMobileNumber", pkFarmerMobileNumber);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("fieldBoardImgPathName", fieldBoardImgPathName);
        contentValues.put("fieldBoardImgPath", fieldBoardImgPath);
        contentValues.put("fieldBoardImgStatus", fieldBoardImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_FIELDBOARDDATA, null, contentValues);
        db.close();
        return true;

    }


    public boolean insertFieldBannerData(String userCode, String focussedVillage,
                                         String state, String district, String taluka,
                                         String othervillage, String bannerCount, String remarks,
                                         String farmerDetails, String pkFarmerMobileNumber, String cropType,
                                         String product, String taggedAddress, String taggedCordinates,
                                         String fieldBannerImgName, String fieldBannerImgPath,
                                         String fieldBannerImgStatus, String isSynced, String villagecode) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("bannerCount", bannerCount);
        contentValues.put("remarks", remarks);
        contentValues.put("farmerDetails", farmerDetails);
        contentValues.put("pkFarmerMobileNumber", pkFarmerMobileNumber);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("fieldBannerImgName", fieldBannerImgName);
        contentValues.put("fieldBannerImgPath", fieldBannerImgPath);
        contentValues.put("fieldBannerImgStatus", fieldBannerImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_FIELDBANNERDATA, null, contentValues);
        db.close();
        return true;

    }

    public boolean insertATLExhibitionData(String userCode, String focussedVillage,
                                           String state, String district, String taluka,
                                           String othervillage, String numberOfDays, String numberOfVisitors,
                                           String taggedAddress, String taggedCordinates,
                                           String selectRBM, String selectTBM, String selectMDO,
                                           String activityImgName, String activityImgPath,
                                           String activityImgStatus,
                                           String isSynced, String villagecode) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("numberOfDays", numberOfDays);
        contentValues.put("numberOfVisitors", numberOfVisitors);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("selectRBM", selectRBM);
        contentValues.put("selectTBM", selectTBM);
        contentValues.put("selectMDO", selectMDO);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);

        db.insert(TABLE_ATLEXHIBITIONDATA, null, contentValues);
        db.close();
        return true;

    }

    public void updateATLExhibitionData(String isNotSynced, String isSynced,
                                        String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);

        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_ATLEXHIBITIONDATA, contentValues, whereClause, null);

        // db.update(TABLE_ATLEXHIBITIONDATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertATLMarketDayData(String userCode, String focussedVillage,
                                          String state, String district, String taluka,
                                          String othervillage, String numberOfVisitors,
                                          String taggedAddress, String taggedCordinates,
                                          String activityImgName, String activityImgPath,
                                          String activityImgStatus, String isSynced, String villagecode) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("numberOfVisitors", numberOfVisitors);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_ATL_MARKETDAY_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateATLMarketDayData(String isNotSynced, String isSynced, String activityImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        // db.update(TABLE_ATL_MARKETDAY_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_ATL_MARKETDAY_DATA, contentValues, whereClause, null);


        db.close();
    }

    public boolean insertATLVillageWallPaintingData(String userCode, String selectedWallPaintingType, String focussedVillage,
                                                    String state, String district, String taluka, String othervillage,
                                                    String strMandiName, String taggedAddress, String taggedCordinates,
                                                    String taggedAddressMandiNameStart, String taggedCordinatesMandiNameStart,
                                                    String taggedAddressMandiNameEnd,
                                                    String taggedCordinatesMandiNameEnd,
                                                    String finalPopupJson, String villagecode) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("selectedWallPaintingType", selectedWallPaintingType);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("strMandiName", strMandiName);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("taggedAddressMandiNameStart", taggedAddressMandiNameStart);
        contentValues.put("taggedCordinatesMandiNameStart", taggedCordinatesMandiNameStart);
        contentValues.put("taggedAddressMandiNameEnd", taggedAddressMandiNameEnd);
        contentValues.put("taggedCordinatesMandiNameEnd", taggedCordinatesMandiNameEnd);
        contentValues.put("finalPopupJson", finalPopupJson);
        contentValues.put("isSynced", "0");
        contentValues.put("villagecode", villagecode);
        Date entrydate = new Date();
        final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);
        contentValues.put("EntryDt", InTime);

        db.insert(TABLE_ATL_WALL_PAINTING_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateWallpaintingDetailDataIntoMaster(String finalPopupJson,
                                                       String strdate, String activityImgStatus,
                                                       String farmerListPhotoStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("finalPopupJson", finalPopupJson);
        db.update(TABLE_FIELDDAYDATA, contentValues, "strftime( '%Y-%m-%d', EntryDt)='" + strdate + "'", null);
        db.close();
    }

    public boolean insertATLVillageWallPaintingDetails(String userCode, String croptypePopup, String productNamePopup, String taggedAddressPopup,
                                                       String taggedCordinatesPopup, String strWidthInFt, String strHeightInFt, String strTotalInSqFt,
                                                       String activityImgName, String activityImgPath, String activityImgStatus,
                                                       String strTotalWallPainted, String isSynced) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("croptypePopup", croptypePopup);
        contentValues.put("productNamePopup", productNamePopup);
        contentValues.put("taggedAddressPopup", taggedAddressPopup);
        contentValues.put("taggedCordinatesPopup", taggedCordinatesPopup);
        contentValues.put("strWidthInFt", strWidthInFt);
        contentValues.put("strHeightInFt", strHeightInFt);
        contentValues.put("strTotalInSqFt", strTotalInSqFt);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("strTotalWallPainted", strTotalWallPainted);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_ATL_WALL_PAINTING_DETAILS, null, contentValues);
        db.close();
        return true;
    }

    public void updateWallPaintingData(String finalPopupJson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("finalPopupJson", finalPopupJson);
        db.update(TABLE_ATL_WALL_PAINTING_DATA, contentValues, "finalPopupJson='" + finalPopupJson + "'", null);
        db.close();
    }


    public void updateATLVillageWallPaintingDataDetails(String isNotSynced, String isSynced, String activityImgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        db.update(TABLE_ATL_WALL_PAINTING_DETAILS, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }


    public void updateATLVillageWallPaintingData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String queryString = "SELECT * FROM ATLVillageWallPaintingData WHERE _id = '" + id + "' AND isSynced = '0'";

        Cursor cursor = db.rawQuery(queryString, null);
        int count = cursor.getCount();
        if (count > 0) {
            JSONArray jsonArrayUpdate = new JSONArray();

            jsonArrayUpdate = getResultsVillageDetails(queryString);
            Log.d("jsonArrayUpdate", jsonArrayUpdate.toString());

            String whereClause = "_id = '" + id + "' AND isSynced = '0'";
            contentValues.put("isSynced", isSynced);

            db.update(TABLE_ATL_WALL_PAINTING_DATA, contentValues, whereClause, null);
            cursor.close();
            db.close();

        }

    }


    public boolean insertATLVillagePosteringData(String userCode, String selectedPosteringType, String focussedVillage,
                                                 String state, String district, String taluka, String othervillage,
                                                 String strMandiName, String numberOfSpots, String taggedAddress, String taggedCordinates,
                                                 String taggedAddressMandiNameStart, String taggedCordinatesMandiNameStart,
                                                 String taggedAddressMandiNameEnd,
                                                 String taggedCordinatesMandiNameEnd,
                                                 String finalPopupJson, String villagecode
    ) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("selectedPosteringType", selectedPosteringType);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("strMandiName", strMandiName);
        contentValues.put("numberOfSpots", numberOfSpots);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("taggedAddressMandiNameStart", taggedAddressMandiNameStart);
        contentValues.put("taggedCordinatesMandiNameStart", taggedCordinatesMandiNameStart);
        contentValues.put("taggedAddressMandiNameEnd", taggedAddressMandiNameEnd);
        contentValues.put("taggedCordinatesMandiNameEnd", taggedCordinatesMandiNameEnd);
        contentValues.put("finalPopupJson", finalPopupJson);
        contentValues.put("isSynced", "0");
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_ATL_POSTERING_DATA, null, contentValues);
        db.close();
        return true;
    }


    public boolean insertATLVillagePosteringDetails(String userCode, String croptypePopup, String productNamePopup, String taggedAddressPopup,
                                                    String taggedCordinatesPopup, String activityImgName, String activityImgPath,
                                                    String activityImgStatus, String strTotalPosters, String totalSpots, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("croptypePopup", croptypePopup);
        contentValues.put("productNamePopup", productNamePopup);
        contentValues.put("taggedAddressPopup", taggedAddressPopup);
        contentValues.put("taggedCordinatesPopup", taggedCordinatesPopup);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("strTotalPosters", strTotalPosters);
        contentValues.put("totalSpots", totalSpots);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_ATL_POSTERING_DETAILS, null, contentValues);
        db.close();
        return true;
    }


    public void updateATLVillagePosteringData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        String queryString = "SELECT * FROM ATLVillagePosteringData WHERE _id = '" + id + "' AND isSynced = '0'";

        Cursor cursor = db.rawQuery(queryString, null);
        int count = cursor.getCount();
        if (count > 0) {
            JSONArray jsonArrayUpdate = new JSONArray();

            jsonArrayUpdate = getResultsVillageDetails(queryString);
            Log.d("jsonArrayUpdate", jsonArrayUpdate.toString());

            contentValues.put("isSynced", isSynced);
            String whereClause = "_id = '" + id + "' AND isSynced = '0'";
            db.update(TABLE_ATL_POSTERING_DATA, contentValues, whereClause, null);
            cursor.close();
            db.close();

        }

    }

    public boolean insertTrolleyPaintingData(String userCode, String focussedVillage, String state, String district, String taluka,
                                             String othervillage, String cropType, String product, String farmerName, String farmerMobileNumber,
                                             String trolleyNumber, String totalDimensionLeft, String totalDimensionRight, String totalDimensionBack,
                                             String totalDimensionFront, String leftSideImgName, String leftSideImgPath, String leftSideImgStatus,
                                             String rightSideImgName, String rightSideImgPath, String rightSideImgStatus,
                                             String backSideImgName, String backSideImgPath, String backSideImgStatus, String frontSideImgName,
                                             String frontSideImgPath,
                                             String frontSideImgStatus, String isSynced, String villagecode) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("othervillage", othervillage);
        contentValues.put("cropType", cropType);
        contentValues.put("product", product);
        contentValues.put("farmerName", farmerName);
        contentValues.put("farmerMobileNumber", farmerMobileNumber);
        contentValues.put("trolleyNumber", trolleyNumber);
        contentValues.put("totalDimensionLeft", totalDimensionLeft);
        contentValues.put("totalDimensionRight", totalDimensionRight);
        contentValues.put("totalDimensionBack", totalDimensionBack);
        contentValues.put("totalDimensionFront", totalDimensionFront);
        contentValues.put("leftSideImgName", leftSideImgName);
        contentValues.put("leftSideImgPath", leftSideImgPath);
        contentValues.put("leftSideImgStatus", leftSideImgStatus);
        contentValues.put("rightSideImgName", rightSideImgName);
        contentValues.put("rightSideImgPath", rightSideImgPath);
        contentValues.put("rightSideImgStatus", rightSideImgStatus);
        contentValues.put("backSideImgName", backSideImgName);
        contentValues.put("backSideImgPath", backSideImgPath);
        contentValues.put("backSideImgStatus", backSideImgStatus);
        contentValues.put("frontSideImgName", frontSideImgName);
        contentValues.put("frontSideImgPath", frontSideImgPath);
        contentValues.put("frontSideImgStatus", frontSideImgStatus);
        contentValues.put("isSynced", isSynced);
        contentValues.put("villagecode", villagecode);
        db.insert(TABLE_ATL_TROLLEY_PAINTING_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateTrolleyPaintingData(String isNotSynced, String isSynced, String leftSideImgStatus,
                                          String rightSideImgStatus, String backSideImgStatus,
                                          String frontSideImgStatus, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("leftSideImgStatus", leftSideImgStatus);
        contentValues.put("rightSideImgStatus", rightSideImgStatus);
        contentValues.put("backSideImgStatus", backSideImgStatus);
        contentValues.put("frontSideImgStatus", frontSideImgStatus);

        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_ATL_TROLLEY_PAINTING_DATA, contentValues, whereClause, null);

        // db.update(TABLE_ATL_TROLLEY_PAINTING_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertDistributerVisitisData(String userCode, String state, String district, String taluka, String marketPlace,
                                                String distributerDetails, String comments, String commentDesc, String taggedAddress,
                                                String taggedCordinates, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("marketPlace", marketPlace);
        contentValues.put("distributerDetails", distributerDetails);
        contentValues.put("comments", comments);
        contentValues.put("commentDesc", commentDesc);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_DISTRIBUTOR_VISIT_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateDistributerVisitsData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_DISTRIBUTOR_VISIT_DATA, contentValues, whereClause, null);


        //db.update(TABLE_DISTRIBUTOR_VISIT_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);

        db.close();
    }

    public boolean insertReviewMeeitngData(String userCode, String state, String district, String taluka, String meetingPlace,
                                           String meetingPurpose, String comments, String taggedAddress,
                                           String taggedCordinates, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("meetingPlace", meetingPlace);
        contentValues.put("meetingPurpose", meetingPurpose);
        contentValues.put("comments", comments);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_REVIEW_MEETING_DATA, null, contentValues);
        db.close();
        return true;
    }


    public void updateReviewMeeitngData(String isNotSynced, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        db.update(TABLE_REVIEW_MEETING_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }


    public boolean insertRetailerVisitisData(String userCode, String state, String district, String taluka, String marketPlace,
                                             String retailerDetails, String comments, String commentDesc, String taggedAddress,
                                             String taggedCordinates, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("marketPlace", marketPlace);
        contentValues.put("retailerDetails", retailerDetails);
        contentValues.put("comments", comments);
        contentValues.put("commentDesc", commentDesc);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_RETAILER_VISIT_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateRetailerVisitsData(String isNotSynced, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        // String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        // db.update(TABLE_RETAILER_VISIT_DATA, contentValues, whereClause, null);

        db.update(TABLE_RETAILER_VISIT_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertFarmerVisitsData(String userCode, String focussedVillage, String state, String district, String taluka,
                                          String village, String crop, String product, String farmerName, String mobileNumber, String whatsappNumber,
                                          String totalLand, String comments, String commentDesc, String taggedAddress,
                                          String taggedCordinates,
                                          String isSynced, String isSamruddhaKisan, String villagecode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("crop", crop);
        contentValues.put("village", village);
        contentValues.put("product", product);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("totalLand", totalLand);
        contentValues.put("comments", comments);
        contentValues.put("commentDesc", commentDesc);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("isSynced", isSynced);
        contentValues.put("isSamruddhaKisan", isSamruddhaKisan);
        contentValues.put("villagecode", villagecode);

        db.insert(TABLE_FARMER_VISIT_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateFarmerVisitsData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);

        // String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        // db.update(TABLE_FARMER_VISIT_DATA, contentValues, whereClause, null);

        db.update(TABLE_FARMER_VISIT_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertSamruddhaKisanVisitsData(String userCode, String focussedVillage, String state, String district, String taluka,
                                                  String village, String crop, String product, String farmerName, String mobileNumber, String whatsappNumber,
                                                  String totalLand, String aadharNumber, String emailID, String taggedAddress,
                                                  String taggedCordinates, String isSynced) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("focussedVillage", focussedVillage);
        contentValues.put("userCode", userCode);
        contentValues.put("state", state);
        contentValues.put("district", district);
        contentValues.put("taluka", taluka);
        contentValues.put("village", village);
        contentValues.put("crop", crop);
        contentValues.put("village", village);
        contentValues.put("product", product);
        contentValues.put("farmerName", farmerName);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("whatsappNumber", whatsappNumber);
        contentValues.put("totalLand", totalLand);
        contentValues.put("aadharNumber", aadharNumber);
        contentValues.put("emailID", emailID);
        contentValues.put("taggedAddress", taggedAddress);
        contentValues.put("taggedCordinates", taggedCordinates);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_SAMRUDDHA_KISAN_VISIT_DATA, null, contentValues);
        db.close();
        return true;
    }

    public void updateSamruddhaKisanVisitsData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        // db.update(TABLE_SAMRUDDHA_KISAN_VISIT_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);
        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_SAMRUDDHA_KISAN_VISIT_DATA, contentValues, whereClause, null);


        db.close();
    }

    public boolean insertSamruddhaKisanValidationData(JSONObject jsonObject, String userCode) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", jsonObject.getString("id"));
        contentValues.put("focussedVillage", jsonObject.getString("focussedVillage"));
        contentValues.put("userCode", jsonObject.getString("userCode"));
        contentValues.put("state", jsonObject.getString("state"));
        contentValues.put("district", jsonObject.getString("district"));
        contentValues.put("taluka", jsonObject.getString("taluka"));
        contentValues.put("village", jsonObject.getString("village"));
        contentValues.put("crop", jsonObject.getString("crop"));
        contentValues.put("village", jsonObject.getString("village"));
        contentValues.put("product", jsonObject.getString("product"));
        contentValues.put("farmerName", jsonObject.getString("farmerName"));
        contentValues.put("mobileNumber", jsonObject.getString("mobileNumber"));
        contentValues.put("whatsappNumber", jsonObject.getString("whatsappNumber"));
        contentValues.put("totalLand", jsonObject.getString("totalLand"));
        contentValues.put("taggedAddress", jsonObject.getString("taggedAddress"));
        contentValues.put("taggedCordinates", jsonObject.getString("taggedCordinates"));
        contentValues.put("comment", jsonObject.getString("comment"));
        contentValues.put("commentDescription", jsonObject.getString("commentDescription"));
        contentValues.put("mdoCode", jsonObject.getString("userCode"));
        contentValues.put("tbmCode", userCode);
        contentValues.put("mdoDesc", jsonObject.getString("mdoDesc"));
        contentValues.put("tbmDesc", jsonObject.getString("tbmDesc"));
        contentValues.put("validateBy", userCode);// jsonObject.getString("userCode"));
        contentValues.put("action", jsonObject.getString("Action"));
        contentValues.put("reasons", jsonObject.getString("reasons"));
        contentValues.put("isSamruddhaKisan", jsonObject.getString("isSamruddhaKisan"));
        contentValues.put("EntryDt", jsonObject.getString("entryDt"));
        contentValues.put("isSynced", jsonObject.getString("isSynced"));

        // New Parameters Added
        contentValues.put("farmer_dob", jsonObject.getString("farmer_dob"));
        contentValues.put("farmer_anniversarydate", jsonObject.getString("farmer_anniversarydate"));
        contentValues.put("farmer_pincode", jsonObject.getString("farmer_pincode"));
        contentValues.put("farmer_landmark", jsonObject.getString("farmer_landmark"));
        contentValues.put("farmer_house_latlong", jsonObject.getString("farmer_house_latlong"));
        contentValues.put("farmer_house_address", jsonObject.getString("farmer_house_address"));
        contentValues.put("farmer_photo_path", jsonObject.getString("farmer_photo_path"));
        contentValues.put("farmer_photo_name", jsonObject.getString("farmer_photo_name"));

        db.insert(TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA, null, contentValues);
        db.close();
        return true;
    }

    public boolean updateSamruddhaKisanValidationData(String isNotSynced, JSONObject jsonObject) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String queryString = "SELECT * FROM SamruddhaKisanValidationData WHERE id = '" + jsonObject.getString("_id") + "' ";

        Cursor cursor = db.rawQuery(queryString, null);
        int count = cursor.getCount();
        if (count > 0) {
            JSONArray jsonArrayUpdate = new JSONArray();

            jsonArrayUpdate = getResultsVillageDetails(queryString);

            Log.d("jsonArrayUpdate", jsonArrayUpdate.toString());

            String whereClause = "id = '" + jsonObject.getString("_id") + "' ";

            contentValues.put("district", jsonObject.getString("district"));
            contentValues.put("taluka", jsonObject.getString("taluka"));
            contentValues.put("village", jsonObject.getString("village"));
            contentValues.put("crop", jsonObject.getString("crop"));
            contentValues.put("village", jsonObject.getString("village"));
            contentValues.put("farmerName", jsonObject.getString("farmerName"));
            contentValues.put("mobileNumber", jsonObject.getString("mobileNumber"));
            contentValues.put("whatsappNumber", jsonObject.getString("whatsappNumber"));
            contentValues.put("totalLand", jsonObject.getString("totalLand"));
            contentValues.put("taggedAddress", jsonObject.getString("taggedAddress"));
            contentValues.put("mdoDesc", jsonObject.getString("mdoDesc"));
            contentValues.put("mdoCode", jsonObject.getString("mdoCode"));
            contentValues.put("action", jsonObject.getString("action"));
            contentValues.put("reasons", jsonObject.getString("reasons"));
            contentValues.put("EntryDt", jsonObject.getString("EntryDt"));
            contentValues.put("isSynced", jsonObject.getString("isSynced"));
//New Parameters Addition for samruddhi Kisan
            contentValues.put("farmer_dob", jsonObject.getString("farmer_dob"));
            contentValues.put("farmer_anniversarydate", jsonObject.getString("farmer_anniversarydate"));
            contentValues.put("farmer_pincode", jsonObject.getString("farmer_pincode"));
            contentValues.put("farmer_landmark", jsonObject.getString("farmer_landmark"));
            contentValues.put("farmer_house_latlong", jsonObject.getString("farmer_house_latlong"));
            contentValues.put("farmer_house_address", jsonObject.getString("farmer_house_address"));
            contentValues.put("farmer_photo_path", jsonObject.getString("farmer_photo_path"));
            contentValues.put("farmer_photo_name", jsonObject.getString("farmer_photo_name"));


            db.update(TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA, contentValues, whereClause, null);
            cursor.close();
            db.close();

        }
        return true;
    }


    public void updateSamruddhaKisanUploadData(String isNotSynced, String isSynced, JSONObject jsonObject) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        JSONArray jsonArray = jsonObject.getJSONObject("table").getJSONArray("Table");

        for (int i = 0; i < jsonArray.length(); i++) {
            String queryString = "SELECT * FROM SamruddhaKisanValidationData WHERE id = '" + jsonArray.getJSONObject(i).getString("id") + "' ";

            Cursor cursor = db.rawQuery(queryString, null);
            int count = cursor.getCount();
            if (count > 0) {

                JSONArray jsonArrayUpdate = new JSONArray();

                jsonArrayUpdate = getResultsVillageDetails(queryString);

                Log.d("jsonArrayUpdate", jsonArrayUpdate.toString());

                String whereClause = "id = '" + jsonArray.getJSONObject(i).getString("id") + "' ";
                contentValues.put("isSynced", isSynced);

                db.update(TABLE_SAMRUDDHA_KISAN_VALIDATION_DATA, contentValues, whereClause, null);
                db.close();
            }
        }
    }

    public void updateJeepCampaigningData(String isNotSynced, String isSynced, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        String queryString = "SELECT * FROM JeepCampaigningData WHERE _id = '" + id + "' AND isSynced = '0'";

        Cursor cursor = db.rawQuery(queryString, null);
        int count = cursor.getCount();
        if (count > 0) {
            JSONArray jsonArrayUpdate = new JSONArray();

            jsonArrayUpdate = getResultsVillageDetails(queryString);
            Log.d("jsonArrayUpdate", jsonArrayUpdate.toString());

            String whereClause = "_id = '" + id + "' AND isSynced = '0'";
            contentValues.put("isSynced", isSynced);

            db.update(TABLE_JEEP_CAMPAIGNING, contentValues, whereClause, null);
            cursor.close();
            db.close();

        }


    }

    //for pop display
    public void updatePopDisplayData(String isNotSynced, String isSynced, String shopImg1Status,
                                     String shopImg2Status, String shopImg3Status, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("photoOneImgStatus", shopImg1Status);
        contentValues.put("photoTwoImgStatus", shopImg2Status);
        contentValues.put("photoThreeImgStatus", shopImg3Status);

        String whereClause = "_id = '" + id + "' AND isSynced = '0'";
        db.update(TABLE_POP_DISPLAY, contentValues, whereClause, null);

        // db.update(TABLE_POP_DISPLAY, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public boolean insertJeepCampaginingLocationDetails(String userCode, String statePopup,
                                                        String districtPopup, String talukaPopup,
                                                        String villagePopup, String startTime, String endTime,
                                                        String noOfFarmers, String taggedAddressPopup,
                                                        String taggedCordinatesPopup, String activityImgName,
                                                        String activityImgPath, String activityImgStatus,
                                                        String isSynced) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", userCode);
        contentValues.put("statePopup", statePopup);
        contentValues.put("districtPopup", districtPopup);
        contentValues.put("talukaPopup", talukaPopup);
        contentValues.put("villagePopup", villagePopup);
        contentValues.put("startTime", startTime);
        contentValues.put("endTime", endTime);
        contentValues.put("noOfFarmers", noOfFarmers);
        contentValues.put("taggedAddressPopup", taggedAddressPopup);
        contentValues.put("taggedCordinatesPopup", taggedCordinatesPopup);
        contentValues.put("activityImgName", activityImgName);
        contentValues.put("activityImgPath", activityImgPath);
        contentValues.put("activityImgStatus", activityImgStatus);
        contentValues.put("isSynced", isSynced);
        db.insert(TABLE_JEEP_CAMPAIGNING_LOC_DETAILS, null, contentValues);
        db.close();
        return true;


    }

    public int checkentryexist(String query) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        try {
            String countQuery = query;

            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } catch (Exception ex) {
            Log.d("MAA", "getnocoloumncount() : " + ex.toString());
        }
        //db.close();
        return count;
    }

    public void checkexisttable() {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE  IF NOT EXISTS " + TABLE_PRODUCTS + "(Password TEXT," + UserCode + " TEXT," + IMEINo + " TEXT" + ",DisplayName TEXT,RoleID TEXT,unit TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
        String CREATE_PRODUCTS_TABLE2 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS2 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + UserCode + " TEXT,imeino TEXT,coordinates TEXT,address TEXT,entrydate DATE,Flag TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE2);
        String CREATE_PRODUCTS_TABLE3 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS3 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + UserCode + " TEXT,VisitNo TEXT,imeino TEXT,VisitType TEXT,coordinate TEXT,Outcoordinate TEXT,Location TEXT,OutLocation TEXT,CropType TEXT,ProductName TEXT,NoofFormer TEXT,InTime DATE,Comments TEXT,Status TEXT,Dist TEXT,Taluka TEXT,Village TEXT,Imgname TEXT,Img BLOB,Imgname2 TEXT,Img2 BLOB,commentdecription TEXT,imgstatus TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE3);
        String CREATE_PRODUCTS_TABLE4 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS4 + "(ProductName TEXT," +
                "CropName TEXT,CropType TEXT,Crop_Code TEXT,Prod_Code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE4);
        String CREATE_PRODUCTS_TABLE5 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS5 + "(ActvityName TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE5);
        String CREATE_PRODUCTS_TABLE6 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS6 + "(FarmerName TEXT,TalukaName TEXT," +
                "VillageName TEXT,AadharNo TEXT,MobileNo TEXT,Email TEXT,Status TEXT," +
                "statename TEXT,dist TEXT,TotalLand TEXT,usercode TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE6);
        String CREATE_PRODUCTS_TABLE7 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS7 + "(state TEXT,state_code TEXT,district TEXT,district_code TEXT,taluka TEXT,taluka_code TEXT,village TEXT,village_code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE7);
        String CREATE_PRODUCTS_TABLE8 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS8 + "(RetailerName TEXT,taluka TEXT," +
                "taluka_code TEXT,activity TEXT,dist TEXT,code TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE8);
        String CREATE_PRODUCTS_TABLE9 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS9 + "(id TEXT,visitno TEXT,crop TEXT,product TEXT,stock TEXT,currentstock TEXT,salestock TEXT,status TEXT,INTime DATE)";
        db.execSQL(CREATE_PRODUCTS_TABLE9);
        String CREATE_PRODUCTS_TABLE10 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS10 + "(comment TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE10);
        String CREATE_PRODUCTS_TABLE11 = "CREATE    TABLE  IF NOT EXISTS  " + TABLE_PRODUCTS11 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,UserCode TEXT,Name TEXT,Name2 TEXT ,Name3 TEXT,Type TEXT,VisitNo TEXT," +
                "Mobileno TEXT,Dist TEXT,Taluka TEXT,Village TEXT,Imgname TEXT,TotalPkt TEXT," +
                "TotalMahycopkt TEXT,Remark TEXT,VehicleNo TEXT,Status TEXT,imagepath TEXT," +
                "location TEXT,firmname TEXT,PaddyTotalpkt TEXT,PaddyMahycototalPkt TEXT," +
                " MaizeTotalpkt TEXT, MaizeMahycototalPkt TEXT,Year TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE11);
        String CREATE_PRODUCTS_TABLE12 = "CREATE    TABLE  IF NOT EXISTS  " + TABLE_PRODUCTS12 + "(VehicleNo TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE12);
        String CREATE_PRODUCTS_TABLE13 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS13 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,startdate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE13);
        String CREATE_PRODUCTS_TABLE14 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS14 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,place TEXT,coordinate TEXT,startaddress TEXT,date TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE14);
        String CREATE_PRODUCTS_TABLE15 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS15 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT,dist TEXT,taluka TEXT,marketplace TEXT," +
                "retailername TEXT ,retailerfirm TEXT,age TEXT, mobileno TEXT,asscoi_distributor TEXT,keyretailer TEXT," +
                "otherbussiness TEXT,experianceSeedwork TEXT, comments TEXT,Status TEXT,type TEXT," +
                "newfirm TEXT,birthdate TEXT,WhatsappNo TEXT,state TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE15);
        String CREATE_PRODUCTS_TABLE16 = "CREATE    TABLE IF NOT EXISTS  " + TABLE_PRODUCTS16 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE16);


        String CREATE_PRODUCTS_TABLE17 = "CREATE    TABLE  IF NOT EXISTS " + TABLE_PRODUCTS17 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,coordinate TEXT,startaddress TEXT ,enddate TEXT," +
                "dist TEXT,taluka TEXT," +
                "village TEXT,imgname TEXT,imgpath TEXT,Status TEXT,txtkm TEXT,place TEXT,imgstatus TEXT,vehicletype TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE17);


        String CREATE_TABLE_DEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,area TEXT,seedQuantity TEXT,unit TEXT,sowingDate TEXT,coordinates TEXT,soilType Text,irrigationMode Text,spacingRow TEXT,spacingPlan TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT, checkHybrids TEXT,remarks TEXT)";
        db.execSQL(CREATE_TABLE_DEMOMODELDATA);


        String CREATE_TABLE_DEMOREVIEWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_DEMOREVIEWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate TEXT,sowingDate TEXT,coordinates TEXT,purposeVisit TEXT,comment TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT)";
        db.execSQL(CREATE_TABLE_DEMOREVIEWDATA);

        String CREATE_TABLE_VALIDATED_DEMOMODELDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOMODELDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,userCode TEXT,plotType TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,area TEXT,seedQuantity TEXT,unit TEXT,sowingDate TEXT,coordinates TEXT,soilType Text,irrigationMode Text,spacingRow TEXT,spacingPlan TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT, checkHybrids TEXT,remarks TEXT,mdoCode TEXT DEFAULT '',entrydate TEXT,isUploaded TEXT)";
        db.execSQL(CREATE_TABLE_VALIDATED_DEMOMODELDATA);

        String CREATE_TABLE_VALIDATED_DEMOREVIEWDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_VALIDATEDDEMOREVIEWDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,uIdP TEXT,userCode TEXT,taluka TEXT," +
                "farmerName TEXT,mobileNumber TEXT,crop TEXT, product TEXT,area TEXT,visitingDate TEXT,sowingDate TEXT,coordinates TEXT,purposeVisit TEXT,comment TEXT,imgName TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT,mdoCode TEXT  DEFAULT '',entrydate TEXT,isUploaded TEXT)";
        db.execSQL(CREATE_TABLE_VALIDATED_DEMOREVIEWDATA);


        String CREATE_TABLE_MDOLISTDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_MDOLISTDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,user_code TEXT,MDO_name TEXT)";
        db.execSQL(CREATE_TABLE_MDOLISTDATA);

        String CREATE_TABLE_COUPONSCHEMEMASTER1 = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONSCHEMEMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,year TEXT,season TEXT,state TEXT,region TEXT," +
                "crop TEXT,product TEXT,productCode TEXT,schemeName TEXT,schemeUnit TEXT," +
                "advAmtPerUnit TEXT,perUnitBenifitOffer TEXT,couponValue TEXT,schemeFrom TEXT,schemeEnd TEXT)";
        db.execSQL(CREATE_TABLE_COUPONSCHEMEMASTER1);

        String CREATE_TABLE_COUPONRECORDDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_COUPONRECORDDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,uId TEXT,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,taluka TEXT,village TEXT," +
                "farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,crop TEXT, product TEXT,quantityPacket TEXT,packetsBooked TEXT,couponsIssued TEXT,totalAmount TEXT,couponQr1 TEXT DEFAULT '',couponQr2 TEXT DEFAULT '',couponQr3 TEXT DEFAULT ''," +
                "couponQr4 TEXT DEFAULT '',couponQr5 TEXT DEFAULT '',couponQr6 TEXT DEFAULT ''," +
                "farmerPhoto TEXT,imgPath TEXT,imgStatus TEXT,isSynced TEXT,address TEXT,cordinates TEXT)";
        db.execSQL(CREATE_TABLE_COUPONRECORDDATA);


        String CREATE_TABLE_RETAILERSURVEYDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_RETAILERSURVEYDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT,timeStamp TEXT,userCode TEXT,state TEXT,district TEXT,territory TEXT,tehsil TEXT," +
                "unitType TEXT,retailerFirmName TEXT,contactPersonName TEXT,retailerNumber TEXT,retailerAddress TEXT, retailerCordinates TEXT," +
                "whatsappNumber TEXT DEFAULT '' ,asscoidistributor TEXT,majorVegCrops TEXT,estimatedAvgVolumeSeedSold TEXT,estimatedVolumeMahycoSungro TEXT DEFAULT ''" +
                ",mahycoProductSold TEXT ,sungroProductSold TEXT " +
                ",retailerOtherCompany TEXT ,businessOtherThenSeed TEXT ,isSynced TEXT)";
        db.execSQL(CREATE_TABLE_RETAILERSURVEYDATA);


        String CREATE_TABLE_KISANCLUBDATA = " CREATE  TABLE IF NOT EXISTS " + TABLE_KISANCLUBDATA + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,entryDate TEXT, timeStamp TEXT,userCode TEXT,state TEXT,territory TEXT,district TEXT,tehsil TEXT,village TEXT," +
                "unitType TEXT, farmerName TEXT,mobileNumber TEXT,whatsappNumber TEXT,vegetableChoice TEXT," +
                "area TEXT DEFAULT '' ,cropSown TEXT,productAwareness TEXT," +
                "ratingMahyco TEXT  DEFAULT '',ratingSungro TEXT  DEFAULT '' ,isSynced TEXT,comments TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_KISANCLUBDATA);


        String CREATE_TABLE_STATETERRITORYMASTER = " CREATE  TABLE IF NOT EXISTS " + TABLE_STATETERRITORYMASTER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,zone TEXT,territory TEXT,state TEXT,state_code TEXT )";
        db.execSQL(CREATE_TABLE_STATETERRITORYMASTER);

        // Demand Servery Data
        String CREATE_PRODUCTS_TABLE18 = "CREATE    TABLE IF NOT EXISTS  mdo_demandassesmentsurvey(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,state TEXT,dist TEXT,taluka TEXT,village TEXT," +
                "mobileno TEXT,awaremahycoprd TEXT,entrydate DATE," +
                "Status TEXT,farmername TEXT,crop TEXT,coordinate TEXT,address TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE18);
        String CREATE_PRODUCTS_TABLE19 = "CREATE    TABLE  IF NOT EXISTS  mdo_cultivation(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,performance TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE19);
        String CREATE_PRODUCTS_TABLE20 = "CREATE    TABLE  IF NOT EXISTS  mdo_cultivationtobe(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,year TEXT,crop TEXT,area TEXT,hybridsown TEXT," +
                "mobileno TEXT,underhydarea TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE20);


        String CREATE_PRODUCTS_TABLE21 = "CREATE    TABLE IF NOT EXISTS  mdo_awaremahycoproduct(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,hybridsown TEXT," +
                "mobileno TEXT,typestatus TEXT,reason TEXT,entrydate DATE," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE21);
        String CREATE_PRODUCTS_TABLE22 = "CREATE    TABLE  IF NOT EXISTS  " + TABLE_PRODUCTS22 + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,mdocode TEXT,compname TEXT,crop TEXT ,product TEXT," +
                "qty TEXT,retailermobile TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE22);
        String CREATE_PRODUCTS_TABLE23 = "CREATE    TABLE  IF NOT EXISTS  mdo_photoupdate(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                "mdocode TEXT,imgname TEXT,imgpath TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE23);

        String CREATE_PRODUCTS_TABLE24 = "CREATE    TABLE IF NOT EXISTS   CompanyNamemaster(" + COLUMN_ID + " INTEGER PRIMARY KEY,Compname TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE24);

        String CREATE_PRODUCTS_TABLE25 = "CREATE   TABLE IF NOT EXISTS  SAPCROPMaster(ProductName TEXT,PRoductCode TEXT," +
                "CropName TEXT,CropCode TEXT,CropType TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE25);

        String CREATE_PRODUCTS_TABLE26 = "CREATE  TABLE IF NOT EXISTS  RetailerPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerName TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE26);

        String CREATE_PRODUCTS_TABLE27 = "CREATE  TABLE IF NOT EXISTS  RetailerCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE27);

        String CREATE_PRODUCTS_TABLE28 = "CREATE  TABLE  IF NOT EXISTS RetailerNextBtPOGTable(usercode TEXT," +
                "RetailerMobileno TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date TEXT,AssociateDistributor TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE28);

        String CREATE_PRODUCTS_TABLE29 = "CREATE  TABLE IF NOT EXISTS  DistributorCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,RetailerMobileno TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT," +
                "BalStock TEXT ,placementstock TEXT, stockDate TEXT,Status TEXT,DistrCode TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE29);

        String CREATE_PRODUCTS_TABLE30 = "CREATE  TABLE  IF NOT EXISTS  DistributorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,Mobileno TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT,plan TEXT," +
                "RecStockdepot TEXT,RecStockdistr TEXT,ShiftStockToDistr TEXT,NetStock TEXT," +
                "BalStock TEXT ,placementstock TEXT,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE30);

        String CREATE_PRODUCTS_TABLE31 = "CREATE  TABLE IF NOT EXISTS  DistributorNextBtPOGTable(usercode TEXT," +
                "Mobileno TEXT,DistrCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date  TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE31);

        String CREATE_PRODUCTS_TABLE32 = "CREATE  TABLE  IF NOT EXISTS mdo_pogsapdata(Cust_Code TEXT," +
                "Crop_Name TEXT ,Crop_Code TEXT,Prod_Code TEXT," +
                "Prod_Name TEXT,QTY_Unit TEXT," +
                "QTY_Base TEXT, Plan_Qty TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE32);

        String CREATE_PRODUCTS_TABLE33 = "CREATE  TABLE  IF NOT EXISTS  MDO_tagRetailerList(state TEXT," +
                "type TEXT ,mdocode TEXT,marketplace TEXT," +
                "dist TEXT,taluka TEXT," +
                "mobileno TEXT,firmname TEXT,name TEXT ,retailercode TEXT )";
        db.execSQL(CREATE_PRODUCTS_TABLE33);

        // DistributorAsRetailer
        String CREATE_PRODUCTS_TABLE34 = "CREATE  TABLE IF NOT EXISTS  DistributorAsRetailerPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE34);

        //DistributorAsRetailerCompetitator
        String CREATE_PRODUCTS_TABLE35 = "CREATE  TABLE IF NOT EXISTS  DistributorAsRetailerCompetitatorPOGTable(usercode TEXT," +
                "state TEXT,dist TEXT,Taluka TEXT,MktPlace TEXT,DistrCode TEXT," +
                "CropName TEXT,CropCode TEXT," +
                "CompanyName TEXT," +
                "RecStock TEXT,ShiftStock TEXT,NetStock TEXT,SoldStock TEXT," +
                "BalStock TEXT ,stockDate TEXT,Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE35);


        //DistributorAsRetailer BT
        String CREATE_PRODUCTS_TABLE36 = "CREATE  TABLE  IF NOT EXISTS  DistributorAsRetailerNextBtPOGTable(usercode TEXT," +
                "DistrCode TEXT," +
                "ProductName TEXT,ProductCode TEXT," +
                "BalStock TEXT," +
                "actionstatus TEXT,Qty TEXT,Date TEXT,AssociateDistributor TEXT," +
                "Status TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE36);
//
// PaymentTable
        String createPaymentTable = "CREATE  TABLE IF NOT EXISTS PaymentTable(" +
                "usercode TEXT," +
                "rzpPaymentId TEXT," +
                "orderId TEXT DEFAULT ''," +
                "farmersIdList TEXT," +
                "status TEXT," +
                "amount TEXT)";
        db.execSQL(createPaymentTable);
        // db.close();

    }

    public void updateJeepCampaigningImageData(String isNotSynced, String isSynced, String activityImgStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSynced", isSynced);
        contentValues.put("activityImgStatus", activityImgStatus);
        db.update(TABLE_JEEP_CAMPAIGNING_LOC_DETAILS, contentValues, "isSynced='" + isNotSynced + "'", null);
        db.close();
    }

    public synchronized JSONObject getResultsDetails(String Query) {
        SQLiteDatabase db = getReadableDatabase();
        JSONObject resultSet = new JSONObject();
        JSONObject rowObject = new JSONObject();
        try {
            String myTable = "Table1";//Set name of your table
            String searchQuery = Query;
            Cursor cursor = db.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                //JSONObject rowObject = new JSONObject();

                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                Log.d("TAG_NAME", cursor.getString(i));
                                if (cursor.getColumnName(i).equalsIgnoreCase("finalVillageJSON")) {
                                    JSONObject jsonArray = new JSONObject(cursor.getString(i));
                                    rowObject.put(cursor.getColumnName(i), jsonArray);
                                } else {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                            } else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }
                        } catch (Exception e) {
                            Log.d("TAG_NAME", e.getMessage());
                        }
                    }
                }
                // resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        return rowObject;
    }


    // User  Feedback saved
    public boolean insertUserFeedback(String usercode, String status) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("status", status);

        db.insert("MDO_UserFeedBackStatus", null, contentValues);
        db.close();
        return true;
    }

    public void updateblacksynced() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isSynced", "0");
            String whereClause = " isSynced !='1'";
            db.execSQL("update  " + TABLE_SANMAN_MELA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_VILLAGE_MEETING + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_PROMOTION + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_CROP_SEMINAR + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_JEEP_CAMPAIGNING + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_POP_DISPLAY + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_TESTIMONIAL_COLLECTION + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_FARMER_VISIT_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_RETAILER_VISIT_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_DISTRIBUTOR_VISIT_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_TESTIMONIAL_COLLECTION + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_FARMER_VISIT_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_FIELDBOARDDATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_FIELDBANNERDATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_ATLEXHIBITIONDATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_ATL_MARKETDAY_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_ATL_WALL_PAINTING_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_ATL_POSTERING_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_ATL_TROLLEY_PAINTING_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_ATL_MARKETDAY_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");

            db.execSQL("update  " + TABLE_ATL_WALL_PAINTING_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");
            db.execSQL("update  " + TABLE_ATL_POSTERING_DATA + " set isSynced = 0 where isSynced !='1' or isSynced IS NULL");


            //ATL


            //db.update(TABLE_DISTRIBUTOR_VISIT_DATA, contentValues, "isSynced='" + isNotSynced + "'", null);

            db.close();
        } catch (Exception ex) {
            Log.d("MAA", "update blankcsys() : " + ex.toString());
        }

    }

    public void updateimageblank(String str, String Tablename, String usercode) {
        SQLiteDatabase db = getReadableDatabase();
        JSONArray resultSet = new JSONArray();
        try {
            Date entrydate = new Date();
            long epoch;

            String timeStamp = "";
            String myTable = "Table1";//Set name of your table
            String searchQuery = str;
            Cursor cursor = db.rawQuery(searchQuery, null);
            String imagename = "";
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    epoch = entrydate.getTime();
                    //timeStamp = String.valueOf(epoch);
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
                    imagename = id + "_" + timeStamp + "_" + usercode;
                    updatebank(Tablename, id, imagename);
                    cursor.moveToNext();
                }
            }
            cursor.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //db.close();

    }

    public boolean updatebank(String tablename, String id, String imagename) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("imgname", imagename);
        initialValues.put("imgstatus", "0");


        boolean result = db.update(tablename, initialValues, "_id" + "='"
                + id + "'", null) > 0;

        db.close();
        return result;

    }

}
