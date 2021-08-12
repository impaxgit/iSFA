package com.example.pauldavies.isfa;

/*
* This class is meant to help the user collect orders from customers.
* It generates a list of items with details and allows the user to enter the quantity of goods expected for delivery.
* It uses a temporary table to store information while the order is being taken.
* It only holds the info for today's orders.
* Upon saving the order, the entries in the temporary table are all deleted in readiness for next order regarding this customer.
* It however allows taking orders from more than one customer before closing the another's*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.print.PrintManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GenerateCustomerOrder extends AppCompatActivity
{

    ArrayList<ClientSalesOrder> clientSalesOrders;
    RecyclerView recyclerView;
    GenerateCustomerOrderAdapter generateCustomerOrderAdapter;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;
    CommonClass commonClass;
    public static String order_note="";
    String sales_order_header   =   "";
    DatePicker datePicker;
    String action="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_customer_order);

        this.setTitle("New/Edit Sales Order");

        context =   this;
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass =   new CommonClass();

        datePicker   =   (DatePicker)findViewById(R.id.new_sales_order_delivery_date);

        /*Create the order header code; only if it is not an edit from other sources the */
        if(!sharedPrefs.getItem("sales_head_code_edit").trim().isEmpty())
        {
            action  =   "edit";
            sales_order_header  = sharedPrefs.getItem("sales_head_code_edit");
        }
        else
        {
            action="";
            sales_order_header   =   commonClass.getOrderId(sharedPrefs.getItem("customer"));
        }

        clientSalesOrders   =   new ArrayList<>();
        recyclerView    =   findViewById(R.id.new_cusromer_order_generation);
        generateCustomerOrderAdapter    =   new GenerateCustomerOrderAdapter(context, clientSalesOrders, recyclerView, (TextView)findViewById(R.id.stock_take_totals));
        recyclerView.setAdapter(generateCustomerOrderAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Set the minimum date to today to prevent delivery of order in the past
        ((DatePicker)findViewById(R.id.new_sales_order_delivery_date)).setMinDate(System.currentTimeMillis());

        //Get products in preparation for the SO, insert into the temp table then populate the list grid for order taking;
        //This is only done only is there is not entry for this customer that has been done for today.
        //No previous records should be maintained this table but only records for today.
        Cursor cursor   =   db.getProductsForSalesOrderTakingIntoTemp();
        if(cursor.getCount()>0)
        {
            db.deleteSalesOrderTemp(sharedPrefs.getItem("customer")); //Delete the previously dated records
            while(cursor.moveToNext())
            {
                Cursor check_cursor =   db.checkItemExist(sharedPrefs.getItem("customer"), cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)));
                if(check_cursor.getCount()>0)
                {
                    check_cursor.moveToFirst();
                    if(check_cursor.getInt(check_cursor.getColumnIndex(Commons.SO_QTY))==0)
                    {
                        db.deleteSalesOrderTemp(sharedPrefs.getItem("customer"), check_cursor.getString(check_cursor.getColumnIndex(Commons.SO_SKU_CODE)));
                        db.createSalesOrder_Temp(sharedPrefs.getItem("customer"), cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)), cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME)), cursor.getInt(cursor.getColumnIndex(Commons.PRODUCT_COST)), 0, commonClass.getCurrentDate());

                        if(action.equals("edit"))
                        {
                            //Check if it is an edit and update the item quantities from the sales order
                            Cursor cursor1  =   db.getOrderLines(sales_order_header, check_cursor.getString(check_cursor.getColumnIndex(Commons.SO_SKU_CODE)));
                            if(cursor1.getCount()>0)
                            {
                                cursor1.moveToFirst();
                                db.updateSalesTemp(cursor1.getString(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE)), cursor1.getInt(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)), cursor1.getFloat(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)));
                            }
                        }
                    }
                }
                else
                {
                    db.createSalesOrder_Temp(sharedPrefs.getItem("customer"), cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)), cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME)), cursor.getInt(cursor.getColumnIndex(Commons.PRODUCT_COST)), 0, commonClass.getCurrentDate());

                    if(action.equals("edit"))
                    {
                        //Check if it is an edit and update the item quantities from the sales order
                        Cursor cursor1  =   db.getOrderLines(sales_order_header, cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)));
                        if(cursor1.getCount()>0)
                        {
                            cursor1.moveToFirst();
                            db.updateSalesTemp(cursor1.getString(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE)), cursor1.getInt(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)), cursor1.getFloat(cursor1.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)));
                        }
                    }
                }
            }
        }

        Cursor  cursor1 =   db.getProductsOntoGripList(sharedPrefs.getItem("customer"));
        if(cursor1.getCount()>0)
        {
            while(cursor1.moveToNext())
            {
                ClientSalesOrder    clientSalesOrder    =
                        new ClientSalesOrder(
                                 cursor1.getString(cursor1.getColumnIndex(Commons.SO_CUSTOMER_CODE))
                                ,cursor1.getString(cursor1.getColumnIndex(Commons.SO_SKU_CODE))
                                ,cursor1.getString(cursor1.getColumnIndex(Commons.SO_SKU_NAME))
                                ,cursor1.getString(cursor1.getColumnIndex(Commons.SO_UNIT_PRICE))
                                ,cursor1.getString(cursor1.getColumnIndex(Commons.SO_LINE_AMOUNT))
                                ,cursor1.getString(cursor1.getColumnIndex(Commons.SO_QTY)));

                clientSalesOrders.add(clientSalesOrder);
            }
        }

        generateCustomerOrderAdapter.notifyDataSetChanged();

        //Update the total field
        ((TextView)findViewById(R.id.stock_take_totals)).setText(commonClass.getCurrencyFormat(db.getSaesOrderSum(sharedPrefs.getItem("customer"))));

        findViewById(R.id.view_sales_order).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<PreviewCustomerCurrentSalesOrder> previewCustomerCurrentSalesOrders    =   new ArrayList<>();

                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view   =   layoutInflater.inflate(R.layout.sales_order_preview, null, false);

                view.findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Check inf there exists a sales order with the header number and alert the user
                        Cursor cursor2  =   db.getOrdersHeaders(sharedPrefs.getItem("customer"), sales_order_header);
                        if(cursor2.getCount()>0)
                        {
                            AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
                            alertDialog.setIcon(R.drawable.mywarning_24dp);
                            alertDialog.setTitle("Duplication Alert");
                            alertDialog.setMessage("The sales order had been saved.\nWould you like to overwrite?");
                            Dialog dialog;
                            alertDialog.setNegativeButton("No, Close", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            }).setPositiveButton("Yes, Overwrite", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                    db.deleteSalesOrder(sales_order_header);
                                    saveSalesOrder(1);
                                }
                            });
                            dialog  =   alertDialog.create();
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                        else
                        {
                            saveSalesOrder(1);
                        }
                    }
                });

                view.findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottomSheetDialog.cancel();
                    }
                });


                Cursor preview_cursor   =   db.getSalesOrderForPreview(sharedPrefs.getItem("customer"));
                if(preview_cursor.getCount()>0)
                {
                    if(!sales_order_header.trim().isEmpty())
                    {
                        ((TextView)view.findViewById(R.id.order_preview_header_code)).setText("Sales Order::"+sales_order_header);
                    }

                    while(preview_cursor.moveToNext())
                    {
                        PreviewCustomerCurrentSalesOrder    previewCustomerCurrentSalesOrder    =   new PreviewCustomerCurrentSalesOrder(preview_cursor.getString(preview_cursor.getColumnIndex(Commons.SO_SKU_CODE)), preview_cursor.getString(preview_cursor.getColumnIndex(Commons.SO_SKU_NAME)), String.valueOf(preview_cursor.getInt(preview_cursor.getColumnIndex(Commons.SO_QTY))), String.valueOf(preview_cursor.getFloat(preview_cursor.getColumnIndex(Commons.SO_LINE_AMOUNT))));
                        previewCustomerCurrentSalesOrders.add(previewCustomerCurrentSalesOrder);
                    }
                }

                RecyclerView preview_recycler   = view.findViewById(R.id.order_preview_recycler);
                PreviewCustomerCurrentSalesOrderAdapter previewCustomerCurrentSalesOrderAdapter =   new PreviewCustomerCurrentSalesOrderAdapter(context, previewCustomerCurrentSalesOrders);
                preview_recycler.setAdapter(previewCustomerCurrentSalesOrderAdapter);
                preview_recycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                preview_recycler.setLayoutManager(new LinearLayoutManager(context));
                preview_recycler.setItemAnimator(new DefaultItemAnimator());

                ((TextView)view.findViewById(R.id.tv_preview_total)).setText("Total:\t\t"+ commonClass.getCurrencyFormat(Float.valueOf(db.getCustomerCurrentSalesOrderTotal(sharedPrefs.getItem("customer")))));
                bottomSheetDialog.setContentView(view);

                //Handle printing of the invoice
                view.findViewById(R.id.preview_print_invoice).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        printSalesOrder();
                    }
                });

                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();
            }
        });

        findViewById(R.id.sales_order_invoicing).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                printSalesOrder();
            }
        });

        findViewById(R.id.sales_order_saving).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(action.equals("edit"))
                {
                    db.deleteSalesOrder(sales_order_header);
                    saveSalesOrder(0);
                }
                else
                {
                    //Check inf there exists a sales order with the header number and alert the user
                    Cursor cursor2  =   db.getOrdersHeaders(sharedPrefs.getItem("customer"), sales_order_header);
                    if(cursor2.getCount()>0)
                    {
                        AlertDialog.Builder alertDialog =   new AlertDialog.Builder(context);
                        alertDialog.setIcon(R.drawable.mywarning_24dp);
                        alertDialog.setTitle("Duplication Detected");
                        alertDialog.setMessage("The sales order had been saved.\nWould you like to overwrite?");
                        Dialog dialog;
                        alertDialog.setNegativeButton("No, Close", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Yes, Overwrite", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                                db.deleteSalesOrder(sales_order_header);
                                saveSalesOrder(0);
                            }
                        });
                        dialog  =   alertDialog.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    else
                    {
                        saveSalesOrder( 0);
                    }
                }
            }
        });

        findViewById(R.id.sales_order_notes).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater  layoutInflater  =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view   =   layoutInflater.inflate(R.layout.note_dialog_view, null, false);
                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(view);

                view.findViewById(R.id.close_note_pad).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottomSheetDialog.cancel();
                        order_note  =   "";
                    }
                });

                view.findViewById(R.id.save_note_pad).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        order_note  =   ((EditText)view.findViewById(R.id.sales_order_note_pad)).getText().toString().trim();
                        bottomSheetDialog.cancel();
                    }
                });

                order_note  =   "";
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(action.equals("edit"))
        {
            db.deleteSalesTempForCustomer(sharedPrefs.getItem("customer"));
            sharedPrefs.putItem("sales_head_code_edit", "");
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    public void printSalesOrder()
    {
        PrintManager printManager   =   (PrintManager)context.getSystemService(PRINT_SERVICE);
        String job_name =   context.getString(R.string.app_name)+":Invoice";
        //Get the sales order details from the temp table
        Cursor print_cursor =   db.getSalesOrderForPreview(sharedPrefs.getItem("customer"));
        if(print_cursor.getCount()>0)
        {
            ArrayList<DocumentPrintDetails> documentPrintInfo    =   new ArrayList<>();
            DocumentPrintDetails    documentPrintDetails    =
                    new DocumentPrintDetails(db.getCustomers(sharedPrefs.getItem("customer")), sales_order_header, commonClass.getCurrentDate(), "Invoice");
            while(print_cursor.moveToNext())
            {
                documentPrintDetails.document_lines.add(new String[]{print_cursor.getString(print_cursor.getColumnIndex(Commons.SO_SKU_NAME)), String.valueOf(print_cursor.getInt(print_cursor.getColumnIndex(Commons.SO_QTY))), String.valueOf(print_cursor.getFloat(print_cursor.getColumnIndex(Commons.SO_UNIT_PRICE))), "0", String.valueOf(print_cursor.getFloat(print_cursor.getColumnIndex(Commons.SO_LINE_AMOUNT)))});
                documentPrintInfo.add(documentPrintDetails);
            }

            printManager.print(job_name, new DocumentPrinter(context, sharedPrefs.getItem("customer")+" Invoice", documentPrintInfo, db.salesSubTotal(sharedPrefs.getItem("customer")), db.salesTax(sharedPrefs.getItem("customer"))), null);
            sharedPrefs.putItem("print_sales_header", sales_order_header);

            String sales_order_date =   String.format("%s-%s-%s", datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());
            sharedPrefs.putItem("print_sales_date", sales_order_date);
            sharedPrefs.putItem("print_note", order_note);
        }
        else
        {
            Snackbar.make(findViewById(R.id.main_order_interface), "No sales records were found; cannot print.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void saveSalesOrder(int source)
    {

        Cursor temp_cursor  =   db.getProductFromTemp(sharedPrefs.getItem("customer"));
        if(temp_cursor.getCount()>0)
        {
            /*Generate Sales Order header and save it along with the lines.
             * create the header for SO.
             * After saving the header, save the lines also*/

            String sales_order_date =   String.format("%s-%s-%s", datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());

            if(db.createSalesOrderHeader(sales_order_header, sharedPrefs.getItem("customer"), sales_order_date, db.getSaesOrderSum(sharedPrefs.getItem("customer")), sharedPrefs.getItem("username"), order_note, "0") != -1)
            {
                while(temp_cursor.moveToNext())
                {
                    db.createSalesOrderLines(commonClass.getLineId(), sales_order_header, temp_cursor.getString(temp_cursor.getColumnIndex(Commons.SO_SKU_CODE)), temp_cursor.getString(temp_cursor.getColumnIndex(Commons.SO_SKU_NAME)), temp_cursor.getString(temp_cursor.getColumnIndex(Commons.SO_SKU_NAME)), temp_cursor.getInt(temp_cursor.getColumnIndex(Commons.SO_QTY)), temp_cursor.getFloat(temp_cursor.getColumnIndex(Commons.SO_UNIT_PRICE)), temp_cursor.getFloat(temp_cursor.getColumnIndex(Commons.SO_LINE_AMOUNT)), db.getProductUnitCost(temp_cursor.getString(temp_cursor.getColumnIndex(Commons.SO_SKU_CODE))), sales_order_date);
                }

                /*Delete the records in the table for this customer*/
                db.deleteSalesOrderTempAllForCustomer(sharedPrefs.getItem("customer"));
                if(source==0)
                {
                    Snackbar.make(findViewById(R.id.main_order_interface), "Sales Order saved.", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Sales Order saved.", Toast.LENGTH_LONG).show();
                }

                sales_order_header  =   ""; //flush the header number
                finish();
            }
            else
            {
                if(source==0)
                {
                    Snackbar.make(findViewById(R.id.main_order_interface), "Could not save the Sales Order. Restart the App and try again.", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Could not save the Sales Order. Restart the App and try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            if(source==0)
            {
                Snackbar.make(findViewById(R.id.main_order_interface), "You have not taken any order, cannot save.", Snackbar.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "You have not taken any order, cannot save.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
