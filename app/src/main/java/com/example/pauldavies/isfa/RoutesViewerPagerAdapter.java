package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RoutesViewerPagerAdapter extends FragmentStatePagerAdapter
{

    FragmentManager fm;
    int TabsCount;
    Context context;


    public RoutesViewerPagerAdapter(Context context,FragmentManager fm,int TabsCount)
    {
        super(fm);
        this.context=context;
        this.fm=fm;
        this.TabsCount=TabsCount;

    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return new Fragment_JP();

            case 1:
                return new Fragment_Maps();

            case 2:
                return new Fragment_All_Customers();

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
