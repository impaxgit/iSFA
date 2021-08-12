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
    ArrayList<OrderLines> orderLineslist =new ArrayList<>();
    ArrayList<OrderLines> orderLines;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_lines);

        context =   this;
        db  =   new DB(context);
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);

        setTitle("Lines for "+sharedPrefs.getItem("OrderId"));

        commonClass.createOrderLines(context, (ExpandableListView) findViewById(R.id.elv_orderLinesHome), sharedPrefs.getItem("OrderId"));
        swipeRefreshLayout    =   findViewById(R.id.sales_order_line_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayout.setRefreshing(true);
                    commonClass.createOrderLines(context, (ExpandableListView) findViewById(R.id.elv_orderLinesHome), sharedPrefs.getItem("OrderId"));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    FloatingActionButton fab=findViewById(R.id.generate_fbaddOrderLines);
    fab.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View view = layoutInflater.inflate(R.layout.new_orderline, null, false);
            final AlertDialog dialog = commonClass.createCustomDialog(context, view);

            ((TextView)view.findViewById(R.id.generateOrder_txtOrderLineNo)).setText(commonClass.getLineId());
            //Load product Names to spinner
            Cursor cursor = db.getProducts();
            if (cursor != null)
            {
                if (cursor.getCount() > 0)
                {
                    String[] codes = new String[cursor.getCount()];
                    String[] description = new String[cursor.getCount()];
                    int i = 0;
                    while (cursor.moveToNext())
                    {
                        codes[i] = cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE));
                        description[i] = cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME));

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
            Cursor uom = db.getUom();
            if (uom != null)
            {
                if (uom.getCount() > 0)
                {
                    String[] codes = new String[uom.getCount()];
                    String[] description = new String[uom.getCount()];
                    int i = 0;
                    while (uom.moveToNext()) {
                        codes[i] = uom.getString(uom.getColumnIndex(Commons.UOM_UNIT));
                        description[i] = uom.getString(uom.getColumnIndex(Commons.UOM_DESCRIPTION));

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
                    String product_code = ((Spinner) view.findViewById(R.id.generate_txtProductName)).getSelectedItem().toString().trim();
                    String uom = ((Spinner) view.findViewById(R.id.generate_txtUoM)).getSelectedItem().toString().trim();
                    String qty = ((EditText) view.findViewById(R.id.generate_txtQty)).getText().toString().trim();

                    if (!orderLine_No.isEmpty())
                    {
                        if (!product_code.isEmpty())
                        {
                            if (!uom.isEmpty())
                            {
                                if (!qty.isEmpty())
                                {
                                    if(db.createSalesOrderLines(
                                              orderLine_No
                                            , sharedPrefs.getItem("OrderId")
                                            , product_code
                                            , db.getProductName(product_code)
                                            , db.getProductName(product_code)
                                            , Integer.valueOf(qty)
                                            , db.getProductUnitPrice(product_code)
                                            , db.getProductUnitPrice(product_code)*Integer.valueOf(qty)
                                            , db.getProductUnitCost(product_code)
                                            , commonClass.getCurrentDate()) != -1)
                                    {
                                        //Update the header with this amount
                                        db.updateSalesOrderHeaderAmount(sharedPrefs.getItem("OrderId"), db.getProductUnitPrice(product_code)*Integer.valueOf(qty));

                                        commonClass.createToaster(context, "Line item for "+sharedPrefs.getItem("OrderId")+" has been created.", Commons.TOASTER_LONG, R.drawable.smile);
                                        swipeRefreshLayout.post(new Runnable() //Refresh the list so it can pick the current entry
                                        {
                                            @Override
                                            public void run()
                                            {
                                                swipeRefreshLayout.setRefreshing(true);
                                                    commonClass.createOrderLines(context, (ExpandableListView) findViewById(R.id.elv_orderLinesHome), sharedPrefs.getItem("OrderId"));
                                                swipeRefreshLayout.setRefreshing(false);
                                            }
                                        });
                                        commonClass.destroyDialog(dialog);
                                    }
                                    else
                                    {
                                        commonClass.createToaster(context, "Could not create line item, try again.\nIf this persists close the dialog and open again.", Commons.TOASTER_LONG, R.drawable.sad);
                                        commonClass.destroyDialog(dialog);
                                    }
                                }
                                else
                                {
                                    ((EditText) view.findViewById(R.id.generate_txtQty)).setError("Quantity please?");
                                    (view.findViewById(R.id.generate_txtQty)).requestFocus();
                                }
                            }
                            else
                            {
                                (view.findViewById(R.id.generate_txtUoM)).performClick();
                            }
                        }
                        else
                        {
                            (view.findViewById(R.id.generate_txtProductName)).performClick();
                        }
                    }
                    else
                    {
                        commonClass.createToaster(context, "No line number was created. Close the dialog and open it again.", Commons.TOASTER_LONG, R.drawable.sad);
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
}
