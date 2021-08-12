package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

public class CustomerList extends AppCompatActivity implements AsyncTaskDelegate
{
    Context context;
    CommonClass commonClass;
    ArrayList<Customer> customer;
    DB db;
    int lastExpandedPosition    =   -1;
    String customer_route   =   "";
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        context =   this;
        commonClass =   new CommonClass();
        this.setTitle("iSFA::Customers");
        db  =   new DB(context);

        fragmentManager =   getSupportFragmentManager();

        Spinner spinner =   findViewById(R.id.customer_sp_route);

        commonClass.createCustomers(context, (ExpandableListView)findViewById(R.id.customers_list), customer_route);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                customer_route  =   parent.getSelectedItem().toString().trim();
                commonClass.createCustomers(context, (ExpandableListView)findViewById(R.id.customers_list), customer_route);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final SwipeRefreshLayout swipeRefreshLayout   =   findViewById(R.id.customer_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                commonClass.createCustomers(context, (ExpandableListView)findViewById(R.id.customers_list), customer_route);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Cursor routes_cursor =   db.getRoutes();
        if(routes_cursor.getCount()>0)
        {
            String[] code           =   new String[routes_cursor.getCount()+1];
            String[] description    =   new String[routes_cursor.getCount()+1];
            int counter=0;
            code[0] =   "";
            description[0]  =   "";
            counter++;
            while(routes_cursor.moveToNext())
            {
                code[counter]   =   routes_cursor.getString(routes_cursor.getColumnIndex(Commons.ROUTE_CODE));
                description[counter]    =   routes_cursor.getString(routes_cursor.getColumnIndex(Commons.ROUTE_DESCRIPTION));

                counter++;
            }

            SpinnerTwoColumnAdapter spinnerTwoColumnAdapter =   new SpinnerTwoColumnAdapter(context, code, description);
            spinner.setAdapter(spinnerTwoColumnAdapter);
            spinnerTwoColumnAdapter.setDropDownViewResource(R.layout.spinner_two_column);
        }


        ((ExpandableListView) findViewById(R.id.customers_list)).setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            @Override
            public void onGroupExpand(int groupPosition)
            {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                {
                    ((ExpandableListView) findViewById(R.id.customers_list)).collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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
                        Spinner spinner =   view.findViewById(R.id.new_customer_rote);
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
                        String customer_code    =   commonClass.getCustomerId(route);
                        String customer_geo_cod =   "SJDFKJ"; //Should be captured at the point of saving then replace with this dummy
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
                                                        if(db.createCustomer(customer_code, outlet_name, description, outlet_type, route, customer_geo_cod, cust_created_on, customer_type, 0, customer_email, customer_mobile, contact_name, contact_email, contact_mobile, 0, 0, 0, 0, owner_name, owner_number, owner_email, owner_postal))
                                                        {
                                                            Snackbar.make(findViewById(R.id.customers_list), "Customer record saved successfully!", Snackbar.LENGTH_LONG).show();
                                                            new_customer_dialog.dismiss();
                                                        }
                                                        else
                                                        {
                                                            Snackbar.make(findViewById(R.id.customers_list), "Could not save the record.\nTry again!", Snackbar.LENGTH_LONG).show();
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
                commonClass.createCustomersSearchUnApproved(context, (ExpandableListView)findViewById(R.id.customers_list));
                break;
            case R.id.customer_search:
                    fragmentManager.beginTransaction()
                            .replace(R.id.search_fragment, new customer_search())
                            .addToBackStack(null)
                            .commit();
                break;
            default:
                item.setIcon(R.drawable.notificationalert);
                commonClass.createToaster(context, "Unknown action", Commons.TOASTER_LONG, R.drawable.cross);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(String result)
    {
        commonClass.createCustomersSearch(context, (ExpandableListView)findViewById(R.id.customers_list), result);
    }
}
