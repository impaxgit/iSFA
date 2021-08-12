package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class OutletActivity extends AppCompatActivity
{
    Context context;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);

        context=this;
        sharedPrefs =   new SharedPrefs(context);


        final CheckBox generateOrder=findViewById(R.id.outlet_chkGenerateOrder);
        generateOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( ((CheckBox)v).isChecked() )
              {
                  Intent intent=new Intent(context,GenerateOrder.class);
                  startActivity(intent);
              }
            }
        });

        final CheckBox deliverOrder=findViewById(R.id.outlet_chkDeliveryOrder);
        deliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DeliverOrder.class);
               startActivity(intent);
            }
        });

        final  CheckBox stockTake =findViewById(R.id.outlet_chkStockTake);
        stockTake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent stockTake = new Intent(context,StockTake.class);
                startActivity(stockTake);
            }
        });

        final CheckBox assettracking = findViewById(R.id.outlet_chkAssetTracking);
        assettracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,AssetTracking.class);
                startActivity(intent);
            }
        });
    }
}
