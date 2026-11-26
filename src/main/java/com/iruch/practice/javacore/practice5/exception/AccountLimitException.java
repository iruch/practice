package com.iruch.practice.javacore.practice5.exception;

public class AccountLimitException extends BankException {
    public AccountLimitException(String message) {
        super(message);
    }
}
