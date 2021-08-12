package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Customer_Order_Delivery_Lines_Reception extends RecyclerView.Adapter<Customer_Order_Delivery_Lines_Reception.ViewsHolder>
{
    ArrayList<DeliveryLine> deliveryLines;
    CommonClass commonClass;
    Context context;
    DB db;
    String header_code;
    String lines;

    public Customer_Order_Delivery_Lines_Reception(ArrayList<DeliveryLine> deliveryLines, Context context, String header_code, String lines)
    {
        this.deliveryLines  =   deliveryLines;
        commonClass         =   new CommonClass();
        this.context        =   context;
        db                  =   new DB(context);
        this.header_code    =   header_code;
        this.lines          =   lines;
    }

    @Override
    public void  onBindViewHolder(final ViewsHolder holder, final int position)
    {
        holder.setIsRecyclable(false);
        holder.code.setText(deliveryLines.get(position).getCode());
        holder.name.setText(deliveryLines.get(position).getName());
        holder.qty_requested.setText(deliveryLines.get(position).getQty_ordered());
        holder.qty.setText(deliveryLines.get(position).getQty());
        holder.total.setText(deliveryLines.get(position).getTotal());

        //Add the text change lister to the qty fields
        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(!holder.qty.getText().toString().trim().isEmpty())
                {
                    String line_total   =   String.valueOf(Float.valueOf(deliveryLines.get(position).getPrice_unit())*Float.valueOf(holder.qty.getText().toString().trim()));

                    holder.total.setText(commonClass.getCurrencyFormat(Float.valueOf(line_total)));

                    //Get the current total and display on the lines total field
                    if(db.updateSalesOrderLine(header_code, holder.code.getText().toString().trim(), Integer.valueOf(holder.qty.getText().toString().trim())) != -1)
                    {
                        //Update the total as well then the text view
                        if(db.updateSalesOrderLine(header_code, holder.code.getText().toString().trim(), Float.valueOf(line_total)) != -1)
                        {
                            CustomerOrderDeliveryHeaders.lineTotal.setText("\t\tNo. Lines:\t"+lines+" \t\t\tAmount Required: "+String.valueOf(db.getSalesLinesSum(header_code)));
                        }
                    }
                }
                else
                {
                    holder.total.setText(deliveryLines.get(position).getTotal());
                }
            }
        });
    }

    @Override
    public Customer_Order_Delivery_Lines_Reception.ViewsHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new ViewsHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_delivery_order_lines, viewGroup, false));
    }

    @Override
    public int getItemCount()
    {
        return deliveryLines.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView code, name, qty_requested, total;
        EditText qty;

        public ViewsHolder(View view)
        {
            super(view);

            code            =   view.findViewById(R.id.textView36);
            name            =   view.findViewById(R.id.textView43);
            qty_requested   =   view.findViewById(R.id.textView44);
            total           =   view.findViewById(R.id.textView45);

            qty             =   view.findViewById(R.id.editText2);
        }
    }
}
