package com.expensemaster.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expensemaster.Bean.IncomePage;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.DAO.SQLiteDAOImpl;
import com.expensemaster.Management.ManagementBean;

public class OnlyIncomeActivity extends AppCompatActivity {

    private TextView dayIncomeAmount, weekIncomeAmount, monthIncomeAmount;
    private RelativeLayout dayIncomeLayout, weekIncomeLayout, monthIncomeLayout;
    SQLiteDAO daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_income);

        dayIncomeAmount = (TextView) findViewById(R.id.txt_day_income_amount);
        weekIncomeAmount = (TextView) findViewById(R.id.txt_week_income_amount);
        monthIncomeAmount = (TextView) findViewById(R.id.txt_month_income_amount);
        dayIncomeLayout = (RelativeLayout)findViewById(R.id.day_income_layout);
        weekIncomeLayout = (RelativeLayout)findViewById(R.id.week_income_layout);
        monthIncomeLayout = (RelativeLayout)findViewById(R.id.month_income_layout);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());

        loadData();

        dayIncomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "DayIncomeList");
                startActivity(intent);
            }
        });

        weekIncomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "WeekIncomeList");
                startActivity(intent);
            }
        });

        monthIncomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "MonthIncomeList");
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        IncomePage data = daoImpl.getIncomeData();
        dayIncomeAmount.setText(data.getCurrentDayIncome());
        if(data.getCurrentDayIncome() == null){
            dayIncomeAmount.setText("0");
        }
        weekIncomeAmount.setText(data.getCurrentWeekIncome());
        if(data.getCurrentWeekIncome() == null){
            weekIncomeAmount.setText("0");
        }
        monthIncomeAmount.setText(data.getCurrentMonthIncome());
        if(data.getCurrentMonthIncome() == null){
            monthIncomeAmount.setText("0");
        }
    }
}
