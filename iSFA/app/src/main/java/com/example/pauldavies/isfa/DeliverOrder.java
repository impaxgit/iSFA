package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class DeliverOrder extends AppCompatActivity
{
    Activity context;
    CommonClass commonClass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverorder);

        setTitle("Deliver Order");
        context = this;

        context = this;
        commonClass = new CommonClass();

        commonClass.deliverOrders(context, (ExpandableListView)findViewById(R.id.deliverOrder_elvHome));
    }
}
