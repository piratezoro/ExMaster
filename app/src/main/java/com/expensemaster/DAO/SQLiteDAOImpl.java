package com.expensemaster.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.Expense;
import com.expensemaster.Bean.ExpensePage;
import com.expensemaster.Bean.FirstPageData;
import com.expensemaster.Bean.IncomePage;
import com.expensemaster.Bean.Quote;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aditya on 1/7/2017.
 */
public class SQLiteDAOImpl extends android.database.sqlite.SQLiteOpenHelper implements SQLiteDAO {

    private static final String DATABASE_NAME = "ExpenseMaster";
    private static final String EXPENSE_TABLE_NAME = "Expense";
    private static final String CATEGORIES_TABLE_NAME = "Categories";
    private static final String QUOTES_TABLE_NAME = "Quotes";

    private static final int DATABASE_VERSION = 1;

    private static final String key_id = "Id";
    private static final String category = "Category";
    private static final String amount = "Amount";
    private static final String remarks = "Remarks";
    private static final String date = "Date";
    private static final String type = "Type";
    private static final String quotes = "Quotes";
    private static final String authorname = "Name";

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public SQLiteDAOImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String expenseTableQuery = "CREATE TABLE " + EXPENSE_TABLE_NAME +
                " ("+key_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +category +" TEXT, "
                +amount+ " INTEGER, "
                +remarks+ " TEXT, "
                +date+ " DATE,"
                +type+ " TEXT);";
        db.execSQL(expenseTableQuery);
        System.out.println("11111111111111111111111111" + expenseTableQuery);

        String categoriesTableQuery = "CREATE TABLE " + CATEGORIES_TABLE_NAME +
                " ("+key_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +category +" TEXT);";
        db.execSQL(categoriesTableQuery);
        System.out.println("11111111111111111111111111" + categoriesTableQuery);

        String QuotesTableQuery = "CREATE TABLE " + QUOTES_TABLE_NAME +
                " ("+key_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +quotes +" TEXT,"+authorname+" TEXT);";
        db.execSQL(QuotesTableQuery);
        insertquotes(db);



        System.out.println("11111111111111111111111111" + QuotesTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    @Override
    public FirstPageData getLoadData() {
        try {
            FirstPageData data = new FirstPageData();
            String startDateOfWeek = "", endDateOfWeek = "", currentDate = "", startDateOfMonth = "", endDateOfMonth = "";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SQLiteDatabase db = this.getReadableDatabase();

            Calendar c = GregorianCalendar.getInstance();
            currentDate = df.format(c.getTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDateOfWeek = df.format(c.getTime());
            c.add(Calendar.DATE, 6);
            endDateOfWeek = df.format(c.getTime());
            c = GregorianCalendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
            startDateOfMonth = df.format(c.getTime());
            c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDateOfMonth = df.format(c.getTime());
            System.out.println("Start Date of current week = " + startDateOfWeek);
            System.out.println("End Date of current week = " + endDateOfWeek);
            System.out.println("Current date = " + currentDate);
            System.out.println("Start Date of current month = " + startDateOfMonth);
            System.out.println("End Date of current month = " + endDateOfMonth);

            String weekExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                    +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "';";

            System.out.println(weekExpenseQuery);
            Cursor cursor1= db.rawQuery(weekExpenseQuery, null);
            if (cursor1.moveToFirst())
            {
                do
                {
                    System.out.println(cursor1.getString(cursor1.getColumnIndex("SUM_AMT")));
                    data.setCurrentWeekExpense(cursor1.getString(cursor1.getColumnIndex("SUM_AMT")));
                }while(cursor1.moveToNext());
            }

            String weekIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                    +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "';";
            System.out.println(weekIncomeQuery);
            Cursor cursor2= db.rawQuery(weekIncomeQuery, null);
            if (cursor2.moveToFirst())
            {
                do
                {
                    System.out.println(cursor2.getString(cursor2.getColumnIndex("SUM_AMT")));
                    data.setCurrentWeekIncome(cursor2.getString(cursor2.getColumnIndex("SUM_AMT")));
                }while(cursor2.moveToNext());
            }

            String dayIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date = '"
                    + currentDate +"';";
            System.out.println(dayIncomeQuery);
            Cursor cursor3= db.rawQuery(dayIncomeQuery, null);
            if (cursor3.moveToFirst())
            {
                do
                {
                    System.out.println(cursor3.getString(cursor3.getColumnIndex("SUM_AMT")));
                    data.setCurrentDayIncome(cursor3.getString(cursor3.getColumnIndex("SUM_AMT")));
                }while(cursor3.moveToNext());
            }

            String dayExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date = '"
                    + currentDate +"';";
            System.out.println(dayExpenseQuery);
            Cursor cursor4= db.rawQuery(dayExpenseQuery, null);
            if (cursor4.moveToFirst())
            {
                do
                {
                    System.out.println(cursor4.getString(cursor4.getColumnIndex("SUM_AMT")));
                    data.setCurrentDayExpense(cursor4.getString(cursor4.getColumnIndex("SUM_AMT")));
                }while(cursor4.moveToNext());
            }

            String monthExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                    +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "';";
            System.out.println(monthExpenseQuery);
            Cursor cursor5= db.rawQuery(monthExpenseQuery, null);
            if (cursor5.moveToFirst())
            {
                do
                {
                    System.out.println(cursor5.getString(cursor5.getColumnIndex("SUM_AMT")));
                    data.setCurrentMonthExpense(cursor5.getString(cursor5.getColumnIndex("SUM_AMT")));
                }while(cursor5.moveToNext());
            }

            String monthIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                    +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "';";
            System.out.println(monthIncomeQuery);
            Cursor cursor6= db.rawQuery(monthIncomeQuery, null);
            if (cursor6.moveToFirst())
            {
                do
                {
                    System.out.println(cursor6.getString(cursor6.getColumnIndex("SUM_AMT")));
                    data.setCurrentMonthIncome(cursor6.getString(cursor6.getColumnIndex("SUM_AMT")));
                }while(cursor6.moveToNext());
            }

            db.close();
            return data;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> getCategorywiseExpense() {
        System.out.println("In getCategorywiseExpense DAO method");
        SQLiteDatabase db = this.getReadableDatabase();
        List<Category> categoriesList = new ArrayList<Category>();
        String selectquery= "SELECT Category,sum(Amount) FROM "+ EXPENSE_TABLE_NAME+" WHERE "+type+" = 'Expense' GROUP BY "+category +";";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Category genericcategies = new Category();
                genericcategies.setCategory(cursor.getString(0));
                genericcategies.setAmount(cursor.getFloat(1));
                categoriesList.add(genericcategies);
            }while(cursor.moveToNext());
        }
        db.close();
        return  categoriesList;
    }

    @Override
    public void addExpense(Expense expense) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            if(expense.getId()==0){
                values.put(category, expense.getCategory());
                values.put(amount, expense.getAmount());
                values.put(remarks, expense.getRemarks());
                values.put(date,new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate()));
                values.put(type, expense.getType());
                System.out.println("Inserting");
                db.insert("Expense", null, values);
                System.out.println("After Insert");
            }
            else{
                System.out.println("Expense Id :"+expense.getId());
                values.put(category, expense.getCategory());
                values.put(amount, expense.getAmount());
                values.put(remarks, expense.getRemarks());
                values.put(date,new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate()));
                values.put(type, expense.getType());
                System.out.println("Updating");
                db.update("Expense",values,key_id+"="+expense.getId(),null);
                System.out.println("After Update");
            }
            db.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<Expense> getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getDayExpenses() {
        String currentDate = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        currentDate = df.format(c.getTime());
        System.out.println("Current date = " + currentDate);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date = '"
                + currentDate +"' ORDER BY "+date +" DESC;";
        /*String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "' ORDER BY "+date +" DESC;";*/
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getWeekExpenses() {
        String startDateOfWeek = "", endDateOfWeek = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        startDateOfWeek = df.format(c.getTime());
        c.add(Calendar.DATE, 6);
        endDateOfWeek = df.format(c.getTime());
        System.out.println("Start Date of current week = " + startDateOfWeek);
        System.out.println("End Date of current week = " + endDateOfWeek);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "' ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getMonthExpenses() {
        String startDateOfMonth = "", endDateOfMonth = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
        startDateOfMonth = df.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDateOfMonth = df.format(c.getTime());
        System.out.println("Start Date of current month = " + startDateOfMonth);
        System.out.println("End Date of current month = " + endDateOfMonth);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "' ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getDayIncome() {
        String currentDate = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        currentDate = df.format(c.getTime());
        System.out.println("Current date = " + currentDate);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date = '"
                + currentDate +"' ORDER BY "+date +" DESC;";
        /*String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "' ORDER BY "+date +" DESC;";*/
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getWeekIncome() {
        String startDateOfWeek = "", endDateOfWeek = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        startDateOfWeek = df.format(c.getTime());
        c.add(Calendar.DATE, 6);
        endDateOfWeek = df.format(c.getTime());
        System.out.println("Start Date of current week = " + startDateOfWeek);
        System.out.println("End Date of current week = " + endDateOfWeek);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "' ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> getMonthIncome() {
        String startDateOfMonth = "", endDateOfMonth = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
        startDateOfMonth = df.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDateOfMonth = df.format(c.getTime());
        System.out.println("Start Date of current month = " + startDateOfMonth);
        System.out.println("End Date of current month = " + endDateOfMonth);

        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "' ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<Expense> deleteExpense(int idHidden) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(EXPENSE_TABLE_NAME, key_id+"=?", new String[] {String.valueOf(idHidden)});
        db.close();

        db = this.getReadableDatabase();
        List<Expense> expenses=new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" ORDER BY "+date +" DESC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                System.out.println("11111111111111111" + cursor.getInt(0));
                System.out.println("22222222222222222222222222" + exp.getId());
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
            }while(cursor.moveToNext());
        }
        db.close();
        return  expenses;
    }

    @Override
    public List<String> addCategory(String cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> categoriesList = new ArrayList<String>();
        ContentValues values = new ContentValues();
        values.put(category, cat);
        System.out.println("Inserting");
        db.insert("Categories", null, values);
        System.out.println("After Insert");
        db.close();

        db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM "+ CATEGORIES_TABLE_NAME +" ORDER BY "+category +" ASC;";
        Cursor cursor= db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categoriesList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return categoriesList;
    }

    @Override
    public List<String> getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> categoriesList = new ArrayList<String>();
        String selectquery= "SELECT * FROM "+ CATEGORIES_TABLE_NAME +" ORDER BY "+category +" ASC;";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                categoriesList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return categoriesList;
    }

    @Override
    public List<String> deleteCategory(String cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORIES_TABLE_NAME, category + "=?", new String[]{cat});
        db.close();

        List<String> categoriesList = new ArrayList<String>();
        db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM "+ CATEGORIES_TABLE_NAME +" ORDER BY "+category +" ASC;";
        System.out.println(selectQuery);
        Cursor cursor= db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categoriesList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return categoriesList;
    }

    @Override
    public List<Expense> searchTransactions(String from, String to) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses = new ArrayList<Expense>();
        String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE date"
                +" between '" + from+"' and '" +to+ "' ORDER BY "+date +" DESC;";
        System.out.println(selectquery);
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(cursor.getInt(0));
                exp.setCategory(cursor.getString(1));
                exp.setAmount(cursor.getFloat(2));
                exp.setRemarks(cursor.getString(3));
                try {
                    System.out.println(cursor.getString(4));
                    exp.setDate(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exp.setType(cursor.getString(5));
                expenses.add(exp);
                System.out.println(exp.getDate() + " - " + exp.getAmount());
            }while(cursor.moveToNext());
        }
        db.close();
        return expenses;
    }
    @Override
    public List<Category> getExpense(String from, String to) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Category> categoryList = new ArrayList<Category>();
        /*String selectquery= "SELECT * FROM "+ EXPENSE_TABLE_NAME+" WHERE date"
                +" between '" + from+"' and '" +to+ "' ORDER BY "+date +" DESC;";*/
        String selectquery= "SELECT Category,sum(Amount) FROM "+ EXPENSE_TABLE_NAME+" WHERE "+type+" = 'Expense' AND date " +
                " between '"+from+"' and '"+to + "' GROUP BY "+category +";";

        System.out.println(selectquery);
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategory(cursor.getString(0));
                category.setAmount(cursor.getFloat(1));

                categoryList.add(category);
               System.out.println(" -" + from +to);
            }while(cursor.moveToNext());
        }
        db.close();
        return categoryList;
    }

    @Override
    public ExpensePage getExpenseData() {
        ExpensePage expensePage = new ExpensePage();
        try {
            String startDateOfWeek = "", endDateOfWeek = "", currentDate = "", startDateOfMonth = "", endDateOfMonth = "";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SQLiteDatabase db = this.getReadableDatabase();

            Calendar c = GregorianCalendar.getInstance();
            currentDate = df.format(c.getTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDateOfWeek = df.format(c.getTime());
            c.add(Calendar.DATE, 6);
            endDateOfWeek = df.format(c.getTime());
            c = GregorianCalendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
            startDateOfMonth = df.format(c.getTime());
            c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDateOfMonth = df.format(c.getTime());
            System.out.println("Start Date of current week = " + startDateOfWeek);
            System.out.println("End Date of current week = " + endDateOfWeek);
            System.out.println("Current date = " + currentDate);
            System.out.println("Start Date of current month = " + startDateOfMonth);
            System.out.println("End Date of current month = " + endDateOfMonth);

            String weekExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                    +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "';";

            System.out.println(weekExpenseQuery);
            Cursor cursor1= db.rawQuery(weekExpenseQuery, null);
            if (cursor1.moveToFirst())
            {
                do
                {
                    System.out.println(cursor1.getString(cursor1.getColumnIndex("SUM_AMT")));
                    expensePage.setCurrentWeekExpense(cursor1.getString(cursor1.getColumnIndex("SUM_AMT")));
                }while(cursor1.moveToNext());
            }

            String dayExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date = '"
                    + currentDate +"';";
            System.out.println(dayExpenseQuery);
            Cursor cursor4= db.rawQuery(dayExpenseQuery, null);
            if (cursor4.moveToFirst())
            {
                do
                {
                    System.out.println(cursor4.getString(cursor4.getColumnIndex("SUM_AMT")));
                    expensePage.setCurrentDayExpense(cursor4.getString(cursor4.getColumnIndex("SUM_AMT")));
                }while(cursor4.moveToNext());
            }

            String monthExpenseQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Expense' AND date"
                    +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "';";
            System.out.println(monthExpenseQuery);
            Cursor cursor5= db.rawQuery(monthExpenseQuery, null);
            if (cursor5.moveToFirst())
            {
                do
                {
                    System.out.println(cursor5.getString(cursor5.getColumnIndex("SUM_AMT")));
                    expensePage.setCurrentMonthExpense(cursor5.getString(cursor5.getColumnIndex("SUM_AMT")));
                }while(cursor5.moveToNext());
            }

            db.close();
            return expensePage;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public IncomePage getIncomeData() {
        IncomePage incomePage = new IncomePage();
        try {
            String startDateOfWeek = "", endDateOfWeek = "", currentDate = "", startDateOfMonth = "", endDateOfMonth = "";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SQLiteDatabase db = this.getReadableDatabase();

            Calendar c = GregorianCalendar.getInstance();
            currentDate = df.format(c.getTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDateOfWeek = df.format(c.getTime());
            c.add(Calendar.DATE, 6);
            endDateOfWeek = df.format(c.getTime());
            c = GregorianCalendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
            startDateOfMonth = df.format(c.getTime());
            c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDateOfMonth = df.format(c.getTime());
            System.out.println("Start Date of current week = " + startDateOfWeek);
            System.out.println("End Date of current week = " + endDateOfWeek);
            System.out.println("Current date = " + currentDate);
            System.out.println("Start Date of current month = " + startDateOfMonth);
            System.out.println("End Date of current month = " + endDateOfMonth);

            String weekIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                    +" between '" + startDateOfWeek+"' and '" +endDateOfWeek+ "';";
            System.out.println(weekIncomeQuery);
            Cursor cursor2= db.rawQuery(weekIncomeQuery, null);
            if (cursor2.moveToFirst())
            {
                do
                {
                    System.out.println(cursor2.getString(cursor2.getColumnIndex("SUM_AMT")));
                    incomePage.setCurrentWeekIncome(cursor2.getString(cursor2.getColumnIndex("SUM_AMT")));
                }while(cursor2.moveToNext());
            }

            String dayIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date = '"
                    + currentDate +"';";
            System.out.println(dayIncomeQuery);
            Cursor cursor3= db.rawQuery(dayIncomeQuery, null);
            if (cursor3.moveToFirst())
            {
                do
                {
                    System.out.println(cursor3.getString(cursor3.getColumnIndex("SUM_AMT")));
                    incomePage.setCurrentDayIncome(cursor3.getString(cursor3.getColumnIndex("SUM_AMT")));
                }while(cursor3.moveToNext());
            }

            String monthIncomeQuery= "SELECT SUM("+amount+") AS SUM_AMT FROM "+ EXPENSE_TABLE_NAME+" WHERE "+ type +" = 'Income' AND date"
                    +" between '" + startDateOfMonth+"' and '" +endDateOfMonth+ "';";
            System.out.println(monthIncomeQuery);
            Cursor cursor6= db.rawQuery(monthIncomeQuery, null);
            if (cursor6.moveToFirst())
            {
                do
                {
                    System.out.println(cursor6.getString(cursor6.getColumnIndex("SUM_AMT")));
                    incomePage.setCurrentMonthIncome(cursor6.getString(cursor6.getColumnIndex("SUM_AMT")));
                }while(cursor6.moveToNext());
            }

            db.close();
            return incomePage;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Quote> getQuotes()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Quote> quotes=new ArrayList<Quote>();
        String selectquery= "SELECT * FROM "+ QUOTES_TABLE_NAME+";";
        Cursor cursor= db.rawQuery(selectquery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Quote quote = new Quote();
                quote.setId(cursor.getInt(0));
                quote.setQuote(cursor.getString(1));
                quote.setAuthorname(cursor.getString(2));



                quotes.add(quote);
            }while(cursor.moveToNext());
        }
        db.close();
        return  quotes;
    }

    @Override
    public Quote gettodayQuote(int date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Quote quote = new Quote();
        String selectquery= "SELECT * FROM "+ QUOTES_TABLE_NAME+ " WHERE Id ="+date+";";
        Cursor cursor= db.rawQuery(selectquery, null);

       if (cursor.moveToFirst())
        {
            do
            {

                quote.setId(cursor.getInt(0));
                quote.setQuote(cursor.getString(1));
                quote.setAuthorname(cursor.getString(2));




            }while(cursor.moveToNext());
        }
        db.close();
        return  quote;
    }

    public void insertquotes(SQLiteDatabase db)
    {

        try {
            ContentValues values = new ContentValues();
            System.out.println("After Insert quotes hahaha");
            values.put(quotes, "Every time you borrow money, you're robbing your future self.");
            values.put(authorname, "--N. W. Morris");
            db.insert("Quotes", null, values);

            values.put(quotes, "Never spend your money before you have it.");
            values.put(authorname, "--T. Jefferson");
            db.insert("Quotes", null, values);

            values.put(quotes, "Wealth is not his that has it, but his that enjoys it.");
            values.put(authorname, "--Benjamin Franklin");
            db.insert("Quotes", null, values);

            values.put(quotes, "Don't tell me what you value, show me your budget, and I'll tell you what you value.");
            values.put(authorname, "--Joe Biden");
            db.insert("Quotes", null, values);
//5
            values.put(quotes, "If you live for having it all, what you have is never enough.");
            values.put(authorname, "--Vicki Robin");
            db.insert("Quotes", null, values);

            values.put(quotes, "We make a living by what we get, but we make a life by what we give.");
            values.put(authorname, "--Winston Churchill");
            db.insert("Quotes", null, values);

            values.put(quotes, "Not everything that can be counted counts, and not everything that counts can be counted.");
            values.put(authorname, "--Albert Einstein");
            db.insert("Quotes", null, values);

            values.put(quotes, "Screw it, Let''s do it!");
            values.put(authorname, "--Richard Branson");
            db.insert("Quotes", null, values);

            values.put(quotes, "If plan A fails, remember there are 25 more letters.");
            values.put(authorname, "--Chris Guillebeau");
            db.insert("Quotes", null, values);

            values.put(quotes, "Believe you can and youâ€™re halfway there.");
            values.put(authorname, "--Theodore Roosevelt");
            db.insert("Quotes", null, values);

            values.put(quotes, "If we command our wealth, we shall be rich and free. If our wealth commands us, we are poor indeed.");
            values.put(authorname, "--Edmund Burke");
            db.insert("Quotes", null, values);

            values.put(quotes, "No wealth can ever make a bad man at peace with himself.");
            values.put(authorname, "--Plato");
            db.insert("Quotes", null, values);

            values.put(quotes, "I'd like to live as a poor man with lots of money.");
            values.put(authorname, "--Pablo Picasso");
            db.insert("Quotes", null, values);

            values.put(quotes, "You can be young without money, but you can't be old without it.");
            values.put(authorname, "--Tennessee Williams");
            db.insert("Quotes", null, values);
//15
            values.put(quotes, "The best thing money can buy is financial freedom.");
            values.put(authorname, "--Ankit");
            db.insert("Quotes", null, values);

            values.put(quotes, "Rule No 1: Never lose money.Rule No 2: Never forget rule No 1 ");
            values.put(authorname, "--Warren Buffett");
            db.insert("Quotes", null, values);

            values.put(quotes, "Wealth is the ability to fully experience life.");
            values.put(authorname, "--Henry David Thoreau");
            db.insert("Quotes", null, values);

            values.put(quotes, "When I had money everyone called me brother.");
            values.put(authorname, "--Polish proverb");
            db.insert("Quotes", null, values);

            values.put(quotes, "Know what you own, and know why you own it.");
            values.put(authorname, "--Plato");
            db.insert("Quotes", null, values);

            values.put(quotes, "Money often costs too much.");
            values.put(authorname, "--Ralph Waldo Emerson");
            db.insert("Quotes", null, values);


            values.put(quotes, "An investment in knowledge pays the best interest");
            values.put(authorname, "--Benjamin Franklin");
            db.insert("Quotes", null, values);

            values.put(quotes, "You try to be greedy when others are fearful. And you try to be fearful when others are greedy.");
            values.put(authorname, "--Warren Buffett");
            db.insert("Quotes", null, values);
//23
            values.put(quotes, "Believe you can and you’re halfway there.");
            values.put(authorname, "--Theodore Roosevelt");
            db.insert("Quotes", null, values);

            values.put(quotes, "When buying shares, ask yourself, would you buy the whole company?");
            values.put(authorname, "--Rene Rivkin");
            db.insert("Quotes", null, values);

            values.put(quotes, "Fortune sides with him who dares. ");
            values.put(authorname, "--Virgil");
            db.insert("Quotes", null, values);
//26
            values.put(quotes, "Every time you borrow money, you're robbing your future self.");
            values.put(authorname, "--N. W. Morris");
            db.insert("Quotes", null, values);

            values.put(quotes, "Never spend your money before you have it.");
            values.put(authorname, "--T. Jefferson");
            db.insert("Quotes", null, values);

            values.put(quotes, "Wealth is not his that has it, but his that enjoys it.");
            values.put(authorname, "--Benjamin Franklin");
            db.insert("Quotes", null, values);

            values.put(quotes, "Don't tell me what you value, show me your budget, and I'll tell you what you value.");
            values.put(authorname, "--Joe Biden");
            db.insert("Quotes", null, values);

            values.put(quotes, "If you live for having it all, what you have is never enough.");
            values.put(authorname, "--Vicki Robin");
            db.insert("Quotes", null, values);

            System.out.println("After Insert hua re chutyo");

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
}
