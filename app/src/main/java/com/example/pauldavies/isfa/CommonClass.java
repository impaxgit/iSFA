package com.example.pauldavies.isfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

public class CommonClass
{
    /*
     * This class forms the heart of functions called all through within the app and even external services.
     * As a result, any changes being done to it must be consciously done. */

    /*This method creates a toaster.
     * Called at any point in the app when a toaster is required.
     * It requires passing of the message and icon to be displayed*/

    SharedPrefs sharedPrefs;


    String command ="";

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
   /* public void createStock(Context context, RecyclerView recyclerView, String stock_location)
    {
        DB db = new DB(context);

        Cursor cursor = db.getStockLocationBased(stock_location);
                if(cursor != null)
                {
                    while(cursor.moveToNext())
                    {
                        db.deleteStagingProduct(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)));
                        db.createProducts_Temp(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CATEGORY))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_BARCODE))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_SEARCH_NAME))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PACKAGING))
                                    , Float.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_COST)))
                                    , Float.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PRICE)))
                                    , Integer.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_SYNC)))
                                    , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_LOCATION)));
                    }
                }

        ArrayList<A_Stock> AStocks = new ArrayList<>();
        Cursor cursor1  =   db.getStagingProducts();

        if(cursor1!=null)
        {
            while (cursor1.moveToNext())
            {
                    A_Stock stock = new A_Stock(cursor1.getString(cursor1.getColumnIndex(Commons.STAGING_PRODUCT_CODE))
                            ,  db.getProductName(cursor1.getString(cursor1.getColumnIndex(Commons.STAGING_PRODUCT_CODE)))
                            , ""
                            , ""
                            , ""
                            , "");
                    AStocks.add(stock);

            }
            FragmentStockAdapter stockAdapter = new FragmentStockAdapter(AStocks,context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(stockAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }*/
    public void populateRecycler(Context context, SwipeRefreshLayout swipeRefreshLayout, DB db, SharedPrefs sharedPrefs, RecyclerView recyclerView)
    {
        Cursor cursor   =   db.getCustomerReminders(sharedPrefs.getItem("customer"));
        if(cursor.getCount()>0)
        {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

            ArrayList<A_Reminder> reminders =   new ArrayList<>();

            Adapter_Customer_Reminders_List adapter_customer_reminders_list =   new Adapter_Customer_Reminders_List(reminders, swipeRefreshLayout, recyclerView);

            while(cursor.moveToNext())
            {
                A_Reminder a_reminder   =   new A_Reminder();
                a_reminder.setRemider(cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_REMINDER_NOTE)));
                a_reminder.setRem_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_REMIDER_ID))));
                a_reminder.setStatus(true);

                reminders.add(a_reminder);
            }

            recyclerView.setAdapter(adapter_customer_reminders_list);
        }
    }

    public String getCustomerId(String route_code)
    {
        Calendar calendar   =   Calendar.getInstance();
        String date =   String.format("%s%s%s%s%s%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));//year, month, day, hour, minute, second
       return String.format("%s-%s", route_code, date);
    }


    public String getCurrencyFormat(float amount)
    {
        DecimalFormat decimalFormat =   new DecimalFormat("#,###,###");
        decimalFormat.setCurrency(Currency.getInstance("KES"));

        return decimalFormat.format(amount);
    }
    public void createOrderLines(Context context,final ExpandableListView expandableListView, String header_code)
    {
        DB db=new DB(context);
        ArrayList<OrderLines> orderLines = new ArrayList<>();

        Cursor cursor = db.getOrderLines(header_code);
        if(cursor !=null)
        {
            while (cursor.moveToNext ())
            {
                OrderLines orderLinesList = new OrderLines(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_NO)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)));
                orderLinesList.OrderLineDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_UNIT_PRICE)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_DATE_AND_TIME))});

                orderLines.add(orderLinesList);
            }

            OrderLinesAdapter orderLinesAdapter=new OrderLinesAdapter(context,orderLines);
            expandableListView.setAdapter(orderLinesAdapter);
        }

    }

    public void createOrders(Context context, final ExpandableListView expandableListView, String customer_code)
    {
        DB db   =   new DB(context);
        ArrayList<Orders> orders   =   new ArrayList<>();

        Cursor cursor   =   db.getOrdersSalesOrderHeaders(customer_code);
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Orders    orderList   =   new Orders(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_NO)),cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_DATE_AND_TIME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_CUSTOMER_CODE)));
                orderList.OrderDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_AMOUNT))});
                orders.add(orderList);
            }

            OrdersHeaderAdapter ordersHeaderAdapter =   new OrdersHeaderAdapter(context, orders);
            expandableListView.setAdapter(ordersHeaderAdapter);
        }
    }

    public String getCurrentTime()
    {
        Calendar calendar   =   Calendar.getInstance();
        return String.format("%s:%s:%s-%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
    }

    public String getLineId()
    {
        Calendar calendar   =   Calendar.getInstance();
        return String.format("%s%s%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public String getCurrentDate()
    {
        Calendar calendar   =   Calendar.getInstance();
        //To keep consistence with SQL, check and append a zero when the day has one digit.
        String day;
        String month;

        if(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).trim().length()>1)
        {
            day   =   String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }
        else
        {
            day   =   String.valueOf("0"+calendar.get(Calendar.DAY_OF_MONTH));
        }

        if(String.valueOf(calendar.get(Calendar.MONTH)+1).trim().length()>1)
        {
            month   =   String.valueOf(calendar.get(Calendar.MONTH)+1);
        }
        else
        {
            month   =   String.valueOf("0"+(calendar.get(Calendar.MONTH)+1));
        }

        return String.format("%s-%s-%s", calendar.get(Calendar.YEAR), month, day);
    }

    public String getOrderId(String customer_code)
    {
        Calendar calendar   =   Calendar.getInstance();
        return String.format("%s-%s%s%s%s%s%s", customer_code, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public void createCustomers(Context context, final ExpandableListView expandableListView, String route)
    {
        DB db   =   new DB(context);
        ArrayList<Customer> customer   =   new ArrayList<>();

        Cursor cursor   =   db.getCustomersRouteBased(route);
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

    public void createCustomersSearch(Context context, final ExpandableListView expandableListView, String criterion)
    {
        DB db   =   new DB(context);
        ArrayList<Customer> customer   =   new ArrayList<>();

        Cursor cursor   =   db.searchCustomer(criterion);
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Customer    customers   =   new Customer(cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_NAME)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CODE)));
                customers.customerDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_OUTLET_TYPE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_LOCATION))});

                customer.add(customers);
            }

            cursor.close();

            CustomerListAdapter customerListAdapter =   new CustomerListAdapter(context, customer);
            expandableListView.setAdapter(customerListAdapter);
        }
    }

    public void createCustomersSearchUnApproved(Context context, final ExpandableListView expandableListView)
    {
        DB db   =   new DB(context);
        ArrayList<Customer> customer   =   new ArrayList<>();

        Cursor cursor   =   db.searchCustomer();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                Customer    customers   =   new Customer(cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_NAME)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CODE)));
                customers.customerDetails.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_OUTLET_TYPE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_PHONE)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_CONTACT_PERSON)), cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_LOCATION))});

                customer.add(customers);
            }

            cursor.close();

            CustomerListAdapter customerListAdapter =   new CustomerListAdapter(context, customer);
            expandableListView.setAdapter(customerListAdapter);
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


    public  void createAssets(Context context, final  RecyclerView recyclerView) {
        DB db = new DB(context);
        ArrayList<Assets> assets = new ArrayList<>();

        Cursor cursor = db.getAssets();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Assets asset = new Assets(cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NO))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NAME))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_ON_SITE))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_CONDITION))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_REASON))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_LAST_SERVICE_DATE))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_DATE))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_NEXT_SERVICE_DATE))
                        , cursor.getString(cursor.getColumnIndex(Commons.ASSETS_COMMENTS)));
                assets.add(asset);

            }
            AssetAdapter adapter = new AssetAdapter(assets, context);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public void createBadStock(Context context, final RecyclerView recyclerView)
    {
       // sharedPrefs=new SharedPrefs(context);
        DB db   =   new DB(context);
        ArrayList<A_Stock> AStocks =   new ArrayList<>();

        Cursor cursor   =   db.getStock();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                A_Stock AStock = new A_Stock();

                AStock.setProduct_code(cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_ITEM_CODE)));
                AStock.setProduct_name(cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_ITEM_NAME)));
                AStock.setExpired(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.BAD_STOCK_EXPIRED))));
                AStock.setDamaged(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.BAD_STOCK_DAMAGED))));
                AStock.setTotals(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.BAD_STOCK_QTY))));
                AStock.setComments(cursor.getString(cursor.getColumnIndex(Commons.BAD_STOCK_REASON)));

                AStocks.add(AStock);
             }

            BadStockAdapter stockAdapter =   new BadStockAdapter(AStocks,context);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(stockAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }
}
