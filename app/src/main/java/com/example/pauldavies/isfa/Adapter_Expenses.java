package com.example.pauldavies.isfa;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Expenses extends RecyclerView.Adapter<Adapter_Expenses.ViewHolder>
{
Context context;
ArrayList<A_Expense> expenses;
CommonClass commonClass;

    private static int currentPosition = -1;


    public Adapter_Expenses(Context context, ArrayList<A_Expense> expenses)
    {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        commonClass =new CommonClass();
        return new ViewHolder(((LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_expenses, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position)
    {
        final A_Expense expense = expenses.get(position);

        viewHolder.expense_type.setText(expense.getExpense_type());
        viewHolder.expense_date.setText(expense.getDate());
        viewHolder.expense_amount.setText(commonClass.getCurrencyFormat(Float.valueOf(expense.getAmount())));
        viewHolder.notes.setText(expense.getNotes());


        final boolean isExpanded = position==currentPosition;
        final Animation slideDown= AnimationUtils.loadAnimation(context,R.anim.anim);
        viewHolder.linearLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewHolder.itemView.setActivated(isExpanded);
        viewHolder.linearLayout.startAnimation(slideDown);

        viewHolder.headerLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentPosition = isExpanded ? -1:position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView expense_type,expense_date,expense_amount,notes;
        RelativeLayout linearLayout;
        LinearLayout headerLayout;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            expense_type         =itemView.findViewById(R.id.expense_tv_type);
            expense_date = itemView.findViewById(R.id.expense_tv_date);
            expense_amount      = itemView.findViewById(R.id.expense_tv_amount);
            notes      = itemView.findViewById(R.id.expense_tv_notes);
            linearLayout=itemView.findViewById(R.id.expenses_relativelayout);
            headerLayout=itemView.findViewById(R.id.linearlayout);

        }
    }
}
