package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class JourneyPlan extends AppCompatActivity
{
    Context context;
    private TabLayout tabLayout;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan);

        this.setTitle("Journey Plans");

        context = this;
        tabLayout = findViewById(R.id.routes_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.journeyPlan).setIcon(R.drawable.ic_my_location_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.map).setIcon(R.drawable.ic_map_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_customers).setIcon(R.drawable.ic_group_black_24dp));


        ViewPager viewPager = findViewById(R.id.routes_viewpager);
        RoutesViewerPagerAdapter adapter = new RoutesViewerPagerAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //on tab select

        tabLayout.setOnTabSelectedListener(
        new TabLayout.ViewPagerOnTabSelectedListener(viewPager)
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                super.onTabSelected(tab);
            }
        });

    }

}