package com.expensemaster.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.DAO.SQLiteDAOImpl;
import com.expensemaster.Management.ManagementBean;
import com.expensemaster.Supporting.Validator;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private Button submitExpense, submitNewExpense;
    private EditText txtCategory, txtAmount, txtRemarks, txtDate, txtIdHidden;
    private RadioGroup radioGroup;
    private RelativeLayout layoutAddExpense;
    private SQLiteDAO daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        setTitle("Add Transaction");

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");

        submitExpense = (Button) findViewById(R.id.btn_submit_expense);
        submitNewExpense = (Button) findViewById(R.id.btn_submit_new_expense);

        txtCategory = (EditText) findViewById(R.id.txt_category);
        txtAmount = (EditText) findViewById(R.id.txt_amount);
        txtRemarks = (EditText) findViewById(R.id.txt_remarks);
        txtDate = (EditText) findViewById(R.id.txt_date);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        txtIdHidden = (EditText) findViewById(R.id.txt_id_hidden);
        layoutAddExpense = (RelativeLayout) findViewById(R.id.layout_add_expense);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<String> categoriesList = daoImpl.getAllCategories();
        //final String[] items = new String[]{"Travel", "Food", "Shopping","Needs","Picnic","Trek","Office","Job","Timepass","Movie","Sports","12","13","14","15"};

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        System.out.println("Flag:"+flag);
        if("Update".equalsIgnoreCase(flag)){
            txtCategory.setText(intent.getStringExtra("expCategory"));
            txtAmount.setText(String.valueOf(intent.getStringExtra("expAmount")));
            txtRemarks.setText(intent.getStringExtra("expRemarks"));
            txtDate.setText(String.valueOf(intent.getStringExtra("expDate")));
            txtIdHidden.setText(String.valueOf(intent.getStringExtra("expId")));
            if(intent.getStringExtra("expType").equalsIgnoreCase("Income")){
                radioGroup.check(R.id.radio_income);
            }
            else{
                radioGroup.check(R.id.radio_expense);
            }
        }

        final ListPopupWindow lpw;
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categoriesList));
        lpw.setAnchorView(txtCategory);
        lpw.setModal(true);
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = categoriesList.get(position);
                txtCategory.setText(item);
                lpw.dismiss();
            }
        });

        txtCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lpw.show();
                }
            }
        });

    /*ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
    txtCategory.setAdapter(adapter);
    txtCategory.setThreshold(1);*/
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lpw.show();
            }
        });

        txtCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                lpw.show();
                return true;
                /*if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (v.getWidth() - ((EditText) v)
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        lpw.show();
                        return true;
                    }
                }*/
                //return false;
            }
        });

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int mYear, mMonth, mDay;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    DatePickerDialog dpd = new DatePickerDialog(AddExpenseActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txtDate.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                    txtDate.setError(null);
                                }
                            }, mYear, mMonth, mDay);
                    dpd.show();
                }
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                DatePickerDialog dpd = new DatePickerDialog(AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                txtDate.setError(null);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        if(radioGroup.getCheckedRadioButtonId()==-1){
            layoutAddExpense.setVisibility(View.INVISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                layoutAddExpense.setVisibility(View.VISIBLE);
            }
        });



        submitNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense expense = new Expense();
                if(!txtIdHidden.getText().toString().isEmpty()){
                    expense.setId(Integer.parseInt(txtIdHidden.getText().toString()));
                }
                RadioButton typeRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                expense.setType(typeRadioButton.getText().toString());
                expense.setCategory(txtCategory.getText().toString());
                expense.setAmount(Float.parseFloat(txtAmount.getText().toString()));
                expense.setRemarks(txtRemarks.getText().toString());
                try {
                    expense.setDate(formatter.parse(txtDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                daoImpl.addExpense(expense);
                Toast.makeText(AddExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
                radioGroup.check(0);
                txtCategory.setText(null);
                txtAmount.setText(null);
                txtRemarks.setText(null);
                txtDate.setText(null);
            }
        });

        submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(Validator.hasText(txtCategory));

                if(Validator.hasText(txtCategory) && Validator.hasText(txtAmount) && Validator.hasText(txtRemarks) && Validator.hasText(txtDate)) {
                    Expense expense = new Expense();
                    if (!txtIdHidden.getText().toString().isEmpty()) {
                        expense.setId(Integer.parseInt(txtIdHidden.getText().toString()));
                    }
                    RadioButton typeRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    expense.setType(typeRadioButton.getText().toString());
                    expense.setCategory(txtCategory.getText().toString());
                    expense.setAmount(Float.parseFloat(txtAmount.getText().toString()));
                    expense.setRemarks(txtRemarks.getText().toString());
                    try {
                        expense.setDate(formatter.parse(txtDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    daoImpl.addExpense(expense);
                    Toast.makeText(AddExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
                    radioGroup.check(0);
                    txtCategory.setText(null);
                    txtAmount.setText(null);
                    txtRemarks.setText(null);
                    txtDate.setText(null);

                    Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
