package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class OrderLines
{
    public String OrderLineId;
    public String Description;

    ArrayList<String[]> OrderLineDetails =   new ArrayList<>();

    public OrderLines(String orderLineId, String description)
    {
        OrderLineId = orderLineId;
        Description = description;

    }
}
