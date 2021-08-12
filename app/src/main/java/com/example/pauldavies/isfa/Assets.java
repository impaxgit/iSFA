package com.example.pauldavies.isfa;

public class Assets
{
    String id;
    String assetName;
    String onSite;
    String condition;
    String reasons;
    String lastServiceDate;
    String date;
    String nextServiceDate;
    String comments;

    public Assets(String id, String assetName, String onSite, String condition, String reasons, String lastServiceDate,String date, String nextServiceDate, String comments) {
        this.id = id;
        this.assetName = assetName;
        this.onSite = onSite;
        this.condition = condition;
        this.reasons = reasons;
        this.lastServiceDate = lastServiceDate;
        this.date =date;
        this.nextServiceDate = nextServiceDate;
        this.comments = comments;
    }

    public String getId()
    {
        return id;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public String isOnSite()
    {
        return onSite;
    }

    public String isCondition()
    {
        return condition;
    }

    public String  getReasons() {
        return reasons;
    }

    public String getLastServiceDate() {
        return lastServiceDate;
    }

    public String getNextServiceDate() {
        return nextServiceDate;
    }

    public String getComments() {
        return comments;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOnSite(String onSite) {
        this.onSite = onSite;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public void setLastServiceDate(String lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public void setNextServiceDate(String nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
