package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SavedSalesOrderInvoiceAndPrint extends AppCompatActivity
{

    DB db;
    Context context;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_sales_order_invoice_and_print);

        context     =   this;
        db          =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        Cursor cursor = db.getSavedOrdersSalesOrderHeaders(sharedPrefs.getItem("customer"));
        if(cursor.getCount()>0)
        {
            List<SalesOrderHeaderForInvoiceAndPrintProperties> salesOrderHeaderForInvoiceAndPrintProperties = new ArrayList<>();
            while(cursor.moveToNext())
            {
                SalesOrderHeaderForInvoiceAndPrintProperties salesOrderHeader
                        =   new SalesOrderHeaderForInvoiceAndPrintProperties(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_NO)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_DATE_AND_TIME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_AMOUNT)));
                salesOrderHeaderForInvoiceAndPrintProperties.add(salesOrderHeader);
            }

            RecyclerView recyclerView   =   (RecyclerView)findViewById(R.id.invoice_and_print_lst);
            SavedSalesOrdersPrintInvoiceAdapter savedSalesOrdersPrintInvoiceAdapter =   new SavedSalesOrdersPrintInvoiceAdapter(context, salesOrderHeaderForInvoiceAndPrintProperties);
            recyclerView.setAdapter(savedSalesOrdersPrintInvoiceAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        }
        else
        {
            Snackbar.make(findViewById(R.id.saved_sales_order_lst), "No saved Sales orders.", Snackbar.LENGTH_LONG).show();
        }

    }
}
