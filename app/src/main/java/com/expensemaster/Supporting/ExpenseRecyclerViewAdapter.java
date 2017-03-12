package com.expensemaster.Supporting;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensemaster.Activities.R;
import com.expensemaster.Bean.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 1/14/2017.
 */
public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.MyViewHolder>{

    private List<Expense> expenseList;
    private SparseBooleanArray selectedItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount,type,category,date,idHidden;

        public MyViewHolder(View view) {
            super(view);
            amount=(TextView)view.findViewById(R.id.lst_amount);
            type=(TextView)view.findViewById(R.id.lst_type);
            category=(TextView)view.findViewById(R.id.lst_category);
            date= (TextView)view.findViewById(R.id.lst_date);
            idHidden = (TextView)view.findViewById(R.id.lst_id_hidden);
        }
    }


    public ExpenseRecyclerViewAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
        this.selectedItems = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Expense expensedetails = expenseList.get(position);
        holder.amount.setText(String.valueOf(expensedetails.getAmount()));
        holder.category.setText(expensedetails.getCategory());
        holder.type.setText(expensedetails.getType());
        if("Income".equals(expensedetails.getType())){
            holder.type.setTextColor(Color.parseColor("#00B33C"));
            holder.amount.setBackgroundResource(R.drawable.round_green_edittext);
        }
        else{
            holder.type.setTextColor(Color.RED);
            holder.amount.setBackgroundResource(R.drawable.round_red_edittext);
        }
        holder.date.setText(new SimpleDateFormat("dd-MMM-yyyy").format(expensedetails.getDate()));
        holder.idHidden.setText(String.valueOf(expensedetails.getId()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void toggleSelection(int pos) {
        System.out.println("In toggle");
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        System.out.println("Count selected : "+selectedItems.size());
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
}
