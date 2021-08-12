package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class AssetTracking extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_tracking);
        context=this;
        commonClass=new CommonClass();
        db=new DB(context);
        sharedPrefs=new SharedPrefs(context);

        commonClass.getAssets(context, (RecyclerView) findViewById(R.id.assets_rv));

    }
}
