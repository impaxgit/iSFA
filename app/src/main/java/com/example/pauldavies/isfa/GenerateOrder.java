package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateOrder extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    DB db;
    ArrayList<Orders> orders;
    SharedPrefs sharedPrefs;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateorder);
        setTitle("Order Headers");

        context = this;
        db = new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass = new CommonClass();

        commonClass.createOrders(context, (ExpandableListView) findViewById(R.id.elv_ordersHome), sharedPrefs.getItem("customer"));
        swipeRefreshLayout  =  findViewById(R.id.refresh_sales_orders_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                    commonClass.createOrders(context, (ExpandableListView) findViewById(R.id.elv_ordersHome), sharedPrefs.getItem("customer"));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = findViewById(R.id.generate_fbaddOrders);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.new_order, null, false);
                final AlertDialog dialog = commonClass.createCustomDialog(context, view);

                ((TextView)view.findViewById(R.id.neworder_txtOutletName)).setText(sharedPrefs.getItem("customer")+" >"+sharedPrefs.getItem("customer_name"));
                ((TextView)view.findViewById(R.id.neworder_lblOrderId)).setText(commonClass.getOrderId(sharedPrefs.getItem("customer")));
                ((DatePicker)view.findViewById(R.id.order_date)).setMaxDate(System.currentTimeMillis());
                view.findViewById(R.id.order_btnOk).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String orderId         =   ((TextView)view.findViewById(R.id.neworder_lblOrderId)).getText().toString().trim();
                        DatePicker datePicker  =    ((DatePicker)view.findViewById(R.id.order_date));
                        int year    =   datePicker.getYear();
                        int month   =   datePicker.getMonth()+1;
                        int day     =   datePicker.getDayOfMonth();

                        String date =   String.format("%s-%s-%s", year, month, day);

                       if(!orderId.isEmpty())
                       {
                           //Toast.makeText(context, date, Toast.LENGTH_LONG).show();
                           /*if(db.createSalesOrderHeader(orderId, sharedPrefs.getItem("customer"), date, 0, sharedPrefs.getItem("username"), ((EditText)view.findViewById(R.id.txt_order_comment)).getText().toString()) != -1)
                           {
                               //The header has been created; therefore open the lines interface
                               swipeRefreshLayout.post(new Runnable()
                               {
                                   @Override
                                   public void run()
                                   {
                                       swipeRefreshLayout.setRefreshing(true);
                                        commonClass.createOrders(context, (ExpandableListView) findViewById(R.id.elv_ordersHome), sharedPrefs.getItem("customer"));
                                       swipeRefreshLayout.setRefreshing(false);
                                   }
                               });

                               sharedPrefs.putItem("OrderId", orderId);
                               commonClass.createToaster(context, "Header created, start creating lines.", Commons.TOASTER_LONG, R.drawable.smile);
                               commonClass.destroyDialog(dialog);
                               Intent intent =  new Intent(context, SalesOrder_Lines.class);
                               startActivity(intent);
                           }
                           else
                           {
                               commonClass.createToaster(context, "Could not create header, try again!", Commons.TOASTER_LONG, R.drawable.sad);
                           }*/
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

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
                    commonClass.createOrders(context, (ExpandableListView) findViewById(R.id.elv_ordersHome), sharedPrefs.getItem("customer"));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}


