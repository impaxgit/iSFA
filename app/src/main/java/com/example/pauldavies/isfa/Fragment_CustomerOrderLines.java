package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_CustomerOrderLines extends Fragment
{
    Context context;
    View view;
    SharedPrefs sharedPrefs;

    public Fragment_CustomerOrderLines()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context =   getContext();
        sharedPrefs =   new SharedPrefs(context);

        view =LayoutInflater.from(container.getContext()).inflate(R.layout.customer_order_lines_custom, container, false);
        return view;
    }
}
