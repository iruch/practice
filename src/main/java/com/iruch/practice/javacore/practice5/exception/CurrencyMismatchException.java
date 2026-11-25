package com.iruch.practice.javacore.practice5.exception;

public class CurrencyMismatchException extends BankException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}
