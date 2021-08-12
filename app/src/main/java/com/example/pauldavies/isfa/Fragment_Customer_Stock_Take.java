package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_Customer_Stock_Take extends Fragment
{
    View view;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;
    CommonClass commonClass;
    FragmentManager fragmentManager;
    StockAdapter  stockAdapter;
    Spinner spinner;
    RecyclerView recyclerView;
    TextView textView;
    ImageView save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        context = this.getActivity();
        commonClass = new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db = new DB(context);

        final View v = inflater.inflate(R.layout.activity_customer__stock__take, container, false);

        spinner = v.findViewById(R.id.stockTake_sp_location);
        recyclerView = v.findViewById(R.id.stock_recyclerView);
        textView = v.findViewById(R.id.stock_take_totals);
        save =v.findViewById(R.id.stocks_saving);

        new GetItems().execute("");

        //Load Locations to the spinner
        Cursor stockLocation_cursor   =   db.getBins();
        if(stockLocation_cursor != null)
        {
            if(stockLocation_cursor.getCount()>0)
            {
                int i= 0;
                String[] codes  =   new String[stockLocation_cursor.getCount()+1];
                String[] description=new String[stockLocation_cursor.getCount()+1];

                codes[0]="";
                description[0]="";
                i++;
                while(stockLocation_cursor.moveToNext())
                {
                    codes[i]    =   stockLocation_cursor.getString(stockLocation_cursor.getColumnIndex(Commons.BIN_NAME));
                    if(!stockLocation_cursor.getString(stockLocation_cursor.getColumnIndex(Commons.BIN_NAME)).trim().isEmpty()) //If name is not provided then use description instead
                    {
                        description[i]=stockLocation_cursor.getString(stockLocation_cursor.getColumnIndex(Commons.BIN_NAME));
                    }
                    else
                    {
                        description[i]=stockLocation_cursor.getString(stockLocation_cursor.getColumnIndex(Commons.BIN_DESCRIPTION));
                    }

                    i++;

                }

                SpinnerTwoColumnAdapter spinnerTwoColumnAdapter =   new SpinnerTwoColumnAdapter(context, codes, description);
                spinner.setAdapter(spinnerTwoColumnAdapter);
                spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                new GetItems().execute(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


        return v;
    }

    class GetItems extends AsyncTask<String, Void, ArrayList<A_Stock>>
    {
        ArrayList<A_Stock> AStocks =new ArrayList<>();


        public GetItems() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<A_Stock> AStocks)
        {
            super.onPostExecute(AStocks);

            stockAdapter    =   new StockAdapter(AStocks, context, recyclerView, textView,save);
            recyclerView.setAdapter(stockAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }

        @Override
        protected ArrayList<A_Stock> doInBackground(String... string)
        {
            Cursor cursor = db.getProductsForSalesOrderTakingIntoTemp(string[0]);
            db.deleteStagingProduct(sharedPrefs.getItem("customer"));
            if (cursor.getCount()>0)
            {
                while (cursor.moveToNext())
                {
                    db.createProducts_Temp(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CODE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_CATEGORY))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_BARCODE))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_SEARCH_NAME))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PACKAGING))
                            , Float.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_COST)))
                            , Float.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_PRICE)))
                            , Integer.valueOf(cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_SYNC)))
                            , cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_LOCATION)), 0, 0, 0, sharedPrefs.getItem("customer"));
                }
            }



            Cursor  cursor1 =   db.getProductsOntoList(sharedPrefs.getItem("customer"));
            if(cursor1.getCount()>0)
            {
                A_Stock AStock1 = new A_Stock();
                AStock1.setComments("");
                AStock1.setDamaged("Damaged");
                AStock1.setExpired("Expired");
                AStock1.setProduct_name("Product Name");
                AStock1.setProduct_code("");
                AStock1.setTotals("Totals");

                AStocks.add(AStock1);

                while(cursor1.moveToNext())
                {

                    A_Stock AStock = new A_Stock();
                    AStock.setProduct_name(cursor1.getString(cursor1.getColumnIndex(Commons.STAGING_PRODUCT_NAME)));
                    AStock.setProduct_code(cursor1.getString(cursor1.getColumnIndex(Commons.STAGING_PRODUCT_CODE)));

                    AStocks.add(AStock);
                }
            }


            return AStocks;
        }
    }
}
