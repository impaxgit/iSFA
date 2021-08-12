package com.example.pauldavies.isfa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OutletActivities extends AppCompatActivity
{

    FragmentManager fragmentManager;
    FragmentAdapter fragmentAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_activities);

        this.setTitle("Call Objectives");

        fragmentManager =   getSupportFragmentManager();
        fragmentAdapter =   new FragmentAdapter(fragmentManager);
        viewPager       =   findViewById(R.id.customer_call_activities_view_pager);
        tabLayout       =   findViewById(R.id.customer_call_tabs);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.addFragment(new Fragment_Customer_Call_Activities_List(), "Activities");
        fragmentAdapter.addFragment(new Fragment_Customer_Call_Reminders(), "Reminders");

        fragmentAdapter.notifyDataSetChanged();

        tabLayout.getTabAt(0).setIcon(R.drawable.work);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_active_black_24dp);
    }
}
