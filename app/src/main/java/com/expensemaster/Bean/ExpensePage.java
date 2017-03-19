package com.expensemaster.Bean;

/**
 * Created by Aditya on 3/18/2017.
 */
public class ExpensePage {
    private String currentWeekExpense;
    private String currentDayExpense;
    private String currentMonthExpense;

    public String getCurrentWeekExpense() {
        return currentWeekExpense;
    }

    public void setCurrentWeekExpense(String currentWeekExpense) {
        this.currentWeekExpense = currentWeekExpense;
    }

    public String getCurrentDayExpense() {
        return currentDayExpense;
    }

    public void setCurrentDayExpense(String currentDayExpense) {
        this.currentDayExpense = currentDayExpense;
    }

    public String getCurrentMonthExpense() {
        return currentMonthExpense;
    }

    public void setCurrentMonthExpense(String currentMonthExpense) {
        this.currentMonthExpense = currentMonthExpense;
    }
}
