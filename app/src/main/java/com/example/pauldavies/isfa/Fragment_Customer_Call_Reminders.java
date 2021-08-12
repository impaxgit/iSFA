package com.example.pauldavies.isfa;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class Fragment_Customer_Call_Reminders extends Fragment
{
    View view;
    Context context;
    DB  db;
    SharedPrefs sharedPrefs;
    SwipeRefreshLayout swipeRefreshLayout;

    public Fragment_Customer_Call_Reminders()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context =   getContext();
        db      =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);

        view    =    inflater.inflate(R.layout.fragment_fragment_customer_call_reminders, container, false);

        swipeRefreshLayout   =   view.findViewById(R.id.call_reminders_list_ref);

        populateRecycler();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateRecycler();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        view.findViewById(R.id.call_objectives_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final BottomSheetDialog bottomSheetDialog =   new BottomSheetDialog(context);
                final View view1  =   ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.note_dialog_view, null, false);

                bottomSheetDialog.setContentView(view1);

                ((TextView)view1.findViewById(R.id.textView30)).setText("Note");

                view1.findViewById(R.id.save_note_pad).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText note   =   view1.findViewById(R.id.sales_order_note_pad);
                        String notes    =   note.getText().toString().trim();

                        if(!notes.isEmpty())
                        {
                            if(db.saveCustomerReminders(sharedPrefs.getItem("customer"), notes) != -1)
                            {
                                bottomSheetDialog.dismiss();
                                populateRecycler();
                               Snackbar.make(view, "Reminder saved successfully.", Snackbar.LENGTH_LONG).show();
                            }
                            else
                            {
                                Snackbar.make(view1, "Unable to save reminder. Try again.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            note.setError("Write reminder here!");
                            note.requestFocus();
                        }
                    }
                });
                view1.findViewById(R.id.close_note_pad).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();
            }
        });


        return view;
    }

    public void populateRecycler()
    {
        Cursor cursor   =   db.getCustomerReminders(sharedPrefs.getItem("customer"));
        if(cursor.getCount()>0)
        {
            RecyclerView recyclerView   =   view.findViewById(R.id.call_reminders_list);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

            ArrayList<A_Reminder> reminders =   new ArrayList<>();

            Adapter_Customer_Reminders_List adapter_customer_reminders_list =   new Adapter_Customer_Reminders_List(reminders, swipeRefreshLayout, recyclerView);

            while(cursor.moveToNext())
            {
                A_Reminder a_reminder   =   new A_Reminder();
                a_reminder.setRemider(cursor.getString(cursor.getColumnIndex(Commons.CUSTOMER_REMINDER_NOTE)));
                a_reminder.setRem_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(Commons.CUSTOMER_REMIDER_ID))));
                a_reminder.setStatus(true);

                reminders.add(a_reminder);
            }

            recyclerView.setAdapter(adapter_customer_reminders_list);
        }
    }

}
