package com.example.pauldavies.isfa;

public class DeliveryLine
{
    public String code;
    public String name;
    public String qty_ordered;
    public String total;
    public String qty;
    public String price_unit;

    public DeliveryLine(String code, String name, String qty_ordered, String total, String qty, String price_unit)
    {
        this.code           =   code;
        this.name           =   name;
        this.qty_ordered    =   qty_ordered;
        this.total          =   total;
        this.qty            =   qty;
        this.price_unit     =   price_unit;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getQty_ordered()
    {
        return qty_ordered;
    }

    public String getTotal()
    {
        return total;
    }

    public String getQty()
    {
        return qty;
    }

    public String getPrice_unit()
    {
        return price_unit;
    }
}
