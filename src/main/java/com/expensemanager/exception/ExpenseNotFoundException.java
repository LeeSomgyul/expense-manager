package com.expensemanager.exception;

//⭐ 조회하려는 Expense(지출)를 찾지 못했을 때 발생시키는 예외
public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(String message){
        super(message);
    }
}
