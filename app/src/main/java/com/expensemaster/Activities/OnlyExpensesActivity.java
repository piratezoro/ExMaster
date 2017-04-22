package com.expensemaster.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.ExpensePage;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.Management.ManagementBean;
import com.expensemaster.Supporting.CategoriesAmountListViewAdapter;

import java.util.List;

public class OnlyExpensesActivity extends AppCompatActivity {

    private SQLiteDAO daoImpl;
    private TextView dayExpenseAmount, weekExpenseAmount, monthExpenseAmount;
    private RelativeLayout dayExpenseLayout, weekExpenseLayout, monthExpenseLayout;
    ListView listCategoriesAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_expenses);

        weekExpenseAmount = (TextView) findViewById(R.id.txt_week_expense_amount);
        dayExpenseAmount = (TextView) findViewById(R.id.txt_day_expense_amount);
        monthExpenseAmount = (TextView) findViewById(R.id.txt_month_expense_amount);
        dayExpenseLayout = (RelativeLayout)findViewById(R.id.day_expense_layout);
        weekExpenseLayout = (RelativeLayout)findViewById(R.id.week_expense_layout);
        monthExpenseLayout = (RelativeLayout)findViewById(R.id.month_expense_layout);
        listCategoriesAmount = (ListView) findViewById(R.id.all_categories_amount_list);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());

        loadData();

        dayExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "DayExpenseList");
                startActivity(intent);
            }
        });

        weekExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "WeekExpenseList");
                startActivity(intent);
            }
        });


        monthExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "MonthExpenseList");
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        ExpensePage data = daoImpl.getExpenseData();
        dayExpenseAmount.setText(data.getCurrentDayExpense());
        if(data.getCurrentDayExpense() == null){
            dayExpenseAmount.setText("0");
        }
        weekExpenseAmount.setText(data.getCurrentWeekExpense());
        if(data.getCurrentWeekExpense() == null){
            weekExpenseAmount.setText("0");
        }
        monthExpenseAmount.setText(data.getCurrentMonthExpense());
        if(data.getCurrentMonthExpense() == null){
            monthExpenseAmount.setText("0");
        }

        List<Category> categoriesAmountList = daoImpl.getCategorywiseExpense();
        final CategoriesAmountListViewAdapter categoriesListViewAdapter = new CategoriesAmountListViewAdapter(getApplication(), categoriesAmountList);
        listCategoriesAmount.setAdapter(categoriesListViewAdapter);
    }
}
