package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Customer
{
    public String customerName;
    public String customerContact;
    public String customerCode;

    ArrayList<String[]> customerDetails =   new ArrayList<>();

    public Customer(String customerName, String customerContact, String customerCode)
    {
        this.customerName   =   customerName;
        this.customerContact=   customerContact;
        this.customerCode   =   customerCode;
    }
}
