package myactvity.mahyco.utils;

import java.util.List;

public class LeaveHistoryModel {


    private String InitiationDate;
    private String FromDate;
    private String ToDate;
    private String AbsenceDays;
    private String Status;

    public String getUWLId() {
        return UWLId;
    }

    public void setUWLId(String UWLId) {
        this.UWLId = UWLId;
    }

    private String UWLId;







    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getPeriodFrom() {
        return PeriodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        PeriodFrom = periodFrom;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    private String Reason;
    private String PeriodFrom;
    private String LeaveType;


    private List<LeaveHistoryChildModel> leaveHistoryChildModels = null;

    public String getInitiationDate() {
        return InitiationDate;
    }

    public void setInitiationDate(String initiationDate) {
        InitiationDate = initiationDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getAbsenceDays() {
        return AbsenceDays;
    }

    public void setAbsenceDays(String absenceDays) {
        AbsenceDays = absenceDays;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<LeaveHistoryChildModel> getLeaveHistoryChildModels() {
        return leaveHistoryChildModels;
    }

    public void setLeaveHistoryChildModels(List<LeaveHistoryChildModel> leaveHistoryChildModels) {
        this.leaveHistoryChildModels = leaveHistoryChildModels;
    }
}