package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_AcceptReject extends RecyclerView.Adapter<Adapter_AcceptReject.ViewHolder>
{
    Context context;
   ArrayList<Product> stocks;

    public Adapter_AcceptReject(Context context, ArrayList<Product> stocks)
    {
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public Adapter_AcceptReject.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_accept_reject, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AcceptReject.ViewHolder viewHolder, int i)
    {
          Product product=stocks.get(i);
        viewHolder.productName.setText(product.productnme);
        viewHolder.Qty.setText(stocks.get(i).product_details.get(i)[0]);
        viewHolder.Amount.setText(stocks.get(i).product_details.get(i)[1]);
        viewHolder.Totals.setText(stocks.get(i).product_details.get(i)[2]);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName,Qty,Amount,Totals;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName   =itemView.findViewById(R.id.AcceptReject_tvProductName);
            Qty           = itemView.findViewById(R.id.AcceptReject_tvQty);
            Amount        =itemView.findViewById(R.id.AcceptReject_tvAmount);
            Totals        =itemView.findViewById(R.id.AcceptReject_tvTotals);
        }
    }
}
