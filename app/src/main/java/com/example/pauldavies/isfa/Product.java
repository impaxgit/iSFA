package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Product
{
    String productcode;
    String productnme;

    ArrayList<String[]> product_details =   new ArrayList<>();

    public Product(String productcode, String productnme)
    {
        this.productcode    =   productcode;
        this.productnme     =   productnme;
    }
}
