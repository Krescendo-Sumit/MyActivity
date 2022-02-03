package myactvity.mahyco.paymentlink.responserazorpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RazorPayResponse {
    @SerializedName("accept_partial")
    @Expose
    private Boolean acceptPartial;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("amount_paid")
    @Expose
    private Integer amountPaid;
    @SerializedName("callback_method")
    @Expose
    private String callbackMethod;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;
    @SerializedName("cancelled_at")
    @Expose
    private Integer cancelledAt;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("expire_by")
    @Expose
    private Integer expireBy;
    @SerializedName("expired_at")
    @Expose
    private Integer expiredAt;
    @SerializedName("first_min_partial_amount")
    @Expose
    private Integer firstMinPartialAmount;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("notes")
    @Expose
    private Notes notes;
    @SerializedName("notify")
    @Expose
    private Notify notify;
    @SerializedName("payments")
    @Expose
    private Object payments;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("reminder_enable")
    @Expose
    private Boolean reminderEnable;
    @SerializedName("reminders")
    @Expose
    private List<Object> reminders = null;
    @SerializedName("short_url")
    @Expose
    private String shortUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private Integer updatedAt;
    @SerializedName("upi_link")
    @Expose
    private Boolean upiLink;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public Boolean getAcceptPartial() {
        return acceptPartial;
    }

    public void setAcceptPartial(Boolean acceptPartial) {
        this.acceptPartial = acceptPartial;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Integer cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExpireBy() {
        return expireBy;
    }

    public void setExpireBy(Integer expireBy) {
        this.expireBy = expireBy;
    }

    public Integer getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Integer expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Integer getFirstMinPartialAmount() {
        return firstMinPartialAmount;
    }

    public void setFirstMinPartialAmount(Integer firstMinPartialAmount) {
        this.firstMinPartialAmount = firstMinPartialAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Notify getNotify() {
        return notify;
    }

    public void setNotify(Notify notify) {
        this.notify = notify;
    }

    public Object getPayments() {
        return payments;
    }

    public void setPayments(Object payments) {
        this.payments = payments;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getReminderEnable() {
        return reminderEnable;
    }

    public void setReminderEnable(Boolean reminderEnable) {
        this.reminderEnable = reminderEnable;
    }

    public List<Object> getReminders() {
        return reminders;
    }

    public void setReminders(List<Object> reminders) {
        this.reminders = reminders;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getUpiLink() {
        return upiLink;
    }

    public void setUpiLink(Boolean upiLink) {
        this.upiLink = upiLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RazorPayResponse{" +
                "acceptPartial=" + acceptPartial +
                ", amount=" + amount +
                ", amountPaid=" + amountPaid +
                ", callbackMethod='" + callbackMethod + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", cancelledAt=" + cancelledAt +
                ", createdAt=" + createdAt +
                ", currency='" + currency + '\'' +
                ", customer=" + customer +
                ", description='" + description + '\'' +
                ", expireBy=" + expireBy +
                ", expiredAt=" + expiredAt +
                ", firstMinPartialAmount=" + firstMinPartialAmount +
                ", id='" + id + '\'' +
                ", notes=" + notes +
                ", notify=" + notify +
                ", payments=" + payments +
                ", referenceId='" + referenceId + '\'' +
                ", reminderEnable=" + reminderEnable +
                ", reminders=" + reminders +
                ", shortUrl='" + shortUrl + '\'' +
                ", status='" + status + '\'' +
                ", updatedAt=" + updatedAt +
                ", upiLink=" + upiLink +
                ", userId='" + userId + '\'' +
                '}';
    }
}
