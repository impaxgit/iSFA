package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Home extends AppCompatActivity
{

    Menu home_menu;
    Context context;
    String [] items ={"Customers","Product Master","StockChild Management", "My Trip", "Day in Trade","Performance", "Feedback Central", "Expenses ", "My Reports", "Synchronizations"};
    Integer[] icons ={R.drawable.customer,R.drawable.product,R.drawable.stock,R.drawable.trips,R.drawable.days,R.drawable.performance,R.drawable.feedback,R.drawable.expenses,R.drawable.report, R.drawable.sync};
    CommonClass commonClass;
    SharedPrefs sharedPrefs;
    DB db;
    String header,date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Fixing Later Map loading Delay
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Fragment_Maps mv = new Fragment_Maps();
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();*/

        context=this;
        this.setTitle("iSFA::Home");
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db  =   new DB(context);

        int alert   =   10;
        if(alert>0)
        {
            ((FloatingActionButton)findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.notificationalert);
            (findViewById(R.id.home_alert_btn)).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));


        }
        else
        {
            ((FloatingActionButton)findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.notificationclear);
            (findViewById(R.id.home_alert_btn)).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
        }
        findViewById(R.id.home_alert_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(context, Notification.class);
                startActivity(intent);
            }
        });


        HomeAdapter homeAdapter=new HomeAdapter(this, items,icons);
        ((ListView)findViewById(R.id.lv_home)).setAdapter(homeAdapter);

        ((ListView)findViewById(R.id.lv_home)).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getItemAtPosition(position).toString())
                {
                    case "Customers":
                        Intent intent   =   new Intent(context, CustomerList.class);
                        startActivity(intent);
                    break;
                    case "My Trip":
                        Intent trip = new Intent(context, TripManagement.class);
                        startActivity(trip);
                    break;
                    case "Synchronizations":
                        createCustomers();
                    break;
                    case "Day in Trade":
                         Intent dayinTrade=new Intent(context,JourneyPlan.class);
                         startActivity(dayinTrade);
                         break;
                    default:
                        Toast.makeText(context,"Am yet to be programmed", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    public void createCustomers() {

    final AlertDialog progressbar = commonClass.createProgressBar(context);

    new ExternalConnectionTest(new AsyncTaskDelegate() {
        @Override
        public void getResult(String result) {
            if (result.contains(Commons.CON_SUCCESS))
            {


                //Get customers
        new ShareAsyncClass(Commons.URL_GET_CUSTOMERS, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result)
            {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Customer customers = new Customer(jsonObject1.getString("customer_name"), jsonObject1.getString("contact_person_mobile"), jsonObject1.getString("customer_code"));
                customers.customerDetails.add(new String[]{jsonObject1.getString("outlet_type"), jsonObject1.getString("contact_person"), jsonObject1.getString("mobile"), jsonObject1.getString("business_owner"), jsonObject1.getString("route")});

                /*Delete the current customer record before inserting a new one.
                 *This assumes that the record coming form the external source is most updated.*
                 */
                db.deleteCustomer(jsonObject1.getString("customer_code"));

                db.createCustomer(jsonObject1.getString("customer_code")
                        , jsonObject1.getString("customer_name")
                        , jsonObject1.getString("description")
                        , jsonObject1.getString("outlet_type")
                        , jsonObject1.getString("route")
                        , jsonObject1.getString("geocoordnates")
                        , jsonObject1.getString("date_created")
                        , jsonObject1.getString("customerType")
                        , Integer.valueOf(jsonObject1.getString("taxable"))
                        , jsonObject1.getString("email")
                        , jsonObject1.getString("mobile")
                        , jsonObject1.getString("contact_person")
                        , jsonObject1.getString("email")
                        , jsonObject1.getString("contact_person_mobile")
                        , Float.valueOf(jsonObject1.getString("credit_limit"))
                        , Float.valueOf(jsonObject1.getString("credit_balance"))
                        , Integer.valueOf(jsonObject1.getString("status"))
                        , Integer.valueOf(jsonObject1.getString("sync")));
            }
        } catch (JSONException e) {
            commonClass.createToaster(context, "Could not parse customer data", Commons.TOASTER_LONG, R.drawable.warning);
        }
    }
}).execute();


        //Get the routes
                new ShareAsyncClass( Commons.URL_ROUTES, new AsyncTaskDelegate() {
                    @Override
                    public void getResult(String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                db.deleteRoutes(String.valueOf(jsonObject1.getInt("id")));
                                db.createRoutes(jsonObject1.getInt("id")
                                        , jsonObject1.getString("route_id")
                                        , jsonObject1.getString("route_name")
                                        , jsonObject1.getString("description")
                                        , jsonObject1.getString("territory")
                                        , jsonObject1.getString("region")
                                        , jsonObject1.getInt("active"));
                            }
                        } catch (JSONException e) {

                        }
                    }
                }).execute();
            //Get Orders and lines
                    new ShareAsyncClass(Commons.URL_GET_ORDERSHEADERS, new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result) {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            db.deleteOrders(jsonObject1.getString("order_header"));
                            db.createOrdersHeaders(jsonObject1.getInt("order_id")
                                    , jsonObject1.getString("order_header")
                                    , jsonObject1.getString("customer_code")
                                    , jsonObject1.getString("date_ordered")
                                    , Float.valueOf(jsonObject1.getString("order_amount"))
                                    , jsonObject1.getString("created_by"));
                }
            } catch (JSONException e)
                    {
               commonClass.createToaster(context, "Could not parse Order Header data", Commons.TOASTER_LONG, R.drawable.warning);
                }
        }
        }).execute();

                // lines
                new ShareAsyncClass(Commons.URL_GET_ORDERSLINES, new AsyncTaskDelegate() {
                    @Override
                    public void getResult(String result)
                    {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                db.deleteOrderLines(jsonObject1.getString("order_line"));
                                db.createOrderLines(jsonObject1.getInt("id")
                                        , jsonObject1.getString("order_header")
                                        , jsonObject1.getString("order_line")
                                        , jsonObject1.getString("product_code")
                                        , jsonObject1.getString("product_name")
                                        , jsonObject1.getString("description")
                                        , Float.valueOf(jsonObject1.getString("unit_price"))
                                        , Integer.valueOf(jsonObject1.getString("quantity"))
                                        , Float.valueOf(jsonObject1.getString("line_amount"))
                                        , jsonObject1.getString("date"));
                            }
                        } catch (JSONException e) {
                            commonClass.createToaster(context, "Could not parse OrderLinesHeader data", Commons.TOASTER_LONG, R.drawable.warning);
                        }
                    }
                }).execute();


                //Get sales order transactions and lines
                new ShareAsyncClass(Commons.URL_GET_TRANSORDERS, new AsyncTaskDelegate()
                {
                    @Override
                    public void getResult(String result)
                    {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                db.deleteSalesOrders(jsonObject1.getString("trans_header_id"));
                                db.GetSalesOrders(jsonObject1.getInt("id")
                                        , jsonObject1.getString("trans_header_id")
                                        , jsonObject1.getString("customer_code")
                                        , jsonObject1.getString("date")
                                        , Float.valueOf(jsonObject1.getString("trans_amount"))
                                        , jsonObject1.getString("created_by")
                                        , jsonObject1.getString("delivered"));



                            }
                        } catch (JSONException e)
                        {
                            commonClass.createToaster(context, "Could not parse Sales Order data", Commons.TOASTER_LONG, R.drawable.warning);
                        }
                    }
                }).execute();
            } else

            {
                commonClass.createToaster(context, "Could not connect externally.", Commons.TOASTER_LONG, R.drawable.sad);
            }

    commonClass.destroyDialog(progressbar);
        }
}).execute();

        //Add OrdersLines to sales order headers
        new ShareAsyncClass( Commons.URL_GET_TRANSORDERSLINES, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result) {

                try {
                    JSONObject jsonObject2 = new JSONObject(result);
                    JSONArray jsonArray1 = jsonObject2.getJSONArray(Commons.RESULT);
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject3 = jsonArray1.getJSONObject(j);

                        db.deleteSalesOrderLines(jsonObject3.getString("order_trans_line"));
                        db.GetSalesOrderLines(jsonObject3.getInt("id")
                                , jsonObject3.getString("order_trans_header")
                                , jsonObject3.getString("order_trans_line")
                                , jsonObject3.getString("product_code")
                                , jsonObject3.getString("product_name")
                                , jsonObject3.getString("description")
                                , jsonObject3.getInt("quantityOrdered")
                                , jsonObject3.getInt("quantityDelivered")
                                , Float.valueOf(jsonObject3.getString("unit_price"))
                                , Float.valueOf(jsonObject3.getString("line_amount"))
                                , jsonObject3.getString("date")
                                , jsonObject3.getString("delivered")
                                , jsonObject3.getString("comments"));

                    }

                } catch (JSONException e)
                {
                  commonClass.createToaster(context, "Could not parse Sales order lines data", Commons.TOASTER_LONG, R.drawable.warning);
                }

            }
        }).execute();

        //Get all the  products

        new TwoParamsShareAsync( Commons.URL_GET_PRODUCTS, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(j);

                        db.deleteProducts(jsonObject3.getString("product_code"),"salesperson_id");
                        db.GetProducts(jsonObject3.getString("product_code")
                                , jsonObject3.getString("product_name")
                                , jsonObject3.getString("product_category")
                                , jsonObject3.getString("product_barcode")
                                , jsonObject3.getString("product_packaging")
                                , Float.valueOf(jsonObject3.getString("product_cost"))
                                , Float.valueOf(jsonObject3.getString("product_price"))
                                , jsonObject3.getString("salesperson_id"));
                    }

                } catch (JSONException e)
                {
                    commonClass.createToaster(context, "Could not parse Product Data", Commons.TOASTER_LONG, R.drawable.warning);
                }

            }
        }).execute();
//Get all notifications
        new TwoParamsShareAsync( Commons.URL_GET_NOTIFICATIONS, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(j);

                        db.deleteNotifications(jsonObject3.getString("notifications_Id"));
                        db.createNotifications(jsonObject3.getString("notifications_Id")
                                , jsonObject3.getString("subject")
                                , jsonObject3.getString("contents")
                                , jsonObject3.getString("status"));
                    }

                } catch (JSONException e)
                {
                    commonClass.createToaster(context, "Could not parse Notifications Data", Commons.TOASTER_LONG, R.drawable.warning);
                }

            }
        }).execute();


        //Get assets

        new ShareAsyncClass(Commons.URL_GET_ASSETS, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray   =jsonObject.getJSONArray(Commons.RESULT);
                    for(int i = 0; i <jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        db.deleteAssets(object.getString("assetNo"));
                        db.createAssets(object.getString("assetNo")
                                 ,object.getString("assetName")
                                 ,object.getString("onSite")
                                 ,object.getString("condition")
                                 ,object.getString("reason")
                                 ,object.getString("lastServiceDate")
                                 ,object.getString("Date")
                                 ,object.getString("nextServiceDate")
                                 ,object.getString("comments"));
                    }


                }catch (JSONException e)
                {

                    commonClass.createToaster(context, "Could not parse Assets Data", Commons.TOASTER_LONG, R.drawable.warning);
                }


            }
        }).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater findMenuitems  =   getMenuInflater();
        findMenuitems.inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view   =   layoutInflater.inflate(R.layout.logout_layout, null, false);
                final AlertDialog dialog  =   commonClass.createCustomDialog(context, view);
                ((TextView)view.findViewById(R.id.logout_salutation)).setText("Logout Alert");
                ((TextView)view.findViewById(R.id.logout_msg)).setText("Hey "+sharedPrefs.getItem("username")+",\nYou have chosen to log out.\nConfirm by clicking 'Logout' but all your data will remain intact next time you login.");
                view.findViewById(R.id.logout_logout_btn).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        sharedPrefs.clearAll();
                        commonClass.destroyDialog(dialog);
                        finish();
                    }
                });

                view.findViewById(R.id.logout_close).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        commonClass.destroyDialog(dialog);
                    }
                });
            break;
            default:
                commonClass.createToaster(context, "Unknown action", Commons.TOASTER_LONG, R.drawable.cross);
            break;

        }

        return super.onOptionsItemSelected(item);
    }
}
