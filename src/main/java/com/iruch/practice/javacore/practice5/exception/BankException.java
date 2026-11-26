package com.iruch.practice.javacore.practice5.exception;

public abstract class BankException extends RuntimeException {
    protected BankException(String message) {
        super(message);
    }
}
