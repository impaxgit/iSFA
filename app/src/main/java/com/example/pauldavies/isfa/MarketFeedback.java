package com.example.pauldavies.isfa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MarketFeedback extends AppCompatActivity
{

    TabLayout tabLayout;
    FragmentManager fragmentManager;
    FragmentAdapter fragmentAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_feedback);

        this.setTitle("Market Feedback");

        fragmentManager =   getSupportFragmentManager();
        fragmentAdapter =   new FragmentAdapter(fragmentManager);

        tabLayout   =   findViewById(R.id.market_feedback_tab);
        viewPager   =   findViewById(R.id.market_view_pager);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.addFragment(new Fragment_new_MarketFeed(), "New");
        fragmentAdapter.addFragment(new Fragment_list_MarketFeedback(), "List");

        fragmentAdapter.notifyDataSetChanged();

        tabLayout.getTabAt(0).setIcon(R.drawable.new_note);
        tabLayout.getTabAt(1).setIcon(R.drawable.list);
    }
}
