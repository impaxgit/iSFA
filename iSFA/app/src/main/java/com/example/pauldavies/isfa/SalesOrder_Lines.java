package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SalesOrder_Lines extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_lines);
        setTitle("Order Lines");

        context =   this;
        db  =   new DB(context);
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
       Toast.makeText(context, sharedPrefs.getItem("OrderId"), Toast.LENGTH_LONG).show();

        commonClass.createOrderLines(context, (ExpandableListView) findViewById(R.id.elv_orderLinesHome));
        refreshlist();

    FloatingActionButton fab=findViewById(R.id.generate_fbaddOrderLines);
    fab.setOnClickListener(new View.OnClickListener()
    {
    @Override
    public void onClick(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.new_orderline, null, false);
        final AlertDialog dialog = commonClass.createCustomDialog(context, view);

        //Load product Names to spinner
        Cursor cursor = db.getOrderLines();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                String[] codes = new String[cursor.getCount()];
                String[] description = new String[cursor.getCount()];
                int i = 0;
                while (cursor.moveToNext()) {
                    codes[i] = cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE));
                    description[i] = cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME));

                    i++;
                }
                // fill adapter
                SpinnerTwoColumnAdapter spinnerTwoColumnAdapter = new SpinnerTwoColumnAdapter(context, codes, description);
                Spinner spinner = (Spinner) view.findViewById(R.id.generate_txtProductName);
                spinner.setAdapter(spinnerTwoColumnAdapter);
                spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
        }

        //Load item Description
        Cursor uom = db.getOrderLines();
        if (uom != null) {
            if (uom.getCount() > 0) {
                String[] codes = new String[uom.getCount()];
                String[] description = new String[uom.getCount()];
                int i = 0;
                while (uom.moveToNext()) {
                    codes[i] = uom.getString(uom.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE));
                    description[i] = uom.getString(uom.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION));

                    i++;
                }
                // fill adapter
                SpinnerTwoColumnAdapter spinnerTwoColumnAdapter = new SpinnerTwoColumnAdapter(context, codes, description);
                Spinner spinner = (Spinner) view.findViewById(R.id.generate_txtUoM);
                spinner.setAdapter(spinnerTwoColumnAdapter);
                spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
        }
        view.findViewById(R.id.generate_btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderLine_No = ((TextView) view.findViewById(R.id.generateOrder_txtOrderLineNo)).getText().toString().trim();
                String product_name = ((Spinner) view.findViewById(R.id.generate_txtProductName)).getSelectedItem().toString().trim();
                String uom = ((Spinner) view.findViewById(R.id.generate_txtUoM)).getSelectedItem().toString().trim();
                String qty = ((EditText) view.findViewById(R.id.generate_txtQty)).getText().toString().trim();
                String date = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                if (!orderLine_No.isEmpty()) {

                    if (!product_name.isEmpty()) {
                            if (!uom.isEmpty()) {
                                if (!qty.isEmpty()) {
                                            if (!date.isEmpty()) {
                                              if (db.createOrderLines(0,"",orderLine_No, "", product_name, uom, Float.valueOf(0), Integer.valueOf(qty), Float.valueOf(0), date)!=-1)
                                                {
                                                    Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                }
                            }

            }
        });

        view.findViewById(R.id.generate_btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.destroyDialog(dialog);
            }
        });
    }

    });
    }

    private void refreshlist()
    {
        final SwipeRefreshLayout swipeRefreshLayout   =   (SwipeRefreshLayout)findViewById(R.id.orderLines_wipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                commonClass.createOrderLines(context, ((ExpandableListView)findViewById(R.id.elv_orderLinesHome)));

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
