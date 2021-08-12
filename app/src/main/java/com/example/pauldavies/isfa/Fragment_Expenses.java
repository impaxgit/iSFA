package com.example.pauldavies.isfa;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public  class Fragment_Expenses extends Fragment
{
    View view;
    DB db;
    CommonClass commonClass;
    Context context;
    SharedPrefs sharedPrefs;
    String expense_type ="";
    Adapter_Expenses adapter;

    public Fragment_Expenses()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        context =   getContext();
        db      =   new DB(context);
        sharedPrefs =   new SharedPrefs(context);
        commonClass=new CommonClass();

        view=inflater.inflate(R.layout.fragment_new_expense,container,false);

        final EditText amount =view.findViewById(R.id.expense_amount);
        final EditText notes  =view.findViewById(R.id.expense_notes);
        final TextView total_expenses = view.findViewById(R.id.expenses_totals);
        final Button save    = view.findViewById(R.id.expenses_btnSave);
        final Spinner spinner = view.findViewById(R.id.expense_type_spinner);

        //Load expense type on to spinner
        Cursor cursor   =   db.getExpenseTypes();
        if(cursor.getCount()>0)
        {
            String[] header =   new String[cursor.getCount()];
            String[] description    =   new String[cursor.getCount()];
            int counter =0;
            while(cursor.moveToNext())
            {
                header[counter] =   cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_CODE));
                description[counter]    =   cursor.getString(cursor.getColumnIndex(Commons.EXPENSE_NAME));

                counter++;
            }

            SpinnerTwoColumnAdapter spinnerTwoColumnAdapter =   new SpinnerTwoColumnAdapter(context, header, description);
            spinnerTwoColumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerTwoColumnAdapter);
        }

//On Spinner Select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                  expense_type = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//On amount text change
        amount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                total_expenses.setText(String.valueOf(db.getExpensesAmount(sharedPrefs.getItem("username"),commonClass.getCurrentDate())));

            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        //On Button Click

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!amount.getText().toString().isEmpty())
                {
                    if(db.saveExpense(expense_type,commonClass.getCurrentDate(),Float.valueOf(amount.getText().toString().trim()),notes.getText().toString().trim(),Float.valueOf(total_expenses.getText().toString().trim()),sharedPrefs.getItem("username"),"1") !=-1)
                    {
                        Snackbar.make(v,"Expense Saved",Snackbar.LENGTH_LONG).show();
                    }
                    else
                    {
                        Snackbar.make(v,"Expense not saved",Snackbar.LENGTH_LONG).show();

                    }
                }
                else
                {
                    amount.setError("Expense Amount?");
                    amount.requestFocus();

                }

            }
        });
        return view;
    }
}
