package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Orders
{
    public String OrderId;
    public String OrderDate;
    public String OutletName;

    ArrayList<String[]> OrderDetails =   new ArrayList<>();

    public Orders(String orderId,String orderDate,String outletName)
    {
        OrderId = orderId;
        OrderDate=orderDate;
        OutletName=outletName;
    }
}
