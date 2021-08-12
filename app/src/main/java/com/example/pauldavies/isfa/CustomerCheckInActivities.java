package com.example.pauldavies.isfa;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustomerCheckInActivities extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_check_in_activities);

        this.setTitle("Outlet Activities");

        context =   this;
        db  =   new DB(context);
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);

        findViewById(R.id.customer_call_objective).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =   new Intent(context, OutletActivities.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_stock_take).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             Intent intent  = new Intent(context, Customer_StockTake.class);
             startActivity(intent);
             overridePendingTransition(R.anim.to_the_left,R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_delivery).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent   =   new Intent(context, CustomerOrderDelivery.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_invoice_and_print).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent   =   new Intent(context, SavedSalesOrderInvoiceAndPrint.class);
                Bundle bundle   = ActivityOptions.makeCustomAnimation(context, R.anim.to_the_left, R.anim.to_the_right).toBundle();
                startActivity(intent, bundle);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_generate_order).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPrefs.putItem("activity", "generate_order");
                Intent intent   =   new Intent(context, GenerateCustomerOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_asset).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(context, Customer_Asset_Tracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left,R.anim.to_the_right);
            }
        });

        findViewById(R.id.customer_check_in_call_end).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
                alertDialog.setIcon(R.drawable.warning);
                alertDialog.setTitle("Completing Customer Call");
                alertDialog.setMessage("You are about to complete your check in to customer "+sharedPrefs.getItem("customer_name")+".\nConfirm the action.");

                alertDialog.setNegativeButton("No, Not Ready Yet", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing
                        dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("Yes, complete Call", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(db.updateCustomerCheckActivities(sharedPrefs.getItem("customer"), Commons.CUSTOMER_ACTIVITY_COMPLETE_CALL, 1) != -1)
                        {
                            if(db.closeCustomerActivitiesCheckin() != -1) //Deactivate the controls because the call has been ended.
                            {
                                commonClass.createToaster(context, "Call completed successfully.", Commons.TOASTER_LONG, R.drawable.smile);
                                dialog.dismiss();
                                findViewById(R.id.customer_check_in_call_end).setEnabled(false);
                                findViewById(R.id.customer_check_in_call_end).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_merchandise).setEnabled(false);
                                findViewById(R.id.customer_check_in_merchandise).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_call_objective).setEnabled(false);
                                findViewById(R.id.customer_call_objective).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_stock_take).setEnabled(false);
                                findViewById(R.id.customer_check_in_stock_take).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_generate_order).setEnabled(false);
                                findViewById(R.id.customer_check_in_generate_order).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_invoice_and_print).setEnabled(false);
                                findViewById(R.id.customer_check_in_invoice_and_print).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_delivery).setEnabled(false);
                                findViewById(R.id.customer_check_in_delivery).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_payment_collection).setEnabled(false);
                                findViewById(R.id.customer_check_in_payment_collection).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_merchandise).setEnabled(false);
                                findViewById(R.id.customer_check_in_merchandise).setBackgroundColor(Color.DKGRAY);
                                findViewById(R.id.customer_check_in_asset).setEnabled(false);
                                findViewById(R.id.customer_check_in_asset).setBackgroundColor(Color.DKGRAY);
                            }
                        }
                        else
                        {
                            commonClass.createToaster(context, "Could not complete this call. Try again and if this persists close the form and open again.", Commons.TOASTER_LONG, R.drawable.sad);
                            dialog.dismiss();
                            findViewById(R.id.customer_check_in_call_end).setEnabled(true);
                            findViewById(R.id.customer_check_in_merchandise).setEnabled(true);
                            findViewById(R.id.customer_call_objective).setEnabled(true);
                            findViewById(R.id.customer_check_in_stock_take).setEnabled(true);
                            findViewById(R.id.customer_check_in_generate_order).setEnabled(true);
                            findViewById(R.id.customer_check_in_invoice_and_print).setEnabled(true);
                            findViewById(R.id.customer_check_in_delivery).setEnabled(true);
                            findViewById(R.id.customer_check_in_payment_collection).setEnabled(true);
                            findViewById(R.id.customer_check_in_merchandise).setEnabled(true);
                            findViewById(R.id.customer_check_in_asset).setEnabled(true);
                        }
                    }
                });

                Dialog dialog   =   alertDialog.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
    }
}
