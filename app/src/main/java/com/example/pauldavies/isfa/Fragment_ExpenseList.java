package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class Fragment_ExpenseList extends Fragment
{
    View view;
    DB  db;
    Context context;
    SharedPrefs sharedPrefs;
    Adapter_Expenses adapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    CommonClass commonClass;

    ArrayList<A_Expense> expenses;
    public Fragment_ExpenseList()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        context     =   getContext();
        db          =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass=new CommonClass();

        view    =   inflater.inflate(R.layout.expense_list, container, false);
        recyclerView=view.findViewById(R.id.expense_recyclerView);
        fab=view.findViewById(R.id.expenses_sync);

        new populateRecyler().execute();

        final SwipeRefreshLayout swipeRefreshLayout   =   view.findViewById(R.id.expenses_swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                new populateRecyler().execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v2)
            {
                new SaveExpenses(new AsyncTaskDelegate() {
                    @Override
                    public void getResult(String result) {
                        if (!result.isEmpty())
                        {
                            try
                            {
                             JSONObject object=new JSONObject();
                             object.put("expense_code",Commons.EXPENSE_CODE);
                             object.put("description",Commons.EXPENSE_NAME);
                             object.put("salesperson_id",Commons.EXPENSE_SALESPERSON);
                             object.put("date",Commons.EXPENSE_DATE);
                             object.put("value",Commons.EXPENSE_value);
                             object.put("comments",Commons.EXPENSE_NOTES);
                             object.put("settled",Commons.EXPENSE_STATUS);

                            }
                            catch (JSONException j)
                            {
                            }
                            Snackbar.make(v2,"Testing",Snackbar.LENGTH_SHORT);
                        }
                        else
                        {
                            Snackbar.make(v2,"Failed to insert",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
            }
        });
        return view;
    }
    class populateRecyler extends AsyncTask<String, Void, ArrayList<A_Expense>>
    {


        public populateRecyler()
        {

        }

        @Override
        protected ArrayList<A_Expense> doInBackground(String... strings)
        {
            expenses = new ArrayList<>();
            Cursor cursor   =   db.getExpenses();
            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {

                    A_Expense expense = new A_Expense();
                    expense.setExpense_type(cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_TYPE)));
                    expense.setDate(cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_DATE)));
                    expense.setAmount(cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_value)));
                    expense.setNotes(cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_NOTES)));

                    expenses.add(expense);
                }
            }


            return expenses;
            }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<A_Expense> a_expenses) {
            super.onPostExecute(a_expenses);

            adapter    =   new Adapter_Expenses(context,expenses);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

    }
}
