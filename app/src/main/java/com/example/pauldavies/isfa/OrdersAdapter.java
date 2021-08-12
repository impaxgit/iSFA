package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>
{

    private ArrayList<DeliveryOrderItems> orderItems;


    Context context;
    CommonClass commonClass;
    TextView totals;
    FloatingActionButton payment;
    private  Double totalAmount=0.0;


    public OrdersAdapter(Context context,ArrayList<DeliveryOrderItems> orderItems)
    {
        this.context        =   context;
        this.orderItems     =   orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View orderView = inflater.inflate(R.layout.orderdelivery_items, viewGroup, false);


        ViewHolder viewHolder = new ViewHolder(orderView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position)
    {

        DeliveryOrderItems orders_ls =orderItems.get(position);

        viewHolder.productName.setText(orders_ls.getProductName());
        viewHolder.qtyOrdered.setText(orders_ls.getQty());
        viewHolder.unitPrice.setText(orders_ls.getUnitPrice());



       //Checkbox activities

         viewHolder.status.setOnCheckedChangeListener(null);
         viewHolder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
         {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
             {
                 //orderItems.get(viewHolder.getAdapterPosition()).setDelivered(isChecked);
             }
         });


       viewHolder.count.addTextChangedListener(new TextWatcher()
       {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count)
           {
             //  Log.d("Sample",s.toString());
           }

           @Override
           public void afterTextChanged(Editable s)
           {
               try
               {
                   Double c=Integer.parseInt(String.valueOf(viewHolder.count.getText())) * Double.parseDouble(String.valueOf(viewHolder.unitPrice.getText().toString()));
                   viewHolder.lineTotal.setText(c.toString());
                  totalAmount =totalAmount + c;
                  // totals.setText(totalAmount.toString());
                   Toast.makeText(context, "Total : " + totalAmount.toString(), Toast.LENGTH_LONG).show();

               }
                catch (NumberFormatException e)
                   {
                      e.printStackTrace();
                   }


           }
       });
    }

    @Override
    public int getItemCount()
    {
       return orderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView qtyOrdered;
        public TextView unitPrice;
        public EditText count;
        public TextView lineTotal;
        public CheckBox status;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName=   itemView.findViewById(R.id.deliverOrders_txtProductName);
            qtyOrdered =   itemView.findViewById(R.id.deliverOrders_txtQty);
            unitPrice  =   itemView.findViewById(R.id.deliverOrders_txtUnitPrice);
            count      =   itemView.findViewById(R.id.deliverOrders_txtCount);
            lineTotal  =   itemView.findViewById(R.id.deliverOrders_txtAmount);
            status     =   itemView.findViewById(R.id.deliverOrders_chkStatus);
            //totals     =   itemView.findViewById(R.id.deliverOrders_txtTotalAmount);
        }
    }
}
