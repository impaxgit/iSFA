package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_LONG;

public class Fragment_All_Customers extends Fragment
{
    View view;
    ExpandableListView expandableListView;
    Context context;
    CommonClass commonClass;
    DB db;

    public Fragment_All_Customers() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)
    {
        context   =   this.getContext();
        commonClass =   new CommonClass();
        db  =   new DB(context);

        view = inflater.inflate(R.layout.fragment_fragment__all__customers, container, false);

        expandableListView = view.findViewById(R.id.elv_customers);

        commonClass.createCustomers(context, expandableListView, "");


        view.findViewById(R.id.plan_new_customer).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view   =   layoutInflater.inflate(R.layout.new_customer, null, false);
                final AlertDialog dialog  =   commonClass.createCustomDialog(context, view);

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
                        commonClass.destroyDialog(dialog);
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
                                                        if(db.createCustomer(customer_code, outlet_name, description, outlet_type, route, customer_geo_cod, cust_created_on, customer_type, 0, customer_email, customer_mobile, contact_name, contact_email, contact_mobile, 0, 0, 0, 0, owner_name, owner_number, owner_email, owner_postal))
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

            }
        });

        return view;
    }
}

