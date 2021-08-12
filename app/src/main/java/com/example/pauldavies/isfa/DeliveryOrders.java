package com.example.pauldavies.isfa;

public class DeliveryOrders
{
    public String OrderId;
    public String OrderDate;
    public String amount;

    public DeliveryOrders(String orderId,String orderDate,String amount)
    {
        this.OrderId    =   orderId;
        this.OrderDate  =   orderDate;
        this.amount     =   amount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount =   amount;
    }
}
