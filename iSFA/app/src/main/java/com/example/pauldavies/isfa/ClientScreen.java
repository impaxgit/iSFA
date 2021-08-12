package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClientScreen extends AppCompatActivity
{
    Context context;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_screen);

        context =   this;

        sharedPrefs =   new SharedPrefs(context);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(sharedPrefs.getItem("logged_in").equals("YES"))
                {
                    Intent intent   =   new Intent(context, Home.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent   =   new Intent(context, Login.class);
                    startActivity(intent);
                }

                finish();
            }
        }, Commons.CLIENT_SPLASH_DURATION);
    }
}
