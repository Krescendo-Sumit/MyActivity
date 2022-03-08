package myactvity.mahyco.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributorCallModel {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("UserCode")
        @Expose
        private String userCode;
        @SerializedName("tbm")
        @Expose
        private String tbm;
        @SerializedName("State")
        @Expose
        private String state;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("Taluka")
        @Expose
        private String taluka;
        @SerializedName("DistributorName")
        @Expose
        private String distributorName;
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
        @SerializedName("DistributorResponse")
        @Expose
        private String distributorResponse;
        @SerializedName("CallSummary")
        @Expose
        private String callSummary;
        @SerializedName("entryDt")
        @Expose
        private String entryDt;
    @SerializedName("distributorMobile")
    @Expose
    private String distributorMobile;

    @SerializedName("distributorFirmName")
    @Expose
    private String distributorFirmName;


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

        public String getTbm() {
            return tbm;
        }

        public void setTbm(String tbm) {
            this.tbm = tbm;
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

        public String getDistributorName() {
            return distributorName;
        }

        public void setDistributorName(String distributorName) {
            this.distributorName = distributorName;
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

        public String getDistributorResponse() {
            return distributorResponse;
        }

        public void setDistributorResponse(String distributorResponse) {
            this.distributorResponse = distributorResponse;
        }

        public String getCallSummary() {
            return callSummary;
        }

        public void setCallSummary(String callSummary) {
            this.callSummary = callSummary;
        }

        public String getEntryDt() {
            return entryDt;
        }

        public void setEntryDt(String entryDt) {
            this.entryDt = entryDt;
        }


    public void setDistributorMobile(String distributorMobile) {
        this.distributorMobile = distributorMobile;

    }
    public String getDistributorMobile() {
        return distributorMobile;
    }


    public void setDistributorFirmName(String distributorFirmName) {
        this.distributorFirmName = distributorFirmName;

    }
    public String getDistributorFirmName() {
        return distributorFirmName;
    }

}
