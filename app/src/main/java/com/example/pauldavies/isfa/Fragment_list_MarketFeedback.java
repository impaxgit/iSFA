package com.example.pauldavies.isfa;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class Fragment_list_MarketFeedback extends Fragment
{

    View view;
    DB  db;
    Context context;
    SharedPrefs sharedPrefs;

    public Fragment_list_MarketFeedback()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context     =   getContext();
        db          =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        view    =   inflater.inflate(R.layout.fragment_fragment_list_market_feedback, container, false);

        this.populateRecycle();

        final SwipeRefreshLayout swipeRefreshLayout   =   view.findViewById(R.id.market_feedback_ref);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateRecycle();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void populateRecycle()
    {
        Cursor cursor   =   db.getMarketFeedback();
        if(cursor.getCount()>0)
        {
            RecyclerView recyclerView   =   view.findViewById(R.id.marketfeedback_list);
            ArrayList<A_MarketFeedBack> marketFeedBacks =   new ArrayList<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

            Adapter_MarketFeedback  adapter_marketFeedback  =   new Adapter_MarketFeedback(marketFeedBacks, context);

            recyclerView.setAdapter(adapter_marketFeedback);

            while (cursor.moveToNext())
            {
                A_MarketFeedBack a_marketFeedBack   =   new A_MarketFeedBack();
                a_marketFeedBack.setCustomer(cursor.getString(cursor.getColumnIndex(Commons.FEEDBACK_MARKET_CUSTOMER_CODE)));
                a_marketFeedBack.setType(db.getFeedbackType(cursor.getInt(cursor.getColumnIndex(Commons.FEEDBACK_MARKET_TYPE))));
                a_marketFeedBack.setFeedback(cursor.getString(cursor.getColumnIndex(Commons.FEEDBACK_MARKET_NOTE)));

                marketFeedBacks.add(a_marketFeedBack);
            }

            adapter_marketFeedback.notifyDataSetChanged();
        }
    }

}
