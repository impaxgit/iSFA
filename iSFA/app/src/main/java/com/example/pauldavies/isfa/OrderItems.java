package com.example.pauldavies.isfa;



public class OrderItems
{
    public String productName;
    public String Qty;
    public String unitPrice;
    public String Count;
    public String totalAmount;
    public boolean isDelivered;
    public int totals;

    public OrderItems(String productName, String qty, String unitPrice, String count, String totalAmount, boolean isDelivered)
    {
        this.productName = productName;
        this.Qty = qty;
        this.unitPrice = unitPrice;
        this.Count = count;
        this.totalAmount = totalAmount;
        this.isDelivered = isDelivered;
    }

    public boolean getDelivered() {
        return isDelivered;
    }

    public String getCount() {
        return Count;
    }

    public String getProductName()
    {
        return productName;
    }

    public String getQty() {
        return Qty;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setDelivered(boolean delivered)
    {
        isDelivered = delivered;
    }

    public int getTotals()
    {
        return totals;
    }

    public void setTotals(int totals)
    {
        this.totals = totals;
    }
}
