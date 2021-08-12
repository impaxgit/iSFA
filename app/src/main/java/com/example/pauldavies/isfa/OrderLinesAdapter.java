package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class OrderLinesAdapter extends BaseExpandableListAdapter
{
    Context context;
    private ArrayList<OrderLines> orderlines;

    public OrderLinesAdapter(Context context, ArrayList<OrderLines> orderlines)
    {
        this.context = context;
        this.orderlines = orderlines;
    }

    @Override
    public int getGroupCount()
    {
        return orderlines.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return orderlines.get(groupPosition).OrderLineDetails.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewGroupHolder viewGroupHolder;

        if (view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.saleslines_header, null, false);
            viewGroupHolder =   new ViewGroupHolder(view);
            view.setTag(viewGroupHolder);
        }


        else
        {
            viewGroupHolder =   (ViewGroupHolder) view.getTag();
        }

        viewGroupHolder.orderLineId.setText(orderlines.get(groupPosition).OrderLineId);
        viewGroupHolder.description.setText(orderlines.get(groupPosition).Description);


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewChildHolder viewChildHolder;
        if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.saleslines_details, null, false);
            viewChildHolder =   new ViewChildHolder(view);
            view.setTag(viewChildHolder);
        }
        else
        {
            viewChildHolder =   (ViewChildHolder)view.getTag();
        }


        viewChildHolder.itemCode.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[0]);
        viewChildHolder.itemName.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[1]);
        viewChildHolder.description.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[2]);
        viewChildHolder.qty.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[3]);
        viewChildHolder.unitPrice.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[4]+"/=");
        viewChildHolder.total_amount.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[5]+"/=");
        viewChildHolder.date.setText(orderlines.get(groupPosition).OrderLineDetails.get(childPosition)[6]);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    class ViewChildHolder
    {
        TextView itemCode;
        TextView itemName;
        TextView description;
        TextView qty;
        TextView unitPrice;
        TextView total_amount;
        TextView date;

        public ViewChildHolder(View view)
        {
            itemCode           =    view.findViewById(R.id.orderLineDetails_txtProductCode);
            itemName           =    view.findViewById(R.id.orderLinesDetails_txtProductName);
            description        =    view.findViewById(R.id.orderLinesDetails_txtDescription);
            qty                =    view.findViewById(R.id.orderLinesDetails_txtQty);
            unitPrice          =    view.findViewById(R.id.orderLinesDetails_txtUnitPrice);
            total_amount       =    view.findViewById(R.id.orderLinesDetails_txtAmount);
            date               =    view.findViewById(R.id.orderLinesDetails_txtOrderDate);

        }
    }

    class ViewGroupHolder
    {
        TextView orderLineId;
        TextView description;

        public ViewGroupHolder(View view)
        {
            orderLineId    =   view.findViewById(R.id.OrderLinesHeaders_txtOrderId);
            description    =   view.findViewById(R.id.OrderLinesHeaders_txtDetailsOrderId);

        }
    }
}