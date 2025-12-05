package com.expensemanager.repository;

import com.expensemanager.domain.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryExpenseRepository implements ExpenseRepository{

    /**Expense 객체를 여러 개 담을 수 있는 리스트(DB 대신 임시방편)**/
    private final List<Expense> storage = new ArrayList<>();

    @Override
    public void save(Expense expense){
        storage.add(expense);
    }

    @Override
    public List<Expense> findAll(){
        return new ArrayList<>(storage);
    }

    @Override
    public Optional<Expense> findById(long id){
        return storage.stream() //storage 리스트를 돌면서(stream)
                .filter(expense -> expense.getId() == id) //DB와 저장된 id와 찾으려는 id가 같은 경우의 Expense를 찾아서
                .findFirst(); //첫번째 값 반환
    }

    @Override
    public List<Expense> findByMonth(int year, int month){
        return storage.stream()
                //getDate()는 Expense.java에서 구현
                .filter(expense -> expense.getDate().getYear() == year && expense.getDate().getMonthValue() == month)
                .toList();
    }

    @Override
    public void deleteById(long id){
        storage.removeIf(expense -> expense.getId() == id);
    }
}
