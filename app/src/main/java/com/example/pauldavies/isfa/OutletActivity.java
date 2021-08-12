package com.example.pauldavies.isfa;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OutletActivity extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);

        context=this;
        commonClass =   new CommonClass();
        db  =   new DB(context);

        findViewById(R.id.mark_customer_activity_complete).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
                alertDialog.setIcon(R.drawable.mywarning_24dp);
                alertDialog.setTitle("Check In Activity Closure");
                alertDialog.setMessage("Confirm that you are done with the customer activities.\nPlease note that after the confirmation this process won't be available today.");
                Dialog dialog;
                alertDialog.setNegativeButton("No, Don't Close", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("Yes, I am Done", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(db.closeCustomerActivitiesCheckin() != -1)
                        {
                            finish();
                        }
                        else
                        {
                            commonClass.createToaster(context, "Could not close this customer's check in activities. Try again.", Commons.TOASTER_LONG, R.drawable.sad);
                        }
                    }
                });

                dialog  =   alertDialog.create();
                dialog.show();
            }
        });
    }
}
