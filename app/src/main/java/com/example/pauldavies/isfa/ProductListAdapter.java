package com.example.pauldavies.isfa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductListAdapter extends BaseExpandableListAdapter
{
    ArrayList<Product> product;
    Context context;
    SharedPrefs sharedPrefs;

    public ProductListAdapter(Context context, ArrayList<Product> product)
    {
        super();
        this.context    =   context;
        this.product    =   product;
        sharedPrefs=new SharedPrefs(context);
    }

    @Override
    public int getGroupCount()
    {
        return product.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return product.get(groupPosition).product_details.size();
    }

    @Override
    public Object getGroup(int i)
    {
        return product.get(i);
    }

    @Override
    public Object getChild(int i, int i1)
    {
        return product.get(i).product_details.get(i1);
    }

    @Override
    public long getGroupId(int i)
    {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View convertView, ViewGroup viewGroup)
    {
        View view   =   convertView;
        Header header;

        if(view == null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.product_header, null, false);
            header  =   new Header(view);
            view.setTag(header);
        }
        else
        {
            header  =   (Header) view.getTag();
        }

        header.code.setText(product.get(i).productcode);
        header.name.setText(product.get(i).productnme);

        if(isExpanded)
        {
            header.imageView.setImageResource(R.drawable.minimise);
            sharedPrefs.putItem("product_code", product.get(i).productcode);
          //  Toast.makeText(context,sharedPrefs.getItem("product_code"),Toast.LENGTH_LONG).show();
        }
        else
        {
            header.imageView.setImageResource(R.drawable.maxmise);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup)
    {
        View view   =   convertView;
        Child child;

        if(view == null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.product_child, null, false);
            child   =   new Child(view);
            view.setTag(child);
        }
        else
        {
            child   =   (Child)view.getTag();
        }

        child.cost.setText(product.get(i).product_details.get(i1)[0]);
        child.category.setText(product.get(i).product_details.get(i1)[1]);
        child.price.setText(product.get(i).product_details.get(i1)[2]);
        child.packaging.setText(product.get(i).product_details.get(i1)[3]);
        child.barcode.setText(product.get(i).product_details.get(i1)[4]);
        

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1)
    {
        return false;
    }

    class Child
    {
        TextView cost;
        TextView category;
        TextView price;
        TextView packaging;
        TextView barcode;

        public Child(View view)
        {
            cost = view.findViewById(R.id.tv_product_cost);
            category = view.findViewById(R.id.tv_product_category);
            price = view.findViewById(R.id.tv_product_price);
            packaging = view.findViewById(R.id.tv_product_packaging);
            barcode = view.findViewById(R.id.tv_product_barcode);

                    
        }
    }

    class Header
    {
        TextView code;
        TextView name;
        ImageView imageView;

        public Header(View view)
        {
            code    =   view.findViewById(R.id.tv_product_code);
            name    =   view.findViewById(R.id.tv_product_name);
            imageView   =   view.findViewById(R.id.imageView9);
        }
    }
}
