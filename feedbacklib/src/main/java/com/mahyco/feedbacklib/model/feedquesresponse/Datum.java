package com.mahyco.feedbacklib.model.feedquesresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("QuestionId")
    @Expose
    private Integer questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("AppNameType")
    @Expose
    private Object appNameType;
    @SerializedName("OrderBy")
    @Expose
    private Object orderBy;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;
    @SerializedName("StarType")
    @Expose
    private Object starType;
    @SerializedName("TrDate")
    @Expose
    private Object trDate;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Object getAppNameType() {
        return appNameType;
    }

    public void setAppNameType(Object appNameType) {
        this.appNameType = appNameType;
    }

    public Object getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Object orderBy) {
        this.orderBy = orderBy;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Object getStarType() {
        return starType;
    }

    public void setStarType(Object starType) {
        this.starType = starType;
    }

    public Object getTrDate() {
        return trDate;
    }

    public void setTrDate(Object trDate) {
        this.trDate = trDate;
    }

}

