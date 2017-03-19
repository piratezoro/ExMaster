package com.expensemaster.Bean;

/**
 * Created by Aditya on 3/18/2017.
 */
public class IncomePage {
    private String currentWeekIncome;
    private String currentDayIncome;
    private String currentMonthIncome;

    public String getCurrentWeekIncome() {
        return currentWeekIncome;
    }

    public void setCurrentWeekIncome(String currentWeekIncome) {
        this.currentWeekIncome = currentWeekIncome;
    }

    public String getCurrentMonthIncome() {
        return currentMonthIncome;
    }

    public void setCurrentMonthIncome(String currentMonthIncome) {
        this.currentMonthIncome = currentMonthIncome;
    }

    public String getCurrentDayIncome() {
        return currentDayIncome;
    }

    public void setCurrentDayIncome(String currentDayIncome) {
        this.currentDayIncome = currentDayIncome;
    }
}
