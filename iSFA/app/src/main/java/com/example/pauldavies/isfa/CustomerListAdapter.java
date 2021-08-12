package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerListAdapter extends BaseExpandableListAdapter
{
    Context context;
    ArrayList<Customer> customer;

    public CustomerListAdapter(Context context, ArrayList<Customer> customer)
    {
        super();

        this.context    =   context;
        this.customer   =   customer;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return customer.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return customer.get(groupPosition).customerDetails.size();
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
        HeaderView  headerView;
        if(view ==  null)
        {
            LayoutInflater  layoutInflater  =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.customer_header, null, false);
            headerView  =   new HeaderView(view);
            view.setTag(headerView);
        }
        else
        {
            headerView  =   (HeaderView)view.getTag();
        }

        headerView.name.setText(customer.get(groupPosition).customerName);
        headerView.contact.setText(customer.get(groupPosition).customerContact);

        if(isExpanded)
        {
            headerView.indicator.setImageResource(R.drawable.less);
        }
        else
        {
            headerView.indicator.setImageResource(R.drawable.expand);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        Details details;

        if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.customer_details, null, false);
            details =   new Details(view);
            view.setTag(details);
        }
        else
        {
            details =   (Details) view.getTag();
        }

       details.outlet_type.setText(customer.get(groupPosition).customerDetails.get(childPosition)[0]);
       details.owner_name.setText(customer.get(groupPosition).customerDetails.get(childPosition)[1]);
       details.contact_number.setText(customer.get(groupPosition).customerDetails.get(childPosition)[2]);
       details.contact_name.setText(customer.get(groupPosition).customerDetails.get(childPosition)[3]);
       details.location.setText(customer.get(groupPosition).customerDetails.get(childPosition)[4]);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    class HeaderView
    {
        TextView name;
        TextView contact;
        ImageView indicator;

        public HeaderView(View view)
        {
            name    =   view.findViewById(R.id.customer_name_txt);
            contact =   view.findViewById(R.id.customer_contact_txt);
            indicator   =   view.findViewById(R.id.customer_listing_img);
        }
    }

    class Details
    {
        TextView outlet_type;
        TextView contact_name;
        TextView contact_number;
        TextView location;
        TextView owner_name;

        public Details(View view)
        {
            outlet_type =   view.findViewById(R.id.customer_details_outlettype);
            contact_name=   view.findViewById(R.id.deliverOrderLines_txtOrderDate);
            contact_number= view.findViewById(R.id.deliverOrderLines_txtAmount);
            location    =   view.findViewById(R.id.customer_details_location);
            owner_name  =   view.findViewById(R.id.customer_details_owrner_txt);
        }
    }
}
