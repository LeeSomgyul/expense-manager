package com.expensemanager.repository;

import com.expensemanager.domain.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    void save(Expense expense);
    List<Expense> findAll();
    Optional<Expense> findById(long id);
    List<Expense> findByMonth(int year, int month);
    void deleteById(long id);
}
