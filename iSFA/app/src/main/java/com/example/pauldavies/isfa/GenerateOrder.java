package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateOrder extends AppCompatActivity {
    Context context;
    CommonClass commonClass;
    DB db;
    ArrayList<Orders> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateorder);
        setTitle("Order Headers");
        context = this;

        context = this;
        db = new DB(context);
        commonClass = new CommonClass();

        commonClass.createOrders(context, (ExpandableListView) findViewById(R.id.elv_ordersHome));
        refreshList();

        FloatingActionButton fab = findViewById(R.id.generate_fbaddOrders);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.new_order, null, false);
                final AlertDialog dialog = commonClass.createCustomDialog(context, view);

                view.findViewById(R.id.order_btnOk).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String orderId         =   ((EditText)view.findViewById(R.id.neworder_txtOrderId)).getText().toString().trim();
                        String orderDate       =   String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                       if(!orderId.isEmpty())
                       {
                           if(!orderDate.isEmpty())
                           {
                               db.createOrdersHeaders(0,orderId,"",orderDate,Float.valueOf(0),"");
                           }
                           else
                           {
                               Toast.makeText(context,"Unsuccessful",Toast.LENGTH_LONG).show();
                           }

                       }

                    }
                });

                view.findViewById(R.id.order_btnCancel).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                      dialog.dismiss();
                    }
                });
            }
        });
    }

    private void refreshList()
    {
        final SwipeRefreshLayout swipeRefreshLayout   =   (SwipeRefreshLayout)findViewById(R.id.orders_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.createOrders(context, ((ExpandableListView)findViewById(R.id.elv_ordersHome)));

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}


