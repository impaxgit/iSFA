package com.example.pauldavies.isfa;

public class Stock
{
    String product_name;
    String product_code;
    String expired;
    String damaged;
    String totals;
    String comments;

    public Stock(String product_name, String product_code, String expired, String damaged, String totals, String comments) {
        this.product_name = product_name;
        this.product_code = product_code;
        this.expired = expired;
        this.damaged = damaged;
        this.totals = totals;
        this.comments = comments;
    }

    public String getProduct_name()
    {
        return product_name;
    }

    public String getTotals()
    {
        return totals;
    }

    public String getExpired()
    {
        return expired;
    }

    public String getDamaged()
    {
        return damaged;
    }

    public String getComments()
    {
        return comments;
    }
}
