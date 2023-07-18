package com.mahyco.customercomplaint.ccfnetwork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CCFPendingCmplntPojoMOdel extends CCFBaseApiResponse {

    @SerializedName("data")
    private ArrayList< Data> data = new ArrayList<>();

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("RegisterComplaintID")
        private long RegisterComplaintID = 0;

        @SerializedName("ComplaintType")
        private String ComplaintType = "";

        @SerializedName("ComplaintSubType")
        private String ComplaintSubType = "";

        @SerializedName("SubCategoryDesc")
        private String SubCategoryDesc = "";

        @SerializedName("LotNumber")
        private String LotNumber = "";

        @SerializedName("BusinessUnit")
        private String BusinessUnit = "";

        @SerializedName("CropDtl")
        private String CropDtl = "";

        @SerializedName("Hybrid_Variety")
        private String Hybrid_Variety = "";

        @SerializedName("PackingSize")
        private double PackingSize = 0;

        @SerializedName("LotQty")
        private String LotQty = "";

        @SerializedName("NoOfPkts")
        private int NoOfPkts = 0;

        @SerializedName("FarmerName")
        private String FarmerName = "";

        @SerializedName("FarmerContact")
        private String FarmerContact = "";

        @SerializedName("State")
        private String State = "";

        @SerializedName("Depot")
        private String Depot = "";

        @SerializedName("District")
        private String District = "";

        @SerializedName("Taluka")
        private String Taluka = "";

        @SerializedName("Village")
        private String Village = "";

        @SerializedName("DateOfComplaint")
        private String DateOfComplaint = "";

        @SerializedName("TBMSubmissionDate")
        private String TBMSubmissionDate = "";

        @SerializedName("SowingDate")
        private String SowingDate = "";

        @SerializedName("CropStage")
        private String CropStage = "";

        @SerializedName("OtherfarmersDtl")
        private String OtherfarmersDtl = "";

        @SerializedName("RemarksInput")
        private String RemarksInput = "";

        @SerializedName("ContactNoTBM")
        private String ContactNoTBM = "";

        @SerializedName("RaisedComplaintTBM")
        private String RaisedComplaintTBM = "";

        @SerializedName("RaisedComplaintTBM_RBM")
        private String RaisedComplaintTBM_RBM = "";

        @SerializedName("ComplaintStatus")
        private String ComplaintStatus = "";

        @SerializedName("Gp_AdmixtureType")
        private String Gp_AdmixtureType = "";

        @SerializedName("Gp_AdmixturePercent")
        private double Gp_AdmixturePercent;

        @SerializedName("Gp_OffType")
        private String Gp_OffType = "";

        @SerializedName("Gp_OffTypePercent")
        private double Gp_OffTypePercent = 0;

        @SerializedName("GrmBelowStd")
        private String GrmBelowStd = "";

        @SerializedName("GrmPercent")
        private double GrmPercent = 0;

        @SerializedName("LateGrm")
        private String LateGrm = "";

        @SerializedName("Rain_NoPktsOrBagDamaged")
        private int Rain_NoPktsOrBagDamaged = 0;

        @SerializedName("PhotoUploadedBy")
        private String PhotoUploadedBy = "";

        @SerializedName("Photo1UploadedPath")
        private String Photo1UploadedPath = "";

        @SerializedName("Photo2UploadedPath")
        private String Photo2UploadedPath = "";

        @SerializedName("Photo3UploadedPath")
        private String Photo3UploadedPath = "";

        @SerializedName("Photo4UploadedPath")
        private String Photo4UploadedPath = "";

        @SerializedName("No_PKtsBagDamagedOperationalComplaint")
        private int No_PKtsBagDamagedOperationalComplaint = 0;

        @SerializedName("DiseaseInfestationComment")
        private String DiseaseInfestationComment = "";

        @SerializedName("DaysAfterSowing")
        private int DaysAfterSowing = 0;

        @SerializedName("PestAttackComment")
        private String PestAttackComment = "";

        @SerializedName("OthersInput")
        private String OthersInput = "";

        @SerializedName("OtherManualTypingBox")
        private String OtherManualTypingBox = "";

        public long getRegisterComplaintID() {
            return RegisterComplaintID;
        }

        public void setRegisterComplaintID(long registerComplaintID) {
            RegisterComplaintID = registerComplaintID;
        }

        public String getComplaintType() {
            return ComplaintType;
        }

        public void setComplaintType(String complaintType) {
            ComplaintType = complaintType;
        }

        public String getComplaintSubType() {
            return ComplaintSubType;
        }

        public void setComplaintSubType(String complaintSubType) {
            ComplaintSubType = complaintSubType;
        }

        public String getSubCategoryDesc() {
            return SubCategoryDesc;
        }

        public void setSubCategoryDesc(String subCategoryDesc) {
            SubCategoryDesc = subCategoryDesc;
        }

        public String getLotNumber() {
            return LotNumber;
        }

        public void setLotNumber(String lotNumber) {
            LotNumber = lotNumber;
        }

        public String getBusinessUnit() {
            return BusinessUnit;
        }

        public void setBusinessUnit(String businessUnit) {
            BusinessUnit = businessUnit;
        }

        public String getCropDtl() {
            return CropDtl;
        }

        public void setCropDtl(String cropDtl) {
            CropDtl = cropDtl;
        }

        public String getHybrid_Variety() {
            return Hybrid_Variety;
        }

        public void setHybrid_Variety(String hybrid_Variety) {
            Hybrid_Variety = hybrid_Variety;
        }

        public double getPackingSize() {
            return PackingSize;
        }

        public void setPackingSize(double packingSize) {
            PackingSize = packingSize;
        }

        public String getLotQty() {
            return LotQty;
        }

        public void setLotQty(String lotQty) {
            LotQty = lotQty;
        }

        public int getNoOfPkts() {
            return NoOfPkts;
        }

        public void setNoOfPkts(int noOfPkts) {
            NoOfPkts = noOfPkts;
        }

//            public double getTotalLotQty() {
//                return TotalLotQty;
//            }
//
//            public void setTotalLotQty(double totalLotQty) {
//                TotalLotQty = totalLotQty;
//            }
//
//            public int getTotalNoOfPkts() {
//                return TotalNoOfPkts;
//            }
//
//            public void setTotalNoOfPkts(int totalNoOfPkts) {
//                TotalNoOfPkts = totalNoOfPkts;
//            }

        public String getFarmerName() {
            return FarmerName;
        }

        public void setFarmerName(String farmerName) {
            FarmerName = farmerName;
        }

        public String getFarmerContact() {
            return FarmerContact;
        }

        public void setFarmerContact(String farmerContact) {
            FarmerContact = farmerContact;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getDepot() {
            return Depot;
        }

        public void setDepot(String depot) {
            Depot = depot;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public String getTaluka() {
            return Taluka;
        }

        public void setTaluka(String taluka) {
            Taluka = taluka;
        }

        public String getVillage() {
            return Village;
        }

        public void setVillage(String village) {
            Village = village;
        }

        public String getDateOfComplaint() {
            return DateOfComplaint;
        }

        public void setDateOfComplaint(String dateOfComplaint) {
            DateOfComplaint = dateOfComplaint;
        }

        public String getTBMSubmissionDate() {
            return TBMSubmissionDate;
        }

        public void setTBMSubmissionDate(String TBMSubmissionDate) {
            this.TBMSubmissionDate = TBMSubmissionDate;
        }

        public String getSowingDate() {
            return SowingDate;
        }

        public void setSowingDate(String sowingDate) {
            SowingDate = sowingDate;
        }

        public String getCropStage() {
            return CropStage;
        }

        public void setCropStage(String cropStage) {
            CropStage = cropStage;
        }

        public String getOtherfarmersDtl() {
            return OtherfarmersDtl;
        }

        public void setOtherfarmersDtl(String otherfarmersDtl) {
            OtherfarmersDtl = otherfarmersDtl;
        }

        public String getRemarksInput() {
            return RemarksInput;
        }

        public void setRemarksInput(String remarksInput) {
            RemarksInput = remarksInput;
        }

        public String getContactNoTBM() {
            return ContactNoTBM;
        }

        public void setContactNoTBM(String contactNoTBM) {
            ContactNoTBM = contactNoTBM;
        }

        public String getRaisedComplaintTBM() {
            return RaisedComplaintTBM;
        }

        public void setRaisedComplaintTBM(String raisedComplaintTBM) {
            RaisedComplaintTBM = raisedComplaintTBM;
        }

        public String getRaisedComplaintTBM_RBM() {
            return RaisedComplaintTBM_RBM;
        }

        public void setRaisedComplaintTBM_RBM(String raisedComplaintTBM_RBM) {
            RaisedComplaintTBM_RBM = raisedComplaintTBM_RBM;
        }

        public String getComplaintStatus() {
            return ComplaintStatus;
        }

        public void setComplaintStatus(String complaintStatus) {
            ComplaintStatus = complaintStatus;
        }

        public String getGp_AdmixtureType() {
            return Gp_AdmixtureType;
        }

        public void setGp_AdmixtureType(String gp_AdmixtureType) {
            Gp_AdmixtureType = gp_AdmixtureType;
        }

        public double getGp_AdmixturePercent() {
            return Gp_AdmixturePercent;
        }

        public void setGp_AdmixturePercent(double gp_AdmixturePercent) {
            Gp_AdmixturePercent = gp_AdmixturePercent;
        }

        public String getGp_OffType() {
            return Gp_OffType;
        }

        public void setGp_OffType(String gp_OffType) {
            Gp_OffType = gp_OffType;
        }

        public double getGp_OffTypePercent() {
            return Gp_OffTypePercent;
        }

        public void setGp_OffTypePercent(double gp_OffTypePercent) {
            Gp_OffTypePercent = gp_OffTypePercent;
        }

        public String getGrmBelowStd() {
            return GrmBelowStd;
        }

        public void setGrmBelowStd(String grmBelowStd) {
            GrmBelowStd = grmBelowStd;
        }

        public double getGrmPercent() {
            return GrmPercent;
        }

        public void setGrmPercent(double grmPercent) {
            GrmPercent = grmPercent;
        }

        public String getLateGrm() {
            return LateGrm;
        }

        public void setLateGrm(String lateGrm) {
            LateGrm = lateGrm;
        }

        public int getRain_NoPktsOrBagDamaged() {
            return Rain_NoPktsOrBagDamaged;
        }

        public void setRain_NoPktsOrBagDamaged(int rain_NoPktsOrBagDamaged) {
            Rain_NoPktsOrBagDamaged = rain_NoPktsOrBagDamaged;
        }

        public String getPhotoUploadedBy() {
            return PhotoUploadedBy;
        }

        public void setPhotoUploadedBy(String photoUploadedBy) {
            PhotoUploadedBy = photoUploadedBy;
        }

        public String getPhoto1UploadedPath() {
            return Photo1UploadedPath;
        }

        public void setPhoto1UploadedPath(String photo1UploadedPath) {
            Photo1UploadedPath = photo1UploadedPath;
        }

        public String getPhoto2UploadedPath() {
            return Photo2UploadedPath;
        }

        public void setPhoto2UploadedPath(String photo2UploadedPath) {
            Photo2UploadedPath = photo2UploadedPath;
        }

        public String getPhoto3UploadedPath() {
            return Photo3UploadedPath;
        }

        public void setPhoto3UploadedPath(String photo3UploadedPath) {
            Photo3UploadedPath = photo3UploadedPath;
        }

        public String getPhoto4UploadedPath() {
            return Photo4UploadedPath;
        }

        public void setPhoto4UploadedPath(String photo4UploadedPath) {
            Photo4UploadedPath = photo4UploadedPath;
        }

        public int getNo_PKtsBagDamagedOperationalComplaint() {
            return No_PKtsBagDamagedOperationalComplaint;
        }

        public void setNo_PKtsBagDamagedOperationalComplaint(int no_PKtsBagDamagedOperationalComplaint) {
            No_PKtsBagDamagedOperationalComplaint = no_PKtsBagDamagedOperationalComplaint;
        }

        public String getDiseaseInfestationComment() {
            return DiseaseInfestationComment;
        }

        public void setDiseaseInfestationComment(String diseaseInfestationComment) {
            DiseaseInfestationComment = diseaseInfestationComment;
        }

        public int getDaysAfterSowing() {
            return DaysAfterSowing;
        }

        public void setDaysAfterSowing(int daysAfterSowing) {
            DaysAfterSowing = daysAfterSowing;
        }

        public String getPestAttackComment() {
            return PestAttackComment;
        }

        public void setPestAttackComment(String pestAttackComment) {
            PestAttackComment = pestAttackComment;
        }

        public String getOthersInput() {
            return OthersInput;
        }

        public void setOthersInput(String othersInput) {
            OthersInput = othersInput;
        }

        public String getOtherManualTypingBox() {
            return OtherManualTypingBox;
        }

        public void setOtherManualTypingBox(String otherManualTypingBox) {
            OtherManualTypingBox = otherManualTypingBox;
        }
    }
}
