package com.iruch.practice.javacore.practice5;

import com.iruch.practice.javacore.practice5.exception.AccountLimitException;
import com.iruch.practice.javacore.practice5.exception.InsufficientFundsException;
import com.iruch.practice.javacore.practice5.exception.TransactionAmountIsInvalid;

import java.util.UUID;

public final class BankAccount {

    private static final long DEFAULT_TOTAL_LIMIT = 5000;
    private static final long DEFAULT_OVERDRAFT_LIMIT = 5000;

    private final UUID account = UUID.randomUUID();
    private final BankAccountType type;
    private final Currency currency;
    private long balance;
    private long totalLimit;
    private long overdraftLimit;

    public BankAccount(Currency currency) {
        this(currency, BankAccountType.STANDARD, 0);
    }

    public BankAccount(Currency currency, BankAccountType type, long balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Баланс не должен быть меньше 0");
        }
        if (currency == null) {
            throw new NullPointerException("Валюта должна быть указана");
        }
        this.type = type == null ? BankAccountType.STANDARD : type;
        this.currency = currency;
        this.balance = balance;
        this.totalLimit = DEFAULT_TOTAL_LIMIT;
        this.overdraftLimit = type == BankAccountType.OVERDRAFT ? DEFAULT_OVERDRAFT_LIMIT : 0;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public UUID getAccount() {
        return account;
    }

    public synchronized void deposit(long amount) {
        if (amount <= 0) {
            throw new TransactionAmountIsInvalid("Нельзя внести отрицательную или 0 сумму пополнения");
        }
        if (balance + amount > totalLimit) {
            throw new AccountLimitException("Превышен размер допустимого лимита баланса банковского счета");
        }
        this.balance += amount;
    }

    public synchronized void withdraw(long amount) {
        if (amount <= 0) {
            throw new TransactionAmountIsInvalid("Нельзя списать отрицательную или нулевую сумму");
        }
        if (amount > balance) {
            if (type == BankAccountType.STANDARD) {
                throw new InsufficientFundsException("Сумма списания больше текущего баланса");
            }
            long availableBalance = balance - amount;
            if (availableBalance < -overdraftLimit) {
                throw new InsufficientFundsException("Сумма списания превышает допустимый овердрафт");
            }
        }
        this.balance -= amount;
    }

    public synchronized void changeTotalLimit(long limit) {
        if (limit <= 0) {
            throw new AccountLimitException("Новый лимит аккаунта должен быть больше 0");
        }
        this.totalLimit = limit;
    }

    public synchronized void changeOverdraftLimit(long limit) {
        if (limit <= 0) {
            throw new AccountLimitException("Новый лимит овердрафта аккаунта должен быть больше 0");
        }
        if (type == BankAccountType.STANDARD) {
            throw new AccountLimitException("Нельзя изменить овердрафт у счета без доступного овердрафта");
        }
        this.overdraftLimit = limit;
    }

    public synchronized boolean canWithdraw(long amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            if (type == BankAccountType.STANDARD) {
                return false;
            }
            long availableBalance = balance - amount;
            return availableBalance >= -overdraftLimit;
        }
        return true;
    }

    public synchronized boolean canDeposit(long amount) {
        if (amount <= 0) {
            return false;
        }
        return balance + amount <= totalLimit;
    }

}
