package com.mahyco.customercomplaint.ccfnetwork;

import android.util.Log;

public class CCFLotData {

    private String Hybrid = "";
    private double PackingSize = 0;
    private String Batch_LotNumber = "";
    private String Crop = "";
    private String BusinessUnitCode = "";
    private double LotQty = 0;
    private int NoOfPkts = 0;
    private double TotalLotQty;
    private int TotalNoOfPkts;

    public CCFLotData(String hybrid, double packingSize, String batch_LotNumber, String crop, String businessUnitCode, double lotQty, int noOfPkts, double totalLotQty, int totalNoOfPkts) {
        Hybrid = hybrid;
        PackingSize = packingSize;
        Batch_LotNumber = batch_LotNumber;
        Crop = crop;
        BusinessUnitCode = businessUnitCode;
        LotQty = lotQty;
        NoOfPkts = noOfPkts;
        TotalLotQty = totalLotQty;
        TotalNoOfPkts = totalNoOfPkts;
    }

    public String getHybrid() {
        return Hybrid;
    }

    public void setHybrid(String hybrid) {
        Hybrid = hybrid;
    }

    public double getPackingSize() {
        return PackingSize;
    }

    public void setPackingSize(double packingSize) {
        PackingSize = packingSize;
    }

    public String getBatch_LotNumber() {
        return Batch_LotNumber;
    }

    public void setBatch_LotNumber(String batch_LotNumber) {
        Batch_LotNumber = batch_LotNumber;
    }

    public String getCrop() {
        return Crop;
    }

    public void setCrop(String crop) {
        Crop = crop;
    }

    public String getBusinessUnitCode() {
        return BusinessUnitCode;
    }

    public void setBusinessUnitCode(String businessUnitCode) {
        BusinessUnitCode = businessUnitCode;
    }

    public double getLotQty() {
        return LotQty;
    }

    public void setLotQty(double lotQty) {
        LotQty = lotQty;
    }

    public int getNoOfPkts() {
        return NoOfPkts;
    }

    public void setNoOfPkts(int noOfPkts) {
        NoOfPkts = noOfPkts;
    }

    public double getTotalLotQty() {
        return TotalLotQty;
    }

    public void setTotalLotQty(double totalLotQty) {
        TotalLotQty = totalLotQty;
    }

    public int getTotalNoOfPkts() {
        return TotalNoOfPkts;
    }

    public void setTotalNoOfPkts(int totalNoOfPkts) {
        TotalNoOfPkts = totalNoOfPkts;
    }
}
