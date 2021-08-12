package com.example.pauldavies.isfa;

public class A_Stock
{
    String product_name;
    String product_code;
    String expired;
    String damaged;
    String totals;
    String comments;


    public A_Stock()
    {

    }

    public String getProduct_name()
    {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code()
    {
        return product_code;
    }

    public String getTotals()
    {
        return totals;
    }

    public void setTotals(String totals) {
        this.totals = totals;
    }

    public String getExpired()
    {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getDamaged()
    {
        return damaged;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments()
    {
        return comments;
    }

    public void setDamaged(String damaged) {
        this.damaged = damaged;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
