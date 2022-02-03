package myactvity.mahyco.helper;

public class PurchaseDetailModel {

    private String uId;
    private String mmkID;
    private String userCode;
    private String crop;
    private String productPurchase;
    private String qty;
    private String entryDt;


    public String getEntryDt() {
        return entryDt;
    }

    public void setEntryDt(String entryDt) {
        this.entryDt = entryDt;
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

 public String getMmkID() {
        return mmkID;
    }

    public void setMmkID(String mmkID) {
        this.mmkID = mmkID;
    }

    public String getCropType() {
        return crop;
    }

    public void setCropType(String cropType) {
        this.crop = cropType;
    }

    public String getProductPurchase() {
        return productPurchase;
    }

    public void setProductPurchase(String productPurchase) {
        this.productPurchase = productPurchase;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }



}
