package com.expensemaster.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.Expense;
import com.expensemaster.Bean.FirstPageData;
import com.expensemaster.DAO.SQLiteDAOImpl;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class OverviewActivity extends AppCompatActivity {

    //private PieChart pie;
    private TextView dayExpenseAmount, weekExpenseAmount, monthExpenseAmount, dayIncomeAmount, weekIncomeAmount, monthIncomeAmount;
    private Button getExpenseButton, addCategoriesButton, exportButton, chartDisplayButton;
    private RelativeLayout dayExpenseLayout, weekExpenseLayout, monthExpenseLayout, dayIncomeLayout, weekIncomeLayout, monthIncomeLayout;
    int[] rainbow;

    SQLiteDAOImpl daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Expense Master");

        rainbow = getApplicationContext().getResources().getIntArray(R.array.rainbow);
        weekExpenseAmount = (TextView) findViewById(R.id.txt_week_expense_amount);
        dayExpenseAmount = (TextView) findViewById(R.id.txt_day_expense_amount);
        monthExpenseAmount = (TextView) findViewById(R.id.txt_month_expense_amount);
        dayIncomeAmount = (TextView) findViewById(R.id.txt_day_income_amount);
        weekIncomeAmount = (TextView) findViewById(R.id.txt_week_income_amount);
        monthIncomeAmount = (TextView) findViewById(R.id.txt_month_income_amount);
        exportButton = (Button) findViewById(R.id.btn_get_file);
        getExpenseButton = (Button) findViewById(R.id.btn_get_expense);
        addCategoriesButton = (Button)findViewById(R.id.btn_add_category);
        chartDisplayButton = (Button)findViewById(R.id.btn_get_chart);
        dayExpenseLayout = (RelativeLayout)findViewById(R.id.day_expense_layout);
        weekExpenseLayout = (RelativeLayout)findViewById(R.id.week_expense_layout);
        monthExpenseLayout = (RelativeLayout)findViewById(R.id.month_expense_layout);
        dayIncomeLayout = (RelativeLayout)findViewById(R.id.day_income_layout);
        weekIncomeLayout = (RelativeLayout)findViewById(R.id.week_income_layout);
        monthIncomeLayout = (RelativeLayout)findViewById(R.id.month_income_layout);

        daoImpl=new SQLiteDAOImpl(OverviewActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Income/Expense", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), AddExpenseActivity.class);
                intent.putExtra("flag", "New");
                startActivity(intent);
            }
        });

        chartDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChartsActivity.class);
                startActivity(intent);
            }
        });

        getExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String expense=daoImpl.getAllExpenses().toString();
                Intent intent = new Intent(getApplicationContext(), ExpenseRecyclerActivity.class);
                intent.putExtra("listFlag", "AllTransactionsList");
                startActivity(intent);
            }
        });

        addCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoriesListActivity.class);
                startActivity(intent);
            }
        });

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

        loadData();
        //piechartdisplay();

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar c = GregorianCalendar.getInstance();
                saveExcelFile(getApplicationContext(), "ExpenseMaster"+df.format(c.getTime())+ ".xlsx");
                AlertDialog.Builder builder =  new AlertDialog.Builder(OverviewActivity.this);
                builder.setTitle("File exported successfully!");
                builder.setMessage("File directory is : "+getApplicationContext().getExternalFilesDir(null).getAbsolutePath());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                Toast.makeText(getApplicationContext(),"File exported successfully!",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadData() {
        FirstPageData data = daoImpl.getLoadData();
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

    /*private void piechartdisplay() {
        pie = (PieChart) findViewById(R.id.mySimplePieChart);
        final float padding = PixelUtils.dpToPix(30);
        pie.getPie().setPadding(padding, padding, padding, padding);
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        List<Category> categoryList=daoImpl.getCategorywiseExpense();
        List<Segment> piesegmentList=new ArrayList<>();
        List<SegmentFormatter> piesegmentFormatterList=new ArrayList<>();
        //int[] colorse = {12,65536,256,16777216,91};
        for (Category element : categoryList) {
            // 1 - can call methods of element
            Segment segments= new Segment(element.getCategory(),element.getAmount());
            //SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);
            piesegmentList.add(segments);
            System.out.println("Category"+element.getCategory()+"Amount"+element.getAmount());
            //  count++;
            // ...
        }
        for(int i=0;i<categoryList.size();i++)
        {
            SegmentFormatter formatter=new SegmentFormatter(categoryColor[i]);
            formatter.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            formatter.getFillPaint().setMaskFilter(emf);
            piesegmentFormatterList.add(formatter);
        }

        for(int j=0;j<piesegmentFormatterList.size();j++)
        {
            pie.addSegment(piesegmentList.get(j),piesegmentFormatterList.get(j));
        }
        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), SearchTransactionActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        //piechartdisplay();
    }

    /*private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        System.out.println(getApplicationContext().getDatabasePath("ExpenseMaster.db"));
        //String currentDBPath = "/data/"+ "com.expensemaster.expensemaster" +"/databases/"+"ExpenseMaster.db";
        String currentDBPath = "/data/user/0/com.expensemaster.expensemaster/databases/ExpenseMaster.db";
        String backupDBPath = "ExpenseMaster";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(getApplicationContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    private void saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        /*if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }*/

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("All Transactions");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Transaction Id");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Type");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Category");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Description");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Amount");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Date");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 300));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 300));
        sheet1.setColumnWidth(4, (15 * 300));
        sheet1.setColumnWidth(5, (15 * 300));

        List<Expense> expenseList = new ArrayList<>();
        expenseList = daoImpl.getAllExpenses();

        SimpleDateFormat form = new SimpleDateFormat("dd-MMMM-yyyy");

        int i=1;
        for(Expense exp: expenseList ){
            row = sheet1.createRow(i);

            c = row.createCell(0);
            c.setCellValue(exp.getId());

            c = row.createCell(1);
            c.setCellValue(exp.getType());

            c = row.createCell(2);
            c.setCellValue(exp.getCategory());

            c = row.createCell(3);
            c.setCellValue(exp.getRemarks());

            c = row.createCell(4);
            c.setCellValue(exp.getAmount());

            System.out.println(form.format(exp.getDate()));
            c = row.createCell(5);
            c.setCellValue(form.format(exp.getDate()));
            i+=1;
        }



        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null).getAbsolutePath(), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
    }
}
