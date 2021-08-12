package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class BadStockAdapter extends RecyclerView.Adapter<BadStockAdapter.ViewHolder>
{
    ArrayList<A_Stock> AStocks;
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    private static int currentPosition = -1;

    public BadStockAdapter(ArrayList<A_Stock> AStocks, Context context)
    {
        this.AStocks = AStocks;
        this.context = context;

    }


    @NonNull
    @Override
    public BadStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_stock_details, viewGroup, false);

        db=new DB(context);
        commonClass =new CommonClass();
        sharedPrefs =new SharedPrefs(context);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BadStockAdapter.ViewHolder viewHolder, final int position)
    {
       final A_Stock AStock = AStocks.get(position);

        viewHolder.product_name.setText(AStock.getProduct_name());
        viewHolder.expired.setText(AStock.getExpired());
        viewHolder.damaged.setText(AStock.getDamaged());
        viewHolder.totals.setText(AStock.getTotals());
        viewHolder.name.setText(AStock.getProduct_name());


        final boolean isExpanded = position==currentPosition;
        final Animation slideDown= AnimationUtils.loadAnimation(context, R.anim.anim);
        viewHolder.linearLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewHolder.itemView.setActivated(isExpanded);
        viewHolder.linearLayout.startAnimation(slideDown);

        viewHolder.product_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentPosition = isExpanded ? -1:position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return AStocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_code,product_name,expired,damaged,totals,name;
        RelativeLayout linearLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
           // product_code=itemView.findViewById(R.id.item_code);
            name   =itemView.findViewById(R.id.stock_tv_product2);
            product_name = itemView.findViewById(R.id.stock_tv_product);
            expired      = itemView.findViewById(R.id.stock_tv_expired);
            damaged      = itemView.findViewById(R.id.stock_tv_damaged);
            totals       = itemView.findViewById(R.id.stock_tv_totals);
            linearLayout=itemView.findViewById(R.id.stock_relativelayout);
        }
    }
}
