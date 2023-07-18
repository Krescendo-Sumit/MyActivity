package com.mahyco.customercomplaint.ccfnetwork;

public class CCFMainCategoryList {

    public CCFMainCategoryList(int CTID, String CTDesc, int MTID) {
        this.CTID = CTID;
        this.CTDesc = CTDesc;
        this.MTID = MTID;
    }

    private int CTID;
    private String CTDesc;
    private int MTID;

    public int getCTID() {
        return CTID;
    }

    public void setCTID(int CTID) {
        this.CTID = CTID;
    }

    public String getCTDesc() {
        return CTDesc;
    }

    public void setCTDesc(String CTDesc) {
        this.CTDesc = CTDesc;
    }

    public int getMTID() {
        return MTID;
    }

    public void setMTID(int MTID) {
        this.MTID = MTID;
    }

    public String toString() {
        return CTDesc;
    }
}
