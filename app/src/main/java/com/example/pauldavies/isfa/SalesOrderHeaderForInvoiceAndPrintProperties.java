package com.example.pauldavies.isfa;

public class SalesOrderHeaderForInvoiceAndPrintProperties
{
    String sales_order_code;
    String sales_order_date;
    String sales_order_amount;

    public SalesOrderHeaderForInvoiceAndPrintProperties(String sales_order_code, String sales_order_date, String sales_order_amount)
    {
        this.sales_order_code   =   sales_order_code;
        this.sales_order_date   =   sales_order_date;
        this.sales_order_amount =   sales_order_amount;
    }

    public void setSalesOrderCode(String sales_order_code)
    {
        this.sales_order_code   =   sales_order_code;
    }

    public String getSales_order_code()
    {
        return sales_order_code;
    }

    public void setSalesOrderDate(String sales_order_date)
    {
        this.sales_order_date   =   sales_order_date;
    }

    public String getSales_order_date()
    {
        return sales_order_date;
    }

    public void setSalesOrderAmount(String sales_order_amount)
    {
        this.sales_order_amount =   sales_order_amount;
    }

    public String getSales_order_amount()
    {
        return sales_order_amount;
    }
}
