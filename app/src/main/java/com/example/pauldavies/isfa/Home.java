package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Home extends AppCompatActivity
{

    Menu home_menu;
    Context context;
    CommonClass commonClass;
    SharedPrefs sharedPrefs;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context=this;
        this.setTitle("Home");
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);


        db  =   new DB(context);

        //Check if there are any notifications and change the icon of the alert button

        if(db.checkNotifications())
        {
            ((FloatingActionButton) findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }

        findViewById(R.id.home_alert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =   new Intent(context, Notification.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });
        findViewById(R.id.home_card_customers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =   new Intent(context, CustomerList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.home_card_products).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product_master   =   new Intent(context, ProductList.class);
                startActivity(product_master);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.home_card_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trip = new Intent(context, TripManagement.class);
                startActivity(trip);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.home_card_trade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent day_in_trade = new Intent(context, DayInTrade.class);
                startActivity(day_in_trade);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.home_card_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings     =   new Intent(context, SettingsCustom.class);
                startActivity(settings);
                overridePendingTransition(R.anim.anim, R.anim.anim);
            }
        });

        findViewById(R.id.home_card_expense).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent expenses  = new Intent(context,Expense_Management.class);
               startActivity(expenses);
               overridePendingTransition(R.anim.anim,R.anim.anim);
            }
        });

        findViewById(R.id.home_card_synchronize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomers();
            }
        });
    }


    public void createCustomers()
    {

        final AlertDialog progressbar =   commonClass.createProgressBar(context);

        new ExternalConnectionTest(new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result)
            {
                if(result.contains(Commons.CON_SUCCESS))
                {
                    //Get customers
                    new GetCustomersList(new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {
                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray jsonArray =   jsonObject.getJSONArray(Commons.RESULT);
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                    Customer    customers   =   new Customer(jsonObject1.getString("customer_name"), jsonObject1.getString("contact_person_mobile"), jsonObject1.getString("customer_code"));
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
                                            , Integer.valueOf(jsonObject1.getString("sync"))
                                            , jsonObject1.getString("business_owner")
                                            , jsonObject1.getString("business_owner_number")
                                            , jsonObject1.getString("business_owner_email")
                                            , jsonObject1.getString("business_owner_address"));
                                }
                            }
                            catch (JSONException e)
                            {
                                commonClass.createToaster(context, "Could not parse customer data", Commons.TOASTER_LONG, R.drawable.warning);
                            }
                        }
                    }).execute();


                    new AsyncGetCallObjectives(new AsyncTaskDelegate() {
                        @Override
                        public void getResult(String result)
                        {
                            try
                            {
                                JSONObject  jsonObject  =   new JSONObject(result);
                                JSONArray   jsonArray   =   jsonObject.getJSONArray(Commons.RESULT);
                                db.deleteOutletActivities();
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                    db.createOutletActivities(jsonObject1.getInt("activity_id"), jsonObject1.getString("name"), jsonObject1.getString("startdate"), jsonObject1.getString("enddate"), jsonObject1.getString("createdby"), jsonObject1.getInt("isregion"), jsonObject1.getInt("isteritory"), jsonObject1.getInt("route"), jsonObject1.getInt("outlet"));
                                }
                            }
                            catch (JSONException e)
                            {

                            }
                        }
                    }).execute();


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

                    //Get the routes
                    new GetRoutesAsyncTask(new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {
                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray  jsonArray    =   jsonObject.getJSONArray(Commons.RESULT);
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                    db.deleteRoutes(String.valueOf(jsonObject1.getInt("id")));
                                    db.createRoutes(jsonObject1.getInt("id"), jsonObject1.getString("route_id"), jsonObject1.getString("route_name"), jsonObject1.getString("description"), jsonObject1.getString("territory"), jsonObject1.getString("region"), jsonObject1.getInt("active"));
                                }
                            }
                            catch (JSONException e)
                            {

                            }
                        }
                    }).execute();

            //Get Bins/Location
                    new GetLocaation(new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {
                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray  jsonArray    =   jsonObject.getJSONArray(Commons.RESULT);
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                   db.deleteLocation(String.valueOf(jsonObject1.getInt("id")));
                                   db.createLocation(jsonObject1.getInt("id")
                                           ,jsonObject1.getString("bin_code")
                                           ,jsonObject1.getString("bin_name")
                                           ,jsonObject1.getString("description")
                                           ,jsonObject1.getString("location")
                                           ,jsonObject1.getInt("active"));
                               }
                            }
                            catch(JSONException e)
                            {

                            }
                        }
                    }).execute();

                    //Get journey plans
                    new GetRoutes(new AsyncTaskDelegate()
                    {

                        @Override
                        public void getResult(String result)
                        {
                            if (result != null)
                            {
                                try
                                {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("Journey Plan");

                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                                db.deleteJourneyPlan(object.getInt("plai_id"));
                                                db.createJourneyPlans(object.getInt("plai_id"), object.getString("customer_code"), object.getString("plan_date"), 0, 0);

                                    }

                                }
                                catch (JSONException e)
                                {
                                    commonClass.createToaster(context, "There was an error parsing json data.", Toast.LENGTH_LONG, R.drawable.sad);
                                    //commonClass.createToaster(context,e.getMessage(), Toast.LENGTH_LONG, R.drawable.sad);
                                }
                            }
                        }
                    }).execute();


                    //Get permissions

                    new GetUserPermisions(new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {
                            if(result.contains(Commons.NO_PERMISIONS))
                            {
                                commonClass.createToaster(context, "No permissions have been defined for you. Contact the admin.", Commons.TOASTER_LONG, R.drawable.sad);
                            }
                            else if(result.contains(Commons.RESULT))
                            {
                                try
                                {
                                    JSONObject   jsonObject  =   new JSONObject(result);
                                    JSONArray    jsonArray   =   jsonObject.getJSONArray(Commons.RESULT);
                                    for(int i=0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject1   =   jsonArray.getJSONObject(i);
                                        db.deletePermissions();
                                        db.createPermisions(sharedPrefs.getItem("username")
                                                , jsonObject1.getString("category")
                                                , jsonObject1.getInt("call_objective")
                                                , jsonObject1.getInt("stock_take")
                                                , jsonObject1.getInt("generate_order")
                                                , jsonObject1.getInt("invoice_print")
                                                , jsonObject1.getInt("payment_collection")
                                                , jsonObject1.getInt("machandising")
                                                , jsonObject1.getInt("order_delivery")
                                                , jsonObject1.getInt("asset_tracking")
                                                , jsonObject1.getInt("complete_call")
                                                , jsonObject1.getInt("is_active"));
                                    }
                                }
                                catch (JSONException e)
                                {
                                    commonClass.createToaster(context, "Could not parse json data", Commons.TOASTER_LONG, R.drawable.sad);
                                }
                            }
                            else
                            {
                                commonClass.createToaster(context, "Could not assign permission. Contact the system admin.", Commons.TOASTER_LONG, R.drawable.sad);
                            }
                        }
                    }).execute(sharedPrefs.getItem("username"));

                    //Get customer order headers
                    new GetOrderHeaders(new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {
                            if(result.contains(Commons.RESULT))
                            {
                                try
                                {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);

                                    for(int i=0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        db.deleteSalesOrder(jsonObject1.getString("order_header")); //This will delete the header and all its lines if
                                        if(db.createSalesOrderHeader(jsonObject1.getString("order_header"), jsonObject1.getString("customer_code"), jsonObject1.getString("date_ordered"), Float.valueOf(jsonObject1.getString("order_amount")), jsonObject1.getString("created_by"), jsonObject1.getString("comment"), "") != -1)
                                        {
                                            //Get the lines corresponding to this header
                                            new GetOrdersLines(new AsyncTaskDelegate()
                                            {
                                                @Override
                                                public void getResult(String result)
                                                {
                                                    try
                                                    {
                                                        JSONObject jsonObject = new JSONObject(result);
                                                        JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);
                                                        for(int i=0; i<jsonArray.length(); i++)
                                                        {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            db.createSalesOrderLines(jsonObject1.getString("order_line"), jsonObject1.getString("order_header"), jsonObject1.getString("product_code"), jsonObject1.getString("product_name"), jsonObject1.getString("description"), jsonObject1.getInt("quantity"), Float.valueOf(jsonObject1.getString("unit_price")), Float.valueOf(jsonObject1.getString("line_amount")), Float.valueOf(jsonObject1.getString("unit_cost")), jsonObject1.getString("date"));
                                                        }
                                                    }
                                                    catch (JSONException e)
                                                    {
                                                        //commonClass.createToaster(context, "Could not parse OrderLinesHeader data", Commons.TOASTER_LONG, R.drawable.warning);
                                                    }
                                                }
                                            }).execute(jsonObject1.getString("order_header"), sharedPrefs.getItem("username"));
                                        }
                                        else
                                        {
                                            //Log those headers that could not be inserted
                                            //Toast.makeText(context, "Could not do headers", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                catch (JSONException e)
                                {
                                    //commonClass.createToaster(context, "Could not parse json data.", Commons.TOASTER_LONG, R.drawable.warning);
                                }
                            }
                            else
                            {
                                commonClass.createToaster(context, "No data for sales orders found.", Commons.TOASTER_LONG, R.drawable.sad);
                            }
                        }
                    }).execute(sharedPrefs.getItem("username"));


                    new GetFeedbackTypes(new AsyncTaskDelegate() {
                        @Override
                        public void getResult(String result) {
                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray  jsonArray    =   jsonObject.getJSONArray(Commons.RESULT);

                                db.deleteFeedbackTypes();

                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject  jsonObject1 =   jsonArray.getJSONObject(i);
                                    db.saveFeedbacktypes(jsonObject1.getInt("feedback_id"), jsonObject1.getString("feedback"));
                                }
                            }
                            catch (JSONException e)
                            {

                            }
                        }
                    }).execute();


                    new GetExpenseType(new AsyncTaskDelegate() {
                        @Override
                        public void getResult(String result) {
                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray  jsonArray    =   jsonObject.getJSONArray(Commons.RESULT);

                                db.deleteExpense_Types();

                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject  jsonObject1 =   jsonArray.getJSONObject(i);
                                    db.saveExpenseTypes(jsonObject1.getString("code"), jsonObject1.getString("name"));
                                }
                            }
                            catch (JSONException e)
                            {

                            }
                        }
                    }).execute();

                    //Get all the products
                    new ShareAsyncClass(Commons.URL_GET_PRODUCT_MASTER, new AsyncTaskDelegate()
                    {
                        @Override
                        public void getResult(String result)
                        {

                            try
                            {
                                JSONObject jsonObject   =   new JSONObject(result);
                                JSONArray   jsonArray   =   jsonObject.getJSONArray(Commons.RESULT);
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                    //Insert into device db
                                    db.deleteProduct(jsonObject1.getString("product_code"));
                                    db.createProducts(jsonObject1.getString("product_code")
                                            , jsonObject1.getString("product_name")
                                            , jsonObject1.getString("product_category")
                                            , jsonObject1.getString("product_barcode")
                                            , jsonObject1.getString("product_search_name")
                                            , jsonObject1.getString("product_packaging")
                                            , Float.valueOf(jsonObject1.getString("product_cost"))
                                            , Float.valueOf(jsonObject1.getString("product_price"))
                                            , 0
                                            ,jsonObject1.getString("location"));

                                }

                            }
                            catch (JSONException e)
                            {
                                commonClass.createToaster(context, "Could not parse json data", Commons.TOASTER_LONG, R.drawable.sad);
                            }
                        }
                    }).execute();
                }
                else
                {
                    commonClass.createToaster(context, "Could not connect externally.", Commons.TOASTER_LONG, R.drawable.sad);
                }

                commonClass.destroyDialog(progressbar);
            }
        }).execute();


        new ShareAsyncClass(Commons.URL_GET_UOM, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result)
            {
                try
                {
                    JSONObject  jsonObject  =   new JSONObject(result);
                    JSONArray   jsonArray   =   jsonObject.getJSONArray(Commons.RESULT);
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                        db.createUom(jsonObject1.getString("unit"), jsonObject1.getString("description"));
                    }
                }
                catch (JSONException e)
                {
                    commonClass.createToaster(context, "Cannot parse json data.", Commons.TOASTER_LONG, R.drawable.sad);
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
                        db.deleteAssets(object.getString("asset_no"));
                        db.createAssets(object.getString("asset_no")
                                ,object.getString("asset_name")
                                ,object.getString("onsite")
                                ,object.getString("condition")
                                ,object.getString("reason")
                                ,object.getString("last_service_date")
                                ,object.getString("date")
                                ,object.getString("next_service_date")
                                ,object.getString("comments"));
                    }


                }catch (JSONException e)
                {

                    commonClass.createToaster(context, "Could not parse Assets Data", Commons.TOASTER_LONG, R.drawable.warning);
                    //commonClass.createToaster(context,e.getMessage(),Commons.TOASTER_LONG,R.drawable.warning);
                }


            }
        }).execute();


        //Deliveries
        new OneParamAsyncTask(Commons.URL_GET_TRANSORDERS, new AsyncTaskDelegate()
        {
            @Override
            public void getResult(String result)
            {

                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Commons.RESULT);

                    db.deleteSalesOrders();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        final JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        if(db.GetSalesOrders(jsonObject1.getString("trans_header_id"), jsonObject1.getString("customer_code"), jsonObject1.getString("date"), Float.valueOf(jsonObject1.getString("trans_amount"))) != -1)
                        {
                            new OneParamAsyncTask( Commons.URL_GET_TRANSORDERSLINES, new AsyncTaskDelegate()
                            {
                                @Override
                                public void getResult(String result)
                                {
                                    Log.d("Testing", result);
                                    try
                                    {
                                        JSONObject jsonObject2 = new JSONObject(result);
                                        JSONArray jsonArray1 = jsonObject2.getJSONArray(Commons.RESULT);
                                        db.deleteSalesOrderLines(jsonObject1.getString("trans_header_id"));
                                        for (int j = 0; j < jsonArray1.length(); j++)
                                        {
                                            JSONObject jsonObject3 = jsonArray1.getJSONObject(j);

                                            db.GetSalesOrderLines(jsonObject1.getString("trans_header_id")
                                                    , jsonObject3.getString("product_code")
                                                    , jsonObject3.getString("description")
                                                    , jsonObject3.getInt("quantityOrdered")
                                                    , jsonObject3.getInt("quantityDelivered")
                                                    , Float.valueOf(jsonObject3.getString("unit_price"))
                                                    , Float.valueOf(jsonObject3.getString("line_amount"))
                                                    , jsonObject3.getString("comments"));

                                        }

                                    } catch (JSONException e)
                                    {
                                        commonClass.createToaster(context, "Could not parse Sales order lines data", Commons.TOASTER_LONG, R.drawable.warning);
                                    }

                                }
                            }).execute(jsonObject1.getString("trans_header_id"));
                        }
                    }
                } catch (JSONException e)
                {
                    commonClass.createToaster(context, "Could not parse Sales Order data", Commons.TOASTER_LONG, R.drawable.warning);
                }
            }
        }).execute(sharedPrefs.getItem("username"));


        if(db.checkNotifications())
        {
            ((FloatingActionButton) findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }

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

    @Override
    protected void onResume() {
        super.onResume();

        if(db.checkNotifications())
        {
            ((FloatingActionButton) findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }
        else
        {
            ((FloatingActionButton) findViewById(R.id.home_alert_btn)).setImageResource(R.drawable.notificationclear);
        }
    }
}
