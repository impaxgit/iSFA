package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerOrderDelivery extends AppCompatActivity
{

    Context context;
    DB db;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_delivery);

        this.setTitle("Orders For Delivery");
        context =   this;
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        Cursor cursor = db.getSalesTrans(sharedPrefs.getItem("customer"));

        ArrayList<DeliveryOrders> orders    =   new ArrayList<>();
        RecyclerView recyclerView   =   findViewById(R.id.home_order_recycle);
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

    }
}
