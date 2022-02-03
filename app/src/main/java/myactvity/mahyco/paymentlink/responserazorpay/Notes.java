package myactvity.mahyco.paymentlink.responserazorpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {
    @SerializedName("policy_name")
    @Expose
    private String policyName;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "policyName='" + policyName + '\'' +
                '}';
    }
}
