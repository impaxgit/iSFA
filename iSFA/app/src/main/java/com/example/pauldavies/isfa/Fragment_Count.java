package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Count extends Fragment
{
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    public Fragment_Count()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.context=getContext();
        commonClass=new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db=new DB(context);

        final View view = inflater.inflate(R.layout.fragment_count,container,false);

        final   Spinner product           =  view.findViewById(R.id.stock_spproduct);
        final TextView product_code       =  view.findViewById(R.id.stock_tvproductcode);
        product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selected_product = parent.getItemAtPosition(position).toString();
                 product_code.setText(selected_product);
                sharedPrefs.putItem("productcode",selected_product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        view.findViewById(R.id.stock_btnSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                String productcode   =((TextView)view.findViewById(R.id.stock_tvproductcode)).getText().toString().trim();
                String expired      = ((EditText)view.findViewById(R.id.stock_txt_expired)).getText().toString().trim();
                String damaged      = ((EditText)view.findViewById(R.id.stock_txt_damaged)).getText().toString().trim();
                String totals       = ((EditText)view.findViewById(R.id.stock_txt_totals)).getText().toString().trim();
                String comments     =((TextView)view.findViewById(R.id.stock_txt_comments)).getText().toString();

                if(!expired.isEmpty())
                {
                  if(!damaged.isEmpty())
                  {
                      if(!totals.isEmpty())
                      {
                              if( db.createBadStock(productcode,expired,damaged,totals,comments) != -1)
                              {
                                  commonClass.createToaster(context, "record saved successfully.", Toast.LENGTH_SHORT, R.drawable.smile);

                                  ((TextView) view.findViewById(R.id.stock_tvproductcode)).setText("");
                                  ((EditText)view.findViewById(R.id.stock_txt_expired)).setText("");
                                  ((EditText)view.findViewById(R.id.stock_txt_damaged)).setText("");
                                  ((EditText)view.findViewById(R.id.stock_txt_totals)).setText("");
                                  ((EditText)view.findViewById(R.id.stock_txt_comments)).setText("");

                              }
                              else
                              {
                                  commonClass.createToaster(context, "Could not save the record.\nTry again!", Toast.LENGTH_LONG, R.drawable.sad);
                              }

                      }
                      else
                      {
                          ((TextView)view.findViewById(R.id.stock_txt_totals)).setError("Totals?");
                          (view.findViewById(R.id.stock_txt_totals)).requestFocus();
                      }
                  }
                  else
                  {
                      ((EditText)view.findViewById(R.id.stock_txt_damaged)).setError("Damaged Stock?");
                      (view.findViewById(R.id.stock_txt_damaged)).requestFocus();
                  }
                }
                else
                {
                    ((EditText)view.findViewById(R.id.stock_txt_expired)).setError("Expired Stock?");
                    (view.findViewById(R.id.stock_txt_expired)).requestFocus();
                }

            }
        });
        return view;
    }
}
