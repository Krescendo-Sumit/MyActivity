package myactvity.mahyco.paymentlink.requestobj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HDPSRequest {
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("accept_partial")
    @Expose
    private Boolean acceptPartial;
    @SerializedName("first_min_partial_amount")
    @Expose
    private Integer firstMinPartialAmount;
    @SerializedName("expire_by")
    @Expose
    private Integer expireBy;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("notify")
    @Expose
    private Notify notify;
    @SerializedName("reminder_enable")
    @Expose
    private Boolean reminderEnable;
    @SerializedName("notes")
    @Expose
    private Notes notes;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;
    @SerializedName("callback_method")
    @Expose
    private String callbackMethod;
    @SerializedName("options")
    @Expose
    private Options options;

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getAcceptPartial() {
        return acceptPartial;
    }

    public void setAcceptPartial(Boolean acceptPartial) {
        this.acceptPartial = acceptPartial;
    }

    public Integer getFirstMinPartialAmount() {
        return firstMinPartialAmount;
    }

    public void setFirstMinPartialAmount(Integer firstMinPartialAmount) {
        this.firstMinPartialAmount = firstMinPartialAmount;
    }

    public Integer getExpireBy() {
        return expireBy;
    }

    public void setExpireBy(Integer expireBy) {
        this.expireBy = expireBy;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Notify getNotify() {
        return notify;
    }

    public void setNotify(Notify notify) {
        this.notify = notify;
    }

    public Boolean getReminderEnable() {
        return reminderEnable;
    }

    public void setReminderEnable(Boolean reminderEnable) {
        this.reminderEnable = reminderEnable;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

}
