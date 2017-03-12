package com.expensemaster.Bean;

import java.util.Date;

/**
 * Created by Aditya on 1/7/2017.
 */
public class Expense {

    private int id;
    private String type;
    private String category;
    private Float amount;
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Date date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemarks() { return remarks; }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
