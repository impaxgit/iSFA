package com.example.pauldavies.isfa;

public class DeliveryOrderItems
{
    public String productName;
    public String Qty;
    public String unitPrice;
    public String Count;
    public String totalAmount;

    public DeliveryOrderItems(String productName, String qty, String unitPrice, String count, String totalAmount)
    {
        this.productName = productName;
        this.Qty = qty;
        this.unitPrice = unitPrice;
        this.Count = count;
        this.totalAmount = totalAmount;
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
}
