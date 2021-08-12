package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class Payment extends AppCompatActivity
{

    Context context;
    CommonClass commonClass;
    DB db;
    ArrayList<OrderPayment> orderPayments;
    String productCode,productName,Qty, Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        setTitle("Order Payment ");
        context = this;

        context = this;
        db = new DB(context);
        commonClass = new CommonClass();
//        commonClass.orderPayment(context,(RecyclerView) findViewById(R.id.orders_rvPayment),productCode,productName,Qty,Amount);

        FloatingActionButton pay = findViewById(R.id.payment_fbPay);
        pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                  LayoutInflater inflater=Payment.this.getLayoutInflater();
                final View view   =   inflater.inflate(R.layout.receipt, null, false);
                final AlertDialog payment =   commonClass.createCustomDialog(context, view);

                view.findViewById(R.id.receipt_btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payment.dismiss();
                    }
                });

                view.findViewById(R.id.receipt_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payment.dismiss();
                    }
                });
                payment.show();
            }
        });

        FloatingActionButton cancel =findViewById(R.id.Payment_fbCancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}
