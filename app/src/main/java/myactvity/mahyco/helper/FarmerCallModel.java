package myactvity.mahyco.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerCallModel {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("UserCode")
        @Expose
        private String userCode;
        @SerializedName("farmerName")
        @Expose
        private String farmerName;
        @SerializedName("farmerMobile")
        @Expose
        private String farmerMobile;
        @SerializedName("farmerType")
        @Expose
        private String farmerType;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("taluka")
        @Expose
        private String taluka;
        @SerializedName("focussedVillage")
        @Expose
        private String focussedVillage;
        @SerializedName("otherVillage")
        @Expose
        private String otherVillage;
        @SerializedName("totlaLand")
        @Expose
        private String totlaLand;
        @SerializedName("cropGrownByFarmer")
        @Expose
        private String cropGrownByFarmer;
        @SerializedName("productUsedByFarmer")
        @Expose
        private String productUsedByFarmer;
        @SerializedName("CallTypeProductPromotion")
        @Expose
        private String callTypeProductPromotion;
        @SerializedName("CallTypeOtherActivity")
        @Expose
        private String callTypeOtherActivity;
        @SerializedName("CropDiscussed")
        @Expose
        private String cropDiscussed;
        @SerializedName("ProductDiscussed")
        @Expose
        private String productDiscussed;
        @SerializedName("farmerResponse")
        @Expose
        private String farmerResponse;
        @SerializedName("callSummary")
        @Expose
        private String callSummary;
        @SerializedName("entryDt")
        @Expose
        private String entryDt;

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

        public String getFarmerName() {
            return farmerName;
        }

        public void setFarmerName(String farmerName) {
            this.farmerName = farmerName;
        }

        public String getFarmerMobile() {
            return farmerMobile;
        }

        public void setFarmerMobile(String farmerMobile) {
            this.farmerMobile = farmerMobile;
        }

        public String getFarmerType() {
            return farmerType;
        }

        public void setFarmerType(String farmerType) {
            this.farmerType = farmerType;
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

        public String getFocussedVillage() {
            return focussedVillage;
        }

        public void setFocussedVillage(String focussedVillage) {
            this.focussedVillage = focussedVillage;
        }

        public String getOtherVillage() {
            return otherVillage;
        }

        public void setOtherVillage(String otherVillage) {
            this.otherVillage = otherVillage;
        }

        public String getTotlaLand() {
            return totlaLand;
        }

        public void setTotlaLand(String totlaLand) {
            this.totlaLand = totlaLand;
        }

        public String getCropGrownByFarmer() {
            return cropGrownByFarmer;
        }

        public void setCropGrownByFarmer(String cropGrownByFarmer) {
            this.cropGrownByFarmer = cropGrownByFarmer;
        }

        public String getProductUsedByFarmer() {
            return productUsedByFarmer;
        }

        public void setProductUsedByFarmer(String productUsedByFarmer) {
            this.productUsedByFarmer = productUsedByFarmer;
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

        public String getCropDiscussed() {
            return cropDiscussed;
        }

        public void setCropDiscussed(String cropDiscussed) {
            this.cropDiscussed = cropDiscussed;
        }

        public String getProductDiscussed() {
            return productDiscussed;
        }

        public void setProductDiscussed(String productDiscussed) {
            this.productDiscussed = productDiscussed;
        }

        public String getFarmerResponse() {
            return farmerResponse;
        }

        public void setFarmerResponse(String farmerResponse) {
            this.farmerResponse = farmerResponse;
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


}
