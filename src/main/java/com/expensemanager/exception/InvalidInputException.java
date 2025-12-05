package com.expensemanager.exception;

//⭐ 사용자가 잘못된 입력을 했을 때 발생시키는 예외
public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message){
        super(message);
    }
}
