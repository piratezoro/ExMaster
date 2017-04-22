package com.expensemaster.Supporting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expensemaster.Activities.R;
import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.Expense;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Aditya on 1/8/2017.
 */
public class CategoriesAmountListViewAdapter extends BaseAdapter {

    Context context;
    List<Category> rowItems;

    public CategoriesAmountListViewAdapter(Context context, List<Category> rowItems){
        this.context = context;
        this.rowItems = rowItems;
    }

    public class ViewHolder{
        TextView category;
        TextView amount;
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
            convertView = mInflater.inflate(R.layout.categories_amount_list_row_item, null);
            holder = new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.categories_amount_lst_category);
            holder.amount = (TextView) convertView.findViewById(R.id.categories_amount_lst_amount);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Category rowItem = (Category)getItem(position);
        holder.category.setText(rowItem.getCategory());
        holder.amount.setText(String.valueOf(rowItem.getAmount()));
        return convertView;
    }
}
