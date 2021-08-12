package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class DayInTrade extends AppCompatActivity
{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_in_trade);

        context =   this;

        this.setTitle("Day In Trade");

        findViewById(R.id.market_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =   new Intent(context, MarketFeedback.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim, R.anim.anim);
            }
        });

        findViewById(R.id.calls_trade_new_outlet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewOutlet.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim, R.anim.anim);

            }
        });

        findViewById(R.id.calls_trade_calls).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent   =   new Intent(context, JourneyPlan.class);
                startActivity(intent);
                overridePendingTransition(R.anim.to_the_left, R.anim.to_the_right);
            }
        });
    }
}
