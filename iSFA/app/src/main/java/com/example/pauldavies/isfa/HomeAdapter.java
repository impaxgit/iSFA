package com.example.pauldavies.isfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final String[] items;
    private  final Integer[] icons;


    public HomeAdapter(Context context, String[] items,Integer[] icons)
    {
        super(context, R.layout.home_details, items);
        this.context =  context;
        this.items   =  items;
        this.icons   =  icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view  =   convertView;
        ViewHolder  viewHolder;

        if(view ==  null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.home_details, null,true);
            viewHolder  =   new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder  =   (ViewHolder) view.getTag();
        }

        viewHolder.txtTitle.setText(items[position]);
        viewHolder.imageView.setImageResource(icons[position]);

        return view;

    }

    class ViewHolder
    {
        TextView txtTitle;
        ImageView imageView;

        public ViewHolder(View view)
        {
            txtTitle    =  view.findViewById(R.id.txt);
            imageView   =  view.findViewById(R.id.icon);
        }
    }
}
