package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrdersHeaderAdapter extends BaseExpandableListAdapter
{
    Context context;
   private ArrayList<Orders> orders;
   SharedPrefs sharedPrefs;
   DB db;

    public OrdersHeaderAdapter(Context context, ArrayList<Orders> orders)
    {
        this.context = context;
        this.orders = orders;
        sharedPrefs =   new SharedPrefs(context);
    }


    @Override
    public int getGroupCount()
    {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return orders.get(groupPosition).orderItems.size();
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
    public long getChildId(int groupPosition, int childPosition) {
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
            view = layoutInflater.inflate(R.layout.order_headers, null, false);
            viewGroupHolder =   new ViewGroupHolder(view);
            view.setTag(viewGroupHolder);
        }


        else
        {
            viewGroupHolder =   (ViewGroupHolder) view.getTag();
        }

       viewGroupHolder.orderId.setText(orders.get(groupPosition).OrderId);
        viewGroupHolder.date.setText(orders.get(groupPosition).OrderDate);

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent)
    {
        View view   =   convertView;
        ViewChildHolder viewChildHolder;
        if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.order_details, null, false);
            viewChildHolder =   new ViewChildHolder(view);
            view.setTag(viewChildHolder);
        }
        else
        {
            viewChildHolder =   (ViewChildHolder)view.getTag();
        }
        viewChildHolder.amount.setText(orders.get(groupPosition).orderItems.get(childPosition).getTotalAmount());


        Button lines=(Button)view.findViewById(R.id.generate_btnLines);
        lines.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPrefs.putItem("OrderId", orders.get(groupPosition).OrderId);
                Intent intent = new Intent(context, SalesOrder_Lines.class);
                context.startActivity(intent);
                }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    class ViewChildHolder
    {

        TextView amount;

        public ViewChildHolder(View view)
        {
            amount          =    view.findViewById(R.id.orderDetails_txtAmount);

        }
    }

    class ViewGroupHolder
    {
        TextView orderId;
        TextView date;


        public ViewGroupHolder(View view)
        {
            orderId    =   view.findViewById(R.id.OrderHeaders_txtOrderNo);
            date       =   view.findViewById(R.id.OrderHeaders_txtOrderDate);

        }
    }
}
