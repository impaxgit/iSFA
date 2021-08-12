package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public  class Fragment_Assets extends Fragment
{
    Context context;
    DB  db;
    SharedPrefs sharedPrefs;
    SwipeRefreshLayout swipeRefreshLayout;
    CommonClass commonClass;
    public Fragment_Assets()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        commonClass = new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db = new DB(context);

        final View rootView = inflater.inflate(R.layout.activity_asset__tracking, container, false);
        commonClass.createAssets(context, (RecyclerView) rootView.findViewById(R.id.assets_rv));

        //refreshList();
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.asset_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.createAssets(context, (RecyclerView) rootView.findViewById(R.id.assets_rv));

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }
}

