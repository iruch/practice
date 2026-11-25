package com.iruch.practice.javacore.practice5.exception;

public class MoneyTransfersIsInvalid extends BankException {
    public MoneyTransfersIsInvalid(String message) {
        super(message);
    }
}
