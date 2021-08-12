package com.example.pauldavies.isfa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Customer_StockTake extends AppCompatActivity
{
    FragmentManager fragmentManager;
    FragmentAdapter fragmentAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);

        this.setTitle("Stock Take");

        fragmentManager =   getSupportFragmentManager();
        fragmentAdapter =   new FragmentAdapter(fragmentManager);
        viewPager       =   findViewById(R.id.stock_take_view_pager);
        tabLayout       =   findViewById(R.id.stock_take_tabs);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.addFragment(new Fragment_Customer_Stock_Take(), "Stock Count");
        fragmentAdapter.addFragment(new Fragment_Customer_Stock(), "Stock");

        fragmentAdapter.notifyDataSetChanged();

        tabLayout.getTabAt(0).setIcon(R.drawable.stock_count);
        tabLayout.getTabAt(1).setIcon(R.drawable.stocks);
    }

}
