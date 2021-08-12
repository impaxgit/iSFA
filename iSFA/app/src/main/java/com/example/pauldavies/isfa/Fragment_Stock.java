package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment_Stock extends Fragment
{

FragmentActivity context;
CommonClass commonClass;
DB db;
View view;
SharedPrefs sharedPrefs;
private  ViewPager viewPager;

    public Fragment_Stock()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        commonClass = new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db = new DB(context);

        sharedPrefs.getItem("customercode");
        final View rootView = inflater.inflate(R.layout.activity_fragment__stock, container, false);
        commonClass.createBadStock(context, (RecyclerView) rootView.findViewById(R.id.stock_recyclerView));

        //refreshList();
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.orders_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.createBadStock(context, (RecyclerView) rootView.findViewById(R.id.stock_recyclerView));

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }
}
