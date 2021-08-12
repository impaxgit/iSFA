package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Asset_Tracking extends Fragment
{
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        context =   getContext();
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass = new CommonClass();

        final View v = inflater.inflate(R.layout.activity_asset__tracking, container, false);

        commonClass.getAssets(context, (RecyclerView)v.findViewById(R.id.assets_rv));

        //refreshList();
        final SwipeRefreshLayout swipeRefreshLayout =v.findViewById(R.id.asset_swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.getAssets(context, (RecyclerView)v.findViewById(R.id.assets_rv));

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;

    }
}
