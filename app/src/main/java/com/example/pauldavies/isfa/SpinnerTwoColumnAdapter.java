package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerTwoColumnAdapter extends ArrayAdapter<String>
{
    Context context;
    String[] codes;
    String[] description;

    public SpinnerTwoColumnAdapter(Context context, String[] codes, String[] description)
    {
        super(context, R.layout.spinner_two_column, codes);

        this.context        =   context;
        this.codes          =   codes;
        this.description    =   description;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view   =   convertView;
        ViewHolder viewHolder;

        if(view==null)
        {
            LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view    =   layoutInflater.inflate(R.layout.spinner_two_column, null, false);
            viewHolder  =   new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder  =   (ViewHolder) view.getTag();
        }

        viewHolder.code.setText(codes[position]);
        viewHolder.description.setText(description[position]);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }

    class ViewHolder
    {
        TextView code;
        TextView description;

        public ViewHolder(View view)
        {
            code    =   view.findViewById(R.id.spinner_two_column_code);
            description =   view.findViewById(R.id.spinner_two_column_description);
        }
    }
}
