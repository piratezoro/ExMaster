package com.expensemaster.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.Management.ManagementBean;
import com.expensemaster.Supporting.DividerItemDecoration;
import com.expensemaster.Supporting.ExpenseRecyclerViewAdapter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchTransactionActivity extends AppCompatActivity {

    Button btnSearch;
    EditText txtFrom, txtTo;
    RecyclerView searchRecyclerView;
    private static ExpenseRecyclerViewAdapter mAdapter;
    private List<Expense> expenseList = new ArrayList<>();
    private SQLiteDAO daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transaction);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        txtFrom = (EditText) findViewById(R.id.txt_from);
        txtTo = (EditText) findViewById(R.id.txt_to);
        btnSearch = (Button) findViewById(R.id.btn_search);
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        searchRecyclerView.setLayoutManager(mLayoutManager);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        txtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog dpd = new DatePickerDialog(SearchTransactionActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txtFrom.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
                return false;
            }
        });

        txtTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog dpd = new DatePickerDialog(SearchTransactionActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txtTo.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDate = null, toDate = null;
                mAdapter = new ExpenseRecyclerViewAdapter(expenseList);
                searchRecyclerView.setAdapter(mAdapter);
                try {
                    fromDate = df.format(formatter.parse(txtFrom.getText().toString()));
                    toDate = df.format(formatter.parse(txtTo.getText().toString()));
                    System.out.println("dates are as follows"+fromDate+"="+toDate);
             /*       if(fromDate.equals(null))
                    {

                        System.out.println("mai ayay aya aya");
                        AlertDialog.Builder builder =  new AlertDialog.Builder(SearchTransactionActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("No Transaction found");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
             */
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                expenseList = daoImpl.searchTransactions(fromDate, toDate);

                mAdapter = new ExpenseRecyclerViewAdapter(expenseList);
                searchRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }
        });


    }
}
