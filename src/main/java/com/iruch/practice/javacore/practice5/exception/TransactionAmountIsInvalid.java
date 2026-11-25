package com.iruch.practice.javacore.practice5.exception;

public class TransactionAmountIsInvalid extends BankException {
    public TransactionAmountIsInvalid(String message) {
        super(message);
    }
}
