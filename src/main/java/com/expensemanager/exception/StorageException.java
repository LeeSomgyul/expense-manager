package com.expensemanager.exception;

//⭐ 저장소(DB, 메모리 등)에 문제가 발생했을 때 사용하는 예외
public class StorageException extends RuntimeException {
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
