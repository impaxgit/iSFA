package com.example.pauldavies.isfa;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class Fragment_Customer_Call_Activities_List extends Fragment
{
    View view;
    Context context;
    DB  db;
    SharedPrefs sharedPrefs;

    public Fragment_Customer_Call_Activities_List()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context =   getContext();
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        view    = inflater.inflate(R.layout.fragment_fragment_customer_call_activities_list, container, false);

        final SwipeRefreshLayout  swipeRefreshLayout  =   view.findViewById(R.id.outlet_activities_ref);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                populateRecycler();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        populateRecycler();


        view.findViewById(R.id.customer_activity_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                View view1  =   ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_one_column, null, false);

                Cursor cursor   =   db.getOutletActivities(sharedPrefs.getItem("customer"));
                if(cursor.getCount()>0)
                {
                    RecyclerView recyclerView   =   view1.findViewById(R.id.view_customer_activities_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

                    ArrayList<OneStringProperty> oneStringProperties    =   new ArrayList<>();
                    Adapter_One_Column adapter_one_column   =   new Adapter_One_Column(oneStringProperties);

                    recyclerView.setAdapter(adapter_one_column);
                    while(cursor.moveToNext())
                    {
                        OneStringProperty   oneStringProperty   =   new OneStringProperty();
                        oneStringProperty.setValue(cursor.getString(cursor.getColumnIndex(Commons.OUTLET_ACTIVITY_NAME_CUSTOMER)));
                        oneStringProperties.add(oneStringProperty);
                    }

                    adapter_one_column.notifyDataSetChanged();
                }

                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.setCancelable(false);

                view1.findViewById(R.id.show_visibles).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }
        });


        return view;
    }

    public void populateRecycler()
    {
        Cursor cursor   =   db.getOutletActivities();
        if(cursor.getCount()>0)
        {
            ArrayList<An_Outlet_Activity> outlet_activities =   new ArrayList<>();

            RecyclerView    recyclerView    =   view.findViewById(R.id.outlet_activities_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

            Adapter_Customer_Outlet_Activities adapter_customer_outlet_activities   =   new Adapter_Customer_Outlet_Activities(outlet_activities);
            recyclerView.setAdapter(adapter_customer_outlet_activities);

            while(cursor.moveToNext())
            {
                An_Outlet_Activity an_outlet_activity   =   new An_Outlet_Activity();
                an_outlet_activity.setName(cursor.getString(cursor.getColumnIndex(Commons.OUTLET_ACTIVITY_NAME)));
                an_outlet_activity.setCode(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.OUTLET_ACTIVITY_ID))));
                an_outlet_activity.setChecked(false);

                outlet_activities.add(an_outlet_activity);
            }

            adapter_customer_outlet_activities.notifyDataSetChanged();
        }
    }

}
