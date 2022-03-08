package myactvity.mahyco.helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class RetailerCallModel {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("UserCode")
        @Expose
        private String userCode;
        @SerializedName("State")
        @Expose
        private String state;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("Taluka")
        @Expose
        private String taluka;
        @SerializedName("RetailerName")
        @Expose
        private String retailerName;
        @SerializedName("CallTypeProductPromotion")
        @Expose
        private String callTypeProductPromotion;
        @SerializedName("CallTypeOtherActivity")
        @Expose
        private String callTypeOtherActivity;
        @SerializedName("CropType")
        @Expose
        private String cropType;
        @SerializedName("ProductName")
        @Expose
        private String productName;
        @SerializedName("RetailerResponse")
        @Expose
        private String retailerResponse;
        @SerializedName("CallSummary")
        @Expose
        private String callSummary;
        @SerializedName("RetailerType")
        @Expose
        private String retailerType;
        @SerializedName("entryDt")
        @Expose
        private String entryDt;

    @SerializedName("validateDt")
    @Expose
    private String validateDt;
    @SerializedName("RetailerMobile")
    @Expose
    private String retailerMobile;

    @SerializedName("RetailerFirmName")
    @Expose
    private String retailerFirmName;


    public String getValidateDt() {
            return validateDt;
        }

        public void setValidateDt(String validateDt) {
            this.validateDt = validateDt;
        }
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

        public String getRetailerName() {
            return retailerName;
        }

        public void setRetailerName(String retailerName) {
            this.retailerName = retailerName;
        }

        public String getCallTypeProductPromotion() {
            return callTypeProductPromotion;
        }

        public void setCallTypeProductPromotion(String callTypeProductPromotion) {
            this.callTypeProductPromotion = callTypeProductPromotion;
        }

        public String getCallTypeOtherActivity() {
            return callTypeOtherActivity;
        }

        public void setCallTypeOtherActivity(String callTypeOtherActivity) {
            this.callTypeOtherActivity = callTypeOtherActivity;
        }

        public String getCropType() {
            return cropType;
        }

        public void setCropType(String cropType) {
            this.cropType = cropType;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getRetailerResponse() {
            return retailerResponse;
        }

        public void setRetailerResponse(String retailerResponse) {
            this.retailerResponse = retailerResponse;
        }


    public String getCallSummary() {
        return callSummary;
    }

    public void setCallSummary(String callSummary) {
        this.callSummary = callSummary;
    }


    public String getRetailerType() {
        return retailerType;
    }

    public void setRetailerType(String retailerType) {
        this.retailerType = retailerType;
    }
        public String getEntryDt() {
            return entryDt;
        }

        public void setEntryDt(String entryDt) {
            this.entryDt = entryDt;
        }


    public void setRetailerMobile(String retailerMobile) {
        this.retailerMobile = retailerMobile;
    }

    public String getRetailerMobile() {
        return retailerMobile;
    }

    public void setRetailerFirmName(String retailerFirmName) {
        this.retailerFirmName = retailerFirmName;
    }
    public String getRetailerFirmName() {
        return retailerFirmName;
    }
}
