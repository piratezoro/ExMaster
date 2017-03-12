package com.expensemaster.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAOImpl;
import com.expensemaster.Supporting.DividerItemDecoration;
import com.expensemaster.Supporting.ExpenseRecyclerViewAdapter;
import com.expensemaster.Supporting.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRecyclerActivity extends AppCompatActivity{

    private List<Expense> expenseList = new ArrayList<>();
    private RecyclerView recyclerView;
    SQLiteDAOImpl daoImpl;
    private static ExpenseRecyclerViewAdapter mAdapter;
    int deleteID;
    Expense expense;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_recycler);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daoImpl=new SQLiteDAOImpl(ExpenseRecyclerActivity.this);
        Intent intent = getIntent();
        String listFlag = intent.getStringExtra("listFlag");
        if(listFlag.equalsIgnoreCase("AllTransactionsList")){
            setTitle("All Transactions");
            expenseList = daoImpl.getAllExpenses();
        }else if(listFlag.equalsIgnoreCase("DayExpenseList")){
            setTitle("Today's Expenses");
            expenseList = daoImpl.getDayExpenses();
        }else if(listFlag.equalsIgnoreCase("WeekExpenseList")){
            setTitle("Week's Expenses");
            expenseList = daoImpl.getWeekExpenses();
        }else if(listFlag.equalsIgnoreCase("MonthExpenseList")){
            setTitle("Month's Expenses");
            expenseList = daoImpl.getMonthExpenses();
        }else if(listFlag.equalsIgnoreCase("DayIncomeList")){
            setTitle("Today's Income");
            expenseList = daoImpl.getDayIncome();
        }else if(listFlag.equalsIgnoreCase("WeekIncomeList")){
            setTitle("Week's Income");
            expenseList = daoImpl.getWeekIncome();
        }else if(listFlag.equalsIgnoreCase("MonthIncomeList")){
            setTitle("Month's Income");
            expenseList = daoImpl.getMonthIncome();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ExpenseRecyclerViewAdapter(expenseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ExpenseRecyclerActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Delete");
        arrayAdapter.add("Edit");
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(ExpenseRecyclerActivity.this);
        builder1.setTitle("Options");
        builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = arrayAdapter.getItem(which);
                if (item.equalsIgnoreCase("Delete")) {
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(ExpenseRecyclerActivity.this);
                    builderInner.setTitle("Do you want to delete this transaction?");
                    builderInner.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            expenseList = daoImpl.deleteExpense(deleteID);
                            mAdapter = new ExpenseRecyclerViewAdapter(expenseList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    builderInner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();

                } else if (item.equalsIgnoreCase("Edit")) {
                    Intent intent = new Intent(getApplicationContext(), AddExpenseActivity.class);
                    intent.putExtra("flag", "Update");
                    intent.putExtra("expId", String.valueOf(expense.getId()));
                    intent.putExtra("expType", expense.getType());
                    intent.putExtra("expCategory", expense.getCategory());
                    intent.putExtra("expAmount", expense.getAmount().toString());
                    intent.putExtra("expDate", dateFormat.format(expense.getDate()));
                    intent.putExtra("expRemarks", expense.getRemarks());
                    finish();
                    startActivity(intent);
                }
            }
        });




        /*final String[] longClickItems = new String[]{"Delete"};
        final ListPopupWindow lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, longClickItems));
        lpw.setAnchorView(recyclerView);
        lpw.setModal(true);
        lpw.setHorizontalOffset(0);
        lpw.setVerticalOffset(-800);
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = longClickItems[position];
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                if (item.equalsIgnoreCase("Delete")) {
                    expenseList = daoImpl.deleteExpense(deleteID);
                    mAdapter = new ExpenseRecyclerViewAdapter(expenseList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                lpw.dismiss();
            }
        });*/

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Expense exp = expenseList.get(position);

                //Toast.makeText(getApplicationContext(), exp.getAmount() + " is selected!", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getApplicationContext(),AddExpenseActivity.class);
                intent.putExtra("flag","Update");
                intent.putExtra("expId", String.valueOf(exp.getId()));
                intent.putExtra("expType",exp.getType());
                intent.putExtra("expCategory",exp.getCategory());
                intent.putExtra("expAmount", exp.getAmount().toString());
                intent.putExtra("expDate", dateFormat.format(exp.getDate()));
                intent.putExtra("expRemarks", exp.getRemarks());
                finish();
                startActivity(intent);*/
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ExpenseRecyclerActivity.this);
                builder2.setTitle("Transaction Details");
                builder2.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), AddExpenseActivity.class);
                        intent.putExtra("flag", "Update");
                        intent.putExtra("expId", String.valueOf(exp.getId()));
                        intent.putExtra("expType", exp.getType());
                        intent.putExtra("expCategory", exp.getCategory());
                        intent.putExtra("expAmount", exp.getAmount().toString());
                        intent.putExtra("expDate", dateFormat.format(exp.getDate()));
                        intent.putExtra("expRemarks", exp.getRemarks());
                        finish();
                        startActivity(intent);
                    }
                });
                LayoutInflater inflater = getLayoutInflater();
                final View convertView = (View) inflater.inflate(R.layout.recycler_item_click_view, null);
                builder2.setView(convertView);
                TextView typeAlertText = (TextView) convertView.findViewById(R.id.alert_view_type_txt);
                TextView categoryAlertText = (TextView) convertView.findViewById(R.id.alert_view_category_txt);
                TextView descriptionAlertText = (TextView) convertView.findViewById(R.id.alert_view_description_txt);
                TextView amountAlertText = (TextView) convertView.findViewById(R.id.alert_view_amount_txt);
                TextView dateAlertText = (TextView) convertView.findViewById(R.id.alert_view_date_txt);
                typeAlertText.setText(exp.getType());
                categoryAlertText.setText(exp.getCategory());
                descriptionAlertText.setText(exp.getRemarks());
                amountAlertText.setText(String.valueOf(exp.getAmount()));
                dateAlertText.setText(dateFormat.format(exp.getDate()));
                builder2.create().show();
            }

            @Override
            public void onLongClick(View view, int position) {
                expense = expenseList.get(position);
                Toast.makeText(getApplicationContext(), expense.getAmount() + " long clicked", Toast.LENGTH_SHORT).show();
                deleteID = expenseList.get(position).getId();
                //lpw.show();
                builder1.create().show();

                //mAdapter.toggleSelection(position);
            }
        }));
    }
}
