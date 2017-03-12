package com.expensemaster.Supporting;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemaster.Activities.R;
import com.expensemaster.Bean.Expense;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Aditya on 1/8/2017.
 */
public class CategoriesListViewAdapter extends BaseAdapter {

    Context context;
    List<String> rowItems;
    private SparseBooleanArray mSelectedItemsIds;

    public CategoriesListViewAdapter(Context context, List<String> rowItems){
        this.context = context;
        this.rowItems = rowItems;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public class ViewHolder{
        TextView category;
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
            convertView = mInflater.inflate(R.layout.categories_list_row_item, null);
            holder = new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.categories_lst_category);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        String cat = (String)getItem(position);
        holder.category.setText(cat);
        return convertView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)

            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
