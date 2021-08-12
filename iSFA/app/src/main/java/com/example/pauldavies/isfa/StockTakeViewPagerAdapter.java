package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class StockTakeViewPagerAdapter extends FragmentStatePagerAdapter
{
    FragmentManager fm;
    int TabsCount;
    Context context;

    public StockTakeViewPagerAdapter(Context context,FragmentManager fm,int tabsCount)
    {
        super(fm);
        this.fm=fm;
        this.context = context;
        this.TabsCount=tabsCount;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
        case 0:
            return new Fragment_Count();

        case 1:
            return new Fragment_Stock();

        default:
            return null;
    }

    }

    @Override
    public int getCount()
    {
        return TabsCount;
    }
}
