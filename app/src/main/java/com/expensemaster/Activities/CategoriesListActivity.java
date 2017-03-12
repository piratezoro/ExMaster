package com.expensemaster.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAOImpl;
import com.expensemaster.Supporting.CategoriesListViewAdapter;
import com.expensemaster.Supporting.ExpenseListViewAdapter;

import java.util.List;

public class CategoriesListActivity extends AppCompatActivity {

    String longClickedCategoryItem;
    List<String> categoriesList;

    private ListView listCategories;
    private Button addCategoryButton;
    private EditText categoryText;
    private SQLiteDAOImpl daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Categories");

        listCategories = (ListView)findViewById(R.id.categories_list);
        addCategoryButton = (Button)findViewById(R.id.btn_add_category_to_list);
        categoryText = (EditText)findViewById(R.id.txt_add_category);
        daoImpl = new SQLiteDAOImpl(getApplicationContext());

        categoriesList = daoImpl.getAllCategories();
        final CategoriesListViewAdapter categoriesListViewAdapter = new CategoriesListViewAdapter(getApplication(), categoriesList);
        listCategories.setAdapter(categoriesListViewAdapter);

        /*listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) listCategories.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Category : " + category, Toast.LENGTH_SHORT).show();
            }
        });*/

        final String[] longClickItems = new String[]{"Delete"};
        final ListPopupWindow lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, longClickItems));
        lpw.setAnchorView(listCategories);
        lpw.setModal(true);
        lpw.setHorizontalOffset(0);
        lpw.setVerticalOffset(-800);
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = longClickItems[position];
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                if (item.equalsIgnoreCase("Delete")) {
                    categoriesList = daoImpl.deleteCategory(longClickedCategoryItem);
                    CategoriesListViewAdapter expenseListViewAdapter = new CategoriesListViewAdapter(getApplication(), categoriesList);
                    listCategories.setAdapter(expenseListViewAdapter);
                }
                lpw.dismiss();
            }
        });

        /*listCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedCategoryItem = (String) listCategories.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), longClickedCategoryItem, Toast.LENGTH_SHORT).show();
                lpw.show();
                return true;
            }
        });*/

        listCategories.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listCategories.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //CategoriesListActivity.this.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0a0a0a")));
                final int checkedCount = listCategories.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                categoriesListViewAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_multiselect_categories_list, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = categoriesListViewAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size()-1 ); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                String selecteditem = (String)categoriesListViewAdapter.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                System.out.println(selecteditem);
                                categoriesList = daoImpl.deleteCategory(selecteditem);

                            }
                        }
                        CategoriesListViewAdapter expenseListViewAdapter = new CategoriesListViewAdapter(getApplication(), categoriesList);
                        listCategories.setAdapter(expenseListViewAdapter);
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                categoriesListViewAdapter.removeSelection();
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesList = daoImpl.addCategory(categoryText.getText().toString());
                CategoriesListViewAdapter expenseListViewAdapter = new CategoriesListViewAdapter(getApplication(), categoriesList);
                listCategories.setAdapter(expenseListViewAdapter);
                categoryText.setText(null);
            }
        });
    }

    private void deleteCategory(){

    }
}
