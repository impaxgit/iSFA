package com.example.pauldavies.isfa;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter
{
    List<Fragment> fragments    =   new ArrayList<>();
    List<String>   titles =   new ArrayList<>();

    public FragmentAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles.get(position);
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragments.add(fragment);
        titles.add(title);
    }
}
