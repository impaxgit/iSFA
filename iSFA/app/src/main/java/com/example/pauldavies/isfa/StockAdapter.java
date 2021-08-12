package com.example.pauldavies.isfa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>
{
    ArrayList<Stock> stocks;
    Activity context;
    SharedPrefs sharedPrefs;
    CommonClass commonClass;
    DB db;

    private static int currentPosition = -1;

    public StockAdapter(ArrayList<Stock> stocks, Activity context)
    {
        this.stocks = stocks;
        this.context = context;
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_details, viewGroup, false);

        db=new DB(context);
        v.findViewById(R.id.assets_editimg).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_count);

                dialog.findViewById(R.id.stock_spproduct).setEnabled(false);
                dialog.findViewById(R.id.stock_btnSave).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String productcode   =((TextView)dialog.findViewById(R.id.stock_tvproductcode)).getText().toString().trim();
                        String productname   =((TextView)dialog.findViewById(R.id.stock_tvproductname)).getText().toString().trim();
                        String expired      = ((EditText)dialog.findViewById(R.id.stock_txt_expired)).getText().toString().trim();
                        String damaged      = ((EditText)dialog.findViewById(R.id.stock_txt_damaged)).getText().toString().trim();
                        String totals       = ((EditText)dialog.findViewById(R.id.stock_txt_totals)).getText().toString().trim();
                        String comments     =((TextView)dialog.findViewById(R.id.stock_txt_comments)).getText().toString();

                        if(!expired.isEmpty())
                        {
                            if(!damaged.isEmpty())
                            {
                                if(!totals.isEmpty())
                                {
                                    if( db.StockEdit(productcode,productname,expired,damaged,totals,comments) != -1)
                                    {
                                      //  commonClass.createToaster(context, "record saved successfully.", Toast.LENGTH_SHORT, R.drawable.smile);
                                        Toast.makeText(context,"Update Successful",Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                    else
                                    {
                                        commonClass.createToaster(context, "Could not save the record.\nTry again!", Toast.LENGTH_LONG, R.drawable.sad);
                                    }

                                }
                                else
                                {
                                    ((TextView)dialog.findViewById(R.id.stock_txt_totals)).setError("Totals?");
                                    (dialog.findViewById(R.id.stock_txt_totals)).requestFocus();
                                }
                            }
                            else
                            {
                                ((EditText)dialog.findViewById(R.id.stock_txt_damaged)).setError("Damaged Stock?");
                                (dialog.findViewById(R.id.stock_txt_damaged)).requestFocus();
                            }
                        }
                        else
                        {
                            ((EditText)dialog.findViewById(R.id.stock_txt_expired)).setError("Expired Stock?");
                            (dialog.findViewById(R.id.stock_txt_expired)).requestFocus();
                        }

                    }
                });
                dialog.show();

            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder viewHolder, final int position)
    {
        Stock stock = stocks.get(position);

        viewHolder.product_name.setText(stock.getProduct_name());
        viewHolder.expired.setText(stock.getExpired());
        viewHolder.damaged.setText(stock.getDamaged());
        viewHolder.totals.setText(stock.getTotals());
        viewHolder.comments.setText(stock.getComments());

        final boolean isExpanded = position==currentPosition;
        final Animation slideDown=AnimationUtils.loadAnimation(context,R.anim.anim);
        viewHolder.linearLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewHolder.edit.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewHolder.itemView.setActivated(isExpanded);
        viewHolder.edit.setActivated(isExpanded);
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
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView product_name,expired,damaged,totals,comments;
        LinearLayout linearLayout;
        ImageView edit;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            product_name = itemView.findViewById(R.id.stock_tv_name);
            expired      = itemView.findViewById(R.id.stock_tv_expired);
            damaged      = itemView.findViewById(R.id.stock_tv_damaged);
            totals       = itemView.findViewById(R.id.stock_tv_totals);
            comments     = itemView.findViewById(R.id.stock_tv_comments);
            edit         = itemView.findViewById(R.id.assets_editimg);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
