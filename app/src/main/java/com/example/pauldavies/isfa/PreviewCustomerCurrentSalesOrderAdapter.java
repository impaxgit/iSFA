package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PreviewCustomerCurrentSalesOrderAdapter extends RecyclerView.Adapter<PreviewCustomerCurrentSalesOrderAdapter.ViewHolder>
{

    Context context;
    List<PreviewCustomerCurrentSalesOrder> previewCustomerCurrentSalesOrders;
    CommonClass commonClass;

    public PreviewCustomerCurrentSalesOrderAdapter(Context context, List<PreviewCustomerCurrentSalesOrder> previewCustomerCurrentSalesOrders)
    {
        this.context    =   context;
        this.previewCustomerCurrentSalesOrders  =   previewCustomerCurrentSalesOrders;
        commonClass =   new CommonClass();
    }

    @Override
    public PreviewCustomerCurrentSalesOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.preview_sales_invoice_custom, null, false));
    }

    @Override
    public void onBindViewHolder(PreviewCustomerCurrentSalesOrderAdapter.ViewHolder viewHolder, int position)
    {
        final PreviewCustomerCurrentSalesOrder previewCustomerCurrentSalesOrder =   previewCustomerCurrentSalesOrders.get(position);
        viewHolder.setIsRecyclable(false);

        viewHolder.product_code.setText(previewCustomerCurrentSalesOrder.getProduct_code());
        viewHolder.product_name.setText(previewCustomerCurrentSalesOrder.getProduct_name());
        viewHolder.qty.setText(previewCustomerCurrentSalesOrder.getQty());
        viewHolder.line_amount.setText(commonClass.getCurrencyFormat(Float.valueOf(previewCustomerCurrentSalesOrder.getLine_amount())));
    }

    @Override
    public int getItemCount()
    {
        return previewCustomerCurrentSalesOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_code, product_name, qty, line_amount;

        public ViewHolder(View view)
        {
            super(view);

            product_code    =   view.findViewById(R.id.preview_item_code);
            product_name    =   view.findViewById(R.id.preview_item_name);
            qty             =   view.findViewById(R.id.preview_line_qty);
            line_amount     =   view.findViewById(R.id.preview_line_amount);
        }
    }
}
