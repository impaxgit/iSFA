package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_One_Column extends RecyclerView.Adapter<Adapter_One_Column.ViewsHolder>
{
    ArrayList<OneStringProperty> oneStringProperties;

    public Adapter_One_Column(ArrayList<OneStringProperty> oneStringProperties)
    {
        this.oneStringProperties    =   oneStringProperties;
    }

    @NonNull
    @Override
    public Adapter_One_Column.ViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        return new ViewsHolder(((LayoutInflater)(viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.one_column_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_One_Column.ViewsHolder viewsHolder, int position)
    {
        viewsHolder.textView.setText(oneStringProperties.get(position).getValue());
    }

    @Override
    public int getItemCount()
    {
        return oneStringProperties.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public ViewsHolder(@NonNull View views)
        {
            super(views);

            textView    =   views.findViewById(R.id.call_activities_display);
        }
    }
}
