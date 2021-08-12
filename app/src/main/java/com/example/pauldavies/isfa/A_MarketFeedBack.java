package com.example.pauldavies.isfa;

public class A_MarketFeedBack
{
    String type;
    String customer;
    String feedback;

    public A_MarketFeedBack()
    {

    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
