package com.example.pauldavies.isfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class OrderDeliveryAdapter extends BaseAdapter
{
    Context context;
    private ArrayList<OrderItems> orderItems;


    public OrderDeliveryAdapter(Context context, ArrayList<OrderItems> orderItems)
    {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public int getCount()
    {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewDetailsHolder viewDetailsHolder;
        if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.orderdelivery_items, null, false);
            viewDetailsHolder =   new ViewDetailsHolder(view);
            view.setTag(viewDetailsHolder);
        }
        else
        {
            viewDetailsHolder =   (ViewDetailsHolder) view.getTag();
        }

        OrderItems orders=orderItems.get(position);

        viewDetailsHolder.productName.setText(orders.productName);
        viewDetailsHolder.qty.setText(orders.Qty);
        viewDetailsHolder.unitPrice.setText(orders.unitPrice);
        viewDetailsHolder.count.setText(orders.Count);
        viewDetailsHolder.lineTotal.setText(orders.totalAmount);

        return view;
    }

    class ViewDetailsHolder
    {
        TextView productName;
        TextView qty;
        TextView unitPrice;
        TextView count;
        TextView lineTotal;

        public ViewDetailsHolder(View view)
        {
            productName    =   view.findViewById(R.id.deliverOrders_txtProductName);
            qty            =   view.findViewById(R.id.deliverOrders_txtQty);
            unitPrice      =   view.findViewById(R.id.deliverOrders_txtUnitPrice);
            count          =   view.findViewById(R.id.deliverOrders_txtCount);
            lineTotal      =   view.findViewById(R.id.deliverOrders_txtAmount);

        }
    }
}
