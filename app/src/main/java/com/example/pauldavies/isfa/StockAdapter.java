package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>
{
    ArrayList<A_Stock> AStocks;
    Context context;
    SharedPrefs sharedPrefs;
    CommonClass commonClass;
    DB db;
    TextView StockTakeTotals;
    RecyclerView recyclerView;
    ImageView save;


    public StockAdapter(ArrayList<A_Stock> AStocks, Context context, RecyclerView recyclerView, TextView StockTakeTotals, ImageView save) {
        this.AStocks = AStocks;
        this.context = context;
        this.StockTakeTotals = StockTakeTotals;
        this.recyclerView = recyclerView;
        this.save=save;
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        context=viewGroup.getContext();
        db = new DB(context);
        sharedPrefs = new SharedPrefs(context);

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_take_details, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StockAdapter.ViewHolder viewHolder, final int position)
    {
        final A_Stock AStock = AStocks.get(position);

        if(position==0)
        {
            viewHolder.product_name.setText(AStock.getProduct_name());
            viewHolder.product_code.setText(AStock.getProduct_code());
            viewHolder.totals.setText(AStock.getTotals());

            viewHolder.product_name.setBackgroundColor(Color.GRAY);

            viewHolder.expired.setText("Expired");
            viewHolder.expired.setCursorVisible(false);
            viewHolder.expired.setKeyListener(null);
            viewHolder.expired.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            viewHolder.expired.setBackgroundColor(Color.GRAY);


            viewHolder.damaged.setCursorVisible(false);
            viewHolder.damaged.setKeyListener(null);
            viewHolder.damaged.setText("Damaged");
            viewHolder.damaged.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            viewHolder.damaged.setBackgroundColor(Color.GRAY);




            viewHolder.totals.setCursorVisible(false);
            viewHolder.totals.setKeyListener(null);
            viewHolder.totals.setBackgroundColor(Color.GRAY);
            viewHolder.totals.setText("Totals");
        }
        else
        {
            viewHolder.product_name.setText(AStock.getProduct_name());
            viewHolder.product_code.setText(AStock.getProduct_code());
            viewHolder.totals.setText("0");
            viewHolder.expired.setText("0");
            viewHolder.damaged.setText("0");


            viewHolder.expired.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (!s.toString().trim().isEmpty())
                    {
                        viewHolder.totals.setText(String.valueOf(Integer.valueOf(viewHolder.expired.getText().toString().trim()) +  Integer.valueOf(viewHolder.damaged.getText().toString().trim())));

                        if(db.updateBadStock(AStocks.get(position).getProduct_code(),sharedPrefs.getItem("customer"),Integer.valueOf(viewHolder.expired.getText().toString().trim()),Integer.valueOf(viewHolder.damaged.getText().toString().trim()),Integer.valueOf(viewHolder.totals.getText().toString().trim())) != -1)
                        {
                            //  Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
                        }
                        StockTakeTotals.setText(String.valueOf(db.getStockTakeTotal(sharedPrefs.getItem("customer"))));
                        //  AStocks.get(position).getProduct_code())
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.damaged.addTextChangedListener(new TextWatcher() {

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
                    if (!s.toString().trim().isEmpty())
                    {
                        viewHolder.totals.setText(String.valueOf(Integer.valueOf(viewHolder.expired.getText().toString().trim()) +  Integer.valueOf(viewHolder.damaged.getText().toString().trim())));

                        if(db.updateBadStock(viewHolder.product_code.getText().toString(),sharedPrefs.getItem("customer"),Integer.valueOf(viewHolder.expired.getText().toString().trim()),Integer.valueOf(viewHolder.damaged.getText().toString().trim()),Integer.valueOf(viewHolder.totals.getText().toString().trim())) != -1)
                        {
                            //  Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
                        }
                        StockTakeTotals.setText(String.valueOf(db.getStockTakeTotal(sharedPrefs.getItem("customer"))));

                    }

                }
            });

            //Save button
            save.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Cursor cursor_stock = db.getStagingProducts(sharedPrefs.getItem("customer"));
                    if(cursor_stock.getCount()>0)
                    {
                        while(cursor_stock.moveToNext())
                        {
                            db.deleteBadStock(cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_CODE)));
                            if(db.createBadStock(cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_CODE)),cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_NAME)), Integer.valueOf(cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_EXPIRED))),Integer.valueOf(cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_DAMAGED))),Integer.valueOf(cursor_stock.getString(cursor_stock.getColumnIndex(Commons.STAGING_PRODUCT_TOTALS)))) != -1)

                            {
                                Snackbar.make(v,"Successful",Snackbar.LENGTH_LONG).show();

                            }
                        }

                    }
                    else
                    {
                        Snackbar.make(v,"A_Stock not found",Snackbar.LENGTH_LONG).show();
                    }


                }
            });

        }


    }
    @Override
    public int getItemCount()
    {
        return AStocks.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView product_name,totals,product_code;
        EditText expired,damaged;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            product_code=itemView.findViewById(R.id.stock_code);
            product_name = itemView.findViewById(R.id.stock_tv_productname);
            expired      = itemView.findViewById(R.id.stock_tv_expired);
            damaged      = itemView.findViewById(R.id.stock_tv_damaged);
            totals       = itemView.findViewById(R.id.stock_tv_totals);
        }
    }



}
