package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class Fragment_All_Customers extends Fragment
{
    View view;
    ExpandableListView expandableListView;
    Context context;
    CommonClass commonClass;

    public Fragment_All_Customers() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        context   =   this.getContext();
        commonClass =   new CommonClass();

        view = inflater.inflate(R.layout.fragment_fragment__all__customers, container, false);

        expandableListView = view.findViewById(R.id.elv_customers);

        commonClass.createCustomers(context, expandableListView);

        return view;
    }
}

