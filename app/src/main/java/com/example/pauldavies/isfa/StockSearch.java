package com.example.pauldavies.isfa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class StockSearch extends Fragment
{


    Context context;
    View view;
    AsyncTaskDelegate asyncTaskDelegate;

    public StockSearch()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view    = inflater.inflate(R.layout.fragment_customer_search, container, false);//XML reused

        SearchView searchView   =   view.findViewById(R.id.customer_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                asyncTaskDelegate.getResult(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                asyncTaskDelegate.getResult(newText);

                return false;
            }
        });

        view.findViewById(R.id.imageView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity   =   (Activity)context;

        try
        {
            asyncTaskDelegate   =   (AsyncTaskDelegate)activity;
        }
        catch (ClassCastException e)
        {

        }
    }
}
