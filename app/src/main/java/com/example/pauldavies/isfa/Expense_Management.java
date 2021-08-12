package com.example.pauldavies.isfa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Expense_Management extends AppCompatActivity
{
    FragmentManager fragmentManager;
    FragmentAdapter fragmentAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__management);

        this.setTitle("A_Stock Take");

        fragmentManager =   getSupportFragmentManager();
        fragmentAdapter =   new FragmentAdapter(fragmentManager);
        viewPager       =   findViewById(R.id.stock_take_view_pager);
        tabLayout       =   findViewById(R.id.stock_take_tabs);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.addFragment(new Fragment_Expenses(), "New Expenses");
        fragmentAdapter.addFragment(new Fragment_ExpenseList(), "LIST");

        fragmentAdapter.notifyDataSetChanged();
    }

}
