package com.expensemaster.DAO;

import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.Expense;
import com.expensemaster.Bean.ExpensePage;
import com.expensemaster.Bean.FirstPageData;
import com.expensemaster.Bean.IncomePage;

import java.util.Date;
import java.util.List;

/**
 * Created by Aditya on 1/7/2017.
 */
public interface SQLiteDAO {

    public FirstPageData getLoadData();
    public List<Category> getCategorywiseExpense();
    public void addExpense(Expense expense);
    public List<Expense> getAllExpenses();
    public List<Expense> getDayExpenses();
    public List<Expense> getWeekExpenses();
    public List<Expense> getMonthExpenses();
    public List<Expense> getDayIncome();
    public List<Expense> getWeekIncome();
    public List<Expense> getMonthIncome();
    public List<Expense> deleteExpense(int idHidden);
    public List<String> addCategory(String cat);
    public List<String> getAllCategories();
    public List<String> deleteCategory(String cat);
    public List<Expense> searchTransactions(String from, String to);
    public List<Category> getExpense(String from, String to);
    public ExpensePage getExpenseData();
    public IncomePage getIncomeData();

}
