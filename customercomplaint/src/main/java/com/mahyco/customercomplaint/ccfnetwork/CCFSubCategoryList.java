package com.mahyco.customercomplaint.ccfnetwork;

public class CCFSubCategoryList {

    public CCFSubCategoryList(int CTID, int SCTID, String SCTDesc) {
        this.CTID = CTID;
        this.SCTID = SCTID;
        this.SCTDesc = SCTDesc;
    }

    private int CTID = 0;
    private int SCTID = 0;
    private String SCTDesc = "";

    public int getCTID() {
        return CTID;
    }

    public void setCTID(int CTID) {
        this.CTID = CTID;
    }

    public int getSCTID() {
        return SCTID;
    }

    public void setSCTID(int SCTID) {
        this.SCTID = SCTID;
    }

    public String getSCTDesc() {
        return SCTDesc;
    }

    public void setSCTDesc(String SCTDesc) {
        this.SCTDesc = SCTDesc;
    }

    public String toString() {
        return SCTDesc;
    }

}
