package myactvity.mahyco.helper;

public class SkFarmerCountModel {

    int pending;
    int approve;
    int reject;

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getApprove() {
        return approve;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public int getReject() {
        return reject;
    }

    public void setReject(int reject) {
        this.reject = reject;
    }

    public int getNotvalidate() {
        return notvalidate;
    }

    public void setNotvalidate(int notvalidate) {
        this.notvalidate = notvalidate;
    }

    public int getValidate() {
        return validate;
    }

    public void setValidate(int validate) {
        this.validate = validate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    int notvalidate;
    int validate;
    int total;
}
