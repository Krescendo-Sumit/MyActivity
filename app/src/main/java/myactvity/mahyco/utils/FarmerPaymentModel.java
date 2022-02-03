package myactvity.mahyco.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FarmerPaymentModel implements Comparable<FarmerPaymentModel>,Serializable {
    private String name = "";
    private String StoreImageID;
    private String serialNo;
    private String amount;

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }

    private String amountText;
    private String villagename;
    private String product;
    private String totalCoupon;
    private String mobileNumber;
    private String crop;

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public String getvillagename() {
        return villagename;
    }
    public String getproduct() {
        return product;
    }
    public String gettotalCoupon() {
        return totalCoupon;
    }
    public String getmobileNumber() {
        return mobileNumber;
    }

    public void setvillagename(String villagename) {
        this.villagename = villagename;
    }
    public void setproduct(String product) {
        this.product = product;
    }
    public void settotalCoupon(String totalCoupon) {
        this.totalCoupon = totalCoupon;
    }
    public void setmobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    private String couponcode;

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    private String farmerId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    private String cardId;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getStoreImageID() {
        return StoreImageID;
    }

    public void setStoreImageID(String storeImageID) {
        StoreImageID = storeImageID;
    }

    /*********** Set Methods ******************/
    public void setName(String Caption) {
        this.name = Caption;
    }


    /*********** Get Methods ****************/
    public String getName() {
        return this.name;
    }




    @Override
    public int compareTo(FarmerPaymentModel o) {

        return o.getAmount().compareTo(getAmount());
      //  return getTimeStamp(o.getAmount()).compareTo(getTimeStamp(getAmount()));
    }

    private Timestamp getTimeStamp(String dateAndTime) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = null;
        long time = 0;
        try {
            date = dateFormat.parse(dateAndTime);

            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(time);
    }

    @Override
    public String toString() {
        return "{" +
                "couponcode='" + couponcode + '\'' +
                ", farmerid='" + farmerId + '\'' +
                '}';
    }
}
