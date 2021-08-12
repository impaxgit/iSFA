package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CommonClass
{

    /*
     * This class forms the heart of functions called all through within the app and even external services.
     * As a result, any changes being done to it must be consciously done. */

    /*This method creates a toaster.
     * Called at any point in the app when a toaster is required.
     * It requires passing of the message and icon to be displayed*/

    String command ="";
    ArrayList<OrderItems> orderList;
    public boolean isDelivered;
    String itemcode,customercode;
    SharedPrefs sharedPrefs;

    public String getCurrentTime()
    {
        Calendar calendar   =   Calendar.getInstance();
        return String.format("%s:%s:%s-%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
    }

    public String getCurrentDate()
    {
        Calendar calendar   =   Calendar.getInstance();
        return String.format("%s-%s-%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void createCustomers(Context context, final ExpandableListView expandableListView)
    {
        DB db   =   new DB(context);
        ArrayList<Customer> customer   =   new ArrayList<>();

        Cursor cursor   =   db.getCustomers();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Customer    customers   =   new Customer(cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_NAME)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CODE)));
                customers.customerDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_OUTLET_TYPE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_LOCATION))});

                customer.add(customers);
            }

            CustomerListAdapter customerListAdapter =   new CustomerListAdapter(context, customer);
            expandableListView.setAdapter(customerListAdapter);
        }
    }

    public  void createOrderLines(Context context,final  ExpandableListView expandableListView)
{
    DB db=new DB(context);
    ArrayList<OrderLines> orderLines = new ArrayList<>();

    Cursor cursor  = db.getOrderLines();
    if(cursor !=null)
    {
        while (cursor.moveToNext ())
        {
            OrderLines orderLinesList = new OrderLines(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_HEADER)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)));
            orderLinesList.OrderLineDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_UNIT_PRICE)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_DATE_AND_TIME))});

            orderLines.add(orderLinesList);
        }

        OrderLinesAdapter orderLinesAdapter=new OrderLinesAdapter(context,orderLines);
        expandableListView.setAdapter(orderLinesAdapter);
    }

}

    public void createOrders(Context context, final ExpandableListView expandableListView)
    {
        DB db   =   new DB(context);
        ArrayList<Orders> orders   =   new ArrayList<>();

        Cursor cursor   =   db.getOrdersHeaders();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Orders    orderList   =   new Orders(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_NO)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_DATE_AND_TIME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_CUSTOMER_CODE)));
                orders.add(orderList);

                //Add the lines
            }

            OrdersHeaderAdapter ordersHeaderAdapter =   new OrdersHeaderAdapter(context, orders);
            expandableListView.setAdapter(ordersHeaderAdapter);
        }
    }

    public void createBadStock(FragmentActivity context, final RecyclerView recyclerView)
    {
        sharedPrefs=new SharedPrefs(context);
        DB db   =   new DB(context);
        ArrayList<Stock> stocks   =   new ArrayList<>();

        Cursor cursor   =   db.getBadStock();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Stock stock = new Stock(cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_ITEM_CODE))
                        ,cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_ITEM_CODE))
                        ,cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_EXPIRED))
                         ,cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_DAMAGED))
                          ,cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_QTY))
                           ,cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_REASON)));
                stocks.add(stock);

            }
            StockAdapter stockAdapter =   new StockAdapter(stocks,context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(stockAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }

    public void getNotifications(Context context, final RecyclerView recyclerView)
    {
        DB db   =   new DB(context);
        ArrayList<Notifications> notifications   =   new ArrayList<>();

        Cursor cursor   =   db.getNotifications();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Notifications notify = new Notifications(cursor.getInt(cursor.getColumnIndex(Commons.NOTIFICATIONS_ID))
                        ,cursor.getString(cursor.getColumnIndex(Commons.NOTIFICATIONS_SUBJECT))
                        ,cursor.getString(cursor.getColumnIndex(Commons.NOTIFICATIONS_CONTENT))
                        ,cursor.getString(cursor.getColumnIndex(Commons.NOTIFICATIONS_STATUS)));
                notifications.add(notify);

            }
            NotificationsAdapter adapter =   new NotificationsAdapter(notifications,context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }
    }

    public  void getAssets(Context context, final  RecyclerView recyclerView)
    {
        DB db = new DB(context);
        ArrayList<Assets> assets  = new ArrayList<>();

        Cursor cursor  = db.getAssets();
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                Assets asset  = new Assets(cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NO))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NAME))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_ON_SITE))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_CONDITION))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_REASON))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_LAST_SERVICE_DATE))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_DATE))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NEXT_SERVICE_DATE))
                                            ,cursor.getString(cursor.getColumnIndex(Commons.ASSETS_COMMENTS)));
                assets.add(asset);

            }
            AssetTrackingAdapter adapter =   new AssetTrackingAdapter(assets,context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }
    }

    public void deliverOrders(Activity context, final ExpandableListView expandableListView) {
        DB db = new DB(context);
        ArrayList<Orders> orders = new ArrayList<>();
        ArrayList<OrderItems> orderItems = new ArrayList<>();

        Cursor cursor = db.getSalesTrans();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Orders orderList = new Orders(cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_NO)), cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_DATE_TIME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_CUSTOMER_CODE)));

                //Query for lines and loop through
                Cursor cursorLines = db.getSalesLinesTrans();
                if (cursorLines != null) {
                    OrderItems orderLines = null;
                    while (cursorLines.moveToNext()) {
                        orderLines = new OrderItems(
                                cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_ITEM_LINES_NAME))
                                , cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_LINE_QTY_ORDERED))
                                , cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_LINE_UNIT_PRICE))
                                , cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_LINE_QTY_DELIVERED))
                                , cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_LINE_TOTAL_PRICE))
                                , Boolean.valueOf(cursorLines.getString(cursorLines.getColumnIndex(Commons.SALES_TRANS_LINE_STATUS))));

                        orderItems.add(orderLines);
                    }
                    orders.add(orderList);
                    orderList.orderItems.add(orderLines);
                }
                DeliverOrdersAdapter deliverOrdersAdapter = new DeliverOrdersAdapter(context, orders);
                expandableListView.setAdapter(deliverOrdersAdapter);
            }
        }
    }
    public void createToaster(Context context, String message, int duration, int icon)
    {
        LayoutInflater layoutInflater   =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view   =   layoutInflater.inflate(R.layout.toaster_layoru, null, false);
        Toast toast =   new Toast(context);
        toast.setView(view);

        ((TextView)view.findViewById(R.id.toaster_txt_message)).setText(message);
        ((ImageView)view.findViewById(R.id.toaster_icon)).setImageResource(icon);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.show();


    }


    /*This will always produce a a normal dialog basically for displaying any message desired at any particular point in the App.
     * It requires a title, message and icon to be displayed.
     * Buttons are done at the point of calling the creation of the dialog.*/
    public void createNormalDialog(Context context, String title, String message, int icon, String positive_text, String negative_text, AsyncTaskDelegate delegate)
    {
        LayoutInflater layoutInflater   =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view   =   layoutInflater.inflate(R.layout.dialog_normal, null, false);

        final AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        ((TextView)view.findViewById(R.id.dialog_normal_title)).setText(title);
        ((TextView)view.findViewById(R.id.dialog_normal_message)).setText(message);
        ((ImageView)view.findViewById(R.id.dialog_normal_icon)).setImageResource(icon);

        ((Button)view.findViewById(R.id.button3)).setText(positive_text);
        ((Button)view.findViewById(R.id.button4)).setText(negative_text);

        final AlertDialog dialog  =   alertDialog.create();

        (view.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                command =   "1";
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               command  =   "0";
               dialog.dismiss();
            }
        });

        delegate.getResult(command);
        dialog.show();
    }


    /*This is a view based dialog generator.
     * It produces a pop view of the view given at the time of calling.
     */
    public AlertDialog createCustomDialog(Context context, View view)
    {
        AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
        alertDialog.setView(view);

        AlertDialog dialog  =   alertDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

    /*This creates a progress bar.
     * It is called any time required*/
    public AlertDialog createProgressBar(Context context)
    {
        LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view   =   layoutInflater.inflate(R.layout.progressbar, null, false);

        AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        AlertDialog dialog  =   alertDialog.create();
        dialog.show();

        return dialog;
    }

    public void destroyDialog(AlertDialog dialog)
    {
        dialog.dismiss();
    }

    public void launchCamera(Context context, Activity activity)
    {
        Intent intent   =   new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager())  !=  null)
        {
            activity.startActivityForResult(intent, Commons.REQ_CAPTURE_IMAGE);
        }
    }

    public String checkNetwork(Context context)
    {
        String net_status   =   "";

        ConnectivityManager connectivityManager =   (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =   connectivityManager.getActiveNetworkInfo();
        if(networkInfo ==null || !networkInfo.isConnected())
        {
            net_status  =   Commons.NO_NETWORK;
        }

        return net_status;
    }

    public int calculateTotal()
    {
        int total = 0;
        for(OrderItems orderItems: orderList){
            total+=  orderItems.getTotals();
        }
        return total;
    }
}
