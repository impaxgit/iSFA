package com.example.pauldavies.isfa;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Launcher extends AppCompatActivity
{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        context =   this;

        Handler handler =   new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent   =   new Intent(context, ClientScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);

                finish();
            }
        },
        Commons.IMPAX_SPLASH_DURATION);
    }
}
