package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

public class CustomerList extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    ArrayList<Customer> customer;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        context =   this;
        commonClass =   new CommonClass();
        this.setTitle("iSFA::Customers");
        db  =   new DB(context);

        commonClass.createCustomers(context, (ExpandableListView)findViewById(R.id.customers_list));
        refreshList();

    }

    public void refreshList()
    {
        final SwipeRefreshLayout swipeRefreshLayout   =   (SwipeRefreshLayout)findViewById(R.id.customer_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.createCustomers(context, ((ExpandableListView)findViewById(R.id.customers_list)));

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater findMenuItem   =   getMenuInflater();
        findMenuItem.inflate(R.menu.customer_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.new_customer:
                LayoutInflater layoutInflater   =   this.getLayoutInflater();
                final View view   =   layoutInflater.inflate(R.layout.new_customer, null, false);

                final AlertDialog new_customer_dialog =   commonClass.createCustomDialog(context, view);

                //Load routes in the spinner
                Cursor cursor   =   db.getRoutes();
                if(cursor != null)
                {
                    if(cursor.getCount()>0)
                    {
                        String[] codes  =   new String[cursor.getCount()];
                        String[] description=new String[cursor.getCount()];
                        int i=0;
                        while(cursor.moveToNext())
                        {
                            codes[i]    =   cursor.getString(cursor.getColumnIndex(Commons.ROUTE_CODE));
                            if(!cursor.getString(cursor.getColumnIndex(Commons.ROUTE_NAME)).trim().isEmpty()) //If name is not provided then use description instead
                            {
                                description[i]=cursor.getString(cursor.getColumnIndex(Commons.ROUTE_NAME));
                            }
                            else
                            {
                                description[i]=cursor.getString(cursor.getColumnIndex(Commons.ROUTE_DESCRIPTION));
                            }

                            i++;

                        }

                        SpinnerTwoColumnAdapter spinnerTwoColumnAdapter =   new SpinnerTwoColumnAdapter(context, codes, description);
                        Spinner spinner =   (Spinner) view.findViewById(R.id.new_customer_rote);
                        spinner.setAdapter(spinnerTwoColumnAdapter);
                        spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }
                }
                //Close the form
                view.findViewById(R.id.new_customer_close_btn).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        commonClass.destroyDialog(new_customer_dialog);
                    }
                });
                //Validations and data capture and saving
                view.findViewById(R.id.new_customer_save_btn).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String outlet_name      =   ((EditText)view.findViewById(R.id.customer_new_outletname)).getText().toString().trim();
                        String owner_name       =   ((EditText)view.findViewById(R.id.new_customer_owner_namw)).getText().toString().trim();
                        String owner_number     =   ((EditText)view.findViewById(R.id.new_customer_mobile_number)).getText().toString().trim();
                        String owner_email      =   ((EditText)view.findViewById(R.id.new_customer_email)).getText().toString().trim();
                        String owner_postal     =   ((EditText)view.findViewById(R.id.new_customer_postal_address)).getText().toString().trim();

                        String contact_name     =   ((EditText)view.findViewById(R.id.new_customer_contact_name)).getText().toString().trim();
                        String contact_mobile   =   ((EditText)view.findViewById(R.id.new_customer_contact_number)).getText().toString().trim();
                        String contact_email    =   ((EditText)view.findViewById(R.id.new_customer_contact_email)).getText().toString().trim();
                        String description      =   ((EditText)view.findViewById(R.id.new_customer_txt_description)).getText().toString().trim();
                        String route            =   ((Spinner)view.findViewById(R.id.new_customer_rote)).getSelectedItem().toString().trim();
                        String outlet_type      =   ((Spinner)view.findViewById(R.id.customer_new_outlet_type)).getSelectedItem().toString().trim();
                        String customer_type    =   ((Spinner)view.findViewById(R.id.new_customer_outlet_type)).getSelectedItem().toString().trim();
                        String customer_email   =   ((EditText)view.findViewById(R.id.new_customer_email_business)).getText().toString().trim();
                        String customer_mobile  =   ((EditText)view.findViewById(R.id.new_customer_business_number)).getText().toString().trim();
                        String customer_code    =   "D0001";
                        String customer_geo_cod =   "SJDFKJ";
                        String cust_created_on  =   String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                        if(!route.isEmpty())
                        {
                            if(!outlet_name.isEmpty())
                            {
                                if(!outlet_type.isEmpty())
                                {
                                    if(!customer_type.isEmpty())
                                    {
                                        if(!owner_name.isEmpty())
                                        {
                                            if(!customer_mobile.isEmpty())
                                            {
                                                if(!contact_name.isEmpty())
                                                {
                                                    if(!contact_mobile.isEmpty())
                                                    {
                                                        //Save the customer record.
                                                        if(db.createCustomer(customer_code, outlet_name, description, outlet_type, route, customer_geo_cod, cust_created_on, customer_type, 0, customer_email, customer_mobile, contact_name, contact_email, contact_mobile, 0, 0, 0, 0)!=-1)
                                                        {
                                                            commonClass.createToaster(context, "Customer record saved successfully.", Toast.LENGTH_SHORT, R.drawable.smile);
                                                            ((EditText)view.findViewById(R.id.customer_new_outletname)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_owner_namw)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_mobile_number)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_email)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_postal_address)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_contact_name)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_contact_number)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_contact_email)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_txt_description)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_email_business)).setText("");
                                                            ((EditText)view.findViewById(R.id.new_customer_business_number)).setText("");
                                                        }
                                                        else
                                                        {
                                                            commonClass.createToaster(context, "Could not save the record.\nTry again!", Toast.LENGTH_LONG, R.drawable.sad);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        ((EditText)view.findViewById(R.id.new_customer_contact_number)).setError("Contact number?");
                                                        (view.findViewById(R.id.new_customer_contact_number)).requestFocus();
                                                    }
                                                }
                                                else
                                                {
                                                    ((EditText)view.findViewById(R.id.new_customer_contact_name)).setError("Contact name?");
                                                    (view.findViewById(R.id.new_customer_contact_name)).requestFocus();
                                                }
                                            }
                                            else
                                            {
                                                ((EditText)view.findViewById(R.id.new_customer_business_number)).setError("Owner number?");
                                                (view.findViewById(R.id.new_customer_business_number)).requestFocus();
                                            }
                                        }
                                        else
                                        {
                                            ((EditText)view.findViewById(R.id.new_customer_owner_namw)).setError("Owner name?");
                                            (view.findViewById(R.id.new_customer_owner_namw)).requestFocus();
                                        }
                                    }
                                    else
                                    {
                                        (view.findViewById(R.id.new_customer_outlet_type)).performClick();
                                    }
                                }
                                else
                                {
                                    (view.findViewById(R.id.customer_new_outlet_type)).performClick();
                                }
                            }
                            else
                            {
                                ((EditText)view.findViewById(R.id.customer_new_outletname)).setError("Outlet name?");
                                (view.findViewById(R.id.customer_new_outletname)).requestFocus();
                            }
                        }
                        else
                        {
                            (view.findViewById(R.id.new_customer_rote)).performClick();
                        }
                    }
                });

                break;
            case R.id.today_customer:
                commonClass.createToaster(context, "Today customers", Commons.TOASTER_LONG, R.drawable.smile);
                break;
            case R.id.customer_search:
                commonClass.createToaster(context, "Search customer", Commons.TOASTER_LONG, R.drawable.customer);
                break;
            default:
                item.setIcon(R.drawable.notificationalert);
                commonClass.createToaster(context, "Unknown action", Commons.TOASTER_LONG, R.drawable.cross);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
