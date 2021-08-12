package com.example.pauldavies.isfa;

import android.app.ActionBar;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class StockTake extends AppCompatActivity
{
    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBar actionBar;
    static final int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);

        this.setTitle("iSFA::Stock Take");

        context = getApplicationContext();
        tabLayout = findViewById(R.id.stockTake_tabLayout);
        viewPager = findViewById(R.id.stockTake_viewpager);
        actionBar = getActionBar();

        tabLayout.addTab(tabLayout.newTab().setText(R.string.count).setIcon(R.drawable.count));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stock).setIcon(R.drawable.stok));
        StockTakeViewPagerAdapter adapter = new StockTakeViewPagerAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });


    }
}
