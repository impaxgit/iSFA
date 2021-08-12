package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Calendar;

public class NewOutlet extends AppCompatActivity
{

    CommonClass commonClass;
    DB db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_customer);

        this.setTitle("New Outlet");

        context = this;
        db      =   new DB(context);
        commonClass =   new CommonClass();



        findViewById(R.id.new_customer_close_btn).setVisibility(View.GONE);

        findViewById(R.id.textView15).setBackgroundColor(Color.parseColor("#8f0611"));

        RelativeLayout forButton = new RelativeLayout(context);
        RelativeLayout.LayoutParams forButtonParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forButton.setGravity(Gravity.CENTER);

        findViewById(R.id.new_customer_save_btn).setLayoutParams(forButtonParams);
        findViewById(R.id.new_customer_save_btn).setBackgroundColor(Color.parseColor("#8f0611"));

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
                Spinner spinner =   findViewById(R.id.new_customer_rote);
                spinner.setAdapter(spinnerTwoColumnAdapter);
                spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
        }

        findViewById(R.id.new_customer_save_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String outlet_name      =   ((EditText)findViewById(R.id.customer_new_outletname)).getText().toString().trim();
                String owner_name       =   ((EditText)findViewById(R.id.new_customer_owner_namw)).getText().toString().trim();
                String owner_number     =   ((EditText)findViewById(R.id.new_customer_mobile_number)).getText().toString().trim();
                String owner_email      =   ((EditText)findViewById(R.id.new_customer_email)).getText().toString().trim();
                String owner_postal     =   ((EditText)findViewById(R.id.new_customer_postal_address)).getText().toString().trim();

                String contact_name     =   ((EditText)findViewById(R.id.new_customer_contact_name)).getText().toString().trim();
                String contact_mobile   =   ((EditText)findViewById(R.id.new_customer_contact_number)).getText().toString().trim();
                String contact_email    =   ((EditText)findViewById(R.id.new_customer_contact_email)).getText().toString().trim();
                String description      =   ((EditText)findViewById(R.id.new_customer_txt_description)).getText().toString().trim();
                String route            =   ((Spinner)findViewById(R.id.new_customer_rote)).getSelectedItem().toString().trim();
                String outlet_type      =   ((Spinner)findViewById(R.id.customer_new_outlet_type)).getSelectedItem().toString().trim();
                String customer_type    =   ((Spinner)findViewById(R.id.new_customer_outlet_type)).getSelectedItem().toString().trim();
                String customer_email   =   ((EditText)findViewById(R.id.new_customer_email_business)).getText().toString().trim();
                String customer_mobile  =   ((EditText)findViewById(R.id.new_customer_business_number)).getText().toString().trim();
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
                                                    Snackbar.make(findViewById(R.id.new_customer_contact_number), "Customer record saved successfully!", Snackbar.LENGTH_LONG).show();
                                                    finish();
                                                }
                                                else
                                                {
                                                    Snackbar.make(findViewById(R.id.new_customer_contact_number), "Could not save the record.\nTry again!", Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                            else
                                            {
                                                ((EditText)findViewById(R.id.new_customer_contact_number)).setError("Contact number?");
                                                findViewById(R.id.new_customer_contact_number).requestFocus();
                                            }
                                        }
                                        else
                                        {
                                            ((EditText)findViewById(R.id.new_customer_contact_name)).setError("Contact name?");
                                            findViewById(R.id.new_customer_contact_name).requestFocus();
                                        }
                                    }
                                    else
                                    {
                                        ((EditText)findViewById(R.id.new_customer_business_number)).setError("Owner number?");
                                        findViewById(R.id.new_customer_business_number).requestFocus();
                                    }
                                }
                                else
                                {
                                    ((EditText)findViewById(R.id.new_customer_owner_namw)).setError("Owner name?");
                                    findViewById(R.id.new_customer_owner_namw).requestFocus();
                                }
                            }
                            else
                            {
                                findViewById(R.id.new_customer_outlet_type).performClick();
                            }
                        }
                        else
                        {
                            findViewById(R.id.customer_new_outlet_type).performClick();
                        }
                    }
                    else
                    {
                        ((EditText)findViewById(R.id.customer_new_outletname)).setError("Outlet name?");
                        findViewById(R.id.customer_new_outletname).requestFocus();
                    }
                }
                else
                {
                    findViewById(R.id.new_customer_rote).performClick();
                }
            }
        });
    }
}
