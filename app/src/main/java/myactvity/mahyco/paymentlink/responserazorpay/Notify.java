package myactvity.mahyco.paymentlink.responserazorpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notify {

    @SerializedName("email")
    @Expose
    private Boolean email;
    @SerializedName("sms")
    @Expose
    private Boolean sms;

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    @Override
    public String toString() {
        return "Notify{" +
                "email=" + email +
                ", sms=" + sms +
                '}';
    }
}
