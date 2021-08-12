package com.example.pauldavies.isfa;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>
{
    ArrayList<Notifications> notifications;
    Context context;
    private static int currentPosition = 0;
    DB db;
    CommonClass commonClass;
    SharedPrefs sharedPrefs;

    AsyncTaskDelegate delegate;

    public NotificationsAdapter(ArrayList<Notifications> notifications, Context context)
    {
        this.notifications = notifications;
        this.context = context;
        db = new DB(context);
        commonClass = new CommonClass();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_details, viewGroup, false);

       return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position)
    {
        final Notifications notify = notifications.get(position);
        viewHolder.id.setText(String.valueOf(notify.getId()));
        viewHolder.subject.setText(notify.getSubject());
        viewHolder.content.setText(notify.getContent());
        if(currentPosition == position)
        {
            Animation slidedown=AnimationUtils.loadAnimation(context,R.anim.anim);
            viewHolder.linearLayout.startAnimation(slidedown);
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the position of the item to expand it
                currentPosition = position;

                db.ReadNotifications(viewHolder.id.getText().toString(),viewHolder.subject.getText().toString().trim(),viewHolder.content.getText().toString().trim());

                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView id,subject,content,status;
        LinearLayout linearLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            id = itemView.findViewById(R.id.notification_tv_id);
            subject      = itemView.findViewById(R.id.notification_tv_subject);
            content      = itemView.findViewById(R.id.notification_tv_content);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            cardView     = itemView.findViewById(R.id.card_notifications);
        }


    }
}

