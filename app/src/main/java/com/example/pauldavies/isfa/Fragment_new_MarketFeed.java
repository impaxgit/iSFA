package com.example.pauldavies.isfa;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Fragment_new_MarketFeed extends Fragment
{
    View view;
    Context context;
    DB db;
    SharedPrefs sharedPrefs;

    public Fragment_new_MarketFeed()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context =   getContext();
        db      =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        view    =   inflater.inflate(R.layout.fragment_fragment_new_market_feed, container, false);

        final Spinner spinner     =   view.findViewById(R.id.spinner);
        final EditText message    =   view.findViewById(R.id.editText5);
        Button   button     =   view.findViewById(R.id.button5);

        Cursor cursor   =   db.getFeedbackTypes();
        if(cursor.getCount()>0)
        {
            String[] header =   new String[cursor.getCount()];
            String[] description    =   new String[cursor.getCount()];
            int counter =0;
            while(cursor.moveToNext())
            {
                header[counter] =   cursor.getString(cursor.getColumnIndex(Commons.FEEDBACK_ID));
                description[counter]    =   cursor.getString(cursor.getColumnIndex(Commons.FEEDBACK_MESSAGE));

                counter++;
            }

            SpinnerTwoColumnAdapter spinnerTwoColumnAdapter =   new SpinnerTwoColumnAdapter(context, header, description);
            spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerTwoColumnAdapter);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_s    =   message.getText().toString().trim();
                String type         =   spinner.getSelectedItem().toString().trim();
                if(!type.isEmpty())
                {
                    if(!message_s.isEmpty())
                    {
                        if(db.saveMarketFeedback(type, message_s, sharedPrefs.getItem("customer")) != -1)
                        {
                            message.setText("");
                            Snackbar.make(view, "Feedback saved!", Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            Snackbar.make(view, "Unable to save feedback. Try again!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        message.setError("Feedback?");
                        message.requestFocus();
                    }
                }
                else
                {
                    spinner.performClick();
                }
            }
        });

        return view;
    }

}
