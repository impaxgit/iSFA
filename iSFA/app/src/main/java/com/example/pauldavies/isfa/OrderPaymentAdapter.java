package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderPaymentAdapter extends RecyclerView.Adapter<OrderPaymentAdapter.ViewHolder>
{
    public OrderPaymentAdapter(ArrayList<OrderPayment> orderPayments)
    {
        this.orderPayments = orderPayments;
    }

    private ArrayList<OrderPayment> orderPayments;
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View orderView = inflater.inflate(R.layout.payment, viewGroup, false);


        ViewHolder viewHolder = new ViewHolder(orderView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {
        final  OrderPayment orderPayment=orderPayments.get(position);
        viewHolder.productCode.setText(orderPayment.getProductCode());
        viewHolder.productName.setText(orderPayment.getProductName());
        viewHolder.Qty.setText(orderPayment.getQty());
        viewHolder.Amount.setText(orderPayment.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return orderPayments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productCode;
        public TextView productName;
        public TextView Qty;
        public TextView Amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productCode =   (TextView)itemView.findViewById(R.id.deliverOrders_txtProductCode);
            productName =   (TextView)itemView.findViewById(R.id.deliverOrders_txtProductName);
            Qty         =   (TextView)itemView.findViewById(R.id.deliverOrders_txtQty);
            Amount      =   (TextView)itemView.findViewById(R.id.deliverOrders_txtAmount);
        }
    }
}
