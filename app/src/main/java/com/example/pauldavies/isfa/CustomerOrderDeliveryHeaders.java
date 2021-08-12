package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.print.PrintManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerOrderDeliveryHeaders extends RecyclerView.Adapter<CustomerOrderDeliveryHeaders.ViewsHolder>
{
    View view;
    ArrayList<DeliveryOrders> orders;
    CommonClass commonClass;
    DB db;
    Context context;
    SharedPrefs sharedPrefs;
    public static TextView lineTotal;

    public CustomerOrderDeliveryHeaders(ArrayList<DeliveryOrders> orders, Context context)
    {
        this.orders             =   orders;
        this.context            =   context;
        commonClass             =   new CommonClass();
    }

    @Override
    public CustomerOrderDeliveryHeaders.ViewsHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        view    = LayoutInflater.from(context).inflate(R.layout.customer_order_header_custom, viewGroup, false);


        return new ViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerOrderDeliveryHeaders.ViewsHolder viewsHolder, int position)
    {
        viewsHolder.setIsRecyclable(false);
        final String header_code  =   orders.get(position).getOrderId();
        final String amount = commonClass.getCurrencyFormat(Float.valueOf(orders.get(position).getAmount()))+"/=";
        String count =  String.valueOf(db.getSalesLinesTransSum(header_code));
        viewsHolder.code.setText(header_code);
        viewsHolder.date.setText(orders.get(position).getOrderDate());
        viewsHolder.amount.setText(amount);
        viewsHolder.lines.setText(count);

        viewsHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sharedPrefs.putItem("order_code", header_code);

                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                final View dialog =   LayoutInflater.from(context).inflate(R.layout.customer_order_delivery_lst, null, false);
                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.setCancelable(false);
                ((TextView)dialog.findViewById(R.id.textView46)).setText("<"+header_code+"> \t\t\t Total: "+amount);
                lineTotal   =   dialog.findViewById(R.id.lines_total_tv);

                String no_lines = String.valueOf(db.getSalesLinesTransSum(header_code));

                final String total_amount =   String.valueOf(db.getSalesLinesSum(header_code));
                final String paid_amount  =   String.valueOf(db.getSalesOrderPainAmount(sharedPrefs.getItem("customer"), header_code));


                lineTotal.setText("\t\tNo. Lines:\t"+no_lines+" \t\t\t\tAmount Required: "+total_amount);

                final TextView balance    =   dialog.findViewById(R.id.order_payment_balance);
                TextView paid   =   dialog.findViewById(R.id.amount_paid_order);
                final TextView bal_label  =   dialog.findViewById(R.id.balance_tv_order);
                paid.setText(paid_amount);
                float ini_bal   =   Float.valueOf(total_amount)-Float.valueOf(paid_amount);
                //Set information label for the balance
                if(ini_bal>0) //Not yet cleared amount
                {
                    bal_label.setText("Required");
                    balance.setTextColor(Color.RED);
                    balance.setText(String.valueOf(ini_bal));
                }
                else if(ini_bal<0) //Customer paid more therefore give change
                {
                    bal_label.setText("In excess");
                    balance.setTextColor(Color.GREEN);
                    balance.setText(String.valueOf(ini_bal));
                }
                else
                {
                    bal_label.setText("Balance:");
                    balance.setTextColor(Color.WHITE);
                    balance.setText(String.valueOf(ini_bal));
                }


                dialog.findViewById(R.id.save_customer_order).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PrintManager printManager   =   (PrintManager)context.getSystemService(Context.PRINT_SERVICE);
                        String job_name =   R.string.app_name+"Delivery Receipt";
                        Cursor cursor   =   db.getDeliveredSalesOrdersLines(header_code);
                        ArrayList<DocumentPrintDetails> documentPrintDetails    =   new ArrayList<>();
                        if(cursor.getCount()>0)
                        {
                            DocumentPrintDetails details    =   new DocumentPrintDetails(sharedPrefs.getItem("customer"), header_code, db.getSalesOrderDate(header_code), "Receipt");
                            while(cursor.moveToNext())
                            {
                                details.document_lines.add(new String[]{cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_ITEM_LINES_CODE)), String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_QTY_DELIVERED))), String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_UNIT_PRICE))), "0", String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_TOTAL_PRICE)))});
                                documentPrintDetails.add(details);
                            }

                        }

                        printManager.print(job_name, new DocumentPrinter(context, sharedPrefs.getItem("customer")+"Receipt",documentPrintDetails, db.getSalesOrderTotal(header_code), db.salesTax(sharedPrefs.getItem("customer"))), null);

                    }
                });


                ((EditText)dialog.findViewById(R.id.amount_paid_order)).addTextChangedListener(new TextWatcher()
                {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) { }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        if(!s.toString().trim().isEmpty())
                        {
                            String ttotal_amount    =   String.valueOf(db.getSalesLinesSum(header_code));
                            float computed_balance  =   Float.valueOf(ttotal_amount)-Float.valueOf(s.toString().trim());
                            balance.setText(String.valueOf(computed_balance));

                            //Update the amount paid to this order
                            if(db.updateSalesOrderHeaderPaidAmount(header_code, Float.valueOf(s.toString().trim())) != -1)
                            {
                                //Set information label for the balance
                                if(computed_balance>0) //Not yet cleared amount
                                {
                                    bal_label.setText("Required");
                                    balance.setTextColor(Color.RED);
                                }
                                else if(computed_balance<0) //Customer paid more therefore give change
                                {
                                    bal_label.setText("In excess");
                                    balance.setTextColor(Color.GREEN);
                                }
                                else
                                {
                                    bal_label.setText("Balance:");
                                    balance.setTextColor(Color.WHITE);
                                }
                            }
                            else
                            {
                                Toast.makeText(context, "Could not update payment.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            balance.setText("");
                        }
                    }
                });



                Cursor cursor   =   db.getSalesLinesTrans(header_code);
                if(cursor.getCount()>0)
                {
                    ArrayList<DeliveryLine> lines   =   new ArrayList<>();
                    RecyclerView recyclerView   =   dialog.findViewById(R.id.order_delivery_list);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

                    Customer_Order_Delivery_Lines_Reception customer_order_delivery_lines_reception =
                            new Customer_Order_Delivery_Lines_Reception(lines, context, header_code, no_lines);
                    recyclerView.setAdapter(customer_order_delivery_lines_reception);

                    while(cursor.moveToNext())
                    {
                       lines.add(new DeliveryLine(cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_ITEM_LINES_CODE))
                               ,db.getProductName(cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_ITEM_LINES_CODE)))
                               ,String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_QTY_ORDERED)))
                               ,String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_TOTAL_PRICE)))
                               ,String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_QTY_DELIVERED)))
                               ,String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_LINE_UNIT_PRICE)))));
                    }
                }

                dialog.findViewById(R.id.close_customer_order).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottomSheetDialog.cancel();
                    }
                });

                bottomSheetDialog.show();

            }
        });

        //Put click event listener to only those headers with lines
    }

    @Override
    public int getItemCount()
    {
        return orders.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView code, date, amount, lines;
        ImageView imageView;

        public ViewsHolder(View view)
        {
            super(view);
            code    =   view.findViewById(R.id.customer_order_custom_header);
            date    =   view.findViewById(R.id.custom_order_header_date);
            amount  =   view.findViewById(R.id.customer_order_custom_amount);
            lines   =   view.findViewById(R.id.number_of_lines);
            imageView   =   view.findViewById(R.id.imageView12);
        }
    }
}
