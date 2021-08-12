package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class JourneyPlanAdapter extends BaseExpandableListAdapter
{
    private ArrayList<Journey> journeys;
    Context context;
    String customer_code;
    String customer_name;
    DB db;
    CommonClass commonClass;
    SharedPrefs sharedPrefs;



    public JourneyPlanAdapter(ArrayList<Journey> journeys,Context context)
    {
        this.context=context;
        this.journeys=journeys;
        db  =   new DB(context);
        commonClass =   new CommonClass();
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
        customer_code    =   journey.customerCode;
        customer_name    =   journey.customerName;
        if(isExpanded)
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.less);
            sharedPrefs.putItem("customer", journey.customerCode);
        }
        else
        {
            viewGroupHolder.indicator.setImageResource(R.drawable.expand);
        }


        return view;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, final View convertView, ViewGroup viewGroup)
    {
        View view   =   convertView;
        final ViewChildHolder viewChildHolder;
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
        viewChildHolder.outletType.setText(child1[3]);
        viewChildHolder.decisionMakers.setText(child1[4]);


        final Cursor cursor   =   db.getCustomerCheckInOut(journeys.get(groupPosition).customerCode);
        //Activate the necessary button controls
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();

            if(cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_CHECK_OUT_FLAG))==1)
            {
                viewChildHolder.check_out.setEnabled(false);
                viewChildHolder.activities.setEnabled(false);
                viewChildHolder.check_in.setEnabled(false);
            }
            else if(cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_CHECK_OUT_FLAG))==0 && cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_CHECK_IN_FLAG))==1)
            {
               viewChildHolder.check_in.setEnabled(false);
               viewChildHolder.activities.setEnabled(true);
               viewChildHolder.check_out.setEnabled(false);
            }
            else if(cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_CHECK_ACTIVITIES))==2 && cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_CHECK_OUT_FLAG))==0)
            {
               viewChildHolder.activities.setEnabled(false);
               viewChildHolder.check_out.setEnabled(true);
               viewChildHolder.check_in.setEnabled(false);
            }
        }
        else
        {
            viewChildHolder.check_out.setEnabled(false);
            viewChildHolder.activities.setEnabled(false);
            viewChildHolder.check_in.setEnabled(true);
        }

        //Attach listeners to the activity buttons
        viewChildHolder.check_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
                alertDialog.setIcon(R.drawable.mywarning_24dp);
                alertDialog.setTitle("Checking into "+journeys.get(groupPosition).customerCode);
                alertDialog.setMessage("You are about to check into customer:\n"+journeys.get(groupPosition).customerName+".\nPlease confirm this action.");
                Dialog dialog;

                alertDialog.setNegativeButton("No, Not Now", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                alertDialog.setPositiveButton("Yes, Check In", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(db.createCustomerCheckIn(sharedPrefs.getItem("customer"),commonClass.getCurrentDate()+":"+commonClass.getCurrentTime()) != -1)
                        {
                            if(db.createCustomerActivities(sharedPrefs.getItem("customer"), 0, 0, 0, 0, 0, 0, 0, 0, 0, commonClass.getCurrentDate()+":"+commonClass.getCurrentTime(), 0) != -1) //Meaning the activity checklist has been successfully created in the check list table
                            {
                                viewChildHolder.activities.setEnabled(true);
                                viewChildHolder.check_in.setEnabled(false);
                                journeys.get(childPosition).setColour("#888888");
                                viewChildHolder.check_in.setBackgroundColor(Color.parseColor(journeys.get(childPosition).getColour()));
                                Snackbar.make(viewChildHolder.check_in, "Check in successful.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            viewChildHolder.activities.setEnabled(false);
                            viewChildHolder.check_in.setEnabled(true);
                            Snackbar.make(viewChildHolder.check_in, "Unable to check in. Try again!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                dialog=alertDialog.create();

                dialog.show();
            }
        });

        viewChildHolder.activities.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPrefs.putItem("customer", journeys.get(groupPosition).customerCode);
                sharedPrefs.putItem("customer_name", journeys.get(groupPosition).customerName);
                Intent  intent  =   new Intent(context, CustomerCheckInActivities.class);
                context.startActivity(intent);
                Activity activity   =   (Activity)context;
                activity.overridePendingTransition(R.anim.anim, R.anim.anim);
            }
        });

        viewChildHolder.check_out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog     =   new AlertDialog.Builder(context);
                alertDialog.setIcon(R.drawable.mywarning_24dp);
                alertDialog.setTitle("Checking out of "+customer_code);
                alertDialog.setMessage("You are almost checking out of \n"+customer_name+".\nPlease confirm to checkout.");
                Dialog dialog;

                alertDialog.setNegativeButton("No, Wait", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                alertDialog.setPositiveButton("Yes, Check Out", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(db.checkOutOfCustomer() != -1)
                        {
                            commonClass.createToaster(context, "You checked out successfully from "+customer_name, Commons.TOASTER_LONG, R.drawable.smile);
                        }
                        else
                        {
                            commonClass.createToaster(context, "Sorry, was unable to check out of this customer.", Commons.TOASTER_LONG, R.drawable.sad);
                        }
                    }
                });

                dialog   =   alertDialog.create();
                dialog.show();

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
        TextView location;
        TextView contactName;
        TextView contactNo;
        TextView outletType;
        TextView decisionMakers;
        CardView check_in;
        CardView check_out;
        CardView activities;

        public ViewChildHolder(View view)
        {
            location        =   view.findViewById(R.id.routes_details_txt_Location);
            contactName     =   view.findViewById(R.id.routes_details_txt_ContactPerson);
            contactNo       =   view.findViewById(R.id.routes_details_txt_ContactNo);
            outletType      =   view.findViewById(R.id.routes_details_txt_outletType);
            decisionMakers  =   view.findViewById(R.id.routes_details_txt_decisionMaker);
            check_in        =   view.findViewById(R.id.customer_sign_in);
            check_out       =   view.findViewById(R.id.customer_check_out);
            activities      =   view.findViewById(R.id.customer_activities);
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
