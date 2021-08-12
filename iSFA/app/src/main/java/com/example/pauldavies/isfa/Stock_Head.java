package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Stock_Head
{
    public Stock_Head(String product_name, ArrayList<Stock> stockArrayList)
    {
        this.product_name = product_name;
        this.stockArrayList = stockArrayList;
    }

    String product_name;
    ArrayList<Stock> stockArrayList=new ArrayList<>();

    public String getProduct_name()
    {
        return product_name;
    }

    public ArrayList<Stock> getStockArrayList()
    {
        return stockArrayList;
    }
}
