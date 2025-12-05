package com.expensemanager.service;

import com.expensemanager.repository.ExpenseRepository;

public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository){
        this.repository = repository;
    }
}
