package com.example.pauldavies.isfa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Customer_Asset_Tracking extends AppCompatActivity
{
    FragmentManager fragmentManager;
    FragmentAdapter fragmentAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__asset__tracking);
        this.setTitle("Assets Tracking");

        fragmentManager =   getSupportFragmentManager();
        fragmentAdapter =   new FragmentAdapter(fragmentManager);
        viewPager       =   findViewById(R.id.assets_view_pager);
        tabLayout       =   findViewById(R.id.assets_tabs);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.addFragment(new Asset_Tracking(), "Track");
        fragmentAdapter.addFragment(new Fragment_Assets(), "Assets");

        fragmentAdapter.notifyDataSetChanged();

        tabLayout.getTabAt(0).setIcon(R.drawable.track);
        tabLayout.getTabAt(1).setIcon(R.drawable.stocks);
    }
}
