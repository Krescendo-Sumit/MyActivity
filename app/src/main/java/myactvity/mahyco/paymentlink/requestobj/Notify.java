package myactvity.mahyco.paymentlink.requestobj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notify {
    @SerializedName("sms")
    @Expose
    private Boolean sms;
    @SerializedName("email")
    @Expose
    private Boolean email;

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

}
