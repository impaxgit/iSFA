package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Fragment_Customer_Stock extends Fragment
{
    Context context;
    DB  db;
    SharedPrefs sharedPrefs;
    SwipeRefreshLayout swipeRefreshLayout;
    CommonClass commonClass;
    FloatingActionButton fab;

    public Fragment_Customer_Stock()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        context = this.getActivity();
        commonClass = new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db = new DB(context);

        final View rootView = inflater.inflate(R.layout.fragment__stock, container, false);
        fab=rootView.findViewById(R.id.stocks_saving);
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

