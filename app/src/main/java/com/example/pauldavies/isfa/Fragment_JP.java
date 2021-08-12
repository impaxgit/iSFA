package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_JP extends Fragment
{
    View view;
    ExpandableListView expandableListView;
    ArrayList<Journey> journeys =new ArrayList<>();
    Context context;
    int lastExpandedPosition=-1;
    CommonClass commonClass;
    private ArrayList<Journey> plan;
    DB db;

    public Fragment_JP()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context= this.getContext();
        commonClass =   new CommonClass();
        db  =   new DB(context);

        view=inflater.inflate(R.layout.fragment_fragment__j,container,false);

        //Get Routes
        expandableListView=view.findViewById(R.id.routes_elv_home);

        ArrayList<Journey> journeys =   new ArrayList<>();
        Cursor cursor   =   db.getJourneyPlans();
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                Journey journey =   new Journey(cursor.getString(cursor.getColumnIndex(Commons.PLAN_CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_NAME)));
                journey.journeys.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_LOCATION)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_OUTLET_TYPE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_OWNER_NAME))});

                journeys.add(journey);
            }
        }

        JourneyPlanAdapter journeyPlanAdapter   =   new JourneyPlanAdapter(journeys, context);
        expandableListView.setAdapter(journeyPlanAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {

            @Override
            public void onGroupExpand(int groupPosition)
            {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        return view;
    }


}

