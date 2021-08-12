package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DeliverOrdersAdapter extends BaseExpandableListAdapter
{
    Activity context;
    private ArrayList<DeliveryOrders> orders;
    SharedPrefs sharedPrefs;
    DB db;
    CommonClass commonClass;


    public DeliverOrdersAdapter(Activity context, ArrayList<DeliveryOrders> orders)
    {
        this.context = context;
        this.orders = orders;
        sharedPrefs=new SharedPrefs(context);


    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orders.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewGroupHolder viewGroupHolder;

        if (view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context .getSystemService(LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.deliverorder_header, null, false);
            viewGroupHolder =   new ViewGroupHolder(view);
            view.setTag(viewGroupHolder);
        }

        else
        {
            viewGroupHolder =   (ViewGroupHolder) view.getTag();
        }

        viewGroupHolder.orderId.setText(orders.get(groupPosition).getOrderId());
        viewGroupHolder.date.setText(orders.get(groupPosition).getOrderDate());
        viewGroupHolder.amount.setText(orders.get(groupPosition).getAmount()+"/=");

        if(isExpanded)
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.less);
        }
        else
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.expand);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, final View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewChildHolder viewChildHolder;
        /*if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.orderdelivery, null, false);
            viewChildHolder =   new ViewChildHolder(view);
            view.setTag(viewChildHolder);
        }
        else
        {
            viewChildHolder =   (ViewChildHolder)view.getTag();
        }

        viewChildHolder.payment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.receipt, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog pay=dialogBuilder.create();
                //on save button click
                dialogView.findViewById(R.id.receipt_btnSave).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(context,"Saving",Toast.LENGTH_LONG).show();

                    }
                });

                //on cancel
                dialogView.findViewById(R.id.receipt_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay.dismiss();
                    }
                });
                pay.show();
            }
        });

        OrdersAdapter ordersAdapter = new OrdersAdapter(context,orders.get(groupPosition).orderItems);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewChildHolder.lines.setLayoutManager(layoutManager);
        viewChildHolder.lines.setAdapter(ordersAdapter);
*/

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    class ViewChildHolder
    {

        RecyclerView lines;
        TextView totals;
        FloatingActionButton payment;


        public ViewChildHolder(View view)
        {
            lines          =    view.findViewById(R.id.rvOrdersDelivered);
            totals         =    view.findViewById(R.id.deliverOrders_txtTotalAmount);
            payment        =    view.findViewById(R.id.deliverOrders_pay);
        }
    }

    class ViewGroupHolder
    {
        TextView orderId;
        TextView date;
        ImageView indicator;
        TextView amount;


        public ViewGroupHolder(View view)
        {
            orderId    =   view.findViewById(R.id.textView40);
            date       =   view.findViewById(R.id.textView41);
            indicator  =   view.findViewById(R.id.orders_listing_img);
            amount     =   view.findViewById(R.id.textView42);
        }
    }
}

