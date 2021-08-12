package com.example.pauldavies.isfa;

public class OrderPayment
{
    public String productCode;
    public String productName;
    public String Qty;
    public String totalAmount;

    public OrderPayment(String productCode, String productName, String qty, String totalAmount)
    {
        this.productCode = productCode;
        this.productName = productName;
        Qty = qty;
        this.totalAmount = totalAmount;
    }

  public  String getProductCode()
  {
      return  productCode;
  }

    public String getProductName()
    {
        return productName;
    }

    public String getQty()
    {
        return Qty;
    }

    public String getTotalAmount()
    {
        return totalAmount;
    }
}
