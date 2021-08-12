package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Orders
{
    public String OrderId;
    public String OrderDate;
    public String OutletName;


    ArrayList<OrderItems> orderItems =new ArrayList<>();

    public Orders(String orderId,String orderDate,String outletName)
    {
        OrderId = orderId;
        OrderDate=orderDate;
        OutletName=outletName;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getOutletName() {
        return OutletName;
    }


}
