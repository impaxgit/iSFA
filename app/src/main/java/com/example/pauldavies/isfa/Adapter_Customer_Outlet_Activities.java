package com.example.pauldavies.isfa;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adapter_Customer_Outlet_Activities extends RecyclerView.Adapter<Adapter_Customer_Outlet_Activities.ViewsHolder>
{
    ArrayList<An_Outlet_Activity> outlet_activities;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;

    public Adapter_Customer_Outlet_Activities(ArrayList<An_Outlet_Activity> outlet_activities)
    {
        this.outlet_activities  =   outlet_activities;
    }

    @NonNull
    @Override
    public Adapter_Customer_Outlet_Activities.ViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        context     =   viewGroup.getContext();
        db          =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        return new ViewsHolder(((LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_outlet_activities_list , null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Customer_Outlet_Activities.ViewsHolder viewsHolder, final int position)
    {
        viewsHolder.textView1.setText(outlet_activities.get(position).getCode());
        viewsHolder.textView.setText(outlet_activities.get(position).getName());
        viewsHolder.checkBox.setChecked(outlet_activities.get(position).isChecked);

        viewsHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(db.saveCustomerActivityDone(sharedPrefs.getItem("customer"), Integer.valueOf(outlet_activities.get(position).getCode()), outlet_activities.get(position).getName(), isChecked) != -1)
                {
                    outlet_activities.get(position).setChecked(true);
                }
                else
                {
                    outlet_activities.get(position).setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return outlet_activities.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView textView, textView1;
        CheckBox checkBox;

        public ViewsHolder(@NonNull View views)
        {
            super(views);
            textView1   =   views.findViewById(R.id.outlet_activity_id);
            textView    =   views.findViewById(R.id.custom_outlet_name);
            checkBox    =   views.findViewById(R.id.custom_outlet_flag);
        }
    }
}
