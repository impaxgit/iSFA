package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class DeliverOrder extends AppCompatActivity
{
    Activity context;
    CommonClass commonClass;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverorder);

        setTitle("Deliver Order");
        context = this;

        context = this;
        commonClass = new CommonClass();
        sharedPrefs =   new SharedPrefs(context);


        DB db = new DB(context);
        ArrayList<DeliveryOrders> orders = new ArrayList<>();

        ExpandableListView expandableListView   =   findViewById(R.id.deliverOrder_elvHome);
        DeliverOrdersAdapter deliverOrdersAdapter   =   new DeliverOrdersAdapter(context, orders);
        expandableListView.setAdapter(deliverOrdersAdapter);

        Cursor cursor = db.getSalesTrans(sharedPrefs.getItem("customer"));

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
