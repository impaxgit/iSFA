package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductList extends AppCompatActivity
{
    Context context;
    SharedPrefs sharedPrefs;
    CommonClass commonClass;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        this.setTitle("Product Master");
        context =   this;
        sharedPrefs =   new SharedPrefs(context);
        db  =   new DB(context);

        //Create values for the list of products
        createProducts();

    }

    private void createProducts()
    {
        final ArrayList<Product> products = new ArrayList<>();

        Cursor  cursor  =   db.getProducts();
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                    Product product =   new Product(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME)));
                    product.product_details.add(new String[]{
                              cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_COST))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CATEGORY))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PRICE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PACKAGING))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_BARCODE))
                            ,cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_LOCATION))
                    });
    // sharedPrefs.putItem("productname",cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME)));
    // sharedPrefs.putItem("productcode",cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE)));
                    products.add(product);
            }

            ProductListAdapter productListAdapter   =   new ProductListAdapter(context, products);
            ExpandableListView expandableListView   =  findViewById(R.id.productlist);
            expandableListView.setAdapter(productListAdapter);
        }
        else
        {
            commonClass.createToaster(context, "No products found!", Commons.TOASTER_LONG, R.drawable.sad);
        }
    }
}
