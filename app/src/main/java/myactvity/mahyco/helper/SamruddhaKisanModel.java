package myactvity.mahyco.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SamruddhaKisanModel implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userCode")
    @Expose
    private String userCode;
    @SerializedName("focussedVillage")
    @Expose
    private String focussedVillage;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("crop")
    @Expose
    private String crop;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("whatsappNumber")
    @Expose
    private String whatsappNumber;
    @SerializedName("totalLand")
    @Expose
    private String totalLand;
    @SerializedName("aadharNumber")
    @Expose
    private String aadharNumber;
    @SerializedName("emailID")
    @Expose
    private String emailID;
    @SerializedName("taggedAddress")
    @Expose
    private String taggedAddress;
    @SerializedName("taggedCordinates")
    @Expose
    private String taggedCordinates;
    @SerializedName("isSynced")
    @Expose
    private String isSynced;
    @SerializedName("EntryDt")
    @Expose
    private String entryDt;

    @SerializedName("validateBy")
    @Expose
    private String validateBy;
    @SerializedName("mdoCode")
    @Expose
    private String mdoCode;
    @SerializedName("tbmCode")
    @Expose
    private String tbmCode;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("reasons")
    @Expose
    private String reasons;

    @SerializedName("mdoDesc")
    @Expose
    private String mdoDesc;

    @SerializedName("tbmDesc")
    @Expose
    private String tbmDesc;


    public String getFarmer_photo_name() {
        return farmer_photo_name;
    }

    public void setFarmer_photo_name(String farmer_photo_name) {
        this.farmer_photo_name = farmer_photo_name;
    }

    @SerializedName("farmer_photo_name")
    @Expose
    private String farmer_photo_name;


    public String getFarmer_dob() {
        return farmer_dob;
    }

    public void setFarmer_dob(String farmer_dob) {
        this.farmer_dob = farmer_dob;
    }

    public String getFarmer_anniversarydate() {
        return farmer_anniversarydate;
    }

    public void setFarmer_anniversarydate(String farmer_anniversarydate) {
        this.farmer_anniversarydate = farmer_anniversarydate;
    }

    public String getFarmer_pincode() {
        return farmer_pincode;
    }

    public void setFarmer_pincode(String farmer_pincode) {
        this.farmer_pincode = farmer_pincode;
    }

    public String getFarmer_landmark() {
        return farmer_landmark;
    }

    public void setFarmer_landmark(String farmer_landmark) {
        this.farmer_landmark = farmer_landmark;
    }

    public String getFarmer_house_latlong() {
        return farmer_house_latlong;
    }

    public void setFarmer_house_latlong(String farmer_house_latlong) {
        this.farmer_house_latlong = farmer_house_latlong;
    }

    public String getFarmer_house_address() {
        return farmer_house_address;
    }

    public void setFarmer_house_address(String farmer_house_address) {
        this.farmer_house_address = farmer_house_address;
    }

    public String getFarmer_photo_path() {
        return farmer_photo_path;
    }

    public void setFarmer_photo_path(String farmer_photo_path) {
        this.farmer_photo_path = farmer_photo_path;
    }

    @SerializedName("farmer_dob")
    @Expose
    private String farmer_dob;
    @SerializedName("farmer_anniversarydate")
    @Expose
    private String farmer_anniversarydate;
    @SerializedName("farmer_pincode")
    @Expose
    private String farmer_pincode;
    @SerializedName("farmer_landmark")
    @Expose
    private String farmer_landmark;
    @SerializedName("farmer_house_latlong")
    @Expose
    private String farmer_house_latlong;
    @SerializedName("farmer_house_address")
    @Expose
    private String farmer_house_address;
    @SerializedName("farmer_photo_path")
    @Expose
    private String farmer_photo_path;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFocussedVillage() {
        return focussedVillage;
    }

    public void setFocussedVillage(String focussedVillage) {
        this.focussedVillage = focussedVillage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getTotalLand() {
        return totalLand;
    }

    public void setTotalLand(String totalLand) {
        this.totalLand = totalLand;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getTaggedAddress() {
        return taggedAddress;
    }

    public void setTaggedAddress(String taggedAddress) {
        this.taggedAddress = taggedAddress;
    }

    public String getTaggedCordinates() {
        return taggedCordinates;
    }

    public void setTaggedCordinates(String taggedCordinates) {
        this.taggedCordinates = taggedCordinates;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getEntryDt() {
        return entryDt;
    }

    public void setEntryDt(String entryDt) {
        this.entryDt = entryDt;
    }


    public String getValidateBy() {
        return validateBy;
    }

    public void setValidateBy(String validateBy) {
        this.validateBy = validateBy;
    }

    public String getMdoCode() {
        return mdoCode;
    }

    public void setMdoCode(String mdoCode) {
        this.mdoCode = mdoCode;
    }

    public String getTbmCode() {
        return tbmCode;
    }
    public String getMdoDesc() {
        return mdoDesc;
    }

    public void setMdoDesc(String mdoDesc) {
        this.mdoDesc = mdoDesc;
    }

    public String getTbmDesc() {
        return tbmDesc;
    }

    public void setTbmDesc(String tbmDesc) {
        this.tbmDesc = tbmDesc;
    }
    public void setTbmCode(String tbmCode) {
        this.tbmCode = tbmCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }


    String str_dob;

    public String getStr_dob() {
        return str_dob;
    }

    public void setStr_dob(String str_dob) {
        this.str_dob = str_dob;
    }

    public String getStr_aniversarydate() {
        return str_aniversarydate;
    }

    public void setStr_aniversarydate(String str_aniversarydate) {
        this.str_aniversarydate = str_aniversarydate;
    }

    String str_aniversarydate;


}
