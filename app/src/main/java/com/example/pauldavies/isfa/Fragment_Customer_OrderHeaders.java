package com.example.pauldavies.isfa;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Fragment_Customer_OrderHeaders extends Fragment
{
    Context context;
    DB db;
    View view;
    FragmentManager fragmentManager;
    SharedPrefs sharedPrefs;

    public Fragment_Customer_OrderHeaders()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context =   getContext();
        db  =   new DB(context);
        fragmentManager =   getActivity().getSupportFragmentManager();
        sharedPrefs = new SharedPrefs(context);

        view = LayoutInflater.from(container.getContext()).inflate(R.layout.cutom_order_header_lst, container, false);

        Cursor cursor = db.getSalesTrans(sharedPrefs.getItem("customer"));

        ArrayList<DeliveryOrders> orders    =   new ArrayList<>();
        RecyclerView recyclerView   =   view.findViewById(R.id.custom_order_lst);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        CustomerOrderDeliveryHeaders customerOrderDeliveryHeaders   =   new CustomerOrderDeliveryHeaders(orders, context);
        recyclerView.setAdapter(customerOrderDeliveryHeaders);

        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                DeliveryOrders deliverOrder   =   new DeliveryOrders(cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_ID)), cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_DATE_TIME)), String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_TOTAL_AMOUNT))));
                orders.add(deliverOrder);
            }

        }
        return view;
    }
}
