package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class OrderLines
{
    public String OrderLineId;
    public String ItemName;

    ArrayList<String[]> OrderLineDetails =   new ArrayList<>();

    public OrderLines(String orderLineId,String itemName)
    {
        OrderLineId = orderLineId;
        ItemName=itemName;
    }
}
