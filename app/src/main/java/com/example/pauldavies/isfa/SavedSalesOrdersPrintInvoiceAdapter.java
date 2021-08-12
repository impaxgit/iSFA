package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SavedSalesOrdersPrintInvoiceAdapter extends RecyclerView.Adapter<SavedSalesOrdersPrintInvoiceAdapter.ViewsHolder>
{

    Context context;
    List<SalesOrderHeaderForInvoiceAndPrintProperties> salesOrderHeaderForInvoiceAndPrintProperties;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    public SavedSalesOrdersPrintInvoiceAdapter(Context context, List<SalesOrderHeaderForInvoiceAndPrintProperties> salesOrderHeaderForInvoiceAndPrintProperties)
    {
        this.context    =   context;
        this.salesOrderHeaderForInvoiceAndPrintProperties   =   salesOrderHeaderForInvoiceAndPrintProperties;
        commonClass =   new CommonClass();
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
    }

    @Override
    public ViewsHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        return new ViewsHolder(LayoutInflater.from(context).inflate(R.layout.invoice_and_print_custom, null, false));
    }

    @Override
    public void onBindViewHolder(final ViewsHolder viewsHolder, int position)
    {
        final SalesOrderHeaderForInvoiceAndPrintProperties salesOrderHeaderForInvoiceAndPrintProperty   =   salesOrderHeaderForInvoiceAndPrintProperties.get(position);
        viewsHolder.setIsRecyclable(false);

        viewsHolder.header_code.setText(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
        viewsHolder.sales_date.setText(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_date());
        viewsHolder.sales_amount.setText(commonClass.getCurrencyFormat(Float.valueOf(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_amount()))+"/=");

        viewsHolder.print.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view   =   layoutInflater.inflate(R.layout.sales_order_preview, null, false);

                //Change the icon from saving to edit
                ImageView editor    =   ((ImageView)view.findViewById(R.id.imageView8));
                editor.setImageResource(R.drawable.editor);
                editor.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent   =   new Intent(context, GenerateCustomerOrder.class);
                        sharedPrefs.putItem("sales_head_code_edit", ((TextView)view.findViewById(R.id.order_preview_header_code)).getText().toString().trim());
                        context.startActivity(intent);
                    }
                });

                ((TextView)view.findViewById(R.id.order_preview_header_code)).setText(viewsHolder.header_code.getText().toString().trim());
                ((TextView)view.findViewById(R.id.tv_preview_total)).setText("Total:\t\t"+String.valueOf(commonClass.getCurrencyFormat(db.getSalesInvoiceSum(viewsHolder.header_code.getText().toString().trim())))+"/=");
                view.findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottomSheetDialog.cancel();
                    }
                });

                view.findViewById(R.id.preview_print_invoice).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                       PrintManager printManager   =   (PrintManager)context.getSystemService(Context.PRINT_SERVICE);
                        String job_name =   context.getString(R.string.app_name)+":Invoice";

                        Cursor cursor   =   db.getOrderLines(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
                        if(cursor.getCount()>0)
                        {
                            ArrayList<DocumentPrintDetails> documentPrintDetailsList    =   new ArrayList<>();

                            DocumentPrintDetails    documentPrintDetails    =   new DocumentPrintDetails(db.getCustomers(sharedPrefs.getItem("customer")), salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code(), salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_date(), "Invoice");
                            while(cursor.moveToNext())
                            {
                                documentPrintDetails.document_lines.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_COST)), "0", String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)))});
                                documentPrintDetailsList.add(documentPrintDetails);
                            }

                            printManager.print(job_name, new DocumentPrinter(context, sharedPrefs.getItem("customer")+" Invoice", documentPrintDetailsList, salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_amount(), "0"), null);
                            sharedPrefs.putItem("print_sales_header", salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
                        }
                        else
                        {
                            Toast.makeText(context, "No lines were found.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                bottomSheetDialog.setContentView(view);

                Cursor cursor   =   db.getOrderLines(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
                if(cursor.getCount()>0)
                {
                    ArrayList<PreviewCustomerCurrentSalesOrder> previewCustomerCurrentSalesOrders    =   new ArrayList<>();


                    while(cursor.moveToNext())
                    {
                        PreviewCustomerCurrentSalesOrder    previewCustomerCurrentSalesOrder    =   new PreviewCustomerCurrentSalesOrder(cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_CODE)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)), String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY))), String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT))));
                        previewCustomerCurrentSalesOrders.add(previewCustomerCurrentSalesOrder);
                    }

                    RecyclerView preview_recycler   =   (RecyclerView)view.findViewById(R.id.order_preview_recycler);
                    PreviewCustomerCurrentSalesOrderAdapter previewCustomerCurrentSalesOrderAdapter =   new PreviewCustomerCurrentSalesOrderAdapter(context, previewCustomerCurrentSalesOrders);
                    preview_recycler.setAdapter(previewCustomerCurrentSalesOrderAdapter);
                    preview_recycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                    preview_recycler.setLayoutManager(new LinearLayoutManager(context));
                    preview_recycler.setItemAnimator(new DefaultItemAnimator());
                }

                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();

                /*PrintManager printManager   =   (PrintManager)context.getSystemService(Context.PRINT_SERVICE);
                String job_name =   context.getString(R.string.app_name)+":Invoice";

                Cursor cursor   =   db.getOrderLines(salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
                if(cursor.getCount()>0)
                {
                    ArrayList<DocumentPrintDetails> documentPrintDetailsList    =   new ArrayList<>();

                    DocumentPrintDetails    documentPrintDetails    =   new DocumentPrintDetails(db.getCustomers(sharedPrefs.getItem("customer")), salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code(), salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_date(), "Invoice");
                    while(cursor.moveToNext())
                    {
                        documentPrintDetails.document_lines.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_NAME)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_QTY)), cursor.getString(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_ITEM_COST)), "0", String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT)))});
                        documentPrintDetailsList.add(documentPrintDetails);
                    }

                    printManager.print(job_name, new DocumentPrinter(context, sharedPrefs.getItem("customer")+" Invoice", documentPrintDetailsList, salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_amount(), "0"), null);
                    sharedPrefs.putItem("print_sales_header", salesOrderHeaderForInvoiceAndPrintProperty.getSales_order_code());
                }
                else
                {
                    Toast.makeText(context, "No lines were found.", Toast.LENGTH_LONG).show();
                }*/
            }
        });


    }


    @Override
    public int getItemCount()
    {
        return salesOrderHeaderForInvoiceAndPrintProperties.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView header_code, sales_date, sales_amount;
        ImageView print;

        public ViewsHolder(View view)
        {
            super(view);

            header_code =   view.findViewById(R.id.sales_order_id);
            sales_date  =   view.findViewById(R.id.sales_order_date);
            sales_amount=   view.findViewById(R.id.sales_order_amount);
            print       =   view.findViewById(R.id.imageView11);
        }

        @Override
        public void onClick(View v)
        {
            Log.d("Testing", "Reached");
        }
    }
}
