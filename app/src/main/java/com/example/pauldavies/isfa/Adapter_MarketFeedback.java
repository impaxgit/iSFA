package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_MarketFeedback extends RecyclerView.Adapter<Adapter_MarketFeedback.ViewsHolder>
{
    ArrayList<A_MarketFeedBack> marketFeedBacks;
    Context context;
    private static int currentPosition=0;

    public Adapter_MarketFeedback(ArrayList<A_MarketFeedBack> marketFeedBacks, Context context)
    {
        this.marketFeedBacks    =   marketFeedBacks;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_MarketFeedback.ViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        return new ViewsHolder(((LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_market_feedback, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_MarketFeedback.ViewsHolder viewsHolder, final int position) {
        viewsHolder.customer.setText(marketFeedBacks.get(position).getCustomer());
        viewsHolder.feedback_type.setText(marketFeedBacks.get(position).getType());
        viewsHolder.feedback.setText(marketFeedBacks.get(position).getFeedback());

        viewsHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slidedown=AnimationUtils.loadAnimation(context,R.anim.anim);
                viewsHolder.linearLayout.startAnimation(slidedown);
                viewsHolder.linearLayout.setVisibility(View.VISIBLE);
                currentPosition =   position;

            }
        });
    }

    @Override
    public int getItemCount() {
        return marketFeedBacks.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView customer, feedback_type, feedback;
        LinearLayout linearLayout;
        CardView cardView;

        public ViewsHolder(@NonNull View views) {
            super(views);

            customer    =   views.findViewById(R.id.market_customer);
            feedback_type   =   views.findViewById(R.id.feedback_card_type);
            feedback        =   views.findViewById(R.id.market_feedback_feedback);
            linearLayout    =   views.findViewById(R.id.feedback_layer);
            cardView        =   views.findViewById(R.id.market_feedback_card);
        }
    }
}
