package com.expensemaster.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAOImpl;
import com.expensemaster.Supporting.ExpenseListViewAdapter;

import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    List<Expense> expenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        final ListView listExpenses = (ListView) findViewById(R.id.expense_list);

        final SQLiteDAOImpl daoImpl=new SQLiteDAOImpl(ExpenseListActivity.this);
        expenses = daoImpl.getAllExpenses();

        ExpenseListViewAdapter expenseListViewAdapter = new ExpenseListViewAdapter(ExpenseListActivity.this, expenses);
        listExpenses.setAdapter(expenseListViewAdapter);

        listExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense exp = (Expense) listExpenses.getItemAtPosition(position);
                Toast.makeText(ExpenseListActivity.this, "Category : " + exp.getCategory() + " Amount : " + exp.getAmount() + " Remarks : " + exp.getRemarks(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
