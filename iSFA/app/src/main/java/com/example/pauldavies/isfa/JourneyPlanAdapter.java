package com.example.pauldavies.isfa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class JourneyPlanAdapter extends BaseExpandableListAdapter
{
    private ArrayList<Journey> journeys;
    Context context;
    SharedPrefs sharedPrefs;



    public JourneyPlanAdapter(ArrayList<Journey> journeys,Context context)
    {
        this.context=context;
        this.journeys=journeys;
        sharedPrefs =   new SharedPrefs(context);
    }

    @Override
    public int getGroupCount()
    {
        return journeys.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return  journeys.get(groupPosition).journeys.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return journeys.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {

        return  journeys.get(groupPosition).journeys.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup)
    {
        View view   =   convertView;
        ViewGroupHolder viewGroupHolder;

        if (view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.routes_header, null, false);
            viewGroupHolder =   new ViewGroupHolder(view);
            view.setTag(viewGroupHolder);
        }


        else
        {
            viewGroupHolder =   (ViewGroupHolder) view.getTag();
        }

        Journey journey= (Journey) getGroup(groupPosition);

        viewGroupHolder.customerCode.setText(journey.customerCode);
        viewGroupHolder.customerName.setText(journey.customerName);
        if(isExpanded)
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.less);
            Log.d("Testing",viewGroupHolder.customerCode.getText().toString());
            sharedPrefs.putItem("customer", viewGroupHolder.customerCode.getText().toString());

        }
        else
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.expand);
        }


        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup)
    {
        View view   =   convertView;
        ViewChildHolder viewChildHolder;
        if(view ==  null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.routes_details, null, false);
            viewChildHolder =   new ViewChildHolder(view);
            view.setTag(viewChildHolder);
        }
        else
        {
            viewChildHolder =   (ViewChildHolder)view.getTag();
        }

        String[] child1 = (String[]) getChild(groupPosition, childPosition);

        viewChildHolder.location.setText(child1[0]);
        viewChildHolder.contactName.setText(child1[1]);
        viewChildHolder.contactNo.setText(child1[2]);
        viewChildHolder.orders.setText(child1[3]);
        viewChildHolder.outletType.setText(child1[4]);
        viewChildHolder.decisionMakers.setText(child1[5]);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    class ViewChildHolder
    {
        TextView location;
        TextView contactName;
        TextView contactNo;
        TextView orders;
        TextView outletType;
        TextView decisionMakers;

        public ViewChildHolder(View view)
        {
            location    =    view.findViewById(R.id.routes_details_txt_Location);
            contactName =    view.findViewById(R.id.routes_details_txt_ContactPerson);
            contactNo   =    view.findViewById(R.id.routes_details_txt_ContactNo);
            orders      =    view.findViewById(R.id.routes_details_txt_Orders);
            outletType  =    view.findViewById(R.id.routes_details_txt_outletType);
            decisionMakers=  view.findViewById(R.id.routes_details_txt_decisionMaker);
        }
    }

    class ViewGroupHolder
    {
        TextView customerCode;
        TextView customerName;
        ImageView indicator;

        public ViewGroupHolder(View view)
        {
            customerCode    =   view.findViewById(R.id.jpurney_plan_txt_customer_code);
            customerName       =   view.findViewById(R.id.jourey_plan_customer_name);
            indicator   =   view.findViewById(R.id.imageView4);
        }
    }
}
