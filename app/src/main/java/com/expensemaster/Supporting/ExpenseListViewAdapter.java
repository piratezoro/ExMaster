package com.expensemaster.Supporting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expensemaster.Activities.R;
import com.expensemaster.Bean.Expense;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Aditya on 1/8/2017.
 */
public class ExpenseListViewAdapter extends BaseAdapter {

    Context context;
    List<Expense> rowItems;

    public ExpenseListViewAdapter(Context context,List<Expense> rowItems){
        this.context = context;
        this.rowItems = rowItems;
    }

    public class ViewHolder{
        TextView date;
        TextView category;
        TextView amount;
        TextView remarks;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.expense_list_row_item, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.lst_date);
            holder.category = (TextView) convertView.findViewById(R.id.lst_category);
            holder.amount = (TextView) convertView.findViewById(R.id.lst_amount);
            holder.remarks = (TextView) convertView.findViewById(R.id.lst_remark);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Expense rowItem = (Expense)getItem(position);
        holder.date.setText(new SimpleDateFormat("dd-MMM-yyyy").format(rowItem.getDate()));
        holder.category.setText(rowItem.getCategory());
        holder.remarks.setText(rowItem.getRemarks());
        holder.amount.setText(rowItem.getAmount().toString());
        return convertView;
    }
}
