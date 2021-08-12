package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AcceptReject_Stock extends AppCompatActivity
{
    Context context;
    ArrayList<Product> products;
    DB db;
    Adapter_AcceptReject adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptreject__stock);
        context=this;
        db=new DB(context);
       new  getAllProducts().execute();

       recyclerView=findViewById(R.id.Stock_rv);
    }

    class getAllProducts extends AsyncTask<String,Void,ArrayList<Product>>
    {

        public getAllProducts() {
        }

        @Override
        protected ArrayList<Product> doInBackground(String... strings)
        {
            products=new ArrayList<>();
            Cursor  cursor  =   db.getProducts();
            if(cursor.getCount()>0) {
                while (cursor.moveToNext())
                {
                    Product product = new Product("", cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME)));
                    product.product_details.add(new String[]
                            {
                              cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_COST))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CATEGORY))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PRICE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PACKAGING))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_BARCODE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_LOCATION))
                    });

                    products.add(product);
                }
            }

            return products;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);

            adapter    =   new Adapter_AcceptReject(context,products);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

}
