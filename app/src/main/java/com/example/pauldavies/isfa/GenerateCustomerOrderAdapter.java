package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GenerateCustomerOrderAdapter extends RecyclerView.Adapter<GenerateCustomerOrderAdapter.ViewHolder>
{

    GenerateCustomerOrderAdapter generateCustomerOrderAdapter;

    private List<ClientSalesOrder> clientSalesOrders;
    RecyclerView recyclerView;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;
    TextView salesOrderTotal;
    CommonClass commonClass;

    public GenerateCustomerOrderAdapter(Context context, List<ClientSalesOrder> clientSalesOrders, RecyclerView recyclerView, TextView salesOrderTotal)
    {
        this.context    =   context;
        this.clientSalesOrders  =   clientSalesOrders;
        this.recyclerView       =   recyclerView;

        generateCustomerOrderAdapter    =   this;
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        this.salesOrderTotal    =   salesOrderTotal;

        commonClass =   new CommonClass();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.generate_custome_order_custom, null, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        final ClientSalesOrder clientSalesOrder   =   clientSalesOrders.get(position);

        viewHolder.setIsRecyclable(false);

        viewHolder.sku_code.setText(clientSalesOrder.getSkuCode());
        viewHolder.sku_name.setText(clientSalesOrder.getSkuName());
        viewHolder.unit_price.setText(commonClass.getCurrencyFormat(Float.valueOf(clientSalesOrder.getUnitPrice())));
        viewHolder.line_amount.setText(commonClass.getCurrencyFormat(Float.valueOf(clientSalesOrder.getLineAmount())));
        viewHolder.qty.setText(clientSalesOrder.getQty());

        (viewHolder.qty).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {

                if(!s.toString().trim().isEmpty())
                {
                    viewHolder.line_amount.setText(String.valueOf(Float.valueOf(viewHolder.unit_price.getText().toString().trim()) * Float.valueOf(viewHolder.qty.getText().toString().trim())));
                    db.commitSalesOrder(sharedPrefs.getItem("customer"), viewHolder.sku_code.getText().toString().trim(), Float.valueOf(viewHolder.qty.getText().toString().trim()), Float.valueOf(viewHolder.line_amount.getText().toString().trim()));
                    clientSalesOrder.setLineAmount(commonClass.getCurrencyFormat(Float.valueOf(viewHolder.line_amount.getText().toString().trim())));
                    clientSalesOrder.setQty(viewHolder.qty.getText().toString().trim());

                    //Get the total order and display on the totals text view
                    salesOrderTotal.setText(commonClass.getCurrencyFormat(db.getSaesOrderSum(sharedPrefs.getItem("customer"))));
                }
                else
                {
                    viewHolder.line_amount.setText("");
                    clientSalesOrder.setLineAmount("");
                    clientSalesOrder.setQty("");
                }
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return clientSalesOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView sku_code, sku_name, line_amount, unit_price;
        EditText qty;

        public ViewHolder(View view)
        {
            super(view);

            sku_code    =   view.findViewById(R.id.txt_product_code_take_order);
            sku_name    =   view.findViewById(R.id.txt_product_name_take_order);
            line_amount =   view.findViewById(R.id.txt_product_order_line_amount);
            unit_price  =   view.findViewById(R.id.txt_product_unit_price);
            qty         =   view.findViewById(R.id.txt_product_number_req);
        }
    }

}
