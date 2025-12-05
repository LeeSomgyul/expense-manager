//지출 (날짜, 내용, 금액, 카테고리)
package com.expensemanager.domain;

import java.time.LocalDate;

public class Expense {
    private final long id;
    private final LocalDate date;
    private final String description;
    private final int amount;
    private final Category category;

    public Expense(long id, LocalDate date, String description, int amount, Category category){
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public long getId(){
        return id;
    }

    public LocalDate getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public int getAmount(){
        return amount;
    }

    public Category getCategory(){
        return category;
    }
}
