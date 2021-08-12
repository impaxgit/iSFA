package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

public class Notification extends AppCompatActivity
{
    CommonClass commonClass;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context =   this;
        commonClass =   new CommonClass();

        this.setTitle("Notifications");

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        commonClass.getNotifications(context, recyclerView);

         //refreshList();
        final SwipeRefreshLayout swipeRefreshLayout =findViewById(R.id.notification_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commonClass.getNotifications(context, (RecyclerView)findViewById(R.id.recyclerview));

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
