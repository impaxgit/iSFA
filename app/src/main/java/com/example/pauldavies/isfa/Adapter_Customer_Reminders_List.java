package com.example.pauldavies.isfa;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Customer_Reminders_List extends RecyclerView.Adapter<Adapter_Customer_Reminders_List.ViewsHolder>
{
    ArrayList<A_Reminder> reminders;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;
    SwipeRefreshLayout swipeRefreshLayout;
    CommonClass commonClass;
    RecyclerView recyclerView;

    public Adapter_Customer_Reminders_List(ArrayList<A_Reminder> reminders, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView)
    {
        this.reminders          =   reminders;
        this.swipeRefreshLayout =   swipeRefreshLayout;
        this.recyclerView       =   recyclerView;
    }

    @NonNull
    @Override
    public Adapter_Customer_Reminders_List.ViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        context =   viewGroup.getContext();
        db  =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass =   new CommonClass();

        return new ViewsHolder(((LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_customer_reminder, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Customer_Reminders_List.ViewsHolder viewsHolder, final int position)
    {
        viewsHolder.reminder.setText(reminders.get(position).getRemider());
        viewsHolder.status.setChecked(reminders.get(position).isStatus());
        viewsHolder.remider_id.setText(reminders.get(position).getRem_id());

        viewsHolder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(db.switchOffReminder(sharedPrefs.getItem("customer"), Integer.valueOf(reminders.get(position).getRem_id()), isChecked) != -1)
                {
                    commonClass.populateRecycler(context, swipeRefreshLayout, db, sharedPrefs, recyclerView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView reminder, remider_id;
        Switch status;

        public ViewsHolder(@NonNull View view)
        {
            super(view);
            reminder    =   view.findViewById(R.id.custom_reminder_tv);
            status      =   view.findViewById(R.id.custom_switch);
            remider_id  =   view.findViewById(R.id.remider_id_tv);
        }
    }
}
