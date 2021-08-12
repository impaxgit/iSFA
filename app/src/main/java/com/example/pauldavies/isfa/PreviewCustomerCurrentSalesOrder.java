package com.example.pauldavies.isfa;

public class PreviewCustomerCurrentSalesOrder
{
    String product_code;
    String product_name;
    String qty;
    String line_amount;

    public PreviewCustomerCurrentSalesOrder(String product_code, String product_name, String qty, String line_amount)
    {
        this.product_code   =   product_code;
        this.product_name   =   product_name;
        this.qty            =   qty;
        this.line_amount    =   line_amount;
    }

    public String getProduct_code()
    {
        return product_code;
    }
    public String getProduct_name()
    {
        return product_name;
    }
    public String getQty()
    {
        return qty;
    }
    public String getLine_amount()
    {
        return line_amount;
    }
}
