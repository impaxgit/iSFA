package com.example.pauldavies.isfa;

public class ClientSalesOrder
{
    String customer_code;
    String sku_code;
    String sku_name;
    String unit_price;
    String line_amount;
    String qty;

    public ClientSalesOrder(String customer_code, String sku_code, String sku_name, String unit_price, String line_amount, String qty)
    {
        this.customer_code  =   customer_code;
        this.sku_code       =   sku_code;
        this.sku_name       =   sku_name;
        this.unit_price     =   unit_price;
        this.qty            =   qty;
        this.line_amount    =   line_amount;
    }

    public void setQty(String qty)
    {
        this.qty    =   qty;
    }

    public String getQty()
    {
        return qty;
    }

    public void setLineAmount(String line_amount)
    {
        this.line_amount    =   line_amount;
    }

    public String getLineAmount()
    {
        return line_amount;
    }

    public String getSkuCode()
    {
        return sku_code;
    }

    public String getSkuName()
    {
        return sku_name;
    }

    public String getCustomerCode()
    {
        return customer_code;
    }

    public String getUnitPrice()
    {
        return unit_price;
    }

}
